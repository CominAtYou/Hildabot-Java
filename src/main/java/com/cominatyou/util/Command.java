package com.cominatyou.util;

import java.util.List;

import org.javacord.api.event.message.MessageCreateEvent;

public abstract class Command {
   /**
    * Run the command.
    * @param message The message invoking the command
    * @param messageArgs A list of arguments for the command
    */
   public abstract void execute(MessageCreateEvent message, List<String> messageArgs);
}
