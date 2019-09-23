package com.supcon.mes.module_olxj.presenter;

import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.middleware.model.bean.ResultEntity;
import com.supcon.mes.module_olxj.model.bean.EamXJEntity;
import com.supcon.mes.module_olxj.model.contract.OLXJEamTaskContract;
import com.supcon.mes.module_olxj.model.network.OLXJClient;

import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class OLXJEamTaskPresenter extends OLXJEamTaskContract.Presenter {
    @Override
    public void createTempPotrolTaskByEam(Map<String, Object> paramMap) {
        mCompositeSubscription.add(OLXJClient.createTempPotrolTaskByEam(paramMap)
                .onErrorReturn(new Function<Throwable, CommonEntity<EamXJEntity>>() {
                    @Override
                    public CommonEntity<EamXJEntity> apply(Throwable throwable) throws Exception {
                        CommonEntity<EamXJEntity> waitDealtEntity = new CommonEntity<>();
                        waitDealtEntity.errMsg = throwable.toString();
                        return waitDealtEntity;
                    }
                }).subscribe(new Consumer<CommonEntity<EamXJEntity>>() {
                    @Override
                    public void accept(CommonEntity<EamXJEntity> result) throws Exception {
                        if (result.success) {
                            getView().createTempPotrolTaskByEamSuccess(result);
                        } else {
                            getView().createTempPotrolTaskByEamFailed(result.errMsg);
                        }
                    }
                }));
    }

    @Override
    public void updateTaskById(long taskID) {
        mCompositeSubscription.add(OLXJClient.updateTaskById(taskID)
                .onErrorReturn(new Function<Throwable, ResultEntity>() {
                    @Override
                    public ResultEntity apply(Throwable throwable) throws Exception {
                        ResultEntity resultEntity = new ResultEntity();
                        resultEntity.errMsg = throwable.toString();
                        return resultEntity;
                    }
                }).subscribe(new Consumer<ResultEntity>() {
                    @Override
                    public void accept(ResultEntity resultEntity) throws Exception {
                        if (resultEntity.success) {
                            getView().updateTaskByIdSuccess(resultEntity);
                        } else {
                            getView().updateTaskByIdFailed(resultEntity.errMsg);
                        }
                    }
                }));
    }
}
