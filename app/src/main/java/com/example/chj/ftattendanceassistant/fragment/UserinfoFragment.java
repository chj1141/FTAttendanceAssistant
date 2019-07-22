package com.example.chj.ftattendanceassistant.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chj.ftattendanceassistant.R;
import com.example.chj.ftattendanceassistant.activity.ChangePWActivity;
import com.example.chj.ftattendanceassistant.activity.MainActivity;
import com.example.chj.ftattendanceassistant.network.NetWork;
import com.example.chj.ftattendanceassistant.network.UserKQinfoResult;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserinfoFragment extends Fragment {


    private final String TAG = "UserinfoFragment";
    private TextView textviewPerWeek;
    private TextView textviewPerMonth;
    private TextView textviewPerYear;

    private TextView textviewPerWeekUser;
    private TextView textviewPerMonthUser;
    private TextView textviewPerYearUser;

    private TextView textviewGroup;
    private TextView textviewUsername;
    private TextView textviewFullname;

    private List<UserKQinfoResult.TimeString> listOverTime = new ArrayList<>();
    private List<UserKQinfoResult.TimeString> listLeave = new ArrayList<>();
    private List<UserKQinfoResult.TimeString> listBusinessTravel = new ArrayList<>();

    private LinearLayout viewKqtype;

    private RelativeLayout mRelativeLayoutChangePW;
    private RelativeLayout mRelativeLayoutAppinfo;

    private int flag = 0;

    public UserinfoFragment() {
        // Required empty public constructor
    }

    private Observer<UserKQinfoResult> mUserKQinfoResultObserver = new Observer<UserKQinfoResult>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            try {
                Log.d(TAG, "onError: "+e.toString());
            }catch (Exception ee){
                Log.d(TAG, "onError: " + ee.toString());
            }

        }

        @Override
        public void onNext(UserKQinfoResult userKQinfoResult) {
            Log.d(TAG, "onNext: ");
            Log.d(TAG, userKQinfoResult.getArraylistOvertime().get(0).getPeryear());
            Log.d(TAG, userKQinfoResult.getArraylistLeave().get(0).getPeryear());
            Log.d(TAG, userKQinfoResult.getArraylistBusinesstravel().get(0).getPeryear());
            listOverTime = userKQinfoResult.getArraylistOvertime();
            listLeave = userKQinfoResult.getArraylistLeave();
            listBusinessTravel = userKQinfoResult.getArraylistBusinesstravel();
            if (userKQinfoResult.getMsgtype() == 0x41){
                textviewPerWeek.setText("本周加班");
                textviewPerMonth.setText("本月加班");
                textviewPerYear.setText("本年加班");

                textviewPerWeekUser.setText(listOverTime.get(0).getPerweek()+"时");
                textviewPerMonthUser.setText(listOverTime.get(0).getPermonth()+"时");
                textviewPerYearUser.setText(listOverTime.get(0).getPeryear()+"时");

                flag = 1;

            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_userinfo, container, false);
        Log.d(TAG, "onCreateView: ");
        refreshUserKQinfoData();
        textviewPerWeek = (TextView)view.findViewById(R.id.textview_perweek);
        textviewPerMonth = (TextView)view.findViewById(R.id.textview_permonth);
        textviewPerYear = (TextView)view.findViewById(R.id.textview_peryear);

        textviewPerWeekUser = (TextView)view.findViewById(R.id.user_perweek);
        textviewPerMonthUser = (TextView)view.findViewById(R.id.user_permonth);
        textviewPerYearUser = (TextView)view.findViewById(R.id.user_peryear);

        viewKqtype = (LinearLayout)view.findViewById(R.id.linearlayout_user_kqtype);
        viewKqtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 1){
                    textviewPerWeek.setText("本周出差");
                    textviewPerMonth.setText("本月出差");
                    textviewPerYear.setText("本年出差");

                    textviewPerWeekUser.setText(listBusinessTravel.get(0).getPerweek()+"时");
                    textviewPerMonthUser.setText(listBusinessTravel.get(0).getPermonth()+"时");
                    textviewPerYearUser.setText(listBusinessTravel.get(0).getPeryear()+"时");

                    flag = 2;

                }else if(flag == 2){
                    textviewPerWeek.setText("本周请假");
                    textviewPerMonth.setText("本月请假");
                    textviewPerYear.setText("本年请假");

                    textviewPerWeekUser.setText(listLeave.get(0).getPerweek()+"时");
                    textviewPerMonthUser.setText(listLeave.get(0).getPermonth()+"时");
                    textviewPerYearUser.setText(listLeave.get(0).getPeryear()+"时");

                    flag = 0;

                }else if(flag == 0){
                    textviewPerWeek.setText("本周加班");
                    textviewPerMonth.setText("本月加班");
                    textviewPerYear.setText("本年加班");

                    textviewPerWeekUser.setText(listOverTime.get(0).getPerweek()+"时");
                    textviewPerMonthUser.setText(listOverTime.get(0).getPermonth()+"时");
                    textviewPerYearUser.setText(listOverTime.get(0).getPeryear()+"时");

                    flag = 1;

                }
            }
        });

        mRelativeLayoutChangePW = (RelativeLayout) view.findViewById(R.id.relativelayout_changepw);
        mRelativeLayoutAppinfo = (RelativeLayout)view.findViewById(R.id.relativelayout_appinfo);
        mRelativeLayoutChangePW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangePWActivity.class);
                startActivityForResult(intent, 3);
            }
        });
        mRelativeLayoutAppinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(getActivity(),SweetAlertDialog.NORMAL_TYPE)
                        .setConfirmText("飞腾考勤助手版本：1.00" +
                                "\n"+"版本所有：总体室软件组")
                        .show();
            }
        });

        textviewFullname = (TextView)view.findViewById(R.id.user_KQname);
        textviewFullname.setText("姓名："+MainActivity.fullname);
        textviewGroup = (TextView)view.findViewById(R.id.user_group);
        textviewGroup.setText(MainActivity.groupname);
        textviewUsername = (TextView)view.findViewById(R.id.user_username);
        textviewUsername.setText("账号："+MainActivity.username);




        return view;
    }



    private void refreshUserKQinfoData(){
        NetWork.getUserKQinfoGetApi()
                .getUserKQinfoResult(null,MainActivity.fullname)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mUserKQinfoResultObserver);
    }

}
