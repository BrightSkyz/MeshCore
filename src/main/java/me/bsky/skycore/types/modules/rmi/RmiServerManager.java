package me.bsky.skycore.types.modules.rmi;

import me.bsky.skycore.application.SkyApplication;
import me.bsky.skycore.types.SkyServer;
import me.bsky.skycore.types.enums.LogLevel;
import me.bsky.skycore.types.enums.ServerType;
import me.bsky.skycore.types.interfaces.IBungeeManager;
import me.bsky.skycore.types.interfaces.IServerManager;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RmiServerManager extends UnicastRemoteObject implements IServerManager {

    private SkyApplication skyApplication;
    private Registry registry;
    private Integer rmiPort;
    private String rmiPass;

    public RmiServerManager(SkyApplication skyApplication, Integer rmiPort, String rmiPass) throws RemoteException {
        super();
        this.skyApplication = skyApplication;
        this.rmiPort = rmiPort;
        this.rmiPass = rmiPass;
        registry = LocateRegistry.createRegistry(rmiPort);
        registry.rebind("skycore", this);
    }

    private SkyApplication getSkyApplication() {
        return skyApplication;
    }

    @Override
    public boolean testConnection(String pass, String serverName) {
        if (pass.equals(rmiPass)) {
            getSkyApplication().getSkyLogger().info("A test connection from " + serverName + " was successful.");
            return true;
        }
        return false;
    }

    @Override
    public boolean bungeecordReady(String pass, Integer port) {
        if (pass.equals(rmiPass)) {
            getSkyApplication().getSkyLogger().info("The Bungeecord proxy on port " + port + " is ready.");
            createServer(pass, "Lobby-1", ServerType.LOBBY, true, null, null);
            Integer finalPort = getSkyApplication().getRmiModule().getConfiguration().getInt("rmi.port") + port;
            try {
                getSkyApplication().setRemote((IBungeeManager)Naming.lookup("rmi://localhost:" + finalPort + "/skycore"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            SkyServer skyServer = getServer(pass, "Lobby-1");
            if (skyServer != null) {
                try {
                    getSkyApplication().getRemote().addServerToBungeecord(pass, skyServer.getServerName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean createServer(String pass, String serverName, ServerType serverType, Boolean deleteOnStop, Integer port, Integer maxRam) {
        if (pass.equals(rmiPass)) {
            getSkyApplication().getServerManagerModule().createServer(serverName, serverType, deleteOnStop, port, maxRam);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeServer(String pass, String serverName) {
        if (pass.equals(rmiPass)) {
            getServer(pass, serverName).stopServer(true);
            return true;
        }
        return false;
    }

    @Override
    public SkyServer getServer(String pass, String serverName) {
        if (pass.equals(rmiPass)) {
            return getSkyApplication().getServerManagerModule().getServer(serverName);
        }
        return null;
    }

    @Override
    public boolean sendCommand(String pass, String serverName, String command) {
        if (pass.equals(rmiPass)) {
            SkyServer skyServer = getServer(pass, serverName);
            if (skyServer != null) {
                getServer(pass, serverName).sendCommand(command);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean log(LogLevel logLevel, String message) {
        getSkyApplication().getSkyLogger().log(logLevel, message);
        return true;
    }
}