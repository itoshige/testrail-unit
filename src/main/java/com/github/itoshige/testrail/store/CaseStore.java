/**
 * Copyright 2015 itoshige1017@gmail.com
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package com.github.itoshige.testrail.store;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.github.itoshige.testrail.client.TestRailUnitException;
import com.github.itoshige.testrail.model.store.CaseStoreKey;
import com.github.itoshige.testrail.util.CollectionUtil;

/**
 * Testrail case data
 * 
 * @author itoshige
 * 
 */
public class CaseStore {

    private static final CaseStore instance = new CaseStore();

    // sectionId, title to caseId
    private final ConcurrentHashMap<CaseStoreKey, String> casesMap = CollectionUtil.newConcurrentMap();

    private CaseStore() {
    }

    static CaseStore getIns() {
        return instance;
    }

    public Set<CaseStoreKey> getSectionId2Tiltes() {
        return Collections.unmodifiableSet(casesMap.keySet());
    }

    public String getCaseId(CaseStoreKey key) {
        String caseIdobj = casesMap.get(key);
        if (caseIdobj != null)
            return caseIdobj;

        throw new TestRailUnitException(String.format("caseId isn't in testrail. sectionId:%s title:%s",
            key.getProjectId(), key.getTitle()));
    }

    public void setCasesMap(JSONArray cases, String projectId, String suiteId) {
        copyJsonArrayToMap(cases, casesMap, "section_id", "title", "id");
    }

    public void add(JSONObject createdCase, String sectionId, String title) {
        copyJsonObjToMap(createdCase, casesMap, sectionId, "title", "id");
    }

    public void remove(CaseStoreKey sectionId2Title) {
        casesMap.remove(sectionId2Title);
    }

    private void copyJsonArrayToMap(JSONArray from, ConcurrentHashMap<CaseStoreKey, String> to, String key1,
        String key2, String value) {
        for (int i = 0; i < from.size(); i++) {
            JSONObject obj = (JSONObject) from.get(i);
            Object k1 = obj.get(key1);
            Object k2 = obj.get(key2);
            Object v = obj.get(value);
            if (k1 != null && k2 != null && v != null) {
                to.putIfAbsent(new CaseStoreKey(k1.toString().trim(), k2.toString().trim()), v.toString()
                    .trim());
            }
        }
    }

    private void copyJsonObjToMap(JSONObject from, ConcurrentHashMap<CaseStoreKey, String> to,
        String key1Value, String key2, String value) {
        Object k2 = from.get(key2);
        Object v = from.get(value);
        if (key1Value != null && k2 != null && v != null) {
            to.putIfAbsent(new CaseStoreKey(key1Value, k2.toString().trim()), v.toString().trim());
        }
    }
}
