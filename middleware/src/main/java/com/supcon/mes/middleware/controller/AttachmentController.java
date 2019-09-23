package com.supcon.mes.middleware.controller;

import android.annotation.SuppressLint;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.view.custom.OnResultListener;
import com.supcon.mes.middleware.model.api.AttachmentAPI;
import com.supcon.mes.middleware.model.bean.AttachmentEntity;
import com.supcon.mes.middleware.model.bean.AttachmentListEntity;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.ResultEntity;
import com.supcon.mes.middleware.model.contract.AttachmentContract;
import com.supcon.mes.middleware.model.listener.OnAPIResultListener;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.presenter.AttachmentPresenter;

import java.io.File;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * Created by wangshizhan on 2018/8/17
 * Email:wangshizhan@supcom.com
 */
@Presenter(AttachmentPresenter.class)
public class AttachmentController extends BasePresenterController implements AttachmentContract.View{

    OnAPIResultListener<AttachmentListEntity> onAPIListener;
    OnAPIResultListener<String> uploadListener;
    OnAPIResultListener<BapResultEntity> deleteListener;


    @SuppressLint("CheckResult")
    @Override
    public void getAttachmentsSuccess(AttachmentListEntity entity) {
        if(onAPIListener!=null) {
            onAPIListener.onSuccess(entity);
        }
    }

    @Override
    public void getAttachmentsFailed(String errorMsg) {
        LogUtil.e("AttachmentController errorMsg:"+errorMsg);
        if(onAPIListener!=null) {
            onAPIListener.onFail(errorMsg);
        }
    }

    @Override
    public void queryAttachmentsSuccess(AttachmentListEntity entity) {

        if(onAPIListener!=null){
            onAPIListener.onSuccess(entity);
        }
    }

    @Override
    public void queryAttachmentsFailed(String errorMsg) {
        LogUtil.e("AttachmentController errorMsg:"+errorMsg);
        if(onAPIListener!=null) {
            onAPIListener.onFail(errorMsg);
        }
    }

    @Override
    public void uploadAttachmentSuccess(String entity) {
        LogUtil.i("AttachmentController uploadAttachmentSuccess:"+entity);
        if(uploadListener!=null){
            uploadListener.onSuccess(entity);
        }
    }

    @Override
    public void uploadAttachmentFailed(String errorMsg) {
        LogUtil.e("AttachmentController errorMsg:"+errorMsg);
        if(uploadListener!=null){
            uploadListener.onFail(errorMsg);
        }
    }

    @Override
    public void deleteAttachmentSuccess(BapResultEntity entity) {
        if(deleteListener!=null){
            deleteListener.onSuccess(entity);
        }
    }

    @Override
    public void deleteAttachmentFailed(String errorMsg) {
        LogUtil.e("AttachmentController deleteAttachmentFailed errorMsg:"+errorMsg);
        if(deleteListener!=null){
            deleteListener.onFail(errorMsg);
        }
    }


    public void refreshGalleryView(OnAPIResultListener<AttachmentListEntity> onAPIListener, long tableId){
        this.onAPIListener = onAPIListener;
        presenterRouter.create(AttachmentAPI.class).queryAttachments(tableId);

    }

    public void refreshImageView(OnAPIResultListener<AttachmentListEntity> onAPIListener, long tableId){
        this.onAPIListener = onAPIListener;
        presenterRouter.create(AttachmentAPI.class).getAttachments(tableId);

    }

    public void uploadAttachment(OnAPIResultListener<String> onSuccessListener, File file){
        this.uploadListener = onSuccessListener;
        presenterRouter.create(AttachmentAPI.class).uploadAttachment(file);
    }


    public void deleteAttachment(OnAPIResultListener<BapResultEntity> onSuccessListener, long id2del){
        deleteListener = onSuccessListener;
        presenterRouter.create(AttachmentAPI.class).deleteAttachment(id2del);
    }
}
