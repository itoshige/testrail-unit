/**
 * Copyright 2015 itoshige1017@gmail.com
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
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
