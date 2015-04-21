package com.github.itoshige.testrail.store;

import java.util.concurrent.ConcurrentHashMap;

import com.github.itoshige.testrail.client.Pair;
import com.github.itoshige.testrail.client.TestRailClient;
import com.github.itoshige.testrail.util.CollectionUtil;

/**
 * TestRail Project & Suite Data with run_id as key
 * 
 * @author abhijeet.burle
 * 
 */
public class RunStore {
    private static final RunStore instance = new RunStore();

    // runId to Pair<projectId, suiteId>
    private final ConcurrentHashMap<String, Pair<String, String>> runDataMap = CollectionUtil
        .newConcurrentMap();

    static RunStore getIns() {
        return instance;
    }

    public boolean hasRun(String runId) {
        return runDataMap.containsKey(runId);
    }

    public Pair<String, String> getRun(String runId) {
        Pair<String, String> pair = runDataMap.get(runId);
        if (pair == null) {
            pair = TestRailClient.getRun(runId);
            runDataMap.putIfAbsent(runId, pair);
        }
        return pair;
    }
}
