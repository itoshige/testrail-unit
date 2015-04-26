package com.github.itoshige.testrail.model.store;

/**
 * Section Store Key Model
 * 
 * @author itoshige
 * 
 */
public class SectionStoreKey {

    private final String projectId;
    private final String runId;
    private final String sectionName;

    public SectionStoreKey(String projectId, String runId, String sectionName) {
        super();
        this.projectId = projectId;
        this.runId = runId;
        this.sectionName = sectionName;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getRunId() {
        return runId;
    }

    public String getSectionName() {
        return sectionName;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SectionStoreKey))
            return false;
        SectionStoreKey key = (SectionStoreKey) obj;
        return (projectId.equals(key.projectId) && runId.equals(key.runId) && sectionName
            .equals(key.sectionName));
    }

    @Override
    public int hashCode() {
        return projectId.hashCode() ^ runId.hashCode() ^ sectionName.hashCode();
    }
}
