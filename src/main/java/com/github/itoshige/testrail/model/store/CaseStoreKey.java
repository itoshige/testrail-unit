/**
 * Copyright 2015 itoshige1017@gmail.com
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package com.github.itoshige.testrail.model.store;

/**
 * Case Store Key Model
 * 
 * @author itoshige
 * 
 */
public class CaseStoreKey {

    private final String projectId;
    private final String title;

    public CaseStoreKey(String projectId, String title) {
        super();
        this.projectId = projectId;
        this.title = title;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CaseStoreKey))
            return false;
        CaseStoreKey key = (CaseStoreKey) obj;
        return (projectId.equals(key.projectId) && title.equals(key.title));
    }

    @Override
    public int hashCode() {
        return projectId.hashCode() ^ title.hashCode();
    }
}
