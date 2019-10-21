package com.tatx.partnerapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.commonutills.Common;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

/**
 * Created by Home on 22-09-2016.
 */
public class FreeHandCroppingActivityAddVehicle extends AppCompatActivity implements CropImageView.OnCropImageCompleteListener
{
    public static final String IMAGE_PATH = "imagePath";
    private CropImageView cropImageView;

    private Uri uri;

    private Button crop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_free_hand_croping_add_vehicle);

        cropImageView = (CropImageView) findViewById(R.id.cropImageView);

        crop = (Button) findViewById(R.id.crop);

        uri = (Uri) getIntent().getParcelableExtra("uri");

        Log.d("uri", uri.toString());

        cropImageView.setImageUriAsync(uri);

        cropImageView.setOnCropImageCompleteListener(this);

        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImageView.getCroppedImageAsync();

            }
        });

        Common.Log.i("? - Inside FHCAAV onCreate().");

    }


    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result)
    {
        //cropImageView.getCroppedImageAsync();
        cropImageView.setOnCropImageCompleteListener(null);

        handleCropResult(result);

        Common.Log.i("? - Inside FHCAAV onCropImageComplete()."+result);


    }

    private void handleCropResult(CropImageView.CropResult result)
    {

        Common.Log.i("? - result.getError() : "+result.getError());

        if (result.getError() == null)
        {
            Intent intent = new Intent();

            intent.putExtra("SAMPLE_SIZE", result.getSampleSize());


            Common.Log.i("? - result.getSampleSize() : "+result.getSampleSize());

            Common.Log.i("? - result.getUri() : "+result.getUri());


            if (result.getUri() != null)
            {
                intent.putExtra("URI", result.getUri());
            }
            else
            {
//                RegistrationActivity3.mImage = cropImageView.getCropShape() == CropImageView.CropShape.OVAL ? CropImage.toOvalBitmap(result.getBitmap()) : result.getBitmap();
                Common.Log.i("? - result.getBitmap() : "+result.getBitmap());

                Bitmap bitmap = result.getBitmap();

                File file = Common.saveBitmap(bitmap, uri.getPath());

                intent.putExtra(IMAGE_PATH, file.getPath());

            }

            setResult(Activity.RESULT_OK, intent);

            finish();

        }
        else
        {
            Log.e("AIC", "Failed to crop image", result.getError());
            Toast.makeText(this, Common.getStringResourceText(R.string.image_crop_failed) + result.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
