package com.zhilian.hzrf_oa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.json.SchduleList;

import java.util.List;

/**
 * 事件列表的适配器
 */
public class TestAdapter1 extends BaseAdapter {
    private List<SchduleList> list = null;
    private Context mContext;
    private LayoutInflater mInflater = null;

    public TestAdapter1(Context mContext, List<SchduleList> list) {
        this.mInflater = LayoutInflater.from(mContext);
        this.list = list;
        this.mContext = mContext;
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
            convertView = mInflater.inflate(R.layout.list_item1_layout, null);
        }
        // 日程标题
        TextView title = (TextView) convertView.findViewById(R.id.title);
        title.setText(list.get(position).getTitle());
        // 日程时间
        String wdate = list.get(position).getWdate().substring(11,16);// 开始时间
        //System.out.println("开始时间-->"+wdate);
        String edate = list.get(position).getEdate().substring(11,16);// 结束时间
        //System.out.println("结束时间-->"+edate);
        String event = list.get(position).getEvent();// 全天事件（开始）
        //System.out.println("全天事件（开始）-->"+event);
        String event1 = list.get(position).getEvent1();// 全天事件（结束）
        //System.out.println("全天事件（结束）-->"+event1);

        TextView time = (TextView) convertView.findViewById(R.id.time);
        if(event == null){
            time.setText(wdate+"-"+edate);
        }else{
            String t_event = list.get(position).getEvent().substring(8,10);
            String t_event1 = list.get(position).getEvent1().substring(8,10);
            time.setText(t_event+"-"+t_event1);
        }



        return convertView;
    }


}
