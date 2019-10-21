package com.tatx.partnerapp.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.abstractclasses.BaseActivity;
import com.tatx.partnerapp.adapter.CustomRecyclerViewAdapterCarMake;
import com.tatx.partnerapp.adapter.CustomRecyclerViewAdapterCarModel;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.constants.ServiceUrls;
import com.tatx.partnerapp.cropimages.InternalStorageContentProvider;
import com.tatx.partnerapp.customviews.CustomFloatingLabelEditText;
import com.tatx.partnerapp.network.RetrofitRequester;
import com.tatx.partnerapp.network.RetrofitResponseListener;
import com.tatx.partnerapp.pojos.ApiResponseVo;
import com.tatx.partnerapp.pojos.Make;
import com.tatx.partnerapp.pojos.MakeCabVo;
import com.tatx.partnerapp.pojos.Model;
import com.tatx.partnerapp.pojos.ModelCabVo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.mime.TypedFile;

/**
 * Created by Home on 15-09-2016.
 */
public class AddCarActivity extends BaseActivity implements RetrofitResponseListener,CustomFloatingLabelEditText.OnFloatingLabelEditTextClickListener {


    public static final int REQUEST_CODE_GALLERY = 50001;
    public static final int REQUEST_CODE_CROP_IMAGE = 50003;
    public static final int REQUEST_CODE_TAKE_PICTURE = 50002;
    private File mFileTemp;
    public static final String IMAGE_DIRECTORY_NAME = "Tatx";

    @BindView(R.id.no_plate) EditText plateNo;
    @BindView(R.id.registration_no) EditText registrationNo;
    @BindView(R.id.cet_registration_expiry_date) EditText registrationExpiryDate;
    @BindView(R.id.cet_car_make)
    CustomFloatingLabelEditText etCarMake;
    @BindView(R.id.cet_car_model)  CustomFloatingLabelEditText etCarModel;
    @BindView(R.id.et_car_color) EditText etCarColor;
    @BindView(R.id.btn_save)
    Button saveAccount;
    @BindView(R.id.rel_select_img)
    RelativeLayout relSelectImg;
    @BindView(R.id.tv_select_img)
    TextView tvSelectImg;


    static final int DATE_PICKER_ID_AFTER=2222;
    private int year;
    private int month;
    private int day;
    private EditText copyDate;
    private int mScreenWidth;
    private int mScreenHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car_account);
        ButterKnife.bind(this);
        setTitleText(Common.getStringResourceText(R.string.add_vehicles));
        etCarMake.setOnFloatingLabelEditTextClickListener(this,null);

        etCarModel.setOnFloatingLabelEditTextClickListener(this,etCarColor);

        mScreenWidth = getWindowManager().getDefaultDisplay().getWidth();

        mScreenHeight = getWindowManager().getDefaultDisplay().getHeight();


    }

    @OnClick (R.id.btn_save)
    void setAccountNumber()
    {

        if (plateNo.getText().toString().trim().length() < 3)
        {
            plateNo.requestFocus();
            plateNo.setError(Common.getStringFromResources(R.string.please_enter_car_number));

        }
        else if (registrationNo.getText().toString().trim().length() < 3) {
            registrationNo.requestFocus();
            registrationNo.setError(Common.getStringFromResources(R.string.please_enter_registration_number));

        }
        else if (registrationExpiryDate.getText().toString().trim().length() < 3) {
            registrationExpiryDate.requestFocus();
            registrationExpiryDate.setError(Common.getStringFromResources(R.string.please_enter_registration_expiry_date));

        }
        else if (etCarMake.getText().toString().trim().length() < 1) {
            etCarMake.requestFocus();
            etCarMake.setError(Common.getStringFromResources(R.string.please_select_car_make));

        }else if (etCarModel.getText().toString().trim().length() < 1) {
            etCarModel.requestFocus();
            etCarModel.setError(Common.getStringFromResources(R.string.please_select_car_model));

        }
        else if (etCarColor.getText().toString().trim().length() < 1) {
            etCarColor.requestFocus();
            etCarColor.setError(Common.getStringFromResources(R.string.please_enter_car_color));

        }
/*
        else  if (mFileTemp==null || mFileTemp.length() == 0)
        {
            tvSelectImg.setText(Common.getStringResourceText(R.string.please_select_first));
            tvSelectImg.setTextColor(getResources().getColor(R.color.buttonColor));
        }*/
        else {

            callUpdateProfileApi(plateNo.getText().toString(),etCarMake.getTag().toString(),etCarModel.getTag().toString(),registrationExpiryDate.getText().toString().trim(),etCarColor.getText().toString().trim(),registrationNo.getText().toString().trim());
        }


    }
    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId) {
        if (apiResponseVo.code != Constants.SUCCESS)
        {
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

            case ServiceUrls.RequestNames.ADD_CARS:

                setResult(Constants.ResultCodes.SUCCESS);
                finish();

                break;

        }


    }


    public void createCalenarInstance() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 0);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
    }



    @Override

    protected Dialog onCreateDialog(int id)
    {
        switch (id)
        {
            case DATE_PICKER_ID_AFTER:
                DatePickerDialog pickerDialog1= new DatePickerDialog(this, pickerListener, year, month, day);
                pickerDialog1.getDatePicker().setMinDate(new Date().getTime() - 10000);

                return pickerDialog1;

        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // Show selected date
            copyDate.setText(new StringBuilder().append(pad(day)).append("-")
                    .append(pad(month + 1)).append("-").append(pad(year)));


        }
    };

    /************************date formate 01/02/2015*******Used 0 before value<10********************/
    static String pad(int c){
        if(c>=10){
            return String.valueOf(c);
        }
        else {
            return '0'+String.valueOf(c);
        }
    }


    @OnClick(R.id.cet_registration_expiry_date)
    void setRegistrationExpiryDate(){
        createCalenarInstance();
        copyDate = registrationExpiryDate;
        showDialog(DATE_PICKER_ID_AFTER);
    }
    @OnClick(R.id.rel_select_img)
    void setRelSelectImg()
    {

        showPictureialog();



    }

    private void showPictureialog()
    {

        LinearLayout cameraBtn;

        LinearLayout galleryBtn;

        final Dialog dialog = new Dialog(this);

        // Setting dialogview
        Window window = dialog.getWindow();

        window.setGravity(Gravity.CENTER_VERTICAL);

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // window.setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        dialog.setTitle(Common.getStringResourceText(R.string.pick_image));

        dialog.setContentView(R.layout.capture_image_dailog);

        dialog.setCancelable(true);

        cameraBtn = (LinearLayout) dialog.findViewById(R.id.camera);

        galleryBtn = (LinearLayout) dialog.findViewById(R.id.gallery);

        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state))
        {
            mFileTemp = new File(Environment.getExternalStoragePublicDirectory(IMAGE_DIRECTORY_NAME), InternalStorageContentProvider.getOutputMediaFile(1));
        }
        else
        {
            mFileTemp = new File(getFilesDir(), InternalStorageContentProvider.getOutputMediaFile(1));
        }

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                takePicture();
            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openGallery();

            }
        });

        dialog.show();

    }

    @Override
    public void onFloatingLabelEditTextClick(CustomFloatingLabelEditText labelEditText)
    {

        switch (labelEditText.getId())
        {
            case R.id.cet_car_make:
                carMake();
                break;

            case R.id.cet_car_model:
                carModel();
                break;

        }


    }
    private void carModel()
    {



        Common.Log.i("cetCarModel.getTag() : "+etCarMake.getTag());

        if (etCarMake.getTag()!= null)
        {

            HashMap<String, String> params = new HashMap<>();

            params.put(ServiceUrls.ApiRequestParams.MAKE_ID, String.valueOf(etCarMake.getTag()));

            new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.MODEL_CAB,params);

        }
        else
        {

            Common.customToast(this,Common.getStringResourceText(R.string.please_select_car_make_first));

            etCarModel.clearFocus();

        }
    }


    void carMake()
    {

        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.MAKE_CAB,null);

    }


    private void showCarMakesListDialog(ArrayList<Make> makeList)
    {

        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.country_code_dialog);

        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.my_recycler_view);

        Button close = (Button) dialog.findViewById(R.id.button);

        EditText search = (EditText) dialog.findViewById(R.id.search_button);

        setCarMakeListData(recyclerView,makeList,dialog);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        search.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count)
            {

                ((CustomRecyclerViewAdapterCarMake)recyclerView.getAdapter()).getFilter().filter(charSequence);


            }


            @Override
            public void afterTextChanged(Editable s)
            {

            }


        });


        dialog.show();



    }

    private void setCarMakeListData(final RecyclerView recyclerView, ArrayList<Make> makeList, final Dialog dialog)
    {

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {

                if(TextUtils.isEmpty(etCarMake.getText()))
                {

                    etCarMake.setText(" ");

                }

                etCarMake.clearFocus();

            }


        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);

        if (makeList != null)
        {
            CustomRecyclerViewAdapterCarMake customRecyclerViewAdapterCarMake = new CustomRecyclerViewAdapterCarMake(makeList);

            recyclerView.setAdapter(customRecyclerViewAdapterCarMake);

        }
        else
        {
            recyclerView.setVisibility(View.GONE);
        }


        ((CustomRecyclerViewAdapterCarMake)recyclerView.getAdapter()).setOnItemClickListener(new CustomRecyclerViewAdapterCarMake.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {

                if (dialog != null)
                {
                    dialog.cancel();
                }

                Make make = ((CustomRecyclerViewAdapterCarMake) recyclerView.getAdapter()).getItem(position);

                etCarMake.setText(make.make);

                etCarMake.setTag(make.id);


            }


        });


    }

    private void showCarModelListDialog(ArrayList<Model> modelArrayList)
    {



        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.country_code_dialog);

        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.my_recycler_view);

        Button close = (Button) dialog.findViewById(R.id.button);

        EditText search = (EditText) dialog.findViewById(R.id.search_button);

        setCarModelListData(recyclerView,modelArrayList,dialog);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        search.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count)
            {

                ((CustomRecyclerViewAdapterCarModel)recyclerView.getAdapter()).getFilter().filter(charSequence);


            }


            @Override
            public void afterTextChanged(Editable s)
            {

            }


        });


        dialog.show();


    }


    private void setCarModelListData(final RecyclerView recyclerView, ArrayList<Model> modelArrayList, final Dialog dialog)
    {




        dialog.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {

                if(TextUtils.isEmpty(etCarModel.getText()))
                {

                    etCarModel.setText(" ");



                }

                etCarModel.clearFocus();



            }



        });



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);

        if (modelArrayList != null)
        {
            CustomRecyclerViewAdapterCarModel customRecyclerViewAdapterCarModel = new CustomRecyclerViewAdapterCarModel(modelArrayList);

            recyclerView.setAdapter(customRecyclerViewAdapterCarModel);

        }
        else
        {
            recyclerView.setVisibility(View.GONE);
        }


        ((CustomRecyclerViewAdapterCarModel)recyclerView.getAdapter()).setOnItemClickListener(new CustomRecyclerViewAdapterCarModel.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {

                if (dialog != null)
                {
                    dialog.cancel();
                }

                etCarModel.setText(((CustomRecyclerViewAdapterCarModel)recyclerView.getAdapter()).getItem(position).model);

                etCarModel.setTag(((CustomRecyclerViewAdapterCarModel)recyclerView.getAdapter()).getItem(position).id);


            }


        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

       Common.Log.i("? - requestCode : "+requestCode);

       Common.Log.i("? - resultCode : "+resultCode);

       Common.Log.i("? - data : "+data);


        if (resultCode != RESULT_OK) {

            return;
        }



        if(data == null)
        {

            super.onActivityResult(requestCode, resultCode, data);

            return;

        }

        Common.Log.i("? - data.toString() : "+data.toString());

        switch (requestCode)
        {

            case REQUEST_CODE_GALLERY:
                try
                {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
                    copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();

                    startCropImage();

                }
                catch (Exception e)
                {

                    //Log.e(LOG, "Error while creating temp file", e);
                }

                break;

            case REQUEST_CODE_TAKE_PICTURE:

                startCropImage();

                break;


            case REQUEST_CODE_CROP_IMAGE:

                String path = data.getStringExtra(FreeHandCroppingActivityAddVehicle.IMAGE_PATH);

                Common.Log.i("? - path : "+path);

                if (path == null)
                {

                    Common.customToast(this,"Unable to get the Cropped Image Path.");
                    return;
                }


                tvSelectImg.setText(mFileTemp.getName());

                tvSelectImg.setTextColor(getResources().getColor(R.color.green));

                break;

        }

        super.onActivityResult(requestCode, resultCode, data);




    }


    private void startCropImage()
    {

        Intent intent=new Intent(this,FreeHandCroppingActivityAddVehicle.class);
        intent.putExtra("uri", Uri.fromFile(mFileTemp));
        startActivityForResult(intent,REQUEST_CODE_CROP_IMAGE);

    }


    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    private void takePicture() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            Uri mImageCaptureUri = null;
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                mImageCaptureUri = Uri.fromFile(mFileTemp);
            } else {
                /*
	        	 * The solution is taken from here: http://stackoverflow.com/questions/10042695/how-to-get-camera-result-as-a-uri-in-data-folder
	        	 */
                mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
            }
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
        } catch (ActivityNotFoundException e) {

            Log.d("image", "cannot take picture", e);
        }

    }
    private void openGallery() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
    }

    public void callUpdateProfileApi(String vehicle_no, String make_id, String model_id,  String registration_expires,String color,String reg_no)
    {


        HashMap<String, String> hashMap = new HashMap();
        hashMap.put(ServiceUrls.ApiRequestParams.VEHICLE_NO, vehicle_no);
        hashMap.put(ServiceUrls.ApiRequestParams.MAKE_ID, make_id);
        hashMap.put(ServiceUrls.ApiRequestParams.MODEL_ID, model_id);
        hashMap.put(ServiceUrls.ApiRequestParams.REGISTRATION_EXPIRES, registration_expires);
        hashMap.put(ServiceUrls.ApiRequestParams.COLOR, color);
        hashMap.put(ServiceUrls.ApiRequestParams.REG_NO, reg_no);

        HashMap<String, TypedFile> fileHashMap = new HashMap<>();

        if(mFileTemp != null)
        {
            Log.d("profilePicFile",mFileTemp.getPath()+"\n"+mFileTemp.length());
            fileHashMap.put(ServiceUrls.MultiPartRequestParams.IMAGE, Common.getTypedFile(new File(mFileTemp.getPath())));

        }


        new RetrofitRequester(this).sendMultiPartRequest(ServiceUrls.RequestNames.ADD_CARS, hashMap,fileHashMap);


    }



}
