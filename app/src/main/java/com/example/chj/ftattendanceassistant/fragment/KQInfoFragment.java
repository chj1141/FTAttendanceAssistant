package com.example.chj.ftattendanceassistant.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chj.ftattendanceassistant.R;
import com.example.chj.ftattendanceassistant.network.AttendResultReturn;
import com.example.chj.ftattendanceassistant.network.AttendanceResultReturn;
import com.example.chj.ftattendanceassistant.network.NetWork;
import com.example.chj.ftattendanceassistant.network.UrlContainer;
import com.example.chj.ftattendanceassistant.utils.KQInfo;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.example.chj.ftattendanceassistant.fragment.ReportFragment.adapter;

/**
 * Created by chenghj on 2019/6/10.
 */

public class KQInfoFragment extends DialogFragment {

    private static final String ARG_KQInfo = "KQinfo";
    private static final String ARG_INTEGER = "integer";
    public static final String EXTRA_RENEW = "com.example.chj.ftattendanceassistant.renew";

    protected Subscription mSubscription;
    private int kqInfoPosition = 0;
    private KQInfo kqInfo = null;
    private AlertDialog kqInfoDialog;

    private static final String TAG = "KQInfoFragment";

    private String KQType[]={"加班","出差","请假"};

    private Spinner spinner_kq_type;
    private Spinner spinner_kq_duration_unit;
    private EditText edittext_kq_duration;
    private EditText edittext_kq_project;
    private EditText edittext_kq_emplyee;
    private EditText edittext_kq_remark;


    private ArrayAdapter<CharSequence> mKQtypeAdapter;
    private ArrayAdapter<CharSequence> mKQdurationUnitAdapter;

    //建立观测者模型
    Observer<AttendanceResultReturn> mObserver = new Observer<AttendanceResultReturn>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            try {
                Log.d(TAG, "onError1: "+e.toString());
            }catch (Exception ee){
                Toast.makeText(getContext(), TAG + "onError:" + e.toString(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onError2: " + ee.toString());
            }

        }
        @Override
        public void onNext(AttendanceResultReturn attendanceResultReturn) {
            Log.d(TAG, "onNext: ");
            int i = KQInfo.KQinfoList.size();
            //公司无该人
            if (attendanceResultReturn.getMsgtype() == 0xED) {
                if (kqInfo == null) {
                    KQInfo.KQinfoList.remove(i-1);
                }
                sendResult(Activity.RESULT_OK, 0xED);
            }
            //参数丢失
            if (attendanceResultReturn.getMsgtype() == 0xEC) {
                if (kqInfo == null) {
                    KQInfo.KQinfoList.remove(i-1);
                }
                sendResult(Activity.RESULT_OK, 0xEC);
            }
            //已存在已审批的考勤信息，如需更改，请联系室助理员！
            if (attendanceResultReturn.getMsgtype() == 0xEB) {
                if (kqInfo == null) {
                    KQInfo.KQinfoList.remove(i-1);
                }
                sendResult(Activity.RESULT_OK, 0xEB);
            }
            //插入数据时，出现未知的错误
            if (attendanceResultReturn.getMsgtype() == 0xEE) {
                if (kqInfo == null){
                    KQInfo.KQinfoList.get(i-1).setApprovalStatus("提交失败");
                }
                sendResult(Activity.RESULT_OK, 0xEE);
            }
            //更新数据时，出现未知的错误
            if (attendanceResultReturn.getMsgtype() == 0xEA) {
                if (kqInfo == null){
                    KQInfo.KQinfoList.get(i-1).setApprovalStatus("提交失败");
                }
                sendResult(Activity.RESULT_OK, 0xEA);
            }
            //成功添加考勤信息
            if (attendanceResultReturn.getMsgtype() == 0x01) {
                sendResult(Activity.RESULT_OK, 0x01);
                if (kqInfo == null){
                    KQInfo.KQinfoList.get(i-1).setApprovalStatus("已提交");
                }else{
                    KQInfo.KQinfoList.get(kqInfoPosition).setApprovalStatus("已提交");
                }
            }
            //成功更新考勤信息
            if (attendanceResultReturn.getMsgtype() == 0x02) {
                sendResult(Activity.RESULT_OK, 0x02);
                //Log.d(TAG, "onNext: "+attendanceResultReturn.getResults().get(0).getKQname());
                if (kqInfo == null){
                    KQInfo.KQinfoList.get(i-1).setApprovalStatus("已提交");
                    Log.d(TAG, "onNext: 空"+i);
                }else{
                    KQInfo.KQinfoList.get(kqInfoPosition).setApprovalStatus("已提交");
                    Log.d(TAG, "onNext: 不空"+kqInfoPosition);
                }
            }
            Log.d(TAG, "已提交"+ i);
            Log.d(TAG, attendanceResultReturn.getMsg());

        }
    };
    

    public static KQInfoFragment newInstance(KQInfo kqInfo, Integer integer)
    {
        Bundle args = new Bundle();
        args.putSerializable(ARG_KQInfo,kqInfo);
        args.putSerializable(ARG_INTEGER,integer);

        KQInfoFragment kqInfoFragment = new KQInfoFragment();
        kqInfoFragment.setArguments(args);
        return kqInfoFragment;

    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        kqInfo = (KQInfo)getArguments().getSerializable(ARG_KQInfo);
        kqInfoPosition = (Integer) getArguments().getSerializable(ARG_INTEGER);
        Log.d(TAG, "onCreateDialog: "+kqInfoPosition);


        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_kqinfo,null);

        spinner_kq_type = (Spinner)v.findViewById(R.id.spinner_kq_type);
        spinner_kq_duration_unit = (Spinner)v.findViewById(R.id.spinner_kq_duration_unit);

        edittext_kq_project = (EditText)v.findViewById(R.id.edittext_kq_project);
        edittext_kq_duration = (EditText)v.findViewById(R.id.edittext_kq_duration);
        edittext_kq_emplyee = (EditText)v.findViewById(R.id.edittext_kq_employee);
        edittext_kq_remark = (EditText)v.findViewById(R.id.edittext_kq_remark);

        spinner_kq_type.setFocusable(true);
        mKQtypeAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.kq_type_item, android.R.layout.simple_spinner_item);
        mKQtypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //考勤Spinner
        spinner_kq_type.setAdapter(mKQtypeAdapter);

        mKQdurationUnitAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.kq_duration_unit_item, android.R.layout.simple_spinner_item);
        mKQdurationUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //时长Spinner
        spinner_kq_duration_unit.setAdapter(mKQdurationUnitAdapter);
        //如果通过点击添加按钮进入，说明无数据，这进行初始化操作
        if (kqInfo == null){
            //默认为加班
            spinner_kq_type.setSelection(0);
            //默认为时
            spinner_kq_duration_unit.setSelection(0);
            //默认备注为无
            edittext_kq_remark.setText("无");
        }else{
            String[] str_kqduration_day;
            String[] str_kqduration_hour;

            for(int i=0 ; i<3 ;i++){
                if(kqInfo.getKQtype().equals(mKQtypeAdapter.getItem(i).toString())){
                    spinner_kq_type.setSelection(i,true);
                    break;
                }
            }
            Log.d(TAG, kqInfo.getKQduration());
            edittext_kq_duration.setText(kqInfo.getKQduration());
            spinner_kq_duration_unit.setSelection(0);
            /*if(kqInfo.getKQduration().contains("时")){
                str_kqduration_hour = kqInfo.getKQduration().split("时");
                Log.d(TAG, kqInfo.getKQduration());
                edittext_kq_duration.setText(str_kqduration_hour[0]);
                for(int i=0 ; i<2 ;i++){
                    if("时".equals(mKQdurationUnitAdapter.getItem(i).toString())){
                        spinner_kq_duration_unit.setSelection(0);
                        break;
                    }
                }
            }else if (kqInfo.getKQduration().contains("天")){
                Log.d(TAG, kqInfo.getKQduration());
                str_kqduration_day = kqInfo.getKQduration().split("天");
                edittext_kq_duration.setText(str_kqduration_day[0]);
                for(int i=0 ; i<2 ;i++){
                    if("天".equals(mKQdurationUnitAdapter.getItem(i).toString())){
                        spinner_kq_duration_unit.setSelection(1);
                        break;
                    }
                }
            }*/
            edittext_kq_project.setText(kqInfo.getKQproject());
            edittext_kq_emplyee.setText(kqInfo.getKQname());
            edittext_kq_remark.setText(kqInfo.getKQremark());

        }

        spinner_kq_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = (TextView)view;
                tv.setGravity(Gravity.LEFT);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner_kq_duration_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = (TextView)view;
                tv.setGravity(Gravity.LEFT);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        kqInfoDialog = builder
                .setView(v)
                .setCancelable(false)
                .setTitle(R.string.kq_info)
                .setPositiveButton("提交", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (TextUtils.isEmpty(edittext_kq_duration.getText())|| TextUtils.isEmpty(edittext_kq_emplyee.getText())||
                                TextUtils.isEmpty(edittext_kq_remark.getText())){
                            new SweetAlertDialog(getActivity(),SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("请补全缺漏信息！")
                                    .setConfirmText("确定")
                                    .show();
                            return;
                        }
                        if (kqInfo == null){
                            KQInfo kqInfoTemp = new KQInfo();
                            kqInfoTemp.setKQtype((String)spinner_kq_type.getSelectedItem());
                            kqInfoTemp.setKQproject(edittext_kq_project.getText().toString());
                            if (spinner_kq_duration_unit.getSelectedItem().equals("天".toString())){
                                float temp = Float.parseFloat(edittext_kq_duration.getText().toString())*8;
                                kqInfoTemp.setKQduration(String.valueOf(temp));
                            }else{
                                kqInfoTemp.setKQduration(edittext_kq_duration.getText().toString());
                            }
                            //kqInfoTemp.setKQduration(edittext_kq_duration.getText().toString()+spinner_kq_duration_unit.getSelectedItem());
                            kqInfoTemp.setKQremark(edittext_kq_remark.getText().toString());
                            if (kqInfoTemp.getKQtype().equals("加班".toString())){
                                kqInfoTemp.setKQtypeImageId(R.drawable.overtime);
                            }
                            if (kqInfoTemp.getKQtype().equals("请假".toString())){
                                kqInfoTemp.setKQtypeImageId(R.drawable.leave);
                            }
                            if (kqInfoTemp.getKQtype().equals("出差".toString())){
                                kqInfoTemp.setKQtypeImageId(R.drawable.businesstravel);
                            }
                            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
                            kqInfoTemp.setInfoTime(simpleDateFormat2.format(new Date(System.currentTimeMillis())));
                            kqInfoTemp.setKQname(edittext_kq_emplyee.getText().toString());
                            kqInfoTemp.setApprovalStatus("未提交");

                            KQInfo.KQinfoList.add(kqInfoTemp);

                            UrlContainer.postAttendanceData(kqInfoTemp,mObserver,0);

                        }else{
                            KQInfo.KQinfoList.get(kqInfoPosition).setKQtype((String)spinner_kq_type.getSelectedItem());
                            if (spinner_kq_duration_unit.getSelectedItem().equals("天".toString())){
                                float temp = Float.parseFloat(edittext_kq_duration.getText().toString())*8;
                                KQInfo.KQinfoList.get(kqInfoPosition).setKQduration(String.valueOf(temp));
                            }else{
                                KQInfo.KQinfoList.get(kqInfoPosition).setKQproject(edittext_kq_duration.getText().toString());
                            }
                            KQInfo.KQinfoList.get(kqInfoPosition).setKQproject(edittext_kq_project.getText().toString());
                            //KQInfo.KQinfoList.get(kqInfoPosition).setKQduration(edittext_kq_duration.getText().toString()+spinner_kq_duration_unit.getSelectedItem());
                            if (KQInfo.KQinfoList.get(kqInfoPosition).getKQtype().equals("加班".toString())){
                                KQInfo.KQinfoList.get(kqInfoPosition).setKQtypeImageId(R.drawable.overtime);
                            }
                            if (KQInfo.KQinfoList.get(kqInfoPosition).getKQtype().equals("请假".toString())){
                                KQInfo.KQinfoList.get(kqInfoPosition).setKQtypeImageId(R.drawable.leave);
                            }
                            if (KQInfo.KQinfoList.get(kqInfoPosition).getKQtype().equals("出差".toString())){
                                KQInfo.KQinfoList.get(kqInfoPosition).setKQtypeImageId(R.drawable.businesstravel);
                            }
                            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
                            KQInfo.KQinfoList.get(kqInfoPosition).setInfoTime(simpleDateFormat2.format(new Date(System.currentTimeMillis())));
                            KQInfo.KQinfoList.get(kqInfoPosition).setKQname(edittext_kq_emplyee.getText().toString());
                            KQInfo.KQinfoList.get(kqInfoPosition).setKQremark(edittext_kq_remark.getText().toString());


                            UrlContainer.postAttendanceData(KQInfo.KQinfoList.get(kqInfoPosition),mObserver,0);

                        }
                    }
                })
                .create();

//        kqInfoDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dialogInterface) {
//                Button positiveButton = kqInfoDialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                positiveButton.setOnClickListener(new View.OnClickListener(){
//                    @Override
//                    public void onClick(View view) {
//
//                    }
//                });
//            }
//        });

        return kqInfoDialog;
    }

    //向目标fragment发送数据
    private void sendResult(int resultCode, Integer integer){
        if (getTargetFragment() == null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RENEW, integer);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
