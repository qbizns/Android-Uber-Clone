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
import com.tatx.partnerapp.pojos.Country;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Venkateswarlu SKP on 9/14/2016.
 */

public class CustomRecyclerViewAdapterCountries extends RecyclerView.Adapter<CustomRecyclerViewAdapterCountries.ViewHolder> implements Filterable
{
    private final ArrayList<Country> countryArrayList;

    private OnItemClickListener mListener;

    private CustomFilter customFilter;

    public CustomRecyclerViewAdapterCountries(ArrayList<Country> countryArrayList)
    {
        this.countryArrayList = countryArrayList;

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
        return countryArrayList.size();
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

        holder.tvName.setText(countryArrayList.get(position).countryName);

    }



    public Country getItem(int position) {
        return countryArrayList.get(position);
    }


    @Override
    public Filter getFilter()
    {

        if (customFilter == null)
        {
            customFilter = new CustomFilter(this, countryArrayList);
        }

        return customFilter;
    }

    private static class CustomFilter extends Filter
    {

        private final CustomRecyclerViewAdapterCountries adapter;

        private final List<Country> originalList;

        private final List<Country> filteredList;

        private CustomFilter(CustomRecyclerViewAdapterCountries adapter, ArrayList<Country> originalList)
        {
            super();
            this.adapter = adapter;
            this.originalList = (List<Country>) originalList.clone();
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

                for (final Country country : originalList)
                {
                    if (country.countryName.toLowerCase().startsWith(filterPattern))
                    {
                        filteredList.add(country);
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

            adapter.countryArrayList.clear();

            adapter.countryArrayList.addAll((ArrayList<Country>) results.values);

            Common.Log.i("? - adapter.countryArrayList.size()"+adapter.countryArrayList.size());
            Common.Log.i("? - adapter.countryArrayList.size()"+adapter.getItemCount());

            adapter.notifyDataSetChanged();


        }

    }






}



