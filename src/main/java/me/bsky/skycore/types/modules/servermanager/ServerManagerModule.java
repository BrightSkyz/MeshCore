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
        createServer("Bungeecord-1", ServerType.BUNGEECORD, true, 25565, 512);
    }

    @Override
    public void onDisableApplication() {
        for (Integer i = 0; i != getServers().size(); i++) {
            getSkyApplication().getSkyServers().get(i).stopServer();
        }
    }

    public void createServer(String serverName, ServerType serverType, Boolean deleteOnStop, Integer port, Integer maxRam) {
        Integer finalPort = port;
        Integer finalMaxRam = maxRam;
        if (getServer(serverName) == null) {
            getSkyLogger().info("The server " + serverName + " doesn't exist, continuing...");
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
            new SkyServer(serverName, serverType, deleteOnStop, finalPort, finalMaxRam, this);
        } else {
            getSkyLogger().warn("The server " + serverName + " already exists.");
        }
    }

    public void removeServer(String serverName) {
        getSkyLogger().info("The server " + serverName + " is being removed...");
        for (SkyServer skyServer : getServers()) {
            if (skyServer.getServerName().equalsIgnoreCase(serverName)) {
                getSkyApplication().getSkyServers().remove(skyServer);
                return;
            }
        }
    }

    public SkyServer getServer(String serverName) {
        for (SkyServer skyServer : getServers()) {
            if (skyServer.getServerName().equalsIgnoreCase(serverName)) {
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