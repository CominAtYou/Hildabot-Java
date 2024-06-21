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
            new Activity(ActivityType.CUSTOM, "Version " + Version.VERSION_STRING),
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

                final int index = ThreadLocalRandom.current().nextInt(0, activities.length + 1);
                if (index == activities.length) NationalDebt.setNationalDebtStatus(client);
                else client.updateActivity(activities[index].getType(), activities[index].getActivity());
            }
        }, 0, 600000); // Swaps activities every ten minutes.
    }
}

/**
 * This class represents an activity that can be used with the {@link ActivitySwapper}.
 */
class Activity {
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
