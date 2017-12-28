package com.zhilian.hzrf_oa.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.adapter.WorkGridViewAdapter;
import com.zhilian.hzrf_oa.adapter.WorkGridViewAdapter1;
import com.zhilian.hzrf_oa.ui.activity.AchieveManageActivity;
import com.zhilian.hzrf_oa.ui.activity.FileCabinetActivity;
import com.zhilian.hzrf_oa.ui.activity.InnerSendManagerActivity;
import com.zhilian.hzrf_oa.ui.activity.MainActivity;
import com.zhilian.hzrf_oa.ui.activity.MyMailActivity;
import com.zhilian.hzrf_oa.ui.activity.NotepadActivity;
import com.zhilian.hzrf_oa.ui.activity.NoticeAnnouncementActivity;
import com.zhilian.hzrf_oa.ui.activity.PersonalAgendaActivity;
import com.zhilian.hzrf_oa.ui.activity.ExamineManagerActivity;
import com.zhilian.hzrf_oa.ui.activity.ReceiveManageActivity;
import com.zhilian.hzrf_oa.ui.widget.MyGridView;

/**
 * 工作台
 */
public class DiscoverFragment extends Fragment {
    private MainActivity activity;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private MyGridView gridView;
    private MyGridView gridView1;

    private OnFragmentInteractionListener mListener;

    public static DiscoverFragment newInstance(String param1, String param2) {
        DiscoverFragment fragment = new DiscoverFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DiscoverFragment() {
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
        View v = inflater.inflate(R.layout.work_layout, container, false);
        activity = (MainActivity) getActivity();

        // 添加应用
		RelativeLayout addAppLayout = (RelativeLayout) v.findViewById(R.id.main_add_application);
		addAppLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                Toast.makeText(activity,"提示：暂未开通",Toast.LENGTH_SHORT).show();
				/*Intent intent = new Intent(activity,AddApplication.class);
				startActivity(intent);*/
			}
		});
        initGridView(v);// 初始化九宫格

        return v;
    }

    private void initGridView(View v){
        // 个人办公
        gridView = (MyGridView) v.findViewById(R.id.gridview);
        gridView.setAdapter(new WorkGridViewAdapter(activity));
        gridView.setOnItemClickListener(listener);
        // 公文管理
        gridView1 = (MyGridView) v.findViewById(R.id.gridview1);
        gridView1.setAdapter(new WorkGridViewAdapter1(activity,mParam1));
        gridView1.setOnItemClickListener(listener1);
    }

    // 个人办公
    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            int index = arg2 + 1;
            if(index == 1){// 个人日程
                Intent intent = new Intent(activity, PersonalAgendaActivity.class);
                startActivity(intent);
            }
            if(index == 2){// 记事本
                Intent intent = new Intent(activity, NotepadActivity.class);
                startActivity(intent);
            }
            if(index == 3){// 通知公告
                Intent intent = new Intent(activity, NoticeAnnouncementActivity.class);
                startActivity(intent);
            }
            if(index == 4){// 我的邮件
                Intent intent = new Intent(activity, MyMailActivity.class);
                startActivity(intent);
            }
        }
    };

    // 资料传递
    AdapterView.OnItemClickListener listener1 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            int index = arg2 + 1;
            if(index == 1){// 一般阅知
                Intent intent = new Intent(activity, AchieveManageActivity.class);
                startActivity(intent);
            }
            if(index == 2){// 收文管理
                Intent intent = new Intent(activity, ReceiveManageActivity.class);
                startActivity(intent);
            }
            if(index == 3){// 初审分办
                Intent intent = new Intent(activity, InnerSendManagerActivity.class);
                startActivity(intent);
            }
            if(index == 4){// 档案柜
                Intent intent = new Intent(activity, FileCabinetActivity.class);
                startActivity(intent);
            }
            /**
             * 新增加的功能，内部发文
             */
            if(index == 5){// 内部发文
                Intent intent = new Intent(activity, ExamineManagerActivity.class);
                startActivity(intent);
            }
        }
    };

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
