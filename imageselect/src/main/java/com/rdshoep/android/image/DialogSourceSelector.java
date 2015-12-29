package com.rdshoep.android.image;
/*
 * @description
 *   Please write the DialogSourceSelector module's description
 * @author Zhang (rdshoep@126.com)
 *   http://www.rdshoep.com/
 * @version 
 *   1.0.0(12/28/2015)
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

class DialogSourceSelector implements SourceSelector {
    @Override
    public void selectImageSource(Context context, ImageSource supportImageSource
            , final SourceSelectCallback callback) {
        if (supportImageSource == ImageSource.ALL) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setItems(R.array.image_sources, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            callback.onImageSourceSelected(ImageSource.CAMERA);
                            break;
                        case 1:
                            callback.onImageSourceSelected(ImageSource.ALBUM);
                            break;
                    }
                }
            }).show();
        } else {
            callback.onImageSourceSelected(supportImageSource);
        }
    }
}
