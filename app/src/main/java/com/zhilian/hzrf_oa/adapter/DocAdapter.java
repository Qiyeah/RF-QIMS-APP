package com.zhilian.hzrf_oa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.json.T_Receive;

import java.util.List;

/**
 * Created by Administrator on 2017-1-20.
 */
public class DocAdapter extends BaseAdapter {

    private List<T_Receive> list = null;
    private Context mContext;
    private LayoutInflater mInflater;

    public DocAdapter(List<T_Receive> list, Context context){
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
            convertView = mInflater.inflate(R.layout.doc_item, null);
        }

        ImageView imageView=(ImageView)convertView.findViewById(R.id.imageView);
        if (list.get(position).getStatus()==0){
            imageView.setImageResource(R.drawable.todo);
        }else{
            imageView.setImageResource(R.drawable.done);
        }
        // 文号
        TextView title_number = (TextView) convertView.findViewById(R.id.title_number);
        if (list.get(position).getDocno()!=null){
            title_number.setText(list.get(position).getDocno().toString());
        }

        // 来文标题
        TextView title = (TextView) convertView.findViewById(R.id.title);
        title.setText(list.get(position).getDoctitle());
        // 来文单位
        TextView from_unit = (TextView) convertView.findViewById(R.id.from_unit);
        from_unit.setText(list.get(position).getUnit());
        // 收文时间
        TextView date = (TextView) convertView.findViewById(R.id.date);
        date.setText(list.get(position).getStarttime().substring(0,10));
        //环节名称
        TextView active = (TextView) convertView.findViewById(R.id.active);
        active.setText(list.get(position).getActive());


        return convertView;
    }
}
