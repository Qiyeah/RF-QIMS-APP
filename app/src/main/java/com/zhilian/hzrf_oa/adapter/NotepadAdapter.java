package com.zhilian.hzrf_oa.adapter;

import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.entity.NotepadBeen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 记事本的适配器
 */
public class NotepadAdapter extends BaseAdapter {
    private List<NotepadBeen> list = null;
    private Context mContext;
    private LayoutInflater mInflater;
    private ViewHolder viewHolder;
    ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
    private static HashMap<Integer, Boolean> isSelectedMap;// 用来控制CheckBox的选中状况
    private static HashMap<Integer, Integer> isvisibleMap;// 用来控制CheckBox的显示状况

    public NotepadAdapter(ArrayList<Map<String, Object>> mData, Context context){
        this.mData = mData;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        isSelectedMap = new HashMap<Integer, Boolean>();
        isvisibleMap = new HashMap<Integer, Integer>();
        initDate();
    }

    public NotepadAdapter(List<NotepadBeen> list, Context context){
        this.list = list;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        isSelectedMap = new HashMap<Integer, Boolean>();
        isvisibleMap = new HashMap<Integer, Integer>();
        initDate();
    }

    @Override
    public int getCount() {
        int number = 0;
        if (mData != null) {
            number = mData.size();
        }
        /*if (list != null) {
            number = list.size();
        }*/

        return number;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    // 初始化isSelectedMap的数据
    private void initDate() {
        for (int i = 0; i < mData.size(); i++) {
            getIsSelectedMap().put(i, false);
            getIsvisibleMap().put(i, CheckBox.INVISIBLE);
        }
    }

    public final class ViewHolder {
        public TextView title;
        public TextView content;
        public TextView date;
        public CheckBox cb;
    }

    public static HashMap<Integer, Boolean> getIsSelectedMap() {
        return isSelectedMap;
    }

    public static HashMap<Integer, Integer> getIsvisibleMap() {
        return isvisibleMap;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*if(convertView == null){
            convertView = mInflater.inflate(R.layout.notepad_item_layout, null);
        }
        // 标题
        TextView title = (TextView) convertView.findViewById(R.id.title);
        title.setText(list.get(position).getTitle());
        TextPaint tp = title.getPaint();
        tp.setFakeBoldText(true);
        // 内容
        TextView content = (TextView) convertView.findViewById(R.id.content);
        content.setText(list.get(position).getContent());
        // 时间
        TextView date = (TextView) convertView.findViewById(R.id.date);
        date.setText(list.get(position).getWdate());
        // 选择框
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.cb);*/
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.notepad_item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.content = (TextView) convertView.findViewById(R.id.content);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.cb = (CheckBox) convertView.findViewById(R.id.cb);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.cb.setChecked(getIsSelectedMap().get(position));
            viewHolder.cb.setVisibility(getIsvisibleMap().get(position));// 这里的报错被我忽略了
        }

        viewHolder.title.setText(mData.get(position).get("title").toString());//使用自定义的数据集加载
        TextPaint tp = viewHolder.title.getPaint();
        tp.setFakeBoldText(true);
        Object content=mData.get(position).get("content");
        if (content!=null){//content在页面上为非必输项，防止转换报空值
            viewHolder.content.setText(content.toString());
        }

        viewHolder.date.setText(mData.get(position).get("date").toString());
        /*viewHolder.title.setText(list.get(position).getTitle());//使用list加载数据
        TextPaint tp = viewHolder.title.getPaint();
        tp.setFakeBoldText(true);
        viewHolder.content.setText(list.get(position).getContent());
        viewHolder.date.setText(list.get(position).getWdate());*/

        return convertView;
    }
}
