package com.rdshoep.android.image;
/*
 * @description
 *   Please write the ImageHelper module's description
 * @author Zhang (rdshoep@126.com)
 *   http://www.rdshoep.com/
 * @version 
 *   1.0.0(12/28/2015)
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageHelper {

    /**
     * 创建打开照相机的目标路径
     *
     * @param cxt  上下文
     * @param path 自定义路径
     * @return 保存图片的路径
     */
    public static Uri createCameraImageUri(Context cxt, String path) {
        File fileFolder = cxt.getExternalCacheDir();
        if (fileFolder == null || !fileFolder.exists()) {
            fileFolder = cxt.getCacheDir();
        }

        String name = TextUtils.isEmpty(path) ? String.valueOf(System.currentTimeMillis()) : path;
        String filePath = fileFolder.getPath() + File.separator + name + ".jpg";
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                File folder = file.getParentFile();
                folder.mkdirs();
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return Uri.parse("file://" + filePath);
    }

    /**
     * 保存图片到缓存目录
     *
     * @param cxt    上下文环境
     * @param bitmap 图片
     * @return 保存的路径
     */
    public static String saveImageToCacheFolder(Context cxt, Bitmap bitmap) {
        String pictureDir = "";
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            byte[] byteArray = baos.toByteArray();

            Uri imageUri = createCameraImageUri(cxt, null);
            if (imageUri == null) return null;

            File file = new File(imageUri.getPath());

            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(byteArray);
            pictureDir = file.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return pictureDir;
    }

    /**
     * 压缩图片
     *
     * @param context     上下文
     * @param resourceUri 源图片路径
     * @param maxSize     最大大小
     * @return 压缩后的文件路径
     */
    public static Uri compressImage(Context context, Uri resourceUri, int maxSize) {
        //如果路径已经为文件，并且不需要再次压缩则忽略
        if (resourceUri.getScheme().startsWith("file")
                && !checkIsNeedCompress(context, resourceUri, maxSize / 4)) {
            return resourceUri;
        }
        Bitmap bitmap = decodeUriAsBitmap(context, resourceUri, maxSize / 4);
        String filePath = saveImageToCacheFolder(context, bitmap);

        if (bitmap != null) {
            bitmap.recycle();
        }

        if (!TextUtils.isEmpty(filePath)) {
            return Uri.parse("file://" + filePath);
        }
        return null;
    }

    /**
     * 检查是否需要进行压缩处理
     *
     * @param ctx       上下文
     * @param uri       源图片路径
     * @param maxPixels 最大大小
     * @return
     */
    public static boolean checkIsNeedCompress(Context ctx, Uri uri, int maxPixels) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri), null, opts);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int imageHeight = opts.outHeight;
        int imageWidth = opts.outWidth;
        // false表示读取图片像素数组到内存中，依照设定的采样率
        opts.inJustDecodeBounds = false;
        if (maxPixels > 0) {
            // 获取默认窗体显示的对象
            double scale = Math.max(imageWidth * imageHeight * 1.0 / maxPixels, 1);
            return scale > 1;
        }
        return false;
    }

    /**
     * 重新加载缩略图图片
     *
     * @param ctx       上下文
     * @param uri       图片路径
     * @param maxPixels 最大像素点
     * @return 处理后的图片
     */
    public static Bitmap decodeUriAsBitmap(Context ctx, Uri uri, int maxPixels) {
        Bitmap bitmap = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri), null, opts);
            int imageHeight = opts.outHeight;
            int imageWidth = opts.outWidth;
            // false表示读取图片像素数组到内存中，依照设定的采样率
            opts.inJustDecodeBounds = false;
            if (maxPixels > 0) {
                // 获取默认窗体显示的对象
                double scale = Math.max(imageWidth * imageHeight * 1.0 / maxPixels, 1);
                // 采样率
                opts.inSampleSize = scale <= 1 ? 1 : (int) Math.round(Math.sqrt(scale) + 0.5);
            }
            bitmap = BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri), null, opts);
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
