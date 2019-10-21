package com.tatx.partnerapp.library;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.adapter.CustomRecyclerViewAdapterCountries;
import com.tatx.partnerapp.pojos.Country;

import java.util.ArrayList;

/**
 * Created by Venkateswarlu SKP on 18-10-2016.
 */
public class CountriesListDialog
{

    private final Context context;
    private final ArrayList<Country> countries;

    private OnCountrySelectedListener onCountrySelectedListener;

    public CountriesListDialog(Context context, ArrayList<Country> countries)
    {

        this.context = context;

        this.countries = countries;

    }

    public void setOnCountrySelectedListener(OnCountrySelectedListener onCountrySelectedListener)
    {

        this.onCountrySelectedListener = onCountrySelectedListener;

        showCountriesListDialog(countries);

    }




    private void showCountriesListDialog(ArrayList<Country> countryArrayList)
    {

        final Dialog dialog = new Dialog(context);

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

    private void setCountryListData(final RecyclerView recyclerView, ArrayList<Country> countryArrayList, final Dialog dialog)
    {

/*
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {

                */
/*
                if(TextUtils.isEmpty(cetBankCountry.getText()))
                {

                    cetBankCountry.setText(" ");

                }
*//*

//                cetBankCountry.clearFocus();


                onCountrySelectedListener.onCountrySelected(null);


            }



        });
*/


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);

        recyclerView.setLayoutManager(linearLayoutManager);

        if (countryArrayList != null)
        {
            CustomRecyclerViewAdapterCountries customRecyclerViewAdapterCountries = new CustomRecyclerViewAdapterCountries(countryArrayList);

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
/*

                cetBankCountry.setText(country.countryName);

                cetBankCountry.setTag(country.countryId);

*/

                onCountrySelectedListener.onCountrySelected(country);


            }


        });


    }


    public interface OnCountrySelectedListener {
        void onCountrySelected(Country country);
    }
}
