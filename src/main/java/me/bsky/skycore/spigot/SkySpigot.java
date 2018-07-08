package me.bsky.skycore.spigot;

import me.bsky.skycore.types.SkyLogger;
import me.bsky.skycore.types.SkyModule;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class SkySpigot extends JavaPlugin {

    private Map<String, SkyModule> moduleMap = new HashMap<>();

    private SkyLogger skyLogger;

    @Override
    public void onEnable() {
        skyLogger = new SkyLogger(System.out);
        // Register the modules for the Spigot plugin
    }

    @Override
    public void onDisable() {
        //
    }

    private void registerModule(SkyModule skyModule) {
        // Register a module
        moduleMap.put(skyModule.getModuleName(), skyModule);
    }

    public SkyLogger getSkyLogger() {
        return skyLogger;
    }
}
