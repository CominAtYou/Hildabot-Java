package com.cominatyou.util;

public class BirthdayEntry {
    private final int day;
    private final String userID;

    public BirthdayEntry(String userID, int day) {
        this.userID = userID;
        this.day = day;
    }

    public int getDay() {
        return day;
    }

    public String getUserID() {
        return userID;
    }
}
