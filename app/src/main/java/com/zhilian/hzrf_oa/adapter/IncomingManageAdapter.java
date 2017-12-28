package com.zhilian.hzrf_oa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.entity.DocManage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by YiFan
 * 收文和发文的适配器
 */
public class IncomingManageAdapter extends BaseAdapter {
    private List<DocManage> list = null;
    private Context mContext;
    private LayoutInflater mInflater;

    ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
    ArrayList<String> mListTitle = new ArrayList<String>();
    ArrayList<String> mListDocno = new ArrayList<String>();
    ArrayList<String> mListDate = new ArrayList<String>();
    ArrayList<String> mListUnit = new ArrayList<String>();
    ArrayList<Integer> mListDocid = new ArrayList<Integer>();
    ArrayList<Integer> mListPid = new ArrayList<Integer>();

    public IncomingManageAdapter(List<DocManage> list, Context context){
        this.list = list;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public IncomingManageAdapter(ArrayList<Map<String, Object>> mData, Context context){
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
            convertView = mInflater.inflate(R.layout.receive_manage_item, null);
        }
        // 文号
        TextView title_number = (TextView) convertView.findViewById(R.id.title_number);
        //title_number.setText(list.get(position).getTitleNumber());
        if (mData.get(position).get("docno")!=null){
            title_number.setText(mData.get(position).get("docno").toString());
        }

        // 来文标题
        TextView title = (TextView) convertView.findViewById(R.id.title);
        //title.setText(list.get(position).getTitle());
        title.setText(mData.get(position).get("title").toString());
        // 来文单位
        TextView from_unit = (TextView) convertView.findViewById(R.id.from_unit);
        //from_unit.setText(list.get(position).getFromUnit());
        from_unit.setText(mData.get(position).get("unit").toString());
        // 收文时间
        TextView date = (TextView) convertView.findViewById(R.id.date);
        //date.setText(list.get(position).getDate());
        date.setText(mData.get(position).get("date").toString());

        return convertView;
    }
}
