package me.bsky.skycore.bungee;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.bsky.skycore.api.helpers.ServerHelpers;
import me.bsky.skycore.bungee.commands.skyBungeeCommand;
import me.bsky.skycore.bungee.events.postLoginEvent;
import net.ME1312.SubServers.Bungee.Host.Host;
import net.ME1312.SubServers.Bungee.Host.Server;
import net.ME1312.SubServers.Bungee.Host.SubCreator;
import net.ME1312.SubServers.Bungee.Library.Version.Version;
import net.ME1312.SubServers.Bungee.SubAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SkyBungee extends Plugin {

    private Configuration configuration;
    private HikariDataSource hikariDataSource;
    private SubAPI subAPI;

    private BaseComponent[] messagePrefix = new ComponentBuilder("SkyBungee").color(ChatColor.AQUA).append(" > ").color(ChatColor.WHITE).create();

    @Override
    public void onEnable() {
        // Enable SkyBungee

        // First, create config.yml if it doesn't exist on the server
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, configFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Load the configuration since we know it exists
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Attempt connection to the MySQL database
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.jdbc.Driver");
        hikariConfig.setConnectionTestQuery("SELECT 1");
        hikariConfig.setJdbcUrl("jdbc:mysql://" + getConfiguration().getString("mysql.host", "localhost") + ":" + getConfiguration().getInt("mysql.port", 3306) + "/" + getConfiguration().getString("mysql.database", "skycore"));
        hikariConfig.setMaximumPoolSize(getConfiguration().getInt("mysql.max-pool-size", 5));
        hikariConfig.setUsername(getConfiguration().getString("mysql.user", "root"));
        hikariConfig.setPassword(getConfiguration().getString("mysql.pass", ""));
        hikariDataSource = new HikariDataSource(hikariConfig);

        // Setup connection to the SubAPI
        subAPI = SubAPI.getInstance();
        getProxy().getLogger().info("SubAPI wrapper version: " + getSubAPI().getWrapperVersion());

        // Register the commands
        getProxy().getPluginManager().registerCommand(this, new skyBungeeCommand(this));

        // Register the listeners
        getProxy().getPluginManager().registerListener(this, new postLoginEvent(this));

        // Wait for a host to exist
        boolean hostsExist = false;
        while (!hostsExist) {
            if (!getSubAPI().getHosts().isEmpty()) {
                hostsExist = true;
            }
        }
        // Get a random host server to create the lobby server on
        List<Host> hostsList = new ArrayList<Host>(getSubAPI().getHosts().values());
        int randomHostIndex = new Random().nextInt(hostsList.size());
        Host randomHost = hostsList.get(randomHostIndex);
        // Generate the random port for the server
        Integer serverPort = ServerHelpers.getRandomUnusedPort(getSubAPI());
        // Add a lobby server
        getSubAPI().addServer("Lobby-1", randomHost.getAddress(), serverPort, "Lobby", false, false);
        // Create the server
        randomHost.getCreator().create("Lobby-1", randomHost.getCreator().getTemplate("Vanilla"), Version.fromString("1.13"), serverPort);
    }

    @Override
    public void onDisable() {
        // Disable SkyBungee
    }

    public ServerInfo generateServerInfo(Server server) {
        return getProxy().constructServerInfo(server.getName(), server.getAddress(), server.getMotd(), server.isRestricted());
    }

    public SkyBungee getInstance() {
        // Get the SkyBungee instance
        return this;
    }

    public Configuration getConfiguration() {
        // Get the configuration for the plugin
        return configuration;
    }

    public HikariDataSource getHikariDataSource() {
        // Get the HikariDataSource for the MySQL
        return hikariDataSource;
    }

    public SubAPI getSubAPI() {
        return subAPI;
    }

    public BaseComponent[] getMessagePrefix() {
        return messagePrefix;
    }
}
