package me.bsky.skycore.bungee.events.subapi;

import me.bsky.skycore.bungee.SkyBungee;
import net.ME1312.SubServers.Bungee.Event.SubStopEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class subStopEvent implements Listener {

    private SkyBungee skyBungee;

    public subStopEvent(SkyBungee skyBungee) {
        this.skyBungee = skyBungee;
    }

    @EventHandler
    public void subStopEvent(SubStopEvent event) {
        event.setCancelled(true);
        getSkyBungee().getProxy().getLogger().info("The server " + event.getServer().getName() + " has stopped.");
        if (event.getServer().getName().startsWith("Lobby-") || event.getServer().getName().startsWith("Mini-")) {
            if (event.getServer().isTemporary()) {
                getSkyBungee().getProxy().getLogger().info("The server " + event.getServer().getName() + " is temporary.");
            } else {
                getSkyBungee().getProxy().getLogger().info("The server " + event.getServer().getName() + " isn't temporary so it was set to be.");
            }
            event.getServer().setTemporary(true);
            getSkyBungee().getProxy().getLogger().info("The server " + event.getServer().getName() + " is being deleted...");
            /*try {
                event.getServer().getHost().forceDeleteSubServer(event.getServer().getName());
                event.getServer().getHost().forceRemoveSubServer(event.getServer().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        } else {
            getSkyBungee().getProxy().getLogger().info("The server " + event.getServer().getName() + " is not a lobby or a mini server.");
        }
    }

    public SkyBungee getSkyBungee() {
        return skyBungee;
    }
}
