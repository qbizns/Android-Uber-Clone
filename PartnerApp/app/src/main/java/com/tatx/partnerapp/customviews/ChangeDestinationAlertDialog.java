package com.tatx.partnerapp.customviews;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


import com.tatx.partnerapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Home on 19-09-2016.
 */
public class ChangeDestinationAlertDialog extends Dialog {

    @BindView(R.id.ok)
    Button ok;
    @BindView(R.id.tv_changed_address)
    TextView tvChangedAddress;
    String newAddress;
    DialogClickListener dialogClickListener;

    public ChangeDestinationAlertDialog(Context context, String newAddress, DialogClickListener dialogClickListener) {
        super(context);
        this.dialogClickListener=dialogClickListener;
        this.newAddress=newAddress;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.destination_changed_popup);
        ButterKnife.bind(this);
        tvChangedAddress.setText(newAddress);
        init();


    }

    @OnClick(R.id.ok)
    void okClicked(){
        dialogClickListener.onOkClick();
    }

    public void init(){
        getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

    }

    public interface DialogClickListener{
         void onOkClick();
    }
}
