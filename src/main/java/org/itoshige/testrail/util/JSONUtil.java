package org.itoshige.testrail.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.MultiKeyMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * JSON util
 * 
 * @author itoshige
 * 
 */
public class JSONUtil {

    public static Map<String, String> convertJsonArrayToMap(JSONArray array, String key, String value) {
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = (JSONObject) array.get(i);
            Object k = obj.get(key);
            Object v = obj.get(value);
            if (k != null && v != null)
                map.put(k.toString().trim(), v.toString().trim());
        }
        return map;
    }

    public static MultiKeyMap convertJsonArrayToMultiKeyMap(JSONArray array, String key1, String key2,
        String value) {
        MultiKeyMap map = new MultiKeyMap();
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = (JSONObject) array.get(i);
            Object k1 = obj.get(key1);
            Object k2 = obj.get(key2);
            Object v = obj.get(value);
            if (k1 != null && k2 != null && v != null)
                map.put(k1.toString().trim(), k2.toString().trim(), v.toString().trim());
        }
        return map;
    }
}
