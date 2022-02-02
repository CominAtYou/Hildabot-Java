package com.cominatyou.util.activities;

import org.javacord.api.entity.activity.ActivityType;

public class Activity {
    private final ActivityType type;
    private final String activity;

    protected Activity(ActivityType type, String activity) {
        this.type = type;
        this.activity = activity;
    }

    protected ActivityType getType() {
        return type;
    }

    protected String getActivity() {
        return activity;
    }
}
