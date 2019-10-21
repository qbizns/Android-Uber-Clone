package com.tatx.partnerapp.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.abstractclasses.BaseActivity;
import com.tatx.partnerapp.adapter.CountryCodeListAdapter;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.constants.ServiceUrls;
import com.tatx.partnerapp.customviews.CustomFloatingLabelEditText;
import com.tatx.partnerapp.database.CountryCodeSqliteDbHelper;
import com.tatx.partnerapp.database.SqliteDB;
import com.tatx.partnerapp.dataset.CountryCodePojo;
import com.tatx.partnerapp.dataset.LoginData;
import com.tatx.partnerapp.enums.Language;
import com.tatx.partnerapp.network.RetrofitRequester;
import com.tatx.partnerapp.network.RetrofitResponseListener;
import com.tatx.partnerapp.pojos.ApiResponseVo;
import com.tatx.partnerapp.pojos.CheckUserExistanceStatusVo;
import com.tatx.partnerapp.pojos.SendRegistrationOtpVo;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 20-05-2016.
 */
public class RegistrationActivity1 extends BaseActivity implements RetrofitResponseListener, CustomFloatingLabelEditText.OnFloatingLabelEditTextClickListener {

    public static final int REQUEST_CODE_GALLERY = 0x1;

    public static final String IMAGE_DIRECTORY_NAME = "Tatx";

    private String imagePath;

    private List<LoginData> userProfile;

    public static final int REQUEST_CODE_CROP_IMAGE = 0x3;

    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;

    GridLayoutManager gridLayoutManager;

    CountryCodeListAdapter countryCodeListAdapter;

    private CountryCodeSqliteDbHelper dbHelper;

    private List<CountryCodePojo> countryCodeList;

    private SharedPreferences sp;

    private SharedPreferences.Editor editor;

    private File mFileTemp;

    private CountryCodePojo saudia_flag;

    @BindView(R.id.userexists) TextView userexists;
    @BindView(R.id.et_first_name) EditText etFname;
    @BindView(R.id.et_last_name) EditText etLname;
    @BindView(R.id.et_email) EditText etEmail;
    @BindView(R.id.ph) EditText ph;
    @BindView(R.id.et_password) EditText etPassword;
    @BindView(R.id.btn_next) Button btnNext;
    @BindView(R.id.flag_image) ImageView flag_image;
    @BindView(R.id.tv_country_code) TextView tvCountryCode;
    @BindView(R.id.iv_profile_pic) ImageView ivProfilePic;
    @BindView(R.id.ll_main) LinearLayout llMain;
    @BindView(R.id.ll_cardview) LinearLayout llCardview;
   // @BindView(R.id.et_refer_type) EditText etReferType;
    @BindView(R.id.refer_others) EditText referralCode;



    private boolean dob;
    private String countryId;
    private SqliteDB sqliteDB;
    private Uri mCropImageUri;
    private String locale;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.activity_registration_1);

        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.reg));

        initializedAllValues();


        Common.hideSoftKeyboard(this,llMain);



    }







    @OnClick(R.id.iv_profile_pic)
    void chooseProfilePic()
    {


        CropImage.startPickImageActivity(this);

    }





    public void initializedAllValues()
    {

        TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);

        String line1Number = tm.getLine1Number();

        Common.Log.i("line1Number : "+line1Number);

        sp= PreferenceManager.getDefaultSharedPreferences(this);

        editor=sp.edit();

        sqliteDB = new SqliteDB(this);

        locale=Common.getDefaultSP(this).contains(Constants.SharedPreferencesKeys.LANGUAGE) ? Common.getDefaultSP(this).getString(Constants.SharedPreferencesKeys.LANGUAGE,"en") : "en";

        getCounrtyCodeFromDB();

        Common.removeZeroPrefix(this,ph);
        Common.restrictSpaceInPasswordField(this,etPassword);


        userProfile = sqliteDB.getUserProfile();

        setCountryCodeFlag(saudia_flag);

        if (userProfile.size()!=0)
        {
            setData();

            imagePath=userProfile.get(0).getImage_path();

            if (!imagePath.equalsIgnoreCase(""))
            {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                Bitmap bitmapRounded = Common.getRoundedCroppedBitmap(bitmap, 150);
                ivProfilePic.setImageBitmap(bitmapRounded);
            }

        }

        ph.setText(line1Number);

        llCardview.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                countryCodeAlert(countryCodeList,false);
            }
        });

        getCounrtyCodeFromDB();


    }

    public void countryCodeAlert(final List<CountryCodePojo> countryCodePojos, final boolean nationality) {
        Button close;
        final RecyclerView list;
        EditText search;

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.country_code_dialog);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        list = (RecyclerView) dialog.findViewById(R.id.my_recycler_view);
        close = (Button) dialog.findViewById(R.id.button);
        search = (EditText) dialog.findViewById(R.id.search_button);
        setListOnDialog(countryCodePojos, list, dialog,nationality);
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
                for (int idx = 0; idx < countryCodeList.size(); idx++) {
                    if (countryCodeList.get(idx).getCountryName().contains(charSequence) ||
                            countryCodeList.get(idx).getCountryName().toLowerCase().contains(charSequence) ||
                            countryCodeList.get(idx).getCountryName().toUpperCase().contains(charSequence) ||
                            Common.toTitleCase(countryCodeList.get(idx).getCountryCode()).contains(charSequence)) {
                        searchUserFNFContacts.add(countryCodeList.get(idx));
                        Log.d("name:: ", countryCodeList.get(idx).getCountryCode().toString());
                    }
                }
                setListOnDialog(searchUserFNFContacts,list,dialog,nationality);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dialog.show();
    }


    public void setListOnDialog(final List<CountryCodePojo> countryCodePojos, RecyclerView recyclerView, final Dialog dialog, final boolean nationality)
    {

        gridLayoutManager = new GridLayoutManager(this, 1);

        recyclerView.setLayoutManager(gridLayoutManager);

        if (countryCodePojos != null)
        {
            countryCodeListAdapter = new CountryCodeListAdapter(this, countryCodePojos,nationality);
            recyclerView.setAdapter(countryCodeListAdapter);


        } else {
            recyclerView.setVisibility(View.GONE);
        }

        countryCodeListAdapter.setOnItemClickListener(new CountryCodeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (nationality) {
//                    cetNationality.setText(countryCodePojos.get(position).getCountryName());
                    countryId = String.valueOf(countryCodePojos.get(position).getId());
                } else {
                    setCountryCodeFlag(countryCodePojos.get(position));
                }
                if (dialog != null) {
                    dialog.dismiss();
                }

            }
        });


    }



    @OnClick(R.id.btn_next)
    void next()
    {

        Common.hideSoftKeyboard(RegistrationActivity1.this);
        String fname = etFname.getText().toString().trim();
        String lastName = etLname.getText().toString().trim();
        String emailId = etEmail.getText().toString().trim();
        String passwrd = etPassword.getText().toString().trim();
        String phoneNumber=ph.getText().toString().trim();

        if (fname.length()<3) {
            Common.requestChildFocus(etFname);
            etFname.setError(Common.getStringFromResources(R.string.please_enter_first_name));
           // Common.customToast(this, "Please enter First Name", Common.TOAST_TIME);
        } else if (lastName.length()<3) {
            Common.requestChildFocus(etLname);
            etLname.setError(Common.getStringFromResources(R.string.please_enter_last_name));
           // Common.customToast(this, "Please enter Last Name", Common.TOAST_TIME);
        }
        else if (passwrd.length()<6)
        {
            Common.requestChildFocus(etPassword);
            etPassword.setError(Common.getStringFromResources(R.string.please_enter_password_minimum_6_digits));
            //Common.customToast(this, "Please enter Password minimum 6-digits", Common.TOAST_TIME);

        }  else if (!Common.isValidEmail(emailId)) {
            Common.requestChildFocus(etEmail);
            etEmail.setError(Common.getStringFromResources(R.string.please_enter_emailid));
            //Common.customToast(this, "Please enter EmailId", Common.TOAST_TIME);

        } else if (phoneNumber.length()<9) {
            Common.requestChildFocus(ph);
            ph.setError(Common.getStringFromResources(R.string.please_enter_mobile_number));
           // Common.customToast(this, "Please enter Mobile Number", Common.TOAST_TIME);

        } else if (!Common.isValidMobileNumber(phoneNumber)) {
            Common.requestChildFocus(ph);
            ph.setError(Common.getStringFromResources(R.string.please_enter_valid_mobile_number));
            //Common.customToast(this, "Please enter Valid Mobile Number", Common.TOAST_TIME);

        }
        else if (mFileTemp==null) {
            Common.requestChildFocus(ivProfilePic);
          //  ph.setError(Common.getStringFromResources(R.string.please_enter_valid_mobile_number));
            Common.customToast(this, Common.getStringResourceText(R.string.please_upload_profile_picture), Common.TOAST_TIME);

        }
        /*else if (etReferType.getText().length()<2) {
            Common.requestChildFocus(etReferType);
            etReferType.setError(Common.getStringFromResources(R.string.enter_reference_tpye));
            //Common.customToast(this, "Please enter Valid Mobile Number", Common.TOAST_TIME);

        }else if ((etReferType.getTag().equals(ReferenceType.OTHERS.getReferenceId()) || etReferType.getTag().equals(ReferenceType.TATXEMPLOYEE.getReferenceId())) && referOthers.getText().length()<2) {
            Common.requestChildFocus(referOthers);
            referOthers.setError(Common.getStringFromResources(R.string.enter_reference_tpye));
            //Common.customToast(this, "Please enter Valid Mobile Number", Common.TOAST_TIME);

        }*/
        else
        {



            String countrycode = tvCountryCode.getText().toString().trim().replace("+","");

            HashMap<String, String> requestParams = new HashMap();
            requestParams.put(ServiceUrls.ApiRequestParams.EMAIL, emailId);
            requestParams.put(ServiceUrls.ApiRequestParams.PHONE_NUMBER, countrycode + phoneNumber);
            requestParams.put(ServiceUrls.ApiRequestParams.REFERRAL_CODE, referralCode.getText().toString().trim());
            requestParams.put(ServiceUrls.ApiRequestParams.ROLE, Constants.DRIVER);

            new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.CHECK_USER_EXISTANCE_STATUS, requestParams);




        }



    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != RESULT_OK) {

            return;
        }
        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
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
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                ((ImageView) findViewById(R.id.iv_profile_pic)).setImageURI(result.getUri());
                mFileTemp=Common.savefile(this,result.getUri());
                // Toast.makeText(this, "Cropping successful, Sample: \n" + result.getSampleSize()+"result.getUri() \n "+result.getUri()+"result\n"+result.getBitmap()+"\nprofilePicFile\n"+profilePicFile.length(), Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
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
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                .setMultiTouchEnabled(false)
                .setAspectRatio(5,5)
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(this);
    }






    private void setData() {
        userProfile=sqliteDB.getUserProfile();
    }

    public void getCounrtyCodeFromDB() {
        dbHelper = new CountryCodeSqliteDbHelper(this);
        dbHelper.openDataBase();
        countryCodeList = dbHelper.getCountryCodeFlagList();
        Log.d("countryCodeList", countryCodeList.toString());
        saudia_flag=dbHelper.getDetailsByCountryCode("966");


    }
    private void setCountryCodeFlag(CountryCodePojo countryCodePojos) {
        String code = countryCodePojos.getCountryCode();
        tvCountryCode.setText("+" + code);

        byte[] byteCode = countryCodePojos.getFlag();
        if (byteCode!=null) {
            Bitmap flagBitMap = Common.byteToBitMap(byteCode);
            flag_image.setImageBitmap(flagBitMap);
        }
    }



    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId)
    {

        if (apiResponseVo.code != Constants.SUCCESS)
        {
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
                }else if(!checkUserExistanceStatusVo.referralCode)
                {
                    Common.customToast(this,Common.getStringResourceText(R.string.invalid_referral_code));
                }
                else
                {
                    /*
                    String countrycode = tvCountryCode.getText().toString().trim();

                    HashMap<String, String> params = new HashMap();
                    params.put(ServiceUrls.ApiRequestParams.EMAIL, etEmail.getText().toString().trim());
                    params.put(ServiceUrls.ApiRequestParams.PHONE_NUMBER, countrycode.substring(1, countrycode.length()) + etPhoneNumber.getText().toString().trim());
*/
                    new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.SEND_REGISTRATION_OTP, requestParams);


                }





                break;



            case ServiceUrls.RequestNames.SEND_REGISTRATION_OTP:

              //  String referText= etReferType.getTag().equals(ReferenceType.OTHERS.getReferenceId()) || etReferType.getTag().equals(ReferenceType.TATXEMPLOYEE.getReferenceId()) ? referOthers.getText().toString().trim() : etReferType.getText().toString().trim();

                String countrycode = tvCountryCode.getText().toString().trim().replace("+","");

                SendRegistrationOtpVo sendRegistrationOtpVo = Common.getSpecificDataObject(apiResponseVo.data, SendRegistrationOtpVo.class);

                Common.customToast(this,sendRegistrationOtpVo.message);

                HashMap<String, String> registrationParams = new HashMap<>();

                registrationParams.put(Constants.IntentKeys.F_NAME,etFname.getText().toString().trim());
                registrationParams.put(Constants.IntentKeys.L_NAME, etLname.getText().toString().trim());
                registrationParams.put(Constants.IntentKeys.EMAIL, etEmail.getText().toString().trim());
                registrationParams.put(Constants.IntentKeys.PASSWORD, etPassword.getText().toString().trim());
                registrationParams.put(Constants.IntentKeys.MOBILE_NUMBER, countrycode + ph.getText().toString().trim());
                registrationParams.put(Constants.IntentKeys.COUNTRY_CODE, countrycode);
                registrationParams.put(Constants.IntentKeys.DEVICE_ID, Common.getDeviceId(this));
                registrationParams.put(Constants.IntentKeys.DEVICE, Constants.ANDROID);
                registrationParams.put(Constants.IntentKeys.REFERRAL_CODE, referralCode.getText().toString().trim());
                registrationParams.put(Constants.IntentKeys.TOKEN, Common.getDefaultSP(this).getString(Constants.SharedPreferencesKeys.REG_ID,null));
                registrationParams.put(Constants.IntentKeys.LANGUAGE, String.valueOf(Language.getEnumFieldByLocaleCode(locale).getLanguageCode()));
//                registrationParams.put(Constants.IntentKeys.DRIVER_REFER_TYPE, etReferType.getTag().toString());
//                registrationParams.put(Constants.IntentKeys.REFER_TEXT, referText);


                Intent intent = new Intent(getApplicationContext(), VerificationActivity.class);
                intent.putExtra(Constants.IntentKeys.OTP, sendRegistrationOtpVo.otp);
                intent.putExtra(Constants.IntentKeys.VALUE, Constants.CALL_API_FROM_REGISTRATION);
                intent.putExtra(Constants.IntentKeys.PROFILE_PIC_FILE, mFileTemp);
                intent.putExtra(Constants.IntentKeys.REGISTRATION_VO_KEY,(Serializable) registrationParams);
                startActivity(intent);

                break;

        }


    }





    @Override
    public void onFloatingLabelEditTextClick(CustomFloatingLabelEditText labelEditText)
    {

        switch (labelEditText.getId())
        {
            case R.id.cet_car_make:

                break;

             case R.id.cet_car_model:

                break;

        }


    }



/*
    void setRemoveErrorOnTextChange(EditText... editTexts)
    {

        for (final EditText editText:editTexts)
        {

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                editText.setError(null);
                }
            });

        }


    }*/

/*
    public void referTypePopUp(View view, List<ReferenceType> referenceTypes) {
        PopupMenu popup = new PopupMenu(RegistrationActivity1.this, view);
        //Inflating the Popup using xml file
       // popup.getMenuInflater().inflate(R.menu.menu_gender, popup.getMenu());
        for (ReferenceType referenceType: referenceTypes) {
            popup.getMenu().add(referenceType.getReferenceId(),referenceType.getReferenceId(),referenceType.getReferenceId(),referenceType.getReferenceName());
        }
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                etReferType.setError(null);
                etReferType.setText(item.getTitle());
                etReferType.setTag(item.getItemId());
                Log.d("item.getTitle()",item.getTitle()+" getItemId "+item.getItemId()+"getTag"+etReferType.getTag());
                if (item.getItemId() == ReferenceType.OTHERS.getReferenceId() || item.getItemId() == ReferenceType.TATXEMPLOYEE.getReferenceId()) {
                    textInputLayout.setVisibility(View.VISIBLE);
                    if (item.getItemId() == ReferenceType.TATXEMPLOYEE.getReferenceId()){
                        referOthers.setHint(Common.getStringResourceText(R.string.employee_name));
                    }else{
                        referOthers.setHint(Common.getStringResourceText(R.string.enter_reference_tpye));
                    }

                } else {
                    textInputLayout.setVisibility(View.GONE);
                }

                return true;
            }
        });

        popup.show();//showing popup menu
    }*/
/*@OnClick (R.id.et_refer_type) void referType(){

    referTypePopUp(etReferType,ReferenceType.getList());
}*/

}
