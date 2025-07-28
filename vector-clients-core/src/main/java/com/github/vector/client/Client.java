package com.github.vector.client;

import com.github.vector.data.Embedding;

import java.util.List;

public interface Client {

    String getPort();

    String getJDBC();

    String getAPIKey();

    boolean getTimeout();

    void openCollection(final String name);

    void batchEmbeddings(List<Embedding> embeddingList);

    void store(final Embedding embedding);

    boolean closeConnection();



}
