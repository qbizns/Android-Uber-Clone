package com.tatx.userapp.commonutills;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.Bounds;
import com.google.maps.model.GeocodingResult;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tatx.userapp.R;
import com.tatx.userapp.abstractclasses.PushNotificationListenerActivity;
import com.tatx.userapp.activities.AddCreditBalanceActivity;
import com.tatx.userapp.activities.GoogleMapDrawerActivity;
import com.tatx.userapp.activities.OnTripMapActivity;
import com.tatx.userapp.activities.SplashScreen;
import com.tatx.userapp.adapter.RecyclerItemClickListener;
import com.tatx.userapp.adapter.SavedLocationListAdapterFilterable;
import com.tatx.userapp.application.TATX;
import com.tatx.userapp.customviews.CustomButton;
import com.tatx.userapp.customviews.CustomTextView;
import com.tatx.userapp.customviews.NpaGridLayoutManager;
import com.tatx.userapp.enums.CreditCardType;
import com.tatx.userapp.enums.PaymentType;
import com.tatx.userapp.googlemapadapters.PlacesAutoCompleteAdapter;
import com.tatx.userapp.helpers.GeoDetails;
import com.tatx.userapp.menuactivity.UpdateProfileActivity;
import com.tatx.userapp.menuactivity.cropimages.InternalStorageContentProvider;
import com.tatx.userapp.pojos.ApiResponseVo;
import com.tatx.userapp.pojos.FavLocation;

import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import butterknife.BindView;
import retrofit.mime.TypedFile;


public class Common {
    static final String LOG = Common.class.getSimpleName();
    public static final String INTERNET_UNABLEABLE = "Not connected to the internet. Please check your connection and try again.";
    public static final int TOAST_TIME = 2000;
    private static Activity previousActivity;
    private static Dialog internetErrorDialog;


    public static void customToast(final Context context, final String msg, final int millisec)
    {

        if (context == null)
        {
            return;
        }


        /*
        Activity activity = (Activity) context;

        if (activity == null) {
            return;
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(context, msg, millisec).show();
            }
        });

        */


        new Handler(Looper.getMainLooper())
        {
            @Override
            public void handleMessage(Message message)
            {

                Toast.makeText(context, msg, millisec).show();


            }
        }.obtainMessage().sendToTarget();








    }



    public static void showConfirmationDialog(Activity activity,String message)
    {

        final Dialog confirmationDialog = new Dialog(activity);

        confirmationDialog.setContentView(R.layout.confirmation_dialog);

        confirmationDialog.setCancelable(false);

        CustomTextView ctvConfirmationDialogMessage = (CustomTextView) confirmationDialog.findViewById(R.id.title);

        ctvConfirmationDialogMessage.setText(message);

        CustomButton customButton = (CustomButton) confirmationDialog.findViewById(R.id.ok);

        customButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationDialog.dismiss();
            }
        });

        confirmationDialog.show();
    }



    public static boolean haveInternet(Context ctx) {
        try {
            NetworkInfo info = ((ConnectivityManager) ctx
                    .getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();

            if (info == null || !info.isConnected()) {
                return false;
            }
        } catch (Exception e) {
            android.util.Log.d("err", e.toString());
        }
        return true;
    }

    public static void getAlertDialog(final Activity context, String title,
                                      String Message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(Message)
                .setCancelable(false)
                .setPositiveButton("Invite",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                // current activity
                                context.finish();
                            }
                        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public static void showAlertDialog(final Activity context, String title,
                                       String Message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(Message)
                .setCancelable(false)
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        intent.putExtra("EXIT", true);
                        if (context.getIntent().getBooleanExtra("EXIT", false)) {
                            context.finish();
                        }
                        android.os.Process.killProcess(android.os.Process.myPid());

                    }
                })
                .setPositiveButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public static void getAlertDialogWhyChooseUs(final Activity context, String title,
                                                 String Message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(Message)
                .setCancelable(false)

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
//            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static boolean checkSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
//            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
          return true;
        }
        return false;
    }


    public static void hideSoftKeyboardFromDialog(Dialog dialog,Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (dialog.getCurrentFocus() != null) {
//            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            inputMethodManager.hideSoftInputFromWindow(dialog.getCurrentFocus().getWindowToken(), 0);
        }
    }
    public static String retrieveContactRecord(Context activity, String phoneNo) {
        String contactId;
        String contactName = "";
        try {
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNo));
            String[] projection = new String[]{ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup.PHOTO_URI};
            String selection = null;
            String[] selectionArgs = null;
            String sortOrder = ContactsContract.PhoneLookup.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
            ContentResolver cr = activity.getContentResolver();
            if (cr != null) {
                Cursor resultCur = cr.query(uri, projection, selection, selectionArgs, sortOrder);
                if (resultCur != null) {
                    while (resultCur.moveToNext()) {
                        contactId = resultCur.getString(resultCur.getColumnIndex(ContactsContract.PhoneLookup._ID));
                        contactName = resultCur.getString(resultCur.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME));
                        String photoUri = resultCur.getString(resultCur.getColumnIndexOrThrow(ContactsContract.PhoneLookup.PHOTO_URI));
//                        Log.e("Info","Contact Id : "+contactId);
//                        Log.e("Info","Contact Display Name : "+contactName);
//                        Log.d("SUBBU", photoUri);
                        byte[] data = resultCur.getBlob(resultCur.getColumnIndex(ContactsContract.PhoneLookup.PHOTO_URI));
                        if (data != null) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//                            Log.d("SUBBU", bitmap + "");
                        }
                        return contactName;

                    }
                    //resultCur.close();
                }
            }
        } catch (Exception sfg) {
            // Log.e("Error", "Error in loadContactRecord : "+sfg.toString());
        }
        return contactName;
    }//fn retrieveContactRecord

    public static long getContactIDFromNumber(String contactNumber, Context context) {
        String UriContactNumber = Uri.encode(contactNumber);
        long phoneContactID = new Random().nextInt();
        Cursor contactLookupCursor = context.getContentResolver().query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, UriContactNumber),
                new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID}, null, null, null);
        while (contactLookupCursor.moveToNext()) {
            phoneContactID = contactLookupCursor.getLong(contactLookupCursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
        }
        contactLookupCursor.close();

        return phoneContactID;
    }

    /*date format YYYY-MM-DD to dd-mm-yyyy*/
    public static String dateformate(String dt) {
        String d = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = formatter.parse(dt);
            d = formatter1.format(date);


        } catch (Exception e) {
            // TODO: handle exception
        }
        return d;
    }

    /*date format  dd-mm-yyyy to YYYY-MM-DD */
    public static String dateformateDDMMYYYY(String dt) {
        String d = "";
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(dt);
            d = formatter1.format(date);


        } catch (Exception e) {
            // TODO: handle exception
        }
        return d;
    }

    /*date format  dd-mm-yyyy to YYYY-MM-DD */
    public static String dateformateByTmeZone(String dt) {
        String d = "";
        TimeZone TZ1 = TimeZone.getDefault();
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss a");
        try {
            Date date = formatter1.parse(dt);
            formatter2.setTimeZone(TimeZone.getTimeZone(TZ1.getDisplayName()));
            d = formatter2.format(date);

//            d=formatter1.format(date);


        } catch (Exception e) {
            // TODO: handle exception
        }
        return d;
    }

    public static void showGPSDisabledAlertToUser(final Activity context, String title) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                context.startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public static String toTitleCase(String input) {
        input = input.toLowerCase();
        char c = input.charAt(0);
        String s = new String("" + c);
        String f = s.toUpperCase();
        return f + input.substring(1);
    }

    public static String getAddressString(Context context, double LATITUDE, double LONGITUDE) {

        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);


            if (addresses != null) {
                Address address = addresses.get(0);

                Common.Log.i("address.toString() : " + address.toString());

                StringBuilder sb = new StringBuilder("");
                String Feature = address.getFeatureName();
                String ThroughFrare = address.getSubThoroughfare();
                String Sub_Admin = address.getSubAdminArea();
                String Locality = address.getLocality();
                String Admin = address.getAdminArea();
                String Country = address.getCountryName();

                /*if (Feature != null) {
                    sb.append(Feature);
                }
                if (ThroughFrare != null) {
                    sb.append(","+ThroughFrare);
                }
                if (Sub_Admin != null) {
                    sb.append(","+Sub_Admin);
                }*/
                if (Locality != null) {
                    Common.Log.i("Locality : " + Locality);
                    sb.append(Locality);
                }
                if (Admin != null) {
                    Common.Log.i("Admin : " + Admin);

                    sb.append(", " + Admin);
                }
                if (Country != null) {

                    Common.Log.i("Country : " + Country);

                    sb.append(", " + Country);
                }
                strAdd = sb.toString();


                android.util.Log.w("My Current", "" + sb.toString());
            } else {
                android.util.Log.w("My Current", "No Address returned!");
            }


        } catch (Exception e) {
            e.printStackTrace();
            android.util.Log.w("My Current", "Canont get Address!");
        }
        return strAdd;
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static String dateformater(String dt) {
        String d = "";
        SimpleDateFormat formatter = new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss");
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd:MMMM:yyyy");
        try {
            Date date = formatter.parse(dt);
            d = formatter1.format(date);


        } catch (Exception e) {
            // TODO: handle exception
        }
        return d;
    }


    public static Object stringToObject(String str) {
        try {
            return new ObjectInputStream(new Base64InputStream(
                    new ByteArrayInputStream(str.getBytes()), Base64.NO_PADDING
                    | Base64.NO_WRAP)).readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //eg String=17.12354,86.35466;  to double[] a={17.12354,86.35466};

    public static List<Double> stringToDubleArry(String latlngString) {
        List<Double> latlng = new ArrayList<>();

        String[] sourceArray = latlngString.split(",");

        for (int i = 0; i < sourceArray.length; i++) {
            String lat = sourceArray[i];
            latlng.add(Double.parseDouble(lat));
        }

        return latlng;
    }

    public static String[] spliteString(String stringForSplit) {
        String[] array = stringForSplit.split(",");

        return array;
    }

    public static String constructLatLng(Location latlng) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.valueOf(latlng.getLatitude()));
        stringBuilder.append(',');
        stringBuilder.append(String.valueOf(latlng.getLongitude()));
        String latlngString = stringBuilder.toString();

        return latlngString;
    }

    /**
     * Encodes a sequence of LatLngs into an encoded path string.
     */
    public static String encode(final List<LatLng> path) {
        long lastLat = 0;
        long lastLng = 0;

        final StringBuffer result = new StringBuffer();

        for (final LatLng point : path) {
            long lat = Math.round(point.latitude * 1e5);
            long lng = Math.round(point.longitude * 1e5);

            long dLat = lat - lastLat;
            long dLng = lng - lastLng;

            encode(dLat, result);
            encode(dLng, result);

            lastLat = lat;
            lastLng = lng;
        }
        return result.toString();
    }

    private static void encode(long v, StringBuffer result) {
        v = v < 0 ? ~(v << 1) : v << 1;
        while (v >= 0x20) {
            result.append(Character.toChars((int) ((0x20 | (v & 0x1f)) + 63)));
            v >>= 5;
        }
        result.append(Character.toChars((int) (v + 63)));
    }

    public static List<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }

        return decoded;
    }

    /* public static String encodePolylinrByLntLatList( List<WayLocationsLog> list){
         List<LatLng> logs=new ArrayList<>();
         for (int i=0;i<list.size();i++){
             LatLng point=new LatLng(Double.parseDouble(list.get(i).getLatitude()),Double.parseDouble(list.get(i).getLongitude()));
             logs.add(point);
         }
         String encodedPolyline=Common.encode(logs);
         return encodedPolyline;
     }*/
    public static boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }


    public static Bitmap getRoundedCroppedBitmap(Bitmap bitmap, int radius) {
        Bitmap finalBitmap;
        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius)
            finalBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius,
                    false);
        else
            finalBitmap = bitmap;
        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(),
                finalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, finalBitmap.getWidth(),
                finalBitmap.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);

        paint.setColor(Color.parseColor("#ff6c2c"));
        canvas.drawCircle(finalBitmap.getWidth() / 2 + 0.7f,
                finalBitmap.getHeight() / 2 + 0.7f,
                finalBitmap.getWidth() / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(finalBitmap, rect, rect, paint);

        return output;
    }

    public static boolean isGooglePlayServicesAvailable(Activity context) {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, context, 0).show();
            return false;
        }
    }

    public static final GeoDetails getCompleteAddressString(Context context, double LATITUDE, double LONGITUDE) {
        String completeAddress = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        GeoDetails geoDetails=null;
        StringBuilder sb = new StringBuilder("");
        String city = "";
        Address returnedAddress = null;
        String Country="";
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null&&addresses.size()>0) {
                 returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(",");
                }
                completeAddress = strReturnedAddress.toString();


                String Locality = returnedAddress.getLocality();
                String Admin = returnedAddress.getAdminArea();
                Country = returnedAddress.getCountryName();

                String getSubAdminArea = returnedAddress.getSubAdminArea();
                String getSubLocality = returnedAddress.getSubLocality();
                String getFeatureName = returnedAddress.getFeatureName();
                Common.Log.i("Locality : " + Locality+"getSubLocality : " + getSubLocality+"Admin : " + Admin+"getSubAdminArea : " + getSubAdminArea+"getFeatureName : " + getFeatureName);


                if (Locality != null) {
                    Common.Log.i("Locality : " + Locality);
                    sb.append(Locality);
                }
                if (Admin != null) {
                    Common.Log.i("Admin : " + Admin);
                    if (Locality != null) {
                        sb.append(", " );
                    }
                    sb.append(Admin);
                }
                if (Country != null) {

                    Common.Log.i("Country : " + Country);

                    sb.append(", " + Country);
                }


                if (Locality == null) {

                    city = returnedAddress.getSubAdminArea();
                    if (city==null){
                        city= returnedAddress.getSubLocality();
                    }

                } else {
                    city = Locality;
                }
               geoDetails = new GeoDetails(sb.toString(), completeAddress, returnedAddress.getCountryName(),city);

                android.util.Log.w("My Current", "" + strReturnedAddress.toString());
            } else {
                android.util.Log.w("My Current", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            android.util.Log.w("My Current", "Canont get Address!");
        }
        return new GeoDetails(sb.toString(), completeAddress, Country,city);
    }

    public static void customToast(Context context, String message) {
        customToast(context, message, Toast.LENGTH_LONG);
    }


    public static JSONObject getDataJsonObject(Object o) {
//        Object data = ((ApiResponseVo) o).getData();

        ApiResponseVo apiResponseVo = getSpecificDataObject(o, ApiResponseVo.class);

        /*
        Object data = ((ApiResponseVo) o).data;
        String jsonString = new Gson().toJson(data);
        */

        String jsonString = new Gson().toJson(apiResponseVo.data);

        try {
            return new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static PaymentType getPaymentTypeById(int id) {

        for (PaymentType paymentType : EnumSet.allOf(PaymentType.class)) {

            if (paymentType.getId() == id) {
                return paymentType;
            }

        }


        throw new IllegalArgumentException("Can't find " + id);

    }


    public static CreditCardType getCardBackgroundById(int id) {

        for (CreditCardType creditCardType : EnumSet.allOf(CreditCardType.class)) {

            if (creditCardType.getId() == id) {
                return creditCardType;
            }

        }


        throw new IllegalArgumentException("Can't find " + id);

    }

    public static String getJsonStringFromHashMap(HashMap<String, String> hashMap) {
        return new Gson().toJson(hashMap);
    }

    public static SharedPreferences getDefaultSP(Context context) {

        return PreferenceManager.getDefaultSharedPreferences(context);

    }


    public static int getUserIdFromSP(Context context) {

        return getDefaultSP(context).getInt("userid", 0);

    }

    public static String getDeviceResolution(Context context) {
        int density = context.getResources().getDisplayMetrics().densityDpi;
        switch (density) {
            case DisplayMetrics.DENSITY_MEDIUM:
                return "MDPI";
            case DisplayMetrics.DENSITY_HIGH:
                return "HDPI";
            case DisplayMetrics.DENSITY_LOW:
                return "LDPI";
            case DisplayMetrics.DENSITY_XHIGH:
                return "XHDPI";
            case DisplayMetrics.DENSITY_TV:
                return "TV";
            case DisplayMetrics.DENSITY_XXHIGH:
                return "XXHDPI";
            case DisplayMetrics.DENSITY_XXXHIGH:
                return "XXXHDPI";
            default:
                return "Unknown";
        }
    }

    public static void showContentView(Activity activity, boolean showStatus) {

        int visibleStatus = showStatus ? View.VISIBLE : View.GONE;

        activity.findViewById(android.R.id.content).setVisibility(visibleStatus);


    }

    public static void dismissProgressDialog(ProgressDialog progressDialog) {

        if (progressDialog != null) {
            progressDialog.dismiss();
        }

    }

    public static void finishActivity(Activity activityInstance) {


        if (activityInstance != null) {
            activityInstance.finish();
        }

    }

    public static ProgressDialog showProgressDialog(Context context) {

        final ProgressDialog progressDialog = new ProgressDialog(context);

        progressDialog.setMessage("Please Wait.....");


                if (!((Activity) context).isFinishing())
                {

                    progressDialog.show();

                    Common.Log.i("?????-After progressDialog.show()");

                }



        return progressDialog;


    }

    public static void refreshActivity(Activity activity) {

        Intent intent = activity.getIntent();
        activity.finish();
        activity.startActivity(intent);

    }

    public static Gson getCustomGson() {
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }

    public static <T> T getSpecificDataObject(Object object, Class<T> classOfT) {

        String jsonString = new Gson().toJson(object);

        return new Gson().fromJson(jsonString, classOfT);

    }

    public static void showScreenDensity() {

    }



    public static String getStringResourceText(int resourceId) {
        return TATX.getInstance().getResources().getString(resourceId);
    }

    public static float getDimensionResourceValue(int resourceId) {
        return TATX.getInstance().getResources().getDimension(resourceId);
    }

    public static String getOnlyPhoneNumber(String countryCode, String phoneNumberWithCountryCode) {
        return countryCode == null ? phoneNumberWithCountryCode : phoneNumberWithCountryCode.replaceFirst(countryCode.replace("+", ""), "");
    }

    public static void saveUserIdIntoSP(int userid) {

        getDefaultSP(TATX.getInstance().getApplicationContext()).edit().putInt(Constants.SharedPreferencesKeys.USERID, userid).commit();

    }

    public static void saveLoginStatusIntoSP(boolean status) {

        getDefaultSP(TATX.getInstance().getApplicationContext()).edit().putBoolean(Constants.SharedPreferencesKeys.LOGIN_STATUS, status).commit();

    }

    public static void setCircleImageBackgroundFromUrl(Context context, ImageView profileImgView, String profilePicUrl) {

        if (profilePicUrl != null && !TextUtils.isEmpty(profilePicUrl)) {

            Picasso.with(context).load(profilePicUrl).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(profileImgView);

        } else {
            Common.Log.i("Unable to set Bg Image.");
        }


    }

    public static String getLocaleFromSP(Context context)
    {
//        return Common.getDefaultSP(context).getString(Constants.SharedPreferencesKeys.APP_LOCALE, Language.ENGLISH.getLocaleCode());

        Common.Log.i("? - Locale.getDefault().getLanguage() : "+Locale.getDefault().getLanguage());

        return Common.getDefaultSP(context).getString(Constants.SharedPreferencesKeys.APP_LOCALE, Locale.getDefault().getLanguage());
    }

    public static void setLocaleToSP(Context context, String localeCode)
    {
        Common.getDefaultSP(context).edit().putString(Constants.SharedPreferencesKeys.APP_LOCALE,localeCode).commit();

    }

    public static void restartApp(Activity activity)
    {

        Intent launchIntentForPackage = activity.getPackageManager().getLaunchIntentForPackage(activity.getPackageName());
        launchIntentForPackage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        launchIntentForPackage.putExtra(Constants.IntentKeys.SHOW_CONTENT_VIEW,false);
        activity.startActivity(launchIntentForPackage);

    }

    public static void sendUnReadNotificationCountBrodCast(Context context, int unReadNotificationsCount) {

        Intent intent = new Intent();

        intent.setAction(PushNotificationListenerActivity.UN_READ_NOTIFICATION_COUNT_CHANGE);

        intent.putExtra(Constants.KEY_1,unReadNotificationsCount);

        context.sendBroadcast(intent);

    }

    public static Dialog getAppThemeCustomDialog(Activity activity, int contentViewLayoutId, float leftRightMargin)
    {

        final Dialog dialog = new Dialog(activity);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(contentViewLayoutId);

        DisplayMetrics metrics = new DisplayMetrics();

        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        Common.Log.i("? - Display width in px is " + metrics.widthPixels);

//                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.getWindow().setLayout(metrics.widthPixels - (int)leftRightMargin, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.getWindow().setGravity(Gravity.CENTER);

        return dialog;

    }

    public static void showNegativeTatxBalanceErrorDialog(final Activity activity, String tatxBalance) {

        final Dialog dialog = Common.getAppThemeCustomDialog(activity,R.layout.custom_dialog_pay_tatx_balance, 200);

        TextView  tvTatxBalance = (TextView) dialog.findViewById(R.id.tv_tatx_balance);

        tvTatxBalance.setText(tatxBalance);

        TextView  tvAddTatxBalance = (TextView) dialog.findViewById(R.id.tv_add_tatx_balance);

        tvAddTatxBalance.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

              //  activity.startActivity(new Intent(activity,AddCreditBalanceActivity.class));

                dialog.dismiss();

            }

        });

        dialog.show();

    }



    public static String booleanToIntString(boolean b)
    {

        return String.valueOf(b ? 1 : 0);

    }


    public static class Log {
        public static void i(String string) {

            android.util.Log.i("SKP", string);


        }
    }

    public static void setLanguage(Context context, String language) {
        Locale locale1 = new Locale(language);
        Locale.setDefault(locale1);
        Configuration config1 = new Configuration();
        config1.locale = locale1;
        context.getResources().updateConfiguration(config1,
                context.getResources().getDisplayMetrics());

    }

    public static void hideView(Context context, final View view, int id) {
        Animation animation = AnimationUtils.loadAnimation(context, id);
        //use this to make it longer:  animation.setDuration(1000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }
        });

        view.startAnimation(animation);
    }


    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }


    public static void copy(String string, Context context) {
        if (string.trim().length() > 0) {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboardMgr = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardMgr.setText(string.trim());
            } else {
                // this api requires SDK version 11 and above, so suppress warning for now
                android.content.ClipboardManager clipboardMgr = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied..", string.trim());
                clipboardMgr.setPrimaryClip(clip);
            }
            customToast(context, Common.getStringResourceText(R.string.copied), TOAST_TIME);
        }
    }

    public void paste(TextView txtNotes, Context context) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard.getText() != null) {
                // txtNotes.getText().insert(txtNotes.getSelectionStart(), clipboard.getText());

            }
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            if (item.getText() != null) {
                // txtNotes.getText().insert(txtNotes.getSelectionStart(), item.getText());
            }
        }
    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isValidMobileNumber(String phoneNumber) {

        if (phoneNumber.matches("123456789") || phoneNumber.matches("1234567890")
                || phoneNumber.matches("0123456789") || phoneNumber.matches("0000000000")
                || phoneNumber.matches("1111111111") || phoneNumber.matches("2222222222")
                || phoneNumber.matches("3333333333") || phoneNumber.matches("4444444444")
                || phoneNumber.matches("5555555555") || phoneNumber.matches("6666666666")
                || phoneNumber.matches("7777777777") || phoneNumber.matches("8888888888")
                || phoneNumber.matches("9999999999") || phoneNumber.matches("0000000000")) {
            return false;
        }
        return true;
    }

    public static Bitmap getBitmapFromByteArray(byte[] bytes) {

        return bytes == null ? null : BitmapFactory.decodeStream(new ByteArrayInputStream(bytes));
    }

    public static void bitmapToDrawable(Bitmap bitmap, TextView textView) {
        BitmapDrawable d = new BitmapDrawable(bitmap);
        d.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        textView.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);


    }


    public static Activity getPreviousActivity() {
        return previousActivity;
    }

    public static void setPreviousActivity(Activity activity) {
        previousActivity = activity;
    }


    public static SecretKey generateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        return new SecretKeySpec(Constants.ENCRYPTION_KEY.getBytes(), "AES");
    }

    public static byte[] encryptMsg(String message, SecretKey secret) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
/* Encrypt the message. */
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        byte[] cipherText = cipher.doFinal(message.getBytes("UTF-8"));
        return cipherText;
    }

    public static String decryptMsg(byte[] cipherText, SecretKey secret) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {

    /* Decrypt the message, given derived encContentValues and initialization vector. */
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        String decryptString = new String(cipher.doFinal(cipherText), "UTF-8");
        return decryptString;
    }


    public static String getTwoDecimalRoundValueString(double value) {
//        return new DecimalFormat("#.##").format(value);
        return new DecimalFormat("0.00").format(value);
    }

    public static void removeZeroPrefix(final Context context, final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // android.util.Log.d("beforeTextChanged","No need to enter1 "+s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // android.util.Log.d("beforeTextChanged","No need to enter 2 "+s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // android.util.Log.d("beforeTextChanged","No need to enter 3 "+s.toString());
                if (s.toString().startsWith("0")) {
                    //      android.util.Log.d("beforeTextChanged","No need to enter 4 "+s.toString());
                    editText.setText("");
                    Common.customToast(context, "No need to enter 0");
                }
            }
        });

    }



    public static void restrictSpaceInPasswordField(final Context context, final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                android.util.Log.d("beforeTextChanged","No need to enter1 "+s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                android.util.Log.d("beforeTextChanged","No need to enter 2 "+s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                android.util.Log.d("beforeTextChanged","Space not allowed. "+s.toString());
                if (s.toString().contains(" ")) {
                    //      android.util.Log.d("beforeTextChanged","No need to enter 4 "+s.toString());
                    editText.setText(s.toString().replaceAll("\\s",""));
                    editText.setSelection(s.toString().replaceAll("\\s","").length());
                    Common.customToast(context, "Space not allowed.");
                }
            }
        });

    }


    public static String ignoreZeroPrefix(final EditText editText) {
        String text = editText.getText().toString().trim();
        if (NumberUtils.isDigits(text)) {
            if (text.startsWith("0")){
                return text.substring(1);
            }else {
                return text;
            }


        }else {
             return text;
        }

    }



    public static Bitmap getBitmapFromView(View view)
    {

        Common.Log.i("? - view.getWidth() : " + view.getWidth());
        Common.Log.i("? - view.getHeight() : " + view.getHeight());

        Common.Log.i("? - view.getMeasuredWidth() : " + view.getMeasuredWidth());
        Common.Log.i("? - view.getMeasuredHeight() : " + view.getMeasuredHeight());

        view.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Bitmap b = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(b);

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.draw(c);

        return b;


    }





    /**
     * Compares two version strings.
     *
     * Use this instead of String.compareTo() for a non-lexicographical
     * comparison that works for version strings. e.g. "1.10".compareTo("1.6").
     *
     * @note It does not work if "1.10" is supposed to be equal to "1.10.0".
     *
     * @param str1 a string of ordinal numbers separated by decimal points.
     * @param str2 a string of ordinal numbers separated by decimal points.
     * @return The result is a negative integer if str1 is _numerically_ less than str2.
     *         The result is a positive integer if str1 is _numerically_ greater than str2.
     *         The result is zero if the strings are _numerically_ equal.
     */
    public static int versionCompare(String str1, String str2) {
        String[] vals1 = str1.split("\\.");
        String[] vals2 = str2.split("\\.");
        int i = 0;
        // set index to first non-equal ordinal or length of shortest version string
        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
            i++;
        }
        // compare first non-equal ordinal number
        if (i < vals1.length && i < vals2.length) {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        }
        // the strings are equal or one string is a substring of the other
        // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
        return Integer.signum(vals1.length - vals2.length);
    }



    public static void setupUI(final Activity activity, View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(activity);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(activity, innerView);
            }
        }
    }



    public static File savefile(Activity activity, Uri sourceuri)
    {
        String sourceFilename= sourceuri.getPath();
        //   String destinationFilename = android.os.Environment.getExternalStorageDirectory().getPath()+File.separatorChar+"abc.mp3";
        File mFileTemp;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mFileTemp = new File(Environment.getExternalStoragePublicDirectory(UpdateProfileActivity.IMAGE_DIRECTORY_NAME), InternalStorageContentProvider.getOutputMediaFile(1));
        } else {
            mFileTemp = new File(activity.getFilesDir(), InternalStorageContentProvider.getOutputMediaFile(1));
        }
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(sourceFilename));
            bos = new BufferedOutputStream(new FileOutputStream(mFileTemp.getPath(), false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while(bis.read(buf) != -1);
        } catch (IOException e) {

        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {

            }
        }
        return mFileTemp;
    }


    public static TypedFile getTypedFile(File file) {
        Common.Log.i("file : " + file);
        return (file != null) ? new TypedFile("multipart/form-data", file) : null;
    }


    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


    public static int getColorFromResource(int resourceId) {
        return TATX.getInstance().getResources().getColor(resourceId);
    }


    public static void setViewsEnableStatus(boolean status, View... views)
    {


        Common.Log.i("views.length : "+views.length);

        for (View view : views)
        {

            Common.Log.i("view : "+view);
            Common.Log.i("view.toString() : "+view.toString());
            view.setFocusableInTouchMode(status);
            view.setFocusable(status);
            view.setEnabled(status);


        }


    }

    public static void setViewsEnableStatuss(boolean status, View... views)
    {


        Common.Log.i("views.length : "+views.length);

        for (View view : views)
        {

            Common.Log.i("view : "+view);
            Common.Log.i("view.toString() : "+view.toString());
//            view.setFocusableInTouchMode(status);
//            view.setFocusable(status);
            view.setEnabled(status);


        }


    }



    public static void setSuggetions(final Activity context, final GoogleApiClient mGoogleApiClient, final RecyclerView rvSavedAndRecentSuggestions, final RecyclerView rvGoogleSuggestions, final ArrayList<FavLocation> favLocationArrayList, final EditText editText )
    {

        final LatLngBounds BOUNDS_JAMAICA = new LatLngBounds(new LatLng(-57.965341647205726, 144.9987719580531), new LatLng(72.77492067739843, -9.998857788741589));


        final PlacesAutoCompleteAdapter googleSuggestionsAdapter =  new PlacesAutoCompleteAdapter(context, R.layout.searchview_adapter, mGoogleApiClient, BOUNDS_JAMAICA, null);


        LinearLayoutManager llManagerGSL=new LinearLayoutManager(context);

        rvGoogleSuggestions.setLayoutManager(llManagerGSL);

        rvGoogleSuggestions.setAdapter(googleSuggestionsAdapter);

        LinearLayoutManager llManagerSRL = new LinearLayoutManager(context);

        rvSavedAndRecentSuggestions.setLayoutManager(llManagerSRL);


        SavedLocationListAdapterFilterable savedLocationListAdapterFilterable = null;


        if (favLocationArrayList!=null )
        {

            savedLocationListAdapterFilterable = new SavedLocationListAdapterFilterable(context,favLocationArrayList , 1);

            rvSavedAndRecentSuggestions.setAdapter(savedLocationListAdapterFilterable);


        }


        Filter filterSRL = ((SavedLocationListAdapterFilterable) rvSavedAndRecentSuggestions.getAdapter()).getFilter();

        if (filterSRL != null)
        {
            filterSRL.filter("");
        }

        googleSuggestionsAdapter.getFilter().filter("");




//        googleSuggestionsAdapter.notifyDataSetChanged();

      /*  if (savedLocationListAdapterFilterable!=null)
        {
            savedLocationListAdapterFilterable.notifyDataSetChanged();
        }
*/




        editText.addTextChangedListener(new TextWatcher()
        {

            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                android.util.Log.d("onTextChanged",s.toString());

                ((SavedLocationListAdapterFilterable) rvSavedAndRecentSuggestions.getAdapter()).getFilter().filter(s);


                Common.Log.i("?? - recyclerviewSavedLocatons.getAdapter().getItemCount() : "+rvSavedAndRecentSuggestions.getAdapter().getItemCount());

                Common.Log.i("?? - googleSuggestionsAdapter.getItemCount() : "+googleSuggestionsAdapter.getItemCount());




                if (!s.toString().equals("") && mGoogleApiClient.isConnected())
                {

                    googleSuggestionsAdapter.getFilter().filter(s.toString());

                }
                else if (!mGoogleApiClient.isConnected())
                {
                    // Toast.makeText(getApplicationContext(), Constants.API_NOT_CONNECTED, Toast.LENGTH_SHORT).show();
                    android.util.Log.e(Constants.PlacesTag, Constants.API_NOT_CONNECTED);

                }


            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });


        rvGoogleSuggestions.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Common.hideSoftKeyboard(context);

                        final PlacesAutoCompleteAdapter.PlaceAutocomplete item = googleSuggestionsAdapter.getItem(position);


                        if (editText.isFocused()) {
                            if (item == null) {
                                if (editText.getText().length() > 0) {
                                    Toast.makeText(context, Common.getStringResourceText(R.string.no_result_for) + editText.getText(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, Common.getStringResourceText(R.string.please_enter_address) + editText.getText(), Toast.LENGTH_SHORT).show();
                                }

                                return;
                            }
                            final String placeId = String.valueOf(item.placeId);

                        /*
                        Issue a request to the Places Geo Data API to retrieve a Place object with additional
                        details about the place.
                        */
                            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                                    .getPlaceById(mGoogleApiClient, placeId);
                            placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                                @Override
                                public void onResult(PlaceBuffer places) {
                                    if (!places.getStatus().isSuccess()) {
                                        // Request did not complete successfully
                                        android.util.Log.d("Place query", "Place query did not complete. Error: " + places.getStatus().toString());
                                        places.release();
                                        return;
                                    }
                                    // Get the Place object from the buffer.
                                    final Place place = places.get(0);
                                    LatLng latLng = place.getLatLng();
                                    switch (editText.getId()){
                                        case R.id.tv_destination_address_to_show:
                                            OnTripMapActivity.instance().mRecyclerViewClickListner(latLng);

                                            break;
                                        case R.id.tv_source_selected:
                                            GoogleMapDrawerActivity.getInstatnce().mRecyclerViewSourceClickListner(latLng);

                                            break;
                                        case R.id.tv_destination_selected:
                                            GoogleMapDrawerActivity.getInstatnce().mRecyclerViewDestinationClickListner(latLng);
                                            break;

                                    }

                                    editText.setText(place.getAddress());

                                    googleSuggestionsAdapter.getFilter().filter("");



                                }
                            });
                        }
                    }
                })

        );

        rvSavedAndRecentSuggestions.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LatLng latLng = new LatLng(favLocationArrayList.get(position).latitude, favLocationArrayList.get(position).longitude);

                switch (editText.getId()){
                    case R.id.tv_destination_address_to_show:
                        OnTripMapActivity.instance().mRecyclerViewClickListner(latLng);

                        break;
                    case R.id.tv_source_selected:
                        GoogleMapDrawerActivity.getInstatnce().mRecyclerViewSourceClickListner(latLng);

                        break;
                    case R.id.tv_destination_selected:
                        GoogleMapDrawerActivity.getInstatnce().mRecyclerViewDestinationClickListner(latLng);

                        break;

                }



                editText.setText(Common.getCompleteAddressString(context,favLocationArrayList.get(position).latitude,favLocationArrayList.get(position).longitude).getCompleteAddress());




            }
        }));


    }


    public static void setDestinationSuggetions(final Activity context, final GoogleApiClient mGoogleApiClient, final RecyclerView rvSavedAndRecentSuggestions, final RecyclerView rvGoogleSuggestions, final ArrayList<FavLocation> favLocationArrayList, final EditText editText )
    {

        final LatLngBounds BOUNDS_JAMAICA = new LatLngBounds(new LatLng(-57.965341647205726, 144.9987719580531), new LatLng(72.77492067739843, -9.998857788741589));


        final PlacesAutoCompleteAdapter googleSuggestionsAdapter =  new PlacesAutoCompleteAdapter(context, R.layout.searchview_adapter, mGoogleApiClient, BOUNDS_JAMAICA, null);


        LinearLayoutManager llManagerGSL=new LinearLayoutManager(context);

        rvGoogleSuggestions.setLayoutManager(llManagerGSL);

        rvGoogleSuggestions.setAdapter(googleSuggestionsAdapter);

        LinearLayoutManager llManagerSRL = new LinearLayoutManager(context);

        rvSavedAndRecentSuggestions.setLayoutManager(llManagerSRL);


        SavedLocationListAdapterFilterable savedLocationListAdapterFilterable = null;


        if (favLocationArrayList!=null )
        {

            savedLocationListAdapterFilterable = new SavedLocationListAdapterFilterable(context,favLocationArrayList , 1);

            rvSavedAndRecentSuggestions.setAdapter(savedLocationListAdapterFilterable);


        }


        Filter filterSRL = ((SavedLocationListAdapterFilterable) rvSavedAndRecentSuggestions.getAdapter()).getFilter();

        if (filterSRL != null)
        {
            filterSRL.filter("");
        }

        googleSuggestionsAdapter.getFilter().filter("");



        editText.addTextChangedListener(new TextWatcher()
        {

            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                android.util.Log.d("onTextChanged",s.toString());

                ((SavedLocationListAdapterFilterable) rvSavedAndRecentSuggestions.getAdapter()).getFilter().filter(s);


                Common.Log.i("?? - recyclerviewSavedLocatons.getAdapter().getItemCount() : "+rvSavedAndRecentSuggestions.getAdapter().getItemCount());

                Common.Log.i("?? - googleSuggestionsAdapter.getItemCount() : "+googleSuggestionsAdapter.getItemCount());




                if (!s.toString().equals("") && mGoogleApiClient.isConnected())
                {

                    googleSuggestionsAdapter.getFilter().filter(s.toString());

                }
                else if (!mGoogleApiClient.isConnected())
                {
                    // Toast.makeText(getApplicationContext(), Constants.API_NOT_CONNECTED, Toast.LENGTH_SHORT).show();
                    android.util.Log.e(Constants.PlacesTag, Constants.API_NOT_CONNECTED);

                }


            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });


        rvGoogleSuggestions.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Common.hideSoftKeyboard(context);

                        final PlacesAutoCompleteAdapter.PlaceAutocomplete item = googleSuggestionsAdapter.getItem(position);


                        if (editText.isFocused()) {
                            if (item == null) {
                                if (editText.getText().length() > 0) {
                                    Toast.makeText(context, Common.getStringResourceText(R.string.no_result_for) + editText.getText(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, Common.getStringResourceText(R.string.please_enter_address) + editText.getText(), Toast.LENGTH_SHORT).show();
                                }

                                return;
                            }
                            final String placeId = String.valueOf(item.placeId);

                        /*
                        Issue a request to the Places Geo Data API to retrieve a Place object with additional
                        details about the place.
                        */
                            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                                    .getPlaceById(mGoogleApiClient, placeId);
                            placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                                @Override
                                public void onResult(PlaceBuffer places) {
                                    if (!places.getStatus().isSuccess()) {
                                        // Request did not complete successfully
                                        android.util.Log.d("Place query", "Place query did not complete. Error: " + places.getStatus().toString());
                                        places.release();
                                        return;
                                    }
                                    // Get the Place object from the buffer.
                                    final Place place = places.get(0);
                                    LatLng latLng = place.getLatLng();

                                    GoogleMapDrawerActivity.getInstatnce().mRecyclerViewDestinationClickListner(latLng);

                                    editText.setText(place.getAddress());

                                  //  googleSuggestionsAdapter.getFilter().filter("");



                                }
                            });
                        }
                    }
                })

        );

        rvSavedAndRecentSuggestions.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LatLng latLng = new LatLng(favLocationArrayList.get(position).latitude, favLocationArrayList.get(position).longitude);

                switch (editText.getId()){
                    case R.id.tv_destination_address_to_show:
                        OnTripMapActivity.instance().mRecyclerViewClickListner(latLng);

                        break;
                    case R.id.tv_source_selected:
                        GoogleMapDrawerActivity.getInstatnce().mRecyclerViewSourceClickListner(latLng);

                        break;
                    case R.id.tv_destination_selected:
                        GoogleMapDrawerActivity.getInstatnce().mRecyclerViewDestinationClickListner(latLng);

                        break;

                }



                editText.setText(Common.getCompleteAddressString(context,favLocationArrayList.get(position).latitude,favLocationArrayList.get(position).longitude).getCompleteAddress());




            }
        }));


    }
    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
       /* int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
*/


        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, newWidth, newHeight, false);
        return resizedBitmap;
    }



    public static Bounds getCityBoundaries(Context contextAct,LatLng latLng)
    {
//        GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyDLFHKxjARW-4slFFpIBRUXHB1s4lCBCvw");
//        GeoApiContext geoApiContext = new GeoApiContext().setApiKey(Common.getStringResourceText(R.string.google_maps_key));
        GeoApiContext geoApiContext = new GeoApiContext().setApiKey("AIzaSyBjytr0Gf0glqt_EmtC4RSgFm3HAkAtVuE");

        try
        {
            Geocoder geocoder = new Geocoder(contextAct, Locale.getDefault());

            Common.Log.i("latLng : "+latLng);
            Common.Log.i("latLng.toString() : "+latLng.toString());

            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            Address address = addressList.get(0);

            Common.Log.i("address.toString() : "+address.toString());


            ArrayList<String> alAddressText = new ArrayList<String>();

            alAddressText.add(address.getLocality());

            alAddressText.add(address.getSubAdminArea());

            alAddressText.add(address.getAdminArea());

            alAddressText.add(address.getCountryName());

            alAddressText.removeAll(Collections.singleton(null));


            String searchAddressText = TextUtils.join(",", alAddressText.toArray());

            Common.Log.i("searchAddressText : "+searchAddressText);

            GeocodingResult[] results = GeocodingApi.geocode(geoApiContext, searchAddressText).await();

            Common.Log.i("results.toString() : "+results.toString());

            Common.Log.i("formattedAddress : "+results[0].formattedAddress);

            Bounds bounds = results[0].geometry.bounds;

            if(bounds == null)
            {
                bounds = results[0].geometry.viewport;
            }
            else
            {

                Common.Log.i("bounds.northeast : "+results[0].geometry.bounds.northeast.toString());
                Common.Log.i("bounds.southwest : "+results[0].geometry.bounds.southwest.toString());

            }


            Common.Log.i("viewport.northeast :"+results[0].geometry.viewport.northeast.toString());
            Common.Log.i("viewport.southwest : "+results[0].geometry.viewport.southwest.toString());


            return bounds;




        } catch (Exception e)
        {

            Common.Log.i("????? - Inside getCityBoundaries() Exception : "+e.toString());
            e.printStackTrace();
        }


        return null;
    }



    public static void insertOtp(final Context context, final EditText editText, EditText... editTexts) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // android.util.Log.d("beforeTextChanged","No need to enter1 "+s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // android.util.Log.d("beforeTextChanged","No need to enter 2 "+s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // android.util.Log.d("beforeTextChanged","No need to enter 3 "+s.toString());
                if (s.toString().startsWith("0")) {
                    //      android.util.Log.d("beforeTextChanged","No need to enter 4 "+s.toString());
                    editText.setText("");
                    Common.customToast(context, "No need to enter 0");
                }
            }
        });

    }




    public static void showInternetUnavailableDialog(final Activity activity)
    {

        internetErrorDialog = new Dialog(activity);

        internetErrorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        internetErrorDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        internetErrorDialog.setContentView(R.layout.internet_unavailable);


        internetErrorDialog.setCanceledOnTouchOutside(false);

        internetErrorDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                activity.finish();
            }
        });



        internetErrorDialog.show();

    }




    public static void dismissInternetUnavailableDialog()
    {

        if (internetErrorDialog != null)
        {
            internetErrorDialog.dismiss();
        }


    }





}

