package me.bsky.skycore.application;

import me.bsky.skycore.types.SkyLogger;

import java.io.File;

public class SkyApplication {

    private SkyLogger skyLogger = null;
    private SkyConsole skyConsole = null;

    public void startApplication() {
        // Start by initializing the variable for the utils such as logger.
        skyLogger = new SkyLogger(System.out);

        skyLogger.info("SkyLogger has been initialized.");
        // Run through the setup to make sure everything is ready to go
        skyLogger.info("Running through basic setup steps if they haven't already.");
        // Setup: Create servers directory if it doesn't exists
        File serverDirectory = new File("./servers").getAbsoluteFile();
        if (serverDirectory.isDirectory()) {
            skyLogger.info("Setup: The servers directory already exists so it won't be created.");
        } else {
            skyLogger.info("Setup: The servers directory doesn't exists, creating one...");
            serverDirectory.mkdir();
        }

        // Setup: Create date directory if it doesn't exists
        File dateDirectory = new File("./date").getAbsoluteFile();
        if (dateDirectory.isDirectory()) {
            skyLogger.info("Setup: The data directory already exists so it won't be created.");
        } else {
            skyLogger.info("Setup: The data directory doesn't exists, creating one...");
            dateDirectory.mkdir();
        }

        skyConsole = new SkyConsole(this);
        skyConsole.startConsole();

        getSkyLogger().info("SkyCore has loaded.");
        getSkyLogger().info("Listening on this console for commands, type 'help' for help.");
    }

    public SkyLogger getSkyLogger() {
        return skyLogger;
    }

    public SkyConsole getSkyConsole() {
        return skyConsole;
    }
}
