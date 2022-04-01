package com.example.thegame.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.thegame.R;
import com.example.thegame.myView.MyImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Car_fragment extends Fragment {
    private String uid;
    private View view;
    private LinearLayout linearLayout;
    private LayoutInflater layoutInflater;
    private TextView txt_name, txt_title, txt_mode, txt_money;
    private MyImageView imageView;

    private final OkHttpClient okHttpClient = new OkHttpClient();
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_car, container, false);
        layoutInflater = LayoutInflater.from(getActivity());

        sharedPreferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences("user", Context.MODE_PRIVATE);

        init();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        uid = sharedPreferences.getString("uid", null);
        if (uid != null && !uid.equals(""))
            new Car().execute();
    }


    private void init() {
        linearLayout = view.findViewById(R.id.CF_linear);
    }

    // 购物车请求
    class Car extends AsyncTask<String, String, Boolean> {
        JSONObject jsonObject;
        JSONArray jsonArray;
        InputStream inputStream;

        @Override
        protected Boolean doInBackground(String... strings) {

            FormBody formBody = new FormBody.Builder().add("uid", uid).build();
            Request request = new Request.Builder().post(formBody).url("https://k4608x0632.imdo.co/android/cart.php").build();
            Call call = okHttpClient.newCall(request);

            try {
                Response response = call.execute();
                String responseString = response.body().string();

                jsonObject = JSONObject.parseObject(responseString);
                jsonArray = jsonObject.getJSONArray("shop");

                return jsonObject.getBoolean("success");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (!aBoolean)
                Toast.makeText(getActivity(), "我们遇到了未知错误", Toast.LENGTH_LONG).show();
            List<JSONObject> jsonObjects = JSON.parseArray(jsonArray.toJSONString(), JSONObject.class);
            jsonObjects.forEach(jsonObject1 -> {
                View v = layoutInflater.inflate(R.layout.linear_car_item, linearLayout, false);

                txt_name = v.findViewById(R.id.CF_txt_name);
                txt_title = v.findViewById(R.id.CF_txt_title);
                txt_mode = v.findViewById(R.id.CF_txt_mode);
                txt_money = v.findViewById(R.id.CF_txt_money);
                imageView = v.findViewById(R.id.CF_img);

                txt_name.setText(jsonObject1.getString("c_name"));
                txt_title.setText(jsonObject1.getString("c_area"));
                txt_mode.setText(jsonObject1.getString("c_name"));
                txt_money.setText("￥" + jsonObject1.getString("c_price"));
                imageView.setImageURL(jsonObject1.getString("c_pic"));

                linearLayout.addView(v);
            });

        }
    }
}
