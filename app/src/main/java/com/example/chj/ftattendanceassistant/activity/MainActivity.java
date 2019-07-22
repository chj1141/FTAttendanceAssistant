package com.example.chj.ftattendanceassistant.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.chj.ftattendanceassistant.R;
import com.example.chj.ftattendanceassistant.fragment.ManagementFragment;
import com.example.chj.ftattendanceassistant.fragment.StatisticsFragment;
import com.example.chj.ftattendanceassistant.fragment.UserinfoFragment;
import com.example.chj.ftattendanceassistant.fragment.ReportFragment;
import com.example.chj.ftattendanceassistant.myview.CustomViewPager;
import com.example.chj.ftattendanceassistant.myview.TabBarView;
import com.example.chj.ftattendanceassistant.myview.UserTitleBar;

public class MainActivity extends AppCompatActivity {

    private CustomViewPager viewPager;
    private TabBarView tabBarView;
    private UserTitleBar userTitleBar;
    private ManagementFragment mManagementFragment;
    private StatisticsFragment mStatisticsFragment;
    private UserinfoFragment mUserinfoFragment;
    private ReportFragment mReportFragment;
    public static String username;
    public static String fullname;
    public static String groupname;
    public static int adminFlag =0;// 0:管理员标志 1:普通用户
    public static String department;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏状态栏
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }

        initMainActivity();
//        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
//        startActivityForResult(intent, 2);
    }

    private void initMainActivity(){
        mManagementFragment = new ManagementFragment();
        mStatisticsFragment = new StatisticsFragment();
        mUserinfoFragment = new UserinfoFragment();
        mReportFragment = new ReportFragment();

        viewPager = (CustomViewPager) findViewById(R.id.main_viewpager);
        tabBarView = (TabBarView)findViewById(R.id.home_tabBarView);
        userTitleBar = (UserTitleBar)findViewById(R.id.activity_main_title);

        if (adminFlag == 0){
            tabBarView.setTabBarCellData(0, R.mipmap.pageb1, R.mipmap.pagea1, "报考勤");
            tabBarView.setTabBarCellData(1, R.mipmap.pageb2, R.mipmap.pagea2, "记录");
            tabBarView.setTabBarCellData(2, R.mipmap.pageb3, R.mipmap.pagea3, "统计");
            tabBarView.setTabBarCellData(3, R.mipmap.pageb4, R.mipmap.pagea4, "我");
        }else{
            tabBarView.setTabBarCellData(0, R.mipmap.pageb1, R.mipmap.pagea1, "报考勤");
            tabBarView.setTabBarCellData(1, R.mipmap.pageb3, R.mipmap.pagea3, "统计");
            tabBarView.setTabBarCellData(2, R.mipmap.pageb4, R.mipmap.pagea4, "我");
        }

        tabBarView.setSelected(0);

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);

        select(0);

        tabBarView.setOnTabBarSelectedListener(new TabBarView.OnTabBarSelectedListener() {

            @Override
            public void onCellSelected(int index) {
                select(index);
            }
        });
    }

    private void select(int index){
        viewPager.setCurrentItem(index);
        tabBarView.setSelected(index);
    }

    public class FragmentAdapter extends FragmentPagerAdapter {
        public final static int TAB_COUNT = 4;
        public final static int TAB_COUNT_ORDINARY = 3;
        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int id) {
            if (adminFlag == 0)
            {
                switch (id) {
                    case 0:
                        return mReportFragment; //报考勤界面
                    case 1:
                        return mManagementFragment;
                    case 2:
                        return mStatisticsFragment;
                    case 3:
                        return mUserinfoFragment;
                }
            }else{
                switch (id) {
                    case 0:
                        return mReportFragment; //报考勤界面
                    case 1:
                        return mStatisticsFragment;
                    case 2:
                        return mUserinfoFragment;
                }
            }

            return null;
        }

        @Override
        public int getCount() {
            if (adminFlag == 0)
            {
                return TAB_COUNT;
            }else{
                return TAB_COUNT_ORDINARY;
            }

        }
    }
}
