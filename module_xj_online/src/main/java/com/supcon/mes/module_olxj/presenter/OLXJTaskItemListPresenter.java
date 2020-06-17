package com.supcon.mes.module_olxj.presenter;

import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.middleware.util.PageParamUtil;
import com.supcon.mes.module_olxj.model.bean.OLXJTaskEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJWorkItemEntity;
import com.supcon.mes.module_olxj.model.contract.OLXJTaskRecordsContract;
import com.supcon.mes.module_olxj.model.contract.OLXJTodayTaskItemListContract;
import com.supcon.mes.module_olxj.model.network.OLXJClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by zhangwenshuai1 on 2018/3/12.
 */

public class OLXJTaskItemListPresenter extends OLXJTodayTaskItemListContract.Presenter {

    @Override
    public void getWorkItemList(long taskId, long workId, int pageIndex) {
        Map<String, Object> pageQueryParams = PageParamUtil.pageQueryParam(pageIndex,100);
        pageQueryParams.put("taskIDs",taskId);
        pageQueryParams.put("workID",workId);

        mCompositeSubscription.add(
                OLXJClient.taskGatherList(pageQueryParams)
                .onErrorReturn(throwable -> {
                    CommonBAPListEntity<OLXJWorkItemEntity> commonBAPListEntity = new CommonBAPListEntity<>();
                    commonBAPListEntity.errMsg = HttpErrorReturnUtil.getErrorInfo(throwable);
                    return commonBAPListEntity;
                })
                .subscribe(olxjWorkItemEntityCommonBAPListEntity -> {
                    if (olxjWorkItemEntityCommonBAPListEntity.result !=null){
                        getView().getWorkItemListSuccess(olxjWorkItemEntityCommonBAPListEntity);
                    }else {
                        getView().getWorkItemListFailed(olxjWorkItemEntityCommonBAPListEntity.errMsg);
                    }
                })
        );
    }
}
