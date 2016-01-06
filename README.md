# CropImage
Android platform select/crop image library
> Android 4.3之后系统对权限做了一些调整，导致APP没有权限直接对系统图片资源做裁剪、上传或其他处理，所以在选择图片的适合需要根据不同的版本分别做不同的处理。

之前写了一个复杂的工具类解决此问题，通过各种的判断区分不同的平台并通过静态的空间保存相应的数据。后来在某些情况下，又出现了问题，这时候来查问题发现以前的逻辑理解起来比较麻烦，最终决定重新梳理一下结构，简单化处理此问题。

[CropImage](https://github.com/rdshoep/CropImage)是我针对这个问题的开源项目，目前是初级版本，还有几个问题需要解决，请持续关注。

支持功能：

1. 选择图片支持从图库选择或相机拍照；
2. 裁剪图片支持从图库选择或者相机拍照；
3. 支持自定义ProgressView等待提示框；
4. 支持自定义SourceSelector选择器；

Todo:

* 选择/裁剪产生的图片文件/缓冲文件需要自动设置相应的规则自动删除

您可以快速引用项目本项目：
```groovy
compile 'com.rdshoep.android:CropImage:0.1'
```

#### Select Image
1.在响应图片选择按钮的Activity/Fragment中
```java
ImageSelector imageSelector = new ImageSelector(this);
```
2.实现接口
```java
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
```
3.在onClick事件中添加
```java
//selectRequestCode  请求新界面的requestCode
imageSelector.selectImage(selectRequestCode, ImageSource.ALL, 0, this);
```
4.重写onActivityResult，添加以下代码
```java
imageSelector.onActivityResult(requestCode, resultCode, data);
```

#### Crop Image
操作步骤和Select Image基本相同

1.在响应图片选择按钮的Activity/Fragment中
```java
ImageCropper imageCropper = new ImageCropper(this);
```
2.实现接口
```java
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
     * @param requestCode 进入裁剪界面时的requestCode
     * @param targetUri   最终图片路径
     */
    void onCropEnd(int requestCode, Uri targetUri);
}
```
3.在onClick事件中添加
```java
//cropRequestCode  请求新界面的requestCode
imageCropper.cropImage(cropRequestCode, ImageSource.ALL, -1, this);
```
4.重写onActivityResult，添加以下代码
```java
imageCropper.onActivityResult(requestCode, resultCode, data);
```

###完整示例
**activity_main.xml**
```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:paddingBottom="@dimen/activity_vertical_margin"
    android:paddingLeft="@dimen/activity_horizontal_margin"
    android:paddingRight="@dimen/activity_horizontal_margin"
    android:paddingTop="@dimen/activity_vertical_margin"
    tools:context="com.rdshoep.android.selectimage.MainActivity">

    <ImageView
        android:id="@android:id/icon"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

    <Button
        android:id="@android:id/button1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="100dp"
        android:text="Select" />

    <Button
        android:id="@android:id/button2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginBottom="100dp"
        android:text="Crop" />

```

**MainActivity**
```java
package com.rdshoep.android.selectimage;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.rdshoep.android.image.CropImageCallback;
import com.rdshoep.android.image.DialogProgress;
import com.rdshoep.android.image.ImageCropper;
import com.rdshoep.android.image.ImageSelectCallback;
import com.rdshoep.android.image.ImageSelector;
import com.rdshoep.android.image.ImageSource;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
        , ImageSelectCallback, CropImageCallback {

    ImageCropper imageCropper;
    ImageSelector imageSelector;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageCropper = new ImageCropper(this);
        imageSelector = new ImageSelector(this);
        imageSelector.setProgressView(DialogProgress.newInstance(this));

        findViewById(android.R.id.button1).setOnClickListener(this);
        findViewById(android.R.id.button2).setOnClickListener(this);
        imageView = (ImageView) findViewById(android.R.id.icon);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case android.R.id.button1:
                imageSelector.selectImage(1, ImageSource.ALL, 0, this);
                break;
            case android.R.id.button2:
                imageCropper.cropImage(2, ImageSource.ALL, -1, this);
                break;
        }
    }

    @Override
    public void onImageSelected(int requestCode, Uri targetUri) {
        Toast.makeText(this, targetUri.getPath(), Toast.LENGTH_SHORT).show();
        imageView.setImageURI(targetUri);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        imageSelector.onActivityResult(requestCode, resultCode, data);
        imageCropper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Bundle setCropParams(int requestCode) {
        return null;
    }

    @Override
    public void onCropEnd(int requestCode, Uri targetUri) {
        Toast.makeText(this, targetUri.getPath(), Toast.LENGTH_SHORT).show();
        imageView.setImageURI(targetUri);
    }
}
```
