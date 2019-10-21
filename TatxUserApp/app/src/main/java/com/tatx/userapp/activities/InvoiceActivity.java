package com.tatx.userapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tatx.userapp.R;
import com.tatx.userapp.abstractclasses.BaseActivity;
import com.tatx.userapp.abstractclasses.NetworkChangeListenerActivity;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;
import com.tatx.userapp.constants.ServiceUrls;
import com.tatx.userapp.menuactivity.TripDetailsActivity;
import com.tatx.userapp.network.RetrofitRequester;
import com.tatx.userapp.network.RetrofitResponseListener;
import com.tatx.userapp.pojos.ApiResponseVo;
import com.tatx.userapp.pojos.EndTripVo;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InvoiceActivity extends NetworkChangeListenerActivity implements RetrofitResponseListener {


    private EndTripVo endTripVo;

    @BindView(R.id.tv_pick_address)
    TextView tvPickAddress;

    @BindView(R.id.tv_destination_address)
    TextView tvDestinationAddress;

    @BindView(R.id.tv_trip_amount)
    TextView tvTripAmount;

    @BindView(R.id.tv_distance)
    TextView tvDistance;

    @BindView(R.id.tv_duration)
    TextView tvDuration;

    @BindView(R.id.rb_rating_to_user)
    RatingBar rbRatingToUser;

    @BindView(R.id.details)
    LinearLayout details;

    @BindView(R.id.tv_base_fare_right)
    TextView tvBaseFareRight;

    @BindView(R.id.tv_distance_cost_right)
    TextView tvDistanceCostRight;

    @BindView(R.id.tv_duration_cost_right)
    TextView tvDurationCostRight;

    @BindView(R.id.tv_rounded_duration_cost_right)
    TextView tvRoundedDurationCostRight;

    @BindView(R.id.tv_sub_total_cost_right)
    TextView tvSubTotalCostRight;

    @BindView(R.id.tv_rounded_sub_total_cost_right)
    TextView tvRoundedSubTotalCostRight;

    @BindView(R.id.tv_current_amount)
    TextView tvCurrentAmount;

    @BindView(R.id.tv_previous_amount)
    TextView tvPreviousAmount;

    @BindView(R.id.tv_discount)
    TextView tvDiscount;


    @BindView(R.id.btn_submit_invoice)
    Button submitInvoice;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_invoice);

        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.invoice));

        endTripVo = (EndTripVo) getIntent().getSerializableExtra(Constants.KEY_1);

//        setDetails(endTripVo, " SAR");
        setDetails(endTripVo, " "+endTripVo.currency);

    }

    public void setDetails(EndTripVo endTripVo, String curency) {

        tvPickAddress.setText(endTripVo.pickupLocation + curency);

        tvDestinationAddress.setText(endTripVo.dropLocation + curency);

        tvDistance.setText(endTripVo.distance +" "+Common.getStringResourceText(R.string.km));

        tvDuration.setText(endTripVo.duration +" "+Common.getStringResourceText(R.string.min));

        tvTripAmount.setText(String.valueOf(endTripVo.amount) + curency);

        tvBaseFareRight.setText(String.valueOf(endTripVo.baseFare) + curency);

        tvDistanceCostRight.setText(String.valueOf(endTripVo.distanceCost) + curency);

        tvDurationCostRight.setText(String.valueOf(endTripVo.durationCost) + curency);

        tvRoundedDurationCostRight.setText(String.valueOf(endTripVo.adjustmentAmount) + curency);

        tvSubTotalCostRight.setText(String.valueOf(endTripVo.amount) + curency);

        tvRoundedSubTotalCostRight.setText(String.valueOf(Math.round(endTripVo.amount)) + curency);

        tvCurrentAmount.setText(String.valueOf(Math.floor(endTripVo.tripAmount)) + curency);

        tvPreviousAmount.setText(String.valueOf(Math.abs(endTripVo.adjustmentAmount)) + curency);


        if (endTripVo.discount == null)
        {
            endTripVo.discount = String.valueOf(0.0);
        }

        tvDiscount.setText(endTripVo.discount + curency);


    }

    @OnClick(R.id.btn_submit_invoice)
    void btnSubmitInvoiceClickFunctionality()
    {



        if(rbRatingToUser.getRating() == 0)
        {
            Common.customToast(this,Common.getStringResourceText(R.string.please_give_rating));

        }
        else
        {
            submitInvoice.setClickable(false);
            HashMap<String, String> params = new HashMap<>();
            params.put(ServiceUrls.RequestParams.TRIPID, endTripVo.tripId);
            params.put(ServiceUrls.RequestParams.ORDERID, endTripVo.orderId);
            params.put(ServiceUrls.RequestParams.ROLE, Constants.CUSTOMER);
            params.put(ServiceUrls.RequestParams.RATING, String.valueOf(rbRatingToUser.getRating()));
            params.put(ServiceUrls.RequestParams.DEVICE, Constants.ANDROID);
//        params.put(ServiceUrls.RequestParams.TIP, );

            new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.TRIP_RATING, params);
        }


    }

    @OnClick(R.id.details)
    void tripDetails() {
//        Bundle bundle = new Bundle();
//        Intent intent = new Intent(this, TripDetailsActivity.class);
//        bundle.putInt("value", 1);
//        bundle.putString("trip_id", endTripVo.tripId);
//        intent.putExtra("bundle", bundle);
//        startActivity(intent);
    }


    @Override
    public void onBackPressed() {

        showPleaseRateFirstToast();



    }

    private void showPleaseRateFirstToast() {
        Common.customToast(this, Common.getStringResourceText(R.string.please_rate_first), Common.TOAST_TIME);
    }

    @Override
    protected void onTitleBackPressed() {
        showPleaseRateFirstToast();
    }

    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId) {

        if (apiResponseVo.code != Constants.SUCCESS) {
            Common.customToast(this, apiResponseVo.status);
            return;
        }


        switch (apiResponseVo.requestname)
        {
            case ServiceUrls.RequestNames.TRIP_RATING:


                if (!getIntent().getBooleanExtra(Constants.IntentKeys.FROM_SPLASH_SCREEN,false))
                {
                    Intent intent = new Intent(this, GoogleMapDrawerActivity.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                }

                finish();


                break;


        }


    }


}
