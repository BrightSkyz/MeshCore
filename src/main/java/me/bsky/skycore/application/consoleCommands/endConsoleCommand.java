package me.bsky.skycore.application.consoleCommands;

import me.bsky.skycore.application.SkyConsole;
import me.bsky.skycore.application.SkyConsoleCommand;
import me.bsky.skycore.types.SkyModule;

public class endConsoleCommand extends SkyConsoleCommand {

    public endConsoleCommand(SkyConsole skyConsole) {
        super("end", "Stop SkyCore peacefully", skyConsole);
    }

    @Override
    public void execute(String[] args) {
        getSkyLogger().info("SkyCore is shutting down...");
        for (SkyModule skyModule : getSkyConsole().getSkyApplication().getModuleMap().values()) {
            getSkyLogger().info("Disabling the module " + skyModule.getModuleName());
            skyModule.onDisableApplication();
        }
        getSkyLogger().info("SkyCore is done shutting down.");
        System.exit(0);
    }
}
