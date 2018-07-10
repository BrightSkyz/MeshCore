package me.bsky.skycore.types.modules.rmi;

import me.bsky.skycore.bungeecord.SkyBungee;
import me.bsky.skycore.types.SkyServer;
import me.bsky.skycore.types.interfaces.IBungeeManager;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.net.InetSocketAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RmiBungeeManager extends UnicastRemoteObject implements IBungeeManager {

    private SkyBungee skyBungee;
    private Registry registry;
    private Integer rmiPort;
    private String rmiPass;

    public RmiBungeeManager(SkyBungee skyBungee, Integer rmiPort, String rmiPass) throws RemoteException {
        super();
        this.skyBungee = skyBungee;
        this.rmiPort = rmiPort;
        this.rmiPass = rmiPass;
        registry = LocateRegistry.createRegistry(rmiPort);
        registry.rebind("skycore", this);
    }


    @Override
    public boolean testConnection(String pass) {
        if (pass.equals(rmiPass)) {
            getSkyBungee().getSkyLogger().info("A test connection was successful.");
            return true;
        }
        return false;
    }

    @Override
    public boolean addServerToBungeecord(String pass, String serverName) {
        if (pass.equals(rmiPass)) {
            SkyServer skyServer = null;
            try {
                skyServer = getSkyBungee().getRemote().getServer(pass, serverName);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (skyServer == null) {
                return false;
            } else {
                getSkyBungee().getProxy().getServers().put(serverName, getSkyBungee().getProxy().constructServerInfo(serverName, InetSocketAddress.createUnresolved("localhost", skyServer.getPort()), serverName, false));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean removeServerFromBungeecord(String pass, String serverName) {
        if (pass.equals(rmiPass)) {
            if (getSkyBungee().getProxy().getServers().containsKey(serverName)) {
                for (ProxiedPlayer proxiedPlayer : getSkyBungee().getProxy().getServerInfo(serverName).getPlayers()) {
                    proxiedPlayer.disconnect(new TextComponent("The server " + serverName + " was removed from the proxy, please rejoin."));
                }
                getSkyBungee().getProxy().getServers().remove(serverName);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public SkyBungee getSkyBungee() {
        return skyBungee;
    }
}
