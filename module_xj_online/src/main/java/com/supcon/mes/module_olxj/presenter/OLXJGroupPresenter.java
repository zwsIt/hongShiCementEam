package com.supcon.mes.module_olxj.presenter;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.module_olxj.model.bean.OLXJGroupEntity;
import com.supcon.mes.module_olxj.model.contract.OLXJGroupContract;
import com.supcon.mes.module_olxj.model.network.OLXJClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangshizhan on 2019/3/29
 * Email:wangshizhan@supcom.com
 */
public class OLXJGroupPresenter extends OLXJGroupContract.Presenter {

    @Override
    public void queryGroupList() {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put(Constant.BAPQuery.IS_RUN, "1");
        FastQueryCondEntity fastQueryCond = BAPQueryParamsHelper.createSingleFastQueryCond(queryParams);
        fastQueryCond.modelAlias = "workGroup";

        mCompositeSubscription.add(
                OLXJClient.queryWorkGroupList(fastQueryCond)
                        .onErrorReturn(new Function<Throwable, CommonBAPListEntity<OLXJGroupEntity>>() {
                            @Override
                            public CommonBAPListEntity apply(Throwable throwable) throws Exception {
                                CommonBAPListEntity commonBAPListEntity = new CommonBAPListEntity();
                                commonBAPListEntity.success = false;
                                commonBAPListEntity.errMsg = throwable.toString();

                                return commonBAPListEntity;
                            }
                        })
                        .subscribe(new Consumer<CommonBAPListEntity>() {
                            @Override
                            public void accept(CommonBAPListEntity commonBAPListEntity) throws Exception {

                                if (commonBAPListEntity.result != null) {
                                    Objects.requireNonNull(getView()).queryGroupListSuccess(commonBAPListEntity);
                                } else {
                                    Objects.requireNonNull(getView()).queryGroupListFailed(commonBAPListEntity.errMsg);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Objects.requireNonNull(getView()).queryGroupListFailed(throwable.toString());
                            }
                        }));
    }

    @Override
    public void queryTaskGroupList() {

    }
}
