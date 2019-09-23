package com.supcon.mes.module_main.presenter;

import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.module_main.model.bean.WorkNumEntity;
import com.supcon.mes.module_main.model.contract.EamAnomalyContract;
import com.supcon.mes.module_main.model.network.MainClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/7/30
 * ------------- Description -------------
 */
public class EamAnomalyPresenter extends EamAnomalyContract.Presenter {
    //代办数量
    @Override
    public void getMainWorkCount(String staffID) {
        mCompositeSubscription.add(MainClient.getMainWorkCount(staffID)
                .onErrorReturn(new Function<Throwable, CommonBAPListEntity<WorkNumEntity>>() {
                    @Override
                    public CommonBAPListEntity<WorkNumEntity> apply(Throwable throwable) throws Exception {
                        CommonBAPListEntity<WorkNumEntity> workNumEntity = new CommonBAPListEntity<>();
                        workNumEntity.errMsg = throwable.toString();
                        return workNumEntity;
                    }
                }).subscribe(new Consumer<CommonBAPListEntity<WorkNumEntity>>() {
                    @Override
                    public void accept(CommonBAPListEntity<WorkNumEntity> workNumEntity) throws Exception {
                        if (workNumEntity.result != null) {
                            getView().getMainWorkCountSuccess(workNumEntity);
                        } else {
                            getView().getMainWorkCountFailed(workNumEntity.errMsg);
                        }
                    }
                }));
    }

    //提示信息
    @Override
    public void getSloganInfo() {
        mCompositeSubscription.add(MainClient.getSloganInfo()
                .onErrorReturn(new Function<Throwable, CommonEntity<String>>() {
                    @Override
                    public CommonEntity<String> apply(Throwable throwable) throws Exception {
                        CommonEntity<String> waitDealtEntity = new CommonEntity<>();
                        waitDealtEntity.errMsg = throwable.toString();
                        return waitDealtEntity;
                    }
                }).subscribe(new Consumer<CommonEntity<String>>() {
                    @Override
                    public void accept(CommonEntity<String> result) throws Exception {
                        if (result.success) {
                            getView().getSloganInfoSuccess(result);
                        } else {
                            getView().getSloganInfoFailed(result.errMsg);
                        }
                    }
                }));
    }
}
