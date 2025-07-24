package com.github.vector.util;

public class PCA {
    private int components;
    private int variance;


    public PCABuilder builder() {
        return new PCABuilder();
    }

    public static class PCABuilder {

        public PCA build() {
            return new PCA();
        }

    }
}
