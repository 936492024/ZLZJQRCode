package com.example.asus.zlzjqrcode.network;

import android.graphics.Bitmap;

/**
 * Created by D&LL on 2017/3/13.
 */

public interface MainView {//视图层，数据传输
    void getView(String s);
    void postView(String s);
    void postViews(String s);
    void postViewss(String s);
    void postViewsss(String s);
    void postViewsss_1(String s);
    void postViewsss_2(String s);
    void fail(String s);
    void imgView(Bitmap bitmap);

}
