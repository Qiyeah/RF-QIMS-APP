package com.zhilian.hzrf_oa.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.common.BusinessContant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 名称：登录页面（Activity）
 * 描述：这里是用户登录的页面
 * 功能：1、输入用户名和密码，点击登录后，登录
 * 2、注册账号：点击后，跳转到注册账号的页面
 * 3、忘记密码：点击后，跳转到忘记密码的页面
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    String uname;// 进入系统得到用户名
    String limit;//用户权限级别
    private TextView tv_forgetPwd;// 忘记密码
    private EditText et_user;// 输入用户名
    private EditText et_password;// 输入密码
    private Button login_button;// 登录

    ImageView clear1;// 清除账号
    ImageView clear2;// 清除密码
    ImageView eye;// 查看密码
    private boolean flag = false;
    private SharedPreferences sp;// 保存登录账号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        init();// 初始化
    }

    // 初始化控件
    private void init() {
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        final String name = sp.getString("name", "");
        final String password = sp.getString("password", "");
        //Log.e("Response", "onClick: "+sp.getString("password",""));

        if (!name.isEmpty() && !password.isEmpty()) {
            new Thread() {
                public void run() {
                    try {
                        if (confirmUser(name, password)) {
                            //Log.i("Login","正确");
                            Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("uname", uname);
                            bundle.putString("limit", limit);
                            intent1.putExtras(bundle);
                            startActivity(intent1);
                            finish();
                        } else {
                            //Log.i("Login","错误");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

        // 忘记密码
        tv_forgetPwd = (TextView) findViewById(R.id.loginForgetPwd);
        tv_forgetPwd.setOnClickListener(this);

        // 输入用户名
        et_user = (EditText) findViewById(R.id.loginUser);
        et_user.setOnClickListener(this);
        et_user.setText(name);

        et_user.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    clear1.setVisibility(View.GONE);
                }
            }
        });
        et_user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    clear1.setVisibility(View.GONE);
                } else {
                    clear1.setVisibility(View.VISIBLE);
                }
            }
        });
        // 输入密码
        et_password = (EditText) findViewById(R.id.loginPassword);
        et_password.setOnClickListener(this);
        et_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    clear2.setVisibility(View.GONE);
                }
            }
        });
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    clear2.setVisibility(View.GONE);
                } else {
                    clear2.setVisibility(View.VISIBLE);
                }
            }
        });
        // 登录 zhilian199
        login_button = (Button) findViewById(R.id.loginButton);
        login_button.setOnClickListener(this);

        clear1 = (ImageView) findViewById(R.id.clear1);
        clear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_user.setText("");
            }
        });
        clear2 = (ImageView) findViewById(R.id.clear2);
        clear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_password.setText("");
            }
        });
        eye = (ImageView) findViewById(R.id.eye);
        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == false) {
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    eye.setImageResource(R.drawable.icon_eye2);
                    flag = true;
                } else {
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    eye.setImageResource(R.drawable.icon_eye1);
                    flag = false;
                }
            }
        });
    }

    // 设置监听事件
    @Override
    public void onClick(View v) {
        final String user_id = et_user.getText().toString();
        final String pwd = et_password.getText().toString();
        switch (v.getId()) {
            case R.id.loginButton:// 登录
                String name = et_user.getText().toString();// 拿到edittext中的数据
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("name", name);
                editor.putString("password", pwd);
                editor.commit();
                if (user_id.equals("") && pwd.equals("") && user_id.trim().equals("") && pwd.trim().equals("")) {
                    //login_button.setEnabled(false);
                    Toast.makeText(this, "请输入账号和密码！", Toast.LENGTH_SHORT).show();
                } else if (user_id.equals("") && !pwd.trim().equals("")) {
                    Toast.makeText(this, "请输入账号！", Toast.LENGTH_SHORT).show();
                } else if (pwd.trim().equals("") && !user_id.trim().equals("")) {
                    Toast.makeText(this, "请输入密码！", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread() {
                        public void run() {
                            try {
                                if (confirmUser(user_id, pwd)) {
                                    //Log.i("Login","正确");
                                    Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("uname", uname);
                                    bundle.putString("limit",limit);
                                    //intent1.putExtra("userData",bundle);
                                    intent1.putExtras(bundle);
                                    startActivity(intent1);
                                    finish();
                                } else {
                                    //Log.i("Login","错误");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
                break;
            case R.id.loginForgetPwd:// 忘记密码
//                Intent intent3 = new Intent(LoginActivity.this,ForgetPassword.class);
//                startActivity(intent3);
                break;
        }
    }

    public boolean confirmUser(String user_id, String pwd) {
        BusinessContant bc = new BusinessContant();
        boolean is_exist = false;
        String target = "";
        target = bc.URL + "/Mobile/login/" + user_id + "-" + pwd;
        try {       //要访问的URL地址
            URL url;
            url = new URL(target);

            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();  //创建一个HTTP连接

            urlConn.connect();
            if (urlConn.getResponseCode() == 200) {
                Log.i("ss", "Get方式请求成功");
            } else {
                Log.i("ss", "Get方式请求失败");
            }
            InputStream in = urlConn.getInputStream(); // 获得读取的内容
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "/n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                JSONObject dataJson = new JSONObject(sb.toString());
                /*JSONObject response = dataJson.getJSONObject("response");
                JSONArray data = response.getJSONArray("data");
                JSONObject info=data.getJSONObject(0);*/
                String status = dataJson.getString("status");
                String msg = dataJson.getString("msg");
                limit = dataJson.getString("startReceive");//服务器发出：0，无权限，1，有权限

                if (status.equals("1")) {
                    is_exist = true;
                    bc.setCONFIRM_ID(msg);
                    uname = dataJson.getString("uname");
                    Log.i("sss", "sss");
                } else {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    Looper.loop();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            in.close(); //关闭字符输入流对象
            urlConn.disconnect();   //断开连接
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return is_exist;
    }

    // 点击屏幕关闭软键盘
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        return super.onTouchEvent(event);
    }

}

