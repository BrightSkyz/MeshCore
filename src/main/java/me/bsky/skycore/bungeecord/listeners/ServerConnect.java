package me.bsky.skycore.bungeecord.listeners;

import me.bsky.skycore.types.SkyListener;
import me.bsky.skycore.types.enums.ProgramMode;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ServerConnect extends SkyListener {

    public ServerConnect(Object main) {
        super(ProgramMode.BUNGEECORD, main);
    }

    @EventHandler
    public void onServerConnect(ServerConnectEvent event) {
        if (event.getReason() == ServerConnectEvent.Reason.JOIN_PROXY) {
            List<ServerInfo> lobbyServers = new ArrayList<>();
            for (ServerInfo serverInfo : getSkyBungee().getProxy().getServers().values()) {
                if (serverInfo.getName().startsWith("Lobby-")) {
                    lobbyServers.add(serverInfo);
                }
            }
            Random random = new Random();
            event.setTarget(lobbyServers.get(random.nextInt(lobbyServers.size() + 1)));
        }
    }
}
