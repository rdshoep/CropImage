package com.rdshoep.android.image;
/*
 * @description
 *   Please write the ImageSelectKitKatImpl module's description
 * @author Zhang (rdshoep@126.com)
 *   http://www.rdshoep.com/
 * @version 
 *   1.0.0(12/28/2015)
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

@TargetApi(Build.VERSION_CODES.KITKAT)
class ImageSelectKitKatImpl implements ImageSelectImpl {
    ImageSelector.ContainerType containerType = ImageSelector.ContainerType.NONE;
    Activity activityContainer = null;
    Fragment fragmentContainer = null;

    int curRequestCode;
    int maxSize = ImageSelector.MAX_IMAGE_SIZE;
    Uri curTargetUri;
    ImageSelectCallback selectCallback;

    boolean copyEnabled = true;

    public ImageSelectKitKatImpl() {
    }

    public ImageSelectKitKatImpl(Activity activityContainer) {
        this.activityContainer = activityContainer;
        containerType = ImageSelector.ContainerType.ACTIVITY;
    }

    public ImageSelectKitKatImpl(Fragment fragmentContainer) {
        this.fragmentContainer = fragmentContainer;
        containerType = ImageSelector.ContainerType.FRAGMENT;
    }

    @Override
    public ImageSelectImpl setContainer(Activity activityContainer) {
        this.activityContainer = activityContainer;
        containerType = ImageSelector.ContainerType.ACTIVITY;
        return this;
    }

    @Override
    public ImageSelectImpl setContainer(Fragment fragmentContainer) {
        this.fragmentContainer = fragmentContainer;
        containerType = ImageSelector.ContainerType.FRAGMENT;
        return this;
    }

    @Override
    public void setCopyEnable(boolean enableCopy) {
        //在Kitkat及以上版本，无法裁剪系统图片，必须拷贝后方可使用
        //copyEnabled = enableCopy;
    }

    @Override
    public void selectImageFromCamera(int requestCode, int maxSize, ImageSelectCallback imageSelectCallback) {
        this.maxSize = maxSize;
        curRequestCode = requestCode;
        selectCallback = imageSelectCallback;
        dispatchTakePictureFromCameraIntent(requestCode);
    }

    @Override
    public void selectImageFromAlbum(int requestCode, int maxSize, ImageSelectCallback imageSelectCallback) {
        this.maxSize = maxSize;
        curRequestCode = requestCode;
        selectCallback = imageSelectCallback;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        switch (containerType) {
            case FRAGMENT:
                fragmentContainer.startActivityForResult(intent, requestCode);
                break;
            case ACTIVITY:
                activityContainer.startActivityForResult(intent, requestCode);
                break;
        }
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == curRequestCode) {
            if (resultCode == Activity.RESULT_OK) {
                new BackgroundTask<Uri>() {
                    @Override
                    public void onFinish(Uri data) {
                        if (selectCallback != null && data != null) {
                            selectCallback.onImageSelected(data);
                        }
                    }

                    @Override
                    public Uri doInBackgroundThread() {
                        Uri targetUri = curTargetUri;
                        if (data != null && data.getData() != null) {
                            targetUri = data.getData();
                        }

                        if (targetUri != null) {
                            if (maxSize > 0 || (copyEnabled && maxSize == 0)) {
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

    private Context getContext() {
        Context cxt = activityContainer;
        if (containerType == ImageSelector.ContainerType.FRAGMENT) {
            cxt = fragmentContainer.getContext();
        }
        return cxt;
    }

    private void dispatchTakePictureFromCameraIntent(int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            Uri imageUri = ImageHelper.createCameraImageUri(getContext(), null);
            curTargetUri = imageUri;
            if (imageUri != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            }
            switch (containerType) {
                case FRAGMENT:
                    fragmentContainer.startActivityForResult(takePictureIntent, requestCode);
                    break;
                case ACTIVITY:
                    activityContainer.startActivityForResult(takePictureIntent, requestCode);
                    break;
            }
        }
    }
}
