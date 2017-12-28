package com.zhilian.hzrf_oa.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.adapter.NoticeAnnouncementAdapter;
import com.zhilian.hzrf_oa.common.BusinessContant;
import com.zhilian.hzrf_oa.json.T_Announce;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhilian.api.InQueryMsg;
import com.zhilian.api.JsonStringRequest;
import com.zhilian.api.JsonUtil;
import com.zhilian.api.RequestUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 名称：公告通知
 * 描述：显示公告列表
 * 功能：查看公告详情
 */
public class NoticeAnnouncementActivity extends Activity {
	private ListView listView;// 公告列表
	private Handler myhandler = new Handler();
	private NoticeAnnouncementAdapter adapter;// 公告列表的适配器
	ArrayList<T_Announce> lists = new ArrayList<T_Announce>();// 公告列表的数据
	private List<T_Announce> list;
	private EditText editText;
	private ImageView back,ivDeleteText;// 返回/清空已输入的文字
	private ImageView menu;// 菜单
	private PopupWindow popupWindow;
	private LinearLayout notice_background;// 无内容时显示的背景

	ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
	ArrayList<String> mListTitle = new ArrayList<String>();
	ArrayList<String> mListName = new ArrayList<String>();
	ArrayList<String> mListDate = new ArrayList<String>();
	ArrayList<Integer> mListAtype = new ArrayList<Integer>();

	ArrayList<Integer> mListId = new ArrayList<Integer>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notice_announcement_layout);

		listView = (ListView) findViewById(R.id.list_notice);
		listView.setOnItemClickListener(listener);

		getAnnounceList();// 请求公告列表数据
		initView();// 初始化控件
	}

	private void initView() {
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		notice_background = (LinearLayout) findViewById(R.id.notice_background);

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
		/*menu = (ImageView) findViewById(R.id.menu_p);
		menu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showPopupWindow();
			}
		});*/
	}

	// 这段代码在这不删，方便后面有需要引用
	private void showPopupWindow() {
		View contentView = getLayoutInflater().inflate(R.layout.contract_popup_layout, null);
		popupWindow = new PopupWindow(contentView);
		popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
		popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());

		popupWindow.showAsDropDown(menu);
	}

	// 请求公告列表数据
	private void getAnnounceList() {
		BusinessContant bc = new BusinessContant();
		String key = bc.getCONFIRM_ID();
		String url=bc.URL;
		InQueryMsg inQueryMsg = new InQueryMsg(1348831860, "query", key);
		inQueryMsg.setQueryName("getAnnounceList");
		HashMap<String, String> map = new HashMap<>();
		map.put("getAnnounceList", "公告列表");
		inQueryMsg.setQueryPara(map);
		String postData = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			postData = mapper.writeValueAsString(inQueryMsg);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		System.out.println("发送前的明文：" + postData);
		Log.e("NoticeRep", "发送前的明文：" + postData);
		RequestQueue requestQueue = RequestUtil.getRequestQueue();

		JsonRequest jsonRequest = new JsonStringRequest(Request.Method.POST, url, postData,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						//Log.d("TAG", "response -> " + response.toString());
						Log.e("NoticeRep", "response -> " + response.toString());
						//System.out.println("解密后：" + response.toString());

						list = JsonUtil.getAnnounceList(response.toString());
						System.out.println("list:" + list);

						getmData(mData);//将获得的数据打包成一个数据集
						// 拆分数据
						for (int i = 0; i < list.size(); i++) {
							String sendtime = list.get(i).getSendtime();
							String title = list.get(i).getTitle();
							String name = list.get(i).getName();
							int atype = list.get(i).getAtype();
							int id = list.get(i).getId();

							T_Announce lst = new T_Announce(id, sendtime, title, name, atype);
							lists.add(lst);
						}
						adapter = new NoticeAnnouncementAdapter(mData, getApplication());
						listView.setAdapter(adapter);

						if(adapter.getCount() == 0){
							notice_background.setVisibility(View.VISIBLE);
						}else{
							notice_background.setVisibility(View.GONE);
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


	// 公告详情
	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
			int id = lists.get(position).getId();// 拿到点击的公告id
			Intent intent = new Intent(NoticeAnnouncementActivity.this,NoticeAnnouncementDetails.class);
			Bundle bundle = new Bundle();
			bundle.putInt("id",id);
			intent.putExtras(bundle);

			startActivity(intent);
		}
	};

	Runnable eChanged = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			String data = editText.getText().toString();
			mData.clear();
			mData = getmDataSub(mData, data);
			if (adapter!=null) {
				adapter.notifyDataSetChanged();
			}
			//listViewinit();
		}
	};

	private ArrayList<Map<String, Object>> getmDataSub(ArrayList<Map<String, Object>> mDataSubs, String data)
	{
		int length = mListTitle.size();//获取当前列表的全部列数
		for(int i = 0; i < length; ++i){
			if(mListTitle.get(i).contains(data) || mListName.get(i).contains(data)
					|| mListDate.get(i).contains(data)){//在当前页面任何项包含输入的值则将该项添加进显示列表
				Map<String,Object> item = new HashMap<String,Object>();
				item.put("title",mListTitle.get(i));
				item.put("name",mListName.get(i));
				item.put("time",mListDate.get(i));
				item.put("atype",mListAtype.get(i));
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
			mListDate.add(list.get(i).getSendtime());
			mListName.add(list.get(i).getName());
			mListAtype.add(list.get(i).getAtype());
			mListId.add(list.get(i).getId());
			item.put("title", mListTitle.get(i));
			item.put("time", mListDate.get(i));
			item.put("name", mListName.get(i));
			item.put("atype", mListAtype.get(i));
			item.put("id",mListId.get(i));
			mDatas.add(item);
			item = new HashMap<String, Object>();
		}
	}

}
