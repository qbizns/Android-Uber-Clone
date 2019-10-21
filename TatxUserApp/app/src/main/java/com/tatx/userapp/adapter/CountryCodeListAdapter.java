package com.tatx.userapp.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tatx.userapp.R;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.dataset.CountryCodePojo;

import java.util.List;


/**
 * Created by Subbu on 6/19/2015.
 */
public class CountryCodeListAdapter extends RecyclerView.Adapter<CountryCodeListAdapter.ViewHolder> {
    Context context;
    List<CountryCodePojo> objects;
    String data;
    String code;
    SharedPreferences prefs;
    int userid;
    private OnItemClickListener mListener;
    private byte[] imageBitmap;


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public CountryCodeListAdapter(Context context, List<CountryCodePojo> objects) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        userid = prefs.getInt("userid", 0);
        this.context = context;
        this.objects = objects;

    }

    @Override
    public int getItemCount() {
        // return productDetailses.size();
        return (null != objects ? objects.size() : 0);
    }


    /********* Create a holder Class to contain inflated xml file elements *********/
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.country_code_list_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        v.setTag(i);
        return vh;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView message;
        ImageView flagImage;


        public ViewHolder(final View vi) {
            super(vi);

            message = (TextView) vi.findViewById(R.id.message);
            flagImage = (ImageView) vi.findViewById(R.id.flag_image);

            vi.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {

                if (mListener != null) {
                    mListener.onItemClick(v, getPosition());
                }
        }
    }
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            imageBitmap = objects.get(position).getFlag();
            code = objects.get(position).getCountryName();
            if (imageBitmap!=null) {
                holder.flagImage.setImageBitmap(Common.getBitmapFromByteArray(imageBitmap));
            }
            holder.message.setText(code);



        }

        }



