package com.zhilian.hzrf_oa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.json.T_Achieve;

import java.util.List;

/**
 * 收文和发文归档的适配器
 */
public class DocCabAdapter extends BaseAdapter {
    private List<T_Achieve> list = null;
    private Context mContext;
    private LayoutInflater mInflater;

    public DocCabAdapter(List<T_Achieve> list, Context context){
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
            convertView = mInflater.inflate(R.layout.doc_achieve_list, null);
        }
        // 文号
       /*if (bc.getSubmData()!=null){
           mData=bc.getSubmData();
       }else {
           //mData;
       }*/
        TextView docno = (TextView) convertView.findViewById(R.id.docno);
        //docno.setText(list.get(position).getDocno());
        docno.setText(list.get(position).getDocno());
        // 来文标题
        TextView title = (TextView) convertView.findViewById(R.id.title);
        //title.setText(list.get(position).getTitle());
        title.setText(list.get(position).getTitle());
        // 来文单位
        TextView from_unit = (TextView) convertView.findViewById(R.id.from_unit);
        //from_unit.setText(list.get(position).getUnit());
        from_unit.setText(list.get(position).getUnit());
        // 收文时间
        TextView date = (TextView) convertView.findViewById(R.id.date);
        //date.setText(list.get(position).getReceivedate());
        date.setText(list.get(position).getReceivedate());
        ImageView status=(ImageView)convertView.findViewById(R.id.news_icon);
        int status1=list.get(position).getStatus();
        if (status1==0){
            status.setVisibility(View.VISIBLE);
            status.setImageResource(R.drawable.icon_news);
        }else{
            status.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }




}
