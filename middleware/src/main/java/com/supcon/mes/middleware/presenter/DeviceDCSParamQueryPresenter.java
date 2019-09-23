package com.supcon.mes.middleware.presenter;

import android.annotation.SuppressLint;

import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.DeviceDCSEntity;
import com.supcon.mes.middleware.model.contract.DeviceDCSParamQueryContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangshizhan on 2019/5/28
 * Email:wangshizhan@supcom.com
 */
public class DeviceDCSParamQueryPresenter extends DeviceDCSParamQueryContract.Presenter {
    @SuppressLint("CheckResult")
    @Override
    public void getDeviceDCSParams(long eamId) {

        /*CommonListEntity<DeviceDCSEntity> deviceDCSEntityCommonListEntity = new CommonListEntity<>();
        deviceDCSEntityCommonListEntity.result = new ArrayList<>();
        deviceDCSEntityCommonListEntity.success = true;

        Flowable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        for(int i = 0; i < 5; i++) {
                            DeviceDCSEntity deviceDCSEntity = new DeviceDCSEntity();
                            deviceDCSEntity.name = "设备位号名称"+i;
                            deviceDCSEntity.itemNumber = ""+i;
                            deviceDCSEntity.minValue = "10";
                            deviceDCSEntity.maxValue = "20";
                            deviceDCSEntityCommonListEntity.result.add(deviceDCSEntity);
                        }

                        getView().getDeviceDCSParamsSuccess(deviceDCSEntityCommonListEntity);
                    }
                });*/

        mCompositeSubscription.add(
                MiddlewareHttpClient.getMeasParam(eamId)
                        .onErrorReturn(new Function<Throwable, CommonListEntity<DeviceDCSEntity>>() {
                            @Override
                            public CommonListEntity<DeviceDCSEntity> apply(Throwable throwable) throws Exception {
                                CommonListEntity<DeviceDCSEntity> commonListEntity = new CommonListEntity<>();
                                commonListEntity.success = false;
                                commonListEntity.errMsg = throwable.toString();
                                return commonListEntity;
                            }
                        })
                        .subscribe(new Consumer<CommonListEntity<DeviceDCSEntity>>() {
                            @Override
                            public void accept(CommonListEntity<DeviceDCSEntity> deviceDCSEntityCommonListEntity) throws Exception {
                                if (getView() != null) {
                                    if (deviceDCSEntityCommonListEntity.success) {
                                        getView().getDeviceDCSParamsSuccess(deviceDCSEntityCommonListEntity);
                                    } else {
                                        getView().getDeviceDCSParamsFailed(deviceDCSEntityCommonListEntity.errMsg);
                                    }
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                getView().getDeviceDCSParamsFailed(throwable.toString());
                            }
                        }));
    }
}
