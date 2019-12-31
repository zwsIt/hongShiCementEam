package com.supcon.mes.module_sparepartapply_hl.presenter;

import com.supcon.mes.middleware.model.bean.SparePartReceiveListEntity;
import com.supcon.mes.module_sparepartapply_hl.model.contract.SparePartApplyDetailContract;
import com.supcon.mes.module_wxgd.model.network.HttpClient;

public class SparePartApplyDetailPresenter extends SparePartApplyDetailContract.Presenter {
    @Override
    public void listSparePartApplyDetail(String url, Long id) {
        mCompositeSubscription.add(
                HttpClient.listSparePartApplyDetail(url, id)
                .onErrorReturn(throwable -> {
                    SparePartReceiveListEntity entity = new SparePartReceiveListEntity();
                    entity.errMsg = throwable.toString();
                    return entity;
                })
                .subscribe(sparePartReceiveListEntity -> {
                    if (sparePartReceiveListEntity.result != null){
                        getView().listSparePartApplyDetailSuccess(sparePartReceiveListEntity);
                    }else {
                        getView().listSparePartApplyDetailFailed(sparePartReceiveListEntity.errMsg);
                    }
                })
        );
    }
}
