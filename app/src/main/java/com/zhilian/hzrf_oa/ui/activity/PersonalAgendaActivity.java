package com.zhilian.hzrf_oa.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.adapter.CalendarAdapter;
import com.zhilian.hzrf_oa.adapter.TestAdapter1;
import com.zhilian.hzrf_oa.common.BusinessContant;
import com.zhilian.hzrf_oa.json.SchduleList;
import com.zhilian.hzrf_oa.ui.widget.ScrollableLayout;
import com.zhilian.hzrf_oa.util.Constants;
import com.zhilian.hzrf_oa.util.SpecialCalendar;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nineoldandroids.view.ViewHelper;
import com.zhilian.api.InQueryMsg;
import com.zhilian.api.JsonStringRequest;
import com.zhilian.api.JsonUtil;
import com.zhilian.api.RequestUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/10.
 * 个人日程
 */
public class PersonalAgendaActivity extends FragmentActivity implements GestureDetector.OnGestureListener {
	private GestureDetector gestureDetector = null;// 手势监听器
	private TextView topText = null;// 头部的年月显示
	private LinearLayout mBtnLeft, mBtnRight;// 上一个月和下一个月的按钮（暂未用到）
	private RelativeLayout mTopLayout;// 日历gradView的布局
	private GridView gridView = null;// 日历的gradView
	private CalendarAdapter calV = null;// 日历gradView的适配器

	private ScrollableLayout mScrollLayout;// 星期栏以下的自定义滑动布局
	private ListView mListView;// 事件列表
	//private TestAdapter mAdapter;// 事件列表的适配器（待删）
	private TestAdapter1 adapter;// 事件列表的适配器
	private ArrayList<Map<String, String>> dataList = null;// 事件列表的数据（待删）
	private List<SchduleList> schdule = new ArrayList<SchduleList>();// 事件列表的数据
	private LinearLayout syllabus_empty = null;// 事件列表为空时，显示的布局

	private ArrayList<String> mData = new ArrayList<String>();
	private static int jumpMonth = 0;   // 每次滑动，增加或减去一个月,默认为0（即显示当前月）
	private static int jumpYear = 0;    // 滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
	private static int jumpDay = 0;     // 点击本月日历中上月或下月日期，跳转月份选中对应日期使用
	private int year_c = 0;             // 当天日期的年
	private int month_c = 0;            // 当天日期的月
	private int day_c = 0;              // 当天日期的日
	private String currentDate = "";
	private float location;             // 最终决定的收缩比例值
	private float currentLoction = 1f;  // 记录当天的收缩比例值
	private float selectLoction = 1f;   // 记录选择那一天的收缩比例值
	private boolean scrollFlag = false; // 标记是否滑动
	private int lastVisibleItemPosition;// 标记上次滑动位置

	private static String result1;// 日期标记的字符串
	String scheduleDay = null;
	String scheduleYear = null;
	String scheduleMonth = null;

	public PersonalAgendaActivity() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
		currentDate = sdf.format(date);  // 当前日期
		year_c = Integer.parseInt(currentDate.split("-")[0]);
		month_c = Integer.parseInt(currentDate.split("-")[1]);
		day_c = Integer.parseInt(currentDate.split("-")[2]);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar);

		ImageView back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		initView();// 初始化控件

	}

	public void initView() {
		gestureDetector = new GestureDetector(this);
		// 添加我的日程
		ImageView addMyAgenda = (ImageView) findViewById(R.id.add_my_agenda);
		addMyAgenda.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int id = 0;
				Intent intent = new Intent(PersonalAgendaActivity.this,ScheduleDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("id",id);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		topText = (TextView) findViewById(R.id.tv_month);// 头部年月显示
//		mBtnLeft = (LinearLayout) findViewById(R.id.btn_prev_month);// 上一个月按钮
//		mBtnRight = (LinearLayout) findViewById(R.id.btn_next_month);// 下一个月按钮
		mTopLayout = (RelativeLayout) findViewById(R.id.rl_head);// 包裹日历gradView的布局
		gridView = (GridView) findViewById(R.id.gridview);// 日历的gradView

		syllabus_empty = (LinearLayout) findViewById(R.id.my_syllabus_empty);// 事件列表为空时，显示的布局
		mScrollLayout = (ScrollableLayout) findViewById(R.id.scrollableLayout);// 星期栏以下的自定义滑动布局
		mListView = (ListView) findViewById(R.id.main_lv);// 事件列表
		mScrollLayout.setOnScrollListener(new ScrollableLayout.OnScrollListener() {
			@Override
			public void onScroll(int currentY, int maxY) {
				// 实现view的移动控制
				ViewHelper.setTranslationY(mTopLayout, currentY * location);
			}
		});
		/**
		 * 解决反复上下滑动时，listView自身滑动问题
		 */
		mListView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				switch (motionEvent.getAction()) {
					case MotionEvent.ACTION_MOVE:
						//mListView.setEnabled(false);// 暂时设置为不可点击
						if (!mScrollLayout.isCloseMin()) {
							return true;
						} else {
							return false;
						}
					default:
						break;
				}
				return false;
			}
		});
		mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView absListView, int scrollState) {
				switch (scrollState) {
					// 屏幕停止滚动
					case SCROLL_STATE_TOUCH_SCROLL:
						scrollFlag = true;
						break;
					// 用户在以触屏方式滚动屏幕并且手指仍然还在屏幕上
					case SCROLL_STATE_FLING:
						scrollFlag = false;
						break;
					// 用户由于之前划动屏幕并抬起手指，屏产生惯性滑动
					case SCROLL_STATE_IDLE:
						scrollFlag = false;
						mScrollLayout.setIsFirstMoveDown();
						Log.i("zxListView", "惯性滑动");
						break;
					default:
						break;
				}
			}

			@Override
			public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (scrollFlag) {
					if (firstVisibleItem > lastVisibleItemPosition) {// 上滑
						mScrollLayout.setSlippageDirection(0);
						mScrollLayout.setIsFirstMoveDown();
					}
					if (firstVisibleItem < lastVisibleItemPosition) {// 下滑
						mScrollLayout.setSlippageDirection(1);
					}
					if (firstVisibleItem == lastVisibleItemPosition) {
						return;
					}
					lastVisibleItemPosition = firstVisibleItem;
				}
			}
		});
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
				int id = schdule.get(position).getId();
				Intent intent = new Intent(PersonalAgendaActivity.this,ScheduleDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("id",id);
				intent.putExtras(bundle);
				System.out.println("传入的id--》"+id);
				startActivity(intent);
			}
		});
		mScrollLayout.getHelper().setCurrentContainer(mListView);
	}

	public void initDate() {
		// 计算当天的位置和收缩比例
		SpecialCalendar calendar = new SpecialCalendar();
		boolean isLeapYear = calendar.isLeapYear(year_c);
		int days = calendar.getDaysOfMonth(isLeapYear, month_c);
		int dayOfWeek = calendar.getWeekdayOfMonth(year_c, month_c);
		int todayPosition = day_c;
		if (dayOfWeek != 7) {
			days = days + dayOfWeek;
			todayPosition += dayOfWeek - 1;
		} else {
			todayPosition -= 1;
		}
		/**
		 * 如果 等于28天，显示4行；少于或者等于35天，大于28天显示五行 多余35天显示六行
		 * 四行：收缩比例是：0.3333,0.6666,0.9999
		 * 五行: 收缩比例是：0.25，0.5，0.75，1
		 * 六行: 收缩比例是：0.2，0.4，0.6，0.8，1
		 */
		if(days == 28){
			Constants.scale = 0.3333f;
			currentLoction = (3 - todayPosition / 7) * Constants.scale;
		} else {
			if (days <= 35) {
				Constants.scale = 0.25f;
				currentLoction = (4 - todayPosition / 7) * Constants.scale;
			} else {
				Constants.scale = 0.2f;
				currentLoction = (5 - todayPosition / 7) * Constants.scale;
			}
		}
		location = currentLoction;

		/**
		 * 日程标记的数据
		 */
		{
			calV = new CalendarAdapter(result1,PersonalAgendaActivity.this, jumpDay, jumpMonth, jumpYear, year_c, month_c, day_c,null);
//			mListView.setAdapter(calV);

			// 这里拿数据，我定义到了方法里面
			schduleDate(calV.getShowYear(),calV.getShowMonth());
		}

		/**
		 * 日程列表的数据
		 */
		{
			scheduleYear = String.valueOf(year_c);
			scheduleMonth = String.valueOf(month_c);
			scheduleDay = String.valueOf(day_c);

			System.out.println("年："+scheduleYear);//String.valueOf(year_c);
			System.out.println("月："+scheduleMonth);
			System.out.println("日："+scheduleDay);

			// 这里拿数据，我定义到了方法里面
			schduleEvent(scheduleYear,scheduleMonth,scheduleDay);
		}

		/**
		 * 如果当日没有事件，则显示提示布局
		 * 如果当日有事件,则显示事件列表
		 */
//		if(schdule.size() == 0){
//			mListView.setVisibility(View.GONE);
//			syllabus_empty.setVisibility(View.VISIBLE);
//		} else {
//			mListView.setVisibility(View.VISIBLE);
//			syllabus_empty.setVisibility(View.GONE);
//		}

		calV = new CalendarAdapter(result1,this, jumpDay, jumpMonth, jumpYear, year_c, month_c, day_c,null);
		addGridView();
		gridView.setAdapter(calV);
		adapter = new TestAdapter1(this, schdule);
		mListView.setAdapter(adapter);
		addTextToTopTextView(topText);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
						   float velocityY) {
		if (mScrollLayout.isCloseMin()) {
			return false;
		}
		if (e1.getX() - e2.getX() > 120) {
			// 向左滑动
			jumpMonth++;     // 下一个月
			jumpDay = 0;
			upDateView(null);

			schduleDate(calV.getShowYear(),calV.getShowMonth());

			return true;
		} else if (e1.getX() - e2.getX() < -120) {
			// 向右滑动
			jumpMonth--;     // 上一个月
			jumpDay = 0;
			upDateView(null);

			schduleDate(calV.getShowYear(),calV.getShowMonth());

			return true;
		}

		return false;
	}

	private void upDateView(String jumpToMonth) {
		addGridView();   // 添加一个gridView
		calV = new CalendarAdapter(result1,this, jumpDay, jumpMonth, jumpYear, year_c, month_c, day_c,jumpToMonth);
		gridView.setAdapter(calV);
		addTextToTopTextView(topText);
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.gestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
							float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	// 添加头部的年份 闰哪月等信息
	public void addTextToTopTextView(TextView view) {
		StringBuffer textDate = new StringBuffer();
		textDate.append(calV.getShowYear()).append("年").append(
				calV.getShowMonth()).append("月").append("\t");
		view.setText(textDate);
		view.setTextColor(Color.WHITE);
		view.setTypeface(Typeface.DEFAULT_BOLD);

		System.out.println("年(滑动)---》"+calV.getShowYear());
		System.out.println("月(滑动)---》"+calV.getShowMonth());
	}

	// 添加gridview
	private void addGridView() {
		// 如果滑动到其他月默认定位到第一行，划回本月定位到当天那行
		if (jumpMonth == 0) {
			location = currentLoction;
		} else {
			location = 1f;
		}
		// 选择的月份 定位到选择的那天
		if(jumpMonth + month_c <= 0){
			if ((((jumpMonth + month_c)%12 + 12) + "").equals(Constants.zMonth)) {
				location = selectLoction;
			}
		} else if(jumpMonth + month_c > 12) {
			if((jumpMonth + month_c)%12 != 0){
				if (((jumpMonth + month_c)%12 + "").equals(Constants.zMonth)) {
					location = selectLoction;
				}
			} else {
				if (((jumpMonth + month_c)%12 +12 +  "").equals(Constants.zMonth)) {
					location = selectLoction;
				}
			}

		} else {
			if (((jumpMonth + month_c) + "").equals(Constants.zMonth)) {
				location = selectLoction;
			}
		}

		gridView.setOnTouchListener(new View.OnTouchListener() {
			// 将gridview中的触摸事件回传给gestureDetector
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return PersonalAgendaActivity.this.gestureDetector.onTouchEvent(event);
			}
		});

		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			// gridView中的每一个item的点击事件
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
									long arg3) {
				// 点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
				int startPosition = calV.getStartPositon();
				int endPosition = calV.getEndPosition();
//				String scheduleDay = null;
//				String scheduleYear = null;
//				String scheduleMonth = null;
				location = (float) ((5 - position / 7) * 0.2);
				if (startPosition <= position + 7 && position <= endPosition - 7) {
					scheduleDay = calV.getDateByClickItem(position).split("\\.")[0];  //这一天的阳历
					//String scheduleLunarDay = calV.getDateByClickItem(position).split("\\.")[1];  //这一天的阴历
					scheduleYear = calV.getShowYear();
					scheduleMonth = calV.getShowMonth();
					Constants.zYear = scheduleYear;
					Constants.zMonth = scheduleMonth;
					Constants.zDay = scheduleDay;
					if(Constants.scale == 0.3333f){
						location = (3 - position / 7) * Constants.scale;
					} else {
						if (Constants.scale == 0.2f) {
							location = (5 - position / 7) * Constants.scale;
						} else {
							location = (4 - position / 7) * Constants.scale;
						}
					}
					{
						schdule.clear();
						schduleDate(scheduleYear,scheduleMonth);
//						if("5".equals(scheduleDay) || "10".equals(scheduleDay) || "15".equals(scheduleDay) || "20".equals(scheduleDay) ||
//								"25".equals(scheduleDay) || "30".equals(scheduleDay)){
//						} else {
//							Map<String,String> map = new HashMap<String, String>();
//							map.put("title","五十音图第五回");
//							map.put("date","19:00-21:00");
//							map.put("teacher_name","暖暖");
//							map.put("teacher_icon","url");
//							dataList.add(map);
//							map = new HashMap<String, String>();
//							map.put("title","新标日初级中");
//							map.put("date","每周二15:00-17:00");
//							map.put("teacher_name","蕾蕾");
//							map.put("teacher_icon","url");
//							dataList.add(map);
//							map = new HashMap<String, String>();
//							map.put("title","新标日初级下");
//							map.put("date","每周三16:00-17:00");
//							map.put("teacher_name","阿来");
//							map.put("teacher_icon","url");
//							dataList.add(map);
//							map = new HashMap<String, String>();
//							map.put("title","新标日高级上");
//							map.put("date","每周四19:00-21:00");
//							map.put("teacher_name","佩佩");
//							map.put("teacher_icon","url");
//							dataList.add(map);
//							map = new HashMap<String, String>();
//							map.put("title","新标日高级中");
//							map.put("date","每周五15:00-17:00");
//							map.put("teacher_name","花花");
//							map.put("teacher_icon","url");
//							dataList.add(map);
//							map = new HashMap<String, String>();
//							map.put("title","新标日高级下");
//							map.put("date","每周六16:00-17:00");
//							map.put("teacher_name","草草");
//							map.put("teacher_icon","url");
//							dataList.add(map);
//						}
					}
					selectLoction = location;
					calV.notifyDataSetChanged();
					//Toast.makeText(PersonalAgendaActivity.this, scheduleYear + "-" + scheduleMonth + "-" + scheduleDay, Toast.LENGTH_SHORT).show();
					if(schdule.size() == 0){
						mListView.setVisibility(View.GONE);
						syllabus_empty.setVisibility(View.VISIBLE);
					} else {
						mListView.setVisibility(View.VISIBLE);
						syllabus_empty.setVisibility(View.GONE);
						adapter.notifyDataSetChanged();
					}
				} else if(startPosition > position + 7){
					scheduleDay = calV.getDateByClickItem(position).split("\\.")[0];  // 这一天的阳历
					//String scheduleLunarDay = calV.getDateByClickItem(position).split("\\.")[1];  //这一天的阴历
					scheduleYear = calV.getShowYear();
					scheduleMonth = calV.getShowMonth();
					Constants.zDay = scheduleDay;
					if("1".equals(scheduleMonth)){
						scheduleMonth = "13";
						Constants.zMonth = Integer.valueOf(scheduleMonth) - 1 + "";
						Constants.zYear = Integer.valueOf(scheduleYear) - 1 + "";
						Toast.makeText(PersonalAgendaActivity.this, (Integer.valueOf(scheduleYear) - 1) + "-" + (Integer.valueOf(scheduleMonth) - 1) + "-" + scheduleDay, Toast.LENGTH_SHORT).show();
					} else {
						Constants.zMonth = Integer.valueOf(scheduleMonth) - 1 + "";
						Constants.zYear = scheduleYear;
						Toast.makeText(PersonalAgendaActivity.this, scheduleYear + "-" + (Integer.valueOf(scheduleMonth) - 1) + "-" + scheduleDay, Toast.LENGTH_SHORT).show();
					}
					selectLoction = 0;
					calV.notifyDataSetChanged();
					Log.i("zx","上个月");
					jumpMonth--;     // 上一个月
					jumpDay = startPosition - 7 - position;
					upDateView(CalendarAdapter.JUMPTOBEFORE);
				} else if(position > endPosition - 7){
					scheduleDay = calV.getDateByClickItem(position).split("\\.")[0];  // 这一天的阳历
					//String scheduleLunarDay = calV.getDateByClickItem(position).split("\\.")[1];  //这一天的阴历
					scheduleYear = calV.getShowYear();
					scheduleMonth = calV.getShowMonth();
					Constants.zDay = scheduleDay;
					if("12".equals(scheduleMonth)){
						scheduleMonth = "0";
						Constants.zYear = Integer.valueOf(scheduleYear) + 1 + "";
						Constants.zMonth = Integer.valueOf(scheduleMonth) + 1 + "";
						Toast.makeText(PersonalAgendaActivity.this, (Integer.valueOf(scheduleYear) + 1) + "-" + (Integer.valueOf(scheduleMonth) + 1) + "-" + scheduleDay, Toast.LENGTH_SHORT).show();
					} else {
						Constants.zYear = scheduleYear;
						Constants.zMonth = Integer.valueOf(scheduleMonth) + 1 + "";
						Toast.makeText(PersonalAgendaActivity.this, scheduleYear + "-" + (Integer.valueOf(scheduleMonth) + 1) + "-" + scheduleDay, Toast.LENGTH_SHORT).show();
					}
					selectLoction = 1;
					calV.notifyDataSetChanged();
					Log.i("zx","下个月");
					jumpMonth++;     // 下一个月
					jumpDay = position + 7 - endPosition;
					upDateView(CalendarAdapter.JUMPTONEXT);
				}
				schduleEvent(scheduleYear,scheduleMonth,scheduleDay);

				//System.out.println("年--》"+scheduleYear+"月--》"+scheduleMonth+"日--》"+scheduleDay);

			}

		});
	}

	// 日程列表的数据
	private void schduleEvent(String year,String month,String day){
		schdule.clear();
		BusinessContant bc = new BusinessContant();
		String key = bc.getCONFIRM_ID();
		String url=bc.URL;
		InQueryMsg inQueryMsg = new InQueryMsg(1348831860, "query", key);
		inQueryMsg.setQueryName("schduleEvent");
		HashMap<String, String> map1 = new HashMap<>();
		map1.put("date",String.valueOf(year+"-"+month+"-"+day) );
		inQueryMsg.setQueryPara(map1);
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
						List<SchduleList> list = JsonUtil.getSchduleList(response.toString());
						System.out.println("list:"+list);
						for (int i=0;i<list.size();i++){
							int id;
							String title,wdate,edate,event,event1;
							id = list.get(i).getId();
							title = list.get(i).getTitle();
							wdate = list.get(i).getWdate();
							edate = list.get(i).getEdate();
							event = list.get(i).getEvent();
							event1 = list.get(i).getEvent1();
							schdule.add(new SchduleList(id, title, wdate, edate, event, event1));
						}

						adapter = new TestAdapter1(PersonalAgendaActivity.this, schdule);
						mListView.setAdapter(adapter);

						if(schdule.size() == 0){
							mListView.setVisibility(View.GONE);
							syllabus_empty.setVisibility(View.VISIBLE);
						} else {
							mListView.setVisibility(View.VISIBLE);
							syllabus_empty.setVisibility(View.GONE);
							adapter.notifyDataSetChanged();
						}

					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
				Toast.makeText(PersonalAgendaActivity.this,"出错了!",Toast.LENGTH_LONG).show();
			}
		});
		requestQueue.add(jsonRequest);
	}

	// 日程标记数据
	private void schduleDate(String year,String month){

		System.out.println("年(点击)---》"+year_c);
		System.out.println("月(点击)---》"+month_c);
		BusinessContant bc = new BusinessContant();
		String key = bc.getCONFIRM_ID();
		String url=bc.URL;
		InQueryMsg inQueryMsg = new InQueryMsg(1348831860, "query", key);
		inQueryMsg.setQueryName("schduleDate");
		HashMap<String, String> map = new HashMap<>();
		map.put("month", String.valueOf(year+"-"+month));
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
						String[] result = response.toString().split(",");
						result1 =  response.toString();

//						int[] icon = new int[result.length];// 这里是拆分字符串的代码
//						for(int i=0;i<result.length;i++){
//							icon[i] = Integer.parseInt(result[i]);
//
//						}

//						for(int i=0;i<result.length;i++){
//							System.out.println(icon[i]);
//						}

						calV = new CalendarAdapter(result1,PersonalAgendaActivity.this, jumpDay, jumpMonth, jumpYear, year_c, month_c, day_c,null);
						gridView.setAdapter(calV);

					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
				Toast.makeText(PersonalAgendaActivity.this,"出错了!",Toast.LENGTH_LONG).show();
			}
		});
		requestQueue.add(jsonRequest);
	}

	@Override
	protected void onResume() {
		super.onResume();
		initDate();// 加载数据
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// TODO 页面被销毁时，清空选择的日期数据
		Constants.zYear = "";
		Constants.zMonth = "";
		Constants.zDay = "";
	}

}
