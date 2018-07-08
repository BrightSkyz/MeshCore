package me.bsky.skycore.application;

import me.bsky.skycore.types.SkyLogger;

public abstract class SkyConsoleCommand {

    private String name;
    private SkyConsole skyConsole;

    public SkyConsoleCommand(String name, SkyConsole skyConsole) {
        this.name = name;
        this.skyConsole = skyConsole;
    }

    public abstract void execute(String args[]);

    public String getName() {
        return name;
    }

    public SkyConsole getSkyConsole() {
        return skyConsole;
    }

    public SkyLogger getSkyLogger() {
        return skyConsole.getSkyApplication().getSkyLogger();
    }
}
