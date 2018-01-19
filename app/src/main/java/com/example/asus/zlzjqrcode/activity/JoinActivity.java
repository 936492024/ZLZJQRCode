package com.example.asus.zlzjqrcode.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.example.asus.zlzjqrcode.R;
import com.example.asus.zlzjqrcode.ToastUtils;
import com.example.asus.zlzjqrcode.base.BPApplication;
import com.example.asus.zlzjqrcode.base.BaseActivity;
import com.example.asus.zlzjqrcode.base.SystemConstant;
import com.example.asus.zlzjqrcode.db.AreaBean;

import com.example.asus.zlzjqrcode.eventbus.MainSendEvent;
import com.example.asus.zlzjqrcode.network.MainPresenter;
import com.example.asus.zlzjqrcode.network.MainView;
import com.example.asus.zlzjqrcode.utils.CreateMD5;
import com.example.asus.zlzjqrcode.utils.StatusBarUtils;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;

import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.greenrobot.event.EventBus;

/**
 * Created by asus on 2018/1/9.
 */

public class JoinActivity extends BaseActivity implements View.OnClickListener,MainView{

    private EditText ok_name,ok_idcard,ok_phone_1,ok_phone_2,ok_area;
    private RelativeLayout ok_choice,user_add_back;
    private Button ok_ok;
    private String id_regular="^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
    private TextView area_text,area_text_2;
    private MainPresenter mainPresenter;
    private String provinces,citys,areas;
    public CityConfig.WheelType mWheelType;
    private boolean isShowBg = false;
    private int visibleItems = 5;
    private String defaultProvinceName = "北京";

    private String defaultCityName = "北京";

    private String defaultDistrict = "东城区";

    @Override
    public void addLayout() {
        setContentView(R.layout.join_activity);
    }

    @Override
    public void initView() {
        StatusBarUtils.with(this)
                .init();
        mainPresenter=new MainPresenter(this,this);
        ok_name=(EditText) findViewById(R.id.ok_name);
        ok_idcard=(EditText) findViewById(R.id.ok_idcard);
        ok_phone_1=(EditText) findViewById(R.id.ok_phone_1);
        ok_phone_2=(EditText) findViewById(R.id.ok_phone_2);
        ok_choice=(RelativeLayout) findViewById(R.id.ok_choice);
        ok_choice.setOnClickListener(this);
        ok_area=(EditText) findViewById(R.id.ok_area);
        ok_ok=(Button) findViewById(R.id.ok_ok);
        ok_ok.setOnClickListener(this);
        area_text=(TextView) findViewById(R.id.area_text);
        user_add_back= (RelativeLayout) findViewById(R.id.user_add_back);
        user_add_back.setOnClickListener(this);
        area_text_2= (TextView) findViewById(R.id.area_text_2);
        edit_onclic();
        edit_onclic_2();
        handler_2.postDelayed(runnable_2, 500);
        if(BPApplication.getInstance().getIsadd().equals("2")){
            mWheelType = CityConfig.WheelType.PRO_CITY;
        }else if(BPApplication.getInstance().getIsadd().equals("3")){
            mWheelType = CityConfig.WheelType.PRO_CITY_DIS;
        }
    }


    @Override
    public void onClick(View view) {
           switch (view.getId()){
               case R.id.ok_ok:
                   String ok_names = ok_name.getText().toString();
                   String ok_idcards = ok_idcard.getText().toString();
                   String ok_phone_1s = ok_phone_1.getText().toString();
                   String ok_phone_2s = ok_phone_2.getText().toString();
                   String area_text_3 = area_text_2.getText().toString();
                   String ok_areas = ok_area.getText().toString();
                   if(TextUtils.isEmpty(area_text_3)){
                       ToastUtils.showToast(JoinActivity.this,"请选择地区");
                       return;
                   }
//                   if(TextUtils.isEmpty(ok_areas)){
//                       ToastUtils.showToast(JoinActivity.this,"请填写负责范围");
//                       return;
//                   }
                   if(TextUtils.isEmpty(ok_names)){
                       ToastUtils.showToast(JoinActivity.this,"请输入姓名");
                       return;
                   }
                   if(!isNameric(ok_names)){
                       ToastUtils.showToast(JoinActivity.this,"姓名必须为简体中文");
                       return;
                   }
                   if(TextUtils.isEmpty(ok_idcards)){
                       ToastUtils.showToast(JoinActivity.this,"请输入身份证号码");
                       return;
                   }
                   if(!isidcard(ok_idcards)){
                       ToastUtils.showToast(JoinActivity.this,"身份证号输入有误");
                       return;
                   }


                   if(TextUtils.isEmpty(ok_phone_1s)){
                       ToastUtils.showToast(JoinActivity.this,"请输入手机号码");
                       return;
                   }
                   if(!isNumeric(ok_phone_1s)){
                       ToastUtils.showToast(JoinActivity.this,"请输入正确的手机号码");
                       return;
                   }

                   if(TextUtils.isEmpty(ok_phone_2s)){
                       ToastUtils.showToast(JoinActivity.this,"请输入手机号码");
                       return;
                   }
                   if(!isNumeric(ok_phone_2s)){
                       ToastUtils.showToast(JoinActivity.this,"请输入正确的手机号码");
                       return;
                   }
                   if(!ok_phone_1s.equals(ok_phone_2s)){
                       ToastUtils.showToast(JoinActivity.this,"手机号码输入不一致，请确认");
                       return;
                   }
                   String paren_id = BPApplication.getInstance().getMember_Id();
                   String lv_id=BPApplication.getInstance().getIsadd();
                   Map<String,String > map = new HashMap<>();
                   map.put("paren_id",paren_id);//上级id
                   map.put("lv_id",lv_id);//合伙人等级
                   map.put("name",ok_names);
                   map.put("mobile",ok_phone_2s);
                   map.put("desc",ok_areas);//负责范围备注
                   map.put("card_id",ok_idcards);//身份证号
                   map.put("provinces",provinces);
                   map.put("citys",citys);
                   if(BPApplication.getInstance().getIsadd().equals("3")){
                       map.put("areas",areas);
                       map.put("secret", CreateMD5.getMd5(areas+ok_idcards+citys+ok_areas+lv_id+ok_phone_2s+ok_names+paren_id
                               +provinces+SystemConstant.PublicConstant.Public_SECRET));
                   }else if(BPApplication.getInstance().getIsadd().equals("2")){
                       map.put("secret", CreateMD5.getMd5(ok_idcards+citys+ok_areas+lv_id+ok_phone_2s+ok_names+paren_id
                               +provinces+SystemConstant.PublicConstant.Public_SECRET));
                   }
                   Log.e("添加人",map.toString());
                   mainPresenter.postMap(SystemConstant.PublicConstant.API_ADD_PARTNER,map);
                   break;
               case R.id.ok_choice:
                   wheel();
                   break;
               case R.id.user_add_back:
                   finish();
                   break;
           }
    }


    public boolean isidcard(String idcard){
        Pattern pattern = Pattern.compile(id_regular);
        Matcher isNum = pattern.matcher(idcard);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    public boolean isNameric(String str){
        Pattern pattern = Pattern.compile("^[\\u4E00-\\u9FA5]+$");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("^1[3|4|5|7|8|9][0-9]{9}$");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    @Override
    public void getView(String s) {

    }

    @Override
    public void postView(final String s) {
        Log.e("添加合伙人",s);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = JSONObject.fromObject(s);
                if(jsonObject.getString("success").equals("true")){
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(JoinActivity.this);
                    builder.setTitle("温馨提示");
                    builder.setMessage("添加成功");
                    builder.setCancelable(false);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            EventBus.getDefault().post(new MainSendEvent("加入成功"));
                            finish();
                        }
                    });
                    builder.setIcon(R.drawable.xiufanghua);
                    builder.show();
                }else {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(JoinActivity.this);
                    builder.setTitle("温馨提示");
                    builder.setMessage(jsonObject.getString("msg"));
                    builder.setCancelable(false);
                    builder.setPositiveButton("确定",null);
                    builder.setIcon(R.drawable.xiufanghua);
                    builder.show();
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

    Handler handler_2=new Handler();
    Runnable runnable_2=new Runnable() {
        @Override
        public void run() {
            CityPickerView.getInstance().init(JoinActivity.this);
            handler_2.removeCallbacks(runnable_2);
        }
    };

    public void edit_onclic(){
        ok_phone_1.addTextChangedListener(new TextWatcher() {//电话号码的监听
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length()==11){
                    String getphone=ok_phone_1.getText().toString();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm!=null){
                        boolean isOpen=imm.isActive();
                        if(isOpen){//输入法打开
                            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        }else {//输入法关闭
                            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    }
                    if(!isNumeric(getphone)){
                        ToastUtils.showToast(JoinActivity.this,"请输入正确的电话号码");
                        return;
                    }
                }
            }
        });
    }
    public void edit_onclic_2(){
        ok_phone_2.addTextChangedListener(new TextWatcher() {//电话号码的监听
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length()==11){
                    String getphone=ok_phone_2.getText().toString();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm!=null){
                    boolean isOpen=imm.isActive();
                    if(isOpen){//输入法打开
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }else {//输入法关闭
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    }
                    if(!isNumeric(getphone)){
                        ToastUtils.showToast(JoinActivity.this,"请输入正确的电话号码");
                        return;
                    }
                }
            }
        });
    }


    /**
     * 弹出选择器
     */
    private void wheel() {
        CityConfig cityConfig = new CityConfig.Builder(JoinActivity.this).title("选择城市")
                .titleTextSize(18)
                .titleTextColor("#585858")
                .titleBackgroundColor("#E9E9E9")
                .confirTextColor("#585858")
                .confirmText("确认")
                .confirmTextSize(16)
                .cancelTextColor("#585858")
                .cancelText("取消")
                .cancelTextSize(16)
                .setCityWheelType(mWheelType)
                .showBackground(isShowBg)
                .visibleItemsCount(visibleItems)
                .province(defaultProvinceName)
                .city(defaultCityName)
                .district(defaultDistrict)
                .provinceCyclic(false)
                .cityCyclic(false)
                .districtCyclic(false)
                .setCustomItemLayout(R.layout.item_city)
                .setCustomItemTextViewId(R.id.item_city_name_tv)
                .build();

        CityPickerView.getInstance().setConfig(cityConfig);
        CityPickerView.getInstance().setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                if(BPApplication.getInstance().getIsadd().equals("3")){
                    if (province != null) {
                        provinces=province.getName();
                    }
                    if (city != null) {
                        citys=city.getName();
                    }
                    if (district != null) {
                        areas=district.getName();
                    }
                    area_text_2.setVisibility(View.VISIBLE);
                    area_text.setVisibility(View.GONE);
                    area_text_2.setText(provinces+citys+areas);
                }else if(BPApplication.getInstance().getIsadd().equals("2")){
                    if (province != null) {
                        provinces=province.getName();
                    }
                    if (city != null) {
                        citys=city.getName();
                    }
                    area_text_2.setVisibility(View.VISIBLE);
                    area_text.setVisibility(View.GONE);
                    area_text_2.setText(provinces+citys);
                }
            }
            @Override
            public void onCancel() {

            }
        });
        CityPickerView.getInstance().showCityPicker(this);
    }
}
