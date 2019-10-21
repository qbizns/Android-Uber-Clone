package com.tatx.userapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tatx.userapp.R;
import com.tatx.userapp.pojos.Transactions;

import java.util.List;


/**
 * Created by Subbu on 6/19/2015.
 */
public class CreditHistoryListAdapter extends RecyclerView.Adapter<CreditHistoryListAdapter.ViewHolder> {
    List<Transactions> historyList;
    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public CreditHistoryListAdapter(Context MainActivity, List<Transactions> historyList) {
        this.historyList = historyList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.credit_balance_transaction_hist_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        v.setTag(i);
        return vh;
    }

    @Override

    public void onBindViewHolder(ViewHolder holder, int i) {
        Transactions data2 = historyList.get(i);
        holder.transefer_id.setText(data2.id);
        holder.bill_no.setText(data2.id);
        holder.amount.setText(data2.amount);
        holder.date.setText(data2.createdAt);
    }

    @Override
    public int getItemCount() {
        // return productDetailses.size();
        return (null != historyList ? historyList.size() : 0);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView transefer_id, bill_no, date,amount;

        public ViewHolder(final View rowView) {
            super(rowView);
            transefer_id = (TextView) rowView.findViewById(R.id.transefer_id);
            bill_no = (TextView) rowView.findViewById(R.id.bill_no);
            date = (TextView) rowView.findViewById(R.id.date);
            amount=(TextView)rowView.findViewById(R.id.amount);

        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }


    }

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
