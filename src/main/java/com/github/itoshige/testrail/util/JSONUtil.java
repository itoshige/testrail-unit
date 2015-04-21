package com.github.itoshige.testrail.util;

import java.util.concurrent.ConcurrentHashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.itoshige.testrail.client.Pair;

/**
 * JSON util
 * 
 * @author itoshige
 * 
 */
public class JSONUtil {
    private static final Logger logger = LoggerFactory.getLogger(JSONUtil.class);

    public static void copyJsonObjToMap(JSONObject from, ConcurrentHashMap<Pair<String, String>, String> to,
        String key1Value, String key2, String value) {
        Object k2 = from.get(key2);
        Object v = from.get(value);
        if (key1Value != null && k2 != null && v != null) {
            to.putIfAbsent(new Pair<String, String>(key1Value, k2.toString().trim()), v.toString().trim());
            logger.debug("key1:{} key2:{} value:{}", new Object[] { key1Value, k2.toString().trim(),
                v.toString().trim() });
        }
    }

    public static void copyJsonObjToMap(JSONObject from, ConcurrentHashMap<String, String> to, String key,
        String value) {
        Object k = from.get(key);
        Object v = from.get(value);
        if (k != null && v != null) {
            to.putIfAbsent(k.toString().trim(), v.toString().trim());
        }
    }

    public static void copyJsonArrayToMap(JSONArray from, ConcurrentHashMap<String, String> to, String key,
        String value) {
        for (int i = 0; i < from.size(); i++) {
            JSONObject obj = (JSONObject) from.get(i);
            copyJsonObjToMap(obj, to, key, value);
        }
    }

    public static void copyJsonArrayToMultiKeyMap(JSONArray from,
        ConcurrentHashMap<Pair<String, String>, String> to, String key1, String key2, String value) {
        for (int i = 0; i < from.size(); i++) {
            JSONObject obj = (JSONObject) from.get(i);
            Object k1 = obj.get(key1);
            Object k2 = obj.get(key2);
            Object v = obj.get(value);
            if (k1 != null && k2 != null && v != null) {
                to.putIfAbsent(new Pair<String, String>(k1.toString().trim(), k2.toString().trim()), v
                    .toString().trim());
            }
        }
    }

    public static void copyJsonArrayToMultiKeyMap2(JSONArray from,
        ConcurrentHashMap<Pair<String, String>, String> to, String key1Value, String key2, String value) {
        for (int i = 0; i < from.size(); i++) {
            JSONObject obj = (JSONObject) from.get(i);
            Object k2 = obj.get(key2);
            Object v = obj.get(value);
            if (key1Value != null && k2 != null && v != null) {
                to.putIfAbsent(new Pair<String, String>(key1Value, k2.toString().trim()), v.toString().trim());
            }
        }
    }
}
