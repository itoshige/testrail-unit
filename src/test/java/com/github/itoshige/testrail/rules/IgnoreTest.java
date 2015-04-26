package com.github.itoshige.testrail.rules;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.github.itoshige.testrail.TestBase;
import com.github.itoshige.testrail.annotation.IgnoreTestRail;

@IgnoreTestRail
public class IgnoreTest extends TestBase {

    @Test
    public void ignoreTestRailClass() {
        assertThat("aaa", is("aaa"));
    }
}
