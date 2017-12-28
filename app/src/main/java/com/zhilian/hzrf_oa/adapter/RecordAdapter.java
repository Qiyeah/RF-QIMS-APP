package com.zhilian.hzrf_oa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.json.T_Record;

import java.util.List;

/**
 * 流转记录的适配器
 */
public class RecordAdapter extends BaseAdapter {
    private List<T_Record> list = null;
    private Context mContext;
    private LayoutInflater mInflater;

    public RecordAdapter(List<T_Record> list, Context context){
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
            convertView = mInflater.inflate(R.layout.record_item_layout, null);
        }
        // 意见
        TextView opinion = (TextView) convertView.findViewById(R.id.opinion);
        opinion.setText(list.get(position).getOpinion());
        TextView name=(TextView) convertView.findViewById(R.id.name);
        name.setText(list.get(position).getItemid1());
        TextView username1=(TextView) convertView.findViewById(R.id.username1);
        username1.setText(list.get(position).getName());
        TextView operation=(TextView) convertView.findViewById(R.id.operation);
        operation.setText(list.get(position).getOperation());
        TextView starttime=(TextView) convertView.findViewById(R.id.starttime);
        starttime.setText(list.get(position).getBegintime());
        TextView endtime=(TextView) convertView.findViewById(R.id.endtime);
        endtime.setText(list.get(position).getEndtime());
        TextView nextusername=(TextView) convertView.findViewById(R.id.nextusername);
        nextusername.setText(list.get(position).getUser2());


        return convertView;
    }
}
