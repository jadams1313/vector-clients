package com.github.vector.embedding;

import com.github.vector.data.Embedding;
import com.github.vector.data.Metadata;
import com.github.vector.data.Text;

public class EmbeddingRequest {
    private final Text text;
    private final String requestId;
    private final Metadata metadata;
    private final String collection;
    private EmbeddingRequest(Builder builder) {
        this.text = builder.text;
        this.requestId = builder.requestId;
        this.metadata = builder.metadata;
        this.collection = builder.collection;
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

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {
        private Text text;
        private String requestId;
        private Metadata metadata;
        private String collection;

        public Builder text(Text text) {
            this.text = text;
            return this;
        }

        public Builder requestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        public Builder metadata(Metadata metadata) {
            this.metadata = metadata;
            return this;
        }

        public Builder collection(String collection) {
            this.collection = collection;
            return this;
        }

        public EmbeddingRequest build() {
            if (text == null) {
                throw new IllegalArgumentException("Text cannot be null");
            }
            return new EmbeddingRequest(this);
        }
    }

    @Override
    public String toString() {
        return "EmbeddingRequest{" +
                "text=" + text +
                ", requestId='" + requestId + '\'' +
                ", metadata=" + metadata +
                ", collection='" + collection + '\'' +
                '}';
    }
}
