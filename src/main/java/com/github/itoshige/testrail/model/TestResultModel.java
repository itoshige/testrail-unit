package com.github.itoshige.testrail.model;

import java.util.HashMap;
import java.util.Map;

import com.github.itoshige.testrail.client.TestRailClient.ResultStatus;

/**
 * Junit test result model
 * 
 * @author itoshige
 * 
 */
public class TestResultModel {
    private String testId;
    private ResultStatus status;
    private String comment;

    public TestResultModel(String testId, ResultStatus status, String comment) {
        super();
        this.testId = testId;
        this.status = status;
        this.comment = comment;
    }

    public Map<String, Object> getResult() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("test_id", testId);
        data.put("status_id", status.getId());
        data.put("comment", comment);
        return data;
    }

    public String getTestId() {
        return testId;
    }
}
