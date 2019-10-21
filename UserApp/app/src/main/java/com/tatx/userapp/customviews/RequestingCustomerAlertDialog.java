package com.tatx.userapp.customviews;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.tatx.userapp.R;
import com.tatx.userapp.interfaces.DialogClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Home on 19-09-2016.
 */
public class RequestingCustomerAlertDialog extends Dialog {

    @BindView(R.id.title)
    TextView tvTitle;
    String title;


    public RequestingCustomerAlertDialog(Context context, String title) {
        super(context);

        this.title=title;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.requesting_custom_alert_dialog);
        ButterKnife.bind(this);
        tvTitle.setText(title);
        init();


    }


    public void init(){
        getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

    }


}
