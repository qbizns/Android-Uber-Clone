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
import com.tatx.partnerapp.pojos.Model;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Venkateswarlu SKP on 9/14/2016.
 */

public class CustomRecyclerViewAdapterCarModel extends RecyclerView.Adapter<CustomRecyclerViewAdapterCarModel.ViewHolder> implements Filterable
{
    private final ArrayList<Model> modelArrayList;

    private OnItemClickListener mListener;

    private CustomFilter customFilter;

    public CustomRecyclerViewAdapterCarModel(ArrayList<Model> modelArrayList)
    {
        this.modelArrayList = modelArrayList;

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
        return modelArrayList.size();
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

        holder.tvCarMake.setText(modelArrayList.get(position).model);

    }



    public Model getItem(int position) {
        return modelArrayList.get(position);
    }


    @Override
    public Filter getFilter()
    {

        if (customFilter == null)
        {
            customFilter = new CustomFilter(this, modelArrayList);
        }

        return customFilter;
    }

    private static class CustomFilter extends Filter
    {

        private final CustomRecyclerViewAdapterCarModel adapter;

        private final List<Model> originalList;

        private final List<Model> filteredList;

        private CustomFilter(CustomRecyclerViewAdapterCarModel adapter, ArrayList<Model> originalList)
        {
            super();
            this.adapter = adapter;
            this.originalList = (List<Model>) originalList.clone();
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

                for (final Model model : originalList)
                {
                    if (model.model.toLowerCase().startsWith(filterPattern))
                    {
                        filteredList.add(model);
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

            adapter.modelArrayList.clear();

            adapter.modelArrayList.addAll((ArrayList<Model>) results.values);

            Common.Log.i("? - adapter.modelArrayList.size()"+adapter.modelArrayList.size());
            Common.Log.i("? - adapter.modelArrayList.size()"+adapter.getItemCount());

            adapter.notifyDataSetChanged();


        }

    }






}



