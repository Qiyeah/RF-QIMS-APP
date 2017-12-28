package com.zhilian.hzrf_oa.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.adapter.ReceiverAdapter;
import com.zhilian.hzrf_oa.adapter.RecordAdapter;
import com.zhilian.hzrf_oa.adapter.SelectmanAdapter;
import com.zhilian.hzrf_oa.adapter.TextAdapter;
import com.zhilian.hzrf_oa.common.BusinessContant;
import com.zhilian.hzrf_oa.common.Common;
import com.zhilian.hzrf_oa.json.InnerSendDetailJson;
import com.zhilian.hzrf_oa.json.ReceiveDetailJson;
import com.zhilian.hzrf_oa.json.T_FJList;
import com.zhilian.hzrf_oa.json.T_InnerSend;
import com.zhilian.hzrf_oa.json.T_Record;
import com.zhilian.hzrf_oa.json.T_Selectman;
import com.zhilian.hzrf_oa.net_exception.ITimeOutException;
import com.zhilian.hzrf_oa.net_exception.TimeOutException;
import com.zhilian.hzrf_oa.ui.widget.NoScrollListView;
import com.zhilian.hzrf_oa.util.DownLoadUtil;
import com.zhilian.hzrf_oa.util.LogUtil;

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
 * 内部发文表（内部发文）
 */
public class InnerSendDetail extends Activity {
    private static final String TAG = InnerSendDetail.class.getName();
    private Button bt_record;// 记录
    private Spinner spinner;
    private ArrayAdapter<String> arrayAdapter;
    private EditText opinion;
    private String[] opinions = {};
    private RadioGroup radioGroup;
    private CheckBox checkBox;
    private List<CheckBox> checkBoxs = new ArrayList<CheckBox>();
    private RadioButton tempButton;
    private TextView from_unit, date, tv_uname, tv_auditor, tv_security, tv_docno, tv_doctitle;
    private TextView tv_opinion1, tv_opinion2, tv_opinion3;
    private ListView recordListView, mansListview;// 流转记录列表
    private ListView receiverListView;//签收人列表
    private NoScrollListView fjListView;//附件列表
    private TextAdapter fjAdapter;
    private String checked_id;

    //private ReceiveDetailJson json;
    private InnerSendDetailJson json;

    private SelectmanAdapter selectmanAdapter;
    private List<T_Record> recordList = new ArrayList<T_Record>();
    private List<T_Selectman> selectmenlist = new ArrayList<T_Selectman>();
    private List<T_FJList> lists = new ArrayList<T_FJList>();
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

        setContentView(R.layout.innersend_detail);
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
        //tv_uname = (TextView) findViewById(R.id.uname);
        //tv_auditor = (TextView) findViewById(R.id.auditor);
        tv_security = (TextView) findViewById(R.id.security);
        tv_docno = (TextView) findViewById(R.id.docno);
        tv_doctitle = (TextView) findViewById(R.id.doctitle);

        //tv_test=(TextView)findViewById(R.id.test);
        fjListView = (NoScrollListView) findViewById(R.id.fj_list);
        addData();
        initView();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }


    private void initView() {
        fjListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                Context mContext = getApplicationContext();
                //点击下载或打开文件
                name = lists.get(position).getName();
                url = lists.get(position).getUrl();
                downLoad.context = InnerSendDetail.this;
                downLoad.name = url;
                downLoad.downPath = bc.SAVEPATH + url;
                downLoad.downUrl = bc.DOWNLOADURL + url;
                downLoad.DownOrOpen();
            }
        });
    }


    private void addData() {
        new Thread() {
            public void run() {
                String key = bc.getCONFIRM_ID();
                String url = bc.URL;
                Map<String, String> queryParas = common.getQueryParas();
                url = RequestUtil.buildUrlWithQueryString(url, queryParas);
                final InQueryMsg inQueryMsg = new InQueryMsg(1348831860, "query", key);
                //请求接口
                inQueryMsg.setQueryName("InnerSendDetail");
                HashMap<String, String> map = new HashMap<>();
                Log.e(TAG, "docid: " + docid);
                map.put("docid", docid);
                map.put("isdone", isdone);
                inQueryMsg.setQueryPara(map);
                String postData = null;
                postData = common.getPostData(inQueryMsg);
                Log.e("Response", "postData: "+postData);
                //System.out.println("发送前的明文：" + postData);
                RequestQueue requestQueue = RequestUtil.getRequestQueue();
                JsonRequest jsonRequest = new JsonStringRequest(Request.Method.POST, url, postData,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e(TAG, "解密后：" + response.toString());
                            //System.out.println("解密后：" + response.toString());
                            //Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                            if (response.toString().equals(bc.ERROR)) {
                                Toast.makeText(InnerSendDetail.this, response.toString(), Toast.LENGTH_SHORT).show();
                                new TimeOutException().reLogin(InnerSendDetail.this, new ITimeOutException.CallBack(){
                                    @Override
                                    public void onReloginSuccess() {
                                        addData();
                                    }
                                });
                            } else {
                                try {
                                    json = JSON.parseObject(response, InnerSendDetailJson.class);
                                    // Log.e(TAG, "json -> " + json);
                                    String unit = json.getUnit();
                                    String receivedate = json.getSenddate();
                                    String security = json.getSecurity();
                                    String uname = json.getU_id();
                                    String docno = json.getDocno();
                                    String doctitle = json.getTitle();
                                    //int fjid = json.getFjid();   //附件id
                                    lists = json.getFjid();
                                    fjAdapter = new TextAdapter(lists, getApplicationContext());
                                    fjListView.setAdapter(fjAdapter);
                                    from_unit.setText(unit);
                                    //tv_uname.setText(uname);
                                    date.setText(receivedate);
                                    tv_security.setText(security);
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
}
