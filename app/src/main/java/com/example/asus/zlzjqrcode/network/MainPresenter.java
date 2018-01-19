package com.example.asus.zlzjqrcode.network;

import android.content.Context;
import java.util.Map;

/**
 * Created by D&LL on 2017/3/13.
 */

public class MainPresenter {//方法操作层

    private Model model = Model.getInstance();
    private MainView mainView;
    private Context context;



    public MainPresenter(Context context, MainView mainView) {
        this.context = context;
        this.mainView = mainView;
    }

    public void getRequest(String url) {
        model.getSynchronized(context, url, new ICallBack() {
            @Override
            public void result(String s) {
                System.out.println(s);
                mainView.getView(s);

            }

            @Override
            public void fail(String f) {
                mainView.fail(f);
            }
        });
    }
    public void wodes(String url, Map<String, String> map) {
        model.wodes(context, url, map, new ICallBack() {
            @Override
            public void result(String s) {
                System.out.println(s);
                mainView.postViews(s);
            }

            @Override
            public void fail(String f) {
                mainView.fail(f);
            }
        });
    }

    public void wodess(String url, Map<String, String> map) {
        model.wodes(context, url, map, new ICallBack() {
            @Override
            public void result(String s) {
                System.out.println(s);
                mainView.postViewss(s);
            }

            @Override
            public void fail(String f) {
                mainView.fail(f);
            }
        });
    }

    public void wodesss(String url, Map<String, String> map) {
        model.wodes(context, url, map, new ICallBack() {
            @Override
            public void result(String s) {
                System.out.println(s);
                mainView.postViewsss(s);
            }

            @Override
            public void fail(String f) {
                mainView.fail(f);
            }
        });
    }

    public void wodesss_1(String url, Map<String, String> map) {
        model.wodes(context, url, map, new ICallBack() {
            @Override
            public void result(String s) {
                System.out.println(s);
                mainView.postViewsss_1(s);
            }

            @Override
            public void fail(String f) {
                mainView.fail(f);
            }
        });
    }

    public void wodesss_2(String url, Map<String, String> map) {
        model.wodes(context, url, map, new ICallBack() {
            @Override
            public void result(String s) {
                System.out.println(s);
                mainView.postViewsss_2(s);
            }

            @Override
            public void fail(String f) {
                mainView.fail(f);
            }
        });
    }

    public void postMap(String url, Map<String, String> map) {
        model.postMap(context, url, map, new ICallBack() {
            @Override
            public void result(String s) {
                System.out.println(s);
                mainView.postView(s);
            }

            @Override
            public void fail(String f) {
                mainView.fail(f);
            }
        });
    }
}
