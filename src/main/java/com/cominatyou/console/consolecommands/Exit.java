package com.cominatyou.console.consolecommands;

import java.util.List;

public class Exit implements ConsoleCommand {
    @Override
    public void execute(List<String> args) {
        System.exit(0);
    }

}
