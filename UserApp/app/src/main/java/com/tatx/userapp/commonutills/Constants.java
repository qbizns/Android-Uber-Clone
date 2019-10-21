package com.tatx.userapp.commonutills;

import android.content.IntentFilter;
import android.content.pm.PackageManager;

import com.tatx.userapp.application.TATX;

/**
 * Created by Home on 27-04-2016.
 */
public class Constants
{




    public static final String PDUS = "pdus";
    public static final String ANDROID = "Android";
    public static final int SUCCESS = 200;
    public static final String KEY_1 = "key1";
    public static final long CONNECTION_TIME_OUT = 60;
    public static final long READ_TIME_OUT = 60;
    public static final long WRITE_TIME_OUT = 60;
    public static final int ZOOM_LEVEL = 17;

    public static final String BROADCAST_LOCATION ="broadcastLocation";
    public static final String ORDER_CANCELED_BY_DRIVER ="orderCanceledByDriver";


    public static final int CALL_API_FROM_REGISTRATION = 1;

    public static final int CALL_API_FROM_FORGOT_PASS = 4;

    public static final int GOOGLE_SUGGETIONS_SOURCE = 101;
    public static final String MESSAGE = "message";
    public static final String CODE = "code";
    public static final String DESCRIPTION = "description";



    public static final String INTERNET_UNABLEABLE="Not connected to the internet. Please check your connection and try again.";
    public static final String CUSTOMER = "customer";
    public static final String UPDATE = "UPDATE";
    public static final String DELETE = "DELETE";
    public static final String ADD = "ADD";
    public static final String NO_INTERNET_CONNECTION_MESSAGE = "Please Connect to Internet.\nAnd Try Again.";
    public static final String UNABLE_TO_CONNECT_OUR_SERVER = "Unable to Connect Our Server.\nPlease Try Again Later.";
    public static final String ENCRYPTION_KEY = "TATX.KSA";
    public static final String MOBILE = "mobile";
    public static final String CUSTOM_FONT_PATH = "fonts/RobotoRegular.ttf";
    public static final String PLAY_STORE_URL = "https://play.google.com/store/apps/details?id="+ TATX.getInstance().getPackageName() +"&hl=en";
    public static final int _201 = 201;
    public static final int _202 = 202;
    public static final String CUSTOMER_ROLE_ID = "4";
    public static final int RESULT_FROM_LOCATIONS = 1111;
    public static final int INTENT_INTEGER_DEFAULT_VALUE = -1;
    public static final String FEEDBACK = "feedback";
    public static final String CUSROMER = "cusromer";
    public static final String ORDER_CANCEL_TIME = "order_cancel_time";
    public static String FAVOURITE_LOCATION="favourite_location";
    public static String ACTION_MyUpdate="com.tatx.userapp.UPDATE_FAVORUTIES_LOC";
    public static final String API_NOT_CONNECTED = "Google API not connected";
    public static final String SOMETHING_WENT_WRONG = "OOPs!!! Something went wrong...";
    public static String PlacesTag = "Google Places Auto Complete";
    public static final String SUGGESTION = "suggestion";
    public static final String CONTACT_US = "contact_us";
    public static final String COMPLAINTS = "complaints";


    public interface LoyalityTypes
    {


        String BRONZE = "Bronze";
        String SILVER = "Silver";
        String GOLD = "Gold";
        String DIAMOND = "Diamond";
        String LOYALTY_ZERO = "loyaltyZero";
    }



    public interface TripStatuses
    {


        int NOT_ON_TRIP = 0;
        int ON_TRIP_BUT_NOT_STARTED = 1;
        int ON_TRIP_AND_STARTED = 2;



    }






    public interface PaymentTypes
    {
        String ONLINE = "CREDIT_CARD";
        String CASH = "CASH";
        String CREDITS = "CREDITS";
    }




    public interface SharedPreferencesKeys
    {
        String USERID = "userid";
        String PRIMARY_CARD_ID = "primaryCardId";
        String LOGIN_STATUS = "loginStatus";
        String REG_ID = "regId";
        String EMAIL_ID = "emailId";
        String PHONE_NUMBER = "phoneNumber";
        String FIRST_NAME = "firstName";
        String LAST_NAME = "lastName";
        String PASSWORD = "password";
        String CUSTOMER_LOGIN_HASH_MAP = "customerLoginHashMap";
        String LOGIN_TYPE = "loginType";
        String APP_LOCALE = "appLocale";
        String ONTRIP_STATUS = "ontrip_status";
        String SHOW_OFFER_SCREEN = "show_offer_screen";
        String LANGUAGE = "language";
    }


    public interface SocialMediaTypes
    {

        String FACEBOOK = "fb";
        String GOOGLE_PLUS = "gp";
        String TWITTER = "tw";
    }




    public interface RequestCodes
    {
        int ONCREATE_REQUEST_CODE = 5000;
    }


    public interface IntentKeys
    {

        String SOURCE_ADDRESS = "sourceAddress";
        String DESTINATION_ADDRESS = "destinationAddress";
        String FIRST_NAME = "first_name";
        String LAST_NAME = "last_name";
        String EMAIL = "email";
        String PASSWORD = "password";
        String PHONE_NUMBER = "phone_number";
        String OTP = "otp";
        String VALUE = "value";
        String REFERRAL_CODE = "referral_code";
        String COUNTRY_CODE = "country_code";
        String FROM_SPLASH_SCREEN = "fromSplashScreen";
        String SHOW_CONTENT_VIEW = "showContentView";

        String LOYALITY_POINT = "loyality_point";
        String LOYALITY_TYPE = "loyality_type";
        String PROMO_IMG = "promo_img";
        String LOYALITY_TEXT = "loyality_text";
        String UN_READ_NOTIFICATION_COUNT = "unReadNotificationCount";
        String PROFILE_PIC_FILE = "profilePicFile";

        String REQUEST_ACTION = "request_action";
        String UPDATAE_LOCATION_KEY = "updatae_location_key";
        String GET_SAVED_LOCATION_VO = "getSavedLocationVo";
        String TRIP_ID = "trip_id";
        String LANGUAGE = "language";
    }


    public class PanelState {
        public static final String COLLAPSED = "COLLAPSED";
        public static final String EXPANDED = "EXPANDED";
    }
}

