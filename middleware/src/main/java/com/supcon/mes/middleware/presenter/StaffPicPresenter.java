package com.supcon.mes.middleware.presenter;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.contract.StaffPicDownloadContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;
import com.supcon.mes.middleware.util.PicUtil;

import java.io.File;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import okhttp3.MediaType;
import okhttp3.ResponseBody;

/**
 * Created by wangshizhan on 2018/8/1
 * Email:wangshizhan@supcom.com
 */
public class StaffPicPresenter extends StaffPicDownloadContract.Presenter {
    @Override
    public void getStaffPic(long id) {

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
                                return responseBody!=null;
                            }
                        })
                        .map(new Function<ResponseBody, File>() {
                            @Override
                            public File apply(ResponseBody responseBody) throws Exception {

                                return PicUtil.savePicToDisk(String.valueOf(id), Constant.IMAGE_SAVE_PATH, responseBody);
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
}
