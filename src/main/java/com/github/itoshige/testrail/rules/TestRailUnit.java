package com.github.itoshige.testrail.rules;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.itoshige.testrail.client.Pair;
import com.github.itoshige.testrail.client.TestInitializerException;
import com.github.itoshige.testrail.client.TestRailClient;
import com.github.itoshige.testrail.store.SyncManager;
import com.github.itoshige.testrail.util.ConfigrationUtil;
import com.github.itoshige.testrail.util.TestRailUnitUtil;

/**
 * @Classrule annotation / connect testrail
 * 
 * @author itoshige
 * 
 */
public class TestRailUnit extends TestWatcher {
    private static final Logger logger = LoggerFactory.getLogger(TestRailUnit.class);
    private String runId;
    private String projectId;

    public String getRunId() {
        return runId;
    }

    public String getProjectId() {
        return projectId;
    }

    protected void starting(Description desc) {
        if (isDisabled(desc))
            return;

        try {
            String testClassPath = desc.getTestClass().getName();
            this.runId = ConfigrationUtil.getRunId(testClassPath);

            if (runId == null || runId.isEmpty())
                throw new TestInitializerException("runId is empty.");

            TestRailUnitUtil.checkParams(runId);

            this.projectId = SyncManager.storeTestRailData(runId, desc.getTestClass());

        } catch (TestInitializerException e) {
            logger.error("[ERROR] exception:{}", e);
            System.exit(1);
        }
    }

    protected void finished(Description desc) {
        if (isDisabled(desc))
            return;

        TestRailClient.addResults(new Pair<String, Class<?>>(runId, desc.getTestClass()));
        logger.debug("TestRailUnit end");
    }

    private boolean isDisabled(Description desc) {
        return ConfigrationUtil.isDisabled() || TestRailUnitUtil.isSkipClass(desc.getTestClass());
    }
}
