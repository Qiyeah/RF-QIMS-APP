package com.zhilian.hzrf_oa.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.common.BusinessContant;
import com.zhilian.hzrf_oa.ui.activity.MyMailActivity;
import com.zhilian.hzrf_oa.ui.activity.WriteLetterActivity;

/**
 * 名称：审图中的侧滑栏（工程审图）
 * 描述：显示工程基本信息栏、总设备清单栏、防护单元列表和口部列表
 * 功能：1、点击对应栏目，跳转到查看对应栏目信息的页面
 *       2、添加防护单元：点击后，跳转到添加防护单元信息页面
 */
public class MyMailSliding extends Fragment implements OnClickListener{
	private MyMailActivity myMailActivity;
	private TextView project_name;// 工程名称
	private LinearLayout base_info;// 写信
	private LinearLayout device_list;// 收件箱
	private LinearLayout device_list2;// 发件箱
	private LinearLayout device_list3;// 草稿箱
	private LinearLayout device_list4;// 垃圾箱
	BusinessContant bc=new BusinessContant();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_menu, null);
		myMailActivity = (MyMailActivity) getActivity();

		initView(view);// 初始化控件

		return view;
	}

	// 初始化控件
	private void initView(View view) {
		// 写信
		base_info = (LinearLayout) view.findViewById(R.id.base_info);
		base_info.setOnClickListener(this);
		// 收件箱
		device_list = (LinearLayout) view.findViewById(R.id.device_list);
		device_list.setOnClickListener(this);
		// 发件箱
		device_list2 = (LinearLayout) view.findViewById(R.id.device_list2);
		device_list2.setOnClickListener(this);
		// 草稿箱
		device_list3 = (LinearLayout) view.findViewById(R.id.device_list3);
		device_list3.setOnClickListener(this);
		// 垃圾箱
		device_list4 = (LinearLayout) view.findViewById(R.id.device_list4);
		device_list4.setOnClickListener(this);
	}

	// 事件监听
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.base_info:// 写信
				bc.setModel(null);
				Intent intent = new Intent(myMailActivity, WriteLetterActivity.class);
				startActivity(intent);
				break;
			case R.id.device_list:// 收件箱
				InboxFragment fragment = new InboxFragment();
				Bundle bundle = new Bundle();
				bundle.putString("sss","getReceiveMailList");
				fragment.setArguments(bundle);
				switchFragment(fragment,null);
				break;
			case R.id.device_list2:// 发件箱
				InboxFragment fragment2 = new InboxFragment();
				Bundle bundle2 = new Bundle();
				bundle2.putString("sss","getSendList");
				fragment2.setArguments(bundle2);
				switchFragment(fragment2,null);
				break;
			case R.id.device_list3:// 草稿箱
				InboxFragment fragment3 = new InboxFragment();
				Bundle bundle3 = new Bundle();
				bundle3.putString("sss","getDraftList");
				fragment3.setArguments(bundle3);
				switchFragment(fragment3,null);
				break;
			case R.id.device_list4:// 垃圾箱
				InboxFragment fragment4 = new InboxFragment();
				Bundle bundle4 = new Bundle();
				bundle4.putString("sss","getRubbishList");
				fragment4.setArguments(bundle4);
				switchFragment(fragment4,null);
				break;
		}
	}

	// 切换fragment
	private void switchFragment(Fragment fragment, String title) {
		if (getActivity() == null) {
			return;
		}
		if (getActivity() instanceof MyMailActivity) {
			MyMailActivity fca = (MyMailActivity) getActivity();
			fca.switchConent(fragment, title);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
