package com.example.asus.zlzjqrcode.base;

/**
 * Created by asus on 2017/9/22.
 */

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 系统全局配置类
 *
 * @author fly
 *
 */
public class BPApplication extends Application {
    /**
     * 应用实例(单例)
     */
    private static BPApplication singleton;

//    private static ;
    public static Map<String, Object> map = new HashMap<String, Object>();

    /**
     * 返回应用实例
     *
     * @return
     */
    public static BPApplication getInstance() {
        return singleton;
    }

    private SharedPreferences share;

    /**
     * 系统activity列表,保存所有activity实例列表
     */
    public List<Activity> activityList = new LinkedList<Activity>();

    /**
     * 添加activity
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    /**
     * 关闭每一个list内的activity
     */
    public void exit() {
        try {
            for (Activity activity : activityList) {
                if (activity != null) {
                    activity.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    public void init(String member_id,String name,String setCard_Id,String setMobile,String paren_id,String is_show
                     ,String password,String join_time,String token,String lv_name,String lv_id,
                     Boolean yes,String invite,String isadd,String quyu,String number,String url) {
        Editor editor = share.edit();
        editor.commit();
        setMember_Id(member_id);
        setName(name);
        setCard_Id(setCard_Id);
        setMobile(setMobile);
        setParen_id(paren_id);
        setIs_show(is_show);
        setPassword(password);
        setJoin_time(join_time);
        setToken(token);
        setLv_name(lv_name);
        setLv_id(lv_id);
        setLogined(yes);
        setInvite(invite);
        setIsadd(isadd);
        setQuyu(quyu);
        setNumber(number);
        setUrl(url);
    }

    /**
     * 关闭除nowactivity之外的所有Activity
     *
     * @param
     */
    public void exits(Activity nowactivity) {
//        activityList.get(1).finish();
        try {
            for (Activity activity : activityList) {
                if (activity == nowactivity) {
                    continue;
                }
                if (activity != null) {
                    activity.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exit_1() {
        activityList.get(activityList.size()).finish();
        activityList.get(activityList.size()-1).finish();
    }

    @Override
    public void onCreate() {
        super.onCreate();
            share = getSharedPreferences(SystemConstant.SHARED_PREFERENCE_NAME,
                    MODE_PRIVATE);
            singleton = this;
        ZXingLibrary.initDisplayOpinion(this);
//            Thread.setDefaultUncaughtExceptionHandler(restartHandler); // 程序崩溃时触发线程
//            BPApplication.getInstance().setLogined(false);
    }



    /**
     * 异常捕获，重启应用
     */
    public UncaughtExceptionHandler restartHandler = new UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
//            exit();
        }
    };

    /**
     * 杀进程
     */
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    public SharedPreferences getShare() {
        return share;
    }


    public String getName() {
        return share.getString("name", "");
    }

    public void setName(String name) {
        Editor editor = share.edit();
        editor.putString("name", name);
        editor.commit();
    }

    public String getIs_show() {
        return share.getString("is_show", "");
    }

    public void setIs_show(String is_show) {
        Editor editor = share.edit();
        editor.putString("is_show", is_show);
        editor.commit();
    }

    public String getParen_id() {
        return share.getString("paren_id", "");
    }

    public void setParen_id(String paren_id) {
        Editor editor = share.edit();
        editor.putString("paren_id", paren_id);
        editor.commit();
    }


    public String getToken() {
        return share.getString("token", "");
    }

    public void setToken(String token) {
        Editor editor = share.edit();
        editor.putString("token", token);
        editor.commit();
    }



    public String getLove_code() {
        return share.getString("love_code", "");
    }

    public void setLove_code(String love_code) {
        Editor editor = share.edit();
        editor.putString("love_code", love_code);
        editor.commit();
    }



    public String getJoin_time() {
        return share.getString("join_time", "");
    }

    public void setJoin_time(String join_time) {
        Editor editor = share.edit();
        editor.putString("join_time", join_time);
        editor.commit();
    }

    public boolean isLogined() {
        return share.getBoolean("logined", false);
    }

    public void setLogined(boolean logined) {
        Editor editor = share.edit();
        editor.putBoolean("logined", logined);
        editor.commit();
    }

    public String getInvite() {
        return share.getString("invite", "");
    }

    public void setInvite(String invite) {
        Editor editor = share.edit();
        editor.putString("invite", invite);
        editor.commit();
    }



    public String getIsadd() {
        return share.getString("isadd", "");
    }

    public void setIsadd(String isadd) {
        Editor editor = share.edit();
        editor.putString("isadd", isadd);
        editor.commit();
    }

    public String getWeChat_3() {
        return share.getString("wechat_3", "");
    }

    public void setWeChat_3(String wechat3) {
        Editor editor = share.edit();
        editor.putString("wechat_3", wechat3);
        editor.commit();
    }

    public String getQuyu() {
        return share.getString("quyu", "");
    }

    public void setQuyu(String quyu) {
        Editor editor = share.edit();
        editor.putString("quyu", quyu);
        editor.commit();
    }
    public String getMobile() {
        return share.getString("mobile", "");
    }

    public void setMobile(String mobile) {
        Editor editor = share.edit();
        editor.putString("mobile", mobile);
        editor.commit();
    }



    public String getLv_name() {
        return share.getString("lv_name", "");
    }

    public void setLv_name(String lv_name) {
        Editor editor = share.edit();
        editor.putString("lv_name", lv_name);
        editor.commit();
    }

    public String getSigner() {
        return share.getString("signer", "");
    }

    public void setSigner(String signer) {
        Editor editor = share.edit();
        editor.putString("signer", signer);
        editor.commit();
    }


    public String getPassword() {
        return share.getString("password", "");
    }

    public void setPassword(String password) {
        Editor editor = share.edit();
        editor.putString("password", password);
        editor.commit();
    }


    public String getLv_id() {
        return share.getString("lv_id", "");
    }

    public void setLv_id(String lv_id) {
        Editor editor = share.edit();
        editor.putString("lv_id", lv_id);
        editor.commit();
    }


    public String getCard_Id() {
        return share.getString("card_id", "");
    }

    public void setCard_Id(String card_id) {
        Editor editor = share.edit();
        editor.putString("card_id", card_id);
        editor.commit();
    }

    public String getMember_Id() {
        return share.getString("member_id", "");
    }

    public void setMember_Id(String member_id) {
        Editor editor = share.edit();
        editor.putString("member_id", member_id);
        editor.commit();
    }

    public String getTitle() {
        return share.getString("title", "");
    }

    public void setTitle(String title) {
        Editor editor = share.edit();
        editor.putString("title", title);
        editor.commit();
    }


    public String getNumber() {
        return share.getString("number", "");
    }

    public void setNumber(String number) {
        Editor editor = share.edit();
        editor.putString("number", number);
        editor.commit();
    }

    public String getUrl() {
        return share.getString("url", "");
    }

    public void setUrl(String url) {
        Editor editor = share.edit();
        editor.putString("url", url);
        editor.commit();
    }

    public String getUpdate() {
        return share.getString("update", "");
    }

    public void setUpdate(String update) {
        Editor editor = share.edit();
        editor.putString("update", update);
        editor.commit();
    }


    public String getSubscribe_url() {
        return share.getString("subscribe_url", "");
    }

    public void setSubscribe_url(String subscribe_url) {
        Editor editor = share.edit();
        editor.putString("subscribe_url", subscribe_url);
        editor.commit();
    }
}















