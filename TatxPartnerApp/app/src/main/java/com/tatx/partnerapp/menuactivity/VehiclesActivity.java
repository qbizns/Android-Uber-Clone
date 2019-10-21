package com.tatx.partnerapp.menuactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.abstractclasses.BaseActivity;
import com.tatx.partnerapp.activities.AddCarActivity;
import com.tatx.partnerapp.adapter.VehiclesListAdapter;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.constants.ServiceUrls;
import com.tatx.partnerapp.network.RetrofitRequester;
import com.tatx.partnerapp.network.RetrofitResponseListener;
import com.tatx.partnerapp.pojos.ApiResponseVo;
import com.tatx.partnerapp.pojos.Car;
import com.tatx.partnerapp.pojos.ChangeCarVo;
import com.tatx.partnerapp.pojos.DeleteCar;
import com.tatx.partnerapp.pojos.GetCarVo;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Home on 17-05-2016.
 */
public class VehiclesActivity extends BaseActivity implements RetrofitResponseListener
{
    TextView pgtitle;
    VehiclesListAdapter vehiclesListAdapter;
    GridLayoutManager gridLayoutManager;
    private TextView recordnotfnd;
    RecyclerView savedLocationRecyclerView;
    private TextView tvAddVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles);
        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.my_vehicles));
        initilaizedAll();
    }

    public void initilaizedAll() {

        recordnotfnd = (TextView) findViewById(R.id.recordnotfndnotify);
        savedLocationRecyclerView = (RecyclerView) findViewById(R.id.saved_location_recycler_view_notify);
        tvAddVehicle=(TextView)findViewById(R.id.tv_add_vehicle);


        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.GET_CAR,null);

        tvAddVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(VehiclesActivity.this, AddCarActivity.class),Constants.RequestCodes.ONE);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

            if(requestCode == Constants.RequestCodes.ONE && resultCode == Constants.ResultCodes.SUCCESS)
            {

                new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.GET_CAR,null);

            }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setListOnAdapter(final List<Car> car)
    {
        gridLayoutManager = new GridLayoutManager(this, 1);
        savedLocationRecyclerView.setLayoutManager(gridLayoutManager);


        vehiclesListAdapter = new VehiclesListAdapter(this, car);
        savedLocationRecyclerView.setAdapter(vehiclesListAdapter);

        vehiclesListAdapter.notifyDataSetChanged();


        if (car == null || car.size() == 0)
        {

            recordnotfnd.setVisibility(View.VISIBLE);
        }
        else
        {
            recordnotfnd.setVisibility(View.GONE);

        }

    }

    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId) {
        if (apiResponseVo.code != Constants.SUCCESS)
        {
            switch (apiResponseVo.requestname)
            {
                case ServiceUrls.RequestNames.CHANGE_CAR:

                    ChangeCarVo changeCarVo = Common.getSpecificDataObject(apiResponseVo.data, ChangeCarVo.class);

                    Common.customToast(this, changeCarVo.message.replace("\\n","\n"));

                    break;

                default:
                    Common.customToast(this, apiResponseVo.status);
                    break;
            }

            return;
        }

        switch (apiResponseVo.requestname)
        {

            case ServiceUrls.RequestNames.GET_CAR:

                GetCarVo getCarVo = Common.getSpecificDataObject(apiResponseVo.data, GetCarVo.class);

                setListOnAdapter(getCarVo.cars);

                break;


            case ServiceUrls.RequestNames.DELETE_CAR:

                DeleteCar deleteCar = Common.getSpecificDataObject(apiResponseVo.data, DeleteCar.class);

                Common.customToast(this,deleteCar.message);

                new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.GET_CAR,null);



                break;

            case ServiceUrls.RequestNames.CHANGE_CAR:

                ChangeCarVo changeCarVo = Common.getSpecificDataObject(apiResponseVo.data, ChangeCarVo.class);

                Common.customToast(this,changeCarVo.message);

                new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.GET_CAR,null);



                break;


        }

    }
}
