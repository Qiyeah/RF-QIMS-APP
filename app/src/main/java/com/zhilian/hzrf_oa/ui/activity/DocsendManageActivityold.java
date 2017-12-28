package com.zhilian.hzrf_oa.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.adapter.IncomingManageAdapter;
import com.zhilian.hzrf_oa.common.BusinessContant;
import com.zhilian.hzrf_oa.entity.DocManage;
import com.zhilian.hzrf_oa.json.T_Receive;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhilian.api.InQueryMsg;
import com.zhilian.api.JsonStringRequest;
import com.zhilian.api.JsonUtil;
import com.zhilian.api.ParaMap;
import com.zhilian.api.RequestUtil;
import com.zhilian.api.Sign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发文管理
 */
public class DocsendManageActivityold extends Activity {
	private ListView listView;// 发文管理列表
	private EditText editText;
	private ImageView ivDeleteText;

	private IncomingManageAdapter adapter;// 发文管理列表的适配器
	private List<DocManage> adapterlist = new ArrayList<DocManage>();
	private ImageView back;// 返回
	List<T_Receive> list;
	private Handler myhandler = new Handler();
	ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
	ArrayList<String> mListTitle = new ArrayList<String>();
	ArrayList<String> mListDocno = new ArrayList<String>();
	ArrayList<String> mListDate = new ArrayList<String>();
	ArrayList<String> mListUnit = new ArrayList<String>();
	ArrayList<Integer> mListDocid = new ArrayList<Integer>();
	ArrayList<Integer> mListPid = new ArrayList<Integer>();
	String docno,unit,starttime,startdate,doctitle;
	int docid,id;
	Bundle savedInstanceState;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.docsend_list);

		listView = (ListView) findViewById(R.id.list_outgoing_manage);

		addData();// 添加数据（测试数据）
		initView();// 初始化
	}

	private void initView() {
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		editText=(EditText)findViewById(R.id.searchtext);
		ivDeleteText=(ImageView)findViewById(R.id.ivDeleteText);
		ivDeleteText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				editText.setText("");
			}
		});

		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				//这个应该是在改变的时候会做的动作吧，具体还没用到过。
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
										  int arg3) {
				// TODO Auto-generated method stub
				//这是文本框改变之前会执行的动作
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				/**这是文本框改变之后 会执行的动作
				 * 因为我们要做的就是，在文本框改变的同时，我们的listview的数据也进行相应的变动，并且如一的显示在界面上。
				 * 所以这里我们就需要加上数据的修改的动作了。
				 */
				if(s.length() == 0){
					ivDeleteText.setVisibility(View.GONE);//当文本框为空时，则叉叉消失
				}
				else {
					ivDeleteText.setVisibility(View.VISIBLE);//当文本框不为空时，出现叉叉
				}
				myhandler.post(eChanged);
			}
		});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(DocsendManageActivityold.this,DocsendDetail.class);

				BusinessContant bc=new BusinessContant();
				bc.setDoc_id(Integer.parseInt(mData.get(position).get("docid").toString()));

				startActivity(intent);
			}
		});
	}

	private void addData() {
		/*list.add(new DocManage("惠人防〔2016〕3269号","发文标题","综合科","2016-11-09",1));
		list.add(new DocManage("惠人防〔2016〕6958号","xx工程建设意见","综合科","2016-11-12",1));*/
		BusinessContant bc = new BusinessContant();
		String key = bc.getCONFIRM_ID();
		//String url = "http://192.168.9.126:8083/Api";
		String url=bc.URL;
		String token = "1lj4hbato30kl1ppytwa1ueqdn";
		final String encodingAesKey = "InVjlo7czsOWrCSmTPgEUXBzlFnmqpNMQU3ZfilULHyHZiRjVUhxxWpexhYH6f4i";
		Map<String,String> ret = Sign.sign(url, token, encodingAesKey);
		String signature = ret.get("signature");
		String nonceStr = ret.get("nonceStr");
		String timestamp = ret.get("timestamp");
		Map<String, String> queryParas = ParaMap.create("accessToken", token)
				.put("nonce", nonceStr)
				.put("timestamp", timestamp)
				.put("signature", signature)
				.getData();
		url = RequestUtil.buildUrlWithQueryString(url,queryParas);
		InQueryMsg inQueryMsg = new InQueryMsg(1348831860,"query",key);
		inQueryMsg.setQueryName("getDocsendList");
		HashMap<String,String> map = new HashMap<>();
		map.put("projectName","收文");
		map.put("pid",bc.getPid());
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

		JsonRequest jsonRequest = new JsonStringRequest(Request.Method.POST,url, postData,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d("TAG", "response -> " + response.toString());
						System.out.println("解密后："+response.toString());
						if (response.toString().equals("[]")){
							Toast.makeText(DocsendManageActivityold.this,"没有待处理的发文！",Toast.LENGTH_SHORT).show();
						}else {
							//Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
							list = JsonUtil.getReceiveList(response.toString());
							System.out.println("list:"+list);
							getmData(mData);

							/*for (int i=0;i<list.size();i++){
								docno=list.get(i).getDocno();
								unit=list.get(i).getUnit();
								starttime=list.get(i).getStarttime();
								startdate=starttime.substring(0,10);
								doctitle=list.get(i).getDoctitle();
								docid=list.get(i).getDocid();
								id=list.get(i).getPid();
								//adapterlist.add(new DocManage(docno,doctitle,unit,startdate,id,docid));
							}*/
							adapter = new IncomingManageAdapter(mData,getApplicationContext());
							listView.setAdapter(adapter);
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

	}

	Runnable eChanged = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String data = editText.getText().toString();

			mData.clear();
			mData=getmDataSub(mData, data);
			//bc.setSubmData(mData);
			adapter.notifyDataSetChanged();
			//listViewinit();
		}
	};

	private ArrayList<Map<String, Object>> getmDataSub(ArrayList<Map<String, Object>> mDataSubs, String data)
	{
		int length = mListTitle.size();
		for(int i = 0; i < length; ++i){
			if(mListTitle.get(i).contains(data) || mListDocno.get(i).contains(data)
					|| mListUnit.get(i).contains(data)||mListDate.get(i).contains(data)){
				Map<String,Object> item = new HashMap<String,Object>();
				item.put("title",mListTitle.get(i));
				item.put("unit",mListUnit.get(i));
				item.put("docno",mListDocno.get(i));
				item.put("date",mListDate.get(i));
				item.put("docid",mListDocid.get(i));
				item.put("pid",mListPid.get(i));
				mDataSubs.add(item);
			}
		}
		return mDataSubs;
	}

	public void getmData(ArrayList<Map<String, Object>> mDatas) {
		Map<String, Object> item = new HashMap<String, Object>();
		for (int i = 0; i < list.size(); i++) {
			starttime=list.get(i).getStarttime();
			startdate=starttime.substring(0,10);
			mListTitle.add(list.get(i).getDoctitle());
			mListDocno.add(list.get(i).getDocno());
			mListDate.add(startdate);
			mListUnit.add(list.get(i).getUnit());
			mListDocid.add(list.get(i).getDocid());
			mListPid.add(list.get(i).getPid());
			item.put("title", mListTitle.get(i));
			item.put("unit", mListUnit.get(i));
			item.put("docno", mListDocno.get(i));
			item.put("date", mListDate.get(i));
			item.put("docid", mListDocid.get(i));
			item.put("pid",mListPid.get(i));
			mDatas.add(item);
			item = new HashMap<String, Object>();
		}

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		mData.clear();
		this.onCreate(savedInstanceState);
	}
}
