package com.tatx.partnerapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tatx.partnerapp.R;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.pojos.AllReferral;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Venkateswarlu SKP on 27-12-2016.
 */
public class ViewAllReferralsListAdapter  extends RecyclerView.Adapter<ViewAllReferralsListAdapter.ViewHolder>
{


    List<AllReferral> allReferral;

    Context context;

    private final String currencyCode;



    public ViewAllReferralsListAdapter(Context context, List<AllReferral> allReferral, String currencyCode)
    {

        this.context = context;

        this.allReferral = allReferral;

        this.currencyCode = currencyCode;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_referral_code_list_item,viewGroup,false));
    }

    @Override

    public void onBindViewHolder(ViewHolder holder, int i)
    {

        Common.Log.i("? - allReferral : "+allReferral);

        if (allReferral.get(i).profile != null)
        {
            Picasso.with(context).load(allReferral.get(i).profile).into(holder.ivReferralProfilePic);
        }

        holder.tvName.setText(allReferral.get(i).firstName+" "+allReferral.get(i).lastName);

        holder.tvEmail.setText(allReferral.get(i).email);

        holder.tvTotalReferralAmount.setText(allReferral.get(i).totalReferralAmount+" "+currencyCode);

    }

    @Override
    public int getItemCount()
    {

        Common.Log.i("? - allReferral.size() : "+allReferral.size());
//        return allReferral.size();

        return (null != allReferral ? allReferral.size() : 0);
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {


       @BindView(R.id.iv_referral_profile_pic) ImageView ivReferralProfilePic;
       @BindView(R.id.tv_name) TextView tvName;
       @BindView(R.id.tv_email) TextView tvEmail;
       @BindView(R.id.tv_total_referral_amount) TextView tvTotalReferralAmount;

        public ViewHolder(final View rowView)
        {

            super(rowView);

            ButterKnife.bind(this,rowView);

        }

    }
}
