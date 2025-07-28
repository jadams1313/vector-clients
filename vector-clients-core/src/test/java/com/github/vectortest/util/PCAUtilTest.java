package com.github.vectortest.util;

import com.github.vector.util.PCA;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.FloatBuffer;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class PCAUtilTest {


    private static PCA pca;

    private float[] input;



    @BeforeAll
    public static void setUp() {
        pca = PCA.builder().build();
    }


    @Nested
    @DisplayName("Data Validation Tests")
    class DataValidation {
        @Test
        @DisplayName("Should throw exception for null data and insufficient dimension(n is not >=d.")
        void shouldThrowExceptionForNullData() {
            int nSamplesOne = 0;
            int nSamplesTwo = 10;
            int nFeatures = 2;
            float[] rawData = new float[10];
            FloatBuffer data = FloatBuffer.wrap(rawData);
            assertThrows(IllegalArgumentException.class, () -> pca.fit(data, nSamplesOne, nFeatures));
            assertThrows(IllegalArgumentException.class, () -> pca.transform(data, nSamplesTwo));
        }

        @Test
        @DisplayName("Should throw exception for empty data")
        void shouldThrowExceptionForEmptyData() {
            FloatBuffer emptyData = null;
            int nSamples = 10;
            int nFeatures = 2;
            assertThrows(IllegalArgumentException.class, () -> pca.fit(null, nSamples, nFeatures));
        }

        @Test
        @DisplayName("Should throw exception for inconsistent vector dimensions")
        void shouldThrowExceptionForInconsistentDimensions() {
            int nSamples = 10;
            int nFeatures = 5;
            float[] rawData = new float[20]; // Should be 50 dimensions (10 * 5)
            FloatBuffer data = FloatBuffer.wrap(rawData);
            assertThrows(IllegalArgumentException.class, () -> pca.fit(data, nSamples, nFeatures));
        }

    }

    @Nested
    @DisplayName("Fit and Transform Tests")
    class FitAndTransform {

    }

}
