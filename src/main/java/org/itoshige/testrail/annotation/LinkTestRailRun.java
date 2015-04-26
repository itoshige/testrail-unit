package org.itoshige.testrail.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This Annotation will be used to Link JUNIT classes to TestRail Run using the
 * RunId specified as a value This can be applied to CLASS or PACKAGE.And will
 * be Inherited in the CLASS hirerachy
 * 
 * @author abhijeet.burle
 *
 */
@Inherited
@Target(value = { ElementType.PACKAGE, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface LinkTestRailRun {
	long value();
}
