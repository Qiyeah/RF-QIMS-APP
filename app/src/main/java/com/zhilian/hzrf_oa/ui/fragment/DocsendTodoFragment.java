package com.zhilian.hzrf_oa.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.adapter.DocAdapter;
import com.zhilian.hzrf_oa.common.BusinessContant;
import com.zhilian.hzrf_oa.json.PageReceive;
import com.zhilian.hzrf_oa.json.T_Receive;
import com.zhilian.hzrf_oa.net_exception.ITimeOutException;
import com.zhilian.hzrf_oa.net_exception.TimeOutException;
import com.zhilian.hzrf_oa.ui.activity.DocsendDetail;
import com.zhilian.hzrf_oa.ui.activity.DocsendManageActivity;
import com.zhilian.hzrf_oa.ui.widget.CustomListFragment;
import com.zhilian.hzrf_oa.ui.widget.CustomListView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhilian.api.InQueryMsg;
import com.zhilian.api.JsonStringRequest;
import com.zhilian.api.ParaMap;
import com.zhilian.api.RequestUtil;
import com.zhilian.api.Sign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-1-20.
 */
public class DocsendTodoFragment extends CustomListFragment implements CustomListView.OnPullListener {

    DocsendManageActivity activity;
    BusinessContant bc = new BusinessContant();
    private ImageView search;
    private PopupWindow popupWindow;
    private EditText editText;
    private ImageView ivDeleteText;
    private TextView request;
    private View view;
    private ListView listView;// 收文待办列表
    private LinearLayout menu;

    private RelativeLayout noinfo;//没有数据时显示的文字

    Handler myhandler=new Handler();
    private DocAdapter adapter;
    private List<T_Receive> list;
    private List<T_Receive> adapterlist=new ArrayList<T_Receive>();

    private boolean isPrepared = false;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    Integer pageNumber=1;//添加数据页码
    Integer pageNumbers=1;//搜索数据页码

    public DocsendTodoFragment() {
        // Required empty public constructor
    }

    public static DocsendTodoFragment newInstance(String param1, String param2) {
        DocsendTodoFragment fragment = new DocsendTodoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


        //addData();// 添加数据（测试数据）
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.receive_cab_list, container, false);
        activity = (DocsendManageActivity) getActivity();// 拿到合同详情的Activity
        isPrepared = true;//初始化时置true
        view = (View) v.findViewById(R.id.view);
        menu=(LinearLayout)activity.findViewById(R.id.menu);
        noinfo=(RelativeLayout)v.findViewById(R.id.noinfo);
        initView(v);

        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {//给listview添加点击事件
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(getActivity(),DocsendDetail.class);
        intent.putExtra("docid", String.valueOf(adapterlist.get(position-1).getDocid()));
        intent.putExtra("isdone","0");//判断是否为已批办文件，1为已批办，0为未批办
        startActivity(intent);
    }

    private void initView(View v) {

    }

    private void addData() {

        String key = bc.getCONFIRM_ID();
        //String url = "http://192.168.9.126:8083/Api";
        String url=bc.URL;
        String token = "1lj4hbato30kl1ppytwa1ueqdn";
        final String encodingAesKey = "InVjlo7czsOWrCSmTPgEUXBzlFnmqpNMQU3ZfilULHyHZiRjVUhxxWpexhYH6f4i";
        Map<String,String> ret = Sign.sign(url, token, encodingAesKey);
        String signature = ret.get("signature");
        String nonceStr = ret.get("nonceStr");
        String timestamp = ret.get("timestamp");
        Map<String, String> queryParas = ParaMap.create("accessToken", token)
                .put("nonce", nonceStr)
                .put("timestamp", timestamp)
                .put("signature", signature)
                .getData();
        url = RequestUtil.buildUrlWithQueryString(url,queryParas);
        InQueryMsg inQueryMsg = new InQueryMsg(1348831860,"query",key);
        inQueryMsg.setQueryName("getDocsendTodoList");
        HashMap<String,String> map = new HashMap<>();
        map.put("projectName","收文");
        map.put("pid",bc.getPid());
        map.put("condition","");
        map.put("pageNumber",String.valueOf(pageNumber));
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
                        System.out.println("解密后：" + response.toString());
                        if (!response.equals(bc.ERROR)) {
                            PageReceive page = JSON.parseObject(response.toString(), PageReceive.class);
                            list = page.getList();
                            if (list.size() > 0) {
                                pageNumber++;
                                for (int i = 0; i < list.size(); i++) {
                                    list.get(i).setStatus(0);
                                    adapterlist.add(list.get(i));
                                }
                                if (pageNumber == 2) {//第一次请求的数据
                                    adapter = new DocAdapter(adapterlist, getActivity());
                                    setListAdapter(adapter);
                                } else if (pageNumber > 2) {
                                    onLoadComplete();
                                    myhandler.post(eChanged);
                                }
                            } else {
                                if (pageNumber == 1) {
                                    noinfo.setVisibility(View.VISIBLE);
                                } else if (pageNumber > 1) {
                                    onLoadComplete();
                                    Toast.makeText(getActivity(), "没有更多数据！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }else{
                            new TimeOutException().reLogin(getActivity(), new ITimeOutException.CallBack(){
                                @Override
                                public void onReloginSuccess() {
                                    addData();
                                }
                            });
                            Toast.makeText(getActivity(), bc.ERROR, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                Toast.makeText(getActivity(),"出错了!",Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonRequest);
    }

    public void search(String condition){
        BusinessContant bc = new BusinessContant();
        String key = bc.getCONFIRM_ID();
        //String url = "http://192.168.9.126:8083/Api";
        String url=bc.URL;
        String token = "1lj4hbato30kl1ppytwa1ueqdn";
        final String encodingAesKey = "InVjlo7czsOWrCSmTPgEUXBzlFnmqpNMQU3ZfilULHyHZiRjVUhxxWpexhYH6f4i";
        Map<String,String> ret = Sign.sign(url, token, encodingAesKey);
        String signature = ret.get("signature");
        String nonceStr = ret.get("nonceStr");
        String timestamp = ret.get("timestamp");
        Map<String, String> queryParas = ParaMap.create("accessToken", token)
                .put("nonce", nonceStr)
                .put("timestamp", timestamp)
                .put("signature", signature)
                .getData();
        url = RequestUtil.buildUrlWithQueryString(url,queryParas);
        InQueryMsg inQueryMsg = new InQueryMsg(1348831860,"query",key);
        inQueryMsg.setQueryName("getDocsendTodoList");
        HashMap<String,String> map = new HashMap<>();
        map.put("projectName","收文");
        map.put("pid",bc.getPid());
        map.put("condition",condition);
        map.put("pageNumber",String.valueOf(pageNumbers));
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
                        if (pageNumbers == 1) {
                            adapterlist.clear();
                        }
                        noinfo.setVisibility(View.GONE);
                        try {
                            PageReceive page = JSON.parseObject(response.toString(), PageReceive.class);
                            list = page.getList();
                            if (list.size() > 0) {
                                pageNumbers++;
                                for (int i = 0; i < list.size(); i++) {
                                    list.get(i).setStatus(1);
                                    adapterlist.add(list.get(i));
                                }
                                if (pageNumbers == 2) {
                                    adapter = new DocAdapter(adapterlist, getActivity());
                                    setListAdapter(adapter);
                                } else if (pageNumbers > 2) {
                                    onLoadComplete();
                                    myhandler.post(eChanged);
                                }
                            } else {
                                if (pageNumbers == 1) {
                                    adapter = new DocAdapter(adapterlist, getActivity());
                                    setListAdapter(adapter);
                                } else if (pageNumbers > 1) {
                                    onLoadComplete();
                                    Toast.makeText(getActivity(), "没有更多数据！", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(),"出错了!",Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonRequest);
    }

    Runnable eChanged = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub

            //bc.setSubmData(mData);
            adapter.notifyDataSetChanged();
            //listViewinit();
        }
    };


    private void showPopupWindow() {
        View contentView = activity.getLayoutInflater().inflate(R.layout.search_in_service_layout, null);
        editText = (EditText) contentView.findViewById(R.id.condition);
        ivDeleteText = (ImageView) contentView.findViewById(R.id.ivDeleteText);
        request=(TextView)contentView.findViewById(R.id.request);
        popupWindow = new PopupWindow(contentView);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAsDropDown(menu);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                             @Override
                                             public void onDismiss() {
                                                 view.setVisibility(View.GONE);
                                             }
                                         }

        );
        editText.setFocusable(true);//使弹框获取焦点
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                //这个应该是在改变的时候会做的动作吧，具体还没用到过。
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
                            popupWindow.dismiss();
                        }
                    });
                    ivDeleteText.setVisibility(View.GONE);//当文本框为空时，则叉叉消失
                } else {
                    request.setText("搜索");
                    request.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            search(editText.getText().toString());
                        }
                    });
                    ivDeleteText.setVisibility(View.VISIBLE);//当文本框不为空时，出现叉叉
                }

            }
        });
        ivDeleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });

    }

    //上拉加载
    @Override
    public void onPullUpLoadMore(CustomListView.LoadMode mode, ProgressBar footBar,
                                 TextView loadTextView) {
        new Thread(new Runnable() {
            public void run() {
                if (pageNumbers>1){
                    search(editText.getText().toString());
                }else{
                    addData();
                }

            }
        }).start();
    }

    //下拉刷新
    @Override
    public void onPullDownRefresh(View headView) {

        System.out.print("aa");
        new Thread(new Runnable() {
            public void run() {
                refreshData();
            }
        }).start();
    }

    private void refreshData() {
        try {
            Thread.sleep(1 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        myhandler.post(new Runnable() {
            public void run() {
                onRefrshComplete();
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
// TODO Auto-generated method stub
        setUserVisibleHint(true);
        super.onActivityCreated(savedInstanceState);
        setListViewOnPullListener(this);
        setPullUpLoadMode(CustomListView.LoadMode.AUTO_LOAD_MORE);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isPrepared && isVisibleToUser) {//可见的并且是初始化之后才加载
            search = (ImageView) activity.findViewById(R.id.search);
            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.setVisibility(View.VISIBLE);
                    showPopupWindow();
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("onResume--->");
        adapterlist=new ArrayList<T_Receive>();
        pageNumber=1;
        addData();

    }
}
