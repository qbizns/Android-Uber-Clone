package com.tatx.userapp.menuactivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.tatx.userapp.R;

import com.tatx.userapp.activities.AddCreditWebViewActivity;
import com.tatx.userapp.abstractclasses.BaseActivity;
import com.tatx.userapp.commonutills.Common;

import butterknife.ButterKnife;

/**
 * Created by user on 01-05-2016.
 */
public class AboutActivity extends BaseActivity implements View.OnClickListener{
// RelativeLayout back;
    private Button appversion;
    private Button terms;
    private Button rateongoogleplay;
    private Button website;

    PackageInfo pInfo = null;
    private int verCode;
    private String URL_TERMS;
    private String URL_WEBSITE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.about));

        initilaiedAll();
    }

   public void initilaiedAll(){

       try {
           pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
           verCode = pInfo.versionCode;
       } catch (PackageManager.NameNotFoundException e) {
           e.printStackTrace();
       }
       String version = pInfo.versionName;
           appversion = (Button) findViewById(R.id.appversion);
           terms = (Button) findViewById(R.id.terms);
           rateongoogleplay = (Button) findViewById(R.id.rateongoogleplay);
           website=(Button)findViewById(R.id.website);

       appversion.setOnClickListener(this);
       terms.setOnClickListener(this);
       rateongoogleplay.setOnClickListener(this);
       website.setOnClickListener(this);
       Log.d("locale",getResources().getConfiguration().locale.getLanguage());
       URL_TERMS="http://tatx.com/"+getResources().getConfiguration().locale.getLanguage()+"/index.php/terms/";
       URL_WEBSITE="http://tatx.com/"+getResources().getConfiguration().locale.getLanguage();
       Common.Log.i("? - URL_TERMS : "+URL_TERMS+ "\n URL_WEBSITE "+URL_WEBSITE);

   }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.terms:
                Intent intent2=new Intent(this,AddCreditWebViewActivity.class);
                intent2.putExtra("url",URL_TERMS);
                intent2.putExtra("title",Common.getStringResourceText(R.string.terms));
                startActivity(intent2);
                break;
            case R.id.rateongoogleplay:

                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                break;
            case R.id.website:
                Intent intent1=new Intent(this,AddCreditWebViewActivity.class);
                intent1.putExtra("url",URL_WEBSITE);
                intent1.putExtra("title",Common.getStringResourceText(R.string.about));
                startActivity(intent1);

                break;

            case R.id.appversion:
       // showAppinfo();
                break;

        }
    }


}
