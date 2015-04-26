package com.github.itoshige.testrail.rules;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.github.itoshige.testrail.TestBase;
import com.github.itoshige.testrail.annotation.Section;

@Section(name = "サンプルセクション2")
public class TestRailClassAdd2Test extends TestBase {

    @Test
    public void サンプルTest() {
        assertThat("aaa", is("aaa"));
    }

    @Test
    public void サンプルTest2() {
        assertThat("aaa", is("bbb"));
    }
}
