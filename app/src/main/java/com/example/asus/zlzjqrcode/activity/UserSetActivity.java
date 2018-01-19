package com.example.asus.zlzjqrcode.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allenliu.versionchecklib.core.AllenChecker;
import com.allenliu.versionchecklib.core.VersionParams;
import com.example.asus.zlzjqrcode.R;
import com.example.asus.zlzjqrcode.ToastUtils;
import com.example.asus.zlzjqrcode.base.BPApplication;
import com.example.asus.zlzjqrcode.base.BaseActivity;
import com.example.asus.zlzjqrcode.base.SystemConstant;
import com.example.asus.zlzjqrcode.network.MainPresenter;
import com.example.asus.zlzjqrcode.network.MainView;
import com.example.asus.zlzjqrcode.utils.CreateMD5;
import com.example.asus.zlzjqrcode.utils.CustomVersionDialogActivity;
import com.example.asus.zlzjqrcode.utils.StatusBarUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by asus on 2018/1/10.
 */

public class UserSetActivity extends BaseActivity implements View.OnClickListener,MainView{

    private RelativeLayout user_set_back,version_upload,make_password_rt;
    private Button login_out;
    private Intent intent;
    private MainPresenter mainPresenter;
    private int versionCode;
    private TextView version_name;


    @Override
    public void addLayout() {
        setContentView(R.layout.userset_activity);
    }

    @Override
    public void initView() {
        StatusBarUtils.with(this)
                .init();
        mainPresenter=new MainPresenter(this,this);
        user_set_back= (RelativeLayout) findViewById(R.id.user_set_back);
        user_set_back.setOnClickListener(this);
        login_out= (Button) findViewById(R.id.login_out);
        login_out.setOnClickListener(this);
        version_upload= (RelativeLayout) findViewById(R.id.version_upload);
        version_upload.setOnClickListener(this);
        make_password_rt= (RelativeLayout) findViewById(R.id.make_password_rt);
        make_password_rt.setOnClickListener(this);
        version_name= (TextView) findViewById(R.id.version_name);
        String versionName = null;
        try {
            versionName = this.getPackageManager().getPackageInfo(this.getPackageName(),0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version_name.setText("V"+versionName);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.user_set_back:
                finish();
                break;
            case R.id.login_out://退出登录的操作
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(UserSetActivity.this);
                builder.setTitle("温馨提示");
                builder.setMessage("您确定退出当前账户？");
                builder.setCancelable(false);
                builder.setNegativeButton("取消",null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        intent=new Intent(UserSetActivity.this,LoginActivity.class);
                        intent.putExtra("UserSetActivity","UserSetActivity");
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setIcon(R.drawable.xiufanghua);
                builder.show();
                break;
            case R.id.version_upload:
                updata();
                break;
            case R.id.make_password_rt:
                intent = new Intent(UserSetActivity.this,Make_PasswordActivity.class);
                startActivity(intent);
                break;

        }
    }

    public void updata(){
        try {
            versionCode = UserSetActivity.this.getPackageManager().getPackageInfo("com.example.asus.zlzjqrcode", 0).versionCode;
            Map<String,String> map = new HashMap<>();
            map.put("version",versionCode+"");
            map.put("name","APP");
            Log.e("版本更新",map.toString());
            map.put("secret", CreateMD5.getMd5("APP"+versionCode+ SystemConstant.PublicConstant.Public_SECRET));
            mainPresenter.postMap(SystemConstant.PublicConstant.API_CHECK,map);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getView(String s) {

    }

    @Override
    public void postView(final String s) {
        Log.e("版本更新",s);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                net.sf.json.JSONObject jsonObject1 = net.sf.json.JSONObject.fromObject(s);
                if(jsonObject1.getString("success").equals("true")){
                    if(jsonObject1.getString("updata").equals("0")){
                        ToastUtils.showToast(UserSetActivity.this,"已是最新版本");
                    }else {
                        VersionParams.Builder builder = new VersionParams.Builder();
                        builder.setOnlyDownload(true)
                                .setDownloadUrl(jsonObject1.getString("web"))
                                .setTitle("版本更新")
                                .setForceRedownload(false)
                                .setCustomDownloadActivityClass(CustomVersionDialogActivity.class)
                                .setUpdateMsg(jsonObject1.getString("intro"));
                        AllenChecker.startVersionCheck(UserSetActivity.this, builder.build());
                    }
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
