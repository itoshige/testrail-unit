package org.itoshige.testrail.rules;

import org.itoshige.testrail.cache.CaseCache;
import org.itoshige.testrail.cache.SectionCache;
import org.itoshige.testrail.cache.TestCache;
import org.itoshige.testrail.client.Pair;
import org.itoshige.testrail.client.TestInitializerException;
import org.itoshige.testrail.client.TestRailClient;
import org.junit.BeforeClass;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Classrule annotation / connect testrail
 * 
 * @author itoshige
 * 
 */
public class TestRailUnit extends TestWatcher {
    private static final Logger logger = LoggerFactory.getLogger(TestRailUnit.class);

    @BeforeClass
    public void beforeclass() {
        System.out.println("test");
    }

    private String runId;

    public TestRailUnit(String runId) {
        this.runId = runId;
        try {
            if (runId == null || runId.isEmpty())
                throw new TestInitializerException("runId is empty.");

            TestCache.getIns().setTestsMap(runId);
            Pair<String, String> pair = TestRailClient.getRun(runId);

            SectionCache.getIns().setSectionMap(pair.getFirst(), pair.getSecond());
            CaseCache.getIns().setCasesMap(pair.getFirst(), pair.getSecond());
            logger.info("set testrail data to cache. runId:{}", runId);
        } catch (TestInitializerException e) {
            logger.error("[ERROR] exception:{}", e);
            System.exit(1);
        }
    }

    protected void finished(Description desc) {
        TestRailClient.addResults(new Pair<String, Class<?>>(runId, desc.getTestClass()));
        logger.info("update testrail. runId:{}", runId);
    }
}
