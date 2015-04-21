package com.github.itoshige.testrail.unit;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.Set;

import org.junit.Test;

import com.github.itoshige.testrail.TestBase;
import com.github.itoshige.testrail.annotation.IgnoreTestRail;
import com.github.itoshige.testrail.util.PackageClassSearchUtil;

@IgnoreTestRail
public class PackageClassSearchTest extends TestBase {

    @Test
    public void getTestClasses() {
        Set<String> sections = PackageClassSearchUtil.getTestClasses("359");
        assertThat(sections.contains("サンプルセクション2"), is(true));
    }
}
