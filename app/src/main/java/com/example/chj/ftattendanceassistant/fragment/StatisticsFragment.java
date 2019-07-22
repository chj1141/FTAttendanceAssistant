package com.example.chj.ftattendanceassistant.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.chj.ftattendanceassistant.R;
import com.example.chj.ftattendanceassistant.network.EmployeeDataResult;
import com.example.chj.ftattendanceassistant.network.EmployeeInfoResult;
import com.example.chj.ftattendanceassistant.network.NetWork;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.formatter.ColumnChartValueFormatter;
import lecho.lib.hellocharts.formatter.LineChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleColumnChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleLineChartValueFormatter;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatisticsFragment extends Fragment {

    public final static String[] days = new String[]{"01", "02", "03", "04", "05", "06", "07", "08",
                                                    "09", "10", "11", "12", "13", "14", "15", "16",
                                                     "17", "18", "19", "20", "21", "22", "23", "24",
                                                    "25", "26", "27", "28", "29", "30", "31"};

    private LineChartView chartTop;
    private ColumnChartView chartBottom;

    private LineChartData lineData;
    private ColumnChartData columnData;
    private List<String> list_department = new ArrayList<String>();
    private ArrayAdapter<String> adapter_department;
    private Spinner spinner_department;
    private List<String> list_team = new ArrayList<String>();
    private ArrayAdapter<String> adapter_team;
    private Spinner spinner_team;
    private List<String> list_person = new ArrayList<String>();
    private ArrayAdapter<String> adapter_person;
    private Spinner spinner_person;
    private List<String> list_type = new ArrayList<String>();
    private ArrayAdapter<CharSequence> adapter_type;
    private Spinner spinner_type;
    private List<EmployeeInfoResult.EmployeeInfo> employeeInfos = new ArrayList<>();

    private Button button_search;
    private Button button_reset;

    private final String TAG = "StatisticsFragment";
    //设置默认值
    private String mStrDepartment = "全部";
    private String mStrGroup = "全部";
    private String mStrName = "全部";
    private String mStrKQtype = "加班";
    private int lineColorValue = 0;

    private View rootView;

    //月数据
    private List<EmployeeDataResult.EmployeeData> listMonths = new ArrayList<>();
    //天数据
    private List<EmployeeDataResult.EmployeeData> listDays = new ArrayList<>();


    public StatisticsFragment() {
        // Required empty public constructor
    }

    private Observer<EmployeeInfoResult> mEmployeeInfoResultObserver = new Observer<EmployeeInfoResult>() {
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
        public void onNext(EmployeeInfoResult employeeInfoResult) {
            //Log.d(TAG, "onNext: "+employeeInfoResult.getArraylist().get(1).getName());
            Log.d(TAG, "onNext: "+employeeInfoResult.getMsg());
            if (employeeInfoResult.getMsgtype() == 0x31){
                employeeInfos = employeeInfoResult.getArraylist();
                initSpinnerContent(list_department,adapter_department,employeeInfos,spinner_department,0x31);
                Log.d(TAG, "开始初始化部门信息！");
                initSpinnerContent(list_team,adapter_team,employeeInfos,spinner_team,0x32);
                initSpinnerContent(list_person,adapter_person,employeeInfos,spinner_person,0x33);

            }else if (employeeInfoResult.getMsgtype() == 0x32){
                Log.d(TAG, "开始获取某一个部门信息！");
                employeeInfos = employeeInfoResult.getArraylist();
                initSpinnerContent(list_team,adapter_team,employeeInfos,spinner_team,0x32);
                initSpinnerContent(list_person,adapter_person,employeeInfos,spinner_person,0x33);
            }else if (employeeInfoResult.getMsgtype() == 0x33){
                Log.d(TAG, "开始获取某一个小组信息！");
                employeeInfos = employeeInfoResult.getArraylist();
                initSpinnerContent(list_person,adapter_person,employeeInfos,spinner_person,0x33);
            }
        }
    };

    private Observer<EmployeeDataResult> mEmployeeDataResultObserver = new Observer<EmployeeDataResult>() {
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
        public void onNext(EmployeeDataResult employeeDataResult) {
            if ((employeeDataResult.getMsgtype() == 0x34)||(employeeDataResult.getMsgtype() == 0x35)||
               (employeeDataResult.getMsgtype() == 0x36)||(employeeDataResult.getMsgtype() == 0x37)){
                listMonths = employeeDataResult.getArraylist();
                setColumnData(listMonths);
            }
            if ((employeeDataResult.getMsgtype() == 0x38)||(employeeDataResult.getMsgtype() == 0x39)||
                    (employeeDataResult.getMsgtype() == 0x3A)||(employeeDataResult.getMsgtype() == 0x3B)){
                listDays = employeeDataResult.getArraylist();
                setLineData(listDays);
            }
            Log.d(TAG, "onNext: "+employeeDataResult.getMsg());
            //Log.d(TAG, "onNext: "+employeeDataResult.getArraylist().get(0).getStrdate());
            for (int i=0;i<=employeeDataResult.getArraylist().size();i++){
                Log.d(TAG, "onNext: "+employeeDataResult.getArraylist().get(i).getStrdate());
                Log.d(TAG, "onNext: "+employeeDataResult.getArraylist().get(i).getDurationsum());
            }

        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_statistics, container, false);
        spinner_type = (Spinner)rootView.findViewById(R.id.spinner_statistics_type);
        adapter_type = ArrayAdapter.createFromResource(getActivity(),
                R.array.kq_type_item, android.R.layout.simple_spinner_item);
        adapter_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_type.setAdapter(adapter_type);
        spinner_type.setSelection(0);

        chartTop = (LineChartView)rootView.findViewById(R.id.chart_top);
        //初始化折线表
        generateInitialLineData();
        //请求考勤的所有数据 整个所里的数据
        getColumnData();


        chartBottom = (ColumnChartView) rootView.findViewById(R.id.chart_bottom);


        //部门
        spinner_department = (Spinner)rootView.findViewById(R.id.spinner_statistics_department);
        //姓名
        spinner_person = (Spinner)rootView.findViewById(R.id.spinner_statistics_name);
        //小组
        spinner_team = (Spinner)rootView.findViewById(R.id.spinner_statistics_group);

        button_search = (Button)rootView.findViewById(R.id.button_statistics_search);
        button_reset = (Button)rootView.findViewById(R.id.button_statistics_reset);
        button_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSpinnerdata();
                setColumnData(null);
                lineColorValue = ChartUtils.COLOR_GREEN;
                setLineData( null);
            }
        });
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getColumnData();
                lineColorValue = ChartUtils.COLOR_GREEN;

                setLineData( null);
            }
        });

        //若部门是全部，则不需要获取小组和姓名，否则根据部门依次获取内容
        spinner_department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemSelected: ");
                mStrGroup = "全部";
                mStrName = "全部";
                if (spinner_department.getSelectedItem().toString().equals("全部".toString())){
                    mStrDepartment = "全部";
                    initSpinnerContent(list_team,adapter_team,employeeInfos,spinner_team,0x32);
                    initSpinnerContent(list_person,adapter_person,employeeInfos,spinner_person,0x33);
                }else{
                    mStrDepartment = spinner_department.getSelectedItem().toString();
                    employInfoRequest("getgroup",mStrDepartment,null,null,mEmployeeInfoResultObserver);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner_team.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mStrName ="全部";
                if (spinner_team.getSelectedItem().toString().equals("全部".toString())){
                    mStrGroup = "全部";
                    initSpinnerContent(list_person,adapter_person,employeeInfos,spinner_person,0x33);
                }else{
                    mStrGroup = spinner_team.getSelectedItem().toString();
                    employInfoRequest("getname",mStrDepartment,mStrGroup,null,mEmployeeInfoResultObserver);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner_person.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mStrName = spinner_person.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mStrKQtype = spinner_type.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        getSpinnerdata();

        return rootView;
    }


    private void generateInitialLineData() {
        int numValues = 31;


        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<PointValue> values = new ArrayList<PointValue>();
        for (int i = 0; i < numValues; ++i) {
            values.add(new PointValue(i, 0));
            axisValues.add(new AxisValue(i).setLabel(days[i]));
        }

        Line line = new Line(values);
        line.setColor(ChartUtils.COLOR_GREEN).setCubic(true);

        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        lineData = new LineChartData(lines);
        Axis axisBottom = new Axis(axisValues);
        axisBottom.setHasLines(true);
        axisBottom.setTextColor(Color.rgb(0, 0, 0));
        lineData.setAxisXBottom(axisBottom);
        Axis axisLeft = new Axis();
        axisLeft.setHasLines(true);
        axisLeft.setMaxLabelChars(3);
        axisLeft.setTextColor(Color.rgb(0, 0, 0));
        lineData.setAxisYLeft(axisLeft);

        chartTop.setLineChartData(lineData);

        // For build-up animation you have to disable viewport recalculation.
        chartTop.setViewportCalculationEnabled(false);

        // And set initial max viewport and current viewport- remember to set viewports after data.
        Viewport v = new Viewport(0, 110, 31, 0);
        chartTop.setMaximumViewport(v);
        chartTop.setCurrentViewport(v);

        chartTop.setValueSelectionEnabled(true);
        chartTop.setZoomType(ZoomType.HORIZONTAL);
    }

    private void employInfoRequest(String operationType, String department, String group, String name, Observer<EmployeeInfoResult> observer){

        NetWork.getEmployeeInfoRequestApi()
                .getEmployeeInfo(operationType,department,group,name,null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }


    private void employDataRequest(String operationType, String department, String group, String name,
                                   String type, String infotime, Observer<EmployeeDataResult> observer){
        NetWork.getEmployeeStatisticsDataRequestApi()
                .getEmployeeStatisticsData(operationType,department,group,name,type,infotime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }
    //设置柱状图
    private void setColumnData(List<EmployeeDataResult.EmployeeData> list){
        chartBottom.cancelDataAnimation();
        //X轴
        List<AxisValue> axisValues = new ArrayList<>();
        List<Column> columns = new ArrayList<>();
        //节点数据集合
        List<SubcolumnValue> values;
        float fMax =(float)0.0;
        float fSum;
        //获取最大值
        for (int i = 0; i< 12; i++){
            try{
                fSum = Float.parseFloat(list.get(i).getDurationsum());
            }catch (Exception e){
                fSum = (float)0.0;
            }
            if (fSum > fMax){
                fMax = fSum;
            }
            values = new ArrayList<>();
            values.add(new SubcolumnValue(fSum, ChartUtils.pickColor()));
            //设置X轴标签
            try{
                axisValues.add(new AxisValue(i).setLabel(list.get(i).getStrdate()));
            }catch (Exception e){

            }

            Column column = new Column(values);
            //设置小数
            ColumnChartValueFormatter chartValueFormatter = new SimpleColumnChartValueFormatter(1);
            column.setFormatter(chartValueFormatter);
            column.setHasLabelsOnlyForSelected(true);
            columns.add(column);
            Log.d(TAG, "setColumnData: "+fSum);

        }
        columnData = new ColumnChartData(columns);
        //x轴
        Axis axisBottom = new Axis(axisValues);
        axisBottom.setHasLines(true);
        axisBottom.setTextColor(Color.rgb(0, 0, 0));
        columnData.setAxisXBottom(axisBottom);
        //Y轴
        Axis axisLeft = new Axis();
        axisLeft.setHasLines(true);
        axisLeft.setMaxLabelChars(2);
        axisLeft.setTextColor(Color.rgb(0, 0, 0));
        columnData.setAxisYLeft(axisLeft);

        //显示
        chartBottom.setColumnChartData(columnData);

        // Set value touch listener that will trigger changes for chartTop.
        //设置点击事件
        chartBottom.setOnValueTouchListener(new ValueTouchListener());

        // Set selection mode to keep selected month column highlighted.
        chartBottom.setValueSelectionEnabled(true);

        chartBottom.setZoomType(ZoomType.HORIZONTAL);


        //根据大小切换
        Viewport v1 = new Viewport((float)-0.5, fMax+(float)10, (float)11.5, 0);
        Viewport v2 = new Viewport((float)5.5, fMax+(float)10, (float)11.5, 0);
        chartBottom.setMaximumViewport(v1);
        chartBottom.setCurrentViewport(v2);

    }

    private class ValueTouchListener implements ColumnChartOnValueSelectListener {
        @Override
        public void onValueSelected(int i, int i1, SubcolumnValue subcolumnValue) {
            lineColorValue = subcolumnValue.getColor();

            lineColorValue = subcolumnValue.getColor();
            List<AxisValue> axisValuesTemp;
            Axis axis;
            axis = columnData.getAxisXBottom();
            axisValuesTemp = axis.getValues();
            employDataRequest("getsumByDay",mStrDepartment,mStrGroup,mStrName,mStrKQtype,String.valueOf(axisValuesTemp.get(i).getLabel()),mEmployeeDataResultObserver);
        }

        @Override
        public void onValueDeselected() {
            lineColorValue = ChartUtils.COLOR_GREEN;
            setLineData(null);
        }
    }
    //获取考勤人员信息
    private void getSpinnerdata(){
        employInfoRequest("getdepartment",null,null,null,mEmployeeInfoResultObserver);
        Log.d(TAG, "getSpinnerdata: ");

    }
    //初始化spinner信息
    private void initSpinnerContent(List<String> list, ArrayAdapter<String> adapter, List<EmployeeInfoResult.EmployeeInfo> employeeInfoResults, Spinner spinner, int type){
        list.clear();
        list.add("全部");
        //根据返回类型设置值
        if (employeeInfoResults != null)
        {
            if (type == 0x31){
                for(int i = 0; i < employeeInfoResults.size(); i++){
                    list.add(employeeInfoResults.get(i).getDepartment());
                }
            }else if (type == 0x32){
                for(int i = 0; i < employeeInfoResults.size(); i++){
                    list.add(employeeInfoResults.get(i).getGroup());
                }
            }else if (type == 0x33){
                for(int i = 0; i < employeeInfoResults.size(); i++){
                    list.add(employeeInfoResults.get(i).getName());
                }
            }
        }

        employeeInfoResults.clear();
        adapter = new ArrayAdapter<>(rootView.getContext(),R.layout.support_simple_spinner_dropdown_item,list);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


    }
    //获取柱状图数据
    private void getColumnData(){
        employDataRequest("getsumByMonth",mStrDepartment,mStrGroup,mStrName,mStrKQtype,null,mEmployeeDataResultObserver);
        Log.d(TAG, "getColumnData: ");
    }
    //设置折线图数据
    private void setLineData(List<EmployeeDataResult.EmployeeData> list){
        chartTop.cancelDataAnimation();
        int numValues;
        //设置横坐标标签
        if (list == null){
            numValues = 31;
        }else{
            numValues = list.size();
        }

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        for (int i = 0; i < numValues; ++i) {
            axisValues.add(new AxisValue(i).setLabel(days[i]));
        }
        //获取最大值
        float fMax =(float)0.0;
        float fSum;

        //只有一条折线
        Line line = lineData.getLines().get(0);
        line.setColor(lineColorValue);
        for (int i = 0; i < numValues; i ++){
            try {
                fSum = Float.parseFloat(list.get(i).getDurationsum());
            }catch (Exception e){
                fSum = (float)0.0;
            }

            if(fSum>fMax){
                fMax = fSum;
            }
            line.getValues().get(i).setTarget(line.getValues().get(i).getX(),fSum);
            LineChartValueFormatter chartValueFormatter = new SimpleLineChartValueFormatter(2);
            line.setFormatter(chartValueFormatter);
            line.setHasLabelsOnlyForSelected(true);
        }
        Viewport v = new Viewport(0, fMax+(float)10.0, numValues-1, 0);
        chartTop.setMaximumViewport(v);
        chartTop.setCurrentViewport(v);
        // Start new data animation with 300ms duration;
        chartTop.startDataAnimation(300);
    }



}
