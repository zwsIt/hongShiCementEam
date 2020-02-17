package com.supcon.mes.middleware.controller;

import android.annotation.SuppressLint;

import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.beans.GalleryBean;
import com.supcon.mes.mbap.network.Api;
import com.supcon.mes.middleware.model.bean.AttachmentEntity;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.model.network.NetworkAPI;
import com.supcon.mes.middleware.util.PicUtil;

import org.reactivestreams.Publisher;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

import static com.supcon.mes.mbap.adapter.GalleryAdapter.FILE_TYPE_VIDEO;

/**
 * Created by wangshizhan on 2018/12/17
 * Email:wangshizhan@supcom.com
 */
public class AttachmentDownloadController extends BasePresenterController {

    private String dir;
    private CompositeDisposable mCompositeSubscription = new CompositeDisposable();
    private Set<String> downloadList = new HashSet<>();

    public AttachmentDownloadController(String dir) {
        this.dir = dir;
    }

    @SuppressLint("CheckResult")
    private void downloadGalleryPics(List<AttachmentEntity> attachmentEntities, String entityCode, OnSuccessListener<List<GalleryBean>> successListener) {

        if (attachmentEntities == null || attachmentEntities.size() == 0) {
            return;
        }

        List<GalleryBean> galleryBeans = new ArrayList<>();
        Flowable.fromIterable(attachmentEntities)
                .subscribeOn(Schedulers.newThread())
                .filter(attachmentEntity -> attachmentEntity.deploymentId != 0)
                .flatMap(new Function<AttachmentEntity, Publisher<GalleryBean>>() {
                    @Override
                    public Publisher<GalleryBean> apply(AttachmentEntity attachment) throws Exception {
                        GalleryBean galleryBean = new GalleryBean();
                        String path = dir;
                        if (attachment.name.contains(".jpg") || attachment.name.contains(".png") || attachment.name.contains(".PNG")) {

                        } else if (attachment.name.contains(".txt") || attachment.name.contains(".doc")
                                || attachment.name.contains(".pdf") || attachment.name.contains(".xls") || attachment.name.contains(".ppt")) {

                        } else if (attachment.name.contains(".mp4") || attachment.name.contains(".adv")) {
                            galleryBean.fileType = FILE_TYPE_VIDEO;
                        } else if (attachment.name.contains(".mp3") || attachment.name.contains(".wav")) {

                        }
                        galleryBean.url = attachment.path;
                        String suffix = attachment.name;
                        File pic = new File(path, suffix);
                        if (pic.exists()) {
                            galleryBean.localPath = pic.getAbsolutePath();
                            return Flowable.just(galleryBean);
                        } else {
                            String key = entityCode + attachment.id;
                            if (!downloadList.contains(key)) {
                                downloadList.add(key);
                                return Api.getInstance().retrofit.create(NetworkAPI.class).downloadFile(attachment.id, entityCode)
                                        .onErrorReturn(new Function<Throwable, ResponseBody>() {
                                            @Override
                                            public ResponseBody apply(Throwable throwable) throws Exception {
                                                LogUtil.e("onErrorReturn");
                                                return null;
                                            }
                                        })
                                        .subscribeOn(Schedulers.newThread())
                                        .map(new Function<ResponseBody, GalleryBean>() {
                                            @Override
                                            public GalleryBean apply(ResponseBody responseBody) throws Exception {
                                                File file = PicUtil.writeToDisk(attachment.name, path, responseBody); // 存储本地
                                                galleryBean.localPath = file.getAbsolutePath();
                                                return galleryBean;

                                            }
                                        });
                            } else {
                                LogUtil.w("已经存在相同的下载任务");
                                return null;
                            }
                        }
                    }
                })
                .filter(new Predicate<GalleryBean>() {
                    @Override
                    public boolean test(GalleryBean galleryBean) throws Exception {
                        // 过滤文档
                        return galleryBean != null && !galleryBean.localPath.contains(".txt") && !galleryBean.localPath.contains(".doc")
                                && !galleryBean.localPath.contains(".pdf") && !galleryBean.localPath.contains(".xls") && !galleryBean.localPath.contains(".ppt");
                    }
                })
                /*.flatMap(new Function<File, Publisher<GalleryBean>>() {
                    @Override
                    public Publisher<GalleryBean> apply(File file) throws Exception {
                        GalleryBean galleryBean = new GalleryBean();
                        LogUtil.w("save localPath:" + galleryBean.localPath);
                        if (PicUtil.isVideo(file.getName())) {
                            galleryBean.fileType = FILE_TYPE_VIDEO;
                        }
                        galleryBean.localPath = file.getAbsolutePath();
                        return Flowable.just(galleryBean);
                    }
                })*/
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GalleryBean>() {
                    @Override
                    public void accept(GalleryBean galleryBean) throws Exception {
                        galleryBeans.add(galleryBean);
                    }
                }, throwable -> {
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        if (successListener != null) {
                            successListener.onSuccess(galleryBeans);
                        }
                    }
                });

    }

    @SuppressLint("CheckResult")
    public void downloadEamPic(AttachmentEntity entity, String entityCode, OnSuccessListener<File> successListener) {
        if (entity == null) {
            return;
        }

        Flowable.just(entity)
                .subscribe(
                        new Consumer<AttachmentEntity>() {
                            @Override
                            public void accept(AttachmentEntity attachment) throws Exception {
                                String path = dir;
                                if (attachment.name.contains(".jpg") || attachment.name.contains(".png") || attachment.name.contains(".PNG")) {

                                } else if (attachment.name.contains(".txt") || attachment.name.contains(".doc")) {
                                } else if (attachment.name.contains(".mp4") || attachment.name.contains(".adv")) {
                                } else if (attachment.name.contains(".mp3") || attachment.name.contains(".wav")) {

                                }
                                String suffix = attachment.name;
                                File pic = new File(path, suffix);
                                if (pic.exists()) {
                                    LogUtil.w("localPath:" + pic.getAbsolutePath());
                                    if (successListener != null) {
                                        successListener.onSuccess(pic);
                                    }
                                } else {
                                    String finalPath = path;
                                    String key = entityCode + attachment.id;
                                    if (downloadList.contains(key)) {
                                        LogUtil.w("已经存在相同的下载任务");
                                        return;
                                    }
                                    downloadList.add(key);
                                    mCompositeSubscription.add(Api.getInstance().retrofit.create(NetworkAPI.class).downloadFile(attachment.id, entityCode)
                                            .subscribeOn(Schedulers.newThread())
                                            .onErrorReturn(new Function<Throwable, ResponseBody>() {
                                                @Override
                                                public ResponseBody apply(Throwable throwable) throws Exception {
                                                    LogUtil.e("onErrorReturn2");
                                                    return null;
                                                }
                                            })
                                            .map(new Function<ResponseBody, File>() {
                                                @Override
                                                public File apply(ResponseBody responseBody) throws Exception {
                                                    File file = PicUtil.writeToDisk(attachment.name, finalPath, responseBody);
//                                                if(PicUtil.isVideo(attachment.name)) {
//                                                    PicUtil.createThumbnail(finalPath, file);
//                                                }
                                                    return file;
                                                }
                                            })
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(file -> {
                                                LogUtil.w("save localPath:" + file.getAbsolutePath());
                                                if (successListener != null) {
                                                    successListener.onSuccess(file);
                                                }
                                            }, new Consumer<Throwable>() {
                                                @Override
                                                public void accept(Throwable throwable) throws Exception {
                                                    LogUtil.e("downloadFile accept2 " + throwable);
                                                }
                                            }));
                                }

                            }
                        });

    }


    @SuppressLint("CheckResult")
    public void downloadPic(List<AttachmentEntity> attachmentEntities, String entityCode, OnSuccessListener<List<GalleryBean>> successListener) {

        downloadGalleryPics(attachmentEntities, entityCode, successListener);

    }

    @SuppressLint("CheckResult")
    public void downloadAreaPic(AttachmentEntity entity, String entityCode, OnSuccessListener<File> successListener) {
        if (entity == null) {
            return;
        }

        Flowable.just(entity)
                .subscribe(
                        attachment -> {
                            String path = dir;
                            if (attachment.name.contains(".jpg") || attachment.name.contains(".png") || attachment.name.contains(".PNG")) {

                            } else if (attachment.name.contains(".txt") || attachment.name.contains(".doc")) {
                            } else if (attachment.name.contains(".mp4") || attachment.name.contains(".adv")) {
                            } else if (attachment.name.contains(".mp3") || attachment.name.contains(".wav")) {

                            }
                            String suffix = attachment.name;
                            File pic = new File(path, suffix);
                            if (pic.exists()) {
                                LogUtil.w("localPath:" + pic.getAbsolutePath());
                                if (successListener != null) {
                                    successListener.onSuccess(pic);
                                }
                            } else {
                                String finalPath = path;
                                String key = entityCode + attachment.id;
                                if (downloadList.contains(key)) {
                                    LogUtil.w("已经存在相同的下载任务");
                                    return;
                                }
                                downloadList.add(key);
                                mCompositeSubscription.add(Api.getInstance().retrofit.create(NetworkAPI.class).downloadFile(attachment.id, entityCode)
                                        .subscribeOn(Schedulers.newThread())
                                        .onErrorReturn(new Function<Throwable, ResponseBody>() {
                                            @Override
                                            public ResponseBody apply(Throwable throwable) throws Exception {
                                                LogUtil.e("onErrorReturn2");
                                                return null;
                                            }
                                        })
                                        .map(new Function<ResponseBody, File>() {
                                            @Override
                                            public File apply(ResponseBody responseBody) throws Exception {
                                                File file = PicUtil.writeToDisk(attachment.name, finalPath, responseBody);
//                                                if(PicUtil.isVideo(attachment.name)) {
//                                                    PicUtil.createThumbnail(finalPath, file);
//                                                }
                                                return file;
                                            }
                                        })
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(file -> {
                                            LogUtil.w("save localPath:" + file.getAbsolutePath());
                                            if (successListener != null) {
                                                successListener.onSuccess(file);
                                            }
                                        }, new Consumer<Throwable>() {
                                            @Override
                                            public void accept(Throwable throwable) throws Exception {
                                                LogUtil.e("downloadFile accept2 " + throwable);
                                            }
                                        }));
                            }

                        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        dispose();
    }

    public void dispose() {
        mCompositeSubscription.dispose();
    }


}
