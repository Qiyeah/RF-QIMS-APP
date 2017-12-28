package com.zhilian.hzrf_oa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.common.BusinessContant;
import com.zhilian.hzrf_oa.json.T_Achieve;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 收文和发文的适配器
 */
public class DocManageAdapter extends BaseAdapter {
    private List<T_Achieve> list = null;
    private Context mContext;
    private LayoutInflater mInflater;
    BusinessContant bc=new BusinessContant();
    ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
    ArrayList<String> mListTitle = new ArrayList<String>();
    ArrayList<String> mListText = new ArrayList<String>();
    ArrayList<String> mListDate = new ArrayList<String>();
    ArrayList<String> mListUnit = new ArrayList<String>();
    ArrayList<Integer> mListStatus = new ArrayList<Integer>();
    ArrayList<Integer> mListId = new ArrayList<Integer>();

    public DocManageAdapter(List<T_Achieve> list, Context context, ArrayList<Map<String, Object>> mData){
        this.list = list;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.mData=mData;
    }

    public DocManageAdapter(Context context, ArrayList<Map<String, Object>> mData){
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.mData=mData;
    }

    public DocManageAdapter(List<T_Achieve> list, Context context){
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
        docno.setText(list.get(position).getDocno());
        //docno.setText(mData.get(position).get("docno").toString());
        // 来文标题
        TextView title = (TextView) convertView.findViewById(R.id.title);
        title.setText(list.get(position).getTitle());
        //title.setText(mData.get(position).get("title").toString());
        // 来文单位
        TextView from_unit = (TextView) convertView.findViewById(R.id.from_unit);
        from_unit.setText(list.get(position).getUnit());
        //from_unit.setText(mData.get(position).get("unit").toString());
        // 收文时间
        TextView date = (TextView) convertView.findViewById(R.id.date);
        date.setText(list.get(position).getReceivedate());
        //date.setText(mData.get(position).get("receivedate").toString());
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
