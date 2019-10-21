package com.tatx.partnerapp.menuactivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.abstractclasses.BaseActivity;
import com.tatx.partnerapp.activities.ExpandableListAdapter;
import com.tatx.partnerapp.activities.ExpandableListDataPump;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.constants.ServiceUrls;
import com.tatx.partnerapp.network.RetrofitRequester;
import com.tatx.partnerapp.network.RetrofitResponseListener;
import com.tatx.partnerapp.pojos.ApiResponseVo;
import com.tatx.partnerapp.pojos.Faq;
import com.tatx.partnerapp.pojos.GetFaqsVo;

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

    @BindView(R.id.tatxhelp) TextView tatxhelp;
    @BindView(R.id.contactinfo) TextView contactinfo;
    @BindView(R.id.emailinfo) TextView emailinfo;
    @BindView(R.id.postboxinfo) TextView postboxinfo;
    @BindView(R.id.submit) Button submit;
    @BindView(R.id.helpqs) EditText helpqs;

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    HashMap<String, List<String>> expandableListDetail;

    List<String> expandableListTitle1;
    HashMap<String, List<String>> expandableListDetail1;
    @BindView(R.id.rl_root_view_kbh)
    RelativeLayout rlRootViewKbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.help));

        Common.hideSoftKeyboard(this,rlRootViewKbh);

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
        hashMap.put(ServiceUrls.ResponseParams.ROLE, Constants.DRIVER);
        hashMap.put(ServiceUrls.ResponseParams.TYPE, ServiceUrls.RequestNames.FAQ);

        new RetrofitRequester(HelpActivity.this).sendStringRequest(ServiceUrls.RequestNames.FAQ,hashMap);



    }
    @OnClick (R.id.submit) void sendFeedback(){
        if (helpqs.length() >4) {
            HashMap<String,String>  hashMap=new HashMap();
            hashMap.put(ServiceUrls.ResponseParams.MESSAGE, helpqs.getText().toString().trim());
            hashMap.put(ServiceUrls.ResponseParams.TYPE, Constants.CONTACT_US);
            hashMap.put(ServiceUrls.ApiRequestParams.ROLE, Constants.DRIVER);

            new RetrofitRequester(HelpActivity.this).sendStringRequest(ServiceUrls.RequestNames.FEEDBACK,hashMap);

        }else {
            Common.customToast(HelpActivity.this,Common.getStringResourceText(R.string.please_enter_minimum_5_characters),Common.TOAST_TIME);
        }
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
