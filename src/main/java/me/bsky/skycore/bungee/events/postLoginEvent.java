package me.bsky.skycore.bungee.events;

import me.bsky.skycore.bungee.SkyBungee;
import net.ME1312.SubServers.Bungee.Host.Server;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.*;

public class postLoginEvent implements Listener {

    private SkyBungee skyBungee;

    public postLoginEvent(SkyBungee skyBungee) {
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
    }

    public SkyBungee getSkyBungee() {
        return skyBungee;
    }
}
