package com.tatx.partnerapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.commonutills.CircleImageView;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.pojos.Trip;


/**
 * Created by Subbu on 6/19/2015.
 */
public class TripHistoryListAdapter extends RecyclerView.Adapter<TripHistoryListAdapter.ViewHolder> {
    List<Trip> historyList;
    private OnItemClickListener mListener;
    Context context;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public TripHistoryListAdapter(Context context, List<Trip> historyList) {
        this.historyList = historyList;
        this.context = context;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.triphistory_list_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        v.setTag(i);
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int i)
    {
        Trip data2 = historyList.get(i);

        holder.date.setText(Common.dateformateByTmeZone(data2.endTime));
        holder.vehiclemake.setText("("+data2.make +" "+data2.model+")");
        holder.rs.setText(String.valueOf(data2.tripAmount));
        holder.tripStatus.setText(data2.tripStatus);
        holder.tvCrrency.setText(data2.currency);
        Picasso.with(context).load(data2.route).into(holder.mapImage);
        if (data2.profile != null)
        {
            Picasso.with(context).load(data2.profile).into(holder.driverImage);
        }
    }
    @Override
    public int getItemCount()
    {
        // return productDetailses.size();
        return (null != historyList ? historyList.size() : 0);
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tripStatus;
        ImageView driverImage;
        TextView time;
        TextView vehiclemake;
        TextView date;
        TextView rs;
        ImageView mapImage;
        RatingBar userrating;
        TextView tvCrrency;
        public ViewHolder(final View rowView) {
            super(rowView);
            date = (TextView) rowView.findViewById(R.id.date);
            time = (TextView) rowView.findViewById(R.id.time);
            tvCrrency = (TextView) rowView.findViewById(R.id.sar);
            vehiclemake = (TextView) rowView.findViewById(R.id.vehiclemake);
            mapImage = (ImageView) rowView.findViewById(R.id.mapImage);
            rs = (TextView) rowView.findViewById(R.id.rs);
            tripStatus = (TextView) rowView.findViewById(R.id.trip_status);
            driverImage=(CircleImageView) rowView.findViewById(R.id.driver_image);

            mapImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }
    }
}
