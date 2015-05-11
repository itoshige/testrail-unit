/**
 * Copyright 2015 itoshige1017@gmail.com
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package com.github.itoshige.testrail.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.itoshige.testrail.client.TestRailUnitException;
import com.github.itoshige.testrail.model.TestResultModel;
import com.github.itoshige.testrail.model.store.TestResultStoreKey;
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
    private ConcurrentHashMap<TestResultStoreKey, List<TestResultModel>> resultsMap = CollectionUtil
        .newConcurrentMap();

    private TestResultStore() {
    }

    static TestResultStore getIns() {
        return instance;
    }

    public void setResult(TestResultStoreKey key, TestResultModel result) {
        List<TestResultModel> testResults = resultsMap.get(key);
        List<TestResultModel> list = Collections.synchronizedList(new ArrayList<TestResultModel>());
        if (isEmplyResult(testResults)) {
            list.add(result);
            resultsMap.putIfAbsent(key, list);
        } else if (!testResults.contains(result)) {
            testResults.add(result);
        } else {
            logger.error("execute same test. runId:{} testId:{}", key.getRunId(), result.getTestId());
            throw new TestRailUnitException(String.format("execute same test. runId:%s testId:%s",
                key.getRunId(), result.getTestId()));
        }
    }

    private boolean isEmplyResult(List<TestResultModel> testResults) {
        return testResults == null || testResults.isEmpty();
    }

    public Map<String, List<Map<String, Object>>> getResults(TestResultStoreKey key) {

        List<TestResultModel> testResults = resultsMap.get(key);
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
