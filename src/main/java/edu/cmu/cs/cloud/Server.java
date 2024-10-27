package edu.cmu.cs.cloud;

import redis.clients.jedis.Jedis;

/**
 * A server class that manages resource reservation and checkout using Redis.
 */
public class Server {
    private Jedis jedis;
    private RedisLock lock;

    /**
     * Constructs a Server with the specified Jedis client.
     *
     * @param jedis The Jedis client to use for Redis operations.
     */
    public Server(Jedis jedis) {
        this.jedis = jedis;
        this.lock = new RedisLock(jedis);
    }

    /**
     * Attempts to reserve a resource for a user. This function should
     * return true if and only if the resource was available for reservation.
     * If the function is unable to complete a reservation, it should return
     * immediatly.
     *
     * @param resourceID The ID of the resource to reserve.
     * @param userID The ID of the user attempting to reserve the resource.
     * @return true if the reservation was successful, false otherwise.
     */
    public boolean reserve(String resourceID, Integer userID) {
        String lockKey = "lock:" + resourceID;
        Long ttl = 30000L;
        boolean lockAcquired = lock.acquireLock(lockKey, ttl);
        if (!lockAcquired) {
            return false; // Couldn't acquire lock, resource is being processed
        }

        try {
            String resourceKey = "resource:" + resourceID;
            String currentUser = jedis.get(resourceKey);
            if (currentUser == null) {
                jedis.set(resourceKey, userID.toString());
                return true;
            } else {
                return false; // Resource is already reserved
            }
        } finally {
            lock.releaseLock(lockKey);
        }
    }

    /**
     * Attempts to checkout (release) a resource for a user.
     *
     * @param resourceID The ID of the resource to checkout.
     * @param userID The ID of the user attempting to checkout the resource.
     * @return true if the checkout was successful, false otherwise.
     */
    public boolean checkout(String resourceID, Integer userID) {
        String lockKey = "lock:" + resourceID;
        Long ttl = 30000L; // 30 seconds
        boolean lockAcquired = lock.acquireLock(lockKey, ttl);
        if (!lockAcquired) {
            return false;
        }

        try {
            String resourceKey = "resource:" + resourceID;
            String currentUser = jedis.get(resourceKey);
            if (currentUser != null && currentUser.equals(userID.toString())) {
                jedis.del(resourceKey);
                return true;
            } else {
                return false; // Resource reserved by other user
            }
        } finally {
            lock.releaseLock(lockKey);
        }
    }
}
