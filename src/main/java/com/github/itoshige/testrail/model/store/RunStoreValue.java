package com.github.itoshige.testrail.model.store;

/**
 * Run Store Value Model
 * 
 * @author itoshige
 * 
 */
public class RunStoreValue {

    private final String projectId;
    private final String suiteId;

    public RunStoreValue(String projectId, String suiteId) {
        super();
        this.projectId = projectId;
        this.suiteId = suiteId;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getSuiteId() {
        return suiteId;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RunStoreValue))
            return false;
        RunStoreValue key = (RunStoreValue) obj;
        return (projectId.equals(key.projectId) && suiteId.equals(key.suiteId));
    }

    @Override
    public int hashCode() {
        return projectId.hashCode() ^ suiteId.hashCode();
    }
}
