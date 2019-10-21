package com.tatx.partnerapp.menuactivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.mime.TypedFile;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tatx.partnerapp.R;
import com.tatx.partnerapp.abstractclasses.BaseActivity;
import com.tatx.partnerapp.activities.GoogleMapDrawerActivity;
import com.tatx.partnerapp.activities.SplashActivity;
import com.tatx.partnerapp.adapter.CustomArrayAdapterLanguages;
import com.tatx.partnerapp.application.TATX;
import com.tatx.partnerapp.commonutills.CircleTransform;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.constants.ServiceUrls;
import com.tatx.partnerapp.customviews.TripCanceledAlertDialog;
import com.tatx.partnerapp.database.SqliteDB;
import com.tatx.partnerapp.database.UpdateProfileResponse;
import com.tatx.partnerapp.dataset.LoginData;
import com.tatx.partnerapp.enums.Language;
import com.tatx.partnerapp.network.RetrofitRequester;
import com.tatx.partnerapp.network.RetrofitResponseListener;
import com.tatx.partnerapp.pojos.ApiResponseVo;
import com.tatx.partnerapp.pojos.DriverOnTripVo;
import com.tatx.partnerapp.pojos.GetDriverProfileVo;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class DisplayProfileActivity extends BaseActivity implements RetrofitResponseListener {


    SqliteDB sqliteDB;

    public static final int REQUEST_CODE_CROP_IMAGE = 0x3;

    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;

    public static final int REQUEST_CODE_GALLERY = 0x1;

    public static final String IMAGE_DIRECTORY_NAME = "Tatx";

    private File mFileTemp;


    private String imagePath;

    private View[] editableViews;


    @BindView(R.id.et_last_name)
    EditText etLastName;

    @BindView(R.id.et_first_name)
    EditText etFirstName;

    @BindView(R.id.et_email)
    EditText etEmail;

    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;

    @BindView(R.id.tv_contry_code)
    TextView tvContryCode;

    @BindView(R.id.tv_save)
    TextView tvSave;

    @BindView(R.id.tv_edit)
    TextView tvEdit;

    @BindView(R.id.ll_driver_other_details)
    LinearLayout llDriverOtherDetails;

    @BindView(R.id.tv_suspensions)
    TextView tvSuspensions;

    @BindView(R.id.tv_earnings)
    TextView tvEarnings;

    @BindView(R.id.iv_profile_pic)
    ImageView ivProfilePic;

    @BindView(R.id.tv_start_date)
    TextView tvStartDate;

    @BindView(R.id.tv_avg_rating)
    TextView tvAvgRating;

    @BindView(R.id.tv_avg_acceptance_rate)
    TextView tvAvgAcceptanceRate;

    @BindView(R.id.tv_avg_earnings)
    TextView tvAvgEarnings;

    @BindView(R.id.tv_tatx_id)
    TextView tvTatxId;

    @BindView(R.id.spinner_language)
    Spinner spinnerLanguage;

    @BindView(R.id.ll_root_view_kbh)
    LinearLayout llRootViewKbh;

    private List<LoginData> userProfile;
    private String locale;
    private View[] editableViewsDisable;
    private Uri mCropImageUri;
    private TripCanceledAlertDialog tripCanceledAlertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.profile_driver);

        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.profile_driver));

        Common.setupUI(this, findViewById(android.R.id.content));




        sqliteDB = new SqliteDB(this);
        userProfile = sqliteDB.getUserProfile();
        editableViewsDisable = new View[]{etFirstName, etLastName, etEmail, etPhoneNumber, tvContryCode};

        editableViews = new View[]{etFirstName, etLastName};

        Common.Log.i("etLastName : " + etLastName);

        Common.Log.i("editableViews.length : " + editableViews.length);

        setViewsEnableStatus(false, editableViewsDisable);

        setViewsVisibilityStatus(View.GONE, tvSave);

        ivProfilePic.setClickable(false);

        spinnerLanguage.setAdapter(new CustomArrayAdapterLanguages(this, R.layout.languages_item, Language.values()));


        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.GET_DRIVER_PROFILE, null, Constants.RequestCodes.ONCREATE_REQUEST_CODE);


    }


    @OnClick(R.id.tv_save)
    public void saveFunctionality(View view) {

        Common.hideSoftKeyboard(DisplayProfileActivity.this);

        String fname = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String emailId = etEmail.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();

        if (fname.length() < 3) {
            etFirstName.requestFocus();
            etFirstName.setError(Common.getStringFromResources(R.string.please_enter_first_name));
        } else if (lastName.length() < 3) {
            etLastName.requestFocus();
            etLastName.setError(Common.getStringFromResources(R.string.please_enter_last_name));
        } else if (!Common.isValidEmail(emailId)) {
            etEmail.requestFocus();
            etEmail.setError(Common.getStringFromResources(R.string.please_enter_emailid));

        }/* else if (phoneNumber.length()<9) {
            etPhoneNumber.requestFocus();
            etPhoneNumber.setError("Please enter Mobile Number");

        }*/ else if (!Common.isValidMobileNumber(phoneNumber)) {
            etPhoneNumber.requestFocus();
            etPhoneNumber.setError(Common.getStringFromResources(R.string.please_enter_valid_mobile_number));

        } else {

            HashMap<String, String> hashMap = new HashMap<>();

            hashMap.put(ServiceUrls.ResponseParams.FIRST_NAME, etFirstName.getText().toString());
            hashMap.put(ServiceUrls.ResponseParams.LAST_NAME, etLastName.getText().toString());
//            hashMap.put(ServiceUrls.ResponseParams.EMAIL, etEmail.getText().toString());
//            hashMap.put(ServiceUrls.ResponseParams.PHONE_NUMBER, etPhoneNumber.getText().toString());
            hashMap.put(ServiceUrls.ApiRequestParams.LANGUAGE, String.valueOf(((Language) spinnerLanguage.getSelectedItem()).getLanguageCode()));


            HashMap<String, TypedFile> fileHashMap = new HashMap<>();

            if (mFileTemp != null) {
                Log.d("profilePicFile", mFileTemp.getPath() + "\n" + mFileTemp.length());
                fileHashMap.put(ServiceUrls.MultiPartRequestParams.IMAGE, Common.getTypedFile(new File(mFileTemp.getPath())));

            }


            new RetrofitRequester(this).sendMultiPartRequest(ServiceUrls.RequestNames.UPDATE_DRIVER_PROFILE, hashMap, fileHashMap);
        }

    }


    @OnClick(R.id.tv_edit)
    public void editFunctionality(View view) {

        setViewsEnableStatus(true, editableViews);

        setViewsVisibilityStatus(View.VISIBLE, tvSave);

        setViewsVisibilityStatus(View.GONE, tvEdit, llDriverOtherDetails);
        ivProfilePic.setClickable(true);

    }


    private void setViewsEnableStatus(boolean status, View... views) {


        Common.Log.i("views.length : " + views.length);

        for (View view : views) {

            Common.Log.i("view : " + view);
            Common.Log.i("view.toString() : " + view.toString());
            view.setFocusableInTouchMode(status);
            view.setFocusable(status);
            view.setEnabled(status);

        }


    }


    private void setViewsVisibilityStatus(int status, View... views) {


        Common.Log.i("views.length : " + views.length);

        for (View view : views) {

            Common.Log.i("view : " + view);
            Common.Log.i("view.toString() : " + view.toString());
            view.setVisibility(status);

        }


    }


    @OnClick(R.id.iv_profile_pic)
    public void showChoosePictureDialog(View view) {


        //  CropImage.startPickImageActivity(this);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

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
                mFileTemp = Common.savefile(this, result.getUri());
                // Toast.makeText(this, "Cropping successful, Sample: \n" + result.getSampleSize()+"result.getUri() \n "+result.getUri()+"result\n"+result.getBitmap()+"\nprofilePicFile\n"+profilePicFile.length(), Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }

        // super.onActivityResult(requestCode, resultCode, data);
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
                .setAspectRatio(5, 5)
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(this);
    }


    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId) {


        if (apiResponseVo.code != Constants.SUCCESS) {
            Common.customToast(this, apiResponseVo.status);
            return;
        }


        switch (apiResponseVo.requestname) {

            case ServiceUrls.RequestNames.GET_DRIVER_PROFILE:

                Common.Log.i("apiResponseVo.data : " + apiResponseVo.data);

//                GetDriverProfileDataVo getDriverProfileDataVo = (GetDriverProfileDataVo) apiResponseVo.data;


                String jsonString = new Gson().toJson(apiResponseVo.data);

//                GetDriverProfileDataVo getDriverProfileDataVo = new Gson().fromJson(json, new TypeToken<GetDriverProfileDataVo>() { }.getType());
                GetDriverProfileVo getDriverProfileVo = new Gson().fromJson(jsonString, GetDriverProfileVo.class);

                tvTatxId.setText(getDriverProfileVo.tatxId);
                etFirstName.setText(getDriverProfileVo.firstName);
                etLastName.setText(getDriverProfileVo.lastName);
                etEmail.setText(getDriverProfileVo.email);
                tvContryCode.setText("+" + getDriverProfileVo.country);
                etPhoneNumber.setText(getDriverProfileVo.phoneNumber.substring(String.valueOf(getDriverProfileVo.country).length()));
//                tvSuspensions.setText(getDriverProfileDataVo.);
                tvEarnings.setText(String.valueOf(getDriverProfileVo.earning));
                tvStartDate.setText(getDriverProfileVo.createdAt);
                tvAvgRating.setText(getDriverProfileVo.avgrating);
                tvAvgAcceptanceRate.setText(String.valueOf(Common.getTwoDecimalRoundValueString(getDriverProfileVo.avgacceptance)));
                tvAvgEarnings.setText(String.valueOf(getDriverProfileVo.erningday));
                spinnerLanguage.setSelection(Integer.parseInt(getDriverProfileVo.language));


                if (getDriverProfileVo.image != null) {
                    // Picasso.with(DisplayProfileActivity.this).invalidate(imagePath);
                    Log.d("imagePath", getDriverProfileVo.image);
                    Log.d("response", ">>>>...Anil1 " + imagePath);
                    Picasso.with(this).load(getDriverProfileVo.image).memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE).transform(new CircleTransform()).into(ivProfilePic);
                }


                break;

            case ServiceUrls.RequestNames.UPDATE_DRIVER_PROFILE:

                UpdateProfileResponse updateProfileResponse = Common.getSpecificDataObject(apiResponseVo.data, UpdateProfileResponse.class);

                Common.Log.i("? - statusorder - o.toString() 3 : " + "requestCode : " + apiResponseVo.data);

                Common.customToast(this, apiResponseVo.status);

                TATX.updateLanguage(DisplayProfileActivity.this, ((Language) spinnerLanguage.getSelectedItem()).getLocaleCode());
                Intent intent;
                DriverOnTripVo driverOnTripVo = new DriverOnTripVo();
                driverOnTripVo.driverStatus=updateProfileResponse.onlineStatus;

                if (updateProfileResponse.onlineStatus == 0 || updateProfileResponse.onlineStatus == 1) {
                    intent = new Intent(this, GoogleMapDrawerActivity.class);
                } else {
                    intent = new Intent(this, SplashActivity.class);
                }

                intent.putExtra(Constants.ONLINE_STATUS, updateProfileResponse.onlineStatus);

                intent.putExtra(Constants.KEY_1, driverOnTripVo);

                intent.putExtra(Constants.FROM_DISPLAY_ACT, true);

                intent.putExtra(Constants.IntentKeys.PROFILE_PIC, updateProfileResponse.profilePic);

                startActivity(intent);

                // this.setResult(Activity.RESULT_OK,intent);

                finish();

                //  Common.refreshActivity(this);


                break;


        }


    }


}


