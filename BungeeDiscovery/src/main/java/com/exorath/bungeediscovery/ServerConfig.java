package com.exorath.bungeediscovery;

import org.apache.commons.lang.Validate;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * Created by twan1 on 4/20/2016.
 */
public class ServerConfig {
    private String game;
    private InetSocketAddress address;
    private Integer id;
    private boolean generateId = true;
    private boolean restricted = false;

    protected ServerConfig(ServerConfigCreator creator) throws UnknownHostException {
        Validate.notNull(creator.getIp(), "Bungee Discovery tried to register a server but did not receive an ip parameter.");
        Validate.notNull(creator.getPort(), "Bungee Discovery tried to register a server but did not receive a port parameter.");

        setAddress(new InetSocketAddress(InetAddress.getByName(creator.getIp()), creator.getPort()));
        setGame(creator.getGame());
        setGenerateId(creator.isGenerateIdInName());
        setRestricted(creator.isRestricted());
    }

    private void setGame(String game) {
        if (game != null)
            this.game = game;
    }

    private void setAddress(InetSocketAddress address) {
        if (address != null)
            this.address = address;
    }

    private void setRestricted(Boolean restricted) {
        if (restricted != null)
            this.restricted = restricted;
    }

    public void setGenerateId(Boolean generateId) {
        if (generateId != null)
            this.generateId = generateId;
    }

    public String getGame() {
        return game;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public boolean isGenerateId() {
        return generateId;
    }

    public boolean isRestricted() {
        return restricted;
    }

    protected void setId(int id){
        if(generateId) {
            this.id = id;
            generateId = false;
            setGame(getGame() + id);
        }
    }

    public Integer getId() {
        return id;
    }
    //    @Override
//    public boolean equals(Object obj) {
//        if (obj == null || !(obj instanceof ServerConfig))
//            return false;
//        ServerConfig that = (ServerConfig) obj;
//
//        return (this.getName().equals(that.getName())
//                && this.getAddress().equals(that.getAddress())
//                && this.isRestricted() == that.isRestricted());
//    }
//
//    @Override
//    public int hashCode() {
//        return name.hashCode() + address.hashCode() + Boolean.hashCode(restricted) + Boolean.hashCode(restricted);
//    }
}
