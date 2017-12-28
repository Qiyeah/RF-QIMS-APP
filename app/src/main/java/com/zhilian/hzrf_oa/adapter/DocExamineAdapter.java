package com.zhilian.hzrf_oa.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.json.T_Examine;
import com.zhilian.hzrf_oa.json.T_Receive;

import java.util.ArrayList;
import java.util.List;

/**
 * 初审分办的适配器
 */
public class DocExamineAdapter extends BaseAdapter {

    private List<T_Examine> list = null;
    private Context mContext;
    private LayoutInflater mInflater;

    public DocExamineAdapter(List<T_Examine> list, Context context){
        this.list = list;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        Log.e("Response", "getCount: "+list.size());
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
            convertView = mInflater.inflate(R.layout.doc_item, null);
        }

        ImageView imageView=(ImageView)convertView.findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.todo);
        //T obj = list.get(position);
        T_Examine obj = list.get(position);
        // 文号
        TextView title_number = (TextView) convertView.findViewById(R.id.title_number);
        if (obj.getDocno() != null) {
            title_number.setText(obj.getDocno().toString());
        }
        // 来文标题
        TextView title = (TextView) convertView.findViewById(R.id.title);
        title.setText(obj.getTitle());
        // 来文单位
        TextView from_unit = (TextView) convertView.findViewById(R.id.from_unit);
        from_unit.setText(obj.getUnit());
        // 收文时间
        TextView date = (TextView) convertView.findViewById(R.id.date);
        date.setText(obj.getReceivedate().substring(0, 10));
        return convertView;
    }
}
