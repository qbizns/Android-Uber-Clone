package com.tatx.partnerapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.abstractclasses.BaseActivity;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SignupLoginActivity extends BaseActivity {

    @BindView(R.id.spinner1)
    Spinner spnChangeLanguage;
    @BindView(R.id.sign)
    Button sign;
    @BindView(R.id.bcomdriver)
    Button bcomdriver;


    String[] Languages = {"English", "Arabic"};

    public static SignupLoginActivity getInstance() {
        return instance;
    }

    private static SignupLoginActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_registration);
        ButterKnife.bind(this);
        instance = this;
        Locale defaultLocale = Locale.getDefault();
        String language = defaultLocale.getLanguage();
     //   Common.getDefaultSP(SignupLoginActivity.this).edit().putString(Constants.SharedPreferencesKeys.LANGUAGE, language).commit();
        Common.Log.i("language " + language);

        // Setting a Custom Adapter to the Spinner
        spnChangeLanguage.setAdapter(new MyAdapter(this, R.layout.custom_spinner_language,
                Languages));

        if (language.equalsIgnoreCase("ar"))
        {
            spnChangeLanguage.setSelection(1, true);
        } else {
            spnChangeLanguage.setSelection(0, true);
        }

        View v = spnChangeLanguage.getSelectedView();
        Common.Log.i("onItemSelected H " + v.getId());

        ((TextView) v).setTextColor(Common.getColorFromResource(R.color.white));

        spnChangeLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Common.Log.i("onItemSelected H " + view.getId() + " pos " + position);

                String lang = position == 1 ? "ar" : "en";

                Common.getDefaultSP(SignupLoginActivity.this).edit().putString(Constants.SharedPreferencesKeys.LANGUAGE, lang).commit();

                ((TextView) parent.getChildAt(0)).setTextColor(Common.getColorFromResource(R.color.white));


                if ((position == 1 && Locale.getDefault().getLanguage().equalsIgnoreCase("ar")) || (position == 0 && Locale.getDefault().getLanguage().equalsIgnoreCase("en")))
                {

                    Common.Log.i("????? - Same Locale.");

                }
                else
                {





                    recreate();

                    Common.setLanguage(SignupLoginActivity.this, lang);

                    Common.Log.i("????? - After recreate().");


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((TextView) spnChangeLanguage.getSelectedView()).setTextColor(Common.getColorFromResource(R.color.white));
            }
        }, 100);


    }

    @OnClick(R.id.sign)
    void setSign() {
        Intent i = new Intent(SignupLoginActivity.this, LoginActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.bcomdriver)
    void setBecomeDriver() {
        Intent i = new Intent(SignupLoginActivity.this, RegistrationActivity1.class);
        startActivity(i);
    }


    // Creating an Adapter Class
    public class MyAdapter extends BaseAdapter implements SpinnerAdapter {
        Context context;
        int textViewResourceId;
        String[] objects;

        public MyAdapter(Context context, int textViewResourceId,
                         String[] objects) {
            this.objects = objects;
            this.context = context;
            this.textViewResourceId = textViewResourceId;

        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {

            // Inflating the layout for the custom Spinner
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_spinner_language, parent, false);
            ((TextView) layout).setGravity(Gravity.CENTER);

            // Declaring and Typecasting the textview in the inflated layout
            TextView tvLanguage = (TextView) layout
                    .findViewById(R.id.tvLanguage);

            // Setting the text using the array
            tvLanguage.setText(Languages[position]);


            return layout;
        }

        // It gets a View that displays in the drop down popup the data at the specified position
        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {

            Common.Log.i("onItemSelected I 1" + " pos " + position);
            return getCustomView(position, convertView, parent);
        }

        @Override
        public int getCount() {
            return objects.length;
        }

        @Override
        public Object getItem(int position) {
            return objects[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        // It gets a View that displays the data at the specified position
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Common.Log.i("onItemSelected J 2" + " pos " + position);

            return getCustomView(position, convertView, parent);
        }
    }


}
