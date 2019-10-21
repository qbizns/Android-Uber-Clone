package com.tatx.partnerapp.menuactivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.abstractclasses.BaseActivity;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.pojos.BonusHistory;
import com.tatx.partnerapp.pojos.GetBonusDetailsVo;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by user on 20-05-2016.
 */
public class BonusActivity extends BaseActivity
{


    @BindView(R.id.tv_dirver_acceptance_rate) TextView tvDirverAcceptanceRate;
    @BindView(R.id.tv_acceptance_rate_desc) TextView tvAcceptanceRateDesc;
    @BindView(R.id.tv_avg_rating) TextView tvAvgRating;
    @BindView(R.id.tv_avg_rating_desc) TextView tvAvgRatingDesc;

    @BindView(R.id.tv_week_online_hours) TextView tvWeekOnlineHours;
    @BindView(R.id.tv_online_hours_desc) TextView tvOnlineHoursDesc;

    @BindView(R.id.tv_peak_online_hours) TextView tvPeakOnlineHours;

    @BindView(R.id.tv_peak_online_hours_desc) TextView tvPeakOnlineHoursDesc;

    @BindView(R.id.tv_driver_week_total_trips) TextView tvDriverWeekTotalTrips;

    @BindView(R.id.tv_driver_week_total_trips_desc) TextView tvDriverWeekTotalTripsDesc;


    @BindView(R.id.table_layout_main) TableLayout tableLayoutMain;
    @BindView(R.id.tv_bonus_amount_header) TextView tvBonusAmountHeader;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bonus);

        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.bonus));


        GetBonusDetailsVo getBonusDetailsVo = (GetBonusDetailsVo) getIntent().getSerializableExtra(Constants.KEY_1);

        tvDirverAcceptanceRate.setText(getBonusDetailsVo.acceptanceRate);

        tvAcceptanceRateDesc.setText(getBonusDetailsVo.acceptanceRateDesc);

        tvAvgRating.setText(getBonusDetailsVo.avgRating);

        tvAvgRatingDesc.setText(getBonusDetailsVo.avgRatingDesc);

        tvWeekOnlineHours.setText(getBonusDetailsVo.onlineHoursWeek);

        tvOnlineHoursDesc.setText(getBonusDetailsVo.onlineHoursWeekDesc);

        tvPeakOnlineHours.setText(getBonusDetailsVo.onlineHoursPeak);

        tvPeakOnlineHoursDesc.setText(getBonusDetailsVo.onlineHoursPeakDesc);

        tvDriverWeekTotalTrips.setText(getBonusDetailsVo.mintripsWeek);

        tvDriverWeekTotalTripsDesc.setText(getBonusDetailsVo.mintripsWeekDesc);




        tvBonusAmountHeader.setText(Common.getStringResourceText(R.string.bonus)+" ("+getBonusDetailsVo.currency+")");


        for (BonusHistory bonusHistory:getBonusDetailsVo.bonusHistory)
        {

            View inflatedView = getLayoutInflater().inflate(R.layout.bonus_details_row,null);

            TextView tvStartDate = (TextView) inflatedView.findViewById(R.id.tv_start_date);
            TextView tvEndDate = (TextView) inflatedView.findViewById(R.id.tv_end_date);
            TextView tvBonusAmount = (TextView) inflatedView.findViewById(R.id.tv_bonus_amount);
            TextView tvStatus = (TextView) inflatedView.findViewById(R.id.tv_status);

            tvStartDate.setText(bonusHistory.startDate.replace(" ","\n"));
            tvEndDate.setText(bonusHistory.endDate.replace(" ","\n"));
            tvBonusAmount.setText(bonusHistory.bonusAmount);
            tvStatus.setText(bonusHistory.status);

            tableLayoutMain.addView(inflatedView);


        }




    }


}
