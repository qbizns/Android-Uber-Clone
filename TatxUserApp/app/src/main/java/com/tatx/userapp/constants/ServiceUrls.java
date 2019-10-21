package com.tatx.userapp.constants;

import com.tatx.userapp.enums.Environment;

/**
 * Created by Venkateswarlu SKP on 29-06-2016.
 */
public class ServiceUrls

{

    public static Environment CURRENT_ENVIRONMENT = Environment.PRODUCTION;

    public interface RequestNames
    {
        String ACCOUNTS_CUSTOMER = "accountsCustomer";
        String UPDATE_PAYMENT_TYPE = "updatePaymentType";
        String ADD_CREDIT = "addCredit";
        String TRANSFER_CREDITS = "TransferCredits";
        String REDEEM_LOYALTY = "redeemLoyalty";
        String CUSTOMER_ON_TRIP = "customerOnTrip";
        String DELETE_CARD = "DeleteCard";
        String ON_SOCKET_OPEN = "onSocketOpen";
        String SET_CUSTOMER_LOC = "setCustomerLoc";
        String GET_FARE = "getFare";
        String PLACE_ORDER = "placeOrder";
        String GET_LOYALITY_POINT = "getLoyalityPoint";
        String ORDER_INITIATED = "orderInitiated";
        String SPECIFIC_DRIVER="specificDriver";
        String SPECIFIC_BROADCAST="specificBroadcast";
        String CHANGE_CARD ="changeCard";
        String UPDATE_DEVICE_TOKEN ="updateDeviceToken";
        String TRIP_RATING = "tripRating";
        String CANCEL_TRIP = "cancelTrip";
        String USER_LOGOUT = "userLogout";
        String CUSTOMER_LOGIN = "customerLogin";
        String CREATE_CUSTOMER = "createCustomer";
        String SEND_REGISTRATION_OTP = "sendRegistrationOtp";
        String USER_STATUS = "userStatus";
        String GET_SAVED_LOCATIONS ="getSavedLocations";
        String DELETE_FAV_LOCATIONS="deleteFavLocations";
        String SHARE="share";
        String CHECK_USER_EXISTANCE_STATUS ="checkUserExistanceStatus";
        String PUSH_NOTIFICATION = "pushNotification";
        String PROMO_LIST = "promoList";
        String CHECK_PROMO_VALIDITY = "checkPromoValidity";
        String GET_CUSTOMER_PROFILE = "getCustomerProfile";
        String LAST_TRIP_RATING = "lastTripRating";
        String CHANGE_DEFAULT_LANGUAGE = "changeDefaultLanguage";
        String RESEND_OTP_CUSTOMER = "resendOtpCustomer";
        String GET_REASONS_FOR_CANCEL = "getReasonsForCancel";
        String UPDATE_READ_STATUS = "updateReadStatus";
        String UPDATE_CUSTOMER_PROFILE = "updateCustomerProfile";
        String UPDATE_FAV_LOCATIONS = "updateFavLocations";
        String SAVE_FAV_LOCATIONS = "saveFavLocations";
        String GET_COUNTRY_WISE_AIRPORT_DETAILS = "getCountrywiseAirportDetails";
        String GET_ONLINE_HOURS = "getOnlineHours";
        String FORGET_PASSWORD = "forgetPassword";
        String TRANS_CUSTOMER = "transCustomer";
        String FEEDBACK = "feedback";
        String FAQ = "faq";
        String ADD_CARD = "addCard";
        String TRIP_CUSTOMER = "tripCustomer";
        String TRIP_DETAIL = "tripDetail";
        String CHANGE_DESTINATION = "changeDestination";
    }

    public interface RequestParams
    {
        String PAYMENT_TYPE = "payment_type";
        String AMOUNT = "amount";
        String CARD_ID = "card_id";
        String EMAIL = "email";
        String POINTS = "points";
        String SOCIAL = "social";
        String TOKEN = "token";
        String GCM_TOKEN = "gcm_token";
        String SOCIAL_TOKEN = "social_token";
        String PROFILE_PIC = "profile_pic";
        String STOKEN = "stoken";
        String ROLE = "role";
        String ONLINE = "online";
        String OS = "os";
        String LATITUDE = "latitude";
        String LONGITUDE = "longitude";
        String CITY = "city";
        String PICK_LATITUDE = "pick_latitude";
        String PICK_LONGITUDE = "pick_longitude";
        String DROP_LATITUDE = "drop_latitude";
        String DROP_LONGITUDE = "drop_longitude";
        String TYPE = "type";
        String VC_ID = "vc_id";
        String PICK_LOCATION = "pick_location";
        String DROP_LOCATION = "drop_location";
        String DEVICE_ID = "device_id";
        String DEVICE = "device";
        String TRIPID = "tripid";
        String RATING = "rating";
        String ORDERID = "orderid";
        String REQUESTING = "requesting";
        String TIME_EXCEEDED = "timeExceeded";
        String PASSWORD = "password";
        String PHONE_NUMBER = "phone_number";
        String FIRST_NAME = "first_name";
        String LAST_NAME = "last_name";
        String STATUS = "status";
        String FAV_ID="fav_id";
        String REFERRAL_CODE = "referral_code";
        String COUNTRY_CODE = "country_code";
        String PCODE = "pcode";
        String LANGUAGE = "language";
        String REFFERAL_CODE = "refferal_code";

        String LOGOUT_DUE_TO_PUSH = "logoutDueToPush";
        String REASON_ID = "reason_id";
        String ROLE_ID = "role_id";
        String NOTIFICATION_ID = "notification_id";
        String DOB = "dob";
        String AGE = "age";
        String GENDER = "gender";
        String EMERGENCY_NAME = "emergency_name";
        String EMERGENCY_COUNTRY_CODE = "emergency_country_code";
        String EMERGENCY_MOBILE = "emergency_mobile";
        String CUSTOM_REASON = "custom_reason";
        String ID = "id";
        String NAME = "name";
        String ADDRESS = "address";
        String COUNTRY_NAME = "country_name";
        String isAirport = "isAirport";
        String FROM_DATE = "fromDate";
        String TO_DATE = "toDate";
        String MESSAGE = "message";
        String TRIP_ID = "trip_id";
        String EXPIRY_DATE = "expiry_date";
        String NUMBER = "number";
        String BRAND = "brand";
        String CURRENCY = "currency";
        String PRIMARY = "primary";
        String DISABLE = "disable";
    }

    public interface ResponseParams
    {
        String PAYMENT = "payment";
        String PAYMENT_TYPE_ID = "payment_type_id";
        String BRAND = "brand";
        String ID = "id";
        String PRIMARY = "primary";



    }

    public interface MultiPartRequestParams {
        String IMAGE = "image";
    }


    public class PushResponseCodes
    {
        public static final int _10001 = 10001;
        public static final int _20007 = 20007;
        public static final int FROM_NOTIFICATION_MANAGEMENT_20008 = 20008;


    }
}
