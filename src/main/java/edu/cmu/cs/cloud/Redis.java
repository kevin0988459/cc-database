package edu.cmu.cs.cloud;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * A simplified version of Redis.
 *
 * This is simplified in the aspects including but not limited to:
 * 1. This program only covers the three most basic data structures with a
 * subset of the storage commands.
 * 2. This program is single-node.
 * 3. This program is not thread-safe.
 *
 * You can use <a href="http://try.redis.io/">Try Redis</a> as the reference
 * Redis interface.
 *
 * You are not allowed to use a 3rd-party library.
 * Only <a href="https://docs.oracle.com/javase/8/docs/api/">built-in APIs
 * of Java SE 8</a> are allowed to import.
 */
public class Redis {

    /**
     * We will implement the three most basic data structures supported in
     * Redis.
     *
     * Redis Strings.
     * It is the only data type in Memcached.
     * {@link #set(String, String)}
     * {@link #get(String)}
     *
     * Redis Hashes.
     * {@link #hset(String, String, String)}
     * {@link #hget(String, String)}
     * {@link #hgetall(String)}
     *
     * Redis Lists.
     * Redis lists are implemented via {@link LinkedList}.
     * For a database system it is crucial to be able to add elements to a
     * very long list in a very fast way.
     * {@link #rpop(String)}
     * {@link #rpush(String, String...)}
     * {@link #llen(String)}
     *
     * General operations.
     * {@link #del(String...)}
     *
     * Please implement the unfinished functions, which are marked by
     * {@code new UnsupportedOperationException("Waiting to be implemented")}
     *
     * Throw {@link UnsupportedOperationException} when performing an
     * operation against the wrong type given a key using
     * {@link #checkType(String, String)}.
     */
    private HashMap<String, Object> store = new HashMap<>();

    /**
     * The exception message when performing an operation against the wrong
     * type given a key.
     */
    private static final String WRONG_TYPE_MESSAGE =
            "WRONGTYPE Operation against a key holding the wrong kind of value";
    /**
     * The string representation of the types of the value supported by Redis.
     */
    private static final String STRING = "string";
    private static final String HASH = "hash";
    private static final String LIST = "list";

    /**
     * Unknown type, used in {@link #type(String)}.
     */
    private static final String UNKNOWN = "unknown";

    /**
     * The string representation of when the key does not exist.
     */
    private static final String NONE = "none";

    /**
     * Returns the string representation of the type of the value stored at
     * key.
     *
     * The different types that can be returned are: string, list and hash.
     *
     * @param k key
     * @return the type of the value stored at key, or "none" when the key does
     * not exist.
     * @see <a href="https://redis.io/commands/type">TYPE - Redis</a>
     */
    public String type(String k) {
        if (store.containsKey(k)) {
            Object v = store.get(k);
            if (v instanceof String) {
                return STRING;
            } else if (v instanceof HashMap) {
                return HASH;
            } else if (v instanceof List) {
                return LIST;
            } else {
                return UNKNOWN;
            }
        }
        return NONE;
    }

    /**
     * Set key to hold the string value.
     *
     * SET will replace any existing value already stored into the key, in the
     * case that the key already exists, even if the key is associated with a
     * non-string value.
     *
     * @param k key
     * @param v string value
     * @return "OK" to indicate SET was executed correctly
     * @see <a href="https://redis.io/commands/set">SET - Redis</a>
     */
    public String set(String k, String v) {
        store.put(k, v);
        return "OK";
    }

    /**
     * Check if the value stored at key matches the type.
     *
     * type: STRING HASH LIST
     *
     * Throws {@link UnsupportedOperationException} if the value
     * stored at key exists but does not match the type.
     *
     * @param k    key
     * @param type to check
     */
    void checkType(String k, String type) {
        if (!type(k).equals(NONE) && !type(k).equals(type)) {
            throw new UnsupportedOperationException(WRONG_TYPE_MESSAGE);
        }
    }

    /**
     * Get the string value of key.
     *
     * An error is returned if the value stored at key is not a string,
     * because GET only handles string values.
     *
     * @param k the key
     * @return the string value if the key exists,
     * or null when key does not exist
     * @see <a href="https://redis.io/commands/get">GET - Redis</a>
     */
    public String get(String k) {
        checkType(k, STRING);
        return (String) store.get(k);
    }

    /**
     * Removes the specified keys. E.g. {@code del(key1, key2, key3)}
     *
     * A key is ignored if it does not exist.
     *
     * @param ks keys to remove
     * @return the number of keys that were removed
     * @see <a href="https://redis.io/commands/del">DEL - Redis</a>
     */
    public int del(String... ks) {
        int n = 0;
        for (String k : ks) {
            if (store.containsKey(k)) {
                store.remove(k);
                n++;
            }
        }
        return n;
    }


    /**
     * Sets field in the hash stored at key to value.
     *
     * If key does not exist, a new key holding a hash is created.
     *
     * If field already exists in the hash, it is overwritten.
     *
     * @param k key
     * @param f field
     * @param v value
     * @return 1 if field is a new field in the hash and value was set,
     * 0 if field already exists in the hash and the value was updated.
     * @see <a href="https://redis.io/commands/hset">HSET - Redis</a>
     */
    public int hset(String k, String f, String v) {
        checkType(k, HASH);
        @SuppressWarnings("unchecked")
        HashMap<String, String> m = (HashMap<String, String>) store.get(k);
        if (m == null) {
            m = new HashMap<>();
            store.put(k, m);
        }
        int initialSize = m.size();
        m.put(f, v);
        int finalSize = m.size();
        return finalSize > initialSize ? 1 : 0;
    }

    /**
     * Returns the value associated with field in the hash stored at key.
     *
     * @param k key
     * @param f field
     * @return the value associated with field, or null when field is not
     * present in the hash or key does not exist.
     * @see <a href="https://redis.io/commands/hget">HGET - Redis</a>
     */
    public String hget(String k, String f) {
        checkType(k, HASH);
        @SuppressWarnings("unchecked")
        HashMap<String, String> m = (HashMap<String, String>) store.get(k);
        if (m == null) {
            return null;
        }
        return m.get(f);
    }

    /**
     * Returns all fields and values of the hash stored at key.
     *
     * In the returned value, every field name is followed by its value, so the
     * length of the reply is twice the size of the hash.
     *
     * @param k key
     * @return a list of fields and their values stored in the hash,
     * or an empty list when key does not exist.
     * @see <a href="https://redis.io/commands/hgetall">HGETALL - Redis</a>
     */
    public List<String> hgetall(String k) {
        checkType(k, HASH);
        List<String> list = new LinkedList<>();
        @SuppressWarnings("unchecked")
        HashMap<String, String> m = (HashMap<String, String>) store.get(k);
        if (m == null) {
            return list;
        }
        for (HashMap.Entry<String, String> entry: m.entrySet()) {
            list.add(entry.getKey());
            list.add(entry.getValue());
        }
        return list;
    }

    /**
     * Returns the length of the list stored at key.
     *
     * If key does not exist, it is interpreted as an empty list and 0 is
     * returned. An error is returned when the value stored at key is not a
     * list.
     *
     * @param k key
     * @return the length of the list at key
     * @see <a href="https://redis.io/commands/llen">LLEN - Redis</a>
     */
    public int llen(String k) {
        checkType(k, LIST);
        @SuppressWarnings("unchecked")
        LinkedList<String> list = (LinkedList<String>) store.get(k);
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    /**
     * Insert all the specified values at the tail of the list stored at key.
     *
     * If key does not exist, it is created as an empty list before performing the
     * push operation.
     *
     * When key holds a value that is not a list, an error is returned.
     *
     * @param k  key
     * @param vs values
     * @return the length of the list after the push operation
     * @see <a href="https://redis.io/commands/rpush">RPUSH - Redis</a>
     */
    public int rpush(String k, String... vs) {
        checkType(k, LIST);
        @SuppressWarnings("unchecked")
        LinkedList<String> list = (LinkedList<String>) store.get(k);
        if (list == null) {
            list = new LinkedList<>();
            store.put(k, list);
        }
        for (String v: vs) {
            list.addLast(v);
        }
        return list.size();
    }

    /**
     * Removes and returns the last element of the list stored at key.
     *
     * When key holds a value that is not a list, an error is returned.
     * If the list is empty, null value will be returned
     *
     * @param k the key
     * @return the value of the last element, or null when key does not exist.
     * @see <a href="https://redis.io/commands/rpop">RPOP - Redis</a>
     */
    public String rpop(String k) {
        checkType(k, LIST);
        @SuppressWarnings("unchecked")
        LinkedList<String> list = (LinkedList<String>) store.get(k);
        if (list == null) {
            return null;
        }
        return list.removeLast();
    }
}