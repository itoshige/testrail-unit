package com.github.itoshige.testrail.rules;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import com.github.itoshige.testrail.TestBase;
import com.github.itoshige.testrail.annotation.IgnoreTestRail;

public class TestRailAddTest extends TestBase {

    @Test
    public void addTestRail() {
        assertThat("aaa", is("aaa"));
    }

    @IgnoreTestRail
    @Test
    public void notAddTestRail() {
        assertThat("aaa", is("aaa"));
    }

    @Ignore
    @Test
    public void notAddTestRail2() {
        assertThat("aaa", is("aaa"));
    }
}
