package com.tatx.partnerapp.menuactivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.abstractclasses.BaseActivity;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.constants.ServiceUrls;
import com.tatx.partnerapp.fragments.AllHoursFragment;
import com.tatx.partnerapp.fragments.AvgHoursFragment;
import com.tatx.partnerapp.fragments.TodayHoursFragment;
import com.tatx.partnerapp.network.RetrofitRequester;
import com.tatx.partnerapp.network.RetrofitResponseListener;
import com.tatx.partnerapp.pojos.ApiResponseVo;
import com.tatx.partnerapp.pojos.OnlineHoursVo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class OnlineHoursActivity extends BaseActivity implements RetrofitResponseListener{

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private static final int DATE_PICKER_ID_FROM = 100;
    private static final int DATE_PICKER_ID_TO = 101;
    @BindView(R.id.from_date)
    TextView fromDate;
    @BindView(R.id.tv_to_date)
    TextView toDate;
    @BindView(R.id.search)
    RelativeLayout search;
    @BindView(R.id.tv_today_date)
    TextView tvTodayDate;
    @BindView(R.id.rl_today)
    RelativeLayout rlToday;
    @BindView(R.id.linear_layout1)
    LinearLayout linearLayout1Search;

    private int year;
    private int month;
    private int day;
    private TextView copyDate;
    private OnlineHoursVo onlineHoursVo=new OnlineHoursVo();
    private ProgressDialog progressDialog;
    private List<ViewsTvIv1> viewsList1=new ArrayList<>();
    int index=0;
    private int tabPosition;
    private int requestIdSearch=-1;
    private String TAG="Anil";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_hours);

        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.online_hours));


        Log.d("customViewIconText","ddfdf onlineHoursActivity 1");
        initilaiedAll();

    }

    public void initilaiedAll(){



        viewsList1.add(0,new ViewsTvIv1(0,R.drawable.all_hours_black_icon,R.drawable.all_hours_red_icon,Common.getStringFromResources(R.string.all_hours),(TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null)));
        viewsList1.add(1,new ViewsTvIv1(1,R.drawable.avg_hours_black_icon,R.drawable.avg_hours_red_icon,Common.getStringFromResources(R.string.average_hours),(TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null)));
        viewsList1.add(2,new ViewsTvIv1(2,R.drawable.today_hours_black_icon,R.drawable.today_hours_red_icon,Common.getStringFromResources(R.string.today_hours),(TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null)));

        createCalenarInstance();
        String from_date = Common.getCalculatedDate("yyyy-MM-dd", -7);
        int months = month + 1;
        String currentDate = year + "-" + months + "-" + day;

        toDate.setText(currentDate);
        fromDate.setText(from_date);
        tvTodayDate.setText(currentDate);

        getOnlieHoursRequest(from_date,currentDate);






    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    /**
     * Adding custom view to tab
     */
    private void setupTabIcons(int tabPosition,List<ViewsTvIv1> viewsTvIv1s) {
        for (ViewsTvIv1 viewsTvIv1:viewsTvIv1s){
            Log.d("viewsTvIv1.getId()", String.valueOf(viewsTvIv1.getId()));
            if (tabPosition==viewsTvIv1.getId()) {
                viewsTvIv1.getTextView().setTextColor(Common.getColorFromResource(R.color.button_color));
                viewsTvIv1.getTextView().setText(viewsTvIv1.getTabName());
                viewsTvIv1.getTextView().setCompoundDrawablesWithIntrinsicBounds(0, viewsTvIv1.getDrawableRedId(), 0, 0);
                tabLayout.getTabAt(viewsTvIv1.getId()).setCustomView( viewsTvIv1.getTextView());
            }else{
                viewsTvIv1.getTextView().setTextColor(Common.getColorFromResource(R.color.black));
                viewsTvIv1.getTextView().setText(viewsTvIv1.getTabName());
                viewsTvIv1.getTextView().setCompoundDrawablesWithIntrinsicBounds(0, viewsTvIv1.getDrawableBlackId(), 0, 0);
                tabLayout.getTabAt(viewsTvIv1.getId()).setCustomView( viewsTvIv1.getTextView());

            }
        }
/*
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("ONE");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.all_hours_black_icon, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("TWO");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.all_hours_black_icon, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("THREE");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.all_hours_black_icon, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);*/
    }

    /**
     * Adding fragments to ViewPager
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new AllHoursFragment(), Common.getStringFromResources(R.string.all_hours));
        adapter.addFrag(new AvgHoursFragment(), Common.getStringFromResources(R.string.average_hours));
        adapter.addFrag(new TodayHoursFragment(), Common.getStringFromResources(R.string.today_hours));
        viewPager.setAdapter(adapter);

    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void getOnlieHoursRequest(String from_date,String currentDate){
        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(ServiceUrls.ApiRequestParams.FROM_DATE, from_date);
        hashMap.put(ServiceUrls.ApiRequestParams.TO_DATE,  currentDate);
        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.GET_ONLINE_HOURS, hashMap);
    }

    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId) {
        Log.d("requestId", String.valueOf(requestId));
        if (apiResponseVo.code != Constants.SUCCESS)
        {
            Common.customToast(this, apiResponseVo.status);
            return;
        }


        switch (apiResponseVo.requestname) {

            case ServiceUrls.RequestNames.GET_ONLINE_HOURS:
                onlineHoursVo = Common.getSpecificDataObject(apiResponseVo.data, OnlineHoursVo.class);

                viewPager = (ViewPager) findViewById(R.id.viewpager);
                Log.d("tabselected", "requestId "+String.valueOf(requestId)+"R.id.search"+R.id.search);
                setupViewPager(viewPager);

                tabLayout = (TabLayout) findViewById(R.id.tabs);
                tabLayout.setupWithViewPager(viewPager);
                setupTabIcons(0,viewsList1);
                viewPager.setCurrentItem(0);
                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

                tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        Log.d("tabselected", String.valueOf(tab.getPosition()));
                        tabPosition=tab.getPosition();
                        setupTabIcons(tabPosition,viewsList1);
                        if (tabPosition==2){
                            linearLayout1Search.setVisibility(View.GONE);
                            rlToday.setVisibility(View.VISIBLE);
                        }else {
                            linearLayout1Search.setVisibility(View.VISIBLE);
                            rlToday.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);

                    }
                });

//                tabLayout.setupWithViewPager(viewPager);
//                setupTabIcons(0,viewsList1);




                break;
        }
    }


    @OnClick(R.id.from_date) void setFromDate(){

        // createCalenarInstance();
        Calendar c=Common.getCalculatedDate(-7);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        Log.d("date","day "+day+" month "+month+" year "+year);
        copyDate = fromDate;

        showDialog(DATE_PICKER_ID_FROM);
    }
    @OnClick(R.id.tv_to_date) void setToDate(){

        createCalenarInstance();

        copyDate = toDate;

        showDialog(DATE_PICKER_ID_TO);
    }

    @OnClick(R.id.search) void setSearch(){
             requestIdSearch=R.id.search;
        getOnlieHoursRequest(fromDate.getText().toString(),toDate.getText().toString());
    }

    public void createCalenarInstance() {
        Calendar c = Calendar.getInstance(new Locale("en"));
        c.add(Calendar.DATE, 0);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
    }


    @Override

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID_TO:

                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date picker
                DatePickerDialog pickerDialog = new DatePickerDialog(this, pickerListener, year, month, day);
                // pickerDialog.getDatePicker().setMinDate(new Date().getTime() - 10000);
                pickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                return pickerDialog;
            case DATE_PICKER_ID_FROM:
                DatePickerDialog pickerDialog1 = new DatePickerDialog(this, pickerListener, year, month, day);
                //pickerDialog1.getDatePicker().setMinDate(new Date().getTime() - 10000);
                pickerDialog1.getDatePicker().setMaxDate(System.currentTimeMillis());

                return pickerDialog1;

        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // Show selected date
            copyDate.setText(new StringBuilder().append(pad(year)).append("-").append(pad(month + 1)).append("-").append(pad(day)));



        }
    };

    /************************
     * date formate 01/02/2015*******Used 0 before value<10
     ********************/
    static String pad(int c) {
        if (c >= 10) {
            return String.valueOf(c);
        } else {
            return '0' + String.valueOf(c);
        }
    }

public OnlineHoursVo getOnlineHoursVo(){

    return onlineHoursVo;
}

    public String getDates(){

        return fromDate.getText().toString()+" to "+toDate.getText().toString();
    }

    public String getTodayDates(){

        return toDate.getText().toString();
    }
    public void refreshFragment(ViewPager viewPager)
    {
        int index = viewPager.getCurrentItem();
        ViewPagerAdapter adapter1 = ((ViewPagerAdapter)viewPager.getAdapter());
        Fragment fragment = adapter1.getItem(index);

        android.support.v4.app.FragmentManager fragmentMg = this.getSupportFragmentManager();
        FragmentTransaction fragmentTrans = fragmentMg.beginTransaction();
        fragmentTrans.replace(R.id.viewpager, fragment, "TABBILLING");
        fragmentTrans.addToBackStack(null);
        fragmentTrans.commit();

        //adapter.notifyDataSetChanged();

    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        Log.i(TAG, "onstart");

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Log.i(TAG, "onresume");

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        Log.i(TAG, "onpause");
       // moveTaskToBack(false);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        Log.i(TAG, "onstop");
       // moveTaskToBack(true);

      // finish();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        Log.i(TAG, "ondestroy");
        //finish();
    }
}
