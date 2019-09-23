package com.supcon.mes.middleware.presenter;

import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.WorkCountEntity;
import com.supcon.mes.middleware.model.contract.WorkCountContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;

import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/8/13
 * ------------- Description -------------
 */
public class WorkCountPresenter extends WorkCountContract.Presenter {
    @Override
    public void getWorkCount(String url, Map<String, Object> queryParam) {
        mCompositeSubscription.add(
                MiddlewareHttpClient.getWorkCount(url,queryParam)
                        .onErrorReturn(new Function<Throwable, CommonListEntity<WorkCountEntity>>() {
                            @Override
                            public CommonListEntity<WorkCountEntity> apply(Throwable throwable) throws Exception {
                                CommonListEntity<WorkCountEntity> workCountEntitys = new CommonListEntity<>();
                                workCountEntitys.errMsg = throwable.toString();
                                return workCountEntitys;
                            }
                        })
                        .subscribe(new Consumer<CommonListEntity<WorkCountEntity>>() {
                            @Override
                            public void accept(CommonListEntity<WorkCountEntity> workCountEntitys) throws Exception {
                                if (workCountEntitys.success) {
                                    getView().getWorkCountSuccess(workCountEntitys);
                                } else {
                                    getView().getWorkCountFailed(workCountEntitys.errMsg);
                                }
                            }
                        }));
    }
}
