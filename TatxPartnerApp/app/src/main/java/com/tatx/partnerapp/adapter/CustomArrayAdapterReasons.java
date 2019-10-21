package com.tatx.partnerapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.pojos.Reason;

import java.util.List;

/**
 * Created by Venkateswarlu SKP on 25-10-2016.
 */
public class CustomArrayAdapterReasons extends ArrayAdapter
{
    private final int reasonsItem;
    private final List<Reason> reasons;

    public CustomArrayAdapterReasons(Context context, int reasonsItem, List<Reason> reasons)
    {
        super(context,reasonsItem,reasons);

        this.reasonsItem = reasonsItem;

        this.reasons = reasons;



    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View inflatedView = LayoutInflater.from(getContext()).inflate(reasonsItem,null);

        TextView tvReason = (TextView) inflatedView.findViewById(R.id.tv_reason);

        tvReason.setText(reasons.get(position).reason);
        tvReason.setTag(reasons.get(position).reasonId);


        return inflatedView;
    }
}
