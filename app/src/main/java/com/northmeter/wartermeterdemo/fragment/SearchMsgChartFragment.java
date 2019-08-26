package com.northmeter.wartermeterdemo.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.utils.LoggerUtil;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author zz
 * @time 2016/08/26 15:01
 * @des 用水账单统计图的fragment
 */
public class SearchMsgChartFragment extends Fragment implements OnChartDataCallback {

    @BindView(R.id.linechart_search_msg)
    LineChart mLineChartSearchMsg;
    @BindView(R.id.barchart_search_msg)
    BarChart mBarchartSearchMsg;

    //    private ArrayList<String> moneyArr = new ArrayList<>();
    private BarData mBarData;
    private ArrayList<String> mMoneyArr;
    private ArrayList<String> mUseValue;
    private ArrayList<String> mMonthArr;
    private int mWidth;
    private int mHeight;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_msg_chart, container, false);
        unbinder = ButterKnife.bind(this, view);

        //测量图表的宽高
        WindowManager wm = getActivity().getWindowManager();
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        mWidth = point.x;
        mHeight = point.y;

        //缴费折线图
//        LineData mLineData = getLineData(12);
        LineData mLineData = getLineData(mMonthArr.size());
        showChart(mLineChartSearchMsg, mLineData);

        //用水量柱状图
//        mBarData = getBarData(12);
        mBarData = getBarData(mMonthArr.size());
        showBarChart(mBarchartSearchMsg, mBarData);

        return view;
    }


    /**
     * 生成一个数据
     *
     * @param count 表示图表中有多少个x坐标点
     * @return
     */
    private LineData getLineData(int count) {
        ArrayList<String> xValues = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            // x轴显示的数据，这里默认使用数字下标显示
//            xValues.add(i +"月");
            xValues.add(mMonthArr.get(i));
        }


        // y轴的数据
        ArrayList<Entry> yValues = new ArrayList<>();
        for (int i = 0; i < mMoneyArr.size(); i++) {
            yValues.add(new Entry(Float.valueOf(mMoneyArr.get(i)), i));
        }

        // create a dataset and give it a type
        // y轴的数据集合
        LineDataSet lineDataSet = new LineDataSet(yValues, "月缴费折线图" /*显示在比例图上*/);
        // mLineDataSet.setFillAlpha(110);
        // mLineDataSet.setFillColor(Color.RED);

        //用y轴的集合来设置参数
        lineDataSet.setLineWidth(1.75f); // 线宽
        lineDataSet.setCircleSize(3f);// 显示的圆形大小
        lineDataSet.setColor(Color.parseColor("#20b8ed"));// 显示颜色
        lineDataSet.setCircleColor(Color.parseColor("#20b8ed"));// 圆形的颜色
        lineDataSet.setHighLightColor(Color.parseColor("#20b8ed")); // 高亮的线的颜色
        lineDataSet.setValueTextSize(12f);
        lineDataSet.setDrawCircleHole(false);

        // 改变折线样式，用曲线。
//        lineDataSet.setDrawCubic(true);//平滑的曲线
        // 曲线的平滑度，值越大越平滑。
        lineDataSet.setCubicIntensity(0.2f);

        // 填充曲线下方的区域，红色，半透明。
//        lineDataSet.setDrawFilled(true);
//        lineDataSet.setFillAlpha(128);
//        lineDataSet.setFillColor(Color.RED);

        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
        lineDataSets.add(lineDataSet); // add the datasets

        // create a data object with the datasets
        LineData lineData = new LineData(xValues, lineDataSets);

        return lineData;
    }

    // 设置显示的样式
    private void showChart(LineChart lineChart, LineData lineData) {
        lineChart.setDrawBorders(false);  //是否在折线图上添加边框

        // no description text
        lineChart.setDescription("");// 数据描述
        if (mMoneyArr.size() == 0) {

            lineChart.setDescription("没有数据可供显示");// 数据描述
            lineChart.setDescriptionPosition((float) (mWidth / (1.5)), (float) (mHeight / 4.5));
            lineChart.setDescriptionTextSize(16);
        }else {
            lineChart.setDescription("单位：元");
            lineChart.setDescriptionTextSize(14);
        }
        // 如果没有数据的时候，会显示这个，类似listview的emtpyview
//        lineChart.setNoDataTextDescription("You need to provide data for the chart.");


        // enable / disable grid background
        lineChart.setDrawGridBackground(false); // 是否显示表格颜色
        lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度

        lineChart.getAxisRight().setEnabled(false); // 隐藏右边 的坐标轴
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM); // 让x轴在下面
        lineChart.getXAxis().setGridColor(getResources().getColor(R.color.transparent));
        lineChart.getXAxis().setSpaceBetweenLabels(1);

        // enable touch gestures
        lineChart.setTouchEnabled(true); // 设置是否可以触摸

        // enable scaling and dragging
        lineChart.setDragEnabled(true);// 是否可以拖拽
        lineChart.setScaleEnabled(true);// 是否可以缩放

        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(false);//

//        lineChart.setBackgroundColor(Color.WHITE);// 设置背景

        // add data
        lineChart.setData(lineData); // 设置数据

        // get the legend (only possible after setting data)
        Legend mLegend = lineChart.getLegend(); // 设置比例图标示，就是那个一组y的value的

        // modify the legend ...
//         mLegend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
        mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
        mLegend.setFormSize(12f);// 样式大小
        mLegend.setTextColor(Color.BLACK);// 颜色
//      mLegend.setTypeface(mTf);// 字体
        mLegend.setTextSize(14f);

        lineChart.animateX(2500); // 立即执行的动画,x轴
    }

    private BarData getBarData(int count) {
        ArrayList<String> xValues = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
//            xValues.add((i + 1) + "月");
            xValues.add(mMonthArr.get(i));
        }

        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();

        for (int i = 0; i < mUseValue.size(); i++) {
            yValues.add(new BarEntry(Float.valueOf(mUseValue.get(i)), i));
        }

        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yValues, "月用水量柱状图");
        barDataSet.setColor(Color.parseColor("#20b8ed"));
        barDataSet.setValueTextSize(12f);

        ArrayList<IBarDataSet> barDataSets = new ArrayList<>();
        barDataSets.add(barDataSet); // add the datasets

        BarData barData = new BarData(xValues, barDataSets);

        return barData;
    }

    private void showBarChart(BarChart barChart, BarData barData) {
        barChart.setDrawBorders(false);  ////是否在折线图上添加边框

        if (mMonthArr.size() == 0) {
            barChart.setDescription("没有数据可供显示");// 数据描述
            barChart.setDescriptionPosition((float) (mWidth / (1.5)), (float) (mHeight / 4.5));
            barChart.setDescriptionTextSize(16);
        }else {
            barChart.setDescription("单位：㎥");
            barChart.setDescriptionTextSize(14);
        }

        // 如果没有数据的时候，会显示这个，类似ListView的EmptyView
//        barChart.setNoDataTextDescription("没有数据可供显示");

        barChart.setDrawGridBackground(false); // 是否显示表格颜色
        barChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度

        barChart.getAxisRight().setEnabled(false); // 隐藏右边 的坐标轴
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM); // 让x轴在下面
        barChart.getXAxis().setGridColor(getResources().getColor(R.color.transparent));

        barChart.setTouchEnabled(true); // 设置是否可以触摸

        barChart.setDragEnabled(true);// 是否可以拖拽
        barChart.setScaleEnabled(true);// 是否可以缩放

        barChart.setPinchZoom(false);//

//      barChart.setBackgroundColor();// 设置背景

        barChart.setDrawBarShadow(true);

        barChart.setData(barData); // 设置数据

        Legend mLegend = barChart.getLegend(); // 设置比例图标示

        mLegend.setForm(Legend.LegendForm.SQUARE);// 样式
        mLegend.setFormSize(12f);// 样式
        mLegend.setTextColor(Color.BLACK);// 颜色
        mLegend.setTextSize(14f);


        barChart.animateX(2500); // 立即执行的动画,x轴
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void setMoneyData(ArrayList<String> moneyArr) {
//        LoggerUtil.d("money--->"+money);
//        moneyArr.add(money);
//        LoggerUtil.d("Arrmoney--->"+money.size());
        Collections.reverse(moneyArr);
        this.mMoneyArr = moneyArr;
    }

    @Override
    public void setUseValueData(ArrayList<String> useValue) {
//        LoggerUtil.d("useValue--->"+useValue);
        Collections.reverse(useValue);
        this.mUseValue = useValue;
    }

    @Override
    public void setMonthData(ArrayList<String> month) {
        Collections.reverse(month);
        this.mMonthArr = month;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
}
