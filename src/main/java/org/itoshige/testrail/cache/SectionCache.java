package org.itoshige.testrail.cache;

import java.util.concurrent.ConcurrentHashMap;

import org.itoshige.testrail.client.TestInitializerException;
import org.itoshige.testrail.client.TestRailClient;
import org.itoshige.testrail.client.TestRailUnitException;
import org.itoshige.testrail.util.CollectionUtil;
import org.itoshige.testrail.util.JSONUtil;
import org.json.simple.JSONArray;

/**
 * Testrail section data
 * 
 * @author itoshige
 * 
 */
public class SectionCache {
    private static final SectionCache instance = new SectionCache();

    // sectionName to sectionId
    private final ConcurrentHashMap<String, String> sectionMap = CollectionUtil.newConcurrentMap();

    public static SectionCache getIns() {
        return instance;
    }

    public String getSectionId(String sectionName) {
        String sectionId = sectionMap.get(sectionName);
        if (sectionId != null)
            return sectionId;
        throw new TestRailUnitException("sectionId isn't in testrail. sectionName:" + sectionName);
    }

    public void setSectionMap(String projectId, String suiteId) {
        JSONArray sections = TestRailClient.getSections(projectId, suiteId);
        JSONUtil.convertJsonArrayToMap(sections, sectionMap, "name", "id");
        if (sectionMap == null || sectionMap.isEmpty())
            throw new TestInitializerException("section data isn't in testrail. projectId:" + projectId
                + " suiteId:" + suiteId);
    }
}
