package com.example.asus.zlzjqrcode.activity;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.example.asus.zlzjqrcode.R;
import com.example.asus.zlzjqrcode.ToastUtils;
import com.example.asus.zlzjqrcode.base.BaseActivity;
import com.example.asus.zlzjqrcode.base.SystemConstant;
import com.example.asus.zlzjqrcode.network.MainPresenter;
import com.example.asus.zlzjqrcode.network.MainView;
import com.example.asus.zlzjqrcode.utils.CreateMD5;
import com.example.asus.zlzjqrcode.utils.MyCountDownTimer;

import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by asus on 2017/9/21.
 */

public class Forget_PassWordActivity extends BaseActivity implements View.OnClickListener ,MainView {
    private Button login_btn;
    private MainPresenter presenter;
    private EditText forget_password_import_phone,yanzheng_import_text,login_import_dlpassword,login_import_qrpassword;
    private ToastUtils toastUtil;
    private Map<String, String> map = new HashMap<>();
    private String captcha;
    private TextView forgetpassword_getmescode;
    private Dialog progressDialog;

    @Override
    public void addLayout() {
        setContentView(R.layout.forget_password_activity);
    }

    @Override
    public void initView() {
        presenter = new MainPresenter(Forget_PassWordActivity.this, this);
        forgetpassword_getmescode=(TextView)findViewById(R.id.forgetpassword_getmescode);
        forgetpassword_getmescode.setOnClickListener(this);
        forget_password_import_phone= (EditText) findViewById(R.id.forget_password_import_phone);
        yanzheng_import_text= (EditText) findViewById(R.id.yanzheng_import_text);
        login_btn= (Button) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);
        login_import_dlpassword= (EditText) findViewById(R.id.login_import_dlpassword);
        login_import_qrpassword= (EditText) findViewById(R.id.login_import_qrpassword);
        progressDialog = new Dialog(Forget_PassWordActivity.this,R.style.progress_dialog);
        progressDialog.setContentView(R.layout.dialog);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setText("加载中...");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.forgetpassword_getmescode:
                String phone=forget_password_import_phone.getText().toString();
                if(TextUtils.isEmpty(phone)){
                    toastUtil.showToast(Forget_PassWordActivity.this,"手机号码不能为空");
                    return;
                }
                if(!isNumeric(phone)){
                    toastUtil.showToast(Forget_PassWordActivity.this,"请输入正确的手机号码");
                    return;
                }
                    progressDialog.show();
                    map.put("mobile", phone);
                    map.put("secret", CreateMD5.getMd5(phone+SystemConstant.PublicConstant.Public_SECRET));
                    presenter.postMap(SystemConstant.PublicConstant.API_SMS,map);
                break;
            case R.id.login_btn:
                String phones=forget_password_import_phone.getText().toString();
                if(TextUtils.isEmpty(phones)){
                    toastUtil.showToast(Forget_PassWordActivity.this,"手机号码不能为空");
                    return;
                }
                if(!isNumeric(phones)){
                    toastUtil.showToast(Forget_PassWordActivity.this,"请输入正确的手机号码");
                    return;
                }
                if(TextUtils.isEmpty(yanzheng_import_text.getText().toString())){
                    toastUtil.showToast(Forget_PassWordActivity.this,"验证码不能为空");
                    return;
                }
                if(!yanzheng_import_text.getText().toString().equals(captcha)){
                    toastUtil.showToast(Forget_PassWordActivity.this,"验证码有误");
                    return;
                }
                if(TextUtils.isEmpty(login_import_dlpassword.getText().toString())){
                    toastUtil.showToast(Forget_PassWordActivity.this,"重新设置的密码不能为空");
                    return;
                }
                if(TextUtils.isEmpty(login_import_qrpassword.getText().toString())){
                    toastUtil.showToast(Forget_PassWordActivity.this,"确认的密码不能为空");
                    return;
                }
                if(!login_import_dlpassword.getText().toString().equals(login_import_qrpassword.getText().toString())){
                    toastUtil.showToast(Forget_PassWordActivity.this,"两次输入的密码不一致");
                    return;
                }
                progressDialog.show();
                Map<String ,String > map = new HashMap<>();
                map.put("mobile",forget_password_import_phone.getText().toString());
                map.put("password",login_import_dlpassword.getText().toString());
                map.put("secret",CreateMD5.getMd5(forget_password_import_phone.getText().toString()+login_import_dlpassword.getText().toString()+SystemConstant.PublicConstant.Public_SECRET));
                presenter.wodes(SystemConstant.PublicConstant.API_FOR_GET_PASSWORD,map);
                break;
        }
    }

    @Override
    public void getView(final String s) {

    }

    @Override
    public void postView(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject json = JSONObject.fromObject(s);
                if(json.getString("success").equals("true")){
                    toastUtil.showToast(Forget_PassWordActivity.this,"发送成功");
                    MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60000,1000,forgetpassword_getmescode);
                    myCountDownTimer.start();
                    captcha=json.getString("captcha");
                    progressDialog.dismiss();
                }else {
                    progressDialog.dismiss();
                    toastUtil.showToast(Forget_PassWordActivity.this,json.getString("msg"));
                }
            }
        });

    }

    @Override
    public void postViews(final String s) {
        Log.e("忘记密码",s);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = JSONObject.fromObject(s);
                if(jsonObject.getString("success").equals("true")){
                    progressDialog.dismiss();
                    toastUtil.showToast(Forget_PassWordActivity.this,"重置密码成功");
                    finish();
                }else {
                    progressDialog.dismiss();
                    toastUtil.showToast(Forget_PassWordActivity.this,"网络连接有误，请检查网络设置");
                }
            }
        });
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
         runOnUiThread(new Runnable() {
             @Override
             public void run() {
                 progressDialog.dismiss();
                 toastUtil.showToast(Forget_PassWordActivity.this,"网络连接有误，请检查网络设置");
             }
         });
    }

    @Override
    public void imgView(Bitmap bitmap) {

    }



    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("^1[3|4|5|7|8][0-9]{9}$");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
}
