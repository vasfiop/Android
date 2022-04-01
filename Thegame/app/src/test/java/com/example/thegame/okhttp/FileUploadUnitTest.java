package com.example.thegame.okhttp;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FileUploadUnitTest {
    @Test
    public void okhttp() {
        OkHttpClient okHttpClient = new OkHttpClient();
        File file1 = new File("C:\\Users\\DELL\\Desktop\\1.txt");
        File file2 = new File("C:\\Users\\DELL\\Desktop\\1.txt");


        Request request = new Request.Builder().url("https://www.httpbin.org/post").post(
                new MultipartBody.Builder()
                        .addFormDataPart("file1", file1.getName(), RequestBody.create(file1, MediaType.parse("text/plain")))
                        .addFormDataPart("file2", file2.getName(), RequestBody.create(file2, MediaType.parse("text/plain")))
                        .build()
        ).build();

        Call call = okHttpClient.newCall(request);
        Response response = null;
        try {
            response = call.execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
