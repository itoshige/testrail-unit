package org.itoshige.testrail.util;

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
            String url = assureProp("testrail.url");
            String user = assureProp("testrail.user");
            String password = assureProp("testrail.password");

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

    private static String assureProp(String key) {
        String str = TestRailUnitPropertyUtil.INSTANCE.getProperty(key);

        if (str == null || str.isEmpty())
            throw new TestInitializerException(key + " is null or Empty");
        return str;
    }

}
