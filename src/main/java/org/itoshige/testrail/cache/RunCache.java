package org.itoshige.testrail.cache;

import java.util.HashMap;
import java.util.Map;

import org.itoshige.testrail.client.Pair;
import org.itoshige.testrail.client.TestRailClient;

/**
 * TestRail Project & Suite Data with run_id as key
 * 
 * @author abhijeet.burle
 *
 * Created on 2015/04/19
 */
public class RunCache {
    private static final RunCache instance = new RunCache();

    // runId to Pair<projectId, suiteId>
    private final Map<String, Pair<String, String>> runDataMap = new HashMap<String, Pair<String, String>>();

    public static RunCache getIns() {
        return instance;
    }

    public boolean hasRun(String runId) {
    	return runDataMap.containsKey(runId);
    }
    
    public Pair<String, String> getRun(String runId) {
    	Pair<String, String> pair = runDataMap.get(runId);
    	if(pair==null){
    		synchronized(runDataMap) {
    			pair = runDataMap.get(runId);
    	    	if(pair==null){
    	    		TestCache.getIns().setTestsMap(runId);
    	            pair = TestRailClient.getRun(runId);
    	            runDataMap.put(runId,pair);

    	            SectionCache.getIns().setSectionMap(pair.getFirst(), pair.getSecond());
    	            CaseCache.getIns().setCasesMap(pair.getFirst(), pair.getSecond());

    	    	}
			}
    	}
    	return pair;
    }
}
