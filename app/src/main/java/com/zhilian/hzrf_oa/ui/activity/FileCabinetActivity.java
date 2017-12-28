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

import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.ui.fragment.DocsendCabFragment;
import com.zhilian.hzrf_oa.ui.fragment.OnFragmentInteractionListener;
import com.zhilian.hzrf_oa.ui.fragment.ReceiveCabFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/3.
 * 档案柜
 */
public class FileCabinetActivity extends FragmentActivity implements View.OnClickListener,ViewPager.OnPageChangeListener, OnFragmentInteractionListener {
	private View contractBaseInfoLayout;// 切换到发文归档
	private View contractDeviceListLayout;// 切换到收文归档

	private ViewPager mViewPager;
	private List<Fragment> mTabs = new ArrayList<Fragment>();
	private FragmentPagerAdapter mAdapter;

	private PopupWindow popupWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_cabinet_layout);

		ImageView back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		initView();// 初始化控件
	}

	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

		contractBaseInfoLayout = findViewById(R.id.base_info);
		contractDeviceListLayout = findViewById(R.id.device_list);
		contractBaseInfoLayout.setOnClickListener(this);
		contractDeviceListLayout.setOnClickListener(this);

		contractBaseInfoLayout.setBackgroundColor(Color.parseColor("#99CCFF"));
		initFragments();
	}

	private void initFragments() {
		mTabs.add(ReceiveCabFragment.newInstance("", ""));
		mTabs.add(DocsendCabFragment.newInstance("", ""));

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
			contractBaseInfoLayout.setBackgroundColor(Color.parseColor("#99CCFF"));
			contractDeviceListLayout.setBackgroundColor(0xffffffff);
		}else if(position == 1){
			contractDeviceListLayout.setBackgroundColor(Color.parseColor("#99CCFF"));
			contractBaseInfoLayout.setBackgroundColor(0xffffffff);
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
				contractBaseInfoLayout.setBackgroundColor(Color.parseColor("#99CCFF"));
				break;
			case R.id.device_list:// 发文归档
				mViewPager.setCurrentItem(1, false);
				contractDeviceListLayout.setBackgroundColor(Color.parseColor("#99CCFF"));
				break;
		}
	}

	// 清除掉所有的选中状态
	private void clearSelection(){
		contractBaseInfoLayout.setBackgroundColor(0xffffffff);
		contractDeviceListLayout.setBackgroundColor(0xffffffff);
	}

}
