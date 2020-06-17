package com.supcon.mes.middleware.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.constant.Constant;
import com.watermark.androidwm.WatermarkBuilder;
import com.watermark.androidwm.bean.WatermarkText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * @Description: 图片水印
 * @Author: zhangwenshuai
 * @CreateDate: 2020/6/3 22:38
 */
public class WatermarkUtil {
    public static void makeText(Context context, String text, File file){
        WatermarkText watermarkText = new WatermarkText(text)
                .setPositionX(0.5) // 横坐标
                .setPositionY(0.5) // 纵坐标
                .setTextAlpha(200) // 透明度
                .setTextColor(Color.RED) // 文字水印文字颜色
                .setTextSize(25)
//                .setTextFont(Typeface.BOLD_ITALIC)
                .setTextFont(R.font.huawenxinkai) // 字体
//                .setRotation(20)
                .setTextShadow(0.1f, 2, 2, context.getResources().getColor(R.color.white)); // 字体的阴影

        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        bitmap = WatermarkBuilder
//                .create(this,R.drawable.xj_record20200524_170521)
                .create(context, bitmap) // 加载背景底图
                .loadWatermarkText(watermarkText) // 加载水印对象
                .getWatermark() // 绘制带有水印的图片
                .getOutputImage();
//                .saveToLocalPng(file.getParent()); // 保存本地
//                .setToImageView(imageView); // 设置结果到 ImageView 里

        compressPicture(bitmap,file.getAbsolutePath(),90);

    }



    /**
     * 按照图片尺寸压缩
     * @param bitmap
     * @param desPath
     */
    public static void compressPicture(Bitmap bitmap, String desPath, int quality) {
        FileOutputStream fos = null;
        BitmapFactory.Options op = new BitmapFactory.Options();

        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        op.inJustDecodeBounds = true;
//        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, op);
//        op.inJustDecodeBounds = false;

        // 缩放图片的尺寸
        float w = op.outWidth;
        float h = op.outHeight;
        float hh = 1024f;//
        float ww = 1024f;//
        // 最长宽度或高度1024
        float be = 1.0f;
        if (w > h && w > ww) {
            be = w / ww;
        } else if (w < h && h > hh) {
            be = h / hh;
        }
        if (be <= 0) {
            be = 1.0f;
        }
        op.inSampleSize = (int) be;// 设置缩放比例,这个数字越大,图片大小越小.
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
//        bitmap = BitmapFactory.decodeFile(srcPath, op);
        int desWidth = (int) (w / be);
        int desHeight = (int) (h / be);
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        try {
            fos = new FileOutputStream(desPath);
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
