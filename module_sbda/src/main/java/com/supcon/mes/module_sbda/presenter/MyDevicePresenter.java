package com.supcon.mes.module_sbda.presenter;

import com.supcon.common.com_http.util.RxSchedulers;
import com.supcon.mes.middleware.constant.EamPermission;
import com.supcon.mes.middleware.model.bean.CommonDeviceEntity;
import com.supcon.mes.middleware.util.DeviceManager;
import com.supcon.mes.module_sbda.model.bean.MyDeviceListEntity;
import com.supcon.mes.module_sbda.model.contract.MyDeviceContract;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * Created by wangshizhan on 2017/11/29.
 * Email:wangshizhan@supcon.com
 */

public class MyDevicePresenter extends MyDeviceContract.Presenter {


    @Override
    public void getMyDevice(String module,int page,int pageNum) {

        final MyDeviceListEntity myDeviceListEntity = new MyDeviceListEntity();

        final List<CommonDeviceEntity> commonDeviceEntities = DeviceManager.getInstance().commonSearch(module, page, pageNum);
        myDeviceListEntity.devices = commonDeviceEntities;

        Flowable.timer(100, TimeUnit.MILLISECONDS)
                .compose(RxSchedulers.io_main())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if(page != 1)
                        {
                            if(getView()!=null)
                            getView().getMyDeviceSuccess(myDeviceListEntity);
                            return;
                        }
                        else if(commonDeviceEntities == null || commonDeviceEntities.size() == 0) {
                            if(getView()!=null)
                            getView().getMyDeviceFailed("未找到对应模块设备！");
                            return;
                        }
                        if(getView()!=null)
                        getView().getMyDeviceSuccess(myDeviceListEntity);
                    }
                });


//        final List<CommonDeviceEntity> commonDeviceEntities = new ArrayList<>();
//        myDeviceListEntity.devices = commonDeviceEntities;
//
//        Flowable.fromIterable(DeviceManager.getInstance().getMyDeviceEntitiesByModule(module, page, pageNum))
//                .compose(RxSchedulers.io_main())
//                .filter(commonDeviceEntity -> EamPermission.hasPermission(module, commonDeviceEntity.eamPermission))
//                .subscribe(commonDeviceEntity -> commonDeviceEntities.add(commonDeviceEntity), throwable -> {
//                }, () -> {
//                    if(page != 1)
//                    {
//                        getView().getMyDeviceSuccess(myDeviceListEntity);
//                        return;
//                    }
//                    else if(commonDeviceEntities == null || commonDeviceEntities.size() == 0) {
//                        getView().getMyDeviceFailed("未找到对应模块设备！");
//                        return;
//                    }
//                    getView().getMyDeviceSuccess(myDeviceListEntity);
//                });


    }
}
