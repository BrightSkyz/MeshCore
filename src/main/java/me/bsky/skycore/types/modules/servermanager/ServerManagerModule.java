package me.bsky.skycore.types.modules.servermanager;

import me.bsky.skycore.types.SkyModule;
import me.bsky.skycore.types.SkyServer;
import me.bsky.skycore.types.enums.ProgramMode;
import me.bsky.skycore.types.enums.ServerType;

import java.util.*;

public class ServerManagerModule extends SkyModule {

    public ServerManagerModule(ProgramMode programMode, Object main) {
        super("serverManager", programMode, main);
        if (programMode == ProgramMode.APPLICATION) {
        }
    }

    @Override
    public void onEnableApplication() {
        createServer("Test-1", ServerType.SPIGOT, false, 25565, null);
        createServer("Test-2", ServerType.SPIGOT, true, null, null);
    }

    @Override
    public void onDisableApplication() {
        for (Integer i = 0; i != getServers().size(); i++) {
            getSkyApplication().getSkyServers().get(i).stopServer();
        }
    }

    public void createServer(String name, ServerType serverType, Boolean deleteOnStop, Integer port, Integer maxRam) {
        Integer finalPort = port;
        Integer finalMaxRam = maxRam;
        if (getServer(name) == null) {
            getSkyLogger().info("The server " + name + " doesn't exist, continuing...");
            if (port == null) {
                // Pick the lowest unused port between 25300 and 25500
                if (getServers().isEmpty()) {
                    finalPort = 25300;
                } else {
                    List<Integer> usedPorts = new ArrayList<>();
                    for (SkyServer skyServer : getServers()) {
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
            new SkyServer(name, serverType, deleteOnStop, finalPort, finalMaxRam, this);
        } else {
            getSkyLogger().warn("The server " + name + " already exists.");
        }
    }

    public void removeServer(String name) {
        getSkyLogger().info("The server " + name + " is being removed...");
        for (SkyServer skyServer : getServers()) {
            if (skyServer.getName().equalsIgnoreCase(name)) {
                getSkyApplication().getSkyServers().remove(skyServer);
                return;
            }
        }
    }

    public SkyServer getServer(String name) {
        for (SkyServer skyServer : getServers()) {
            if (skyServer.getName().equalsIgnoreCase(name)) {
                return skyServer;
            }
        }
        return null;
    }

    public List<SkyServer> getServers() {
        if (getSkyApplication().getSkyServers() == null) {
            return new ArrayList<>();
        } else {
            return getSkyApplication().getSkyServers();
        }
    }
}
