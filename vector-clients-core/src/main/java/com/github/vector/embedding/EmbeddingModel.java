package com.github.vector.embedding;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface EmbeddingModel {

    /**
     * Generate embedding for a single text input.
     * @param request The embedding request containing text and configuration
     * @return CompletableFuture containing the embedding response with Embedding object
     */
    CompletableFuture<EmbeddingResponse> embed(EmbeddingRequest request);

    /**
     * Generate embeddings for multiple text inputs in a batch.
     * @param requests List of embedding requests
     * @return CompletableFuture containing list of embedding responses
     */
    CompletableFuture<List<EmbeddingResponse>> embedBatch(List<EmbeddingRequest> requests);

    /**
     * Get the dimensionality of embeddings produced by this model.
     * @return The number of dimensions in output vectors
     */
    int getDimensions();

    /**
     * Get the maximum number of tokens this model can process.
     * @return Maximum token count, -1 if unlimited
     */
    int getMaxTokens();

    /**
     * Get the model identifier/name.
     * @return String identifier for this model
     */
    String getModelName();

    /**
     * Get the maximum batch size supported by this model.
     * @return Maximum number of texts that can be processed in one batch
     */
    int getMaxBatchSize();
}
