package com.example.asus.zlzjqrcode.eventbus;

/**
 * Created by asus on 2017/10/19.
 */

public class MainSendEvent {
    protected String mstrMsg;


    public MainSendEvent(String msg) {
        mstrMsg = msg;
    }

    public String getStringMsgData(){
        return mstrMsg;
    }


}
