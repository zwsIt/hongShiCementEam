package com.supcon.mes.module_main.presenter;

import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.middleware.model.bean.ResultEntity;
import com.supcon.mes.module_main.model.contract.WaitDealtSubmitContract;
import com.supcon.mes.module_main.model.network.MainClient;

import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/7/24
 * ------------- Description -------------
 */
public class WaitDealtSubmitPresenter extends WaitDealtSubmitContract.Presenter {
    @Override
    public void bulkSubmitCustom(Map<String, Object> queryMap) {
        mCompositeSubscription.add(MainClient.bulkSubmitCustom(queryMap)
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
                            getView().bulkSubmitCustomSuccess(resultEntity);
                        } else {
                            getView().bulkSubmitCustomFailed(resultEntity.errMsg);
                        }
                    }
                }));
    }
}
