package org.itoshige.testrail.util;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Collection Util
 * 
 * @author itoshige
 * 
 */
public class CollectionUtil {

    public static final <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<K, V>();
    }

    public static final <K, V> ConcurrentHashMap<K, V> newConcurrentMap() {
        return new ConcurrentHashMap<K, V>();
    }
}
