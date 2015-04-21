package com.github.itoshige.testrail.store;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.github.itoshige.testrail.client.Pair;
import com.github.itoshige.testrail.client.TestRailUnitException;
import com.github.itoshige.testrail.util.CollectionUtil;
import com.github.itoshige.testrail.util.JSONUtil;

/**
 * Testrail case data
 * 
 * @author itoshige
 * 
 */
public class CaseStore {

    private static final CaseStore instance = new CaseStore();

    // sectionId, title to caseId
    private final ConcurrentHashMap<Pair<String, String>, String> casesMap = CollectionUtil
        .newConcurrentMap();

    static CaseStore getIns() {
        return instance;
    }

    public Set<Pair<String, String>> getSectionId2Tiltes() {
        return casesMap.keySet();
    }

    public String getCaseId(Pair<String, String> sectionId2Title) {
        Object caseIdobj = casesMap.get(sectionId2Title);
        if (caseIdobj != null)
            return caseIdobj.toString();

        throw new TestRailUnitException(new StringBuilder("caseId isn't in testrail. sectionId:")
            .append(sectionId2Title.getFirst()).append(" title:").append(sectionId2Title.getSecond())
            .toString());
    }

    public void setCasesMap(JSONArray cases, String projectId, String suiteId) {
        JSONUtil.copyJsonArrayToMultiKeyMap(cases, casesMap, "section_id", "title", "id");
    }

    public void add(JSONObject createdCase, String sectionId, String title) {
        JSONUtil.copyJsonObjToMap(createdCase, casesMap, sectionId, "title", "id");
    }

    public void remove(Pair<String, String> sectionId2Title) {
        casesMap.remove(sectionId2Title);
    }
}
