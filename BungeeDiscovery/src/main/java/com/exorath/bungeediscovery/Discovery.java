package com.exorath.bungeediscovery;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;


/**
 * Created by twan1 on 4/20/2016.
 */
public class Discovery extends Plugin implements DiscoveryAPI {
    private static Discovery instance = null;
    private Configuration configuration;
    private JedisPool jedisPool = new JedisPool("localhost", 6379);

    @Override
    public void onEnable() {
        instance = this;
        loadConfig();
        ServerRegistry.getOrCreate();
        ServerPubSub.getOrCreate();
    }

    private void loadConfig(){
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(getDataFolder());
            if(configuration.getKeys().size() == 0){
                configuration.set("interval", "1");
                configuration.set("timeunit", "SECONDS");
            }
        } catch (IOException e) {e.printStackTrace();}
    }

    @Override
    public void register(ServerConfigCreator configCreator){
        try {
            ServerRegistry.getOrCreate().register(new ServerConfig(configCreator));
        } catch (UnknownHostException e) {e.printStackTrace();}
    }

    @Override
    public void unregister(ServerConfigCreator configCreator) {
        try {
            ServerRegistry.getOrCreate().unregister(new ServerConfig(configCreator));
        } catch (UnknownHostException e) {e.printStackTrace();}
    }

    @Override
    public void unregister(String ip, int port) {
        try {
            ServerRegistry.getOrCreate().unregister(new InetSocketAddress(InetAddress.getByName(ip), port));
        } catch (UnknownHostException e) {e.printStackTrace();}
    }

    @Override
    public String getStringFromConfig(String key, String def) {
        return configuration.getString(key, def);
    }

    @Override
    public int getIntFromConfig(String key, int def) {
        return configuration.getInt(key, def);
    }

    @Override
    public JedisPool getRedisPool() {
        return jedisPool;
    }

    protected static Discovery getInstance() {
        return instance;
    }
    public static DiscoveryAPI getAPI() {
        return instance;
    }
}
