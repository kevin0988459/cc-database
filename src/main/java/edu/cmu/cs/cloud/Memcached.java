package edu.cmu.cs.cloud;

import java.util.HashMap;

/**
 * A simplified version of Memcached.
 *
 * This is simplified in the aspects including but not limited to:
 * 1. This program only covers the most basic 3 storage commands.
 * 2. This program is single-node.
 * 3. This program is not thread-safe.
 */
public class Memcached {

    /**
     * In-memory k-v store.
     *
     * K: String
     * V: String is the only supported type in Memcached
     */
    private HashMap<String, String> store = new HashMap<>();

    /**
     * Set the value with the key in the cache regardless of any existing value.
     *
     * @param k the key
     * @param v the value
     * @return "STORED" to indicate success
     */
    public String set(String k, String v) {
        store.put(k, v);
        return "STORED";
    }

    /**
     * Get with a single key.
     *
     * @param k the key
     * @return the value, or null if there is none
     */
    public String get(String k) {
        return store.get(k);
    }

    /**
     * Delete the given key from the cache.
     *
     * @param k the key
     * @return "DELETED" to indicate success, or "NOT_FOUND" to indicate that
     * the item with this key was not found
     */
    public String delete(String k) {
        if (store.containsKey(k)) {
            store.remove(k);
            return "DELETED";
        }
        return "NOT_FOUND";
    }
}