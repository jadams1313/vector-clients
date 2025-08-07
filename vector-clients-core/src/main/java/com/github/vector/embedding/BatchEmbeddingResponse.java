package com.github.vector.embedding;

import com.github.vector.data.Metadata;
import com.github.vector.data.Text;

import java.nio.FloatBuffer;
import java.time.Instant;
import java.util.List;

public class BatchEmbeddingResponse {
    final List<Text> texts;
    final List<FloatBuffer> embeddings;
    final List<Metadata> metadatas;
    final Instant timeCreated;
    final List<String> collections;

    public BatchEmbeddingResponse(final List<Text> texts, final List<FloatBuffer> embeddings, final List<Metadata> metadatas, final List<String> collections) {
        this.texts = texts;
        this.embeddings = embeddings;
        this.metadatas = metadatas;
        this.collections = collections;
        this.timeCreated = Instant.now();
    }

    public List<Text> getTexts() {
        return texts;
    }
    public List<FloatBuffer> getEmbeddings() {
        return embeddings;
    }
    public List<Metadata> getMetadatas() {
        return metadatas;
    }
    public Instant getTimeCreated() {
        return timeCreated;
    }

}
