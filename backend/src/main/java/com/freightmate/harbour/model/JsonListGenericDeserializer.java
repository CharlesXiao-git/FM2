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
        /*
         * TODO: Error initialising cls when creating an object that has Carrier object in it as a class variable
         *  The error appears when performing a create endpoint and caused the app to throw HttpStatus.UNSUPPORTED_MEDIA_TYPE
         *  Workaround is to set the cls if the type.getActualTypeArguments is not a type of [java.util.List<T>]
         */
        Class<T> cls1 = null;
        final ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        if (!type.getActualTypeArguments()[0].getTypeName().equals("java.util.List<T>")) {
            cls1 = (Class<T>) type.getActualTypeArguments()[0];
        }
        this.cls = cls1;
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
