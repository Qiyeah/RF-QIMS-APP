package com.zhilian.hzrf_oa.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.adapter.MyMailAdapter;
import com.zhilian.hzrf_oa.common.BusinessContant;
import com.zhilian.hzrf_oa.json.ReceiveMailList;
import com.zhilian.hzrf_oa.ui.activity.MyMailActivity;
import com.zhilian.hzrf_oa.ui.activity.MyMailCheckActivity;
import com.zhilian.hzrf_oa.ui.activity.WriteLetterActivity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhilian.api.InQueryMsg;
import com.zhilian.api.JsonStringRequest;
import com.zhilian.api.JsonUtil;
import com.zhilian.api.RequestUtil;
import com.zhilian.api.StrKit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/30.
 * 收件箱、发件箱、草稿箱、垃圾箱
 */
public class InboxFragment extends Fragment {
	private MyMailActivity myMailActivity;

	private Handler myhandler=new Handler();
	private ImageView ivDelete;
	private EditText editText;
	BusinessContant bc=new BusinessContant();
	ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
	ArrayList<String> mListTitle = new ArrayList<String>();
	ArrayList<String> mListFromUserName = new ArrayList<String>();
	ArrayList<String> mListSendTime = new ArrayList<String>();
	ArrayList<Integer> mListIsRead = new ArrayList<Integer>();
	ArrayList<Integer> mListId = new ArrayList<Integer>();
	private ListView listView;// 邮件列表
	private MyMailAdapter adapter;// 邮件列表的适配器
	private TextView top_title;
	private List<ReceiveMailList> list;
	private List<ReceiveMailList> lists = new ArrayList<ReceiveMailList>();
	String queryName = null;
	int id;// 邮件id

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.inbox_layout, null);
		myMailActivity = (MyMailActivity) getActivity();

		// 拿到传过来的数据请求接口
		queryName = getArguments().getString("sss");

		editText = (EditText)myMailActivity.findViewById(R.id.searchtext);
		ivDelete = (ImageView)myMailActivity.findViewById(R.id.ivDeleteText);
		top_title = (TextView)myMailActivity.findViewById(R.id.top_title);
		listView = (ListView) view.findViewById(R.id.list_my_mail);
		listView.setOnItemClickListener(listener);
//		adapter = new MyMailAdapter(lists,myMailActivity);
//		listView.setAdapter(adapter);

		addDate(queryName);// 加载数据
		switch (queryName){
			case "getSendList":
				top_title.setText("发信箱");
				break;
			case "getReceiveMailList":
				top_title.setText("收件箱");
				break;
			case "getDraftList":
				top_title.setText("草稿箱");
				break;
			case "getRubbishList":
				top_title.setText("垃圾箱");
				break;
			default:
				break;
		}
		initView();
		return view;
	}

	public void initView(){
		ivDelete.setOnClickListener(new View.OnClickListener() {
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
					ivDelete.setVisibility(View.GONE);//当文本框为空时，则叉叉消失
				}
				else {
					ivDelete.setVisibility(View.VISIBLE);//当文本框不为空时，出现叉叉
				}
				myhandler.post(eChanged);
			}
		});

	}

	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
			id = Integer.parseInt(mData.get(position).get("id").toString());// 拿到点击的邮件id
			if(queryName.equals("getDraftList")){// 草稿箱跳转的页面
				bc.setModel(null);
				Intent intent = new Intent(myMailActivity, WriteLetterActivity.class);
				/*Bundle bundele = new Bundle();
				bundele.putInt("id",id);
				bundele.putString("isBox",queryName);
				intent.putExtras(bundele);*/
				startActivity(intent);

			}else{// 其他箱跳转的页面
				Intent intent = new Intent(myMailActivity, MyMailCheckActivity.class);
				Bundle bundele = new Bundle();
				bundele.putInt("id",id);
				bundele.putString("isBox",queryName);
				intent.putExtras(bundele);

				startActivity(intent);
			}


		}
	};

	private void addDate(String queryName) {
		BusinessContant bc = new BusinessContant();
		String key = bc.getCONFIRM_ID();
		String url = bc.URL;
		InQueryMsg inQueryMsg = new InQueryMsg(1348831860, "query", key);
		inQueryMsg.setQueryName(queryName);
		HashMap<String, String> map = new HashMap<>();
		map.put("MyMail", "我的邮件");
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

						list = JsonUtil.getReceiveMailList(response.toString());
						System.out.println("list:" + list);
						getmData(mData);
						// 拆分数据
						/*for (int i = 0; i < list.size(); i++) {
							String title = list.get(i).getTitle();
							String fromUserName = list.get(i).getFromUserName();
							String sendtime = list.get(i).getSendTime();
							int isRead = list.get(i).getIsRead();
							int id = list.get(i).getId();

							ReceiveMailList rml = new ReceiveMailList(id, title, fromUserName, sendtime, isRead);
							lists.add(rml);
						}*/
						adapter = new MyMailAdapter(mData, myMailActivity);
						listView.setAdapter(adapter);

					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
				Toast.makeText(myMailActivity, "出错了!", Toast.LENGTH_LONG).show();
			}
		});

		requestQueue.add(jsonRequest);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
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
		int length = mListTitle.size();//获取当前列表的全部列数
		for(int i = 0; i < length; ++i){
			if(mListTitle.get(i).contains(data)||(StrKit.isBlank(mListFromUserName.get(i))?false:mListFromUserName.get(i).contains(data))||mListSendTime.get(i).contains(data)){//在当前页面任何项包含输入的值则将该项添加进显示列表
				Map<String,Object> item = new HashMap<String,Object>();
				item.put("title",mListTitle.get(i));
				item.put("username",mListFromUserName.get(i));
				item.put("sendtime",mListSendTime.get(i));
				item.put("isread",mListIsRead.get(i));
				item.put("id",mListId.get(i));
				mDataSubs.add(item);
			}
		}
		return mDataSubs;
	}

	public void getmData(ArrayList<Map<String, Object>> mDatas) {
		Map<String, Object> item = new HashMap<String, Object>();
		System.out.print("list==="+list);
		for (int i = 0; i < list.size(); i++) {
			mListTitle.add(list.get(i).getTitle());
			mListFromUserName.add(list.get(i).getFromUserName());
			mListSendTime.add(list.get(i).getSendTime());
			mListIsRead.add(list.get(i).getIsRead());
			mListId.add(list.get(i).getId());
			item.put("title",mListTitle.get(i));
			item.put("username",mListFromUserName.get(i));
			item.put("sendtime",mListSendTime.get(i));
			item.put("isread",mListIsRead.get(i));
			item.put("id",mListId.get(i));
			mDatas.add(item);
			item = new HashMap<String, Object>();
		}

	}
}
