package me.bsky.skycore.api.helpers;

import me.bsky.skycore.bungee.SkyBungee;

public class SetTemporaryThread extends Thread {

    private SkyBungee skyBungee;
    private String serverName;

    public SetTemporaryThread(SkyBungee skyBungee, String serverName) {
        this.skyBungee = skyBungee;
        this.serverName = serverName;
    }

    public void run() {
        getSkyBungee().getProxy().getLogger().info("SetTemporaryThread has started and will now set the server " + serverName + " as temporary.");
        while (!getSkyBungee().getSubAPI().getSubServer(serverName).isTemporary()) {
            boolean before = getSkyBungee().getSubAPI().getSubServer(serverName).isTemporary();
            getSkyBungee().getSubAPI().getSubServer(serverName).setTemporary(true);
            if (before != getSkyBungee().getSubAPI().getSubServer(serverName).isTemporary()) {
                getSkyBungee().getProxy().getLogger().info("The server " + serverName + " is now marked as temporary.");
            }
        }
    }

    public SkyBungee getSkyBungee() {
        return skyBungee;
    }

    public String getServerName() {
        return serverName;
    }
}
