package com.tatx.partnerapp.menuactivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tatx.partnerapp.R;
import com.tatx.partnerapp.abstractclasses.BaseActivity;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;
import com.tatx.partnerapp.constants.ServiceUrls;
import com.tatx.partnerapp.customviews.AlertDialogForZoomImg;
import com.tatx.partnerapp.customviews.CustomerAlertDialog;
import com.tatx.partnerapp.dataset.Document;
import com.tatx.partnerapp.dataset.DocumentsListVo;
import com.tatx.partnerapp.interfaces.DialogClickListener;
import com.tatx.partnerapp.network.RetrofitRequester;
import com.tatx.partnerapp.network.RetrofitResponseListener;
import com.tatx.partnerapp.pojos.ApiResponseVo;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.mime.TypedFile;

/**
 * Created by user on 20-05-2016.
 */
public class DocumentsActivity extends BaseActivity implements RetrofitResponseListener{

    private GridLayoutManager gridLayoutManager;

    @BindView(R.id.documents_recycler_view)
    RecyclerView documentsRecyclerView;
    @BindView(R.id.recordnotfound) TextView recordnotfnd;
    private DocumentsListAdapterCopy documentsListAdapter;
    private File mFileTemp;
    private Uri mCropImageUri;
    private ImageView imageView;
    private String documentId;
    private double cabId;
    private AlertDialogForZoomImg alertDialogForZoomImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);
        ButterKnife.bind(this);
        setTitleText(Common.getStringResourceText(R.string.documents));


        new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.GET_DRIVER_DOCUMENTS,null);




    }




    public void setListOnAdapter(final List<Document> documentList) {
        gridLayoutManager = new GridLayoutManager(this, 2);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen._1sdp);
        documentsRecyclerView.addItemDecoration(itemDecoration);
        documentsRecyclerView.setLayoutManager(gridLayoutManager);

        if (documentList.size() != 0) {
            recordnotfnd.setVisibility(View.GONE);
            documentsListAdapter = new DocumentsListAdapterCopy(this, documentList);
            documentsRecyclerView.setAdapter(documentsListAdapter);



            documentsListAdapter.notifyDataSetChanged();

        } else if (documentList == null||documentList.size()==0) {

            recordnotfnd.setVisibility(View.VISIBLE);
        }


    }


    @Override
    public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId) {
        if (apiResponseVo.code != Constants.SUCCESS)
        {
            Common.customToast(this, apiResponseVo.status);
            return;
        }

        switch (apiResponseVo.requestname)
        {

            case ServiceUrls.RequestNames.GET_DRIVER_DOCUMENTS:

                DocumentsListVo documentsListVo = Common.getSpecificDataObject(apiResponseVo.data, DocumentsListVo.class);
                setListOnAdapter(documentsListVo.documents);

                break;


            case ServiceUrls.RequestNames.DRIVER_FILES:

                Common.customToast(this,"The Document was sent for review. It will update once approval done.");
                recreate();

                break;



        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
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

                mFileTemp=Common.savefile(this,result.getUri());

                HashMap<String, TypedFile> fileHashMap = new HashMap<>();

                // fileHashMap.put(ServiceUrls.MultiPartRequestParams.IMG_VEHICLEPHOTO, Common.getTypedFile(new File(img_vehicle)));
                fileHashMap.put(documentId, Common.getTypedFile(mFileTemp));

                HashMap<String, String> params = new HashMap<String, String>();
                params.put(ServiceUrls.ApiRequestParams.CAB_ID, String.valueOf(cabId));


                new RetrofitRequester(DocumentsActivity.this).sendMultiPartRequest(ServiceUrls.RequestNames.DRIVER_FILES, params, fileHashMap);


                imageView.setImageURI(result.getUri());
                // Toast.makeText(this, "Cropping successful, Sample: \n" + result.getSampleSize()+"result.getUri() \n "+result.getUri()+"result\n"+result.getBitmap()+"\nprofilePicFile\n"+profilePicFile.length(), Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }


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
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(this);
    }





    public class DocumentsListAdapterCopy extends RecyclerView.Adapter<DocumentsListAdapterCopy.ViewHolder> {
        Context context;
        List<Document> documentsList;
        String code;

        public DocumentsListAdapterCopy(Context context, List<Document> documentsList) {
            this.context = context;
            this.documentsList = documentsList;

        }

        @Override
        public int getItemCount() {
            // return productDetailses.size();
            return (null != documentsList ? documentsList.size() : 0);
        }


        /********* Create a holder Class to contain inflated xml file elements *********/
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_example, viewGroup, false);
            ViewHolder vh = new ViewHolder(v);
            v.setTag(i);
            return vh;
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            @BindView(R.id.title)
            TextView title;
            @BindView(R.id.document_image)
            ImageView image;
            @BindView(R.id.iv_edit_documents)
            ImageView ivEditDocuments;

            public ViewHolder(final View vi)
            {
                super(vi);
                ButterKnife.bind(this,vi);
            }


        }
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            Log.d("documentsList",documentsList.get(position).doc);
            holder.title.setText(documentsList.get(position).docName);
            Picasso.with(context).load(documentsList.get(position).doc).resize((int)Common.getDimensionResourceValue(R.dimen._150sdp),(int)Common.getDimensionResourceValue(R.dimen._150sdp)).memoryPolicy(MemoryPolicy.NO_CACHE )
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(holder.image);
            //          holder.image.setBackgroundResource(R.drawable.image_background);
            holder.image.setPadding(3,3,3,3);


            if (documentsList.get(position).isUnderReview)
            {
                holder.ivEditDocuments.setVisibility(View.GONE);
            }


            holder.ivEditDocuments.setOnClickListener(new View.OnClickListener() {
                public CustomerAlertDialog alertDialog;

                @Override
                public void onClick(View v) {


                    alertDialog = new CustomerAlertDialog(DocumentsActivity.this, "Your document will update after approval.\nDo You Want to Continue.....", new DialogClickListener() {
                        @Override
                        public void onCancelClick() {
                            alertDialog.dismiss();
                        }

                        @Override
                        public void onOkClick()
                        {

                            alertDialog.dismiss();

                            imageView=holder.image;

                            documentId="img_"+documentsList.get(position).docId;

                            cabId=documentsList.get(position).cabId;

                            Log.d("documentId",documentId+"cabId "+cabId);

                            CropImage.startPickImageActivity(DocumentsActivity.this);





                        }

                    });



                    alertDialog.show();



                }
            });

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent=new Intent(DocumentsActivity.this,ZoomActivity.class);
//                    intent.putExtra("doc",documentsList.get(position).doc);
//                    startActivity(intent);


                   alertDialogForZoomImg=new AlertDialogForZoomImg(DocumentsActivity.this, documentsList.get(position).doc, new AlertDialogForZoomImg.DialogClickListener() {
                        @Override
                        public void onOkClick() {

                            if (alertDialogForZoomImg!=null) {
                                alertDialogForZoomImg.cancel();
                            }
                        }
                    });


                    alertDialogForZoomImg.show();
                }
            });



        }

    }

}
