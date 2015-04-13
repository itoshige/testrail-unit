package org.itoshige.testrail.util;

import java.io.FileInputStream;
import java.util.Properties;

import org.itoshige.testrail.client.TestInitializerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TestRail client info
 * 
 * @author itoshige
 * 
 */
public class ClientInfoUtil {
    private static final Logger logger = LoggerFactory.getLogger(ClientInfoUtil.class);

    public static ClientInfoModel getClientInfo() {
        try {
            Properties prop = getProp();
            String url = assureProp(prop, "testrail.url");
            String user = assureProp(prop, "testrail.user");
            String password = assureProp(prop, "testrail.password");

            return new ClientInfoModel(url, user, password);
        } catch (TestInitializerException e) {
            logger.error("[STOP TEST]init error exception:{}", e);
            System.exit(1);
            return null;
        }
    }

    public static class ClientInfoModel {
        private String url;
        private String user;
        private String password;

        private ClientInfoModel(String url, String user, String password) {
            super();
            this.url = url;
            this.user = user;
            this.password = password;
        }

        public String getUrl() {
            return url;
        }

        public String getUser() {
            return user;
        }

        public String getPassword() {
            return password;
        }
    }

    private static String assureProp(Properties prop, String key) {
        String str = prop.getProperty(key);

        if (str == null || str.isEmpty())
            throw new TestInitializerException(key + " is null or Empty");
        return str;
    }

    public static Properties getProp() {
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream(FileSearchUtil.getFilePath(System.getProperty("user.dir"),
                "testrail-unit.properties")));
            return prop;
        } catch (Exception e) {
            throw new TestInitializerException("testrail-unit can't be read.");
        }
    }
}
