package com.exorath.spigotdiscovery;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.sun.java.browser.net.ProxyService;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 * Created by twan1 on 4/21/2016.
 */
public class ServerPubSub extends JedisPubSub {
    public static final String SERVER_PING_CHANNEL = "discovery_ping";
    public static final String SERVER_REGISTRATION_CHANNEL = "discovery_servers";

    private static ServerPubSub instance = null;


    public static ServerPubSub getOrCreate(){
        if (instance == null) {
            instance = new ServerPubSub();
            Jedis resource = Discovery.getRedisPool().getResource();
            try {
                resource.subscribe(instance, SERVER_REGISTRATION_CHANNEL);
            }catch(Exception e){
                e.printStackTrace();
            }
            resource.close();
        }
        return instance;
    }

    @Override
    public void onMessage(String channel, String msg) {
        if(channel.equals(SERVER_PING_CHANNEL))
            publishServerRegistration();
    }

    private void publishServerRegistration(){

    }
    private JsonObject getServerRegistration(){
        JsonObject obj = new JsonObject();
        obj.add("type", new JsonPrimitive("registration"));
        obj.add("ip", );
    }
}
