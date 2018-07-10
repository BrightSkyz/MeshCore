package me.bsky.skycore.application;

import me.bsky.skycore.types.SkyLogger;

public abstract class SkyConsoleCommand {

    private String name;
    private String description;
    private SkyConsole skyConsole;

    public SkyConsoleCommand(String name, String description, SkyConsole skyConsole) {
        this.name = name;
        this.description = description;
        this.skyConsole = skyConsole;
    }

    public abstract void execute(String args[]);

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public SkyConsole getSkyConsole() {
        return skyConsole;
    }

    public SkyLogger getSkyLogger() {
        return skyConsole.getSkyApplication().getSkyLogger();
    }
}
