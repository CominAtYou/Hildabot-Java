package com.cominatyou.util.activities;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import com.cominatyou.util.versioning.Version;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.activity.ActivityType;

public class ActivitySwapper {
    private static final Activity[] activities = { new Activity(ActivityType.WATCHING, "the Woffs migrate"),
            new Activity(ActivityType.WATCHING, "for your submissions!"),
            new Activity(ActivityType.WATCHING, "Frida practice magic"),
            new Activity(ActivityType.WATCHING, "Hilda and the Mountain King"),
            new Activity(ActivityType.WATCHING, "the sunset over Trolberg"),
            new Activity(ActivityType.PLAYING, "with the Sparrow Scouts"),
            new Activity(ActivityType.PLAYING, "with the trolls"),
            new Activity(ActivityType.PLAYING, "with Twig"),
            new Activity(ActivityType.PLAYING, "with the water spirits"),
            new Activity(ActivityType.PLAYING, "Version " + Version.VERSION_STRING),
            new Activity(ActivityType.COMPETING, "a dodgeball game"),
            new Activity(ActivityType.LISTENING, "the Rat King's secrets"),
            new Activity(ActivityType.LISTENING, "Alfur lecture"),
            new Activity(ActivityType.LISTENING, "the bells"),
            new Activity(ActivityType.LISTENING, "a campfire story"),
            new Activity(ActivityType.LISTENING, "The Life of Hilda")
    };

    public static void start(DiscordApi client) {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (ActivityStatus.isActive()) return;

                final int index = ThreadLocalRandom.current().nextInt(0, activities.length);
                client.updateActivity(activities[index].getType(), activities[index].getActivity());
            }
        }, 0, 600000); // Swaps activities every ten minutes.
    }
}
