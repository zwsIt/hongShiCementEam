package com.supcon.mes.module_wxgd.presenter;


import android.text.TextUtils;

import com.supcon.mes.module_wxgd.model.bean.SparePartApplyHeaderInfoEntity;
import com.supcon.mes.module_wxgd.model.contract.TableInfoContract;
import com.supcon.mes.module_wxgd.model.network.HttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class TableInfoPresenter extends TableInfoContract.Presenter {
    @Override
    public void getSparePartApplyTableInfo(Long id, String includes) {
        mCompositeSubscription.add(
                HttpClient.get(id,includes)
                .onErrorReturn(throwable -> {
                    SparePartApplyHeaderInfoEntity entity = new SparePartApplyHeaderInfoEntity();
                    entity.errMsg = throwable.toString();
                    return entity;
                })
                .subscribe(sparePartApplyHeaderInfoEntity -> {
                    if (TextUtils.isEmpty(sparePartApplyHeaderInfoEntity.errMsg)){
                        getView().getSparePartApplyTableInfoSuccess(sparePartApplyHeaderInfoEntity);
                    }else {
                        getView().getSparePartApplyTableInfoFailed(sparePartApplyHeaderInfoEntity.errMsg);
                    }
                })
        );
    }
}
