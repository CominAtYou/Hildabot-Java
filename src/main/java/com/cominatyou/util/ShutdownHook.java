package com.cominatyou.util;

import com.cominatyou.App;
import com.cominatyou.db.RedisInstance;

public class ShutdownHook {
    private static boolean initialized = false;

    public static void init() {
        if (initialized) return;

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            App.getClient().disconnect().join();
            RedisInstance.disconnect();
        }));

        initialized = true;
    }
}
