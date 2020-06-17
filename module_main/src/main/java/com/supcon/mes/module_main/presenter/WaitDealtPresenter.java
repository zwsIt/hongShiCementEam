package com.supcon.mes.module_main.presenter;

import android.text.TextUtils;

import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.module_main.model.bean.WaitDealtEntity;
import com.supcon.mes.module_main.model.contract.WaitDealtContract;
import com.supcon.mes.module_main.model.network.MainClient;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/7/24
 * ------------- Description -------------
 */
public class WaitDealtPresenter extends WaitDealtContract.Presenter {
    //工作提醒
    @Override
    public void getWaitDealt(int page, int pageSize, Map<String, Object> params) {
        FastQueryCondEntity fastQueryCond;
        Map<String, Object> paramsName;
        if (params.containsKey(Constant.BAPQuery.SUBORDINATE)){
            params.remove(Constant.BAPQuery.SUBORDINATE);
            fastQueryCond = BAPQueryParamsHelper.createSingleFastQueryCond(params);

            JoinSubcondEntity joinSubcondEntity;
            paramsName = new HashMap<>();
            paramsName.put(Constant.BAPQuery.SUBORDINATE_DEPARTMENT, EamApplication.getAccountInfo().departmentId);
            joinSubcondEntity = BAPQueryParamsHelper.createJoinSubcondEntity(paramsName, "BASE_DEPARTMENT,ID,ALL_PERSON_WORK_INFO,DEPARTMENT_ID");
            fastQueryCond.subconds.add(joinSubcondEntity);
            paramsName.remove(Constant.BAPQuery.SUBORDINATE_DEPARTMENT);
            paramsName.put(Constant.BAPQuery.SUBORDINATE_POSITION, EamApplication.getAccountInfo().positionId);
            joinSubcondEntity = BAPQueryParamsHelper.createJoinSubcondEntity(paramsName, "BASE_POSITION,ID,ALL_PERSON_WORK_INFO,POSITION_ID");
            fastQueryCond.subconds.add(joinSubcondEntity);
        }else {
            fastQueryCond = BAPQueryParamsHelper.createSingleFastQueryCond(params);

            paramsName = new HashMap<>();
            paramsName.put(Constant.BAPQuery.NAME, EamApplication.getAccountInfo().staffName);
            paramsName.put(Constant.BAPQuery.ID, EamApplication.getAccountInfo().staffId);
            JoinSubcondEntity joinSubcondEntity = BAPQueryParamsHelper.createJoinSubcondEntity(paramsName, "base_staff,ID,ALL_PERSON_WORK_INFO,STAFFID");
            fastQueryCond.subconds.add(joinSubcondEntity);
        }

        Map<String, Object> pageQueryParams = new HashMap<>();
        pageQueryParams.put("page.pageNo", page);
        pageQueryParams.put("page.pageSize", pageSize);
        pageQueryParams.put("page.maxPageSize", 500);

        fastQueryCond.modelAlias = "allPersonWorkInfo";
        mCompositeSubscription.add(
                MainClient.getWaitDealtNew(fastQueryCond, pageQueryParams)
                .onErrorReturn(new Function<Throwable, CommonBAPListEntity<WaitDealtEntity>>() {
                    @Override
                    public CommonBAPListEntity<WaitDealtEntity> apply(Throwable throwable) throws Exception {
                        CommonBAPListEntity<WaitDealtEntity> waitDealtEntity = new CommonBAPListEntity<>();
                        waitDealtEntity.errMsg = throwable.toString();
                        return waitDealtEntity;
                    }
                }).subscribe(new Consumer<CommonBAPListEntity<WaitDealtEntity>>() {
                    @Override
                    public void accept(CommonBAPListEntity<WaitDealtEntity> waitDealtEntity) throws Exception {
                        if (waitDealtEntity.result != null) {
                            getView().getWaitDealtSuccess(waitDealtEntity);
                        } else {
                            getView().getWaitDealtFailed(waitDealtEntity.errMsg);
                        }
                    }
                }));
    }

    //工作提醒委托
    @Override
    public void proxyPending(long pendingId, String proxyUserIds, String proxDesc) {
        Map<String, Object> pageQueryParams = new HashMap<>();
        pageQueryParams.put("pendingId", pendingId);
        pageQueryParams.put("pendingIds", "");
        pageQueryParams.put("proxyUsers", proxyUserIds);
        pageQueryParams.put("proxyType", 2);
        pageQueryParams.put("proxyUsers_MultiIDs", proxyUserIds);
        pageQueryParams.put("proxyUsers_DeleteIds", "");
        pageQueryParams.put("proxyUsers_AddIds", proxyUserIds);
        pageQueryParams.put("proxyUsers_", "");
        if (!TextUtils.isEmpty(proxDesc)) {
            pageQueryParams.put("proxDesc", proxDesc);
        }

        mCompositeSubscription.add(MainClient.proxyPending(pageQueryParams)
                .onErrorReturn(throwable -> {
                    BapResultEntity bapResultEntity = new BapResultEntity();
                    bapResultEntity.errMsg = throwable.toString();
                    return bapResultEntity;
                }).subscribe(bapResultEntity -> {
                    if (bapResultEntity.dealSuccessFlag) {
                        getView().proxyPendingSuccess(bapResultEntity);
                    } else {
                        getView().proxyPendingFailed(bapResultEntity.errMsg);
                    }
                }));
    }
}
