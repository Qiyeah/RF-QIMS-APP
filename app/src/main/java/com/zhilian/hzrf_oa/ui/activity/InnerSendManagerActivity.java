package com.zhilian.hzrf_oa.ui.activity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.ui.fragment.InnerSendReadFragment;
import com.zhilian.hzrf_oa.ui.fragment.InnerSendUnreadFragment;
import com.zhilian.hzrf_oa.ui.fragment.OnFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 新增界面：内部发文
 *
 * Created by Administrator on 2017-9-12.
 */
public class InnerSendManagerActivity  extends FragmentActivity implements View.OnClickListener,ViewPager.OnPageChangeListener, OnFragmentInteractionListener {
    private View unReadLayout;// 切换到未读发文
    private View readLayout;// 切换到已读发文

    private TextView title;//大标题
    private TextView unRead;//fragment标题1发
    private TextView read;//fragment标题2
    private ViewPager mViewPager;
    private List<Fragment> mTabs = new ArrayList<Fragment>();
    private FragmentPagerAdapter mAdapter;

    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_cabinet_layout);
        title = (TextView)findViewById(R.id.title) ;
        title.setText("内部发文");
        unRead =(TextView)findViewById(R.id.base_info) ;
        unRead.setText("未读发文");
        read =(TextView)findViewById(R.id.device_list) ;
        read.setText("已读发文");
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initView();// 初始化控件
    }

    private void initView(){
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

        unReadLayout = findViewById(R.id.base_info);
        readLayout = findViewById(R.id.device_list);
        unReadLayout.setOnClickListener(this);
        readLayout.setOnClickListener(this);

        unReadLayout.setBackgroundColor(Color.parseColor("#99CCFF"));
        initFragments();
    }
    private void initFragments() {

        mTabs.add(InnerSendUnreadFragment.newInstance("", ""));
        mTabs.add(InnerSendReadFragment.newInstance("", ""));

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
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(position == 0){
            unReadLayout.setBackgroundColor(Color.parseColor("#99CCFF"));
            readLayout.setBackgroundColor(0xffffffff);
//            InnerSendUnreadFragment fragment=  (InnerSendUnreadFragment)(mTabs.get(position));
//            fragment.reloadData();
        }else if(position == 1){
            readLayout.setBackgroundColor(Color.parseColor("#99CCFF"));
            unReadLayout.setBackgroundColor(0xffffffff);
//            InnerSendReadFragment fragment=  (InnerSendReadFragment)(mTabs.get(position));
//            fragment.reloadData();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        clearSelection();
        switch (v.getId()){
            case R.id.base_info:// 未读发文
                mViewPager.setCurrentItem(0, false);
                unReadLayout.setBackgroundColor(Color.parseColor("#99CCFF"));
                break;
            case R.id.device_list:// 已读发文
                mViewPager.setCurrentItem(1, false);
                readLayout.setBackgroundColor(Color.parseColor("#99CCFF"));
                break;
        }
    }

    // 清除掉所有的选中状态
    private void clearSelection(){
        unReadLayout.setBackgroundColor(0xffffffff);
        readLayout.setBackgroundColor(0xffffffff);
    }
}
