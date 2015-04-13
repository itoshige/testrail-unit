package org.itoshige.testrail.client;

import java.util.List;
import java.util.Map;

import org.itoshige.testrail.cache.ResultCache;
import org.itoshige.testrail.client.core.APIClient;
import org.itoshige.testrail.util.ClientInfoUtil;
import org.itoshige.testrail.util.ClientInfoUtil.ClientInfoModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TestRail connection client
 * 
 * @author itoshige
 * 
 */
public class TestRailClient {
    private static final Logger logger = LoggerFactory.getLogger(TestRailClient.class);

    private static APIClient client;
    static {
        ClientInfoModel info = ClientInfoUtil.getClientInfo();
        client = new APIClient(info.getUrl());
        client.setUser(info.getUser());
        client.setPassword(info.getPassword());
    }

    public static Pair<String, String> getRun(String runId) {
        JSONObject obj = (JSONObject) get(new StringBuilder("get_run/").append(runId).toString());
        Object projectId = obj.get("project_id");
        Object suiteId = obj.get("suite_id");
        if (projectId != null && suiteId != null)
            return new Pair<String, String>(projectId.toString(), suiteId.toString());
        throw new TestInitializerException("test run data isn't in testrail. runId:" + runId);
    }

    public static JSONArray getSections(String projectId, String suiteId) {
        return (JSONArray) get(new StringBuilder("get_sections/").append(projectId).append("&suite_id=")
            .append(suiteId).toString());
    }

    public static JSONArray getCases(String projectId, String suiteId) {
        return (JSONArray) get(new StringBuilder("get_cases/").append(projectId).append("&suite_id=")
            .append(suiteId).toString());
    }

    public static JSONArray getTests(String runId) {
        return (JSONArray) get(new StringBuilder("get_tests/").append(runId).toString());
    }

    private static Object get(String command) {
        try {

            Object obj = client.sendGet(command);
            return obj;
        } catch (Exception e) {
            throw new TestInitializerException("exception:" + e);
        }
    }

    public static void addResults(Pair<String, Class<?>> runId2Class) {
        try {
            Map<String, List<Map<String, Object>>> results = ResultCache.getIns().getResults(runId2Class);
            if (results == null || results.isEmpty())
                return;
            client.sendPost(new StringBuilder("add_results/").append(runId2Class.getFirst()).toString(),
                results);
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
