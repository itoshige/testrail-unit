package org.itoshige.testrail.util;

import org.itoshige.testrail.client.TestRailUnitException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * JSON util
 * 
 * @author itoshige
 * 
 */
public class JSONUtils {
    public interface Predicate {
        boolean apply(JSONObject jsonObj);
    }

    public static JSONObject getJsonObject(JSONArray array, Predicate predicate) {
        for (int i = 0; i < array.size(); i++) {
            JSONObject jsonObj = (JSONObject) array.get(i);
            if (predicate.apply(jsonObj))
                return jsonObj;
        }
        throw new TestRailUnitException("not found JSONArray:" + array.toJSONString());
    }
}
