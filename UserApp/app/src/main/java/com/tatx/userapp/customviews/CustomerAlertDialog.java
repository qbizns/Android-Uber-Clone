package com.tatx.userapp.customviews;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
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
public class CustomerAlertDialog extends Dialog {
    @BindView(R.id.cancel)
    Button cancel;
    @BindView(R.id.ok)
    Button ok;
    @BindView(R.id.title)
    TextView tvTitle;
    String title;
    DialogClickListener dialogClickListener;

    public CustomerAlertDialog(Context context,String title, DialogClickListener dialogClickListener) {
        super(context);
        this.dialogClickListener=dialogClickListener;
        this.title=title;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_alert_dialog);
        ButterKnife.bind(this);
        tvTitle.setText(title);
        init();


    }
    @OnClick(R.id.cancel)
    void cancelClicked(){
        dialogClickListener.onCancelClick();
    }
    @OnClick(R.id.ok)
    void okClicked(){
        dialogClickListener.onOkClick();
    }

    public void init(){
        getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

    }

    public void setTvTitle(String title){
        tvTitle.setText(title);
    }
}
