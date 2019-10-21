package com.tatx.userapp.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tatx.userapp.R;
import com.tatx.userapp.adapter.CountryCodeListAdapter;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;
import com.tatx.userapp.constants.ServiceUrls;
import com.tatx.userapp.database.CountryCodeSqliteDbHelper;
import com.tatx.userapp.dataset.CountryCodePojo;
import com.tatx.userapp.enums.Language;
import com.tatx.userapp.menuactivity.cropimages.InternalStorageContentProvider;
import com.tatx.userapp.network.RetrofitRequester;
import com.tatx.userapp.network.RetrofitResponseListener;
import com.tatx.userapp.pojos.ApiResponseVo;
import com.tatx.userapp.pojos.CheckUserExistanceStatusVo;
import com.tatx.userapp.pojos.SendRegistrationOtpVo;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 11/04/2016.
 */

public class RegistrationActivity extends SocialMediaLoginBaseActivity implements CompoundButton.OnCheckedChangeListener, RetrofitResponseListener
{

    private File mFileTemp;

    GridLayoutManager gridLayoutManager;

    CountryCodeListAdapter countryCodeListAdapter;

    public static String emailId;

    ProgressDialog progressDialog;

    private CountryCodeSqliteDbHelper dbHelper;

    private List<CountryCodePojo> colorsList;

    private String socialMediaToken;

    private String socialMediaType;

    private String socialProfilePicUrl;

    private CountryCodePojo saudia_flag;


    @BindView(R.id.et_fname) EditText etFname;
    @BindView(R.id.et_lname) EditText etLname;
    @BindView(R.id.et_email) EditText etEmail;
    @BindView(R.id.et_password) EditText etPassword;
    @BindView(R.id.iv_gp_login) ImageView ivGpLogin;
    @BindView(R.id.tv_title_text) TextView registration;
    @BindView(R.id.signupwith) TextView signupwith;
    @BindView(R.id.et_phone_number) EditText etPhoneNumber;
    @BindView(R.id.btn_create_account) Button btnCreateAccount;
    @BindView(R.id.cb) CheckBox cb;
    @BindView(R.id.iv_user_pic) ImageView ivUserPic;
    @BindView(R.id.tv_country_code) TextView tvCountryCode;
    @BindView(R.id.et_referral_code) TextView etReferralCode;
    @BindView(R.id.terms) TextView tc;
    @BindView(R.id.tcc) TextView tcc;
    @BindView(R.id.flag_image) ImageView flag_image;
    @BindView(R.id.cardview) LinearLayout cardview;
    private Uri mCropImageUri;
    private String URL_TERMS;
    private String locale;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.registration);

        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.registration));

        initializedAllValues();
        URL_TERMS="http://tatx.com/"+getResources().getConfiguration().locale.getLanguage()+"/index.php/terms/";

        Common.Log.i("? - URL_TERMS : "+URL_TERMS);

    }

    public void initializedAllValues()

    {

        TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);

        String number = tm.getLine1Number();

        locale=Common.getDefaultSP(this).contains(Constants.SharedPreferencesKeys.LANGUAGE) ? Common.getDefaultSP(this).getString(Constants.SharedPreferencesKeys.LANGUAGE,"en") : "en";

        getCountryCodeFromDB();

        Common.Log.i("? - number : "+number);
        Common.removeZeroPrefix(this,etPhoneNumber);
        Common.restrictSpaceInPasswordField(this,etPassword);
        etPhoneNumber.setText(number);
        setCountryCodeFlag(saudia_flag);

        cb.setOnCheckedChangeListener(this);

        tc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(RegistrationActivity.this,AddCreditWebViewActivity.class);
                intent2.putExtra("url",URL_TERMS);
                intent2.putExtra("title","Terms");
                startActivity(intent2);
            }
        });


        ivUserPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // showPictureialog();
               CropImage.startPickImageActivity(RegistrationActivity.this);

            }
        });

        cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                termsAlert(colorsList);
            }
        });



    }



    private void updateUI(String profilePicUrl, String firstName, String lastName, String email)
    {

        Common.Log.i("profilePicUrl : "+profilePicUrl);

        Common.Log.i("firstName : "+firstName);

        Common.Log.i("lastName : "+lastName);

        Common.Log.i("email : "+email);

        etFname.setText(firstName);

        etLname.setText(lastName);

        etEmail.setText(email);

        if (profilePicUrl != null)
        {


            Picasso.with(RegistrationActivity.this).load(profilePicUrl).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                    ivUserPic.setImageBitmap(Common.getRoundedCroppedBitmap(bitmap, 150));

                    Common.Log.i("onBitmapLoaded");

                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    Common.Log.i("onBitmapFailed");

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    Common.Log.i("onPrepareLoad");

                }
            });
        }
        else
        {

            ivUserPic.setImageResource(R.drawable.userpic);

        }

        etPassword.setVisibility(View.GONE);

        Common.Log.i("UpdateUI() Completed.");

    }


    @OnClick(R.id.btn_create_account)
    public  void createAccount(){


        Common.Log.i("Inside Create Account Click Listener Start.");

        String fname = etFname.getText().toString().trim();
        String lastName = etLname.getText().toString().trim();
        emailId = etEmail.getText().toString().trim();
        String passwrd = etPassword.getText().toString().trim();
        String phoneNumber= etPhoneNumber.getText().toString().trim();

        if (fname.length()<3) {
            etFname.requestFocus();
            etFname.setError(Common.getStringResourceText(R.string.please_enter_first_name));
        } else if (lastName.length()<3) {
            etLname.requestFocus();
            etLname.setError(Common.getStringResourceText(R.string.please_enter_last_name));
        }
        else if (passwrd.length()<6 && etPassword.getVisibility() == View.VISIBLE)
        {
            etPassword.requestFocus();
            etPassword.setError(Common.getStringResourceText(R.string.please_enter_password_minimum_6_digits));

        }
        else if (!Common.isValidEmail(emailId)) {
            etEmail.requestFocus();
            etEmail.setError(Common.getStringResourceText(R.string.please_enter_emailid));

        } else if (phoneNumber.length()<9) {
            etPhoneNumber.requestFocus();
            etPhoneNumber.setError(Common.getStringResourceText(R.string.please_enter_mobile_number));

        } else if (!Common.isValidMobileNumber(phoneNumber)) {
            etPhoneNumber.requestFocus();
            etPhoneNumber.setError(Common.getStringResourceText(R.string.please_enter_valid_mobile_number));

        } else if (!cb.isChecked()) {
            tcc.requestFocus();
            tcc.setText(Html.fromHtml(Common.getStringResourceText(R.string.please_accept_terms_and_conditions)));
        } else
        {

            String countrycode = tvCountryCode.getText().toString().trim();

            HashMap<String, String> requestParams = new HashMap();
            requestParams.put(ServiceUrls.RequestParams.EMAIL, emailId);
            requestParams.put(ServiceUrls.RequestParams.PHONE_NUMBER, countrycode.substring(1, countrycode.length()) + phoneNumber);
            requestParams.put(ServiceUrls.RequestParams.REFERRAL_CODE, etReferralCode.getText().toString());
            requestParams.put(ServiceUrls.RequestParams.ROLE, Constants.CUSTOMER);


            new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.CHECK_USER_EXISTANCE_STATUS, requestParams);



        }


        Common.Log.i("Inside Create Account Click Listener End.");

    }








    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {



        if (resultCode != RESULT_OK) {

            return;
        }

        // handle result of pick image chooser
        if (requestCode == com.theartofdev.edmodo.cropper.CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = com.theartofdev.edmodo.cropper.CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (com.theartofdev.edmodo.cropper.CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                }
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }

        // handle result of CropImageActivity
        if (requestCode == com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            com.theartofdev.edmodo.cropper.CropImage.ActivityResult result = com.theartofdev.edmodo.cropper.CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                ivUserPic.setImageURI(result.getUri());
                mFileTemp=Common.savefile(this,result.getUri());
                // Toast.makeText(this, "Cropping successful, Sample: \n" + result.getSampleSize()+"result.getUri() \n "+result.getUri()+"result\n"+result.getBitmap()+"\nprofilePicFile\n"+profilePicFile.length(), Toast.LENGTH_LONG).show();
            } else if (resultCode == com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissions granted, start crop image activity
            startCropImageActivity(mCropImageUri);
        } else {
            Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Start crop image activity for the given image.
     */
    private void startCropImageActivity(Uri imageUri) {
        com.theartofdev.edmodo.cropper.CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                .setMultiTouchEnabled(false)
                .setAspectRatio(5,5)
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(this);
    }




    @Override
    public void onSocialMediaLoginSuccess(String socialMediaType, String socialMediaToken, String profilePicUrl, String fName, String lName, String emailId, String id)
    {


        updateUI(profilePicUrl, fName, lName, emailId);

        this.socialMediaType = socialMediaType;

        this.socialMediaToken = socialMediaType.equals(Constants.SocialMediaTypes.FACEBOOK) ? id : socialMediaToken;

        this.socialProfilePicUrl = profilePicUrl;




    }


    public void getCountryCodeFromDB()
    {
        dbHelper = new CountryCodeSqliteDbHelper(this);
        dbHelper.openDataBase();
        colorsList = dbHelper.getCountryCodeFlagList();
        Log.d("colorsList", colorsList.toString());
        saudia_flag=dbHelper.getDetailsByCountryCode("966");

    }
    

    public void termsAlert(List<CountryCodePojo> countryCodePojos)
    {
        Button close;
        final RecyclerView list;
        EditText search;

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.country_code_dialog);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        list = (RecyclerView) dialog.findViewById(R.id.my_recycler_view);
        search = (EditText) dialog.findViewById(R.id.search_button);
        close = (Button) dialog.findViewById(R.id.button);
        setListOnDialog(countryCodePojos, list, dialog);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                List<CountryCodePojo> searchUserFNFContacts = new ArrayList<CountryCodePojo>();
                for (int idx = 0; idx < colorsList.size(); idx++) {
                    if (colorsList.get(idx).getCountryName().contains(charSequence) ||
                            colorsList.get(idx).getCountryName().toLowerCase().contains(charSequence) ||
                            colorsList.get(idx).getCountryName().toUpperCase().contains(charSequence) ||
                            Common.toTitleCase(colorsList.get(idx).getCountryCode()).contains(charSequence)) {
                        searchUserFNFContacts.add(colorsList.get(idx));
                        Log.d("name:: ", colorsList.get(idx).getCountryCode().toString()+" blob"+colorsList.get(idx).getFlag());
                    }
                }
                setListOnDialog(searchUserFNFContacts, list, dialog);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dialog.show();
    }

    public void setListOnDialog(final List<CountryCodePojo> countryCodePojos, RecyclerView recyclerView, final Dialog dialog) {
        gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        if (countryCodePojos != null) {
            countryCodeListAdapter = new CountryCodeListAdapter(this, countryCodePojos);
            recyclerView.setAdapter(countryCodeListAdapter);
            countryCodeListAdapter.notifyDataSetChanged();
        } else {
            recyclerView.setVisibility(View.GONE);
        }

        countryCodeListAdapter.setOnItemClickListener(new CountryCodeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                setCountryCodeFlag(countryCodePojos.get(position));

                if (dialog != null) {
                    dialog.dismiss();
                }

            }
        });


    }

    private void setCountryCodeFlag(CountryCodePojo countryCodePojos) {
        String code = countryCodePojos.getCountryCode();
        tvCountryCode.setText("+" + code);

        byte[] byteCode = countryCodePojos.getFlag();
        if (byteCode!=null) {
            Bitmap flagBitMap = Common.getBitmapFromByteArray(byteCode);
            flag_image.setImageBitmap(flagBitMap);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb:
                if (isChecked) {
                    tcc.setText("");
                } else {
                    tcc.requestFocus();
                    tcc.setText(Html.fromHtml(Common.getStringResourceText(R.string.please_accept_terms_and_conditions)));
                }

                break;
        }
    }


    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId)
    {



        if (apiResponseVo.code != Constants.SUCCESS) {

            Common.customToast(this, apiResponseVo.status);
            return;

        }


        switch (apiResponseVo.requestname)
        {


            case ServiceUrls.RequestNames.CHECK_USER_EXISTANCE_STATUS:

                CheckUserExistanceStatusVo checkUserExistanceStatusVo = Common.getSpecificDataObject(apiResponseVo.data, CheckUserExistanceStatusVo.class);


                if(checkUserExistanceStatusVo.email && checkUserExistanceStatusVo.mobile)
                {
                    Common.customToast(this,Common.getStringResourceText(R.string.email_id_and_phone_number_already_exists));
                }
                else if(checkUserExistanceStatusVo.email)
                {
                    Common.customToast(this,Common.getStringResourceText(R.string.email_id_already_exists));
                }
                else if(checkUserExistanceStatusVo.mobile)
                {
                    Common.customToast(this,Common.getStringResourceText(R.string.phone_number_already_exists));
                }
                else if(!checkUserExistanceStatusVo.referralCode)
                {

                    Common.customToast(this, Common.getStringResourceText(R.string.invalid_referral_code));

                }
                else
                {
                    /*
                    String countrycode = tvCountryCode.getText().toString().trim();

                    HashMap<String, String> params = new HashMap();
                    params.put(ServiceUrls.RequestParams.EMAIL, etEmail.getText().toString().trim());
                    params.put(ServiceUrls.RequestParams.PHONE_NUMBER, countrycode.substring(1, countrycode.length()) + etPhoneNumber.getText().toString().trim());
*/
                    new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.SEND_REGISTRATION_OTP, requestParams);


                }





                break;

            case ServiceUrls.RequestNames.SEND_REGISTRATION_OTP:

                SendRegistrationOtpVo sendRegistrationOtpVo = Common.getSpecificDataObject(apiResponseVo.data, SendRegistrationOtpVo.class);

                Common.customToast(this,apiResponseVo.status);

                Intent intent = new Intent(getApplicationContext(), VerificationActivity.class);

                String countrycode = tvCountryCode.getText().toString().trim().replace("+","");

                intent.putExtra(Constants.IntentKeys.FIRST_NAME, etFname.getText().toString().trim());
                intent.putExtra(Constants.IntentKeys.LAST_NAME, etLname.getText().toString().trim());
                intent.putExtra(Constants.IntentKeys.EMAIL, etEmail.getText().toString().trim());
                intent.putExtra(Constants.IntentKeys.PASSWORD, etPassword.getText().toString().trim());
                intent.putExtra(Constants.IntentKeys.COUNTRY_CODE,countrycode);
                intent.putExtra(Constants.IntentKeys.PHONE_NUMBER, countrycode + etPhoneNumber.getText().toString().trim());
                intent.putExtra(Constants.IntentKeys.REFERRAL_CODE,etReferralCode.getText().toString().trim());
                intent.putExtra(ServiceUrls.RequestParams.SOCIAL, socialMediaType);
                intent.putExtra(ServiceUrls.RequestParams.SOCIAL_TOKEN, socialMediaToken);
                intent.putExtra(ServiceUrls.RequestParams.PROFILE_PIC, socialProfilePicUrl);
                intent.putExtra(Constants.IntentKeys.OTP, sendRegistrationOtpVo.otp);
                intent.putExtra(Constants.IntentKeys.VALUE, Constants.CALL_API_FROM_REGISTRATION);
                intent.putExtra(Constants.IntentKeys.VALUE, Constants.CALL_API_FROM_REGISTRATION);
                intent.putExtra(Constants.IntentKeys.PROFILE_PIC_FILE, mFileTemp);
                intent.putExtra(Constants.IntentKeys.LANGUAGE, String.valueOf(Language.getEnumFieldByLocaleCode(locale).getLanguageCode()));


                startActivity(intent);


                break;



        }



    }

}
