package com.supcon.mes.module_sbda.presenter;

import com.supcon.mes.middleware.model.bean.CommonDeviceEntity;
import com.supcon.mes.middleware.util.DeviceManager;
import com.supcon.mes.module_sbda.model.bean.SBDAViewEntity;
import com.supcon.mes.module_sbda.model.contract.SBDAViewContract;


/**
 * Environment: hongruijun
 * Created by Xushiyun on 2018/3/30.
 */

public class SBDAViewPresenter extends SBDAViewContract.Presenter {
    @Override
    public void getSBDAItem(Long eamId) {
        final CommonDeviceEntity commonDeviceEntity = DeviceManager.getInstance().getDeviceEntityByEamId(eamId);

        if (commonDeviceEntity == null) {
            getView().getSBDAItemFailed("未搜索到该设备信息");
            return;
        }
        SBDAViewEntity sbdaViewEntity = new SBDAViewEntity();
        sbdaViewEntity.entity = commonDeviceEntity;
        sbdaViewEntity.success = true;
        getView().getSBDAItemSuccess(sbdaViewEntity);

    }
}
