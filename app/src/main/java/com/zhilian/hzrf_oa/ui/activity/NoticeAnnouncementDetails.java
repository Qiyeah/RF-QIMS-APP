package com.zhilian.hzrf_oa.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.common.BusinessContant;
import com.zhilian.hzrf_oa.json.T_AnnounceDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhilian.api.InQueryMsg;
import com.zhilian.api.JsonStringRequest;
import com.zhilian.api.RequestUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * 公告通知详情
 */
public class NoticeAnnouncementDetails extends Activity{
	TextView title;// 标题
	TextView uname;// 发布人
	TextView dname;// 部门
	TextView sendtime;// 发布时间
	TextView content;// 内容

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notice_announcement_details_layout);

		int id = getIntent().getExtras().getInt("id");// 拿到公告通知传来的，公告id

		getAnnounceDetail(id);// 请求公告详情数据
		saveViewAnnounce(id);// 把用户放到已阅者中
		initView();// 初始化控件
	}

	private void initView() {
		ImageView back = (ImageView) findViewById(R.id.back);// 返回
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		title = (TextView) findViewById(R.id.title);// 标题
		uname = (TextView) findViewById(R.id.uname);// 发布人
		dname = (TextView) findViewById(R.id.dname);// 部门
		sendtime = (TextView) findViewById(R.id.sendtime);// 发布时间
		content = (TextView) findViewById(R.id.content);// 内容

	}

	private void getAnnounceDetail(int id){
		BusinessContant bc = new BusinessContant();
		String key = bc.getCONFIRM_ID();
		String url=bc.URL;
		InQueryMsg inQueryMsg = new InQueryMsg(2016120712, "query", key);
		inQueryMsg.setQueryName("AnnounceDetail");
		HashMap<String, String> map = new HashMap<>();
		map.put("id", String.valueOf(id));
		//System.out.println("id------------>" + id);
		inQueryMsg.setQueryPara(map);
		String postData = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			postData = mapper.writeValueAsString(inQueryMsg);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
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
							T_AnnounceDetails model = objectMapper.readValue(response.toString(), T_AnnounceDetails.class);

							title.setText(model.getTitle());// 标题
							uname.setText(model.getUid());// 发布人
							dname.setText(model.getDid());// 部门
							sendtime.setText(model.getSendtime());// 发布时间
							content.setText(Html.fromHtml(model.getContent()));// 内容

						} catch (IOException e) {
							e.printStackTrace();
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

	/**
	 * 查看公告后，把用户放到已查阅人员中
	 * 注意：这里的请求方式有问题，但是后台做了相应的处理（有需要再做修改）
	 */
	private void saveViewAnnounce(int id){
		BusinessContant bc = new BusinessContant();
		String key = bc.getCONFIRM_ID();
		String url=bc.URL;
		InQueryMsg inQueryMsg = new InQueryMsg(1348831860, "query", key);
		inQueryMsg.setQueryName("viewAnnounce");
		HashMap<String, String> map = new HashMap<>();
		map.put("id", String.valueOf(id));
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

						ObjectMapper objectMapper = new ObjectMapper();
						objectMapper.configure(
								DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
						try {
							JSONObject dataJson = new JSONObject(response.toString());
							String msg = dataJson.getString("msg");

							System.out.println(msg);
							/*Toast.makeText(NoticeAnnouncementDetails.this, msg, Toast.LENGTH_SHORT)
									.show();*/
						} catch (JSONException e){
							e.printStackTrace();
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
}
