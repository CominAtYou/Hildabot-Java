package com.cominatyou.console.ConsoleCommands;

import java.util.List;

import com.cominatyou.console.ConsoleChannel;

public abstract class ChannelBasedConsoleCommand {
    protected abstract void command(List<String> args); // Command body

    public void execute(List<String> args) {
        if (!ConsoleChannel.isChannelSet()) {
            System.err.println("You need to set a channel before you can do this!");
            return;
        }

        command(args);
    }
}
