package com.zhilian.hzrf_oa.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.adapter.ReceiverAdapter;
import com.zhilian.hzrf_oa.adapter.RecordAdapter;
import com.zhilian.hzrf_oa.adapter.SelectmanAdapter;
import com.zhilian.hzrf_oa.adapter.TextAdapter;
import com.zhilian.hzrf_oa.common.BusinessContant;
import com.zhilian.hzrf_oa.common.Common;
import com.zhilian.hzrf_oa.entity.Opinion;
import com.zhilian.hzrf_oa.json.ReceiveDetailJson;
import com.zhilian.hzrf_oa.json.T_FJList;
import com.zhilian.hzrf_oa.json.T_Record;
import com.zhilian.hzrf_oa.json.T_Selectman;
import com.zhilian.hzrf_oa.listener.OnTextChangedListener;
import com.zhilian.hzrf_oa.net_exception.ITimeOutException;
import com.zhilian.hzrf_oa.net_exception.TimeOutException;
import com.zhilian.hzrf_oa.ui.widget.NoScrollListView;
import com.zhilian.hzrf_oa.util.DownLoadUtil;
import com.zhilian.hzrf_oa.util.LogUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhilian.api.InQueryMsg;
import com.zhilian.api.InSaveMsg;
import com.zhilian.api.JsonStringRequest;
import com.zhilian.api.JsonUtil;
import com.zhilian.api.ParaMap;
import com.zhilian.api.RequestUtil;
import com.zhilian.api.Sign;
import com.zhilian.api.StrKit;
import com.zhilian.hzrf_oa.util.OpinionUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 文件呈批表（收文管理）
 */
public class ReceiveDetail extends Activity {
    private Button bt_record;// 记录
    private Button bt_writeopinion1;// 填写意见
    private Button bt_writeopinion2;// 填写意见
    private Button bt_writeopinion3;// 填写意见
    private Button bt_submit;// 提交
    private Button bt_receive;//签收
    private Button bt_backspace;//退回
    private Button bt_point;//指派
    private Button bt_receiver;//查看签收人
    private Spinner spinner;
    private ArrayAdapter<String> arrayAdapter;
    private EditText opinion;
    private String[] opinions = {};
    private RadioGroup radioGroup;
    private CheckBox checkBox;
    private List<CheckBox> checkBoxs = new ArrayList<CheckBox>();
    private RadioButton tempButton;
    private TextView from_unit, date, tv_uname, tv_auditor, tv_count, tv_superman, tv_security, tv_doflag, tv_docno, tv_doctitle, tv_title, tv_test;
    private TextView tv_opinion1, tv_opinion2, tv_opinion3;
    private ListView recordListView, mansListview;// 流转记录列表
    private ListView receiverListView;//签收人列表
    private NoScrollListView fjListView;//附件列表
    private TextAdapter fjAdapter;
    private String checked_id;

    private ReceiveDetailJson json;

    private SelectmanAdapter selectmanAdapter;
    private List<T_Record> recordList = new ArrayList<T_Record>();
    private List<T_Selectman> selectmenlist = new ArrayList<T_Selectman>();
    private ArrayList<T_FJList> lists = new ArrayList<T_FJList>();
    private String mens;
    private String atype;
    String name;
    String url;
    String backLastStep;
    int result;
    private AlertDialog alertDialog;
    BusinessContant bc = new BusinessContant();
    Common common = new Common();
    String docid = "";
    String isdone = "";

    private DownLoadUtil downLoad = new DownLoadUtil();

    private RecordAdapter recordAdapter;//下载进度条


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.receive_detail);
        Intent intent = this.getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent
        docid = intent.getStringExtra("docid");
        isdone = intent.getStringExtra("isdone");

        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        fjListView = (NoScrollListView) findViewById(R.id.fj_list);
        tv_opinion1 = (TextView) findViewById(R.id.opinion1);
        tv_opinion2 = (TextView) findViewById(R.id.opinion2);
        tv_opinion3 = (TextView) findViewById(R.id.opinion3);
        addData();
        initView();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }


    private void initView() {
        opinion = (EditText) findViewById(R.id.nothing);//防止报错
        bt_receive = (Button) findViewById(R.id.receive);
        bt_receive.setOnClickListener(receivelistener);
        bt_record = (Button) findViewById(R.id.record);
        bt_record.setOnClickListener(listener1);
        bt_backspace = (Button) findViewById(R.id.bt_backspace);
        bt_backspace.setOnClickListener(backSpaceListener);
        bt_writeopinion1 = (Button) findViewById(R.id.bt_opinion1);
        bt_writeopinion2 = (Button) findViewById(R.id.bt_opinion2);
        bt_writeopinion3 = (Button) findViewById(R.id.bt_opinion3);
        bt_writeopinion1.setOnClickListener(writeopinionlistener);
        bt_writeopinion2.setOnClickListener(writeopinionlistener);
        bt_writeopinion3.setOnClickListener(writeopinionlistener);
        bt_receiver = (Button) findViewById(R.id.bt_receiver);
        bt_receiver.setOnClickListener(viewReceivers);
        bt_submit = (Button) findViewById(R.id.submit);
        bt_submit.setOnClickListener(listener3);
        bt_point = (Button) findViewById(R.id.bt_point);
        bt_point.setOnClickListener(pointListener);
        fjListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                Context mContext = getApplicationContext();
                //点击下载或打开文件
                name = lists.get(position).getName();
                url = lists.get(position).getUrl();
                downLoad.context = ReceiveDetail.this;
                downLoad.name = url;
                downLoad.downPath = bc.SAVEPATH + url;
                downLoad.downUrl = bc.DOWNLOADURL + url;
                downLoad.DownOrOpen();
            }
        });
    }

    String docno = null;

    private void addData() {
        new Thread() {
            public void run() {
                String key = bc.getCONFIRM_ID();
                String url = bc.URL;
                Map<String, String> queryParas = common.getQueryParas();
                url = RequestUtil.buildUrlWithQueryString(url, queryParas);
                final InQueryMsg inQueryMsg = new InQueryMsg(1348831860, "query", key);
                inQueryMsg.setQueryName("ReceiveDetail");
                HashMap<String, String> map = new HashMap<>();
                map.put("docid", docid);
                map.put("isdone", isdone);
                inQueryMsg.setQueryPara(map);
                String postData = null;
                postData = common.getPostData(inQueryMsg);
                //System.out.println("发送前的明文：" + postData);
                RequestQueue requestQueue = RequestUtil.getRequestQueue();

                JsonRequest jsonRequest = new JsonStringRequest(Request.Method.POST, url, postData,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("TAG", "response -> " + response.toString());
                            //System.out.println("解密后：" + response.toString());
                            //Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                            if (response.toString().equals(bc.ERROR)) {
                                new TimeOutException().reLogin(getApplicationContext(), new ITimeOutException.CallBack() {
                                    @Override
                                    public void onReloginSuccess() {
                                        addData();
                                    }
                                });
                                Toast.makeText(ReceiveDetail.this, response.toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    json = JSON.parseObject(response, ReceiveDetailJson.class);
                                    atype = json.getAtype();
                                    backLastStep = json.getBacklaststep();
                                    String unit = json.getUnit();
                                    String receivedate = json.getDate();
                                    String auditor = json.getAuditor();
                                    String count = json.getCount();
                                    String superman = json.getSuperman();
                                    String security = json.getSecurity();
                                    String uname = json.getUname();
                                    docno = json.getDocno();
                                    String isreceive = json.getMustsubmit();//判断当前处理人是否已签收
                                    String pid = String.valueOf(json.getWf().getId());

                                    String doctitle = json.getTitle();
                                    List<T_FJList> fjlist1 = json.getFjlist();   //附件列表
                                    String opinion1 = json.getOpinion1(); //领导意见
                                    String opinion2 = json.getOpinion2(); //传阅意见
                                    String opinion3 = json.getOpinion3();//办理情况
                                    String itemid = json.getItemid();
                                    String positionId = json.getPositionId();//当前处理人职位

                                    opinions = json.getOpinions();

                                    bc.setItem_id(itemid);
                                    bc.setPid(pid);
                                    bc.setOpinion(json.getOpinion());

                                    if (json.getReceiverList().size() > 0) {// 如果该文有人签收则 “签收人”按钮可见
                                        bt_receiver.setVisibility(View.VISIBLE);
                                    }

                                    if (itemid.equals("107")) { // 承办环节
                                        if (positionId.equals("12") || positionId.equals("14") || positionId.equals("16")) { // 正科级别
                                            if (isreceive.equals("0")) { // 没有签收
                                                bt_point.setVisibility(View.VISIBLE); // 允许指派
                                                json.setOpinionfield("opinion1");
                                                bt_writeopinion1.setVisibility(View.VISIBLE);
                                                if (StrKit.isBlank(json.getOpinion())) { // 没有填过批办意见
                                                    bt_receive.setVisibility(View.VISIBLE);  // 允许签收
                                                }
                                            } else {
                                                json.setOpinionfield("opinion3");
                                                bt_writeopinion3.setVisibility(View.VISIBLE);
                                                if (StrKit.notBlank(json.getOpinion())) { // 填过办理意见
                                                    bt_submit.setVisibility(View.VISIBLE);  // 允许提交
                                                }
                                            }
                                        } else {
                                            if (isreceive.equals("0")) { // 没有签收
                                                bt_receive.setVisibility(View.VISIBLE);  // 允许签收
                                            } else {
                                                json.setOpinionfield("opinion3");
                                                bt_writeopinion3.setVisibility(View.VISIBLE);
                                                if (StrKit.notBlank(json.getOpinion())) { // 填过办理意见
                                                    bt_submit.setVisibility(View.VISIBLE);  // 允许提交
                                                }
                                            }
                                        }
                                    } else {
                                        String opinionfield = json.getOpinionfield();
                                        if (StrKit.notBlank(opinionfield)) {//如果意见域不为空，则根据意见域判断显示哪个填写意见按钮
                                            switch (opinionfield) {
                                                case "opinion1":
                                                    bt_writeopinion1.setVisibility(View.VISIBLE);
                                                    break;
                                                case "opinion2":
                                                    bt_writeopinion2.setVisibility(View.VISIBLE);
                                                    break;
                                            }

                                            if (StrKit.notBlank(json.getOpinion())) {//判断当前处理人是否已填写意见
                                                bt_submit.setVisibility(View.VISIBLE);
                                                if (backLastStep.equals("1")) {//如果当前环节允许退回上步，则退回按钮可见
                                                    bt_backspace.setVisibility(View.VISIBLE);
                                                }
                                            }
                                        }
                                    }
                                    if (StrKit.notBlank(opinion1)) {
                                        LogUtil.e("rep", opinion1.replace("&nbsp;", " ").replace("<br>", "\n"));
                                        tv_opinion1.setText(opinion1.replace("&nbsp;", " ").replace("<br>", "\n"));
                                    }
                                    if (StrKit.notBlank(opinion2)) {
                                        tv_opinion2.setText(opinion2.replace("&nbsp;", " ").replace("<br>", "\n"));
                                    }
                                    if (StrKit.notBlank(opinion3)) {
                                        tv_opinion3.setText(opinion3.replace("&nbsp;", " ").replace("<br>", "\n"));
                                    }
                                    if ("3".equals(json.getAtype())) {
                                        bt_submit.setText("归档");
                                    }
//                                    if ("周玲凤".equals(json.getUname()) && isdone.equals("0")) {
//                                        bt_submit.setVisibility(View.VISIBLE);
//                                        bt_receive.setVisibility(View.GONE);
//                                        bt_backspace.setVisibility(View.GONE);
//                                    }
                                    if (json.getItemid().equals("26")) { // 传阅环节
                                        bt_receive.setVisibility(View.GONE);
                                        bt_backspace.setVisibility(View.GONE);
                                    }
                                    if (isdone.equals("1")) {//如果为已办收文，则所有操作按钮皆不可见
                                        bt_writeopinion1.setVisibility(View.GONE);
                                        bt_writeopinion2.setVisibility(View.GONE);
                                        bt_writeopinion3.setVisibility(View.GONE);
                                        bt_submit.setVisibility(View.GONE);
                                        bt_receive.setVisibility(View.GONE);
                                        bt_backspace.setVisibility(View.GONE);
                                        bt_point.setVisibility(View.GONE);
                                    }
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

                                    if (StrKit.notBlank(count)) {
                                        tv_count.setText(count);
                                    }
                                    if (StrKit.notBlank(superman)) {
                                        tv_superman.setText(superman);
                                    }
                                    tv_security.setText(security);
                                    tv_doflag.setText(json.getType());
                                    tv_docno.setText(docno);
                                    tv_doctitle.setText(doctitle);
                                } catch (Exception e) {
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
        }.start();
    }

    private void receive() {
        new Thread() {
            @Override
            public void run() {
                BusinessContant bc = new BusinessContant();
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
                inQueryMsg.setQueryName("receive");
                HashMap<String, String> map = new HashMap<>();
                map.put("isreceive", json.getWf().getIsreceive());
                map.put("pid", String.valueOf(json.getWf().getId()));
                map.put("docid", json.getDocid());
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
                            Toast.makeText(ReceiveDetail.this, response, Toast.LENGTH_SHORT).show();
                            if (response.equals("签收操作成功，请填写办理情况！")) {
                               /* bt_writeopinion3.setVisibility(View.VISIBLE);
                                bt_receive.setVisibility(View.GONE);*/
                                finish();
                            }
                        }
                    }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                        Toast.makeText(ReceiveDetail.this, "出错了!", Toast.LENGTH_LONG).show();
                    }
                });
                requestQueue.add(jsonRequest);

            }
        }.start();
    }

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
                    map.put("curitemid", bc.getItem_id());//环节ID
                    map.put("pid", bc.getPid());
                    map.put("doc", "receive");
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
                                    String ato, name;
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

                                            tempButton = new RadioButton(ReceiveDetail.this);
                                            tempButton.setText(ato);
                                            tempButton.setTextColor(0xFF505050);
                                            tempButton.setTextSize(16f);
                                            tempButton.setPadding(80, 0, 0, 0);                 // 设置文字距离按钮四周的距离
                                            tempButton.setId(id);
                                            tempButton.setText(ato);
                                            //radioGroup.addView(tempButton, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

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
                                        String amount = dataJson.getString("type");//你跟踪看一下这个值有没有不一样
                                        String sname, pname;
                                        List<T_Selectman> list = JsonUtil.getselectmanList(user.toString());
                                        for (int i = 0; i < user.length(); i++) {
                                            id = list.get(i).getId();
                                            name = list.get(i).getName();
                                            sname = list.get(i).getD_id();
                                            pname = list.get(i).getPid();

                                            if (amount.equals("1")) {
                                                tempButton = new RadioButton(ReceiveDetail.this);

                                                tempButton.setTextSize(16f);
                                                tempButton.setText(name + " ( " + pname + " : " + sname + " )");
                                                tempButton.setPadding(80, 0, 0, 0);  // 设置文字距离按钮四周的距离
                                                tempButton.setTextSize(16f);
                                                tempButton.setId(id);
                                                //radioGroup.addView(tempButton, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                radioGroup.addView(tempButton);
                                                if (i == 0) {
                                                    radioGroup.check(tempButton.getId());
                                                }
                                            } else {
                                                selectmenlist.add(new T_Selectman(id, name, pname, sname));
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

    public void selectDMan() {
        String key = bc.getCONFIRM_ID();
        String url = bc.URL;
        Map<String, String> queryParas = Common.getQueryParas();
        url = RequestUtil.buildUrlWithQueryString(url, queryParas);
        InQueryMsg inQueryMsg = new InQueryMsg(1348831860, "query", key);
        inQueryMsg.setQueryName("getManInDept");
        HashMap<String, String> map = new HashMap<>();
        inQueryMsg.setQueryPara(map);
        String postData = Common.getPostData(inQueryMsg);
        RequestQueue requestQueue = RequestUtil.getRequestQueue();

        JsonRequest jsonRequest = new JsonStringRequest(Request.Method.POST, url, postData,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    LogUtil.v("v", response);
                    selectmenlist.clear();
                    if (!response.equals(bc.ERROR)) {
                        List<T_Selectman> list = JSON.parseArray(response, T_Selectman.class);
                        for (int i = 0; i < list.size(); i++) {
                            selectmenlist.add(list.get(i));
                        }
                        selectmanAdapter = new SelectmanAdapter(selectmenlist, ReceiveDetail.this);
                        mansListview.setAdapter(selectmanAdapter);
                    } else {
                        new TimeOutException().reLogin(getApplicationContext(), new ITimeOutException.CallBack() {
                            @Override
                            public void onReloginSuccess() {
                                selectDMan();
                            }
                        });
                    }
                }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.e("e", error.getMessage());
                Toast.makeText(getApplicationContext(), "出错了!", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonRequest);
    }


    public void selectman(String operation1) {
        final String operation = operation1;
        String tempopinion = opinion.getText().toString();
        if (StrKit.notBlank(json.getOpinionfield()) && StrKit.isBlank(tempopinion)) {
            Toast.makeText(ReceiveDetail.this, "请填写意见!", Toast.LENGTH_SHORT).show();
        } else {
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
                        InSaveMsg insaveMsg = new InSaveMsg(1348831860, "save", key);
                        insaveMsg.setModelName("receivesave");
                        HashMap<String, String> map = new HashMap<>();
                        map.put("nexttype", bc.getNexttype());//要改的
                        if (atype.equals("3") || operation.equals("TuiHuiShangBu")) {//如果为完成操作或是退货操作，传回当前环节
                            map.put("nextitemid", bc.getItem_id());
                        } else {
                            map.put("nextitemid", bc.getCheckid());
                        }
                        map.put("nexttodoman", bc.getUserid());//选中人的id
                        map.put("pid", bc.getPid());
                        map.put("doc", "receive");
                        map.put("operation", operation);
                        map.put("opinionfield", json.getOpinionfield());
                        map.put("opinion", opinion.getText().toString());
                        map.put("flow", "receive");
                        insaveMsg.setModelProperty(map);
                        String postData = null;
                        ObjectMapper mapper = new ObjectMapper();
                        try {
                            postData = mapper.writeValueAsString(insaveMsg);
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
                                    try {
                                        JSONObject dataJson = new JSONObject(response);
                                        String nexttype = dataJson.getString("nextType");
                                        String amount = dataJson.getString("type");
                                        if (nexttype.equals("toast")) {
                                            Toast.makeText(getApplicationContext(), amount, Toast.LENGTH_LONG).show();
                                            finish();
                                        } else {
                                            JSONArray users = dataJson.getJSONArray("user");
                                            bc.setNexttype("toast");
                                            String name, sname, pname;
                                            int id;
                                            JSONArray user = dataJson.getJSONArray("user");
                                            List<T_Selectman> list = JsonUtil.getselectmanList(users.toString());
                                            radioGroup.removeAllViews();
                                            for (int i = 0; i < user.length(); i++) {
                                                id = list.get(i).getId();
                                                name = list.get(i).getName();
                                                sname = list.get(i).getD_id();
                                                pname = list.get(i).getPid();

                                                if (amount.equals("1")) {
                                                    tempButton = new RadioButton(ReceiveDetail.this);

                                                    tempButton.setTextSize(16f);
                                                    tempButton.setText(name + " ( " + pname + " : " + sname + " )");
                                                    tempButton.setPadding(80, 0, 0, 0);                 // 设置文字距离按钮四周的距离
                                                    tempButton.setId(id);
                                                    //radioGroup.addView(tempButton, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                    radioGroup.addView(tempButton);
                                                    if (i == 0) {
                                                        radioGroup.check(tempButton.getId());
                                                        System.out.println("=====");
                                                    }
                                                } else {
                                                    selectmenlist.add(new T_Selectman(id, name, pname, sname));
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
                                LogUtil.e("e", error.getMessage());
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
    }

    public void writeopinion(String type) {
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
        final InSaveMsg insaveMsg = new InSaveMsg(1348831860, "save", key);
        insaveMsg.setModelName("editopinion");
        HashMap<String, String> map = new HashMap<>();
        map.put("itemid", json.getItemid());
        map.put("opinion", opinion.getText().toString());
        map.put("pid", String.valueOf(json.getWf().getId()));
        map.put("isreceiveopinion", type);
        map.put("docid", json.getDocid());
        insaveMsg.setModelProperty(map);
        String postData = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            postData = mapper.writeValueAsString(insaveMsg);
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
                    if (response.toString().equals("保存意见成功！")) {
                        Toast.makeText(ReceiveDetail.this, response.toString(), Toast.LENGTH_SHORT).show();
                        if (json.getItemid().equals("107") && json.getMustsubmit().equals("0")) {//如果是承办则指派可见，否则为提交
                            bt_receive.setVisibility(View.GONE);
                        } else {
                            bt_submit.setVisibility(View.VISIBLE);
                            if (backLastStep.equals("1")) {
                                bt_backspace.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        Toast.makeText(ReceiveDetail.this, response, Toast.LENGTH_SHORT).show();
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

    private View.OnClickListener receivelistener = new View.OnClickListener() {//签收按钮监听
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ReceiveDetail.this);
            builder.setTitle("确认窗口");
            builder.setMessage("该操作代表此文将由您负责办理,是否确认？");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    bt_writeopinion1.setVisibility(View.GONE);

                    receive();
                    //json.getWf().setIsreceive("1");
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.create().show();
        }
    };

    private View.OnClickListener backSpaceListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ReceiveDetail.this);

            builder.setTitle("确认窗口");
            builder.setMessage(bc.BACK_LAST_STEP);
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                }
            });
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    bc.setNexttype("toast");
                    selectman("TuiHuiShangBu");
                }
            });
            alertDialog = builder.create();
            alertDialog.show();
        }

    };

    private View.OnClickListener listener1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ReceiveDetail.this);
            builder.setTitle("流转记录");
            View view = getLayoutInflater().inflate(R.layout.button1_layout, null);
            recordListView = (ListView) view.findViewById(R.id.recordList);
            new Thread() {
                public void run() {
                    try {
                        String key = bc.getCONFIRM_ID();
                        String pid = String.valueOf(bc.getPid());//其实是pid
                        //String url = "http://192.168.9.126:8083/Api";
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
                }
            });

            builder.create().show();
        }
    };

    String opinionStr = null;
    private View.OnClickListener writeopinionlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ReceiveDetail.this);
            builder.setTitle("填写意见");
            View view = getLayoutInflater().inflate(R.layout.button2_layout, null);
            opinion = (EditText) view.findViewById(R.id.editopinion);
            /**
             * 每次填写意见时，显示已经缓存过的意见
             */
            opinionStr = OpinionUtil.getInstance(getApplicationContext())
                .getOpinion(docno, bc.getItem_id());
//           LogUtil.e("opinion","opinion = "+opinionStr);

            if (StrKit.isBlank(opinionStr)) {
                opinionStr = bc.getOpinion();
            }
            opinion.setText(opinionStr);
            /**
             * 监听填写意见的文本框
             */
            opinion.addTextChangedListener(new OnTextChangedListener() {
                @Override
                public void afterTextChanged(Editable s) {
                    opinionStr = opinion.getText().toString().trim();
                    /**
                     * 填写意见的文本框内容改变时，缓存意见
                     */
                    OpinionUtil.getInstance(getApplicationContext())
                        .saveOpinion(
                            new Opinion(docno, bc.getItem_id(), opinionStr));
                    bc.setOpinion(opinionStr);

                }
            });
            spinner = (Spinner) view.findViewById(R.id.responsible_person);
            arrayAdapter = new ArrayAdapter<String>(ReceiveDetail.this, android.R.layout.simple_spinner_item, opinions);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String edit = opinions[position];
                    if (position > 0) {
                        opinion.setText(edit + "");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            spinner.setAdapter(arrayAdapter);
            builder.setView(view);// 使用自定义布局作为对话框内容

            // 正面语义按钮：确定 支持。。。。。。
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    /**
                     * 意见保存到后台时，清除缓存
                     */
                    OpinionUtil.getInstance(getApplicationContext())
                        .clearOpinion(docno, bc.getItem_id());

                    if (StrKit.notBlank(opinion.getText().toString())) {
                        if (json.getItemid().equals("107")) {
                            if (json.getMustsubmit().equals("0")) {
                                tv_opinion1.setText(getOpinions(json.getTempopinion1()));
                                writeopinion("0");
                            } else {
                                tv_opinion3.setText(getOpinions(json.getTempopinion3()));
                                writeopinion("1");
                            }
                        } else {
                            switch (json.getOpinionfield()) {
                                case "opinion1":
                                    tv_opinion1.setText(getOpinions(json.getTempopinion1()));
                                    bt_receive.setVisibility(View.GONE);
                                    writeopinion("0");
                                    break;
                                case "opinion2":
                                    tv_opinion2.setText(getOpinions(json.getTempopinion2()));
                                    writeopinion("0");
                                    bt_receive.setVisibility(View.GONE);
                                    break;
                            }
                        }
                    }
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

    /**
     * 点击查看签收人
     */
    private View.OnClickListener viewReceivers = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ReceiveDetail.this);
            builder.setTitle("已签收的人");
            View view = getLayoutInflater().inflate(R.layout.button1_layout, null);
            receiverListView = (ListView) view.findViewById(R.id.recordList);
            ReceiverAdapter adapter = new ReceiverAdapter(json.getReceiverList(), getApplicationContext());
            receiverListView.setAdapter(adapter);
            builder.setView(view);// 使用自定义布局作为对话框内容
            builder.setNegativeButton("关闭", null);// 负面语义按钮
            builder.create().show();
        }
    };
    private View.OnClickListener pointListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ReceiveDetail.this);
            builder.setTitle("指派处理人");

            View view = getLayoutInflater().inflate(R.layout.button3_layout, null);
            //stepListView=(ListView)view.findViewById(R.id.steps);
            tv_title = (TextView) view.findViewById(R.id.title);
            mansListview = (ListView) view.findViewById(R.id.selectmans);
            selectDMan();//寻找同部门的人
            builder.setView(view);// 使用自定义布局作为对话框内容

            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selectman("ZhiPai");
                }
            });

            // 负面语义按钮
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog = builder.create();
            alertDialog.show();
        }
    };
    private View.OnClickListener listener3 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ReceiveDetail.this);
            if (atype.equals("3")) {//环节类型 3为结束 实现完成操作
                builder.setTitle("确认窗口");
                builder.setMessage("此操作将结束流程，是否继续？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        selectman("WanCheng");
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
            } else {//查找下一处理人或下一环节
                builder.setTitle("下一环节");
                View view = getLayoutInflater().inflate(R.layout.button3_layout, null);
                //stepListView=(ListView)view.findViewById(R.id.steps);
                tv_title = (TextView) view.findViewById(R.id.title);
                radioGroup = (RadioGroup) view.findViewById(R.id.selectman);
                mansListview = (ListView) view.findViewById(R.id.selectmans);
                if (bc.getItem_id() != null) {
                    System.out.println("===itemid=" + bc.getItem_id());
                    if (Common.judgeNet(ReceiveDetail.this)) {
                        Fasong();
                        if (StrKit.isBlank(bc.getNexttype()) ? false : bc.getNexttype().equals("toast")) {
                            builder.setTitle("下一环节处理人");
                        }
                    } else {
                        Toast.makeText(ReceiveDetail.this, "请用新数据进行测试~", Toast.LENGTH_SHORT).show();
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
                                selectman("FaSongdoc");
                            } else {
                                Toast.makeText(ReceiveDetail.this, "请选择！", Toast.LENGTH_SHORT).show();
                            }

                            if (bc.getNexttype().equals("toast")) {
                                alertDialog.dismiss();

                            } else {
                                alertDialog.setTitle("下一环节处理人");
                                alertDialog.show();
                            }
                        }

                    });
                }
            }
        }
    };

    /**
     * 获取当前意见域的全部意见
     *
     * @param tempOpinion 之前的意见
     * @return
     */
    private String getOpinions(String tempOpinion) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String opinions = "";
        if (StrKit.notBlank(tempOpinion)) {
            opinions = tempOpinion.replace("&nbsp;", " ").replace("<br>", "\n")
                + "  " + opinion.getText() + "\n        " + json.getUname() + "  [" + sdf.format(new Date()) + "]\n\n";
        } else {
            opinions = "  " + opinion.getText() + "\n        " + json.getUname() + "  [" + sdf.format(new Date()) + "]\n\n";
        }
        return opinions;
    }


}
