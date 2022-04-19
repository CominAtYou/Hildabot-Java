package com.cominatyou.util;

import java.util.List;

import org.javacord.api.event.message.MessageCreateEvent;

public abstract class Command {
   public abstract void execute(MessageCreateEvent message, List<String> messageArgs);
}
