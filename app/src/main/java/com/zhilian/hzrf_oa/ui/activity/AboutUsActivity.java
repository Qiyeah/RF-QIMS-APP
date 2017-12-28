package com.zhilian.hzrf_oa.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.zhilian.api.StrKit;
import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.common.BusinessContant;
import com.zhilian.hzrf_oa.ui.widget.CustomDialog;
import com.zhilian.hzrf_oa.update.UpdateService;
import com.zhilian.hzrf_oa.update.UpdateVersion;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhilian.api.InQueryMsg;
import com.zhilian.api.JsonStringRequest;
import com.zhilian.api.RequestUtil;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by root on 2015/9/9.
 * 关于我们
 */
public class AboutUsActivity extends Activity implements View.OnClickListener {
    private TextView id_help, id_contact_us, id_feedback, id_update,tv_version;
    private Button btn_view;
    private String DownUrl = null;

    private static final int REQUEST_WRITE_STORAGE = 111;
    private CustomDialog mDialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //actionbar();
        init();
    }

    /*private void actionbar() {
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeAsUpIndicator(R.drawable.icon_left);
    }*/

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        switch (id) {
//            case android.R.id.home:
//                finish();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void init() {
        id_help = (TextView) findViewById(R.id.id_help);
        id_help.setOnClickListener(this);
        id_contact_us = (TextView) findViewById(R.id.id_contact_us);
        id_contact_us.setOnClickListener(this);
        id_feedback = (TextView) findViewById(R.id.id_feedback);
        id_feedback.setOnClickListener(this);
        id_update = (TextView) findViewById(R.id.id_update);
        id_update.setOnClickListener(this);
        String app_version = new BusinessContant().APP_VERSION;
        if (StrKit.notBlank(app_version)){
            tv_version = (TextView)findViewById(R.id.tv_version);
            tv_version.setText("人防办OA V"+app_version);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_help:// 使用帮助
                /*Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", AppConfig.API_IMAGE_URL+"/rfweb/core/api/page/newsread?seq=2");
                intent.putExtra("title", "使用帮助");
                startActivity(intent);*/
                break;
            case R.id.id_contact_us:// 联系我们
                startActivity(new Intent(this, ContactActivity.class));
                break;
            case R.id.id_feedback:// 意见反馈
                //startActivity(new Intent(this, FeedbackActivity.class));
                break;
            case R.id.id_update:// 检查更新
                chechVersion();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //获取到存储权限,进行下载
                    //startDownload();
                    Intent it = new Intent(AboutUsActivity.this, UpdateService.class);
                    it.putExtra("apkUrl", DownUrl);
                    startService(it);
                    mDialog.dismiss();
                } else {
                    Toast.makeText(AboutUsActivity.this, "不授予存储权限将无法进行下载!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

	/**
     * 检测版本更新
     */
    private void chechVersion() {
        BusinessContant bc = new BusinessContant();
        String key = bc.getCONFIRM_ID();
        String url = bc.URL;
        InQueryMsg inQueryMsg = new InQueryMsg(1348831860, "query", key);
        inQueryMsg.setQueryName("updateVersion");
        HashMap<String, String> map = new HashMap<>();
        map.put("app_type", String.valueOf(1));
        inQueryMsg.setQueryPara(map);
        String postData = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            postData = mapper.writeValueAsString(inQueryMsg);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println("发送前的明文：" + postData);

        RequestQueue requestQueue = RequestUtil.getRequestQueue();

        JsonRequest jsonRequest = new JsonStringRequest(Request.Method.POST, url, postData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", "response -> " + response.toString());
                        System.out.println("解密后：" + response.toString());

                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.configure(
                                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        try {
                            UpdateVersion model = objectMapper.readValue(response.toString(), UpdateVersion.class);

                            DownUrl = model.getDown_url();// url地址
                            String version_desc = model.getVersion_desc();// 更新说明
                            String app_version = model.getApp_version();// 版本号
                            int force_update = model.getForce_update();

                            int version_code = Integer.parseInt(app_version.substring(app_version.length()-1));
                            if(version_code > BusinessContant.localVersion){
                                mDialog = new CustomDialog(AboutUsActivity.this);
                                mDialog.setTitle("发现新版本");
                                mDialog.setContent("为了给大家提供更好的用户体验，每次应用的更新都包含速度和稳定性的提升，感谢您的使用！");
                                mDialog.setOKButton("立即更新", new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        //请求存储权限
                                        boolean hasPermission = (ContextCompat.checkSelfPermission(AboutUsActivity.this,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                                        if (!hasPermission) {
                                            ActivityCompat.requestPermissions(AboutUsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
                                            ActivityCompat.shouldShowRequestPermissionRationale(AboutUsActivity.this,
                                                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                        } else {
                                            //下载
                                            //startDownload();
                                            Intent it = new Intent(AboutUsActivity.this, UpdateService.class);
                                            it.putExtra("apkUrl", DownUrl);
                                            startService(it);
                                            mDialog.dismiss();
                                        }

                                    }
                                });
                                mDialog.setCancelButton("下次再说", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mDialog.dismiss();
                                    }
                                });
                                mDialog.show();
                            }else{
                                Toast.makeText(AboutUsActivity.this,"当前已是最新版本！",Toast.LENGTH_LONG).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                Toast.makeText(getApplicationContext() , "出错了!", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonRequest);

    }


    /**
     * 启动下载
     */
    /*private void startDownload() {
        Intent it = new Intent(AboutUsActivity.this, UpdateService.class);
        //下载地址
        it.putExtra("apkUrl", APK_DOWNLOAD_URL);
        startService(it);
        mDialog.dismiss();
    }*/

}
