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
