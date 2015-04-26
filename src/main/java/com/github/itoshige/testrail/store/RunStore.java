package com.github.itoshige.testrail.store;

import java.util.concurrent.ConcurrentHashMap;

import com.github.itoshige.testrail.client.TestRailClient;
import com.github.itoshige.testrail.model.store.RunStoreValue;
import com.github.itoshige.testrail.util.CollectionUtil;

/**
 * TestRail Project & Suite Data with run_id as key
 * 
 * @author abhijeet.burle
 * 
 */
public class RunStore {
    private static final RunStore instance = new RunStore();

    // runId to projectId, suiteId
    private final ConcurrentHashMap<String, RunStoreValue> runDataMap = CollectionUtil.newConcurrentMap();

    private RunStore() {
    }

    static RunStore getIns() {
        return instance;
    }

    public boolean hasRun(String runId) {
        return runDataMap.containsKey(runId);
    }

    public RunStoreValue getRun(String runId) {
        RunStoreValue value = runDataMap.get(runId);
        if (value == null) {
            value = TestRailClient.getRun(runId);
            runDataMap.putIfAbsent(runId, value);
        }
        return value;
    }
}
