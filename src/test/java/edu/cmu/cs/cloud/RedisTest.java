package edu.cmu.cs.cloud;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Usage:
 * mvn test
 *
 * <p>You should pass all the provided test cases before you make any submission.
 *
 * <p>Feel free to add more test cases.
 */
class RedisTest {

    /**
     * Used Java Reflection to access {@link Redis#store} which is private.
     *
     * @throws NoSuchFieldException   if a field with the specified name is
     *                                not found.
     * @throws IllegalAccessException if this {@code Field} object
     *                                is enforcing Java language access control
     *                                and the underlying field is either inaccessible or final.
     */
    @Test
    void type() throws NoSuchFieldException, IllegalAccessException {
        Redis redisClient = new Redis();

        // use reflection to access the private field
        Field field = redisClient.getClass().getDeclaredField("store");
        field.setAccessible(true);
        HashMap<String, Object> store = new HashMap<>();
        store.put("unknown", new Object());
        field.set(redisClient, store);

        assertEquals("OK", redisClient.set("mykey", "cloud"));
        assertEquals("string", redisClient.type("mykey"));

        redisClient.hset("hash", "myfield", "myvalue");
        assertEquals("hash", redisClient.type("hash"));

        redisClient.rpush("list", "myvalue1", "myvalue2", "myvalue3");
        assertEquals("list", redisClient.type("list"));

        assertEquals("none", redisClient.type("the key does not exist"));
        assertEquals("unknown", redisClient.type("unknown"));

    }

    @Test
    void checkType() {
        Redis redisClient = new Redis();
        assertEquals("OK", redisClient.set("mykey", "cloud"));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> redisClient.checkType("mykey", "HASH"));
    }

    @Test
    void set() {
        Redis redisClient = new Redis();

        assertEquals("OK", redisClient.set("mykey", "cloud"));
        assertEquals("OK", redisClient.set("mykey", "cool"));
        assertEquals("OK", redisClient.set("secondkey", "yes"));
        assertNotEquals("Random", redisClient.set("thirdkey", "no"));
    }

    @Test
    void get() {
        Redis redisClient = new Redis();

        assertNull(redisClient.get("mykey"));

        redisClient.set("mykey", "cloud");
        assertNotNull(redisClient.get("mykey"));
        assertEquals("cloud", redisClient.get("mykey"));

        redisClient.set("mykey", "cool");
        assertNotEquals("cloud", redisClient.get("mykey"));
        assertEquals("cool", redisClient.get("mykey"));
    }

    @Test
    void del() {
        Redis redisClient = new Redis();

        assertEquals(0, redisClient.del("mykey"));

        redisClient.set("mykey1", "cloud");
        redisClient.set("mykey2", "cool");
        redisClient.set("mykey3", "awesome");
        assertEquals(2, redisClient.del("mykey1", "mykey3"));

        assertNotEquals(0, redisClient.del("mykey2"));
    }

    @Test
    void hset() {
        Redis redisClient = new Redis();

        assertEquals(1, redisClient.hset("hash1", "field1", "value1"));
        assertEquals(0, redisClient.hset("hash1", "field1", "value2"));
        assertEquals(1, redisClient.hset("hash1", "field2", "value3"));
        assertEquals(1, redisClient.hset("hash2", "field1", "value1"));
    }

    @Test
    void hget() {
        Redis redisClient = new Redis();

        assertNull(redisClient.hget("nonExistingHash", "field1"));

        redisClient.hset("hash1", "field1", "value1");
        redisClient.hset("hash1", "field2", "value2");

        assertEquals("value1", redisClient.hget("hash1", "field1"));
        assertEquals("value2", redisClient.hget("hash1", "field2"));

        assertNull(redisClient.hget("hash1", "field3"));
    }

    @Test
    void hgetall() {
        Redis redisClient = new Redis();

        List<String> result = redisClient.hgetall("nonExistingHash");
        assertNotNull(result);
        assertTrue(result.isEmpty());

        redisClient.hset("hash1", "field1", "value1");
        redisClient.hset("hash1", "field2", "value2");

        result = redisClient.hgetall("hash1");
        assertEquals(4, result.size()); 
        assertTrue(result.contains("field1"));
        assertTrue(result.contains("value1"));
        assertTrue(result.contains("field2"));
        assertTrue(result.contains("value2"));
    }

    @Test
    void llen() {
        Redis redisClient = new Redis();

        assertEquals(0, redisClient.llen("nonExistingList"));

        redisClient.rpush("list1");
        assertEquals(0, redisClient.llen("list1"));

        redisClient.rpush("list1", "value1");
        assertEquals(1, redisClient.llen("list1"));

        redisClient.rpush("list1", "value2", "value3");
        assertEquals(3, redisClient.llen("list1"));

        redisClient.set("stringKey", "someValue");
    }

    @Test
    void rpush() {
        Redis redisClient = new Redis();

        assertEquals(2, redisClient.rpush("list1", "value1", "value2"));

        assertEquals(4, redisClient.rpush("list1", "value3", "value4"));

        assertEquals(4, redisClient.rpush("list1"));

        assertEquals(6, redisClient.rpush("list1", "value5", "value6"));
    }

    @Test
    void rpop() {
        Redis redisClient = new Redis();

        assertNull(redisClient.rpop("nonExistingList"));

        redisClient.rpush("list1");
        assertNull(redisClient.rpop("list1"));

        redisClient.rpush("list1", "value1", "value2", "value3");
        assertEquals("value3", redisClient.rpop("list1"));
        assertEquals(2, redisClient.llen("list1"));

        assertEquals("value2", redisClient.rpop("list1"));
        assertEquals(1, redisClient.llen("list1"));

        assertEquals("value1", redisClient.rpop("list1"));
        assertEquals(0, redisClient.llen("list1"));

        assertNull(redisClient.rpop("list1"));
    }
}