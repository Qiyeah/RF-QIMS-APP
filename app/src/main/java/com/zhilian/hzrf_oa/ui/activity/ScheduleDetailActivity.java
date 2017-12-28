package com.zhilian.hzrf_oa.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.adapter.SelectmanAdapter;
import com.zhilian.hzrf_oa.common.BusinessContant;
import com.zhilian.hzrf_oa.entity.SelectPerson;
import com.zhilian.hzrf_oa.entity.Test;
import com.zhilian.hzrf_oa.json.PersonnalADepartment;
import com.zhilian.hzrf_oa.json.SchduleList;
import com.zhilian.hzrf_oa.json.T_Selectman;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhilian.api.InDeleteMsg;
import com.zhilian.api.InQueryMsg;
import com.zhilian.api.InSaveMsg;
import com.zhilian.api.JsonStringRequest;
import com.zhilian.api.JsonUtil;
import com.zhilian.api.RequestUtil;
import com.zhilian.api.StrKit;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查看 日程详情
 * 修改 日程信息
 * 添加 日程
 */
public class ScheduleDetailActivity extends Activity implements View.OnClickListener,View.OnLayoutChangeListener {
	private String currentDate = "";// 当前日期
	private String receiveids = "";// 公开人员id
	private String receive;// 公开人员
	int m_ClickNum = 0; // 全天事件框点击数
	int id;// 拿到的日程id
	int u_id;// 拿到编辑人id（用户id）

	private int screenHeight = 0;//屏幕高度

	private int keyHeight = 0;//软件盘弹起后所占高度阀值
	String name;// 编辑人
	int scope;// 公开范围（值）
	String tixing;// 提醒（值）
	final static int[] sizeTable = {9, 99, 999, 9999, 99999, 999999, 9999999,
			99999999, 999999999, Integer.MAX_VALUE};
	ImageView iv_back;// 返回
	TextView tv_cancel;// 取消
	TextView tv_save;// 保存
	TextView tv_delete;// 删除
	TextView top_title;// 头部标题

	TextView tv_name;// 编辑人
	TextView tv_nametext;//编辑人提示框
	EditText et_type;// 日程类型
	EditText et_title;// 日程标题
	EditText et_content;// 日程内容
	EditText et_reason;// 日程备注
	Spinner s_remind;// 提醒

	CheckBox cb_event;// 全天事件
	LinearLayout l_event1;// 全天事件（框）
	TextView tv_begin_date;// 开始时间（年月日）
	TextView tv_begin_time;// 开始时间（时分）
	TextView tv_end_date;// 结束时间（年月日）
	TextView tv_end_time;// 结束时间（时分）

	LinearLayout operation;//操作栏
	LinearLayout l_begin_time_i;// 开始选择框（时分）
	LinearLayout l_end_time_i;// 结束选择框（时分）
	LinearLayout l_begin_date_i;// 开始选择框（年月日）
	LinearLayout l_end_date_i;// 结束选择框（年月日）
	LinearLayout activity_layout;

	RadioGroup rg_scope_group;// 公开范围的group
	RadioButton rb_scope1;// 私人
	RadioButton rb_scope2;// 全部人员
	RadioButton rb_scope3;// 指定人员
	LinearLayout l_scope;// 公开人员（框）
	LinearLayout l_scope_personnel;// 公开人员（半框）
	TextView tv_scope_personnel_name;// 公开人员名字
	ExpandableListView e_listView;// 选择公开人员
	//ScopePersonnelAdapter scopeAdapter;// 选择公开人员的适配器
	List<PersonnalADepartment> group = new ArrayList<PersonnalADepartment>();// 父层（科室）
	List<Test> child = new ArrayList<Test>();// 子层（人员）
	List<List<Test>> total = new ArrayList<List<Test>>();// 子层（人员分类）
	private List<T_Selectman> selectmenlist = new ArrayList<T_Selectman>();

	ListView ee_listView;// 选择公开人员
	//SelectPersonAdapter adapter;
	SelectmanAdapter mansAdapter;
	ArrayList<HashMap<String, Object>> listData = new ArrayList<HashMap<String, Object>>();


	public ScheduleDetailActivity() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		currentDate = sdf.format(date);  // 当期日期
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_my_agenda_layout);

		Bundle bundle = getIntent().getExtras();
		id = bundle.getInt("id");// 拿到传入的日程id
		System.out.println("拿到的id--》" + id);

		if (id != 0) {
			getScheduleDetail(id);// 根据传入的日程id，拿到日程详情的数据
		}
		initView();// 初始化控件
		activity_layout=(LinearLayout)findViewById(R.id.activity_layout) ;
		operation=(LinearLayout)findViewById(R.id.operation);
		screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();//获取屏幕高度
		keyHeight = screenHeight / 3;//阀值设置为屏幕高度的1/3
		System.out.print("screen="+screenHeight+",key="+keyHeight);
		getPersonnalInfo();// 选择公开人员
	}

	// 初始化控件
	private void initView() {
		top_title = (TextView) findViewById(R.id.top_title);// 头部标题
		iv_back = (ImageView) findViewById(R.id.back);// 返回
		tv_cancel = (TextView) findViewById(R.id.cancel);// 取消
		tv_save = (TextView) findViewById(R.id.save);// 保存
		tv_delete = (TextView) findViewById(R.id.delete);// 删除
		tv_name = (TextView) findViewById(R.id.name);// 编辑人
		tv_nametext=(TextView) findViewById(R.id.nametext);// 编辑人提示框
		if (id == 0) {// 如果是添加事件
			tv_name.setVisibility(View.GONE);
			tv_nametext.setVisibility(View.GONE);
			tv_delete.setVisibility(View.GONE);// 1、隐藏删除按钮
			top_title.setText("新日程");// 2、头部标题改为"新日程"
		}


		et_type = (EditText) findViewById(R.id.type);// 日程类型
		et_title = (EditText) findViewById(R.id.title);// 日程标题
		et_content = (EditText) findViewById(R.id.content);// 日程内容
		et_reason = (EditText) findViewById(R.id.reason);// 日程备注

		s_remind = (Spinner) findViewById(R.id.remind);// 提醒
		ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource
				(ScheduleDetailActivity.this, R.array.remind1, android.R.layout.simple_spinner_dropdown_item);
		s_remind.setAdapter(adapter1);// 给提醒的选择框加载数据

		rg_scope_group = (RadioGroup) findViewById(R.id.scope_group);// 公开范围的group
		rb_scope1 = (RadioButton) findViewById(R.id.scope1);// 私人
		rb_scope1.setChecked(true);
		rb_scope2 = (RadioButton) findViewById(R.id.scope2);// 全部人员
		rb_scope3 = (RadioButton) findViewById(R.id.scope3);// 指定人员
		l_scope = (LinearLayout) findViewById(R.id.scope);// 公开人员（全框）
		l_scope_personnel = (LinearLayout) findViewById(R.id.scope_personnel);// 公开人员（半框）
		tv_scope_personnel_name = (TextView) findViewById(R.id.scope_personnel_name);// 公开人员（名字）

		l_begin_date_i = (LinearLayout) findViewById(R.id.begin_date_i);// 开始选择框（年月日）
		tv_begin_date = (TextView) findViewById(R.id.begin_date);// 开始时间（年月日）
		l_begin_time_i = (LinearLayout) findViewById(R.id.begin_time_i);// 开始选择框（时分）
		tv_begin_time = (TextView) findViewById(R.id.begin_time);// 开始时间（时分）
		cb_event = (CheckBox) findViewById(R.id.event);// 全天事件
		l_event1 = (LinearLayout) findViewById(R.id.event1);// 全天事件（框）
		l_end_date_i = (LinearLayout) findViewById(R.id.end_date_i);// 结束选择框（年月日）
		tv_end_date = (TextView) findViewById(R.id.end_date);// 结束时间（年月日）
		l_end_time_i = (LinearLayout) findViewById(R.id.end_time_i);// 结束选择框（时分）
		tv_end_time = (TextView) findViewById(R.id.end_time);// 结束时间（时分）

		/*************************************事件监听************************************/

		iv_back.setOnClickListener(this);
		tv_cancel.setOnClickListener(this);
		tv_save.setOnClickListener(this);
		tv_delete.setOnClickListener(this);
		l_begin_date_i.setOnClickListener(this);
		l_end_date_i.setOnClickListener(this);
		l_begin_time_i.setOnClickListener(this);
		l_end_time_i.setOnClickListener(this);
		l_scope.setVisibility(View.GONE);// 刚开始先隐藏掉公开人员（框）
		rb_scope1.setOnClickListener(this);
		rb_scope2.setOnClickListener(this);
		rb_scope3.setOnClickListener(this);
		rb_scope3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				l_scope.setVisibility(View.VISIBLE);// 指定人员选中时，显示公开人员（框）
			}
		});

		l_scope_personnel.setOnClickListener(this);

		// 这段代码可不要（作用：可以点击框来直接选中全天事件的CheckBox）
		l_event1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (m_ClickNum % 2 == 0) {
					cb_event.setChecked(true);
				} else {
					cb_event.setChecked(false);
				}
				m_ClickNum++;
			}
		});

		/**  提醒（Spinner）  选择项的事件监听
		 *   首先判断是否是全天事件，再根据选中项，赋值给要传到后台的数据
		 **/
		s_remind.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (!cb_event.isChecked()) {// 1、不是全天事件
					if (position == 0) {// 根据选择的position,赋值给要传到后台的数据
						tixing = null;
					} else if (position == 1) {
						tixing = String.valueOf(1);
					} else if (position == 2) {
						tixing = String.valueOf(2);
					} else if (position == 3) {
						tixing = String.valueOf(3);
					} else if (position == 4) {
						tixing = String.valueOf(4);
					} else if (position == 5) {
						tixing = String.valueOf(5);
					}
				} else {// 2、是全天事件
					if (position == 0) {// 根据选择的position,赋值给要传到后台的数据
						tixing = null;
					} else if (position == 1) {
						tixing = String.valueOf(1);
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		/**  公开范围（RadioGroup）  选择RadioButton的事件监听
		 *   根据选中项，赋值给要传到后台的数据
		 **/
		rg_scope_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == rb_scope1.getId()) {// 1、选中私人
					scope = 3;
				} else if (checkedId == rb_scope2.getId()) {// 2、选中全部人员
					scope = 0;
				} else if (checkedId == rb_scope3.getId()) {// 3、选中指定人员
					scope = 1;// 这里选择指定人员未完成
				}
			}
		});

		/**  全天事件（CheckBox） 是否选中的事件监听  **/
		cb_event.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override// 全天事件
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {// 1、选中时
					l_begin_time_i.setVisibility(View.GONE);// 隐藏开始时间（时分）
					l_end_time_i.setVisibility(View.GONE);// 隐藏结束时间（时分）

					/*l_begin_date_i.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							DateDialogBegin mdd=new DateDialogBegin();
							mdd.show(ScheduleDetailActivity.this.getFragmentManager(), "Picker_date");
						}
					});
					l_end_date_i.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							DateDialogEnd mdd=new DateDialogEnd();
							mdd.show(ScheduleDetailActivity.this.getFragmentManager(), "Picker_date");
						}
					});*/

					ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource
							(ScheduleDetailActivity.this, R.array.remind2, android.R.layout.simple_spinner_dropdown_item);
					s_remind.setAdapter(adapter2);// 给提醒的选择框加载（全天事件）数据
				} else {// 2、未选中时
					l_begin_time_i.setVisibility(View.VISIBLE);// 显示开始时间（时分）
					l_end_time_i.setVisibility(View.VISIBLE);// 显示结束时间（时分）

					/*l_begin_date_i.setOnClickListener(null);
					l_end_date_i.setOnClickListener(null);*/

					ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource
							(ScheduleDetailActivity.this, R.array.remind1, android.R.layout.simple_spinner_dropdown_item);
					s_remind.setAdapter(adapter1);// 给提醒的选择框加载数据
				}
			}
		});

	}

	/**
	 * 事件监听
	 **/
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.back:// 返回
				finish();
				break;
			case R.id.cancel:// 取消
				finish();
				break;
			case R.id.save:// 保存
				System.out.println("公开范围-------》" + scope);
				// 公开范围是1时，还需要传选择的公开人员（待完善）
				String begin_date = tv_begin_date.getText().toString();
				String begin_time = tv_begin_time.getText().toString();
				String end_date = tv_end_date.getText().toString();
				String end_time = tv_end_time.getText().toString();
				if (!cb_event.isChecked()) {
					end_date = begin_date;
				} else {
					begin_date = tv_begin_date.getText().toString();
					begin_time = tv_begin_time.getText().toString();
					end_date = tv_end_date.getText().toString();
					end_time = tv_end_time.getText().toString();
				}
				if (StrKit.isBlank(et_type.getText().toString())){
					Toast.makeText(this, "请填写日程类型！", Toast.LENGTH_SHORT).show();
					break;
				}
				if (begin_date.equals("0000-00-00") || end_date.equals("0000-00-00")){
					Toast.makeText(this, "请填写日程时间！", Toast.LENGTH_SHORT).show();
					break;
				}
				System.out.println("开始日期——》" + begin_date);
				System.out.println("结束日期——》" + end_date);
				System.out.println("开始时间——》" + begin_time);
				System.out.println("开始时间——》" + end_time);
				System.out.println("提醒——》" + tixing);
				savedata(id, tixing,
						begin_date + " " + begin_time,
						end_date + " " + end_time,
						null, null,
						scope, et_type.getText().toString(), et_title.getText().toString(),
						et_content.getText().toString(), et_reason.getText().toString());
				finish();
				break;
			case R.id.delete:// 删除
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				AlertDialog dialog = null;

				builder.setTitle("提示");// 标题
				//builder.setIcon(R.drawable.ic_launcher);// 图标
				builder.setMessage("确认要删除此日程吗？");// 内容
				builder.setCancelable(false);// 设置为不可取消

				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						deleteData(id);
						finish();

					}
				});

				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Toast.makeText(ScheduleDetailActivity.this, "取消", Toast.LENGTH_SHORT)
								.show();
					}
				});
				dialog = builder.create();
				dialog.show();
				break;
			case R.id.begin_date_i:// 开始时间（年月日）
				DateDialogBegin mdb = new DateDialogBegin();
				mdb.show(ScheduleDetailActivity.this.getFragmentManager(), "Picker_date");
				break;
			case R.id.end_date_i:// 结束时间（年月日）
				DateDialogEnd mde = new DateDialogEnd();
				mde.show(ScheduleDetailActivity.this.getFragmentManager(), "Picker_date");
				break;
			case R.id.begin_time_i:// 开始选择框（时分）
				TimeDialogBegin mtb = new TimeDialogBegin();
				mtb.show(ScheduleDetailActivity.this.getFragmentManager(), "Picker_Time");
				break;
			case R.id.end_time_i:// 结束时间选择框（时分）
				TimeDialogEnd mte = new TimeDialogEnd();
				mte.show(ScheduleDetailActivity.this.getFragmentManager(), "Picker_Time");
				break;
			case R.id.scope3:// 指定人员
				l_scope.setVisibility(View.VISIBLE);// 显示公开人员框
				break;
			case R.id.scope1:// 私人
				l_scope.setVisibility(View.GONE);// 隐藏公开人员框
				break;
			case R.id.scope2:// 全部人员
				l_scope.setVisibility(View.GONE);// 隐藏公开人员框
				break;
			case R.id.scope_personnel:// 选择公开人员
				//getPersonnalADepartment();// 拿到选择公开人员的数据（后续用！）
				//getPersonnalInfo();// 选择公开人员
				AlertDialog.Builder builder1 = new AlertDialog.Builder(ScheduleDetailActivity.this);
				builder1.setTitle("选择公开人员");
				View view = getLayoutInflater().inflate(R.layout.scope_personnel_layout, null);

				ee_listView = (ListView) view.findViewById(R.id.select_public_personnel);

				//adapter = new SelectPersonAdapter(ScheduleDetailActivity.this, listData);
				//ee_listView.setAdapter(adapter);
				mansAdapter = new SelectmanAdapter(selectmenlist,ScheduleDetailActivity.this);
				ee_listView.setAdapter(mansAdapter);
				/*scopeAdapter = new ScopePersonnelAdapter(group,total,ScheduleDetailActivity.this);
				e_listView.setAdapter(scopeAdapter);*/

				builder1.setView(view);

				builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//HashMap<Integer, Boolean> state = adapter.state;
						HashMap<Integer, Boolean> state = mansAdapter.state;
						String options = "选择的项是:";
						String data = "";
						/*for (int j = 0; j < adapter.getCount(); j++) {
						System.out.println("state.get(" + j + ")==" + state.get(j));
						if (state.get(j) != null) {
							@SuppressWarnings("unchecked")
							HashMap<String, Object> map = (HashMap<String, Object>) adapter.getItem(j);
							String username = map.get("friend_username").toString();
							String id = map.get("friend_id").toString();
							options += "\n" + id + "." + username;

							data += "," + username;

						}
					}*/
						for (int j = 0; j < mansAdapter.getCount(); j++) {
							System.out.println("state.get(" + j + ")==" + state.get(j));
							if (state.get(j) != null) {
								String username=selectmenlist.get(j).getName();
								String id = String.valueOf(selectmenlist.get(j).getId());
								options += "\n" + id + "." + username;
								data += "," + username;
								receiveids += ","+id;

							}
						}
						//显示选择内容
						if (data.length()>1){
							tv_scope_personnel_name.setText(data.substring(1));
							receive = data.substring(1);
						}

						Toast.makeText(getApplicationContext(), options, Toast.LENGTH_LONG).show();
					}
				});

				builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Toast.makeText(ScheduleDetailActivity.this, "取消", Toast.LENGTH_SHORT)
								.show();
					}
				});

				builder1.create().show();
				break;
		}
	}

	/**
	 * 根据传入的 id ，拿到对应日程详情的数据
	 * id = 0 ，则为添加新日程
	 **/
	private void getScheduleDetail(int id) {
		BusinessContant bc = new BusinessContant();
		String key = bc.getCONFIRM_ID();
		String url = bc.URL;
		InQueryMsg inQueryMsg = new InQueryMsg(1348831860, "query", key);
		inQueryMsg.setQueryName("scheduleDetail");
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
							SchduleList model = objectMapper.readValue(response.toString(), SchduleList.class);

							receive = model.getReceiver();// 公开人员
							receiveids = model.getReceiver_id();// 公开人员id
							u_id = model.getU_id();// 编辑人id
							String name = model.getName();// 编辑人
							String type = model.getType();// 日程类型
							String title = model.getTitle();// 日程标题
							String content = model.getContent();// 日程内容
							String reason = model.getReason();// 日程备注
							String begin_date = model.getWdate().substring(0, 10);// 开始时间（年月日）
							String begin_time = model.getWdate().substring(11, 16);// 开始时间（时分）
							String end_date = model.getEdate().substring(0, 10);// 结束时间（年月日）
							String end_time = model.getEdate().substring(11, 16);// 结束时间（时分）
							String event = model.getEvent();// 全天事件（开始）
							String event1 = model.getEvent1();// 全天事件（结束）
							scope = model.getScope();// 公开范围
							String receiver = model.getReceiver();// 接收人
							int remind = model.getRemind();// 提醒

							/** 拿到日程详情后，给相应显示位置赋值 **/
							tv_name.setText(name);// 编辑人
							et_type.setText(type);// 日程类型
							et_title.setText(title);// 日程标题
							et_content.setText(content);// 日程类型
							et_reason.setText(reason);// 日程备注

							if (event == null) {// 1、不是全天事件，显示的数据
								cb_event.setChecked(false);// 不选中全天事件框
								tv_begin_date.setText(begin_date);// 开始日期
								tv_end_date.setText(end_date);// 结束日期
								tv_begin_time.setText(begin_time);// 开始时间
								tv_end_time.setText(end_time);// 结束时间
//								if(begin_time != null){// 是否要加 当改为不是全天事件时，时分的默认值（当前时间）
//									tv_begin_time.setText(begin_time);
//									tv_end_time.setText(end_time);
//								}else{
//									tv_begin_time.setText(currentDate);
//									tv_end_time.setText(currentDate);
//								}
								if (remind == 1) {// 1、提醒，根据拿到值设置显示项
									s_remind.setSelection(remind, true);
								} else if (remind == 2) {
									s_remind.setSelection(remind, true);
								} else if (remind == 3) {
									s_remind.setSelection(remind, true);
								} else if (remind == 4) {
									s_remind.setSelection(remind, true);
								} else if (remind == 5) {
									s_remind.setSelection(remind, true);
								} else {
									s_remind.setSelection(0, true);
								}
							} else {// 2、是全天事件，显示的数据
								cb_event.setChecked(true);// 选中全天事件框
								tv_begin_date.setText(event);// 开始日期
								tv_end_date.setText(event1);// 结束日期
//								tv_begin_time.setText("00:00");
//								tv_end_time.setText("00:00");
								if (remind == 6) {// 2、提醒， 根据拿到值设置显示项
									s_remind.setSelection(1, true);
								} else {
									s_remind.setSelection(0, true);
								}
							}

							if (scope == 3) {// 公开范围，根据拿到值设置显示项
								rb_scope1.setChecked(true);// 选中私人
							} else if (scope == 0) {
								rb_scope2.setChecked(true);// 选中全部人员
							} else {
								rb_scope3.setChecked(true);// 选中指定人员
								tv_scope_personnel_name.setText(receiver);// 根据拿到值，显示指定人员名单
							}

						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
				Toast.makeText(ScheduleDetailActivity.this, "出错了!", Toast.LENGTH_LONG).show();
			}
		});
		requestQueue.add(jsonRequest);
	}

	/**
	 * 拿到选择公开人员的数据（科室和联系人） ***后续优化再采用这种方法
	 **/
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
		System.out.println("发送前的明文：" + postData);
		RequestQueue requestQueue = RequestUtil.getRequestQueue();

		JsonRequest jsonRequest = new JsonStringRequest(Request.Method.POST, url, postData,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d("TAG", "response -> " + response.toString());
						System.out.println("解密后：" + response.toString());

						List<PersonnalADepartment> list = JsonUtil.getPersonnalADepartment(response.toString());

						System.out.println("list-->" + list.toString());

						int id, d_id;
						String name, sname;
						List<Test> lists;

						for (int i = 0; i < list.size(); i++) {// 科室数据（父层）
							d_id = list.get(i).getD_id();
							sname = list.get(i).getSname();
							PersonnalADepartment p1 = new PersonnalADepartment(d_id, sname);
							group.add(p1);
						}
						System.out.println("拿到的所有科室-->" + group.toString());

						//lists = list.get(1).getFname();
						//System.out.println("listlist----->"+lists.toString());

						for (int j = 0; j < list.size(); j++) {// 对应科室的联系人（子层）

							lists = list.get(j).getFname();

							child = new ArrayList<Test>();
							for (int i = 0; i < lists.size(); i++) {
								//System.out.println("size()----->"+lists.size());
								id = (Integer) (((Map) lists.get(i)).get("id"));
								name = (String) ((Map) lists.get(i)).get("name");
								Test t = new Test(id, name);
								child.add(t);
							}
							total.add(j, child);

							System.out.println("sssssssssss-->" + total.toString());

							//System.out.println("sssssssssss-->"+total.toString());

						}

						System.out.println("sssssssssss-->" + total.toString());
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
				Toast.makeText(ScheduleDetailActivity.this, "出错了!", Toast.LENGTH_LONG).show();
			}
		});
		requestQueue.add(jsonRequest);
	}

	/**
	 * 修改日程详情
	 **/
	private void savedata(int id, String remind, String wdate, String edate,
						  String event, String event1, int scope, String type,
						  String title, String content, String reason) {
		BusinessContant bc = new BusinessContant();
		String key = bc.getCONFIRM_ID();
		String url = bc.URL;
		InSaveMsg inSaveMsg = new InSaveMsg(1348831860, "save", key);
		inSaveMsg.setModelName("t_Myschedule");
		HashMap<String, String> map = new HashMap<>();

		map.put("id", String.valueOf(id));// 日程id
		//map.put("u_id", String.valueOf(u_id));// 用户id
		map.put("remind", remind);// 提醒

		map.put("wdate", wdate);// 开始时间
		map.put("edate", edate);// 结束时间
		map.put("event", event);// 开始时间（全天事件）
		map.put("event1", event1);// 结束时间（全天事件）
		if (StrKit.notBlank(receiveids) && receiveids.length()>1) {
			map.put("receiver_id", receiveids.substring(1));// 公开人员id
		}
		map.put("receiver",receive);// 公开人员
		map.put("scope", String.valueOf(scope));// 公开范围
		map.put("type", type);// 日程类型
		map.put("title", title);// 日程标题
		map.put("content", content);// 日程内容
		map.put("reason", reason);// 日程备注

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

						Toast.makeText(ScheduleDetailActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
				Toast.makeText(ScheduleDetailActivity.this, "出错了!", Toast.LENGTH_LONG).show();
			}
		});

		requestQueue.add(jsonRequest);
	}

	/**
	 * 根据日程id，删除日程事件
	 **/
	private void deleteData(int id) {
		BusinessContant bc = new BusinessContant();
		String key = bc.getCONFIRM_ID();
		String url = bc.URL;
		InDeleteMsg inDeleteMsg = new InDeleteMsg(1348831860, "delete", key);
		inDeleteMsg.setModelName("t_Myschedule");

		inDeleteMsg.setEntityId(String.valueOf(id));// id+""
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

						Toast.makeText(ScheduleDetailActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
				Toast.makeText(ScheduleDetailActivity.this, "出错了!", Toast.LENGTH_LONG).show();
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
						String names,snames,pnames;
						String[] name = new String[list.size()];
						String[] id = new String[list.size()];
						String[] sname = new String[list.size()];
						String[] pname = new String[list.size()];

						for (int i = 0; i < list.size(); i++) {
							ids = list.get(i).getId();
							names = list.get(i).getName();
							snames=list.get(i).getSname();
							pnames=list.get(i).getPname();
							name[i] = names;
							sname[i] = snames;
							pname[i] = pnames;
							id[i] = String.valueOf(ids);
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

						for (int i = 0; i < list.size(); i++) {
							HashMap<String, Object> map = new HashMap<String, Object>();
							//map.put("friend_image", R.drawable.icon_select_person);
							/*map.put("friend_username", name[i]);
							map.put("sname",sname[i]);
							map.put("pname",pname[i]);
							map.put("friend_id", id[i]);
							map.put("selected", false);*/
							//添加数据
							selectmenlist.add(new T_Selectman(Integer.parseInt(id[i]), name[i], pname[i], sname[i],false));

						}

						/*adapter = new SelectPersonAdapter(WriteLetterActivity.this, listData);
						e_listView.setAdapter(adapter);*/
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
				Toast.makeText(ScheduleDetailActivity.this, "出错了!", Toast.LENGTH_LONG).show();
			}
		});
		requestQueue.add(jsonRequest);
	}

	/**
	 * 创建日期弹出框（开始）
	 */
	@SuppressLint({"ValidFragment", "NewApi"})
	class DateDialogBegin extends DialogFragment implements DatePickerDialog.OnDateSetListener {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			Date date = new Date();
			DatePickerDialog dpd = new DatePickerDialog(this.getActivity(), this, date.getYear() + 1900, date.getMonth(), date.getDate());
			return dpd;
		}

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
							  int dayOfMonth) {
			// TODO Auto-generated method stub
			String s_monthOfYear, s_dayOfMonth;
			if (sizeOfInt(monthOfYear + 1) == 1) {
				s_monthOfYear = "0" + (monthOfYear + 1);
			} else {
				s_monthOfYear = "" + (monthOfYear + 1);
			}
			if (sizeOfInt(dayOfMonth) == 1) {
				s_dayOfMonth = "0" + dayOfMonth;
			} else {
				s_dayOfMonth = "" + dayOfMonth;
			}

			tv_begin_date.setText(year + "-" + s_monthOfYear + "-" + s_dayOfMonth);
			if (!cb_event.isChecked()){
				tv_end_date.setText(year + "-" + s_monthOfYear + "-" + s_dayOfMonth);
			}
			//Toast.makeText(ScheduleDetailActivity.this, year + "/" + (monthOfYear + 1) + "/" + dayOfMonth, Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 创建日期弹出框（结束）
	 */
	@SuppressLint({"ValidFragment", "NewApi"})
	class DateDialogEnd extends DialogFragment implements DatePickerDialog.OnDateSetListener {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			Date date = new Date();
			DatePickerDialog dpd = new DatePickerDialog(this.getActivity(), this, date.getYear() + 1900, date.getMonth(), date.getDate());
			return dpd;
		}

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
							  int dayOfMonth) {
			// TODO Auto-generated method stub
			String s_monthOfYear, s_dayOfMonth;
			if (sizeOfInt(monthOfYear + 1) == 1) {
				s_monthOfYear = "0" + (monthOfYear + 1);
			} else {
				s_monthOfYear = "" + (monthOfYear + 1);
			}
			if (sizeOfInt(dayOfMonth) == 1) {
				s_dayOfMonth = "0" + dayOfMonth;
			} else {
				s_dayOfMonth = "" + dayOfMonth;
			}

			tv_end_date.setText(year + "-" + s_monthOfYear + "-" + s_dayOfMonth);
			if (!cb_event.isChecked()&& !tv_begin_date.getText().toString().equals("0000-00-00")
					&&!tv_begin_date.getText().toString().equals(tv_end_date.getText().toString())){
				tv_end_date.setText("0000-00-00");
				Toast.makeText(ScheduleDetailActivity.this,"非全天事件不能跨天！", Toast.LENGTH_SHORT).show();
			}
			//Toast.makeText(ScheduleDetailActivity.this, year + "/" + (monthOfYear + 1) + "/" + dayOfMonth, Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 创建时间弹出框（开始）
	 */
	@SuppressLint({"ValidFragment", "NewApi"})
	class TimeDialogBegin extends DialogFragment implements
			TimePickerDialog.OnTimeSetListener {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			Date date = new Date();// 用于弹出对话框时，时间为当前时间
			TimePickerDialog tdp = new TimePickerDialog(ScheduleDetailActivity.this,
					this, date.getHours(), date.getMinutes(),
					DateFormat.is24HourFormat(this.getActivity()));
			//创建一个显示时间设置的Dialog（Fragment）
			return tdp;
		}

		// 设置时间事件
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			String s_hourOfDay, s_minute;
			if (sizeOfInt(hourOfDay) == 1) {
				s_hourOfDay = "0" + hourOfDay;
			} else {
				s_hourOfDay = "" + hourOfDay;
			}
			if (sizeOfInt(minute) == 1) {
				s_minute = "0" + minute;
			} else {
				s_minute = "" + minute;
			}

			tv_begin_time.setText(s_hourOfDay + ":" + s_minute);
			Toast.makeText(ScheduleDetailActivity.this, "set:  " + hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 创建时间弹出框（结束）
	 */
	@SuppressLint({"ValidFragment", "NewApi"})
	class TimeDialogEnd extends DialogFragment implements
			TimePickerDialog.OnTimeSetListener {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			Date date = new Date();// 用于弹出对话框时，时间为当前时间
			TimePickerDialog tdp = new TimePickerDialog(ScheduleDetailActivity.this,
					this, date.getHours(), date.getMinutes(),
					DateFormat.is24HourFormat(this.getActivity()));
			//创建一个显示时间设置的Dialog（Fragment）
			return tdp;
		}

		// 设置时间事件
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			String s_hourOfDay, s_minute;
			if (sizeOfInt(hourOfDay) == 1) {
				s_hourOfDay = "0" + hourOfDay;
			} else {
				s_hourOfDay = "" + hourOfDay;
			}
			if (sizeOfInt(minute) == 1) {
				s_minute = "0" + minute;
			} else {
				s_minute = "" + minute;
			}

			tv_end_time.setText(s_hourOfDay + ":" + s_minute);
			Toast.makeText(ScheduleDetailActivity.this, "set:  " + hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();
		}

	}

	// 判断一个int是几位数
	static int sizeOfInt(int x) {
		for (int i = 0; ; i++)
			if (x <= sizeTable[i])
				return i + 1;
	}

	@Override
	protected void onResume() {
		super.onResume();

		//添加layout大小发生改变监听器
		activity_layout.addOnLayoutChangeListener(this);
	}

	// 点击屏幕关闭软键盘
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		return super.onTouchEvent(event);
	}

	@Override
	public void onLayoutChange(View v, int left, int top, int right,
							   int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

		//old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值

//      System.out.println(oldLeft + " " + oldTop +" " + oldRight + " " + oldBottom);
//      System.out.println(left + " " + top +" " + right + " " + bottom);


		//现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
		if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {

			operation.setVisibility(View.GONE);

		} else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {

			operation.setVisibility(View.VISIBLE);

		}

	}
}
