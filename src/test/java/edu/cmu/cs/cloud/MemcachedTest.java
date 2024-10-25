package edu.cmu.cs.cloud;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Usage:
 * mvn test
 *
 * Test cases for Memcached.
 *
 * Feel free to add more tests.
 */
class MemcachedTest {

    @Test
    void set() {
        Memcached memcachedClient = new Memcached();

        assertEquals("STORED", memcachedClient.set("mykey", "cloud"));
        assertEquals("STORED", memcachedClient.set("mykey", "cool"));
        assertEquals("STORED", memcachedClient.set("secondkey", "yes"));
        assertNotEquals("Random", memcachedClient.set("thirdkey", "no"));
    }

    @Test
    void get() {
        Memcached memcachedClient = new Memcached();

        assertNull(memcachedClient.get("mykey"));

        memcachedClient.set("mykey", "cloud");
        assertNotNull(memcachedClient.get("mykey"));
        assertEquals("cloud", memcachedClient.get("mykey"));

        memcachedClient.set("mykey", "cool");
        assertNotEquals("cloud", memcachedClient.get("mykey"));
        assertEquals("cool", memcachedClient.get("mykey"));
    }

    @Test
    void delete() {
        Memcached memcachedClient = new Memcached();

        assertEquals("NOT_FOUND", memcachedClient.delete("mykey"));

        memcachedClient.set("mykey", "cloud");
        assertEquals("DELETED", memcachedClient.delete("mykey"));

        assertNotEquals("DELETED", memcachedClient.delete("mykey"));
    }
}
