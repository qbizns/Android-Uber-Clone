package com.tatx.partnerapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.squareup.picasso.Picasso;
import com.tatx.partnerapp.R;
import com.tatx.partnerapp.pojos.Push;


/**
 * Created by Subbu on 6/19/2015.
 */
public class NotificationsListAdapter extends RecyclerView.Adapter<NotificationsListAdapter.ViewHolder> {
    List<Push> notificationlist;
    private OnItemClickListener mListener;
    Context context;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public NotificationsListAdapter(Context context, List<Push> notificationlist) {
        this.notificationlist = notificationlist;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_list_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        v.setTag(i);
        return vh;
    }

    @Override

    public void onBindViewHolder(ViewHolder holder, int i) {
        holder.notify_text.setText(notificationlist.get(i).message);
        //holder.offersimg.setImageResource(imageId[position]);
        Picasso.with(context).load(notificationlist.get(i).image).into(holder.notify_img);
    }

    @Override
    public int getItemCount() {
        // return productDetailses.size();
        return (null != notificationlist ? notificationlist.size() : 0);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView notify_text;
        ImageView notify_img;

        public ViewHolder(final View rowView) {
            super(rowView);
            notify_text = (TextView) rowView.findViewById(R.id.notify_text);
            notify_img = (ImageView) rowView.findViewById(R.id.notify_img);
            rowView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }
    }
}
