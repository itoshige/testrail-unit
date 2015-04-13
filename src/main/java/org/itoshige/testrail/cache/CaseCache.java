package org.itoshige.testrail.cache;

import java.util.concurrent.ConcurrentHashMap;

import org.itoshige.testrail.client.Pair;
import org.itoshige.testrail.client.TestInitializerException;
import org.itoshige.testrail.client.TestRailClient;
import org.itoshige.testrail.client.TestRailUnitException;
import org.itoshige.testrail.util.CollectionUtil;
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
    private final ConcurrentHashMap<Pair<String, String>, String> casesMap = CollectionUtil
        .newConcurrentMap();

    public static CaseCache getIns() {
        return instance;
    }

    public String getCaseId(Pair<String, String> sectionId2Title) {
        Object caseIdobj = casesMap.get(sectionId2Title);
        if (caseIdobj != null)
            return caseIdobj.toString();
        throw new TestRailUnitException(new StringBuilder("caseId isn't in testrail. sectionId:")
            .append(sectionId2Title.getFirst()).append(" title:").append(sectionId2Title.getSecond())
            .toString());
    }

    public void setCasesMap(String projectId, String suiteId) {
        JSONArray cases = TestRailClient.getCases(projectId, suiteId);
        JSONUtil.convertJsonArrayToMultiKeyMap(cases, casesMap, "section_id", "title", "id");
        if (casesMap == null || casesMap.isEmpty())
            throw new TestInitializerException("case data isn't in testrail. projectId:" + projectId
                + " suiteId:" + suiteId);
    }
}
