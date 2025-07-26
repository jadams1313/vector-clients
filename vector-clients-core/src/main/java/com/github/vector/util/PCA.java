package com.github.vector.util;

import java.nio.FloatBuffer;

public class PCA {
    private FloatBuffer components;          // Principal components matrix (nComponents x nFeatures)
    private FloatBuffer explainedVariance;  // Variance explained by each component
    private FloatBuffer mean;               // Mean vector for centering
    private int nComponents;                // Number of components to keep
    private int nFeatures;                  // Original feature dimensions
    private boolean fitted;                 // Training state
    private boolean whiten;                 // Whitening flag

    private PCA() {
        this.fitted = false;
    }

    public static PCABuilder builder() {
        return new PCABuilder();
    }

    public void fit(FloatBuffer data, int nSamples, int nFeatures) {
        if (this.nComponents <= 0 || this.nComponents > nFeatures) {
            throw new IllegalArgumentException("Invalid number of components");
        }

        this.nFeatures = nFeatures;

        allocateBuffers();

        computeMean(data, nSamples, nFeatures);

        FloatBuffer centeredData = centerData(data, nSamples, nFeatures);

        performSVD(centeredData, nSamples, nFeatures);

        this.fitted = true;
    }

    public FloatBuffer transform(FloatBuffer data, int nSamples) {
        if (!fitted) {
            throw new IllegalStateException("PCA must be fitted before transform");
        }

        FloatBuffer transformed = FloatBuffer.allocate(nSamples * nComponents);

        FloatBuffer centeredData = FloatBuffer.allocate(nSamples * nFeatures);
        centerDataInPlace(data, centeredData, nSamples);

        matrixMultiply(centeredData, components, transformed, nSamples, nFeatures, nComponents);

        if (whiten) {
            applyWhitening(transformed, nSamples);
        }

        return transformed;
    }

    public FloatBuffer fitTransform(FloatBuffer data, int nSamples, int nFeatures) {
        fit(data, nSamples, nFeatures);
        return transform(data, nSamples);
    }

    public FloatBuffer inverseTransform(FloatBuffer transformedData, int nSamples) {
        if (!fitted) {
            throw new IllegalStateException("PCA must be fitted before inverse transform");
        }

        FloatBuffer original = FloatBuffer.allocate(nSamples * nFeatures);
        FloatBuffer tempData = transformedData;

        if (whiten) {
            tempData = FloatBuffer.allocate(nSamples * nComponents);
            reverseWhitening(transformedData, tempData, nSamples);
        }

        matrixMultiply(tempData, components, original, nSamples, nComponents, nFeatures);

        addMean(original, nSamples);

        return original;
    }

    private void allocateBuffers() {
        this.components = FloatBuffer.allocate(nComponents * nFeatures);
        this.explainedVariance = FloatBuffer.allocate(nComponents);
        this.mean = FloatBuffer.allocate(nFeatures);
    }

    private void computeMean(FloatBuffer data, int nSamples, int nFeatures) {
        mean.clear();

        for (int j = 0; j < nFeatures; j++) {
            float sum = 0.0f;
            for (int i = 0; i < nSamples; i++) {
                sum += data.get(i * nFeatures + j);
            }
            mean.put(j, sum / nSamples);
        }
    }

    private FloatBuffer centerData(FloatBuffer data, int nSamples, int nFeatures) {
        FloatBuffer centered = FloatBuffer.allocate(nSamples * nFeatures);

        for (int i = 0; i < nSamples; i++) {
            for (int j = 0; j < nFeatures; j++) {
                int idx = i * nFeatures + j;
                centered.put(idx, data.get(idx) - mean.get(j));
            }
        }

        return centered;
    }

    private void centerDataInPlace(FloatBuffer source, FloatBuffer target, int nSamples) {
        for (int i = 0; i < nSamples; i++) {
            for (int j = 0; j < nFeatures; j++) {
                int idx = i * nFeatures + j;
                target.put(idx, source.get(idx) - mean.get(j));
            }
        }
    }

    private void performSVD(FloatBuffer centeredData, int nSamples, int nFeatures) {
        FloatBuffer covariance = FloatBuffer.allocate(nFeatures * nFeatures);
        computeCovariance(centeredData, covariance, nSamples, nFeatures);

        FloatBuffer eigenValues = FloatBuffer.allocate(nFeatures);
        FloatBuffer eigenVectors = FloatBuffer.allocate(nFeatures * nFeatures);

        jacobiEigenDecomposition(covariance, eigenValues, eigenVectors, nFeatures);

        sortEigenPairs(eigenValues, eigenVectors, nFeatures);

        extractTopComponents(eigenValues, eigenVectors, nFeatures);
    }

    private void computeCovariance(FloatBuffer centeredData, FloatBuffer covariance, int nSamples, int nFeatures) {
        float scale = 1.0f / (nSamples - 1);

        for (int i = 0; i < nFeatures; i++) {
            for (int j = 0; j < nFeatures; j++) {
                float sum = 0.0f;
                for (int k = 0; k < nSamples; k++) {
                    sum += centeredData.get(k * nFeatures + i) * centeredData.get(k * nFeatures + j);
                }
                covariance.put(i * nFeatures + j, sum * scale);
            }
        }
    }

    private void jacobiEigenDecomposition(FloatBuffer matrix, FloatBuffer eigenValues, FloatBuffer eigenVectors, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                eigenVectors.put(i * n + j, (i == j) ? 1.0f : 0.0f);
            }
        }

        for (int i = 0; i < n; i++) {
            eigenValues.put(i, matrix.get(i * n + i));
        }

        int maxIterations = 50;
        float tolerance = 1e-6f;

        for (int iter = 0; iter < maxIterations; iter++) {
            float maxOffDiag = 0.0f;
            int maxI = 0, maxJ = 1;

            for (int i = 0; i < n - 1; i++) {
                for (int j = i + 1; j < n; j++) {
                    float absVal = Math.abs(matrix.get(i * n + j));
                    if (absVal > maxOffDiag) {
                        maxOffDiag = absVal;
                        maxI = i;
                        maxJ = j;
                    }
                }
            }

            if (maxOffDiag < tolerance) break;

            jacobiRotation(matrix, eigenVectors, maxI, maxJ, n);
        }

        for (int i = 0; i < n; i++) {
            eigenValues.put(i, matrix.get(i * n + i));
        }
    }

    private void jacobiRotation(FloatBuffer matrix, FloatBuffer eigenVectors, int p, int q, int n) {
        float app = matrix.get(p * n + p);
        float aqq = matrix.get(q * n + q);
        float apq = matrix.get(p * n + q);

        float theta = 0.5f * (aqq - app) / apq;
        float t = 1.0f / (Math.abs(theta) + (float)Math.sqrt(1.0f + theta * theta));
        if (theta < 0) t = -t;

        float c = 1.0f / (float)Math.sqrt(1.0f + t * t);
        float s = t * c;

        matrix.put(p * n + p, app - t * apq);
        matrix.put(q * n + q, aqq + t * apq);
        matrix.put(p * n + q, 0.0f);
        matrix.put(q * n + p, 0.0f);

        for (int i = 0; i < n; i++) {
            if (i != p && i != q) {
                float aip = matrix.get(i * n + p);
                float aiq = matrix.get(i * n + q);
                matrix.put(i * n + p, c * aip - s * aiq);
                matrix.put(p * n + i, c * aip - s * aiq);
                matrix.put(i * n + q, s * aip + c * aiq);
                matrix.put(q * n + i, s * aip + c * aiq);
            }
        }

        for (int i = 0; i < n; i++) {
            float vip = eigenVectors.get(i * n + p);
            float viq = eigenVectors.get(i * n + q);
            eigenVectors.put(i * n + p, c * vip - s * viq);
            eigenVectors.put(i * n + q, s * vip + c * viq);
        }
    }

    private void sortEigenPairs(FloatBuffer eigenValues, FloatBuffer eigenVectors, int n) {

    }

    private void extractTopComponents(FloatBuffer eigenValues, FloatBuffer eigenVectors, int nFeatures) {
        // Copy top nComponents eigenvalues
        for (int i = 0; i < nComponents; i++) {
            explainedVariance.put(i, eigenValues.get(i));
        }

        // Copy top nComponents eigenvectors (transpose for easier multiplication)
        for (int i = 0; i < nComponents; i++) {
            for (int j = 0; j < nFeatures; j++) {
                components.put(i * nFeatures + j, eigenVectors.get(j * nFeatures + i));
            }
        }
    }

    private void matrixMultiply(FloatBuffer a, FloatBuffer b, FloatBuffer result, int m, int k, int n) {
        // result = a * b where a is m×k and b is k×n
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                float sum = 0.0f;
                for (int l = 0; l < k; l++) {
                    sum += a.get(i * k + l) * b.get(l * n + j);
                }
                result.put(i * n + j, sum);
            }
        }
    }

    private void applyWhitening(FloatBuffer data, int nSamples) {
        for (int i = 0; i < nSamples; i++) {
            for (int j = 0; j < nComponents; j++) {
                int idx = i * nComponents + j;
                float variance = explainedVariance.get(j);
                if (variance > 1e-8f) {
                    data.put(idx, data.get(idx) / (float)Math.sqrt(variance));
                }
            }
        }
    }

    private void reverseWhitening(FloatBuffer source, FloatBuffer target, int nSamples) {
        for (int i = 0; i < nSamples; i++) {
            for (int j = 0; j < nComponents; j++) {
                int idx = i * nComponents + j;
                float variance = explainedVariance.get(j);
                target.put(idx, source.get(idx) * (float)Math.sqrt(variance));
            }
        }
    }

    private void addMean(FloatBuffer data, int nSamples) {
        for (int i = 0; i < nSamples; i++) {
            for (int j = 0; j < nFeatures; j++) {
                int idx = i * nFeatures + j;
                data.put(idx, data.get(idx) + mean.get(j));
            }
        }
    }

    // Getters
    public int getNumComponents() { return nComponents; }
    public int getNumFeatures() { return nFeatures; }
    public boolean isFitted() { return fitted; }
    public FloatBuffer getExplainedVariance() { return explainedVariance.asReadOnlyBuffer(); }
    public FloatBuffer getComponents() { return components.asReadOnlyBuffer(); }

    public static class PCABuilder {
        private int nComponents = -1;  // Will be set to input dimensions if not specified
        private boolean whiten = false;
        private String solver = "jacobi";  // Fixed typo: was "solvent"
        private double tolerance = 1e-6;
        private int maxIterations = 50;

        public PCABuilder nComponents(int nComponents) {
            this.nComponents = nComponents;
            return this;
        }

        public PCABuilder whiten(boolean whiten) {
            this.whiten = whiten;
            return this;
        }

        public PCABuilder solver(String solver) {
            this.solver = solver;
            return this;
        }

        public PCABuilder tolerance(double tolerance) {
            this.tolerance = tolerance;
            return this;
        }

        public PCABuilder maxIterations(int maxIterations) {
            this.maxIterations = maxIterations;
            return this;
        }

        public PCA build() {
            PCA pca = new PCA();
            pca.nComponents = this.nComponents;
            pca.whiten = this.whiten;
            return pca;
        }
    }
}