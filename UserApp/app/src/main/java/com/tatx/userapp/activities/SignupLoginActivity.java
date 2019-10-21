package com.tatx.userapp.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.tatx.userapp.R;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;
import com.tatx.userapp.customviews.CustomerAlertDialog;
import com.tatx.userapp.interfaces.DialogClickListener;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SignupLoginActivity extends AppCompatActivity {
    Button login, signUp;
    @BindView(R.id.spinner1)
    Spinner spnChangeLanguage;
    String[] Languages = {"English", "Arabic"};

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup_login);
        ButterKnife.bind(this);

        Locale defaultLocale = Locale.getDefault();
        String language = defaultLocale.getLanguage();
//        Common.getDefaultSP(SignupLoginActivity.this).edit().putString(Constants.SharedPreferencesKeys.LANGUAGE, language).commit();

        Common.Log.i("????? - language " + language);

        // Setting a Custom Adapter to the Spinner
        spnChangeLanguage.setAdapter(new MyAdapter(this, R.layout.custom_spinner_language, Languages));

        if (language.equalsIgnoreCase("ar"))
        {

            spnChangeLanguage.setSelection(1, true);

            Common.Log.i("????? - spnChangeLanguage.setSelection(1, true)");


        }
        else
        {
            spnChangeLanguage.setSelection(0, true);

            Common.Log.i("????? - spnChangeLanguage.setSelection(0, true)");

        }

        View v = spnChangeLanguage.getSelectedView();

        Common.Log.i("????? - Selected View Id : " + v.getId());

        ((TextView) v).setTextColor(Common.getColorFromResource(R.color.white));



        spnChangeLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                Common.Log.i("????? - onItemSelected H " + view.getId() + " pos " + position);

                String lang = ((position == 1) ? "ar" : "en");

                Common.getDefaultSP(SignupLoginActivity.this).edit().putString(Constants.SharedPreferencesKeys.LANGUAGE, lang).commit();

//                Common.setLanguage(SignupLoginActivity.this, lang);

                ((TextView) parent.getChildAt(0)).setTextColor(Common.getColorFromResource(R.color.white));

/*
                Intent intent = getIntent();
                finish();
                startActivity(intent);
*/


                Common.Log.i("????? - savedInstanceState.toString() : "+ ((savedInstanceState == null) ?"null":savedInstanceState.toString()));

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
 },100);


    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Common.Log.i("????? - newConfig : "+newConfig.toString());

    }

    @OnClick(R.id.sign)
    void setSign()
    {
        Intent i = new Intent(SignupLoginActivity.this, LoginActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.btn_create_account)
    void setBecomeDriver()
    {
        Intent i = new Intent(SignupLoginActivity.this, RegistrationActivity.class);
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
