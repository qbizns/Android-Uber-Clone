package com.tatx.partnerapp.commonutills;


import com.tatx.partnerapp.R;
import com.tatx.partnerapp.application.TATX;

public class Constants
{

    public static final String PDUS = "pdus";
    public static final String createDriver = "createDriver";
    public static final String driverLogin = "driverLogin";
    public static final String userStatus = "userStatus";
    public static final String resendOtpCustomer = "resendOtpCustomer";
    public static final String forgetPassword = "forgetPassword";
    public static final String driverFiles = "driverFiles";
    public static final String tripDriver = "tripDriver";
    public static final String getDocuments = "getDocuments";

    public static final String offers = "offers";
    public static final String feedback = "feedback";
    public static final String faq = "faq";

    public static final String getWayBill = "getWayBill";
    public static final String tripRating = "tripRating";
    public static final String getDriverRating = "getDriverRating";

    public static final int CALL_API_FROM_REGISTRATION = 1;
    public static final int CALL_API_FROM_LOGIN = 2;
    public static final int CALL_VERIFICATION_ACTIVITY = 3;
    public static final int CALL_API_FROM_FORGOT_PASS = 4;
    public static final int CALL_API_FROM_CHANGE_PASSWORD = 5;

    public static final int CALL_API_FROM_TRIP_HIST_ACTIVITY = 10;
    public static final int CALL_API_FROM_REGISTRATION_SEC = 11;
    public static final int CALL_API_FROM_HELP = 12;
    public static final int CALL_API_FROM_NOTIFICATIONS = 13;
    public static final int CALL_API_FROM_OFFERS = 14;
    public static final int CALL_API_FROM_FEEDBACK = 15;
    public static final int CALL_API_FROM_GET_DRIVER_WAYBILL = 19;
    public static final int CALL_API_FROM_INVOICE = 20;
    public static final int CALL_API_FROM_RATING = 22;
    public static final int CALL_API_FROM_DOCUMENTS = 23;
    public static final int CALL_API_FROM_INVITEFRIEND = 24;
    public static final int CALL_API_FROM_UPDATE_PROFILE = 25;
    public static final int CALL_API_FROM_ADD_CAR_ACT =26 ;

    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";


    public static final String MESSAGE = "message";
    public static final String DATA = "data";
    public static final String CODE = "code";
    public static final String DESCRIPTION = "description";
    public static final int SUCCESS = 200;
    public static final long CONNECTION_TIME_OUT = 60;
    public static final long READ_TIME_OUT = 60;
    public static final long WRITE_TIME_OUT = 60;
    public static final String NO_INTERNET_CONNECTION_MESSAGE = "Please Connect to Internet.\nAnd Try Again.";
    public static final String TIP_AMOUNT = "0";
    public static final String DRIVER = "driver";
    public static final String DRIVER_ROLE_ID = "3";
    public static final String CUSTOMER_ROLE_ID = "4";
    public static final String LAST_TRIP_VALUE = "LAST_TRIP_VALUE";
    public static final String ORDER_ACCEPTED = "1";
    public static final String ORDER_DECLINED = "0";
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final String STATIC_PROFILE_PIC_URL = "http://jeevanayoga.in/wp-content/uploads/2014/09/vedi-profile.jpg";
    public static final int ZOOM_LEVEL = 17;
    public static final String INTERNET_UNABLEABLE="Not connected to the internet. Please check your connection and try again.";
    public static final String UNABLE_TO_CONNECT_OUR_SERVER = "Unable to Connect Our Server.\nPlease Try Again Later.";
    public static final int PRIMARY = 1;
    public static final String CUSTOM_FONT_PATH = "fonts/Roboto-Regular.ttf";
    public static final String DATE_FORMAT_STRING = "yyyy-MM-dd";
    public static final String SAUDI_COUNTRY_CODE = "187";
    public static final String CUSTOMER = "customer";
    public static final String PUSH = "push";
    public static final String PLAY_STORE_URL = "https://play.google.com/store/apps/details?id="+ TATX.getInstance().getPackageName() +"&hl=en";
    public static final int INTENT_INTEGER_DEFAULT_VALUE = -1;
    public static final String SUGGESTION = "suggestion";
    public static final String CONTACT_US = "contact_us";
    public static final String COMPLAINTS = "complaints";
    public static final String FROM_INVOICE = "from_invoice";
    public static final String START_TRIP_TIME = "start_trip_time";
    public static final String PICKUP_TRIP_TIME = "pickup_trip_time";
    public static final String FROM_DISPLAY_ACT = "from_display_act";
    public static final String ONLINE_STATUS = "online_status";
    public static int RIYADH_CITY_ID=3;


    public interface SharedPreferencesKeys
    {
        String USERID = "userid";
        String LOGIN_STATUS = "loginStatus";
        String REG_ID = "regId";
        String DRIVER_LOGIN_HASH_MAP = "driverLoginHashMap";
        String LAST_TRIP_VALUE = "last_trip_value";
        String LANGUAGE = "language";
    }


    public interface IntentKeys
    {

        String SOURCE_ADDRESS = "sourceAddress";
        String DESTINATION_ADDRESS = "destinationAddress";
        String OTP = "otp";
        String VALUE = "value";
        String LAST_TRIP    =     "last_trip";

        String EMAIL = "email";
        String PASSWORD = "password";

        String CITY = "city";
        String DOB = "dob";
        String GENDER = "gender";

        String REGISTRATION_VO_KEY = "registrationVo";
        String ADDRESS = "address";
        String PROFILE_PIC_FILE = "profilePicFile";
        String STATUS="status";
        String TOKEN="token";
        String DEVICE="device";
        String ROLE="role";
        String F_NAME = "fname";
        String L_NAME = "lname";
        String MOBILE_NUMBER = "mobile_number";
        String NATIONALITY = "nationality";
        String COUNTRY_OF_RESIDENCY = "country_of_residency";
        String IQAMA_NUMBER = "iqama_number";
        String IQAMA_NUMBER_EXP_DATE = "iqama_number_exp_date";
        String LOCAL_IQAMA_NUMBER_EXP_DATE = "local_iqama_number_exp_date";
        String VEHICLE_REG_SERIAL_NUMBER = "vehicle_reg_serial_number";
        String VEHICLE_REG_EXP_DATE = "vehicle_reg_exp_date";
        String LOCAL_VEHICLE_REG_EXP_DATE = "local_vehicle_reg_exp_date";
        String INSURANCE_NUMBER = "insurance_number";
        String INSURANCE_EXP_DATE = "insurance_exp_date";
        String VEHICLE_ENG_NUMBER = "vehicle_eng_number";
        String CAR_MAKER = "car_maker";
        String CAR_MODEL = "car_model";
        String CAR_YEAR = "car_year";
        String OWNER = "owner";
        String IBAN_NUMBER = "iban_number";
        String BANK_NAME = "bank_name";
        String BANK_COUNTRY = "bank_country";
        String OWNER_AUTHORITY = "owner_authority";
        String COUNTRY_CODE = "country_code";
        String EMPLOYEE_TYPE = "employee_type";
        String CAB_ID = "cab_id";
        String PROFILE_PIC = "profile_pic";
        String DEVICE_ID = "device_id";
        String SHOW_CONTENT_VIEW = "showContentView";
        String POLICE_CLEARANCE = "police_clearance";
        String DRIVER_REFER_TYPE = "driver_refer_type";
        String REFER_TEXT = "refer_text";
        String TRIP_ID = "trip_id";
        String ON_LINE_HOURS_VO = "onlineHoursVo";
        String DATE = "date";
        String TODAY_VO = "today_vo";
        String TOTAL_HOURS = "total_hours";
        String SHARE_AND_EARN_VO = "shareAndEarnVo";
        String REFERRAL_CODE = "referral_code";
        String CAR_MODEL_NAME = "car_model_name";
        String CAR_MAKE_NAME = "car_make_name";

        String FEEDBACK_TYPE = "feedback_type";

        String FROM_SPLASH_SCREEN = "from_splash_screen";

        String IS_FROM_SERVICE = "isFromService";






        String LANGUAGE = "language";
    }


    public interface DriverStatuses
    {
        int OFFLINE = 0;
        int ONLINE = 1;
        int RIDE_ACCEPTED = 2;
        int PICKED_UP_CUSTOMER = 3;
        int TRIP_STARTED = 4;
        int TRIP_COMPLETED = 5;
        int SOCKET_DISCONNECTED = 8;



    }

    public interface DriverStatusTexts
    {
        String GO_OFFLINE       = Common.getStringFromResources(R.string.go_offline);
        String GO_ONLINE        = Common.getStringFromResources(R.string.go_online);
        String PICKUP_CUSTOMER  = TATX.getInstance().getApplicationContext().getResources().getString(R.string.pickup_customer);
        String START_TRIP       = Common.getStringFromResources(R.string.start_trip);
        String COMPLETE_TRIP    = Common.getStringFromResources(R.string.complete_trip);
    }



    public interface RequestCodes
    {
        int ONCREATE_REQUEST_CODE = 5000;
        int RIDE_ACCEPTED_REQUEST_CODE = 2000;
        int RIDE_IGNORED_REQUEST_CODE  = 50001;
        int ONE  = 1;
    }


    public interface ResponseCodes
    {
        int RIDE_ACCEPTED_RESPONSE_CODE = 786;
    }


    public interface ResultCodes
    {
        int SUCCESS = 1;

        int FAILURE = 0;
    }



    public static final String ANDROID = "Android";

    public static final String KEY_1 = "key1";




}
