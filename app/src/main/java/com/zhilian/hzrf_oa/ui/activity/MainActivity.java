package com.zhilian.hzrf_oa.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.david.gradientuilibrary.GradientIconView;
import com.david.gradientuilibrary.GradientTextView;
import com.zhilian.hzrf_oa.common.BusinessContant;
import com.zhilian.hzrf_oa.service.DocsendService;
import com.zhilian.hzrf_oa.service.InnerSendService;
import com.zhilian.hzrf_oa.service.ReceiveService;
import com.zhilian.hzrf_oa.ui.fragment.AboutMeFragment;
import com.zhilian.hzrf_oa.ui.fragment.ChatsFragment;
import com.zhilian.hzrf_oa.ui.fragment.ContactsFragment;
import com.zhilian.hzrf_oa.ui.fragment.DiscoverFragment;
import com.zhilian.hzrf_oa.ui.fragment.OnFragmentInteractionListener;
import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.ui.widget.CustomDialog;
import com.zhilian.hzrf_oa.update.UpdateService;
import com.zhilian.hzrf_oa.update.UpdateVersion;
import com.zhilian.hzrf_oa.util.PollingUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhilian.api.InQueryMsg;
import com.zhilian.api.JsonStringRequest;
import com.zhilian.api.RequestUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 主界面
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener,
        ViewPager.OnPageChangeListener, OnFragmentInteractionListener {

    BusinessContant bc = new BusinessContant();
    private ViewPager mViewPager;
    private List<Fragment> mTabs = new ArrayList<Fragment>();
    private FragmentPagerAdapter mAdapter;

    private List<GradientIconView> mTabIconIndicator = new ArrayList<GradientIconView>();
    private List<GradientTextView> mTabTextIndicator = new ArrayList<GradientTextView>();
    private GradientIconView mChatsIconView;
    private GradientIconView mContactsIconView;
    private GradientIconView mDiscoverIconView;
    private GradientIconView mAboutMeIconView;
    private GradientTextView mTvChats;
    private GradientTextView mTvContacts;
    private GradientTextView mTvDiscover;
    private GradientTextView mTvAboutMe;

    private static final int REQUEST_WRITE_STORAGE = 111;
    private CustomDialog mDialog = null;// 版本更新的弹窗
    private String DownUrl = null;// 版本更新Url地址
    String uname;// 用户名
    String limit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Bundle bundle = getIntent().getBundleExtra("userData");
        Bundle bundle = getIntent().getExtras();
        limit = bundle.getString("limit");
        uname = bundle.getString("uname");
        initView();// 初始化控件
        chechVersion();// 检测版本更新
        PollingUtils.startPollingService(this, 10, ReceiveService.class, ReceiveService.ACTION);
        PollingUtils.startPollingService(this, 10, InnerSendService.class, InnerSendService.ACTION);
        PollingUtils.startPollingService(this, 10, DocsendService.class, DocsendService.ACTION);
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

        mChatsIconView = (GradientIconView) findViewById(R.id.id_iconfont_chat);
        mChatsIconView.setOnClickListener(this);
        mTabIconIndicator.add(mChatsIconView);
        mChatsIconView.setIconAlpha(1.0f);

        mContactsIconView = (GradientIconView) findViewById(R.id.id_iconfont_friend);
        mContactsIconView.setOnClickListener(this);
        mTabIconIndicator.add(mContactsIconView);

        mDiscoverIconView = (GradientIconView) findViewById(R.id.id_iconfont_faxian);
        mDiscoverIconView.setOnClickListener(this);
        mTabIconIndicator.add(mDiscoverIconView);

        mAboutMeIconView = (GradientIconView) findViewById(R.id.id_iconfont_me);
        mAboutMeIconView.setOnClickListener(this);
        mTabIconIndicator.add(mAboutMeIconView);

        mTvChats = (GradientTextView) findViewById(R.id.id_chats_tv);
        mTvChats.setOnClickListener(this);
        mTabTextIndicator.add(mTvChats);
        mTvChats.setTextViewAlpha(1.0f);

        mTvContacts = (GradientTextView) findViewById(R.id.id_contacts_tv);
        mTvContacts.setOnClickListener(this);
        mTabTextIndicator.add(mTvContacts);

        mTvDiscover = (GradientTextView) findViewById(R.id.id_discover_tv);
        mTvDiscover.setOnClickListener(this);
        mTabTextIndicator.add(mTvDiscover);

        mTvAboutMe = (GradientTextView) findViewById(R.id.id_about_me_tv);
        mTvAboutMe.setOnClickListener(this);
        mTabTextIndicator.add(mTvAboutMe);

        initFragments();
    }

    private void initFragments() {
        mTabs.add(ChatsFragment.newInstance("", ""));
        mTabs.add(DiscoverFragment.newInstance(limit, ""));
        mTabs.add(ContactsFragment.newInstance("", ""));
        mTabs.add(AboutMeFragment.newInstance("", ""));

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return mTabs.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return mTabs.get(arg0);
            }
        };

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(this);
    }

    /**
     * 重置其他的Tab
     */
    private void resetOtherTabs() {
        resetOtherTabIcons();
        resetOtherTabText();
    }

    /**
     * 重置其他的Tab icon
     */
    private void resetOtherTabIcons() {
        for (int i = 0; i < mTabIconIndicator.size(); i++) {
            mTabIconIndicator.get(i).setIconAlpha(0);
        }
    }

    /**
     * 重置其他的Tab text
     */
    private void resetOtherTabText() {
        for (int i = 0; i < mTabTextIndicator.size(); i++) {
            mTabTextIndicator.get(i).setTextViewAlpha(0);
        }
    }

    @Override
    public void onClick(View v) {
        resetOtherTabs();
        switch (v.getId()) {
            case R.id.id_iconfont_chat:
            case R.id.id_chats_tv:
                mTabIconIndicator.get(0).setIconAlpha(1.0f);
                mTabTextIndicator.get(0).setTextViewAlpha(1.0f);
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.id_iconfont_friend:
            case R.id.id_contacts_tv:
                mTabIconIndicator.get(1).setIconAlpha(1.0f);
                mTabTextIndicator.get(1).setTextViewAlpha(1.0f);
                mViewPager.setCurrentItem(1, false);
                break;
            case R.id.id_iconfont_faxian:
            case R.id.id_discover_tv:
                mTabIconIndicator.get(2).setIconAlpha(1.0f);
                mTabTextIndicator.get(2).setTextViewAlpha(1.0f);
                mViewPager.setCurrentItem(2, false);
                break;
            case R.id.id_iconfont_me:
            case R.id.id_about_me_tv:
                mTabIconIndicator.get(3).setIconAlpha(1.0f);
                mTabTextIndicator.get(3).setTextViewAlpha(1.0f);
                mViewPager.setCurrentItem(3, false);
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset > 0) {
            GradientIconView iconLeft = mTabIconIndicator.get(position);
            GradientIconView iconRight = mTabIconIndicator.get(position + 1);

            GradientTextView textLeft = mTabTextIndicator.get(position);
            GradientTextView textRight = mTabTextIndicator.get(position + 1);

            iconLeft.setIconAlpha(1 - positionOffset);
            textLeft.setTextViewAlpha(1 - positionOffset);
            iconRight.setIconAlpha(positionOffset);
            textRight.setTextViewAlpha(positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 检测版本更新
     */
    private void chechVersion() {

        String key = bc.getCONFIRM_ID();
        String url = bc.URL;
        InQueryMsg inQueryMsg = new InQueryMsg(1348831860, "query", key);
        inQueryMsg.setQueryName("updateVersion");
        HashMap<String, String> map = new HashMap<>();
        map.put("app_type", String.valueOf(1));// 这里传的是类型 0: ios  1: android
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
                            bc.APP_VERSION = model.getVersion_code();
                            int force_update = model.getForce_update();// 强制更新
                            // 判断服务版本号是否大于当前版本号
                            int version_code = Integer.parseInt(app_version.substring(app_version.length()-1));
                            if(version_code > BusinessContant.localVersion){
                                mDialog = new CustomDialog(MainActivity.this);
                                mDialog.setTitle("发现新版本");
                                mDialog.setContent(version_desc);// 更新说明
                                mDialog.setOKButton("立即更新", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //请求存储权限
                                        boolean hasPermission = (ContextCompat.checkSelfPermission(MainActivity.this,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                                        if (!hasPermission) {
                                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
                                            ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                        } else {
                                            //下载
                                            Intent it = new Intent(MainActivity.this, UpdateService.class);
                                            it.putExtra("apkUrl", DownUrl);
                                            startService(it);
                                            mDialog.dismiss();
                                        }

                                    }
                                });
                                if (force_update ==0){
                                    mDialog.setCancelButton("下次再说", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mDialog.dismiss();
                                        }
                                    });
                                }
                                mDialog.show();
                            }else{
                                System.out.println("当前已是最新版本！");
                                //Toast.makeText(MainActivity.this,"当前已是最新版本！",Toast.LENGTH_LONG).show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PollingUtils.stopPollingService(this, ReceiveService.class, ReceiveService.ACTION);
        PollingUtils.stopPollingService(this, InnerSendService.class, InnerSendService.ACTION);
        PollingUtils.stopPollingService(this, DocsendService.class, DocsendService.ACTION);
    }
}
