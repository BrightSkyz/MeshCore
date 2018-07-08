package me.bsky.skycore.bungeecord;

import me.bsky.skycore.types.SkyLogger;
import me.bsky.skycore.types.SkyModule;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class SkyBungee extends Plugin {

    private Map<String, SkyModule> moduleMap = new HashMap<>();

    private SkyLogger skyLogger;

    @Override
    public void onEnable() {
        skyLogger = new SkyLogger(System.out);
        // Register the modules for the Bungeecord plugin
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
