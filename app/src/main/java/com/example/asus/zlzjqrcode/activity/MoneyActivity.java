package com.example.asus.zlzjqrcode.activity;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
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

import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by asus on 2018/1/13.
 */

public class MoneyActivity extends BaseActivity implements MainView,View.OnClickListener {

    private MainPresenter mainPresenter;
    private RelativeLayout user_money_back;
    private Dialog progressDialog;
    private TextView user_num,user_load,user_interest;
    @Override
    public void addLayout() {
        setContentView(R.layout.money_activity);
    }

    @Override
    public void initView() {
        mainPresenter=new MainPresenter(this,this);
        user_load= (TextView) findViewById(R.id.user_load);
        user_num= (TextView) findViewById(R.id.user_num);
        user_interest= (TextView) findViewById(R.id.user_interest);
        user_money_back= (RelativeLayout) findViewById(R.id.user_money_back);
        user_money_back.setOnClickListener(this);
        progressDialog = new Dialog(MoneyActivity.this,R.style.progress_dialog);
        progressDialog.setContentView(R.layout.dialog);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setText("加载中...");
        progressDialog.show();
        if(BPApplication.getInstance().getIsadd().equals("0")){
            getData_2();
        }else {
            getData_1();
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
                Log.e("事业合伙人",s);
                JSONObject jsonObject1 = JSONObject.fromObject(s);
                if(jsonObject1.getString("success").equals("true")){
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    user_num.setText(jsonObject1.getString("yonghu_count")+"人");
                    user_load.setText(jsonObject1.getString("download_count")+"次");
                    user_interest.setText(jsonObject1.getString("subscribe")+"人");
                }else {
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

    @Override
    public void postViews(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("事业合伙人",s);
                JSONObject jsonObject1 = JSONObject.fromObject(s);
                if(jsonObject1.getString("success").equals("true")){
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    user_num.setText(jsonObject1.getString("count")+"人");
                    user_load.setText(jsonObject1.getString("download_count")+"次");
                    user_interest.setText(jsonObject1.getString("subscribe")+"人");
                }else {
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
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
    public void fail(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                ToastUtils.showToast(MoneyActivity.this,"服务器异常，请稍后再试");
            }
        });
    }

    @Override
    public void imgView(Bitmap bitmap) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.user_money_back:
                finish();
                break;
        }
    }

    public void getData_1(){
        Map<String ,String> maps = new HashMap<>();
        maps.put("member_id", BPApplication.getInstance().getMember_Id());
        maps.put("secret", CreateMD5.getMd5(BPApplication.getInstance().getMember_Id()+ SystemConstant.PublicConstant.Public_SECRET));
        mainPresenter.postMap(SystemConstant.PublicConstant.API_DATA,maps);
    }
    public void getData_2(){
        Map<String ,String> maps = new HashMap<>();
        maps.put("member_id", BPApplication.getInstance().getMember_Id());
        maps.put("secret", CreateMD5.getMd5(BPApplication.getInstance().getMember_Id()+ SystemConstant.PublicConstant.Public_SECRET));
        mainPresenter.wodes(SystemConstant.PublicConstant.API_DATA,maps);
    }
}
