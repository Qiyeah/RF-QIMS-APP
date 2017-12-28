package com.zhilian.hzrf_oa.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.ui.fragment.InboxFragment;
import com.zhilian.hzrf_oa.ui.fragment.MyMailSliding;
import com.zhilian.hzrf_oa.ui.widget.slidingmenu.SlidingMenu;
import com.zhilian.hzrf_oa.ui.widget.slidingmenu.app.SlidingFragmentActivity;

/**
 * Created by YiFan
 * 我的邮件
 */
public class MyMailActivity extends SlidingFragmentActivity implements View.OnClickListener{
	private Fragment mContent;// Fragment页面

	private ImageView back;// 返回
	private ImageView menu;// 菜单
	private TextView top_title;// 头部标题

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_mail_layout);

		initView();// 初始化

		// 默认显示收件箱
		InboxFragment fragment = new InboxFragment();
		Bundle bundle = new Bundle();
		bundle.putString("sss","getReceiveMailList");
		fragment.setArguments(bundle);
		switchConent(fragment,null);

		initSlidingMenu(savedInstanceState);// 初始化侧滑栏
	}

	private void initView() {
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(this);
		menu = (ImageView) findViewById(R.id.menu_image);
		menu.setOnClickListener(this);
	}

	// 初始化侧滑栏
	private void initSlidingMenu(Bundle savedInstanceState) {
		// 如果保存的状态不为空则得到之前保存的Fragment，否则实例化MyFragment
		if (savedInstanceState != null) {
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		}

		if (mContent == null) {
			mContent = new InboxFragment();
		}

		// 设置左侧滑动菜单
		setBehindContentView(R.layout.menu_frame_left);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new MyMailSliding()).commit();

		// 实例化滑动菜单对象
		SlidingMenu sm = getSlidingMenu();
		// 设置可以左右滑动的菜单
		sm.setMode(SlidingMenu.LEFT);
		// 设置滑动阴影的宽度
		sm.setShadowWidthRes(R.dimen.shadow_width);
		// 设置滑动菜单阴影的图像资源
		sm.setShadowDrawable(null);
		// 设置滑动菜单视图的宽度
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置渐入渐出效果的值
		sm.setFadeDegree(0.35f);
		// 设置触摸屏幕的模式,这里设置为全屏
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		// 设置下方视图的在滚动时的缩放比例
		sm.setBehindScrollScale(0.0f);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}

	// 事件监听
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.menu_image:// 菜单
				toggle();
				break;
			case R.id.back:// 返回
				finish();
				break;
			default:
				break;
		}
	}

	// 切换Fragment
	public void switchConent(Fragment fragment, String title) {
		mContent = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		getSlidingMenu().showContent();
		//topTextView.setText(title);
	}
}
