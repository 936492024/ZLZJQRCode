package com.example.asus.zlzjqrcode.uset_list;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.asus.zlzjqrcode.R;
import com.example.asus.zlzjqrcode.base.BPApplication;
import com.example.asus.zlzjqrcode.base.BaseActivity;
import com.example.asus.zlzjqrcode.base.SystemConstant;
import com.example.asus.zlzjqrcode.network.MainPresenter;
import com.example.asus.zlzjqrcode.network.MainView;
import com.example.asus.zlzjqrcode.utils.CreateMD5;
import com.example.asus.zlzjqrcode.utils.StatusBarUtils;

import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by asus on 2018/1/24.
 */

public class T_List_Activity extends BaseActivity implements View.OnClickListener ,MainView{
    private RelativeLayout t_list_back;
    private TextView text_1,text_2,text_3,user_interest,user_load,user_num;
    private MainPresenter mainPresenter;
    private Dialog progressDialog;
    private Intent intent;
    @Override
    public void addLayout() {
        setContentView(R.layout.t_list_activity);
    }

    @Override
    public void initView() {
        mainPresenter=new MainPresenter(this,this);
        StatusBarUtils.with(this)
                .init();
        progressDialog = new Dialog(T_List_Activity.this,R.style.progress_dialog);
        progressDialog.setContentView(R.layout.dialog);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setText("加载中...");
        progressDialog.show();
        t_list_back= (RelativeLayout) findViewById(R.id.t_list_back);
        t_list_back.setOnClickListener(this);
        text_1= (TextView) findViewById(R.id.text_1);
        text_2= (TextView) findViewById(R.id.text_2);
        text_3= (TextView) findViewById(R.id.text_3);
        user_interest= (TextView) findViewById(R.id.user_interest);
        user_load= (TextView) findViewById(R.id.user_load);
        user_num= (TextView) findViewById(R.id.user_num);
        text_1.setText(BPApplication.getInstance().getLv_name()+"("+BPApplication.getInstance().getQuyu()+")");
        if(Integer.valueOf(BPApplication.getInstance().getCard_Id().substring(16, 17))%2==0) {
            text_2.setText(BPApplication.getInstance().getName()+"女士");
        }else{
            text_2.setText(BPApplication.getInstance().getName()+"先生");
        }
        text_3.setText(BPApplication.getInstance().getInvite());
        getData();
    }

    @Override
    public void onClick(View view) {
         switch (view.getId()){
             case R.id.t_list_back:
                 finish();
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
                Log.e("事业合伙人",s);
                JSONObject jsonObject1 = JSONObject.fromObject(s);
                if(jsonObject1.getString("success").equals("true")){
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    user_num.setText(jsonObject1.getString("user_count")+"人");
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


    public void getData(){
        Map<String ,String> maps = new HashMap<>();
        maps.put("member_id",BPApplication.getInstance().getMember_Id());
        maps.put("secret", CreateMD5.getMd5(BPApplication.getInstance().getMember_Id()+ SystemConstant.PublicConstant.Public_SECRET));
        mainPresenter.postMap(SystemConstant.PublicConstant.API_DATA,maps);
    }
}
