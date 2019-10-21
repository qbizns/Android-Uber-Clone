package com.tatx.partnerapp.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.abstractclasses.BaseActivity;
import com.tatx.partnerapp.adapter.CustomRecyclerViewAdapterBankNames;
import com.tatx.partnerapp.adapter.CustomRecyclerViewAdapterCarMake;
import com.tatx.partnerapp.adapter.CustomRecyclerViewAdapterCountries;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.constants.ServiceUrls;
import com.tatx.partnerapp.customviews.CustomFloatingLabelEditText;
import com.tatx.partnerapp.network.RetrofitRequester;
import com.tatx.partnerapp.network.RetrofitResponseListener;
import com.tatx.partnerapp.pojos.ApiResponseVo;
import com.tatx.partnerapp.pojos.Bank;
import com.tatx.partnerapp.pojos.Country;
import com.tatx.partnerapp.pojos.GetBanksVo;
import com.tatx.partnerapp.pojos.GetCountriesVo;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Home on 15-09-2016.
 */
public class AddBankDetailsActivity extends BaseActivity implements RetrofitResponseListener,CustomFloatingLabelEditText.OnFloatingLabelEditTextClickListener {

    @BindView(R.id.cflet_country)
    CustomFloatingLabelEditText cfletCountry;
    @BindView(R.id.cflet_bank_name)
    CustomFloatingLabelEditText cfletBankName;
    @BindView(R.id.account_number)
    EditText accountNumber;
    @BindView(R.id.confirm_account_no)
    EditText confirmAccountNo;
    @BindView(R.id.cet_bank_unique_code)
    EditText cetBankUniqueCode;
    @BindView(R.id.cet_bank_unique_code_confirm)
    EditText cetBankUniqueCodeConfirm;
    @BindView(R.id.btn_save)
    Button saveAccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank_account);
        ButterKnife.bind(this);
        setTitleText(Common.getStringResourceText(R.string.add_bank_account));

        cfletCountry.setOnFloatingLabelEditTextClickListener(this,null);

        cfletBankName.setOnFloatingLabelEditTextClickListener(this,null);




    }

    @OnClick (R.id.btn_save)
    void setAccountNumber()
    {
        if (cfletBankName.getText().toString().trim().length()<2){
            cfletBankName.setError(Common.getStringFromResources(R.string.please_enter_bank_name));
            cfletBankName.requestFocus();
        }else if(accountNumber.getText().toString().trim().length()<2){
            accountNumber.setError(Common.getStringFromResources(R.string.please_account_number));
            accountNumber.requestFocus();
        }else if(confirmAccountNo.getText().toString().trim().length()<2){
            confirmAccountNo.setError("Please confirm your account number.");
            confirmAccountNo.requestFocus();
        }else if(!accountNumber.getText().toString().trim().equalsIgnoreCase(confirmAccountNo.getText().toString().trim())){
            confirmAccountNo.setError("Mismatch account number.");
            confirmAccountNo.requestFocus();
        }
        else if(!cetBankUniqueCode.getText().toString().trim().equalsIgnoreCase(cetBankUniqueCodeConfirm.getText().toString().trim()))
        {
            cetBankUniqueCodeConfirm.setError("Mismatch Bank Unique Code.");
            cetBankUniqueCodeConfirm.requestFocus();
        }
        else {

            HashMap<String, String> requestParams = new HashMap();
            requestParams.put(ServiceUrls.ApiRequestParams.COUNTRY_ID, String.valueOf(cfletCountry.getTag()));
            requestParams.put(ServiceUrls.ApiRequestParams.BANK_ID, String.valueOf(cfletBankName.getTag()));
            requestParams.put(ServiceUrls.ApiRequestParams.ACCOUNT_NO, confirmAccountNo.getText().toString().trim());
            requestParams.put(ServiceUrls.ApiRequestParams.BANK_UNIQUE_CODE, cetBankUniqueCodeConfirm.getText().toString().trim());

            new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.ADD_BANK_ACCOUNT, requestParams);
        }
    }
    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId) {
        if (apiResponseVo.code != Constants.SUCCESS)
        {
            Common.customToast(this, apiResponseVo.status);
            return;
        }

        switch (apiResponseVo.requestname)
        {
            case ServiceUrls.RequestNames.ADD_BANK_ACCOUNT:
                Intent intent = new Intent(this, GoogleMapDrawerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;

            case ServiceUrls.RequestNames.GET_COUNTRIES:

                GetCountriesVo getCountriesVo = Common.getSpecificDataObject(apiResponseVo.data, GetCountriesVo.class);

                showCountriesListDialog((ArrayList<Country>) getCountriesVo.countries);

                break;

            case ServiceUrls.RequestNames.GET_BANKS:

                GetBanksVo getBanksVo = Common.getSpecificDataObject(apiResponseVo.data, GetBanksVo.class);

                showBanksListDialog((ArrayList<Bank>) getBanksVo.bank);

                break;

        }


    }

    private void showBanksListDialog(ArrayList<Bank> bankArrayList)
    {




        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.country_code_dialog);

        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.my_recycler_view);

        Button close = (Button) dialog.findViewById(R.id.button);

        EditText search = (EditText) dialog.findViewById(R.id.search_button);

        setBankListData(recyclerView,bankArrayList,dialog);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        search.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count)
            {

                ((CustomRecyclerViewAdapterCarMake)recyclerView.getAdapter()).getFilter().filter(charSequence);


            }


            @Override
            public void afterTextChanged(Editable s)
            {

            }


        });





        dialog.show();


    }

    private void setBankListData(final RecyclerView recyclerView, ArrayList<Bank> bankArrayList, final Dialog dialog)
    {




        dialog.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {

                if(TextUtils.isEmpty(cfletBankName.getText()))
                {

                    cfletBankName.setText(" ");



                }

                cfletBankName.clearFocus();



            }



        });



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);

        if (bankArrayList != null)
        {
            CustomRecyclerViewAdapterBankNames customRecyclerViewAdapterBankNames = new CustomRecyclerViewAdapterBankNames(bankArrayList);

            recyclerView.setAdapter(customRecyclerViewAdapterBankNames);

        }
        else
        {
            recyclerView.setVisibility(View.GONE);
        }


        ((CustomRecyclerViewAdapterBankNames)recyclerView.getAdapter()).setOnItemClickListener(new CustomRecyclerViewAdapterBankNames.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {

                if (dialog != null)
                {
                    dialog.cancel();
                }

                cfletBankName.setText(((CustomRecyclerViewAdapterBankNames)recyclerView.getAdapter()).getItem(position).bankName);

                cfletBankName.setTag(((CustomRecyclerViewAdapterBankNames)recyclerView.getAdapter()).getItem(position).bankId);


            }


        });






    }

    private void showCountriesListDialog(ArrayList<Country> countryArrayList)
    {

        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.country_code_dialog);

        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.my_recycler_view);

        Button close = (Button) dialog.findViewById(R.id.button);

        EditText search = (EditText) dialog.findViewById(R.id.search_button);

        setCountryListData(recyclerView,countryArrayList,dialog);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        search.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count)
            {

                ((CustomRecyclerViewAdapterCountries)recyclerView.getAdapter()).getFilter().filter(charSequence);


            }


            @Override
            public void afterTextChanged(Editable s)
            {

            }


        });


        dialog.show();



    }

    private void setCountryListData(final RecyclerView recyclerView, ArrayList<Country> makeList, final Dialog dialog)
    {

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {

                if(TextUtils.isEmpty(cfletCountry.getText()))
                {

                    cfletCountry.setText(" ");

                }

                cfletCountry.clearFocus();



            }



        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);

        if (makeList != null)
        {
            CustomRecyclerViewAdapterCountries customRecyclerViewAdapterCountries = new CustomRecyclerViewAdapterCountries(makeList);

            recyclerView.setAdapter(customRecyclerViewAdapterCountries);

        }
        else
        {
            recyclerView.setVisibility(View.GONE);
        }


        ((CustomRecyclerViewAdapterCountries)recyclerView.getAdapter()).setOnItemClickListener(new CustomRecyclerViewAdapterCountries.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {

                if (dialog != null)
                {
                    dialog.cancel();
                }

                Country country = ((CustomRecyclerViewAdapterCountries) recyclerView.getAdapter()).getItem(position);

                cfletCountry.setText(country.countryName);

                cfletCountry.setTag(country.countryId);




            }


        });


    }

    @Override
    public void onFloatingLabelEditTextClick(CustomFloatingLabelEditText customFloatingLabelEditText)
    {

        switch (customFloatingLabelEditText.getId())
        {
            case R.id.cflet_country:
                getCountriesData();
                break;

            case R.id.cflet_bank_name:
                getBanksData();
                break;

        }


    }

    private void getBanksData()
    {


        Common.Log.i("cfletCountry.getTag() : "+ cfletCountry.getTag());

        if (cfletCountry.getTag()!= null)
        {

            HashMap<String, String> params = new HashMap<>();

            params.put(ServiceUrls.ApiRequestParams.COUNTRY_ID, String.valueOf(cfletCountry.getTag()));

            new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.GET_BANKS,params);

        }
        else
        {

            Common.customToast(this,"Please Select Car Make First.");

            cfletCountry.clearFocus();

        }




    }

    private void getCountriesData()
    {
        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.GET_COUNTRIES,null);

    }

}
