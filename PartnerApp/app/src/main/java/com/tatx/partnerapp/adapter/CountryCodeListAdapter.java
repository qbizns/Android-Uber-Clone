package com.tatx.partnerapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.tatx.partnerapp.R;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.dataset.CountryCodePojo;


/**
 * Created by Subbu on 6/19/2015.
 */
public class CountryCodeListAdapter extends RecyclerView.Adapter<CountryCodeListAdapter.ViewHolder> {
    Context context;
    List<CountryCodePojo> objects;
    String code;
    private OnItemClickListener mListener;
    private byte[] imageBitmap;
    boolean nationality;



    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public CountryCodeListAdapter(Context context, List<CountryCodePojo> objects, boolean nationality) {
        this.context = context;
        this.objects = objects;
        this.nationality=nationality;

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
        @BindView(R.id.message)
        TextView message;
        @BindView(R.id.flag_image)
        ImageView flagImage;

        public ViewHolder(final View vi) {
            super(vi);
            ButterKnife.bind(this,vi);
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
            code = objects.get(position).getCountryName();
            holder.message.setText(code);

            if (nationality){
                holder.flagImage.setVisibility(View.GONE);
            }else {
                imageBitmap = objects.get(position).getFlag();
                if (imageBitmap != null) {
                    holder.flagImage.setImageBitmap(Common.byteToBitMap(imageBitmap));
                }

            }


        }

        }



