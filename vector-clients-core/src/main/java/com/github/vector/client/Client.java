package com.github.vector.client;

import com.github.vector.Config;
import com.github.vector.data.Embedding;
import com.github.vector.exception.VectorClientException;

import java.util.List;

public interface Client {

    // === Connection Management ===
    void connect(Config config) throws VectorClientException;
    boolean isConnected();
    void disconnect();

    // === Core CRUD Operations ===
    void createCollection(String collectionName) throws VectorClientException;

    void store(Embedding embedding) throws VectorClientException;
    void batchStore(List<Embedding> embeddings) throws VectorClientException;

    List<Embedding> search(Embedding embedding, int topK) throws VectorClientException;

    void delete(String embeddingId) throws VectorClientException;


}
