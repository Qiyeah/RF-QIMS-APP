package com.zhilian.hzrf_oa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.entity.MessageBean;

import java.util.List;

/**
 * 消息的适配器
 */
public class MessageAdapter extends BaseAdapter {
    private List<MessageBean> list = null;
    private Context mContext;
    private LayoutInflater mInflater;

    public MessageAdapter(List<MessageBean> list, Context context){
        this.list = list;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
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
            convertView = mInflater.inflate(R.layout.message_item_layout, null);
        }
        // 头像
        ImageView imageView = (ImageView) convertView.findViewById(R.id.img_msg_item);
        imageView.setImageResource(list.get(position).getPhotoDrawableId());
        // 呢称
        TextView nameMsg = (TextView)convertView.findViewById(R.id.name_msg_item);
        nameMsg.setText(list.get(position).getMessageName());
        // 消息内容
        TextView contentMsg = (TextView)convertView.findViewById(R.id.content_msg_item);
        contentMsg.setText(list.get(position).getMessageContent());
        // 消息时间
        TextView timeMsg = (TextView)convertView.findViewById(R.id.time_msg_item);
        timeMsg.setText(list.get(position).getMessageTime());

        return convertView;
    }
}
