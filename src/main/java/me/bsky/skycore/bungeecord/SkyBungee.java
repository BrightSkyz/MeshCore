package me.bsky.skycore.bungeecord;

import me.bsky.skycore.bungeecord.listeners.ServerConnect;
import me.bsky.skycore.types.SkyListener;
import me.bsky.skycore.types.SkyLogger;
import me.bsky.skycore.types.SkyModule;
import me.bsky.skycore.types.SkyPlayer;
import me.bsky.skycore.types.enums.LogLevel;
import me.bsky.skycore.types.enums.ProgramMode;
import me.bsky.skycore.types.interfaces.IServerManager;
import me.bsky.skycore.types.modules.rmi.RmiModule;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.rmi.Naming;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkyBungee extends Plugin {

    private Map<String, SkyModule> moduleMap = new HashMap<>();
    private RmiModule rmiModule = null;

    private Map<UUID, SkyPlayer> skyPlayerMap = new HashMap<>();

    private Configuration configuration = null;
    private IServerManager remote = null;
    private SkyLogger skyLogger;

    @Override
    public void onEnable() {
        skyLogger = new SkyLogger(System.out);
        // Get the config and put it in the configuration variable
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Register the modules for the Bungeecord plugin
        rmiModule = new RmiModule(ProgramMode.BUNGEECORD, this);
        registerModule(rmiModule);
        // Setup remote connection to application
        try {
            remote = (IServerManager) Naming.lookup("rmi://" + getConfiguration().getString("rmi.host") + ":" + getConfiguration().getInt("rmi.port") + "/skycore");
            boolean connectionTest = remote.testConnection(getConfiguration().getString("rmi.pass"), "a Bungeecord proxy");
            if (connectionTest) {
                getSkyLogger().info("Connection to the RmiServerManager was successful.");
                final Integer[] bungeePort = {0};
                getProxy().getConfig().getListeners().forEach(listenerInfo -> {
                    bungeePort[0] = listenerInfo.getHost().getPort();
                });
                getRemote().bungeecordReady(getConfiguration().getString("rmi.pass"), bungeePort[0]);
            } else {
                getSkyLogger().warn("Connection to the RmiServerManager was not successful.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Enable the listeners
        registerListener(new ServerConnect(this));
        // Log to the application that SkyBungee has been enabled.
        try {
            getRemote().log(LogLevel.INFO, "SkyBungee has been enabled.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        try {
            getRemote().log(LogLevel.INFO, "SkyBungee is disabling.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerModule(SkyModule skyModule) {
        // Register a module
        moduleMap.put(skyModule.getModuleName(), skyModule);
    }

    public Map<UUID, SkyPlayer> getSkyPlayerMap() {
        return skyPlayerMap;
    }

    public SkyLogger getSkyLogger() {
        return skyLogger;
    }

    public IServerManager getRemote() {
        return remote;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void registerListener(SkyListener skyListener) {
        getProxy().getPluginManager().registerListener(this, skyListener);
    }
}
