package org.itoshige.testrail.rule.level1.level2;

import static org.junit.Assert.assertEquals;

import org.itoshige.testrail.TestBase;
import org.junit.Test;

/**
 * This is a sample Test class which does not specify the TestRail Id using
 * annotation at class level, But instead gets it from
 * src\test\java\org\itoshige\testrail\rule\level1\package-info.java
 * 
 * @author abhijeet.burle
 *
 */
public class TestLevel2Default extends TestBase{
	/**
	 * 
	 * @return
	 */
	@Test
	public void doSomething() {
		assertEquals("HelloWorld", "HelloWorld");
	}
}
