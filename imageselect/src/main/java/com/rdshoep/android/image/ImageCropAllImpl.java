package com.rdshoep.android.image;
/*
 * @description
 *   Please write the ImageCropAllImpl module's description
 * @author Zhang (rdshoep@126.com)
 *   http://www.rdshoep.com/
 * @version 
 *   1.0.0(12/28/2015)
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

class ImageCropAllImpl implements ImageCropImpl {

    Activity activityContainer;
    Fragment fragmentContainer;

    Uri tempUri;
    int requestCode;
    int maxSize;
    CropImageCallback cropImageCallback;

    public ImageCropAllImpl(Activity activityContainer) {
        this.activityContainer = activityContainer;
    }

    public ImageCropAllImpl(Fragment fragmentContainer) {
        this.fragmentContainer = fragmentContainer;
    }

    private Context getContext() {
        return fragmentContainer == null ? activityContainer : fragmentContainer.getContext();
    }

    @Override
    public void cropImage(int requestCode, Uri uri, int maxSize, CropImageCallback callback) {
        this.requestCode = requestCode;
        this.maxSize = maxSize;
        this.cropImageCallback = callback;

        final Intent intent = new Intent();
        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        if (callback != null) {
            Bundle bundle = cropImageCallback.setCropParams(ImageCropAllImpl.this.requestCode);
            if (bundle == null) {
                bundle = ImageCropper.addCropExtras(null, 0, 0, 0, 0);
            }

            if (!bundle.containsKey(MediaStore.EXTRA_OUTPUT)) {
                bundle.putParcelable(MediaStore.EXTRA_OUTPUT, ImageHelper
                        .createCameraImageUri(getContext(), null));
            }
            intent.putExtras(bundle);

            tempUri = bundle.getParcelable(MediaStore.EXTRA_OUTPUT);

            if (activityContainer != null) {
                activityContainer.startActivityForResult(intent, ImageCropAllImpl.this.requestCode);
            } else {
                fragmentContainer.startActivityForResult(intent, ImageCropAllImpl.this.requestCode);
            }
        }
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == this.requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                new BackgroundTask<Uri>() {
                    @Override
                    public void onFinish(Uri data) {
                        if (cropImageCallback != null && data != null) {
                            cropImageCallback.onCropEnd(data);
                        }
                    }

                    @Override
                    public Uri doInBackgroundThread() {
                        Uri targetUri = tempUri;
                        if (data != null && data.getData() != null) {
                            targetUri = data.getData();
                        }

                        if (targetUri != null) {
                            if (maxSize > 0) {
                                targetUri = ImageHelper.compressImage(getContext(), targetUri, maxSize);
                            }
                        }
                        return targetUri;
                    }
                }.execute();
                return true;
            }
        }
        return false;
    }
}
