package edu.cmu.cs.cloud;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;


/**
 * A class that implements a distributed lock using Redis.
 */
public class RedisLock {
    private Jedis jedis;

    /**
     * Constructs a RedisLock with the specified Jedis client.
     *
     * @param jedis The Jedis client to use for Redis operations.
     */
    public RedisLock(Jedis jedis) {
        this.jedis = jedis;
    }

    /**
     * Attempts to acquire a lock with the given key and time-to-live (TTL).
     *
     * @param lockKey The key to use for the lock in Redis.
     * @param ttl The time-to-live for the lock in seconds.
     * @return true if the lock was successfully acquired, false otherwise.
     */
    public boolean acquireLock(String lockKey, Long ttl) {
        // used to set any additional options for the redis command
        SetParams setParams = new SetParams(); 
        setParams.nx().px(ttl);
        String uuid = java.util.UUID.randomUUID().toString();
        String result = jedis.set(lockKey, uuid, setParams);
        if ("OK".equals(result)) {
            return true;
        }
        return false;
    }

    /**
     * Attempts to release a previously acquired lock.
     * This function should return true only if the lock was
     * acquired and was has been successfully released.
     *
     * @param lockKey The key of the lock to release.
     * @return true if the lock was successfully released, false otherwise.
     */
    public boolean releaseLock(String lockKey) {
        String currentValue = jedis.get(lockKey);
        if (currentValue == null) {
            return false;
        }
        jedis.del(lockKey);
        return true;
    }
}