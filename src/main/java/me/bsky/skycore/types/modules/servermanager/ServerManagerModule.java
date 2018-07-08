package me.bsky.skycore.types.modules.servermanager;

import me.bsky.skycore.types.SkyModule;
import me.bsky.skycore.types.SkyServer;
import me.bsky.skycore.types.enums.ProgramMode;
import me.bsky.skycore.types.enums.ServerType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerManagerModule extends SkyModule {

    private Map<String, SkyServer> serverMap = new HashMap<>();

    public ServerManagerModule(ProgramMode programMode, Object main) {
        super("serverManager", programMode, main);
    }

    @Override
    public void onEnableApplication() {
        createServer("Test-1", ServerType.SPIGOT, false, 25565, null);
        createServer("Test-2", ServerType.SPIGOT, true, null, null);
    }

    @Override
    public void onDisableApplication() {
        for (SkyServer skyServer : serverMap.values()) {
            skyServer.stopServer();
            removeServer(skyServer.getName());
        }
    }

    public void createServer(String name, ServerType serverType, Boolean deleteOnStop, Integer port, Integer maxRam) {
        Integer finalPort = port;
        Integer finalMaxRam = maxRam;
        if (serverMap == null || !serverMap.containsKey(name)) {
            if (serverMap == null) {
                serverMap = new HashMap<>();
            }
            getSkyLogger().info("The server " + name + " doesn't exist, continuing...");
            if (port == null) {
                // Pick the lowest unused port between 25300 and 25500
                if (serverMap.isEmpty()) {
                    finalPort = 25300;
                } else {
                    List<Integer> usedPorts = new ArrayList<>();
                    for (SkyServer skyServer : serverMap.values()) {
                        usedPorts.add(skyServer.getPort());
                    }
                    boolean portFound = false;
                    for (Integer i = 25300; i <= 25500 && !portFound; i++) {
                        if (usedPorts.contains(i)) {
                            continue;
                        } else {
                            finalPort = i;
                            portFound = true;
                        }
                    }
                }
            }
            if (maxRam == null) {
                finalMaxRam = 768;
            }
            SkyServer skyServer = new SkyServer(name, serverType, deleteOnStop, finalPort, finalMaxRam, this);
            serverMap.put(name, skyServer);
        } else {
            getSkyLogger().warn("The server " + name + " already exists.");
        }
    }

    public void removeServer(String name) {
        getSkyLogger().info("The server " + name + " is being removed...");
        serverMap.remove(name);
    }

    public SkyServer getServer(String name) {
        if (serverMap.containsKey(name)) {
            return serverMap.get(name);
        }
        return null;
    }
}
