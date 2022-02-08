package com.cominatyou.util.activities;

import org.javacord.api.entity.activity.ActivityType;

public class Activity {
    private final ActivityType type;
    private final String activity;

    /**
     * Create a new Activity object for use with the ActivitySwapper.
     * @param type The type of activity.
     * @param activity The details of the activity.
     */
    protected Activity(ActivityType type, String activity) {
        this.type = type;
        this.activity = activity;
    }

    /**
     * Get the type of the activity.
     * @return The activity's type.
     */
    protected ActivityType getType() {
        return type;
    }

    /**
     * The details of the activity.
     * @return The activity's details.
     */
    protected String getActivity() {
        return activity;
    }
}
