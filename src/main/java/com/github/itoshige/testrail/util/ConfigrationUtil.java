/**
 * Copyright 2015 itoshige1017@gmail.com
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package com.github.itoshige.testrail.util;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.itoshige.testrail.client.TestInitializerException;
import com.github.itoshige.testrail.model.config.RunIdInfo;
import com.github.itoshige.testrail.model.config.RunIdInfoComparator;

/**
 * TestRail client info
 * 
 * @author itoshige
 * 
 */
public class ConfigrationUtil {
    public static final String CONFIG_FILE = "testrail-unit.json";
    private static final Logger logger = LoggerFactory.getLogger(ConfigrationUtil.class);

    /**
     * get testrail api info.
     * 
     * @return ClientInfoModel
     */
    public static ClientInfoModel getClientInfo() {
        try {
            return getInfo();
        } catch (TestInitializerException e) {
            logger.error("[STOP TEST]init error exception:{}", e);
            System.exit(1);
            return null;
        }
    }

    /**
     * check test rail unit disabled
     * 
     * @return boolean
     */
    public static boolean isDisabled() {
        JSONObject jsonObject = getConfig();
        return assureBoolean(jsonObject, "isDisabled");
    }

    /**
     * sync junit test and testrail
     * 
     * @return boolean
     */
    public static boolean isSync() {
        JSONObject jsonObject = getConfig();
        return assureBoolean(jsonObject, "sync");
    }

    public static boolean isTestRailDebugEnabled() {
        JSONObject jsonObject = getConfig();
        try {
            return assureBoolean(jsonObject, "isDebugEnabled");
        } catch (TestInitializerException e) {
            logger.debug("isDebugEnabled flag doesn't exist.");
            return false;
        }
    }

    public static class ClientInfoModel {
        private String url;
        private String user;
        private String password;

        private ClientInfoModel(String url, String user, String password) {
            super();
            this.url = url;
            this.user = user;
            this.password = password;
        }

        public String getUrl() {
            return url;
        }

        public String getUser() {
            return user;
        }

        public String getPassword() {
            return password;
        }
    }

    private static ClientInfoModel getInfo() {
        JSONObject jsonObject = getConfig();

        String url = assureValue(jsonObject, "url");
        String user = assureValue(jsonObject, "user");
        String password = assureValue(jsonObject, "password");

        return new ClientInfoModel(url, user, password);
    }

    private static String assureValue(JSONObject jsonObject, String key) {
        String value = (String) jsonObject.get(key);
        if (value == null || value.isEmpty())
            throw new TestInitializerException(String.format("key:%s is null or Empty", key));
        return value;
    }

    private static boolean assureBoolean(JSONObject jsonObject, String key) {
        try {
            return (Boolean) jsonObject.get(key);
        } catch (Exception e) {
            throw new TestInitializerException(String.format("key:%s is invalid.", key));
        }
    }

    /**
     * get runId of target test.
     * 
     * @param packageName
     * @return
     */
    public static String getRunId(String classPath) {
        JSONObject jsonObject = getConfig();

        JSONArray runIds = (JSONArray) jsonObject.get("runIds");
        for (RunIdInfo runIdInfo : getRunIdInfos(runIds)) {
            if (classPath.indexOf(runIdInfo.getTarget()) != -1) {
                logger.debug("target path:{}", runIdInfo.getTarget());
                return runIdInfo.getRunId();
            }
        }
        throw new TestInitializerException(String.format("classPath:%s isn't defined runId in %s.",
            classPath, CONFIG_FILE));
    }

    /**
     * get packages of target runId
     * 
     * @param runId
     * @return
     */
    public static List<String> getPackages(String runId) {
        JSONObject jsonObject = getConfig();
        JSONArray runIds = (JSONArray) jsonObject.get("runIds");

        List<String> packages = new ArrayList<String>();
        for (RunIdInfo runIdInfo : getRunIdInfos(runIds)) {
            if (runIdInfo.getRunId().equals(runId)) {
                packages.add(runIdInfo.getTarget());
            }
        }
        return packages;
    }

    private static List<RunIdInfo> getRunIdInfos(JSONArray runIds) {
        List<RunIdInfo> runIdInfos = new ArrayList<RunIdInfo>();
        for (int i = 0; i < runIds.size(); i++) {
            JSONObject runIdInfo = (JSONObject) runIds.get(i);
            runIdInfos.add(new RunIdInfo(runIdInfo.get("target"), runIdInfo.get("runId")));
        }
        Collections.sort(runIdInfos, new RunIdInfoComparator());
        return runIdInfos;
    }

    private static final ConcurrentHashMap<String, JSONObject> configs = CollectionUtil.newConcurrentMap();

    private static JSONObject getConfig() {
        try {
            JSONObject obj = configs.get(CONFIG_FILE);
            if (obj != null && !obj.isEmpty())
                return obj;

            JSONParser parser = new JSONParser();
            configs.putIfAbsent(CONFIG_FILE,
                (JSONObject) parser.parse(new FileReader(FilePathSearchUtil.getPath(CONFIG_FILE))));

            return configs.get(CONFIG_FILE);
        } catch (Exception e) {
            throw new TestInitializerException(String.format("%s can't be read.", CONFIG_FILE), e);
        }
    }
}
