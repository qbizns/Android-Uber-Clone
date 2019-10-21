package com.tatx.partnerapp.menuactivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.tatx.partnerapp.R;
import com.tatx.partnerapp.abstractclasses.BaseActivity;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.constants.ServiceUrls;
import com.tatx.partnerapp.network.RetrofitRequester;
import com.tatx.partnerapp.network.RetrofitResponseListener;
import com.tatx.partnerapp.pojos.ApiResponseVo;
import com.tatx.partnerapp.pojos.DriverEarn;
import com.tatx.partnerapp.pojos.DriverEarningVo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EarningsActivity extends BaseActivity implements RetrofitResponseListener, OnChartValueSelectedListener
{

    protected BarChart mChart;

    protected String[] weeks = new String[]{"M", "TU", "W", "TH", "F", "SA", "SU"};

    private Calendar startCalendarInstance;

    private Calendar endCalendarInstance;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @BindView(R.id.tv_week_date) TextView tvWeekDate;

    @BindView(R.id.ctv_total_amount) TextView ctvTotalAmount;

    @BindView(R.id.last_paid) TextView tvLastPaid;

    @BindView(R.id.due_amount) TextView tvDueAmount;

    @BindView(R.id.ctv_last_paid_date) TextView ctvLastPaidDate;

    @BindView(R.id.ctv_last_paid_currency) TextView ctvLastPaidCurrency;

    @BindView(R.id.ctv_due_amount_currency) TextView ctvDueAmountCurrency;

    private float tripTotalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_earnings);

        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.earnings));

        startCalendarInstance = Calendar.getInstance();

        startCalendarInstance.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        endCalendarInstance = Calendar.getInstance();

        endCalendarInstance.setTime(startCalendarInstance.getTime());

        endCalendarInstance.add(Calendar.DAY_OF_MONTH, 6);

        getWeekDataFromServer(startCalendarInstance, endCalendarInstance, Constants.RequestCodes.ONCREATE_REQUEST_CODE);

        mChart = (BarChart) findViewById(R.id.gbc_earnings);

        mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawBarShadow(false);

        mChart.setDrawValueAboveBar(true);

        mChart.setDescription(null);

        mChart.setPinchZoom(false);

        mChart.setScaleEnabled(false);

        mChart.setDrawGridBackground(false);




        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
//        xAxis.setLabelCount(7);

        xAxis.setValueFormatter(new IAxisValueFormatter()
        {
            @Override
            public String getFormattedValue(float value, AxisBase axis)
            {
                Common.Log.i("? - value : "+value);

                Common.Log.i("? - axis : "+axis);

                return weeks[(int) value];
            }


        });







        YAxis leftAxis = mChart.getAxisLeft();
//        leftAxis.setLabelCount(8, false);
        leftAxis.setDrawLabels(true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0);

        leftAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Common.Log.i("? - value : Y : "+value);

                Common.Log.i("? - axis :  Y : "+axis);

                return String.valueOf((int)value);
            }
        });


        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);




        mChart.getAxisLeft().setTextColor(Common.getColorFromResource(R.color.white));
        mChart.getAxisLeft().setTextSize(10f);

        mChart.getXAxis().setTextColor(Common.getColorFromResource(R.color.white));
        mChart.getXAxis().setTextSize(10f);

        mChart.getLegend().setEnabled(false);



    }



    private ArrayList<String> getWeekDates(Calendar startDateCalendar)
    {


        Calendar tempCalendarInstance = Calendar.getInstance();

        tempCalendarInstance.setTime(startDateCalendar.getTime());

        ArrayList<String> arrayList = new ArrayList<String>();

        for (int i = 0; i < 7; i++) {
            Log.i("dateTag", sdf.format(tempCalendarInstance.getTime()));

            arrayList.add(sdf.format(tempCalendarInstance.getTime()));

            tempCalendarInstance.add(Calendar.DAY_OF_WEEK, 1);
        }


        return arrayList;


    }

    private void getWeekDataFromServer(Calendar startCalendar, Calendar endCalendar, int requestId)
    {


        HashMap<String, String> params = new HashMap<>();

        params.put("start_date", sdf.format(startCalendar.getTime()));

        params.put("end_date", sdf.format(endCalendar.getTime()));


        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.DRIVER_EARNING, params, requestId);


    }


    public void onClick(View view)
    {

        switch (view.getId())
        {

            case R.id.iv_previous:

                backwardOneWeek();
                getWeekDataFromServer(startCalendarInstance, endCalendarInstance, view.getId());

                break;

            case R.id.iv_next:

                forwardOneWeek();

                getWeekDataFromServer(startCalendarInstance, endCalendarInstance, view.getId());

                break;




        }

    }

    private void backwardOneWeek() {
        startCalendarInstance.add(Calendar.DAY_OF_MONTH, -7);
        endCalendarInstance.add(Calendar.DAY_OF_MONTH, -7);

    }

    private void forwardOneWeek() {

        startCalendarInstance.add(Calendar.DAY_OF_MONTH, 7);
        endCalendarInstance.add(Calendar.DAY_OF_MONTH, 7);

    }



    private void populateBarData(int count, HashMap<String, Float> barDataAl)
    {


        ArrayList<BarEntry> barEntryArrayList = new ArrayList<BarEntry>();

        ArrayList<String> weekDatesAl = getWeekDates(startCalendarInstance);

        Common.Log.i("weekDatesAl.toString() : " + weekDatesAl.toString());


        for (int i = 0; i < weekDatesAl.size(); i++)
        {

            String weekDate = weekDatesAl.get(i);

            Float amountValue = barDataAl.get(weekDate);

            amountValue = amountValue != null ? amountValue : 0;


            tripTotalAmount = tripTotalAmount + amountValue;


            Common.Log.i("amountValue : " + amountValue);


            barEntryArrayList.add(new BarEntry(i, amountValue));


/*

            barEntryArrayList.add(new BarEntry(0, 80));
            barEntryArrayList.add(new BarEntry(1, 180));
            barEntryArrayList.add(new BarEntry(2, 80));
            barEntryArrayList.add(new BarEntry(3, 800));
            barEntryArrayList.add(new BarEntry(4, 110));
            barEntryArrayList.add(new BarEntry(5, 2000));
            barEntryArrayList.add(new BarEntry(6, 1000));
*/

        }




        BarDataSet barDataSet = new BarDataSet(barEntryArrayList, "The year 2017");
        barDataSet.setColor(rgb("#7FD323"));


        ArrayList<IBarDataSet> dataSets1 = new ArrayList<IBarDataSet>();

        dataSets1.add(barDataSet);

        BarData barData = new BarData(dataSets1);

        barData.setValueTextSize(10f);

        barData.setBarWidth(0.9f);

        mChart.setData(barData);

//        mChart.getBarData().setValueTextColor(Color.parseColor("#ffffff"));





    }


    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId) {


        Common.Log.i("getClass().getSimpleName() : " + getClass().getSimpleName());


        if (apiResponseVo.code != Constants.SUCCESS) {
            Common.customToast(this, apiResponseVo.status);

            switch (requestId) {
                case R.id.iv_next:
                    backwardOneWeek();
                    break;

                case R.id.iv_previous:
                    forwardOneWeek();
                    break;

            }


            return;
        }




        switch (apiResponseVo.requestname)
        {

            case ServiceUrls.RequestNames.DRIVER_EARNING:



                HashMap<String, Float> driverEarnHashMap = new HashMap<String, Float>();


                DriverEarningVo driverEarningVo = Common.getSpecificDataObject(apiResponseVo.data, DriverEarningVo.class);

                for (DriverEarn driverEarn : driverEarningVo.driverEarn)
                {

                    driverEarnHashMap.put(driverEarn.date, Float.valueOf(driverEarn.amount));

                }


                populateBarData(weeks.length, driverEarnHashMap);


                mChart.getBarData().setValueTextColor(Color.parseColor("#ffffff"));

                mChart.invalidate();

                String dateText = startCalendarInstance.get(Calendar.DAY_OF_MONTH) + " " + String.format(Locale.US, "%tB", startCalendarInstance) + "   -   " + endCalendarInstance.get(Calendar.DAY_OF_MONTH) + " " + String.format(Locale.US, "%tB", endCalendarInstance);
                tvWeekDate.setText(dateText);

                String totalAmount = Common.getTwoDecimalRoundValueString(tripTotalAmount + (driverEarningVo.bonus != null ? Double.parseDouble(driverEarningVo.bonus) : 0));

                ctvTotalAmount.setText(driverEarningVo.currency + "   " + String.valueOf(totalAmount));


                ctvDueAmountCurrency.setText(driverEarningVo.currency);

                tvDueAmount.setText(String.valueOf(driverEarningVo.dueAmount));


                ctvLastPaidCurrency.setText(driverEarningVo.currency);

                tvLastPaid.setText(String.valueOf(driverEarningVo.lastPaid));

                ctvLastPaidDate.setText(Common.convertDate_yyyy_MM_dd_TO_dd_MM_yyyy(driverEarningVo.lastPaidDate));

                tripTotalAmount = 0;




                break;


        }


    }




    @Override
    public void onValueSelected(Entry e, Highlight h)
    {

        if (e == null)
            return;

        Log.i("Activity", "Selected: " + e.toString() + ", dataSet: " + h.getDataSetIndex());

    }

    @Override
    public void onNothingSelected()
    {

    }


    public static int rgb(String hex) {
        int color = (int) Long.parseLong(hex.replace("#", ""), 16);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        return Color.rgb(r, g, b);
    }


}
