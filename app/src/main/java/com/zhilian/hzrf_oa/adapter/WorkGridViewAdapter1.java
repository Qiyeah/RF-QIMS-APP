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
import com.zhilian.hzrf_oa.ui.widget.BaseViewHolder;

/**
 * Created by Administrator on 2016/8/13.
 * 工作的九宫格Adapter（适配器）
 */
public class WorkGridViewAdapter1 extends BaseAdapter {
    private Context mContext;
    //public String[] img_text = {"一般阅知","收文管理","发文管理","档案柜"};
    public String[] img_text = null;
   // public int[] imgs ={R.drawable.image6, R.drawable.image7, R.drawable.image8, R.drawable.image9,R.drawable.image9};
    public int[] imgs =null;
    public WorkGridViewAdapter1(Context mContext) {
        super();
        this.mContext = mContext;
    }
    public WorkGridViewAdapter1(Context mContext,String limit) {
        super();
        this.mContext = mContext;

        switch (limit){
            case "1":
                img_text = new String[]{"一般阅知", "收文管理", "内部发文", "档案柜", "初审分办"};
                imgs = new int[]{R.drawable.image6, R.drawable.image7, R.drawable.image8, R.drawable.image9, R.drawable.image9};
                break;
            case "0":
                img_text = new String[]{"一般阅知", "收文管理", "内部发文", "档案柜"};
                imgs = new int[]{R.drawable.image6, R.drawable.image7, R.drawable.image8, R.drawable.image9};
                break;
        }
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
