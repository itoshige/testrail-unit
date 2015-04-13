package org.itoshige.testrail.model;

import java.util.HashMap;
import java.util.Map;

import org.itoshige.testrail.client.TestRailClient.ResultStatus;

/**
 * Junit test result model
 * 
 * @author itoshige
 * 
 */
public class TestResult {
    private String testId;
    private ResultStatus status;
    private String comment;

    public TestResult(String testId, ResultStatus status, String comment) {
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
}
