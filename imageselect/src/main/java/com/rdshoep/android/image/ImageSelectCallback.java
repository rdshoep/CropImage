package com.rdshoep.android.image;
/*
 * @description
 *   Please write the ImageSelectCallback module's description
 * @author Zhang (rdshoep@126.com)
 *   http://www.rdshoep.com/
 * @version 
 *   1.0.0(12/28/2015)
 */

import android.net.Uri;

public interface ImageSelectCallback {

    /**
     * 图片选择完成回调
     *
     * @param requestCode requestCode
     * @param targetUri   处理后的文件Uri
     */
    void onImageSelected(int requestCode, Uri targetUri);
}
