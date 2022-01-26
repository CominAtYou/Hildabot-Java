package com.cominatyou;

import com.cominatyou.util.RedisInstance;
import com.cominatyou.xp.XPSystemCalculator;

public class RedisUserEntry {
    long id;
    String redisKey;

    public RedisUserEntry(long id) {
        this.id = id;
        this.redisKey = "users:" + id;
    }

    public long getID() {
        return id;
    }

    public String getRedisKey() {
        return redisKey;
    }

    public int getXP() {
        String res = RedisInstance.getInstance().get(redisKey + ":xp");
        if (res == null) {
            RedisInstance.getInstance().set(redisKey + ":xp", "0");
            return 0;
        }
        return Integer.valueOf(res);
    }

    public void addXP(int amount) {
        RedisInstance.getInstance().incrby(redisKey + ":xp", amount);
    }

    public int getLevel() {
        return XPSystemCalculator.determineLevelFromXP(getXP());
    }

    public String getBirthday() {
        return RedisInstance.getInstance().get(redisKey + ":birthday:string");
    }

    public String getRedisFormattedBirthday() {
        if (getBirthday() == null) return null;
        final String[] birthdaySplit = getBirthday().split("-");
        return birthdaySplit[0] + ":" + birthdaySplit[1];
    }

    public boolean isEnrolled() {
        final String res = RedisInstance.getInstance().get(redisKey + ":enrolled");
        if (res == null) return false;
        else return true;
    }
}
