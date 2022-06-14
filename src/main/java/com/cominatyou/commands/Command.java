package com.cominatyou.commands;

import java.util.List;

import org.javacord.api.event.message.MessageCreateEvent;

/**
 * A command that can be run with the bot, either in a DM or server text channel.
 */
public interface Command {
   /**
    * Run the command.
    * @param message The message invoking the command
    * @param messageArgs A list of arguments for the command
    */
   public abstract void execute(MessageCreateEvent message, List<String> messageArgs);
}
