package com.zhilian.hzrf_oa.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.zhilian.api.ParaMap;
import com.zhilian.api.RequestUtil;
import com.zhilian.api.Sign;
import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.adapter.DocManageAdapter;
import com.zhilian.hzrf_oa.common.BusinessContant;
import com.zhilian.hzrf_oa.json.PageAchieve;
import com.zhilian.hzrf_oa.json.T_Achieve;
import com.zhilian.hzrf_oa.ui.widget.LoadListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 一般阅知
 */
public class AchieveManageActivity extends Activity implements AbsListView.OnScrollListener, LoadListView.ILoadListener {
    // 设置一个最大的数据条数，超过即不再加载
    private int MaxDateNum;
    // 最后可见条目的索引
    private int lastVisibleIndex;
    private BusinessContant bc = new BusinessContant();
    private ListView listView;// 收文管理列表
    View footer;
    private DocManageAdapter adapter;
    private List<T_Achieve> adapterlist = new ArrayList<T_Achieve>();
    private List<T_Achieve> list;
    private Handler myhandler = new Handler();
    ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
    ArrayList<String> mListTitle = new ArrayList<String>();
    ArrayList<String> mListText = new ArrayList<String>();
    ArrayList<String> mListDate = new ArrayList<String>();
    ArrayList<String> mListUnit = new ArrayList<String>();
    ArrayList<Integer> mListStatus = new ArrayList<Integer>();
    ArrayList<Integer> mListId = new ArrayList<Integer>();
    String docno, unit, starttime, receivedate, title;
    int status;
    int pageNumber = 1;//加载数据页码
    int pageNumbers = 1;//查询数据页码
    private String content;//搜索框内容

    private EditText editText;
    private ImageView back, search, ivDeleteText;// 返回
    private TextView request;
    private RelativeLayout noinfo;//没有数据时显示的文字
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.achieceve_manage_activety);
        bc.setListSize("0");
        listView = (ListView) findViewById(R.id.list);
        noinfo = (RelativeLayout) findViewById(R.id.noinfo);
        text = (TextView) findViewById(R.id.text);
        footer = LayoutInflater.from(AchieveManageActivity.this).inflate(R.layout.loadmore, null);
        addData();// 添加数据
        initView();// 初始化
        listView.setOnScrollListener(this);
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        editText = (EditText) findViewById(R.id.searchtext);
        request = (TextView) findViewById(R.id.request);
        ivDeleteText = (ImageView) findViewById(R.id.ivDeleteText);

        ivDeleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                editText.setText("");
            }
        });
        /*search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content=editText.getText().toString();
                getsearchList();
            }
        });*/
        editText.addTextChangedListener(new TextWatcher() {
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
                /**这是文本框改变之后 会执行的动作
                 * 因为我们要做的就是，在文本框改变的同时，我们的listview的数据也进行相应的变动，并且如一的显示在界面上。
                 * 所以这里我们就需要加上数据的修改的动作了。
                 */
                if (s.length() == 0) {
                    request.setText("取消");
                    request.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addData();
                        }
                    });
                    ivDeleteText.setVisibility(View.GONE);//当文本框为空时，则叉叉消失
                } else {
                    request.setText("搜索");
                    request.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            content = editText.getText().toString();
                            pageNumbers = 1;
                            getsearchList();
                        }
                    });
                    ivDeleteText.setVisibility(View.VISIBLE);//当文本框不为空时，出现叉叉
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AchieveManageActivity.this, AchieveDetail.class);
                BusinessContant bc = new BusinessContant();
                bc.setDoc_id(adapterlist.get(position).getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onLoad() {
        /*//模拟数据加载延时2秒,增强用户体验,当然开发中不需要使用handler来进行延时操作
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initLoadData(); //加载更多数据
                mMyadapter.notifyDataSetChanged(); //通知适配器更新
                mList.setlastitemhide();   //加载完数据后设置footerview隐藏
            }
        },2000);*/
    }

    private void addData() {
        BusinessContant bc = new BusinessContant();
        String key = bc.getCONFIRM_ID();
        bc.setSubmData(null);
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
        inQueryMsg.setQueryName("getAchieveList");
        HashMap<String, String> map = new HashMap<>();
        map.put("projectName", "收文查看");
        map.put("pageNumber", String.valueOf(pageNumber));
        map.put("content", content);
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

                    PageAchieve page = JSON.parseObject(response.toString(), PageAchieve.class);
                    list = page.getList();
                    // System.out.println("list:" + list);
                    if (list.size() > 0) {
                        pageNumber++;
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setStatus(1);
                            adapterlist.add(list.get(i));
                        }
                        //初始状态为隐藏
                        if (pageNumber == 2) {//第一次请求的数据
                            adapter = new DocManageAdapter(adapterlist, AchieveManageActivity.this);
                            listView.setAdapter(adapter);
                        } else if (pageNumber > 2) {

                            myhandler.post(eChanged);
                        }
                    } else {
                        if (pageNumber == 1) {
                            adapter = new DocManageAdapter(adapterlist, AchieveManageActivity.this);
                            listView.setAdapter(adapter);
                            text.setText("没有阅知文件！");
                            noinfo.setVisibility(View.VISIBLE);
                        } else if (pageNumber > 1) {
                            Toast.makeText(AchieveManageActivity.this, "没有更多数据！", Toast.LENGTH_SHORT).show();
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

    public void getsearchList() {
        BusinessContant bc = new BusinessContant();
        String key = bc.getCONFIRM_ID();
        bc.setSubmData(null);
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
        inQueryMsg.setQueryName("getAchieveList");
        HashMap<String, String> map = new HashMap<>();
        map.put("projectName", "收文查看");
        map.put("pageNumber", String.valueOf(pageNumbers));
        map.put("content", content);
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
                    PageAchieve page = JSON.parseObject(response.toString(), PageAchieve.class);
                    list = page.getList();
                    System.out.println("list:" + list);
                    if (pageNumbers == 1) {
                        adapterlist.clear();
                    }
                    if (list.size() > 0) {
                        pageNumbers++;
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setStatus(1);
                            adapterlist.add(list.get(i));
                        }
                        //初始状态为隐藏
                        if (pageNumbers == 2) {
                            adapter = new DocManageAdapter(adapterlist, AchieveManageActivity.this);
                            listView.setAdapter(adapter);
                        } else if (pageNumbers > 2) {

                            myhandler.post(eChanged);
                        }
                    } else {
                        if (pageNumbers == 1) {
                            adapter = new DocManageAdapter(adapterlist, AchieveManageActivity.this);
                            listView.setAdapter(adapter);
                            text.setText("没有阅知文件！");
                            noinfo.setVisibility(View.VISIBLE);
                        } else if (pageNumbers > 1) {
                            Toast.makeText(AchieveManageActivity.this, "没有更多数据！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);

            }
        });
        requestQueue.add(jsonRequest);
    }

    Runnable eChanged = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            adapter.notifyDataSetChanged();
        }
    };


    /**
     * 滑动状态变化
     *
     * @param view
     * @param totalItemCount 1 SCROLL_STATE_TOUCH_SCROLL是拖动  2 SCROLL_STATE_FLING是惯性滑动 0SCROLL_STATE_IDLE是停止 , 只有当在不同状态间切换的时候才会执行
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // 计算最后可见条目的索引
        lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 滑到底部后自动加载，判断listview已经停止滚动并且最后可视的条目等于adapter的条目并且条目不低于6条
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastVisibleIndex > 6
            && lastVisibleIndex == adapter.getCount()) {
            footer.setVisibility(View.VISIBLE);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //开始加载更多数据
                    if (pageNumbers > 1) {
                        getsearchList();
                    } else {
                        addData();
                    }
                    loadComplete();
                }
            }, 2000);
        }
    }

    private void loadComplete() {
        footer.setVisibility(View.GONE);
    }

}
