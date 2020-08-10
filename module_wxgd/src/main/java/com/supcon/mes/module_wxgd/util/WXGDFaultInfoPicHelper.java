package com.supcon.mes.module_wxgd.util;

import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.view.CustomGalleryView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.AttachmentController;
import com.supcon.mes.middleware.controller.AttachmentDownloadController;
import com.supcon.mes.middleware.controller.TableInfoController;
import com.supcon.mes.middleware.model.bean.AttachmentEntity;
import com.supcon.mes.middleware.model.bean.AttachmentListEntity;
import com.supcon.mes.middleware.model.bean.WXGDEntity;
import com.supcon.mes.middleware.model.listener.OnAPIResultListener;
import com.supcon.mes.module_wxgd.constant.WXGDConstant;

import java.util.List;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/7/27
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class WXGDFaultInfoPicHelper {

    private static AttachmentController mAttachmentController;
    private static TableInfoController mTableInfoController;
    private static AttachmentDownloadController sMDownloadController;


    public static void initPic(Long tableId, CustomGalleryView yhGalleryView) {

        if (mTableInfoController == null){
            mTableInfoController = new TableInfoController();
        }
        mTableInfoController.getTableInfo(WXGDConstant.URL.PRE_URL, tableId, WXGDConstant.HeaderData.HEADER_DATA_INCLUDES, new OnAPIResultListener() {
            @Override
            public void onFail(String errorMsg) {
                LogUtil.e(errorMsg);
            }

            @Override
            public void onSuccess(Object result) {
                WXGDEntity wxgdEntity = GsonUtil.gsonToBean(GsonUtil.gsonString(result),WXGDEntity.class);
                downloadAttachment(wxgdEntity.faultInfo.tableInfoId,yhGalleryView);
            }
        });

    }

    private static void downloadAttachment(Long tableInfoId, CustomGalleryView yhGalleryView) {

        if (mAttachmentController == null){
            mAttachmentController = new AttachmentController();
        }
        if (sMDownloadController == null){
            sMDownloadController = new AttachmentDownloadController(Constant.IMAGE_SAVE_YHPATH);
        }
        mAttachmentController.refreshGalleryView(new OnAPIResultListener<AttachmentListEntity>() {
            @Override
            public void onFail(String errorMsg) {}

            @Override
            public void onSuccess(AttachmentListEntity entity) {
//                wxgdEntity.attachmentEntities = entity.result;
                if (entity.result.size() > 0){
                    sMDownloadController.downloadPic(entity.result, "BEAM2_1.0.0_faultInfo",
                            result -> yhGalleryView.setGalleryBeans(result));
                }
            }
        }, tableInfoId);

    }

}
