package me.bsky.skycore.types.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IBungeeManager extends Remote {

    boolean testConnection(String pass) throws RemoteException;

    boolean addServerToBungeecord(String pass, String serverName) throws RemoteException;
    boolean removeServerFromBungeecord(String pass, String serverName) throws RemoteException;
}
