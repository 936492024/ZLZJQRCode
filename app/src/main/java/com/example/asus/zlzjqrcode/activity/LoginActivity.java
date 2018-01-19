package com.example.asus.zlzjqrcode.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.asus.zlzjqrcode.R;
import com.example.asus.zlzjqrcode.ToastUtils;
import com.example.asus.zlzjqrcode.base.BPApplication;
import com.example.asus.zlzjqrcode.base.BaseActivity;
import com.example.asus.zlzjqrcode.base.SystemConstant;
import com.example.asus.zlzjqrcode.network.MainPresenter;
import com.example.asus.zlzjqrcode.network.MainView;
import com.example.asus.zlzjqrcode.utils.CreateMD5;
import com.example.asus.zlzjqrcode.utils.MyCountDownTimer;
import com.example.asus.zlzjqrcode.utils.StatusBarUtils;

import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by asus on 2018/1/3.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener,MainView{

    private EditText login_phone,login_password,login_name,login_textmescode;
    private Button login_sure,login_sure_2;
    private String id_regular="^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
    private TextView login_getmescode,password_text;
    private SweetAlertDialog pDialog;
    private MainPresenter mainPresenter;
    private String mes_code;
    private String phone_code;
    private RelativeLayout r1,r2,r3,r4,r5;
    private Intent intent ;
    private Dialog progressDialog;
    private long mExitTime;

    @Override
    public void addLayout() {
        setContentView(R.layout.activity_login);
    }

    @Override
    public void initView() {
        StatusBarUtils.with(this)
                .init();
        mainPresenter=new MainPresenter(this,this);
        login_sure= (Button) findViewById(R.id.login_sure);
        login_sure.setOnClickListener(this);
        login_sure_2=(Button) findViewById(R.id.login_sure_2);
        login_sure_2.setOnClickListener(this);
        login_phone= (EditText) findViewById(R.id.login_phone);
        login_password= (EditText) findViewById(R.id.login_password);
        login_name= (EditText) findViewById(R.id.login_name);
        login_textmescode= (EditText) findViewById(R.id.login_textmescode);

        password_text= (TextView) findViewById(R.id.password_text);
        password_text.setOnClickListener(this);

        login_getmescode= (TextView) findViewById(R.id.login_getmescode);
        login_getmescode.setOnClickListener(this);

        r1=(RelativeLayout)findViewById(R.id.rt_1);
        r2=(RelativeLayout) findViewById(R.id.rt_2);
        r3=(RelativeLayout)findViewById(R.id.rt_3);
        r4=(RelativeLayout)findViewById(R.id.rt_4);
        r5=(RelativeLayout)findViewById(R.id.rt_5);
        edit_onclic();
        edit_onclic_2();
        if(!TextUtils.isEmpty(getIntent().getStringExtra("UserSetActivity"))){
            Log.e("dsadaada",getIntent().getStringExtra("UserSetActivity"));
            BPApplication.getInstance().exits(LoginActivity.this);
            BPApplication.getInstance().setLogined(false);
            BPApplication.getInstance().setMember_Id("");
            BPApplication.getInstance().setMobile("");
            BPApplication.getInstance().setToken("");
        }
        progressDialog = new Dialog(LoginActivity.this,R.style.progress_dialog);
        progressDialog.setContentView(R.layout.dialog);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setText("加载中...");
//        zzzzzz=(TextView)findViewById(R.id.zzzzzz);
//        zzzzzz.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setClass(LoginActivity.this,MainActivity.class);
//                startActivity(intent);
//            }
//        });
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_sure://首次登录，完善信息
                String phone=login_phone.getText().toString();
                String password_1=login_password.getText().toString();
                String name = login_name.getText().toString();
                String mescode = login_textmescode.getText().toString();
                if(TextUtils.isEmpty(phone)){
                    ToastUtils.showToast(LoginActivity.this,"请输入手机号码");
                    return;
                }
                if(!isNumeric(phone)){
                    ToastUtils.showToast(LoginActivity.this,"请输入正确的手机号码");
                    return;
                }
                if(TextUtils.isEmpty(mescode)){
                    ToastUtils.showToast(LoginActivity.this,"请输入验证码");
                    return;
                }
                if(!mes_code.equals(mescode)){
                    ToastUtils.showToast(LoginActivity.this,"验证码不匹配");
                    return;
                }
                //TODO
                //验证码的正则
                if(!phone.equals(phone_code)){
                    ToastUtils.showToast(LoginActivity.this,"信息有误，请检查手机号码");
                    return;
                }
                if(TextUtils.isEmpty(name)){
                    ToastUtils.showToast(LoginActivity.this,"请输入姓名");
                    return;
                }
                if(!isNameric(name)){
                    ToastUtils.showToast(LoginActivity.this,"姓名必须为简体中文");
                    return;
                }
                if(TextUtils.isEmpty(password_1)){
                    ToastUtils.showToast(LoginActivity.this,"请输入密码");
                    return;
                }
                if(password_1.length()!=6){
                    ToastUtils.showToast(LoginActivity.this,"密码必须是6位");
                    return;
                }
                progressDialog.show();
                Map<String,String> map = new HashMap();
                map.put("mobile",phone);
                map.put("password",password_1);
                map.put("name",name);
                map.put("secret",CreateMD5.getMd5(phone+name+password_1+SystemConstant.PublicConstant.Public_SECRET));
                mainPresenter.wodesss(SystemConstant.PublicConstant.API_ONE_LOGIN,map);
                break;
            case R.id.login_sure_2://老用户登录
                String phone_2=login_phone.getText().toString();
                String password_2=login_password.getText().toString();
                if(TextUtils.isEmpty(phone_2)){
                    ToastUtils.showToast(LoginActivity.this,"请输入手机号码");
                    return;
                }
                if(!isNumeric(phone_2)){
                    ToastUtils.showToast(LoginActivity.this,"请输入正确的手机号码");
                    return;
                }
                if(TextUtils.isEmpty(password_2)){
                    ToastUtils.showToast(LoginActivity.this,"请输入密码");
                    return;
                }
                if(password_2.length()!=6){
                    ToastUtils.showToast(LoginActivity.this,"密码必须是六位");
                    return;
                }
                progressDialog.show();
                Map<String,String> maps = new HashMap<>();
                maps.put("mobile",phone_2);
                maps.put("password",password_2);
                maps.put("secret",CreateMD5.getMd5(phone_2+password_2+SystemConstant.PublicConstant.Public_SECRET));
                mainPresenter.wodess(SystemConstant.PublicConstant.API_LOGIN,maps);
                break;
            case R.id.login_getmescode://获取验证码
                String phones=login_phone.getText().toString();
                if(TextUtils.isEmpty(phones)){
                    ToastUtils.showToast(LoginActivity.this,"请输入电话号码");
                    return;
                }
                if(!isNumeric(phones)){
                    ToastUtils.showToast(LoginActivity.this,"请输入正确的电话号码");
                    return;
                }
                progressDialog.show();
                Map<String,String> mapss= new HashMap<>();
                mapss.put("mobile",phones);
                mapss.put("secret", CreateMD5.getMd5(phones+SystemConstant.PublicConstant.Public_SECRET));
                mainPresenter.postMap(SystemConstant.PublicConstant.API_SMS,mapss);
                break;
            case R.id.password_text:
                intent = new Intent(LoginActivity.this,Forget_PassWordActivity.class);
                startActivity(intent);
                break;
        }
    }
    public void edit_onclic(){
        login_phone.addTextChangedListener(new TextWatcher() {//电话号码的监听
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length()==11){
                    String getphone=login_phone.getText().toString();
                    if(!isNumeric(getphone)){
                        ToastUtils.showToast(LoginActivity.this,"请输入正确的电话号码");
                        return;
                    }
                    progressDialog.show();
                    Map<String ,String > map = new HashMap<>();
                    map.put("mobile",getphone);
                    map.put("secret",CreateMD5.getMd5(getphone+SystemConstant.PublicConstant.Public_SECRET));
                    mainPresenter.wodes(SystemConstant.PublicConstant.API_IS_ONE,map);
                }
            }
        });
    }
    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("^1[3|4|5|7|8|9][0-9]{9}$");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    public boolean isidcard(String idcard){
        Pattern pattern = Pattern.compile(id_regular);
        Matcher isNum = pattern.matcher(idcard);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    public boolean isNameric(String str){
        Pattern pattern = Pattern.compile("^[\\u4E00-\\u9FA5]+$");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    private void showDialog(String str) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(str);
        builder.setCancelable(false);
        builder.setPositiveButton("确定",null);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.show();
    }

    @Override
    public void getView(String s) {

    }

    @Override
    public void postView(final String s) {//手机验证码
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = JSONObject.fromObject(s);
                Log.e("dsadad",s);
                if(jsonObject.getString("success").equals("true")){
                    progressDialog.dismiss();
                    ToastUtils.showToast(LoginActivity.this,"发送成功");
                    MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60000,1000,login_getmescode);
                    myCountDownTimer.start();
                    mes_code=jsonObject.getString("captcha");//验证码
                    phone_code=jsonObject.getString("mobile");//电话
                }else {
                    progressDialog.dismiss();
                    ToastUtils.showToast(LoginActivity.this,"获取失败");
                }
            }
        });

    }

    @Override
    public void postViews(final String s) {//验证手机号
          runOnUiThread(new Runnable() {
              @Override
              public void run() {
                  Log.e("验证手机号",s);
                  JSONObject jsonObject = JSONObject.fromObject(s);
                  if(jsonObject.getString("success").equals("true")){
                      progressDialog.dismiss();
                      if(jsonObject.getString("status").equals("1")){//首次登录
                          r2.setVisibility(View.VISIBLE);
                          r3.setVisibility(View.VISIBLE);
                          r4.setVisibility(View.VISIBLE);
                          r5.setVisibility(View.VISIBLE);
                          login_sure.setVisibility(View.VISIBLE);
                          login_sure_2.setVisibility(View.GONE);
                          password_text.setVisibility(View.GONE);
                      }else {//已经登录
                          r2.setVisibility(View.GONE);
                          r3.setVisibility(View.GONE);
                          r4.setVisibility(View.VISIBLE);
                          r5.setVisibility(View.VISIBLE);
                          login_sure.setVisibility(View.GONE);
                          login_sure_2.setVisibility(View.VISIBLE);
                          password_text.setVisibility(View.VISIBLE);
                      }
                  }else {//不存在用户
                      progressDialog.dismiss();
                      android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(LoginActivity.this);
                      builder.setTitle("温馨提示");
                      builder.setMessage(jsonObject.getString("msg"));
                      builder.setCancelable(false);
                      builder.setPositiveButton("确定",null);
                      builder.setIcon(R.drawable.xiufanghua);
                      builder.show();
                  }
              }
          });
    }

    @Override
    public void postViewss(final String s) {//老用户登录
          runOnUiThread(new Runnable() {
              @Override
              public void run() {
                  Log.e("老用户登录",s);
                  JSONObject jsonObject_1 =JSONObject.fromObject(s);
                  if(jsonObject_1.getString("success").equals("true")){
                      progressDialog.dismiss();
                      String data=jsonObject_1.getString("data");
                      JSONObject jsonObject = JSONObject.fromObject(data);
                      BPApplication.getInstance().init(jsonObject.getString("member_id"),jsonObject.getString("name"),jsonObject.getString("card_id"),
                              jsonObject.getString("mobile"),jsonObject.getString("paren_id"),jsonObject.getString("is_show"),
                              jsonObject.getString("password"),jsonObject.getString("join_time"),jsonObject.getString("token"),jsonObject.getString("lv_name"),
                              jsonObject.getString("lv_id"),true,jsonObject.getString("invite"),jsonObject.getString("isadd"),jsonObject.getString("quyu"),
                              jsonObject.getString("number"),jsonObject.getString("url"));
                      BPApplication.getInstance().setSubscribe_url(jsonObject.getString("subscribe_url"));
                      intent=new Intent(LoginActivity.this,Main_Activity.class);
                      startActivity(intent);
                      finish();
                  }else {
                      progressDialog.dismiss();
                      ToastUtils.showToast(LoginActivity.this,jsonObject_1.getString("msg"));
                  }
              }
          });
    }

    @Override
    public void postViewsss(final String s) {//新用户完善信息
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("新用户完善信息",s);
                JSONObject jsonObject_1 =JSONObject.fromObject(s);
                if(jsonObject_1.getString("success").equals("true")){
                    progressDialog.dismiss();
                    String data=jsonObject_1.getString("data");
                    JSONObject jsonObject = JSONObject.fromObject(data);
                    BPApplication.getInstance().init(jsonObject.getString("member_id"),jsonObject.getString("name"),jsonObject.getString("card_id"),
                            jsonObject.getString("mobile"),jsonObject.getString("paren_id"),jsonObject.getString("is_show"),
                            jsonObject.getString("password"),jsonObject.getString("join_time"),jsonObject.getString("token"),jsonObject.getString("lv_name"),
                            jsonObject.getString("lv_id"),true,jsonObject.getString("invite"),jsonObject.getString("isadd"),jsonObject.getString("quyu"),
                            jsonObject.getString("number"),jsonObject.getString("url"));
                    BPApplication.getInstance().setSubscribe_url(jsonObject.getString("subscribe_url"));
                    intent=new Intent(LoginActivity.this,Main_Activity.class);
                    startActivity(intent);
                    finish();
                }else {
                    progressDialog.dismiss();
                    ToastUtils.showToast(LoginActivity.this,jsonObject_1.getString("msg"));
                }
            }
        });
    }

    @Override
    public void postViewsss_1(String s) {

    }

    @Override
    public void postViewsss_2(String s) {

    }

    @Override
    public void fail(String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                ToastUtils.showToast(LoginActivity.this,"网络连接错误，请检查网络设置");
            }
        });
    }

    @Override
    public void imgView(Bitmap bitmap) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了“返回键”
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //与上次点击返回键时刻作差
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                //大于2000ms则认为是误操作，使用Toast进行提示
                ToastUtils.showToast(this,"再按一次退出程序");
                //并记录下本次点击“返回键”的时刻，以便下次进行判断
                mExitTime = System.currentTimeMillis();
            } else {
                //小于2000ms则认为是用户确实希望退出程序-调用System.exit()方法进行退出
                BPApplication.getInstance().exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void edit_onclic_2(){
        login_password.addTextChangedListener(new TextWatcher() {//电话号码的监听
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length()==6){
                    String getphone=login_password.getText().toString();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm!=null){
                        boolean isOpen=imm.isActive();
                        if(isOpen){//输入法打开
                            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        }else {//输入法关闭
                            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    }
                    if(getphone.length()!=6){
                        ToastUtils.showToast(LoginActivity.this,"密码必须是6六位");
                        return;
                    }
                }
            }
        });
    }
}
