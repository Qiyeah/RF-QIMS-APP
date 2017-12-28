package com.zhilian.hzrf_oa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.json.ReceiverJson;

import java.util.List;

public class ReceiverAdapter extends BaseAdapter {
    private List<ReceiverJson> list = null;
    private Context mContext;
    private LayoutInflater mInflater;

    public ReceiverAdapter(List<ReceiverJson> list, Context context){
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
            convertView = mInflater.inflate(R.layout.receiver_item, null);
        }
        // 意见
        TextView opinion = (TextView) convertView.findViewById(R.id.tv_opinion);
        opinion.setText(list.get(position).getOpinion());
        //签收人
        TextView name=(TextView) convertView.findViewById(R.id.tv_name);
        name.setText(list.get(position).getName());
        //签收时间
        TextView time=(TextView) convertView.findViewById(R.id.tv_time);
        time.setText(list.get(position).getReceiveTime());

        return convertView;
    }
}
