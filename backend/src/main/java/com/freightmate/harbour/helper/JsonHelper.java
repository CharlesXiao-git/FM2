package com.freightmate.harbour.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonHelper {

    private JsonHelper() {
        throw new IllegalStateException("Helper class shouldn't be constructed");
    }

    public static List<String> arrayNodeToList(ArrayNode node) {
        List<String> values = new ArrayList<>();
        Iterator<JsonNode> elements = node.elements();
        while(elements.hasNext()){
            values.add(elements.next().asText());
        }
        return values;
    }
}
