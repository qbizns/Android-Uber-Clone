package com.tatx.userapp.menuactivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.tatx.userapp.R;
import com.tatx.userapp.abstractclasses.BaseActivity;
import com.tatx.userapp.activities.ExpandableListAdapter;
import com.tatx.userapp.activities.ExpandableListDataPump;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;
import com.tatx.userapp.constants.ServiceUrls;
import com.tatx.userapp.network.RetrofitRequester;
import com.tatx.userapp.network.RetrofitResponseListener;
import com.tatx.userapp.pojos.ApiResponseVo;
import com.tatx.userapp.pojos.Faq;
import com.tatx.userapp.pojos.GetFaqsVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Home on 17-05-2016.
 */
public class HelpActivity extends BaseActivity implements RetrofitResponseListener{


    @BindView(R.id.submit) Button submit;
    @BindView(R.id.helpqs) EditText helpqs;

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    HashMap<String, List<String>> expandableListDetail;

    List<String> expandableListTitle1;
    HashMap<String, List<String>> expandableListDetail1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.help);

        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.help));


        initilaizedAll();

    }

    public void expandableListview() {
        expandableListTitle1 = new ArrayList<String>(expandableListDetail1.keySet());
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListDetail = ExpandableListDataPump.getData();
        expandableListAdapter = new ExpandableListAdapter(this, expandableListTitle1, expandableListDetail1);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListTitle1.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListTitle1.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        expandableListTitle1.get(groupPosition)
                                + " -> "
                                + expandableListDetail1.get(
                                expandableListTitle1.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                )
                        .show();
                return false;
            }
        });

    }


    public void initilaizedAll() {
        expandableListDetail1=new HashMap<String, List<String>>();


        HashMap<String,String>  hashMap=new HashMap();
        hashMap.put(ServiceUrls.RequestParams.ROLE, Constants.CUSROMER);
        hashMap.put(ServiceUrls.RequestParams.TYPE, ServiceUrls.RequestNames.FAQ);

        new RetrofitRequester(HelpActivity.this).sendStringRequest(ServiceUrls.RequestNames.FAQ,hashMap);




    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }





    @OnClick(R.id.submit) void sendFeedback(){
        if (helpqs.length() >4) {
            HashMap<String,String>  hashMap=new HashMap();
            hashMap.put(ServiceUrls.RequestParams.MESSAGE, helpqs.getText().toString().trim());
            hashMap.put(ServiceUrls.RequestParams.TYPE, Constants.CONTACT_US);
            hashMap.put(ServiceUrls.RequestParams.ROLE, Constants.CUSROMER);

            new RetrofitRequester(HelpActivity.this).sendStringRequest(ServiceUrls.RequestNames.FEEDBACK,hashMap);

        }else {
            Common.customToast(HelpActivity.this,Common.getStringResourceText(R.string.please_enter_minimum_5_characters),Common.TOAST_TIME);
        }
    }

    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId) {
        if (apiResponseVo.code != Constants.SUCCESS)
        {
            Common.customToast(this, apiResponseVo.status);
            return;
        }

        switch (apiResponseVo.requestname) {

            case ServiceUrls.RequestNames.FAQ:

                GetFaqsVo getFaqsVo = Common.getSpecificDataObject(apiResponseVo.data, GetFaqsVo.class);

                for (Faq faq:getFaqsVo.faq){
                    expandableListTitle1= new ArrayList<>();
                    expandableListTitle1.add(faq.answer);
                    expandableListDetail1.put(faq.question,expandableListTitle1);
                }
                expandableListview();
                break;
            case ServiceUrls.RequestNames.FEEDBACK:

                Common.customToast(this, apiResponseVo.status);
                finish();

                break;
        }
    }
}
