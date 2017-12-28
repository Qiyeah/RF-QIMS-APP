package com.zhilian.hzrf_oa.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.adapter.NotepadAdapter;
import com.zhilian.hzrf_oa.common.BusinessContant;
import com.zhilian.hzrf_oa.entity.NotepadBeen;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhilian.api.InDeleteMsg;
import com.zhilian.api.InQueryMsg;
import com.zhilian.api.JsonStringRequest;
import com.zhilian.api.JsonUtil;
import com.zhilian.api.RequestUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 记事本
 */
public class NotepadActivity extends Activity implements View.OnClickListener{
	private ListView listView;// 记事本列表
	private NotepadAdapter adapter;// 列表的适配器
	private List<NotepadBeen> lists = new ArrayList<NotepadBeen>();// 列表数据
	private Handler myhandler=new Handler();

	private EditText editText;
	private ImageView ivDelete;

	List<NotepadBeen> list;
	ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
	ArrayList<String> mListTitle = new ArrayList<String>();
	ArrayList<String> mListContent = new ArrayList<String>();
	ArrayList<String> mListDate = new ArrayList<String>();

	ArrayList<Integer> mListId = new ArrayList<Integer>();

	private LinearLayout relativeLayout1;
	private Dialog alertDialog;
	private LayoutInflater inflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notepad_layout);

		initView();// 初始化控件

	}

	@Override
	protected void onResume() {
		super.onResume();
		getNoteList();// 记事本列表数据
	}

	private void initView() {
		relativeLayout1 = (LinearLayout) findViewById(R.id.relativeLayout1);

		Button button1 = (Button) findViewById(R.id.button1);
		Button button2 = (Button) findViewById(R.id.button2);
		Button button3 = (Button) findViewById(R.id.button3);
		Button button4 = (Button) findViewById(R.id.button4);
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		button4.setOnClickListener(this);
		editText=(EditText)findViewById(R.id.searchtext);
		ivDelete=(ImageView)findViewById(R.id.ivDeleteText);
		listView = (ListView) findViewById(R.id.list_notepad);
		listView.setOnItemClickListener(itemListener);
		listView.setOnItemLongClickListener(longListener);

		ImageView back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		ImageView add_event = (ImageView) findViewById(R.id.add_event);
		add_event.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NotepadActivity.this,NoteDetailActivity.class);
				Bundle bundele = new Bundle();
				bundele.putInt("id",0);
				intent.putExtras(bundele);

				startActivity(intent);
			}
		});

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

	// 点击记事本列表item的点击事件
	private AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
			/*ListView listView = (ListView) parent;
			int id = listView.getId();
			switch (id) {
				case R.id.list_notepad:
					NotepadAdapter.ViewHolder holder = (NotepadAdapter.ViewHolder) view.getTag();
					// 改变CheckBox的状态
					holder.cb.toggle();
					adapter.getIsSelectedMap()
							.put(position, true);
					break;

				default:
					break;
			}*/

			int id1 = lists.get(position).getId();
			Intent intent = new Intent(NotepadActivity.this,NoteDetailActivity.class);

			Bundle bundle = new Bundle();
			bundle.putInt("id",id1);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	};

	// 点击记事本列表item的长按事件
	private AdapterView.OnItemLongClickListener longListener = new AdapterView.OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long arg3) {
			AlertDialog.Builder builder = new AlertDialog.Builder(NotepadActivity.this);

			builder.setMessage("确认删除吗");
			builder.setTitle("提示");
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					arg0.dismiss();
				}
			});
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub

					int id = lists.get(position).getId();
					System.out.println("id----->"+id);
					deleteData(id);
					arg0.dismiss();

				}
			});
			builder.create().show();
			/*ListView listView = (ListView) parent;
			int id = listView.getId();
			switch (id) {
				case R.id.list_notepad:
					relativeLayout1.setVisibility(View.VISIBLE);
					for (int i = 0; i < lists.size(); i++) {
						adapter.getIsvisibleMap().put(i,
								CheckBox.VISIBLE);
					}
					adapter.notifyDataSetChanged();
					break;
				default:
					break;
			}*/

			return true;
		}
	};

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.button1:// 全选
				for (int i = 0; i < lists.size(); i++) {
					adapter.getIsSelectedMap().put(i,
							true);
				}
				adapter.notifyDataSetChanged();
				break;
			case R.id.button2:// 反选
				for (int i = 0; i < lists.size(); i++) {
					if (adapter.getIsSelectedMap().get(i)) {
						adapter.getIsSelectedMap().put(i, false);
					} else {
						adapter.getIsSelectedMap().put(i, true);
					}
				}
				adapter.notifyDataSetChanged();
				break;
			case R.id.button3:// 取消
				relativeLayout1.setVisibility(View.GONE);
				for (int i = 0; i < lists.size(); i++) {
					adapter.getIsSelectedMap().put(i,
							false);
					adapter.getIsvisibleMap().put(i,
							CheckBox.INVISIBLE);
				}

				adapter.notifyDataSetChanged();
				break;
			case R.id.button4:// 删除

				alertDialog = new AlertDialog.Builder(this)
						.setTitle("确定删除？")
						.setMessage("您确定删除所选信息？")
						//.setIcon(R.drawable.lianxiren)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
														int arg1) {
										int len = lists.size();
										for (int i = len - 1; i >= 0; i--) {
											Boolean value = adapter.getIsSelectedMap().get(i);
											if (value) {
												lists.remove(i);
												adapter.getIsSelectedMap().put(i,
														false);
											}
										}
										adapter.notifyDataSetChanged();
										alertDialog.cancel();
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
														int arg1) {
										alertDialog.cancel();
									}
								}).create();
				alertDialog.show();
				break;

			default:
				break;
		}
	}

	// 拿到记事本列表数据
	private void getNoteList() {
		BusinessContant bc = new BusinessContant();
		String key = bc.getCONFIRM_ID();
		String url = bc.URL;
		InQueryMsg inQueryMsg = new InQueryMsg(1348831860, "query", key);
		inQueryMsg.setQueryName("getNoteList");
		HashMap<String, String> map = new HashMap<>();
		map.put("NoteList", "记事本列表");
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

						list = JsonUtil.getNoteList(response.toString());
						System.out.println("list:" + list);
						getmData(mData);
						for (int i = 0; i < list.size(); i++) {
							int id = list.get(i).getId();
							String title = list.get(i).getTitle();
							String content = list.get(i).getContent();
							String wdate = list.get(i).getWdate();

							NotepadBeen nb = new NotepadBeen(id, title, content, wdate);
							lists.add(nb);
						}
						adapter = new NotepadAdapter(mData, NotepadActivity.this);
						listView.setAdapter(adapter);

					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
				Toast.makeText(NotepadActivity.this, "出错了!", Toast.LENGTH_LONG).show();
			}
		});

		requestQueue.add(jsonRequest);
	}

	// 删除记事本的事件
	private void deleteData(int id){
		BusinessContant bc = new BusinessContant();
		String key = bc.getCONFIRM_ID();
		String url = bc.URL;
		InDeleteMsg inDeleteMsg = new InDeleteMsg(1348831860, "delete",key);
		inDeleteMsg.setModelName("t_Note");
		/*HashMap<String, String> map = new HashMap<>();
		map.put("id", String.valueOf(id));*/

		inDeleteMsg.setEntityId(id+"");
		String postData = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			postData = mapper.writeValueAsString(inDeleteMsg);
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

						Toast.makeText(NotepadActivity.this,response.toString(),Toast.LENGTH_SHORT).show();

					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
				Toast.makeText(NotepadActivity.this, "出错了!", Toast.LENGTH_LONG).show();
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
		int length = mListTitle.size();//获取当前列表的全部列数
		for(int i = 0; i < length; ++i){
			if(mListTitle.get(i).contains(data) || (mListContent.get(i)==null?false:mListContent.get(i).contains(data))
					|| mListDate.get(i).contains(data)||mListDate.get(i).contains(data)){//在当前页面任何项包含输入的值则将该项添加进显示列表
				Map<String,Object> item = new HashMap<String,Object>();
				item.put("title", mListTitle.get(i));
				item.put("content", mListContent.get(i));
				item.put("date", mListDate.get(i));
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
			mListContent.add(list.get(i).getContent());
			mListDate.add(list.get(i).getWdate());
			mListId.add(list.get(i).getId());
			item.put("title", mListTitle.get(i));
			item.put("content", mListContent.get(i));
			item.put("date", mListDate.get(i));
			item.put("id",mListId.get(i));
			mDatas.add(item);
			item = new HashMap<String, Object>();
		}

	}

}
