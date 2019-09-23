package com.supcon.mes.middleware.presenter;

import android.text.TextUtils;

import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.middleware.model.bean.LongResultEntity;
import com.supcon.mes.middleware.model.contract.ModulePermissonCheckContract;
import com.supcon.mes.middleware.model.contract.UserPowerCheckContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;

import org.json.JSONObject;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangshizhan on 2018/9/21
 * Email:wangshizhan@supcom.com
 */
public class UserPowerCheckPresenter extends UserPowerCheckContract.Presenter {
    @Override
    public void checkUserPower(long companyId, String menuOperateCodes) {
        mCompositeSubscription.add(
                MiddlewareHttpClient.checkUserPower(companyId, menuOperateCodes)
                        .onErrorReturn(new Function<Throwable, Object>() {
                            @Override
                            public Object apply(Throwable throwable) throws Exception {
                                return throwable.toString();
                            }
                        })
                        .subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object object) throws Exception {
                                if (object != null && !TextUtils.isEmpty(object.toString()) && !object.toString().contains("Exception")) {
                                    getView().checkUserPowerSuccess(object);
                                } else {
                                    getView().checkUserPowerFailed(object.toString());
                                }
                            }
                        })
        );

    }
}
