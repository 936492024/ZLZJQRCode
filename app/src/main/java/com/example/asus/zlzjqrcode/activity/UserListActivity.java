package com.example.asus.zlzjqrcode.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.asus.zlzjqrcode.R;
import com.example.asus.zlzjqrcode.ToastUtils;
import com.example.asus.zlzjqrcode.adpter.Listview_Adapter;
import com.example.asus.zlzjqrcode.adpter.MyListViewAdapter;
import com.example.asus.zlzjqrcode.base.BPApplication;
import com.example.asus.zlzjqrcode.base.BaseActivity;
import com.example.asus.zlzjqrcode.base.SystemConstant;
import com.example.asus.zlzjqrcode.eventbus.MainSendEvent;
import com.example.asus.zlzjqrcode.huadong.LeftAdapter;
import com.example.asus.zlzjqrcode.huadong.MyListView;
import com.example.asus.zlzjqrcode.huadong.MySyncHorizontalScrollView;
import com.example.asus.zlzjqrcode.huadong.Product;
import com.example.asus.zlzjqrcode.huadong.RightAdapter;
import com.example.asus.zlzjqrcode.network.MainPresenter;
import com.example.asus.zlzjqrcode.network.MainView;
import com.example.asus.zlzjqrcode.network.StringUtils;
import com.example.asus.zlzjqrcode.utils.CreateMD5;
import com.example.asus.zlzjqrcode.utils.Goods;
import com.example.asus.zlzjqrcode.utils.StatusBarUtils;
import com.example.asus.zlzjqrcode.utils.TableAdapter;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.greenrobot.event.EventBus;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by asus on 2018/1/10.
 */

public class UserListActivity extends BaseActivity implements View.OnClickListener,MainView{

    private TextView text_1,text_2,text_3;
    private Intent intent;
    private DrawerLayout dl;
    private LinearLayout drawer_view,linear_scroll,lin_card_4;
    private RelativeLayout add_user,user_list_back,add_rt;
    private Button add;
    private MainPresenter mainPresenter;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ListView listview;
    private MyListView tableListView;
    private  List<Goods> list = new ArrayList<Goods>();
    private Dialog progressDialog;
    private MySyncHorizontalScrollView rightTitleHorscrollView = null, rightContentHorscrollView = null;
    private MyListView contentListViewLeft = null, contentListViewRight = null;
    private LeftAdapter leftAdapter = null;
    private RightAdapter rightAdapter = null;
    private MyListViewAdapter myListViewAdapter;
    private List<Product> lists = new ArrayList<>();
    private String level_count,yonghu_count;
    private TextView level_count_text,yonghu_count_text;
    private CardView card;
    private CardView card_2,card_3,re_tt;
    private int flag=0;
    private int sum,sums=0;
    private TextView diqu,user_num,text_4,user_load,user_interest;
    private MyListView card_4_list_1,card_4_list_2;
    private ArrayList<String> member_id = new ArrayList<>();
    private View view_view;


    @Override
    public void addLayout() {
        setContentView(R.layout.userlist_activity);
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        progressDialog = new Dialog(UserListActivity.this,R.style.progress_dialog);
        progressDialog.setContentView(R.layout.dialog);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setText("加载中...");

        StatusBarUtils.with(this)
                .init();
        mainPresenter=new MainPresenter(this,this);
        add_user= (RelativeLayout) findViewById(R.id.add_user);
        add_user.setOnClickListener(this);
        user_list_back= (RelativeLayout) findViewById(R.id.user_list_back);
        user_list_back.setOnClickListener(this);
        drawer_view= (LinearLayout) findViewById(R.id.drawer_view);
        add= (Button) findViewById(R.id.add);
        add.setOnClickListener(this);
        text_1= (TextView) findViewById(R.id.text_1);
        text_2= (TextView) findViewById(R.id.text_2);
        text_3= (TextView) findViewById(R.id.text_3);
        text_4= (TextView) findViewById(R.id.text_4);
        card_4_list_1= (MyListView) findViewById(R.id.card_4_list_1);
        card_4_list_2= (MyListView) findViewById(R.id.card_4_list_2);
        lin_card_4= (LinearLayout) findViewById(R.id.lin_card_4);
        view_view=findViewById(R.id.view_view);
        diqu=(TextView) findViewById(R.id.diqu);
        add_rt= (RelativeLayout) findViewById(R.id.add_rt);
        re_tt= (CardView) findViewById(R.id.re_tt);
        user_load= (TextView) findViewById(R.id.user_load);
        user_interest= (TextView) findViewById(R.id.user_interest);
        level_count_text= (TextView) findViewById(R.id.level_count_text);
        yonghu_count_text= (TextView) findViewById(R.id.yonghu_count_text);
        linear_scroll= (LinearLayout) findViewById(R.id.linear_scroll);
        card= (CardView) findViewById(R.id.card);
        card_2=(CardView) findViewById(R.id.card_2);
        card_3=(CardView) findViewById(R.id.card_3);
        text_1.setText(BPApplication.getInstance().getLv_name()+"("+BPApplication.getInstance().getQuyu()+")");
        if(Integer.valueOf(BPApplication.getInstance().getCard_Id().substring(16, 17))%2==0) {
            text_2.setText(BPApplication.getInstance().getName()+"女士");
        }else{
            text_2.setText(BPApplication.getInstance().getName()+"先生");
        }
        text_3.setText(BPApplication.getInstance().getInvite());
        user_num=(TextView) findViewById(R.id.user_num);
        dl= (DrawerLayout) findViewById(R.id.drawer);
        listview= (ListView) findViewById(R.id.listview);
        tableListView = (MyListView) findViewById(R.id.list);

        if(BPApplication.getInstance().getIsadd().equals("0")){
            add_user.setVisibility(View.GONE);
            add_rt.setVisibility(View.GONE);
            card_2.setVisibility(View.GONE);
            card_3.setVisibility(View.GONE);
            re_tt.setVisibility(View.VISIBLE);
            getData_2();
            text_4.setText("用户统计");
        }else if(BPApplication.getInstance().getIsadd().equals("3")){
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            progressDialog.show();
            getData_1();
            text_4.setText("团队信息");
        }else if(BPApplication.getInstance().getIsadd().equals("2")){
            progressDialog.show();
            card_2.setVisibility(View.GONE);
            add_user.setVisibility(View.GONE);
            view_view.setVisibility(View.GONE);
            lin_card_4.setVisibility(View.VISIBLE);
            getData_3();
        }
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(arrayList.size()!=0){
                    card_3.requestFocus();
                    progressDialog.show();
                    listview.setCacheColorHint(0);
                    dl.closeDrawer(drawer_view);
                    Log.e("sdadaa",i+"");
                    diqu.setText(arrayList.get(i));
                    sum=i;
                    Map<String ,String > map = new HashMap<>();
                    map.put("member_id",BPApplication.getInstance().getMember_Id());
                    map.put("area",arrayList.get(i));
                    map.put("secret",CreateMD5.getMd5(arrayList.get(i)+BPApplication.getInstance().getMember_Id()+SystemConstant.PublicConstant.Public_SECRET));
                    mainPresenter.wodes(SystemConstant.PublicConstant.API_COUNT,map);
                }
            }
        });

        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        });
        scorllview();

        card_4_list_1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                progressDialog.show();
                gethttp(i);
            }
        });
    }

    //事件接受
    public void onEventMainThread(final MainSendEvent event){
        if(event != null){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(event.getStringMsgData().equals("加入成功")){
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_user:
                if (!dl.isDrawerOpen(drawer_view)) {
                    dl.openDrawer(drawer_view);
                }else {
                    dl.closeDrawer(drawer_view);
                }
                break;
            case R.id.user_list_back:
                finish();
                break;
            case R.id.add:
                intent = new Intent(UserListActivity.this,JoinActivity.class);
                startActivity(intent);
                break;
        }
    }


    public void getData_1(){
        Map<String ,String> maps = new HashMap<>();
        maps.put("member_id",BPApplication.getInstance().getMember_Id());
        maps.put("secret", CreateMD5.getMd5(BPApplication.getInstance().getMember_Id()+SystemConstant.PublicConstant.Public_SECRET));
        mainPresenter.wodess(SystemConstant.PublicConstant.API_DATA,maps);
    }

    public void getData_2(){
        Map<String ,String> maps = new HashMap<>();
        maps.put("member_id",BPApplication.getInstance().getMember_Id());
        maps.put("secret", CreateMD5.getMd5(BPApplication.getInstance().getMember_Id()+SystemConstant.PublicConstant.Public_SECRET));
        mainPresenter.wodesss_1(SystemConstant.PublicConstant.API_DATA,maps);
    }

    public void getData_3(){
        Map<String ,String> mapss = new HashMap<>();
        mapss.put("secret", CreateMD5.getMd5(SystemConstant.PublicConstant.Public_SECRET));
        mainPresenter.wodesss_2(SystemConstant.PublicConstant.API_GET_CITYS,mapss);
    }

    @Override
    public void getView(String s) {

    }

    @Override
    public void postView(final String s) {
          runOnUiThread(new Runnable() {
              @Override
              public void run() {
                  Log.e("停用手机号",s);
                  JSONObject jsonObject1 =JSONObject.fromObject(s);
                  if(jsonObject1.getString("success").equals("true")){
                      handler.sendEmptyMessage(0);
                      ToastUtils.showToast(UserListActivity.this,jsonObject1.getString("msg"));
                  }else {

                  }
                  progressDialog.dismiss();
              }
          });
    }

    @Override
    public void postViews(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("查询区域内合伙人和用户",s);
                JSONObject jsonObject = JSONObject.fromObject(s);
                lists.clear();
                if(jsonObject.getString("success").equals("true")){
                    if(flag==0){
                        add_rt.setVisibility(View.GONE);
                        card.setVisibility(View.GONE);
                        card_2.setVisibility(View.VISIBLE);
                        card_3.setVisibility(View.VISIBLE);
                        flag=1;
                    }
                    JSONArray jsonArray = JSONArray.fromObject(jsonObject.getString("data"));
                    JSONObject jsonObject1 = new JSONObject();
                    for(int i=0;i<jsonArray.size();i++){
                        jsonObject1=jsonArray.getJSONObject(i);
                        lists.add(new Product(jsonObject1.getString("name"), jsonObject1.getString("card_id"), jsonObject1.getString("mobile"),jsonObject1.getString("sum_count"),jsonObject1.getString("member_id"),jsonObject1.getString("is_show")));
                        Log.e("查询区域内合伙人和用户33",jsonObject1.toString());
                    }
                    contentListViewLeft.setAdapter(new LeftAdapter(UserListActivity.this, 0, lists));
                    contentListViewRight.setAdapter(new RightAdapter(UserListActivity.this, 0, lists));
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
                Log.e("负责区域/下级合伙人/和用户数量数据",s);
                JSONObject jsonObject  =JSONObject.fromObject(s);
                if(jsonObject.getString("success").equals("true")){
                    progressDialog.dismiss();
                    arrayList.clear();
                    list.clear();
                    level_count_text.setText(jsonObject.getString("level_count"));
                    yonghu_count_text.setText(jsonObject.getString("total_count"));
                     JSONArray jsonArray = JSONArray.fromObject(jsonObject.getString("data"));
                     JSONObject item = new JSONObject();
                     for (int i=0;i<jsonArray.size();i++){
                          item = jsonArray.getJSONObject(i);
                          arrayList.add(item.getString("areas"));
                         list.add(new Goods(item.getString("areas"), item.getString("count"), item.getString("sum_count")));
                     }
                    listview.setAdapter(new Listview_Adapter(UserListActivity.this,arrayList));
                    tableListView.setAdapter(new TableAdapter(UserListActivity.this, list));
                }else {
                    progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void postViewsss(final String s) {
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
                            ToastUtils.showToast(UserListActivity.this,jsonObject1.getString("msg"));
                        }else {
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            ToastUtils.showToast(UserListActivity.this,jsonObject1.getString("msg"));
                        }
                    }
                });

            }
        });
    }

    @Override
    public void postViewsss_1(final String s) {
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
    public void postViewsss_2(final String s) {
         runOnUiThread(new Runnable() {
             @Override
             public void run() {
                 Log.e("G级登录获取所有城市合作负责人",s);
                 JSONObject jsonObject1 = JSONObject.fromObject(s);
                 member_id.clear();
                 if( jsonObject1.getString("success").equals("true")){
                     JSONArray jsonObject11 = JSONArray.fromObject(jsonObject1.getString("data"));
                     card_4_list_1.setAdapter(new MyListViewAdapter(UserListActivity.this,jsonObject11,1));
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
    public void fail(final String s) {
       runOnUiThread(new Runnable() {
           @Override
           public void run() {
               if(progressDialog.isShowing()){
                   progressDialog.dismiss();
               }
               ToastUtils.showToast(UserListActivity.this,"服务器异常，请稍后再试");
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
            if (!dl.isDrawerOpen(drawer_view)) {
                finish();
            }else {
                dl.closeDrawer(drawer_view);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    public void scorllview(){
        //initviews
        rightTitleHorscrollView = (MySyncHorizontalScrollView)findViewById(R.id.rightTitleHorscrollView);
        rightContentHorscrollView = (MySyncHorizontalScrollView)findViewById(R.id.rightContentHorscrollView);
        contentListViewLeft = (MyListView)findViewById(R.id.contentListViewLeft);
        contentListViewRight = (MyListView)findViewById(R.id.contentListViewRight);

        //相互引用
        rightTitleHorscrollView.setmSyncView(rightContentHorscrollView);
        rightContentHorscrollView.setmSyncView(rightTitleHorscrollView);
        contentListViewLeft.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showHeadDialog(i);
                return false;
            }
        });
        contentListViewRight.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showHeadDialog(i);
                return false;
            }
        });
    }



    /*设置并显示Dialog*/
    public void showHeadDialog(final int position) {
        View view = LayoutInflater.from(UserListActivity.this).inflate(R.layout.head_dialog, null);
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
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(UserListActivity.this);
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
                        map.put("secret",CreateMD5.getMd5("0"+lists.get(position).getMember_id()+SystemConstant.PublicConstant.Public_SECRET));
                        mainPresenter.postMap(SystemConstant.PublicConstant.API_IS_SHOW,map);
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
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(UserListActivity.this);
                builder.setTitle("温馨提示");
                builder.setMessage("确认停用"+lists.get(position).getName()+"的账号？");
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
                        mainPresenter.postMap(SystemConstant.PublicConstant.API_IS_SHOW,map);
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

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if(message.what==0){
                Log.e("sum",sum+"");
                Map<String ,String > map = new HashMap<>();
                map.put("member_id",BPApplication.getInstance().getMember_Id());
                map.put("area",arrayList.get(sum));
                map.put("secret",CreateMD5.getMd5(arrayList.get(sum)+BPApplication.getInstance().getMember_Id()+SystemConstant.PublicConstant.Public_SECRET));
                mainPresenter.wodes(SystemConstant.PublicConstant.API_COUNT,map);
            }else if(message.what==1){
                getData_1();//加入计划成功的回调刷新界面
            }
            return false;
        }
    });

    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("^1[3|4|5|7|8|9][0-9]{9}$");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    @SuppressWarnings("static-access")
    public void game_clock_dialog(final int position) {
        final LayoutInflater inflater = LayoutInflater.from(UserListActivity.this);
        View view = inflater.inflate(R.layout.make_password_dialog, null);
        Button cancel_dialog = (Button) view.findViewById(R.id.cancel_dialog);
        Button make_sure =(Button) view.findViewById(R.id.make_sure);
        final EditText make_phone =(EditText) view.findViewById(R.id.make_phone);
        final Dialog dialog = new Dialog(UserListActivity.this);
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
                    ToastUtils.showToast(UserListActivity.this,"请输入正确的手机号码");
                    return;
                }
                progressDialog.show();
                Map<String,String > map = new HashMap<>();
                map.put("mobile",phone);
                map.put("member_id",lists.get(position).getMember_id());
                map.put("secret",CreateMD5.getMd5(lists.get(position).getMember_id()+phone+SystemConstant.PublicConstant.Public_SECRET));
                mainPresenter.wodesss(SystemConstant.PublicConstant.API_SET_MOBILE,map);
                dialog.dismiss();
            }
        });
    }


    public void user_xiangqing_dialog(int position){
        final LayoutInflater inflater = LayoutInflater.from(UserListActivity.this);
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
        final Dialog dialog = new Dialog(UserListActivity.this);
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
                        ToastUtils.showToast(UserListActivity.this,"服务器异常，请稍后再试");
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
                                ToastUtils.showToast(UserListActivity.this,"网络连接错误，请检查网络设置");
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
                                            ToastUtils.showToast(UserListActivity.this,"暂无数据");
                                    }
                                    MyListViewAdapter adapter = new MyListViewAdapter(UserListActivity.this,jsonObject,2);
                                    adapter.setDefSelect(0);
                                    card_4_list_2.setAdapter(adapter);
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
                            ToastUtils.showToast(UserListActivity.this,"服务器异常，请稍后再试");
                        }
                    });
                }
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
}
