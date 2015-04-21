package com.github.itoshige.testrail.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.itoshige.testrail.client.core.APIClient;
import com.github.itoshige.testrail.model.CaseModel;
import com.github.itoshige.testrail.model.SectionModel;
import com.github.itoshige.testrail.store.SyncManager;
import com.github.itoshige.testrail.util.ConfigrationUtil;
import com.github.itoshige.testrail.util.ConfigrationUtil.ClientInfoModel;

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
        ClientInfoModel info = ConfigrationUtil.getClientInfo();
        client = new APIClient(info.getUrl());
        client.setUser(info.getUser());
        client.setPassword(info.getPassword());
    }

    /************ test run ************/
    /**
     * return projectId and suiteId
     * 
     * @param runId
     * @return projectId & suiteId
     */
    public static Pair<String, String> getRun(String runId) {
        JSONObject obj = (JSONObject) get(new StringBuilder("get_run/").append(runId).toString());
        Object projectId = obj.get("project_id");
        Object suiteId = obj.get("suite_id");
        if (projectId != null && suiteId != null)
            return new Pair<String, String>(projectId.toString(), suiteId.toString());
        throw new TestInitializerException(new StringBuilder("test run data isn't in testrail. runId:")
            .append(runId).toString());
    }

    /************ section ************/
    public static JSONArray getSections(String projectId, String suiteId) {
        return (JSONArray) get(new StringBuilder("get_sections/").append(projectId).append("&suite_id=")
            .append(suiteId).toString());
    }

    public static JSONObject addSection(String projectId, SectionModel section) {
        logger.info("add section in testrail. projectId:{} sectionName:{}", projectId, section.getName());
        return (JSONObject) post(new StringBuilder("add_section/").append(projectId).toString(),
            section.getSection());
    }

    public static void deleteSection(String sectionId) {
        logger.info("delete section in testrail. sectionId:{}", sectionId);
        post(new StringBuilder("delete_section/").append(sectionId).toString());
    }

    /************ test case ************/
    public static JSONArray getCases(String projectId, String suiteId) {
        return (JSONArray) get(new StringBuilder("get_cases/").append(projectId).append("&suite_id=")
            .append(suiteId).toString());
    }

    public static JSONObject addCase(String sectionId, CaseModel caze) {
        logger.info("add case in testrail. sectionId:{} title:{}", sectionId, caze.getTitle());
        return (JSONObject) post(new StringBuilder("add_case/").append(sectionId).toString(), caze.getCase());
    }

    public static void deleteCase(String caseId) {
        logger.info("delete case in testrail. caseId:{}", caseId);
        post(new StringBuilder("delete_case/").append(caseId).toString());
    }

    /************ test ************/
    public static JSONArray getTests(String runId) {
        return (JSONArray) get(new StringBuilder("get_tests/").append(runId).toString());
    }

    /************ results ************/
    public static JSONArray addResults(Pair<String, Class<?>> runId2Class) {
        Map<String, List<Map<String, Object>>> results = SyncManager.getJunitTestResults(runId2Class);
        if (results.isEmpty())
            return new JSONArray();

        logger.info("update testrail. runId:{}", runId2Class.getFirst());
        return (JSONArray) post(new StringBuilder("add_results/").append(runId2Class.getFirst()).toString(),
            results);
    }

    private static Object get(String command) {
        try {
            Object obj = client.sendGet(command);
            return obj;
        } catch (Exception e) {
            throw new TestInitializerException(new StringBuilder("command:").append(command)
                .append(" cannot get.").toString(), e);
        }
    }

    private static Object post(String command) {
        return post(command, new HashMap<String, String>());
    }

    private static Object post(String command, final Map<String, ?> map) {
        try {
            Object obj = client.sendPost(command, map);
            return obj;
        } catch (Exception e) {
            throw new TestInitializerException(new StringBuilder("command:").append(command)
                .append(" cannot post. map:").append(map.toString()).toString(), e);
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
