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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.beans.GalleryBean;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/11
 * Email zhangwenshuai1@supcon.com
 * Desc 编辑adapter
 */
public class SafetyMeasuresAdapter extends BaseListDataRecyclerViewAdapter<SafetyMeasuresEntity> {

    private WorkTicketCameraController workTicketCameraController;

    private Long eleApplyTableInfoId; // 停电单据tableInfoId

    public SafetyMeasuresAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position, SafetyMeasuresEntity safetyMeasuresEntity) {
        return safetyMeasuresEntity.getOperateType();
    }

    @Override
    protected BaseRecyclerViewHolder<SafetyMeasuresEntity> getViewHolder(int viewType) {
        if (viewType == OperateType.VIDEO.getType()) {
            return new VideoSafetyMeasViewHolder(context);
        } else if (viewType == OperateType.PHOTO.getType()) {
            return new PhotoSafetyMeasViewHolder(context);
        } else if (viewType == OperateType.NFC.getType()) {
            return new NFCSafetyMeasViewHolder(context);
        } else {
            return new ConfirmSafetyMeasViewHolder(context);
        }
    }

    public void initCamera() {
        if (context instanceof WorkTicketEditActivity) {
            workTicketCameraController = ((WorkTicketEditActivity) context).getController(WorkTicketCameraController.class);
        } else if (context instanceof WorkTicketViewActivity) {
            workTicketCameraController = ((WorkTicketViewActivity) context).getController(WorkTicketCameraController.class);
        }
        workTicketCameraController.init(Constant.IMAGE_SAVE_WORKTICKETPATH, Constant.PicType.WORK_TICKET_PIC);
    }

    public void setEleOffTableInfoId(Long eleApplyTableInfoId) {
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

        ImageView gifIv;

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
            gifIv = itemView.findViewById(R.id.gifIv);
        }

        @Override
        protected void initListener() {
            super.initListener();
            chkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                SafetyMeasuresEntity entity = getItem(getAdapterPosition());
                entity.setIsExecuted(isChecked);

                onItemChildViewClick(chkBox, 0, entity);
            });
        }

        @Override
        protected void update(SafetyMeasuresEntity data) {
            index.setText(String.valueOf(getAdapterPosition() + 1));
            content.setContent(data.getSafetyMeasure());
            chkBox.setChecked(data.isIsExecuted());
            // 加载停电照片附件
            if (getAdapterPosition() == 1 && eleApplyTableInfoId != -1) {
                gifIv.setVisibility(View.VISIBLE);
                Glide.with(context).asGif().load(R.drawable.preloader_3).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).into(gifIv);
                workTicketCameraController.addGalleryView(getAdapterPosition(), elePhotoGalleryView);
                initEleOffAttachment(data);
            } else {
                gifIv.setVisibility(View.GONE);
                elePhotoGalleryView.clear();
            }

        }

        /**
         * 加载停电照片附件
         *
         * @param data
         */
        private void initEleOffAttachment(SafetyMeasuresEntity data) {

            if (attachmentController == null) {
                attachmentController = new AttachmentController();
            }
            if (attachmentDownloadController == null) {
                attachmentDownloadController = new AttachmentDownloadController(Constant.IMAGE_SAVE_ELE_PATH);
            }
            if (data.getEleOffAttachmentEntityList() != null) {
                downloadPic(data.getEleOffAttachmentEntityList());

            } else {
                attachmentController.refreshGalleryView(new OnAPIResultListener<AttachmentListEntity>() {
                    @Override
                    public void onFail(String errorMsg) {
                        ToastUtils.show(context,errorMsg);
                        gifIv.setImageResource(R.drawable.ic_error);
                    }

                    @Override
                    public void onSuccess(AttachmentListEntity result) {
                        if (result.result.size() > 0) {
                            data.setEleOffAttachmentEntityList(result.result);
                            downloadPic(result.result);
                        }else {
                            // 无附件图片
                            gifIv.setImageResource(R.drawable.ic_no_file);
                        }
                    }
                }, eleApplyTableInfoId);
            }
        }

        private void downloadPic(List<AttachmentEntity> eleOffAttachmentEntityList) {
            attachmentDownloadController.downloadPic(eleOffAttachmentEntityList, "BEAMEle_1.0.0_onOrOff", result1 -> {
                gifIv.setVisibility(View.GONE);
                elePhotoGalleryView.setGalleryBeans(result1);
            });
        }

    }

    private class VideoSafetyMeasViewHolder extends BaseRecyclerViewHolder<SafetyMeasuresEntity> {
        private AttachmentDownloadController mDownloadController;
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

        ImageView gifIv;

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
            gifIv = itemView.findViewById(R.id.gifIv);
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
                            workTicketCameraController.showCustomDialog();
//                            videoGalleryView.findViewById(R.id.customCameraIv).performClick();
//                            if (videoGalleryView.getGalleryAdapter() != null && videoGalleryView.getGalleryAdapter().getItemCount() == 1) {
//                                new CustomDialog(context).twoButtonAlertDialog("只支持一个附件,是否重拍?")
//                                        .bindClickListener(R.id.redBtn, new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                workTicketCameraController.startVideo();
//                                            }
//                                        }, true)
//                                        .bindClickListener(R.id.grayBtn, null, true).show();
//                            } else {
//                                workTicketCameraController.startVideo();
//                            }
                        }
                    });
        }

        @Override
        protected void update(SafetyMeasuresEntity data) {
            index.setText(String.valueOf(getAdapterPosition() + 1));
            content.setContent(data.getSafetyMeasure());
            // 初始化事件
            setViewAndListener(getAdapterPosition(), videoGalleryView);
            // 初始化附件
            initAttachFiles(data, mDownloadController, videoGalleryView, gifIv);
        }
    }

    private class PhotoSafetyMeasViewHolder extends BaseRecyclerViewHolder<SafetyMeasuresEntity> {
        private AttachmentDownloadController mDownloadController;
        @BindByTag("index")
        TextView index;
        @BindByTag("content")
        CustomTextView content;
        @BindByTag("photoIv")
        ImageView photoIv;
        @BindByTag("photoGalleryView")
        CustomGalleryView photoGalleryView;

        ImageView gifIv;

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
            gifIv = itemView.findViewById(R.id.gifIv);
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
                            workTicketCameraController.showCustomDialog();
//                            photoGalleryView.findViewById(R.id.customCameraIv).performClick();
//                            if (photoGalleryView.getGalleryAdapter() != null && photoGalleryView.getGalleryAdapter().getItemCount() == 1) {
//                                new CustomDialog(context).twoButtonAlertDialog("只支持一个附件,是否重拍?")
//                                        .bindClickListener(R.id.redBtn, new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                workTicketCameraController.startCamera();
//                                            }
//                                        }, true)
//                                        .bindClickListener(R.id.grayBtn, null, true).show();
//                            } else {
//                                workTicketCameraController.startCamera();
//                            }
                        }
                    });
        }

        @Override
        protected void update(SafetyMeasuresEntity data) {
            index.setText(String.valueOf(getAdapterPosition() + 1));
            content.setContent(data.getSafetyMeasure());
            // 初始化事件
            setViewAndListener(getAdapterPosition(), photoGalleryView);

            // 初始化附件
            initAttachFiles(data, mDownloadController, photoGalleryView, gifIv);
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

    /**
     * @param
     * @return
     * @description 初始化相机事件
     * @author zhangwenshuai1 2019/12/28
     */
    public void setViewAndListener(int adapterPosition, CustomGalleryView galleryView) {
        workTicketCameraController.addGalleryView(adapterPosition, galleryView);
        workTicketCameraController.setOnSuccessListener(adapterPosition, file -> {
//                    videoIv.setVisibility(View.GONE);
            SafetyMeasuresEntity data = getItem(adapterPosition);
                StringBuilder picPath = new StringBuilder();
                for (GalleryBean galleryBean : galleryView.getGalleryAdapter().getList()) {
                    if (!TextUtils.isEmpty(galleryBean.url)) {
                        picPath.append(galleryBean.url).append(",");
                    }
                }
                if (!picPath.toString().contains(file.getName())) {
                    // 删除
                    if (picPath.length() == 0) {
                        data.setIsExecuted(false);
                        data.setAttachFileFileAddPaths(null);
                        if (!TextUtils.isEmpty(data.getAttachFileMultiFileIds())) {
                            data.setAttachFileFileDeleteIds(data.getAttachFileMultiFileIds());
                        }
                    } else {
                        List<String> attachFileIdList = Arrays.asList(data.getAttachFileMultiFileIds().split(","));
                        List<String> attachFileNameList = Arrays.asList(data.getAttachFileMultiFileNames().split(","));
                        List<String> attachFileFileAddPathsList = Arrays.asList(data.getAttachFileFileAddPaths().split(","));
                        // 删除已保存
                        if (data.getAttachFileMultiFileNames().contains(file.getName())){
                            for (String name : attachFileNameList){
                                if (name.contains(file.getName())){
                                    String fileFileDeleteIds = data.getAttachFileFileDeleteIds() == null ? "" : data.getAttachFileFileDeleteIds();
                                    data.setAttachFileFileDeleteIds(fileFileDeleteIds + ","+ attachFileIdList.get(attachFileNameList.indexOf(name)));
                                    break;
                                }
                            }
                        }else { // 本地删除
                            for (String path : attachFileFileAddPathsList){
                                if (path.contains(file.getName())){
                                    attachFileFileAddPathsList.remove(path);
                                    data.setAttachFileFileAddPaths(picPath.substring(0, picPath.length() - 1));
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    //添加
                    data.setIsExecuted(true);
                    data.setAttachFileFileAddPaths(picPath.substring(0, picPath.length() - 1));
                }
        });
    }

    private void initAttachFiles(SafetyMeasuresEntity data, AttachmentDownloadController downloadController, CustomGalleryView galleryView, ImageView gifIv) {
        if (!TextUtils.isEmpty(data.getAttachFileMultiFileIds())) {
            Glide.with(context).asGif().load(R.drawable.preloader_3).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).into(gifIv);
            if (downloadController == null) {
                downloadController = new AttachmentDownloadController(Constant.IMAGE_SAVE_WORKTICKETPATH);
            }
            List<AttachmentEntity> attachmentEntities;
            if (data.getAttachmentEntityList() != null) {
                attachmentEntities = data.getAttachmentEntityList();
            } else {
                attachmentEntities = new ArrayList<>();
                AttachmentEntity attachmentEntity;
                List<String> attachFileIdList = Arrays.asList(data.getAttachFileMultiFileIds().split(","));
                List<String> attachFileNameList = Arrays.asList(data.getAttachFileMultiFileNames().split(","));
                for (String id : attachFileIdList) {
                    attachmentEntity = new AttachmentEntity();
                    attachmentEntity.id = Long.parseLong(id);
                    attachmentEntity.name = attachFileNameList.get(attachFileIdList.indexOf(id));
                    attachmentEntity.deploymentId = attachmentEntity.id; // 赋值附近id,防止下载过滤
                    attachmentEntities.add(attachmentEntity);
                }
                data.setAttachmentEntityList(attachmentEntities);
            }
            downloadController.downloadPic(attachmentEntities, "WorkTicket_8.20.3.03_workTicket", result -> {
                gifIv.setVisibility(View.GONE);
                galleryView.setGalleryBeans(result);
            });
        }else {
            gifIv.setVisibility(View.GONE);
        }
    }

}
