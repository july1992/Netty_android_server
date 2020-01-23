package com.vily.websocket_android_server;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.vily.websocket_android_server.bean.PaketData;
import com.vily.websocket_android_server.server.NettyServer;
import com.vily.websocket_android_server.utils.ChannelMapUtils;
import com.vily.websocket_android_server.widgt.CommondUi;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TextView mTv_stater;
    private CommondUi mCmd_ui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();

    }

    private void initView() {
        mTv_stater = findViewById(R.id.tv_stater);
        mCmd_ui = findViewById(R.id.cmd_ui);
    }

    private void initListener() {
        NettyServer.getInstance().setOnServerOpenListener(new NettyServer.OnServerOpenListener() {
            @Override
            public void serverOpen(final boolean isOpen) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTv_stater.setText("服务开启状态："+isOpen);
                    }
                });

            }
        });

        mCmd_ui.setOnCommondSendListener(new CommondUi.OnCommondSendListener() {
            @Override
            public void onCommond() {

                int fromId = mCmd_ui.getFromId();
                int toId = mCmd_ui.getToId();
                PaketData paketData=new PaketData(fromId,toId);

                Log.i(TAG, "onCommond: ----:"+paketData.toString());
                Channel channel = ChannelMapUtils.get(fromId);
                if(channel!=null){
                    Log.i(TAG, "onCommond: ----22:2"+paketData.toString());

                    channel. writeAndFlush(
                            new TextWebSocketFrame(JSON.toJSONString(paketData)));

                }else{
                    Toast.makeText(getApplicationContext(),"leapId 未绑定",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void openserver(View view) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                NettyServer.getInstance().start();
            }
        }).start();

    }
}
