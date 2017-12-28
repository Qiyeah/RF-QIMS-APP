package com.zhilian.hzrf_oa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhilian.hzrf_oa.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 选择联系人的适配器
 */
public class SelectPersonAdapter extends BaseAdapter {
    //private List<SelectPerson> list = null;
    ArrayList<HashMap<String, Object>> listData;
    private Context mContext;
    private LayoutInflater mInflater;
    //记录checkbox的状态
    public HashMap<Integer, Boolean> state = new HashMap<Integer, Boolean>();

    public SelectPersonAdapter(Context context, ArrayList<HashMap<String, Object>> listData) {
        this.listData = listData;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.select_person_layout, null);

        // 图标
        ImageView image = (ImageView) convertView.findViewById(R.id.friend_image);
        image.setBackgroundResource((Integer) listData.get(position).get("friend_image"));
        // 名字
        TextView username = (TextView) convertView.findViewById(R.id.friend_username);
        username.setText((String) listData.get(position).get("friend_username"));
        // id
        TextView id = (TextView) convertView.findViewById(R.id.friend_id);
        id.setText((String) listData.get(position).get("friend_id"));

        CheckBox check = (CheckBox) convertView.findViewById(R.id.selected);
        check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    state.put(position, isChecked);
                } else {
                    state.remove(position);
                }
            }
        });
        check.setChecked((state.get(position) == null ? false : true));

        return convertView;
    }
}
