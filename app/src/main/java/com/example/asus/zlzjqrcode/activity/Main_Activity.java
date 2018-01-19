package com.example.asus.zlzjqrcode.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allenliu.versionchecklib.core.AllenChecker;
import com.allenliu.versionchecklib.core.VersionParams;
import com.allenliu.versionchecklib.core.http.HttpParams;
import com.allenliu.versionchecklib.core.http.HttpRequestMethod;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.util.Util;
import com.example.asus.zlzjqrcode.R;
import com.example.asus.zlzjqrcode.ToastUtils;
import com.example.asus.zlzjqrcode.base.BPApplication;
import com.example.asus.zlzjqrcode.base.BaseActivity;
import com.example.asus.zlzjqrcode.base.SystemConstant;
import com.example.asus.zlzjqrcode.eventbus.MainSendEvent;
import com.example.asus.zlzjqrcode.network.MainPresenter;
import com.example.asus.zlzjqrcode.network.MainView;
import com.example.asus.zlzjqrcode.service.DemoService_2;
import com.example.asus.zlzjqrcode.utils.CreateMD5;
import com.example.asus.zlzjqrcode.utils.CustomVersionDialogActivity;
import com.example.asus.zlzjqrcode.utils.ImgUtils;
import com.example.asus.zlzjqrcode.utils.StatusBarUtils;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by asus on 2018/1/9.
 */

public class Main_Activity extends BaseActivity implements View.OnClickListener ,MainView,EasyPermissions.PermissionCallbacks {

    private RelativeLayout set;
    private com.example.asus.zlzjqrcode.utils.StampView StampView;
    private Intent intent ;
    private Button user_btn,money;
    private TextView name;
    private int sum=0;
    private TextView lv_name;
    private long mExitTime;
    private MainPresenter mainPresenter;
    private Button share_code;
    private ImageView code_image;

    private static final String SAVE_PIC_PATH = Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory().getAbsolutePath() :
            "/mnt/sdcard";//保存到SD卡
    private static final String SAVE_REAL_PATH = SAVE_PIC_PATH + "/good/savePic";
    private static final int REQUEST_CODE_SAVE_IMG = 10;
    private static final String TAG = "MainActivity";
    private TextView main_text,text_3;
    private Bitmap mBitmap = null;
    private Context mContext = this;
    private Bitmap bmp;
    private Bitmap bmp_2;

    @Override
    public void addLayout() {
        setContentView(R.layout.main_activity);
    }

    @Override
    public void initView() {
        StatusBarUtils.with(this)
                .init();
        mContext = this;
        EventBus.getDefault().register(this);
        mainPresenter=new MainPresenter(this,this);
        StampView= (com.example.asus.zlzjqrcode.utils.StampView) findViewById(R.id.StampView);
        set= (RelativeLayout) findViewById(R.id.set);
        set.setOnClickListener(this);
        user_btn= (Button) findViewById(R.id.user_btn);
        user_btn.setOnClickListener(this);
        name= (TextView) findViewById(R.id.name);
        name.setOnClickListener(this);
        lv_name= (TextView) findViewById(R.id.lv_name);
        lv_name.setOnClickListener(this);
        money= (Button) findViewById(R.id.money);
        money.setOnClickListener(this);
        share_code= (Button) findViewById(R.id.share_code);
        share_code.setOnClickListener(this);
        code_image= (ImageView) findViewById(R.id.code_image);
        main_text= (TextView) findViewById(R.id.main_text);
        text_3= (TextView) findViewById(R.id.text_3);
        text_3.setText(BPApplication.getInstance().getInvite());
        Resources r = this.getResources();
        bmp= BitmapFactory.decodeResource(r, R.mipmap.code_share);
        updata();
        writeview();


        if(BPApplication.getInstance().getIsadd().equals("0")){
            String url = URLDecoder.decode(BPApplication.getInstance().getSubscribe_url());
            if(mContext!=null){
                Glide.with(mContext).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                    code_image.setImageBitmap(resource);
                        bmp_2=resource;
                        code_image.setImageBitmap(mergeBitmap(resource,bmp));
                    }
                });
            }
//            Glide.with(Main_Activity.this).load(URLDecoder.decode(BPApplication.getInstance().getSubscribe_url())).into(code_image);
        }else {
            mBitmap = CodeUtils.createImage(BPApplication.getInstance().getUrl(), 400, 400, BitmapFactory.decodeResource(getResources(), R.drawable.xiufanghua));
            code_image.setImageBitmap(mBitmap);
        }
        if(!TextUtils.isEmpty(getIntent().getStringExtra("Make_PasswordActivity"))){
            BPApplication.getInstance().exits(Main_Activity.this);
        }
    }

    //事件接受
    public void onEventMainThread(final MainSendEvent event){
            if(event != null){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(event.getStringMsgData().equals("自动登录成功")){
                                if(sum==0){
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            handler.sendEmptyMessage(1);
                                        }
                                    }).start();
                                    sum=1;
                                }
                        }
                    }
                });
            }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.set:
                if(!checkIsLogined()){//未登录
                    return;
                }else {
                    intent = new Intent(Main_Activity.this, UserSetActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.user_btn:
                if(!checkIsLogined()){//未登录
                    return;
                }else {
                    intent = new Intent(Main_Activity.this,UserListActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.name:
                if(!checkIsLogined()){//未登录
                    return;
                }
                break;
            case R.id.lv_name:
                if(!checkIsLogined()){//未登录
                    return;
                }
                break;
            case R.id.money:
                intent = new Intent(Main_Activity.this,MoneyActivity.class);
                startActivity(intent);
                break;
            case R.id.share_code:
                requestPermission();
                break;
        }
    }

    public void writeview(){
        if(!TextUtils.isEmpty(BPApplication.getInstance().getMember_Id())){
            lv_name.setText("负责区域："+BPApplication.getInstance().getQuyu());
            if(BPApplication.getInstance().getIsadd().equals("2")||BPApplication.getInstance().getIsadd().equals("3")){
                user_btn.setText("团队");
            }else {
                user_btn.setText("用户");
            }
            if(Integer.valueOf(BPApplication.getInstance().getCard_Id().substring(16, 17))%2==0) {
                name.setText(BPApplication.getInstance().getName()+"女士");
            }else{
                name.setText(BPApplication.getInstance().getName()+"先生");
            }
            StampView.setText("第"+BPApplication.getInstance().getNumber()+"位"+BPApplication.getInstance().getLv_name());
            StampView.setTextColor(Color.RED);
            if(BPApplication.getInstance().getIsadd().equals("3")){
                main_text.setText("城市合作负责人");
            }else if(BPApplication.getInstance().getIsadd().equals("2")){
                main_text.setText("公司系统管理员");
            }else {
                main_text.setText("团队合作负责人");
            }
        }
    }

    private boolean checkIsLogined() {
        if (!BPApplication.getInstance().isLogined()) {
            Intent intent = new Intent(Main_Activity.this, LoginActivity.class);
            startActivity(intent);
            return false;
        }
        return true;
    }


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case 1:
                    //做一些操作；
                    write_msg();
                    break;
                case 2:

                    break;
            }
            return false;
        }
    });


    public void write_msg(){
        lv_name.setText("负责区域："+BPApplication.getInstance().getQuyu());
        if(Integer.valueOf(BPApplication.getInstance().getCard_Id().substring(16, 17))%2==0) {
            name.setText(BPApplication.getInstance().getName()+"女士");
        }else{
            name.setText(BPApplication.getInstance().getName()+"先生");
        }
        if(BPApplication.getInstance().getIsadd().equals("0")){
            if (Util.isOnMainThread()) {
                String url = URLDecoder.decode(BPApplication.getInstance().getSubscribe_url());
                if(mContext!=null){
                    Glide.with(mContext).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            bmp_2=resource;
                            code_image.setImageBitmap(mergeBitmap(resource,bmp));
                        }
                    });
                }
            }

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(Util.isOnMainThread()&&!this.isFinishing())
        {
            Glide.with(this).pauseRequests();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了“返回键”
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //与上次点击返回键时刻作差
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                //大于2000ms则认为是误操作，使用Toast进行提示
                ToastUtils.showToast(this,"再按一次退出程序");
                //并记录下本次点击“返回键”的时刻，以便下次进行判断
                mExitTime = System.currentTimeMillis();
            } else {
                //小于2000ms则认为是用户确实希望退出程序-调用System.exit()方法进行退出
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void updata(){
        try {
            int versionCode = Main_Activity.this.getPackageManager().getPackageInfo("com.example.asus.zlzjqrcode", 0).versionCode;
            Map<String,String> map = new HashMap<>();
            map.put("version",versionCode+"");
            map.put("name","APP");
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

                     }else {
                         VersionParams.Builder builder = new VersionParams.Builder();
                         builder.setOnlyDownload(true)
                                 .setDownloadUrl(jsonObject1.getString("web"))
                                 .setTitle("版本更新")
                                 .setForceRedownload(false)
                                 .setCustomDownloadActivityClass(CustomVersionDialogActivity.class)
                                 .setUpdateMsg(jsonObject1.getString("intro"));
                         AllenChecker.startVersionCheck(Main_Activity.this, builder.build());
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



    public void saveFile(Bitmap bm, String fileName, String path) throws IOException {
        String subForder = SAVE_REAL_PATH + path;
        File foder = new File(subForder);
        if (!foder.exists()) {
            foder.mkdirs();
        }
        File myCaptureFile = new File(subForder, fileName);
        if (!myCaptureFile.exists()) {
            myCaptureFile.createNewFile();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED); //这是刷新SD卡
//    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);  // 这是刷新单个文件
        Uri uri = Uri.fromFile(new File(SAVE_REAL_PATH));
        intent.setData(uri);
        sendBroadcast(intent);

    }


    //保存图片
    private void saveImage() {
        boolean isSaveSuccess;
        if(BPApplication.getInstance().getIsadd().equals("0")){
//            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.erweima);
            isSaveSuccess= ImgUtils.saveImageToGallery(mContext, mergeBitmap(bmp_2, bmp));
        }else {
            mBitmap = CodeUtils.createImage(BPApplication.getInstance().getUrl(), 400, 400, BitmapFactory.decodeResource(getResources(), R.drawable.xiufanghua));
            isSaveSuccess = ImgUtils.saveImageToGallery(mContext, mBitmap);
        }

        if (isSaveSuccess) {
            Toast.makeText(mContext, "保存图片成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "保存图片失败，请稍后重试", Toast.LENGTH_SHORT).show();
        }
    }
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            //读取sd卡的权限
            String[] mPermissionList = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (EasyPermissions.hasPermissions(mContext, mPermissionList)) {
                //已经同意过
                saveImage();
            } else {
                //未同意过,或者说是拒绝了，再次申请权限
                EasyPermissions.requestPermissions(
                        this,  //上下文
                        "保存图片需要读取sd卡的权限", //提示文言
                        REQUEST_CODE_SAVE_IMG, //请求码
                        mPermissionList //权限列表
                );
            }
        } else {
            saveImage();
        }
    }

    //授权结果，分发下去
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        //跳转到onPermissionsGranted或者onPermissionsDenied去回调授权结果
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    //同意授权
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        Log.i(TAG, "onPermissionsGranted:" + requestCode + ":" + list.size());
        saveImage();
    }

    //拒绝授权
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.i(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            //打开系统设置，手动授权
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            //拒绝授权后，从系统设置了授权后，返回APP进行相应的操作
            Log.i(TAG, "onPermissionsDenied:------>自定义设置授权后返回APP");
            saveImage();
        }
    }

    public void updatas(){
        try {
            int versionCode = getPackageManager().getPackageInfo("com.example.asus.zlzjqrcode", 0).versionCode;
            HttpParams httpParams = new HttpParams();
            httpParams.put("name","APP");
            httpParams.put("version",versionCode+"");
            httpParams.put("secret", CreateMD5.getMd5("APP"+versionCode+SystemConstant.PublicConstant.Public_SECRET));
            Log.e("httpParams",httpParams.toString());
            VersionParams.Builder builder = new VersionParams.Builder()
                    .setRequestUrl(SystemConstant.PublicConstant.API_CHECK)
                    .setRequestMethod(HttpRequestMethod.POST)
                    .setRequestParams(httpParams)
                    .setService(DemoService_2.class);
            stopService(new Intent(Main_Activity.this, DemoService_2.class));
            CustomVersionDialogActivity.customVersionDialogIndex = 2;
            builder.setCustomDownloadActivityClass(CustomVersionDialogActivity.class);
            CustomVersionDialogActivity.isCustomDownloading = false;
            builder.setCustomDownloadActivityClass(CustomVersionDialogActivity.class);
            CustomVersionDialogActivity.isForceUpdate = false;
            builder.setCustomDownloadActivityClass(CustomVersionDialogActivity.class);
            AllenChecker.startVersionCheck(Main_Activity.this, builder.build());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Bitmap mergeBitmap(Bitmap firstBitmap, Bitmap secondBitmap) {
        Bitmap bitmap = Bitmap.createBitmap(firstBitmap.getWidth(), firstBitmap.getHeight(),firstBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        Log.e("firstBitmap",secondBitmap.getWidth()+"--"+secondBitmap.getHeight());
        canvas.drawBitmap(firstBitmap, new Matrix(), null);
        canvas.drawBitmap(secondBitmap, firstBitmap.getWidth()/2-45, firstBitmap.getHeight()/2-45, null);
        return bitmap;
    }
}
