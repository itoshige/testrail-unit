package org.itoshige.testrail.annotation;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.itoshige.testrail.TestBase;
import org.itoshige.testrail.junit.TestRailUnit;
import org.junit.Rule;
import org.junit.Test;

/**
 * @TestRail annotation test
 * 
 * @author itoshige
 * 
 */
public class TestRailAnnotationTest extends TestBase {

    @Rule
    public TestRailUnit tu = new TestRailUnit("281");

    @Test
    public void assertSuccess() {
        assertThat("aaa", is("aaa"));
    }

    @Test
    public void assertFail() {
        assertThat("aaa", is("bbb"));
    }

    @TestRail
    @Test
    public void Sample_01() {
        assertThat("aaa", is("aaa"));
    }

    @TestRail
    @Test
    public void hogehogeページの確認() {
        assertThat("aaa", is("aaa"));
    }

    @TestRail
    @Test
    public void hogehogeページの確認3() {
        assertThat("aaa", is("aaa"));
    }

    @Test
    public void hogehogeページの確認2() {
        assertThat("aaa", is("aaa"));
    }
}
