package com.vily.websocket_android_server.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;

/**
 *  * description :  将 leapId 和 channel 绑定
 *  * Author : Vily
 *  * Date : 2019-11-22
 *  
 **/
public class ChannelMapUtils {

    private static Map<Integer, Channel> manager = new ConcurrentHashMap<>();

    public static void put(Integer leapId, Channel channel) {
        manager.put(leapId, channel);
    }

    public static Channel get(Integer leapId) {
        return manager.get(leapId);
    }

    public static int getSenderNo(Channel channel){

        for (Integer key : manager.keySet()) {
            if(channel.equals(manager.get(key))){
                return key;
            }
        }

        return -1;
    }

    public static void stop(Integer leapId){
        Channel channel = get(leapId);
        channel.close();
    }

    public static void remove(Channel channel){
        manager.values().remove(channel);
    }

}
