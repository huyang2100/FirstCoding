package net.sourceforge.simcpux.net;

import android.os.Handler;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by yanghu on 2018/3/29.
 */

public class OkHttpMananger {
    private static OkHttpMananger instance = new OkHttpMananger();
    private OkHttpClient client;
    private Request.Builder requestBuild;
    private Handler handler;

    private OkHttpMananger() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10 * 1000, TimeUnit.MILLISECONDS);
        client = builder.build();

        requestBuild = new Request.Builder();
        handler = new Handler();
    }

    public interface CallBack {
        void onSuccess(String str);

        void onErr(Exception e);
    }

    private static OkHttpMananger getInstance() {
        return instance;
    }

    public static void get(String url, CallBack callback) {
        handleResult(instance.requestBuild.url(url).build(), callback);
    }

    public static void postString(String url, CallBack callback) {
        handleResult(instance.requestBuild.url(url).post(RequestBody.create(MediaType.parse("text/plain;charset=utf-8"), "Hello Android!")).build(), callback);
    }

    private static void handleResult(Request request, final CallBack callback) {
        instance.client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                showErr(e, callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    showSuccess(response.body().string(), callback);
                } else {
                    showErr(new Exception(String.valueOf(response.code())), callback);
                }
            }
        });
    }

    private static void showSuccess(final String string, final CallBack callback) {
        instance.handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(string);
            }
        });
    }

    private static void showErr(final Exception e, final CallBack callback) {
        instance.handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onErr(e);
            }
        });
    }
}
