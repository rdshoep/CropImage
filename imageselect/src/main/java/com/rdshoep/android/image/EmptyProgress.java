package com.rdshoep.android.image;
/*
 * @description
 *   Please write the EmptyProgress module's description
 * @author Zhang (rdshoep@126.com)
 *   http://www.rdshoep.com/
 * @version 
 *   1.0.0(12/29/2015)
 */

class EmptyProgress implements ProgressView {

    public static final ProgressView _instance;

    static {
        _instance = new EmptyProgress();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }
}
