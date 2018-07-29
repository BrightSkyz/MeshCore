package me.bsky.skycore.bungee.events.subapi;

import me.bsky.skycore.api.helpers.ServerHelpers;
import me.bsky.skycore.bungee.SkyBungee;
import net.ME1312.SubServers.Bungee.Event.SubAddHostEvent;
import net.ME1312.SubServers.Bungee.Library.Version.Version;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class SubAddHost implements Listener {

    private SkyBungee skyBungee;

    public SubAddHost(SkyBungee skyBungee) {
        this.skyBungee = skyBungee;
    }

    @EventHandler
    public void onSubAddHost(SubAddHostEvent event) {
        if (!event.getHost().getName().equalsIgnoreCase("~")) {
            if (getSkyBungee().getSubAPI().getServers().isEmpty()) {
                // Generate the random port for the server
                Integer serverPort = ServerHelpers.getRandomUnusedPort(getSkyBungee().getSubAPI());
                // Create the server
                event.getHost().getCreator().create("Lobby-1", event.getHost().getCreator().getTemplate("Vanilla"), Version.fromString("1.13"), serverPort);
            }
        }
    }

    public SkyBungee getSkyBungee() {
        return skyBungee;
    }
}
