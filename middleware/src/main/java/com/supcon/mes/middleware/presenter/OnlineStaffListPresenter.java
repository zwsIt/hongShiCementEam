package com.supcon.mes.middleware.presenter;

import android.text.TextUtils;

import com.supcon.mes.middleware.model.bean.TxlListEntity;
import com.supcon.mes.middleware.model.contract.OnlineStaffListContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;

/**
 * @Author xushiyun
 * @Create-time 7/23/19
 * @Pageage com.supcon.mes.middleware.presenter
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
public class OnlineStaffListPresenter extends OnlineStaffListContract.Presenter {
    @Override
    public void getOnlineStaffList() {
        mCompositeSubscription.add(
                MiddlewareHttpClient.getStaffList(0)
                        .onErrorReturn(throwable -> {
                            TxlListEntity txlListEntity = new TxlListEntity();
                            txlListEntity.errMsg = throwable.toString();
                            return txlListEntity;
                        })
                        .subscribe(txlListEntity -> {
                            if (TextUtils.isEmpty(txlListEntity.errMsg)) {
                                getView().getOnlineStaffListSuccess(txlListEntity);
                            }else {
                                getView().getOnlineStaffListFailed(txlListEntity.errMsg);
                            }
                        }));
    }
    
}
