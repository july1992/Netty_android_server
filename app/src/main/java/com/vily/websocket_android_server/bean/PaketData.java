package com.vily.websocket_android_server.bean;

import java.io.Serializable;

/**
 *  * description : 
 *  * Author : Vily
 *  * Date : 2019-11-22
 *  
 **/
public class PaketData implements Serializable {

    private int fromId;

    private int toId;

    private byte type;

    public PaketData() {
    }

    public PaketData(int fromId, int toId, byte type) {
        this.fromId = fromId;
        this.toId = toId;
        this.type = type;
    }

    public PaketData(int fromId, int toId) {
        this.fromId = fromId;
        this.toId = toId;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PaketData{" +
                "fromId=" + fromId +
                ", toId=" + toId +
                ", type=" + type +
                '}';
    }
}
