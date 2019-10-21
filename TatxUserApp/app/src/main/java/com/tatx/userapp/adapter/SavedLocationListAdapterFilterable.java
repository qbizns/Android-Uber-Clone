package com.tatx.userapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tatx.userapp.R;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.pojos.FavLocation;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Subbu on 6/19/2015.
 */
public class SavedLocationListAdapterFilterable extends RecyclerView.Adapter<SavedLocationListAdapterFilterable.ViewHolder> implements Filterable
{
    private ArrayList<FavLocation> favLocations;
    private OnItemClickListener mListener;
    OnItemLongClickListener itemLongClickListener;
    int locationtype;
    private CustomFilter customFilter;
    private final static int HEADER_VIEW = 0;
    private final static int CONTENT_VIEW = 1;

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


    public SavedLocationListAdapterFilterable(Context MainActivity, ArrayList<FavLocation> favLocations, int locationtype)
    {
        this.favLocations = favLocations;
        this.locationtype = locationtype;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        int layoutRes = 0;
        switch (i) {
            case HEADER_VIEW:
                layoutRes = R.layout.saved_location_header;
                break;
            case CONTENT_VIEW:
                layoutRes = R.layout.saved_location_item;
                break;
        }


        View v = LayoutInflater.from(viewGroup.getContext()).inflate(layoutRes, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        v.setTag(i);
        return vh;
    }

    @Override

    public void onBindViewHolder(ViewHolder holder, int i) {
        if (favLocations != null && favLocations.size() > 0) {
            FavLocation data2 = favLocations.get(i);
            holder.mPrediction.setText(data2.name);
        }

    }

    @Override
    public int getItemCount() {
        // return productDetailses.size();
        return (null != favLocations ? favLocations.size() : 0);
    }

    @Override
    public int getItemViewType(int position) {
        switch(position) {
            /*case 0:
                return HEADER_VIEW;*/
            default:
                return CONTENT_VIEW;
        }


    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView mPrediction;
        RelativeLayout mRow;

        public ViewHolder(final View rowView) {
            super(rowView);
            mPrediction = (TextView) rowView.findViewById(R.id.address);
            mRow = (RelativeLayout) rowView.findViewById(R.id.predictedRow);
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

    @Override
    public Filter getFilter() {
        if (customFilter == null)
        {
            customFilter = new CustomFilter(this, favLocations);
        }

        return customFilter;

    }


        private static class CustomFilter extends Filter {

            private final SavedLocationListAdapterFilterable adapter;

            private final List<FavLocation> originalList;

            private final List<FavLocation> filteredList;

            private CustomFilter(SavedLocationListAdapterFilterable adapter, ArrayList<FavLocation> originalList) {
                super();
                this.adapter = adapter;
                this.originalList = (List<FavLocation>) originalList.clone();
                this.filteredList = new ArrayList<>();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                filteredList.clear();

                final FilterResults results = new Filter.FilterResults();

                if (constraint.length() == 0) {
                    filteredList.addAll(originalList);
                } else {
                    final String filterPattern = constraint.toString().toLowerCase().trim();

                    for (final FavLocation favLocation : originalList) {
                        if (favLocation.name.toLowerCase().startsWith(filterPattern)) {
                            filteredList.add(favLocation);
                        }
                    }
                }



                results.values = filteredList;

                results.count = filteredList.size();

                return results;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                adapter.favLocations.clear();

                adapter.favLocations.addAll((ArrayList<FavLocation>) results.values);

                Common.Log.i("? - adapter.makeList.size()" + adapter.favLocations.size());
                Common.Log.i("? - adapter.makeList.size()" + adapter.getItemCount());

                adapter.notifyDataSetChanged();


            }

        }


    }

