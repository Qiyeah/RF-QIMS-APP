package com.zhilian.hzrf_oa.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.adapter.OpinionAdapter;
import com.zhilian.hzrf_oa.adapter.RecordAdapter;
import com.zhilian.hzrf_oa.adapter.TextAdapter;
import com.zhilian.hzrf_oa.common.BusinessContant;
import com.zhilian.hzrf_oa.common.Common;
import com.zhilian.hzrf_oa.entity.OpinionBeen;
import com.zhilian.hzrf_oa.json.T_FJList;
import com.zhilian.hzrf_oa.json.T_Record;
import com.zhilian.hzrf_oa.json.T_Selectman;
import com.zhilian.hzrf_oa.util.DownLoadUtil;
import com.zhilian.hzrf_oa.util.LogUtil;
import com.zhilian.hzrf_oa.util.OpenFileIntent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhilian.api.InQueryMsg;
import com.zhilian.api.JsonStringRequest;
import com.zhilian.api.JsonUtil;
import com.zhilian.api.ParaMap;
import com.zhilian.api.RequestUtil;
import com.zhilian.api.Sign;
import com.zhilian.api.StrKit;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/3.
 * 文件呈批表（收文归档）
 */
public class DocsendCabDetail extends Activity{
	private Button record,tianxieyijian,submit,openfile;// 记录/ 填写意见/ 提交/ 查看正文
	private BusinessContant bc = new BusinessContant();
	private TextView tv_dname,tv_approvedate,tv_uname,tv_testername,tv_security,tv_count,tv_send1;
	private TextView tv_send2,tv_docno,tv_title,tv_openfile,tv_fjlist,tv_opinion;
	private ListView fjListView,opinionListView,recordListView;
	private TextAdapter fjadapter;
	private OpinionAdapter opinionAdapter;
	private List<OpinionBeen> opinionBeenList=new ArrayList<OpinionBeen>();
	private ArrayList<T_FJList> fjlist = new ArrayList<T_FJList>();
	private ArrayList<T_FJList> lists = new ArrayList<T_FJList>();
	private List<T_Selectman> selectmenlist = new ArrayList<T_Selectman>();
	private List<T_Record> recordList=new ArrayList<T_Record>();

	private DownLoadUtil downLoad = new DownLoadUtil();

	String name;
	private RecordAdapter recordAdapter;//下载进度条
	private NotificationManager mNotificationManager=null;
	private Notification mNotification;
	private int fileSize;
	int downLoadFileSize;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// 定义一个Handler，用于处理下载线程与UI间通讯
			switch (msg.what) {
				case 1:
					int result = downLoadFileSize * 100 / fileSize;
					mNotification.contentView.setTextViewText(R.id.content_view_text1, name+" "+result + "%");
					mNotification.contentView.setProgressBar(R.id.content_view_progress, fileSize, downLoadFileSize, false);
					mNotificationManager.notify(0, mNotification);
					Log.e("size", "文件"+downLoadFileSize+":"+fileSize+":"+result);
					break;
				case 2:
					Toast.makeText(getApplication(), "文件下载完成", Toast.LENGTH_SHORT).show();
					break;

				case -1:
					String error = msg.getData().getString("error");
					Toast.makeText(getApplication(), error, Toast.LENGTH_SHORT).show();
					break;

				default:
					break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.docsend_detail);

		ImageView back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		fjListView=(ListView)findViewById(R.id.fj_list);
		opinionListView=(ListView)findViewById(R.id.opinionList);
		tv_dname=(TextView)findViewById(R.id.dname);
		tv_approvedate=(TextView)findViewById(R.id.starttime);
		tv_uname=(TextView)findViewById(R.id.uname);
		tv_testername=(TextView)findViewById(R.id.testuname);
		tv_security=(TextView)findViewById(R.id.security);
		tv_count=(TextView)findViewById(R.id.count);
		tv_send1=(TextView)findViewById(R.id.send1);
		tv_send2=(TextView)findViewById(R.id.send2);
		tv_docno=(TextView)findViewById(R.id.docno);
		tv_title=(TextView)findViewById(R.id.title);
		tianxieyijian = (Button) findViewById(R.id.tianxieyijian);
		submit = (Button) findViewById(R.id.submit);
		addData();

		initView();
	}

	private void initView() {
		record = (Button) findViewById(R.id.record);
		record.setOnClickListener(listener1);
		openfile = (Button) findViewById(R.id.openfile);
		openfile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Context mContext=getApplicationContext();
				final ConnectivityManager connectivityManager = (ConnectivityManager) mContext
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				final NetworkInfo mobNetInfoActivity = connectivityManager
						.getActiveNetworkInfo();
				if (mobNetInfoActivity == null || !mobNetInfoActivity.isAvailable()) {
					Toast.makeText(DocsendCabDetail.this, "当前没有连接网络！不能下载！", Toast.LENGTH_SHORT).show();
				} else {
					openfile();
				}
				/*Intent it = OpenFileIntent.getWordFileIntent("/storage/emulated/0/tencent/QQfile_recv/testWord.doc");
				startActivity(it);*/
			}
		});
		fjListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
				Context mContext=getApplicationContext();
				name = fjlist.get(position).getName();
				String url = fjlist.get(position).getUrl();
				downLoad.context = mContext;
				downLoad.name = url;
				downLoad.downPath = bc.SAVEPATH + url;
				downLoad.downUrl = bc.DOWNLOADURL + url;
				downLoad.DownOrOpen();
			}
		});
	}

	private View.OnClickListener listener1 = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			AlertDialog.Builder builder = new AlertDialog.Builder(DocsendCabDetail.this);
			builder.setTitle("流转记录");

			View view = getLayoutInflater().inflate(R.layout.button1_layout, null);
			recordListView=(ListView)view.findViewById(R.id.recordList);
			new Thread() {
				public void run()
				{
					try {
						String key = bc.getCONFIRM_ID();
						String pid=String.valueOf(bc.getPid());
						String url=bc.URL;
						String token = "1lj4hbato30kl1ppytwa1ueqdn";
						final String encodingAesKey = "InVjlo7czsOWrCSmTPgEUXBzlFnmqpNMQU3ZfilULHyHZiRjVUhxxWpexhYH6f4i";
						Map<String,String> ret = Sign.sign(url, token, encodingAesKey);
						Map<String, String> queryParas = ParaMap.create("accessToken", token)
								.getData();
						url = RequestUtil.buildUrlWithQueryString(url,queryParas);
						InQueryMsg inQueryMsg = new InQueryMsg(1348831860,"query",key);
						inQueryMsg.setQueryName("ReceiveRecord");
						HashMap<String,String> map = new HashMap<>();
						map.put("projectName","发文流转记录");
						map.put("pid",pid);
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
										Log.d("TAG", "response -> " + response.toString());
										LogUtil.i("i","解密后：" + response.toString());

										try{

											List<T_Record> list = JsonUtil.getRecordList(response.toString());
											recordList.clear();
											for (int i=0;i<list.size();i++){
												String item, doman, starttime, endtime, operation, nextusername, opinion;
												opinion=list.get(i).getOpinion();
												starttime=list.get(i).getBegintime();
												operation=list.get(i).getOperation();
												endtime=list.get(i).getEndtime();
												doman=list.get(i).getName();
												item=list.get(i).getItemid1();
												nextusername=list.get(i).getUser2();
												recordList.add(new T_Record(item, doman, starttime, endtime, operation, nextusername, opinion));
											}
											recordAdapter=new RecordAdapter(recordList,getApplicationContext());
											recordListView.setAdapter(recordAdapter);
										}catch (Exception e){
											e.printStackTrace();
										}
									}
								}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								Log.e("TAG", error.getMessage(), error);
								Toast.makeText(getApplicationContext(),"出错了!",Toast.LENGTH_LONG).show();
							}
						});
						requestQueue.add(jsonRequest);

					}catch  (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					}
				}
			}.start();

			builder.setView(view);// 使用自定义布局作为对话框内容

			// 负面语义按钮
			builder.setNegativeButton("关闭", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					/*Toast.makeText(DocsendDetail.this, "点击了取消", Toast.LENGTH_SHORT)
							.show();*/
				}
			});

			builder.create().show();
		}
	};

	private void addData() {
		String key = bc.getCONFIRM_ID();
		String url = bc.URL;
		String token = "1lj4hbato30kl1ppytwa1ueqdn";
		final String encodingAesKey = "InVjlo7czsOWrCSmTPgEUXBzlFnmqpNMQU3ZfilULHyHZiRjVUhxxWpexhYH6f4i";
		Map<String, String> ret = Sign.sign(url, token, encodingAesKey);
		String signature = ret.get("signature");
		String nonceStr = ret.get("nonceStr");
		String timestamp = ret.get("timestamp");
		Map<String, String> queryParas = ParaMap.create("accessToken", token)
				.put("nonce", nonceStr)
				.put("timestamp", timestamp)
				.put("signature", signature)
				.getData();
		url = RequestUtil.buildUrlWithQueryString(url, queryParas);
		InQueryMsg inQueryMsg = new InQueryMsg(1348831860, "query", key);
		inQueryMsg.setQueryName("DocsendDetail");
		HashMap<String, String> map = new HashMap<>();
		map.put("projectName", "发文");
		map.put("docid", String.valueOf(bc.getDoc_id()));
		map.put("isdone","1");
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

		JsonRequest jsonRequest = new JsonStringRequest(Request.Method.POST, url, postData,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d("TAG", "response -> " + response.toString());
						LogUtil.i("i","解密后：" + response.toString());
						//Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
						String fjname,opinion, signimg, signdate,sign;
						Integer fjid;
						submit.setVisibility(View.GONE);
						tianxieyijian.setVisibility(View.GONE);


						try {
							JSONObject dataJson = new JSONObject(response.toString());
							tv_dname.setText(dataJson.getString("dname"));
							tv_approvedate.setText(dataJson.getString("approvedate"));
							tv_uname.setText(dataJson.getString("uname"));
							if(StrKit.notBlank(dataJson.getString("proof"))){
								tv_testername.setText(dataJson.getString("proof"));
							}
							tv_security.setText(dataJson.getString("security"));
							if (StrKit.notBlank(dataJson.getString("num"))) {
								tv_count.setText(dataJson.getString("num"));
							}
							if (StrKit.notBlank(dataJson.getString("send1"))) {
								tv_send1.setText(dataJson.getString("send1"));
							}
							if (StrKit.notBlank(dataJson.getString("send2"))) {
								tv_send2.setText(dataJson.getString("send2"));
							}
							tv_docno.setText(dataJson.getString("docno"));
							tv_title.setText(dataJson.getString("title"));
							//String opinionfield=dataJson.getString("");
							bc.setPid(dataJson.getString("pid"));
							String itemid = dataJson.getString("opinion5");
							JSONArray fjids=dataJson.getJSONArray("fjid");
							JSONArray opinions=dataJson.getJSONArray("opinion1");
							List<T_FJList> list = JsonUtil.getFJList(fjids.toString());
							bc.setItem_id(itemid);
							//List<OpinionBeen> opinionlist = JsonUtil.getOpinionList(opinion1.toString());
							/*if (!opinionfield.equals("opinion1")) {
								tianxieyijian.setVisibility(View.GONE);
							}*/
							for (int i = 0; i < list.size(); i++) {
								fjname = list.get(i).getName();
								fjid = list.get(i).getId();
								String url = list.get(i).getUrl();
								String size=list.get(i).getSize();
								fjlist.add(new T_FJList(fjid, fjname, url,size));
							}
							fjadapter=new TextAdapter(fjlist,DocsendCabDetail.this);
							fjListView.setAdapter(fjadapter);
							if (list.size()>0){
								setListViewHeightBasedOnChildren(fjListView);
							}

							for (int i = 0; i < opinions.length(); i++) {
								JSONObject info = opinions.getJSONObject(i);
								int id1 = info.getInt("id");
								opinion = info.getString("opinion");
								signimg = info.getString("url");
								sign = info.getString("leader");
								signdate = info.getString("opiniontime");
								if (StrKit.notBlank(signimg)) {
									opinionBeenList.add(new OpinionBeen(opinion, signimg, signdate, id1));
								} else {
									opinionBeenList.add(new OpinionBeen(opinion, sign, signdate));
								}
							}
							opinionAdapter=new OpinionAdapter(opinionBeenList,DocsendCabDetail.this);
							opinionListView.setAdapter(opinionAdapter);
							if (opinions.length()>0){
								setListViewHeightBasedOnChildren(opinionListView);
							}


						}catch (Exception e){
							e.printStackTrace();
						}

					}
				} , new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
				Toast.makeText(getApplicationContext(), "出错了!", Toast.LENGTH_LONG).show();
			}
		});

		requestQueue.add(jsonRequest);
	}

	private void openfile(){
		String key = bc.getCONFIRM_ID();
		String pid=String.valueOf(bc.getPid());
		String url = bc.URL;
		String token = "1lj4hbato30kl1ppytwa1ueqdn";
		final String encodingAesKey = "InVjlo7czsOWrCSmTPgEUXBzlFnmqpNMQU3ZfilULHyHZiRjVUhxxWpexhYH6f4i";
		Map<String, String> ret = Sign.sign(url, token, encodingAesKey);
		String signature = ret.get("signature");
		String nonceStr = ret.get("nonceStr");
		String timestamp = ret.get("timestamp");
		Map<String, String> queryParas = ParaMap.create("accessToken", token)
				.put("nonce", nonceStr)
				.put("timestamp", timestamp)
				.put("signature", signature)
				.getData();
		url = RequestUtil.buildUrlWithQueryString(url, queryParas);
		InQueryMsg inQueryMsg = new InQueryMsg(1348831860, "query", key);
		inQueryMsg.setQueryName("getfile");
		HashMap<String, String> map = new HashMap<>();

		map.put("pid", pid);
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

		JsonRequest jsonRequest = new JsonStringRequest(Request.Method.POST, url, postData,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d("TAG", "response -> " + response.toString());
						LogUtil.i("i","解密后：" + response.toString());
						if (response.toString().equals("没有该文件！")) {
							Toast.makeText(DocsendCabDetail.this, "没有该文件！", Toast.LENGTH_SHORT).show();
						} else {


							final String fileurl = bc.Domain + response.toString();
							String[] paths = fileurl.split("/");
							final String name = paths[paths.length - 1];
							new Thread(new Runnable() {
								@Override
								public void run() {
									URL url;
									try {
										url = new URL(fileurl);
										InputStream is = url.openStream();
										byte[] arr = new byte[1];
										ByteArrayOutputStream baos = new ByteArrayOutputStream();
										BufferedOutputStream bos = new BufferedOutputStream(baos);
										int n = is.read(arr);
										while (n > 0) {
											bos.write(arr);
											n = is.read(arr);
										}
										bos.close();
										String path = bc.SAVEPATH + name;

										File file = new File(bc.SAVEPATH);
										if (!file.exists()) {
											file.mkdirs();
										}
										bc.verifyStoragePermissions(DocsendCabDetail.this);
										FileOutputStream fos = new FileOutputStream(path);
										fos.write(baos.toByteArray());
										fos.close();
										Intent it = new Intent();
										if (path.substring(path.length() - 4).equals(".pdf")) {
											it = OpenFileIntent.getPdfFileIntent(path);
										} else if (path.substring(path.length() - 4).equals(".doc")) {
											it = OpenFileIntent.getWordFileIntent(path);
										} else {
											it = OpenFileIntent.getImageFileIntent(path);
										}
										startActivity(it);
										is.close();
									} catch (MalformedURLException e) {
										e.printStackTrace();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}).start();
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

	public void down_file(String url, String path) throws IOException {
		// 下载函数

		// 获取文件名
		URL myURL = new URL(url);
		URLConnection conn = myURL.openConnection();
		conn.connect();
		InputStream is=null;
		try{
			is = conn.getInputStream();
		}catch (IOException e){
			e.printStackTrace();
			Looper.prepare();
			Toast.makeText(DocsendCabDetail.this,"没有找到文件！",Toast.LENGTH_SHORT).show();
			Looper.loop();
		}

		this.fileSize = conn.getContentLength();// 根据响应获取文件大小
		if (this.fileSize <= 0)
			throw new RuntimeException("无法获知文件大小 ");
		if (is == null)
			throw new RuntimeException("stream is null");
		FileOutputStream fos = new FileOutputStream(path);
		// 把数据存入路径+文件名
		byte buf[] = new byte[1024];
		downLoadFileSize = 0;
		sendMsg(0);
		int numread=0;
		int times=0;//设置更新频率，频繁操作ＵＩ线程会导致系统奔溃
		BufferedInputStream bis = new BufferedInputStream(is);
		while((numread=bis.read(buf))!=-1){
			fos.write(buf, 0, numread);
			downLoadFileSize += numread;
			//Log.i("num", rate+","+total+","+p);
			if(times>=512|| times==0 ||downLoadFileSize==this.fileSize )
			{/*
                    这是防止频繁地更新通知，而导致系统变慢甚至崩溃。
                                                             非常重要。。。。。*/
				Log.i("time", "time");
				times=0;
				sendMsg(1);
			}
			times++;

		}
		sendMsg(2);// 通知下载完成
		try {
			fos.close();
			is.close();
		} catch (Exception ex) {
			Log.e("tag", "error: " + ex.getMessage(), ex);
		}

	}

	private void sendMsg(int flag) {
		Message msg = new Message();
		msg.what = flag;
		handler.sendMessage(msg);
	}

	public void chooseappopenfile(String path){//选择应用程序打开文件
		Common commom=new Common();
		File file=new File(path);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		String type=commom.getMIMEType(file);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri,type);
		startActivity(intent);
	}

	private void notificationInit(String name){
		Common commom=new Common();
		//通知栏内显示下载进度条
		File file=new File(name);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		String type=commom.getMIMEType(file);
		intent.setDataAndType(Uri.parse("file://" + file.getAbsolutePath()), type);//点击进度条，进入相应界面
		PendingIntent pIntent=PendingIntent.getActivity(this, 0, intent, 0);
		mNotificationManager=(NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
		mNotification=new Notification();
		mNotification.icon=R.drawable.fj_icon;
		mNotification.tickerText="开始下载";
		mNotification.contentView=new RemoteViews(getPackageName(),R.layout.download_pro);//通知栏中进度布局
		mNotification.contentIntent=pIntent;
//  mNotificationManager.notify(0,mNotification);
	}

	public void setListViewHeightBasedOnChildren(ListView listView) {

		ListAdapter listAdapter = listView.getAdapter();
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		int totalHeight = 0;
		if (listAdapter == null) {
			return;
		}
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		//((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10); // 可删除
		listView.setLayoutParams(params);
	}
}
