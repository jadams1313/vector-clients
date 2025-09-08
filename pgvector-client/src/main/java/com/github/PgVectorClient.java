package com.github;


import com.github.vector.client.Client;
import com.github.vector.data.Embedding;

import java.util.List;

public class PgVectorClient implements Client {
    @Override
    public String getPort() {
        return "";
    }

    @Override
    public String getJDBC() {
        return "";
    }

    @Override
    public String getAPIKey() {
        return "";
    }

    @Override
    public boolean getTimeout() {
        return false;
    }

    @Override
    public void openCollection(String s) {

    }

    @Override
    public void batchEmbeddings(List<Embedding> list) {

    }

    @Override
    public void store(Embedding embedding) {

    }

    @Override
    public boolean closeConnection() {
        return false;
    }
}
