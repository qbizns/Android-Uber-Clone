package com.tatx.partnerapp.activities;

import android.app.Activity;
import android.content.Intent;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

/**
 * Created by Home on 22-09-2016.
 */
public class FreeHandCroppingActivity extends AppCompatActivity implements CropImageView.OnCropImageCompleteListener
{
    private CropImageView cropImageView;

    private Uri uri;

    private Button crop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_hand_croping);
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
    }


    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
        //cropImageView.getCroppedImageAsync();
        cropImageView.setOnCropImageCompleteListener(null);
        handleCropResult(result);
        Log.d("uri", "onCropImageComplete" + result);
    }

    private void handleCropResult(CropImageView.CropResult result) {
        if (result.getError() == null) {
            Intent intent = new Intent(this, RegistrationActivity3.class);
            intent.putExtra("SAMPLE_SIZE", result.getSampleSize());

            if (result.getUri() != null) {
                intent.putExtra("URI", result.getUri());
            } else {

                RegistrationActivity3.mImage = cropImageView.getCropShape() == CropImageView.CropShape.OVAL ? CropImage.toOvalBitmap(result.getBitmap()) : result.getBitmap();
            }

            setResult(Activity.RESULT_OK, intent);

            finish();

        } else {
            Log.e("AIC", "Failed to crop image", result.getError());
            Toast.makeText(this, Common.getStringResourceText(R.string.image_crop_failed) + result.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
