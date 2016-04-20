package com.exorath.bungeediscovery;

/**
 * Created by twan1 on 4/20/2016.
 */
public class ServerConfigCreator {
    private String game;
    private String ip;
    private Integer port;
    private Boolean generateIdInName;
    private Boolean restricted;

    private ServerConfigCreator(){}

    public static ServerConfigCreator getCreator(){
        return new ServerConfigCreator();
    }

    public String getGame() {
        return game;
    }

    public ServerConfigCreator setGame(String game) {
        this.game = game;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public ServerConfigCreator setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public ServerConfigCreator setPort(Integer port) {
        this.port = port;
        return this;
    }

    public Boolean isGenerateIdInName() {
        return generateIdInName;
    }

    public ServerConfigCreator setGenerateIdInName(Boolean generateIdInName) {
        this.generateIdInName = generateIdInName;
        return this;
    }

    public Boolean isRestricted() {
        return restricted;
    }

    public ServerConfigCreator setRestricted(Boolean restricted) {
        this.restricted = restricted;
        return this;
    }
}
