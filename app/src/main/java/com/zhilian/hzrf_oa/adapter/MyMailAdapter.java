package com.zhilian.hzrf_oa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.json.ReceiveMailList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 我的邮件的适配器
 */
public class MyMailAdapter extends BaseAdapter {
    private List<ReceiveMailList> list = null;
    private Context mContext;
    private LayoutInflater mInflater;
    ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();

    public MyMailAdapter(List<ReceiveMailList> list, Context context){
        this.list = list;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public MyMailAdapter(ArrayList<Map<String, Object>> mData, Context context){
        this.mData = mData;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.my_mail_item_layout, null);
        }
        // 图标
        ImageView imageView = (ImageView) convertView.findViewById(R.id.img_msg_item);
        imageView.setImageResource(R.drawable.image1);
        // 发件人
        TextView nameMsg = (TextView)convertView.findViewById(R.id.name_msg_item);
        if (mData.get(position).get("username") !=null) {
            nameMsg.setText(mData.get(position).get("username").toString());
        }
        // 邮件标题
        TextView contentMsg = (TextView)convertView.findViewById(R.id.content_msg_item);
        if (mData.get(position).get("title")!=null) {
            contentMsg.setText(mData.get(position).get("title").toString());
        }
        // 发送时间
        TextView timeMsg = (TextView)convertView.findViewById(R.id.time_msg_item);
        if (mData.get(position).get("sendtime")!=null) {
            timeMsg.setText(mData.get(position).get("sendtime").toString());
        }

        return convertView;

    }
}
