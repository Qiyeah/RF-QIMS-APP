package com.zhilian.hzrf_oa.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
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
import com.zhilian.hzrf_oa.adapter.StepAdapter;
import com.zhilian.hzrf_oa.adapter.TextAdapter;
import com.zhilian.hzrf_oa.common.BusinessContant;
import com.zhilian.hzrf_oa.entity.OpinionBeen;
import com.zhilian.hzrf_oa.json.ReceiveDetailJson;
import com.zhilian.hzrf_oa.json.T_FJList;
import com.zhilian.hzrf_oa.json.T_Record;
import com.zhilian.hzrf_oa.util.DownLoadUtil;
import com.zhilian.hzrf_oa.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/3.
 */
public class ReceiveCabDetail extends Activity {
    private Button button1;// 记录
    private Button button2;// 填写意见
    private Button button3;// 提交
    private TextView from_unit, date, tv_uname, tv_auditor, tv_count, tv_superman, tv_security, tv_doflag, tv_docno, tv_doctitle, tv_title, tv_test;
    private TextView tv_opinion1, tv_opinion2, tv_opinion3;
    private ListView fjListView, opinionListView, signListView, recordListView, mansListview;// 收文附件列表/领导意见列表/传阅意见列表/流转记录列表
    private TextAdapter fjAdapter;
    private ReceiveDetailJson json;
    private OpinionAdapter opinionAdapter, signAdapter;
    private StepAdapter stepAdapter;
    private SelectmanAdapter selectmanAdapter;
    private List<OpinionBeen> opinionBeenList = new ArrayList<OpinionBeen>();
    private List<OpinionBeen> signList = new ArrayList<OpinionBeen>();
    private List<T_Record> recordList = new ArrayList<T_Record>();
    private ArrayList<T_FJList> lists = new ArrayList<T_FJList>();
    private final static String ALBUM_PATH = "/mnt/sdcard/mypic_data/";
    BusinessContant bc = new BusinessContant();

    private DownLoadUtil downLoad = new DownLoadUtil();

    String name;
    private RecordAdapter recordAdapter;//下载进度条
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
        setContentView(R.layout.receive_detail);
        Intent intent = getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addData();
        initView();
    }

    private void initView() {
        from_unit = (TextView) findViewById(R.id.from_unit);
        date = (TextView) findViewById(R.id.receivedate);
        tv_uname = (TextView) findViewById(R.id.uname);
        tv_auditor = (TextView) findViewById(R.id.auditor);
        tv_count = (TextView) findViewById(R.id.count);
        tv_superman = (TextView) findViewById(R.id.superman);
        tv_security = (TextView) findViewById(R.id.security);
        tv_doflag = (TextView) findViewById(R.id.doflag);
        tv_docno = (TextView) findViewById(R.id.docno);
        tv_doctitle = (TextView) findViewById(R.id.doctitle);
        //tv_test=(TextView)findViewById(R.id.test);
        fjListView = (ListView) findViewById(R.id.fj_list);
        tv_opinion1 = (TextView) findViewById(R.id.opinion1);
        tv_opinion2 = (TextView) findViewById(R.id.opinion2);
        tv_opinion3 = (TextView) findViewById(R.id.opinion3);
        button1 = (Button) findViewById(R.id.record);
        button1.setOnClickListener(listener1);

        fjListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                Context mContext = getApplicationContext();
                name = lists.get(position).getName();
                String url = lists.get(position).getUrl();
                downLoad.context = ReceiveCabDetail.this;
                downLoad.mContext1 = ReceiveCabDetail.this;
                downLoad.name = url;
                downLoad.downPath = bc.SAVEPATH + url;
                downLoad.downUrl = bc.DOWNLOADURL + url;
                downLoad.DownOrOpen();
            }
        });
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
        inQueryMsg.setQueryName("ReceiveDetail");
        HashMap<String, String> map = new HashMap<>();
        map.put("projectName", "收文");
        map.put("docid", String.valueOf(bc.getDoc_id()));
        map.put("isdone", "1");
        inQueryMsg.setQueryPara(map);
        String postData = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            postData = mapper.writeValueAsString(inQueryMsg);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        LogUtil.i("i", "发送前的明文：" + postData);
        RequestQueue requestQueue = RequestUtil.getRequestQueue();

        JsonRequest jsonRequest = new JsonStringRequest(Request.Method.POST, url, postData,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("TAG", "response -> " + response.toString());
                    LogUtil.i("i", "解密后：" + response.toString());
                    //Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();

                    try {
                        json = JSON.parseObject(response, ReceiveDetailJson.class);
                        String unit = json.getUnit();
                        String receivedate = json.getDate();
                        String auditor = json.getAuditor();
                        String count = json.getCount();
                        String superman = json.getSuperman();
                        String security = json.getSecurity();
                        String uname = json.getUname();
                        String docno = json.getDocno();
                        String pid = String.valueOf(json.getWf().getId());
                        String opinionfield = json.getOpinionfield();
                        String doctitle = json.getTitle();
                        List<T_FJList> fjlist1 = json.getFjlist();   //附件列表
                        String opinion1 = json.getOpinion1(); //领导意见
                        String opinion2 = json.getOpinion2(); //传阅意见
                        String opinion3 = json.getOpinion3();//办理情况
                        String itemid = json.getItemid();
                        //bc.setItem_id(itemid);
                        bc.setPid(pid);
                        for (int i = 0; i < fjlist1.size(); i++) {//附件列表
                            lists.add(fjlist1.get(i));
                        }
                        fjAdapter = new TextAdapter(lists, getApplicationContext());
                        fjListView.setAdapter(fjAdapter);
                        from_unit.setText(unit);
                        tv_uname.setText(uname);
                        date.setText(receivedate);
                        if (StrKit.notBlank(auditor)) {
                            tv_auditor.setText(auditor);
                        }

                        tv_count.setText(count);
                        if (StrKit.notBlank(superman)) {
                            tv_superman.setText(superman);
                        }
                        if (StrKit.notBlank(opinion1)) {
                            tv_opinion1.setText(opinion1.replace("&nbsp;", " ").replace("<br>", "\n"));
                        }
                        if (StrKit.notBlank(opinion2)) {
                            tv_opinion2.setText(opinion2.replace("&nbsp;", " ").replace("<br>", "\n"));
                        }

                        if (StrKit.notBlank(opinion3)) {
                            tv_opinion3.setText(opinion3.replace("&nbsp;", " ").replace("<br>", "\n"));
                        }
                        tv_security.setText(security);
                        tv_doflag.setText(json.getType());
                        tv_docno.setText(docno);
                        tv_doctitle.setText(doctitle);
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

    private View.OnClickListener listener1 = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ReceiveCabDetail.this);
            builder.setTitle("流转记录");
            View view = getLayoutInflater().inflate(R.layout.button1_layout, null);
            recordListView = (ListView) view.findViewById(R.id.recordList);
            new Thread() {
                public void run() {
                    try {
                        String key = bc.getCONFIRM_ID();
                        String pid = String.valueOf(bc.getPid());//其实是pid
                        /*String url = bc.URL;
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
                        InQueryMsg inQueryMsg = new InQueryMsg(1348831860, "query", key);*/
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
                        inQueryMsg.setQueryName("ReceiveRecord");
                        HashMap<String, String> map = new HashMap<>();
                        map.put("projectName", "收文流转记录");
                        map.put("pid", pid);
                        inQueryMsg.setQueryPara(map);
                        String postData = null;
                        ObjectMapper mapper = new ObjectMapper();
                        try {
                            postData = mapper.writeValueAsString(inQueryMsg);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                        LogUtil.i("i", "发送前的明文：" + postData);
                        RequestQueue requestQueue = RequestUtil.getRequestQueue();

                        JsonRequest jsonRequest = new JsonStringRequest(Request.Method.POST, url, postData,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("TAG", "response -> " + response.toString());
                                    LogUtil.i("i", "解密后：" + response.toString());

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
                }
            });

            builder.create().show();
        }
    };

    public void setListViewHeightBasedOnChildren(ListView listView) {

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
}
