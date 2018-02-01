package com.example.asus.zlzjqrcode.activity;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.asus.zlzjqrcode.R;
import com.example.asus.zlzjqrcode.ToastUtils;
import com.example.asus.zlzjqrcode.adpter.TableAdapter_2;
import com.example.asus.zlzjqrcode.base.BPApplication;
import com.example.asus.zlzjqrcode.base.BaseActivity;
import com.example.asus.zlzjqrcode.base.SystemConstant;
import com.example.asus.zlzjqrcode.huadong.MyListView;
import com.example.asus.zlzjqrcode.network.MainPresenter;
import com.example.asus.zlzjqrcode.network.MainView;
import com.example.asus.zlzjqrcode.utils.CreateMD5;
import com.example.asus.zlzjqrcode.utils.TableAdapter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by asus on 2018/1/13.
 */

public class MoneyActivity extends BaseActivity implements MainView,View.OnClickListener {

    private MainPresenter mainPresenter;
    private RelativeLayout user_money_back;
    private Dialog progressDialog;
    private TextView user_num,user_load,user_interest;
    private MyListView money_list,money_list_2,money_list_3;
    private ArrayList<String > all_key = new ArrayList<>();
    private ArrayList<String > all_sum = new ArrayList<>();
    JSONArray jsonArray;
    String a;
    private TextView level_count_text,yonghu_count_text;
    private CardView card_2;

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
//        money_list= (MyListView) findViewById(R.id.money_list);
        money_list_2= (MyListView) findViewById(R.id.money_list_2);
//        money_list_3= (MyListView) findViewById(R.id.money_list_3);
        card_2= (CardView) findViewById(R.id.card_2);
        user_money_back= (RelativeLayout) findViewById(R.id.user_money_back);
        user_money_back.setOnClickListener(this);
        progressDialog = new Dialog(MoneyActivity.this,R.style.progress_dialog);
        progressDialog.setContentView(R.layout.dialog);
        progressDialog.setCancelable(true);
        level_count_text= (TextView) findViewById(R.id.level_count_text);
//        yonghu_count_text= (TextView) findViewById(R.id.yonghu_count_text);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setText("加载中...");
        progressDialog.show();
        if(BPApplication.getInstance().getIsadd().equals("0")){
            card_2.setVisibility(View.GONE);
            getData_2_1();
        }else if(BPApplication.getInstance().getIsadd().equals("3")){
            getData_3();
            getData_2();
        }else {
            card_2.setVisibility(View.GONE);
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
    public void postViews(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("事业合伙人1",s);
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
    public void postViewss(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = JSONObject.fromObject(s);
                Log.e("所有数据",jsonObject.toString());
                if(jsonObject.getString("success").equals("true")) {
                    if (jsonObject.getString("data").equals("[]")||TextUtils.isEmpty(jsonObject.getString("data"))) {
                        ToastUtils.showToast(MoneyActivity.this, "暂无数据");
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
                    yonghu_count_text.setText(number_2 + "");
//                    money_list.setAdapter(new TableAdapter_2(MoneyActivity.this, strings,all_key, 1));
//                    money_list_2.setAdapter(new TableAdapter_2(MoneyActivity.this,strings,all_key ,2));
//                    money_list_3.setAdapter(new TableAdapter_2(MoneyActivity.this, strings,all_key, 3));
                    money_list_2.setAdapter(new TableAdapter_2(MoneyActivity.this, strings,all_key, 4));
                }
            }
        });
    }

    @Override
    public void postViewsss(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("事业合伙人1",s);
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
                ToastUtils.showToast(MoneyActivity.this,"网络连接超时，请稍后再试");
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
        maps.put("secret", CreateMD5.getMd5( SystemConstant.PublicConstant.Public_SECRET));
        mainPresenter.postMap(SystemConstant.PublicConstant.API_GET_TOTAL,maps);
    }
    public void getData_2(){
        Map<String ,String> maps = new HashMap<>();
        maps.put("member_id", BPApplication.getInstance().getMember_Id());
        maps.put("secret", CreateMD5.getMd5(BPApplication.getInstance().getMember_Id()+ SystemConstant.PublicConstant.Public_SECRET));
        mainPresenter.wodes(SystemConstant.PublicConstant.API_DATA,maps);
    }

    public void getData_2_1(){
        Map<String ,String> maps = new HashMap<>();
        maps.put("member_id", BPApplication.getInstance().getMember_Id());
        maps.put("secret", CreateMD5.getMd5(BPApplication.getInstance().getMember_Id()+ SystemConstant.PublicConstant.Public_SECRET));
        mainPresenter.wodesss(SystemConstant.PublicConstant.API_DATA,maps);
    }


    public void getData_3(){
        Map<String ,String> maps = new HashMap<>();
        maps.put("member_id",BPApplication.getInstance().getMember_Id());
        maps.put("secret", CreateMD5.getMd5(BPApplication.getInstance().getMember_Id()+SystemConstant.PublicConstant.Public_SECRET));
        mainPresenter.wodess(SystemConstant.PublicConstant.API_GET_LEVEL,maps);
    }
}
