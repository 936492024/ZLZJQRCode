package com.example.asus.zlzjqrcode.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.asus.zlzjqrcode.R;
import com.example.asus.zlzjqrcode.base.BPApplication;
import com.example.asus.zlzjqrcode.base.BaseActivity;
import com.example.asus.zlzjqrcode.service.ServiceLogin;

/**
 * Created by asus on 2018/1/9.
 */

public class Stact_Activity extends BaseActivity {

    private SharedPreferences sharedPreferences;
    private Boolean user_first;
    private Intent intent;

    @Override
    public void addLayout() {
        setContentView(R.layout.stact_activity);
    }

    @Override
    public void initView() {
        sharedPreferences= getSharedPreferences("IsFrist", 0);
        user_first = sharedPreferences.getBoolean("FIRST", true);
//        BPApplication.getInstance().setLogined(false);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                isfirst();
            }
        }, 2000);
    }

    public void isfirst() {
        if (user_first) {// 第一次进入程序
            sharedPreferences.edit().putBoolean("FIRST", false).commit();
                intent=new Intent(Stact_Activity.this,LoginActivity.class);
                startActivity(intent);
                finish();

        } else {
            if( BPApplication.getInstance().isLogined()==false){
                intent=new Intent(Stact_Activity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }else {
                intent=new Intent(Stact_Activity.this, ServiceLogin.class);
                startService(intent);
                intents();
            }
        }
    }

    public void intents(){
        intent=new Intent(Stact_Activity.this,Main_Activity.class);
        startActivity(intent);
        finish();
    }
}
