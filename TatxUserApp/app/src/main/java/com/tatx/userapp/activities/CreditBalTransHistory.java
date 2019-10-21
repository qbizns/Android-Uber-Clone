package com.tatx.userapp.activities;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tatx.userapp.R;
import com.tatx.userapp.abstractclasses.BaseActivity;
import com.tatx.userapp.adapter.CreditHistoryListAdapter;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;
import com.tatx.userapp.constants.ServiceUrls;
import com.tatx.userapp.network.RetrofitRequester;
import com.tatx.userapp.network.RetrofitResponseListener;
import com.tatx.userapp.pojos.ApiResponseVo;
import com.tatx.userapp.pojos.CreditBalanceTransactionVo;
import com.tatx.userapp.pojos.Transactions;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Home on 17-05-2016.
 */
public class CreditBalTransHistory extends BaseActivity implements RetrofitResponseListener {

    CreditHistoryListAdapter creditHistoryListAdapter;
    GridLayoutManager gridLayoutManager;
    private TextView recordnotfnd;
    RecyclerView savedLocationRecyclerView;


     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_balance_transaction_hist);
        initilaizedAll();
    }

        public void initilaizedAll() {

            recordnotfnd = (TextView) findViewById(R.id.recordnotfnd);

            savedLocationRecyclerView = (RecyclerView) findViewById(R.id.saved_location_recycler_view);
            getAccountDetailsApi();


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



    public void setListOnAdapter(final List<Transactions> historyList) {
        gridLayoutManager = new GridLayoutManager(this, 1);
        savedLocationRecyclerView.setLayoutManager(gridLayoutManager);
        if (historyList.size() != 0) {
            recordnotfnd.setVisibility(View.GONE);
            creditHistoryListAdapter = new CreditHistoryListAdapter(this, historyList);
            savedLocationRecyclerView.setAdapter(creditHistoryListAdapter);
            creditHistoryListAdapter.setOnItemClickListener(new CreditHistoryListAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(View view, int position) {

                    Transactions data = historyList.get(position);
                    int pos=position;
                    switch (view.getId()) {

                    }
                }
            });
            creditHistoryListAdapter.notifyDataSetChanged();

        } else if (historyList == null) {

            recordnotfnd.setVisibility(View.VISIBLE);
        }


    }


    public void getAccountDetailsApi() {

        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.TRANS_CUSTOMER,null);

    }

    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId) {
        if (apiResponseVo.code != Constants.SUCCESS)
        {

            Common.customToast(this, apiResponseVo.status);

            return;


        }


        switch (apiResponseVo.requestname)
        {

            case ServiceUrls.RequestNames.TRANS_CUSTOMER:
                CreditBalanceTransactionVo creditBalanceTransactionVo=Common.getSpecificDataObject(apiResponseVo.data,CreditBalanceTransactionVo.class);

                setListOnAdapter(creditBalanceTransactionVo.transactions);
             break;
        }
    }
}
