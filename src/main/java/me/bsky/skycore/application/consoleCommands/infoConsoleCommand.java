package me.bsky.skycore.application.consoleCommands;

import me.bsky.skycore.application.SkyConsole;
import me.bsky.skycore.application.SkyConsoleCommand;

public class infoConsoleCommand extends SkyConsoleCommand {

    public infoConsoleCommand(SkyConsole skyConsole) {
        super("info", skyConsole);
    }

    @Override
    public void execute(String[] args) {
        getSkyLogger().info("--- Info for SkyCore ---");
        getSkyLogger().info("The core plugin for the open source Minecraft network SkyCove.");
        getSkyLogger().info("Created by BrightSkyz and its contributors.");
        getSkyLogger().info("---");
        getSkyLogger().info("SkyCove Website: https://skycove.co");
        getSkyLogger().info("Github: https://github.com/BrightSkyz/SkyCore");
        getSkyLogger().info("Issue Tracker: https://github.com/BrightSkyz/SkyCore/issues");
    }
}
