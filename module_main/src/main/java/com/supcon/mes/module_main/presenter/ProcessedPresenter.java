package com.supcon.mes.module_main.presenter;

import android.text.TextUtils;

import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.middleware.model.bean.ProcessedEntity;
import com.supcon.mes.module_main.model.contract.ProcessedContract;
import com.supcon.mes.module_main.model.network.MainClient;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class ProcessedPresenter extends ProcessedContract.Presenter {

    @Override
    public void workflowHandleList(Map<String, Object> queryParam, int page, int pageSize) {
        FastQueryCondEntity fastQueryCond = BAPQueryParamsHelper.createSingleFastQueryCond(queryParam);
        Map<String, Object> paramsName = new HashMap<>();
        paramsName.put(Constant.BAPQuery.NAME, EamApplication.getAccountInfo().staffName);
        paramsName.put(Constant.BAPQuery.ID, EamApplication.getAccountInfo().staffId);
        JoinSubcondEntity joinSubcondEntity = BAPQueryParamsHelper.createJoinSubcondEntity(paramsName, "base_staff,ID,ALL_PROCESS_INFO,STAFF");
        fastQueryCond.subconds.add(joinSubcondEntity);

        Map<String, Object> pageQueryParams = new HashMap<>();
        pageQueryParams.put("page.pageNo",page);
        pageQueryParams.put("page.pageSize",pageSize);
        pageQueryParams.put("page.maxPageSize",100);

        Flowable<CommonBAPListEntity<ProcessedEntity>> mainClient;
//        if (EamApplication.isHailuo()){
            fastQueryCond.modelAlias = "allProcessInfo";
            mainClient = MainClient.workflowHandleListNew(fastQueryCond, pageQueryParams);
//        }else {
//            fastQueryCond.modelAlias = "processFlowInfo";
//            mainClient =MainClient.workflowHandleList(fastQueryCond, pageQueryParams);
//        }

        mCompositeSubscription.add(mainClient
                .onErrorReturn(new Function<Throwable, CommonBAPListEntity<ProcessedEntity>>() {
                    @Override
                    public CommonBAPListEntity<ProcessedEntity> apply(Throwable throwable) throws Exception {
                        CommonBAPListEntity<ProcessedEntity> processedEntitys = new CommonBAPListEntity<>();
                        processedEntitys.errMsg = throwable.toString();
                        return processedEntitys;
                    }
                }).subscribe(new Consumer<CommonBAPListEntity<ProcessedEntity>>() {
                    @Override
                    public void accept(CommonBAPListEntity<ProcessedEntity> processedEntitys) throws Exception {
                        if (TextUtils.isEmpty(processedEntitys.errMsg)) {
                            getView().workflowHandleListSuccess(processedEntitys);
                        } else {
                            getView().workflowHandleListFailed(processedEntitys.errMsg);
                        }
                    }
                }));
    }
}
