package com.cominatyou.db;

import com.cominatyou.xp.XPSystemCalculator;

/**
 * This class contains methods for reading and modifying data in a user's Redis database entry, without the hassles of having to use {@link RedisInstance#getInstance}.
 */
public class RedisUserEntry {
    final String id;
    final String redisKey;

    /**
     * Create an instance of this class to access and modify a user's Redis database entry.
     * @param id The Discord ID of the user.
     * @throws IllegalArgumentException If the ID passed is not 18 digits long.
     */
    public RedisUserEntry(long id) throws IllegalArgumentException {
        if (!String.valueOf(id).matches("[0-9]{18}")) {
            throw new IllegalArgumentException("Invalid ID supplied");
        }

        this.id = String.valueOf(id);
        this.redisKey = "users:" + id;
    }

    /**
     * Create an instance of this class to access and modify a user's Redis database entry.
     * @param id The Discord ID of the user.
     * @throws IllegalArgumentException If the ID passed is not 18 digits long.
     */
    public RedisUserEntry(String id) throws IllegalArgumentException {
        if (!id.matches("[0-9]{18}")) {
            throw new IllegalArgumentException("Invalid ID supplied");
        }

        this.id = id;
        this.redisKey = "users:" + id;
    }

    /**
     * Get the ID of the user representing this database entry.
     * @return The user's ID.
     */
    public long getId() {
        return Long.valueOf(id);
    }

    /**
     * Get the formatted user key for the user, in the form of {@code users:userID}.
     * @return The user's key.
     */
    public String getRedisKey() {
        return redisKey;
    }

    /**
     * Get the user's XP value.
     * @return The user's XP value.
     */
    public int getXP() {
        final String res = RedisInstance.getInstance().get(redisKey + ":xp");
        if (res == null) {
            RedisInstance.getInstance().set(redisKey + ":xp", "0");
            return 0;
        }
        return Integer.valueOf(res);
    }

    /**
     * Get the user's level.
     * @return The user's level.
     */
    public int getLevel() {
        return XPSystemCalculator.determineLevelFromXP(getXP());
    }

    /**
     * Get a key that represents a boolean in the user's database entry.
     * @param key The key in which to query.
     * @return {@code true} if the string value of the key (ignoring case) equals {@code "true"}. {@code false} if the key does not exist or does not equal {@code "true"}, ignoring case.
     * @see Boolean#parseBoolean(String) Boolean#parseBoolean for more information on the logic.
     */
    public boolean getBoolean(String key) {
        final String res = RedisInstance.getInstance().get(redisKey + ":" + key);
        return Boolean.parseBoolean(res);
    }

    /**
     * Get a key that represents an int in the user's database entry.
     * @param key The key in which to query.
     * @return The integer value if it is present, or {@code 0} if the key does not exist or does not have a numerical representation.
     */
    public int getInt(String key) {
        final String res = RedisInstance.getInstance().get(redisKey + ":" + key);
        try {
            return Integer.parseInt(res);
        }
        catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Get a string from the user's database entry.
     * @param key The key in which to query.
     * @return The string from the key, or {@code null} if the key does not exist.
     */
    public String getString(String key) {
        return RedisInstance.getInstance().get(redisKey + ":" + key);
    }

    /**
     * Set a value in the user's database entry.
     * @param key The key to set.
     * @param value The value to set.
     * @return {@code OK} if {@code SET} was executed properly.
     */
    public String set(String key, String value) {
        return RedisInstance.getInstance().set(redisKey + ":" + key, value);
    }

    /**
     * Set the expiry of a key in the user's database entry.
     * @param key The key to set the expiry of.
     * @param expiry The expiry, in Unix timestamp form, in seconds. If the timestamp is in the past, the key will be deleted.
     * @return {@code true} if the expiry was set. {@code false} if the key does not exist or if there was an issue setting the expiry.
     */
    public boolean expireKeyAt(String key, long expiry) {
        return RedisInstance.getInstance().expireat(redisKey + ":" + key, expiry);
    }

    /**
     * Expire the key in a certain number of seconds in the future.
     * @param key The key to set the expiry of.
     * @param seconds The seconds from when set that the key should expire.
     * @return {@code true} if the expiry was set. {@code false} if the key does not exist or if there was an issue setting the expiry.
     */
    public boolean expireKeyIn(String key, long seconds) {
        return RedisInstance.getInstance().expire(redisKey + ":" + key, seconds);
    }

    /**
     * Increment a key in the user's database entry by a certain amount.
     * @param key The key to increment.
     * @param value The value to increment the key by.
     * @return The value of the key post-increment.
     */
    public long incrementKey(String key, int value) {
        return RedisInstance.getInstance().incrby(redisKey + ":" + key, value);
    }

    /**
     * Increment a key in the user's database entry by {@code 1}.
     * @param key The key to increment.
     * @param value The value to increment the key by.
     * @return The value of the key post-increment.
     */
    public long incrementKey(String key) {
        return RedisInstance.getInstance().incr(redisKey + ":" + key);
    }
}
