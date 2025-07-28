package com.github.vector.data;
import com.sun.jdi.Value;

import java.util.Collection;
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

    public void putAll(final Map<String, Object> map) {this.metadata.putAll(map);}

    public void removeAll() {this.metadata.clear();}

    public boolean validateMetaData(Map<String, Object> map) {
        if(map == null || metadata == null) {throw new IllegalArgumentException("Error in Validating metadata. Map is null");}
        if(map.size() != metadata.size()) {return false;}
        Collection<String> currentKeys = metadata.keySet();
        Collection<Map.Entry<String, Object>> currentValues = metadata.entrySet();
        Collection<String> keys = map.keySet();
        Collection<Map.Entry<String, Object>> values = map.entrySet();

        boolean allKeysMatch = keys.containsAll(currentKeys);
        boolean allValuesMatch = values.containsAll(currentValues);

        return allKeysMatch && allValuesMatch;
    }
}
