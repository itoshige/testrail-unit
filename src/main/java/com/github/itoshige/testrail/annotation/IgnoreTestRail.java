/**
 * Copyright 2015 itoshige1017@gmail.com
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package com.github.itoshige.testrail.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * If you ignore from testrail, please use this annotation.
 * 
 * @author itoshige
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface IgnoreTestRail {
}
