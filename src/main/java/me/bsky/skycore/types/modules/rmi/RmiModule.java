package me.bsky.skycore.types.modules.rmi;

import me.bsky.skycore.types.SkyModule;
import me.bsky.skycore.types.enums.ProgramMode;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.rmi.RemoteException;

public class RmiModule extends SkyModule {

    private Configuration configuration;

    private RmiServerManager rmiServerManager;
    private RmiBungeeManager rmiBungeeManager;

    public RmiModule(ProgramMode programMode, Object main) {
        super("RmiModule", programMode, main);
    }

    @Override
    public void onEnableApplication() {
        // Create the plugin config file from the jar
        if (new File("./data/plugin-config.yml").isFile()) {
            //getSkyLogger().info("The config for the plugin already exists so a new one will not override it.");
        } else {
            //getSkyLogger().info("The config for the plugin doesn't exist in ./data/plugin-config.yml. Please edit it or the server will not function.");
            copyConfigHelper("/server-configs/plugin-config.yml", "./data/plugin-config.yml");
        }
        // Get the config
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File("./data/plugin-config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Create the RMI server
        getSkyLogger().info("RmiServerManager is now listening on port " + configuration.getInt("rmi.port"));
        try {
            rmiServerManager = new RmiServerManager(getSkyApplication(), configuration.getInt("rmi.port"), configuration.getString("rmi.pass"));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisableApplication() {
        //
    }

    @Override
    public void onEnableBungee() {
        // Create the RMI server for Bungeecord
        final Integer[] bungeePort = {0};
        getSkyBungee().getProxy().getConfig().getListeners().forEach(listenerInfo -> {
            bungeePort[0] = listenerInfo.getHost().getPort();
        });
        Integer finalPort = getSkyBungee().getConfiguration().getInt("rmi.port") + bungeePort[0];
        getSkyLogger().info("RmiBungeeManager is now listening on port " + finalPort);
        try {
            rmiBungeeManager = new RmiBungeeManager(getSkyBungee(), finalPort, getSkyBungee().getConfiguration().getString("rmi.pass"));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisableBungee() {
        //
    }

    private void copyConfigHelper(String pathInJar, String outputPath) {
        try {
            InputStream configIs = Class.class.getResourceAsStream(pathInJar);
            InputStreamReader configIsr = new InputStreamReader(configIs);
            BufferedReader configBr = new BufferedReader(configIsr);
            StringBuilder configSb = new StringBuilder();
            String configLine;
            while ((configLine = configBr.readLine()) != null) {
                configSb.append(configLine).append("\n");
            }
            FileUtils.writeStringToFile(new File(outputPath).getAbsoluteFile(), configSb.toString(), Charset.defaultCharset());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
