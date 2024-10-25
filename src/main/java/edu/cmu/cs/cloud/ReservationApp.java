package edu.cmu.cs.cloud;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.nio.file.Paths;

public class ReservationApp {

    private static final String CONFIGURATION_FILE = "configuration.yaml";
    private static String globalPath = Paths.get("src", "main", "resources", CONFIGURATION_FILE).toString();

    public static void main(String[] args) throws IOException {
        Config config = new Config(Paths.get(globalPath).toFile().getAbsolutePath());
        Jedis jedis = new Jedis(config.getHostName(), 6380, DefaultJedisClientConfig.builder()
                .password(config.getKey())
                .ssl(true)
                .build());

        System.out.println(jedis.ping());
    }
}
