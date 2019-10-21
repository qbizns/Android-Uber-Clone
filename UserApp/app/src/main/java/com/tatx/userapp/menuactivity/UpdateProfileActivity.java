package com.tatx.userapp.menuactivity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tatx.userapp.R;
import com.tatx.userapp.abstractclasses.BaseActivity;
import com.tatx.userapp.activities.GoogleMapDrawerActivity;
import com.tatx.userapp.adapter.CountryCodeListAdapter;
import com.tatx.userapp.adapter.CustomArrayAdapterLanguages;
import com.tatx.userapp.application.TATX;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;
import com.tatx.userapp.constants.ServiceUrls;
import com.tatx.userapp.database.CountryCodeSqliteDbHelper;
import com.tatx.userapp.dataset.CountryCodePojo;
import com.tatx.userapp.enums.Language;
import com.tatx.userapp.network.RetrofitRequester;
import com.tatx.userapp.network.RetrofitResponseListener;
import com.tatx.userapp.pojos.ApiResponseVo;
import com.tatx.userapp.pojos.GetCustomerProfileVo;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.mime.TypedFile;

import static com.tatx.userapp.enums.Language.values;

/**
 * Created by user on 01-05-2016.
 */

public class UpdateProfileActivity extends BaseActivity implements RetrofitResponseListener
{
    public static final String IMAGE_DIRECTORY_NAME = "Tatx";
    private File mFileTemp;
    @BindView(R.id.first_name) EditText first_name;
    @BindView(R.id.last_name) EditText last_name;
    @BindView(R.id.et_email) TextView email;
    @BindView(R.id.phone_number) TextView phone_number;
    @BindView(R.id.tv_save) TextView edit_profile;
    @BindView(R.id.profileImgView) ImageView profileImgView;
    @BindView(R.id.hide_keyboard_layout) LinearLayout hideKeyboardView;
    @BindView(R.id.tv_tatx_id) TextView tvTatxId;

    @BindView(R.id.spinner_language) Spinner spinnerLanguage;
    @BindView(R.id.tv_country_code) TextView tvCountryCode;
    @BindView(R.id.flag_image) ImageView flagImage;
    @BindView(R.id.iv_emergency_contact_flag) ImageView ivEmergencyContactFlag;
    @BindView(R.id.ctv_emergency_name) EditText ctvEmergencyName;
    @BindView(R.id.ctv_emergency_country_code) TextView ctvEmergencyCountryCode;
    @BindView(R.id.ctv_emergency_phone_number) EditText ctvEmergencyPhoneNumber;

    private List<CountryCodePojo> colorsList;
    private Uri mCropImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.editprofile_activity);

        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.edit_pro));

        initialaizedAll();




    }

    @OnClick (R.id.ll_emergency_country_code_view)
    void showCountrySelectionDialog()
    {

        CountryCodeSqliteDbHelper dbHelper = new CountryCodeSqliteDbHelper(this);

        dbHelper.openDataBase();

        colorsList = dbHelper.getCountryCodeFlagList();

        termsAlert(colorsList);

    }




    public void termsAlert(List<CountryCodePojo> countryCodePojos) {
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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        CountryCodeListAdapter countryCodeListAdapter = null;
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

                // Common.bitmapToDrawable(flagBitMap,contry_codeButon);
                if (dialog != null) {
                    dialog.dismiss();
                }

            }
        });


    }

    private void setCountryCodeFlag(CountryCodePojo countryCodePojos)
    {
        String code = countryCodePojos.getCountryCode();
        ctvEmergencyCountryCode.setText(code == null ? "":"+"+code);
        ivEmergencyContactFlag.setImageBitmap(Common.getBitmapFromByteArray(countryCodePojos.getFlag()));
    }


    public void initialaizedAll(){
        Common.hideSoftKeyboard(this);


        ArrayList<String> languagesArray = new ArrayList<>();

        for (Language language: values())
        {


            languagesArray.add(language.getLanguageName());

        }


        spinnerLanguage.setAdapter(new CustomArrayAdapterLanguages(this,R.layout.languages_item, values()));


        GetCustomerProfileVo getCustomerProfileVo = (GetCustomerProfileVo) getIntent().getSerializableExtra(Constants.KEY_1);

        if(getCustomerProfileVo != null)
        {
            first_name.setText(getCustomerProfileVo.firstName);
            last_name.setText(getCustomerProfileVo.lastName);
            email.setText(getCustomerProfileVo.email);
            tvTatxId.setText(getCustomerProfileVo.tatxId);

            CountryCodeSqliteDbHelper dbHelper = new CountryCodeSqliteDbHelper(this);

            dbHelper.openDataBase();



            String customerCountryCode = getCustomerProfileVo.country;
            CountryCodePojo countryCodePojo = dbHelper.getDetailsByCountryCode(customerCountryCode);
            flagImage.setImageBitmap(Common.getBitmapFromByteArray(countryCodePojo.getFlag()));
            tvCountryCode.setText(customerCountryCode == null ? "":"+"+customerCountryCode);
            phone_number.setText(getCustomerProfileVo.phoneNumber);


            spinnerLanguage.setSelection(Integer.parseInt(getCustomerProfileVo.language));


            String emergencyCountryCode = getCustomerProfileVo.emergencyCountryCode;
            if (emergencyCountryCode!=null ) {
                ctvEmergencyName.setText(getCustomerProfileVo.emergencyName);
                CountryCodePojo emergencyCountryCodePojo = dbHelper.getDetailsByCountryCode(emergencyCountryCode);
                ivEmergencyContactFlag.setImageBitmap(Common.getBitmapFromByteArray(emergencyCountryCodePojo.getFlag()));
                ctvEmergencyCountryCode.setText(emergencyCountryCode == null ? "" : "+" + emergencyCountryCode);
                ctvEmergencyPhoneNumber.setText(getCustomerProfileVo.emergencyMobile);
            }else{
                ivEmergencyContactFlag.setImageBitmap(Common.getBitmapFromByteArray(countryCodePojo.getFlag()));
                ctvEmergencyCountryCode.setText(customerCountryCode == null ? "" : "+" + customerCountryCode);
            }

//            Picasso.with(this).load(getCustomerProfileVo.profilePic).memoryPolicy(MemoryPolicy.NO_CACHE ).networkPolicy(NetworkPolicy.NO_CACHE).transform(new CircleTransform()).into(profileImgView);

            Common.setCircleImageBackgroundFromUrl(this, profileImgView, getCustomerProfileVo.profilePic);



        }




        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.hideSoftKeyboard(UpdateProfileActivity.this);
                String fname = first_name.getText().toString().trim();
                String lastName = last_name.getText().toString().trim();
                String emailId = email.getText().toString().trim();
                String phoneNumber=phone_number.getText().toString().trim();
              //  if (mFileTemp!=null) {
                    if (fname.length()<3) {
                        first_name.requestFocus();
                        first_name.setError(Common.getStringResourceText(R.string.please_enter_first_name));
                    } else if (lastName.length()<3) {
                        last_name.requestFocus();
                        last_name.setError(Common.getStringResourceText(R.string.please_enter_last_name));
                    }
                      else if (!Common.isValidEmail(emailId)) {
                        email.requestFocus();
                        email.setError(Common.getStringResourceText(R.string.please_enter_emailid));

                    } else if (phoneNumber.length()<9) {
                        phone_number.requestFocus();
                        phone_number.setError(Common.getStringResourceText(R.string.please_enter_mobile_number));

                    }else if (!Common.isValidMobileNumber(phoneNumber)) {
                        phone_number.requestFocus();
                        phone_number.setError(Common.getStringResourceText(R.string.please_enter_valid_mobile_number));

                    }
                    else if( Common.haveInternet(getApplicationContext()))
                      //  callUpdateProfileApi(fname, lastName,
                      //          emailId, "1900-01-01", phoneNumber);
                    {


                        HashMap<String, String> hashMap = new HashMap<>();

                        hashMap.put(ServiceUrls.RequestParams.FIRST_NAME, fname);
                        hashMap.put(ServiceUrls.RequestParams.LAST_NAME, lastName);
                        hashMap.put(ServiceUrls.RequestParams.DOB, "");
                        hashMap.put(ServiceUrls.RequestParams.AGE, "");
                        hashMap.put(ServiceUrls.RequestParams.GENDER, "");
                        hashMap.put(ServiceUrls.RequestParams.LANGUAGE, String.valueOf(((Language)spinnerLanguage.getSelectedItem()).getLanguageCode()));
                        hashMap.put(ServiceUrls.RequestParams.EMERGENCY_NAME, ctvEmergencyName.getText().toString().trim());
                        hashMap.put(ServiceUrls.RequestParams.EMERGENCY_COUNTRY_CODE, ctvEmergencyCountryCode.getText().toString().trim());
                        hashMap.put(ServiceUrls.RequestParams.EMERGENCY_MOBILE, ctvEmergencyPhoneNumber.getText().toString().trim());
                        hashMap.put(ServiceUrls.RequestParams.DEVICE, "Android");


                        HashMap<String, TypedFile> fileHashMap = new HashMap<>();

                        if(mFileTemp != null)
                        {
                            Log.d("profilePicFile",mFileTemp.getPath()+"\n"+mFileTemp.length());
                            fileHashMap.put(ServiceUrls.MultiPartRequestParams.IMAGE, Common.getTypedFile(new File(mFileTemp.getPath())));

                        }



                        new RetrofitRequester(UpdateProfileActivity.this).sendMultiPartRequest(ServiceUrls.RequestNames.UPDATE_CUSTOMER_PROFILE, hashMap,fileHashMap);
                    }

                     else {
                        Common.customToast(getApplicationContext(), Common.INTERNET_UNABLEABLE, Common.TOAST_TIME);
                    }
            }
        });
        profileImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               CropImage.startPickImageActivity(UpdateProfileActivity.this);
            }
        });
        hideKeyboardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Common.hideSoftKeyboard(UpdateProfileActivity.this);
                return true;
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

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

                profileImgView.setImageURI(result.getUri());
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
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId) {

        if (apiResponseVo.code != Constants.SUCCESS)
        {
            Common.customToast(this, apiResponseVo.status);
            return;
        }


        switch (apiResponseVo.requestname) {

            case ServiceUrls.RequestNames.UPDATE_CUSTOMER_PROFILE:



                TATX.updateLanguage(UpdateProfileActivity.this,((Language)spinnerLanguage.getSelectedItem()).getLocaleCode());

                break;
        }
    }
}
