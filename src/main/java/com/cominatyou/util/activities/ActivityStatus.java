package com.cominatyou.util.activities;

public class ActivityStatus {
    private static boolean active = false;

    public static boolean isActive() {
        return active;
    }

    public static void setActive(boolean active) {
        ActivityStatus.active = active;
    }
}
