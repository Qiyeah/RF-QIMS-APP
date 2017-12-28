package com.zhilian.hzrf_oa.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhilian.api.InQueryMsg;
import com.zhilian.api.JsonStringRequest;
import com.zhilian.api.RequestUtil;
import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.common.BusinessContant;
import com.zhilian.hzrf_oa.json.PageInnerSend;
import com.zhilian.hzrf_oa.json.PageReceive;
import com.zhilian.hzrf_oa.json.T_InnerSend;
import com.zhilian.hzrf_oa.json.T_Receive;
import com.zhilian.hzrf_oa.net_exception.ITimeOutException;
import com.zhilian.hzrf_oa.net_exception.TimeOutException;
import com.zhilian.hzrf_oa.ui.activity.InnerSendDetail;
import com.zhilian.hzrf_oa.ui.activity.ReceiveDetail;
import com.zhilian.hzrf_oa.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 收文的service
 */
public class InnerSendService extends Service {
	public static final String ACTION = "com.zhilian.service.InnerSendService";
	BusinessContant bc = new BusinessContant();

	private List<T_InnerSend> list;
	private List<T_InnerSend> adapterlist=new ArrayList<T_InnerSend>();

	private Notification mNotification;
	private NotificationManager mManager;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		//initNotifiManager();
		addData();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		new PollingThread().start();
	}

	private void initNotifiManager(String projectName, String activeName, int id) {
		NotificationManager mNotificationManager = (NotificationManager)
				this.getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

		Intent intent = new Intent(this, InnerSendDetail.class);
		//bc.setQuotationid(String.valueOf(id));
		intent.putExtra("docid", String.valueOf(id));
		intent.putExtra("isdone", "0");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		mBuilder.setContentTitle("待读发文：" + projectName)// 标题
				.setContentIntent(pendingIntent)// 点击意图
				//.setNumber(number)// 设置通知集合的数量 Context.getDefalutIntent(Notification.FLAG_AUTO_CANCEL)
				.setTicker("您有待处理信息")// 通知首次出现在通知栏，带上升动画效果的
				.setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
				.setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
				.setAutoCancel(true)// 设置这个标志当用户单击面板就可以让通知将自动取消
				.setOngoing(false)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
				.setDefaults(Notification.DEFAULT_SOUND)// 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合Notification.DEFAULT_VIBRATE
				//Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
				.setSmallIcon(R.drawable.icon_app);// 设置通知小ICON
		Notification notify = mBuilder.build();
		notify.flags = Notification.FLAG_ONLY_ALERT_ONCE;
		/*notify.ledARGB = 0xff0000ff;Notification.FLAG_SHOW_LIGHTS
		notify.ledOnMS = 300;
		notify.ledOffMS = 300;*/
		mNotificationManager.notify(id, mBuilder.build());
	}

	/**
	 * Polling thread
	 */
	int count = 0;

	class PollingThread extends Thread {
		@Override
		public void run() {
			LogUtil.i("i","Polling...");
			count++;
			if (count % 5 == 0) {
				addData();
				//initNotifiManager();
				LogUtil.i("i","New message!");
			}
		}
	}

	private void addData() {
		String key = bc.getCONFIRM_ID();
		String url = bc.URL;
		InQueryMsg inQueryMsg = new InQueryMsg(1348831860,"query",key);
		inQueryMsg.setQueryName("getInnerSendList");
		HashMap<String,String> map = new HashMap<>();
		map.put("projectName","");
		map.put("condition","");
		map.put("pageNumber",String.valueOf(1));
		inQueryMsg.setQueryPara(map);
		String postData = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			postData = mapper.writeValueAsString(inQueryMsg);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		LogUtil.i("i","发送前的明文：" + postData);
		RequestQueue requestQueue = RequestUtil.getRequestQueue();

		JsonRequest jsonRequest = new JsonStringRequest(Request.Method.POST,url, postData,
			   new Response.Listener<String>() {
				   @Override
				   public void onResponse(String response) {
					   LogUtil.i("i","解密后：" + response.toString());
					   if (response.equals(bc.ERROR)) {
						   new TimeOutException().reLogin(getApplicationContext(), new ITimeOutException.CallBack(){
							   @Override
							   public void onReloginSuccess() {
								   addData();
							   }
						   });
						   Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
					   } else {
						   PageInnerSend page = JSON.parseObject(response.toString(), PageInnerSend.class);
						   list = page.getList();

						   String project_name[] = new String[list.size()];
						   String active_name[] = new String[list.size()];
						   int id[] = new int[list.size()];
						   for (int i = 0; i < list.size(); i++) {
							   list.get(i).setStatus(0);
							   project_name[i] = list.get(i).getTitle();// 标题
							   id[i] = list.get(i).getId();//文档ID
						   }

						   for (int i = 0; i < list.size(); i++) {
							   initNotifiManager(project_name[i], active_name[i], id[i]);
						   }

					   }
				   }
			   }, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
				Toast.makeText(getApplicationContext(), "出错了!", Toast.LENGTH_LONG).show();
			}
		});
		requestQueue.add(jsonRequest);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtil.i("i","Service:onDestroy");
	}
}
