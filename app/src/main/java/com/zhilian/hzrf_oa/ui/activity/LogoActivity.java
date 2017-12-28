package com.zhilian.hzrf_oa.ui.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.ui.base.BaseActivity;

/**
 * 暂放，后续使用（布局还待设计）
 */
public class LogoActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_logo);

		// 时间任务 ： 在到达时间点之后 执行一段代码
		TimerTask tt = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message message = Message.obtain();
				// message.obj  表示为可以再message上绑定一个对象
				// message.setData(data) 也可以传递数据
				message.what = 1;
				mhandler.sendMessage(message);
			}
		};

		Timer timer = new Timer(true);// 计时器
		timer.schedule(tt, 2000);// 2秒之后执行task内run方法的代码

	}


	private Handler mhandler = new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
				case 1:
					LogoActivity.this.startActivity(LoginActivity.class, R.anim.fade, R.anim.hold);
					break;
			}
		}
	};
}
