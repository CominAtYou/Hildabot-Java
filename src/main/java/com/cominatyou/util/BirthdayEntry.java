package com.cominatyou.util;

public class BirthdayEntry {
    private final int day;
    private final String userId;

    /**
     * Create a birthday entry for use with the birthday announcer.
     *
     * @param userId The ID of the user.
     * @param day    The day of the month the user's birthday falls on.
     */
    public BirthdayEntry(String userId, int day) {
        this.userId = userId;
        this.day = day;
    }

    /**
     * Get the day of the month that the user's birthday falls on.
     *
     * @return The day of the month that the user's birthday falls on.
     */
    public int getDay() {
        return day;
    }

    /**
     * Get the user's ID.
     *
     * @return The user's ID.
     */
    public String getUserId() {
        return userId;
    }
}
