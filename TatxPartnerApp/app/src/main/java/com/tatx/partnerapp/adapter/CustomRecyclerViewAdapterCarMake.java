package com.tatx.partnerapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.pojos.Make;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Venkateswarlu SKP on 9/14/2016.
 */

public class CustomRecyclerViewAdapterCarMake extends RecyclerView.Adapter<CustomRecyclerViewAdapterCarMake.ViewHolder> implements Filterable
{
    private final ArrayList<Make> makeList;

    private OnItemClickListener mListener;

    private CustomFilter customFilter;

    public CustomRecyclerViewAdapterCarMake(ArrayList<Make> makeList)
    {
        this.makeList = makeList;

    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }



    @Override
    public int getItemCount()
    {
        return makeList.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.car_make_list_item, viewGroup, false));
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        @BindView(R.id.tv_name) TextView tvCarMake;

        public ViewHolder(View view)
        {
            super(view);

            ButterKnife.bind(this,view);

            view.setOnClickListener(this);


        }

        @Override
        public void onClick(View v)
        {

            if (mListener != null)
            {
                mListener.onItemClick(v, getAdapterPosition());
            }

        }


    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {

        holder.tvCarMake.setText(makeList.get(position).make);

    }



    public Make getItem(int position) {
        return makeList.get(position);
    }


    @Override
    public Filter getFilter()
    {

        if (customFilter == null)
        {
            customFilter = new CustomFilter(this, makeList);
        }

        return customFilter;
    }

    private static class CustomFilter extends Filter
    {

        private final CustomRecyclerViewAdapterCarMake adapter;

        private final List<Make> originalList;

        private final List<Make> filteredList;

        private CustomFilter(CustomRecyclerViewAdapterCarMake adapter, ArrayList<Make> originalList)
        {
            super();
            this.adapter = adapter;
            this.originalList = (List<Make>) originalList.clone();
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            filteredList.clear();

            final FilterResults results = new Filter.FilterResults();

            if (constraint.length() == 0)
            {
                filteredList.addAll(originalList);
            }
            else
            {
                final String filterPattern = constraint.toString().toLowerCase().trim();

                for (final Make make : originalList)
                {
                    if (make.make.toLowerCase().startsWith(filterPattern))
                    {
                        filteredList.add(make);
                    }
                }
            }

            results.values = filteredList;

            results.count = filteredList.size();

            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {

            adapter.makeList.clear();

            adapter.makeList.addAll((ArrayList<Make>) results.values);

            Common.Log.i("? - adapter.makeList.size()"+adapter.makeList.size());
            Common.Log.i("? - adapter.makeList.size()"+adapter.getItemCount());

            adapter.notifyDataSetChanged();


        }

    }






}



