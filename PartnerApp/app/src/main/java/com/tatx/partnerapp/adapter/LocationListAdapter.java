package com.tatx.partnerapp.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.dataset.UserLocations;


/**
 * Created by Subbu on 6/19/2015.
 */
public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.ViewHolder> {
    private List<UserLocations> userLocationses;
    private OnItemClickListener mListener;
    SharedPreferences prefs;
    String lat;
    String lon;
    double latitude;
    double longitude;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public LocationListAdapter(Context MainActivity, List<UserLocations> userLocationses, double latitude, double longitude) {
        this.userLocationses = userLocationses;
        this.latitude = latitude;
        this.longitude = longitude;
        prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity);
        lat = prefs.getString(Constants.LATITUDE, "");
        lon = prefs.getString(Constants.LONGITUDE, "");


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
        UserLocations data2 = userLocationses.get(i);
        holder.locationname.setText(data2.getLocName());
      //  holder.locationAdd.setText(data2.getLocAddr());
       /* String distanceInMiles = String.valueOf(distance(latitude, longitude, Double.parseDouble(data2.getLocLat()), Double.parseDouble(data2.getLocLong())));
        double dis = (Double.parseDouble(distanceInMiles));
        double km = 1.609344;
        double total = dis * km;*/
       // holder.distance.setText(String.valueOf(total));
    }

    @Override
    public int getItemCount() {
        // return productDetailses.size();
        return (null != userLocationses ? userLocationses.size() : 0);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView locationname, locationAdd, distance;
        ImageView image1;
        ImageButton visibility;
        LinearLayout linear_row_click;

        public ViewHolder(final View rowView) {
            super(rowView);
            locationname = (TextView) rowView.findViewById(R.id.textView5);
           // locationAdd = (TextView) rowView.findViewById(R.id.textView6);
          //  distance = (TextView) rowView.findViewById(R.id.distance);
            image1 = (ImageView) rowView.findViewById(R.id.image);
            visibility = (ImageButton) rowView.findViewById(R.id.button_shareLocation);
            linear_row_click = (LinearLayout) rowView.findViewById(R.id.linear_row_click);

            visibility.setOnClickListener(this);
            linear_row_click.setOnClickListener(this);
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
