/**
 * Copyright 2015 itoshige1017@gmail.com
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package com.github.itoshige.testrail.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.github.itoshige.testrail.client.TestRailUnitException;
import com.github.itoshige.testrail.model.store.SectionStoreKey;
import com.github.itoshige.testrail.util.CollectionUtil;
import com.github.itoshige.testrail.util.PackageClassSearchUtil;

/**
 * Testrail section data
 * 
 * @author itoshige
 * 
 */
public class SectionStore {
    private static final SectionStore instance = new SectionStore();

    // projectId, runId, sectionName to sectionId
    private final ConcurrentHashMap<SectionStoreKey, String> sectionMap = CollectionUtil.newConcurrentMap();

    private SectionStore() {
    }

    static SectionStore getIns() {
        return instance;
    }

    public String getSectionId(String projectId, String runId, String sectionName) {
        String sectionId = sectionMap.get(new SectionStoreKey(projectId, runId, sectionName));
        if (sectionId != null)
            return sectionId;
        throw new TestRailUnitException(String.format(
            "sectionId isn't in testrail. sectionName:%s projectId:%s runId:%s", sectionName, projectId,
            runId));
    }

    public String getSectionId(SectionStoreKey key) {
        return getSectionId(key.getProjectId(), key.getRunId(), key.getSectionName());
    }

    public List<SectionStoreKey> getDeletedSections(String projectId, String runId) {
        List<SectionStoreKey> notExistedSections = new ArrayList<SectionStoreKey>();
        for (SectionStoreKey key : sectionMap.keySet()) {
            if (needToDelete(key, projectId, runId))
                notExistedSections.add(key);
        }
        return notExistedSections;
    }

    private boolean needToDelete(SectionStoreKey key, String projectId, String runId) {
        Set<String> testClasses = PackageClassSearchUtil.getTestClasses(runId);

        return key.getProjectId().equals(projectId) && key.getRunId().equals(runId)
            && !testClasses.contains(key.getSectionName());
    }

    public void setSectionMap(JSONArray sections, String projectId, String runId, String suiteId) {
        copyJsonArrayToMap(sections, sectionMap, projectId, runId, "name", "id");
    }

    public String createSection(JSONObject createdSection, String projectId, String runId) {
        copyJsonObjToMap(createdSection, sectionMap, projectId, runId, "name", "id");
        return createdSection.get("id").toString();
    }

    private void copyJsonArrayToMap(JSONArray from, ConcurrentHashMap<SectionStoreKey, String> to,
        String projectId, String runId, String key2, String value) {
        for (int i = 0; i < from.size(); i++) {
            JSONObject obj = (JSONObject) from.get(i);
            Object k2 = obj.get(key2);
            Object v = obj.get(value);
            if (projectId != null && k2 != null && v != null) {
                to.putIfAbsent(new SectionStoreKey(projectId, runId, k2.toString().trim()), v.toString()
                    .trim());
            }
        }
    }

    private void copyJsonObjToMap(JSONObject from, ConcurrentHashMap<SectionStoreKey, String> to,
        String projectId, String runId, String key2, String value) {
        Object k2 = from.get(key2);
        Object v = from.get(value);
        if (projectId != null && k2 != null && v != null) {
            to.putIfAbsent(new SectionStoreKey(projectId, runId, k2.toString().trim()), v.toString().trim());
        }
    }
}
