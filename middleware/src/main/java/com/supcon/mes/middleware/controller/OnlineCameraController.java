package com.supcon.mes.middleware.controller;

import android.view.View;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.activity.BaseActivity;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.beans.GalleryBean;
import com.supcon.mes.mbap.view.CustomGalleryView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.bean.AttachmentEntity;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.listener.OnAPIResultListener;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.util.PicUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.supcon.mes.mbap.adapter.GalleryAdapter.FILE_TYPE_VIDEO;

/**
 * Created by wangshizhan on 2019/2/27
 * Email:wangshizhan@supcom.com
 */
public class OnlineCameraController extends BaseCameraController {


    @BindByTag("yhGalleryView")
    CustomGalleryView yhGalleryView;

    private AttachmentController mAttachmentController;
    private AttachmentDownloadController mDownloadController;
    public List<AttachmentEntity> attachmentEntities;

    public OnlineCameraController(View rootView) {
        super(rootView);
    }

    @Override
    public void onInit() {
        super.onInit();
    }

    public void addGalleryView(int position, CustomGalleryView customGalleryView){
        yhGalleryView = customGalleryView;
        addListener(position, customGalleryView);
    }


    public void setPicData(List<AttachmentEntity> attachmentEntities){
        if(attachmentEntities == null ){
            return;
        }

        this.attachmentEntities = attachmentEntities;
        if(yhGalleryView == null){
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

        mDownloadController.downloadYHPic(attachmentEntities, "BEAM2_1.0.0_faultInfo", new OnSuccessListener<List<GalleryBean>>() {
            @Override
            public void onSuccess(List<GalleryBean> result) {
                yhGalleryView.setGalleryBeans(result);
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

        if(mDownloadController!=null)
            mDownloadController.dispose();
    }

    private void initAttachmentController() {
        mAttachmentController = new AttachmentController();

    }



    @Override
    public void initListener() {
        super.initListener();
        addListener(0, yhGalleryView);
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
        yhGalleryView.deletePic(position);
        delete(galleryBean);
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


    private void uploadLocalPic(File file) {
        ((BaseActivity)activity).onLoading("正在上传照片...");
        mAttachmentController.uploadAttachment(new OnAPIResultListener<String>() {
            @Override
            public void onFail(String errorMsg) {
                ((BaseActivity)activity).onLoadFailed("照片上传失败！");
            }

            @Override
            public void onSuccess(String result) {
                GalleryBean galleryBean = new GalleryBean();
                galleryBean.localPath = file.getAbsolutePath();
                galleryBean.url = result;
                yhGalleryView.addGalleryBean(galleryBean);
                pics.add(galleryBean);
                ((BaseActivity)activity).onLoadSuccess("照片上传成功！");
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
                yhGalleryView.addGalleryBean(galleryBean);
                pics.add(galleryBean);

            }
        }, file);
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
}
