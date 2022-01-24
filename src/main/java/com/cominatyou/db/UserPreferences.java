package com.cominatyou.db;

import com.cominatyou.util.RedisInstance;

public class UserPreferences {
    long id;
    String redisKey;

    public UserPreferences(long id) {
        this.id = id;
        this.redisKey = "users:" + id;
    }

    public boolean levelNotificationsEnabled() {
        String resp = RedisInstance.getInstance().get(redisKey + ":levelnotificationsenabled");
        // Level notifications are enabled by default.
        return resp == null ? true : resp.equals("true");
    }

    public void setLevelNotificationsPreference(boolean preference) {
        RedisInstance.getInstance().set(redisKey + ":levelnotificationsenabled", "true");
    }
}
