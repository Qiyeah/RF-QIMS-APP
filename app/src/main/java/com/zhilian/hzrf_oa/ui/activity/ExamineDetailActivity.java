package com.zhilian.hzrf_oa.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
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
import com.zhilian.hzrf_oa.adapter.RecordAdapter;
import com.zhilian.hzrf_oa.adapter.SelectmanAdapter;
import com.zhilian.hzrf_oa.adapter.TextAdapter;
import com.zhilian.hzrf_oa.common.BusinessContant;
import com.zhilian.hzrf_oa.common.Common;
import com.zhilian.hzrf_oa.entity.Opinion;
import com.zhilian.hzrf_oa.json.ExamineDetailBean;
import com.zhilian.hzrf_oa.json.T_FJList;
import com.zhilian.hzrf_oa.json.T_Record;
import com.zhilian.hzrf_oa.json.T_Selectman;
import com.zhilian.hzrf_oa.listener.OnTextChangedListener;
import com.zhilian.hzrf_oa.net_exception.ITimeOutException;
import com.zhilian.hzrf_oa.net_exception.TimeOutException;
import com.zhilian.hzrf_oa.ui.widget.NoScrollListView;
import com.zhilian.hzrf_oa.util.DownLoadUtil;
import com.zhilian.hzrf_oa.util.LogUtil;
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
 * Created by Administrator on 2017-9-20.
 *
 * 初审分办
 */
public class ExamineDetailActivity extends Activity {
    float fileSize;
    float downLoadFileSize;

    private static final String TAG = "Response";
    private Button bt_record;// 记录
    private Button bt_writeopinion1;// 填写意见
    //    private Button bt_writeopinion2;// 填写意见
//    private Button bt_writeopinion3;// 填写意见
    private Button bt_submit;// 提交
    private Spinner spinner;
    private ArrayAdapter<String> arrayAdapter;
    private EditText opinion;
    private String[] opinions = {"--请选择常用意见--","同意。","已核。","已阅。"};
    private RadioGroup radioGroup;
    private CheckBox checkBox;
    private List<CheckBox> checkBoxs = new ArrayList<CheckBox>();
    private RadioButton tempButton;
    private TextView from_unit, date, tv_uname,  tv_count, tv_security, tv_doflag, tv_docno, tv_doctitle, tv_title;
    private TextView tv_opinion1;
    private ListView recordListView, mansListview;// 流转记录列表
    private ListView receiverListView;//签收人列表
    private NoScrollListView fjListView;//附件列表
    private TextAdapter fjAdapter;
    private String checked_id;

    private ExamineDetailBean json;

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

        setContentView(R.layout.examine_detail);
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
        tv_count = (TextView) findViewById(R.id.count);
        tv_security = (TextView) findViewById(R.id.security);
        tv_doflag = (TextView) findViewById(R.id.doflag);
        tv_docno = (TextView) findViewById(R.id.docno);
        tv_doctitle = (TextView) findViewById(R.id.doctitle);

        //tv_test=(TextView)findViewById(R.id.test);
        fjListView = (NoScrollListView) findViewById(R.id.fj_list);
        tv_opinion1 = (TextView) findViewById(R.id.opinion1);
        addData();
        initView();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }


    private void initView() {
        opinion = (EditText) findViewById(R.id.nothing);//防止报错
//        bt_receive = (Button) findViewById(R.id.receive);
        //bt_receive.setOnClickListener(receivelistener);
        bt_record = (Button) findViewById(R.id.record);
        // bt_record.setOnClickListener(listener1);
//        bt_backspace = (Button) findViewById(R.id.bt_backspace);
        //  bt_backspace.setOnClickListener(backSpaceListener);
        bt_writeopinion1 = (Button) findViewById(R.id.bt_opinion1);
//        bt_writeopinion2 = (Button) findViewById(R.id.bt_opinion2);
//        bt_writeopinion3 = (Button) findViewById(R.id.bt_opinion3);
        bt_writeopinion1.setOnClickListener(writeopinionlistener);
        // bt_writeopinion2.setOnClickListener(writeopinionlistener);
        // bt_writeopinion3.setOnClickListener(writeopinionlistener);
//        bt_receiver = (Button) findViewById(R.id.bt_receiver);
        // bt_receiver.setOnClickListener(viewReceivers);
        bt_submit = (Button) findViewById(R.id.submit);
        bt_submit.setOnClickListener(listener3);
//        bt_point = (Button) findViewById(R.id.bt_point);
        // bt_point.setOnClickListener(pointListener);

        fjListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                Context mContext = getApplicationContext();
                //点击下载或打开文件
                name = lists.get(position).getName();
                url = lists.get(position).getUrl();
              //  Log.e("Response", "onItemClick: url = "+url);
                downLoad.context = ExamineDetailActivity.this;
                downLoad.name = url;
                downLoad.downPath = bc.SAVEPATH + url;
                downLoad.downUrl = bc.DOWNLOADURL + url;
                downLoad.DownOrOpen();

            }
        });

        date = (TextView) findViewById(R.id.receivedate);
    }


    private void addData() {
        new Thread() {
            public void run() {
                String key = bc.getCONFIRM_ID();
                String url = bc.URL;
                Map<String, String> queryParas = common.getQueryParas();
                url = RequestUtil.buildUrlWithQueryString(url, queryParas);
                final InQueryMsg inQueryMsg = new InQueryMsg(1348831860, "query", key);
                inQueryMsg.setQueryName("AchieveDetail");
                HashMap<String, String> map = new HashMap<>();
                map.put("docid", docid);
                map.put("isdone", isdone);
                inQueryMsg.setQueryPara(map);
                String postData = null;
                postData = common.getPostData(inQueryMsg);
                //System.out.println("发送前的明文：" + postData);
                // Log.e("Response", "postData -> " + postData);
                RequestQueue requestQueue = RequestUtil.getRequestQueue();

                JsonRequest jsonRequest = new JsonStringRequest(Request.Method.POST, url, postData,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", " -> " + response);
                            if (response.toString().equals(bc.ERROR)) {
                                new TimeOutException().reLogin(getApplicationContext(), new ITimeOutException.CallBack(){
                                    @Override
                                    public void onReloginSuccess() {
                                        addData();
                                    }
                                });
                                Toast.makeText(ExamineDetailActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                  //  Log.e("Response", "-> " + response);
                                    json = JSON.parseObject(response, ExamineDetailBean.class);
                                    String unit = json.getUnit();
                                    String receivedate = json.getDate();
                                    String count = json.getCount();
                                    String security = json.getSecurity();
                                    String uname = json.getUname();
                                    docno = json.getDocno();
                                    String opinion1 = json.getOpinion();
                                    String opinionTime = json.getOpinionTime();
                                    opinions = json.getOpinions();
                                    atype = json.getAtype();
                                    backLastStep = json.getBacklaststep();
                                    bc.setItem_id(json.getItemid());
                                    bc.setPid(String.valueOf(json.getWf().getId()));
                                    String doctitle = json.getTitle();
                                    List<T_FJList> fjlist1 = json.getFjlist();   //附件列表
                                    for (int i = 0; i < fjlist1.size(); i++) {//附件列表
                                        lists.add(fjlist1.get(i));
                                    }
                                    fjAdapter = new TextAdapter(lists, getApplicationContext());
                                    fjListView.setAdapter(fjAdapter);
                                    from_unit.setText(unit);
                                    tv_uname.setText(uname);
                                    date.setText(receivedate);
                                    tv_count.setText(count);
                                    tv_security.setText(security);
                                    tv_doflag.setText(json.getType());
                                    tv_docno.setText(docno.trim());
                                    tv_doctitle.setText(doctitle);
                                    bc.setOpinion(opinion1);
                                    if (StrKit.notBlank(opinion1)){
                                        tv_opinion1.setText(getOpinions(opinion1,opinionTime));
                                        bt_submit.setVisibility(View.VISIBLE);
                                    }
                                    bt_writeopinion1.setVisibility(View.VISIBLE);
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

                                            tempButton = new RadioButton(ExamineDetailActivity.this);
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
                                                tempButton = new RadioButton(ExamineDetailActivity.this);

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

    private String opinionStr;
    private String docno;
    private View.OnClickListener writeopinionlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ExamineDetailActivity.this);
            builder.setTitle("填写意见");
            View view = getLayoutInflater().inflate(R.layout.button2_layout, null);
            opinion = (EditText) view.findViewById(R.id.editopinion);
            opinion.setText(bc.getOpinion());
            /**
             * 每次填写意见时，显示已经缓存过的意见
             */
            opinionStr = OpinionUtil.getInstance(getApplicationContext())
                .getOpinion(docno,bc.getItem_id());
//           LogUtil.e("opinion","opinion = "+opinionStr);

            if (StrKit.isBlank(opinionStr)){
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
                            new Opinion(docno,bc.getItem_id(),opinionStr));
                    bc.setOpinion(opinionStr);

                }
            });
            spinner = (Spinner) view.findViewById(R.id.responsible_person);
            arrayAdapter = new ArrayAdapter<String>(ExamineDetailActivity.this, android.R.layout.simple_spinner_item, opinions);
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
                        .clearOpinion(docno,bc.getItem_id());
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                   String str = opinion.getText().toString().trim();
                    if (StrKit.notBlank(str)) {
                       // LogUtil.e("rep",json.getTempopinion1());
                        String tempOpinion = getOpinions(json.getTempopinion1(),null);
                        tv_opinion1.setText(tempOpinion);
                        //LogUtil.e("rep",str);
                        json.setOpinion(str);
                        writeopinion("0");
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
    private View.OnClickListener listener3 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ExamineDetailActivity.this);
            builder.setTitle("下一环节");
            View view = getLayoutInflater().inflate(R.layout.button3_layout, null);
            //stepListView=(ListView)view.findViewById(R.id.steps);
            tv_title = (TextView) view.findViewById(R.id.title);
            radioGroup = (RadioGroup) view.findViewById(R.id.selectman);
            mansListview = (ListView) view.findViewById(R.id.selectmans);
            if (bc.getItem_id() != null) {
             //   System.out.println("===itemid=" + bc.getItem_id());
                if (Common.judgeNet(ExamineDetailActivity.this)) {
                    Fasong();
                    if (StrKit.isBlank(bc.getNexttype()) ? false : bc.getNexttype().equals("toast")) {
                        builder.setTitle("下一环节处理人");
                    }
                } else {
                    Toast.makeText(ExamineDetailActivity.this, "请用新数据进行测试~", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(ExamineDetailActivity.this, "请选择！", Toast.LENGTH_SHORT).show();
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
    };

    /**
     * 获取当前意见域的全部意见
     *
     * @param tempOpinion 之前的意见
     * @return
     */
    private String getOpinions(String tempOpinion, String opinionTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String opinions = "";
        if (StrKit.notBlank(tempOpinion)) {
            opinions = tempOpinion.replace("&nbsp;", " ").replace("<br>", "\n")
                + opinion.getText() + "\n        " + json.getUname();
            if (StrKit.isBlank(opinionTime)){
               opinions += "  [" + sdf.format(new Date()) + "]";
            }else {
                opinions +="  [" + opinionTime + "]";
            }
        } else {
            opinions = opinion.getText() + "\n        " + json.getUname() + "  [" + sdf.format(new Date()) + "]";
        }
        return opinions;
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
       // Log.e(TAG, "json: "+json);
        map.put("itemid", json.getItemid());
        map.put("opinion", json.getOpinion());
        map.put("pid", String.valueOf(json.getWf().getId()));
        map.put("isreceiveopinion", type);
        map.put("docid", json.getDocid());

//        Log.e(TAG, "opinion: "+map.get("opinion"));
        insaveMsg.setModelProperty(map);
        String postData = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            postData = mapper.writeValueAsString(insaveMsg);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
       // System.out.println("发送前的明文：" + postData);
//        Log.e(TAG, "postData: "+postData);
        RequestQueue requestQueue = RequestUtil.getRequestQueue();

        JsonRequest jsonRequest = new JsonStringRequest(Request.Method.POST, url, postData,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
//                    Log.d(TAG, "response -> " + response.toString());
                    //System.out.println("解密后：" + response.toString());
                    //Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                    if (response.toString().equals("保存意见成功！")) {
                        Toast.makeText(ExamineDetailActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                        bt_submit.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(ExamineDetailActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage(), error);
                Toast.makeText(getApplicationContext(), "出错了!", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonRequest);

    }

    public void selectman(String operation1) {
        final String operation = operation1;
        String tempopinion =json.getOpinion();
//        Log.e(TAG, "tempopinion: "+tempopinion);
        if (StrKit.notBlank(json.getOpinionfield()) && StrKit.isBlank(tempopinion)) {
            Toast.makeText(ExamineDetailActivity.this, "请填写意见!", Toast.LENGTH_SHORT).show();
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
                                                    tempButton = new RadioButton(ExamineDetailActivity.this);
                                                    tempButton.setTextSize(16f);
                                                    tempButton.setText(name + " ( " + pname + " : " + sname + " )");
                                                    tempButton.setPadding(80, 0, 0, 0);// 设置文字距离按钮四周的距离
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
}
