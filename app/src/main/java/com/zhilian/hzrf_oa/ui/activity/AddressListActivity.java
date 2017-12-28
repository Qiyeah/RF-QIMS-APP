package com.zhilian.hzrf_oa.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.zhilian.hzrf_oa.adapter.SortAdapter;
import com.zhilian.hzrf_oa.common.BusinessContant;
import com.zhilian.hzrf_oa.entity.SelectPerson;
import com.zhilian.hzrf_oa.entity.SortModel;
import com.zhilian.hzrf_oa.ui.widget.SideBar;
import com.zhilian.hzrf_oa.util.CharacterParser;
import com.zhilian.hzrf_oa.util.PinyinComparator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhilian.api.InQueryMsg;
import com.zhilian.api.JsonStringRequest;
import com.zhilian.api.JsonUtil;
import com.zhilian.api.RequestUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * 通讯录
 */
public class AddressListActivity extends Activity {
	private ListView sortListView;// 联系人列表
	private SideBar sideBar;// 侧面的索引
	private TextView dialog;// 点击索引显示的字母
	private SortAdapter adapter;// 联系人列表的适配器
	//private ClearEditText mClearEditText;// 搜索框

	private CharacterParser characterParser;// 汉字转换成拼音的类
	private List<SortModel> SourceDateList;// 联系人的数据

	private PinyinComparator pinyinComparator;// 根据拼音来排列ListView里面的数据类

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.address_list_layout);

		initViews();// 初始化
	}

	private void initViews() {
		//实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) findViewById(R.id.sidrbar);// 侧面的索引
		dialog = (TextView) findViewById(R.id.dialog);// 点击索引显示的文字
		sideBar.setTextView(dialog);// 显示点击的索引

		ImageView back = (ImageView) findViewById(R.id.back);// 返回
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// 设置侧面索引触摸监听
		sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
			@Override
			public void onTouchingLetterChanged(String s) {
				//该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if(position != -1){
					sortListView.setSelection(position);
				}

			}
		});

		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				// 截取出数据后面的联系人id
				String data = ((SortModel)adapter.getItem(position)).getName();
				String [] str = data.split("）");
				String text = str[1];
				// 跳转，并将联系人id传到 联系人详情页面
				Intent intent = new Intent(AddressListActivity.this,AddressListDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("id",text);
				intent.putExtras(bundle);
				startActivity(intent);

				//Toast.makeText(getApplication(), ((SortModel)adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
			}
		});


		/**
		 * 请求数据，拿到联系人
		 */
		BusinessContant bc = new BusinessContant();
		String key = bc.getCONFIRM_ID();
		String url = bc.URL;
		InQueryMsg inQueryMsg = new InQueryMsg(1348831860, "query", key);
		inQueryMsg.setQueryName("PersonnalInfo");
		HashMap<String, String> map = new HashMap<>();
		map.put("getPersonnalInfo", "选择联系人");
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

						List<SelectPerson> list = JsonUtil.getPersonnalInfo(response.toString());

						int ids;
						String names,pnames,snames;
						String[] name = new String[list.size()];
						String[] sname = new String[list.size()];
						String[] pname = new String[list.size()];
						String[] id = new String[list.size()];

						for (int i = 0; i < list.size(); i++) {
							ids = list.get(i).getId();
							names = list.get(i).getName();
							snames = list.get(i).getSname();
							pnames = list.get(i).getPname();
							name[i] = names+"（"+snames+"："+pnames+"）"+ids;
							pname[i] = pnames;
							sname[i] = snames;
							id[i] = String.valueOf(ids);
						}
						SourceDateList = filledData(name);
						// 根据a-z进行排序源数据
						Collections.sort(SourceDateList, pinyinComparator);
						adapter = new SortAdapter(AddressListActivity.this, SourceDateList);
						sortListView.setAdapter(adapter);

					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
				Toast.makeText(AddressListActivity.this, "出错了!", Toast.LENGTH_LONG).show();
			}
		});
		requestQueue.add(jsonRequest);


		//mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
		// 根据输入框输入值的改变来过滤搜索
		/*mClearEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});*/
	}

	/**
	 * 为ListView填充数据
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(String [] date){
		List<SortModel> mSortList = new ArrayList<SortModel>();

		for(int i=0; i<date.length; i++){
			SortModel sortModel = new SortModel();
			sortModel.setName(date[i]);
			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if(sortString.matches("[A-Z]")){
				sortModel.setSortLetters(sortString.toUpperCase());
			}else{
				sortModel.setSortLetters("#");
			}

			mSortList.add(sortModel);
		}
		return mSortList;

	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * @param filterStr
	 */
	private void filterData(String filterStr){
		List<SortModel> filterDateList = new ArrayList<SortModel>();

		if(TextUtils.isEmpty(filterStr)){
			filterDateList = SourceDateList;
		}else{
			filterDateList.clear();
			for(SortModel sortModel : SourceDateList){
				String name = sortModel.getName();
				if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
					filterDateList.add(sortModel);
				}
			}
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}

}
