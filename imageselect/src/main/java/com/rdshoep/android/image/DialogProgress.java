package com.rdshoep.android.image;
/*
 * @description
 *   Please write the ProgressDialog module's description
 * @author Zhang (rdshoep@126.com)
 *   http://www.rdshoep.com/
 * @version 
 *   1.0.0(12/28/2015)
 */

import android.app.Dialog;
import android.content.Context;

public class DialogProgress implements ProgressView {

    public static ProgressView newInstance(Context context) {
        return new DialogProgress(context);
    }

    Context context;
    Dialog dialog;

    public DialogProgress(Context context) {
        this.context = context;
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_progress);
        dialog.setCancelable(false);
    }

    @Override
    public void showProgress() {
        dialog.show();
    }

    @Override
    public void hideProgress() {
        if (dialog != null && dialog.isShowing()) {
            dialog.hide();
        }
    }
}
