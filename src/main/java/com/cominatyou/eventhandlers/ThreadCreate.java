package com.cominatyou.eventhandlers;

import org.javacord.api.event.channel.thread.ThreadCreateEvent;

public class ThreadCreate {
    public static void onCreate(ThreadCreateEvent event) {
        event.getChannel().asServerThreadChannel().get().addThreadMember(event.getApi().getYourself());
    }
}
