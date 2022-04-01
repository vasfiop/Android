package com.example.thegame.okhttp;

import androidx.annotation.NonNull;

import com.example.thegame.BuildConfig;

import org.junit.Test;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InternetUnitTest {
    @Test
    public void okhttp() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request request = chain.request();
                Headers headers = request.headers();
                String id = headers.get("os");
                System.out.println(id);
                Response response = chain.proceed(chain.request());

                request.newBuilder().addHeader("os", "android")
                        .addHeader("version", BuildConfig.VERSION_NAME);

                return response;
            }
        }).build();

        Request request = new Request.Builder()
                .addHeader("os", "android")
                .url("https://www.httpbin.org/get?a=1&b=2").build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
