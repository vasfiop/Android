package com.example.thegame.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.example.thegame.R;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Login_Activity extends AppCompatActivity {
    private EditText editText_name, editText_password; // 用户名控件 密码控件
    private Button btn_login, btn_register; // 登陆按钮 注册按钮
    private ImageButton imgbtn_back; // 返回按钮

    private final String TAG = "Login_Activity"; // 测试语句

    private String name;
    private String password;

    private final OkHttpClient okHttpClient = new OkHttpClient();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        editText_name = findViewById(R.id.LA_edittxt_username);
        editText_password = findViewById(R.id.LA_edittxt_password);
        btn_login = findViewById(R.id.LA_btn_login);
        imgbtn_back = findViewById(R.id.LA_imgbtn_back);
        btn_register = findViewById(R.id.LA_btn_register);

        sharedPreferences = this.getSharedPreferences("user", MODE_PRIVATE);

//        登陆事件
        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                name = editText_name.getText().toString();
                password = editText_password.getText().toString();

                if (name.equals(""))
                    Toast.makeText(Login_Activity.this, "邮箱不可为空！", Toast.LENGTH_LONG).show();
                else {
                    if (!checkEmail())
                        Toast.makeText(Login_Activity.this, "请输入合法邮箱!", Toast.LENGTH_LONG).show();
                    else if (password.equals(""))
                        Toast.makeText(Login_Activity.this, "密码不可为空！", Toast.LENGTH_LONG).show();
                    else
                        new MyTask().execute();
                }
            }
        });

//        注册事件
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = editText_name.getText().toString();
                password = editText_password.getText().toString();

                if (name.equals(""))
                    Toast.makeText(Login_Activity.this, "邮箱不可为空！", Toast.LENGTH_LONG).show();
                else {
                    if (!checkEmail())
                        Toast.makeText(Login_Activity.this, "请输入合法邮箱!", Toast.LENGTH_LONG).show();
                    else if (password.equals(""))
                        Toast.makeText(Login_Activity.this, "密码不可为空！", Toast.LENGTH_LONG).show();
                    else
                        new Register().execute();
                }
            }
        });

//        返回事件
        imgbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //    AsyncTask多线程 负责注册
    class Register extends AsyncTask<String, String, Integer> {
        JSONObject jsonObject;
        private final int SERVER_ERROR = 500;

        @Override
        protected Integer doInBackground(String... strings) {
            FormBody formBody = new FormBody.Builder().add("u_email", name).add("u_password", password).build();
            Request request = new Request.Builder().post(formBody).url("https://k4608x0632.imdo.co/android/doRegister.php").build();
            Call call = okHttpClient.newCall(request);

            try {
                Response response = call.execute();
                String responseString = response.body().string();

                jsonObject = JSONObject.parseObject(responseString);

                return response.code();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return SERVER_ERROR;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            switch (integer / 100) {
                case 1:
                case 2:
                case 3:
                    Intent intent = new Intent();
                    Map<String, Object> map = jsonObject.getJSONObject("0").getInnerMap();
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        intent.putExtra(entry.getKey(), String.valueOf(entry.getValue()));
                    }
                    setResult(RESULT_OK, intent);

                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        editor.putString(entry.getKey(), (String) entry.getValue());
                    }
                    editor.commit();

                    finish();
                    break;
                case 4:
                    Toast.makeText(Login_Activity.this, "客户端错误！", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    Toast.makeText(Login_Activity.this, "服务器错误！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    // AsyncTask多线程 负责登陆部分
    class MyTask extends AsyncTask<String, String, Integer> {
        JSONObject jsonObject;
        private final int SERVER_ERROR = 500;

        @Override
        protected Integer doInBackground(String... strings) {
            // 运行于子线程
            // okHttp
            FormBody formBody = new FormBody.Builder().add("u_email", name).add("u_password", password).build();
            Request request = new Request.Builder().post(formBody).url("https://k4608x0632.imdo.co/android/doLogin.php").build();
            Call call = okHttpClient.newCall(request);

            try {
                Response response = call.execute();
                String responseString = response.body().string();

                jsonObject = JSONObject.parseObject(responseString);

                return response.code();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return SERVER_ERROR;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            // 当doIn执行完毕后系统会自动调用这个函数，并且doin方法的返回值是该函数的参数
            super.onPostExecute(integer);

            switch (integer / 100) {
                case 1:
                case 2:
                case 3:
                    Intent intent = new Intent();
                    Map<String, Object> map = jsonObject.getJSONObject("0").getInnerMap();
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        intent.putExtra(entry.getKey(), String.valueOf(entry.getValue()));
                    }
                    setResult(RESULT_OK, intent);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        editor.putString(entry.getKey(), (String) entry.getValue());
                    }
                    editor.commit();

                    finish();
                    break;
                case 4:
                    Toast.makeText(Login_Activity.this, "客户端错误！", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    Toast.makeText(Login_Activity.this, "服务器错误！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private boolean checkEmail() {
        boolean success = name.equals("@") && name.equals(".");
        String[] array = name.split("\\.");
        success = array.length != 1;

        return success;
    }
}
