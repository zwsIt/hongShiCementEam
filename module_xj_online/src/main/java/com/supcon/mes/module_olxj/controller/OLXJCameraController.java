package com.supcon.mes.module_olxj.controller;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;

import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.adapter.GalleryAdapter;
import com.supcon.mes.mbap.beans.GalleryBean;
import com.supcon.mes.mbap.utils.SheetUtil;
import com.supcon.mes.mbap.view.CustomGalleryView;
import com.supcon.mes.mbap.view.CustomSheetDialog;
import com.supcon.mes.middleware.controller.BaseCameraController;
import com.supcon.mes.middleware.model.event.ImageDeleteEvent;
import com.supcon.mes.middleware.util.FaultPicHelper;
import com.supcon.mes.module_olxj.model.bean.OLXJWorkItemEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangshizhan on 2019/3/8
 * Email:wangshizhan@supcom.com
 */
public class OLXJCameraController extends BaseCameraController {

    BaseListDataRecyclerViewAdapter<OLXJWorkItemEntity> mOLXJWorkListAdapter;
    CustomGalleryView currItemCustomGalleryView; // 单个viewHolder拍照控件
//    int itemPosition; // 单个viewHolder位置
//    public Map<String, CustomGalleryView> galleryViewHashMap  = new HashMap<String, CustomGalleryView>(); // map存储对应ViewHolder中的galleryView

    private int currAdapterPosition;

    public OLXJCameraController(View rootView) {
        super(rootView);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deleteImage(ImageDeleteEvent imageDeleteEvent) {
        CustomGalleryView customGalleryView = currItemCustomGalleryView;
        List<String> picStrs = FaultPicHelper.getImagePathList(customGalleryView.getGalleryAdapter().getList());
        int position = -1;
        boolean isMatch = false;
        for (int i = 0; !isMatch && picStrs.size() > 0; i++) {
            String name = picStrs.get(i);
            if (name.equals(imageDeleteEvent.getPicName())) {
                position = picStrs.indexOf(name);
                isMatch = true;
            }
        }

        if (position != -1) {
            customGalleryView.deletePic(position);
            deleteFile(imageDeleteEvent.getPicName());

            OLXJWorkItemEntity xjWorkItemEntity = mOLXJWorkListAdapter.getItem(currAdapterPosition);
            String xjImgUrl = xjWorkItemEntity.xjImgUrl;
            if (xjImgUrl.startsWith(imageDeleteEvent.getPicName())) {
                if (xjImgUrl.equals(imageDeleteEvent.getPicName())) {
                    xjWorkItemEntity.xjImgUrl = xjImgUrl.replace(imageDeleteEvent.getPicName(), "");
                } else {
                    xjWorkItemEntity.xjImgUrl = xjImgUrl.replace(imageDeleteEvent.getPicName() + ",", "");
                }
            } else {
                xjWorkItemEntity.xjImgUrl = xjImgUrl.replace("," + imageDeleteEvent.getPicName(), "");
            }
            if (TextUtils.isEmpty(xjWorkItemEntity.xjImgUrl)) {  //实际拍照字段还原
                xjWorkItemEntity.isPhonere = false;
            }

            mOLXJWorkListAdapter.notifyItemChanged(currAdapterPosition);

        }
    }

    public void addListener(CustomGalleryView customGalleryView, int position, BaseListDataRecyclerViewAdapter OLXJWorkListAdapter) {
        super.addListener(position, customGalleryView);
//        this.itemCustomGalleryView = customGalleryView;
//        this.itemPosition = position;
        mOLXJWorkListAdapter = OLXJWorkListAdapter;
//        galleryViewHashMap.put(String.valueOf(position),customGalleryView);
    }

    @Override
    protected void onFileReceived(File file) {

        if(currAdapterPosition == -1){
            LogUtil.e("adapterPosition == -1");
            return;
        }
        OLXJWorkItemEntity xjWorkItemEntity = mOLXJWorkListAdapter.getItem(currAdapterPosition);

        if(xjWorkItemEntity == null){
            return;
        }
        if (TextUtils.isEmpty(xjWorkItemEntity.xjImgUrl)) {
            xjWorkItemEntity.xjImgUrl = file.getAbsolutePath();
        } else {
            xjWorkItemEntity.xjImgUrl += ","+file.getAbsolutePath();
        }
        if (!TextUtils.isEmpty(xjWorkItemEntity.xjImgUrl)) {  //拍照时才可以修改(拍照但是又X掉，该字段为空)
            xjWorkItemEntity.isPhonere = true;
        }
        mOLXJWorkListAdapter.notifyItemChanged(currAdapterPosition);
    }

    @Override
    protected void onFileDelete(GalleryBean galleryBean, int position) {

        if(currAdapterPosition == -1){
            LogUtil.e("adapterPosition == -1");
            return;
        }

        OLXJWorkItemEntity xjWorkItemEntity = mOLXJWorkListAdapter.getItem(currAdapterPosition);
        if(xjWorkItemEntity == null){
            return;
        }
        currItemCustomGalleryView.deletePic(position);
        List<String> imgNamesList = Arrays.asList(xjWorkItemEntity.xjImgUrl.split(","));
        String xjImgUrl = xjWorkItemEntity.xjImgUrl;
        String imgName = imgNamesList.get(position);
        deleteFile(imgName);

        if (xjImgUrl.startsWith(imgName)) {
            if (xjImgUrl.equals(imgName)) {
                xjWorkItemEntity.xjImgUrl = xjImgUrl.replace(imgName, "");
            } else {
                xjWorkItemEntity.xjImgUrl = xjImgUrl.replace(imgName + ",", "");
            }
        } else {
            xjWorkItemEntity.xjImgUrl = xjImgUrl.replace("," + imgName, "");
        }
        if (TextUtils.isEmpty(xjWorkItemEntity.xjImgUrl)) {  //实际拍照字段还原
            xjWorkItemEntity.isPhonere = false;
        }

        mOLXJWorkListAdapter.notifyItemChanged(currAdapterPosition);
    }

    private static final String[] SHEET_ENTITY = {"拍摄照片", "拍摄短视频"};

    /**
     * 重写拍照选择方式：仅支持："拍摄照片", "拍摄短视频"
     */
    @SuppressLint("CheckResult")
    public void showCustomDialog() {
        new CustomSheetDialog(context)
                .sheet("请选择获取照片或视频的方式", SheetUtil.getSheetEntities(SHEET_ENTITY))
                .setOnItemChildViewClickListener((childView, position, action, obj) -> {

                    if(!check(position)){
                        return;
                    }

                    if (position == 0) {
                        startCamera();
                    }
                    else if(position == 1){
                        startVideo();
                    }
                }).show();
    }

    private boolean check(int position) {
        if(currItemCustomGalleryView.getGalleryAdapter().getItemCount() == 9){
            ToastUtils.show(context, "最多支持9张照片或视频！");
            return false;
        }

        if(checkVideo() && position == 1){
            ToastUtils.show(context, "最多支持录制1次视频！");
            return false;
        }

        return true;
    }
    private boolean checkVideo(){
        if(currItemCustomGalleryView.getGalleryAdapter().getItemCount()!=0)
            for(GalleryBean galleryBean: currItemCustomGalleryView.getGalleryAdapter().getList()){
                if(galleryBean.fileType == GalleryAdapter.FILE_TYPE_VIDEO){
                    return true;
                }
            }

        return false;
    }

    public void setCurrAdapterPosition(int adapterPosition, CustomGalleryView ufItemPics) {
        currAdapterPosition = adapterPosition;
        currItemCustomGalleryView = ufItemPics;
    }
}
