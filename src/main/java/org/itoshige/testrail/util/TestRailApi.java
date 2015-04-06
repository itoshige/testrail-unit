package org.itoshige.testrail.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.itoshige.testrail.client.APIClient;
import org.itoshige.testrail.client.APIException;
import org.itoshige.testrail.client.TestRailUnitException;
import org.itoshige.testrail.util.JSONUtils.Predicate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TestRailApi Util
 * 
 * @author itoshige
 * 
 */
public class TestRailApi {

    private static final Logger logger = LoggerFactory.getLogger(TestRailApi.class);

    private static ConcurrentHashMap<String, JSONArray> runIdtoJsonArray = new ConcurrentHashMap<String, JSONArray>();

    private static APIClient client;
    static {
        ClientInfoModel info = getClientInfo();
        client = new APIClient(info.url);
        client.setUser(info.user);
        client.setPassword(info.password);
    }

    private static ClientInfoModel getClientInfo() {
        try {
            Properties prop = getProp();
            String url = assureProp(prop, "testrail.url");
            String user = assureProp(prop, "testrail.user");
            String password = assureProp(prop, "testrail.password");

            return new ClientInfoModel(url, user, password);
        } catch (TestRailUnitException e) {
            logger.error("[STOP TEST]init error exception:{}", e);
            System.exit(1);
            return null;
        }
    }

    private static class ClientInfoModel {
        public String url;
        public String user;
        public String password;

        private ClientInfoModel(String url, String user, String password) {
            super();
            this.url = url;
            this.user = user;
            this.password = password;
        }
    }

    private static String assureProp(Properties prop, String key) {
        String str = prop.getProperty(key);

        if (str == null || str.isEmpty())
            throw new TestRailUnitException(key + " is null or Empty");
        return str;
    }

    public static Properties getProp() {
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream(FileSearch.getFilePath(System.getProperty("user.dir"),
                "testrail-unit.properties")));
            return prop;
        } catch (Exception e) {
            throw new TestRailUnitException("testrail-unit can't be read.");
        }
    }

    /**
     * get current testId
     * 
     * @param runId
     * @param desc
     * @return
     */
    public static String getTestId(String runId, final Description desc) {
        try {
            JSONArray jsonArray = getJSONArrayByRunId(runId);

            JSONObject target = JSONUtils.getJsonObject(jsonArray, new Predicate() {
                public boolean apply(JSONObject obj) {
                    if (obj.get("title").toString().trim().equals(desc.getMethodName()))
                        return equalSectionName(obj, desc.getTestClass().getSimpleName());
                    return false;
                }
            });
            equalSectionName(target, desc.getTestClass().getSimpleName());

            return target.get("id").toString();

        } catch (Exception e) {
            logger.error("[STOP TEST] methodName: isn't in testrail. runId:{} exception:{}", new Object[] {
                desc.getMethodName(), runId, e });
            System.exit(1);
            return null;
        }
    }

    private static boolean equalSectionName(JSONObject target, String className) {
        try {
            String caseId = target.get("case_id").toString();
            JSONObject jsonOfCase = (JSONObject) client.sendGet(new StringBuilder("get_case/").append(caseId)
                .toString());

            String sectionId = jsonOfCase.get("section_id").toString();
            JSONObject jsonOfSection = (JSONObject) client.sendGet(new StringBuilder("get_section/").append(
                sectionId).toString());

            String sectionName = jsonOfSection.get("name").toString();

            return className.equals(sectionName);
        } catch (Exception e) {
            logger.error("exception:", e);
            throw new TestRailUnitException("exception:" + e);
        }
    }

    private static JSONArray getJSONArrayByRunId(String runId) throws MalformedURLException, IOException,
        APIException {
        JSONArray jsonArray = runIdtoJsonArray.get(runId);
        if (jsonArray == null || jsonArray.isEmpty()) {
            jsonArray = (JSONArray) client.sendGet(new StringBuilder("get_tests/").append(runId).toString());
            runIdtoJsonArray.put(runId, jsonArray);
        }
        return jsonArray;
    }

    /**
     * add result
     * 
     * @param testId
     * @param status
     * @param comment
     * @return
     */
    public static JSONObject addResult(String testId, ResultStatus status, String comment) {
        try {
            HashMap<String, Object> data = new HashMap<String, Object>();
            data.put("status_id", status.getId());
            data.put("comment", comment);

            JSONObject result = (JSONObject) client.sendPost(new StringBuilder("add_result/").append(testId)
                .toString(), data);
            return result;
        } catch (Exception e) {
            logger.error("[ERROR]add result exception:{}", e);
            throw new TestRailUnitException(e);
        }
    }

    public enum ResultStatus {
        PASSED(1), BLOCKED(2), UNTESTED(3), RETEST(4), FAILED(5);

        private Integer statusId;

        ResultStatus(Integer statusId) {
            this.statusId = statusId;
        }

        public Integer getId() {
            return this.statusId;
        }
    }

}
