package com.supcon.mes.module_sbda_online.presenter;

import android.text.TextUtils;

import com.supcon.mes.module_sbda_online.model.bean.RepairListEntity;
import com.supcon.mes.module_sbda_online.model.contract.RepairContract;
import com.supcon.mes.module_sbda_online.model.network.SBDAOnlineHttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/1
 * ------------- Description -------------
 */
public class RepairPresenter extends RepairContract.Presenter {
    @Override
    public void workRecord(Long beamID, int page) {
        Map<String, Object> pageQueryParams = new HashMap<>();
        pageQueryParams.put("page.pageNo", page);
        pageQueryParams.put("page.pageSize", 20);
        pageQueryParams.put("page.maxPageSize", 500);
        mCompositeSubscription.add(SBDAOnlineHttpClient.workRecord(beamID, pageQueryParams)
                .onErrorReturn(throwable -> {
                    RepairListEntity repairListEntity = new RepairListEntity();
                    repairListEntity.errMsg = throwable.toString();
                    return repairListEntity;
                }).subscribe(repairListEntity -> {
                    if (TextUtils.isEmpty(repairListEntity.errMsg)) {
                        getView().workRecordSuccess(repairListEntity);
                    } else {
                        getView().workRecordFailed(repairListEntity.errMsg);
                    }
                }));
    }
}
