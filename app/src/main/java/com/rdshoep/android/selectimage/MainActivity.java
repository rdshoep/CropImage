package com.rdshoep.android.selectimage;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.rdshoep.android.image.CropImageCallback;
import com.rdshoep.android.image.DialogProgress;
import com.rdshoep.android.image.ImageCropper;
import com.rdshoep.android.image.ImageSelectCallback;
import com.rdshoep.android.image.ImageSelector;
import com.rdshoep.android.image.ImageSource;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
        , ImageSelectCallback, CropImageCallback {

    ImageCropper imageCropper;
    ImageSelector imageSelector;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageCropper = new ImageCropper(this);
        imageSelector = new ImageSelector(this);
        imageSelector.setProgressView(DialogProgress.newInstance(this));

        findViewById(android.R.id.button1).setOnClickListener(this);
        findViewById(android.R.id.button2).setOnClickListener(this);
        imageView = (ImageView) findViewById(android.R.id.icon);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case android.R.id.button1:
                imageSelector.selectImage(1, ImageSource.ALL, 0, this);
                break;
            case android.R.id.button2:
                imageCropper.cropImage(2, ImageSource.ALL, -1, this);
                break;
        }
    }

    @Override
    public void onImageSelected(int requestCode, Uri targetUri) {
        Toast.makeText(this, targetUri.getPath(), Toast.LENGTH_SHORT).show();
        imageView.setImageURI(targetUri);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        imageSelector.onActivityResult(requestCode, resultCode, data);
        imageCropper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Bundle setCropParams(int requestCode) {
        return null;
    }

    @Override
    public void onCropEnd(int requestCode, Uri targetUri) {
        Toast.makeText(this, targetUri.getPath(), Toast.LENGTH_SHORT).show();
        imageView.setImageURI(targetUri);
    }
}
