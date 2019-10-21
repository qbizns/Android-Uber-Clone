package com.tatx.partnerapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.dataset.OffersPojo;


/**
 * Created by Subbu on 6/19/2015.
 */
public class OffersListAdapter extends RecyclerView.Adapter<OffersListAdapter.ViewHolder> {
    List<OffersPojo> offersList;
    private OnItemClickListener mListener;
    Context context;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public OffersListAdapter(Context context, List<OffersPojo> offersList) {
        this.offersList = offersList;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.offers_list, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        v.setTag(i);
        return vh;
    }

    @Override

    public void onBindViewHolder(ViewHolder holder, int i) {
        OffersPojo data2 = offersList.get(i);
        holder.offerslist.setText(data2.getMessage());
        //holder.offersimg.setImageResource(imageId[position]);
        //Picasso.with(context).load(data2.getRoute()).into(holder.offersimg);
    }

    @Override
    public int getItemCount() {
        // return productDetailses.size();
        return (null != offersList ? offersList.size() : 0);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView offerslist;
        ImageView offersimg;

        public ViewHolder(final View rowView) {
            super(rowView);
            offerslist = (TextView) rowView.findViewById(R.id.offerslist);
            offersimg = (ImageView) rowView.findViewById(R.id.offers_img);

        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }
    }
}
