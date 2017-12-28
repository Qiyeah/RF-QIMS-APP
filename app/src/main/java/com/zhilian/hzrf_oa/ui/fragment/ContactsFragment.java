package com.zhilian.hzrf_oa.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.ui.activity.AddressListActivity;
import com.zhilian.hzrf_oa.ui.activity.MainActivity;

/**
 * 联系人
 */
public class ContactsFragment extends Fragment implements View.OnClickListener{
    private MainActivity activity;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static ContactsFragment newInstance(String param1, String param2) {
        ContactsFragment fragment = new ContactsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ContactsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.friends_layout, container, false);
        activity = (MainActivity) getActivity();

        initView(v);// 初始化控件

        return v;
    }

    // 初始化控件
    private void initView(View v){
        LinearLayout address_list = (LinearLayout) v.findViewById(R.id.friends_address_list);// 通讯录
        LinearLayout friends_my_group = (LinearLayout) v.findViewById(R.id.friends_my_group);// 我的群组
        LinearLayout friends_create_group = (LinearLayout) v.findViewById(R.id.friends_create_group);// 我的群组

        address_list.setOnClickListener(this);
        friends_my_group.setOnClickListener(this);
        friends_create_group.setOnClickListener(this);
    }

    /**
     * 事件监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.friends_address_list:// 通讯录
                Intent intent = new Intent(activity,AddressListActivity.class);
                startActivity(intent);
                break;
            case R.id.friends_my_group:// 我的群组
                Toast.makeText(activity,"提示：暂未开通",Toast.LENGTH_SHORT).show();
                break;
            case R.id.friends_create_group:// 创建群组
                Toast.makeText(activity,"提示：暂未开通",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
