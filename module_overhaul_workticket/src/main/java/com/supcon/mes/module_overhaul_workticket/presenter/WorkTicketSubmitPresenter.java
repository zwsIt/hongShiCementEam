package com.supcon.mes.module_overhaul_workticket.presenter;

import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.middleware.util.FormDataHelper;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_overhaul_workticket.model.contract.WorkTicketSubmitContract;
import com.supcon.mes.module_overhaul_workticket.model.network.HttpClient;

import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/24
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class WorkTicketSubmitPresenter extends WorkTicketSubmitContract.Presenter {

    @Override
    public void submit(String view, Map<String, Object> queryParams, Map<String, Object> attachmentMap, String __pc__) {
        Map<String, RequestBody> params = FormDataHelper.createDataFormBody(queryParams);
        List<MultipartBody.Part> part = FormDataHelper.createFileForm(attachmentMap);
        mCompositeSubscription.add(
                HttpClient.submit(view,params,part,__pc__)
                .onErrorReturn(new Function<Throwable, BapResultEntity>() {
                    @Override
                    public BapResultEntity apply(Throwable throwable) throws Exception {
                        BapResultEntity bapResultEntity = new BapResultEntity();
                        bapResultEntity.errMsg = throwable.toString();
                        return bapResultEntity;
                    }
                })
                .subscribe(new Consumer<BapResultEntity>() {
                    @Override
                    public void accept(BapResultEntity bapResultEntity) throws Exception {
                        if (bapResultEntity.dealSuccessFlag){
                            getView().submitSuccess(bapResultEntity);
                        }else {
                            getView().submitFailed(bapResultEntity.errMsg);
                        }
                    }
                })
        );
    }

    @Override
    public void retrial(String offApplyTableNo) {
        mCompositeSubscription.add(
                HttpClient.retrial(offApplyTableNo)
                        .onErrorReturn(new Function<Throwable, CommonEntity<Boolean>>() {
                            @Override
                            public CommonEntity<Boolean> apply(Throwable throwable) throws Exception {
                                CommonEntity<Boolean> commonEntity = new CommonEntity<>();
                                commonEntity.errMsg = HttpErrorReturnUtil.getErrorInfo(throwable);
                                return commonEntity;
                            }
                        })
                        .subscribe(new Consumer<CommonEntity<Boolean>>() {
                            @Override
                            public void accept(CommonEntity<Boolean> booleanCommonEntity) throws Exception {
                                if (booleanCommonEntity.success){
                                    getView().retrialSuccess(booleanCommonEntity);
                                }else {
                                    getView().retrialFailed(booleanCommonEntity.errMsg);
                                }
                            }
                        })
        );
    }
}
