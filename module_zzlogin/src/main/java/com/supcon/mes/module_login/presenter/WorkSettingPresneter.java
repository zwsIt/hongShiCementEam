package com.supcon.mes.module_login.presenter;

import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_login.model.bean.WorkInfo;
import com.supcon.mes.module_login.model.contract.WorkSettingContract;

import java.util.List;

/**
 * Created by wangshizhan on 2017/11/16.
 * Email:wangshizhan@supcon.com
 */

public class WorkSettingPresneter extends WorkSettingContract.Presenter {
    @Override
    public void setWork(List<WorkInfo> workInfoList) {

        SharedPreferencesUtils.setParam(MBapApp.getAppContext(), Constant.SPKey.WORKS, GsonUtil.gsonString(workInfoList));

        getView().setWorkSuccess();
    }
}
