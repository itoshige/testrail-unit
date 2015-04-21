package com.github.itoshige.testrail.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.github.itoshige.testrail.client.Pair;
import com.github.itoshige.testrail.client.TestRailUnitException;
import com.github.itoshige.testrail.util.CollectionUtil;
import com.github.itoshige.testrail.util.JSONUtil;
import com.github.itoshige.testrail.util.PackageClassSearchUtil;

/**
 * Testrail section data
 * 
 * @author itoshige
 * 
 */
public class SectionStore {
    private static final SectionStore instance = new SectionStore();

    // projectId, sectionName to sectionId
    private final ConcurrentHashMap<Pair<String, String>, String> sectionMap = CollectionUtil
        .newConcurrentMap();

    static SectionStore getIns() {
        return instance;
    }

    public String getSectionId(String projectId, String sectionName) {
        String sectionId = sectionMap.get(new Pair<String, String>(projectId, sectionName));
        if (sectionId != null)
            return sectionId;
        throw new TestRailUnitException(new StringBuilder("sectionId isn't in testrail. sectionName:")
            .append(sectionName).toString());
    }

    public String getSectionId(Pair<String, String> projectId2sectionName) {
        return getSectionId(projectId2sectionName.getFirst(), projectId2sectionName.getSecond());
    }

    public List<Pair<String, String>> getDeletedSections(String projectId, String runId) {
        Set<String> testClasses = PackageClassSearchUtil.getTestClasses(runId);

        List<Pair<String, String>> notExistedSections = new ArrayList<Pair<String, String>>();
        for (Pair<String, String> pair : sectionMap.keySet()) {
            if (pair.getFirst().equals(projectId) && !testClasses.contains(pair.getSecond()))
                notExistedSections.add(pair);
        }
        return notExistedSections;
    }

    public void setSectionMap(JSONArray sections, String projectId, String suiteId) {
        JSONUtil.copyJsonArrayToMultiKeyMap2(sections, sectionMap, projectId, "name", "id");
    }

    public String createSection(JSONObject createdSection, String projectId) {
        JSONUtil.copyJsonObjToMap(createdSection, sectionMap, projectId, "name", "id");
        return createdSection.get("id").toString();
    }
}
