package com.github.itoshige.testrail.rules;

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
@Section(name = "サンプルセクション ")
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
}
