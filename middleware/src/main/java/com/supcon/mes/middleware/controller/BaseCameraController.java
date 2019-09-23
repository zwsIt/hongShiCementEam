package com.supcon.mes.middleware.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import com.supcon.common.com_http.util.RxSchedulers;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.beans.GalleryBean;
import com.supcon.mes.mbap.utils.CameraUtil;
import com.supcon.mes.mbap.utils.ImageUtil;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomGalleryView;
import com.supcon.mes.middleware.IntentRouter;
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.util.FaultPicHelper;
import com.supcon.mes.middleware.util.PicUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;
import static com.supcon.mes.mbap.adapter.GalleryAdapter.FILE_TYPE_PICTURE;
import static com.supcon.mes.mbap.adapter.GalleryAdapter.FILE_TYPE_VIDEO;

/**
 * Created by wangshizhan on 2019/2/26
 * Email:wangshizhan@supcom.com
 */
public class BaseCameraController extends BaseViewController{


    protected File tempFile/*, thumbnail*/;
    private Uri pictureUri;
    private final int ACT_GALLERY = 0;
    private final int ACT_PHOTO = 1;
    private final int ACT_VIDEO = 2;

    protected Activity activity;
    protected List<GalleryBean> pics = new ArrayList<>();
    protected String picName;
    protected String dir;
    private OnSuccessListener<File> mOnSuccessListener;
    private int lastAction = -1;
//    protected Map<Integer, CustomGalleryView> mViewMap = new HashMap<>();
    protected int actionPosition;
    protected CustomGalleryView actionGalleryView;

    public BaseCameraController(View rootView) {
        super(rootView);
        this.activity = (Activity) context;
    }

    @Override
    public void onInit() {
        super.onInit();
        picName = "NewPic";
    }

    @Override
    public void initListener() {
        super.initListener();

    }

    public void addListener(int viewPosition, CustomGalleryView customGalleryView){
        if(customGalleryView!=null){
            customGalleryView.setOnChildViewClickListener(new OnCameraChildViewClickListener(customGalleryView, viewPosition));
//            mViewMap.put(viewPosition, customGalleryView);
//            customGalleryView.setOnChildViewClickListener(new OnChildViewClickListener() {
//                @Override
//                public void onChildViewClick(View childView, int action, Object obj) {
//
//                    actionPosition = updatePosition(customGalleryView);
//                    actionGalleryView = customGalleryView;
//
//                    int position = (int) obj;
//
//                    if (position == -1) {
//
//                        if (action == CustomGalleryView.ACTION_TAKE_PICTURE_FROM_CAMERA) {
//                            startCamera();
//                        }
//                        else if (action == CustomGalleryView.ACTION_TAKE_PICTURE_FROM_GALLERY) {
//                            startGallery();
//                        }
//                        else if(action == CustomGalleryView.ACTION_TAKE_VIDEO_FROM_CAMERA){
//                            startVideo();
//                        }
//                        return;
//                    }
//                    List<GalleryBean> galleryBeans = customGalleryView.getGalleryAdapter().getList();
//                    GalleryBean galleryBean = galleryBeans.get(position);
//                    if (action == CustomGalleryView.ACTION_VIEW) {
//
//                        if(galleryBean.fileType == FILE_TYPE_PICTURE){
//                            viewPic(customGalleryView, galleryBeans, position);
//                        }
//                        else if(galleryBean.fileType == FILE_TYPE_VIDEO){
//                            viewVideo(galleryBean);
//                        }
//                    } else if (action == CustomGalleryView.ACTION_DELETE) {
//                        showDeleteDialog(galleryBean, position);
//                    }
//                }
//            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ACT_GALLERY:
                if(data!=null){
                    Uri uri = parseUri(data.getData());
                    if(uri!=null){
                        cropBack(uri);
                    }
                }
                break;
            case ACT_PHOTO:
                cropBack(pictureUri);
                break;

            case ACT_VIDEO:
                LogUtil.d("ACT_VIDEO");
                if(data!=null){
//                    Uri uri = parseUri(data.getData());
                    Uri uri = data.getData();
                    if(uri!=null){
                        createThumbnail(uri);
                    }
                }

                break;
        }
    }


    public void init(String dir, String picName){
        this.dir = dir;
        this.picName = picName;
        CameraUtil.createDir(dir);
    }

    public void initPicName(boolean isVideo){
        String fileName = createFileName(picName, isVideo?".mp4":".jpg");
        CameraUtil.createFile(dir, fileName);
        tempFile = new File(dir, fileName);
        pictureUri = Uri.fromFile(tempFile);

    }

    //拍照
    public void startCamera() {
        if(dir == null)
        {
            throw new IllegalArgumentException("You should call init first!");
        }
        initPicName(false);
        //request the camera permission dynamic above android 6.0
        boolean permission = PackageManager.PERMISSION_GRANTED == ContextCompat
                .checkSelfPermission(activity, Manifest.permission.CAMERA);
        if (permission) {
            Intent intent = CameraUtil.openCamera(pictureUri);
            activity.startActivityForResult(intent, ACT_PHOTO);
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(new String[]{Manifest.permission.CAMERA}, 3);
        }
        lastAction = ACT_PHOTO;
    }

    public void startGallery() {
        if(dir == null)
        {
            throw new IllegalArgumentException("You should call init first!");
        }
        initPicName(false);
        Intent intent = CameraUtil.openGallery();
        activity.startActivityForResult(intent, ACT_GALLERY);
        lastAction = ACT_GALLERY;
    }

    public void startVideo(){
        if(dir == null)
        {
            throw new IllegalArgumentException("You should call init first!");
        }
        initPicName(true);
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);// 创建一个请求视频的意图
//        intent.putExtra("autofocus", true);//进行自动对焦操作
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);// 设置视频的质量，值为0-1，
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 8);// 设置视频的录制长度，s为单位
        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 16 * 1024 * 1024L);// 设置视频文件大小，字节为单位
        activity.startActivityForResult(intent, ACT_VIDEO);// 设置请求码，在onActivityResult()方法中接收结果
        lastAction = ACT_VIDEO;
    }

    public void viewPic(CustomGalleryView childView, List<GalleryBean> galleryBeans, int position){
        Bundle bundle = FaultPicHelper.genRequestBundle(context, childView, galleryBeans, position, childView.isEditable());
        activity.getWindow().setWindowAnimations(R.style.fadeStyle);
        IntentRouter.go(context, Constant.Router.IMAGE_VIEW, bundle);
    }

    public void viewVideo(GalleryBean galleryBean){
        Uri uri = Uri.parse(galleryBean.localPath);//调用系统自带的播放器
        Intent intent = new Intent(Intent.ACTION_VIEW);
        LogUtil.d(uri.toString());
        intent.setDataAndType(uri, "video/mp4");
        activity.startActivity(intent);
    }

    protected String createFileName(String fileName, String fileType) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        fileName = fileName + sdf.format(date) + fileType;
        return fileName;
    }

    public void setOnSuccessListener(OnSuccessListener<File> onSuccessListener){
        this.mOnSuccessListener = onSuccessListener;
    }

    private Uri parseUri(Uri inUri) {
        if(inUri == null){
            return null;
        }
        //get picture uri from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.getContentResolver().query(inUri, projection, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                File file = new File(filePath);
                return Uri.fromFile(file);

            }
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }

        return null;
    }

    @SuppressLint("CheckResult")
    private void createThumbnail(Uri inUri) {
        if (inUri == null) {
            return;
        }
        try {

            Flowable.just(inUri)
                    .subscribeOn(Schedulers.newThread())
                    .map(uri -> {
                        File file = new File(uri.getPath());
//                        String thumbnailName = PicUtil.getThumbnailName(file.getName());
//                        CameraUtil.createFile(dir, thumbnailName);
//                        thumbnail = new File(dir, thumbnailName);
//                        ImageUtil.compressAndGenImage(PicUtil.getVideoFirstFrame(file), thumbnail.getAbsolutePath(), 300);
                        return file;

                    })
                    .compose(RxSchedulers.io_main())
                    .subscribe(new Consumer<File>() {
                        @Override
                        public void accept(File file) throws Exception {
                            if(mOnSuccessListener!= null){
                                mOnSuccessListener.onSuccess(file);
                            }
                            onFileReceived(file);
                            tempFile = null;
                        }
                    });


        } catch (Exception e) {
            LogUtil.e("cropBack",e.getMessage());
            e.printStackTrace();
        }
    }

    @SuppressLint("CheckResult")
    private void cropBack(Uri inUri) {
        if (inUri == null || tempFile == null) {
            return;
        }
        try {

            Flowable.just(inUri)
                    .map(new Function<Uri, File>() {
                        @Override
                        public File apply(Uri uri) throws Exception {
                            File file = new File(uri.getPath());
                            if(file.getAbsolutePath().startsWith(dir) && lastAction == ACT_GALLERY){
                                return file;
                            }
                            ImageUtil.compressPicture(uri.getPath(), tempFile.getAbsolutePath(), 90);
                            return tempFile;

                        }
                    })
                    .compose(RxSchedulers.io_main())
                    .subscribe(new Consumer<File>() {
                        @Override
                        public void accept(File file) throws Exception {
                            if(mOnSuccessListener!= null){
                                mOnSuccessListener.onSuccess(file);
                            }
                            onFileReceived(file);
                            tempFile = null;
                        }
                    });


        } catch (Exception e) {
            LogUtil.e("cropBack",e.getMessage());
            e.printStackTrace();
        }
    }

//    private int updatePosition(CustomGalleryView customGalleryView){
//        for(Integer position:mViewMap.keySet()){
//
//            if(customGalleryView.equals(mViewMap.get(position))){
//                return position;
//            }
//
//        }
//
//        return -1;
//    }
//
//    public void clearViewPositions(){
//        if(mViewMap!=null && mViewMap.size()!=0){
//            mViewMap.clear();
//        }
//    }

    protected void onFileReceived(File file){


    }

    protected void onFileDelete(GalleryBean galleryBean, int position){

    }


    protected void showDeleteDialog(GalleryBean galleryBean, int position) {
        new CustomDialog(context)
                .twoButtonAlertDialog(galleryBean.fileType == FILE_TYPE_PICTURE?"是否删除图片?":"是否删除视频?")
                .bindView(R.id.grayBtn, "取消")
                .bindView(R.id.redBtn, "确定")
                .bindClickListener(R.id.grayBtn, v1 -> {
                }, true)
                .bindClickListener(R.id.redBtn, v3 -> {
                    onFileDelete(galleryBean, position);
                    if(mOnSuccessListener!=null){
                        mOnSuccessListener.onSuccess(new File(galleryBean.localPath));
                    }
                }, true)
                .show();
    }


    public void deleteGalleryBean(GalleryBean galleryBean, int position){

    }

    public void deleteFile(String localPath){

        if(TextUtils.isEmpty(localPath)){
            return;
        }

        File file = new File(localPath);
        if (file.exists()) {
            if (file.isFile())
                file.delete();
        }
    }


    private class OnCameraChildViewClickListener implements OnChildViewClickListener{

        private int mViewPosition;
        private CustomGalleryView mCustomGalleryView;

        public OnCameraChildViewClickListener(CustomGalleryView customGalleryView, int viewPosition){
            mCustomGalleryView = customGalleryView;
            mViewPosition =  viewPosition;
        }


        @Override
        public void onChildViewClick(View childView, int action, Object obj) {

            actionPosition = mViewPosition;
            actionGalleryView = mCustomGalleryView;
            int position = (int) obj;

            if (position == -1) {

                if (action == CustomGalleryView.ACTION_TAKE_PICTURE_FROM_CAMERA) {
                    startCamera();
                }
                else if (action == CustomGalleryView.ACTION_TAKE_PICTURE_FROM_GALLERY) {
                    startGallery();
                }
                else if(action == CustomGalleryView.ACTION_TAKE_VIDEO_FROM_CAMERA){
                    startVideo();
                }
                return;
            }
            List<GalleryBean> galleryBeans = mCustomGalleryView.getGalleryAdapter().getList();
            GalleryBean galleryBean = galleryBeans.get(position);
            if (action == CustomGalleryView.ACTION_VIEW) {

                if(galleryBean.fileType == FILE_TYPE_PICTURE){
                    viewPic(mCustomGalleryView, galleryBeans, position);
                }
                else if(galleryBean.fileType == FILE_TYPE_VIDEO){
                    viewVideo(galleryBean);
                }
            }
            else if(action == CustomGalleryView.ACTION_VIDEO_PLAY){
                viewVideo(galleryBean);
            }
            else if (action == CustomGalleryView.ACTION_DELETE) {
                showDeleteDialog(galleryBean, position);
            }
        }
    }

}
