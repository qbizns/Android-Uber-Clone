package com.tatx.partnerapp.commonutills;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 20-04-2016.
 */
public class Urls {

    public  static final String WebApi="http://183.82.112.99/MediAPIServicesQA/";
    //http://202.63.105.22/MediAPIServicesQA/";
    public  static final String WebApidev="http://183.82.112.99/MediAPIServicesdev/";

    public static final String LoginUrl="Token";

    public static final String RegistrationUrl="api/Accounts/CreateAccount";

    public static final String ActivateUrl="api/Accounts/ActivateAccount";

    public static final String ForgotPassword="api/Profile/ForgotPassword";


    public static final String Testuser="api/Accounts/IsUserNameExist?strUserName";

    public static final String BasicInfo="api/Profile/AddProfile";

    //public static final ArrayList<DoctorNames> listtest=new ArrayList<DoctorNames>();

    public static String loc_id="1";
    public static int id=1;

    public static final int intcheck=0;

    public static int pageindex=0;

    public static int pageNo=1;



    public static String ProfileId=" ";
    public static String AccountId=" ";
    public static String mobilenum=" ";
    public static String name=" ";
    ProgressDialog pDialog;
    Context context;
    //public static String
    JSONArray testarray = new JSONArray();
    JSONArray dataArray;

    JSONArray sameidlist=new JSONArray();

    public static JSONArray fulldatalist;

    ArrayList<List> foreachh;
    public static ArrayList<ArrayList<String>> collection = new ArrayList<ArrayList<String>>();
    public static ArrayList<String> list;
    public static ArrayList<String> fees;

    public static ArrayList<String> timeslotlist1;

    public static ArrayList<String> address;

    public static ArrayList<String> idlist;
    JSONObject jsonMain;
    ArrayList<JSONArray> jlist;
    public static ArrayList<HashMap<String, ArrayList<String>>> slot_book=new ArrayList<HashMap<String, ArrayList<String>>>();

    static ArrayList< String> firstweek=new ArrayList<String>();

    static ArrayList< String> secondweek=new ArrayList<String>();

    ArrayList<String> hospi_list=new ArrayList<String>();
    String MY_FILE_NAME = "mytextfile.txt";
    public static ArrayList<String> checklist;
    public static HashMap<String,  ArrayList<List>> book=null;

    public static String SLOTTIME="slottime";
    public static String SLOTDATE="slotdate";
    public static String STATUS="status";
    public static ArrayList<String> slotslist=new ArrayList<String>();
    public static JSONObject sinobj,match;
    public static HashMap<String,  ArrayList<String>> slots;

    Calendar currentDate = Calendar.getInstance(); //Get the current date

    SimpleDateFormat formatter= new SimpleDateFormat("dd/MM/yyyy"); //format it as per your requirement
    String dateToday = formatter.format(currentDate.getTime());


    @SuppressLint("NewApi")
    public void hospitallist(String jsonString){
        try{

            JSONObject hos_json=new JSONObject(jsonString);
            JSONArray hos_arr=hos_json.getJSONArray("ListofDoctors");

            for (int i = 0; i < hos_arr.length(); i++) {

                JSONObject for_json=hos_arr.getJSONObject(i);

                for (int j = i+1; j < hos_arr.length(); j++) {

                    JSONObject for_json1=hos_arr.getJSONObject(j);


                    if (for_json.getInt("DoctorID")==for_json1.getInt("DoctorID")) {
                        checklist=new ArrayList<String>();

                        JSONArray arr_json = new JSONArray(hos_arr.getJSONObject(j).getString("ApptTimingsList"));
                        for (int m = 0; m < arr_json.length(); m++) {

                            JSONObject json_book=arr_json.getJSONObject(m);
                            checklist.add(json_book.getString("SlotTime"));
                            checklist.add(json_book.getString("SlotDate"));

                        }

                    }

                    collection.add(checklist);
                    ((List) hos_arr).remove(j);
                }

            }


        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void checkList(String jsonString){


        try {
            HashMap<String,List<String>> myMap = new HashMap<String,List<String>>();
            checklist=new ArrayList<String>();
            JSONObject hos_json=new JSONObject(jsonString);
            JSONArray hos_arr=hos_json.getJSONArray("ListofDoctors");
            ArrayList<String> arrayID = null;
            for(int i = 0; i < hos_arr.length(); i++)
            {

                JSONObject tagObject = hos_arr.getJSONObject(i);
                int mandatory_tag = tagObject.getInt("DoctorID");
                String id = tagObject.getString("ServiceProviderName");

                if(myMap.isEmpty())
                {

                    JSONArray arr_json = new JSONArray(hos_arr.getJSONObject(i).getString("ApptTimingsList"));
                    for (int m = 0; m < arr_json.length(); m++) {

                        JSONObject json_book=arr_json.getJSONObject(m);
                        checklist.add(json_book.getString("SlotTime"));
                        checklist.add(json_book.getString("SlotDate"));

                    }

                    arrayID = new ArrayList<String>();
                    arrayID.add(checklist.toString());
                    myMap.put(""+mandatory_tag, arrayID);
                    //  checklist=new ArrayList<String>();
                }
                else if(myMap.containsKey(mandatory_tag))
                {

                    JSONArray arr_json = new JSONArray(hos_arr.getJSONObject(i).getString("ApptTimingsList"));
                    for (int m = 0; m < arr_json.length(); m++) {

                        JSONObject json_book=arr_json.getJSONObject(m);
                        checklist.add(json_book.getString("SlotTime"));
                        checklist.add(json_book.getString("SlotDate"));

                    }
                    arrayID.add(checklist.toString());
                    myMap.put(""+mandatory_tag, arrayID);
                }
                else
                {
                    //checklist=new ArrayList<String>();
                    arrayID = new ArrayList<String>();
                    JSONArray arr_json = new JSONArray(hos_arr.getJSONObject(i).getString("ApptTimingsList"));
                    for (int m = 0; m < arr_json.length(); m++) {

                        JSONObject json_book=arr_json.getJSONObject(m);
                        checklist.add(json_book.getString("SlotTime"));
                        checklist.add(json_book.getString("SlotDate"));

                    }
                    arrayID.add(checklist.toString());
                    myMap.put(""+mandatory_tag, arrayID);
                }


            }


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


}
