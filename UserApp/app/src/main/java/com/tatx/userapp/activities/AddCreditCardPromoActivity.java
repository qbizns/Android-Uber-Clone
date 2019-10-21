package com.tatx.userapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.tatx.userapp.R;
import com.tatx.userapp.commonutills.Common;


/**
 * Created by Home on 18-05-2016.
 */
public class AddCreditCardPromoActivity extends AppCompatActivity implements View.OnClickListener{
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private int userid;
    private Button cancel;
    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_credit_promo);
        initialaziedAll();
    }

    public void initialaziedAll() {
        sp= PreferenceManager.getDefaultSharedPreferences(this);
        editor=sp.edit();
        userid=sp.getInt("userid",0);
        cancel=(Button)findViewById(R.id.add_credit_card);
        imageView=(ImageView)findViewById(R.id.imageView);

        cancel.setOnClickListener(this);
        imageView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.add_credit_card:
                Intent intent=new Intent(this,AddCreditCardActivity.class);
                startActivity(intent);
                finish();

                break;
            case R.id.imageView:
                Intent intent1=new Intent(this,AddCreditWebViewActivity.class);
                intent1.putExtra("title", Common.getStringResourceText(R.string.select_payments));
                intent1.putExtra("url","http://tatx.com");
                startActivity(intent1);
                finish();
                break;

        }
    }
}
