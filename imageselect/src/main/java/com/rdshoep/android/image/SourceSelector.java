package com.rdshoep.android.image;
/*
 * @description
 *   Please write the SourceSelector module's description
 * @author Zhang (rdshoep@126.com)
 *   http://www.rdshoep.com/
 * @version 
 *   1.0.0(12/28/2015)
 */

import android.content.Context;

public interface SourceSelector {
    /**
     * 选择图片来源
     * @param context 上下文
     * @param supportImageSource 支持的图像来源
     * @param callback 选择完成后的回调
     */
    void selectImageSource(Context context, ImageSource supportImageSource
            , SourceSelectCallback callback);
}
