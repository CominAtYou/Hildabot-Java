package com.cominatyou.util.activities;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.activity.ActivityType;

public class ActivitySwapper {
    private static final Activity[] activities = { new Activity(ActivityType.LISTENING, "Nav-fi"),
            new Activity(ActivityType.PLAYING, "with Twig"),
            new Activity(ActivityType.WATCHING, "the Woffs migrate"),
            new Activity(ActivityType.WATCHING, "for your submissions!") };

    public static void start(DiscordApi client) {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                final int index = ThreadLocalRandom.current().nextInt(0, activities.length);
                client.updateActivity(activities[index].getType(), activities[index].getActivity());
            }
        }, 0, 600000);
    }
}
