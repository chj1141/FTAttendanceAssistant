package com.example.chj.ftattendanceassistant.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.chj.ftattendanceassistant.R;
import com.example.chj.ftattendanceassistant.utils.KQInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListViewFragment extends ListFragment {

    private final String TAG = "ListViewFragment";
    private MySimpleAdapter adapter = null;

    private View view;
    private int isAllSelecte = 0;

    private Map<String, Object> mapSelect = new HashMap<String, Object>();
    private List<KQInfo> mDataList;
    private List<Map<String, Object>> list = new ArrayList<>();

    public ListViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_view, container, false);
    }

    private List<Map<String, Object>> getData(List<KQInfo> appList) {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map;
        for (KQInfo kqInfoData : appList){
            map = new HashMap<>();
            map.put("姓名",kqInfoData.getKQname());
            map.put("类型",kqInfoData.getKQtype());
            map.put("项目",kqInfoData.getKQproject());//可以为空
            map.put("时长",kqInfoData.getKQduration());
            map.put("审批状态",kqInfoData.getApprovalStatus());
            Log.d(TAG, "getData: " + kqInfoData.getKQduration());

            //记得添加备注，点击item时响应
            list.add(map);
        }
        mDataList = appList;
        return list;
    }

    public void refreshData(List<KQInfo> appList) {
        if (appList==null)
            return;


        if (adapter == null){
            list = getData(appList);
            adapter = new MySimpleAdapter(getActivity(),list,
                    R.layout.kqinfo_listview_item,new String[]{"姓名","类型","项目","时长","审批状态"},
                    new int[]{R.id.textview_item_KQname,R.id.textview_item_KQtype,R.id.textview_item_KQproject,
                            R.id.textview_item_KQduration,R.id.textview_item_KQappovalstatus});
            setListAdapter(adapter);
        }else{
            list.clear();
            list.addAll(getData(appList));
            adapter.notifyDataSetChanged();

        }

    }
    //设置选择状态
    public void setSelectCheck(String KQname,boolean isSelect){
        mapSelect.put(KQname, isSelect);
    }
    //获取
    public Map<String, Object> getSelectItem(){
        return mapSelect;
    }
    //全选
    public void selectAllData(boolean bIsSelect){
        for (KQInfo kqInfoData : mDataList) {
            if ( kqInfoData.getApprovalStatus().equals("已提交") )
                setSelectCheck(kqInfoData.getKQname()+"",bIsSelect);
        }
    }



    //为ListView设置Adapter
    public class MySimpleAdapter extends SimpleAdapter {
        private FragmentActivity context;
        public MySimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            this.context = (FragmentActivity)context;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            //需要修改的在该函数中覆盖
            final CheckBox checkBox = (CheckBox)view.findViewById(R.id.checkbox_is_select);
            checkBox.setVisibility(View.VISIBLE);
            final TextView mTextViewApproStatus = (TextView)view.findViewById(R.id.textview_item_KQappovalstatus);
            final TextView mTextViewKQname = (TextView)view.findViewById(R.id.textview_item_KQname);
            final LinearLayout kqinfoitem = (LinearLayout)view.findViewById(R.id.linearlayout_kqinfo_item);
            switch (mTextViewApproStatus.getText().toString()){
                case "已提交":
                    checkBox.setEnabled(true);
                    mTextViewApproStatus.setTextColor(Color.argb(255,234,139,17));
                    break;
                case "已批准":
                    checkBox.setEnabled(false);
                    mTextViewApproStatus.setTextColor(Color.argb(255,121,183,19));
                    setSelectCheck(mTextViewKQname.getText().toString(),false);
                    break;
                case "已否决":
                    checkBox.setEnabled(false);
                    mTextViewApproStatus.setTextColor(Color.argb(255,141,31,25));
                    setSelectCheck(mTextViewKQname.getText().toString(),false);
                    break;
                default:
                    break;
            }
            if (position%2 == 1){
                kqinfoitem.setBackgroundColor(Color.argb(80,49,119,239));
            }else{
                kqinfoitem.setBackgroundColor(Color.argb(10,0,0,0));
            }
            //根据hashMap状态设置CheckBox状态
            try {
                if ((boolean)mapSelect.get(mTextViewKQname.getText().toString())){
                    checkBox.setChecked(true);
                }else{
                    checkBox.setChecked(false);
                }
            }catch (Exception e){
                checkBox.setChecked(false);
            }

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String str = mTextViewKQname.getText().toString();
                    setSelectCheck(str,checkBox.isChecked());
                }
            });


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new SweetAlertDialog(getActivity(),SweetAlertDialog.NORMAL_TYPE)
                            .setTitleText("备注信息")
                            .setContentText(mDataList.get(position).getKQremark())
                            .setConfirmText("确定")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }
            });







            return view;
        }
    }

}
