package org.itoshige.testrail.rules;

import org.itoshige.testrail.annotation.IgnoreTestRail;
import org.itoshige.testrail.annotation.LinkTestRailHelper;
import org.itoshige.testrail.annotation.LinkTestRailRun;
import org.itoshige.testrail.cache.CaseCache;
import org.itoshige.testrail.cache.SectionCache;
import org.itoshige.testrail.cache.TestCache;
import org.itoshige.testrail.client.Pair;
import org.itoshige.testrail.client.TestInitializerException;
import org.itoshige.testrail.client.TestRailClient;
import org.itoshige.testrail.util.TestRailUnitPropertyUtil;
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
    
    public TestRailUnit() {
    }

	@Override
	protected void starting(Description desc) {
		if(TestRailUnitPropertyUtil.INSTANCE.isTestRailUnitDisabled())
			return;
		if (isIgnore(desc)) 
			return;
		
		String runId = null;

		LinkTestRailRun objRun = LinkTestRailHelper.getAnnotation(
				desc.getTestClass(), LinkTestRailRun.class);
		if (objRun != null) {
			runId = Long.toString(objRun.value());
		}
		try {
			// This check is mandatory. If Run ID  is not found then stop the processing
			// Will skip the checking from other places
			if (runId == null || runId.isEmpty())
				throw new TestInitializerException(
						"Annotation @LinkTestRailRun(runId) not found in any scope for the class["
								+ desc.getTestClass().getSimpleName() + "].");
		

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

    @Override
    protected void finished(Description desc) {
    	if(TestRailUnitPropertyUtil.INSTANCE.isTestRailUnitDisabled())
			return;
		if (isIgnore(desc)) 
			return;
    	
    	String runId = Long.toString(LinkTestRailHelper.getAnnotation(
				desc.getTestClass(), LinkTestRailRun.class).value());
		// No Need to runId for null here as it has already been checked in the 
		// method starting
    	
        TestRailClient.addResults(new Pair<String, Class<?>>(runId, desc.getTestClass()));
        logger.info("update testrail. runId:{}", runId);
    }
    
    private boolean isIgnore(Description desc) {
        IgnoreTestRail testrail = desc.getAnnotation(IgnoreTestRail.class);
        return testrail != null && IgnoreTestRail.class.equals(testrail.annotationType());
    }
}
