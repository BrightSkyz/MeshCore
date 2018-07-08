package me.bsky.skycore.application;

import me.bsky.skycore.application.consoleCommands.endConsoleCommand;
import me.bsky.skycore.application.consoleCommands.helpConsoleCommand;
import me.bsky.skycore.application.consoleCommands.infoConsoleCommand;

import java.io.Console;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SkyConsole {

    private SkyApplication skyApplication;
    private Thread consoleThread;

    private Map<String, SkyConsoleCommand> commandMap = new HashMap<>();

    public SkyConsole(SkyApplication skyApplication) {
        this.skyApplication = skyApplication;
        registerCommand(new endConsoleCommand(this));
        registerCommand(new helpConsoleCommand(this));
        registerCommand(new infoConsoleCommand(this));
    }

    public void startConsole() {
        consoleThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Run the console input
                while (true) {
                    Console console = System.console();
                    if (console == null) {
                        getSkyApplication().getSkyLogger().error("No console: not in interactive mode!");
                        System.exit(0);
                    }
                    System.out.print(">");
                    String command = console.readLine();
                    // Split command
                    if (command != null && command != "") {
                        String args[] = {};
                        if (command.contains(" ")) {
                            args = command.split(" ");
                            command = args[0];
                            args = Arrays.copyOfRange(args, 1, args.length);
                        }
                        // Execute command if it exists
                        if (commandMap.containsKey(command)) {
                            commandMap.get(command).execute(args);
                        } else {
                            getSkyApplication().getSkyLogger().info("That command doesn't exists, use 'help' to get help.");
                        }
                    }
                }
            }
        });
        // Start the thread
        consoleThread.start();
    }

    public SkyApplication getSkyApplication() {
        return skyApplication;
    }

    public void registerCommand(SkyConsoleCommand skyConsoleCommand) {
        commandMap.put(skyConsoleCommand.getName(), skyConsoleCommand);
    }
}
