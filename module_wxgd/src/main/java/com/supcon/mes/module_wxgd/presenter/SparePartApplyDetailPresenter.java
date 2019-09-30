package com.supcon.mes.module_wxgd.presenter;

import com.supcon.mes.middleware.model.bean.SparePartReceiveListEntity;
import com.supcon.mes.module_wxgd.model.contract.SparePartApplyDetailContract;
import com.supcon.mes.module_wxgd.model.network.HttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class SparePartApplyDetailPresenter extends SparePartApplyDetailContract.Presenter {
    @Override
    public void listSparePartApplyDetail(Long id) {
        mCompositeSubscription.add(
                HttpClient.listSparePartApplyDetail(id)
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
