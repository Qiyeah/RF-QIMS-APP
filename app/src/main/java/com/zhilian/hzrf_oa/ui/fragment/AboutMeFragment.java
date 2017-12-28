package com.zhilian.hzrf_oa.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.ui.activity.AboutUsActivity;
import com.zhilian.hzrf_oa.ui.activity.LoginActivity;
import com.zhilian.hzrf_oa.ui.activity.MainActivity;

/**
 * 我的
 */
public class AboutMeFragment extends Fragment implements View.OnClickListener{
    private MainActivity activity;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static AboutMeFragment newInstance(String param1, String param2) {
        AboutMeFragment fragment = new AboutMeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AboutMeFragment() {
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
        View v = inflater.inflate(R.layout.my_layout, container, false);
        activity = (MainActivity) getActivity();
        String uname = activity.getIntent().getExtras().getString("uname");

        initView(v,uname);

        return v;
    }

    private void initView(View v,String uname) {
        RelativeLayout relativeLayout4 = (RelativeLayout) v.findViewById(R.id.relativeLayout4);
        RelativeLayout relativeLayout5 = (RelativeLayout) v.findViewById(R.id.relativeLayout5);
        RelativeLayout relativeLayout6 = (RelativeLayout) v.findViewById(R.id.relativeLayout6);
        RelativeLayout relativeLayout7 = (RelativeLayout) v.findViewById(R.id.relativeLayout7);
        RelativeLayout relativeLayout8 = (RelativeLayout) v.findViewById(R.id.relativeLayout8);
        RelativeLayout relativeLayout9 = (RelativeLayout) v.findViewById(R.id.relativeLayout9);
        RelativeLayout relativeLayout10 = (RelativeLayout) v.findViewById(R.id.relativeLayout10);
        TextView user_name = (TextView) v.findViewById(R.id.user_name);
        user_name.setText(uname);

        relativeLayout4.setOnClickListener(this);
        relativeLayout5.setOnClickListener(this);
        relativeLayout6.setOnClickListener(this);
        relativeLayout7.setOnClickListener(this);
        relativeLayout8.setOnClickListener(this);
        relativeLayout9.setOnClickListener(this);
        relativeLayout10.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.relativeLayout4:// 账号与安全
                Toast.makeText(activity,"提示：暂未开通",Toast.LENGTH_SHORT).show();
                break;
            case R.id.relativeLayout5:// 新消息通知
                Toast.makeText(activity,"提示：暂未开通",Toast.LENGTH_SHORT).show();
                break;
            case R.id.relativeLayout6:// 勿扰模式
                Toast.makeText(activity,"提示：暂未开通",Toast.LENGTH_SHORT).show();
                break;
            case R.id.relativeLayout7:// 隐私
                Toast.makeText(activity,"提示：暂未开通",Toast.LENGTH_SHORT).show();
                break;
            case R.id.relativeLayout8:// 通用
                Toast.makeText(activity,"提示：暂未开通",Toast.LENGTH_SHORT).show();
                break;
            case R.id.relativeLayout9:// 关于本软件
                startActivity(new Intent(activity, AboutUsActivity.class));
                break;
            case R.id.relativeLayout10:// 退出登录
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                builder.setMessage("确认退出系统？");
                builder.setTitle("提示");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        arg0.dismiss();
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(activity,LoginActivity.class);
                        startActivity(intent);
                        //退出时清除密码缓存
                        SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("password","");
                      //  editor.clear();
                        editor.commit();

                        Log.e("Response", "onClick: "+sp.getString("password",""));

                        activity.finish();
                    }
                });
                builder.create().show();
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

    public SharedPreferences getSharedPreferences(String name, int mode) {
        return getActivity().getSharedPreferences(name, mode);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
