package com.supcon.mes.module_sbda_online.presenter;

import com.supcon.mes.module_sbda_online.model.bean.RoutineCommonEntity;
import com.supcon.mes.module_sbda_online.model.contract.RoutineContract;
import com.supcon.mes.module_sbda_online.model.network.SBDAOnlineHttpClient;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/2
 * ------------- Description -------------
 */
public class RoutinePresenter extends RoutineContract.Presenter {
    @Override
    public void getEamOtherInfo(Long eamID) {
        mCompositeSubscription.add(SBDAOnlineHttpClient.getEamOtherInfo(eamID)
                .onErrorReturn(throwable -> {
                    RoutineCommonEntity routineEntity = new RoutineCommonEntity();
                    routineEntity.errMsg = throwable.toString();
                    return routineEntity;
                }).subscribe(routineEntity -> {
                    if (routineEntity.result != null) {
                        getView().getEamOtherInfoSuccess(routineEntity);
                    } else {
                        getView().getEamOtherInfoFailed("获取运行时间失败");
                    }
                }));
    }
}
