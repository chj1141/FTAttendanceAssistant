package com.example.chj.ftattendanceassistant.fragment;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.chj.ftattendanceassistant.R;
import com.example.chj.ftattendanceassistant.activity.MainActivity;
import com.example.chj.ftattendanceassistant.network.AttendResultReturn;
import com.example.chj.ftattendanceassistant.network.AttendanceResultReturn;
import com.example.chj.ftattendanceassistant.network.NetWork;
import com.example.chj.ftattendanceassistant.utils.KQInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManagementFragment extends Fragment {

    private final String TAG = "ManagementFragment";
    private ListViewFragment fragment;
    private Button btnScreen;
    private Button btnSelectall;
    private Button btnRefresh;
    private Button btnApproval;
    private Button btnReject;

    private Spinner mSpinnerScreenKQtype;
    private ArrayAdapter<CharSequence> mScreenKQtypeAdapter;

    private String screenCondition;
    private List<KQInfo> list = new ArrayList<>();
    private List<KQInfo> listTemp = new ArrayList<>();//筛选后的list
    private short btnFlagRefresh = 0;
    private short btnFlagScreen = 0;
    private Boolean isSelectAll = true;

    public ManagementFragment() {
        // Required empty public constructor
    }
    //管理员获取要处理的数据
    Observer<AttendResultReturn> mObserver = new Observer<AttendResultReturn>() {
        @Override
        public void onCompleted() {
//            Log.d(TAG, "list:"+ list.size());
            fragment.refreshData(list);
            if (btnFlagRefresh == 1){
                btnFlagRefresh = 0;
                new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("")
                        .setContentText("刷新成功！")
                        .setConfirmText("确定")
                        .show();
            }
        }

        @Override
        public void onError(Throwable e) {
            try {
                if (btnFlagRefresh == 1){
                    btnFlagRefresh = 0;
                }
                Log.d(TAG, "onError: "+e.toString());
            }catch (Exception ee){
                Log.d(TAG, "onError: " + ee.toString());
            }

        }

        @Override
        public void onNext(AttendResultReturn attendResultReturn) {
            //Log.d(TAG, "onNext: "+attendResultReturn.getArraylist().get(1).getKQname());
            list = attendResultReturn.getArraylist();
            fragment.refreshData(list);
            Log.d(TAG, "onNext: "+attendResultReturn.getMsg());
        }
    };
    //管理员审批数据
    Observer<AttendanceResultReturn> mObserverApproval = new Observer<AttendanceResultReturn>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(AttendanceResultReturn attendanceResultReturn) {
            Log.d(TAG, "onNext: ");

        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_management, container, false);
        ViewGroup layoutTitle = (ViewGroup)view.findViewById(R.id.layout_title);
        layoutTitle.setBackgroundColor(Color.rgb(177, 173, 172));


        FragmentManager fragmentManager = this.getChildFragmentManager();
        fragment = (ListViewFragment)fragmentManager.findFragmentById(R.id.fragment_listview);

        refreshData();
        btnScreen = (Button)view.findViewById(R.id.button_screen);
        btnSelectall = (Button)view.findViewById(R.id.button_selectall);
        btnRefresh = (Button)view.findViewById(R.id.button_refresh);
        btnReject = (Button)view.findViewById(R.id.button_reject);
        btnApproval = (Button)view.findViewById(R.id.button_approve);
        //筛选按钮
        btnScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = LayoutInflater.from(getActivity())
                        .inflate(R.layout.layout_screen_dialog,null);
                mSpinnerScreenKQtype = (Spinner)v.findViewById(R.id.spinner_screen_kqtype);
                mScreenKQtypeAdapter = ArrayAdapter.createFromResource(getActivity(),
                        R.array.screen_kq_type_item, android.R.layout.simple_spinner_item);
                mScreenKQtypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinnerScreenKQtype.setAdapter(mScreenKQtypeAdapter);
                mSpinnerScreenKQtype.setSelection(0);

                btnFlagScreen = 1;

                new AlertDialog.Builder(getActivity())
                        .setView(v)
                        .setTitle("筛选条件")
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mSpinnerScreenKQtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });
                                screenCondition = mSpinnerScreenKQtype.getSelectedItem().toString();
                                Log.d(TAG, "onClick: " + screenCondition);
                                if (screenCondition.equals("全部".toString())){
                                    listTemp.clear();
                                    listTemp.addAll(list);
                                    Log.d(TAG, "onClick: " +listTemp.size());

                                }else if(screenCondition.equals("加班".toString())){
                                    listTemp.clear();
                                    for (int ii = 0 ; ii < list.size(); ii++){
                                        if (list.get(ii).getKQtype().equals("加班".toString())){
                                            listTemp.add(list.get(ii));
                                        }
                                    }
                                    Log.d(TAG, "onClick: " +listTemp.size());

                                }else if(screenCondition.equals("出差".toString())){
                                    listTemp.clear();
                                    for (int ii = 0 ; ii < list.size(); ii++){
                                        if (list.get(ii).getKQtype().equals("出差".toString())){
                                            listTemp.add(list.get(ii));
                                        }
                                    }
                                    Log.d(TAG, "onClick: " +listTemp.size());

                                }else if(screenCondition.equals("请假".toString())){
                                    listTemp.clear();
                                    for (int ii = 0 ; ii < list.size(); ii++){
                                        if (list.get(ii).getKQtype().equals("请假".toString())){
                                            listTemp.add(list.get(ii));
                                        }
                                    }
                                    Log.d(TAG, "onClick: " +listTemp.size());

                                }
                                Log.d(TAG, "onClick: " + listTemp.size());
                                fragment.refreshData(listTemp);


                            }
                        })
                        .show();


            }
        });
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                refreshData();
                btnFlagRefresh = 1;
            }
        });

        btnSelectall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: 全选");
                if (isSelectAll == true){
                    isSelectAll = false;
                    fragment.selectAllData(true);
                    if (btnFlagScreen == 1){
                        fragment.refreshData(listTemp);
                    }else{
                        fragment.refreshData(list);
                    }
                    final Map<String, Object> mapSelect = fragment.getSelectItem();
                    int count = 0;
                    for (String key :mapSelect.keySet()){
                        if ((boolean)mapSelect.get(key)){
                            count ++;
                        }
                    }
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("提示")
                            .setContentText("已经选定"+count+"项纪录！")
                            .setConfirmText("确定")
                            .show();
                    btnSelectall.setText("全部取消");
                }else{
                    isSelectAll = true;
                    fragment.selectAllData(false);
                    if (btnFlagScreen == 1){
                        fragment.refreshData(listTemp);
                    }else{
                        fragment.refreshData(list);
                    }
                    btnSelectall.setText("全    选");
                }
            }
        });
        btnApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Map<String, Object> mapSelect = fragment.getSelectItem();
                int count = 0;
                for (String key :mapSelect.keySet()){
                    if ((boolean)mapSelect.get(key)){
                        count ++;
                    }
                }
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("提示")
                        .setContentText("是否确定要批准"+count+"项纪录？")
                        .setConfirmButton("确定", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                for (String KQname : mapSelect.keySet()) {
                                    if ((boolean)mapSelect.get(KQname)){
                                        NetWork.setApprovalStatusApi()
                                                .postAttendanceData(KQname, "已批准", 0)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(mObserverApproval);

                                    }
                                }
                                sweetAlertDialog.dismissWithAnimation();
                            }

                        })
                        .setCancelButton("取消", null)
                        .show();
            }
        });
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Map<String, Object> mapSelect = fragment.getSelectItem();
                int count = 0;
                for (String key :mapSelect.keySet()){
                    if ((boolean)mapSelect.get(key)){
                        count ++;
                    }
                }
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("提示")
                        .setContentText("是否确定要否决"+count+"项纪录？")
                        .setConfirmButton("确定", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                for (String KQname : mapSelect.keySet()) {
                                    if ((boolean)mapSelect.get(KQname)){
                                        NetWork.setApprovalStatusApi()
                                                .postAttendanceData(KQname, "已否决", 0)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(mObserverApproval);

                                    }
                                }
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelButton("取消", null)
                        .show();


            }
        });


        return view;
    }

    public void refreshData(){
        //发送请求
        NetWork.getAttendanceDataGetApi()
                .getAttendanceData("getDepartment",MainActivity.department,null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
        Log.d(TAG, "refreshData: ");
        Log.d(TAG, "部门为"+ MainActivity.department);

    }

}
