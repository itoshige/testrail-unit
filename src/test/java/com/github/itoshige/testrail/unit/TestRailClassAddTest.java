/**
 * Copyright 2015 itoshige1017@gmail.com
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package com.github.itoshige.testrail.unit;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.github.itoshige.testrail.TestBase;
import com.github.itoshige.testrail.annotation.IgnoreTestRail;
import com.github.itoshige.testrail.annotation.Section;

/**
 * @TestRail annotation test
 * 
 * @author itoshige
 * 
 */
@Section(name = "サンプルセクション2 ")
public class TestRailClassAddTest extends TestBase {

    @Test
    public void assertSuccess() {
        assertThat("aaa", is("aaa"));
    }

    @IgnoreTestRail
    @Test
    public void ignoreTestRail() {
        assertThat("aaa", is("aaa"));
    }

    @Test
    public void assertSuccess2() {
        assertThat("aaa", is("aaa"));
    }
}
