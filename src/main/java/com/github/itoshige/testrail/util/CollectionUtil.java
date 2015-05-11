/**
 * Copyright 2015 itoshige1017@gmail.com
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package com.github.itoshige.testrail.util;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Collection Util
 * 
 * @author itoshige
 * 
 */
public class CollectionUtil {

    public static final <K, V> ConcurrentHashMap<K, V> newConcurrentMap() {
        return new ConcurrentHashMap<K, V>();
    }
}
