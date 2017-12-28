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
import com.zhilian.hzrf_oa.ui.fragment.OnFragmentInteractionListener;
import com.zhilian.hzrf_oa.ui.fragment.ReceiveDoneFragment;
import com.zhilian.hzrf_oa.ui.fragment.ReceiveTodoFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-1-20.
 */
public class ReceiveManageActivity  extends FragmentActivity implements View.OnClickListener,ViewPager.OnPageChangeListener, OnFragmentInteractionListener {
    private View todoLayout;// 切换到未审批
    private View doneLayout;// 切换到已审批

    private TextView title;//大标题
    private TextView todo;//fragment标题1发
    private TextView done;//fragment标题2
    private ViewPager mViewPager;
    private List<Fragment> mTabs = new ArrayList<Fragment>();
    private FragmentPagerAdapter mAdapter;

    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_cabinet_layout);
        title = (TextView)findViewById(R.id.title) ;
        title.setText("收文管理");
        todo=(TextView)findViewById(R.id.base_info) ;
        todo.setText("未批办");
        done=(TextView)findViewById(R.id.device_list) ;
        done.setText("已批办");
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

        todoLayout = findViewById(R.id.base_info);
        doneLayout = findViewById(R.id.device_list);
        todoLayout.setOnClickListener(this);
        doneLayout.setOnClickListener(this);

        todoLayout.setBackgroundColor(Color.parseColor("#99CCFF"));
        initFragments();
    }

    private void initFragments() {

        mTabs.add(ReceiveTodoFragment.newInstance("", ""));
        mTabs.add(ReceiveDoneFragment.newInstance("", ""));

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
            todoLayout.setBackgroundColor(Color.parseColor("#99CCFF"));
            doneLayout.setBackgroundColor(0xffffffff);
        }else if(position == 1){
            doneLayout.setBackgroundColor(Color.parseColor("#99CCFF"));
            todoLayout.setBackgroundColor(0xffffffff);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        clearSelection();
        switch (v.getId()){
            case R.id.base_info:// 收文归档
                mViewPager.setCurrentItem(0, false);
                todoLayout.setBackgroundColor(Color.parseColor("#99CCFF"));
                break;
            case R.id.device_list:// 发文归档
                mViewPager.setCurrentItem(1, false);
                doneLayout.setBackgroundColor(Color.parseColor("#99CCFF"));
                break;
        }
    }

    // 清除掉所有的选中状态
    private void clearSelection(){
        todoLayout.setBackgroundColor(0xffffffff);
        doneLayout.setBackgroundColor(0xffffffff);
    }
}
