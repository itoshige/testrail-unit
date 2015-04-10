package org.itoshige.testrail;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.itoshige.testrail.util.ClientInfoUtil;
import org.itoshige.testrail.util.ClientInfoUtil.ClientInfoModel;
import org.junit.Test;

/**
 * testrail-unit.properties file read test
 * 
 * @author itoshige
 * 
 */
public class ConfigrationTest extends TestBase {

    @Test
    public void loadProperties() {
        ClientInfoModel info = ClientInfoUtil.getClientInfo();

        assertThat(info.getUrl(), is("http://example.com/"));
        assertThat(info.getUser(), is("hoga"));
        assertThat(info.getPassword(), is("hoge"));
    }
}
