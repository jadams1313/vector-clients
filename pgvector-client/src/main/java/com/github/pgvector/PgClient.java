package com.github.pgvector;


import com.github.vector.Config;
import com.github.vector.client.Client;
import com.github.vector.data.Embedding;
import com.github.vector.exception.VectorClientException;

import java.util.List;

public class PgClient implements Client {
    @Override
    public void connect(Config config) throws VectorClientException {

    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void disconnect() {

    }

    @Override
    public void createCollection(String s) throws VectorClientException {

    }

    @Override
    public void store(Embedding embedding) throws VectorClientException {

    }

    @Override
    public void batchStore(List<Embedding> list) throws VectorClientException {

    }

    @Override
    public List<Embedding> search(Embedding embedding, int i) throws VectorClientException {
        return List.of();
    }

    @Override
    public void delete(String s) throws VectorClientException {

    }
}
