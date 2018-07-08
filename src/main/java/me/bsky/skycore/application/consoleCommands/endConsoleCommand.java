package me.bsky.skycore.application.consoleCommands;

import me.bsky.skycore.application.SkyConsole;
import me.bsky.skycore.application.SkyConsoleCommand;

public class endConsoleCommand extends SkyConsoleCommand {

    public endConsoleCommand(SkyConsole skyConsole) {
        super("end", skyConsole);
    }

    @Override
    public void execute(String[] args) {
        getSkyLogger().info("SkyCore is shutting down...goodbye!");
        System.exit(0);
    }
}
