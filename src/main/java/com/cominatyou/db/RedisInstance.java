package com.cominatyou.db;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.sync.RedisCommands;

import com.cominatyou.Config;

public class RedisInstance {
    private static RedisCommands<String, String> syncCommands;

    /**
     * Open a connection to the Redis database.
     */
    public static void connect() {
        RedisClient client = RedisClient.create(Config.REDIS_INSTANCE_ADDRESS);
        client.setOptions(ClientOptions.builder().autoReconnect(true).build());
        syncCommands = client.connect().sync();
    }

    /**
     * Get the Redis instance.
     * @return The Redis instance.
     */
    public static RedisCommands<String, String> getInstance() {
        return syncCommands;
    }

    /**
     * Check if a key exists in the Redis instance.
     * @param key The key to check.
     * @return <code>true</code> if the key exists, <code>false</code> if not.
     */
    public static boolean keyExists(String key) {
        return syncCommands.get(key) != null;
    }

    /**
     * Get a string that has a numerical value from the Redis instance and convert it to an int.
     * @param key The key of the value.
     * @return The value of the given key, or <code>0</code> if the string does not have a numerical representation or the key does not exist.
     */
    @Deprecated
    public static int getInt(String key) {
        String resp = syncCommands.get(key);
        try {
            return Integer.parseInt(resp);
        }
        catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Get a string that has a boolean representation from the Redis instance and convert it to a boolean.
     * @param key The key of the value.
     * @return <code>true</code> if the value is equal to "true" (case-sensitive), or <code>false</code> if the key does not exist, or the string does not equal "true".
     */
    @Deprecated
    public static boolean getBoolean(String key) {
        final String resp = syncCommands.get(key);
        return resp == null ? false : resp.equals("true");
    }
}
