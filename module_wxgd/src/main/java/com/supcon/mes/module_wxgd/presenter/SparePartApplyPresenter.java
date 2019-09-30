package com.supcon.mes.module_wxgd.presenter;

import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.util.FormDataHelper;
import com.supcon.mes.module_wxgd.model.contract.SparePartApplyContract;
import com.supcon.mes.module_wxgd.model.network.HttpClient;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SparePartApplyPresenter extends SparePartApplyContract.Presenter {

    @Override
    public void submitSparePartApply(Map<String, Object> map, Map<String, Object> attachmentMap) {
        Map<String, RequestBody> formBody = FormDataHelper.createDataFormBody(map);
        List<MultipartBody.Part> parts = FormDataHelper.createFileForm(attachmentMap);
        mCompositeSubscription.add(
                HttpClient.sparePartApplyDoSubmit(formBody,parts)
                        .onErrorReturn(throwable -> {
                            BapResultEntity entity = new BapResultEntity();
                            entity.errMsg = throwable.toString();
                            return entity;
                        })
                        .subscribe(bapResultEntity -> {
                            if (bapResultEntity.dealSuccessFlag){
                                getView().submitSparePartApplySuccess(bapResultEntity);
                            }else {
                                getView().submitSparePartApplyFailed(bapResultEntity.errMsg);
                            }
                        })
        );
    }
}
