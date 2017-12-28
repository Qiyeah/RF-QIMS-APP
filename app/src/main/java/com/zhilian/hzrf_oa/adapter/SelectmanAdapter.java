package com.zhilian.hzrf_oa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.common.BusinessContant;
import com.zhilian.hzrf_oa.json.T_Selectman;
import com.zhilian.hzrf_oa.util.LogUtil;
import com.zhilian.api.StrKit;

import java.util.HashMap;
import java.util.List;

/**
 * 流程环节选择用户的适配器
 */
public class SelectmanAdapter extends BaseAdapter {
    private List<T_Selectman> list = null;
    private Context mContext;
    private LayoutInflater mInflater;
    private static HashMap<Integer, Boolean> isSelected;
    private String mens, menid, mensid = "";
    BusinessContant bc = new BusinessContant();
    public HashMap<Integer, Boolean> state = new HashMap<Integer, Boolean>();

    private HashMap<String, Integer> menmap = new HashMap<>();

    public SelectmanAdapter(List<T_Selectman> list, Context context) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.step_item_layout, null);
        }

        CheckBox selectman = (CheckBox) convertView.findViewById(R.id.stepid);
        selectman.setText(list.get(position).getName() + " ( " + list.get(position).getPid() + " : " + list.get(position).getD_id() + " )");
        menmap.put(list.get(position).getName() + " ( " + list.get(position).getPid() + " : " + list.get(position).getD_id() + " )", list.get(position).getId());

        selectman.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {//日程指定人员
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                mens = String.valueOf(buttonView.getText());
                menid = String.valueOf(menmap.get(mens));
                if (isChecked) {
                    state.put(position, isChecked);
                    mensid += "," + menid;
                } else {
                    state.remove(position);
                    mensid = arrayDelstr(mensid, menid);
                }

                if (StrKit.notBlank(mensid)) {
                    if (mensid.charAt(0) == ',') {
                        mensid = mensid.substring(1);
                    }
                    bc.setUserid(mensid);
                }
                LogUtil.i("i", mensid);
            }
        });
        selectman.setChecked((state.get(position) == null ? false : true));
        return convertView;
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        SelectmanAdapter.isSelected = isSelected;
    }

    public String arrayDelstr(String s, String s1) {
        String tmpstr = "";
        if (s == null)
            s = "";
        String str[] = s.split(",");
        for (int i = 0; i < str.length; i++) {
            if (!str[i].equals(s1)) {
                tmpstr += "," + str[i];
            }
        }
        // System.out.println("===="+tmpstr);
        return tmpstr;
    }


}
