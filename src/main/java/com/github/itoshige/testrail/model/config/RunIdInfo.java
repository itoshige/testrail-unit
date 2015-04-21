package com.github.itoshige.testrail.model.config;

import com.github.itoshige.testrail.client.TestInitializerException;
import com.github.itoshige.testrail.util.ConfigrationUtil;

/**
 * testrail-unit.json runIds info
 * 
 * @author itoshige
 * 
 */
public class RunIdInfo {

    private static final String DELIMITER = "\\.";

    private String target;
    private String runId;

    public RunIdInfo(Object target, Object runId) {
        super();
        if (isEmpty(target) || isEmpty(runId))
            throw new TestInitializerException(new StringBuilder(ConfigrationUtil.CONFIG_FILE).append(
                "[runIds] is invalid.").toString());

        this.target = (String) target;
        this.runId = (String) runId;
    }

    public int getCommaCount() {
        int count = target.split(DELIMITER).length;
        if (count == 0)
            throw new TestInitializerException(new StringBuilder(ConfigrationUtil.CONFIG_FILE).append(
                "[runIds.target] is invalid. Please set package name in target. ex) com.github.itoshige")
                .toString());

        return count;
    }

    public String getTarget() {
        return target;
    }

    public String getRunId() {
        return runId;
    }

    private boolean isEmpty(Object obj) {
        return obj == null || obj.toString().isEmpty();
    }
}
