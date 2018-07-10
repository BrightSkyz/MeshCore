package me.bsky.skycore.types.interfaces;

import me.bsky.skycore.types.SkyServer;
import me.bsky.skycore.types.enums.LogLevel;
import me.bsky.skycore.types.enums.ServerType;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServerManager extends Remote {

    boolean testConnection(String pass, String serverName) throws RemoteException;

    boolean bungeecordReady(String pass, Integer port) throws RemoteException;

    boolean createServer(String pass, String serverName, ServerType serverType, Boolean deleteOnStop, Integer port, Integer maxRam) throws RemoteException;
    boolean removeServer(String pass, String serverName) throws RemoteException;

    SkyServer getServer(String pass, String serverName) throws RemoteException;

    boolean sendCommand(String pass, String serverName, String command) throws RemoteException;

    boolean log(LogLevel logLevel, String message) throws RemoteException;
}
