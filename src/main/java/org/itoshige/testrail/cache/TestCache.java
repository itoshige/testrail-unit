package org.itoshige.testrail.cache;

import java.util.HashMap;
import java.util.Map;

import org.itoshige.testrail.client.TestInitializerException;
import org.itoshige.testrail.client.TestRailClient;
import org.itoshige.testrail.client.TestRailUnitException;
import org.itoshige.testrail.util.CollectionUtil;
import org.itoshige.testrail.util.JSONUtil;
import org.json.simple.JSONArray;

public class TestCache {
    private static final TestCache instance = new TestCache();

    // caseId to testId
    private final HashMap<String, String> testsMap = CollectionUtil.newHashMap();

    public static TestCache getIns() {
        return instance;
    }

    public String getTestId(String caseId) {
        String testId = testsMap.get(caseId);
        if (testId != null)
            return testId;
        throw new TestRailUnitException("sectionId isn't in testrail. caseId:" + caseId);
    }

    public void setTestsMap(String runId) {
        JSONArray tests = TestRailClient.getTests(runId);
        Map<String, String> map = JSONUtil.convertJsonArrayToMap(tests, "case_id", "id");
        if (map == null || map.isEmpty())
            throw new TestInitializerException("section data isn't in testrail. runId:" + runId);

        testsMap.putAll(map);
    }
}
