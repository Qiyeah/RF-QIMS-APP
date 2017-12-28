package com.zhilian.hzrf_oa.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
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
import com.zhilian.hzrf_oa.adapter.TextAdapter;
import com.zhilian.hzrf_oa.common.BusinessContant;
import com.zhilian.hzrf_oa.entity.OpinionBeen;
import com.zhilian.hzrf_oa.json.T_FJList;
import com.zhilian.hzrf_oa.json.T_Record;
import com.zhilian.hzrf_oa.util.DownLoadUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016-12-20.
 */
public class AchieveDetail extends Activity {

    private BusinessContant bc = new BusinessContant();
    private TextView from_unit, date, tv_uname, tv_count, tv_security, tv_doflag, tv_docno, tv_doctitle, tv_title, tv_test;
    private TextView tv_ld, tv_cy, tv_fj;
    private ListView fjListView, opinionListView, signListView, recordListView, mansListview;// 收文附件列表/领导意见列表/传阅意见列表/流转记录列表
    private TextAdapter fjAdapter;
    private OpinionAdapter opinionAdapter;
    private ImageView back;
    private Button button1;

    private List<T_FJList> fjlist = new ArrayList<T_FJList>();
    private List<OpinionBeen> opinionBeenList = new ArrayList<OpinionBeen>();
    private List<T_Record> recordList = new ArrayList<T_Record>();

    private DownLoadUtil downLoad = new DownLoadUtil();

    private RecordAdapter recordAdapter;//下载进度条
    private NotificationManager mNotificationManager = null;
    private Notification mNotification;
    private int fileSize;
    String name;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_achieve_detail);
        from_unit = (TextView) findViewById(R.id.from_unit);
        date = (TextView) findViewById(R.id.receivedate);
        tv_uname = (TextView) findViewById(R.id.uname);
        //tv_auditor=(TextView)findViewById(R.id.auditor);
        tv_count = (TextView) findViewById(R.id.count);
        //tv_superman=(TextView)findViewById(R.id.superman);
        tv_security = (TextView) findViewById(R.id.security);
        tv_doflag = (TextView) findViewById(R.id.doflag);
        tv_docno = (TextView) findViewById(R.id.docno);
        tv_doctitle = (TextView) findViewById(R.id.doctitle);
        //tv_test=(TextView)findViewById(R.id.test);
        fjListView = (ListView) findViewById(R.id.fj_list);
        opinionListView = (ListView) findViewById(R.id.opinionList);
        button1 = (Button) findViewById(R.id.button1);
        addData();
        initview();
    }

    public void addData() {

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
        inQueryMsg.setQueryName("getAchieveDetail");
        HashMap<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(bc.getDoc_id()));
        inQueryMsg.setQueryPara(map);
        String postData = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            postData = mapper.writeValueAsString(inQueryMsg);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        // System.out.println("发送前的明文：" + postData);
        RequestQueue requestQueue = RequestUtil.getRequestQueue();

        JsonRequest jsonRequest = new JsonStringRequest(Request.Method.POST, url, postData,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("TAG", "response -> " + response.toString());
                    // System.out.println("解密后：" + response.toString());
                    if (response.toString().equals("[]")) {

                    } else {
                        try {

                            JSONObject dataJson = new JSONObject(response.toString());
                            String unit = dataJson.getString("unit");
                            String receivedate = dataJson.getString("receivedate");
                            String auditor = dataJson.getString("auditor");
                            String count = dataJson.getString("count");
                            String superman = dataJson.getString("superman");
                            String security = dataJson.getString("security");
                            String doflag = dataJson.getString("doflag");
                            String uname = dataJson.getString("u_id");
                            String docno = dataJson.getString("docno");
                            String pid = dataJson.getString("pid");
                            String opinionfield = dataJson.getString("opinion6");
                            String doctitle = dataJson.getString("title");
                            JSONArray fjids = dataJson.getJSONArray("fjid");   //附件列表
                            JSONArray opinion1 = dataJson.getJSONArray("opinion1"); //领导意见
                            JSONArray opinion2 = dataJson.getJSONArray("opinion2"); //传阅意见
                            String itemid = dataJson.getString("opinion5");//因数据库中有6个意见域，实际只需2个，故借来用用
                            bc.setItem_id(itemid);
                            String fjname = "", opinion = "", sign = "", signdate = "", signimg = "";
                            bc.setPid(pid);
                            int id;
                            // 附件列表
                            List<T_FJList> list = JsonUtil.getFJList(fjids.toString());
                            for (int i = 0; i < list.size(); i++) {
                                fjname = list.get(i).getName();
                                id = list.get(i).getId();
                                String url = list.get(i).getUrl();
                                String size = list.get(i).getSize();
                                fjlist.add(new T_FJList(id, fjname, url, size));
                            }
                            fjAdapter = new TextAdapter(fjlist, getApplicationContext());
                            fjListView.setAdapter(fjAdapter);
                            if (fjids.length() > 0) {
                                setListViewHeightBasedOnChildren(fjListView);
                            }
                            // 审批意见
                            for (int i = 0; i < opinion1.length(); i++) {
                                JSONObject info = opinion1.getJSONObject(i);
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
                            opinionAdapter = new OpinionAdapter(opinionBeenList, getApplicationContext());
                            opinionListView.setAdapter(opinionAdapter);
                            if (opinion1.length() > 0) {
                                setListViewHeightBasedOnChildren(opinionListView);
                            }

                            from_unit.setText(unit);
                            tv_uname.setText(uname);
                            date.setText(receivedate);
                            tv_count.setText(count);
                            tv_security.setText(security);
                            if (doflag != null) {
                                if (doflag.equals("1")) {
                                    tv_doflag.setText("一般阅知");
                                } else {
                                    tv_doflag.setText("普通收文");
                                }
                            }
                            tv_docno.setText(docno);
                            tv_doctitle.setText(doctitle);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    public void initview() {
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fjListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                Context mContext = getApplicationContext();
                name = fjlist.get(position).getName();
                String url = fjlist.get(position).getUrl();
                downLoad.context = AchieveDetail.this;
                downLoad.name = url;
                downLoad.downPath = bc.SAVEPATH + url;
                downLoad.downUrl = bc.DOWNLOADURL + url;
                downLoad.DownOrOpen();
            }
        });
        button1.setOnClickListener(listener1);
    }

    private View.OnClickListener listener1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AchieveDetail.this);
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
                        // System.out.println("发送前的明文：" + postData);
                        RequestQueue requestQueue = RequestUtil.getRequestQueue();

                        JsonRequest jsonRequest = new JsonStringRequest(Request.Method.POST, url, postData,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("TAG", "response -> " + response.toString());
                                    // System.out.println("解密后：" + response.toString());

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