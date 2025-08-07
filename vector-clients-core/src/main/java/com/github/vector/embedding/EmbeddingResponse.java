package com.github.vector.embedding;

import com.github.vector.data.Metadata;
import com.github.vector.data.Text;

import java.nio.FloatBuffer;
import java.time.Instant;

public class EmbeddingResponse {
    private final Text text;
    private final String requestId;
    private final Metadata metadata;
    private final String collection;
    private Instant timeCreated;
    private FloatBuffer embedding;
    private EmbeddingResponse(final Text text, final String requestId, final Metadata metadata, final String collection, final float[][] embedding) {
        this.text = text;
        this.requestId = requestId;
        this.metadata = metadata;
        this.collection = collection;
        this.timeCreated = Instant.now();
        this.embedding = FloatBuffer.allocate(embedding.length);

    }

    public Text getText() {
        return text;
    }

    public String getRequestId() {
        return requestId;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public String getCollection() {
        return collection;
    }

    public FloatBuffer getEmbedding() {
        return embedding;
    }
}
