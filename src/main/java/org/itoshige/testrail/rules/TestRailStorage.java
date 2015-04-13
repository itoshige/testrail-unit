package org.itoshige.testrail.rules;

import org.itoshige.testrail.annotation.IgnoreTestRail;
import org.itoshige.testrail.cache.CaseCache;
import org.itoshige.testrail.cache.ResultCache;
import org.itoshige.testrail.cache.SectionCache;
import org.itoshige.testrail.cache.TestCache;
import org.itoshige.testrail.client.Pair;
import org.itoshige.testrail.client.TestRailClient.ResultStatus;
import org.itoshige.testrail.model.TestResult;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Rule annotation / storage junit test result
 * 
 * @author itoshige
 * 
 */
public class TestRailStorage extends TestWatcher {

    private static final Logger logger = LoggerFactory.getLogger(TestRailStorage.class);

    private String runId;

    public TestRailStorage(String runId) {
        this.runId = runId;
    }

    protected void succeeded(Description desc) {
        if (!isIgnore(desc)) {
            Pair<String, Class<?>> runId2Class = new Pair<String, Class<?>>(runId, desc.getTestClass());

            ResultCache.getIns().setResult(runId2Class,
                new TestResult(getTestId(desc), ResultStatus.PASSED, "This test worked fine!"));
        }
    }

    protected void failed(Throwable e, Description desc) {
        if (!isIgnore(desc)) {
            Pair<String, Class<?>> runId2Class = new Pair<String, Class<?>>(runId, desc.getTestClass());
            logger.error("[ERROR]assertError : {}", e.getMessage());
            ResultCache.getIns().setResult(runId2Class,
                new TestResult(getTestId(desc), ResultStatus.FAILED, e.toString()));

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
        String caseId = CaseCache.getIns().getCaseId(new Pair<String, String>(sectionId, title));
        return TestCache.getIns().getTestId(caseId);
    }
}