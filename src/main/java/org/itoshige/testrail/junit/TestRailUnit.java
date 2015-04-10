package org.itoshige.testrail.junit;

import org.itoshige.testrail.annotation.IgnoreTestRail;
import org.itoshige.testrail.cache.CaseCache;
import org.itoshige.testrail.cache.SectionCache;
import org.itoshige.testrail.cache.TestCache;
import org.itoshige.testrail.client.Pair;
import org.itoshige.testrail.client.TestInitializerException;
import org.itoshige.testrail.client.TestRailClient;
import org.itoshige.testrail.client.TestRailClient.ResultStatus;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestRailUnit extends TestWatcher {
    private static final Logger logger = LoggerFactory.getLogger(TestRailUnit.class);

    public TestRailUnit(String runId) {
        try {
            if (runId == null || runId.isEmpty())
                throw new TestInitializerException("runId is empty.");

            TestCache.getIns().setTestsMap(runId);
            Pair<String, String> pair = TestRailClient.getRun(runId);

            SectionCache.getIns().setSectionMap(pair.getFirst(), pair.getSecond());
            CaseCache.getIns().setCasesMap(pair.getFirst(), pair.getSecond());
        } catch (TestInitializerException e) {
            logger.error("[ERROR] exception:{}", e);
            System.exit(1);
        }
    }

    protected void succeeded(Description desc) {
        if (!isIgnore(desc)) {
            TestRailClient.addResult(getTestId(desc), ResultStatus.PASSED, "This test worked fine!");
        }
    }

    protected void failed(Throwable e, Description desc) {
        if (!isIgnore(desc)) {
            logger.error("[ERROR]assertError : {}", e);
            TestRailClient.addResult(getTestId(desc), ResultStatus.FAILED, e.toString());
        }
    }

    private boolean isIgnore(Description desc) {
        IgnoreTestRail testrail = desc.getAnnotation(IgnoreTestRail.class);
        return testrail != null && IgnoreTestRail.class.equals(testrail.annotationType());
    }

    private String getTestId(Description desc) {
        String sectionName = desc.getTestClass().getSimpleName();
        String title = desc.getMethodName();

        String sectionId = SectionCache.getIns().getSectionId(sectionName);
        String caseId = CaseCache.getIns().getCaseId(sectionId, title);
        return TestCache.getIns().getTestId(caseId);
    }
}
