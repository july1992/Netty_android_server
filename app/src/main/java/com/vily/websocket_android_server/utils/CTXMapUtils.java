package com.vily.websocket_android_server.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 *  * description : 
 *  * Author : Vily
 *  * Date : 2019-11-22
 *  
 **/
public class CTXMapUtils {

    private static Map<Integer, ChannelHandlerContext> manager = new ConcurrentHashMap<>();

    public static void put(Integer leapId, ChannelHandlerContext channel) {
        manager.put(leapId, channel);
    }

    public static ChannelHandlerContext get(Integer leapId) {
        return manager.get(leapId);
    }

    public static int getLeapId(ChannelHandlerContext channel){

        for (Integer key : manager.keySet()) {
            if(channel.equals(manager.get(key))){
                return key;
            }
        }

        return -1;
    }


    public static void remove(ChannelHandlerContext channel){
        manager.values().remove(channel);
    }
}
