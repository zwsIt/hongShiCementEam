package com.supcon.mes.middleware.presenter;

import com.supcon.mes.middleware.model.bean.AttachmentListEntity;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.contract.AttachmentContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;
import com.supcon.mes.middleware.util.FormDataHelper;

import java.io.File;
import java.net.URLDecoder;
import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.MultipartBody;

/**
 * Created by wangshizhan on 2018/8/17
 * Email:wangshizhan@supcom.com
 */
public class AttachmentPresenter extends AttachmentContract.Presenter {

    @Override
    public void getAttachments(long tableId) {
        mCompositeSubscription.add(
                MiddlewareHttpClient.listAttachFiles(tableId)
                        .onErrorReturn(new Function<Throwable, AttachmentListEntity>() {
                            @Override
                            public AttachmentListEntity apply(Throwable throwable) throws Exception {
                                AttachmentListEntity attachmentListEntity = new AttachmentListEntity();
                                attachmentListEntity.success = false;
                                attachmentListEntity.errMsg = throwable.toString();
                                return attachmentListEntity;
                            }
                        })
                        .subscribe(new Consumer<AttachmentListEntity>() {

                            @Override
                            public void accept(AttachmentListEntity attachmentListEntity) throws Exception {
                                if (attachmentListEntity.success) {
                                    if (getView() != null) {
                                        getView().queryAttachmentsSuccess(attachmentListEntity);
                                    }
                                } else {
                                    if (getView() != null) {
                                        getView().queryAttachmentsFailed(attachmentListEntity.errMsg);
                                    }
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                            }
                        })
        );
    }

    @Override
    public void queryAttachments(long tableId) {
        mCompositeSubscription.add(
                MiddlewareHttpClient.listAttachFiles(tableId)
                        .onErrorReturn(new Function<Throwable, AttachmentListEntity>() {
                            @Override
                            public AttachmentListEntity apply(Throwable throwable) throws Exception {
                                AttachmentListEntity attachmentListEntity = new AttachmentListEntity();
                                attachmentListEntity.success = false;
                                attachmentListEntity.errMsg = throwable.toString();
                                return attachmentListEntity;
                            }
                        })
                        .subscribe(new Consumer<AttachmentListEntity>() {
                            @Override
                            public void accept(AttachmentListEntity attachmentListEntity) throws Exception {
                                if (attachmentListEntity.success) {
                                    if (getView() != null) {
                                        getView().queryAttachmentsSuccess(attachmentListEntity);
                                    }
                                } else {
                                    if (getView() != null) {
                                        getView().queryAttachmentsFailed(attachmentListEntity.errMsg);
                                    }
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                            }
                        })
        );
    }

    @Override
    public void uploadAttachment(File file) {

        List<MultipartBody.Part> parts = FormDataHelper.createFileForm(file);
        mCompositeSubscription.add(
                MiddlewareHttpClient.uploadFile(parts)
                        .onErrorReturn(new Function<Throwable, String>() {
                            @Override
                            public String apply(Throwable throwable) throws Exception {

                                return throwable.toString();
                            }
                        })
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String result) throws Exception {
                                String url = URLDecoder.decode(result, "utf-8");
//                                LogUtil.w("uploadAttachment result:"+result);
//                                LogUtil.w("uploadAttachment url:"+url);

                                if (url != null && url.contains("bap-workspace")) {
                                    getView().uploadAttachmentSuccess(url);
                                } else {
                                    getView().uploadAttachmentFailed("上传图片失败！");
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                            }
                        })
        );

    }

    @Override
    public void deleteAttachment(long id) {
        mCompositeSubscription.add(
                MiddlewareHttpClient.deleteFile(id)
                        .onErrorReturn(new Function<Throwable, BapResultEntity>() {
                            @Override
                            public BapResultEntity apply(Throwable throwable) throws Exception {
                                BapResultEntity bapResultEntity = new BapResultEntity();
                                bapResultEntity.dealSuccessFlag = false;
                                bapResultEntity.dealSuccess = false;
                                bapResultEntity.errMsg = throwable.toString();
                                return bapResultEntity;
                            }
                        })
                        .subscribe(new Consumer<BapResultEntity>() {
                            @Override
                            public void accept(BapResultEntity result) throws Exception {
                                if (result.dealSuccess || result.dealSuccessFlag) {
                                    getView().deleteAttachmentSuccess(result);
                                } else {
                                    getView().deleteAttachmentFailed(result.errMsg);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                            }
                        })
        );
    }


}
