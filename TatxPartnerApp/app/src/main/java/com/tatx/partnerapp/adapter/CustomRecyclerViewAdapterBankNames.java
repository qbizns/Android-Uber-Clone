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
import com.tatx.partnerapp.pojos.Bank;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Venkateswarlu SKP on 9/14/2016.
 */

public class CustomRecyclerViewAdapterBankNames extends RecyclerView.Adapter<CustomRecyclerViewAdapterBankNames.ViewHolder> implements Filterable
{
    private final ArrayList<Bank> bankArrayList;

    private OnItemClickListener mListener;

    private CustomFilter customFilter;

    public CustomRecyclerViewAdapterBankNames(ArrayList<Bank> bankArrayList)
    {
        this.bankArrayList = bankArrayList;

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
        return bankArrayList.size();
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

        holder.tvName.setText(bankArrayList.get(position).bankName);

    }



    public Bank getItem(int position) {
        return bankArrayList.get(position);
    }


    @Override
    public Filter getFilter()
    {

        if (customFilter == null)
        {
            customFilter = new CustomFilter(this, bankArrayList);
        }

        return customFilter;
    }

    private static class CustomFilter extends Filter
    {

        private final CustomRecyclerViewAdapterBankNames adapter;

        private final List<Bank> originalList;

        private final List<Bank> filteredList;

        private CustomFilter(CustomRecyclerViewAdapterBankNames adapter, ArrayList<Bank> originalList)
        {
            super();
            this.adapter = adapter;
            this.originalList = (List<Bank>) originalList.clone();
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

                for (final Bank bank : originalList)
                {
                    if (bank.bankName.toLowerCase().startsWith(filterPattern))
                    {
                        filteredList.add(bank);
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

            adapter.bankArrayList.clear();

            adapter.bankArrayList.addAll((ArrayList<Bank>) results.values);

            Common.Log.i("? - adapter.bankArrayList.size()"+adapter.bankArrayList.size());
            Common.Log.i("? - adapter.bankArrayList.size()"+adapter.getItemCount());

            adapter.notifyDataSetChanged();


        }

    }






}



