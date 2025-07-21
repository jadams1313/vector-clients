package com.github.vector.data;

public class Embedding {
    private float[] embedding;
    private Metadata metadata;
    private String collection;

    public Embedding(final float[] embedding, final Text context, final Metadata metadata, final String collection) {
        this.embedding = embedding;
        this.metadata = metadata;
        this.collection = collection;
    }

    public float[] getEmbedding() {
        return embedding;
    }

    public void setEmbedding(float[] embedding) {
        this.embedding = embedding;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }


    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }
}
