package me.bsky.skycore.types;

import me.bsky.skycore.types.enums.ServerType;
import me.bsky.skycore.types.modules.servermanager.ServerManagerModule;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.Charset;

public class SkyServer {

    private Process serverProcess;
    private ProcessBuilder serverProcessBuilder;

    private String name;
    private ServerType serverType;
    private Boolean deleteOnStop;
    private Integer port;
    private Integer maxRam;
    private ServerManagerModule serverManagerModule;
    private SkyLogger skyLogger;

    public SkyServer(String name, ServerType serverType, Boolean deleteOnStop, Integer port, Integer maxRam, ServerManagerModule serverManagerModule) {
        this.name = name;
        this.serverType = serverType;
        this.deleteOnStop = deleteOnStop;
        this.port = port;
        this.maxRam = maxRam;
        this.serverManagerModule = serverManagerModule;
        this.skyLogger = serverManagerModule.getSkyLogger();
        // Create the server
        skyLogger.info("Creating the server " + name + " (" + serverType.getCleanName() + ")");
        File serverDirectory = new File("./servers/" + name).getAbsoluteFile();
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
        getSkyLogger().info("Copying the files needed for the server " + getName() + " (" + getServerType().getCleanName() + ")");
        if (getServerType().getJarName().equalsIgnoreCase("spigot.jar")) {
            // Copy the server jar file
            File serverJarFile = new File("./data/spigot.jar");
            File serverJarFileDest = new File("./servers/" + getName() + "/spigot.jar");
            try {
                FileUtils.copyFile(serverJarFile, serverJarFileDest);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Copy over the Bungeecord config files
            copyConfigHelper("/server-configs/spigot-spigot.yml", "./servers/" + getName() + "/spigot.yml");
            copyConfigHelper("/server-configs/spigot-server.properties", "./servers/" + getName() + "/server.properties");
            File serverPropertiesFile = new File("./servers/" + getName() + "/server.properties");
            String serverPropertiesString;
            try {
                serverPropertiesString = FileUtils.readFileToString(serverPropertiesFile, Charset.defaultCharset());
                serverPropertiesString = serverPropertiesString.replace("{SERVER_PORT}", getPort() + "");
                FileUtils.writeStringToFile(serverPropertiesFile, serverPropertiesString, Charset.defaultCharset());
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Start the server
            getSkyLogger().info("Executing the start command the the server " + getName() + ".");
            serverProcessBuilder = new ProcessBuilder("java", "-Xms128M", "-Xmx" + getMaxRam() + "M", "-Dcom.mojang.eula.agree=true", "-jar", getServerType().getJarName(), "--port", getPort() + "");
            serverProcessBuilder.directory(new File("./servers/" + getName() + "/").getAbsoluteFile());
            try {
                serverProcess = serverProcessBuilder.start();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (getServerType().getJarName().equalsIgnoreCase("bungeecord.jar")) {
            // Copy the server jar file
            File serverJarFile = new File("./data/bungeecord.jar");
            File serverJarFileDest = new File("./servers/" + getName() + "/bungeecord.jar");
            try {
                FileUtils.copyFile(serverJarFile, serverJarFileDest);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Copy over the Bungeecord config files
            copyConfigHelper("/server-configs/bungeecord-config.yml", "./servers/" + getName() + "/config.yml");
            File configYmlFile = new File("./servers/" + getName() + "/config.yml");
            String configYmlString;
            try {
                configYmlString = FileUtils.readFileToString(configYmlFile, Charset.defaultCharset());
                configYmlString = configYmlString.replace("{SERVER_PORT}", getPort() + "");
                FileUtils.writeStringToFile(configYmlFile, configYmlString, Charset.defaultCharset());
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Start the server
            getSkyLogger().info("Executing the start command the the server " + getName() + ".");
            serverProcessBuilder = new ProcessBuilder("java", "-Xms128M", "-Xmx" + getMaxRam() + "M", "-jar", getServerType().getJarName());
            serverProcessBuilder.directory(new File("./servers/" + getName() + "/").getAbsoluteFile());
            try {
                serverProcess = serverProcessBuilder.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendCommand(String command) {
        getSkyLogger().info("A command has been sent to the server " + getName() + ": " + command);
        //PrintWriter printWriter = new PrintWriter(getServerProcess().getOutputStream());
        try {
            //printWriter.write(command + "\n");
            //printWriter.println(command);
            //printWriter.flush();
            //printWriter.close();
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
            getSkyLogger().info("The server " + getName() + " is stopping...");
            sendCommand("stop");
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
                getSkyLogger().info("The server " + getName() + " is set to delete on stop so it will.");
                try {
                    FileUtils.deleteDirectory(new File("./servers/" + getName() + "/"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (removeFromList) {
                getServerManagerModule().removeServer(getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
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
