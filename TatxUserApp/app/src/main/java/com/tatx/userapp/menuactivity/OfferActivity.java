package com.tatx.userapp.menuactivity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.tatx.userapp.R;
import com.tatx.userapp.abstractclasses.BaseActivity;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;
import com.tatx.userapp.constants.ServiceUrls;
import com.tatx.userapp.fragments.OfferDetailFragment;
import com.tatx.userapp.fragments.OfferFragment;
import com.tatx.userapp.network.RetrofitRequester;
import com.tatx.userapp.network.RetrofitResponseListener;
import com.tatx.userapp.pojos.ApiResponseVo;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;


//import android.util.Log;


public class OfferActivity extends BaseActivity implements RetrofitResponseListener{

    private Fragment contentFragment;
    OfferFragment offerFragment;
    OfferDetailFragment pdtDetailFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);
        ButterKnife.bind(this);
        setTitleText(Common.getStringResourceText(R.string.offers));
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("content")) {
                String content = savedInstanceState.getString("content");
                if (content.equals(OfferDetailFragment.ARG_ITEM_ID)) {
                    if (fragmentManager
                            .findFragmentByTag(OfferDetailFragment.ARG_ITEM_ID) != null) {
                        contentFragment = fragmentManager
                                .findFragmentByTag(OfferDetailFragment.ARG_ITEM_ID);
                    }
                }
            }
            if (fragmentManager.findFragmentByTag(OfferFragment.ARG_ITEM_ID) != null) {
                offerFragment = (OfferFragment) fragmentManager
                        .findFragmentByTag(OfferFragment.ARG_ITEM_ID);
                contentFragment = offerFragment;
            }
        } else {
            offerFragment = new OfferFragment();
            switchContent(offerFragment, OfferFragment.ARG_ITEM_ID);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (contentFragment instanceof OfferFragment) {
            outState.putString("content", OfferFragment.ARG_ITEM_ID);
        } else {
            outState.putString("content", OfferDetailFragment.ARG_ITEM_ID);
        }
        super.onSaveInstanceState(outState);
    }

    public void switchContent(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        while (fragmentManager.popBackStackImmediate())
            ;

        if (fragment != null) {
            FragmentTransaction transaction = fragmentManager
                    .beginTransaction();
            transaction.replace(R.id.content_frame, fragment, tag);
            // Only OfferDetailFragment is added to the back stack.
            if (!(fragment instanceof OfferFragment)) {
                transaction.addToBackStack(tag);
            }
            transaction.commit();
            contentFragment = fragment;
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            super.onBackPressed();
        } else if (contentFragment instanceof OfferFragment
                || fm.getBackStackEntryCount() == 0) {
            finish();
        }
    }


    @OnClick(R.id.tv_add_promo)
    void addPromo()
    {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_promo_manually);
//        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
//        dialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

        final EditText cetEnterPromoCode = (EditText) dialog.findViewById(R.id.cet_enter_promo_code);

        TextView tvValidateButton = (TextView) dialog.findViewById(R.id.tv_validate_button);

        TextView tvCancelButton = (TextView) dialog.findViewById(R.id.tv_cancel_button);


        tvValidateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {



                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put(ServiceUrls.RequestParams.PCODE,cetEnterPromoCode.getText().toString().trim());

                new RetrofitRequester(OfferActivity.this).sendStringRequest(ServiceUrls.RequestNames.CHECK_PROMO_VALIDITY,hashMap);

                dialog.dismiss();



            }
        });


        tvCancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });


    }

    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId)
    {


        if (apiResponseVo.code != Constants.SUCCESS)
        {

            switch (apiResponseVo.requestname)
            {
                case ServiceUrls.RequestNames.CHECK_PROMO_VALIDITY:

                    switch (apiResponseVo.code)
                    {

                        case Constants._201 :
                            Common.customToast(this, Common.getStringResourceText(R.string.promo_code_already_used));
                            break;

                        case Constants._202 :

                            Common.customToast(this, Common.getStringResourceText(R.string.invalid_promo_code));

                            break;




                    }



                    break;

                default:
                    Common.customToast(this, apiResponseVo.status);
                    break;

            }

            return;

        }


        switch (apiResponseVo.requestname)
        {
            case ServiceUrls.RequestNames.CHECK_PROMO_VALIDITY:

//                Common.customToast(this, apiResponseVo.status);
                Common.customToast(this, Common.getStringResourceText(R.string.promo_code_validated_successfully));

                break;
        }

    }
}
