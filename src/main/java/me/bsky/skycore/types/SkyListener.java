package me.bsky.skycore.types;

import me.bsky.skycore.bungeecord.SkyBungee;
import me.bsky.skycore.spigot.SkySpigot;
import me.bsky.skycore.types.enums.ProgramMode;
import net.md_5.bungee.api.plugin.Listener;

public abstract class SkyListener implements Listener, org.bukkit.event.Listener {

    private ProgramMode programMode;
    private Object main;

    public SkyListener(ProgramMode programMode, Object main) {
        this.programMode = programMode;
        this.main = main;
    }

    public SkyBungee getSkyBungee() {
        if (programMode == ProgramMode.BUNGEECORD) {
            return (SkyBungee) main;
        }
        return null;
    }

    public SkySpigot getSkySpigot() {
        if (programMode == ProgramMode.SPIGOT) {
            return (SkySpigot) main;
        }
        return null;
    }

    public ProgramMode getProgramMode() {
        return programMode;
    }
}
