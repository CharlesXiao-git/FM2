package com.freightmate.harbour.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class JsonListGenericDeserializer<T> extends JsonDeserializer<List<T>> {

    private final Class<T> cls;

    public JsonListGenericDeserializer() {
        final ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.cls = (Class<T>) type.getActualTypeArguments()[0];
    }

    @Override
    public List<T> deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
        final ObjectCodec objectCodec = p.getCodec();
        final JsonNode arrayNode = objectCodec.readTree(p);
        final List<T> result = new ArrayList<T>();

        for (JsonNode node : arrayNode) {
            result.add(objectCodec.treeToValue(node, cls));
        }

        return result;
    }
}
