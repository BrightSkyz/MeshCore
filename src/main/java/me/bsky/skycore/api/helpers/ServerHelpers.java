package me.bsky.skycore.api.helpers;

import net.ME1312.SubServers.Bungee.Host.Server;
import net.ME1312.SubServers.Bungee.SubAPI;

import java.util.Arrays;
import java.util.Map;

public class ServerHelpers {

    public static Integer getRandomUnusedPort(SubAPI subAPI) {
        // Loop through and find taken ports
        Map<String, Server> servers = subAPI.getServers();
        Integer[] usedPorts = {};
        for (Server server : servers.values()) {
            Arrays.asList(usedPorts).add(server.getAddress().getPort());
        }
        // Sort the ports in ascending order
        Arrays.sort(usedPorts);
        // Get the first non-taken port
        Integer finalPort = 30000;
        boolean foundPort = false;
        for (Integer usedPort : usedPorts) {
            if (foundPort || usedPort != finalPort) {
                foundPort = true;
            } else {
                finalPort++;
            }
        }
        return finalPort;
    }
}
