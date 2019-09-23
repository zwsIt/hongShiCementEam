package com.supcon.mes.middleware.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.supcon.common.com_http.util.RxSchedulers;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.beans.GalleryBean;
import com.supcon.mes.mbap.view.CustomGalleryView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.supcon.mes.mbap.adapter.GalleryAdapter.FILE_TYPE_VIDEO;

/**
 * @author xushiyun
 * @date 2017/12/19
 * Email:ciruy.victory@gmail.com
 */

public class FaultPicHelper {

    /**
     * 初始化缺陷照片
     *
     * @param picLocalPath
     */
    @SuppressLint("CheckResult")
    public static void initImageView(String picLocalPath, ImageView imageView) {
        if (TextUtils.isEmpty(picLocalPath)) {
            return;
        }
        Flowable.just(picLocalPath)
                .onErrorReturn(new Function<Throwable, String>() {
                    @Override
                    public String apply(Throwable throwable) throws Exception {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .map(s -> {
                    GalleryBean bean = new GalleryBean();
                    //本地储存图片
                    if (s.contains("/storage")) {
                        bean.localPath = s;
                        bean.type = "local";
                    } else {
                        bean.url = s.replace("\\", "/").replace(" ", "");
                        bean.type = "network";
                    }
                    return bean;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(galleryBean -> {
                    if (!TextUtils.isEmpty(galleryBean.localPath)) {
                        Glide.with(imageView.getContext()).load(galleryBean.localPath).apply(RequestOptionUtil.getEamRequestOptions(imageView.getContext())).into(imageView);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }


    /**
     * 初始化缺陷照片
     *
     * @param faultPicPaths
     */
    @SuppressLint("CheckResult")
    public static void initPics(String[] faultPicPaths, List<GalleryBean> picPaths, CustomGalleryView customGalleryView) {
        if (faultPicPaths.length == 0) {
            return;
        }
        customGalleryView.clear();
        Flowable.fromIterable(Arrays.asList(faultPicPaths))
                .onErrorReturn(new Function<Throwable, String>() {
                    @Override
                    public String apply(Throwable throwable) throws Exception {
                        if(throwable!=null) {
                            LogUtil.e("throwable:" + throwable.toString());
                        }
                        return null;
                    }
                })
                .compose(RxSchedulers.io_main())
                .map(s -> {
                    GalleryBean bean = new GalleryBean();
                    if (TextUtils.isEmpty(s)) {

                    }
                    //本地储存图片
                    else if (s.contains("/storage")) {
                        bean.localPath = s;
                        bean.type = "local";
                    } else {
                        bean.url = s.replace("\\", "/").replace(" ", "");
                        bean.type = "network";
                    }
                    return bean;
                })
                .subscribe(galleryBean -> {
                    //本地草稿
                    if (!TextUtils.isEmpty(galleryBean.localPath)) {
                        if (PicUtil.isVideo(galleryBean.localPath)) {//如果是视频，则做进一步处理
                            galleryBean.fileType = FILE_TYPE_VIDEO;
//                            String fileName = galleryBean.localPath.substring(galleryBean.localPath.lastIndexOf("/")+1, galleryBean.localPath.length());
//                            galleryBean.thumbnailPath =  PicUtil.getThumbnailName(fileName);
                        }
                        picPaths.add(galleryBean);
                        customGalleryView.addGalleryBean(galleryBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if(throwable!=null){
                            LogUtil.e("throwable:"+throwable.toString());
                        }
                    }
                });
    }


    /**
     * 初始化缺陷照片
     *
     * @param faultPicPaths
     */
    @SuppressLint("CheckResult")
    public static void initPics(List<String> faultPicPaths, CustomGalleryView customGalleryView) {
        if (faultPicPaths.size() == 0) {
            return;
        }
        customGalleryView.clear();
        Flowable.fromIterable(faultPicPaths)
                .compose(RxSchedulers.io_main())
                .map(s -> {
                    GalleryBean bean = new GalleryBean();
                    if (s.contains("/storage")) {
                        bean.localPath = s;
                    }
                    return bean;
                })
                .subscribe(
                        galleryBean -> {
                            //本地草稿
                            if (!TextUtils.isEmpty(galleryBean.localPath)) {
                                if(PicUtil.isVideo(galleryBean.localPath)){//如果是视频，则做进一步处理
                                    galleryBean.fileType = FILE_TYPE_VIDEO;
//                                    String fileName = galleryBean.localPath.substring(galleryBean.localPath.lastIndexOf("/")+1, galleryBean.localPath.length());
//                                    galleryBean.thumbnailPath =  PicUtil.getThumbnailName(fileName);
                                }
                                customGalleryView.addGalleryBean(galleryBean);
                            }
                        });


    }

    /**
     * 初始化缺陷照片
     *
     * @param faultPicPaths
     */
    @SuppressLint("CheckResult")
    public static void initPics(String[] faultPicPaths, CustomGalleryView customGalleryView) {

        if (faultPicPaths.length == 0) {
            customGalleryView.setVisibility(View.GONE);
            return;
        }
        customGalleryView.clear();
        Flowable.fromIterable(Arrays.asList(faultPicPaths))
                .compose(RxSchedulers.io_main())
                .map(s -> {
                    GalleryBean bean = new GalleryBean();
                    if (s.startsWith("/storage")) {
                        bean.localPath = s;
                    }
                    return bean;
                })
                .subscribe(
                        galleryBean -> {
                            if (!TextUtils.isEmpty(galleryBean.localPath)) {
                                if(PicUtil.isVideo(galleryBean.localPath)){//如果是视频，则做进一步处理
                                    galleryBean.fileType = FILE_TYPE_VIDEO;
//                                    String fileName = galleryBean.localPath.substring(galleryBean.localPath.lastIndexOf("/")+1, galleryBean.localPath.length());
//                                    galleryBean.thumbnailPath =  PicUtil.getThumbnailName(fileName);
                                }
                                customGalleryView.addGalleryBean(galleryBean);
                            }
                        });
    }

    /**
     * 根据GalleryBean列表获取图片路径组成的字符串列表
     *
     * @param list 列表
     * @return 路径字符串列表
     */
    public static List<String> getImagePathList(List<GalleryBean> list) {
        List<String> imagePathList = new ArrayList<>();
        for (GalleryBean galleryBean : list) {
            imagePathList.add(galleryBean.localPath);
        }
        return imagePathList;
    }


    public static Bundle genRequestBundle(Context context, int[] location, List<GalleryBean> galleryBeans, int pos) {
        return genRequestBundle(context, location, galleryBeans, pos, false);
    }

    public static Bundle genRequestBundle(Context context, int[] location, List<GalleryBean> galleryBeans, int pos, boolean editable) {
        Bundle bundle = new Bundle();
        //非必须
        bundle.putSerializable("images", (ArrayList) FaultPicHelper.getImagePathList(galleryBeans));
        bundle.putInt("position", pos);
        //必须
        bundle.putInt("locationX", location[0]);
        //必须
        bundle.putInt("locationY", location[1]);
        //必须
        bundle.putInt("width", DisplayUtil.dip2px(100, context));
        //必须
        bundle.putInt("height", DisplayUtil.dip2px(100, context));

        bundle.putBoolean("isEditable", editable);
        return bundle;
    }

    public static Bundle genRequestBundle(Context context, View childView, List<GalleryBean> galleryBeans, int pos) {
        return genRequestBundle(context, childView, galleryBeans, pos, false);
    }

    public static Bundle genRequestBundle(Context context, View childView, List<GalleryBean> galleryBeans, int pos, boolean isEditable) {
        int[] location = new int[2];
        childView.getLocationOnScreen(location);
        return genRequestBundle(context, location, galleryBeans, pos, isEditable);
    }
}
