package com.github.itoshige.testrail.model.config;

import java.util.Comparator;

/**
 * sort package tree(high to low)
 * 
 * @author itoshige
 * 
 */
public class RunIdInfoComparator implements Comparator<RunIdInfo> {

    @Override
    public int compare(RunIdInfo o1, RunIdInfo o2) {
        return o2.getCommaCount() - o1.getCommaCount();
    }
}
