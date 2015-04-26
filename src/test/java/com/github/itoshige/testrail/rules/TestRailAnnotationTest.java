package com.github.itoshige.testrail.rules;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.github.itoshige.testrail.TestBase;
import com.github.itoshige.testrail.annotation.IgnoreTestRail;

/**
 * @TestRail annotation test
 * 
 * @author itoshige
 * 
 */
public class TestRailAnnotationTest extends TestBase {

    @Test
    public void assertSuccess() {
        assertThat("aaa", is("aaa"));
    }

    @IgnoreTestRail
    @Test
    public void assertSuccess2() {
        assertThat("aaa", is("aaa"));
    }

    @IgnoreTestRail
    @Test
    public void assertFail() {
        assertThat("aaa", is("bbb"));
    }

    @Test
    public void テスト成功() {
        assertThat("aaa", is("aaa"));
    }

    @Test
    public void テスト失敗() {
        assertThat("aaa", is("bbb"));
    }
}
