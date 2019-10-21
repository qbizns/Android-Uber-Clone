package com.tatx.partnerapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.abstractclasses.BaseActivity;
import com.tatx.partnerapp.commonutills.Common;


import butterknife.ButterKnife;


/**
 * Created by Home on 18-05-2016.
 */
public class CommonWebViewActivity extends BaseActivity implements View.OnClickListener{
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private int userid;
    private Button cancel;
    private WebView webView;
    private String weburl;
    private TextView title;
    String tileText;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.select_payments));
        initialaziedAll();
    }

    public void initialaziedAll() {
        progressDialog=Common.showProgressDialog(this);
        Intent intent=getIntent();
        weburl=intent.getStringExtra("url");
        tileText=intent.getStringExtra("title");
        sp= PreferenceManager.getDefaultSharedPreferences(this);
        editor=sp.edit();
        userid=sp.getInt("userid",0);
        webView=(WebView)findViewById(R.id.web_view);
        title=(TextView)findViewById(R.id.tv_title_text);

        title.setText(tileText);

        webView.setWebViewClient(new MyBrowser());

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(weburl);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }

    public class MyBrowser extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, final String url) {
            Common.dismissProgressDialog(progressDialog);
        }
    }
}
