package com.supcon.mes.middleware.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.supcon.common.com_http.util.RxSchedulers;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.utils.CameraUtil;
import com.supcon.mes.mbap.utils.ImageUtil;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * @author yangfei.cao
 * @ClassName inspection
 * @date 2018/4/2
 * ------------- Description -------------
 */
@Deprecated
public class CameraManager {
    private File mFile;
    private Uri pictureUri;
    private final int ACT_GALLERY = 0;
    private final int ACT_CAMERA = 1;

    private Activity activity;
    private List<String> picNameSingleList = new ArrayList<>(); //当前拍照图片存储list
    private String picType;
    private OnSuccessListener<File> mOnSuccessListener;

    public CameraManager(Activity activity, String picType) {
        this.activity = activity;
        this.picType = picType;
    }

    //拍照
    public void startCamera(String dir) {

        init(dir);
        //request the camera permission dynamic above android 6.0
        boolean permission = PackageManager.PERMISSION_GRANTED == ContextCompat
                .checkSelfPermission(activity, Manifest.permission.CAMERA);
        if (permission) {
            Intent intent = CameraUtil.openCamera(pictureUri);
            activity.startActivityForResult(intent, ACT_CAMERA);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(new String[]{Manifest.permission.CAMERA}, 3);
        }
    }

    private void init(String dir){
        picNameSingleList.clear();
        String fileName = createFileName(picType, ".jpg");
        CameraUtil.createDir(dir);
        CameraUtil.createFile(dir, fileName);
        mFile = new File(dir, fileName);
        pictureUri = Uri.fromFile(mFile);
    }


    public void startGallery(String dir) {

        init(dir);
        Intent intent = CameraUtil.openGallery();
        activity.startActivityForResult(intent, ACT_GALLERY);
    }

    public void onActivityResult(int requestCode, Intent data) {
        switch (requestCode) {
            case ACT_GALLERY:
                if(data!=null){
                    galleryBack(data.getData());
                }
                break;
            case ACT_CAMERA:
                cropBack(pictureUri);
                break;
        }
    }

    private void galleryBack(Uri inUri) {
        //get picture uri from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.getContentResolver().query(inUri, projection, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                File file = new File(filePath);
                cropBack(Uri.fromFile(file));
            }
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    @SuppressLint("CheckResult")
    private void cropBack(Uri inUri) {
        if (inUri == null) {
            return;
        }
        try {

            Flowable.just(inUri)
                    .map(new Function<Uri, File>() {
                        @Override
                        public File apply(Uri uri) throws Exception {
                            File file = new File(uri.getPath());
                            if(file.getAbsolutePath().startsWith(Constant.IMAGE_SAVE_PATH)){
                                return file;
                            }
                            ImageUtil.compressPicture(uri.getPath(), mFile.getAbsolutePath(), 90);
                            return mFile;

                        }
                    })
                    .compose(RxSchedulers.io_main())
                    .subscribe(new Consumer<File>() {
                        @Override
                        public void accept(File file) throws Exception {
                            picNameSingleList.add(file.getName());
                            if(mOnSuccessListener!= null){
                                mOnSuccessListener.onSuccess(file);
                            }
                            mFile = null;
                        }
                    });


        } catch (Exception e) {
            LogUtil.e("cropBack",e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveBitmap(File file, Bitmap bitmap) {
        if (file != null && bitmap != null) {
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                if (!bitmap.isRecycled() && null != bitmap) {
                    bitmap = null;
                }

                out.flush();
                out.close();
            } catch (FileNotFoundException var3) {

            } catch (IOException var4) {
                var4.printStackTrace();
            }

        }
    }

    //删除本地图片
    public void deleteBitmap(String localPath) {
        /*if (!picPaths.contains(localPath)){
            return;
        }*/
        File file = new File(localPath);
        if (file.exists()) {
            if (file.isFile())
                file.delete();
        }

    }

    //获取当前拍照图片名称
    public String getPicPaths() {
        StringBuffer buffer = new StringBuffer();
        if (picNameSingleList.size() > 0) {
            buffer.append(picNameSingleList.get(0));
            /*for (int i = 0; i < picNameSingleList.size(); i++) {
                String localPath = picNameSingleList.get(i);
                if (i == picNameSingleList.size() - 1) {
                    buffer.append(localPath);
                } else {
                    buffer.append(localPath).append(",");
                }
            }*/
        }
        return buffer.toString();
    }

    //初始化数据库地址图片地址
    public void setPicPaths(String paths) {
        if (TextUtils.isEmpty(paths)) {
            return;
        }
        String[] split = paths.split(",");
        picNameSingleList.addAll(Arrays.asList(split));
    }

    public static String createFileName(String fileName, String fileType) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        fileName = fileName + sdf.format(date) + fileType;
        return fileName;
    }

    public void setOnSuccessListener(OnSuccessListener<File> onSuccessListener){
        this.mOnSuccessListener = onSuccessListener;
    }

}
