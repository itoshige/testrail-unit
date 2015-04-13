package org.itoshige.testrail.cache;

import java.util.concurrent.ConcurrentHashMap;

import org.itoshige.testrail.client.TestInitializerException;
import org.itoshige.testrail.client.TestRailClient;
import org.itoshige.testrail.client.TestRailUnitException;
import org.itoshige.testrail.util.CollectionUtil;
import org.itoshige.testrail.util.JSONUtil;
import org.json.simple.JSONArray;

/**
 * TestRail testCase data
 * 
 * @author itoshige
 * 
 */
public class TestCache {
    private static final TestCache instance = new TestCache();

    // caseId to testId
    private final ConcurrentHashMap<String, String> testsMap = CollectionUtil.newConcurrentMap();

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
        JSONUtil.convertJsonArrayToMap(tests, testsMap, "case_id", "id");
        if (testsMap == null || testsMap.isEmpty())
            throw new TestInitializerException("section data isn't in testrail. runId:" + runId);
    }
}
