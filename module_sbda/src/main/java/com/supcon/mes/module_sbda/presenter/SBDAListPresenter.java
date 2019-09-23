package com.supcon.mes.module_sbda.presenter;

import android.annotation.SuppressLint;

import com.supcon.common.com_http.util.RxSchedulers;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.constant.Module;
import com.supcon.mes.middleware.model.bean.CommonDeviceEntity;
import com.supcon.mes.middleware.util.DeviceManager;
import com.supcon.mes.module_sbda.model.bean.SBDAListEntity;
import com.supcon.mes.module_sbda.model.contract.SBDAListContract;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;


/**
 * Environment: hongruijun
 * Created by Xushiyun on 2018/3/30.
 */

public class SBDAListPresenter extends SBDAListContract.Presenter {
    @SuppressLint("CheckResult")
    @Override
    public void getSearchSBDA(String blurMes, Map<String, Object> params, int page, int pageNum) {
        LogUtil.d("getSearchSBDA page:"+page);
        final SBDAListEntity sbdaListEntity = new SBDAListEntity();
        final List<CommonDeviceEntity> commonDeviceEntities = DeviceManager.getInstance().blurSearch(Module.DeviceCheck.name(), blurMes, params, page, pageNum);
        Flowable.timer(150, TimeUnit.MILLISECONDS)
                .compose(RxSchedulers.io_main())
                .subscribe(aLong -> {
                    if (page != 1) {
                        sbdaListEntity.success = true;
                        sbdaListEntity.result = commonDeviceEntities;
                        if(getView()!=null)
                            getView().getSearchSBDASuccess(sbdaListEntity);
                    }
                    else if (null == commonDeviceEntities || 0 == commonDeviceEntities.size()) {
                        sbdaListEntity.success = false;
                        sbdaListEntity.errMsg = "未搜索到任何设备信息";
                        if(getView()!=null)
                            getView().getSearchSBDAFailed(sbdaListEntity.errMsg);
                    } else {
                        sbdaListEntity.success = true;
                        sbdaListEntity.result = commonDeviceEntities;
                        if(getView()!=null)
                            getView().getSearchSBDASuccess(sbdaListEntity);
                    }
                });
    }
}
