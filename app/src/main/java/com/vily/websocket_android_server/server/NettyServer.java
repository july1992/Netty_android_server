package com.vily.websocket_android_server.server;

import android.util.Log;

import com.vily.websocket_android_server.bean.PaketData;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 *  * description : 
 *  * Author : Vily
 *  * Date : 2019-11-21
 *  
 **/
public class NettyServer {

    private static final String TAG = "NettyServer";


    private static NettyServer mInstance;

    private ServerHandler mServerHandler;

    public static NettyServer getInstance(){

        if(mInstance == null){
            mInstance = new NettyServer();
        }

        return mInstance;
    }

    public void start(){
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(4);

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // websocket 基于http协议，所以要有http编解码器
                            pipeline.addLast(new HttpServerCodec());
                            // 对写大数据流的支持
                            pipeline.addLast(new ChunkedWriteHandler());
                            // 对httpMessage进行聚合，聚合成FullHttpRequest或FullHttpResponse
                            // 几乎在netty中的编程，都会使用到此hanler
                            pipeline.addLast(new HttpObjectAggregator(1024*64));

                            // ====================== 以上是用于支持http协议    ======================

                            // ====================== 增加心跳支持 start    ======================
                            // 针对客户端，如果在1分钟时没有向服务端发送读写心跳(ALL)，则主动断开
                            // 如果是读空闲或者写空闲，不处理
                            pipeline.addLast(new IdleStateHandler(0, 0, 120));
                            // 自定义的空闲状态检测
                            pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
                            // 自定义的handler
                            mServerHandler = new ServerHandler();
                            pipeline.addLast(mServerHandler);


                        }
                    });

            Channel serverChannel = bootstrap.bind(8485).sync().channel();

            System.out.println("------Netty Server Start------");
            if(mOnServerOpenListener!=null){
                mOnServerOpenListener.serverOpen(true);
            }

            serverChannel.closeFuture().sync();

        } catch (Exception e){

            Log.i(TAG, "start: -------有错误啊");
            e.printStackTrace();

            if(mOnServerOpenListener!=null){
                mOnServerOpenListener.serverOpen(false);
            }
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }



    private OnServerOpenListener mOnServerOpenListener;



    public void sendPacket(PaketData paketData) {


        if(mServerHandler!=null){

        }
    }

    public interface OnServerOpenListener{

        void serverOpen(boolean isOpen);
    }

    public void setOnServerOpenListener(OnServerOpenListener onServerOpenListener) {
        mOnServerOpenListener = onServerOpenListener;
    }
}
