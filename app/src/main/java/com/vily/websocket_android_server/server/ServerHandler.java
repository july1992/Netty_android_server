package com.vily.websocket_android_server.server;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.vily.websocket_android_server.bean.PaketData;
import com.vily.websocket_android_server.bean.Type;
import com.vily.websocket_android_server.utils.ChannelMapUtils;


import java.util.logging.LogRecord;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 *  * description : 
 *  * Author : Vily
 *  * Date : 2019-11-21
 *  
 **/
public class ServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final String TAG = "ServerHandler";
    private Handler mHandler=new Handler() ;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg){
        // 获取客户端传输过来的消息
        String content = msg.text();

        if(TextUtils.isEmpty(content)){
            return;
        }

        Log.i(TAG, "channelRead0: ------收到客户端发来的消息:"+content);
        final PaketData paketData = JSON.parseObject(content, PaketData.class);
        if(paketData==null){
            return;
        }
        final Channel channel = ctx.channel();
        Byte type = paketData.getType();

        switch (type){
            case Type.BIND_SERVER :

                Log.i(TAG, "channelRead0: -------这里："+paketData.getFromId());
                ChannelMapUtils.put(paketData.getFromId(),channel);

                ctx.writeAndFlush(
                        new TextWebSocketFrame("leapId 和 channel 已经绑定"));

                break;

            case Type.MESSAGE :


                break;

        }



    }



    /**
     * 连接顺序 1
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        System.out.println("ServerHandler---handlerAdded--- ctxId = "+ctx.channel().id());

    }

    /**
     * 连接顺序 2
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        System.out.println("ServerHandler---channelRegistered--- ctxId = "+ctx.channel().id());
    }

    /**
     * 连接顺序 3
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("ServerHandler---channelActive--- ctxId = "+ctx.channel().id());
        String channelId = ctx.channel().id().asShortText();
        System.out.println("ServerHandler 客户端添加，channelId为：" + channelId);

    }


    /**
     * 关闭顺序-1
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("ServerHandler---channelInactive--- ctxId = "+ctx.channel().id());

    }


    /**
     * 关闭顺序-2
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        System.out.println("ServerHandler---channelUnregistered--- ctxId = "+ctx.channel().id());

    }


    /**
     * 关闭顺序-3
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx){

        String channelId = ctx.channel().id().asShortText();
        System.out.println("ServerHandler  客户端被移除，channelId为：" + channelId);

        ChannelMapUtils.remove(ctx.channel());
        ctx.channel().close();


    }




    /**
     * 异常捕获
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        String channelId = ctx.channel().id().asShortText();
        System.out.println("客户端发送异常 channelId = "+channelId);
        cause.printStackTrace();
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {

        // 判断evt是否是IdleStateEvent（用于触发用户事件，包含 读空闲/写空闲/读写空闲 ）
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent)evt;

            if (event.state() == IdleState.ALL_IDLE) {
                System.out.println("进入读写空闲...");

                ctx.writeAndFlush(
                        new TextWebSocketFrame(JSON.toJSONString("server 读写空闲...")));
            }
        }

    }

}
