package com.example.thegame.okhttp;

import androidx.annotation.NonNull;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CookieUnitTest {
    List<Cookie> cookies = new ArrayList<>();

    @Test
    public void okhttp() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(@NonNull HttpUrl httpUrl, @NonNull List<Cookie> list) {
                        cookies = list;
                    }

                    @NonNull
                    @Override
                    public List<Cookie> loadForRequest(@NonNull HttpUrl httpUrl) {
                        return cookies;
                    }
                })
                .build();

        FormBody formBody = new FormBody.Builder()
                .add("username", "牛肉炒饭")
                .add("password", "dianhua010412")
                .build();
        Request request = new Request.Builder()
                .url("https://www.wanandroid.com//user//login")
                .post(formBody).build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
