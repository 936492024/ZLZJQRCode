package com.example.asus.zlzjqrcode.uset_list;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.asus.zlzjqrcode.R;
import com.example.asus.zlzjqrcode.ToastUtils;
import com.example.asus.zlzjqrcode.activity.JoinActivity;
import com.example.asus.zlzjqrcode.adpter.TableAdapter_3;
import com.example.asus.zlzjqrcode.base.BPApplication;
import com.example.asus.zlzjqrcode.base.BaseActivity;
import com.example.asus.zlzjqrcode.base.SystemConstant;
import com.example.asus.zlzjqrcode.eventbus.MainSendEvent;
import com.example.asus.zlzjqrcode.huadong.MyListView;
import com.example.asus.zlzjqrcode.huadong.Product;
import com.example.asus.zlzjqrcode.network.MainPresenter;
import com.example.asus.zlzjqrcode.network.MainView;
import com.example.asus.zlzjqrcode.utils.CreateMD5;
import com.example.asus.zlzjqrcode.utils.StatusBarUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.greenrobot.event.EventBus;

/**
 * Created by asus on 2018/1/24.
 */

public class C_List_Activity extends BaseActivity implements View.OnClickListener,MainView {
    private RelativeLayout c_list_back;
    private MainPresenter mainPresenter;
    private TextView text_1,text_2,text_3,text_id;
    private Button c_add;
    private Intent intent;
    private MyListView tableListView_2;
    private Dialog progressDialog;
    private List<Product> lists = new ArrayList<>();
    private JSONArray jsonArray =new JSONArray();
    private int sums=0;
    @Override
    public void addLayout() {
        setContentView(R.layout.c_list_activity);
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        mainPresenter=new MainPresenter(this,this);
        StatusBarUtils.with(this)
                .init();
        c_list_back= (RelativeLayout) findViewById(R.id.c_list_back);
        c_list_back.setOnClickListener(this);
        text_1= (TextView) findViewById(R.id.text_1);
        text_2= (TextView) findViewById(R.id.text_2);
        text_3= (TextView) findViewById(R.id.text_3);
        c_add= (Button) findViewById(R.id.c_add);
        c_add.setOnClickListener(this);
        text_id= (TextView) findViewById(R.id.text_id);
        tableListView_2= (MyListView) findViewById(R.id.list_2);
        progressDialog = new Dialog(C_List_Activity.this,R.style.progress_dialog);
        progressDialog.setContentView(R.layout.dialog);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setText("加载中...");
        progressDialog.show();
        text_1.setText(BPApplication.getInstance().getLv_name()+"("+BPApplication.getInstance().getQuyu()+")");
        if(Integer.valueOf(BPApplication.getInstance().getCard_Id().substring(16, 17))%2==0) {
            text_2.setText(BPApplication.getInstance().getName()+"女士");
        }else{
            text_2.setText(BPApplication.getInstance().getName()+"先生");
        }
        text_3.setText(BPApplication.getInstance().getInvite());
        onclic();
        getData_1();
    }

    @Override
    public void onClick(View view) {
          switch (view.getId()){
              case R.id.c_add:
                  intent = new Intent(C_List_Activity.this,JoinActivity.class);
                  startActivity(intent);
                  break;
              case R.id.c_list_back:
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
                Log.e("负责区域/下级合伙人/和用户数量数据",s);
                JSONObject jsonObject  =JSONObject.fromObject(s);
                if(jsonObject.getString("success").equals("true")){
                    progressDialog.dismiss();
//                    arrayList.clear();
                    lists.clear();
                    text_id.setText("下级合作者("+jsonObject.getString("count")+")");
//                    level_count_text.setText(jsonObject.getString("level_count"));
//                    yonghu_count_text.setText(jsonObject.getString("total_count"));
                    jsonArray.clear();
                    jsonArray = JSONArray.fromObject(jsonObject.getString("data"));
                    JSONObject item = new JSONObject();
                    for (int i=0;i<jsonArray.size();i++){
                        item = jsonArray.getJSONObject(i);
//                        arrayList.add(item.getString("areas"));
                        lists.add(new Product(item.getString("name"), item.getString("card_id"), item.getString("mobile"),item.getString("total_count"),item.getString("member_id"),item.getString("is_show")));
                    }
                    tableListView_2.setAdapter(new TableAdapter_3(C_List_Activity.this, jsonArray,2));
                }else {
                    progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void postViews(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("停用/启用手机号",s);
                JSONObject jsonObject1 =JSONObject.fromObject(s);
                if(jsonObject1.getString("success").equals("true")){
                    handler.sendEmptyMessage(0);
                    ToastUtils.showToast(C_List_Activity.this,jsonObject1.getString("msg"));
                }else {

                }
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void postViewss(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("修改手机号",s);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject jsonObject1 = JSONObject.fromObject(s);
                        if(jsonObject1.getString("success").equals("true")){
                            handler.sendEmptyMessage(0);
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            ToastUtils.showToast(C_List_Activity.this,jsonObject1.getString("msg"));
                        }else {
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            ToastUtils.showToast(C_List_Activity.this,jsonObject1.getString("msg"));
                        }
                    }
                });

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

    //事件接受
    public void onEventMainThread(final MainSendEvent event){
        if(event != null){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(event.getStringMsgData().equals("C级加入成功")){
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
            if(message.what==0){
                getData_1();
            }else if(message.what==1){
                getData_1();//加入计划成功的回调刷新界面
            }
            return false;
        }
    });
    public void onclic(){
        tableListView_2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showHeadDialog(i);
                return false;
            }
        });
    }

    /*设置并显示Dialog*/
    public void showHeadDialog(final int position) {
        View view = LayoutInflater.from(C_List_Activity.this).inflate(R.layout.head_dialog, null);
        final Dialog dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.anim_style);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.x = 0;
        layoutParams.y = ( this).getWindowManager().getDefaultDisplay().getHeight();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT; //保证dialog窗体可以水平铺满
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        if(lists.get(position).getIsshow().equals("0")){
            dialog.findViewById(R.id.tv_cancel_photo).setVisibility(View.VISIBLE);
            dialog.findViewById(R.id.tv_take_photo).setVisibility(View.GONE);
        }else {
            dialog.findViewById(R.id.tv_cancel_photo).setVisibility(View.GONE);
            dialog.findViewById(R.id.tv_take_photo).setVisibility(View.VISIBLE);
        }
        dialog.onWindowAttributesChanged(layoutParams);//设置dialog的摆放位置
        dialog.setCanceledOnTouchOutside(true);//设置点击dialog以为的区域dialog消失
        dialog.show();

        /*修改手机号*/
        dialog.findViewById(R.id.tv_select_photo).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                game_clock_dialog(position);
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        /*停用*/
        dialog.findViewById(R.id.tv_take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(C_List_Activity.this);
                builder.setTitle("温馨提示");
                builder.setMessage("确认停用"+lists.get(position).getName()+"的账号？");
                builder.setCancelable(false);
                builder.setNegativeButton("取消",null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressDialog.show();
                        Map<String,String > map = new HashMap<>();
                        map.put("is_show","0");
                        map.put("member_id",lists.get(position).getMember_id());
                        map.put("secret", CreateMD5.getMd5("0"+lists.get(position).getMember_id()+ SystemConstant.PublicConstant.Public_SECRET));
                        mainPresenter.wodes(SystemConstant.PublicConstant.API_IS_SHOW,map);
                    }
                });
                builder.setIcon(R.drawable.xiufanghua);
                builder.show();
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        //启用下级账号
        dialog.findViewById(R.id.tv_cancel_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(C_List_Activity.this);
                builder.setTitle("温馨提示");
                builder.setMessage("确认启用"+lists.get(position).getName()+"的账号？");
                builder.setCancelable(false);
                builder.setNegativeButton("取消",null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressDialog.show();
                        Map<String,String > map = new HashMap<>();
                        map.put("is_show","1");
                        map.put("member_id",lists.get(position).getMember_id());
                        map.put("secret",CreateMD5.getMd5("1"+lists.get(position).getMember_id()+SystemConstant.PublicConstant.Public_SECRET));
                        mainPresenter.wodes(SystemConstant.PublicConstant.API_IS_SHOW,map);
                    }
                });
                builder.setIcon(R.drawable.xiufanghua);
                builder.show();
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.user_xiangxi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_xiangqing_dialog(position);
                dialog.dismiss();
            }
        });
    }

    @SuppressWarnings("static-access")
    public void game_clock_dialog(final int position) {
        final LayoutInflater inflater = LayoutInflater.from(C_List_Activity.this);
        View view = inflater.inflate(R.layout.make_password_dialog, null);
        Button cancel_dialog = (Button) view.findViewById(R.id.cancel_dialog);
        Button make_sure =(Button) view.findViewById(R.id.make_sure);
        final EditText make_phone =(EditText) view.findViewById(R.id.make_phone);
        final Dialog dialog = new Dialog(C_List_Activity.this);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0xffffff));
        dialog.setCanceledOnTouchOutside(true);//
        dialog.show();
        // 设置宽度为屏幕的宽度
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width =  getResources().getDimensionPixelSize(R.dimen.clock_weight);
        lp.height=getResources().getDimensionPixelSize(R.dimen.clock_height);
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setContentView(view);
        cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();

            }
        });
        make_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone=make_phone.getText().toString();
                if(!isNumeric(phone)){
                    ToastUtils.showToast(C_List_Activity.this,"请输入正确的手机号码");
                    return;
                }
                progressDialog.show();
                Map<String,String > map = new HashMap<>();
                map.put("mobile",phone);
                map.put("member_id",lists.get(position).getMember_id());
                map.put("secret",CreateMD5.getMd5(lists.get(position).getMember_id()+phone+SystemConstant.PublicConstant.Public_SECRET));
                mainPresenter.wodess(SystemConstant.PublicConstant.API_SET_MOBILE,map);
                dialog.dismiss();
            }
        });
    }
    public void getData_1(){
        Map<String ,String> maps = new HashMap<>();
        maps.put("member_id",BPApplication.getInstance().getMember_Id());
        maps.put("secret", CreateMD5.getMd5(BPApplication.getInstance().getMember_Id()+SystemConstant.PublicConstant.Public_SECRET));
        mainPresenter.postMap(SystemConstant.PublicConstant.API_GET,maps);
    }
    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("^1[3|4|5|7|8|9][0-9]{9}$");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
    public void user_xiangqing_dialog(int position){
        final LayoutInflater inflater = LayoutInflater.from(C_List_Activity.this);
        View view = inflater.inflate(R.layout.user_xiangqing_dialog, null);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView phone =(TextView) view.findViewById(R.id.phone);
        TextView id_card =(TextView) view.findViewById(R.id.id_card);
        TextView number =(TextView) view.findViewById(R.id.number);
        name.setText(lists.get(position).getName());
        phone.setText(lists.get(position).getMobile());
        id_card.setText(lists.get(position).getIdcard());
        number.setText(lists.get(position).getCount()+"人");
        final TextView cancel =(TextView) view.findViewById(R.id.cancel);
        final Dialog dialog = new Dialog(C_List_Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0xffffff));
        dialog.setCanceledOnTouchOutside(true);//
        dialog.show();
        // 设置宽度为屏幕的宽度
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width =  getResources().getDimensionPixelSize(R.dimen.clock_weights);
        lp.height=getResources().getDimensionPixelSize(R.dimen.clock_heights);
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setContentView(view);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();

            }
        });
    }
}
