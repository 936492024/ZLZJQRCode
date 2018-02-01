package com.example.asus.zlzjqrcode.uset_list;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.asus.zlzjqrcode.R;
import com.example.asus.zlzjqrcode.ToastUtils;
import com.example.asus.zlzjqrcode.activity.JoinActivity;
import com.example.asus.zlzjqrcode.adpter.MyListViewAdapter;
import com.example.asus.zlzjqrcode.base.BPApplication;
import com.example.asus.zlzjqrcode.base.BaseActivity;
import com.example.asus.zlzjqrcode.base.SystemConstant;
import com.example.asus.zlzjqrcode.eventbus.MainSendEvent;
import com.example.asus.zlzjqrcode.network.MainPresenter;
import com.example.asus.zlzjqrcode.network.MainView;
import com.example.asus.zlzjqrcode.network.StringUtils;
import com.example.asus.zlzjqrcode.utils.CreateMD5;
import com.example.asus.zlzjqrcode.utils.StatusBarUtils;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by asus on 2018/1/24.
 */

public class G_ListActivity extends BaseActivity implements View.OnClickListener,MainView{
    private Dialog progressDialog;
    private TextView text_1,text_2,text_3;
    private MainPresenter mainPresenter;
    private RelativeLayout g_list_back;
    private Button g_add;
    private Intent intent;
    private ListView g_list_1,g_list_2;
    private ArrayList<String> member_id = new ArrayList<>();
    private int sums=0;

    @Override
    public void addLayout() {
         setContentView(R.layout.g_list_activity);
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        mainPresenter=new MainPresenter(this,this);
        StatusBarUtils.with(this)
                .init();
        progressDialog = new Dialog(G_ListActivity.this,R.style.progress_dialog);
        progressDialog.setContentView(R.layout.dialog);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setText("加载中...");
        progressDialog.show();
        text_1= (TextView) findViewById(R.id.text_1);
        text_2= (TextView) findViewById(R.id.text_2);
        text_3= (TextView) findViewById(R.id.text_3);
        g_list_back= (RelativeLayout) findViewById(R.id.g_list_back);
        g_list_back.setOnClickListener(this);
        g_add= (Button) findViewById(R.id.g_add);
        g_add.setOnClickListener(this);
        g_list_1= (ListView) findViewById(R.id.card_4_list_1);
        g_list_2= (ListView) findViewById(R.id.card_4_list_2);
        text_1.setText(BPApplication.getInstance().getLv_name()+"("+BPApplication.getInstance().getQuyu()+")");
        if(Integer.valueOf(BPApplication.getInstance().getCard_Id().substring(16, 17))%2==0) {
            text_2.setText(BPApplication.getInstance().getName()+"女士");
        }else{
            text_2.setText(BPApplication.getInstance().getName()+"先生");
        }
        text_3.setText(BPApplication.getInstance().getInvite());
        onclic();
        getData_3();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.g_list_back:
                finish();
                break;
            case R.id.g_add:
                intent = new Intent(G_ListActivity.this,JoinActivity.class);
                startActivity(intent);
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
                Log.e("G级登录获取所有城市合作负责人",s);
                JSONObject jsonObject1 = JSONObject.fromObject(s);
                member_id.clear();
                if( jsonObject1.getString("success").equals("true")){
                    JSONArray jsonObject11 = JSONArray.fromObject(jsonObject1.getString("data"));
                    g_list_1.setAdapter(new MyListViewAdapter(G_ListActivity.this,jsonObject11,1));
                    for (int i=0;i<jsonObject11.size();i++){
                        JSONObject item =jsonObject11.getJSONObject(i);
                        member_id.add(item.getString("member_id")) ;
                    }
                    if(member_id!=null){
                        gethttp(0);
                    }
                }else {

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

    public void onclic(){
        g_list_1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                progressDialog.show();
                gethttp(i);
            }
        });
    }
    public  boolean isGoodJson(String json) {
        if (StringUtils.isEmptyString(json)) {
            return false;
        }
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonParseException e) {
            Log.e("bad json: ",json);
            return false;
        }
    }
    public void gethttp(int i){
        OkHttpClient mOkHttpClient=new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("member_id", member_id.get(i))
                .add("secret", CreateMD5.getMd5(member_id.get(i)+ SystemConstant.PublicConstant.Public_SECRET))
                .build();
        final Request request = new Request.Builder()
                .url(SystemConstant.PublicConstant.API_GET_AREAS)
                .post(formBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        ToastUtils.showToast(G_ListActivity.this,"服务器异常，请稍后再试");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("response",response.toString());
                final String reselt=response.body().string();
                if (response.isSuccessful()) {
                    if(!isGoodJson(reselt)){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(progressDialog.isShowing()){
                                    progressDialog.dismiss();
                                }
                                ToastUtils.showToast(G_ListActivity.this,"网络连接错误，请检查网络设置");
                            }
                        });
                        return;
                    }else {
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        Log.e("网络",reselt);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                JSONObject jsonObject11 = JSONObject.fromObject(reselt);
                                if(jsonObject11.getString("success").equals("true")){
                                    JSONArray jsonObject =JSONArray.fromObject(jsonObject11.getString("data"));
                                    if(jsonObject.size()==0){
                                        ToastUtils.showToast(G_ListActivity.this,"暂无数据");
                                    }
                                    MyListViewAdapter adapter = new MyListViewAdapter(G_ListActivity.this,jsonObject,2);
                                    g_list_2.setAdapter(adapter);
                                }
                            }
                        });
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            ToastUtils.showToast(G_ListActivity.this,"服务器异常，请稍后再试");
                        }
                    });
                }
            }
        });
    }
    public void getData_3(){
        Map<String ,String> mapss = new HashMap<>();
        mapss.put("secret", CreateMD5.getMd5(SystemConstant.PublicConstant.Public_SECRET));
        mainPresenter.postMap(SystemConstant.PublicConstant.API_GET_CITYS,mapss);
    }

    //事件接受
    public void onEventMainThread(final MainSendEvent event){
        if(event != null){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(event.getStringMsgData().equals("G级加入成功")){
                        if(sums==0){
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    handler.sendEmptyMessage(1);
                                }
                            }).start();
                            sums=1;
                        }
                    }
                }
            });
        }
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if(message.what==1){
                getData_3();//加入计划成功的回调刷新界面
            }
            return false;
        }
    });
}
