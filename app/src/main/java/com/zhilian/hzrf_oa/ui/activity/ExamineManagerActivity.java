package com.zhilian.hzrf_oa.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.ui.fragment.ExamineFragment;

/**
 * Created by Administrator on 2017-9-15.
 *
 * 初审分办
 */
public class ExamineManagerActivity extends FragmentActivity {
    private TextView title;//大标题
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_cabinet_layout1);
        title = (TextView)findViewById(R.id.title) ;
        title.setText("初审分办");
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initView();// 初始化控件
        initFragment();
    }

    private void initView(){}
    private void initFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ExamineFragment fragment = ExamineFragment.newInstance("","");
        transaction.addToBackStack("preliminary");
        transaction.replace(R.id.doc_cab_list,fragment);

    }
}
