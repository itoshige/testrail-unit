package org.itoshige.testrail.annotation;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.itoshige.testrail.TestBase;
import org.itoshige.testrail.junit.TestRailUnit;
import org.junit.ClassRule;
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
    @ClassRule
    public static TestRailUnit tr = new TestRailUnit("281");

    @IgnoreTestRail
    @Test
    public void assertSuccess() {
        assertThat("aaa", is("aaa"));
    }

    @IgnoreTestRail
    @Test
    public void assertFail() {
        assertThat("aaa", is("bbb"));
    }

    @IgnoreTestRail
    @Test
    public void assertFail2() {
        assertThat("aaa", is("bbb"));
    }

    @Test
    public void Sample_01() {
        assertThat("aaa", is("aaa"));
    }

    @Test
    public void hogehogeページの確認() {
        assertThat("aaa", is("aaa"));
    }

    @IgnoreTestRail
    @Test
    public void hogehogeページの確認3() {
        assertThat("aaa", is("aaa"));
    }

    @Test
    public void hogehogeページの確認2() {
        assertThat("aaa", is("bbb"));
    }
}
