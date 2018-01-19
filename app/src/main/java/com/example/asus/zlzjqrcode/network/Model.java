package com.example.asus.zlzjqrcode.network;

import android.content.Context;
import android.util.Log;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 方法模型层
 * Created by D&LL on 2017/3/13.
 */

public class Model {
    private static Model instance = new Model();//单例
    public static Model getInstance() {
        return instance;
    }
    public OkHttpClient client = OkHttp3.getClient();

    /**
     * 异步get请求
     *
     * @param context
     * @param url
     * @return
     * @throws Exception
     */
    public void getSynchronized(final Context context, final String url, final ICallBack callback) {
        Request request = new Request.Builder().url(url).build();
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS);
        client = builder.build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.fail(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String reselt =response.body().string();
                    if(!isGoodJson(reselt)){
                        callback.fail("网络连接错误，请检查网络设置");
                        return;
                    }else {
                        callback.result(reselt);
                    }
                } else {
                    callback.fail("服务器异常，请稍后再试");
                }
            }
        });
    }

    /**
     * post方式提交Map
     *
     * @param url
     * @param map
     * @return
     * @throws Exception
     */
    public void postMap(final Context context, final String url, Map<String, String> map, final ICallBack callback) {
        FormBody.Builder builder = new FormBody.Builder();
        if (map != null) {
            //增强for循环遍历
            for (Map.Entry<String, String> entry : map.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
//        String uri ="http://hbhz.sesdf.org/api/auto-login";
        FormBody formBody = builder.build();
        final Request request = new Request.Builder().post(formBody).url(url).build();
        OkHttpClient.Builder builders = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS);
        client = builders.build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.fail(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("response",response.toString());
                String reselt=response.body().string();
                if (response.isSuccessful()) {
                    if(!isGoodJson(reselt)){
                        callback.fail("网络连接错误，请检查网络设置");
                    }else {
                        callback.result(reselt);
                    }
                } else {
                    callback.fail("服务器异常，请稍后再试");
                }
            }
        });
    }


    public void wodes(final Context context, final String url, Map<String, String> map, final ICallBack callback) {
        FormBody.Builder builder = new FormBody.Builder();
        if (map != null) {
            //增强for循环遍历
            for (Map.Entry<String, String> entry : map.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        FormBody formBody = builder.build();
        Request request = new Request.Builder().post(formBody).url(url).build();
        OkHttpClient.Builder builders = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS);
        client = builders.build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.fail(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("response",response.toString());
                String reselt=response.body().string();
                if (response.isSuccessful()) {
                    if(!isGoodJson(reselt)){
                        callback.fail("网络连接错误，请检查网络设置");
                        return;
                    }else {
                        callback.result(reselt);
                    }
                } else {
                    callback.fail("服务器异常，请稍后再试");
                }
            }
        });
    }


    public void wode(final Context context,final String url,final ICallBack iCallBack){
        FormBody.Builder builder = new FormBody.Builder();
        FormBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                iCallBack.result(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    iCallBack.result(response.body().string());
                } else {
                    iCallBack.result("请求失败！");
                }
            }
        });
    }

    /**
     * post方式提交Json
     *
     * @param context
     * @param url
     * @param content
     * @param callback
     */
    public void postJson(final Context context, String url, String content, final ICallBack callback) {

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json"), content);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.result(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.result(response.body().string());
                } else {
                    callback.result("请求失败！");
                }
            }
        });
    }

    /**
     * 上传文件
     *
     * @param context
     * @param url
     * @param file
     * @param callback
     */
    private void postFile(final Context context, String url, File file, final ICallBack callback) {
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/octet-stream"), file))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println(response.body().string());
            }
        });
    }

//    /**
//     * 异步下载文件
//     *
//     * @param context
//     * @param url
//     * @param name
//     */
//    public void downAsynFile(final Context context, String url, final String name, final BitmapCallBack callback) {
//
//        final Request request = new Request.Builder().url(url).build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                System.out.println(e);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) {
//                if (response.isSuccessful()) {
//                    InputStream inputStream = response.body().byteStream();//获取文件输入流
//                    Bitmap bitmap = FileUtil.saveFile(name, inputStream);
//                    callback.imgBitmap(bitmap);
//                    System.out.println("下载成功！");
//
//                } else {
//                    System.out.println("下载失败！");
//                }
//                response.close();
//            }
//        });
//    }

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