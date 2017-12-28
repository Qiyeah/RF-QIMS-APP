package com.zhilian.hzrf_oa.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.zhilian.hzrf_oa.entity.NotepadBeen;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhilian.api.InQueryMsg;
import com.zhilian.api.InSaveMsg;
import com.zhilian.api.JsonStringRequest;
import com.zhilian.api.RequestUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * 记事本的事件详情
 */
public class NoteDetailActivity extends Activity {
	EditText title;// 标题
	TextView wdate;// 时间
	EditText type;// 类型
	EditText content;// 内容

	int id;// 事件id
	private String currentDate = "";

	public NoteDetailActivity() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
		currentDate = sdf.format(date);  // 当前日期
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_detail_layout);

		id = getIntent().getExtras().getInt("id");// 拿到传来的事件id

		initView();// 初始化控件
		if(id != 0){
			getNoteDetail(id);// 事件详情数据
		}
	}

	private void initView() {
		title = (EditText) findViewById(R.id.title);
		wdate = (TextView) findViewById(R.id.wdate);
		wdate.setText(currentDate);// 设置日期为当前日期
		type = (EditText) findViewById(R.id.type);
		content = (EditText) findViewById(R.id.content);

		ImageView back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		TextView save = (TextView) findViewById(R.id.save);
		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				savedata(id,title.getText().toString(),content.getText().toString(),
						type.getText().toString());
				finish();
			}
		});
	}

	// 拿到记事本的事件详情
	private void getNoteDetail(int id){
		BusinessContant bc = new BusinessContant();
		String key = bc.getCONFIRM_ID();
		String url = bc.URL;
		InQueryMsg inQueryMsg = new InQueryMsg(1348831860, "query", key);
		inQueryMsg.setQueryName("NoteDetail");
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
						System.out.println("解密后：" + response.toString());

						ObjectMapper objectMapper = new ObjectMapper();
						objectMapper.configure(
								DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
						try {
							NotepadBeen model = objectMapper.readValue(response.toString(), NotepadBeen.class);

							title.setText(model.getTitle());
							wdate.setText(model.getWdate());
							type.setText(model.getType());
							content.setText(model.getContent());

						} catch (IOException e) {
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
				Toast.makeText(NoteDetailActivity.this, "出错了!", Toast.LENGTH_LONG).show();
			}
		});

		requestQueue.add(jsonRequest);
	}

	// 修改记事本的事件内容
	private void savedata(int id,String title,String content,String type){
		BusinessContant bc = new BusinessContant();
		String key = bc.getCONFIRM_ID();
		String url = bc.URL;
		InSaveMsg inSaveMsg = new InSaveMsg(1348831860, "save",key);
		inSaveMsg.setModelName("t_Note");
		HashMap<String, String> map = new HashMap<>();
		map.put("id", String.valueOf(id));
		map.put("title", title);
		map.put("content",content);
		map.put("type",type);
		map.put("wdate",currentDate);
		System.out.println("时间---->"+currentDate);

		inSaveMsg.setModelProperty(map);
		String postData = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			postData = mapper.writeValueAsString(inSaveMsg);
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

						Toast.makeText(NoteDetailActivity.this,response.toString(),Toast.LENGTH_SHORT).show();

					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
				Toast.makeText(NoteDetailActivity.this, "出错了!", Toast.LENGTH_LONG).show();
			}
		});

		requestQueue.add(jsonRequest);
	}
}
