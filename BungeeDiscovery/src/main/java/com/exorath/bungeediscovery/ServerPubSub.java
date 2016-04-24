package com.exorath.bungeediscovery;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import net.md_5.bungee.api.ProxyServer;
import org.apache.commons.lang.Validate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;


/**
 * Created by Toon Sevrin on 4/20/2016.
 */
public class ServerPubSub extends JedisPubSub {
    public static final String SERVER_PING_CHANNEL = "discovery_ping";
    public static final String SERVER_REGISTRATION_CHANNEL = "discovery_servers";
    private static ServerPubSub instance = null;

    public ServerPubSub(){
        registerPinger();
    }

    private void registerPinger(){
        //Register scheduler to post pings every "interval" (Defined in config)
        ProxyServer.getInstance().getScheduler().schedule(Discovery.getInstance(), () -> {
                    Jedis resource = Discovery.getAPI().getRedisPool().getResource();
                    try{
                        JsonObject ping = new JsonObject();
                        ping.add("bungee", new JsonPrimitive(InetAddress.getLocalHost().toString()));
                        resource.publish(SERVER_PING_CHANNEL, ping.toString());
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    resource.close();
                }, 1, Discovery.getAPI().getIntFromConfig("interval",1),
                TimeUnit.valueOf(Discovery.getAPI().getStringFromConfig("timeunit", "SECONDS")));
    }
    @Override
    public void onMessage(String channel, String msg) {
        if (channel.equals(SERVER_REGISTRATION_CHANNEL))
            handleMessage((JsonObject) new JsonParser().parse(msg));

    }

    private void handleMessage(JsonObject msg) {
        Validate.isTrue(msg.has("type"));
        switch (msg.get("type").getAsString()) {
            case "register":
                handleServerRegistration(msg);
                break;
            case "unregister":
                handleServerUnregistration(msg);
                break;
        }
    }

    private void handleServerRegistration(JsonObject msg) {
        Validate.isTrue(msg.has("ip"), "Bungee Discovery received server info without ip parameter");
        Validate.isTrue(msg.has("port"), "Bungee Discovery received server info without port parameter");
        Validate.isTrue(msg.has("game"), "Bungee Discovery received server info without game parameter");
        Discovery.getAPI().register(getConfigCreator(msg));
    }

    private void handleServerUnregistration(JsonObject msg) {
        Validate.isTrue(msg.has("ip"), "Bungee Discovery received server info without ip parameter");
        Validate.isTrue(msg.has("port"), "Bungee Discovery received server info without port parameter");
        Discovery.getAPI().unregister(msg.get("ip").getAsString(), msg.get("port").getAsInt());
    }

    private ServerConfigCreator getConfigCreator(JsonObject msg) {
        ServerConfigCreator configCreator = ServerConfigCreator.getCreator()
                .setGame(msg.get("game").getAsString())
                .setIp(msg.get("ip").getAsString())
                .setPort(msg.get("port").getAsInt());
        if (msg.has("restricted"))
            configCreator.setRestricted(msg.get("restricted").getAsBoolean());
        if (msg.has("generateid"))
            configCreator.setGenerateIdInName(msg.get("generateid").getAsBoolean());
        return configCreator;
    }

    /**
     * Returns the singleton of this class
     *
     * @return Singleton of this class
     */
    public static ServerPubSub getOrCreate() {
        if (instance == null) {
            instance = new ServerPubSub();
            Jedis resource = Discovery.getAPI().getRedisPool().getResource();
            resource.subscribe(instance, SERVER_REGISTRATION_CHANNEL);
            resource.close();
        }
        return instance;
    }
}
