package com.rdshoep.android.image;
/*
 * @description
 *   Please write the SourceSelectCallback module's description
 * @author Zhang (rdshoep@126.com)
 *   http://www.rdshoep.com/
 * @version 
 *   1.0.0(12/28/2015)
 */

public interface SourceSelectCallback {
    /**
     * 选择图像来源完成
     * @param imageSource 选中的图片来源
     **/
    void onImageSourceSelected(ImageSource imageSource);
}
