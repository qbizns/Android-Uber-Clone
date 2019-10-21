package com.tatx.partnerapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.pojos.DaylyDetail;
import com.tatx.partnerapp.pojos.TimeConvert;

import java.util.List;


/**
 * Created by Subbu on 6/19/2015.
 */
public class AllOnlineHoursListAdapter extends RecyclerView.Adapter<AllOnlineHoursListAdapter.ViewHolder> {
    List<DaylyDetail> daylyDetail;
    private OnItemClickListener mListener;
    Context context;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public AllOnlineHoursListAdapter(Context context,  List<DaylyDetail> daylyDetail) {
        this.daylyDetail = daylyDetail;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_online_hours_list_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        v.setTag(i);
        return vh;
    }

    @Override

    public void onBindViewHolder(ViewHolder holder, int i) {

        holder.tv_date.setText(daylyDetail.get(i).day);
        TimeConvert time = Common.calculateTime(daylyDetail.get(i).seconds);

        holder.tv_hours.setText(time.getHours()+":"+time.getMinutes()+":"+time.getSeconds());
        //holder.offersimg.setImageResource(imageId[position]);
        //Picasso.with(context).load(data2.getRoute()).into(holder.offersimg);
    }

    @Override
    public int getItemCount() {
        // return productDetailses.size();
        return (null != daylyDetail ? daylyDetail.size() : 0);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final RelativeLayout row_view;
        TextView tv_date;
        TextView tv_hours;

        public ViewHolder(final View rowView) {
            super(rowView);
            tv_date = (TextView) rowView.findViewById(R.id.tv_date);
            tv_hours = (TextView) rowView.findViewById(R.id.tv_hours);
            row_view = (RelativeLayout) rowView.findViewById(R.id.row_view);


            row_view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }
    }
}
