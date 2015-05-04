/**
 * Copyright 2015 itoshige1017@gmail.com
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package com.github.itoshige.testrail.rules;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.itoshige.testrail.client.TestRailClient.ResultStatus;
import com.github.itoshige.testrail.model.TestResultModel;
import com.github.itoshige.testrail.model.store.TestResultStoreKey;
import com.github.itoshige.testrail.store.SyncManager;
import com.github.itoshige.testrail.util.ConfigrationUtil;
import com.github.itoshige.testrail.util.TestRailUnitUtil;

/**
 * @Rule annotation / storage junit test result
 * 
 * @author itoshige
 * 
 */
public class TestRailStore extends TestWatcher {

    private static final Logger logger = LoggerFactory.getLogger(TestRailStore.class);

    private String runId;
    private String projectId;

    public TestRailStore(TestRailUnit unit) {
        this.runId = unit.getRunId();
        this.projectId = unit.getProjectId();
    }

    protected void succeeded(Description desc) {
        if (!isDisabled(desc)) {
            TestResultStoreKey key = new TestResultStoreKey(runId, desc.getTestClass());

            SyncManager.storeJunitTestResult(key,
                new TestResultModel(SyncManager.getTestId(projectId, runId, desc), ResultStatus.PASSED,
                    "This test worked fine!"));
        }
    }

    protected void failed(Throwable e, Description desc) {
        if (!isDisabled(desc)) {
            TestResultStoreKey key = new TestResultStoreKey(runId, desc.getTestClass());
            logger.error("[ERROR]assertError : {}", e.getMessage());
            SyncManager.storeJunitTestResult(
                key,
                new TestResultModel(SyncManager.getTestId(projectId, runId, desc), ResultStatus.FAILED, e
                    .toString()));
        }
    }

    private boolean isDisabled(Description desc) {
        return ConfigrationUtil.isDisabled() || TestRailUnitUtil.isSkipClass(desc.getTestClass())
            || TestRailUnitUtil.isSkipMethod(desc);
    }
}
