package com.cominatyou.util;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.sync.RedisCommands;

import com.cominatyou.Config;

public class RedisInstance {
    private static RedisCommands<String, String> syncCommands;

    static {
        RedisClient client = RedisClient.create(Config.getRedisInstanceAddress());
        client.setOptions(ClientOptions.builder().autoReconnect(true).build());
        syncCommands = client.connect().sync();
    }

    public static RedisCommands<String, String> getInstance() {
        return syncCommands;
    }

    public static boolean keyExists(String key) {
        return syncCommands.get(key) != null;
    }

    public static String getString(String key) {
        return syncCommands.get(key);
    }

    /**
     * Get a string that has a numerical value in the Redis instance, and convert it to an int.
     * @param key The key of the value.
     * @return The value of the given key, or <code>-1</code> if the string does not have a numerical representation or the key does not exist.
     */
    public static int getInt(String key) {
        String resp = syncCommands.get(key);
        try {
            return Integer.parseInt(resp);
        }
        catch (NumberFormatException e) {
            return -1;
        }
    }

    public static boolean getBoolean(String key) {
        return syncCommands.get(key).equals("true");
    }
}
