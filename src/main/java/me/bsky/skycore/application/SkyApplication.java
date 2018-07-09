package me.bsky.skycore.application;

import me.bsky.skycore.types.SkyLogger;
import me.bsky.skycore.types.SkyModule;
import me.bsky.skycore.types.SkyServer;
import me.bsky.skycore.types.enums.ProgramMode;
import me.bsky.skycore.types.modules.servermanager.ServerManagerModule;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkyApplication {

    private Map<String, SkyModule> moduleMap = new HashMap<>();

    private List<SkyServer> skyServers = new ArrayList<>();

    private ServerManagerModule serverManagerModule;

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
            skyLogger.info("Setup: The servers directory already exist so it won't be created.");
        } else {
            skyLogger.info("Setup: The servers directory doesn't exist, creating one...");
            serverDirectory.mkdir();
        }

        // Setup: Create date directory if it doesn't exists
        File dateDirectory = new File("./data").getAbsoluteFile();
        if (dateDirectory.isDirectory()) {
            skyLogger.info("Setup: The data directory already exist so it won't be created.");
        } else {
            skyLogger.info("Setup: The data directory doesn't exist, creating one...");
            dateDirectory.mkdir();
        }

        // Setup: Create date directory if it doesn't exists
        File spigotJarFile = new File("./data/spigot.jar").getAbsoluteFile();
        File bungeecordJarFile = new File("./data/bungeecord.jar").getAbsoluteFile();
        if (spigotJarFile.isFile() && bungeecordJarFile.isFile()) {
            skyLogger.info("Setup: The spigot.jar and bungeecord.jar files exist. Continuing...");
        } else {
            skyLogger.info("Setup: The spigot.jar and bungeecord.jar files don't exist in the data directory. Stopping...");
            System.exit(0);
        }

        skyConsole = new SkyConsole(this);
        skyConsole.startConsole();

        // Register the modules for the application
        serverManagerModule = new ServerManagerModule(ProgramMode.APPLICATION, this);
        registerModule(serverManagerModule);

        getSkyLogger().info("SkyCore has loaded.");
        getSkyLogger().info("Listening on this console for commands, type 'help' for help.");
    }

    private void registerModule(SkyModule skyModule) {
        // Register a module
        moduleMap.put(skyModule.getModuleName(), skyModule);
    }

    public Map<String, SkyModule> getModuleMap() {
        return moduleMap;
    }

    public SkyLogger getSkyLogger() {
        return skyLogger;
    }

    public SkyConsole getSkyConsole() {
        return skyConsole;
    }

    public ServerManagerModule getServerManagerModule() {
        return serverManagerModule;
    }

    public List<SkyServer> getSkyServers() {
        return skyServers;
    }
}
