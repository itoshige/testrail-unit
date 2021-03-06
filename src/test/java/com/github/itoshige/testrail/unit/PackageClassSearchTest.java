/**
 * Copyright 2015 itoshige1017@gmail.com
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package com.github.itoshige.testrail.unit;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.Set;

import org.junit.Test;

import com.github.itoshige.testrail.TestBase;
import com.github.itoshige.testrail.util.PackageClassSearchUtil;

public class PackageClassSearchTest extends TestBase {

    @Test
    public void getTestClasses() {
        Set<String> sections = PackageClassSearchUtil.getTestClasses("403");
        assertThat(sections.contains("サンプルセクション2"), is(true));
    }
}
