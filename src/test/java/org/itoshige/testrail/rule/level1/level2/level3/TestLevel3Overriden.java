package org.itoshige.testrail.rule.level1.level2.level3;

import static org.junit.Assert.assertEquals;

import org.itoshige.testrail.TestBase;
import org.itoshige.testrail.annotation.LinkTestRailRun;
import org.junit.Test;

/**
 * This is a sample Test class which specifies its own TestRail Id using
 * annotation at class level
 * 
 * @author abhijeet.burle
 *
 */
@LinkTestRailRun(356)
public class TestLevel3Overriden extends TestBase {
	/**
	 * 
	 * @return
	 */
	@Test
	public void doSomething() {
		assertEquals("HelloWorld", "HelloWorld");
	}

	/**
	 * @return
	 */
	@Test
	public void doSomethingAgain() {
		assertEquals("NamasteWorld", "NamasteWorld");
	}
}
