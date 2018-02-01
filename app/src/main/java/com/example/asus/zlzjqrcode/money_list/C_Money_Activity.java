package com.example.asus.zlzjqrcode.money_list;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.asus.zlzjqrcode.R;
import com.example.asus.zlzjqrcode.ToastUtils;
import com.example.asus.zlzjqrcode.activity.MoneyActivity;
import com.example.asus.zlzjqrcode.adpter.TableAdapter_2;
import com.example.asus.zlzjqrcode.adpter.TableAdapter_3;
import com.example.asus.zlzjqrcode.adpter.TableAdapters;
import com.example.asus.zlzjqrcode.base.BPApplication;
import com.example.asus.zlzjqrcode.base.BaseActivity;
import com.example.asus.zlzjqrcode.base.SystemConstant;
import com.example.asus.zlzjqrcode.huadong.MyListView;
import com.example.asus.zlzjqrcode.huadong.Product;
import com.example.asus.zlzjqrcode.network.MainPresenter;
import com.example.asus.zlzjqrcode.network.MainView;
import com.example.asus.zlzjqrcode.uset_list.C_List_Activity;
import com.example.asus.zlzjqrcode.utils.CreateMD5;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by asus on 2018/1/24.
 */

public class C_Money_Activity extends BaseActivity implements MainView,View.OnClickListener{
    private RelativeLayout c_money_back;
    private MainPresenter mainPresenter;
    private Dialog progressDialog;
    private TextView user_interest,user_load,user_num,level_count_text,yonghu_count_text_1,yonghu_count_text_2,yonghu_count_text_3;
    private ArrayList<String > all_key = new ArrayList<>();
    private JSONArray jsonArray=new JSONArray();
    private MyListView money_list_2;
    @Override
    public void addLayout() {
        setContentView(R.layout.c_money_activity);
    }

    @Override
    public void initView() {
        mainPresenter=new MainPresenter(this,this);
        c_money_back= (RelativeLayout) findViewById(R.id.c_money_back);
        c_money_back.setOnClickListener(this);
        user_interest= (TextView) findViewById(R.id.user_interest);
        user_load= (TextView) findViewById(R.id.user_load);
        user_num= (TextView) findViewById(R.id.user_num);
        money_list_2= (MyListView) findViewById(R.id.money_list_2);
        level_count_text= (TextView) findViewById(R.id.level_count_text);
        yonghu_count_text_1= (TextView) findViewById(R.id.yonghu_count_text_1);
        yonghu_count_text_2= (TextView) findViewById(R.id.yonghu_count_text_2);
        yonghu_count_text_3= (TextView) findViewById(R.id.yonghu_count_text_3);
        progressDialog = new Dialog(C_Money_Activity.this,R.style.progress_dialog);
        progressDialog.setContentView(R.layout.dialog);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setText("加载中...");
        progressDialog.show();
        getData_1();
//        getData_2();
        getData_3();
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
                    user_num.setText(jsonObject1.getString("user_count"));
                    user_load.setText(jsonObject1.getString("download_count"));
                    user_interest.setText(jsonObject1.getString("subscribe"));
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
                JSONObject jsonObject = JSONObject.fromObject(s);
                Log.e("所有数据",jsonObject.toString());
                if(jsonObject.getString("success").equals("true")) {
                    if (jsonObject.getString("data").equals("[]")|| TextUtils.isEmpty(jsonObject.getString("data"))) {
                        ToastUtils.showToast(C_Money_Activity.this, "暂无数据");
                        return;
                    }
                    JSONObject jsonObject1 = JSONObject.fromObject(jsonObject.getString("data"));
                    Iterator iterator = jsonObject1.keys();                       // joData是JSONObject对象
                    while (iterator.hasNext()) {
                        String key = iterator.next() + "";
                        all_key.add(key);
                    }
                    ArrayList<String> strings = new ArrayList<>();
                    int number = 0;
                    int number_2 = 0;
                    for (int i = 0; i < all_key.size(); i++) {
                        strings.add(jsonObject1.getString(all_key.get(i)));
                        jsonArray = JSONArray.fromObject(strings.get(i));
                        for (int j = 0; j < jsonArray.size(); j++) {
                            JSONObject jsonObjects = jsonArray.getJSONObject(j);
                            number_2 += Integer.valueOf(jsonObjects.getString("total_count"));
                        }
                        number += jsonArray.size();
                    }
                    level_count_text.setText(number + "");
                    yonghu_count_text_1.setText(number_2 + "");
                    money_list_2.setAdapter(new TableAdapter_2(C_Money_Activity.this, strings,all_key, 4));
                }
            }
        });
    }

    @Override
    public void postViewss(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("负责区域/下级合伙人/和用户数量数据",s);
                JSONObject jsonObject  =JSONObject.fromObject(s);
                if(jsonObject.getString("success").equals("true")){
                    progressDialog.dismiss();
                    level_count_text.setText(jsonObject.getString("count"));
                    jsonArray.clear();
                    jsonArray = JSONArray.fromObject(jsonObject.getString("data"));
                    JSONObject item = new JSONObject();
                    int subscribe=0;
                    int download_count=0;
                    int user_count=0;
                    for (int i=0;i<jsonArray.size();i++){
                        item = jsonArray.getJSONObject(i);
                        subscribe=subscribe+Integer.valueOf(item.getString("subscribe"));
                        download_count=download_count+Integer.valueOf(item.getString("download_count"));
                        user_count=user_count+Integer.valueOf(item.getString("user_count"));
                    }
                    yonghu_count_text_1.setText(subscribe+"/");
                    yonghu_count_text_2.setText(download_count+"/");
                    yonghu_count_text_3.setText(user_count+"");
                    money_list_2.setAdapter(new TableAdapters(C_Money_Activity.this, jsonArray));
                }else {
                    progressDialog.dismiss();
                }
            }
        });
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.c_money_back:
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
        maps.put("member_id",BPApplication.getInstance().getMember_Id());
        maps.put("secret", CreateMD5.getMd5(BPApplication.getInstance().getMember_Id()+SystemConstant.PublicConstant.Public_SECRET));
        mainPresenter.wodes(SystemConstant.PublicConstant.API_GET_LEVEL,maps);
    }


    public void getData_3(){
        Map<String ,String> maps = new HashMap<>();
        maps.put("member_id",BPApplication.getInstance().getMember_Id());
        maps.put("secret", CreateMD5.getMd5(BPApplication.getInstance().getMember_Id()+SystemConstant.PublicConstant.Public_SECRET));
        mainPresenter.wodess(SystemConstant.PublicConstant.API_GET,maps);
    }
}
