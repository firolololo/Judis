package com.stellar.judis.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stellar.judis.meta.ElementHandle;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;

/**
 * @author firo
 * @version 1.0
 * @date 2021/1/28 14:31
 */
public class JsonUtil {
    private static ObjectMapper mapper = new ObjectMapper();
    public static <T> T stringToJson(String data, Class<T> clazz) {
        try {
            T t = mapper.readValue(data, clazz);
            return t;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String jsonToString(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
