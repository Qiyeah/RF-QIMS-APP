package com.zhilian.hzrf_oa.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.json.T_Announce;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 公告通知的适配器
 */
public class NoticeAnnouncementAdapter extends BaseAdapter {
    private List<T_Announce> list = null;
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Map<String, Object>> mData;

    public NoticeAnnouncementAdapter(List<T_Announce> list, Context context) {
        this.list = list;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public NoticeAnnouncementAdapter(ArrayList<Map<String, Object>> mData, Context context) {
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
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.notice_announcement_item_layout, null);
        }
        // 公告类型
        //int atype = list.get(position).getAtype();

        // 标题
        TextView title = (TextView) convertView.findViewById(R.id.title);
        title.setText(mData.get(position).get("title").toString());
        if ((int) mData.get(position).get("atype") == 1) {
            title.setTextColor(Color.parseColor("#FF0000"));
        } else {
            title.setTextColor(Color.parseColor("#000000"));
        }

        // 发布人
        TextView issuer = (TextView) convertView.findViewById(R.id.issuer);
        issuer.setText(mData.get(position).get("name").toString());
        // 时间
        TextView time = (TextView) convertView.findViewById(R.id.time);
        String sendtime = mData.get(position).get("time").toString();
        String senddate = sendtime.substring(0, 10);
        time.setText(senddate);

        return convertView;
    }

    /*@Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.notice_announcement_item_layout, null);
        }
        // 公告类型
        //int atype = list.get(position).getAtype();

        // 标题
        TextView title = (TextView)convertView.findViewById(R.id.title);
        title.setText(list.get(position).getTitle());
        if(list.get(position).getAtype() == 1){
            title.setTextColor(Color.parseColor("#FF0000"));
        }else{
            title.setTextColor(Color.parseColor("#000000"));
        }

        // 发布人
        TextView issuer = (TextView)convertView.findViewById(R.id.issuer);
        issuer.setText(list.get(position).getName()+"");
        // 时间
        TextView time = (TextView)convertView.findViewById(R.id.time);
        String sendtime=list.get(position).getSendtime();
        String senddate=sendtime.substring(0,10);
        time.setText(senddate);

        return convertView;
    }*/
}
