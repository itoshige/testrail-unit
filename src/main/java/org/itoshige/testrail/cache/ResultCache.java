package org.itoshige.testrail.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.itoshige.testrail.client.Pair;
import org.itoshige.testrail.model.TestResult;
import org.itoshige.testrail.util.CollectionUtil;

/**
 * JuitTest result data
 * 
 * @author itoshige
 * 
 */
public class ResultCache {
    private static final ResultCache instance = new ResultCache();

    // runId, class to testresults
    private ConcurrentHashMap<Pair<String, Class<?>>, CopyOnWriteArrayList<TestResult>> resultsMap = CollectionUtil
        .newConcurrentMap();

    public static ResultCache getIns() {
        return instance;
    }

    public void setResult(Pair<String, Class<?>> runId2Class, TestResult result) {
        CopyOnWriteArrayList<TestResult> testResults = resultsMap.get(runId2Class);
        if (testResults == null || testResults.isEmpty()) {
            CopyOnWriteArrayList<TestResult> list = new CopyOnWriteArrayList<TestResult>();
            list.add(result);
            resultsMap.putIfAbsent(runId2Class, list);
        } else if (!testResults.contains(result)) {
            testResults.add(result);
        }
    }

    public Map<String, List<Map<String, Object>>> getResults(Pair<String, Class<?>> runId2Class) {

        CopyOnWriteArrayList<TestResult> testResults = resultsMap.get(runId2Class);
        Map<String, List<Map<String, Object>>> data = new HashMap<String, List<Map<String, Object>>>();
        data.put("results", new ArrayList<Map<String, Object>>());

        if (testResults == null || testResults.isEmpty())
            return data;

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (TestResult result : testResults) {
            list.add(result.getResult());
        }
        data.put("results", list);

        return data;
    }

    public void clearResults(Pair<String, Class<?>> runId2Class) {
        CopyOnWriteArrayList<TestResult> testResults = resultsMap.get(runId2Class);
        testResults.clear();
    }
}
