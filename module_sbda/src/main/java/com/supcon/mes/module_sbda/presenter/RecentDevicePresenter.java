package com.supcon.mes.module_sbda.presenter;

import android.annotation.SuppressLint;

import com.supcon.common.com_http.util.RxSchedulers;
import com.supcon.mes.middleware.model.bean.CommonDeviceEntity;
import com.supcon.mes.middleware.util.DeviceManager;
import com.supcon.mes.module_sbda.model.bean.RecentDeviceListEntity;
import com.supcon.mes.module_sbda.model.contract.RecentDeviceContract;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wangshizhan on 2017/11/29.
 * Email:wangshizhan@supcon.com
 */

public class RecentDevicePresenter extends RecentDeviceContract.Presenter {

    @SuppressLint("CheckResult")
    @Override
    public void getRecentDevice(String moduleName, int pageIndex) {
        List<CommonDeviceEntity> recentDevices = new ArrayList<>();
       final  List<CommonDeviceEntity> commonDeviceEntities =
                DeviceManager.getInstance()
                        .commonSearch(moduleName, pageIndex);

        Flowable.fromIterable(commonDeviceEntities)
                .subscribeOn(Schedulers.computation())
                .filter(commonDeviceEntity -> commonDeviceEntity.updateTime!=0L)
                .sorted((o1, o2) -> {
                    if(o1.updateTime < o2.updateTime) return 1;
                    else if(o1.updateTime > o2.updateTime) return -1;
                    return 0;
                })
                .subscribe(
                        o -> {if(!recentDevices.contains(o)&&recentDevices.size()<10) recentDevices.add(o);},
                        throwable -> {},
                        ()->{
                            Flowable.fromIterable(commonDeviceEntities)
//                                    .filter(commonDeviceEntity -> EamPermission.hasPermission(moduleName, commonDeviceEntity.eamPermission))
                                    .filter(commonDeviceEntity -> commonDeviceEntity.frequency!=0L)
                                    .sorted((o1, o2) -> {
                                        if(o1.frequency < o2.frequency) return 1;
                                        else if(o1.frequency > o2.frequency) return -1;
                                        return 0;
                                    })
                                    .subscribe(o -> {
                                        if(!recentDevices.contains(o)&&recentDevices.size()<13)
                                            recentDevices.add(o);

                                    });
                            Flowable.timer(400, TimeUnit.MILLISECONDS)
                                    .compose(RxSchedulers.io_main())
                                    .subscribe(new Consumer<Long>() {
                                        @Override
                                        public void accept(Long aLong) throws Exception {

                                            RecentDeviceListEntity recentDeviceListEntity = new RecentDeviceListEntity();
                                            if(recentDevices.size()>10)
                                                recentDeviceListEntity.devices= recentDevices.subList(0, 10);
                                            else
                                                recentDeviceListEntity.devices= recentDevices;
                                            if(pageIndex!= 1)
                                            {
                                                if(null != getView())
                                                getView().getRecentDeviceSuccess(recentDeviceListEntity);
                                            }
                                            if(null == recentDevices||recentDevices.size() == 0) {
                                                if(null != getView())
                                                getView().getRecentDeviceFailed("没有历史设备!");
                                                return;
                                            }
                                            if(null != getView())
                                            getView().getRecentDeviceSuccess(recentDeviceListEntity);
                                        }
                                    });

                        });



//        LogUtil.e("历史设备信息", recentDevices.toString());


    }
}
