package edu.cmu.cs.cloud;

import org.junit.jupiter.api.Test;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.jupiter.api.BeforeAll;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Usage:
 * mvn test
 *
 * <p>You should pass all the provided test cases before you make any submission.
 *
 * <p>Feel free to add more test cases.
 */
class ServerTest {

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
    void reserveEmptyResource() {
        Jedis jedis = jedisPool.getResource();
        Server server = new Server(jedis);
        jedis.flushAll();
        assertTrue(server.reserve("resource1", 1));
    }

    @Test
    void reserveOccupiedResource() {
        throw new RuntimeException("add test cases on your own");
    }

    @Test
    void checkoutOccupiedAndOwnedResource() {
        throw new RuntimeException("add test cases on your own");
    }

    @Test
    void checkoutEmptyResource() {
        throw new RuntimeException("add test cases on your own");
    }

    @Test
    void checkoutUnownedResource() {
        throw new RuntimeException("add test cases on your own");
    }

    @Test
    void reserveAndCheckout() {
        throw new RuntimeException("add test cases on your own");
    }

    @Test
    void reserveAndCheckoutMultipleResources() {
        throw new RuntimeException("add test cases on your own");
    }
}
