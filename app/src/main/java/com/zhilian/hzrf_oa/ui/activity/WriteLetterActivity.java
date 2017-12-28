package com.zhilian.hzrf_oa.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.adapter.SelectPersonAdapter;
import com.zhilian.hzrf_oa.adapter.SelectmanAdapter;
import com.zhilian.hzrf_oa.adapter.TextAdapter;
import com.zhilian.hzrf_oa.common.BusinessContant;
import com.zhilian.hzrf_oa.entity.SelectPerson;
import com.zhilian.hzrf_oa.entity.Test;
import com.zhilian.hzrf_oa.json.MailDetail;
import com.zhilian.hzrf_oa.json.PersonnalADepartment;
import com.zhilian.hzrf_oa.json.T_FJList;
import com.zhilian.hzrf_oa.json.T_Selectman;
import com.zhilian.hzrf_oa.util.LogUtil;
import com.zhilian.hzrf_oa.util.OpenFileIntent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhilian.api.InQueryMsg;
import com.zhilian.api.InSaveMsg;
import com.zhilian.api.JsonStringRequest;
import com.zhilian.api.JsonUtil;
import com.zhilian.api.RequestUtil;
import com.zhilian.api.StrKit;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by YiFan
 * 写信
 */
public class WriteLetterActivity extends Activity implements OnClickListener{
	int m_ClickNum = 0; // 点击数标志
	private static final int FILE_SELECT_CODE = 0;
	TextView back;// 取消
	TextView send;// 发送
	TextView oldinfo;//转发或是回复时使用
	LinearLayout receiver_choose;// 选择收件人
	EditText receiver;// 收件人
	LinearLayout copyer_choose;// 选择抄送人
	EditText copyer;// 抄送人
	EditText title;// 主题
	EditText content;// 正文
	ListView oldfj_list;

	String receiver_id="";// 选中的收件人id
	String copyer_id="";// 选中的抄送人id

	/*********************************************************/
	MailDetail detail;
	ListView e_listView;// 选择公开人员
	ListView relistView;// 选择收件人
	ListView colistView;//选择抄送人
	SelectPersonAdapter adapter;
	SelectmanAdapter mansAdapter;
	private List<T_Selectman> selectmenlist = new ArrayList<T_Selectman>();
	List<T_FJList> oldlist;
	TextAdapter oldfjAdapter;
	private final static String ALBUM_PATH = "/mnt/sdcard/mypic_data/";
	ArrayList<HashMap<String, Object>> listData = new ArrayList<HashMap<String, Object>>();

	BusinessContant bc=new BusinessContant();
	String type="";
	//ScopePersonnelAdapter scopeAdapter;// 选择公开人员的适配器
	List<PersonnalADepartment> group = new ArrayList<PersonnalADepartment>();// 父层（科室）
	List<Test> child = new ArrayList<Test>();// 子层（人员）
	List<List<Test>> total = new ArrayList<List<Test>>();// 子层（人员分类）

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_letter_layout);

		initView();// 初始化控件
		if (bc.getModel() !=null){
			detail=bc.getModel();
			Intent intent = this.getIntent();
			type = intent.getStringExtra("type");
			if (type.equals("reply")){
				addReplyInfo();
			}else{
				addTransportInfo();
			}

		}
		getPersonnalInfo();
	}

	private void createAlertDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		AlertDialog dialog = null;// 对话框对象

		builder.setTitle("离开写邮件");// 标题
		//builder.setIcon(R.drawable.ic_launcher);
		builder.setMessage("已填写的邮件内容将丢失，或保存至草稿");
		builder.setCancelable(false);// 不能取消这个对话框，点击了对话框里的按钮会自动关闭

		builder.setPositiveButton("离开", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		builder.setNegativeButton("保存草稿", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				savedata(0,3,receiver_id,receiver.getText().toString(),copyer_id,
						copyer.getText().toString(),title.getText().toString(),
						content.getText().toString());
			}
		});

		builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});

		dialog = builder.create();
		dialog.show();
	}

	private void initView() {
		back = (TextView) findViewById(R.id.back);// 取消
		send = (TextView) findViewById(R.id.send);// 发送
		receiver_choose = (LinearLayout) findViewById(R.id.receiver_choose);// 选择收件人
		receiver = (EditText) findViewById(R.id.receiver);// 收件人
		copyer_choose = (LinearLayout) findViewById(R.id.copyer_choose);// 选择抄送人
		copyer = (EditText) findViewById(R.id.copyer);// 抄送人
		title = (EditText) findViewById(R.id.title);// 主题
		content = (EditText) findViewById(R.id.content);// 正文
		oldinfo=(TextView)findViewById(R.id.oldinfo);
		oldfj_list=(ListView)findViewById(R.id.oldfj_list);
		oldfj_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
				final String name = oldlist.get(position).getName();
				String url = oldlist.get(position).getUrl();
				final String targeturl = bc.DOWNLOADURL +name;

				new Thread(new Runnable() {
					@Override
					public void run() {
						URL url;
						try {
							url = new URL(targeturl);
							InputStream is=null;
							try{
								is = url.openStream();
							}catch (IOException e){
								e.printStackTrace();
								Looper.prepare();
								Toast.makeText(WriteLetterActivity.this,"没有找到文件！",Toast.LENGTH_SHORT).show();
								Looper.loop();
							}
							byte[] arr = new byte[1];
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							BufferedOutputStream bos = new BufferedOutputStream(baos);
							int n = is.read(arr);
							while (n > 0) {
								bos.write(arr);
								n = is.read(arr);
							}
							bos.close();
							String path = ALBUM_PATH+name;

							File file = new File(ALBUM_PATH);
							if (!file.exists()){
								file.mkdirs();
							}
							FileOutputStream fos = new FileOutputStream(path);
							fos.write(baos.toByteArray());
							fos.close();
							Intent it=new Intent();
							if (path.substring(path.length() - 4).equals(".pdf")) {
								it = OpenFileIntent.getPdfFileIntent(path);
							}else if(path.substring(path.length() - 4).equals("xlsl")||path.substring(path.length() - 4).equals(".doc")){
								it = OpenFileIntent.getWordFileIntent(path);
							}else {
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
		});
		if(StrKit.isBlank(receiver.getText().toString())){
			send.setOnClickListener(null);
			send.setTextColor(Color.parseColor("#9C9C9C"));
		} else {
			send.setOnClickListener(WriteLetterActivity.this);
			send.setTextColor(Color.parseColor("#ffffff"));

		}
		/*****************************************************************/
		back.setOnClickListener(this);
		//send.setOnClickListener(this);
		//receiver_choose.setOnClickListener(this);
		receiver_choose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder1 = new AlertDialog.Builder(WriteLetterActivity.this);
				builder1.setTitle("选择公开人员");
				View view = getLayoutInflater().inflate(R.layout.scope_personnel_layout, null);
				relistView = (ListView) view.findViewById(R.id.select_public_personnel);
				mansAdapter = new SelectmanAdapter(selectmenlist, WriteLetterActivity.this);
				relistView.setAdapter(mansAdapter);
				builder1.setView(view);
				builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//HashMap<Integer, Boolean> state = adapter.state;
						HashMap<Integer, Boolean> state = mansAdapter.state;
						String options = "选择的项是:";
						String data = "";
						for (int j = 0; j < mansAdapter.getCount(); j++) {
							LogUtil.i("i","state.get(" + j + ")==" + state.get(j));
							if (state.get(j) != null) {
								String username = selectmenlist.get(j).getName();
								String id = String.valueOf(selectmenlist.get(j).getId());
								options += "\n" + id + "." + username;
								data += "," + username;
								receiver_id += "," + id;
							}
						}
						//显示选择内容
						if (data.length() > 1) {

							receiver.setText(data.substring(1));
						}else{
							receiver.setText("");
						}
						//Toast.makeText(getApplicationContext(), options, Toast.LENGTH_LONG).show();
					}
				});
				builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						/*Toast.makeText(WriteLetterActivity.this, "取消", Toast.LENGTH_SHORT)
								.show();*/
					}
				});

				builder1.create().show();

			}
		});
		//copyer_choose.setOnClickListener(this);
		copyer_choose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder1 = new AlertDialog.Builder(WriteLetterActivity.this);
				builder1.setTitle("选择公开人员");
				View view = getLayoutInflater().inflate(R.layout.scope_personnel_layout, null);
				relistView = (ListView) view.findViewById(R.id.select_public_personnel);
				mansAdapter = new SelectmanAdapter(selectmenlist, WriteLetterActivity.this);
				relistView.setAdapter(mansAdapter);
				builder1.setView(view);
				builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//HashMap<Integer, Boolean> state = adapter.state;
						HashMap<Integer, Boolean> state = mansAdapter.state;
						String options = "选择的项是:";
						String data = "";
						for (int j = 0; j < mansAdapter.getCount(); j++) {
							LogUtil.i("i","state.get(" + j + ")==" + state.get(j));
							if (state.get(j) != null) {
								String username = selectmenlist.get(j).getName();
								String id = String.valueOf(selectmenlist.get(j).getId());
								options += "\n" + id + "." + username;
								data += "," + username;
								copyer_id += "," + id;
							}
						}
						//显示选择内容
						if (data.length() > 1) {

							copyer.setText(data.substring(1));
						}else{
							copyer.setText("");
						}
						//Toast.makeText(getApplicationContext(), options, Toast.LENGTH_LONG).show();
					}
				});
				builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						/*Toast.makeText(WriteLetterActivity.this, "取消", Toast.LENGTH_SHORT)
								.show();*/
					}
				});

				builder1.create().show();
			}
		});
		receiver.addTextChangedListener(new TextWatcher() {

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
				/**这是文本框改变之后 会执行的动作*/
				if(s.length() == 0){
					send.setOnClickListener(null);
					send.setTextColor(Color.parseColor("#9C9C9C"));
					back.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							finish();
						}
					});
				} else {
					send.setOnClickListener(WriteLetterActivity.this);
					send.setTextColor(Color.parseColor("#ffffff"));
					back.setOnClickListener(WriteLetterActivity.this);
				}

			}
		});


	}

	/**
	 * 事件监听
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.back:
				if(StrKit.isBlank(receiver.getText().toString())){
					finish();
				}else {
					createAlertDialog();
				}
				break;
			case R.id.send:// 发送
				/*LogUtil.i("i","收件人id--------->"+receiver_id);
				LogUtil.i("i","收件人--------->"+receiver.getText().toString());
				LogUtil.i("i","抄送人id--------->"+copyer_id);
				LogUtil.i("i","抄送人--------->"+copyer.getText().toString());
				LogUtil.i("i","主题--------->"+title.getText().toString());
				LogUtil.i("i","正文--------->"+content.getText().toString());*/
				savedata(0,2,receiver_id,receiver.getText().toString(),copyer_id,
						copyer.getText().toString(),title.getText().toString(),
						content.getText().toString());
				finish();
				break;
			case R.id.receiver_choose:// 选择收件人
				//getPersonnalADepartment();// 拿到选择公开人员的数据（后续用！）
				//getPersonnalInfo();// 拿到所有联系人
				AlertDialog.Builder builder = new AlertDialog.Builder(WriteLetterActivity.this);
				builder.setTitle("选择收件人");
				View view = getLayoutInflater().inflate(R.layout.scope_personnel_layout, null);

				e_listView = (ListView) view.findViewById(R.id.select_public_personnel);

				adapter = new SelectPersonAdapter(WriteLetterActivity.this, listData);
				e_listView.setAdapter(adapter);
				/*scopeAdapter = new ScopePersonnelAdapter(group,total,WriteLetterActivity.this);
				e_listView.setAdapter(scopeAdapter);// 后续用！*/

				builder.setView(view);

				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						HashMap<Integer, Boolean> state = adapter.state;
						String options = "选择的项是:";
						String data_username = "";
						String data_id = "";
						for (int j = 0; j < adapter.getCount(); j++) {
							LogUtil.i("i","state.get(" + j + ")==" + state.get(j));
							if (state.get(j) != null) {
								@SuppressWarnings("unchecked")
								HashMap<String, Object> map = (HashMap<String, Object>) adapter.getItem(j);
								String username = map.get("friend_username").toString();
								String id = map.get("friend_id").toString();
								options += "\n" + id + "." + username;

								data_username += "," + username;
								data_id += "," + id;
							}
						}
						receiver_id = data_id.substring(1);
						//显示选择内容
						receiver.setText(data_username.substring(1));
						Toast.makeText(getApplicationContext(), options, Toast.LENGTH_LONG).show();
					}
				});

				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Toast.makeText(WriteLetterActivity.this, "取消", Toast.LENGTH_SHORT)
								.show();
					}
				});

				builder.create().show();
				break;
			case R.id.copyer_choose:// 选择抄送人
				//getPersonnalADepartment();// 拿到选择公开人员的数据（后续用！）
				//getPersonnalInfo();// 拿到所有联系人
				AlertDialog.Builder builder1 = new AlertDialog.Builder(WriteLetterActivity.this);
				builder1.setTitle("选择抄送人");
				View view1 = getLayoutInflater().inflate(R.layout.scope_personnel_layout, null);

				e_listView = (ListView) view1.findViewById(R.id.select_public_personnel);

				adapter = new SelectPersonAdapter(WriteLetterActivity.this, listData);
				e_listView.setAdapter(adapter);
				/*scopeAdapter = new ScopePersonnelAdapter(group,total,WriteLetterActivity.this);
				e_listView.setAdapter(scopeAdapter);// 后续用！*/

				builder1.setView(view1);

				builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						HashMap<Integer, Boolean> state = adapter.state;
						String options = "选择的项是:";
						String data_username = "";
						String data_id = "";
						for (int j = 0; j < adapter.getCount(); j++) {
							LogUtil.i("i","state.get(" + j + ")==" + state.get(j));
							if (state.get(j) != null) {
								@SuppressWarnings("unchecked")
								HashMap<String, Object> map = (HashMap<String, Object>) adapter.getItem(j);
								String username = map.get("friend_username").toString();
								String id = map.get("friend_id").toString();
								options += "\n" + id + "." + username;

								data_username += "," + username;
								data_id += "," + id;
							}
						}
						copyer_id = data_id;
						//显示选择内容
						copyer.setText(data_username.substring(1));
						Toast.makeText(getApplicationContext(), options, Toast.LENGTH_LONG).show();
					}
				});

				builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Toast.makeText(WriteLetterActivity.this, "取消", Toast.LENGTH_SHORT)
								.show();
					}
				});

				builder1.create().show();
				break;
		}
	}

	// 保存记事本的事件内容
	private void savedata(int id ,int boxId, String receiverId, String receiver, String copyerId,
						  String copyer, String title, String content) {
		BusinessContant bc = new BusinessContant();
		String key = bc.getCONFIRM_ID();
		String url = bc.URL;
		InSaveMsg inSaveMsg = new InSaveMsg(1348831860, "save", key);
		inSaveMsg.setModelName("t_Mail");
		HashMap<String, String> map = new HashMap<>();
		map.put("id", String.valueOf(id));
		System.out.print("receiverId="+receiverId);
		map.put("boxId", String.valueOf(boxId));
		LogUtil.i("i","boxid------->"+boxId);
		map.put("isRead","0");
		map.put("receiverId", receiverId.substring(1));// 收件人id
		map.put("receiver", receiver);// 收件人
		if (copyerId.length()>1){
			map.put("copyerId", copyerId.substring(1));// 抄送人id
		}
		map.put("copyer", copyer);// 抄送人
		map.put("title", title);// 标题
		map.put("content", content);// 正文
		if (StrKit.notBlank(type)?false:type.equals("reply")){
			map.put("content", content+"\n\n------------------ 原始邮件 ------------------\n"+"发件人:"+detail.getReceiver()+"\n 发送时间:"
					+detail.getSendTime()+"\n收件人:"+detail.getFromname()+ "\n抄送:"+detail.getCopyer()+"\n正文："+detail.getContent());// 正文
		}



		inSaveMsg.setModelProperty(map);
		String postData = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			postData = mapper.writeValueAsString(inSaveMsg);
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
						Toast.makeText(WriteLetterActivity.this,response.toString(),Toast.LENGTH_SHORT).show();
						/*ObjectMapper objectMapper = new ObjectMapper();
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
						}*/

					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
				Toast.makeText(WriteLetterActivity.this, "出错了!", Toast.LENGTH_LONG).show();
			}
		});

		requestQueue.add(jsonRequest);
	}

	/**
	 * 拿到选择公开人员的数据（科室和联系人）*** 后续优化再采用这种方法
	 ***/
	private void getPersonnalADepartment() {
		BusinessContant bc = new BusinessContant();
		String key = bc.getCONFIRM_ID();
		String url = bc.URL;
		InQueryMsg inQueryMsg = new InQueryMsg(1348831860, "query", key);
		inQueryMsg.setQueryName("PersonnalADepartment");
		HashMap<String, String> map = new HashMap<>();
		map.put("PersonnalADepartment", "选择公开人员");
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

						List<PersonnalADepartment> list = JsonUtil.getPersonnalADepartment(response.toString());

						LogUtil.i("i","list-->" + list.toString());

						int id, d_id;
						String name, sname;
						List<Test> lists;

						for (int i = 0; i < list.size(); i++) {// 科室数据（父层）
							d_id = list.get(i).getD_id();
							sname = list.get(i).getSname();
							PersonnalADepartment p1 = new PersonnalADepartment(d_id, sname);
							group.add(p1);
						}
						LogUtil.i("i","拿到的所有科室-->" + group.toString());

						//lists = list.get(1).getFname();
						//LogUtil.i("i","listlist----->"+lists.toString());

						for (int j = 0; j < list.size(); j++) {// 对应科室的联系人（子层）

							lists = list.get(j).getFname();

							child = new ArrayList<Test>();
							for (int i = 0; i < lists.size(); i++) {
								//LogUtil.i("i","size()----->"+lists.size());
								id = (Integer) (((Map) lists.get(i)).get("id"));
								name = (String) ((Map) lists.get(i)).get("name");
								Test t = new Test(id, name);
								child.add(t);
							}
							total.add(j, child);

							LogUtil.i("i","sssssssssss-->" + total.toString());

							//LogUtil.i("i","sssssssssss-->"+total.toString());

						}

						LogUtil.i("i","sssssssssss-->" + total.toString());
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
				Toast.makeText(WriteLetterActivity.this, "出错了!", Toast.LENGTH_LONG).show();
			}
		});
		requestQueue.add(jsonRequest);
	}

	// 拿到选择联系人的数据
	private void getPersonnalInfo() {
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
		LogUtil.i("i","发送前的明文：" + postData);
		RequestQueue requestQueue = RequestUtil.getRequestQueue();

		JsonRequest jsonRequest = new JsonStringRequest(Request.Method.POST, url, postData,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d("TAG", "response -> " + response.toString());
						LogUtil.i("i","解密后：" + response.toString());

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
							name[i] = names;
							pname[i] = pnames;
							sname[i] = snames;
							id[i] = String.valueOf(ids);
						}

						for (int i = 0; i < list.size(); i++) {
							HashMap<String, Object> map = new HashMap<String, Object>();
							selectmenlist.add(new T_Selectman(Integer.parseInt(id[i]), name[i], pname[i], sname[i],false));

						}


						/*for (int i = 0; i < list.size(); i++) {
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("friend_image", R.drawable.icon_select_person);
							map.put("friend_username", name[i]);
							map.put("friend_id", id[i]);
							map.put("selected", false);
							//添加数据
							listData.add(map);
						}*/



						/*adapter = new SelectPersonAdapter(WriteLetterActivity.this, listData);
						e_listView.setAdapter(adapter);*/


					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
				Toast.makeText(WriteLetterActivity.this, "出错了!", Toast.LENGTH_LONG).show();
			}
		});
		requestQueue.add(jsonRequest);
	}

	public void addReplyInfo(){

		receiver.setText(detail.getFromname());
		receiver_id=detail.getFromId();//回复时将原本的发件人转为发件人
		System.out.print("receiver_id="+receiver_id);
		title.setText("Re:"+detail.getTitle());
		oldinfo.setVisibility(View.VISIBLE);
		oldinfo.setText(detail.getContent());
		oldfj_list.setVisibility(View.VISIBLE);
		oldlist=detail.getFjid();
		oldfjAdapter = new TextAdapter(oldlist, getApplicationContext());
		oldfj_list.setAdapter(oldfjAdapter);
		if (oldlist.size() > 0) {
			setListViewHeightBasedOnChildren(oldfj_list);
		}

	}

	public void addTransportInfo(){
		title.setText("Re:"+detail.getTitle());
		oldinfo.setVisibility(View.VISIBLE);
		oldinfo.setText(detail.getContent());
		oldfj_list.setVisibility(View.VISIBLE);
		oldlist=detail.getFjid();
		oldfjAdapter = new TextAdapter(oldlist, getApplicationContext());
		oldfj_list.setAdapter(oldfjAdapter);
		if (oldlist.size() > 0) {
			setListViewHeightBasedOnChildren(oldfj_list);
		}
	}

	public void setListViewHeightBasedOnChildren(ListView listView) {//计算listView所有子item的高

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

	private void hideKeyBoard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		// 得到InputMethodManager的实例
		if (imm.isActive()) {
			// 如果开启
			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
					InputMethodManager.HIDE_NOT_ALWAYS);
			// 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
		}
	}

}
