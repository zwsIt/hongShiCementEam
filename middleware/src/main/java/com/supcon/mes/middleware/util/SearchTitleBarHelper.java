package com.supcon.mes.middleware.util;

import com.supcon.common.com_http.util.RxSchedulers;
import com.supcon.mes.mbap.utils.ScanInputHelper;
import com.supcon.mes.middleware.model.bean.CommonDeviceEntity;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by wangshizhan on 2017/12/1.
 * Email:wangshizhan@supcon.com
 */

public class SearchTitleBarHelper extends ScanInputHelper {


    public static void addDevice(CommonDeviceEntity runStateDeviceEntity){
        Flowable.just(runStateDeviceEntity)
                .compose(RxSchedulers.io_main())
                .subscribe(entity -> {
                    ScanInputHelper.addWord(entity.eamName);
                    ScanInputHelper.addWord(entity.eamCode);
                });

    }


    public static void addDeviceList(List<CommonDeviceEntity> deviceEntities){
        Flowable.fromIterable(deviceEntities)
                .compose(RxSchedulers.io_main())
                .subscribe(deviceEntity -> {
                    ScanInputHelper.addWord(deviceEntity.eamName);
                    ScanInputHelper.addWord(deviceEntity.eamCode);
                });
    }

}
