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
        FastQueryCondEntity fastQueryCond = BAPQueryParamsHelper.createSingleFastQueryCond(params);

        Map<String, Object> paramsName = new HashMap<>();
        paramsName.put(Constant.BAPQuery.NAME, EamApplication.getAccountInfo().staffName);
        JoinSubcondEntity joinSubcondEntity = BAPQueryParamsHelper.crateJoinSubcondEntity(paramsName, "base_staff,ID,BEAM2_PERSONWORKINFO,STAFFID");
        fastQueryCond.subconds.add(joinSubcondEntity);

        Map<String, Object> pageQueryParams = new HashMap<>();
        pageQueryParams.put("page.pageNo", page);
        pageQueryParams.put("page.pageSize", pageSize);
        pageQueryParams.put("page.maxPageSize", 500);

        Flowable<CommonBAPListEntity<WaitDealtEntity>> mainClient;
        if (EamApplication.isHailuo()){
            fastQueryCond.modelAlias = "allPersonWorkInfo";
            mainClient = MainClient.getWaitDealtByHaiLuo(fastQueryCond, pageQueryParams);
        }else {
            fastQueryCond.modelAlias = "personworkinfo";
            mainClient =MainClient.getWaitDealt(fastQueryCond, pageQueryParams);
        }

        mCompositeSubscription.add(
                mainClient
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
    public void proxyPending(long pendingId, long proxyUserId, String proxDesc) {
        Map<String, Object> pageQueryParams = new HashMap<>();
        pageQueryParams.put("pendingId", pendingId);
        pageQueryParams.put("proxyUsers", proxyUserId);
        pageQueryParams.put("proxyType", 2);
        pageQueryParams.put("proxyUsers_MultiIDs", proxyUserId);
        pageQueryParams.put("proxyUsers_AddIds", proxyUserId);
        if (!TextUtils.isEmpty(proxDesc)) {
            pageQueryParams.put("proxDesc", proxDesc);
        }

        mCompositeSubscription.add(MainClient.proxyPending(pageQueryParams)
                .onErrorReturn(new Function<Throwable, BapResultEntity>() {
                    @Override
                    public BapResultEntity apply(Throwable throwable) throws Exception {
                        BapResultEntity bapResultEntity = new BapResultEntity();
                        bapResultEntity.errMsg = throwable.toString();
                        return bapResultEntity;
                    }
                }).subscribe(new Consumer<BapResultEntity>() {
                    @Override
                    public void accept(BapResultEntity bapResultEntity) throws Exception {
                        if (bapResultEntity.dealSuccessFlag) {
                            getView().proxyPendingSuccess(bapResultEntity);
                        } else {
                            getView().proxyPendingFailed(bapResultEntity.errMsg);
                        }
                    }
                }));
    }
}
