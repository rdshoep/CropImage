package com.rdshoep.android.image;
/*
 * @description
 *   Please write the ImageSelector module's description
 * @author Zhang (rdshoep@126.com)
 *   http://www.rdshoep.com/
 * @version 
 *   1.0.0(12/28/2015)
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;

public class ImageSelector implements ImageSelectCallback {

    static final int MAX_IMAGE_SIZE = 1024 * 1024 * 2; //2M

    private ImageSelectImpl _impl;
    private SourceSelector _sourceSelector;
    private ImageSelectCallback imageSelectCallback;
    private Context context;
    private ProgressView progressView = EmptyProgress._instance;

    public ImageSelector(Activity activity) {
        context = activity;

        init();

        _impl.setContainer(activity);
    }

    public ImageSelector(Fragment fragment) {
        context = fragment.getContext();

        init();

        _impl.setContainer(fragment);
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            _impl = new ImageSelectKitKatImpl();
        } else {
            _impl = new ImageSelectIceCreamImpl();
        }
        _sourceSelector = new DialogSourceSelector();
    }

    public ImageSelector setProgressView(ProgressView progressView) {
        this.progressView = progressView == null ? EmptyProgress._instance : progressView;
        return this;
    }

    public void selectImage(final int requestCode, ImageSource mode, final int maxSize
            , final ImageSelectCallback callback) {
        _sourceSelector.selectImageSource(context, mode, new SourceSelectCallback() {
            @Override
            public void onImageSourceSelected(ImageSource imageSource) {
                switch (imageSource) {
                    case CAMERA:
                        selectImageFromCamera(requestCode, maxSize, callback);
                        break;
                    case ALBUM:
                        selectImageFromAlbum(requestCode, maxSize, callback);
                        break;
                }
            }
        });
    }

    public void selectImageFromCamera(int requestCode, int maxSize, ImageSelectCallback callback) {
        imageSelectCallback = callback;
        _impl.selectImageFromCamera(requestCode, maxSize < 0 ? MAX_IMAGE_SIZE : maxSize, this);
    }

    public void selectImageFromAlbum(int requestCode, int maxSize, ImageSelectCallback callback) {
        imageSelectCallback = callback;
        _impl.selectImageFromAlbum(requestCode, maxSize < 0 ? MAX_IMAGE_SIZE : maxSize, this);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (_impl.onActivityResult(requestCode, resultCode, data)) {
            progressView.showProgress();
        }
    }

    public void setCopyEnabled(boolean copyEnabled) {
        _impl.setCopyEnable(copyEnabled);
    }

    @Override
    public void onImageSelected(int requestCode, Uri targetUri) {
        if (imageSelectCallback != null) {
            imageSelectCallback.onImageSelected(requestCode, targetUri);
            progressView.hideProgress();
        }
    }

    enum ContainerType {
        NONE, ACTIVITY, FRAGMENT
    }
}
