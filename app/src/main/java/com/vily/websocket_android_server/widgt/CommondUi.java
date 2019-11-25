package com.vily.websocket_android_server.widgt;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.vily.websocket_android_server.R;

/**
 *  * description : 
 *  * Author : Vily
 *  * Date : 2019-11-22
 *  
 **/
public class CommondUi extends LinearLayout {


    private Context mContext;

    private LayoutInflater mLayoutInflater;
    private EditText mEt_fromid;
    private EditText mEt_toid;
    private Button mBtn_send;


    public CommondUi(Context context) {
        this(context, null);
    }

    public CommondUi(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommondUi(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
        initView();
    }

    private void initView() {

        mLayoutInflater = LayoutInflater.from(mContext);
        View view = mLayoutInflater.inflate(R.layout.widgt_commond, this, true);

        mEt_fromid = view.findViewById(R.id.et_fromid);
        mEt_toid = view.findViewById(R.id.et_toid);
        mBtn_send = view.findViewById(R.id.btn_send);

        mBtn_send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mOnCommondSendListener!=null){
                    mOnCommondSendListener.onCommond();
                }
            }
        });

    }

    public int getFromId() {

        if (mEt_fromid == null) {
            return -1;
        }

        String fromId = mEt_fromid.getText().toString().trim();

        return toInt(fromId);

    }

    public int getToId(){

        if(mEt_toid==null){

            return -1;
        }

        String toId = mEt_toid.getText().toString().trim();

        return toInt(toId);
    }





    private int toInt(String content) {

        int i = -1;
        if (TextUtils.isEmpty(content)) {
            return i;
        }

        try {
            i = Integer.parseInt(content);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return i;
    }


    private OnCommondSendListener mOnCommondSendListener;
    public interface OnCommondSendListener{
        void onCommond();
    }

    public void setOnCommondSendListener(OnCommondSendListener onCommondSendListener) {
        mOnCommondSendListener = onCommondSendListener;
    }
}
