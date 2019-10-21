package com.tatx.partnerapp.activities;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.abstractclasses.BaseActivity;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.constants.ServiceUrls;
import com.tatx.partnerapp.cropimages.InternalStorageContentProvider;
import com.tatx.partnerapp.network.RetrofitRequester;
import com.tatx.partnerapp.network.RetrofitResponseListener;
import com.tatx.partnerapp.pojos.ApiResponseVo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.mime.TypedFile;

/**
 * Created by user on 20-05-2016.
 */
public class RegistrationActivity3 extends BaseActivity implements RetrofitResponseListener{
    private static final String LOG = RegistrationActivity3.class.getSimpleName();
    public static Bitmap mImage;
    public static final String IMAGE_DIRECTORY_NAME = "Tatx";
    //public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";

    public static final int REQUEST_CODE_GALLERY = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static final int REQUEST_CODE_CROP_IMAGE = 0x3;
    private File mFileTemp;

   // @BindView(R.id.car_picture) ImageView carPicture;
    @BindView(R.id.licence_picture) ImageView licencePicture;
    @BindView(R.id.img_iqama) ImageView imgIqamaPicture;
    @BindView(R.id.reg_picture) ImageView regPicture;
    @BindView(R.id.insurance_picture) ImageView insurancePicture;
    @BindView(R.id.img_ibanBankCard) ImageView imgIbanBankCard;
    @BindView(R.id.img_authorityToDriveCar) ImageView imgAuthorityToDriveCar;
    @BindView(R.id.iv_emp_type_document) ImageView ivEmpTypeDocument;
    @BindView(R.id.reg) Button reg;
    @BindView(R.id.ctv_authorityToDriveCar) TextView ctvAuthorityToDriveCar;
    @BindView(R.id.ctv_emp_type_header) TextView ctvEmpTypeHeader;

    int imageValue=0;
    private SharedPreferences.Editor editor;


    private long MAX_SIZE=2000000;
    private String img_license;
    private String img_iqama;
    private String img_vechreg;
  //  private String img_vehicle;
    private String img_insurance;
    private String img_ibanbankcard;
    private String img_authoritytodrivecar;
    private String ownerAuthority="1";
    private float bitmapHieght;
    private float bitmapWidth;
    private String img_ivEmpTypeDocument;
    private String empTypeHeaderText;
    private String policeClearance;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_3);
        ButterKnife.bind(this);
        setTitleText(Common.getStringResourceText(R.string.reg));

        initilaizedAll();




    }

    public void initilaizedAll()
    {

        ownerAuthority = getIntent().getStringExtra(Constants.IntentKeys.OWNER_AUTHORITY);
        policeClearance = getIntent().getStringExtra(Constants.IntentKeys.NATIONALITY);
        String empType = getIntent().getStringExtra(Constants.IntentKeys.EMPLOYEE_TYPE);


Log.d("ownerAuthority","ownerAuthority: "+ownerAuthority+"policeClearance: "+policeClearance);
        if (ownerAuthority.equalsIgnoreCase("1"))
        {
            imgAuthorityToDriveCar.setVisibility(View.GONE);
            ctvAuthorityToDriveCar.setVisibility(View.GONE);
        }
        if (policeClearance.equals(Constants.SAUDI_COUNTRY_CODE)){

            ctvEmpTypeHeader.setVisibility(View.VISIBLE);
            ivEmpTypeDocument.setVisibility(View.VISIBLE);
            empTypeHeaderText = empType.equalsIgnoreCase("0") ? Common.getStringResourceText(R.string.upload_police_clearance) : Common.getStringResourceText(R.string.upoad_employee_id_card);
            ctvEmpTypeHeader.setText(empTypeHeaderText);

        }else {

            ctvEmpTypeHeader.setVisibility(View.GONE);
            ivEmpTypeDocument.setVisibility(View.GONE);
        }





    }

    private void takePicture()
    {

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

    private void openGallery()
    {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
    }

    private void startCropImage()
    {

        Intent intent=new Intent(this,FreeHandCroppingActivity.class);
        intent.putExtra("uri", Uri.fromFile(mFileTemp));
        startActivityForResult(intent,REQUEST_CODE_CROP_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {

            return;
        }


        switch (requestCode) {
            case REQUEST_CODE_GALLERY:
                try {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
                    copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();

                    startCropImage();

                } catch (Exception e) {

                    Log.e(LOG, "Error while creating temp file", e);
                }

                break;
            case REQUEST_CODE_TAKE_PICTURE:

                startCropImage();
                break;


            case REQUEST_CODE_CROP_IMAGE:
                setImageOnView();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[11024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    private void showPictureialog() {
        LinearLayout cameraBtn;
        LinearLayout galleryBtn;
        final Dialog dialog = new Dialog(this);

        // Setting dialogview
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER_VERTICAL);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // window.setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setTitle(Common.getStringResourceText(R.string.profile_photo));
        dialog.setContentView(R.layout.capture_image_dailog);
        dialog.setCancelable(true);
        cameraBtn = (LinearLayout) dialog.findViewById(R.id.camera);
        galleryBtn = (LinearLayout) dialog.findViewById(R.id.gallery);
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mFileTemp = new File(Environment.getExternalStoragePublicDirectory(IMAGE_DIRECTORY_NAME), InternalStorageContentProvider.getOutputMediaFile(1));
        } else {
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
                // Common.customToast(getApplicationContext(),"Under development..",Common.TOAST_TIME);
            }
        });
        dialog.show();
    }



    public void setImageOnView()
    {
        Intent intent = getIntent();
        String state = Environment.getExternalStorageState();
        File file=null;
        if (Environment.MEDIA_MOUNTED.equals(state))
        {
            file = new File(Environment.getExternalStoragePublicDirectory(IMAGE_DIRECTORY_NAME), InternalStorageContentProvider.getOutputMediaFile(1));
        }
        else
        {
            file = new File(getFilesDir(), InternalStorageContentProvider.getOutputMediaFile(1));
        }

        if (mImage != null)
        {

            int sampleSize = intent.getIntExtra("SAMPLE_SIZE", 1);

            double ratio = ((int) (10 * mImage.getWidth() / (double) mImage.getHeight())) / 10d;

            int byteCount = 0;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR1)
            {
                byteCount = mImage.getByteCount() / 1024;
            }

            String desc = "(" + mImage.getWidth() + ", " + mImage.getHeight() + "), Sample: " + sampleSize + ", Ratio: " + ratio + ", Bytes: " + byteCount + "K";
            Log.d("? - sizes",desc);

            if (byteCount>MAX_SIZE)
            {
                Common.customToast(this, Common.getStringResourceText(R.string.file_should_not_be_more_than_2_mb));

                return;

            }
            int hieght = mImage.getHeight();
            int width = mImage.getWidth();
            Log.d("hieght","width "+width+"hieght "+hieght);
//            bitmapWidth=width*25/100;
//            bitmapHieght=hieght*25/100;


            if(hieght>3500){
                bitmapWidth=width*12/100;
                bitmapHieght=hieght*12/100;
            }
            else if(hieght>=3000&&hieght<=3500){
                bitmapWidth=width*14/100;
                bitmapHieght=hieght*14/100;
            }
            else if(hieght>=2500&&hieght<=3000){
                bitmapWidth=width*17/100;
                bitmapHieght=hieght*17/100;
            }
            else if(hieght>=2000&&hieght<=2500){
                bitmapWidth=width*21/100;
                bitmapHieght=hieght*21/100;
            }
            else if(hieght>=1500&&hieght<=2000){
                bitmapWidth=width*26/100;
                bitmapHieght=hieght*26/100;
            }else if(hieght>=1000&&hieght<=1500){
                bitmapWidth=width*36/100;
                bitmapHieght=hieght*36/100;
            }
            else if(hieght>=500&&hieght<=1000){
                bitmapWidth=width*50/100;
                bitmapHieght=hieght*50/100;
            }else {
                bitmapWidth=width*70/100;
                bitmapHieght=hieght*70/100;
            }
            Log.d("hieght","bitmapWidth "+bitmapWidth+"bitmapHieght "+bitmapHieght);
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(Math.round(bitmapWidth),Math.round(bitmapHieght));
            parms.topMargin=Math.round(getResources().getDimension(R.dimen._5sdp));
          //  bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());
           /* if (imageValue==1){
//                editor.putString(ServiceUrls.MultiPartRequestParams.IMG_VEHICLEPHOTO,Common.saveBitmap(mImage,file.getPath()).getPath());

                img_license =Common.saveBitmap(mImage,file.getPath()).getPath() ;
                carPicture.setLayoutParams(parms);
                carPicture.setImageBitmap(mImage);
                carPicture.setBackgroundResource(R.drawable.image_background);

            }else */
            if (imageValue==2){
//                editor.putString(ServiceUrls.MultiPartRequestParams.IMG_IQAMA,Common.saveBitmap(mImage,file.getPath()).getPath());
                img_license = Common.saveBitmap(mImage,file.getPath()).getPath();
                licencePicture.setLayoutParams(parms);
                licencePicture.setImageBitmap(mImage);
                licencePicture.setBackgroundResource(R.drawable.image_background);
            }else if (imageValue==3){
//                editor.putString(ServiceUrls.MultiPartRequestParams.IMG_VEHICLEREGISTRATION,Common.saveBitmap(mImage,file.getPath()).getPath());
                img_iqama = Common.saveBitmap(mImage,file.getPath()).getPath();
                imgIqamaPicture.setLayoutParams(parms);
                imgIqamaPicture.setImageBitmap(mImage);
                imgIqamaPicture.setBackgroundResource(R.drawable.image_background);
            }else if (imageValue==4){
//                editor.putString(ServiceUrls.MultiPartRequestParams.IMG_DRIVERLICENSE,Common.saveBitmap(mImage,file.getPath()).getPath());
                img_vechreg = Common.saveBitmap(mImage,file.getPath()).getPath();
                regPicture.setLayoutParams(parms);
                regPicture.setImageBitmap(mImage);
                regPicture.setBackgroundResource(R.drawable.image_background);
            }else if (imageValue==5){
//                editor.putString(ServiceUrls.MultiPartRequestParams.IMG_INSURANCE,Common.saveBitmap(mImage,file.getPath()).getPath());
                img_insurance = Common.saveBitmap(mImage,file.getPath()).getPath();
                insurancePicture.setLayoutParams(parms);
                insurancePicture.setImageBitmap(mImage);
                insurancePicture.setBackgroundResource(R.drawable.image_background);
            }else if (imageValue==6){
//                editor.putString(ServiceUrls.MultiPartRequestParams.IMG_IBANBANKCARD,Common.saveBitmap(mImage,file.getPath()).getPath());
                img_ibanbankcard = Common.saveBitmap(mImage,file.getPath()).getPath();
                imgIbanBankCard.setLayoutParams(parms);
                imgIbanBankCard.setImageBitmap(mImage);
                imgIbanBankCard.setBackgroundResource(R.drawable.image_background);
            }else if (imageValue==7){
//                editor.putString(ServiceUrls.MultiPartRequestParams.IMG_AUTHORITYTODRIVECAR,Common.saveBitmap(mImage,file.getPath()).getPath());
                img_authoritytodrivecar = Common.saveBitmap(mImage,file.getPath()).getPath();
                imgAuthorityToDriveCar.setLayoutParams(parms);
                imgAuthorityToDriveCar.setImageBitmap(mImage);
                imgAuthorityToDriveCar.setBackgroundResource(R.drawable.image_background);
            }else if (imageValue==8){
//                editor.putString(ServiceUrls.MultiPartRequestParams.IMG_AUTHORITYTODRIVECAR,Common.saveBitmap(mImage,file.getPath()).getPath());
                img_ivEmpTypeDocument = Common.saveBitmap(mImage,file.getPath()).getPath();
                ivEmpTypeDocument.setLayoutParams(parms);
                ivEmpTypeDocument.setImageBitmap(mImage);
                ivEmpTypeDocument.setBackgroundResource(R.drawable.image_background);
            }
//            editor.commit();

            //imageView.setImageBitmap(mImage);

        }
        else
        {
            /*


            Uri imageUri = intent.getParcelableExtra("URI");
            if (imageUri != null)
            {
               // imageView.setImageURI(imageUri);
                if (imageValue==1){
                    editor.putString(ServiceUrls.MultiPartRequestParams.IMG_VEHICLEPHOTO,Common.saveBitmap(mImage,file.getPath()).getPath());
                    carPicture.setImageURI(imageUri);
                }else if (imageValue==2){
                    editor.putString(ServiceUrls.MultiPartRequestParams.IMG_IQAMA,Common.saveBitmap(mImage,file.getPath()).getPath());
                    licencePicture.setImageURI(imageUri);
                }else if (imageValue==3){
                    editor.putString(ServiceUrls.MultiPartRequestParams.IMG_VEHICLEREGISTRATION,Common.saveBitmap(mImage,file.getPath()).getPath());
                    imgIqamaPicture.setImageURI(imageUri);
                }else if (imageValue==4){
                    editor.putString(ServiceUrls.MultiPartRequestParams.IMG_DRIVERLICENSE,Common.saveBitmap(mImage,file.getPath()).getPath());
                    regPicture.setImageURI(imageUri);
                }else if (imageValue==5){
                    editor.putString(ServiceUrls.MultiPartRequestParams.IMG_INSURANCE,Common.saveBitmap(mImage,file.getPath()).getPath());
                    insurancePicture.setImageURI(imageUri);
                }else if (imageValue==6){
                    editor.putString(ServiceUrls.MultiPartRequestParams.IMG_IBANBANKCARD,Common.saveBitmap(mImage,file.getPath()).getPath());
                    imgIbanBankCard.setImageURI(imageUri);
                }else if (imageValue==7){
                    editor.putString(ServiceUrls.MultiPartRequestParams.IMG_AUTHORITYTODRIVECAR,Common.saveBitmap(mImage,file.getPath()).getPath());
                    imgAuthorityToDriveCar.setImageURI(imageUri);
                }
                editor.commit();

            }
            else
            {
                Toast.makeText(this, "No image is set to show", Toast.LENGTH_LONG).show();
            }
        */


            Common.customToast(this,Common.getStringResourceText(R.string.unable_to_fetch_image));


        }


    }




   /* @OnClick(R.id.car_picture) void  setCarPicture(){
        imageValue=1;
        showPictureialog();
    }*/
    @OnClick(R.id.licence_picture) void setLicencePicture(){
        imageValue=2;
        showPictureialog();
    }
    @OnClick(R.id.img_iqama) void setIdCardPicture(){
        imageValue=3;
        showPictureialog();
    }
    @OnClick(R.id.reg_picture) void setRegPicture(){
        imageValue=4;
        showPictureialog();
    }
    @OnClick(R.id.insurance_picture) void setInsurancePicture(){
        imageValue=5;
        showPictureialog();
    }
    @OnClick(R.id.img_ibanBankCard) void setImgIbanBankCard(){
        imageValue=6;
        showPictureialog();
    }
    @OnClick(R.id.img_authorityToDriveCar) void setImgAuthorityToDriveCar(){
        imageValue=7;
        showPictureialog();
    }
    @OnClick(R.id.iv_emp_type_document) void setImgEmployeeType(){
        imageValue=8;
        showPictureialog();
    }

    @OnClick(R.id.reg)
    void callRegistrationAPI() {

        Log.d("ownerAuthority","ownerAuthority: "+ownerAuthority);
        /*if (img_vehicle == null) {
            requestChildFocus(carPicture);
            Common.customToast(this, Common.getStringResourceText(R.string.please_attach_vehicle_picture), Common.TOAST_TIME);
        } else if (new File(img_vehicle).length() > MAX_SIZE) {
            requestChildFocus(carPicture);
            Common.customToast(this, Common.getStringResourceText(R.string.file_should_not_be_more_than_2_mb), Common.TOAST_TIME);
        } else*/
        if (img_license == null) {
            requestChildFocus(licencePicture);
            Common.customToast(this, Common.getStringResourceText(R.string.please_attach_drivers_license), Common.TOAST_TIME);
        } else if (new File(img_license).length() > MAX_SIZE) {
            requestChildFocus(licencePicture);
            Common.customToast(this, Common.getStringResourceText(R.string.file_should_not_be_more_than_2_mb), Common.TOAST_TIME);
        } else if (img_insurance == null) {
            requestChildFocus(insurancePicture);
            Common.customToast(this, Common.getStringResourceText(R.string.please_attach_insurance_picture), Common.TOAST_TIME);
        } else if (new File(img_insurance).length() > MAX_SIZE) {
            requestChildFocus(insurancePicture);
            Common.customToast(this, Common.getStringResourceText(R.string.file_should_not_be_more_than_2_mb), Common.TOAST_TIME);
        } else if (img_iqama == null) {
            requestChildFocus(imgIqamaPicture);
            Common.customToast(this, Common.getStringResourceText(R.string.please_attach_id_card), Common.TOAST_TIME);
        } else if (new File(img_iqama).length() > MAX_SIZE) {
            requestChildFocus(imgIqamaPicture);
            Common.customToast(this, Common.getStringResourceText(R.string.file_should_not_be_more_than_2_mb), Common.TOAST_TIME);
        } else if (img_vechreg == null) {
            requestChildFocus(regPicture);
            Common.customToast(this, Common.getStringResourceText(R.string.please_attach_vehicle_registration), Common.TOAST_TIME);
        } else if (new File(img_vechreg).length() > MAX_SIZE) {
            requestChildFocus(regPicture);
            Common.customToast(this, Common.getStringResourceText(R.string.file_should_not_be_more_than_2_mb), Common.TOAST_TIME);
        } else if (img_ibanbankcard == null) {
            requestChildFocus(imgIbanBankCard);
            Common.customToast(this, Common.getStringResourceText(R.string.please_attach_iban_bank_card_picture), Common.TOAST_TIME);
        } else if (new File(img_ibanbankcard).length() > MAX_SIZE) {
            requestChildFocus(imgIbanBankCard);
            Common.customToast(this, Common.getStringResourceText(R.string.file_should_not_be_more_than_2_mb), Common.TOAST_TIME);
        }
        else if (img_authoritytodrivecar == null && ownerAuthority.equalsIgnoreCase("0") ) {
            requestChildFocus(imgAuthorityToDriveCar);
            Common.customToast(this, Common.getStringResourceText(R.string.please_attach_authority_latter), Common.TOAST_TIME);
        }
        else if (img_authoritytodrivecar != null && new File(img_authoritytodrivecar).length() > MAX_SIZE) {
            requestChildFocus(imgAuthorityToDriveCar);
            Common.customToast(this, Common.getStringResourceText(R.string.file_should_not_be_more_than_2_mb), Common.TOAST_TIME);
        }
        else if (img_ivEmpTypeDocument == null && policeClearance.equals(Constants.SAUDI_COUNTRY_CODE) ) {
            requestChildFocus(ivEmpTypeDocument);
            Common.customToast(this,empTypeHeaderText , Common.TOAST_TIME);
        }
        else if (img_ivEmpTypeDocument != null && new File(img_ivEmpTypeDocument).length() > MAX_SIZE) {
            requestChildFocus(ivEmpTypeDocument);
            Common.customToast(this, Common.getStringResourceText(R.string.file_should_not_be_more_than_2_mb), Common.TOAST_TIME);
        } else {

            Common.Log.i("? - sizes"
                    + "img_license : "+img_license
                    + "\n " + img_iqama
                    + "\n " + img_vechreg
                    + "\n " + img_insurance
                    +"\n " + img_ibanbankcard
                    + "\n "+img_authoritytodrivecar
                    + "\n "+img_ivEmpTypeDocument);

            //callUpdateProfileApi(img_license,img_idcard,img_vechreg,img_vehicle,img_insurance);

            HashMap<String, TypedFile> fileHashMap = new HashMap<>();

           // fileHashMap.put(ServiceUrls.MultiPartRequestParams.IMG_VEHICLEPHOTO, Common.getTypedFile(new File(img_vehicle)));
            fileHashMap.put(ServiceUrls.MultiPartRequestParams.IMG_DRIVERLICENSE, Common.getTypedFile(new File(img_license)));
            fileHashMap.put(ServiceUrls.MultiPartRequestParams.IMG_INSURANCE, Common.getTypedFile(new File(img_insurance)));
            fileHashMap.put(ServiceUrls.MultiPartRequestParams.IMG_IQAMA, Common.getTypedFile(new File(img_iqama)));
            fileHashMap.put(ServiceUrls.MultiPartRequestParams.IMG_VEHICLEREGISTRATION, Common.getTypedFile(new File(img_vechreg)));
            fileHashMap.put(ServiceUrls.MultiPartRequestParams.IMG_IBANBANKCARD, Common.getTypedFile(new File(img_ibanbankcard)));
            if (img_ivEmpTypeDocument!=null) {
                fileHashMap.put(ServiceUrls.MultiPartRequestParams.IMG_EMPLOYEE_ID_OR_PC, Common.getTypedFile(new File(img_ivEmpTypeDocument)));
            }
            if (img_authoritytodrivecar != null) {
                fileHashMap.put(ServiceUrls.MultiPartRequestParams.IMG_AUTHORITYTODRIVECAR, Common.getTypedFile(new File(img_authoritytodrivecar)));
            }


            HashMap<String, String> params = new HashMap<String, String>();
            params.put(ServiceUrls.ApiRequestParams.CAB_ID,getIntent().getStringExtra(Constants.IntentKeys.CAB_ID));

            Common.Log.i("? - getIntent().getStringExtra(Constants.IntentKeys.CAB_ID) : "+getIntent().getStringExtra(Constants.IntentKeys.CAB_ID));


            new RetrofitRequester(RegistrationActivity3.this).sendMultiPartRequest(ServiceUrls.RequestNames.DRIVER_FILES, params, fileHashMap);


        }
    }

    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId) {
        if (apiResponseVo.code != Constants.SUCCESS) {
            Common.customToast(this, apiResponseVo.status);
            return;
        }

        switch (apiResponseVo.requestname) {
            case ServiceUrls.RequestNames.DRIVER_FILES:
                Intent intent = new Intent(this, GoogleMapDrawerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }

    private void requestChildFocus(View view){
        view.requestFocus();
        view.getParent().requestChildFocus(view,view);
    }
}
