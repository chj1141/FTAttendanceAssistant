package com.example.chj.ftattendanceassistant.fragment;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chj.ftattendanceassistant.R;
import com.example.chj.ftattendanceassistant.activity.MainActivity;
import com.example.chj.ftattendanceassistant.activity.RegisterActivity;
import com.example.chj.ftattendanceassistant.network.AttendResultReturn;
import com.example.chj.ftattendanceassistant.network.AttendanceResultReturn;
import com.example.chj.ftattendanceassistant.network.NetWork;
import com.example.chj.ftattendanceassistant.network.UrlContainer;
import com.example.chj.ftattendanceassistant.utils.KQInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment {
    //碎片销毁时，需要取消订阅
    protected Subscription mSubscription;
    public static RecyclerAdapter adapter;
    private static int ip;

    private static final int REQUEST_KQINFO = 0;

    private RecyclerView mRecyclerView;
    private FloatingActionButton fab_report;

    private final String TAG = "ReportFragment";
    private static final String DIALOG_KQDATA = "DialogKQData";

    private SwipeRefreshLayout mSwipeRefresh;
    private Boolean swipeFlag = false;



    public ReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerview_card);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        initKQInfos();//just for testing

        mRecyclerView.setLayoutManager(linearLayoutManager);
        //设置adapter
        adapter=new RecyclerAdapter(KQInfo.KQinfoList);
        mRecyclerView.setAdapter(adapter);

        fab_report = (FloatingActionButton)view.findViewById(R.id.fab_report_atten_data);
        fab_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "post");
                //打开对话框 增加数据
               //postAttendanceData();
                FragmentManager manager = getFragmentManager();
                KQInfoFragment dialog = KQInfoFragment.newInstance(null,-1);
                dialog.setTargetFragment(ReportFragment.this, REQUEST_KQINFO);
                dialog.show(manager,DIALOG_KQDATA);
            }
        });
        mSwipeRefresh = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
        mSwipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeFlag = true;
                initKQInfos();

            }
        });

        Log.d(TAG, "onCreateView: ");

        return view;
    }
    //建立观测者模型
    Observer<AttendanceResultReturn> mObserver = new Observer<AttendanceResultReturn>() {
        @Override
        public void onCompleted() {
            Log.d(TAG, "onCompleted: ");
        }

        @Override
        public void onError(Throwable e) {
            try {
                Log.d(TAG, "onError: "+e.toString());
            }catch (Exception ee){
                Toast.makeText(getContext(), TAG + "onError:" + e.toString(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onError: " + ee.toString());
            }

        }

        @Override
        public void onNext(AttendanceResultReturn attendanceResultReturn) {
            Log.d(TAG, "onNext: "+attendanceResultReturn.getMsgtype());
            if (attendanceResultReturn.getMsgtype()==0x03){
                //删除成功
                KQInfo.KQinfoList.remove(ip);
                adapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), "数据删除成功！", Toast.LENGTH_SHORT).show();

            }else if (attendanceResultReturn.getMsgtype()==0xE9){
                Toast.makeText(getActivity(), "数据删除时出现未知错误，请联系室助理员！", Toast.LENGTH_SHORT).show();
            }

        }
    };
    //just for test
    Observer<AttendResultReturn> mObserverGetUsername = new Observer<AttendResultReturn>() {
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
        public void onNext(AttendResultReturn attendResultReturn) {
            //Log.d(TAG, "onNext: "+attendResultReturn.getArraylist().get(1).getKQproject());
            //Log.d(TAG, "onNext: "+attendResultReturn.getArraylist().get(0).getInfoTime());
            //Log.d(TAG, "onNext: "+attendResultReturn.getArraylist().get(0).getInfoTime().length());
            KQInfo.KQinfoList.clear();
            KQInfo.KQinfoList.addAll(attendResultReturn.getArraylist());
            if (swipeFlag){
                adapter.notifyDataSetChanged();
                mSwipeRefresh.setRefreshing(false);
                swipeFlag = false;
                Toast.makeText(getActivity(),"更新了"+KQInfo.KQinfoList.size()+"条信息",Toast.LENGTH_SHORT)
                        .show();
            }
        }
    };

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.KQinfoHolder> {

        List<KQInfo> KQinfos=new ArrayList<KQInfo>();
        //构造方法传入数据
        public RecyclerAdapter(List<KQInfo> KQinfos){
            this.KQinfos=KQinfos;
        }

        //创建ViewHolder
        @Override
        public KQinfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //将item的layout转化为view传给viewholder
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_cardview,parent,false);
            return new KQinfoHolder(view);
        }

        //将数据放入相应的位置


        @Override
        public void onBindViewHolder(KQinfoHolder holder, int position) {
            if (KQinfos.get(position).getInfoTime().length()>17){
                holder.KQtime.setText(KQinfos.get(position).getInfoTime().substring(2));
            }else{
                holder.KQtime.setText(KQinfos.get(position).getInfoTime());
            }
            holder.KQduration.setText(KQinfos.get(position).getKQduration());
            holder.KQtype.setText(KQinfos.get(position).getKQtype());
            holder.KQproject.setText(KQinfos.get(position).getKQproject());
            holder.KQname.setText(KQinfos.get(position).getKQname());
            if (KQinfos.get(position).getKQtype().equals("加班".toString())){
                holder.KQimagetype.setImageResource(R.drawable.overtime);
            }else if (KQinfos.get(position).getKQtype().equals("出差".toString())){
                holder.KQimagetype.setImageResource(R.drawable.businesstravel);
            }else if(KQinfos.get(position).getKQtype().equals("请假".toString())){
                holder.KQimagetype.setImageResource(R.drawable.leave);
            }
            holder.KQapprovalStatus.setText(KQinfos.get(position).getApprovalStatus());
            if (KQinfos.get(position).getApprovalStatus().equals("已批准".toString())){
                holder.KQapprovalStatus.setTextColor(Color.argb(255,210,105,30));
            }else if(KQinfos.get(position).getApprovalStatus().equals("已否决".toString())){
                holder.KQapprovalStatus.setTextColor(Color.argb(255,141,31,25));
            }
            holder.bind(KQinfos.get(position));
        }

        @Override
        public int getItemCount() {
            return KQinfos.size();
        }






        public  class KQinfoHolder extends RecyclerView.ViewHolder
                implements View.OnClickListener,View.OnLongClickListener{
            private KQInfo mKQInfo;

            CardView cv;
            TextView KQtype;
            TextView KQproject;
            TextView KQname;
            TextView KQduration;
            TextView KQtime;
            ImageView KQimagetype;
            TextView  KQapprovalStatus;

            KQinfoHolder(View itemview){
                super(itemview);
                cv = (CardView)itemview.findViewById(R.id.cardview);
                KQtype = (TextView)itemview.findViewById(R.id.textview_KQ_type);
                KQproject = (TextView)itemview.findViewById(R.id.textview_KQ_project);
                KQname = (TextView)itemview.findViewById(R.id.textview_KQ_name);
                KQduration  = (TextView)itemview.findViewById(R.id.textview_KQ_duration);
                KQtime = (TextView)itemview.findViewById(R.id.textview_KQ_time);
                KQimagetype = (ImageView)itemview.findViewById(R.id.imageview_KQ_type);
                KQapprovalStatus = (TextView)itemview.findViewById(R.id.textview_KQ_appoval_status);
                itemview.setOnClickListener(this);
                itemview.setOnLongClickListener(this);
            }

            public void bind(KQInfo kqInfo){
                mKQInfo = kqInfo;
            }

            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), mKQInfo.getKQtype(), Toast.LENGTH_SHORT).show();
                ip = mRecyclerView.getChildAdapterPosition(view);
                if (KQInfo.KQinfoList.get(ip).getApprovalStatus().equals("已批准".toString())||
                        KQInfo.KQinfoList.get(ip).getApprovalStatus().equals("否决".toString())){
                    new SweetAlertDialog(getActivity(),SweetAlertDialog.WARNING_TYPE)
                            .setConfirmText("确定")
                            .setTitleText("该条信息已经审批，无法进行更新操作！")
                            .show();
                }else{
                    FragmentManager manager = getFragmentManager();
                    KQInfoFragment dialog = KQInfoFragment.newInstance(mKQInfo,ip);
                    dialog.setTargetFragment(ReportFragment.this, REQUEST_KQINFO);
                    dialog.show(manager,DIALOG_KQDATA);
                }

                Log.d(TAG, "onClick: "+ip);
            }

            //用于删除考勤记录
            @Override
            public boolean onLongClick(View view) {
                ip = mRecyclerView.getChildAdapterPosition(view);
                if (KQInfo.KQinfoList.get(ip).getApprovalStatus().equals("已批准".toString())||
                        KQInfo.KQinfoList.get(ip).getApprovalStatus().equals("否决".toString())) {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setConfirmText("确定")
                            .setTitleText("该条信息已经审批，无法进行删除操作！")
                            .show();
                }else{
                    new SweetAlertDialog(getActivity(),SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("是否确定删除该条信息？")
                            .setConfirmText("删除")
                            .setCancelText("取消")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    UrlContainer.postAttendanceData(KQInfo.KQinfoList.get(ip),mObserver,1);
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.cancel();
                                }
                            })
                            .show();
                }
                return true;
            }
        }



    }

    private void initKQInfos(){
        KQInfo kqInfo1 = new KQInfo();
        KQInfo kqInfo2 = new KQInfo();
        KQInfo kqInfo3 = new KQInfo();

        kqInfo1.setKQname("程鸿健");
        kqInfo1.setKQtype("加班");
        kqInfo1.setKQduration("1时");
        kqInfo1.setKQproject("FT-10A");
        kqInfo1.setKQtypeImageId(R.drawable.overtime);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        kqInfo1.setInfoTime(simpleDateFormat1.format(new Date(System.currentTimeMillis())));

        kqInfo2.setKQname("程鸿健");
        kqInfo2.setKQtype("加班");
        kqInfo2.setKQduration("2时");
        kqInfo2.setKQproject("FT-10A");
        kqInfo2.setKQtypeImageId(R.drawable.overtime);
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        kqInfo2.setInfoTime(simpleDateFormat2.format(new Date(System.currentTimeMillis())));

        kqInfo3.setKQname("程鸿健");
        kqInfo3.setKQtype("加班");
        kqInfo3.setKQduration("3时");
        kqInfo3.setKQproject("FT-10A");
        kqInfo3.setKQtypeImageId(R.drawable.overtime);
        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        kqInfo3.setInfoTime(simpleDateFormat3.format(new Date(System.currentTimeMillis())));

//        KQInfo.KQinfoList.add(kqInfo1);
//        KQInfo.KQinfoList.add(kqInfo2);
//        KQInfo.KQinfoList.add(kqInfo3);
        NetWork.getAttendanceDataGetApi()
                .getAttendanceData("getUsername", null,MainActivity.username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserverGetUsername);
        Log.d(TAG, "refreshData: ");
        Log.d(TAG, "部门为"+ MainActivity.username);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //数据更新即刷新RecylerView
        adapter.notifyDataSetChanged();
        if (resultCode != Activity.RESULT_OK){
            Log.d(TAG, "onActivityResult: 1");
            return;
        }
        if (requestCode == REQUEST_KQINFO){
            int i = (Integer)data.getSerializableExtra(KQInfoFragment.EXTRA_RENEW);
            if (i == 0xED) {
                Toast.makeText(getActivity(), "考勤人员不属于飞腾公司，请确认！", Toast.LENGTH_SHORT).show();
            }else if (i == 0xEC){
                Toast.makeText(getActivity(), "考勤参数有缺失，请确认！", Toast.LENGTH_SHORT).show();
            }else if (i == 0xEB){
                Toast.makeText(getActivity(), "已存在已审批的考勤信息，如需更改，请联系室助理员！", Toast.LENGTH_SHORT).show();
            }else if (i == 0xEE){
                Toast.makeText(getActivity(), "新添考勤信息时出现未知错误，请重试！", Toast.LENGTH_SHORT).show();
            }else if (i == 0xEA){
                Toast.makeText(getActivity(), "更新考勤信息时出现未知错误，请重试！", Toast.LENGTH_SHORT).show();
            }else if (i == 0x01){
                Toast.makeText(getActivity(), "成功添加考勤信息！", Toast.LENGTH_SHORT).show();
            }else if (i == 0x02){
                Toast.makeText(getActivity(), "成功更新考勤信息！", Toast.LENGTH_SHORT).show();
            }



            Log.d(TAG, "onActivityResult: 2 "+i);
        }
    }



}
