package me.bsky.skycore.types;

import me.bsky.skycore.application.SkyApplication;
import me.bsky.skycore.bungeecord.SkyBungee;
import me.bsky.skycore.spigot.SkySpigot;
import me.bsky.skycore.types.enums.ProgramMode;

public abstract class SkyModule {

    private ProgramMode programMode;
    private String moduleName;

    private SkyApplication skyApplication = null;
    private SkyBungee skyBungee = null;
    private SkySpigot skySpigot = null;

    private SkyLogger skyLogger = null;

    public SkyModule(String moduleName, ProgramMode programMode, Object main) {
        this.programMode = programMode;
        this.moduleName = moduleName;
        if (programMode == ProgramMode.APPLICATION) {
            skyApplication = (SkyApplication)main;
            skyLogger = skyApplication.getSkyLogger();
            onEnableApplication();
        } else if (programMode == ProgramMode.BUNGEECORD) {
            skyBungee = (SkyBungee)main;
            skyLogger = skyBungee.getSkyLogger();
            onEnableBungee();
        } else if (programMode == ProgramMode.SPIGOT) {
            skySpigot = (SkySpigot)main;
            onEnableSpigot();
        }
    }

    public void onEnableApplication() {
        // Empty
    }
    public void onDisableApplication() {
        // Empty
    }

    public void onEnableBungee() {
        // Empty
    }
        public void onDisableBungee() {
        // Empty
    }

    public void onEnableSpigot() {
        // Empty
    }
    public void onDisableSpigot() {
        // Empty
    }

    public ProgramMode getProgramMode() {
        return programMode;
    }

    public String getModuleName() {
        return moduleName;
    }

    public SkyApplication getSkyApplication() {
        return skyApplication;
    }

    public SkyBungee getSkyBungee() {
        return skyBungee;
    }

    public SkySpigot getSkySpigot() {
        return skySpigot;
    }

    public Object getMain() {
        if (programMode == ProgramMode.APPLICATION) {
            return skyApplication;
        } else if (programMode == ProgramMode.BUNGEECORD) {
            return skyBungee;
        } else if (programMode == ProgramMode.SPIGOT) {
            return skySpigot;
        }
        return null;
    }

    public SkyLogger getSkyLogger() {
        return skyLogger;
    }
}
