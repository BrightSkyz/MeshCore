package me.bsky.skycore.api.helpers;

import net.ME1312.SubServers.Bungee.Host.Server;
import net.ME1312.SubServers.Bungee.SubAPI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerHelpers {

    public static Integer getRandomUnusedPort(SubAPI subAPI) {
        // Loop through and find taken ports
        Map<String, Server> servers = subAPI.getServers();
        Map<String, Integer> usedPorts = new HashMap<>();
        for (Server server : servers.values()) {
            usedPorts.put(server.getName(), server.getAddress().getPort());
        }
        // Sort the ports in ascending order
        List<Integer> usedPortsList = new ArrayList<Integer>(usedPorts.values());
        Collections.sort(usedPortsList, new Comparator<Integer>() {
            @Override
            public int compare(Integer integer, Integer t1) {
                return t1 - integer;
            }
        });
        Collections.reverseOrder();
        // Get the first non-taken port
        Integer finalPort = 30000;
        boolean foundPort = false;
        for (Integer usedPort : usedPortsList) {
            if (foundPort || usedPort != finalPort) {
                foundPort = true;
            } else {
                finalPort++;
            }
        }
        return finalPort;
    }
}
