package me.bsky.skycore.application.consoleCommands;

import me.bsky.skycore.application.SkyConsole;
import me.bsky.skycore.application.SkyConsoleCommand;
import me.bsky.skycore.types.SkyServer;
import me.bsky.skycore.types.enums.ServerType;

public class statsConsoleCommand extends SkyConsoleCommand {

    public statsConsoleCommand(SkyConsole skyConsole) {
        super("stats", "View quick stats", skyConsole);
    }

    @Override
    public void execute(String[] args) {
        Integer serverCount = 0;
        Integer maxRamOverall = 0;
        Integer bungeecordCount = 0;
        Integer spigotCount = 0;
        Integer lobbyCount = 0;
        Integer miniCount = 0;
        for (SkyServer skyServer : getSkyConsole().getSkyApplication().getSkyServers()) {
            serverCount++;
            maxRamOverall = maxRamOverall + skyServer.getMaxRam();
            if (skyServer.getServerType() == ServerType.BUNGEECORD) {
                bungeecordCount++;
            } else if (skyServer.getServerType() == ServerType.SPIGOT) {
                spigotCount++;
            } else if (skyServer.getServerType() == ServerType.LOBBY) {
                lobbyCount++;
            } else if (skyServer.getServerType() == ServerType.MINI) {
                miniCount++;
            }
        }
        getSkyLogger().info("--- Stats for SkyCore ---");
        getSkyLogger().info("Overall server count: " + serverCount);
        getSkyLogger().info("Server Types: ");
        getSkyLogger().info(" Bungeecord count: " + bungeecordCount);
        getSkyLogger().info(" Spigot count: " + spigotCount);
        getSkyLogger().info(" Lobby count: " + lobbyCount);
        getSkyLogger().info(" Mini count: " + miniCount);
        getSkyLogger().info("Overall max RAM: " + maxRamOverall);
    }
}
