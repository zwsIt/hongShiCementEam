package com.supcon.mes.module_wxgd.presenter;

import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.module_wxgd.model.contract.WXGDStopOrActivateContract;
import com.supcon.mes.module_wxgd.model.network.HttpClient;

import java.util.Map;

import io.reactivex.functions.Consumer;

/**
 * @author yangfei.cao
 * @ClassName eam
 * @date 2018/9/6
 * ------------- Description -------------
 * 暂停或激活
 */
public class WXGDStopOrActivatePresenter extends WXGDStopOrActivateContract.Presenter {
    @Override
    public void stopOrStart(Map<String, Object> map) {
        mCompositeSubscription.add(HttpClient.stopOrActivate(map).onErrorReturn(throwable -> {
            CommonListEntity commonListEntity = new CommonListEntity();
            commonListEntity.errMsg = throwable.toString();
            return commonListEntity;
        }).subscribe(new Consumer<CommonListEntity>() {
            @Override
            public void accept(CommonListEntity commonListEntity) throws Exception {
                if (commonListEntity.success) {
                    getView().stopOrStartSuccess();
                } else {
                    getView().stopOrStartFailed("暂停失败");
                }
            }
        }));
    }
}
