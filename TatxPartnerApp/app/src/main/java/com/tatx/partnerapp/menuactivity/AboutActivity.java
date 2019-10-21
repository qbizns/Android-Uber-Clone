package com.tatx.partnerapp.menuactivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.abstractclasses.BaseActivity;
import com.tatx.partnerapp.activities.CommonWebViewActivity;
import com.tatx.partnerapp.commonutills.Common;

import butterknife.ButterKnife;

/**
 * Created by user on 01-05-2016.
 */
public class AboutActivity extends BaseActivity implements View.OnClickListener {
    private TextView appversion;
    private TextView versioncode;
    private RelativeLayout backButton;
    private String currentVersion;
    private TextView terms;
    private TextView rateongoogleplay;
    private TextView website;
    private String URL_TERMS;
    private String URL_WEBSITE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCurrentVersion();
        setContentView(R.layout.about);
        ButterKnife.bind(this);
        setTitleText(Common.getStringResourceText(R.string.about));
        initilaizedAll();

    }

    public void initilaizedAll() {

        appversion = (TextView) findViewById(R.id.appversion);
        terms = (TextView) findViewById(R.id.terms);
        rateongoogleplay = (TextView) findViewById(R.id.rateongoogleplay);
        website = (TextView) findViewById(R.id.website);

        appversion.setOnClickListener(this);
        terms.setOnClickListener(this);
        rateongoogleplay.setOnClickListener(this);
        website.setOnClickListener(this);


        versioncode = (TextView) findViewById(R.id.versioncode);
        versioncode.setText(currentVersion);
        URL_TERMS="http://tatx.com/"+getResources().getConfiguration().locale.getLanguage()+"/index.php/terms/";
        URL_WEBSITE="http://tatx.com/"+getResources().getConfiguration().locale.getLanguage();
        Common.Log.i("? - URL_TERMS : "+URL_TERMS+ "\n URL_WEBSITE "+URL_WEBSITE);

    }

    private void getCurrentVersion() {
        PackageManager pm = this.getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo = pm.getPackageInfo(this.getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        currentVersion = pInfo.versionName;
        System.out.println("? - My local app version====>>>>>" + currentVersion);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.terms:
                Intent intent2 = new Intent(this, CommonWebViewActivity.class);
                intent2.putExtra("url", URL_TERMS);
                intent2.putExtra("title", Common.getStringResourceText(R.string.terms));
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
                Intent intent1 = new Intent(this, CommonWebViewActivity.class);
                intent1.putExtra("url", URL_WEBSITE);
                intent1.putExtra("title", Common.getStringResourceText(R.string.about));
                startActivity(intent1);


                break;

            case R.id.appversion:
                // showAppinfo();
                break;

        }

    }
}