package com.supcon.mes.module_overhaul_workticket.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomGalleryView;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.AttachmentController;
import com.supcon.mes.middleware.controller.AttachmentDownloadController;
import com.supcon.mes.middleware.model.bean.AttachmentEntity;
import com.supcon.mes.middleware.model.bean.AttachmentListEntity;
import com.supcon.mes.middleware.model.listener.OnAPIResultListener;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.module_overhaul_workticket.R;
import com.supcon.mes.module_overhaul_workticket.constant.OperateType;
import com.supcon.mes.module_overhaul_workticket.controller.WorkTicketCameraController;
import com.supcon.mes.module_overhaul_workticket.model.bean.SafetyMeasuresEntity;
import com.supcon.mes.module_overhaul_workticket.ui.activity.WorkTicketEditActivity;
import com.supcon.mes.module_overhaul_workticket.ui.activity.WorkTicketViewActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/11
 * Email zhangwenshuai1@supcon.com
 * Desc 查看adapter
 */
public class SafetyMeasuresViewAdapter extends BaseListDataRecyclerViewAdapter<SafetyMeasuresEntity> {

    private WorkTicketCameraController workTicketCameraController;
    private AttachmentDownloadController mDownloadController;
    private Long eleApplyTableInfoId; // 停电单据tableInfoId

    public SafetyMeasuresViewAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position, SafetyMeasuresEntity safetyMeasuresEntity) {
        return safetyMeasuresEntity.getOperateType();
    }

    @Override
    protected BaseRecyclerViewHolder<SafetyMeasuresEntity> getViewHolder(int viewType) {
        if (viewType == OperateType.VIDEO.getType()){
            return new VideoSafetyMeasViewHolder(context);
        }else if (viewType == OperateType.PHOTO.getType()){
            return new PhotoSafetyMeasViewHolder(context);
        }else if (viewType == OperateType.NFC.getType()){
            return new NFCSafetyMeasViewHolder(context);
        }else {
            return new ConfirmSafetyMeasViewHolder(context);
        }
    }

    public void initCamera(){
        if (context instanceof WorkTicketEditActivity){
            workTicketCameraController = ((WorkTicketEditActivity)context).getController(WorkTicketCameraController.class);
        }else if (context instanceof WorkTicketViewActivity){
            workTicketCameraController = ((WorkTicketViewActivity)context).getController(WorkTicketCameraController.class);
        }
        workTicketCameraController.init(Constant.IMAGE_SAVE_WORKTICKETPATH, Constant.PicType.WORK_TICKET_PIC);
    }

    public void setEleOffTableInfoId(Long eleApplyTableInfoId){
        this.eleApplyTableInfoId = eleApplyTableInfoId;
    }

    private class ConfirmSafetyMeasViewHolder extends BaseRecyclerViewHolder<SafetyMeasuresEntity> {
        @BindByTag("index")
        TextView index;
        @BindByTag("content")
        CustomTextView content;
        @BindByTag("chkBox")
        CheckBox chkBox;
        @BindByTag("elePhotoGalleryView")
        CustomGalleryView elePhotoGalleryView;

        private AttachmentController attachmentController;
        private AttachmentDownloadController attachmentDownloadController;

        public ConfirmSafetyMeasViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_safety_measure;
        }

        @Override
        protected void initView() {
            super.initView();
            chkBox.setClickable(false);
        }

        @Override
        protected void initListener() {
            super.initListener();
            chkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                SafetyMeasuresEntity entity = getItem(getAdapterPosition());
                entity.setIsExecuted(isChecked);

                onItemChildViewClick(chkBox,0,entity);
            });
        }

        @Override
        protected void update(SafetyMeasuresEntity data) {
            index.setText(String.valueOf(getAdapterPosition() + 1));
            content.setContent(data.getSafetyMeasure());
            chkBox.setChecked(data.isIsExecuted());
            if (data.isIsExecuted()){
                chkBox.setVisibility(View.VISIBLE);
                chkBox.setButtonDrawable(null);
                chkBox.setBackgroundResource(R.drawable.ic_checked);
            }else {
                chkBox.setVisibility(View.GONE);
                chkBox.setButtonDrawable(null);
//                chkBox.setBackgroundResource(R.drawable.ic_unhecked);
            }

            // 加载停电照片附件
            workTicketCameraController.addGalleryView(getAdapterPosition(),elePhotoGalleryView);
            initEleOffAttachment(data);
        }
        /**
         * 加载停电照片附件
         * @param data
         */
        private void initEleOffAttachment(SafetyMeasuresEntity data) {
            if (getAdapterPosition() == 2 && eleApplyTableInfoId != -1){
                if (attachmentController == null){
                    attachmentController = new AttachmentController();
                }
                if (attachmentDownloadController == null){
                    attachmentDownloadController = new AttachmentDownloadController(Constant.IMAGE_SAVE_WORKTICKETPATH);
                }
                if (data.getEleOffAttachmentEntityList() != null && data.getEleOffAttachmentEntityList().size() > 0){
                    attachmentDownloadController.downloadPic(data.getEleOffAttachmentEntityList(), "BEAMEle_1.0.0_onOrOff", result1 -> elePhotoGalleryView.setGalleryBeans(result1));
                }else {
                    attachmentController.refreshGalleryView(new OnAPIResultListener<AttachmentListEntity>() {
                        @Override
                        public void onFail(String errorMsg) {}

                        @Override
                        public void onSuccess(AttachmentListEntity result) {
                            if (result.result.size() > 0){
                                data.setEleOffAttachmentEntityList(result.result);
                                attachmentDownloadController.downloadPic(result.result, "BEAMEle_1.0.0_onOrOff", result1 -> elePhotoGalleryView.setGalleryBeans(result1));
                            }
                        }
                    }, eleApplyTableInfoId);
                }

            }else {
                elePhotoGalleryView.clear();
            }
        }

    }
    private class VideoSafetyMeasViewHolder extends BaseRecyclerViewHolder<SafetyMeasuresEntity> {
        @BindByTag("index")
        TextView index;
        @BindByTag("content")
        CustomTextView content;
        @BindByTag("videoIvLl")
        LinearLayout videoIvLl;
        @BindByTag("videoIv")
        ImageView videoIv;
        @BindByTag("videoGalleryView")
        CustomGalleryView videoGalleryView;

        public VideoSafetyMeasViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_video_safety_measure;
        }

        @Override
        protected void initView() {
            super.initView();
//            videoIvLl.setVisibility(View.GONE);
            videoIv.setVisibility(View.GONE);
            videoGalleryView.setEditable(false);
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(videoIv)
                    .throttleFirst(300, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
//                            onItemChildViewClick(videoGalleryView,0,getItem(getAdapterPosition()));
                            workTicketCameraController.setCurrAdapterPosition(getAdapterPosition());
                            if (videoGalleryView.getGalleryAdapter() != null && videoGalleryView.getGalleryAdapter().getItemCount() == 1){
                                new CustomDialog(context).twoButtonAlertDialog("只支持一个附件,是否重拍?")
                                        .bindClickListener(R.id.redBtn, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                workTicketCameraController.startVideo();
                                            }
                                        }, true)
                                        .bindClickListener(R.id.grayBtn, null,true).show();
                            }else {
                                workTicketCameraController.startVideo();
                            }
                        }
                    });
        }

        @Override
        protected void update(SafetyMeasuresEntity data) {
            index.setText(String.valueOf(getAdapterPosition() + 1));
            content.setContent(data.getSafetyMeasure());

            // 初始化事件
            setViewAndListener(getAdapterPosition(),videoGalleryView);

            // 初始化附件
            if (!TextUtils.isEmpty(data.getAttachFileMultiFileIds())){
                if(mDownloadController == null){
                    mDownloadController = new AttachmentDownloadController(Constant.IMAGE_SAVE_WORKTICKETPATH);
                }
                List<AttachmentEntity> attachmentEntities = new ArrayList<>();
                AttachmentEntity attachmentEntity = new AttachmentEntity();
                attachmentEntity.id = Long.parseLong(data.getAttachFileMultiFileIds());
                attachmentEntity.name = data.getAttachFileMultiFileNames();
                attachmentEntity.deploymentId = attachmentEntity.id; // 赋值附件id,防止下载被过滤
                attachmentEntities.add(attachmentEntity);
                mDownloadController.downloadPic(attachmentEntities,"WorkTicket_8.20.3.03_workTicket" , result -> videoGalleryView.setGalleryBeans(result));
            }
        }
    }
    private class PhotoSafetyMeasViewHolder extends BaseRecyclerViewHolder<SafetyMeasuresEntity> {
        @BindByTag("index")
        TextView index;
        @BindByTag("content")
        CustomTextView content;
        @BindByTag("photoIvLl")
        LinearLayout photoIvLl;
        @BindByTag("photoIv")
        ImageView photoIv;
        @BindByTag("photoGalleryView")
        CustomGalleryView photoGalleryView;

        public PhotoSafetyMeasViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_photo_safety_measure;
        }

        @Override
        protected void initView() {
            super.initView();
            photoIv.setVisibility(View.GONE);
            photoGalleryView.setEditable(false);

        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(photoIv)
                    .throttleFirst(300, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
//                            onItemChildViewClick(videoGalleryView,0,getItem(getAdapterPosition()));
                            workTicketCameraController.setCurrAdapterPosition(getAdapterPosition());
                            if (photoGalleryView.getGalleryAdapter() != null && photoGalleryView.getGalleryAdapter().getItemCount() == 1){
                                new CustomDialog(context).twoButtonAlertDialog("只支持一个附件,是否重拍?")
                                        .bindClickListener(R.id.redBtn, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                workTicketCameraController.startVideo();
                                            }
                                        }, true)
                                        .bindClickListener(R.id.grayBtn, null,true).show();
                            }else {
                                workTicketCameraController.startVideo();
                            }
                        }
                    });
        }

        @Override
        protected void update(SafetyMeasuresEntity data) {
            index.setText(String.valueOf(getAdapterPosition() + 1));
            content.setContent(data.getSafetyMeasure());

            // 初始化事件
            setViewAndListener(getAdapterPosition(),photoGalleryView);

            // 初始化附件
            if (!TextUtils.isEmpty(data.getAttachFileMultiFileIds())){
                if(mDownloadController == null){
                    mDownloadController = new AttachmentDownloadController(Constant.IMAGE_SAVE_WORKTICKETPATH);
                }
                List<AttachmentEntity> attachmentEntities = new ArrayList<>();
                AttachmentEntity attachmentEntity = new AttachmentEntity();
                attachmentEntity.id = Long.parseLong(data.getAttachFileMultiFileIds());
                attachmentEntity.name = data.getAttachFileMultiFileNames();
                attachmentEntity.deploymentId = attachmentEntity.id; // 赋值附近id,防止下载过滤
                attachmentEntities.add(attachmentEntity);
                mDownloadController.downloadPic(attachmentEntities,"WorkTicket_8.20.3.03_workTicket" , result -> photoGalleryView.setGalleryBeans(result));
            }
        }
    }
    private class NFCSafetyMeasViewHolder extends BaseRecyclerViewHolder<SafetyMeasuresEntity> {
        @BindByTag("index")
        TextView index;
        @BindByTag("content")
        CustomTextView content;

        public NFCSafetyMeasViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_nfc_safety_measure;
        }

        @Override
        protected void update(SafetyMeasuresEntity data) {
            index.setText(String.valueOf(getAdapterPosition() + 1));
            content.setContent(data.getSafetyMeasure());
        }
    }

    public void setViewAndListener(int adapterPosition, CustomGalleryView galleryView){
        workTicketCameraController.addGalleryView(adapterPosition,galleryView);
        workTicketCameraController.setOnSuccessListener(adapterPosition,new OnSuccessListener<File>() {
            @Override
            public void onSuccess(File file) {
//                    videoIv.setVisibility(View.GONE);
                SafetyMeasuresEntity data = getItem(adapterPosition);
                if ("videoGalleryView".equals(galleryView.getTag().toString())){  // 视频:只有一个
                    if (galleryView.getGalleryAdapter().getItemCount() == 1){
                        data.setIsExecuted(true);
                        data.setAttachFileFileAddPaths(galleryView.getGalleryAdapter().getItem(0).url);
                    }else {
                        data.setIsExecuted(false);
                        data.setAttachFileFileAddPaths(null);
                        // 删除视频
                        if (!TextUtils.isEmpty(data.getAttachFileMultiFileIds())){
                            data.setAttachFileFileDeleteIds(data.getAttachFileMultiFileIds());
                        }
                    }
                }else if ("photoGalleryView".equals(galleryView.getTag().toString())){  // 拍照:只有一个

                    if (galleryView.getGalleryAdapter().getItemCount() == 1){
                        data.setIsExecuted(true);
                        data.setAttachFileFileAddPaths(galleryView.getGalleryAdapter().getItem(0).url);
                    }else {
                        data.setIsExecuted(false);
                        data.setAttachFileFileAddPaths(null);
                        // 删除图片
                        if (!TextUtils.isEmpty(data.getAttachFileMultiFileIds())){
                            data.setAttachFileFileDeleteIds(data.getAttachFileMultiFileIds());
                        }
                    }

//                    StringBuilder picPath = new StringBuilder();
//                    for (GalleryBean galleryBean :galleryView.getGalleryAdapter().getList()){
//                        picPath.append(galleryBean.url).append(",");
//                    }
//                    if (!picPath.toString().contains(file.getName())){
//                        // 删除
//                        if (picPath.length() == 0 ){
//                            data.setIsExecuted(false);
//                            data.setAttachFileFileAddPaths(null);
//                            if (!TextUtils.isEmpty(data.getAttachFileMultiFileIds())){
//                                data.setAttachFileFileDeleteIds(data.getAttachFileMultiFileIds());
//                            }
//                        }else {
//                            //TODO...
//                        }
//                    }else {
//                        //添加
//                        data.setIsExecuted(true);
//                        data.setAttachFileFileAddPaths(picPath.substring(0,picPath.length()-1));
//                    }
                }

            }
        });
    }

}
