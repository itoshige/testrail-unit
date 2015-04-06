package org.itoshige.testrail;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Properties;

import org.itoshige.testrail.util.TestRailApi;
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
        Properties prop = TestRailApi.getProp();

        String url = prop.getProperty("testrail.url");
        assertThat(url, is("http://example.com/"));

        String user = prop.getProperty("testrail.user");
        assertThat(user, is("hoga"));

        String password = prop.getProperty("testrail.password");
        assertThat(password, is("hoge"));
    }
}
