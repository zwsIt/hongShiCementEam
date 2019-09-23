package com.supcon.mes.module_txl.presenter;

import android.text.TextUtils;

import com.supcon.mes.middleware.model.bean.TxlListEntity;
import com.supcon.mes.module_txl.model.contract.TxlListContract;
import com.supcon.mes.module_txl.model.network.TxlHttpClient;

import io.reactivex.functions.Function;

/**
 * @Author xushiyun
 * @Create-time 7/11/19
 * @Pageage com.supcon.mes.module_txl.presenter
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
public class TxlListPresenter extends TxlListContract.Presenter {
    @Override
    public void getTxlList(int pageNum,String p1, String p2,String p3) {
        mCompositeSubscription.add(
                TxlHttpClient.getStaffList(pageNum, p1, p2,  p3)
                        .onErrorReturn(new Function<Throwable, TxlListEntity>() {
                            @Override
                            public TxlListEntity apply(Throwable throwable) {
                                TxlListEntity txlListEntity = new TxlListEntity();
                                txlListEntity.success = false;
                                txlListEntity.errMsg = throwable.toString();
                                return new TxlListEntity();
                            }
                        }).subscribe(txlListEntity -> {
                    if (!TextUtils.isEmpty(txlListEntity.errMsg)) {
                        getView().getTxlListFailed(txlListEntity.errMsg);
                    } else {
                        getView().getTxlListSuccess(txlListEntity);
                    }
                }));
    }
}

