package com.github.itoshige.testrail.model;

import java.util.HashMap;
import java.util.Map;

/**
 * add test case model
 * 
 * @author itoshige
 * 
 */
public class CaseModel {

    private String title;

    public CaseModel(String title) {
        super();
        this.title = title;
    }

    public Map<String, Object> getCase() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("title", title);
        return data;
    }

    public String getTitle() {
        return title;
    }

}
