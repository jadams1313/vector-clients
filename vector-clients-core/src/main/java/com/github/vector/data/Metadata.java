package com.github.vector.data;
import java.util.Map;

public class Metadata {

    private Map<String, Object> metadata;

    public Metadata(final Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public Map<String, Object> getMetadata() {
        return this.metadata;
    }

    public void setMetadata(final Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    //validates if we have the metadata we want
    public boolean validateMetaData() {

    }
}
