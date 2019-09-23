package com.supcon.mes.middleware.util;

import android.annotation.SuppressLint;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.supcon.common.com_http.util.RxSchedulers;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.beans.GalleryBean;
import com.supcon.mes.mbap.view.CustomGalleryView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.AttachmentEntity;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by wangshizhan on 2017/12/19.
 * Email:wangshizhan@supcon.com
 */

public class PicHelper {

    /**
     * 初始化缺陷照片
     *
     */
//    @SuppressLint("CheckResult")
//    public static void initPics(List<AttachmentEntity> attachmentEntities, CustomGalleryView customGalleryView, String entityCode) {
//
//        if (attachmentEntities.size() == 0) {
//            return;
//        }
//
//        customGalleryView.clear();
//
//        Flowable.fromIterable(attachmentEntities)
//                .filter(attachmentEntity -> attachmentEntity.deploymentId != 0)
//                .compose(RxSchedulers.io_main())
//                .subscribe(
//                        attachment -> {
//                            GalleryBean galleryBean = new GalleryBean();
//                            if (attachment.name.contains(".jpg") || attachment.name.contains(".png") || attachment.name.contains(".PNG")) {
//                                galleryBean.url = attachment.path;
//                                String suffix = attachment.name;
//                                String path = Constant.IMAGE_SAVE_PATH;
//                                File pic = new File(path, suffix);
//                                if (pic.exists()) {
//                                    galleryBean.localPath = pic.getAbsolutePath();
//                                    customGalleryView.addGalleryBean(galleryBean);
//                                } else {
//                                    MiddlewareHttpClient.downloadFile(attachment.id, entityCode)
//                                            .onErrorReturn(throwable -> null)
//                                            .filter(obj -> obj!=null)
//                                            .map(responseBody -> PicUtil.writeToDisk(attachment.name, responseBody))
//                                            .compose(RxSchedulers.io_main())
//                                            .subscribe(file -> {
//                                                galleryBean.localPath = file.getAbsolutePath();
//                                                LogUtil.w("save localPath:" + galleryBean.localPath);
//                                                customGalleryView.addGalleryBean(galleryBean);
//                                            });
//                                }
//                            } else if (attachment.name.contains(".txt") || attachment.name.contains(".doc")) {
//
//                            } else if (attachment.name.contains(".mp4") || attachment.name.contains(".adv")) {
//
//                            } else if (attachment.name.contains(".mp3") || attachment.name.contains(".wav")) {
//
//                            }
//
//
//                        });
//
//
//    }


    public static List<String> getImagePathList(List<GalleryBean> list) {

        List<String> imagePathList = new ArrayList<>();

        for (GalleryBean galleryBean : list) {
            imagePathList.add(galleryBean.localPath);
        }

        return imagePathList;

    }

    public static String[] getImgPath(String paths) {
        String[] split = paths.split(",");
        for (int i = 0; i < split.length; i++) {
            if (!paths.contains("bap")) {
                split[i] = Constant.IMAGE_SAVE_YHPATH + split[i];
            }
        }
        return split;
    }

}
