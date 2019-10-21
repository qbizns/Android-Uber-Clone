package com.tatx.userapp.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobile.connect.PWConnect;
import com.mobile.connect.exception.PWError;
import com.mobile.connect.exception.PWException;
import com.mobile.connect.exception.PWProviderNotInitializedException;
import com.mobile.connect.listener.PWTokenObtainedListener;
import com.mobile.connect.listener.PWTransactionListener;
import com.mobile.connect.payment.PWPaymentParams;
import com.mobile.connect.provider.PWTransaction;
import com.mobile.connect.service.PWProviderBinder;
import com.tatx.userapp.R;
import com.tatx.userapp.abstractclasses.BaseActivity;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;
import com.tatx.userapp.constants.ServiceUrls;
import com.tatx.userapp.enums.CreditCardType;
import com.tatx.userapp.menuactivity.AccountActivity;
import com.tatx.userapp.network.RetrofitRequester;
import com.tatx.userapp.network.RetrofitResponseListener;
import com.tatx.userapp.pojos.ApiResponseVo;

import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

/**
 * Created by Venkateswarlu SKP on 04-01-2017.
 */


public class AddCreditCardActivity extends BaseActivity implements View.OnClickListener,PWTokenObtainedListener,PWTransactionListener, TextWatcher,RetrofitResponseListener
{
    private static final String VISA_REG_EXP = "^4[0-9]{6,}$";
    private static final String MASTERCARD_REG_EXP = "^5[1-5][0-9]{5,}|222[1-9][0-9]{3,}|22[3-9][0-9]{4,}|2[3-6][0-9]{5,}|27[01][0-9]{4,}|2720[0-9]{3,}$";
    private static final String DINERS_REG_EXP = "^3(?:0[0-5]|[68][0-9])[0-9]{4,}$";
    private static final String JCB_REG_EXP = "^(?:2131|1800|35[0-9]{3})[0-9]{3,}$";
    private static final String AMEX_REG_EXP = "^3[47][0-9]{5,}$";
    private int MY_SCAN_REQUEST_CODE = 100; // arbitrary int


    private SharedPreferences sp;
    private int userid;
    private TextView saveCard;
    private ProgressDialog progressDialog;
    private EditText _inputCCExpiryMonth;
    private EditText _inputCCExpiryYear;
    private EditText _inputCCCVV;
    private LinearLayout llCardDetailsMain;
    private EditText _inputName;
    private String cardHolderName;
    private String cardNumber;
    private String month;
    private String year;
    private String cvv;
    private TextView tiltle;





    @BindView(R.id.iv_card_image)
    ImageView ivCardImage;

    @BindView(R.id.et_credit_card_number)
    EditText etCreditCardNumber;
    private CreditCardType CREDIT_CARD_TYPE;

    @BindView(R.id.your_credit_card_will_be_changed) TextView tvYourCreditCardWillBeChanged;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_creditcard);

        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.add_creditcard));

        initialaziedAll();





        etCreditCardNumber.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                String cardNumberText = s.toString();

                if(cardNumberText.matches(VISA_REG_EXP))
                {
                    ivCardImage.setVisibility(View.VISIBLE);
                    ivCardImage.setBackgroundResource(CreditCardType.VISA.getCardDrawableBig());
                    Common.Log.i("CardType - VISA_REG_EXP");
                    CREDIT_CARD_TYPE=CreditCardType.VISA;
                }
                else if(cardNumberText.matches(MASTERCARD_REG_EXP))
                {
                    ivCardImage.setVisibility(View.VISIBLE);
                    ivCardImage.setBackgroundResource(CreditCardType.MASTERCARD.getCardDrawableBig());
                    Common.Log.i("CardType - MASTERCARD_REG_EXP");
                    CREDIT_CARD_TYPE=CreditCardType.MASTERCARD;

                }
                else if(cardNumberText.matches(DINERS_REG_EXP))
                {
                    ivCardImage.setVisibility(View.VISIBLE);
                    ivCardImage.setBackgroundResource(CreditCardType.DINERS.getCardDrawableBig());
                    Common.Log.i("CardType - DINERS_REG_EXP");
                    CREDIT_CARD_TYPE=CreditCardType.DINERS;

                }
                else if(cardNumberText.matches(JCB_REG_EXP))
                {
                    ivCardImage.setVisibility(View.VISIBLE);
                    ivCardImage.setBackgroundResource(CreditCardType.JCB.getCardDrawableBig());
                    Common.Log.i("CardType - JCB_REG_EXP");
                    CREDIT_CARD_TYPE=CreditCardType.JCB;

                }
                else if(cardNumberText.matches(AMEX_REG_EXP))
                {
                    ivCardImage.setVisibility(View.VISIBLE);
                    ivCardImage.setBackgroundResource(CreditCardType.AMEX.getCardDrawableBig());
                    Common.Log.i("CardType - AMEX_REG_EXP");
                    CREDIT_CARD_TYPE=CreditCardType.AMEX;

                }
                else
                {
                    ivCardImage.setVisibility(View.GONE);
                    Common.Log.i("CardType - Not Matched with any Card Type");

                }



            }


        });


        startService(new Intent(this, com.mobile.connect.service.PWConnectService.class));

        bindService(new Intent(this, com.mobile.connect.service.PWConnectService.class), _serviceConnection, Context.BIND_AUTO_CREATE);


    }

    public void initialaziedAll()
    {


        tvYourCreditCardWillBeChanged.setText(Common.getStringResourceText(R.string.your_credit_card_will_be_changed)+" 1 SAR "+Common.getStringResourceText(R.string.for_verification_purposes_and_will_be_automatically_refunded));

        sp= PreferenceManager.getDefaultSharedPreferences(this);


        userid=sp.getInt("userid", 0);

        saveCard=(TextView)findViewById(R.id.save_card);

        _inputName = (EditText) findViewById(R.id.connect_checkout_insert_cc_payment_data_name);

        _inputCCExpiryMonth = (EditText) findViewById(R.id.connect_checkout_insert_cc_payment_data_expiry_month);

        _inputCCExpiryMonth.addTextChangedListener(this);

        _inputCCExpiryYear = (EditText) findViewById(R.id.connect_checkout_insert_cc_payment_data_expiry_year);

        _inputCCExpiryYear.addTextChangedListener(this);

        _inputCCCVV = (EditText)findViewById(R.id.connect_checkout_insert_cc_payment_data_cvv);

        llCardDetailsMain = (LinearLayout)findViewById(R.id.ll_card_details_main);

        tiltle = (TextView) findViewById(R.id.etcredit);

        saveCard.setOnClickListener(this);


    }

    @Override
    public void onClick(View v)
    {

        switch (v.getId())
        {
            case R.id.save_card:

//                sqliteDB.insertCardDetails(cardHolderName.getText().toString().trim(), Integer.parseInt(cardNumber.getText().toString().trim()), expDate.getText().toString().trim());


                if(isValid())
                {
                    if (CREDIT_CARD_TYPE.getPwCreditCardType()!=null) {

                        progressDialog = ProgressDialog.show(this, "",  Common.getStringResourceText(R.string.please_wait), true, true);

                        saveCreditCardDetails();
                    }else {
                        Common.customToast(this,Common.getStringResourceText(R.string.invalid_credit_card_number));
                    }


                }



                break;

        }
    }

    private boolean isValid()
    {

        EditText[] editTextFields = {_inputName,etCreditCardNumber,_inputCCExpiryMonth,_inputCCExpiryYear,_inputCCCVV};

        for (EditText editText : editTextFields)
        {

            if(TextUtils.isEmpty(editText.getText()))
            {

                Common.customToast(this,Common.getStringResourceText(R.string.please_enter_all_fields));

                return false;

            }

        }



        int expiryYear = Integer.valueOf(_inputCCExpiryYear.getText().toString());
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        int expiryMonth = Integer.valueOf(_inputCCExpiryMonth.getText().toString());
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);

        if (_inputName.getText().toString().trim().length()<2){
            Common.customToast(this,Common.getStringResourceText(R.string.please_enter_card_holder_name));
        }
        else if(etCreditCardNumber.getText().toString().length()<12)
        {

            Common.customToast(this,Common.getStringResourceText(R.string.invalid_credit_card_number));

            return false;
        }
        else if(expiryMonth <1 || expiryMonth >12 )
        {
            Common.customToast(this,Common.getStringResourceText(R.string.invalid_month));

            return false;


        }
        else if((expiryYear < currentYear) || (expiryYear == currentYear && expiryMonth < currentMonth))
        {

            Common.customToast(this,Common.getStringResourceText(R.string.invalid_year));

            return false;


        }
        else if(_inputCCCVV.getText().toString().length()<3)
        {

            Common.customToast(this,Common.getStringResourceText(R.string.invalid_cvv));

            return false;


        }


        return true;
    }

    private PWProviderBinder _binder;
/*

    //For Production Environment
    private static final String APPLICATIONIDENTIFIER = "gate2play.CreativityApp.mcommerce";
    private static final String PROFILETOKEN = "34284a2c6de611e6a91b398965e57753";

*/


/*

    //For Dev Environment
    private static final String APPLICATIONIDENTIFIER = "gate2play.CreativityApp.mcommerce.test";
    private static final String PROFILETOKEN = "8a858406ae924adba3c0de76ddb2abfc";

*/






    private ServiceConnection _serviceConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            _binder = (PWProviderBinder) service;

            try
            {
/*

                //Production
                _binder.initializeProvider(PWConnect.PWProviderMode.LIVE, APPLICATIONIDENTIFIER, PROFILETOKEN);

                //Development
                _binder.initializeProvider(PWConnect.PWProviderMode.TEST, APPLICATIONIDENTIFIER, PROFILETOKEN);
*/


                _binder.initializeProvider(ServiceUrls.CURRENT_ENVIRONMENT.getHyperpay().getPwProviderMode(), ServiceUrls.CURRENT_ENVIRONMENT.getHyperpay().getApplicationIdentifier(), ServiceUrls.CURRENT_ENVIRONMENT.getHyperpay().getProfileToken());

                _binder.addTokenObtainedListener(AddCreditCardActivity.this);

                _binder.addTransactionListener(AddCreditCardActivity.this);

            }
            catch (PWException ee)
            {
                Common.Log.i("Error initializing the provider.");
                ee.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            _binder = null;
        }

    };

    private boolean currentTokenization;


    private void saveCreditCardDetails()
    {

        PWPaymentParams paymentParams = null;
        try
        {

            cardHolderName = _inputName.getText().toString();

            cardNumber = etCreditCardNumber.getText().toString();

            month = _inputCCExpiryMonth.getText().toString();

            year = _inputCCExpiryYear.getText().toString();

            cvv = _inputCCCVV.getText().toString();

            Common.Log.i("_binder : "+_binder);


            paymentParams = _binder.getPaymentParamsFactory().createCreditCardTokenizationParams(cardHolderName, CREDIT_CARD_TYPE.getPwCreditCardType(), cardNumber, year, month, cvv);



        }
        catch (PWProviderNotInitializedException e)
        {

            Common.Log.i("Error: Provider not initialized!");

            e.printStackTrace();

            showPaymentError();

            return;

        } catch (PWException e)
        {

            Common.Log.i("Error: Invalid Parameters!");

            e.printStackTrace();

            showPaymentError();

            return;

        }


        Common.Log.i("Preparing...");

        currentTokenization = true;

        try
        {
            _binder.createAndRegisterObtainTokenTransaction(paymentParams);
        }
        catch (PWException e)
        {

            Common.Log.i("Error: Could not contact Gateway!");

            e.printStackTrace();

        }




    }

    private void showPaymentError()
    {

        Common.dismissProgressDialog(progressDialog);

        Common.customToast(this,Common.getStringResourceText(R.string.invalid_card_details));


    }


    @Override
    protected void onDestroy()
    {

        super.onDestroy();

        unbindService(_serviceConnection);

        stopService(new Intent(this, com.mobile.connect.service.PWConnectService.class));

    }

    @Override
    public void obtainedToken(String token, PWTransaction transaction)
    {


        Common.Log.i("Obtained a token!");

        Common.Log.i(token);

        Common.Log.i("Token after removing prefix : ");

        token = token.split("\\.")[1];

        Common.Log.i(token);

        Common.Log.i("Card Number to Send : " + cardNumber.substring(cardNumber.length() - 4, cardNumber.length()));

        saveCreditCardApi(cardHolderName, year + "-" + month + "-00", "********" + cardNumber.substring(cardNumber.length() - 4, cardNumber.length()), String.valueOf(CREDIT_CARD_TYPE.getId()),"SAR",token,"1");


    }

    @Override
    public void creationAndRegistrationFailed(PWTransaction transaction, PWError error)
    {


        Common.Log.i("Error contacting the gateway."+error.toString());

        Common.dismissProgressDialog(progressDialog);

        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {

                Common.customToast(AddCreditCardActivity.this,Common.getStringResourceText(R.string.error_contacting_the_gateway));

            }
        });


    }

    @Override
    public void creationAndRegistrationSucceeded(PWTransaction transaction)
    {

        Common.Log.i("Processing...");

        if(currentTokenization)
        {
            try
            {
                _binder.obtainToken(transaction);
            }
            catch (PWException e)
            {
                Common.Log.i("Invalid Transaction.");
                e.printStackTrace();
            }

        }
        else
        {

            try
            {
                _binder.debitTransaction(transaction);
            }
            catch (PWException e)
            {
                Common.Log.i("Invalid Transaction.");
                e.printStackTrace();
            }

        }
    }

    @Override
    public void transactionFailed(PWTransaction arg0, PWError error)
    {


        Common.Log.i("Error contacting the gateway."+error.toString());

        Common.dismissProgressDialog(progressDialog);

        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {

                Common.customToast(AddCreditCardActivity.this,Common.getStringResourceText(R.string.error_contacting_the_gateway));

            }

        });



    }

    @Override
    public void transactionSucceeded(PWTransaction transaction)
    {
        if(!currentTokenization)
        {

            Common.Log.i("Charged token " + transaction.getPaymentParams().getAmount() + " EURO!");

        }
    }



    public static void finishAndStartAccountActivity(Activity currentActivity)
    {


        currentActivity.startActivity(new Intent(currentActivity,AccountActivity.class));

        Common.finishActivity(Common.getPreviousActivity());

        Common.finishActivity(currentActivity);


    }

    public void saveCreditCardApi(String name, String expiry_date, String number, String brand, String currency,String token, String primary)
    {

        HashMap<String, String> hashMap = new HashMap<String, String>();

        hashMap.put(ServiceUrls.RequestParams.NAME, name);
        hashMap.put(ServiceUrls.RequestParams.EXPIRY_DATE, expiry_date);
        hashMap.put(ServiceUrls.RequestParams.NUMBER, number);
        hashMap.put(ServiceUrls.RequestParams.BRAND, brand);
        hashMap.put(ServiceUrls.RequestParams.CURRENCY, currency);
        hashMap.put(ServiceUrls.RequestParams.TOKEN, token);
        hashMap.put(ServiceUrls.RequestParams.PRIMARY, primary);


        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.ADD_CARD,hashMap);


    }




    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {

    }

    @Override
    public void afterTextChanged(Editable editable)
    {

        View currentFocus = llCardDetailsMain.findFocus();

        if(currentFocus == _inputCCExpiryMonth)
        {
            if(editable.length() == 2)
            {
                _inputCCExpiryYear.requestFocus();
            }

        }
        else if(currentFocus == _inputCCExpiryYear)
        {
            if(editable.length() == 4)
            {
                _inputCCCVV.requestFocus();
            }
        }


    }


    @OnClick(R.id.ll_scan_credit_card)
    void scanCreditCard()
    {
        // This method is set up as an onClick handler in the layout xml
        // e.g. android:onClick="onScanPress"

        Intent scanIntent = new Intent(this, CardIOActivity.class);

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_RESTRICT_POSTAL_CODE_TO_NUMERIC_ONLY, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CARDHOLDER_NAME, false); // default: false

        // hides the manual entry button
        // if set, developers should provide their own manual entry mechanism in the app
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, false); // default: false

        // matches the theme of your application
        scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, false); // default: false

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);


    }

    @Override
    protected void onResume()
    {
        super.onResume();

        Common.Log.i("? - Supports Read Card : "+ CardIOActivity.canReadCardWithCamera());

        if (CardIOActivity.canReadCardWithCamera())
        {
//            scanButton.setText("Scan a credit card with card.io");
            Common.Log.i("? - Scan a credit card with card.io");
        }
        else
        {
//            scanButton.setText("Enter credit card information");
            Common.Log.i("? - Enter credit card information");
        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        String resultStr;
        if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT))
        {
            CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

            // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
            resultStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n";

            Common.Log.i("? - scanResult.getFormattedCardNumber()"+scanResult.getFormattedCardNumber());

            Common.Log.i("? - scanResult.getLastFourDigitsOfCardNumber() : "+scanResult.getLastFourDigitsOfCardNumber());

            Common.Log.i("? - scanResult.getRedactedCardNumber() : "+scanResult.getRedactedCardNumber());

            Common.Log.i("? - scanResult.cardNumber : "+scanResult.cardNumber);

            etCreditCardNumber.setText(scanResult.cardNumber);

            // Do something with the raw number, e.g.:
            // myService.setCardNumber( scanResult.cardNumber );

            if (scanResult.isExpiryValid()) {
                resultStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
            }

            Common.Log.i("? - scanResult.expiryMonth : "+scanResult.expiryMonth);

            _inputCCExpiryMonth.setText(String.valueOf(scanResult.expiryMonth));

            Common.Log.i("? - scanResult.expiryYear : "+scanResult.expiryYear);

            _inputCCExpiryYear.setText(String.valueOf(scanResult.expiryYear));


            if (scanResult.cvv != null) {
                // Never log or display a CVV
                resultStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
            }

            if (scanResult.postalCode != null) {
                resultStr += "Postal Code: " + scanResult.postalCode + "\n";
            }

            if (scanResult.cardholderName != null) {
                resultStr += "Cardholder Name : " + scanResult.cardholderName + "\n";
            }
        } else {
            resultStr = "Scan was canceled.";
        }
//        resultTextView.setText(resultStr);
        Common.Log.i("? - resultStr : "+resultStr);

    }


    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId) {
        if (apiResponseVo.code != Constants.SUCCESS)
        {

            Common.customToast(this, apiResponseVo.status);
            if (progressDialog!=null){
                progressDialog.dismiss();
            }
            finish();

            return;


        }



        switch (apiResponseVo.requestname)
        {

            case ServiceUrls.RequestNames.ADD_CARD:
                finishAndStartAccountActivity(this);
                Common.customToast(this, apiResponseVo.status);
                break;
        }



    }
}
