package com.supcon.mes.module_sbda.presenter;

import com.supcon.common.com_http.util.RxSchedulers;
import com.supcon.mes.middleware.model.bean.CommonDeviceEntity;
import com.supcon.mes.middleware.util.DeviceManager;
import com.supcon.mes.middleware.model.bean.CommonSearchDeviceListEntity;
import com.supcon.mes.module_sbda.model.contract.CommonSearchDeviceContract;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;

/**
 * Environment: hongruijun
 * Created by Xushiyun on 2018/5/17.
 */

public class CommonSearchDevicePresenter extends CommonSearchDeviceContract.Presenter {
    @Override
    public void getSearchDevice(String moduleName, String blurMes, Map<String, Object> params, int pageIndex) {
        final List<CommonDeviceEntity> commonDeviceEntities = DeviceManager.getInstance().blurSearch(moduleName, blurMes, pageIndex);
        Flowable.timer(200, TimeUnit.MILLISECONDS)
                .compose(RxSchedulers.io_main())
                .subscribe(aLong -> {
                    if (pageIndex != 1) {
                        CommonSearchDeviceListEntity commonSearchDeviceListEntity = new CommonSearchDeviceListEntity();
                        commonSearchDeviceListEntity.result = commonDeviceEntities;
                        if(getView()!=null)
                            getView().getSearchDeviceSuccess(commonSearchDeviceListEntity);
                    } else if (commonDeviceEntities == null || commonDeviceEntities.size() == 0) {
                        if(getView()!=null)
                            getView().getSearchDeviceFailed("未获取到搜索结果");
                    } else {
                        CommonSearchDeviceListEntity commonSearchDeviceListEntity = new CommonSearchDeviceListEntity();
                        commonSearchDeviceListEntity.result = commonDeviceEntities;
                        if(getView()!=null)
                            getView().getSearchDeviceSuccess(commonSearchDeviceListEntity);
                    }
                });

    }
}
