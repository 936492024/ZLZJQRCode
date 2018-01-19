package com.example.asus.zlzjqrcode.utils;

import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by asus on 2017/9/22.
 */

public class MyCountDownTimer extends CountDownTimer {
     private TextView btn_djs;
    public MyCountDownTimer(long millisInFuture, long countDownInterval, TextView btn_djs) {
        super(millisInFuture, countDownInterval);
        this.btn_djs=btn_djs;
    }

    //计时过程
    @Override
    public void onTick(long l) {
        //防止计时过程中重复点击
        btn_djs.setClickable(false);
        btn_djs.setText(l/1000+"s");

    }

    //计时完毕的方法
    @Override
    public void onFinish() {
        //重新给Button设置文字
        btn_djs.setText("重新获取验证码");
        //设置可点击
        btn_djs.setClickable(true);
    }
}
