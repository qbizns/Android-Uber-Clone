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
import com.tatx.partnerapp.pojos.City;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Venkateswarlu SKP on 9/14/2016.
 */

public class CustomRecyclerViewAdapterCityNames extends RecyclerView.Adapter<CustomRecyclerViewAdapterCityNames.ViewHolder> implements Filterable
{
    private final ArrayList<City> cityArrayList;

    private OnItemClickListener mListener;

    private CustomFilter customFilter;

    public CustomRecyclerViewAdapterCityNames(ArrayList<City> cityArrayList)
    {
        this.cityArrayList = cityArrayList;

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
        return cityArrayList.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.car_make_list_item, viewGroup, false));
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        @BindView(R.id.tv_name) TextView tvName;

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

        holder.tvName.setText(cityArrayList.get(position).name);

    }



    public City getItem(int position) {
        return cityArrayList.get(position);
    }


    @Override
    public Filter getFilter()
    {

        if (customFilter == null)
        {
            customFilter = new CustomFilter(this, cityArrayList);
        }

        return customFilter;
    }

    private static class CustomFilter extends Filter
    {

        private final CustomRecyclerViewAdapterCityNames adapter;

        private final List<City> originalList;

        private final List<City> filteredList;

        private CustomFilter(CustomRecyclerViewAdapterCityNames adapter, ArrayList<City> originalList)
        {
            super();
            this.adapter = adapter;
            this.originalList = (List<City>) originalList.clone();
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            filteredList.clear();

            final FilterResults results = new FilterResults();

            if (constraint.length() == 0)
            {
                filteredList.addAll(originalList);
            }
            else
            {
                final String filterPattern = constraint.toString().toLowerCase().trim();

                for (final City city : originalList)
                {
                    if (city.name.toLowerCase().startsWith(filterPattern))
                    {
                        filteredList.add(city);
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

            adapter.cityArrayList.clear();

            adapter.cityArrayList.addAll((ArrayList<City>) results.values);

            Common.Log.i("? - adapter.cityArrayList.size()"+adapter.cityArrayList.size());
            Common.Log.i("? - adapter.cityArrayList.size()"+adapter.getItemCount());

            adapter.notifyDataSetChanged();


        }

    }






}



