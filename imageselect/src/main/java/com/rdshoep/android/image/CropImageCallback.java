package com.rdshoep.android.image;
/*
 * @description
 *   Please write the CropImageCallback module's description
 * @author Zhang (rdshoep@126.com)
 *   http://www.rdshoep.com/
 * @version 
 *   1.0.0(12/28/2015)
 */

import android.net.Uri;
import android.os.Bundle;

public interface CropImageCallback {

    /**
     * 获取相关裁剪参数
     *
     * @param requestCode 进入裁剪界面时的requestCode
     * @return 组成的裁剪参数
     */
    Bundle setCropParams(int requestCode);

    /**
     * 图片裁剪完成回调
     *
     * @param targetUri 最终图片路径
     */
    void onCropEnd(Uri targetUri);
}
