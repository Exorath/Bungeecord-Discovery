package com.exorath.bungeediscovery;

import redis.clients.jedis.JedisPool;

import java.net.InetSocketAddress;

/**
 * Created by twan1 on 4/20/2016.
 */
public interface DiscoveryAPI {
    JedisPool getRedisPool();
    void register(ServerConfigCreator configCreator);
    void unregister(ServerConfigCreator configCreator);
    void unregister(String ip, int port);
    String getStringFromConfig(String key, String def);
    int getIntFromConfig(String key, int def);
}
