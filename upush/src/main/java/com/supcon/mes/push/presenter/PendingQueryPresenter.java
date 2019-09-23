package com.supcon.mes.push.presenter;

import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.WXGDEntity;
import com.supcon.mes.middleware.model.bean.YHEntity;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.push.model.contract.PendingQueryContract;
import com.supcon.mes.push.model.network.PushHttpClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangshizhan on 2019/4/30
 * Email:wangshizhan@supcom.com
 */
public class PendingQueryPresenter extends PendingQueryContract.Presenter {
    @Override
    public void queryYH(String tableNo) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put(Constant.BAPQuery.TABLE_NO, tableNo);

        FastQueryCondEntity fastQueryCondEntity = BAPQueryParamsHelper.createSingleFastQueryCond(queryParams);
        fastQueryCondEntity.modelAlias = "faultInfo";

        LogUtil.d("fastQueryCondEntity:"+fastQueryCondEntity);
        mCompositeSubscription.add(
                PushHttpClient.queryFaultInfotPending(fastQueryCondEntity)
                        .onErrorReturn(new Function<Throwable, CommonBAPListEntity<YHEntity>>() {
                            @Override
                            public CommonBAPListEntity<YHEntity> apply(Throwable throwable) throws Exception {
                                CommonBAPListEntity commonBAPListEntity = new CommonBAPListEntity();
                                commonBAPListEntity.success = false;
                                commonBAPListEntity.errMsg = throwable.toString();
                                return commonBAPListEntity;
                            }
                        })
                        .subscribe(new Consumer<CommonBAPListEntity<YHEntity>>() {
                            @Override
                            public void accept(CommonBAPListEntity<YHEntity> commonBAPListEntity) throws Exception {
                                if (commonBAPListEntity.result != null) {
                                    Objects.requireNonNull(getView()).queryYHSuccess(commonBAPListEntity);
                                } else {

                                    Objects.requireNonNull(getView()).queryYHFailed(commonBAPListEntity.errMsg);

                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Objects.requireNonNull(getView()).queryYHFailed(throwable.toString());
                            }
                        })
        );
    }

    @Override
    public void queryWXGD(String tableNo) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put(Constant.BAPQuery.TABLE_NO, tableNo);

        FastQueryCondEntity fastQueryCondEntity = BAPQueryParamsHelper.createSingleFastQueryCond(queryParams);
        fastQueryCondEntity.modelAlias = "workRecord";


        mCompositeSubscription.add(
                PushHttpClient.queryWXGDPending(fastQueryCondEntity)
                        .onErrorReturn(throwable -> {
                            CommonBAPListEntity<WXGDEntity> wxgdListEntity = new CommonBAPListEntity<>();
                            wxgdListEntity.success = false;
                            wxgdListEntity.errMsg = throwable.toString();
                            return wxgdListEntity;
                        }).subscribe(wxgdListEntity -> {
                    if (wxgdListEntity.result != null) {
                        Objects.requireNonNull(getView()).queryWXGDSuccess(wxgdListEntity);
                    } else {
                        Objects.requireNonNull(getView()).queryWXGDFailed(wxgdListEntity.errMsg);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Objects.requireNonNull(getView()).queryWXGDFailed(throwable.toString());
                    }
                })
        );

    }
}
