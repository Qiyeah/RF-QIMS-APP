package com.zhilian.hzrf_oa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.json.T_Step;

import java.util.List;

/**
 * 流程选择环节的适配器
 */
public class StepAdapter extends BaseAdapter {
    private List<T_Step> list = null;
    private Context mContext;
    private LayoutInflater mInflater;

    public StepAdapter(List<T_Step> list, Context context){
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
            convertView = mInflater.inflate(R.layout.step_item_layout, null);
        }
        // 标题
        //RadioButton radioButton=(RadioButton)convertView.findViewById(R.id.stepid);
       /* CheckBox checkBox=(CheckBox)convertView.findViewById(R.id.stepid);
        TextView stepname = (TextView) convertView.findViewById(R.id.stepname);
        stepname.setText(list.get(position).getName());*/

        // 时间

        return convertView;
    }
}
