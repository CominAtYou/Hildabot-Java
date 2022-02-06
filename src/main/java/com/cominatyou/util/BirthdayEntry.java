package com.cominatyou.util;

public class BirthdayEntry {
    private final int day;
    private final String userId;

    public BirthdayEntry(String userId, int day) {
        this.userId = userId;
        this.day = day;
    }

    public int getDay() {
        return day;
    }

    public String getUserId() {
        return userId;
    }
}
