package com.exorath.spigotdiscovery;

import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.JedisPool;

/**
 * Created by twan1 on 4/20/2016.
 */
public class Discovery extends JavaPlugin {
    private static Discovery instance = null;
    private JedisPool jedisPool = new JedisPool("localhost", 6379);

    @Override
    public void onEnable() {
        instance = this;
        ServerPubSub.getOrCreate();
    }

    @Override
    public void onDisable() {

    }

    public static JedisPool getRedisPool(){
        getRedisPool();
    }
}
