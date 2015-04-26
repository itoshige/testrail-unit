package com.github.itoshige.testrail.store;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.runner.Description;

import com.github.itoshige.testrail.client.TestRailClient;
import com.github.itoshige.testrail.client.TestRailUnitException;
import com.github.itoshige.testrail.model.CaseModel;
import com.github.itoshige.testrail.model.SectionModel;
import com.github.itoshige.testrail.model.TestResultModel;
import com.github.itoshige.testrail.model.store.CaseStoreKey;
import com.github.itoshige.testrail.model.store.RunStoreValue;
import com.github.itoshige.testrail.model.store.SectionStoreKey;
import com.github.itoshige.testrail.model.store.TestResultStoreKey;
import com.github.itoshige.testrail.util.ConfigrationUtil;
import com.github.itoshige.testrail.util.TestRailUnitUtil;

/**
 * TestRail Data Store manager
 * 
 * @author itoshige
 * 
 */
public class SyncManager {

    /** sectionStore */
    private final static Set<RunStoreValue> alreadySyncSection = Collections
        .newSetFromMap(new ConcurrentHashMap<RunStoreValue, Boolean>());

    /** caseStore */
    private final static Set<RunStoreValue> alreadySyncCases = Collections
        .newSetFromMap(new ConcurrentHashMap<RunStoreValue, Boolean>());

    /************ update ************/
    /**
     * store testrail data to stores.
     * 
     * @param runId
     * @param testClass
     * @return
     */
    public static String storeTestRailData(String runId, Class<?> testClass) {

        // get projectId and suiteId
        RunStoreValue value = getRunStoreValue(runId);
        String projectId = value.getProjectId();
        String suiteId = value.getSuiteId();

        storeCases(projectId, suiteId);

        syncTestClasses(projectId, runId, suiteId, testClass);

        String sectionId = createSection(projectId, runId, suiteId, testClass);
        syncTestMethod(sectionId, testClass);

        storeTests(runId);

        return projectId;
    }

    /**
     * store junit test result to testResultStore.
     * 
     * @param key
     * @param result
     */
    public static void storeJunitTestResult(TestResultStoreKey key, TestResultModel result) {
        TestResultStore.getIns().setResult(key, result);
    }

    private static RunStoreValue getRunStoreValue(String runId) {
        return RunStore.getIns().getRun(runId);
    }

    private static void syncTestMethod(String sectionId, final Class<?> testClass) {

        addNewTestMethod(sectionId, testClass);

        if (ConfigrationUtil.isSync())
            deleteNotExistedMethod(sectionId, testClass);
    }

    private static String createSection(String projectId, String runId, String suiteId,
        final Class<?> testClass) {
        String sectionName = TestRailUnitUtil.getSectionName(testClass);
        String sectionId = "";
        try {
            sectionId = SectionStore.getIns().getSectionId(projectId, runId, sectionName);
        } catch (TestRailUnitException e) {
        }

        // if testrail doesn't have section, this tool create sectionId.
        if (sectionId == null || sectionId.isEmpty()) {
            JSONObject createdSection = TestRailClient.addSection(projectId, new SectionModel(suiteId,
                sectionName));
            sectionId = SectionStore.getIns().createSection(createdSection, projectId, runId);
        }

        return sectionId;
    }

    /**
     * add new test method in testrail and caseStore.
     * 
     * @param sectionId
     * @param testClass
     */
    private static void addNewTestMethod(String sectionId, final Class<?> testClass) {
        for (final Method method : TestRailUnitUtil.getDeclaredTestMethods(testClass.getDeclaredMethods())) {
            String caseId = "";
            try {
                caseId = CaseStore.getIns().getCaseId(new CaseStoreKey(sectionId, method.getName()));
            } catch (TestRailUnitException e) {
            }

            if (needToAddTestMethod(caseId, method)) {
                String title = method.getName();
                JSONObject createdCase = TestRailClient.addCase(sectionId, new CaseModel(title));
                CaseStore.getIns().add(createdCase, sectionId, title);
            }
        }
    }

    /**
     * delete not existed method in testrail and caseStore.
     * 
     * @param sectionId
     * @param testClass
     */
    private static void deleteNotExistedMethod(String sectionId, final Class<?> testClass) {
        Set<String> junitMethodNames = new HashSet<String>();
        for (final Method junitMethod : TestRailUnitUtil.getDeclaredTestMethods(testClass
            .getDeclaredMethods())) {
            junitMethodNames.add(junitMethod.getName());
        }

        // find not existed titles in testrail.
        List<CaseStoreKey> notExistedTitles = new ArrayList<CaseStoreKey>();
        for (CaseStoreKey key : CaseStore.getIns().getSectionId2Tiltes()) {
            if (key.getProjectId().equals(sectionId) && !junitMethodNames.contains(key.getTitle()))
                notExistedTitles.add(key);
        }

        for (CaseStoreKey key : notExistedTitles) {
            // delete testrail
            TestRailClient.deleteCase(CaseStore.getIns().getCaseId(key));
            // remove store
            CaseStore.getIns().remove(key);
        }
    }

    private static void syncTestClasses(String projectId, String runId, String suiteId, Class<?> testClass) {
        RunStoreValue key = new RunStoreValue(projectId, suiteId);

        if (alreadySyncSection.contains(key))
            return;

        storeSections(projectId, runId, suiteId, testClass);

        if (ConfigrationUtil.isSync())
            deleteUnnessarySections(projectId, runId);

        alreadySyncSection.add(key);
    }

    private static void storeSections(String projectId, String runId, String suiteId, Class<?> testClass) {
        JSONArray sections = TestRailClient.getSections(projectId, suiteId);
        SectionStore.getIns().setSectionMap(sections, projectId, runId, suiteId);
    }

    private static void deleteUnnessarySections(String projectId, String runId) {
        List<SectionStoreKey> notExistedSections = SectionStore.getIns().getDeletedSections(projectId, runId);

        for (SectionStoreKey key : notExistedSections) {
            TestRailClient.deleteSection(SectionStore.getIns().getSectionId(key));
        }

    }

    private static void storeCases(String projectId, String suiteId) {
        RunStoreValue key = new RunStoreValue(projectId, suiteId);
        if (alreadySyncCases.contains(key))
            return;

        JSONArray cases = TestRailClient.getCases(projectId, suiteId);
        CaseStore.getIns().setCasesMap(cases, projectId, suiteId);
        alreadySyncCases.add(key);
    }

    private static void storeTests(String runId) {
        JSONArray tests = TestRailClient.getTests(runId);
        TestStore.getIns().setTestsMap(tests, runId);
    }

    /************ select ************/
    /**
     * get testId from stores.
     * 
     * @param projectId
     * @param desc
     * @return
     */
    public static String getTestId(String projectId, String runId, Description desc) {
        String sectionName = TestRailUnitUtil.getSectionName(desc.getTestClass());
        String title = desc.getMethodName();

        String sectionId = SectionStore.getIns().getSectionId(projectId, runId, sectionName);
        String caseId = CaseStore.getIns().getCaseId(new CaseStoreKey(sectionId, title));
        return TestStore.getIns().getTestId(caseId);
    }

    /**
     * get junit test result from testResultStore.
     * 
     * @param runId2Class
     * @return
     */
    public static Map<String, List<Map<String, Object>>> getJunitTestResults(TestResultStoreKey runId2Class) {
        Map<String, List<Map<String, Object>>> results = TestResultStore.getIns().getResults(runId2Class);
        if (results == null || results.isEmpty() || results.get("results").isEmpty())
            return new HashMap<String, List<Map<String, Object>>>();

        return results;
    }

    private static boolean needToAddTestMethod(String caseId, final Method method) {
        return needToAddTestRail(caseId, new Predicate<Method>() {
            @Override
            public boolean apply() {
                return TestRailUnitUtil.isSkipMethod(method);
            }
        });
    }

    private static <T> boolean needToAddTestRail(String value, Predicate<T> predicate) {
        if (predicate.apply())
            return false;

        return value == null || value.isEmpty();
    }

    private interface Predicate<T> {
        boolean apply();
    }
}
