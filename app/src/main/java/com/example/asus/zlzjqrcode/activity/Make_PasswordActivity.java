package com.example.asus.zlzjqrcode.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


import com.example.asus.zlzjqrcode.R;
import com.example.asus.zlzjqrcode.ToastUtils;
import com.example.asus.zlzjqrcode.base.BPApplication;
import com.example.asus.zlzjqrcode.base.BaseActivity;
import com.example.asus.zlzjqrcode.base.SystemConstant;
import com.example.asus.zlzjqrcode.network.MainPresenter;
import com.example.asus.zlzjqrcode.network.MainView;
import com.example.asus.zlzjqrcode.utils.CreateMD5;

import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by asus on 2017/10/5.
 */

public class Make_PasswordActivity extends BaseActivity implements View.OnClickListener,MainView {
    private LinearLayout mine_makepassword_back;
    private EditText yuan_password,xin_password_1,make_password_2;
    private MainPresenter mainPresenter;
    private Button mine_paypassword_1_btn;
    private ToastUtils toastUtil;
    private ProgressBar pay_set_progress_bar;
    @Override
    public void addLayout() {
        setContentView(R.layout.make_password_activity);
    }

    @Override
    public void initView() {
        mainPresenter=new MainPresenter(this,this);
        mine_makepassword_back= (LinearLayout) findViewById(R.id.mine_makepassword_back);
        mine_makepassword_back.setOnClickListener(this);
        yuan_password= (EditText) findViewById(R.id.yuan_password);
        xin_password_1= (EditText) findViewById(R.id.xin_password_1);
        make_password_2= (EditText) findViewById(R.id.make_password_2);
        mine_paypassword_1_btn= (Button) findViewById(R.id.mine_paypassword_1_btn);
        mine_paypassword_1_btn.setOnClickListener(this);
        pay_set_progress_bar= (ProgressBar) findViewById(R.id.pay_set_progress_bar);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mine_makepassword_back:
                finish();
                break;
            case R.id.mine_paypassword_1_btn:
                String yuan = yuan_password.getText().toString();
                String xin_1=xin_password_1.getText().toString();
                String xin_2 = make_password_2.getText().toString();
                if(TextUtils.isEmpty(yuan)){
                    toastUtil.showToast(Make_PasswordActivity.this,"原密码输入不能为空");
                    return;
                }
                if(TextUtils.isEmpty(xin_1)){
                    toastUtil.showToast(Make_PasswordActivity.this,"新密码输入不能为空");
                    return;
                }
                if(xin_1.length()!=6){
                    toastUtil.showToast(Make_PasswordActivity.this,"密码必须是六位");
                    return;
                }
                if(TextUtils.isEmpty(xin_2)){
                    toastUtil.showToast(Make_PasswordActivity.this,"确认密码输入不能为空");
                    return;
                }
                if(xin_2.length()!=6){
                    toastUtil.showToast(Make_PasswordActivity.this,"密码必须是六位");
                    return;
                }
                if(!xin_1.equals(xin_2)){
                    toastUtil.showToast(Make_PasswordActivity.this,"两次输入的密码不一致");
                    return;
                }
                pay_set_progress_bar.setVisibility(View.VISIBLE);
                    Map<String ,String> map = new HashMap<>();
                    map.put("member_id", BPApplication.getInstance().getMember_Id());
                    map.put("password",yuan);
                    map.put("new_password",xin_1);
                    map.put("secret", CreateMD5.getMd5(BPApplication.getInstance().getMember_Id()+xin_1+yuan+SystemConstant.PublicConstant.Public_SECRET));
                    Log.e("加密",map.toString());
                    mainPresenter.postMap(SystemConstant.PublicConstant.API_PASSWORD,map);
                break;
        }
    }

    @Override
    public void getView(String s) {

    }

    @Override
    public void postView(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = JSONObject.fromObject(s);
                Log.e("修改密码",s);
                if(jsonObject.getString("success").equals("true")){
                    pay_set_progress_bar.setVisibility(View.GONE);
                    ToastUtils.showToast(Make_PasswordActivity.this,"修改成功");
                    Intent intent = new Intent(Make_PasswordActivity.this,Main_Activity.class);
                    intent.putExtra("Make_PasswordActivity","Make_PasswordActivity");
                    startActivity(intent);
                    finish();
                }else {
                    pay_set_progress_bar.setVisibility(View.GONE);
                    ToastUtils.showToast(Make_PasswordActivity.this,jsonObject.getString("msg"));
                }
            }
        });
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

    }

    @Override
    public void imgView(Bitmap bitmap) {

    }
}
