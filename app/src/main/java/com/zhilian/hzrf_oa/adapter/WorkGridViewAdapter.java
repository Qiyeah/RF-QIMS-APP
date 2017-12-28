package com.zhilian.hzrf_oa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.ui.widget.BaseViewHolder;

/**
 * Created by Administrator on 2016/8/13.
 * 工作的九宫格Adapter（适配器）
 */
public class WorkGridViewAdapter extends BaseAdapter {
    private Context mContext;

    public String[] img_text = {"个人日程","记事本","公告通知", "我的邮件"};
    public int[] imgs ={R.drawable.image2,R.drawable.image4, R.drawable.image3,
            R.drawable.image1};

    public WorkGridViewAdapter(Context mContext) {
        super();
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return img_text.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item, parent, false);
        }
        TextView tv = BaseViewHolder.get(convertView, R.id.tv_item);
        ImageView iv = BaseViewHolder.get(convertView, R.id.iv_item);
        iv.setBackgroundResource(imgs[position]);

        tv.setText(img_text[position]);
        return convertView;
    }
}
