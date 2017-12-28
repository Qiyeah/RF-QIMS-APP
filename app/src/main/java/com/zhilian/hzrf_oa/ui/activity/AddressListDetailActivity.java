package com.zhilian.hzrf_oa.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.common.BusinessContant;
import com.zhilian.hzrf_oa.entity.AddressList;
import com.zhilian.hzrf_oa.entity.ContactsInfoModel;
import com.zhilian.hzrf_oa.ui.base.BaseActivity;
import com.zhilian.hzrf_oa.ui.widget.UtilsExit;
import com.zhilian.hzrf_oa.util.PictureUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.zhilian.api.InQueryMsg;
import com.zhilian.api.JsonStringRequest;
import com.zhilian.api.RequestUtil;
import com.zhilian.api.StrKit;

import java.io.IOException;
import java.util.HashMap;

/**
 * 联系人详情
 */
public class AddressListDetailActivity extends BaseActivity implements View.OnClickListener{
	private CollapsingToolbarLayout mCollapsingToolbarLayout;
	private ImageView mImageViewBg;
	private FloatingActionButton mFloatingActionButton;

	private ContactsInfoModel contactsInfo;
	private Bitmap contactsBitmap;

	private boolean isLoadPhoto;

	private TextView tv_mbphone;// 手机
	private LinearLayout friends_mbphone;// 手机（拨号）
	private LinearLayout friends_message1;// 手机（短信）
	private TextView tv_phone;// 内部电话
	private LinearLayout friends_phone;// 内部电话（拨号）
	private LinearLayout friends_message2;// 内部电话（短信）
	private TextView tv_name;// 姓名
	private TextView tv_begin_date;// 开始工作时间
	private TextView tv_married;// 婚姻状态
	private TextView tv_gradename;// 职务名称
	private TextView tv_gradelevel;// 职务级别

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.address_list_detail_layout);

		// 拿到通讯录页面传来的，联系人id
		String id = getIntent().getExtras().getString("id");

		initView();// 初始化控件
		getPersonalDetail(id);// 根据id请求联系人详情数据
	}

	private void initView() {
		contactsInfo = (ContactsInfoModel) getIntent().getSerializableExtra(UtilsExit.CONTACTS_DETAILS_EXTRA);

		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
		mImageViewBg = (ImageView) findViewById(R.id.imageView_bg);
		mFloatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);

		tv_name = (TextView) findViewById(R.id.tv_name);// 姓名
		tv_mbphone = (TextView) findViewById(R.id.tv_mbphone);// 手机
		tv_phone = (TextView) findViewById(R.id.tv_phone);// 内部电话
		tv_begin_date = (TextView) findViewById(R.id.tv_begin_date);// 开始工作时间
		tv_married = (TextView) findViewById(R.id.tv_married);// 婚姻状态
		tv_gradename = (TextView) findViewById(R.id.tv_gradename);// 职务名称
		tv_gradelevel = (TextView) findViewById(R.id.tv_gradelevel);// 职务级别
		friends_mbphone = (LinearLayout) findViewById(R.id.friends_mbphone);// 手机（拨号）
		friends_message1 = (LinearLayout) findViewById(R.id.friends_message1);// 手机（短信）
		friends_phone = (LinearLayout) findViewById(R.id.friends_phone);// 内部电话（拨号）
		friends_message2 = (LinearLayout) findViewById(R.id.friends_message2);// 内部电话（短信）

		friends_mbphone.setOnClickListener(this);
		friends_message1.setOnClickListener(this);
		friends_phone.setOnClickListener(this);
		friends_message2.setOnClickListener(this);

		setSupportActionBar(mToolbar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}

		mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_contacts, menu);
		return super.onCreateOptionsMenu(menu);
	}

	private void initData(String name,String begin_date,String married,String gradename,
						  String gradelevel,String phone,String mbphone) {
		if (contactsInfo != null) {
			mCollapsingToolbarLayout.setTitle(contactsInfo.getName());
			tv_name.setText(contactsInfo.getName());
		} else {
			mCollapsingToolbarLayout.setTitle(name);
			tv_name.setText(name);
		}
		mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE); // 设置还没收缩时状态下字体颜色
		mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE); // 设置收缩后Toolbar上字体的颜色

		if (contactsInfo != null && !TextUtils.isEmpty(contactsInfo.getImgSrc())) {
			Picasso.with(getApplicationContext())
					.load(contactsInfo.getImgSrc())
					.transform(new BlurTransformation())
					.placeholder(R.drawable.user_picture)// default_pic
					.error(R.drawable.user_picture)// default_pic
					.into(mImageViewBg);
			Picasso.with(getApplicationContext())
					.load(contactsInfo.getImgSrc())
					.transform(new CircleTransform())
					.placeholder(R.drawable.user_picture)// transparent_corner_bg
					.error(R.drawable.user_picture)// transparent_corner_bg
					.into(mFloatingActionButton);
		} else {
			contactsBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.contacts_bg);
			isLoadPhoto = true;
		}

		if (isLoadPhoto) {
			executeTask(new Runnable() {
				@Override
				public void run() {
					final Bitmap transBitmap = PictureUtil.blurBitmap(getApplicationContext(), contactsBitmap, 15f);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mFloatingActionButton.setImageResource(R.drawable.user_picture);// transparent_corner_bg
							mImageViewBg.setImageBitmap(transBitmap);
						}
					});
				}
			});
		}

		tv_mbphone.setText(mbphone);// 手机
		tv_phone.setText(phone);// 内部电话
		tv_begin_date.setText(begin_date);// 开始工作时间
		tv_married.setText(married);// 婚姻状态
		tv_gradename.setText(gradename);// 职务名称
		tv_gradelevel.setText(gradelevel);// 职务级别
	}

	@Override
	public void onClick(View v) {
		String mbphone = tv_mbphone.getText().toString();
		String phone = tv_phone.getText().toString();
		switch (v.getId()){
			case R.id.friends_mbphone:// 手机（拨号）
				if(StrKit.notBlank(mbphone)){
					Intent intent1 =new Intent();
					//intent1.setAction("android.intent.action.CALL");
					intent1.setAction(Intent.ACTION_CALL);
					intent1.setData(Uri.parse("tel:"+mbphone));
					// System.out.println("---手机--->"+mbphone);
					startActivity(intent1);
				}else{
					Toast.makeText(this,"号码是空的！",Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.friends_message1:// 手机（短信）
				if(StrKit.notBlank(mbphone)){
					Intent intent2 = new Intent(Intent.ACTION_SENDTO);
					intent2.setData(Uri.parse("smsto:"+mbphone));
					startActivity(intent2);
				}else{
					Toast.makeText(this,"号码是空的！",Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.friends_phone:// 内部电话（拨号）
				if(StrKit.notBlank(phone)){
					Intent intent3 =new Intent();
					//intent3.setAction("android.intent.action.CALL");
					intent3.setAction(Intent.ACTION_CALL);
					intent3.setData(Uri.parse("tel:"+phone));
					// System.out.println("---内部电话--->"+mbphone);
					startActivity(intent3);
				}else{
					Toast.makeText(this,"号码是空的！",Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.friends_message2:// 内部电话（短信）
				if(StrKit.notBlank(phone)){
					Intent intent4 = new Intent(Intent.ACTION_SENDTO);
					intent4.setData(Uri.parse("smsto:"+phone));
					startActivity(intent4);
				}else{
					Toast.makeText(this,"号码是空的！",Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}

	/**
	 * 模糊转换
	 */
	class BlurTransformation implements Transformation {

		public BlurTransformation() {
			super();
		}

		@Override
		public Bitmap transform(Bitmap bitmap) {
			return PictureUtil.blurBitmap(getApplicationContext(), bitmap, 15f);
		}

		@Override
		public String key() {
			return "blur";
		}

	}

	/**
	 * 圆角转换
	 */
	class CircleTransform implements Transformation {

		@Override
		public Bitmap transform(Bitmap source) {
			int size = Math.min(source.getWidth(), source.getHeight());

			int x = (source.getWidth() - size) / 2;
			int y = (source.getHeight() - size) / 2;

			Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
			if (squaredBitmap != source) {
				source.recycle();
			}

			Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

			Canvas canvas = new Canvas(bitmap);
			Paint paint = new Paint();
			BitmapShader shader = new BitmapShader(squaredBitmap,
					BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
			paint.setShader(shader);
			paint.setAntiAlias(true);

			float r = size / 2f;
			canvas.drawCircle(r, r, r, paint);

			squaredBitmap.recycle();
			return bitmap;
		}

		@Override
		public String key() {
			return "circle";
		}

	}

	/**
	 * 拿到联系人详情数据
	 * @param id
	 */
	private void getPersonalDetail(String id){
		BusinessContant bc = new BusinessContant();
		String key = bc.getCONFIRM_ID();
		String url=bc.URL;
		InQueryMsg inQueryMsg = new InQueryMsg(1348831860, "query", key);
		inQueryMsg.setQueryName("PersonalDetail");
		HashMap<String, String> map = new HashMap<>();
		map.put("id", id);
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
							AddressList model = objectMapper.readValue(response.toString(), AddressList.class);

							String name = model.getName();// 姓名
							String begin_date = model.getBegindate();// 开始工作日期
							String married = model.getMarried();// 婚姻状态
							String gradename = model.getGradename();// 职务名称
							String gradelevel = model.getGradelevel();// 职务级别
							String phone = model.getPhone();// 内部电话
							String mbphone = model.getMbphone();// 手机

							initData(name,begin_date,married,gradename,gradelevel,phone,mbphone);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
				Toast.makeText(AddressListDetailActivity.this, "出错了!", Toast.LENGTH_LONG).show();
			}
		});

		requestQueue.add(jsonRequest);
	}

}
