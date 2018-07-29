package me.bsky.skycore.bungee.events;

import me.bsky.skycore.api.helpers.ServerHelpers;
import me.bsky.skycore.bungee.SkyBungee;
import net.ME1312.SubServers.Bungee.Host.Host;
import net.ME1312.SubServers.Bungee.Host.Server;
import net.ME1312.SubServers.Bungee.Library.Version.Version;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PostLogin implements Listener {

    private SkyBungee skyBungee;

    public PostLogin(SkyBungee skyBungee) {
        this.skyBungee = skyBungee;
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        Map<String, Server> servers = getSkyBungee().getSubAPI().getServers();
        Map<String, Server> lobbyServers = new HashMap<>();
        for (Server server : servers.values()) {
            if (server.getName().startsWith("Lobby-")) {
                lobbyServers.put(server.getName(), server);
            }
        }
        // Once we're done going through all the servers, get a random lobby server
        List<Server> serversList = new ArrayList<Server>(lobbyServers.values());
        int randomServerIndex = new Random().nextInt(serversList.size());
        Server randomLobbyServer = serversList.get(randomServerIndex);
        // Send the player to the random server
        event.getPlayer().connect(getSkyBungee().generateServerInfo(randomLobbyServer));
        Integer globalPlayerCount = getSkyBungee().getSubAPI().getGlobalPlayers().size();
        // Check player count against lobby amount.
        if (globalPlayerCount <= (3 * (Math.ceil(Math.abs(globalPlayerCount / 3))))) {
            // Get a random host server to create the lobby server on
            List<Host> hostsList = new ArrayList<Host>(getSkyBungee().getSubAPI().getHosts().values());
            int randomHostIndex = new Random().nextInt(hostsList.size());
            Host randomHost = hostsList.get(randomHostIndex);
            // Generate the random port for the server
            Integer serverPort = ServerHelpers.getRandomUnusedPort(getSkyBungee().getSubAPI());
            // Create the server
            randomHost.getCreator().create("Lobby-" + (lobbyServers.size() + 1), randomHost.getCreator().getTemplate("Vanilla"), Version.fromString("1.13"), serverPort);
            getSkyBungee().getSubAPI().getSubServer("Lobby-" + (lobbyServers.size() + 1)).setTemporary(true);
        }
    }

    public SkyBungee getSkyBungee() {
        return skyBungee;
    }
}
