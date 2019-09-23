package com.supcon.mes.middleware.controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.AttachmentEntity;
import com.supcon.mes.middleware.model.bean.AttachmentListEntity;
import com.supcon.mes.middleware.model.bean.CommonDeviceEntity;
import com.supcon.mes.middleware.model.bean.CommonDeviceEntityDao;
import com.supcon.mes.middleware.model.listener.OnAPIResultListener;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.util.FaultPicHelper;
import com.supcon.mes.middleware.util.RequestOptionUtil;

import java.io.File;

/**
 * Created by wangshizhan on 2018/12/17
 * Email:wangshizhan@supcom.com
 */
public class EamPicController extends BasePresenterController {

    private AttachmentController mAttachmentController;
    private AttachmentDownloadController mAttachmentDownloadController;

    public EamPicController() {
        mAttachmentController = new AttachmentController();
        mAttachmentDownloadController = new AttachmentDownloadController(Constant.IMAGE_SAVE_PATH);
    }

    @SuppressLint("CheckResult")
    public void initEamPic(ImageView imageView, long eamId) {
        CommonDeviceEntity commonDeviceEntity = EamApplication.dao().getCommonDeviceEntityDao().queryBuilder().where(CommonDeviceEntityDao.Properties.EamId.eq(eamId)).unique();
        if (commonDeviceEntity != null && !TextUtils.isEmpty(commonDeviceEntity.eamPic)) {
            if (imageView.getContext() != null && !((Activity) imageView.getContext()).isDestroyed()) {
                FaultPicHelper.initImageView(commonDeviceEntity.eamPic, imageView);
            }

            return;
        }

        mAttachmentController.refreshImageView(new OnAPIResultListener<AttachmentListEntity>() {
            @Override
            public void onFail(String errorMsg) {

            }

            @Override
            public void onSuccess(AttachmentListEntity entity) {

                for (AttachmentEntity attachmentEntity : entity.result) {
                    if ("pic".equals(attachmentEntity.fileType)) {

                        mAttachmentDownloadController.downloadEamPic(attachmentEntity, "mobileEAM_1.0.0_work", new OnSuccessListener<File>() {
                            @Override
                            public void onSuccess(File result) {
                                CommonDeviceEntity commonDeviceEntity = EamApplication.dao().getCommonDeviceEntityDao().queryBuilder().where(CommonDeviceEntityDao.Properties.EamId.eq(eamId)).unique();

                                if (commonDeviceEntity != null) {
                                    commonDeviceEntity.eamPic = result.getAbsolutePath();
                                    EamApplication.dao().getCommonDeviceEntityDao().update(commonDeviceEntity);
                                }

                                if (imageView.getContext() != null && !((Activity) imageView.getContext()).isDestroyed())
                                    Glide.with(imageView.getContext()).load(result).apply(RequestOptionUtil.getEamRequestOptions(imageView.getContext())).into(imageView);
                            }
                        });
                    }
                }

            }
        }, eamId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAttachmentDownloadController.dispose();

    }
}
