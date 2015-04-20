package org.itoshige.testrail.rules;

import org.itoshige.testrail.annotation.IgnoreTestRail;
import org.itoshige.testrail.annotation.LinkTestRailHelper;
import org.itoshige.testrail.annotation.LinkTestRailRun;
import org.itoshige.testrail.cache.CaseCache;
import org.itoshige.testrail.cache.ResultCache;
import org.itoshige.testrail.cache.SectionCache;
import org.itoshige.testrail.cache.TestCache;
import org.itoshige.testrail.client.Pair;
import org.itoshige.testrail.client.TestRailClient.ResultStatus;
import org.itoshige.testrail.model.TestResult;
import org.itoshige.testrail.util.TestRailUnitPropertyUtil;
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


    public TestRailStorage() {
    }
    
    @Override
    protected void succeeded(Description desc) {
    	if(TestRailUnitPropertyUtil.INSTANCE.isTestRailUnitDisabled())
			return;
    	
        if (!isIgnore(desc)) {
        	String runId = Long.toString(LinkTestRailHelper.getAnnotation(
    				desc.getTestClass(), LinkTestRailRun.class).value());
    		
            Pair<String, Class<?>> runId2Class = new Pair<String, Class<?>>(runId, desc.getTestClass());

            ResultCache.getIns().setResult(runId2Class,
                new TestResult(getTestId(desc), ResultStatus.PASSED, "This test worked fine!"));
        }
    }

    @Override
    protected void failed(Throwable e, Description desc) {
    	if(TestRailUnitPropertyUtil.INSTANCE.isTestRailUnitDisabled())
			return;
    	
        if (!isIgnore(desc)) {
        	String runId = Long.toString(LinkTestRailHelper.getAnnotation(
    				desc.getTestClass(), LinkTestRailRun.class).value());
            Pair<String, Class<?>> runId2Class = new Pair<String, Class<?>>(runId, desc.getTestClass());
            logger.error("[ERROR]assertError : {}", e.getMessage());
            ResultCache.getIns().setResult(runId2Class,
                new TestResult(getTestId(desc), ResultStatus.FAILED, e.toString()));

        }
    }

    private boolean isIgnore(Description desc) {
        IgnoreTestRail testrailMethod = desc.getAnnotation(IgnoreTestRail.class);
        IgnoreTestRail testrailClass = desc.getTestClass().getAnnotation(IgnoreTestRail.class);
        return (testrailMethod != null && IgnoreTestRail.class.equals(testrailMethod.annotationType()))
        		|| (testrailClass != null && IgnoreTestRail.class.equals(testrailClass.annotationType()));
    }

    private String getTestId(Description desc) {
        String sectionName = desc.getTestClass().getSimpleName();
        String title = desc.getMethodName();

        String sectionId = SectionCache.getIns().getSectionId(sectionName);
        String caseId = CaseCache.getIns().getCaseId(new Pair<String, String>(sectionId, title));
        return TestCache.getIns().getTestId(caseId);
    }
}
