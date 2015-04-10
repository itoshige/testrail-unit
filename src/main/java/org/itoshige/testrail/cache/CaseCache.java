package org.itoshige.testrail.cache;

import org.apache.commons.collections.map.MultiKeyMap;
import org.itoshige.testrail.client.TestInitializerException;
import org.itoshige.testrail.client.TestRailClient;
import org.itoshige.testrail.client.TestRailUnitException;
import org.itoshige.testrail.util.JSONUtil;
import org.json.simple.JSONArray;

/**
 * Testrail case data
 * 
 * @author itoshige
 * 
 */
public class CaseCache {
    private static final CaseCache instance = new CaseCache();

    // sectionId, title to caseId
    private static final MultiKeyMap casesMap = new MultiKeyMap();

    public static CaseCache getIns() {
        return instance;
    }

    public String getCaseId(String sectionId, String title) {
        Object caseIdobj = casesMap.get(sectionId, title);
        if (caseIdobj != null)
            return caseIdobj.toString();
        throw new TestRailUnitException("caseId isn't in testrail. sectionId:" + sectionId + " title:"
            + title);
    }

    public void setCasesMap(String projectId, String suiteId) {
        JSONArray cases = TestRailClient.getCases(projectId, suiteId);
        MultiKeyMap map = JSONUtil.convertJsonArrayToMultiKeyMap(cases, "section_id", "title", "id");
        if (map == null || map.isEmpty())
            throw new TestInitializerException("case data isn't in testrail. projectId:" + projectId
                + " suiteId:" + suiteId);

        casesMap.putAll(map);
    }
}
