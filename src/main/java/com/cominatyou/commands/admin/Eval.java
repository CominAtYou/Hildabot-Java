package com.cominatyou.commands.admin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.javacord.api.event.message.MessageCreateEvent;

import com.cominatyou.TextCommand;

public class Eval extends TextCommand {
    public void execute(MessageCreateEvent message, List<String> messageArgs) {
        if (!message.getMessageAuthor().isBotOwner()) return;

        final Process process;
        boolean successful = false;
        try {
            final ProcessBuilder processBuilder = new ProcessBuilder("node" ,"-e", "console.log(eval(\"" + String.join(" ", messageArgs) +"\"));");
            process = processBuilder.start();
            successful = process.waitFor(10, TimeUnit.SECONDS);
        }
        catch (Exception e) {
            message.getChannel().sendMessage("Failed to start the Node.js process.");
            return;
        }

        if (!successful) {
            message.getChannel().sendMessage("The Node.js process timed out.");
        }
        else if (process.exitValue() != 0) {
            final String stderr = new BufferedReader(new InputStreamReader(process.getErrorStream())).lines().parallel().collect(Collectors.joining("\n"));
            message.getChannel().sendMessage("The Node.js process exited with code " + process.exitValue() + ".\n```\n" + stderr + "\n```");
        }
        else {
            final String output = new BufferedReader(new InputStreamReader(process.getInputStream())).lines().parallel().collect(Collectors.joining("\n"));
            message.getChannel().sendMessage("```\n" + output + "\n```");
        }
    }
}
