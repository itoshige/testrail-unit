/**
 * Copyright 2015 itoshige1017@gmail.com
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
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
import com.github.itoshige.testrail.model.store.RunStoreValue;
import com.github.itoshige.testrail.model.store.TestResultStoreKey;
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
    public static RunStoreValue getRun(String runId) {
        JSONObject obj = (JSONObject) get(String.format("get_run/%s", runId));
        Object projectId = obj.get("project_id");
        Object suiteId = obj.get("suite_id");
        if (projectId != null && suiteId != null)
            return new RunStoreValue(projectId.toString(), suiteId.toString());
        throw new TestInitializerException(String.format("test run data isn't in testrail. runId:%s", runId));
    }

    /************ section ************/
    public static JSONArray getSections(String projectId, String suiteId) {
        return (JSONArray) get(String.format("get_sections/%s&suite_id=%s", projectId, suiteId));
    }

    public static JSONObject addSection(String projectId, SectionModel section) {
        logger.info("add section in testrail. projectId:{} sectionName:{}", projectId, section.getName());
        return (JSONObject) post(String.format("add_section/%s", projectId), section.getSection());
    }

    public static void deleteSection(String sectionId) {
        logger.info("delete section in testrail. sectionId:{}", sectionId);
        post(String.format("delete_section/%s", sectionId));
    }

    /************ test case ************/
    public static JSONArray getCases(String projectId, String suiteId) {
        return (JSONArray) get(String.format("get_cases/%s&suite_id=%s", projectId, suiteId));
    }

    public static JSONObject addCase(String sectionId, CaseModel caze) {
        logger.info("add case in testrail. sectionId:{} title:{}", sectionId, caze.getTitle());
        return (JSONObject) post(String.format("add_case/%s", sectionId), caze.getCase());
    }

    public static void deleteCase(String caseId) {
        logger.info("delete case in testrail. caseId:{}", caseId);
        post(String.format("delete_case/%s", caseId));
    }

    /************ test ************/
    public static JSONArray getTests(String runId) {
        return (JSONArray) get(String.format("get_tests/%s", runId));
    }

    /************ results ************/
    public static JSONArray addResults(TestResultStoreKey runId2Class) {
        Map<String, List<Map<String, Object>>> results = SyncManager.getJunitTestResults(runId2Class);
        if (results.isEmpty())
            return new JSONArray();

        logger.info("update testrail. runId:{}", runId2Class.getRunId());
        return (JSONArray) post(String.format("add_results/%s", runId2Class.getRunId()), results);
    }

    private static Object get(String command) {
        try {
            Object obj = client.sendGet(command);
            return obj;
        } catch (Exception e) {
            throw new TestInitializerException(String.format("command:%s cannot get.", command), e);
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
            throw new TestInitializerException(String.format("command:%s cannot post. map:%s", command,
                map.toString()), e);
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
