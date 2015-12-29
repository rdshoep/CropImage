package com.rdshoep.android.image;
/*
 * @description
 *   Please write the ImageSelectImpl module's description
 * @author Zhang (rdshoep@126.com)
 *   http://www.rdshoep.com/
 * @version 
 *   1.0.0(12/28/2015)
 */

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

interface ImageSelectImpl {
    /**
     * 设置所在Activity容器
     *
     * @param activityContainer Activity 容器
     * @return this
     */
    ImageSelectImpl setContainer(Activity activityContainer);

    /**
     * 设置Fragment容器
     *
     * @param fragmentContainer Fragment 容器
     *                          * @return this
     */
    ImageSelectImpl setContainer(Fragment fragmentContainer);

    /**
     * 是否需要Copy
     * 如果不需要压缩并且是从图库选择的资源，则不需要从系统中拷贝一份图片出来
     *
     * @param enableCopy
     */
    void setCopyEnable(boolean enableCopy);

    /**
     * 从照相机获取图像
     *
     * @param requestCode requestCode
     * @param maxSize     最大文件大小  <0 默认大小  0  无限制   >0 设置大小
     * @param callback    选择完成回调
     */
    void selectImageFromCamera(int requestCode, int maxSize, ImageSelectCallback callback);

    /**
     * 从图库选择图像
     *
     * @param requestCode requestCode
     * @param maxSize     最大文件大小  <0 默认大小  0  无限制   >0 设置大小
     * @param callback    选择完成回调
     */
    void selectImageFromAlbum(int requestCode, int maxSize, ImageSelectCallback callback);

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
