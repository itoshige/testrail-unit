package org.itoshige.testrail.junit;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.itoshige.testrail.annotation.TestRail;
import org.itoshige.testrail.util.TestRailApi;
import org.itoshige.testrail.util.TestRailApi.ResultStatus;
import org.junit.internal.runners.model.MultipleFailureException;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @see https
 *      ://github.com/tauty/rufa/blob/master/src/main/java/com/github/tauty/
 *      rufa/theories/RTheories.java
 * 
 * @see https
 *      ://github.com/tauty/rufa/blob/master/src/main/java/com/github/tauty/
 *      rufa/rules/RErrorCollector.java
 * 
 * @see https
 *      ://github.com/tauty/rufa/blob/master/src/test/java/com/github/tauty/
 *      rufa/rules/RErrorCollectorTest.java
 * 
 * @author itoshige
 * 
 */
@SuppressWarnings("deprecation")
public class TestRailUnit implements TestRule {

    private static final Logger logger = LoggerFactory.getLogger(TestRailUnit.class);

    private List<Throwable> errors = new ArrayList<Throwable>();
    private Statement fNext;
    private String runId;

    public TestRailUnit(String runId) {
        this.runId = runId;
    }

    @Override
    public Statement apply(Statement stmt, final Description desc) {
        this.fNext = stmt;

        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    fNext.evaluate();
                } catch (Throwable t) {
                    errors.add(t);
                } finally {
                    final TestRail testrail = desc.getAnnotation(TestRail.class);

                    if (isTestRail(testrail)) {
                        final String testId = TestRailApi.getTestId(runId, desc);
                        if (errors.isEmpty()) {
                            TestRailApi.addResult(testId, ResultStatus.PASSED, "This test worked fine!");
                        } else {
                            logger.error("[ERROR]assertError : {}", errors.toString());
                            TestRailApi.addResult(testId, ResultStatus.FAILED, errors.toString());
                        }
                    }
                }
                MultipleFailureException.assertEmpty(errors);
            }
        };
    }

    private boolean isTestRail(Annotation obj) {
        return obj != null && TestRail.class.equals(obj.annotationType());
    }
}
