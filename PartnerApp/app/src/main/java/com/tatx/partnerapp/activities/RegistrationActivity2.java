package com.tatx.partnerapp.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.sahaab.hijri.caldroid.CaldroidFragment;
import com.sahaab.hijri.caldroid.CaldroidListener;
import com.tatx.partnerapp.R;
import com.tatx.partnerapp.abstractclasses.BaseActivity;
import com.tatx.partnerapp.adapter.CountryCodeListAdapter;
import com.tatx.partnerapp.adapter.CustomRecyclerViewAdapterBankNames;
import com.tatx.partnerapp.adapter.CustomRecyclerViewAdapterCarMake;
import com.tatx.partnerapp.adapter.CustomRecyclerViewAdapterCarModel;
import com.tatx.partnerapp.adapter.CustomRecyclerViewAdapterCityNames;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.constants.ServiceUrls;
import com.tatx.partnerapp.customviews.CustomEditText;
import com.tatx.partnerapp.database.CountryCodeSqliteDbHelper;
import com.tatx.partnerapp.database.SqliteDB;
import com.tatx.partnerapp.dataset.CountryCodePojo;
import com.tatx.partnerapp.dataset.LoginData;
import com.tatx.partnerapp.library.CountriesListDialog;
import com.tatx.partnerapp.network.RetrofitRequester;
import com.tatx.partnerapp.network.RetrofitResponseListener;
import com.tatx.partnerapp.pojos.ApiResponseVo;
import com.tatx.partnerapp.pojos.Bank;
import com.tatx.partnerapp.pojos.CheckUserExistanceStatusVo;
import com.tatx.partnerapp.pojos.City;
import com.tatx.partnerapp.pojos.Country;
import com.tatx.partnerapp.pojos.CreateDriverStep2;
import com.tatx.partnerapp.pojos.GetBanksVo;
import com.tatx.partnerapp.pojos.GetCityVo;
import com.tatx.partnerapp.pojos.GetCountriesVo;
import com.tatx.partnerapp.pojos.Make;
import com.tatx.partnerapp.pojos.MakeCabVo;
import com.tatx.partnerapp.pojos.Model;
import com.tatx.partnerapp.pojos.ModelCabVo;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tatx.partnerapp.commonutills.Common.isGooglePlayServicesAvailable;

/**
 * Created by user on 20-05-2016.
 */
public class RegistrationActivity2 extends BaseActivity implements RetrofitResponseListener, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final int REQUEST_CODE_GALLERY = 0x1;
    public static final String IMAGE_DIRECTORY_NAME = "Tatx";
    private String imagePath;
    private List<LoginData> userProfile;
    public static final int REQUEST_CODE_CROP_IMAGE = 0x3;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    SqliteDB sqliteDB;
    GridLayoutManager gridLayoutManager;
    CountryCodeListAdapter countryCodeListAdapter;
    private CountryCodeSqliteDbHelper dbHelper;
    private List<CountryCodePojo> countryCodeList;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private File mFileTemp;
    private CountryCodePojo saudia_flag;
    static final int DATE_PICKER_ID = 1111;
    static final int DATE_PICKER_ID_AFTER = 2222;
    private int year;
    private int month;
    private int day;
    private EditText copyDate;
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;


    @BindView(R.id.cet_date_of_birth)
    EditText cetDateOfBirth;
    @BindView(R.id.age)
    EditText ageTv;
    @BindView(R.id.no_plate)
    EditText plateNo;
    @BindView(R.id.iqama_number)
    EditText iqamaNumber;
    @BindView(R.id.cet_iqama_license_expiry_date)
    EditText cetIqamaLicenseExpiryDate;
    @BindView(R.id.registration_no)
    EditText registrationNo;
    @BindView(R.id.cet_registration_expiry_date)
    EditText cetRegistrationExpiryDate;
    @BindView(R.id.insurance_number)
    EditText cetInsuranceNumber;
    @BindView(R.id.cet_insurance_expiry_date)
    EditText cetInsuranceExpiryDate;
    @BindView(R.id.cet_car_make)
    EditText cetCarMake;
    @BindView(R.id.tnl_car_make_other)
    TextInputLayout tnlCarMakeOther;
    @BindView(R.id.cet_car_make_other)
    EditText cetCarMakeOther;

    @BindView(R.id.cet_car_model)
    EditText cetCarModel;
    @BindView(R.id.tnl_car_model_other)
    TextInputLayout tnlCarModelOther;
    @BindView(R.id.cet_car_model_other)
    EditText cetCarModelOther;

    @BindView(R.id.cet_car_year)
    EditText cetCarYear;
    @BindView(R.id.et_car_color)
    EditText etCarColor;
    @BindView(R.id.cet_iban_number)
    EditText cetIbanNumber;


    @BindView(R.id.cet_bank_name)
    EditText cetBankName;
    @BindView(R.id.cet_bank_country)
    EditText cetBankCountry;
    @BindView(R.id.radioGroup_driver_authority)
    RadioGroup radioGroupDriverAuthority;
    @BindView(R.id.rg_emp_type)
    RadioGroup rgEmpType;

    @BindView(R.id.address)
    EditText etAddress;

    @BindView(R.id.userexists)
    TextView userexists;
    @BindView(R.id.btn_next)
    Button btnNext;
    private boolean dob;
    private String countryId;
    private RadioButton radioButtonAuthority;
    private String ownerAuthority;
    private Context context;
    private DatePickerDialog datePickerDialog;
    @BindView(R.id.ll_main)
    LinearLayout llMain;
    @BindView(R.id.cet_nationality)
    EditText cetNationality;
    @BindView(R.id.cet_gender)
    EditText cetGender;
    @BindView(R.id.cet_city)
    EditText cetCity;
    @BindView(R.id.cet_country_of_residency)
    EditText cetCountryOfResidency;
    @BindView(R.id.ctv_emp_type)
    TextView ctvEmpType;
    @BindView(R.id.ll_non_saudia_no_plate)
    LinearLayout llNonSaudiaNoPlate;
    @BindView(R.id.ll_saudia_plate_no)
    LinearLayout llSaudiaPlateNo;
    @BindView(R.id.saudia_plate_number)
    EditText saudiaPlateNumber;
    @BindView(R.id.saudia_no_plate_letters)
    EditText saudiaNoPlateLetters;
    @BindView(R.id.til_gender)
    TextInputLayout tilGender;
    @BindView(R.id.til_country_of_residency)
    TextInputLayout tilCountryOfResidency;
    @BindView(R.id.til_bank_country)
    TextInputLayout tilBankCountry;







    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog progressDialog;
    private GetCountriesVo getCountriesVo;
    private Locale defaultLocale;
    private String countryName="";
    private String cityName;

//    private Bundle state;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        setContentView(R.layout.activity_registration_2);


        ButterKnife.bind(this);

        setTitleText(Common.getStringResourceText(R.string.reg));

        initializedAllValues();


        setRemoveErrorOnTextChange(cetDateOfBirth, cetBankCountry, cetIqamaLicenseExpiryDate, cetRegistrationExpiryDate, cetCarMake, cetCarModel);

//        state = savedInstanceState;
        setRemoveErrorOnTextChange(cetNationality, cetGender, cetCity, cetInsuranceExpiryDate);

        Common.hideSoftKeyboard(this, llMain);
/*
        String currentCountryName = getApplicationContext().getResources().getConfiguration().locale.getDisplayCountry();

        Common.Log.i("? - currentCountryName : "+currentCountryName);
*/

        if (!isGooglePlayServicesAvailable(this)) {
            Common.customToast(this, Common.getStringResourceText(R.string.google_play_services_are_not_available));
            finish();
        }

        createLocationRequest();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        progressDialog = Common.showProgressDialog(this);


        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.GET_COUNTRIES, null, Constants.RequestCodes.ONCREATE_REQUEST_CODE);

        defaultLocale=Locale.getDefault();

        Locale.setDefault(new Locale("en"));
        Configuration config = new Configuration();
        config.locale = new Locale("en");
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());


    }


    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }


    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    public void initializedAllValues() {


        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        context = getApplicationContext();

        String line1Number = tm.getLine1Number();

        Common.Log.i("line1Number : " + line1Number);

        sp = PreferenceManager.getDefaultSharedPreferences(this);

        editor = sp.edit();

        sqliteDB = new SqliteDB(this);

        getCounrtyCodeFromDB();

        cetGender.setText(Common.getStringResourceText(R.string.male));
        cetGender.setTag("1");

    }


    @OnClick(R.id.btn_next)
    void next() {
        Log.d("cetNationality",String.valueOf(cetNationality.getTag()));
        Common.hideSoftKeyboard(RegistrationActivity2.this);
        int selectedId = radioGroupDriverAuthority.getCheckedRadioButtonId();

        radioButtonAuthority = (RadioButton) findViewById(selectedId);

        String cityName = cetCity.getText().toString().trim();
        String plateNumber;
        EditText view;
        if (cetCountryOfResidency.getTag().equals(Constants.SAUDI_COUNTRY_CODE)){
            plateNumber=saudiaPlateNumber.getText().toString().trim()+saudiaNoPlateLetters.getText().toString().trim();
            view=saudiaNoPlateLetters;
        }else {
            plateNumber=plateNo.getText().toString().trim();
            view=plateNo;
        }


        if (cetGender.getText().toString().trim().length() < 3) {
            Common.requestChildFocus(cetGender);
            cetGender.setError(Common.getStringFromResources(R.string.please_select_your_gender));
          //  Common.customToast(this, "Please select your Gender", Common.TOAST_TIME);

        } else if (cetCity.getText().toString().trim().length() < 3) {
            Common.requestChildFocus(cetCity);
            cetCity.setError(Common.getStringFromResources(R.string.please_enter_city_name));
            //Common.customToast(this, "Please enter city name", Common.TOAST_TIME);

        } else if (cetDateOfBirth.getText().toString().trim().length() < 3) {
            cetDateOfBirth.setError(Common.getStringFromResources(R.string.please_enter_date_of_birth));
           // Common.customToast(this, "Please enter Date Of Birth");
            Common.requestChildFocus(cetDateOfBirth);

        }else if (cetNationality.getText().toString().trim().length() < 3) {
            cetNationality.requestFocus();
            cetNationality.setError(Common.getStringFromResources(R.string.please_select_country_name));
            Common.requestChildFocus(cetNationality);
           // Common.customToast(this, "Please select country name", Common.TOAST_TIME);

        } else if (iqamaNumber.getText().toString().trim().length() < 3) {
            iqamaNumber.setError(Common.getStringFromResources(R.string.please_enter_iqama_number));
            Common.requestChildFocus(iqamaNumber);


        } else if (cetIqamaLicenseExpiryDate.getText().toString().trim().length() < 3) {
            cetIqamaLicenseExpiryDate.setError(Common.getStringFromResources(R.string.please_enter_iqama_expiry_date));
           // Common.customToast(this, "Please enter IQAMA Expiry Date");
            Common.requestChildFocus(cetIqamaLicenseExpiryDate);

        }
        else if (cetInsuranceNumber.getText().toString().trim().length() < 3) {
            cetInsuranceNumber.setError(Common.getStringFromResources(R.string.Please_enter_insurance_number));
            Common.requestChildFocus(cetInsuranceNumber);

        }
        else if (cetRegistrationExpiryDate.getText().toString().trim().length() < 3) {
            cetRegistrationExpiryDate.setError(Common.getStringFromResources(R.string.please_enter_registration_expiry_date));
           // Common.customToast(this, "Please enter registration Expiry Date");
            Common.requestChildFocus(cetRegistrationExpiryDate);

        } else if (cetInsuranceExpiryDate.getText().toString().trim().length() < 3) {
            cetInsuranceExpiryDate.setError(Common.getStringFromResources(R.string.please_enter_insurance_expiry_date));
            //Common.customToast(this, "Please enter Insurance Expiry Date");
            Common.requestChildFocus(cetInsuranceExpiryDate);


        } else if (plateNumber.length() < 3) {
            view.setError(Common.getStringFromResources(R.string.please_enter_car_number));
            Common.requestChildFocus(view);

        }else if (cetCountryOfResidency.getTag().equals(Constants.SAUDI_COUNTRY_CODE) && saudiaPlateNumber.getText().toString().trim().length()< 2){
            saudiaPlateNumber.setError(Common.getStringFromResources(R.string.please_enter_car_number));
            Common.requestChildFocus(saudiaPlateNumber);
        }

        else if (cetCarMake.getText().toString().trim().length() < 1) {
            cetCarMake.setError(Common.getStringFromResources(R.string.please_select_car_make));
            //Common.customToast(this, "Please select Car Make");
            Common.requestChildFocus(cetCarMake);

        } else if (cetCarModel.getText().toString().trim().length() < 1) {
            cetCarModel.setError(Common.getStringFromResources(R.string.please_select_car_model));
            //Common.customToast(this, "Please select Car Model");
            Common.requestChildFocus(cetCarModel);

        } else if (cetCarYear.getText().toString().trim().length() != 4) {
            cetCarYear.setError(Common.getStringFromResources(R.string.please_enter_car_year_yyyy));
            Common.requestChildFocus(cetCarYear);

        } else if (cetCarYear.getText().toString().trim().length() == 4 && Integer.parseInt(cetCarYear.getText().toString().trim()) > Calendar.getInstance().get(Calendar.YEAR)+1) {
            int maxYear = Calendar.getInstance().get(Calendar.YEAR) + 1;
            cetCarYear.setError(Common.getStringFromResources(R.string.year_should_not_be_grater_than_current_year) + " "+maxYear);
            Common.requestChildFocus(cetCarYear);
        } else if (cetIbanNumber.getText().toString().trim().length() < 1) {
            cetIbanNumber.setError(Common.getStringFromResources(R.string.please_enter_iban_number));
            Common.requestChildFocus(cetIbanNumber);

        } else if (cetBankCountry.getText().toString().trim().length() < 3) {
            cetBankCountry.setError(Common.getStringFromResources(R.string.please_select_country_name));
            //Common.customToast(this, "Please select country name");
            Common.requestChildFocus(cetBankCountry);

        } else if (cetBankName.getText().toString().trim().length() < 1) {
            cetBankName.setError(Common.getStringFromResources(R.string.please_enter_bank_name));
            //Common.customToast(this, "Please enter Bank Name");
            Common.requestChildFocus(cetBankName);

        } else {
            ownerAuthority = selectedId==R.id.radioButton ? "1" : "0";
            Log.d("ownerAuthority","ownerAuthority"+ownerAuthority);

            String genderType = tilGender.getVisibility()==View.VISIBLE ? cetGender.getTag().toString() : "1";
            String countryOfResidencyType = tilCountryOfResidency.getVisibility()==View.VISIBLE ? cetCountryOfResidency.getTag().toString() : Constants.SAUDI_COUNTRY_CODE;
            String countryOfBankType = tilBankCountry.getVisibility()==View.VISIBLE ? cetBankCountry.getTag().toString() : Constants.SAUDI_COUNTRY_CODE;



            Locale.setDefault(defaultLocale);
            Configuration config = new Configuration();
            config.locale = defaultLocale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());




            String empType = (rgEmpType.getCheckedRadioButtonId() == R.id.rb_emp_type_1) ? "0" : "1"; // 0 for Private and 1 for Govt.

            HashMap<String, String> registrationParams = new HashMap<>();

            registrationParams.put(Constants.IntentKeys.DOB, Common.dateformater_mon(cetDateOfBirth.getText().toString().trim()));
            registrationParams.put(Constants.IntentKeys.IQAMA_NUMBER, iqamaNumber.getText().toString().trim());

            registrationParams.put(Constants.IntentKeys.IQAMA_NUMBER_EXP_DATE, cetIqamaLicenseExpiryDate.getTag().toString().trim());
            registrationParams.put(Constants.IntentKeys.LOCAL_IQAMA_NUMBER_EXP_DATE, cetIqamaLicenseExpiryDate.getText().toString().trim());

//            registrationParams.put(Constants.IntentKeys.VEHICLE_REG_SERIAL_NUMBER, registrationNo.getText().toString().trim());

            registrationParams.put(Constants.IntentKeys.VEHICLE_REG_EXP_DATE, cetRegistrationExpiryDate.getTag().toString().trim());
            registrationParams.put(Constants.IntentKeys.LOCAL_VEHICLE_REG_EXP_DATE, cetRegistrationExpiryDate.getText().toString().trim());

            registrationParams.put(Constants.IntentKeys.INSURANCE_NUMBER, cetInsuranceNumber.getText().toString().trim());
            registrationParams.put(Constants.IntentKeys.INSURANCE_EXP_DATE, Common.dateformater_mon(cetInsuranceExpiryDate.getText().toString().trim()));
//            registrationParams.put(Constants.IntentKeys.VEHICLE_ENG_NUMBER, plateNo.getText().toString().trim());
            registrationParams.put(Constants.IntentKeys.VEHICLE_ENG_NUMBER, plateNumber);

            registrationParams.put(Constants.IntentKeys.CAR_MAKER, cetCarMake.getTag().toString());

            registrationParams.put(Constants.IntentKeys.CAR_MODEL_NAME, cetCarModelOther.getText().toString());

            registrationParams.put(Constants.IntentKeys.CAR_MODEL, cetCarModel.getTag().toString());

            registrationParams.put(Constants.IntentKeys.CAR_MAKE_NAME, cetCarMakeOther.getText().toString());

            registrationParams.put(Constants.IntentKeys.CAR_YEAR, cetCarYear.getText().toString().trim());
            registrationParams.put(ServiceUrls.ApiRequestParams.COLOR, etCarColor.getText().toString().trim());
            registrationParams.put(Constants.IntentKeys.OWNER, ownerAuthority);
            registrationParams.put(ServiceUrls.ApiRequestParams.EMPLOYEE_TYPE, empType);
            registrationParams.put(Constants.IntentKeys.IBAN_NUMBER, cetIbanNumber.getText().toString().trim());
            registrationParams.put(Constants.IntentKeys.BANK_NAME, cetBankName.getTag().toString());
            registrationParams.put(Constants.IntentKeys.BANK_COUNTRY, countryOfBankType);

            registrationParams.put(Constants.IntentKeys.GENDER, genderType);
            registrationParams.put(Constants.IntentKeys.NATIONALITY, String.valueOf(cetNationality.getTag()));
            registrationParams.put(Constants.IntentKeys.COUNTRY_OF_RESIDENCY, countryOfResidencyType);
            registrationParams.put(Constants.IntentKeys.CITY, String.valueOf(cetCity.getTag()));
            registrationParams.put(Constants.IntentKeys.ADDRESS, etAddress.getText().toString().trim());


            /*
            Common.Log.i("? - new Gson().toJson(registrationParams) : "+new Gson().toJson(registrationParams));

            if (true)
            {
                return;
            }
            */


            new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.CREATE_DRIVER_STEP2, registrationParams);


        }


    }


    @OnClick(R.id.cet_city)
    void setCity() {

        Common.Log.i("cetCountryOfResidency.getTag() : " + cetNationality.getTag());

//        if (cetNationality.getTag()!= null)
        if (cetCountryOfResidency.getTag() != null) {

            HashMap<String, String> params = new HashMap<>();
//            params.put(ServiceUrls.ApiRequestParams.COUNTRY, cetNationality.getTag().toString());
            params.put(ServiceUrls.ApiRequestParams.COUNTRY, cetCountryOfResidency.getTag().toString());
            new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.GET_CITY, params,R.id.cet_city);

        } else {

            Common.customToast(this, Common.getStringResourceText(R.string.please_select_country_of_residency_first));

//            cetBankCountry.clearFocus();
            btnNext.requestFocus();

        }


    }


    private void setData() {
        userProfile = sqliteDB.getUserProfile();
    }


    public void genderPopUp(View view) {
        PopupMenu popup = new PopupMenu(RegistrationActivity2.this, view);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.menu_gender, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                cetGender.setText(item.getTitle());
                Common.Log.i("getItemId:- "+item.getItemId());
                if (Common.getStringResourceText(R.string.male).equals(item.getTitle())) {
                    cetGender.setTag("1");
                } else {
                    cetGender.setTag("2");
                }

                return true;
            }
        });

        popup.show();//showing popup menu
    }


    @OnClick(R.id.cet_gender)
    void setGender() {
        genderPopUp(cetGender);
    }


    @OnClick(R.id.cet_nationality)
    public void setNationality() {

//        countryCodeAlert(countryCodeList,true);
/*
        CountriesListDialog countriesListDialog = new CountriesListDialog(this, (ArrayList<Country>) getCountriesVo.countries);

        countriesListDialog.setOnCountrySelectedListener(new CountriesListDialog.OnCountrySelectedListener()
        {
            @Override
            public void onCountrySelected(Country country)
            {
                cetNationality.setText(country.countryName);
                cetNationality.setTag(country.countryId);
            }

        });*/


        getCountriesData(R.id.cet_nationality);


    }

    @OnClick(R.id.cet_country_of_residency)
    public void setCetCountryOfResidency() {

//        countryCodeAlert(countryCodeList,true);
/*
        CountriesListDialog countriesListDialog = new CountriesListDialog(this, (ArrayList<Country>) getCountriesVo.countries);

        countriesListDialog.setOnCountrySelectedListener(new CountriesListDialog.OnCountrySelectedListener()
        {
            @Override
            public void onCountrySelected(Country country)
            {
                cetNationality.setText(country.countryName);
                cetNationality.setTag(country.countryId);
            }

        });*/


        getCountriesData(R.id.cet_country_of_residency);


    }


    public void getCounrtyCodeFromDB() {
        dbHelper = new CountryCodeSqliteDbHelper(this);
        dbHelper.openDataBase();
        countryCodeList = dbHelper.getCountryCodeFlagList();
        Log.d("countryCodeList", countryCodeList.toString());
        saudia_flag = dbHelper.getDetailsByCountryCode("966");


    }


    public void createCalenarInstance() {
        Calendar c = Calendar.getInstance(new Locale("en"));
        c.add(Calendar.DATE, 0);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
    }


    @Override

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:

                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date picker
                DatePickerDialog pickerDialog = new DatePickerDialog(this, pickerListener, year, month, day);
                pickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                return pickerDialog;
            case DATE_PICKER_ID_AFTER:
                DatePickerDialog pickerDialog1 = new DatePickerDialog(this, pickerListener, year, month, day);
                pickerDialog1.getDatePicker().setMinDate(new Date().getTime() - 10000);

                return pickerDialog1;

        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // Show selected date
            copyDate.setText(new StringBuilder().append(pad(day)).append("-").append(pad(month + 1)).append("-").append(pad(year)));

            if (dob) {
                ageTv.setText("" + Common.getAge(year, month, day));
                dob = false;
            }

        }
    };

    /************************
     * date formate 01/02/2015*******Used 0 before value<10
     ********************/
    static String pad(int c) {
        if (c >= 10) {
            return String.valueOf(c);
        } else {
            return '0' + String.valueOf(c);
        }
    }


    @OnClick(R.id.cet_iqama_license_expiry_date)
    void setIqamaLicenseExpiryDate() {
/*
        createCalenarInstance();

        copyDate = cetIqamaLicenseExpiryDate;

        showDialog(DATE_PICKER_ID_AFTER);*/

        showDatePickerAndSetSelectedDate(cetIqamaLicenseExpiryDate, Constants.DATE_FORMAT_STRING, (String) cetCountryOfResidency.getTag(), Calendar.getInstance().getTimeInMillis(), 0);


    }

    private void showDatePickerAndSetSelectedDate(final EditText editText, String dateFormatString, String countryCode, final long minDate, final long maxDate) {

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatString);

        final Calendar calendar = Calendar.getInstance(new Locale("en"));


        if (countryCode.equals(Constants.SAUDI_COUNTRY_CODE))
        {


            final CaldroidFragment dialogCaldroidFragment = new CaldroidFragment();

            dialogCaldroidFragment.setCaldroidListener(new CaldroidListener() {

                @Override
                public void onSelectDate(Date date, View view) {


//                    Toast.makeText(getApplicationContext(), simpleDateFormat.format(date), Toast.LENGTH_SHORT).show();


                    calendar.setTime(date);

                    Common.Log.i("? - calendar.toString() : " + calendar.toString());

                    Common.Log.i("? - Year : " + calendar.get(Calendar.YEAR));
                    Common.Log.i("? - Month : " + calendar.get(Calendar.MONTH));
                    Common.Log.i("? - Day : " + calendar.get(Calendar.DAY_OF_MONTH));

//                    editText.setTag(calendar.get(Calendar.YEAR)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.DAY_OF_MONTH));

                    editText.setTag(simpleDateFormat.format(calendar.getTime()));


                    UmmalquraCalendar uCal = new UmmalquraCalendar();

//                uCal.setTime(gCal.getTime());
                    uCal.setTime(date);

                    int year = uCal.get(Calendar.YEAR);         // 1433
                    int month = uCal.get(Calendar.MONTH);        // 2
                    int day = uCal.get(Calendar.DAY_OF_MONTH); // 20

                    uCal.getDisplayName(Calendar.MONTH, Calendar.SHORT, new Locale("en"));


                    Common.Log.i("? - uCal.toString()" + uCal.toString());

                    Common.Log.i("? - Local : year+\" - \"+month+\" - \"+day : " + year + " - " + month + " - " + day);


//                    Calendar calendar = Calendar.getInstance();


                    if ((minDate != 0 && uCal.getTimeInMillis() <= minDate) || (maxDate != 0 && uCal.getTimeInMillis() >= maxDate)) {
                        Common.customToast(RegistrationActivity2.this, Common.getStringResourceText(R.string.please_choose_valid_date));

                        return;
                    }


//                    editText.setText(simpleDateFormat.format(date));
//                    editText.setText(simpleDateFormat.format(calendar.getTime()));
//                    editText.setText(simpleDateFormat.format(uCal.getTime()));
                    editText.setText(year + "-" + (month + 1) + "-" + day);

                    dialogCaldroidFragment.dismiss();


                }

                @Override
                public void onChangeMonth(int month, int year) {
                    /*
                    String text = "month: " + month + " year: " + year;
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                    */
                }

                @Override
                public void onLongClickDate(Date date, View view) {

//                    Toast.makeText(getApplicationContext(), "Long click " + simpleDateFormat.format(date), Toast.LENGTH_SHORT).show();

//                GregorianCalendar gCal = new GregorianCalendar(2012, Calendar.FEBRUARY, 12);

//                GregorianCalendar gCal = new GregorianCalendar(date.getYear(), date.getMonth(), date.getDay());

/*

                    UmmalquraCalendar uCal = new UmmalquraCalendar();

//                uCal.setTime(gCal.getTime());
                    uCal.setTime(date);

                    int year = uCal.get(Calendar.YEAR);         // 1433
                    int month = uCal.get(Calendar.MONTH);        // 2
                    int day = uCal.get(Calendar.DAY_OF_MONTH); // 20

                    uCal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH);


                    Log.i("SKP",uCal.toString());

                    Log.i("SKP",year+" - "+month+" - "+day);
*/


                }

                @Override
                public void onCaldroidViewCreated() {/*
                if (caldroidFragment.getLeftArrowButton() != null) {
                    Toast.makeText(getApplicationContext(),
                            "Caldroid view is created", Toast.LENGTH_SHORT)
                            .show();
                }*/
                }

            });

         /*   // If activity is recovered from rotation
            final String dialogTag = "CALDROID_DIALOG_FRAGMENT";
            if (state != null) {
                dialogCaldroidFragment.restoreDialogStatesFromKey(
                        getSupportFragmentManager(), state,
                        "DIALOG_CALDROID_SAVED_STATE", dialogTag);
                Bundle args = dialogCaldroidFragment.getArguments();
                if (args == null) {
                    args = new Bundle();
                    dialogCaldroidFragment.setArguments(args);
                }
            } else {
                // Setup arguments
                Bundle bundle = new Bundle();
                // Setup dialogTitle
                dialogCaldroidFragment.setArguments(bundle);
            }

            dialogCaldroidFragment.show(getSupportFragmentManager(),
                    dialogTag);

*/


            String dialogTag = "CALDROID_DIALOG_FRAGMENT";


            dialogCaldroidFragment.show(getSupportFragmentManager(), dialogTag);


        } else {


            datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    editText.setTag(year + "-" + monthOfYear + "-" + dayOfMonth);

                    calendar.set(year, monthOfYear, dayOfMonth);

                    editText.setText(simpleDateFormat.format(calendar.getTime()));

                    datePickerDialog.dismiss();

                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

            if (minDate != 0) {
                datePickerDialog.getDatePicker().setMinDate(minDate);
            }


            if (maxDate != 0) {
                datePickerDialog.getDatePicker().setMaxDate(maxDate);
            }

            datePickerDialog.show();


        }


    }


    @OnClick(R.id.cet_insurance_expiry_date)
    void setInsuranceExpiryDate() {
        createCalenarInstance();
        copyDate = cetInsuranceExpiryDate;
        showDialog(DATE_PICKER_ID_AFTER);
    }


    @OnClick(R.id.cet_registration_expiry_date)
    void setRegistrationExpiryDate() {

    /*
        createCalenarInstance();
        copyDate = cetRegistrationExpiryDate;
        showDialog(DATE_PICKER_ID_AFTER);
    */

        showDatePickerAndSetSelectedDate(cetRegistrationExpiryDate, Constants.DATE_FORMAT_STRING, (String) cetCountryOfResidency.getTag(), Calendar.getInstance().getTimeInMillis(), 0);


    }

    @OnClick(R.id.cet_date_of_birth)
    public void dateOfBirthClicked() {
        dob = true;
        createCalenarInstance();
        copyDate = cetDateOfBirth;
        showDialog(DATE_PICKER_ID);
        //String date=checkIn.getText().toString();
    }



    /*@OnClick(R.id.cet_bank_country) public void setNationality(){
        //countryCodeAlert(countryCodeList,true);
    }*/


    void carMake() {

        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.MAKE_CAB, null);

    }


    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, final int requestId) {

        if (apiResponseVo.code != Constants.SUCCESS) {
            Common.customToast(this, apiResponseVo.status);
            return;
        }

        switch (apiResponseVo.requestname) {

            case ServiceUrls.RequestNames.MAKE_CAB:

                MakeCabVo makeCabVo = Common.getSpecificDataObject(apiResponseVo.data, MakeCabVo.class);

                showCarMakesListDialog((ArrayList<Make>) makeCabVo.make);

                break;


            case ServiceUrls.RequestNames.MODEL_CAB:

                ModelCabVo modelCabVo = Common.getSpecificDataObject(apiResponseVo.data, ModelCabVo.class);

                showCarModelListDialog((ArrayList<Model>) modelCabVo.model);

                break;

            case ServiceUrls.RequestNames.CHECK_USER_EXISTANCE_STATUS:


                CheckUserExistanceStatusVo checkUserExistanceStatusVo = Common.getSpecificDataObject(apiResponseVo.data, CheckUserExistanceStatusVo.class);


                if (checkUserExistanceStatusVo.email && checkUserExistanceStatusVo.mobile) {
                    Common.customToast(this, Common.getStringResourceText(R.string.email_id_and_phone_number_already_exists));
                } else if (checkUserExistanceStatusVo.email) {
                    Common.customToast(this, Common.getStringResourceText(R.string.email_id_already_exists));
                } else if (checkUserExistanceStatusVo.mobile) {
                    Common.customToast(this,  Common.getStringResourceText(R.string.phone_number_already_exists));
                } else {
                    /*
                    String countrycode = tvCountryCode.getText().toString().trim();

                    HashMap<String, String> params = new HashMap();
                    params.put(ServiceUrls.ApiRequestParams.EMAIL, etEmail.getText().toString().trim());
                    params.put(ServiceUrls.ApiRequestParams.PHONE_NUMBER, countrycode.substring(1, countrycode.length()) + etPhoneNumber.getText().toString().trim());
*/
                    //    new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.SEND_REGISTRATION_OTP, requestParams);


                }


                break;


            case ServiceUrls.RequestNames.CREATE_DRIVER_STEP2:

                CreateDriverStep2 createDriverStep2 = Common.getSpecificDataObject(apiResponseVo.data, CreateDriverStep2.class);

                Intent intent = new Intent(this, RegistrationActivity3.class);
                intent.putExtra(Constants.IntentKeys.OWNER_AUTHORITY, ownerAuthority);
                intent.putExtra(Constants.IntentKeys.COUNTRY_OF_RESIDENCY, String.valueOf(cetCountryOfResidency.getTag()));
                intent.putExtra(Constants.IntentKeys.NATIONALITY, String.valueOf(cetNationality.getTag()));
                intent.putExtra(Constants.IntentKeys.EMPLOYEE_TYPE, requestParams.get(ServiceUrls.ApiRequestParams.EMPLOYEE_TYPE));
                intent.putExtra(Constants.IntentKeys.CAB_ID, createDriverStep2.cabId);

                startActivity(intent);

                finish();


                break;


            case ServiceUrls.RequestNames.GET_COUNTRIES:

//                GetCountriesVo getCountriesVo = Common.getSpecificDataObject(apiResponseVo.data, GetCountriesVo.class);

                getCountriesVo = Common.getSpecificDataObject(apiResponseVo.data, GetCountriesVo.class);


                if (requestId == Constants.RequestCodes.ONCREATE_REQUEST_CODE) {

                    return;

                }


//                showCountriesListDialog((ArrayList<Country>) getCountriesVo.countries);


                CountriesListDialog countriesListDialog = new CountriesListDialog(this, (ArrayList<Country>) getCountriesVo.countries);

                countriesListDialog.setOnCountrySelectedListener(new CountriesListDialog.OnCountrySelectedListener() {
                    @Override
                    public void onCountrySelected(Country country) {


                        CustomEditText customEditText = ((CustomEditText) findViewById(requestId));
                        customEditText.setText(country.countryName);
                        customEditText.setTag(country.countryId);


//                        if (customEditText.getId()==cetCountryOfResidency.getId())
                        if (customEditText.getId()==cetNationality.getId())
                        {
                            if (customEditText.getTag().equals(Constants.SAUDI_COUNTRY_CODE)) {
                                rgEmpType.setVisibility(View.VISIBLE);
                                ctvEmpType.setVisibility(View.VISIBLE);

                            } else {
                                rgEmpType.setVisibility(View.GONE);
                                ctvEmpType.setVisibility(View.GONE);

                            }
                        } if(customEditText.getId()==cetCountryOfResidency.getId()){
                            cetCity.setText("");

                            if (customEditText.getTag().equals(Constants.SAUDI_COUNTRY_CODE)) {
                                llNonSaudiaNoPlate.setVisibility(View.GONE);
                                llSaudiaPlateNo.setVisibility(View.VISIBLE);
                            } else {
                                llNonSaudiaNoPlate.setVisibility(View.VISIBLE);
                                llSaudiaPlateNo.setVisibility(View.GONE);
                            }

                            cetBankCountry.setText(country.countryName);
                            cetBankCountry.setTag(country.countryId);

                        }


                    }

                });


                break;


            case ServiceUrls.RequestNames.GET_BANKS:

                GetBanksVo getBanksVo = Common.getSpecificDataObject(apiResponseVo.data, GetBanksVo.class);

                showBanksListDialog((ArrayList<Bank>) getBanksVo.bank);

                break;

            case ServiceUrls.RequestNames.GET_CITY:


                GetCityVo getCityVo = Common.getSpecificDataObject(apiResponseVo.data, GetCityVo.class);
                Common.Log.i("requestId" +requestId);
                if (requestId==R.id.cet_city) {

                    showCitiesListDialog((ArrayList<City>) getCityVo.cities);
                }else {


                    for (City city : getCityVo.cities) {

                        if (city.id == Constants.RIYADH_CITY_ID) {
                            cetCity.setText(city.name);
                            cetCity.setTag(city.id);
                        }

                    }
                }


                break;


        }


    }

    private void showCitiesListDialog(ArrayList<City> cityArrayList) {


        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.country_code_dialog);

        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.my_recycler_view);

        Button close = (Button) dialog.findViewById(R.id.button);

        EditText search = (EditText) dialog.findViewById(R.id.search_button);

        setCityListData(recyclerView, cityArrayList, dialog);

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

                ((CustomRecyclerViewAdapterCityNames) recyclerView.getAdapter()).getFilter().filter(charSequence);


            }


            @Override
            public void afterTextChanged(Editable s) {

            }


        });


        dialog.show();


    }

    private void setCityListData(final RecyclerView recyclerView, ArrayList<City> cityArrayList, final Dialog dialog) {


        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                if (TextUtils.isEmpty(cetCity.getText())) {

                    cetCity.setText(" ");


                }

                cetCity.clearFocus();


            }


        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);

        if (cityArrayList != null) {
            CustomRecyclerViewAdapterCityNames customRecyclerViewAdapterCityNames = new CustomRecyclerViewAdapterCityNames(cityArrayList);

            recyclerView.setAdapter(customRecyclerViewAdapterCityNames);

        } else {
            recyclerView.setVisibility(View.GONE);
        }


        ((CustomRecyclerViewAdapterCityNames) recyclerView.getAdapter()).setOnItemClickListener(new CustomRecyclerViewAdapterCityNames.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (dialog != null) {
                    dialog.cancel();
                }

                cetCity.setText(((CustomRecyclerViewAdapterCityNames) recyclerView.getAdapter()).getItem(position).name);

                cetCity.setTag(((CustomRecyclerViewAdapterCityNames) recyclerView.getAdapter()).getItem(position).id);


            }


        });


    }


    private void showCarModelListDialog(ArrayList<Model> modelArrayList) {


        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.make_model_dialog);

        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.my_recycler_view);

        Button close = (Button) dialog.findViewById(R.id.button);

        EditText search = (EditText) dialog.findViewById(R.id.search_button);
        LinearLayout llOther = (LinearLayout) dialog.findViewById(R.id.ll_other);
        recyclerView.setNestedScrollingEnabled(false);

        setCarModelListData(recyclerView, modelArrayList, dialog);
        llOther.setVisibility(View.VISIBLE);
        llOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carModelOther(dialog);
            }
        });



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

                ((CustomRecyclerViewAdapterCarModel) recyclerView.getAdapter()).getFilter().filter(charSequence);


            }


            @Override
            public void afterTextChanged(Editable s) {

            }


        });


        dialog.show();


    }

    private void carModelOther(Dialog dialog) {
        if (dialog != null) {
            dialog.cancel();
        }

        cetCarModel.setText("Other");
        cetCarModel.setTag("-1");
        showCustomMakeOtherDialog(tnlCarModelOther,cetCarModelOther,Common.getStringFromResources(R.string.enter_model));
    }


    private void setCarModelListData(final RecyclerView recyclerView, ArrayList<Model> modelArrayList, final Dialog dialog) {


        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                if (TextUtils.isEmpty(cetCarModel.getText())) {

                    cetCarModel.setText(" ");


                }

                cetCarModel.clearFocus();


            }


        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);

        if (modelArrayList != null) {
            CustomRecyclerViewAdapterCarModel customRecyclerViewAdapterCarModel = new CustomRecyclerViewAdapterCarModel(modelArrayList);

            recyclerView.setAdapter(customRecyclerViewAdapterCarModel);

        } else {
            recyclerView.setVisibility(View.GONE);
        }


        ((CustomRecyclerViewAdapterCarModel) recyclerView.getAdapter()).setOnItemClickListener(new CustomRecyclerViewAdapterCarModel.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (dialog != null) {
                    dialog.cancel();
                }
                cetCarModelOther.setText("");
                tnlCarModelOther.setVisibility(View.GONE);

                cetCarModel.setText(((CustomRecyclerViewAdapterCarModel) recyclerView.getAdapter()).getItem(position).model);

                cetCarModel.setTag(((CustomRecyclerViewAdapterCarModel) recyclerView.getAdapter()).getItem(position).id);


            }


        });


    }

    private void showCarMakesListDialog(ArrayList<Make> makeList) {

        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.make_model_dialog);

        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.my_recycler_view);

        Button close = (Button) dialog.findViewById(R.id.button);

        EditText search = (EditText) dialog.findViewById(R.id.search_button);

        LinearLayout llOther = (LinearLayout) dialog.findViewById(R.id.ll_other);
        recyclerView.setNestedScrollingEnabled(false);

        setCarMakeListData(recyclerView, makeList, dialog);
        llOther.setVisibility(View.VISIBLE);
        llOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carMakeOther(dialog);
            }
        });

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

                ((CustomRecyclerViewAdapterCarMake) recyclerView.getAdapter()).getFilter().filter(charSequence);


            }


            @Override
            public void afterTextChanged(Editable s) {

            }


        });


        dialog.show();


    }

    private void setCarMakeListData(final RecyclerView recyclerView, ArrayList<Make> makeList, final Dialog dialog) {

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                if (TextUtils.isEmpty(cetCarMake.getText())) {

                    cetCarMake.setText(" ");

                }

                cetCarMake.clearFocus();


            }


        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);

        if (makeList != null) {
            CustomRecyclerViewAdapterCarMake customRecyclerViewAdapterCarMake = new CustomRecyclerViewAdapterCarMake(makeList);

            recyclerView.setAdapter(customRecyclerViewAdapterCarMake);

        } else {
            recyclerView.setVisibility(View.GONE);
        }


        ((CustomRecyclerViewAdapterCarMake) recyclerView.getAdapter()).setOnItemClickListener(new CustomRecyclerViewAdapterCarMake.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (dialog != null) {
                    dialog.cancel();
                }
                cetCarMakeOther.setText("");
                tnlCarMakeOther.setVisibility(View.GONE);

                Make make = ((CustomRecyclerViewAdapterCarMake) recyclerView.getAdapter()).getItem(position);

                cetCarMake.setText(make.make);

                cetCarModel.setText(null);

                cetCarMake.setTag(make.id);


            }


        });


    }
private void carMakeOther(final Dialog dialog){
    if (dialog != null) {
        dialog.cancel();
    }


    cetCarMake.setText("Other");

    cetCarModel.setText(null);

    cetCarMake.setTag("-1");
    showCustomMakeOtherDialog(tnlCarMakeOther,cetCarMakeOther,Common.getStringFromResources(R.string.enter_make));
}

    private void carModel() {


        Common.Log.i("cetCarModel.getTag() : " + cetCarMake.getTag());

        if (cetCarMake.getTag() != null) {

            HashMap<String, String> params = new HashMap<>();

            params.put(ServiceUrls.ApiRequestParams.MAKE_ID, String.valueOf(cetCarMake.getTag()));

            new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.MODEL_CAB, params);

        } else {

            Common.customToast(this, Common.getStringResourceText(R.string.please_select_car_make));

            cetCarModel.clearFocus();

        }


    }


    void setRemoveErrorOnTextChange(EditText... editTexts) {

        for (final EditText editText : editTexts) {

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


    }


    private void showBanksListDialog(ArrayList<Bank> bankArrayList) {


        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.country_code_dialog);

        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.my_recycler_view);

        Button close = (Button) dialog.findViewById(R.id.button);

        EditText search = (EditText) dialog.findViewById(R.id.search_button);

        setBankListData(recyclerView, bankArrayList, dialog);

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

                ((CustomRecyclerViewAdapterBankNames) recyclerView.getAdapter()).getFilter().filter(charSequence);


            }


            @Override
            public void afterTextChanged(Editable s) {

            }


        });


        dialog.show();


    }

    private void setBankListData(final RecyclerView recyclerView, ArrayList<Bank> bankArrayList, final Dialog dialog) {


        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                if (TextUtils.isEmpty(cetBankName.getText())) {

                    cetBankName.setText(" ");


                }

                cetBankName.clearFocus();


            }


        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);

        if (bankArrayList != null) {
            CustomRecyclerViewAdapterBankNames customRecyclerViewAdapterBankNames = new CustomRecyclerViewAdapterBankNames(bankArrayList);

            recyclerView.setAdapter(customRecyclerViewAdapterBankNames);

        } else {
            recyclerView.setVisibility(View.GONE);
        }


        ((CustomRecyclerViewAdapterBankNames) recyclerView.getAdapter()).setOnItemClickListener(new CustomRecyclerViewAdapterBankNames.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (dialog != null) {
                    dialog.cancel();
                }

                cetBankName.setText(((CustomRecyclerViewAdapterBankNames) recyclerView.getAdapter()).getItem(position).bankName);

                cetBankName.setTag(((CustomRecyclerViewAdapterBankNames) recyclerView.getAdapter()).getItem(position).bankId);


            }


        });


    }


    private void getBanksData() {


        Common.Log.i("cetBankCountry.getTag() : " + cetBankCountry.getTag());

        if (cetBankCountry.getTag() != null) {

            HashMap<String, String> params = new HashMap<>();

            params.put(ServiceUrls.ApiRequestParams.COUNTRY_ID, String.valueOf(cetBankCountry.getTag()));

            new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.GET_BANKS, params);

        } else {

//            Common.customToast(this,"Please Select Car Make First.");

//            cetBankCountry.clearFocus();
            btnNext.requestFocus();

        }


    }


    @OnClick(R.id.cet_car_make)
    void setCetCarMake() {
        carMake();
    }


@OnClick(R.id.cet_car_model_other)
    void stCetCarModelOther() {
    showCustomMakeOtherDialog(tnlCarModelOther,cetCarModelOther,Common.getStringFromResources(R.string.enter_model));
    }

@OnClick(R.id.cet_car_make_other)
    void setCetCarMakeOther() {
    showCustomMakeOtherDialog(tnlCarModelOther,cetCarMakeOther,Common.getStringFromResources(R.string.enter_model));
    }

    @OnClick(R.id.cet_car_model)
    void setCetCarModel() {
        carModel();
    }

    @OnClick(R.id.cet_bank_country)
    void setCetBankCountry() {


        getCountriesData(R.id.cet_bank_country);


    }

    private void getCountriesData(int requestId) {

        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.GET_COUNTRIES, null, requestId);

    }


    @OnClick(R.id.cet_bank_name)
    void setCetBankName() {
        getBanksData();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onResume() {
        super.onResume();


        checkAndShowGpsEnableDialog();


        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    private void checkAndShowGpsEnableDialog() {

        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(Common.getStringFromResources(R.string.your_gps_seems_to_be_disabled))
                    .setCancelable(false)
                    .setPositiveButton(Common.getStringResourceText(R.string.enable), new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    /*.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    })*/;
            final AlertDialog alert = builder.create();
            alert.show();
        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        Common.Log.i("? - Inside onLocationChanged().");

        Common.Log.i("? - TextUtils.isEmpty(cetCountryOfResidency.getText()) : " + TextUtils.isEmpty(cetCountryOfResidency.getText()));

        Common.Log.i("? - getCountriesVo : " + getCountriesVo);

        if (!TextUtils.isEmpty(cetCountryOfResidency.getText()) || getCountriesVo == null) {

            Common.Log.i("Inside - if(!TextUtils.isEmpty(cetCountryOfResidency.getText()) && getCountriesVo != null)");

            return;

        }

        Common.Log.i("? - Inside onLocationChanged().22");
        Geocoder geocoder = new Geocoder(context, new Locale("en"));

        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Common.Log.i("? - Inside onLocationChanged().33");
        if (addresses != null && addresses.size()>0) {
            Address address = addresses.get(0);
             countryName = address.getCountryName();
             cityName = address.getLocality();


            Common.Log.i("? - countryName : 1" + countryName+" cityName "+cityName+" getAdminArea "+address.getAdminArea()+" getLocality "+address.getLocality());

        }

        if (countryName.equalsIgnoreCase("")){
            TelephonyManager teleMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String localeCountry = teleMgr.getNetworkCountryIso();

            if (localeCountry != null) {
                Locale loc = new Locale("en",localeCountry);
                Log.d("? - countryName", "User is from " + loc.getDisplayCountry());
                countryName=loc.getDisplayCountry();
            }

        }else {

           // countryName ="SAUDI ARABIA";
        }



            Common.Log.i("? - countryName : 2" + getCountriesVo.countries);
            Common.Log.i("? - countryName : countryName" + countryName);

            for (Country country : getCountriesVo.countries) {
                Common.Log.i("? - countryName : 3" + country.countryName);
                if (country.countryName.equalsIgnoreCase(countryName)) {
                    cetCountryOfResidency.setTag(country.countryId);
                    cetCountryOfResidency.setText(country.countryName);

                    cetBankCountry.setText(country.countryName);
                    cetBankCountry.setTag(country.countryId);

                        if (cetCountryOfResidency.getTag().equals(Constants.SAUDI_COUNTRY_CODE)) {
                            llNonSaudiaNoPlate.setVisibility(View.GONE);
                            llSaudiaPlateNo.setVisibility(View.VISIBLE);
                            tilGender.setVisibility(View.GONE);
                            tilBankCountry.setVisibility(View.GONE);
                            tilCountryOfResidency.setVisibility(View.GONE);

                            Common.Log.i("? - countryName : 1" + countryName+" cityName "+cityName);

                            if (cityName.equalsIgnoreCase("Riyadh")) {

                                HashMap<String, String> params = new HashMap<>();
                                params.put(ServiceUrls.ApiRequestParams.COUNTRY, Constants.SAUDI_COUNTRY_CODE);
                                new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.GET_CITY, params);

                            }



                        } else {
                            llNonSaudiaNoPlate.setVisibility(View.VISIBLE);
                            llSaudiaPlateNo.setVisibility(View.GONE);
                            tilGender.setVisibility(View.VISIBLE);
                            tilBankCountry.setVisibility(View.VISIBLE);
                            tilCountryOfResidency.setVisibility(View.VISIBLE);
                        }



                    break;

                }

            }


            Common.dismissProgressDialog(progressDialog);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void showCustomMakeOtherDialog(final TextInputLayout textInputLayout,final EditText editText,String hint)
    {



        final Dialog dialog =Common.getAppThemeCustomDialog(this, R.layout.make_model_other_dialog, Common.getDimensionResourceValue(R.dimen._35sdp));

        dialog.setCancelable(false);
        final EditText etReasons = (EditText) dialog.findViewById(R.id.et_reasons);
        Button submit = (Button) dialog.findViewById(R.id.btn_submit);
        LinearLayout rootLayout = (LinearLayout) dialog.findViewById(R.id.root_layout);
        if (editText.getText().length()>0){
            etReasons.setText(editText.getText().toString());
        }
        etReasons.setHint(hint);
        rootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("methodcalling","methodcalling");
                Common.hideSoftKeyboardFromDialog(dialog,RegistrationActivity2.this);
                return true;
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etReasons.getText().toString().trim().length()<1){
                    Common.customToast(RegistrationActivity2.this,Common.getStringResourceText(R.string.enter_your_reason));
                    return;
                }
                textInputLayout.setVisibility(View.VISIBLE);

                editText.setText(etReasons.getText().toString().trim());
                dialog.dismiss();

            }
        });




        dialog.show();



    }
}
