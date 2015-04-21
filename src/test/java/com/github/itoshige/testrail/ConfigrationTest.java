package com.github.itoshige.testrail;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.github.itoshige.testrail.annotation.IgnoreTestRail;
import com.github.itoshige.testrail.util.ConfigrationUtil;
import com.github.itoshige.testrail.util.ConfigrationUtil.ClientInfoModel;

/**
 * testrail-unit.json file read test
 * 
 * @author itoshige
 * 
 */
@IgnoreTestRail
public class ConfigrationTest extends TestBase {

    @Test
    public void loadJSON() {
        ClientInfoModel info = ConfigrationUtil.getClientInfo();

        assertThat(info.getUrl(), is("http://example.com/"));
        assertThat(info.getUser(), is("hoga"));
        assertThat(info.getPassword(), is("hoge"));
    }
}
