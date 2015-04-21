package com.github.itoshige.testrail.store;

import java.util.concurrent.ConcurrentHashMap;

import org.json.simple.JSONArray;

import com.github.itoshige.testrail.client.TestRailUnitException;
import com.github.itoshige.testrail.util.CollectionUtil;
import com.github.itoshige.testrail.util.JSONUtil;

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

    static TestStore getIns() {
        return instance;
    }

    public String getTestId(String caseId) {
        String testId = testsMap.get(caseId);
        if (testId != null)
            return testId;
        throw new TestRailUnitException(new StringBuilder("testId isn't in testrail. caseId:").append(caseId)
            .toString());
    }

    public void setTestsMap(JSONArray tests, String runId) {
        JSONUtil.copyJsonArrayToMap(tests, testsMap, "case_id", "id");
    }
}
