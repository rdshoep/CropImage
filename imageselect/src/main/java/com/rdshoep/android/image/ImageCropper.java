package com.rdshoep.android.image;
/*
 * @description
 *   Please write the ImageCroper module's description
 * @author Zhang (rdshoep@126.com)
 *   http://www.rdshoep.com/
 * @version 
 *   1.0.0(12/28/2015)
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class ImageCropper implements ImageSelectCallback, CropImageCallback {
    static final int MAX_IMAGE_SIZE = 1024 * 1024 * 2; //2M
    static final int CROP_OFFSET = 12345;

    private int requestCode;
    private ImageSelector _imageSelector;
    private Context context;
    CropImageCallback cropImageCallback;
    private ImageCropImpl _impl;
    private int maxSize = MAX_IMAGE_SIZE;
    ProgressView progressView = EmptyProgress._instance;

    public ImageCropper(Activity activity) {
        context = activity;
        _imageSelector = new ImageSelector(activity);
        _impl = new ImageCropAllImpl(activity);

        init();
    }

    public ImageCropper(Fragment fragment) {
        context = fragment.getContext();
        _imageSelector = new ImageSelector(fragment);
        _impl = new ImageCropAllImpl(fragment);

        init();
    }

    private void init() {
        _imageSelector.setCopyEnabled(false);
        progressView = new DialogProgress(context);
        setProgressView(progressView);
    }

    public ImageCropper setProgressView(ProgressView progressView) {
        this.progressView = progressView == null ? EmptyProgress._instance : progressView;
        _imageSelector.setProgressView(this.progressView);
        return this;
    }

    private int calcSelectCodeByCrop(int cropRequestCode) {
        return cropRequestCode + CROP_OFFSET;
    }

    public void cropImage(final int requestCode, ImageSource mode, final int maxSize
            , CropImageCallback callback) {
        this.requestCode = requestCode;
        cropImageCallback = callback;
        this.maxSize = maxSize < 0 ? MAX_IMAGE_SIZE : maxSize;
        _imageSelector.selectImage(calcSelectCodeByCrop(requestCode), mode, 0, this);
    }

    public void cropImageFromCamera(int requestCode, int maxSize, CropImageCallback callback) {
        this.requestCode = requestCode;
        cropImageCallback = callback;
        this.maxSize = maxSize < 0 ? MAX_IMAGE_SIZE : maxSize;
        _imageSelector.selectImageFromCamera(calcSelectCodeByCrop(requestCode), 0, this);
    }

    public void cropImageFromAlbum(int requestCode, int maxSize, CropImageCallback callback) {
        this.requestCode = requestCode;
        cropImageCallback = callback;
        this.maxSize = maxSize < 0 ? MAX_IMAGE_SIZE : maxSize;
        _imageSelector.selectImageFromAlbum(calcSelectCodeByCrop(requestCode), 0, this);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == this.requestCode) {
            if (_impl.onActivityResult(requestCode, resultCode, data)) {
                progressView.showProgress();
            }
        } else if (requestCode == calcSelectCodeByCrop(this.requestCode)) {
            _imageSelector.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void cropImageFromUri(int requestCode, Uri resourceUri, int maxSize, CropImageCallback callback) {
        this.requestCode = requestCode;
        cropImageCallback = callback;
        this.maxSize = maxSize < 0 ? MAX_IMAGE_SIZE : maxSize;
        _impl.cropImage(this.requestCode, resourceUri, this.maxSize, this);
    }

    @Override
    public void onImageSelected(Uri targetUri) {
        _impl.cropImage(this.requestCode, targetUri, this.maxSize, this);
    }

    public static Bundle addCropExtras(Bundle extra, int aspectX, int aspectY
            , int outputX, int outputY) {
        if (extra == null) {
            extra = new Bundle();
        }

        if (aspectX > 0 && aspectY > 0) {
            extra.putInt("aspectX", aspectX);
            extra.putInt("aspectY", aspectY);
        }
        if (outputX > 0 && outputY > 0) {
            extra.putInt("outputX", outputX);
            extra.putInt("outputY", outputY);
        }

        extra.putString("crop", "true");
        extra.putBoolean("scale", true);
//        extra.putBoolean("return-data", true);
        extra.putString("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        extra.putBoolean("noFaceDetection", true); // no face detection
        return extra;
    }

    @Override
    public Bundle setCropParams(int requestCode) {
        return null;
    }

    @Override
    public void onCropEnd(Uri targetUri) {
        if (cropImageCallback != null) {
            cropImageCallback.onCropEnd(targetUri);
            progressView.hideProgress();
        }
    }
}
