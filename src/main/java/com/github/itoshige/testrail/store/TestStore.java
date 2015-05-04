/**
 * Copyright 2015 itoshige1017@gmail.com
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package com.github.itoshige.testrail.store;

import java.util.concurrent.ConcurrentHashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.github.itoshige.testrail.client.TestRailUnitException;
import com.github.itoshige.testrail.util.CollectionUtil;

/**
 * TestRail testCase data
 * 
 * @author itoshige
 * 
 */
public class TestStore {
    private static final TestStore instance = new TestStore();

    // caseId to testId
    private final ConcurrentHashMap<String, String> testsMap = CollectionUtil.newConcurrentMap();

    private TestStore() {
    }

    static TestStore getIns() {
        return instance;
    }

    public String getTestId(String caseId) {
        String testId = testsMap.get(caseId);
        if (testId != null)
            return testId;
        throw new TestRailUnitException(String.format("testId isn't in testrail. caseId:%s", caseId));
    }

    public void setTestsMap(JSONArray tests, String runId) {
        copyJsonArrayToMap(tests, testsMap, "case_id", "id");
    }

    private void copyJsonObjToMap(JSONObject from, ConcurrentHashMap<String, String> to, String key,
        String value) {
        Object k = from.get(key);
        Object v = from.get(value);
        if (k != null && v != null) {
            to.putIfAbsent(k.toString().trim(), v.toString().trim());
        }
    }

    private void copyJsonArrayToMap(JSONArray from, ConcurrentHashMap<String, String> to, String key,
        String value) {
        for (int i = 0; i < from.size(); i++) {
            JSONObject obj = (JSONObject) from.get(i);
            copyJsonObjToMap(obj, to, key, value);
        }
    }
}
