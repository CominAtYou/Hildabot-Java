package com.cominatyou.util.activities;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.activity.ActivityType;

public class ActivitySwapper {
    private static final Activity[] activities = {
        new Activity(ActivityType.PLAYING, "with the bells"),
        new Activity(ActivityType.COMPETING, "in activities with Gerda")
    };

    public static void start(DiscordApi client) {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                final int index = ThreadLocalRandom.current().nextInt(0, activities.length);
                client.updateActivity(activities[index].getType(), activities[index].getActivity());
            }
        }, 0, 600000); // Swaps activities every ten minutes.
    }
}
