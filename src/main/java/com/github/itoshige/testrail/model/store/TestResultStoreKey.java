package com.github.itoshige.testrail.model.store;

/**
 * Test Result Store Key Model
 * 
 * @author itohsige
 * 
 */
public class TestResultStoreKey {

    private final String runId;
    private final Class<?> clazz;

    public TestResultStoreKey(String runId, Class<?> clazz) {
        super();
        this.runId = runId;
        this.clazz = clazz;
    }

    public String getRunId() {
        return runId;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TestResultStoreKey))
            return false;
        TestResultStoreKey key = (TestResultStoreKey) obj;
        return (runId.equals(key.runId) && clazz.equals(key.clazz));
    }

    @Override
    public int hashCode() {
        return runId.hashCode() ^ clazz.hashCode();
    }
}
