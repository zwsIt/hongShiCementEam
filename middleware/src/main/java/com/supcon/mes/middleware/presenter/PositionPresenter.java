package com.supcon.mes.middleware.presenter;

import com.supcon.mes.middleware.model.bean.PositionEntityListEntity;
import com.supcon.mes.middleware.model.contract.PositionQueryContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangshizhan on 2018/8/20
 * Email:wangshizhan@supcom.com
 */
public class PositionPresenter extends PositionQueryContract.Presenter {

    @Override
    public void listPosition() {
        mCompositeSubscription.add(
                MiddlewareHttpClient.listPosition(1)//暂时500个部门够用了
                        .onErrorReturn(new Function<Throwable, PositionEntityListEntity>() {
                            @Override
                            public PositionEntityListEntity apply(Throwable throwable) throws Exception {
                                PositionEntityListEntity positionEntityListEntity = new PositionEntityListEntity();
                                positionEntityListEntity.success = false;
                                positionEntityListEntity.errMsg = throwable.toString();
                                return positionEntityListEntity;
                            }
                        })
                        .subscribe(new Consumer<PositionEntityListEntity>() {
                            @Override
                            public void accept(PositionEntityListEntity positionEntityListEntity) throws Exception {
                                if (positionEntityListEntity.result != null && positionEntityListEntity.result.size() != 0) {
                                    getView().listPositionSuccess(positionEntityListEntity);
                                } else {
                                    getView().listPositionFailed(positionEntityListEntity.errMsg);
                                }
                            }
                        }));

    }
}
