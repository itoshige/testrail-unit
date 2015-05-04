/**
 * Copyright 2015 itoshige1017@gmail.com
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package com.github.itoshige.testrail.model;

import java.util.HashMap;
import java.util.Map;

/**
 * add section model
 * 
 * @author itoshige
 * 
 */
public class SectionModel {

    private String id;
    private String name;

    public SectionModel(String id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public Map<String, Object> getSection() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("suite_id", id);
        data.put("name", name);
        return data;
    }

    public String getName() {
        return name;
    }
}
