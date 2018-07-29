package me.bsky.skycore.bungee.events.subapi;

import me.bsky.skycore.bungee.SkyBungee;
import net.ME1312.SubServers.Bungee.Event.SubStartEvent;
import net.ME1312.SubServers.Bungee.Event.SubStoppedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class SubStart implements Listener {

    private SkyBungee skyBungee;

    public SubStart(SkyBungee skyBungee) {
        this.skyBungee = skyBungee;
    }

    @EventHandler
    public void subStartEvent(SubStartEvent event) {
        getSkyBungee().getProxy().getLogger().info("The server " + event.getServer().getName() + " has started.");
        if (event.getServer().getName().startsWith("Lobby-") || event.getServer().getName().startsWith("Mini-")) {
            event.getServer().setTemporary(true);
        } else {
            getSkyBungee().getProxy().getLogger().info("The server " + event.getServer().getName() + " is not a lobby or a mini server.");
        }
    }

    public SkyBungee getSkyBungee() {
        return skyBungee;
    }
}