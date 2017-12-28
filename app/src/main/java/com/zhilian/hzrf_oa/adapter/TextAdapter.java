package com.zhilian.hzrf_oa.adapter;

import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.json.T_FJList;

import java.util.List;

/**
 * Created by YiFan
 * 记事本的适配器
 */
public class TextAdapter extends BaseAdapter {
    private List<T_FJList> list = null;
    private Context mContext;
    private LayoutInflater mInflater;

    public TextAdapter(List<T_FJList> list, Context context){
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
            convertView = mInflater.inflate(R.layout.text_item_layout, null);
        }
        // 标题
        TextView title = (TextView) convertView.findViewById(R.id.fjname);
        title.setText(list.get(position).getName()+"("+list.get(position).getSize()+")");
        TextPaint tp = title.getPaint();
        tp.setFakeBoldText(true);

        return convertView;
    }


}
