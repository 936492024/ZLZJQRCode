package com.example.asus.zlzjqrcode.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.example.asus.zlzjqrcode.activity.Main_Activity;
import com.example.asus.zlzjqrcode.base.BPApplication;
import com.example.asus.zlzjqrcode.base.SystemConstant;
import com.example.asus.zlzjqrcode.eventbus.MainSendEvent;
import com.example.asus.zlzjqrcode.network.MainPresenter;
import com.example.asus.zlzjqrcode.network.MainView;
import com.example.asus.zlzjqrcode.utils.Connect_Check;
import com.example.asus.zlzjqrcode.utils.CreateMD5;

import net.sf.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by asus on 2017/11/1.
 */

public class ServiceLogin extends Service implements MainView {
    private static final String TAG = "ServiceLogin" ;
    public static final String ACTION = ".ServiceLogin";
    private MainPresenter mainPresenter;
    private Intent intent ;
    private boolean flag=true;

    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG, "ServiceLogin onBind");
        return null;
    }

    @Override
    public void onCreate() {
        mainPresenter=new MainPresenter(this,this);
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.v(TAG, "ServiceLogin onStart");
        super.onStart(intent, startId);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "ServiceLogin onStartCommand");
        autologin();
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        Log.v(TAG, "ServiceLogin onStop");
        super.onDestroy();
    }

    public void autologin(){
        String token = BPApplication.getInstance().getToken();
        String member_id=BPApplication.getInstance().getMember_Id();
        if(Connect_Check.getCurrentNetType(ServiceLogin.this)==1||Connect_Check.getCurrentNetType(ServiceLogin.this)==2){
                if(!TextUtils.isEmpty(token)&&!TextUtils.isEmpty(member_id)){
                        Map<String ,String> map = new HashMap<>();
                        map.put("member_id",member_id);
                        map.put("token",token);
                        map.put("secret", CreateMD5.getMd5(member_id+token+SystemConstant.PublicConstant.Public_SECRET));
                        Log.e("tokensss",map.toString());
                        mainPresenter.postMap(SystemConstant.PublicConstant.API_AUTO_LOGIN,map);
                }else {
                    BPApplication.getInstance().setLogined(false);
                }
        }else {
            BPApplication.getInstance().setLogined(false);
        }
    }

    @Override
    public void getView(String s) {

    }

    @Override
    public void postView(final String s) {
        Log.e("自动登录",s);
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObjects = JSONObject.fromObject(s);
                if(jsonObjects.getString("success").equals("true")){
                    JSONObject jsonObject = JSONObject.fromObject(jsonObjects.getString("data"));
                    BPApplication.getInstance().init(jsonObject.getString("member_id"),jsonObject.getString("name"),jsonObject.getString("card_id"),
                            jsonObject.getString("mobile"),jsonObject.getString("paren_id"),jsonObject.getString("is_show"),
                            jsonObject.getString("password"),jsonObject.getString("join_time"),jsonObject.getString("token"),jsonObject.getString("lv_name"),
                            jsonObject.getString("lv_id"),true,jsonObject.getString("invite"),jsonObject.getString("isadd"),jsonObject.getString("quyu"),
                            jsonObject.getString("number"),jsonObject.getString("url"));
                    BPApplication.getInstance().setSubscribe_url(jsonObject.getString("subscribe_url"));
                    EventBus.getDefault().post(new MainSendEvent("自动登录成功"));
                    Intent intent = new Intent(ServiceLogin.this,ServiceLogin.class);
                    stopService(intent);
                }else {
                    BPApplication.getInstance().setMember_Id("");
                    BPApplication.getInstance().setLogined(false);
                }
            }
        }).start();
    }

    @Override
    public void postViews(String s) {

    }

    @Override
    public void postViewss(String s) {

    }

    @Override
    public void postViewsss(String s) {

    }

    @Override
    public void postViewsss_1(String s) {

    }

    @Override
    public void postViewsss_2(String s) {

    }

    @Override
    public void fail(String s) {
          Log.e("自动登录","错误");
    }

    @Override
    public void imgView(Bitmap bitmap) {

    }


}
