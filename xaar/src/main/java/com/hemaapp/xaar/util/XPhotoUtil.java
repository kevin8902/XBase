package com.hemaapp.xaar.util;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.hemaapp.xaar.XApplication;
import com.hemaapp.xaar.XConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/******************************
 * 作者:邢佩凯
 * 日期:2016/10/11 13:09
 * 名称:XPhotoUtil
 * 注释:关于照片的工具类
 *******************************/
public class XPhotoUtil {

    /**
     * 判断是否有相机
     *
     * @return
     */
    public static boolean HasCamera() {
        PackageManager manager = XApplication.getApplicationInstance().getPackageManager();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List list = manager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }


    /**
     * 获取图片的旋转角度
     *
     * @param path 路径
     * @return 角度
     */
    private static int getPhotoDegree(String path) {
        int degree = 0;
        try {
            ExifInterface mExifInterface = new ExifInterface(path);
            int oriention = mExifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (oriention) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


    /**
     * 将图片按指定的角度进行旋转
     *
     * @param bitmap 图片
     * @param degree 角度
     */
    private static Bitmap rotationBitmapByDegree(Bitmap bitmap, int degree) {
        //根据旋转角度生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        //根据旧图片生成新的图片
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return newBitmap;
    }


    /**
     * 根据地址获取图片
     *
     * @param path
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeBitmapFromFile(String path, int reqWidth, int reqHeight) {
        if (!TextUtils.isEmpty(path)) {
            if (reqHeight <= 0 || reqWidth <= 0) {
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                int degree = getPhotoDegree(path);
                return rotationBitmapByDegree(bitmap, degree);
            } else {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(path, options);
                if (options.outWidth == -1 || options.outHeight == -1) {
                    try {
                        ExifInterface mExifInterface = new ExifInterface(path);
                        int height = mExifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.ORIENTATION_NORMAL);
                        int width = mExifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, ExifInterface.ORIENTATION_NORMAL);
                        options.outWidth = width;
                        options.outHeight = height;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inJustDecodeBounds = false;
                Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                int degree = getPhotoDegree(path);
                return rotationBitmapByDegree(bitmap, degree);
            }
        }

        return null;
    }


    //获取缩放比例
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
            long totalPixels = width * height / inSampleSize;
            long totalReqPixelsCap = reqWidth * reqHeight * 2;
            while (totalPixels > totalReqPixelsCap) {
                inSampleSize *= 2;
                totalPixels /= 2;
            }
        }
        return inSampleSize;
    }


    /**
     * 本地图片上传之前压缩
     *
     * @param path
     * @return
     */
    public static String compressPictureWithSaveDir(String path) {
        Bitmap bitmap = XPhotoUtil.decodeBitmapFromFile(path, XConfig.IMAGE_WIDTH, XConfig.IMAGE_HEIGHT);
        File dir = new File(XConfig.save_path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String savePath = dir.getAbsolutePath();
        File file = new File(savePath, UUID.randomUUID().toString() + ".jpg");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int options = XConfig.IMAGE_QUALITY;
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, bos);
        // 循环判断压缩后的图片是否是大于100kb,如果大于，就继续压缩，否则就不压缩
        while (bos.toByteArray().length / 1024 > XConfig.image_size) {
            bos.reset();// 置为空
            // 每次都减少10
            options -= 10;
            // 压缩options%
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, bos);
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bos.writeTo(out);
            bos.flush();
            bos.close();
            out.flush();
            out.close();
            bitmap.recycle();
            System.gc();
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


}
