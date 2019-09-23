package com.supcon.mes.module_main.presenter;

import android.text.TextUtils;

import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.module_main.model.bean.EamEntity;
import com.supcon.mes.module_main.model.contract.EamContract;
import com.supcon.mes.module_main.model.network.MainClient;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class EamPresenter extends EamContract.Presenter {
    @Override
    public void getEams(int page) {
        Map<String, Object> pageQueryParams = new HashMap<>();
        pageQueryParams.put("page.pageNo", page);
        pageQueryParams.put("page.pageSize", 20);
        pageQueryParams.put("page.maxPageSize", 500);
        mCompositeSubscription.add(MainClient.getEams(String.valueOf(EamApplication.getAccountInfo().staffId), "1", pageQueryParams)
                .onErrorReturn(new Function<Throwable, CommonBAPListEntity<EamEntity>>() {
                    @Override
                    public CommonBAPListEntity<EamEntity> apply(Throwable throwable) throws Exception {
                        CommonBAPListEntity<EamEntity> baseEntityCommon = new CommonBAPListEntity<>();
                        baseEntityCommon.errMsg = throwable.toString();
                        return baseEntityCommon;
                    }
                }).subscribe(new Consumer<CommonBAPListEntity<EamEntity>>() {
                    @Override
                    public void accept(CommonBAPListEntity<EamEntity> eamEntityCommon) throws Exception {
                        if (TextUtils.isEmpty(eamEntityCommon.errMsg)) {
                            getView().getEamsSuccess(eamEntityCommon);
                        } else {
                            getView().getEamsFailed(eamEntityCommon.errMsg);
                        }
                    }
                }));
    }
}
