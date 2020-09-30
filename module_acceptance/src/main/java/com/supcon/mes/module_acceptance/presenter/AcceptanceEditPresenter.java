package com.supcon.mes.module_acceptance.presenter;

import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_acceptance.model.bean.AcceptanceEditEntity;
import com.supcon.mes.module_acceptance.model.contract.AcceptanceEditContract;
import com.supcon.mes.module_acceptance.model.network.AcceptanceHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/6/27
 * ------------- Description -------------
 */
public class AcceptanceEditPresenter extends AcceptanceEditContract.Presenter {
    @Override
    public void getAcceptanceEdit(Long eamId, Long tableId) {

        if (tableId == null){
            mCompositeSubscription.add(AcceptanceHttpClient.getAcceptanceEditByEam(eamId)
                    .onErrorReturn(new Function<Throwable, CommonBAPListEntity<AcceptanceEditEntity>>() {
                        @Override
                        public CommonBAPListEntity<AcceptanceEditEntity> apply(Throwable throwable) throws Exception {
                            CommonBAPListEntity<AcceptanceEditEntity> acceptanceEditEntity = new CommonBAPListEntity<>();
                            acceptanceEditEntity.errMsg = HttpErrorReturnUtil.getErrorInfo(throwable);
                            return acceptanceEditEntity;
                        }
                    }).subscribe(new Consumer<CommonBAPListEntity<AcceptanceEditEntity>>() {
                        @Override
                        public void accept(CommonBAPListEntity<AcceptanceEditEntity> acceptanceEditEntity) throws Exception {
                            if (acceptanceEditEntity.success) {
                                getView().getAcceptanceEditSuccess(acceptanceEditEntity);
                            } else {
                                getView().getAcceptanceEditFailed(acceptanceEditEntity.errMsg);
                            }
                        }
                    }));
        }else {
            mCompositeSubscription.add(AcceptanceHttpClient.getAcceptanceEditByDg(tableId)
                    .onErrorReturn(new Function<Throwable, CommonBAPListEntity<AcceptanceEditEntity>>() {
                        @Override
                        public CommonBAPListEntity<AcceptanceEditEntity> apply(Throwable throwable) throws Exception {
                            CommonBAPListEntity<AcceptanceEditEntity> acceptanceEditEntity = new CommonBAPListEntity<>();
                            acceptanceEditEntity.errMsg = HttpErrorReturnUtil.getErrorInfo(throwable);
                            return acceptanceEditEntity;
                        }
                    }).subscribe(new Consumer<CommonBAPListEntity<AcceptanceEditEntity>>() {
                        @Override
                        public void accept(CommonBAPListEntity<AcceptanceEditEntity> acceptanceEditEntity) throws Exception {
                            if (acceptanceEditEntity.result != null) {
                                getView().getAcceptanceEditSuccess(acceptanceEditEntity);
                            } else {
                                getView().getAcceptanceEditFailed(acceptanceEditEntity.errMsg);
                            }
                        }
                    }));
        }


    }
}
