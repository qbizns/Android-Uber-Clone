package com.tatx.userapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tatx.userapp.R;
import com.tatx.userapp.pojos.FavLocation;

import java.util.List;


/**
 * Created by Subbu on 6/19/2015.
 */
public class SavedLocationListAdapter extends RecyclerView.Adapter<SavedLocationListAdapter.ViewHolder> {
    private List<FavLocation> favLocations;
    private OnItemClickListener mListener;
    OnItemLongClickListener itemLongClickListener;
    int locationtype;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public void setOnItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }


    public SavedLocationListAdapter(Context MainActivity, List<FavLocation> favLocations, int locationtype) {
        this.favLocations = favLocations;
        this.locationtype=locationtype;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.saved_location_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        v.setTag(i);
        return vh;
    }

    @Override

    public void onBindViewHolder(ViewHolder holder, int i) {
        if (favLocations !=null&& favLocations.size()>0) {
            FavLocation data2 = favLocations.get(i);
            holder.mPrediction.setText(data2.name);
        }

    }

    @Override
    public int getItemCount() {
        // return productDetailses.size();
        return (null != favLocations ? favLocations.size() : 0);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView mPrediction;
        RelativeLayout mRow;

        public ViewHolder(final View rowView) {
            super(rowView);
            mPrediction = (TextView) rowView.findViewById(R.id.address);
            mRow=(RelativeLayout)rowView.findViewById(R.id.predictedRow);
            mRow.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }


        @Override
        public boolean onLongClick(View v) {
            if (itemLongClickListener != null) {
                itemLongClickListener.onItemLongClick(v, getPosition());
            }
            return false;
        }
    }

}
