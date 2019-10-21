package com.tatx.partnerapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tatx.partnerapp.R;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.constants.ServiceUrls;
import com.tatx.partnerapp.menuactivity.VehiclesActivity;
import com.tatx.partnerapp.network.RetrofitRequester;
import com.tatx.partnerapp.pojos.Car;

import java.util.HashMap;
import java.util.List;


/**
 * Created by Subbu on 6/19/2015.
 */
public class VehiclesListAdapter extends RecyclerView.Adapter<VehiclesListAdapter.ViewHolder> {
    List<Car> cars;
    private OnItemClickListener mListener;
    Context context;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public VehiclesListAdapter(Context context,  List<Car> cars) {
        this.cars = cars;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vehicles_list_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        v.setTag(i);
        return vh;
    }

    @Override

    public void onBindViewHolder(ViewHolder holder, final int i)
    {
        holder.carName.setText(cars.get(i).model);

        holder.plateNo.setText(cars.get(i).vehicleNo);

        if(Integer.parseInt(cars.get(i).primary) == Constants.PRIMARY)
        {
/*

            holder.carName.setTextColor(context.getResources().getColor(R.color.green));
            holder.plateNo.setTextColor(context.getResources().getColor(R.color.green));
*/

            holder.carName.setTypeface(null, Typeface.BOLD);
            holder.plateNo.setTypeface(null, Typeface.BOLD);

        }


        Picasso.with(context).load(cars.get(i).image).into(holder.carImage);

        holder.ivDeleteCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {




                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setMessage("Do You Want to Delete this Car ?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                        HashMap<String, String> params = new HashMap<>();

                        params.put(ServiceUrls.ApiRequestParams.CAR_ID, String.valueOf(cars.get(i).id));

                        new RetrofitRequester((VehiclesActivity)context).sendStringRequest(ServiceUrls.RequestNames.DELETE_CAR,params);


                    }



                });


                builder.setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });



                builder.create().show();



            }
        });


        holder.rowView.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {


                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setMessage("Do You Want to Set this Car as Primary Car ?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                        HashMap<String, String> params = new HashMap<>();

                        params.put(ServiceUrls.ApiRequestParams.CAR_ID, String.valueOf(cars.get(i).id));

                        new RetrofitRequester((VehiclesActivity)context).sendStringRequest(ServiceUrls.RequestNames.CHANGE_CAR,params);


                    }



                });


                builder.setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });



                builder.create().show();


                return true;
            }
        });








    }

    @Override
    public int getItemCount() {
        // return productDetailses.size();
        return (null != cars ? cars.size() : 0);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final View rowView;
        TextView carName,plateNo;
        ImageView carImage;
        ImageView ivDeleteCar;

        public ViewHolder(final View rowView)
        {

            super(rowView);

            this.rowView = rowView;

            carName = (TextView) rowView.findViewById(R.id.tv_car_name);

            plateNo = (TextView) rowView.findViewById(R.id.tv_plate_no);

            carImage = (ImageView) rowView.findViewById(R.id.car_img);

            ivDeleteCar = (ImageView) rowView.findViewById(R.id.iv_delete_car);




        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }
    }
}
