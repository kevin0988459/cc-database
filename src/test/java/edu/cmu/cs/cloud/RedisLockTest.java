package edu.cmu.cs.cloud;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeAll;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.HostAndPort;

import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;


/**
 * Usage:
 * mvn test
 *
 * <p>You should pass all the provided test cases before you make any submission.
 *
 * <p>Feel free to add more test cases.
 */
class RedisLockTest {
    
    private static final String CONFIGURATION_FILE = "configuration.yaml";
    private static String globalPath = Paths.get("src", "main", "resources", CONFIGURATION_FILE).toString();
    private static JedisPool jedisPool;

    /**
     * Sets up the test environment before running any tests.
     * Initializes the JedisPool with configuration from a YAML file.
     *
     * @throws IOException if there's an error reading the configuration file
     */
    @BeforeAll
    static void setUp() throws IOException {
        GenericObjectPoolConfig<Jedis> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(50);

        Config config = new Config(Paths.get(globalPath).toFile().getAbsolutePath());
        
        jedisPool = new JedisPool(poolConfig, new HostAndPort(config.getHostName(), 6380), 
            DefaultJedisClientConfig.builder()
            .password(config.getKey())
            .ssl(true)
            .build());
    }

    @Test
    void acquireLockSuccess() {
        Jedis jedis = jedisPool.getResource();
        RedisLock redisLock = new RedisLock(jedis);
        jedis.flushAll();
        boolean b = redisLock.acquireLock("lockKey", 1000L);
        assertTrue(b);
    }

    @Test
    void aquireLockMultipleSuccess() {
        Jedis jedis = jedisPool.getResource();
        RedisLock redisLock = new RedisLock(jedis);
        jedis.flushAll();
        
        assertTrue(redisLock.acquireLock("lockKey", 1000L));
        
        assertTrue(redisLock.acquireLock("lockKey2", 1000L));
        
        assertTrue(redisLock.acquireLock("lockKey3", 1000L));
    }

    @Test
    void acquireLockFailure() {
        throw new RuntimeException("add test cases on your own");
    }

    @Test
    void acquireLockMultipleFailure() {
        throw new RuntimeException("add test cases on your own");
    }

    @Test
    void releaseLockSuccess() {
        throw new RuntimeException("add test cases on your own");
    }

    @Test
    void releaseLockFailure() {
        throw new RuntimeException("add test cases on your own");
    }

    @Test
    void lockExpiration() {
        throw new RuntimeException("add test cases on your own");
    }

    @Test
    void aquireAndRelease() {
        throw new RuntimeException("add test cases on your own");
    }

    @Test
    void releaseAfterTimeout() {
        throw new RuntimeException("add test cases on your own");
    }
}

