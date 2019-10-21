package com.tatx.partnerapp.menuactivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.tatx.partnerapp.R;
import com.tatx.partnerapp.abstractclasses.BaseActivity;
import com.tatx.partnerapp.activities.FeedbackActivity;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.pojos.Trip;


public class TripDetailsActivity extends BaseActivity
{


    @BindView(R.id.iv_map_image)
    ImageView ivMapImage;

    @BindView(R.id.iv_profile_pic)
    ImageView ivProfilePic;

    @BindView(R.id.tv_rating)
    TextView tvRating;

    @BindView(R.id.tv_trip_id_right)
    TextView tvTripIdRight;

    @BindView(R.id.tv_payment_method_right)
    TextView tvPaymentMethodRight;

    @BindView(R.id.tv_base_fare_right)
    TextView tvBaseFareRight;

    @BindView(R.id.tv_distance_value_middle)
    TextView tvDistanceValueMiddle;

    @BindView(R.id.tv_distance_cost_right)
    TextView tvDistanceCostRight;

    @BindView(R.id.tv_duration_value_middle)
    TextView tvDurationValueMiddle;

    @BindView(R.id.tv_duration_cost_right)
    TextView tvDurationCostRight;

    @BindView(R.id.tv_rounded_duration_value_middle)
    TextView tvRoundedDurationValueMiddle;

    @BindView(R.id.tv_rounded_duration_cost_right)
    TextView tvRoundedDurationCostRight;

    @BindView(R.id.tv_sub_total_cost_right)
    TextView tvSubTotalCostRight;

    @BindView(R.id.tv_rounded_sub_total_cost_right)
    TextView tvRoundedSubTotalCostRight;

    @BindView(R.id.tv_tip_cost_right)
    TextView tvTipCostRight;

    @BindView(R.id.tv_total_fare_cost_right)
    TextView tvTotalFareCostRight;

    @BindView(R.id.tv_total_fare_cost_right_top)
    TextView tvTotalFareCostRightTop;

    @BindView(R.id.tv_trip_end_date_time)
    TextView tvTripEndDateTime;

    @BindView(R.id.tv_make_model)
    TextView tvMakeModel;

    @BindView(R.id.tv_discount)
    TextView tvDiscount;
    private Trip trip;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tripdetails);

        ButterKnife.bind(this);
        setTitleText(Common.getStringResourceText(R.string.tripdetails));

       trip = (Trip) getIntent().getSerializableExtra(Constants.KEY_1);


        Picasso.with(this).load(trip.route).into(ivMapImage);

/*

        if (!TextUtils.isEmpty(trip.profile) && trip.profile != null)
        {
            Common.setRoundedCroppedBackgroundImage(this,trip.profile,ivProfilePic,100);
        }
*/


        Common.setRoundedCroppedBackgroundImage(this,trip.profile,ivProfilePic,100);

        tvRating.setText(String.valueOf(trip.userRating));

        tvTripIdRight.setText(String.valueOf(trip.tripId));

        tvPaymentMethodRight.setText(trip.paymentTypeId);

        tvBaseFareRight.setText(Common.getSuffixSARString(trip.baseFare,trip.currency));

        tvDistanceValueMiddle.setText(trip.distanceValue + " Km");

        tvDistanceCostRight.setText(Common.getSuffixSARString(trip.distanceCost,trip.currency));

        tvDurationValueMiddle.setText(trip.durationValue + " Min");

        tvDurationCostRight.setText(Common.getSuffixSARString(trip.durationCost,trip.currency));

        tvRoundedDurationValueMiddle.setText(trip.durationValueRound + " Min");

        tvRoundedDurationCostRight.setText(Common.getSuffixSARString(trip.durationCostRound,trip.currency));

        tvSubTotalCostRight.setText(Common.getSuffixSARString(trip.subtotal,trip.currency));

        tvRoundedSubTotalCostRight.setText(Common.getSuffixSARString(trip.subTotalRound,trip.currency));

        tvTipCostRight.setText(Common.getSuffixSARString(trip.tip,trip.currency));

        tvTotalFareCostRight.setText(Common.getSuffixSARString(trip.tripAmount,trip.currency));

        tvTotalFareCostRightTop.setText(Common.getSuffixSARString(trip.tripAmount,trip.currency));

        tvTripEndDateTime.setText(trip.dateTime);

        tvMakeModel.setText(trip.make+" ("+trip.model+")");

        tvDiscount.setText(String.valueOf( Math.abs(trip.discount)));



    }


    @OnClick(R.id.tv_feedback)
    void feedBackButtonFunctionality()
    {
        Intent intent=new Intent(this, FeedbackActivity.class);

        intent.putExtra(Constants.IntentKeys.TRIP_ID,String.valueOf(trip.tripId));
        startActivity( intent);
        finish();

    }










}

