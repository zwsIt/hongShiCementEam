package com.supcon.mes.module_main.presenter;

import android.text.TextUtils;

import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_main.model.bean.WarnDailyWorkEntity;
import com.supcon.mes.module_main.model.contract.WarnPendingListContract;
import com.supcon.mes.module_main.model.network.MainClient;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WarnPendingPresenter extends WarnPendingListContract.Presenter {

    @Override
    public void listWarnPending(Map<String, Object> queryParam, int page, int pageSize) {
        FastQueryCondEntity fastQueryCond = BAPQueryParamsHelper.createSingleFastQueryCond(queryParam);

        Map<String, Object> pageQueryParams = new HashMap<>();
        pageQueryParams.put("page.pageNo",page);
        pageQueryParams.put("page.pageSize",pageSize);
        pageQueryParams.put("page.maxPageSize",500);

        fastQueryCond.modelAlias = "allEarlyWarningInfo";

        mCompositeSubscription.add(MainClient.listWarnInfos(fastQueryCond, pageQueryParams)
                .subscribeOn(Schedulers.io())
                .onErrorReturn(throwable -> {
                    CommonBAPListEntity<WarnDailyWorkEntity> commonBAPListEntity = new CommonBAPListEntity<>();
                    commonBAPListEntity.errMsg = HttpErrorReturnUtil.getErrorInfo(throwable);
                    return commonBAPListEntity;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(warnDailyWorkEntityCommonBAPListEntity -> {
                    if (TextUtils.isEmpty(warnDailyWorkEntityCommonBAPListEntity.errMsg)) {
                        getView().listWarnPendingSuccess(warnDailyWorkEntityCommonBAPListEntity);
                    } else {
                        getView().listWarnPendingFailed(warnDailyWorkEntityCommonBAPListEntity.errMsg);
                    }
                }));
    }
}
