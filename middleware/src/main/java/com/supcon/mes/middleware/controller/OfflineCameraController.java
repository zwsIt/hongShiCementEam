package com.supcon.mes.middleware.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.activity.BaseActivity;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.beans.GalleryBean;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomGalleryView;
import com.supcon.mes.middleware.IntentRouter;
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.util.FaultPicHelper;
import com.supcon.mes.middleware.util.PicUtil;

import java.io.File;
import java.util.List;

import static com.supcon.mes.mbap.adapter.GalleryAdapter.FILE_TYPE_PICTURE;
import static com.supcon.mes.mbap.adapter.GalleryAdapter.FILE_TYPE_VIDEO;

/**
 * Created by wangshizhan on 2019/2/27
 * Email:wangshizhan@supcom.com
 */
public class OfflineCameraController extends BaseCameraController {

    @BindByTag("yhGalleryView")
    protected CustomGalleryView yhGalleryView;

    private String originalPics = "";

    public OfflineCameraController(View rootView) {
        super(rootView);
    }

    /**
     * 如果tag不是yhGalleryView，则在这里初始化
     * @param customGalleryView
     */
    public void addGalleryView(int viewPossition, CustomGalleryView customGalleryView){
        yhGalleryView = customGalleryView;
        addListener(viewPossition, customGalleryView);
    }

    @Override
    public void onInit() {
        super.onInit();
    }

    @Override
    public void initListener() {
        super.initListener();
//        if(yhGalleryView!=null) {
//            yhGalleryView.setOnChildViewClickListener((childView, action, obj) -> {
//
//                int position = (int) obj;
//
//                if (position == -1) {
//
//                    if (action == CustomGalleryView.ACTION_TAKE_PICTURE_FROM_CAMERA) {
//                        startCamera();
//                    } else if (action == CustomGalleryView.ACTION_TAKE_PICTURE_FROM_GALLERY) {
//                        startGallery();
//                    } else if (action == CustomGalleryView.ACTION_TAKE_VIDEO_FROM_CAMERA) {
//                        startVideo();
//                    }
//                    return;
//                }
//                List<GalleryBean> galleryBeans = yhGalleryView.getGalleryAdapter().getList();
//                GalleryBean galleryBean = galleryBeans.get(position);
//                if (action == CustomGalleryView.ACTION_VIEW) {
//
//                    if (galleryBean.fileType == FILE_TYPE_PICTURE) {
//                        viewPic(childView, galleryBeans, position);
//                    } else if (galleryBean.fileType == FILE_TYPE_VIDEO) {
//                        viewVideo(galleryBean);
//                    }
//                } else if (action == CustomGalleryView.ACTION_DELETE) {
//                    showDeleteDialog(galleryBean, position);
//                }
//
//            });
//        }
        addListener(0, yhGalleryView);
    }

//    @Override
//    protected void showDeleteDialog(GalleryBean galleryBean, int position) {
//        new CustomDialog(context)
//                .twoButtonAlertDialog(galleryBean.fileType == FILE_TYPE_PICTURE?"是否删除图片?":"是否删除视频?")
//                .bindView(R.id.grayBtn, "取消")
//                .bindView(R.id.redBtn, "确定")
//                .bindClickListener(R.id.grayBtn, v1 -> {
//                }, true)
//                .bindClickListener(R.id.redBtn, v3 -> {
//                    yhGalleryView.deletePic(position);
//                    delete(galleryBean);
//                }, true)
//                .show();
//    }


    @Override
    protected void onFileDelete(GalleryBean galleryBean, int position) {
        super.onFileDelete(galleryBean, position);
        deleteGalleryBean(galleryBean, position);
    }

    @Override
    protected void onFileReceived(File file) {
        super.onFileReceived(file);

        if(PicUtil.isPic(file.getName())){
            ((BaseActivity) context).onLoading("正在储存图片...");
            savePicSuccess(file);
        }
        else if(PicUtil.isVideo(file.getName())){
            ((BaseActivity) context).onLoading("正在储存视频...");
            saveVideooSuccess(file);
        }

    }

    private void saveVideooSuccess(File file) {
        ((BaseActivity) context).onLoadSuccess("储存成功！");
        GalleryBean galleryBean = new GalleryBean();
        //设置图片储存路径
        galleryBean.localPath = file.getAbsolutePath();
        //temp
        galleryBean.type = "local";
        //设置图片储存url
        galleryBean.url = null;
//        galleryBean.thumbnailPath = thumbnail.getAbsolutePath();
        galleryBean.fileType = FILE_TYPE_VIDEO;

        yhGalleryView.addGalleryBean(galleryBean);
        pics.add(galleryBean);
    }

    private void savePicSuccess(File file) {
        ((BaseActivity) context).onLoadSuccess("储存成功！");
        GalleryBean galleryBean = new GalleryBean();
        //设置图片储存路径
        galleryBean.localPath = file.getAbsolutePath();
        //temp
        galleryBean.type = "local";
        //设置图片储存url
        galleryBean.url = null;

        yhGalleryView.addGalleryBean(galleryBean);
        pics.add(galleryBean);
    }


    @Override
    public void deleteGalleryBean(GalleryBean galleryBean, int position) {
        super.deleteGalleryBean(galleryBean, position);
        yhGalleryView.deletePic(position);
        delete(galleryBean);
    }

    /**
     * 删除图片方法，这里并不对本地的图片进行操作
     */
    private void delete(GalleryBean galleryBean) {

        if (pics.contains(galleryBean)) {
            pics.remove(galleryBean);
        }
    }

    /**
     * 进行照片视图的初始化操作
     *
     * @param faultPicPaths 收纳图片路径的字符串
     */
    public void setPicPaths(String faultPicPaths) {
        if (!TextUtils.isEmpty(faultPicPaths)) {
            if (faultPicPaths.contains(",")) {
                this.setPicPaths(faultPicPaths.split(","));
            } else {
                this.setPicPaths(new String[]{faultPicPaths});
            }
        }
    }

    private void setPicPaths(String[] faultPicPaths) {
        FaultPicHelper.initPics(faultPicPaths, pics, yhGalleryView);
        originalPics = GsonUtil.gsonString(pics);
    }

    /**
     * 所有图片的本地全路径
     * @return 通过，进行分割的本地全路径拼接成的字符串
     */
    public String getLocalPaths() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < pics.size(); i++) {
            if (result.length() != 0) {
                result.append(",");
            }
            result.append(pics.get(i).localPath);
        }
        return result.toString();
    }

    /**
     * 所有图片名
     * @return 通过，进行分割的所有图片名拼接成的字符串
     */
    public String getFileNames() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < pics.size(); i++) {
            if (result.length() != 0) {
                result.append(",");
            }
            String path = pics.get(i).localPath;
            result.append(pics.get(i).localPath.substring(path.lastIndexOf(File.separator) + 1));
        }
        return result.toString();
    }


    @Override
    public boolean isModified() {
        return GsonUtil.gsonString(pics).equals(originalPics);
    }
}
