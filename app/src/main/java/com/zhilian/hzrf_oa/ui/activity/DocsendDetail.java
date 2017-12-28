package com.zhilian.hzrf_oa.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhilian.api.InQueryMsg;
import com.zhilian.api.JsonStringRequest;
import com.zhilian.api.JsonUtil;
import com.zhilian.api.ParaMap;
import com.zhilian.api.RequestUtil;
import com.zhilian.api.Sign;
import com.zhilian.api.StrKit;
import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.adapter.OpinionAdapter;
import com.zhilian.hzrf_oa.adapter.RecordAdapter;
import com.zhilian.hzrf_oa.adapter.SelectmanAdapter;
import com.zhilian.hzrf_oa.adapter.TextAdapter;
import com.zhilian.hzrf_oa.common.BusinessContant;
import com.zhilian.hzrf_oa.common.Common;
import com.zhilian.hzrf_oa.entity.OpinionBeen;
import com.zhilian.hzrf_oa.json.T_FJList;
import com.zhilian.hzrf_oa.json.T_Record;
import com.zhilian.hzrf_oa.json.T_Selectman;
import com.zhilian.hzrf_oa.service.DownloadService;
import com.zhilian.hzrf_oa.util.DownLoadUtil;
import com.zhilian.hzrf_oa.util.OpenFileIntent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/3.
 * 发文呈批笺（发文管理）
 */
public class DocsendDetail extends Activity {
    private Button record, tianxieyijian, submit, openfile;// 记录/ 填写意见/ 提交/ 查看正文
    private BusinessContant bc = new BusinessContant();
    private TextView tv_dname, tv_approvedate, tv_uname, tv_testername, tv_security, tv_count, tv_send1;
    private TextView tv_send2, tv_docno, tv_title, tv_openfile, tv_fjlist, tv_opinion;
    private ListView opinionListView, recordListView, mansListview;
    private ListView fjListView;
    private EditText opinion;
    private RadioGroup radioGroup;
    private RadioButton tempButton;
    private Spinner spinner;
    private AlertDialog alertDialog;
    private String checked_id;
    String name;
    private String docid;
    private String isdone;
    private String opinionfield;//意见域
    private String[] opinions = new String[]{"", "同意。", "已核。", "已阅。"};
    private TextAdapter fjadapter;
    private OpinionAdapter opinionAdapter;
    private SelectmanAdapter selectmanAdapter;
    private List<OpinionBeen> opinionBeenList = new ArrayList<OpinionBeen>();
    private ArrayList<T_FJList> fjlist = new ArrayList<T_FJList>();
    private ArrayList<T_FJList> lists = new ArrayList<T_FJList>();
    private List<T_Selectman> selectmenlist = new ArrayList<T_Selectman>();
    private List<T_Record> recordList = new ArrayList<T_Record>();

    private DownLoadUtil downLoad = new DownLoadUtil();

    private RecordAdapter recordAdapter;
    private NotificationManager mNotificationManager = null;
    private Notification mNotification;
    private int fileSize;
    int downLoadFileSize;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 定义一个Handler，用于处理下载线程与UI间通讯
            switch (msg.what) {
                case 1:
                    int result = downLoadFileSize * 100 / fileSize;
                    mNotification.contentView.setTextViewText(R.id.content_view_text1, name + " " + result + "%");
                    mNotification.contentView.setProgressBar(R.id.content_view_progress, fileSize, downLoadFileSize, false);
                    mNotificationManager.notify(0, mNotification);
                    Log.e("size", "文件" + downLoadFileSize + ":" + fileSize + ":" + result);
                    break;
                case 2:
                    Toast.makeText(getApplication(), "文件下载完成", Toast.LENGTH_SHORT).show();
                    break;

                case -1:
                    String error = msg.getData().getString("error");
                    Toast.makeText(getApplication(), error, Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.docsend_detail);
        Intent intent = this.getIntent();
        docid = intent.getStringExtra("docid");
        isdone = intent.getStringExtra("isdone");
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fjListView = (ListView) findViewById(R.id.fj_list);
        opinionListView = (ListView) findViewById(R.id.opinionList);
        tv_dname = (TextView) findViewById(R.id.dname);
        tv_approvedate = (TextView) findViewById(R.id.starttime);
        tv_uname = (TextView) findViewById(R.id.uname);
        tv_testername = (TextView) findViewById(R.id.testuname);
        tv_security = (TextView) findViewById(R.id.security);
        tv_count = (TextView) findViewById(R.id.count);
        tv_send1 = (TextView) findViewById(R.id.send1);
        tv_send2 = (TextView) findViewById(R.id.send2);
        tv_docno = (TextView) findViewById(R.id.docno);
        tv_title = (TextView) findViewById(R.id.title);
        addData();
        initView();
    }

    private void initView() {
        record = (Button) findViewById(R.id.record);
        record.setOnClickListener(listener1);
        tianxieyijian = (Button) findViewById(R.id.tianxieyijian);
        tianxieyijian.setOnClickListener(listener2);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(listener3);
        openfile = (Button) findViewById(R.id.openfile);
        openfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context mContext = getApplicationContext();
                final ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
                final NetworkInfo mobNetInfoActivity = connectivityManager
                    .getActiveNetworkInfo();
                if (mobNetInfoActivity == null || !mobNetInfoActivity.isAvailable()) {
                    Toast.makeText(DocsendDetail.this, "当前没有连接网络！不能下载！", Toast.LENGTH_SHORT).show();
                } else {
                    openfile();
                }

            }
        });
        fjListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                Context mContext = getApplicationContext();
                name = fjlist.get(position).getName();
                String url = fjlist.get(position).getUrl();
                downLoad.context = DocsendDetail.this;
                downLoad.name = url;
                downLoad.downPath = bc.SAVEPATH + url;
                downLoad.downUrl = bc.DOWNLOADURL + url;
                downLoad.DownOrOpen();
            }
        });
    }

    private View.OnClickListener listener1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(DocsendDetail.this);
            builder.setTitle("流转记录");

            View view = getLayoutInflater().inflate(R.layout.button1_layout, null);
            recordListView = (ListView) view.findViewById(R.id.recordList);
            new Thread() {
                public void run() {
                    try {
                        String key = bc.getCONFIRM_ID();
                        String pid = String.valueOf(bc.getPid());
                        String url = bc.URL;
                        String token = "1lj4hbato30kl1ppytwa1ueqdn";
                        final String encodingAesKey = "InVjlo7czsOWrCSmTPgEUXBzlFnmqpNMQU3ZfilULHyHZiRjVUhxxWpexhYH6f4i";
                        Map<String, String> ret = Sign.sign(url, token, encodingAesKey);
                        Map<String, String> queryParas = ParaMap.create("accessToken", token)
                            .getData();
                        url = RequestUtil.buildUrlWithQueryString(url, queryParas);
                        InQueryMsg inQueryMsg = new InQueryMsg(1348831860, "query", key);
                        inQueryMsg.setQueryName("ReceiveRecord");
                        HashMap<String, String> map = new HashMap<>();
                        map.put("projectName", "发文流转记录");
                        map.put("pid", pid);

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

                                    try {

                                        List<T_Record> list = JsonUtil.getRecordList(response.toString());
                                        recordList.clear();

                                        for (int i = 0; i < list.size(); i++) {
                                            String item, doman, starttime, endtime, operation, nextusername, opinion;
                                            opinion = list.get(i).getOpinion();
                                            starttime = list.get(i).getBegintime();
                                            operation = list.get(i).getOperation();
                                            endtime = list.get(i).getEndtime();
                                            doman = list.get(i).getName();
                                            item = list.get(i).getItemid1();
                                            nextusername = list.get(i).getUser2();
                                            recordList.add(new T_Record(item, doman, starttime, endtime, operation, nextusername, opinion));
                                        }
                                        recordAdapter = new RecordAdapter(recordList, getApplicationContext());
                                        recordListView.setAdapter(recordAdapter);
                                    } catch (Exception e) {
                                        e.printStackTrace();
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

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();

                    }
                }
            }.start();

            builder.setView(view);// 使用自定义布局作为对话框内容

            // 负面语义按钮
            builder.setNegativeButton("关闭", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    /*Toast.makeText(DocsendDetail.this, "点击了取消", Toast.LENGTH_SHORT)
							.show();*/
                }
            });

            builder.create().show();
        }
    };

    private View.OnClickListener listener2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(DocsendDetail.this);
            builder.setTitle("批办意见及领导批示");
            View view = getLayoutInflater().inflate(R.layout.button2_layout, null);
            opinion = (EditText) view.findViewById(R.id.editopinion);
            spinner = (Spinner) view.findViewById(R.id.responsible_person);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String edit = opinions[position];
                    opinion.setText(edit + "");
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            builder.setView(view);// 使用自定义布局作为对话框内容

            // 正面语义按钮：确定 支持。。。。。。
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    new Thread() {
                        public void run() {
                            try {
                                String key = bc.getCONFIRM_ID();
                                String pid = String.valueOf(bc.getPid());
                                String url = bc.URL;
                                String token = "1lj4hbato30kl1ppytwa1ueqdn";
                                final String encodingAesKey = "InVjlo7czsOWrCSmTPgEUXBzlFnmqpNMQU3ZfilULHyHZiRjVUhxxWpexhYH6f4i";
                                Map<String, String> ret = Sign.sign(url, token, encodingAesKey);
                                String signature = ret.get("signature");
                                String nonceStr = ret.get("nonceStr");
                                String timestamp = ret.get("timestamp");
                                String editopinion = opinion.getText().toString();
                                Map<String, String> queryParas = ParaMap.create("accessToken", token)
                                    .put("nonce", nonceStr)
                                    .put("timestamp", timestamp)
                                    .put("signature", signature)
                                    .getData();
                                url = RequestUtil.buildUrlWithQueryString(url, queryParas);
                                InQueryMsg inQueryMsg = new InQueryMsg(1348831860, "query", key);
                                inQueryMsg.setQueryName("newopinion");
                                HashMap<String, String> map = new HashMap<>();
                                map.put("opinion", editopinion);
                                map.put("pid", pid);
                                map.put("module", "docsend");
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

                                            try {
                                                JSONObject dataJson = new JSONObject(response.toString());
                                                if (dataJson.getString("opinion3").equals("1")) {
                                                    submit.setVisibility(View.VISIBLE);
                                                }
                                                String opinion4 = dataJson.getString("opinion4");//
                                                if (opinion4.equals("您不能填写意见!")) {
                                                    Toast.makeText(getApplicationContext(), opinion4, Toast.LENGTH_LONG).show();
                                                } else {
                                                    tv_dname.setText(dataJson.getString("dname"));
                                                    tv_approvedate.setText(dataJson.getString("approvedate"));
                                                    tv_uname.setText(dataJson.getString("uname"));
                                                    if (StrKit.notBlank(dataJson.getString("proof"))) {
                                                        tv_testername.setText(dataJson.getString("proof"));
                                                    }
                                                    tv_security.setText(dataJson.getString("security"));
                                                    if (StrKit.notBlank(dataJson.getString("num"))) {
                                                        tv_count.setText(dataJson.getString("num"));
                                                    }
                                                    if (StrKit.notBlank(dataJson.getString("send1"))) {
                                                        tv_send1.setText(dataJson.getString("send1"));
                                                    }
                                                    if (StrKit.notBlank(dataJson.getString("send2"))) {
                                                        tv_send2.setText(dataJson.getString("send2"));
                                                    }
                                                    if (StrKit.notBlank(dataJson.getString("docno"))) {
                                                        tv_docno.setText(dataJson.getString("docno"));
                                                    }
                                                    tv_title.setText(dataJson.getString("title"));
                                                    String opinionfield = dataJson.getString("opinion2");
                                                    JSONArray fjids = dataJson.getJSONArray("fjid");   //附件列表
                                                    JSONArray opinion1 = dataJson.getJSONArray("opinion1"); //领导意见
                                                    String fjname = "", opinion = "", sign = "", signdate = "", signimg;

                                                    int id;
                                                    opinionBeenList.clear();

                                                    fjlist.clear();
                                                    List<T_FJList> list = JsonUtil.getFJList(fjids.toString());
                                                    if (!opinionfield.equals("opinion1")) {
                                                        tianxieyijian.setVisibility(View.GONE);
                                                    }
                                                    //List<OpinionBeen> opinionlist = JsonUtil.getOpinionList(opinion1.toString());
                                                    for (int i = 0; i < list.size(); i++) {
                                                        fjname = list.get(i).getName();
                                                        id = list.get(i).getId();
                                                        String url = list.get(i).getUrl();
                                                        String size = list.get(i).getSize();
                                                        fjlist.add(new T_FJList(id, fjname, url, size));
                                                    }

                                                    for (int i = 0; i < opinion1.length(); i++) {
                                                        JSONObject info = opinion1.getJSONObject(i);
                                                        opinion = info.getString("opinion");
                                                        sign = info.getString("leader");
                                                        signimg = info.getString("url");
                                                        id = info.getInt("id");
                                                        signdate = info.getString("opiniontime");
                                                        if (StrKit.notBlank(signimg)) {
                                                            opinionBeenList.add(new OpinionBeen(opinion, signimg, signdate, id));
                                                        } else {
                                                            opinionBeenList.add(new OpinionBeen(opinion, sign, signdate));
                                                        }
                                                    }

                                                    opinionAdapter = new OpinionAdapter(opinionBeenList, DocsendDetail.this);
                                                    opinionListView.setAdapter(opinionAdapter);
                                                    if (opinion1.length() > 0) {
                                                        setListViewHeightBasedOnChildren(opinionListView);
                                                    }


                                                    fjadapter = new TextAdapter(fjlist, getApplicationContext());
                                                    fjListView.setAdapter(fjadapter);

                                                    if (fjlist.size() > 0) {
                                                        setListViewHeightBasedOnChildren(fjListView);
                                                    }
                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
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

                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();

                            }
                        }
                    }.start();
                }
            });

            // 负面语义按钮
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                }
            });

            builder.create().show();
        }
    };


    private View.OnClickListener listener3 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(DocsendDetail.this);
            builder.setTitle("下一环节");


            View view = getLayoutInflater().inflate(R.layout.button3_layout, null);
            //stepListView=(ListView)view.findViewById(R.id.steps);
            tv_title = (TextView) view.findViewById(R.id.title);
            radioGroup = (RadioGroup) view.findViewById(R.id.selectman);
            mansListview = (ListView) view.findViewById(R.id.selectmans);
            Fasong();
            if (StrKit.isBlank(bc.getNexttype()) ? false : bc.getNexttype().equals("toast")) {
                builder.setTitle("下一环节处理人");
            }
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    // TODO Auto-generated method stub
                    tempButton = (RadioButton) findViewById(checkedId); // 通过RadioGroup的findViewById方法，找到ID为checkedID的RadioButton
                    // 以下就可以对这个RadioButton进行处理了
                    checked_id = String.valueOf(checkedId);
                    if (bc.getNexttype().equals("toast")) {
                        bc.setUserid(checked_id);//当前选中人
                    } else {
                        bc.setCheckid(checked_id);//选中环节

                    }
                }
            });

            builder.setView(view);// 使用自定义布局作为对话框内容
            builder.setPositiveButton("确定", null);

            // 负面语义按钮
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    bc.setCheckid("");
                    bc.setUserid("");

                }
            });

            alertDialog = builder.create();
            alertDialog.show();
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (StrKit.notBlank(bc.getCheckid())) {
                        checked_id = bc.getCheckid();
                    }
                    if (checked_id != null) {
                        selectman(checked_id);
                    } else {
                        Toast.makeText(DocsendDetail.this, "请选择!", Toast.LENGTH_SHORT).show();
                    }

                    if (bc.getNexttype().equals("toast")) {
                        alertDialog.dismiss();
                        finish();
                    } else {
                        alertDialog.setTitle("下一环节处理人");
                        alertDialog.show();
                    }

                }


            });
        }
    };

    private void Fasong() {
        new Thread() {
            public void run() {
                try {
                    String key = bc.getCONFIRM_ID();

                    String url = bc.URL;
                    String token = "1lj4hbato30kl1ppytwa1ueqdn";
                    final String encodingAesKey = "InVjlo7czsOWrCSmTPgEUXBzlFnmqpNMQU3ZfilULHyHZiRjVUhxxWpexhYH6f4i";
                    Map<String, String> ret = Sign.sign(url, token, encodingAesKey);
                    String signature = ret.get("signature");
                    String nonceStr = ret.get("nonceStr");
                    String timestamp = ret.get("timestamp");
                    Map<String, String> queryParas = ParaMap.create("accessToken", token)
                        .put("nonce", nonceStr)
                        .put("timestamp", timestamp)
                        .put("signature", signature)
                        .getData();
                    url = RequestUtil.buildUrlWithQueryString(url, queryParas);
                    InQueryMsg inQueryMsg = new InQueryMsg(1348831860, "query", key);
                    inQueryMsg.setQueryName("fasong");
                    HashMap<String, String> map = new HashMap<>();
                    map.put("curitemid", bc.getItem_id());
                    map.put("pid", bc.getPid());
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
                                try {
                                    JSONObject dataJson = new JSONObject(response.toString());
                                    String type = dataJson.getString("type");
                                    String ato, name, sname, positionname;
                                    int id;
                                    selectmenlist.clear();
                                    if (type.equals("9")) {
                                        Toast.makeText(getApplicationContext(), "没有下一处理人，请联系管理员！", Toast.LENGTH_LONG).show();
                                    } else if (type.equals("step")) {
                                        bc.setNexttype("windows");
                                        JSONArray was = dataJson.getJSONArray("trans");
                                        List<T_Selectman> list = JsonUtil.getselectmanList(was.toString());
                                        for (int i = 0; i < list.size(); i++) {
                                            id = list.get(i).getId();
                                            ato = list.get(i).getAto();

                                            tempButton = new RadioButton(DocsendDetail.this);
                                            tempButton.setText(ato);
                                            tempButton.setTextColor(0xFF505050);
                                            tempButton.setTextSize(16f);
                                            tempButton.setPadding(80, 0, 0, 0);                 // 设置文字距离按钮四周的距离
                                            tempButton.setId(id);
                                            tempButton.setText(ato);
                                            //radioGroup.addView(tempButton, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                            radioGroup.addView(tempButton);
                                            if (i == 0) {
                                                radioGroup.check(tempButton.getId());
                                            }
                                        }
                                        //selectmanAdapter=new SelectmanAdapter(selectmenlist,getApplicationContext());
                                        //stepListView.setAdapter(selectmanAdapter);
                                        tv_title.setText("下一环节");
                                    } else {
                                        bc.setCheckid(dataJson.getString("nextStep"));
                                        bc.setNexttype("toast");
                                        JSONArray user = dataJson.getJSONArray("user");
                                        String amount = dataJson.getString("type");
                                        List<T_Selectman> list = JsonUtil.getselectmanList(user.toString());
                                        for (int i = 0; i < user.length(); i++) {
                                            id = list.get(i).getId();
                                            name = list.get(i).getName();
                                            sname = list.get(i).getD_id();
                                            positionname = list.get(i).getPid();
                                            if (amount.equals("1")) {
                                                tempButton = new RadioButton(DocsendDetail.this);
                                                tempButton.setText(name + "(" + sname + ":" + positionname + ")");
                                                tempButton.setPadding(80, 0, 0, 0);  // 设置文字距离按钮四周的距离
                                                tempButton.setTextSize(16f);
                                                tempButton.setId(id);
                                                //radioGroup.addView(tempButton, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                radioGroup.addView(tempButton);
                                                if (i == 0) {
                                                    radioGroup.check(tempButton.getId());
                                                }
                                            } else {
                                                selectmenlist.add(new T_Selectman(id, name, sname, positionname));
                                            }
                                        }
                                        if (!amount.equals("1")) {
                                            selectmanAdapter = new SelectmanAdapter(selectmenlist, getApplicationContext());
                                            mansListview.setAdapter(selectmanAdapter);
                                        }
                                        tv_title.setText("下一处理人");
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
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

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                }
            }
        }.start();
    }


    public void selectman(String id) {
        final String itemid = id;
        new Thread() {
            public void run() {
                try {
                    String key = bc.getCONFIRM_ID();
                    String url = bc.URL;
                    String token = "1lj4hbato30kl1ppytwa1ueqdn";
                    final String encodingAesKey = "InVjlo7czsOWrCSmTPgEUXBzlFnmqpNMQU3ZfilULHyHZiRjVUhxxWpexhYH6f4i";
                    Map<String, String> ret = Sign.sign(url, token, encodingAesKey);
                    String signature = ret.get("signature");
                    String nonceStr = ret.get("nonceStr");
                    String timestamp = ret.get("timestamp");
                    Map<String, String> queryParas = ParaMap.create("accessToken", token)
                        .put("nonce", nonceStr)
                        .put("timestamp", timestamp)
                        .put("signature", signature)
                        .getData();
                    url = RequestUtil.buildUrlWithQueryString(url, queryParas);
                    InQueryMsg inQueryMsg = new InQueryMsg(1348831860, "query", key);
                    inQueryMsg.setQueryName("fasong1");
                    HashMap<String, String> map = new HashMap<>();
                   /* map.put("nexttype", bc.getNexttype());//要改的
                    map.put("pid", bc.getPid());
                    if (StrKit.notBlank(bc.getCheckid())){
                        map.put("itemid",bc.getCheckid());
                    }else {
                        map.put("itemid",null);
                    }
                    map.put("userids",bc.getUserid());//选中人的id*/
                    map.put("nexttype", bc.getNexttype());//要改的
                    map.put("nextitemid", bc.getCheckid());

                    map.put("nexttodoman", bc.getUserid());//选中人的id
                    map.put("pid", bc.getPid());
                    map.put("doc", "receive");
                    map.put("operation", "FaSong");
                    map.put("opinionfield", opinionfield);
                    map.put("flow", "send");
                    map.put("opinion", opinion.getText().toString());
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
                                try {
                                    JSONObject dataJson = new JSONObject(response.toString());
                                    String nexttype = dataJson.getString("nextType");
                                    String amount = dataJson.getString("type");
                                    if (nexttype.equals("toast")) {
                                        Toast.makeText(getApplicationContext(), amount, Toast.LENGTH_LONG).show();
                                        tianxieyijian.setVisibility(View.GONE);
                                        submit.setVisibility(View.GONE);
                                    } else {
                                        JSONArray users = dataJson.getJSONArray("user");
                                        bc.setNexttype("toast");
                                        String name, sname, positionname;
                                        int id;
                                        JSONArray user = dataJson.getJSONArray("user");
                                        List<T_Selectman> list = JsonUtil.getselectmanList(users.toString());
                                        radioGroup.removeAllViews();
                                        tv_title.setText("下一处理人");
                                        for (int i = 0; i < user.length(); i++) {
                                            id = list.get(i).getId();
                                            name = list.get(i).getName();
                                            sname = list.get(i).getD_id();
                                            positionname = list.get(i).getPid();

                                            if (amount.equals("1")) {
                                                tempButton = new RadioButton(DocsendDetail.this);

                                                tempButton.setTextSize(16f);
                                                tempButton.setText(name + "(" + sname + ":" + positionname + ")");
                                                tempButton.setPadding(80, 0, 0, 0);                 // 设置文字距离按钮四周的距离
                                                tempButton.setId(id);
                                                radioGroup.addView(tempButton);
                                                if (i == 0) {
                                                    radioGroup.check(tempButton.getId());
                                                }
                                            } else {
                                                selectmenlist.add(new T_Selectman(id, name, positionname, sname));
                                            }
                                        }
                                        if (!amount.equals("1")) {
                                            selectmanAdapter = new SelectmanAdapter(selectmenlist, getApplicationContext());
                                            mansListview.setAdapter(selectmanAdapter);

                                        }

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        , new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("TAG", error.getMessage(), error);
                            Toast.makeText(getApplicationContext(), "出错了!", Toast.LENGTH_LONG).show();
                        }
                    });
                    requestQueue.add(jsonRequest);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                }
            }
        }.start();
    }


    private void addData() {
        String key = bc.getCONFIRM_ID();
        String url = bc.URL;
        String token = "1lj4hbato30kl1ppytwa1ueqdn";
        final String encodingAesKey = "InVjlo7czsOWrCSmTPgEUXBzlFnmqpNMQU3ZfilULHyHZiRjVUhxxWpexhYH6f4i";
        Map<String, String> ret = Sign.sign(url, token, encodingAesKey);
        String signature = ret.get("signature");
        String nonceStr = ret.get("nonceStr");
        String timestamp = ret.get("timestamp");
        Map<String, String> queryParas = ParaMap.create("accessToken", token)
            .put("nonce", nonceStr)
            .put("timestamp", timestamp)
            .put("signature", signature)
            .getData();
        url = RequestUtil.buildUrlWithQueryString(url, queryParas);
        InQueryMsg inQueryMsg = new InQueryMsg(1348831860, "query", key);
        inQueryMsg.setQueryName("DocsendDetail");
        HashMap<String, String> map = new HashMap<>();
        map.put("projectName", "发文");
        map.put("docid", docid);
        map.put("isdone", isdone);
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
                    //Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                    String fjname, opinion, signimg, signdate, sign;
                    Integer fjid;


                    try {
                        JSONObject dataJson = new JSONObject(response.toString());
                        tv_dname.setText(dataJson.getString("dname"));
                        tv_approvedate.setText(dataJson.getString("approvedate"));
                        tv_uname.setText(dataJson.getString("uname"));
                        if (StrKit.notBlank(dataJson.getString("proof"))) {
                            tv_testername.setText(dataJson.getString("proof"));
                        }
                        bc.setPid(dataJson.getString("pid"));
                        tv_security.setText(dataJson.getString("security"));
                        if (StrKit.notBlank(dataJson.getString("num"))) {
                            tv_count.setText(dataJson.getString("num"));
                        }
                        if (StrKit.notBlank(dataJson.getString("send1"))) {
                            tv_send1.setText(dataJson.getString("send1"));
                        }
                        if (StrKit.notBlank(dataJson.getString("send2"))) {
                            tv_send2.setText(dataJson.getString("send2"));
                        }
                        if (StrKit.notBlank(dataJson.getString("docno"))) {
                            tv_docno.setText(dataJson.getString("docno"));
                        }
                        tv_title.setText(dataJson.getString("title"));
                        String itemid = dataJson.getString("opinion5");
                        JSONArray fjids = dataJson.getJSONArray("fjid");
                        opinionfield = dataJson.getString("opinion2");
                        JSONArray opinions = dataJson.getJSONArray("opinion1");
                        List<T_FJList> list = JsonUtil.getFJList(fjids.toString());
                        bc.setItem_id(itemid);
                        //List<OpinionBeen> opinionlist = JsonUtil.getOpinionList(opinion1.toString());
                        if (!opinionfield.equals("opinion1")) {
                            tianxieyijian.setVisibility(View.GONE);
                        }
                        if (!dataJson.getString("opinion3").equals("1")) {
                            submit.setVisibility(View.GONE);
                        }
                        if (isdone.equals("1")) {
                            tianxieyijian.setVisibility(View.GONE);
                            submit.setVisibility(View.GONE);
                        }
                        for (int i = 0; i < list.size(); i++) {
                            fjname = list.get(i).getName();
                            fjid = list.get(i).getId();
                            String url = list.get(i).getUrl();
                            String size = list.get(i).getSize();
                            fjlist.add(new T_FJList(fjid, fjname, url, size));
                        }
                        fjadapter = new TextAdapter(fjlist, DocsendDetail.this);
                        fjListView.setAdapter(fjadapter);
                        if (list.size() > 0) {
                            setListViewHeightBasedOnChildren(fjListView);
                        }

                        for (int i = 0; i < opinions.length(); i++) {
                            JSONObject info = opinions.getJSONObject(i);
                            int id1 = info.getInt("id");
                            opinion = info.getString("opinion");
                            signimg = info.getString("url");
                            sign = info.getString("leader");
                            signdate = info.getString("opiniontime");
                            if (StrKit.notBlank(signimg)) {
                                opinionBeenList.add(new OpinionBeen(opinion, signimg, signdate, id1));
                            } else {
                                opinionBeenList.add(new OpinionBeen(opinion, sign, signdate));
                            }
                        }
                        opinionAdapter = new OpinionAdapter(opinionBeenList, DocsendDetail.this);
                        opinionListView.setAdapter(opinionAdapter);
                        if (opinions.length() > 0) {
                            setListViewHeightBasedOnChildren(opinionListView);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
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

    private void openfile1(String url, String name) {
        Intent it = new Intent(DocsendDetail.this, DownloadService.class);
        it.putExtra("url", url);
        it.putExtra("filename", name);
        startService(it);
    }

    private void openfile() {
        String key = bc.getCONFIRM_ID();
        String pid = String.valueOf(bc.getPid());
        String url = bc.URL;
        String token = "1lj4hbato30kl1ppytwa1ueqdn";
        final String encodingAesKey = "InVjlo7czsOWrCSmTPgEUXBzlFnmqpNMQU3ZfilULHyHZiRjVUhxxWpexhYH6f4i";
        Map<String, String> ret = Sign.sign(url, token, encodingAesKey);
        String signature = ret.get("signature");
        String nonceStr = ret.get("nonceStr");
        String timestamp = ret.get("timestamp");
        Map<String, String> queryParas = ParaMap.create("accessToken", token)
            .put("nonce", nonceStr)
            .put("timestamp", timestamp)
            .put("signature", signature)
            .getData();
        url = RequestUtil.buildUrlWithQueryString(url, queryParas);
        InQueryMsg inQueryMsg = new InQueryMsg(1348831860, "query", key);
        inQueryMsg.setQueryName("getfile");
        HashMap<String, String> map = new HashMap<>();

        map.put("pid", pid);
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
                    if (response.toString().equals("没有该文件！")) {
                        Toast.makeText(DocsendDetail.this, "没有该文件！", Toast.LENGTH_SHORT).show();
                    } else {
                        final String fileurl = bc.Domain + response.toString();
                        String[] paths = fileurl.split("/");
                        final String name = paths[paths.length - 1];
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                URL url;
                                try {
                                    url = new URL(fileurl);
                                    InputStream is = null;
                                    try {
                                        is = url.openStream();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        Looper.prepare();
                                        Toast.makeText(DocsendDetail.this, "没有找到文件！", Toast.LENGTH_SHORT).show();
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
                                    String path = bc.SAVEPATH + name;

                                    File file = new File(bc.SAVEPATH);
                                    if (!file.exists()) {
                                        file.mkdirs();
                                    }
                                    bc.verifyStoragePermissions(DocsendDetail.this);
                                    FileOutputStream fos = new FileOutputStream(path);
                                    fos.write(baos.toByteArray());
                                    fos.close();
                                    Intent it = new Intent();
                                    if (path.substring(path.length() - 4).equals(".pdf")) {
                                        it = OpenFileIntent.getPdfFileIntent(path);
                                    } else if (path.substring(path.length() - 4).equals(".doc")) {
                                        it = OpenFileIntent.getWordFileIntent(path);
                                    } else {
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


    public void down_file(String url, String path) throws IOException {
        // 下载函数

        // 获取文件名
        URL myURL = new URL(url);
        URLConnection conn = myURL.openConnection();
        conn.connect();
        InputStream is = null;
        try {
            is = conn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            Looper.prepare();
            Toast.makeText(DocsendDetail.this, "没有找到文件！", Toast.LENGTH_SHORT).show();
            Looper.loop();
        }

        this.fileSize = conn.getContentLength();// 根据响应获取文件大小
        if (this.fileSize <= 0)
            throw new RuntimeException("无法获知文件大小 ");
        if (is == null)
            throw new RuntimeException("stream is null");
        FileOutputStream fos = new FileOutputStream(path);
        // 把数据存入路径+文件名
        byte buf[] = new byte[1024];
        downLoadFileSize = 0;
        sendMsg(0);

        BufferedInputStream bis = new BufferedInputStream(is);
        int numread = 0;
        int times = 0;//设置更新频率，频繁操作ＵＩ线程会导致系统奔溃
        while ((numread = bis.read(buf)) != -1) {
            fos.write(buf, 0, numread);
            downLoadFileSize += numread;
            //Log.i("num", rate+","+total+","+p);
            if (times >= 512 || times == 0 || downLoadFileSize == this.fileSize) {/*
                    这是防止频繁地更新通知，而导致系统变慢甚至崩溃。
                                                             非常重要。。。。。*/
                Log.i("time", "time");
                times = 0;
                sendMsg(1);
            }
            times++;

        }
        sendMsg(2);// 通知下载完成
        try {
            fos.close();
            is.close();
        } catch (Exception ex) {
            Log.e("tag", "error: " + ex.getMessage(), ex);
        }

    }

    private void sendMsg(int flag) {
        Message msg = new Message();
        msg.what = flag;
        handler.sendMessage(msg);
    }

    private void notificationInit(String name) {
        Common commom = new Common();
        //通知栏内显示下载进度条
        File file = new File(name);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String type = commom.getMIMEType(file);
        intent.setDataAndType(Uri.parse("file://" + file.getAbsolutePath()), type);//点击进度条，进入相应界面
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        mNotificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        mNotification = new Notification();
        mNotification.icon = R.drawable.fj_icon;
        mNotification.tickerText = "开始下载";
        mNotification.contentView = new RemoteViews(getPackageName(), R.layout.download_pro);//通知栏中进度布局
        mNotification.contentIntent = pIntent;
//  mNotificationManager.notify(0,mNotification);
    }


    public void setListViewHeightBasedOnChildren(ListView listView) {//计算listview的和

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

    //将dp转换为像素
    public int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bc.setCheckid("");
        bc.setUserid("");
    }
}
