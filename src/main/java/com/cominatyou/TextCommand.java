package com.cominatyou;

import java.util.List;

import org.javacord.api.event.message.MessageCreateEvent;

/**
 * A command that can be run with the bot, either in a DM or server text channel.
 */
public interface TextCommand {
   /**
    * Run the command.
    * @param message The message invoking the command
    * @param messageArgs A list of arguments for the command
    */
   public void execute(MessageCreateEvent message, List<String> messageArgs);

   /**
    * Get the name of the command.
    * @return The name of the command
    */
   public String getName();
}
