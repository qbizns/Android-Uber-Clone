package com.tatx.userapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.tatx.userapp.R;
import com.tatx.userapp.pojos.FavLocation;

import java.util.List;


/**
 * Created by Subbu on 6/19/2015.
 */
public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.ViewHolder> {
    private List<FavLocation> favLocations;
    private OnItemClickListener mListener;
    OnItemLongClickListener itemLongClickListener;

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


    public LocationListAdapter(Context MainActivity, List<FavLocation> favLocations) {
        this.favLocations = favLocations;



    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.location_list_items, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        v.setTag(i);
        return vh;
    }

    @Override

    public void onBindViewHolder(ViewHolder holder, int i) {
        FavLocation data2 = favLocations.get(i);
        holder.locationname.setText(data2.name);

    }

    @Override
    public int getItemCount() {
        // return productDetailses.size();
        return (null != favLocations ? favLocations.size() : 0);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private final RelativeLayout rlUpdateLocation;
        TextView locationname;
        LinearLayout linear_row_click;

        public ViewHolder(final View rowView) {
            super(rowView);
            locationname = (TextView) rowView.findViewById(R.id.textView5);
            rlUpdateLocation = (RelativeLayout) rowView.findViewById(R.id.rl_update_location);
            linear_row_click = (LinearLayout) rowView.findViewById(R.id.linear_row_click);


            rlUpdateLocation.setOnClickListener(this);
            linear_row_click.setOnClickListener(this);
            linear_row_click.setOnLongClickListener(this);


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
