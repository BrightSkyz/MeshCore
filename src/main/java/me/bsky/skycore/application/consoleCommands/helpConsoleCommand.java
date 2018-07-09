package me.bsky.skycore.application.consoleCommands;

import me.bsky.skycore.application.SkyConsole;
import me.bsky.skycore.application.SkyConsoleCommand;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class helpConsoleCommand extends SkyConsoleCommand {

    private Map<Integer, List<String>> helpPages = new HashMap<>();

    public helpConsoleCommand(SkyConsole skyConsole) {
        super("help", skyConsole);
        helpPages.put(0, Arrays.asList(
                "help [page]: Displays the help",
                "end: Stops SkyCore peacefully",
                "info: Displays information about SkyCore",
                "sendcmd <server name> <command>: Send a command to the specified server"
        ));
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            logHelpPage(0);
        } else if (args.length == 1) {
            try {
                // Convert to number, see if page exits
                Integer pageInteger = Integer.parseInt(args[0]);
                if (helpPages.containsKey((pageInteger + 1))) {
                    logHelpPage(pageInteger);
                } else {
                    getSkyLogger().warn("Incorrect usage: help [page number]");
                }
            } catch (Exception e) {
                getSkyLogger().warn("Incorrect usage: help [page number]");
            }
        } else {
            getSkyLogger().warn("Incorrect usage: help [page number]");
        }
    }

    private void logHelpPage(Integer number) {
        getSkyLogger().info("--- Help for SkyCore [" + (number + 1) + "/" + helpPages.size() + "] ---");
        for (String line : helpPages.get(number)) {
            getSkyLogger().info(line);
        }
        getSkyLogger().info(" Notes: <required> [optional]");
    }
}
