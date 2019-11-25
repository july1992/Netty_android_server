package com.vily.websocket_android_server.bean;

/**
 *  * description : 
 *  * Author : Vily
 *  * Date : 2019-11-22
 *  
 **/
public interface Type {


    byte BIND_SERVER = 100;   // 将leapId 和 channel 绑定

    byte MESSAGE = 101;    // 其他消息心跳等

}
