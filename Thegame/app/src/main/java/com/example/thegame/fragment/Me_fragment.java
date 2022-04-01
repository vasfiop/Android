package com.example.thegame.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.thegame.R;
import com.example.thegame.activity.Login_Activity;

import static android.app.Activity.RESULT_OK;


import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Me_fragment extends Fragment {
    private View view;
    private Button button, btn_logout;
    private View logon, lognot;
    private int mode = 0; // 0为未登录 1为登陆
    private TextView txt_user;
    String username;
    private SharedPreferences sharedPreferences;
    String uid = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_me, container, false);

        sharedPreferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences("user", Context.MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", null);
        if (uid == null || uid.equals(""))
            mode = 0;
        else
            mode = 1;

        logininit();

        if (mode == 0) {
            logon.setVisibility(View.GONE);
            lognot.setVisibility(View.VISIBLE);
            btn_logout.setVisibility(View.GONE);
        } else {
            new UserTask().execute();
            logon.setVisibility(View.VISIBLE);
            lognot.setVisibility(View.GONE);
            btn_logout.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private void logininit() {
        logon = view.findViewById(R.id.MF_include_login);
        lognot = view.findViewById(R.id.MF_include_not_login);

        button = view.findViewById(R.id.ME_btn_login);
        btn_logout = view.findViewById(R.id.FM_btn_logout);
        txt_user = view.findViewById(R.id.LA_txt_username);

        button.setOnClickListener(new MyClickListener());
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logon = view.findViewById(R.id.MF_include_login);
                lognot = view.findViewById(R.id.MF_include_not_login);
                logon.setVisibility(View.GONE);
                lognot.setVisibility(View.VISIBLE);
                btn_logout.setVisibility(View.GONE);
//                清空本地存储
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                mode = 0;
            }
        });
    }

    private class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), Login_Activity.class);
            startActivityForResult(intent, 200);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK) {
//            assert data != null;
            username = data.getStringExtra("u_name");
            mode = 1;
            logon.setVisibility(View.VISIBLE);
            lognot.setVisibility(View.GONE);
            txt_user.setText("Darling: " + username);
            btn_logout.setVisibility(View.VISIBLE);
        }
    }

    class UserTask extends AsyncTask<String, String, Boolean> {
        JSONObject jsonObject;

        @Override
        protected Boolean doInBackground(String... strings) {
            OkHttpClient okHttpClient = new OkHttpClient();
            FormBody formBody = new FormBody.Builder().add("uid", uid).build();
            Request request = new Request.Builder().url("https://k4608x0632.imdo.co/android/getUser.php").post(formBody).build();
            Call call = okHttpClient.newCall(request);
            try {
                Response response = call.execute();
                String jsonString = response.body().string();
                jsonObject = JSONObject.parseObject(jsonString);

                return jsonObject.getBoolean("success");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (!aBoolean) {
                Toast.makeText(getActivity(), "我们遇到了未知错误!", Toast.LENGTH_SHORT).show();
            } else {
                username = jsonObject.getJSONObject("0").getString("u_name");
//                FIXME 这块有点违和
                txt_user.setText("Darling: " + username);
            }
        }
    }
}
