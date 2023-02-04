package com.cominatyou.db;

import com.cominatyou.xp.XPSystemCalculator;

import java.util.List;

import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.user.User;

/**
 * This class contains methods for reading and modifying data in a user's Redis database entry, without the hassles of having to use {@link RedisInstance#getInstance}.
 */
public class RedisUserEntry {
    final String id;
    final String redisKey;

    private void checkForEmptyKey(String key) throws IllegalArgumentException {
        if (key == null || key.equals("")) {
            throw new IllegalArgumentException("Key cannot be empty");
        }
    }

    /**
     * Create an instance of this class to access and modify a user's Redis database entry. Passing a {@link User} or {@link MessageAuthor} object to this constructor is recommended over passing an ID, when possible.
     * @param id The Discord ID of the user.
     * @throws IllegalArgumentException If the ID passed is not 18 digits long.
     */
    public RedisUserEntry(long id) throws IllegalArgumentException {
        if (!String.valueOf(id).matches("[0-9]{17,19}")) {
            throw new IllegalArgumentException("Invalid ID supplied");
        }

        this.id = String.valueOf(id);
        this.redisKey = "users:" + id;
    }

    /**
     * Create an instance of this class to access and modify a user's Redis database entry. Passing a {@link User} or {@link MessageAuthor} object to this constructor is recommended over passing an ID, when possible.
     * @param id The Discord ID of the user.
     * @throws IllegalArgumentException If the ID passed is not 18 digits long.
     */
    public RedisUserEntry(String id) throws IllegalArgumentException {
        if (!id.matches("[0-9]{17,19}")) {
            throw new IllegalArgumentException("Invalid ID supplied");
        }

        this.id = id;
        this.redisKey = "users:" + id;
    }

    /**
     * Create an instance of this class to access and modify a user's Redis database entry.
     * @param user The user to access the entry of.
     */
    public RedisUserEntry(User user) {
        this.id = user.getIdAsString();
        this.redisKey = "users:" + id;
    }

    /**
     * Create an instance of this class to access and modify a user's Redis database entry.
     * @param user The user to access the entry of.
     */
    public RedisUserEntry(MessageAuthor user) {
        this.id = user.getIdAsString();
        this.redisKey = "users:" + id;
    }


    /**
     * Delete a key from a user's Redis database entry.
     * @param key The key to delete.
     * @return The number of keys deleted - {@code 1} if successful, {@code 0} if the key does not exist.
     */
    public long deleteKey(String key) {
        return RedisInstance.getInstance().del(redisKey + ":" + key);
    }

    /**
     * Check if the user's database entry contains a certain key.
     * @param key The key to query
     * @return {@code true} if the key exists in the user's database entry, {@code false} otherwise.
     */
    public boolean hasKey(String key) {
        return RedisInstance.keyExists(redisKey + ":" + key);
    }

    /**
     * Get the ID of the user representing this database entry.
     * @return The user's ID.
     */
    public long getId() {
        return Long.valueOf(id);
    }

    /**
     * Get the ID of the user representing this database entry as a string.
     * @return The user's ID, as a string.
     */
    public String getIdAsString() {
        return id;
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
     * @throws IllegalArgumentException If the value of {@code key} is null or an empty string.
     * @see Boolean#parseBoolean(String) Boolean#parseBoolean for more information on the logic.
     */
    public boolean getBoolean(String key) throws IllegalArgumentException {
        checkForEmptyKey(key);

        final String res = RedisInstance.getInstance().get(redisKey + ":" + key);
        return Boolean.parseBoolean(res);
    }

    /**
     * Get a key that represents an int in the user's database entry.
     * @param key The key in which to query.
     * @return The integer value if it is present, or {@code 0} if the key does not exist or does not have a numerical representation.
     * @throws IllegalArgumentException If the value of {@code key} is null or an empty string.
     */
    public int getInt(String key) throws IllegalArgumentException {
        checkForEmptyKey(key);

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
     * @throws IllegalArgumentException If the value of {@code key} is null or an empty string.
     */
    public String getString(String key) throws IllegalArgumentException {
        checkForEmptyKey(key);
        return RedisInstance.getInstance().get(redisKey + ":" + key);
    }

    /**
     * Get a key that represents a long from the user's database entry.
     * @param key The key in which to query.
     * @return The long value of the key, or {@code 0} if the key does not exist or cannot be represented as a {@code long}.
     * @throws IllegalArgumentException If the value of {@code key} is null or an empty string.
     */
    public long getLong(String key) throws IllegalArgumentException {
        checkForEmptyKey(key);

        try {
            return Long.valueOf(RedisInstance.getInstance().get(redisKey + ":" + key));
        }
        catch (NumberFormatException e) {
            return 0L;
        }
    }

    /**
     * Get a key that represents a list from the user's database entry.
     * @param key The key in which to query.
     * @return The list of strings from the key, or an empty list if the key does not exist.
     * @throws IllegalArgumentException If the value of {@code key} is null or an empty string.
     */
    public List<String> getList(String key) throws IllegalArgumentException {
        checkForEmptyKey(key);

        return RedisInstance.getInstance().lrange(redisKey + ":" + key, 0, -1);
    }

    /**
     * Set a value in the user's database entry.
     * @param key The key to set.
     * @param value The value to set.
     * @return {@code OK} if {@code SET} was executed properly.
     * @throws IllegalArgumentException If the value of {@code key} is null or an empty string.
     */
    public String set(String key, String value) throws IllegalArgumentException {
        checkForEmptyKey(key);

        if (value.equals("")) {
            throw new IllegalArgumentException("Value cannot be empty");
        }

        return RedisInstance.getInstance().set(redisKey + ":" + key, value);
    }

    /**
     * Set the expiry of a key in the user's database entry.
     * @param key The key to set the expiry of.
     * @param expiry The expiry, in Unix timestamp form, in seconds. If the timestamp is in the past, the key will be deleted.
     * @return {@code true} if the expiry was set. {@code false} if the key does not exist or if there was an issue setting the expiry.
     * @throws IllegalArgumentException If the value of {@code key} is null or an empty string.
     */
    public boolean expireKeyAt(String key, long expiry) throws IllegalArgumentException {
        checkForEmptyKey(key);
        return RedisInstance.getInstance().expireat(redisKey + ":" + key, expiry);
    }

    /**
     * Expire the key in a certain number of seconds in the future.
     * @param key The key to set the expiry of.
     * @param seconds The seconds from when set that the key should expire.
     * @return {@code true} if the expiry was set. {@code false} if the key does not exist or if there was an issue setting the expiry.
     * @throws IllegalArgumentException If the value of {@code key} is null or an empty string.
     */
    public boolean expireKeyIn(String key, long seconds) throws IllegalArgumentException {
        checkForEmptyKey(key);
        return RedisInstance.getInstance().expire(redisKey + ":" + key, seconds);
    }

    /**
     * Increment a key in the user's database entry by a certain amount.
     * @param key The key to increment.
     * @param amount The amount to increment the key by.
     * @return The value of the key post-increment.
     * @throws IllegalArgumentException If the value of {@code key} is null or an empty string.
     */
    public long incrementKey(String key, int amount) throws IllegalArgumentException {
        checkForEmptyKey(key);
        return RedisInstance.getInstance().incrby(redisKey + ":" + key, amount);
    }

    /**
     * Increment a key in the user's database entry by {@code 1}.
     * @param key The key to increment.
     * @return The value of the key post-increment.
     * @throws IllegalArgumentException If the value of {@code key} is null or an empty string.
     */
    public long incrementKey(String key) throws IllegalArgumentException {
        checkForEmptyKey(key);
        return RedisInstance.getInstance().incr(redisKey + ":" + key);
    }

    /**
     * Decrement a key in the user's database entry by a certain amount.
     * @param key The key to decrement.
     * @param amount The amount to decrement the key by.
     * @return The value of the key post-decrement.
     * @throws IllegalArgumentException If the value of {@code key} is null or an empty string.
     */
    public long decrementKey(String key, int amount) throws IllegalArgumentException {
        checkForEmptyKey(key);
        return RedisInstance.getInstance().decrby(redisKey + ":" + key, amount);
    }

    /**
     * Decrement a key in the user's database entry by {@code 1}.
     * @param key The key to decrement.
     * @return The value of the key post-decrement.
     * @throws IllegalArgumentException If the value of {@code key} is null or an empty string.
     */
    public long decrementKey(String key) throws IllegalArgumentException {
        checkForEmptyKey(key);
        return RedisInstance.getInstance().decr(redisKey + ":" + key);
    }
}
