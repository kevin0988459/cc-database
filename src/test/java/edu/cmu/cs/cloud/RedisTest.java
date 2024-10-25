package edu.cmu.cs.cloud;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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
        throw new RuntimeException("add test cases on your own");
    }

    @Test
    void hget() {
        throw new RuntimeException("add test cases on your own");
    }

    @Test
    void hgetall() {
        throw new RuntimeException("add test cases on your own");
    }

    @Test
    void llen() {
        throw new RuntimeException("add test cases on your own");
    }

    @Test
    void rpush() {
        throw new RuntimeException("add test cases on your own");
    }

    @Test
    void rpop() {
        throw new RuntimeException("add test cases on your own");
    }
}