package com.github.itoshige.testrail.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.itoshige.testrail.client.Pair;
import com.github.itoshige.testrail.client.TestRailUnitException;
import com.github.itoshige.testrail.model.TestResultModel;
import com.github.itoshige.testrail.util.CollectionUtil;

/**
 * JuitTest result data
 * 
 * @author itoshige
 * 
 */
public class TestResultStore {
    private static final Logger logger = LoggerFactory.getLogger(TestResultStore.class);

    private static final TestResultStore instance = new TestResultStore();

    // runId, class to testresults
    private ConcurrentHashMap<Pair<String, Class<?>>, List<TestResultModel>> resultsMap = CollectionUtil
        .newConcurrentMap();

    static TestResultStore getIns() {
        return instance;
    }

    public void setResult(Pair<String, Class<?>> runId2Class, TestResultModel result) {
        List<TestResultModel> testResults = resultsMap.get(runId2Class);
        List<TestResultModel> list = Collections.synchronizedList(new ArrayList<TestResultModel>());
        if (isEmplyResult(testResults)) {
            list.add(result);
            resultsMap.putIfAbsent(runId2Class, list);
        } else if (!testResults.contains(result)) {
            testResults.add(result);
        } else {
            logger.error("execute same test. runId:{} testId:{}", runId2Class.getFirst(), result.getTestId());
            throw new TestRailUnitException(new StringBuilder("execute same test. runId:")
                .append(runId2Class.getFirst()).append("testId:").append(result.getTestId()).toString());
        }
    }

    private boolean isEmplyResult(List<TestResultModel> testResults) {
        return testResults == null || testResults.isEmpty();
    }

    public Map<String, List<Map<String, Object>>> getResults(Pair<String, Class<?>> runId2Class) {

        List<TestResultModel> testResults = resultsMap.get(runId2Class);
        Map<String, List<Map<String, Object>>> data = new HashMap<String, List<Map<String, Object>>>();
        data.put("results", new ArrayList<Map<String, Object>>());

        if (testResults == null || testResults.isEmpty())
            return data;

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (TestResultModel result : testResults) {
            list.add(result.getResult());
        }
        data.put("results", list);

        return data;
    }
}
