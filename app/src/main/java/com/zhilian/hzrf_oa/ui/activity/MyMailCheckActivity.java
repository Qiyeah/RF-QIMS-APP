package com.zhilian.hzrf_oa.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
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
import com.zhilian.hzrf_oa.adapter.TextAdapter;
import com.zhilian.hzrf_oa.common.BusinessContant;
import com.zhilian.hzrf_oa.json.MailDetail;
import com.zhilian.hzrf_oa.json.T_FJList;
import com.zhilian.hzrf_oa.util.OpenFileIntent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhilian.api.InDeleteMsg;
import com.zhilian.api.InQueryMsg;
import com.zhilian.api.InSaveMsg;
import com.zhilian.api.JsonStringRequest;
import com.zhilian.api.JsonUtil;
import com.zhilian.api.RequestUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/12/24.
 * 查看邮件详情
 */
public class MyMailCheckActivity extends Activity {
    private Dialog alertDialog;
    private List<MailDetail> model;
    private TextAdapter fjAdapter;
    private ArrayList<T_FJList> lists = new ArrayList<T_FJList>();
    private final static String ALBUM_PATH = "/mnt/sdcard/mypic_data/";
    BusinessContant bc=new BusinessContant();
    int id;// 邮件id
    String isBox;// 根据传来值判断在什么栏目（收件箱、发件箱、草稿箱、垃圾箱）
    int m_ClickNum = 0; // 全天事件框点击数
    int oboxId;// 删除之后用于恢复的标志
    int boxId;// 处于什么箱的标志
    TextView hide;// 详情/隐藏
    RelativeLayout details;// 详情/隐藏（框）


    LinearLayout bottom;// 收件箱的底部栏
    TextView reply;// 回复
    TextView delete;// 删除
    TextView transpond;// 转发
    TextView compile;// 编辑
    TextView delete_all;// 彻底删除
    TextView recover;// 恢复

    TextView title;// 标题
    TextView fromname;// 发件人
    TextView hide_fromname;// 发件人（隐藏）
    EditText receiver;// 收件人
    EditText copyer;// 抄送人
    TextView sendTime;// 时间
    ListView fjListView;// 附件列表
    TextView content;// 正文

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mail_details_layout);


        id = getIntent().getExtras().getInt("id");// 拿到传来的事件id
        isBox = getIntent().getExtras().getString("isBox");

        System.out.println("拿到传来的id---》" + id);

        initView();// 初始化控件
        getMailDetail(id);// 拿到邮件详情的数据
    }

    private void initView() {
        ImageView back = (ImageView) findViewById(R.id.back);// 返回
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        delete = (TextView) findViewById(R.id.delete);// 删除
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(MyMailCheckActivity.this)
                        .setTitle("提示")
                        .setMessage("确定删除邮件？")
                        //.setIcon(R.drawable.lianxiren)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        savedata(id, boxId);
                                        finish();
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        alertDialog.cancel();
                                    }
                                }).create();
                alertDialog.show();
            }
        });
        transpond = (TextView) findViewById(R.id.transpond);// 转发
        reply = (TextView) findViewById(R.id.reply);// 回复
        TextView close = (TextView) findViewById(R.id.close);// 关闭
        compile = (TextView) findViewById(R.id.compile);// 编辑
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        hide_fromname = (TextView) findViewById(R.id.hide_fromname);// 发件人
        hide = (TextView) findViewById(R.id.hide);// 详情/隐藏
        details = (RelativeLayout) findViewById(R.id.details);// 详情/隐藏（框）
        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyMailCheckActivity.this,WriteLetterActivity.class);
                intent.putExtra("type","reply");
                startActivity(intent);
            }
        });
        transpond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyMailCheckActivity.this,WriteLetterActivity.class);
                intent.putExtra("type","transpond");
                startActivity(intent);
            }
        });
        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m_ClickNum % 2 == 0) {
                    details.setVisibility(View.VISIBLE);
                    hide_fromname.setVisibility(View.GONE);
                    hide.setText("隐藏");
                } else {
                    details.setVisibility(View.GONE);
                    hide_fromname.setVisibility(View.VISIBLE);
                    hide.setText("详情");
                }
                m_ClickNum++;
            }
        });



        /***************************************************************************/

        bottom = (LinearLayout) findViewById(R.id.bottom);// 收件箱的底部栏

        delete_all = (TextView) findViewById(R.id.delete_all);// 彻底删除
        delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(MyMailCheckActivity.this)
                        .setTitle("提示")
                        .setMessage("确定彻底删除邮件？")
                        //.setIcon(R.drawable.lianxiren)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        deleteData(id);
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        alertDialog.cancel();
                                    }
                                }).create();
                alertDialog.show();
            }
        });
        recover = (TextView) findViewById(R.id.recover);// 恢复
        recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recoverdata(id, oboxId);
                finish();
            }
        });

        /**********************************************************************************/

        title = (TextView) findViewById(R.id.title);// 标题
        fromname = (TextView) findViewById(R.id.fromname);// 发件人
        receiver = (EditText) findViewById(R.id.receiver);// 收件人
        copyer = (EditText) findViewById(R.id.copyer);// 抄送人
        sendTime = (TextView) findViewById(R.id.sendTime);// 时间
        fjListView = (ListView) findViewById(R.id.fj_list);// 附件列表
        content = (TextView) findViewById(R.id.content);// 正文

        whereBox(isBox);


    }

    // 判断当前在哪个箱里
    private void whereBox(String isBox) {
        if (isBox.equals("getRubbishList")) {// 垃圾箱
            delete.setVisibility(View.GONE);
            transpond.setVisibility(View.GONE);
            reply.setVisibility(View.GONE);
            compile.setVisibility(View.GONE);
        } else if (isBox.equals("getSendList")) {// 发件箱
            delete_all.setVisibility(View.GONE);
            recover.setVisibility(View.GONE);
            reply.setVisibility(View.GONE);
            compile.setVisibility(View.GONE);
        } else if (isBox.equals("getDraftList")) {// 草稿箱
            delete_all.setVisibility(View.GONE);
            recover.setVisibility(View.GONE);
            transpond.setVisibility(View.GONE);
            reply.setVisibility(View.GONE);
        } else {
            delete_all.setVisibility(View.GONE);
            recover.setVisibility(View.GONE);
            compile.setVisibility(View.GONE);
        }
    }

    // 拿到邮件详情的数据
    private void getMailDetail(int id) {
        final BusinessContant bc = new BusinessContant();
        String key = bc.getCONFIRM_ID();
        String url = bc.URL;
        InQueryMsg inQueryMsg = new InQueryMsg(1348831860, "query", key);
        inQueryMsg.setQueryName("MailDetail");
        HashMap<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(id));
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
                            MailDetail model = objectMapper.readValue(response.toString(), MailDetail.class);
                            System.out.println("model="+model.getFromId());
                            bc.setModel(model);

                            JSONObject dataJson = new JSONObject(response.toString());
                            title.setText(dataJson.getString("title"));// 标题
                            fromname.setText(dataJson.getString("fromname"));// 发件人

                            hide_fromname.setText(dataJson.getString("fromname"));// 发件人（隐藏）
                            sendTime.setText(dataJson.getString("sendTime"));// 时间
                            receiver.setText(dataJson.getString("receiver"));// 收件人
                            if(dataJson.getString("copyer") == null){
                                copyer.setText(dataJson.getString("copyer"));// 抄送人
                            }else{
                                copyer.setText("");
                            }
                            content.setText(dataJson.getString("content"));// 正文
                            oboxId = dataJson.getInt("oboxId");
                            boxId = dataJson.getInt("boxId");
                            JSONArray fjids = dataJson.getJSONArray("fjid");   //附件列表
                            List<T_FJList> list = JsonUtil.getFJList(fjids.toString());
                            for (int i = 0; i < list.size(); i++) {
                                String fjname = list.get(i).getName();
                                int id = list.get(i).getId();
                                String url = list.get(i).getUrl();
                                String size=list.get(i).getSize();
                                lists.add(new T_FJList(id, fjname, url,size));
                            }

                            fjAdapter = new TextAdapter(lists, getApplicationContext());
                            fjListView.setAdapter(fjAdapter);
                            if (fjids.length() > 0) {
                                setListViewHeightBasedOnChildren(fjListView);
                            }
                            /*fromname.setText(model.getFromname());// 发件人
							title.setText(model.getTitle());// 标题
							hide_fromname.setText(model.getFromname());// 发件人（隐藏）
							sendTime.setText(model.getSendTime());// 时间
							receiver.setText(model.getReceiver());// 收件人
							copyer.setText(model.getCopyer());// 抄送人
							content.setText(model.getContent());// 正文
							oboxId = model.getOboxId();
							boxId = model.getBoxId();*/
                            fjListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                                    final String name = lists.get(position).getName();
                                    String url = lists.get(position).getUrl();
                                    final String targeturl = bc.DOWNLOADURL + name;

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            URL url;
                                            try {
                                                url = new URL(targeturl);
                                                InputStream is = null;
                                                try {
                                                    is = url.openStream();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                    Looper.prepare();
                                                    Toast.makeText(MyMailCheckActivity.this, "没有找到文件！", Toast.LENGTH_SHORT).show();
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
                                                String path = ALBUM_PATH + name;

                                                File file = new File(ALBUM_PATH);
                                                if (!file.exists()) {
                                                    file.mkdirs();
                                                }
                                                FileOutputStream fos = new FileOutputStream(path);
                                                fos.write(baos.toByteArray());
                                                fos.close();
                                                Intent it = new Intent();
                                                if (path.substring(path.length() - 4).equals(".pdf")) {
                                                    it = OpenFileIntent.getPdfFileIntent(path);
                                                } else if (path.substring(path.length() - 4).equals("xlsl") || path.substring(path.length() - 4).equals(".doc")) {
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

                            });



							/*fj_list.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									final String[] names = model.getUrl().split("/");// 拿到传过来的url文件名
									final String name = names[names.length - 1];// 再将url文件名截取出来

									final String targeturl = bc.DOWNLOADURL + name;// 下载的url地址

									new Thread(new Runnable() {
										@Override
										public void run() {
											URL url;
											try {
												url = new URL(targeturl);
												InputStream is = url.openStream();
												byte[] arr = new byte[1];
												ByteArrayOutputStream baos = new ByteArrayOutputStream();
												BufferedOutputStream bos = new BufferedOutputStream(baos);
												int n = is.read(arr);
												while (n > 0) {
													bos.write(arr);
													n = is.read(arr);
												}
												bos.close();
												String path = ALBUM_PATH + name;// 手机缓存的地址

												File file = new File(ALBUM_PATH);
												if (!file.exists()) {
													file.mkdir();
												}
												FileOutputStream fos = new FileOutputStream(path);
												fos.write(baos.toByteArray());
												fos.close();
												Intent it = new Intent();
												it = OpenFileIntent.getExcelFileIntent(path);
												startActivity(it);
												is.close();
											} catch (MalformedURLException e) {
												e.printStackTrace();
											} catch (IOException e) {
												e.printStackTrace();
											}
										}
									}).start();
								}*/


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                Toast.makeText(MyMailCheckActivity.this, "出错了!", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(jsonRequest);
    }

    // 删除（实质：更改状态，放入垃圾箱）
    private void savedata(int id, int boxId) {
        BusinessContant bc = new BusinessContant();
        String key = bc.getCONFIRM_ID();
        String url = bc.URL;
        InSaveMsg inSaveMsg = new InSaveMsg(1348831860, "save", key);
        inSaveMsg.setModelName("t_Mail");
        HashMap<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(id));
        map.put("boxId", String.valueOf(4));
        map.put("oboxId", String.valueOf(boxId));

        inSaveMsg.setModelProperty(map);
        String postData = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            postData = mapper.writeValueAsString(inSaveMsg);
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

                        Toast.makeText(MyMailCheckActivity.this, response.toString(), Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                Toast.makeText(MyMailCheckActivity.this, "出错了!", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(jsonRequest);
    }

    // 恢复（实质：更改状态，改为删除前的状态）
    private void recoverdata(int id, int oboxId) {
        BusinessContant bc = new BusinessContant();
        String key = bc.getCONFIRM_ID();
        String url = bc.URL;
        InSaveMsg inSaveMsg = new InSaveMsg(1348831860, "save", key);
        inSaveMsg.setModelName("t_Mail");
        HashMap<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(id));
        map.put("boxId", String.valueOf(oboxId));

        inSaveMsg.setModelProperty(map);
        String postData = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            postData = mapper.writeValueAsString(inSaveMsg);
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

                        Toast.makeText(MyMailCheckActivity.this, response.toString(), Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                Toast.makeText(MyMailCheckActivity.this, "出错了!", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(jsonRequest);
    }

    // 彻底删除
    private void deleteData(int id) {
        BusinessContant bc = new BusinessContant();
        String key = bc.getCONFIRM_ID();
        String url = bc.URL;
        InDeleteMsg inDeleteMsg = new InDeleteMsg(1348831860, "delete", key);
        inDeleteMsg.setModelName("t_Mail");

        inDeleteMsg.setEntityId(String.valueOf(id));
        String postData = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            postData = mapper.writeValueAsString(inDeleteMsg);
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

                        Toast.makeText(MyMailCheckActivity.this, response.toString(), Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                Toast.makeText(MyMailCheckActivity.this, "出错了!", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(jsonRequest);
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {//计算listView所有子item的高

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
