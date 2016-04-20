package com.exorath.bungeediscovery;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import org.apache.commons.lang.Validate;

import java.net.InetSocketAddress;
import java.util.HashMap;

/**
 * Created by twan1 on 4/20/2016.
 */
public class ServerRegistry {
    private static ServerRegistry instance = null;

    private HashMap<InetSocketAddress, ServerConfig> configs = new HashMap<>();

    protected void register(ServerConfig server) {
        Validate.notNull("ServerRegistry is unable to register a ServerConfig as it's a null reference.");
        if (!configs.containsKey(server.getAddress()))
            handleNewRegister(server);
        else if (!configs.get(server.getAddress()).equals(server))
            handleUpdatedServer(server);
    }

    private void handleUpdatedServer(ServerConfig newServer) {
        unregister(newServer.getAddress());
        newServer.setId(configs.get(newServer.getAddress()).getId());
        configs.put(newServer.getAddress(), newServer);
        register(newServer);
    }

    private void handleNewRegister(ServerConfig server) {
        server.setId(getId(server));
        configs.put(server.getAddress(), server);

        ServerInfo serverInfo = ProxyServer.getInstance().constructServerInfo(getName(server), server.getAddress(), "Default motd", server.isRestricted());
        ProxyServer.getInstance().getServers().put(getName(server), serverInfo);
    }


    protected void unregister(InetSocketAddress address) {
        if (configs.containsKey(address))
            unregister(configs.get(address));
    }

    protected void unregister(ServerConfig config) {
        configs.remove(config.getAddress());
        unregister(getName(config));
    }

    protected void unregister(String name) {
        ProxyServer.getInstance().getServers().remove(name);
    }

    private String getName(ServerConfig config) {
        return config.getId() == null ? config.getGame() : config.getGame() + "-" + config.getId();
    }

    private int getId(ServerConfig server) {
        Integer lowest = null;
        for (ServerConfig tServer : configs.values())
            if (tServer.getGame().equals(server.getGame()) && (lowest == null || tServer.getId() < lowest))
                if (tServer.getAddress().equals(server.getAddress()))
                    return tServer.getId();
                else
                    lowest = tServer.getId();
        return lowest == null ? 0 : lowest;
    }

    public static ServerRegistry getOrCreate() {
        if (instance == null)
            ServerRegistry.instance = new ServerRegistry();
        return instance;
    }
}
