package org.itoshige.testrail.util;

import java.util.concurrent.ConcurrentHashMap;

import org.itoshige.testrail.client.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * JSON util
 * 
 * @author itoshige
 * 
 */
public class JSONUtil {

    public static void convertJsonArrayToMap(JSONArray from, ConcurrentHashMap<String, String> to,
        String key, String value) {
        for (int i = 0; i < from.size(); i++) {
            JSONObject obj = (JSONObject) from.get(i);
            Object k = obj.get(key);
            Object v = obj.get(value);
            if (k != null && v != null) {
                to.putIfAbsent(k.toString().trim(), v.toString().trim());
            }
        }
    }

    public static void convertJsonArrayToMultiKeyMap(JSONArray from,
        ConcurrentHashMap<Pair<String, String>, String> to, String key1, String key2, String value) {
        for (int i = 0; i < from.size(); i++) {
            JSONObject obj = (JSONObject) from.get(i);
            Object k1 = obj.get(key1);
            Object k2 = obj.get(key2);
            Object v = obj.get(value);
            if (k1 != null && k2 != null && v != null)
                to.putIfAbsent(new Pair<String, String>(k1.toString().trim(), k2.toString().trim()), v
                    .toString().trim());
        }
    }
}
