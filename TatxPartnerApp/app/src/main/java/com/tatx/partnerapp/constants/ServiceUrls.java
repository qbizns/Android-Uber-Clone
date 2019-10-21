
package com.tatx.partnerapp.constants;

import com.tatx.partnerapp.enums.Environment;

/**
 * Created by user on 29-06-2016.
 */
public class ServiceUrls
{

    public static Environment CURRENT_ENVIRONMENT = Environment.PRODUCTION;

    public interface RequestNames
    {
        String DRIVER_EARNING = "DriverEarning";
        String GET_DRIVER_PROFILE = "getDriverProfile";
        String UPDATE_DRIVER_PROFILE = "updateDriverProfile";
        String PICKUP_CUSTOMER = "pickupCustomer";
        String START_TRIP = "startTrip";
        String END_TRIP = "endTrip";
        String ORDER_RECEIVED = "orderReceived";
        String ORDER_CANCELED = "orderCanceled";
        String ON_SOCKET_OPEN = "onSocketOpen";
        String GET_COUNTRIES = "getCountries";
        String DRIVER_ON_TRIP = "driverOnTrip";
        String STATUS_ORDER = "statusOrder";
        String CAB_ARRIVED = "cabArrived";
        String SET_DRIVER_LOC = "setDriverLoc";
        String USER_LOGOUT = "userLogout";
        String TRIP_RATING = "tripRating";
        String UPDATE_DEVICE_TOKEN ="updateDeviceToken";
        String SOCKET_INITIATION ="socketInitiation";
        String DRIVER_LOGIN ="driverLogin";
        String MAKE_CAB = "makeCab";
        String MODEL_CAB = "modelCab";
        String GET_BANKS = "getBanks";
        String ADD_BANK_ACCOUNT = "addBankAccount";
        String SEND_REGISTRATION_OTP = "sendRegistrationOtp";
        String CREATE_DRIVER = "createDriver";
        String USER_STATUS = "userStatus";
        String CHECK_REGISTRATION_STATUS = "checkRegistrationStatus";
        String ADD_CARS = "addCars";
        String GET_CAR = "getCar";
        String DELETE_CAR = "deleteCar";
        String CHANGE_CAR = "changeCar";
        String CHECK_USER_EXISTANCE_STATUS ="checkUserExistanceStatus";
        String CREATE_DRIVER_STEP1 = "createDriverStep1";
        String CREATE_DRIVER_STEP2 = "createDriverStep2";
        String DRIVER_FILES = "driverFiles";
        String DESTINATION_REACH_TIME_BROADCAST = "destinationReachTimeBroadcast";
        String GET_DRIVER_DOCUMENTS = "getDriverDocuments";
        String ADD_TO_USER_TATX_BALANCE = "addToUserTatxBalance";
        String GET_CITY = "getCity";
        String GET_REASONS_FOR_CANCEL = "getReasonsForCancel";
        String DRIVER_CANCEL_TRIP = "driverCancelTrip";
        String GET_BONUS_DETAILS = "getBonusDetails";
        String GET_TOP_PERFORMERS_DETAILS = "getTopPerformersDetails";
        String RESEND_OTP_CUSTOMER ="resendOtpCustomer" ;
        String GET_ONLINE_HOURS = "getOnlineHours";
        String GET_ONLINE_HOURS_BY_DATE = "getOnlineHoursByDate";
        String GET_DRIVER_RATING = "getDriverRating";
        String FORGET_PASSWORD = "forgetPassword";
        String TRIP_DRIVER = "tripDriver";
        String FEEDBACK = "feedback";
        String FAQ = "faq";
        String OFFERS = "offers";
        String PUSH_NOTIFICATION = "pushNotification";
        String CHANGE_DESTINATION = "changeDestination";
        String CONFIRM_CHANGE_DESTINATION = "confirmChangeDestination";
        String DESTINATION_CHANGED = "destinationChanged";
        String GET_SHARE_AND_EARN_DETAILS = "getShareAndEarnDetails";
        String UPDATE_READ_STATUS = "updateReadStatus";
        String LAST_TRIP_RATING = "lastTripRating";
        String LAST_TRIP_DRIVER_RATING = "lastTripDriverRating";
        String ON_CONNECT = "onConnect";
        String UPDATE_SOCKET_CONNECTIVITY = "updateSocketConnectivity";
    }


    public interface ResponseParams
    {
        String DRIVER_EARN = "driver_earn";
        String DATE = "date";
        String AMOUNT = "amount";
        String EMAIL = "email";
        String FIRST_NAME = "first_name";
        String LAST_NAME = "last_name";
        String PHONE_NUMBER = "phone_number";

        String VEHICLE_NO = "vehicle_no";
        String LAST_PAID = "last_paid";
        String DUE_AMOUNT = "due_amount";
        String PASSWORD = "password";
        String MESSAGE = "message";
        String TYPE = "type";
        String ROLE = "role";
    }



    public interface ApiRequestParams
    {
        String TRIPID = "tripid";
        String ORDERID = "orderid";
        String LATITUDE = "latitude";
        String LONGITUDE = "longitude";
        String LOCATION = "location";
        String DEVICE = "device";
        String DISTANCE = "distance";
        String DURATION = "duration";
        String TIP = "tip";
        String TYPE = "type";
        String LAST_TRIP = "last_trip";
        String STATUS = "status";
        String ROLE = "role";
        String ONLINE = "online";
        String OS = "os";
        String CITY = "city";
        String CATEGORY = "category";
        String DEVICE_ID = "device_id";
        String RATING = "rating";

        String BREAK_DOWN = "break_down";
        String TOKEN = "token";
        String EMAIL = "email";
        String PASSWORD = "password";
        String MAKE_ID = "make_id";
        String COUNTRY_ID = "country_id";
        String BANK_NAME = "bank_name";
        String BANK_ID = "bank_id";
        String ACCOUNT_NO = "account_no";
        String IBAN_NO = "iban_no";
        String BANK_UNIQUE_CODE = "bank_unique_code";
        String SWIFT_CODE = "swiftcode";
        String PHONE_NUMBER = "phone_number";
        String VEHICLE_NO = "vehicle_no";
        String MODEL_ID = "model_id";
        String REGISTRATION_EXPIRES = "registration_expires";
        String COLOR = "color";
        String REG_NO = "reg_no";
        String CAR_ID = "car_id";
        String CAB_ID = "cab_id";
        String ARRIVED = "arrived";
        String LOGOUT_DUE_TO_PUSH = "logoutDueToPush";
        String AMOUNT = "amount";
        String COLLECTED_AMOUNT = "collected_amount";
        String COUNTRY = "country";
        String EMPLOYEE_TYPE = "employee_type";
        String LANGUAGE = "language";
        String ROLE_ID = "role_id";
        String REASON_ID = "reason_id";
        String CUSTOM_REASON = "custom_reason";
        String FROM_DATE = "fromDate";
        String TO_DATE = "toDate";
        String DATE = "date";
        String MESSAGE = "message";
        String TRIP_ID = "trip_id";
        String DROP_LATITUDE = "drop_latitude";
        String DROP_LONGITUDE = "drop_longitude";
        String DROP_LOCATION = "drop_location";
        String REFERRAL_CODE = "referral_code";
        String NOTIFICATION_ID = "notification_id";
    }


    public interface MultiPartRequestParams
    {
        String PROFILE_PIC = "profilePic";
        String IMG_IQAMA = "img_iqama";
        String IMG_DRIVERLICENSE = "img_driverLicense";
        String IMG_INSURANCE = "img_insurance";
        String IMG_VEHICLEREGISTRATION = "img_vehicleRegistration";
        String IMG_VEHICLEPHOTO = "img_vehiclePhoto";
        String IMG_IBANBANKCARD = "img_ibanBankCard";
        String IMG_AUTHORITYTODRIVECAR = "img_authorityToDriveCar";
        String IMG_EMPLOYEE_ID_OR_PC = "img_employeeIDorPC";
        String IMAGE = "image";

    }

    public interface PushResponseCodes {
        int _10001 = 10001;
        int FROM_NOTIFICATION_MANAGEMENT_20008 = 20008;
    }
}
