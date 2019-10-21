package com.tatx.partnerapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.dataset.HelpPojo;


/**
 * Created by Subbu on 6/19/2015.
 */
public class HelpListAdapter extends RecyclerView.Adapter<HelpListAdapter.ViewHolder> {
    List<HelpPojo> helpList;
    private OnItemClickListener mListener;
    Context context;

    public HelpListAdapter(com.tatx.partnerapp.menuactivity.HelpActivity context, List<HelpPojo> helpList) {
        this.helpList = helpList;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.help_list, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        v.setTag(i);
        return vh;
    }

    @Override

    public void onBindViewHolder(ViewHolder holder, int i) {
        HelpPojo data2 = helpList.get(i);
        holder.helplist.setText(data2.getAnswer());
        //holder.offersimg.setImageResource(imageId[position]);
        //Picasso.with(context).load(data2.getRoute()).into(holder.offersimg);
    }

    @Override
    public int getItemCount() {
        // return productDetailses.size();
        return (null != helpList ? helpList.size() : 0);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView helplist;


        public ViewHolder(final View rowView) {
            super(rowView);
            helplist = (TextView) rowView.findViewById(R.id.helplist);


        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }
    }
}
