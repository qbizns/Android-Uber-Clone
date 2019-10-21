package com.tatx.userapp.menuactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.tatx.userapp.R;

import com.tatx.userapp.abstractclasses.BaseActivity;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;
import com.tatx.userapp.constants.ServiceUrls;
import com.tatx.userapp.customviews.CustomTextView;
import com.tatx.userapp.database.CountryCodeSqliteDbHelper;
import com.tatx.userapp.dataset.CountryCodePojo;
import com.tatx.userapp.enums.Language;
import com.tatx.userapp.network.RetrofitRequester;
import com.tatx.userapp.network.RetrofitResponseListener;
import com.tatx.userapp.pojos.ApiResponseVo;
import com.tatx.userapp.pojos.GetCustomerProfileVo;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DisplayProfileActivity extends BaseActivity implements RetrofitResponseListener {
    @BindView(R.id.first_name) TextView first_name;
    @BindView(R.id.last_name) TextView last_name;
    @BindView(R.id.et_email) EditText email;
    @BindView(R.id.phone_number) TextView phone_number;
    @BindView(R.id.tv_edit) TextView edit_profile;
    @BindView(R.id.profileImgView) ImageView profileImgView;
    @BindView(R.id.tv_tatx_id)
    CustomTextView tvTatxId;
    @BindView(R.id.ctv_language) TextView ctvLanguage;
    @BindView(R.id.ctv_emergency_name) TextView ctvEmergencyName;
    @BindView(R.id.ctv_emergency_phone_number) TextView ctvEmergencyPhoneNumber;
    @BindView(R.id.flag_image) ImageView flagImage;
    @BindView(R.id.tv_country_code) TextView tvCountryCode;
    @BindView(R.id.iv_emergency_contact_flag) ImageView ivEmergencyContactFlag;
    @BindView(R.id.ctv_emergency_country_code) TextView ctvEmergencyCountryCode;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.display_profile_activity);

        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.view_profile));

        initialaizedAll();


        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.GET_CUSTOMER_PROFILE,null);



    }

    public void initialaizedAll()
    {




    }

    private void updateUI(final GetCustomerProfileVo getCustomerProfileVo)
    {


        first_name.setText(getCustomerProfileVo.firstName);
        last_name.setText(getCustomerProfileVo.lastName);
        email.setText(getCustomerProfileVo.email);
        tvTatxId.setText(getCustomerProfileVo.tatxId);

        CountryCodeSqliteDbHelper dbHelper = new CountryCodeSqliteDbHelper(this);

        dbHelper.openDataBase();

        Common.Log.i("? - getCustomerProfileVo.getCustomerCountryCode() : "+getCustomerProfileVo.country);



        String customerCountryCode = getCustomerProfileVo.country;
        CountryCodePojo countryCodePojo = dbHelper.getDetailsByCountryCode(customerCountryCode);
        flagImage.setImageBitmap(Common.getBitmapFromByteArray(countryCodePojo.getFlag()));
        tvCountryCode.setText(customerCountryCode == null ? "":"+"+customerCountryCode);
        phone_number.setText(customerCountryCode == null ? getCustomerProfileVo.phoneNumber :getCustomerProfileVo.phoneNumber.substring(customerCountryCode.length()));

        Common.Log.i("? - getCustomerProfileVo.getLanguageCode() : "+getCustomerProfileVo.language);

        ctvLanguage.setText(Language.getEnumFieldByLanguageCode(Integer.parseInt(getCustomerProfileVo.language)).getLanguageName());


        String emergencyCountryCode = getCustomerProfileVo.emergencyCountryCode;
        if (emergencyCountryCode!=null) {
            CountryCodePojo emergencyCountryCodePojo = dbHelper.getDetailsByCountryCode(emergencyCountryCode);
            ivEmergencyContactFlag.setImageBitmap(Common.getBitmapFromByteArray(emergencyCountryCodePojo.getFlag()));
            ctvEmergencyCountryCode.setText(emergencyCountryCode == null ? "" : "+" + emergencyCountryCode);
            ctvEmergencyPhoneNumber.setText(getCustomerProfileVo.emergencyMobile);
            ctvEmergencyName.setText(getCustomerProfileVo.emergencyName);

        }else{
            ivEmergencyContactFlag.setImageBitmap(Common.getBitmapFromByteArray(countryCodePojo.getFlag()));
            ctvEmergencyCountryCode.setText(customerCountryCode == null ? "" : "+" + customerCountryCode);
        }

        Common.setCircleImageBackgroundFromUrl(this,profileImgView,getCustomerProfileVo.profilePic);

        Common.Log.i("? - getCustomerProfileVo.getLanguageCode() : final");


        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DisplayProfileActivity.this,UpdateProfileActivity.class);

                intent.putExtra(Constants.KEY_1,getCustomerProfileVo);

                startActivity(intent);

                finish();

            }
        });




    }


    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId)
    {


        if (apiResponseVo.code != Constants.SUCCESS)
        {

            Common.customToast(this, apiResponseVo.status);

            switch(apiResponseVo.requestname)
            {
                case ServiceUrls.RequestNames.GET_CUSTOMER_PROFILE:
                    finish();
                    break;
            }




            return;


        }


        switch (apiResponseVo.requestname)
        {

            case ServiceUrls.RequestNames.GET_CUSTOMER_PROFILE:

                GetCustomerProfileVo getCustomerProfileVo = Common.getSpecificDataObject(apiResponseVo.data, GetCustomerProfileVo.class);

                updateUI(getCustomerProfileVo);

//                Common.customToast(this, apiResponseVo.status);

                break;
        }


    }



}
