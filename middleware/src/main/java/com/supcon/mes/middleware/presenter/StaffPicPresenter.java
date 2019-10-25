package com.supcon.mes.middleware.presenter;

import android.text.TextUtils;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.middleware.model.contract.StaffPicDownloadContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;
import com.supcon.mes.middleware.util.PicUtil;

import java.io.File;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import okhttp3.ResponseBody;

/**
 * Created by wangshizhan on 2018/8/1
 * Email:wangshizhan@supcom.com
 */
public class StaffPicPresenter extends StaffPicDownloadContract.Presenter {
    @Override
    public void getStaffPic(long id, String picType) {

        mCompositeSubscription.add(
                MiddlewareHttpClient.downloadStaffPic(id)
                        .onErrorReturn(new Function<Throwable, ResponseBody>() {
                            @Override
                            public ResponseBody apply(Throwable throwable) throws Exception {
                                getView().getStaffPicFailed(throwable.toString());
                                return null;
                            }
                        })
                        .filter(new Predicate<ResponseBody>() {
                            @Override
                            public boolean test(ResponseBody responseBody) throws Exception {
                                return responseBody != null;
                            }
                        })
                        .map(new Function<ResponseBody, File>() {
                            @Override
                            public File apply(ResponseBody responseBody) throws Exception {

                                return PicUtil.savePicToDisk(id + picType, Constant.IMAGE_SAVE_PATH, responseBody);
                            }
                        })
                        .subscribe(new Consumer<File>() {
                            @Override
                            public void accept(File file) throws Exception {
                                if (file != null && getView() != null) {
                                    getView().getStaffPicSuccess(file);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                getView().getStaffPicFailed(throwable.toString());
                            }
                        })
        );

//            MiddlewareHttpClient.downloadPicFromNet(galleryBean.url)
//            .compose(RxSchedulers.io_main())
//            .map(responseBody -> Util.writeResponseBodyToDisk(galleryBean.url, responseBody))
//            .subscribe(file -> {
//                galleryBean.localPath = file.getAbsolutePath();
//                LogUtil.w("localPath:" + galleryBean.localPath);
//            }, throwable -> {
//
//            }, () -> customGalleryView.addGalleryBean(galleryBean));
    }

    @Override
    public void getDocIds(long linkId) {
        mCompositeSubscription.add(
                MiddlewareHttpClient.getDocIds(linkId)
                        .onErrorReturn(new Function<Throwable, CommonEntity<String>>() {
                            @Override
                            public CommonEntity<String> apply(Throwable throwable) throws Exception {
                                CommonEntity<String> strEntity = new CommonEntity<>();
                                strEntity.errMsg = throwable.toString();
                                return strEntity;
                            }
                        })
                        .subscribe(new Consumer<CommonEntity<String>>() {
                            @Override
                            public void accept(CommonEntity<String> strEntity) throws Exception {
                                try {
                                    if (TextUtils.isEmpty(strEntity.errMsg) && !TextUtils.isEmpty(strEntity.result)) {
//                                        Long.parseLong(strEntity.result);
                                        getView().getDocIdsSuccess(strEntity);
                                    } else {
                                        getView().getDocIdsFailed(strEntity.errMsg);
                                    }
                                } catch (Exception e) {
                                    getView().getDocIdsFailed(e.getMessage());
                                }

                            }
                        })

        );
    }
}
