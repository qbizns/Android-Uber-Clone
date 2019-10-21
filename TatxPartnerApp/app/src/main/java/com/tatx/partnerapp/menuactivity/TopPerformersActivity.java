package com.tatx.partnerapp.menuactivity;

import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;
import com.tatx.partnerapp.R;
import com.tatx.partnerapp.abstractclasses.DemoBase;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.pojos.GetTopPerformersDetails;
import com.tatx.partnerapp.pojos.Week;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by user on 20-05-2016.
 */

public class TopPerformersActivity extends DemoBase implements OnChartValueSelectedListener
{

    private static final double REVENUE_GRACE = 5000;
    private static final double TRIPS_GRACE = 20;
    private static final double MAXIMUM_AVG_RATING = 5;
    private static final double MAXIMUM_ACCEPTANCE_PERCENTAGE = 100;
    private static final double ONLINE_HOURS_GRACE = 8;
    private BarChart bcGroupedBarChart;
    private String xAxisLabels[] = {"Revenue","Trips","Rating","Accepatance","Online"};
    private GetTopPerformersDetails getTopPerformersDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_top_performers);

        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.performance_analytics));

        getTopPerformersDetails = (GetTopPerformersDetails) getIntent().getSerializableExtra(Constants.KEY_1);


        bcGroupedBarChart = (BarChart) findViewById(R.id.bc_grouped_bar_chart);

        bcGroupedBarChart.setDrawBarShadow(false);
        bcGroupedBarChart.setDrawValueAboveBar(true);
        bcGroupedBarChart.setDescription(null);
        bcGroupedBarChart.setPinchZoom(false);
        bcGroupedBarChart.setScaleEnabled(false);
        bcGroupedBarChart.setDrawGridBackground(false);
        bcGroupedBarChart.setFitBars(true);
        bcGroupedBarChart.getLegend().setEnabled(false);
        bcGroupedBarChart.getXAxis().setEnabled(false);
        bcGroupedBarChart.getAxisLeft().setEnabled(false);
        bcGroupedBarChart.getAxisRight().setEnabled(false);
        bcGroupedBarChart.setViewPortOffsets(0f, 0f, 0f, 0f);
        bcGroupedBarChart.setBackgroundColor(Common.getColorFromResource(R.color.white));
        bcGroupedBarChart.getAxisRight().setAxisMinimum(0);


        XAxis xAxis = bcGroupedBarChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum(xAxisLabels.length);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);


        YAxis leftAxisLeft = bcGroupedBarChart.getAxisLeft();
        leftAxisLeft.setDrawGridLines(false);
        leftAxisLeft.setSpaceTop(30f);
        leftAxisLeft.setAxisMinimum(0f);



        showWeekPerformanceGraph(getTopPerformersDetails.week);


    }







    private double convertToCentPercentValue(double actualValue, double maximumValue)
    {
        return (100*actualValue)/maximumValue;
    }










    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Activity", "Selected: " + e.toString() + ", dataSet: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Activity", "Nothing selected.");
    }


    @OnClick(R.id.ll_week)
    void showWeekPerformanceGraph()
    {
        Common.Log.i("showWeekPerformanceGraph().");
        showWeekPerformanceGraph(getTopPerformersDetails.week);
    }


    @OnClick(R.id.ll_all)
    void showAllPerformanceGraph()
    {
        Common.Log.i("showAllPerformanceGraph().");

        Week week = new Week();


        Common.copyObject(getTopPerformersDetails.all,week);

        showWeekPerformanceGraph(week);
    }


    private void showWeekPerformanceGraph(Week week)
    {



        //data
        float groupSpace = 0.04f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.46f; // x2 dataset
        // (0.46 + 0.02) * 2 + 0.04 = 1.00 -> interval per "group"

        int startYear = 0;
        int endYear = xAxisLabels.length;


        List<BarEntry> yVals1 = new ArrayList<BarEntry>();
        List<BarEntry> yVals2 = new ArrayList<BarEntry>();


        Week weekData = week;

        Common.Log.i("? - Before weekData : "+new Gson().toJson(weekData));


        double MAXIMUM_REVENUE = (weekData.topPerformersRevenue > weekData.driverRevenue ? weekData.topPerformersRevenue : weekData.driverRevenue) + REVENUE_GRACE;
        double MAXIMUM_TRIPS = (weekData.topPerformersNumberOfTrips > weekData.driverNumberOfTrips ? weekData.topPerformersNumberOfTrips : weekData.driverNumberOfTrips) + TRIPS_GRACE;
        double MAXIMUM_ONLINE_HOURS = (weekData.topPerformersOnlineHours > weekData.driverOnlineHours ? weekData.topPerformersOnlineHours : weekData.driverOnlineHours) + ONLINE_HOURS_GRACE;

        final Double driverYData[] = {weekData.driverRevenue, weekData.driverAvgRating, weekData.driverNumberOfTrips,weekData.driverAvgAcceptancePercentage,weekData.driverOnlineHours};

        double driverRevenueInPercentage = convertToCentPercentValue(weekData.driverRevenue, MAXIMUM_REVENUE);

       double driverAvgRatingInPercentage = convertToCentPercentValue(weekData.driverAvgRating,MAXIMUM_AVG_RATING);

       double driverNumberOfTripsInPercentage = convertToCentPercentValue(weekData.driverNumberOfTrips,MAXIMUM_TRIPS);

       double driverAvgAcceptancePercentageInPercentage = convertToCentPercentValue(weekData.driverAvgAcceptancePercentage,MAXIMUM_ACCEPTANCE_PERCENTAGE);

       double driverOnlineHoursInPercentage = convertToCentPercentValue(weekData.driverOnlineHours,MAXIMUM_ONLINE_HOURS);

        Double driverYDataInPercentage[] = {driverRevenueInPercentage, driverAvgRatingInPercentage, driverNumberOfTripsInPercentage,driverAvgAcceptancePercentageInPercentage,driverOnlineHoursInPercentage};



        for (int i = startYear; i < endYear; i++)
        {

            BarEntry barEntry = new BarEntry(i, driverYDataInPercentage[i].floatValue());


            Common.Log.i("????? - driverYData[i].floatValue() :  "+driverYData[i].floatValue());
            Common.Log.i("????? - driverYDataInPercentage[i].floatValue() :  "+driverYDataInPercentage[i].floatValue());

            yVals1.add(barEntry);

        }


        final Double topPerformersYData[] = {weekData.topPerformersRevenue,weekData.topPerformersAvgRating, weekData.topPerformersNumberOfTrips,weekData.topPerformersAvgAcceptancePercentage,weekData.topPerformersOnlineHours};

        double topPerformersRevenueInPercentage = convertToCentPercentValue(weekData.topPerformersRevenue,MAXIMUM_REVENUE);

        double topPerformersAvgRatingInPercentage = convertToCentPercentValue(weekData.topPerformersAvgRating,MAXIMUM_AVG_RATING);

        double topPerformersNumberOfTripsInPercentage = convertToCentPercentValue(weekData.topPerformersNumberOfTrips,MAXIMUM_TRIPS);

        double topPerformersAvgAcceptancePercentageInPercentage = convertToCentPercentValue(weekData.topPerformersAvgAcceptancePercentage,MAXIMUM_ACCEPTANCE_PERCENTAGE);

        double topPerformersOnlineHoursInPercentage = convertToCentPercentValue(weekData.topPerformersOnlineHours,MAXIMUM_ONLINE_HOURS);

        Double topPerformersYDataInPercentage[] = {topPerformersRevenueInPercentage,topPerformersAvgRatingInPercentage, topPerformersNumberOfTripsInPercentage,topPerformersAvgAcceptancePercentageInPercentage,topPerformersOnlineHoursInPercentage};


        for (int j = startYear; j < endYear; j++)
        {

            yVals2.add(new BarEntry(j, topPerformersYDataInPercentage[j].floatValue()));

        }


        Common.Log.i("? - After weekData : "+new Gson().toJson(weekData));



        BarDataSet set1, set2;


        Common.Log.i("? - bcGroupedBarChart.getData() : "+bcGroupedBarChart.getData());


        if (bcGroupedBarChart.getData() != null) {
            Common.Log.i("? - bcGroupedBarChart.getData().getDataSetCount() : "+bcGroupedBarChart.getData().getDataSetCount() );
        }


        if (bcGroupedBarChart.getData() != null && bcGroupedBarChart.getData().getDataSetCount() > 0)
        {

            set1 = (BarDataSet)bcGroupedBarChart.getData().getDataSetByIndex(0);

            set2 = (BarDataSet)bcGroupedBarChart.getData().getDataSetByIndex(1);


            set1.setValues(yVals1);

            set2.setValues(yVals2);


            bcGroupedBarChart.getData().notifyDataChanged();

            bcGroupedBarChart.notifyDataSetChanged();

        }
        else
        {

            // create 2 datasets with different types
            set1 = new BarDataSet(yVals1, "You (Driver Name)");

            set1.setColor(Common.getColorFromResource(R.color.dark_purple));

            set2 = new BarDataSet(yVals2, "Top Performers Average");

            set2.setColor(Common.getColorFromResource(R.color.dark_yellow));


            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            dataSets.add(set2);

            BarData barData = new BarData(dataSets);

            bcGroupedBarChart.setData(barData);




        }



        set1.setValueFormatter(new IValueFormatter()
        {


            int index;

            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler)
            {

                Common.Log.i("dataSetIndex : "+dataSetIndex);

                Common.Log.i("value : "+value);

                Common.Log.i("entry set1 : "+entry);

                Common.Log.i("????? - index : "+ index);

                Common.Log.i("????? - xAxisLabels.length : "+ xAxisLabels.length);


                if (index >= xAxisLabels.length)
                {
                    index = 0;

                }


                return String.valueOf(driverYData[index++]);




            }
        });


        set2.setValueFormatter(new IValueFormatter()
        {

            int index;

            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler)
            {

                Common.Log.i("entry set2 : "+entry);


                if (index >= xAxisLabels.length)
                {
                    index = 0;

                }


                return String.valueOf(topPerformersYData[index++]);


            }

        });


        bcGroupedBarChart.getBarData().setBarWidth(barWidth);
        bcGroupedBarChart.getXAxis().setAxisMinimum(startYear);
        bcGroupedBarChart.groupBars(startYear, groupSpace, barSpace);

        bcGroupedBarChart.invalidate();



    }








}
