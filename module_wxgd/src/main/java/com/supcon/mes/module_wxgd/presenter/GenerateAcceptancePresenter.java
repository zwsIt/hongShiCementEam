package com.supcon.mes.module_wxgd.presenter;

import com.supcon.mes.module_wxgd.model.bean.GenerateAcceptanceEntity;
import com.supcon.mes.module_wxgd.model.contract.GenerateAcceptanceContract;
import com.supcon.mes.module_wxgd.model.network.HttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/7/2
 * ------------- Description -------------
 */
public class GenerateAcceptancePresenter extends GenerateAcceptanceContract.Presenter {
    @Override
    public void generateCheckApply(long workOrderId, long eamId, String describe, long installId) {
        mCompositeSubscription.add(
                HttpClient.generateCheckApply(workOrderId, eamId, describe, installId)
                        .onErrorReturn(new Function<Throwable, GenerateAcceptanceEntity>() {
                            @Override
                            public GenerateAcceptanceEntity apply(Throwable throwable) throws Exception {
                                GenerateAcceptanceEntity generateAcceptanceEntity = new GenerateAcceptanceEntity();
                                return generateAcceptanceEntity;
                            }
                        })
                        .subscribe(new Consumer<GenerateAcceptanceEntity>() {
                            @Override
                            public void accept(GenerateAcceptanceEntity generateAcceptanceEntity) throws Exception {
                                if (generateAcceptanceEntity.success) {
                                    getView().generateCheckApplySuccess(generateAcceptanceEntity);
                                } else {
                                    getView().generateCheckApplyFailed(generateAcceptanceEntity.errMsg);
                                }
                            }
                        }));
    }
}
