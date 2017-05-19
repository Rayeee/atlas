package com.eparty.ccp.core.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

public class JsonUtils {

    public static final Gson gson = new Gson();
    public static ObjectMapper objectMapper = new ObjectMapper();


    /**
     * obj转json
     * @param o
     * @return
     */
    public static String toJson(Object o) {
        return gson.toJson(o);
    }


    /**
     * json转obj
     * @param json
     * @param classOfT
     * @return
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

    /**
     * Json转JsonNode
     *
     * @param detailJson
     * @return
     * @throws IOException
     */
    public static JsonNode getJsonNode(String detailJson) throws IOException {
        return objectMapper.readTree(detailJson);
    }

}
