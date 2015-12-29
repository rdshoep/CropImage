package com.rdshoep.android.image;
/*
 * @description
 *   Please write the ImageCropImpl module's description
 * @author Zhang (rdshoep@126.com)
 *   http://www.rdshoep.com/
 * @version 
 *   1.0.0(12/28/2015)
 */

import android.content.Intent;
import android.net.Uri;

interface ImageCropImpl {

    /**
     * 裁剪图片
     *
     * @param requestCode requestCode
     * @param uri         原图片Uri
     * @param maxSize     最大文件大小  <0 默认大小  0  无限制   >0 设置大小
     * @param callback    操作完成回调
     */
    void cropImage(int requestCode, Uri uri, int maxSize, CropImageCallback callback);

    /**
     * 从选择界面退出后的返回值捕获
     *
     * @param requestCode requestCode
     * @param resultCode  结果状态
     * @param data        返回的相关数据
     * @return 是否捕获
     */
    boolean onActivityResult(int requestCode, int resultCode, Intent data);
}
