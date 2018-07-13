package me.bsky.skycore.types;

import me.bsky.skycore.types.enums.ServerType;
import me.bsky.skycore.types.modules.servermanager.ServerManagerModule;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

public class SkyServer {

    private Process serverProcess;
    private ProcessBuilder serverProcessBuilder;

    private String serverName;
    private ServerType serverType;
    private Boolean deleteOnStop;
    private Integer port;
    private Integer maxRam;
    private ServerManagerModule serverManagerModule;
    private SkyLogger skyLogger;

    public SkyServer(String serverName, ServerType serverType, Boolean deleteOnStop, Integer port, Integer maxRam, ServerManagerModule serverManagerModule) {
        this.serverName = serverName;
        this.serverType = serverType;
        this.deleteOnStop = deleteOnStop;
        this.port = port;
        this.maxRam = maxRam;
        this.serverManagerModule = serverManagerModule;
        this.skyLogger = serverManagerModule.getSkyLogger();
        // Create the server
        skyLogger.info("Creating the server " + serverName + " (" + serverType.getCleanName() + ")");
        File serverDirectory = new File("./servers/" + serverName).getAbsoluteFile();
        if (serverDirectory.isDirectory()) {
            skyLogger.info("The server directory already exists so the server will just start.");
            startServer();
        } else {
            skyLogger.info("The server directory doesn't exist, creating one...");
            serverDirectory.mkdir();
            startServer();
        }
        getServerManagerModule().getServers().add(this);
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

    public void startServer() {
        getSkyLogger().info("Copying the files needed for the server " + getServerName() + " (" + getServerType().getCleanName() + ")");
        // Check if plugins directory doesn't exist
        if (!new File("./servers/" + getServerName() + "/plugins/").isDirectory()) {
            new File("./servers/" + getServerName() + "/plugins/").mkdir();
        }
        // Check if SkyCore plugin directory doesn't exist
        if (!new File("./servers/" + getServerName() + "/plugins/SkyCore/").isDirectory()) {
            new File("./servers/" + getServerName() + "/plugins/SkyCore/").mkdir();
        }
        // Copy the plugin to the server
        try {
            FileUtils.copyFile(new File("./SkyCore-all.jar"), new File("./servers/" + getServerName() + "/plugins/SkyCore-all.jar"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Setup the plugin config
        if (new File("./data/plugin-config.yml").isFile()) {
            getSkyLogger().info("The config for the plugin already exists so a new one will not override it.");
        } else {
            getSkyLogger().info("The config for the plugin doesn't exist in ./data/plugin-config.yml. Please edit it or the server will not function.");
            copyConfigHelper("/server-configs/plugin-config.yml", "./data/plugin-config.yml");
        }
        // Do server type based configuration + start server
        if (getServerType().getJarName().equalsIgnoreCase("spigot.jar")) {
            // Copy the server jar file
            File serverJarFile = new File("./data/spigot.jar");
            File serverJarFileDest = new File("./servers/" + getServerName() + "/spigot.jar");
            try {
                FileUtils.copyFile(serverJarFile, serverJarFileDest);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Copy the SkySpigot plugin config file
            try {
                FileUtils.copyFile(new File("./data/plugin-config.yml"), new File("./servers/" + getServerName() + "/plugins/SkySpigot/config.yml"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Copy over the Spigot config files
            copyConfigHelper("/server-configs/spigot-spigot.yml", "./servers/" + getServerName() + "/spigot.yml");
            copyConfigHelper("/server-configs/spigot-server.properties", "./servers/" + getServerName() + "/server.properties");
            File serverPropertiesFile = new File("./servers/" + getServerName() + "/server.properties");
            String serverPropertiesString;
            try {
                serverPropertiesString = FileUtils.readFileToString(serverPropertiesFile, Charset.defaultCharset());
                serverPropertiesString = serverPropertiesString.replace("{SERVER_PORT}", getPort() + "");
                FileUtils.writeStringToFile(serverPropertiesFile, serverPropertiesString, Charset.defaultCharset());
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Start the server
            getSkyLogger().info("Executing the start command the the server " + getServerName() + ".");
            serverProcessBuilder = new ProcessBuilder("java", "-Xms128M", "-Xmx" + getMaxRam() + "M", "-Dcom.mojang.eula.agree=true", "-jar", getServerType().getJarName(), "--port", getPort() + "");
            serverProcessBuilder.directory(new File("./servers/" + getServerName() + "/").getAbsoluteFile());
            try {
                serverProcess = serverProcessBuilder.start();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (getServerType().getJarName().equalsIgnoreCase("bungeecord.jar")) {
            // Copy the server jar file
            File serverJarFile = new File("./data/bungeecord.jar");
            File serverJarFileDest = new File("./servers/" + getServerName() + "/bungeecord.jar");
            try {
                FileUtils.copyFile(serverJarFile, serverJarFileDest);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Copy the SkyBungee plugin config file
            try {
                FileUtils.copyFile(new File("./data/plugin-config.yml"), new File("./servers/" + getServerName() + "/plugins/SkyBungee/config.yml"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Copy over the Bungeecord config files
            copyConfigHelper("/server-configs/bungeecord-config.yml", "./servers/" + getServerName() + "/config.yml");
            File configYmlFile = new File("./servers/" + getServerName() + "/config.yml");
            String configYmlString;
            try {
                configYmlString = FileUtils.readFileToString(configYmlFile, Charset.defaultCharset());
                configYmlString = configYmlString.replace("{SERVER_PORT}", getPort() + "");
                FileUtils.writeStringToFile(configYmlFile, configYmlString, Charset.defaultCharset());
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Start the server
            getSkyLogger().info("Executing the start command the the server " + getServerName() + ".");
            serverProcessBuilder = new ProcessBuilder("java", "-Xms128M", "-Xmx" + getMaxRam() + "M", "-jar", getServerType().getJarName());
            serverProcessBuilder.directory(new File("./servers/" + getServerName() + "/").getAbsoluteFile());
            try {
                serverProcess = serverProcessBuilder.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendCommand(String command) {
        getSkyLogger().info("A command has been sent to the server " + getServerName() + ": " + command);
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(getServerProcess().getOutputStream()));
            bufferedWriter.write(command);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        stopServer(false);
    }

    public void stopServer(boolean removeFromList) {
        try {
            getSkyLogger().info("The server " + getServerName() + " is stopping...");
            if (serverType.getJarName().equalsIgnoreCase("bungeecord.jar")) {
                sendCommand("end");
            } else {
                sendCommand("stop");
            }
            boolean hasExited = false;
            // Wait until the process has exited
            while (!hasExited) {
                try {
                    if (getServerProcess().exitValue() == 0) {
                        hasExited = true;
                    }
                } catch (Exception e) {
                    // We're just going to ignore the error since it is caused by the process being alive
                }
            }
            getServerProcess().destroy();
            if (getDeleteOnStop()) {
                getSkyLogger().info("The server " + getServerName() + " is set to delete on stop so it will.");
                try {
                    FileUtils.deleteDirectory(new File("./servers/" + getServerName() + "/"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (removeFromList) {
                getServerManagerModule().removeServer(getServerName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getServerName() {
        return serverName;
    }

    public ServerType getServerType() {
        return serverType;
    }

    public Boolean getDeleteOnStop() {
        return deleteOnStop;
    }

    public Integer getPort() {
        return port;
    }

    public Integer getMaxRam() {
        return maxRam;
    }

    public ServerManagerModule getServerManagerModule() {
        return serverManagerModule;
    }

    public SkyLogger getSkyLogger() {
        return skyLogger;
    }

    public Process getServerProcess() {
        return serverProcess;
    }

    public void setDeleteOnStop(Boolean deleteOnStop) {
        this.deleteOnStop = deleteOnStop;
    }
}