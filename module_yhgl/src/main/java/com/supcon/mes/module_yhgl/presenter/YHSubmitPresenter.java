package com.supcon.mes.module_yhgl.presenter;

import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.util.FormDataHelper;
import com.supcon.mes.module_yhgl.model.contract.YHSubmitContract;
import com.supcon.mes.module_yhgl.model.network.YHGLHttpClient;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;

/**
 * Created by wangshizhan on 2018/8/22
 * Email:wangshizhan@supcom.com
 */
public class YHSubmitPresenter extends YHSubmitContract.Presenter {

    @Override
    public void doSubmit(Map<String, Object> map, Map<String, Object> attachmentMap, boolean isEdit) {
        if(isEdit) {
            List<MultipartBody.Part> parts = FormDataHelper.createFileForm(attachmentMap);
            mCompositeSubscription.add(
                    YHGLHttpClient.editSubmit(map, parts)
                            .onErrorReturn(throwable -> {
                                BapResultEntity resultEntity = new BapResultEntity();
                                resultEntity.dealSuccessFlag = false;
                                resultEntity.errMsg = throwable.toString();

                                return resultEntity;
                            })
                            .subscribe(resultEntity -> {
                                if (resultEntity.dealSuccessFlag) {
                                    getView().doSubmitSuccess(resultEntity);
                                } else {
                                    getView().doSubmitFailed(resultEntity.errMsg);
                                }
                            })
            );
        }
        else
            mCompositeSubscription.add(
                    YHGLHttpClient.viewSubmit(map)
                            .onErrorReturn(throwable -> {
                                BapResultEntity resultEntity = new BapResultEntity();
                                resultEntity.dealSuccessFlag = false;
                                resultEntity.errMsg = throwable.toString();

                                return resultEntity;
                            })
                            .subscribe(resultEntity -> {
                                if (resultEntity.dealSuccessFlag) {
                                    getView().doSubmitSuccess(resultEntity);
                                } else {
                                    getView().doSubmitFailed(resultEntity.errMsg);
                                }
                            })
            );
    }

}
