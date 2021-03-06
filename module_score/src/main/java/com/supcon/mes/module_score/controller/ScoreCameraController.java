package com.supcon.mes.module_score.controller;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;

import com.supcon.common.view.base.activity.BaseActivity;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.adapter.GalleryAdapter;
import com.supcon.mes.mbap.beans.GalleryBean;
import com.supcon.mes.mbap.utils.SheetUtil;
import com.supcon.mes.mbap.view.CustomGalleryView;
import com.supcon.mes.mbap.view.CustomSheetDialog;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.controller.AttachmentController;
import com.supcon.mes.middleware.controller.AttachmentDownloadController;
import com.supcon.mes.middleware.controller.BaseCameraController;
import com.supcon.mes.middleware.model.bean.AttachmentEntity;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.event.ImageDeleteEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.listener.OnAPIResultListener;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.util.PicUtil;
import com.supcon.mes.middleware.util.WatermarkUtil;
import com.supcon.mes.module_score.model.bean.ScoreEamPerformanceEntity;
import com.supcon.mes.module_score.model.bean.ScoreStaffPerformanceEntity;
import com.supcon.mes.module_score.ui.adapter.ScoreStaffPerformanceAdapter;
import com.supcon.mes.module_score.ui.adapter.ScoreStaffPerformanceNewAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.supcon.mes.mbap.adapter.GalleryAdapter.FILE_TYPE_VIDEO;

public class ScoreCameraController extends BaseCameraController {

    CustomGalleryView galleryView; // 当前操作项目

    private int currAdapterPosition; // 当前操作项目位置
    private BaseListDataRecyclerViewAdapter mScoreStaffPerformanceAdapter;

    private AttachmentController mAttachmentController;
    private AttachmentDownloadController mDownloadController;
    public List<AttachmentEntity> attachmentEntities;

    public Map<String, CustomGalleryView> galleryViewHashMap  = new HashMap<String, CustomGalleryView>(); // map存储对应ViewHolder中的galleryView
    public Map<String, OnSuccessListener<File>> onSuccessListenerHashMap  = new HashMap<String, OnSuccessListener<File>>(); // map存储对应ViewHolder中的listener

    public ScoreCameraController(View rootView) {
        super(rootView);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onInit() {
        super.onInit();
    }

    public void addGalleryView(int position, CustomGalleryView customGalleryView, BaseListDataRecyclerViewAdapter baseListDataRecyclerViewAdapter){
        super.addListener(position, customGalleryView);
        mScoreStaffPerformanceAdapter = baseListDataRecyclerViewAdapter;
//        currAdapterPosition = position;
//        galleryView = customGalleryView;
        galleryViewHashMap.put(String.valueOf(position),customGalleryView);
    }

    public void setOnSuccessListener(int position, OnSuccessListener<File> onSuccessListener) {
//        super.setOnSuccessListener(onSuccessListener);
        onSuccessListenerHashMap.put(String.valueOf(position),onSuccessListener);
    }

    /**
     * 加载照片
     * @param attachmentEntities
     */
    public void setPicData(List<AttachmentEntity> attachmentEntities, String entityCode){
        if(attachmentEntities == null ){
            return;
        }

        this.attachmentEntities = attachmentEntities;
        if(galleryView == null){
            LogUtil.e("You should init GalleryView first!");
            return;
        }
        if(dir == null)
        {
            throw new IllegalArgumentException("You should call init first!");
        }

        if(mDownloadController == null){
            mDownloadController = new AttachmentDownloadController(dir);
        }

        mDownloadController.downloadPic(attachmentEntities, entityCode, new OnSuccessListener<List<GalleryBean>>() {
            @Override
            public void onSuccess(List<GalleryBean> result) {
                galleryView.setGalleryBeans(result);
            }
        });
    }

    @Override
    public void initView() {
        super.initView();
        initAttachmentController();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if(mDownloadController!=null)
            mDownloadController.dispose();
    }

    private void initAttachmentController() {
        mAttachmentController = new AttachmentController();

    }

    @Override
    public void initListener() {
        super.initListener();
//        addListener(0, galleryView);
    }

    @Override
    protected void onFileDelete(GalleryBean galleryBean, int position) {
        super.onFileDelete(galleryBean, position);
        deleteGalleryBean(galleryBean, position);
        EventBus.getDefault().post(new RefreshEvent());
    }

    @Override
    protected void onFileReceived(File file) {
        super.onFileReceived(file);


        String fileName = file.getName();
        if(PicUtil.isPic(fileName)){
            uploadLocalPic(file);
        }
        else if(PicUtil.isVideo(fileName)){
            uploadLocalVideo(file);
        }
    }

    @Override
    public void deleteGalleryBean(GalleryBean galleryBean, int position) {
        super.deleteGalleryBean(galleryBean, position);
        actionGalleryView.deletePic(position);
        galleryView = actionGalleryView;
        currAdapterPosition = actionPosition;
//        galleryViewHashMap.get(String.valueOf(currAdapterPosition)).deletePic(position);
        delete(galleryBean);
        updateScoreItem(new File(galleryBean.localPath));
        // 附件删除监听
//        if(onSuccessListenerHashMap.get(String.valueOf(actionPosition))!= null){
//            onSuccessListenerHashMap.get(String.valueOf(actionPosition)).onSuccess(new File(galleryBean.localPath));
//        }
    }

    private void delete(GalleryBean galleryBean) {
        if (pics.contains(galleryBean)) {
            pics.remove(galleryBean);
//            return;
        }

        String picPath = galleryBean.url;

        if (attachmentEntities != null)
            for (AttachmentEntity attachmentEntity : attachmentEntities) {
                if (attachmentEntity.path.equals(picPath)) {
                    mAttachmentController.deleteAttachment(new OnAPIResultListener<BapResultEntity>() {
                        @Override
                        public void onFail(String errorMsg) {
                            ToastUtils.show(context, "删除附件失败！");
                        }

                        @Override
                        public void onSuccess(BapResultEntity result) {
                            ToastUtils.show(context, "删除附件成功！");
                        }
                    }, attachmentEntity.id);
                }
            }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deleteImage(ImageDeleteEvent imageDeleteEvent) {
        actionGalleryView.deletePic(imageDeleteEvent.getPos());
        galleryView = actionGalleryView;
        currAdapterPosition = actionPosition;
        updateScoreItem(new File(imageDeleteEvent.getPicName()));
        // 附件删除监听
//        if(onSuccessListenerHashMap.get(String.valueOf(actionPosition))!= null){
//            onSuccessListenerHashMap.get(String.valueOf(actionPosition)).onSuccess(new File(imageDeleteEvent.getPicName())); // 本地照片路径
//        }
    }


    private void uploadLocalPic(File file) {
        ((BaseActivity)activity).onLoading("正在上传照片...");
        // 添加名称水印
        WatermarkUtil.makeText(context,EamApplication.getAccountInfo().staffName, file);

        mAttachmentController.uploadAttachment(new OnAPIResultListener<String>() {
            @Override
            public void onFail(String errorMsg) {
                ((BaseActivity)activity).onLoadFailed("照片上传失败！");
            }

            @Override
            public void onSuccess(String result) {
                ((BaseActivity)activity).onLoadSuccess("照片上传成功！");
                GalleryBean galleryBean = new GalleryBean();
                galleryBean.localPath = file.getAbsolutePath();
                galleryBean.url = result;
//                CustomGalleryView videoGalleryView = galleryViewHashMap.get(String.valueOf(currAdapterPosition));
//                videoGalleryView.clear(); // 支持单个
//                videoGalleryView.addGalleryBean(galleryBean);
                galleryView.addGalleryBean(galleryBean);
                pics.add(galleryBean);
//                if(onSuccessListenerHashMap.get(String.valueOf(currAdapterPosition))!= null){
//                    onSuccessListenerHashMap.get(String.valueOf(currAdapterPosition)).onSuccess(file);
//                }

                updateScoreItem(file);

            }
        }, file);
    }

    private void uploadLocalVideo(File file){
        ((BaseActivity)activity).onLoading("正在上传视频...");
        mAttachmentController.uploadAttachment(new OnAPIResultListener<String>() {
            @Override
            public void onFail(String errorMsg) {
                ((BaseActivity)activity).onLoadFailed("视频上传失败！");
            }

            @Override
            public void onSuccess(String result) {
                ((BaseActivity)activity).onLoadSuccess("视频上传成功！");
                GalleryBean galleryBean = new GalleryBean();
                galleryBean.fileType = FILE_TYPE_VIDEO;
                galleryBean.localPath = file.getAbsolutePath();
//                galleryBean.thumbnailPath = thumbnail.getAbsolutePath();
                galleryBean.url = result;
//                CustomGalleryView videoGalleryView = galleryViewHashMap.get(String.valueOf(currAdapterPosition));
//                videoGalleryView.clear(); // 支持单个
                galleryView.addGalleryBean(galleryBean);
//                galleryView.addGalleryBean(galleryBean);
                pics.add(galleryBean);
//                if(onSuccessListenerHashMap.get(String.valueOf(currAdapterPosition))!= null){
//                    onSuccessListenerHashMap.get(String.valueOf(currAdapterPosition)).onSuccess(file);
//                }
                updateScoreItem(file);
            }
        }, file);
    }

     /**
      * @method  更新评分项数据
      * @description
      * @author: zhangwenshuai
      * @date: 2020/6/19 22:09
      * @param  * @param null
      * @param file
      * @return
      */
    private void updateScoreItem(File file) {

        if (mScoreStaffPerformanceAdapter.getItem(currAdapterPosition) instanceof ScoreStaffPerformanceEntity){
            ScoreStaffPerformanceEntity data = (ScoreStaffPerformanceEntity) mScoreStaffPerformanceAdapter.getItem(currAdapterPosition);
            StringBuilder picPath = new StringBuilder();
            List<String> picPathList = new ArrayList<>();
            StringBuilder picLocalPaths = new StringBuilder();
            for (GalleryBean galleryBean : galleryView.getGalleryAdapter().getList()) { // 控件展示附件
                if (!TextUtils.isEmpty(galleryBean.url)) {
                    picPath.append(galleryBean.url).append(",");
                    picPathList.add(galleryBean.url);
                }
                picLocalPaths.append(galleryBean.localPath).append(",");
            }
            if (!picLocalPaths.toString().contains(file.getName())) {
                // 删除
                if (picLocalPaths.length() == 0) {
                    data.setAttachFileFileAddPaths(null);
                    if (data.getAttachFileMultiFileIds().size() > 0) {
                        data.setAttachFileFileDeleteIds(data.getAttachFileMultiFileIds());
                    }
                } else {
                    // 删除已保存
                    if (data.getAttachFileMultiFileIds() != null && data.getAttachFileMultiFileNames().contains(file.getName())){
                        data.getAttachFileFileDeleteIds().add(data.getAttachFileMultiFileIds().get(data.getAttachFileMultiFileNames().indexOf(file.getName())));
//                    List<String> attachFileIdList = Arrays.asList(data.getAttachFileMultiFileIds().split(","));
//                    List<String> attachFileNameList = Arrays.asList(data.getAttachFileMultiFileNames().split(","));
//                    for (String name : attachFileNameList){
//                        if (name.contains(file.getName())){
////                          String fileFileDeleteIds = data.getAttachFileFileDeleteIds() == null ? "" : data.getAttachFileFileDeleteIds();
//                            data.setAttachFileFileDeleteIds(TextUtils.isEmpty(data.getAttachFileFileDeleteIds()) ?
//                            attachFileIdList.get(attachFileNameList.indexOf(name)) : data.getAttachFileFileDeleteIds() + ","+ attachFileIdList.get(attachFileNameList.indexOf(name)));
//                            break;
//                        }
//                    }
                    }else { // 本地删除
//                    ArrayList<String> attachFileFileAddPathsList = new ArrayList<>(Arrays.asList(data.getAttachFileFileAddPaths().split(",")));
                        for (String path : data.getAttachFileFileAddPaths()){
                            if (path.contains(file.getName())){
//                            attachFileFileAddPathsList.remove(path);
                                data.getAttachFileFileAddPaths().remove(path);
                                break;
                            }
                        }
                    }
                }
            } else {
                //添加
//            data.setAttachFileFileAddPaths(picPath.substring(0, picPath.length() - 1));
                data.setAttachFileFileAddPaths(picPathList);
            }
        }else if (mScoreStaffPerformanceAdapter.getItem(currAdapterPosition) instanceof ScoreEamPerformanceEntity){
            ScoreEamPerformanceEntity data = (ScoreEamPerformanceEntity) mScoreStaffPerformanceAdapter.getItem(currAdapterPosition);
            StringBuilder picPath = new StringBuilder();
            List<String> picPathList = new ArrayList<>();
            StringBuilder picLocalPaths = new StringBuilder();
            for (GalleryBean galleryBean : galleryView.getGalleryAdapter().getList()) { // 控件展示附件
                if (!TextUtils.isEmpty(galleryBean.url)) {
                    picPath.append(galleryBean.url).append(",");
                    picPathList.add(galleryBean.url);
                }
                picLocalPaths.append(galleryBean.localPath).append(",");
            }
            if (!picLocalPaths.toString().contains(file.getName())) {
                // 删除
                if (picLocalPaths.length() == 0) {
                    data.setAttachFileFileAddPaths(null);
                    if (data.getAttachFileMultiFileIds().size() > 0) {
                        data.setAttachFileFileDeleteIds(data.getAttachFileMultiFileIds());
                    }
                } else {
                    // 删除已保存
                    if (data.getAttachFileMultiFileIds() != null && data.getAttachFileMultiFileNames().contains(file.getName())){
                        data.getAttachFileFileDeleteIds().add(data.getAttachFileMultiFileIds().get(data.getAttachFileMultiFileNames().indexOf(file.getName())));
//                    List<String> attachFileIdList = Arrays.asList(data.getAttachFileMultiFileIds().split(","));
//                    List<String> attachFileNameList = Arrays.asList(data.getAttachFileMultiFileNames().split(","));
//                    for (String name : attachFileNameList){
//                        if (name.contains(file.getName())){
////                          String fileFileDeleteIds = data.getAttachFileFileDeleteIds() == null ? "" : data.getAttachFileFileDeleteIds();
//                            data.setAttachFileFileDeleteIds(TextUtils.isEmpty(data.getAttachFileFileDeleteIds()) ?
//                            attachFileIdList.get(attachFileNameList.indexOf(name)) : data.getAttachFileFileDeleteIds() + ","+ attachFileIdList.get(attachFileNameList.indexOf(name)));
//                            break;
//                        }
//                    }
                    }else { // 本地删除
//                    ArrayList<String> attachFileFileAddPathsList = new ArrayList<>(Arrays.asList(data.getAttachFileFileAddPaths().split(",")));
                        for (String path : data.getAttachFileFileAddPaths()){
                            if (path.contains(file.getName())){
//                            attachFileFileAddPathsList.remove(path);
                                data.getAttachFileFileAddPaths().remove(path);
                                break;
                            }
                        }
                    }
                }
            } else {
                //添加
//            data.setAttachFileFileAddPaths(picPath.substring(0, picPath.length() - 1));
                data.setAttachFileFileAddPaths(picPathList);
            }
        }
    }

    @Override
    public boolean doSave(Map<String, Object> attachmentMap) {
        List<Map<String, Object>> files = new ArrayList<>();
        for (GalleryBean galleryBean : pics) {
            Map<String, Object> file = new HashMap<>();
            file.put("file.filePath", galleryBean.url);
            file.put("file.name", galleryBean.url.substring(galleryBean.url.lastIndexOf("\\") + 1));
            file.put("file.memos", "pda拍照上传");
            file.put("file.fileType", "attachment");
            files.add(file);
        }

        if (pics.size() != 0) {
            attachmentMap.put("file.staffId", String.valueOf(EamApplication.getAccountInfo().staffId));
            attachmentMap.put("file.type", "Table");
            attachmentMap.put("files", files);
        }
        return true;
    }

    @Override
    public boolean isModified() {
        return pics.size()!=0;
    }

    public void setCurrAdapterPosition(int adapterPosition, CustomGalleryView itemPics) {
        this.currAdapterPosition = adapterPosition;
        galleryView = itemPics;
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
        CustomGalleryView galleryViewLocal = galleryView /*galleryViewHashMap.get(String.valueOf(currAdapterPosition))*/;
        if(galleryViewLocal.getGalleryAdapter().getItemCount() == 9){
            ToastUtils.show(context, "最多支持9张照片或视频！");
            return false;
        }

        if(checkVideo(galleryViewLocal) && position == 1){
            ToastUtils.show(context, "最多支持录制1次视频！");
            return false;
        }

        return true;
    }
    private boolean checkVideo(CustomGalleryView galleryViewLocal){
        if(galleryViewLocal.getGalleryAdapter().getItemCount()!=0)
            for(GalleryBean galleryBean: galleryViewLocal.getGalleryAdapter().getList()){
                if(galleryBean.fileType == GalleryAdapter.FILE_TYPE_VIDEO){
                    return true;
                }
            }

        return false;
    }

}
