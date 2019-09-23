package com.supcon.mes.middleware.controller;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.model.api.StaffDetailInfoQueryAPI;
import com.supcon.mes.middleware.model.bean.StaffDetailInfoEntity;
import com.supcon.mes.middleware.model.contract.StaffDetailInfoQueryContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;
import com.supcon.mes.middleware.presenter.StaffDetailInfoPresenter;

/**
 * Created by wangshizhan on 2018/8/1
 * Email:wangshizhan@supcom.com
 */
@Presenter(StaffDetailInfoPresenter.class)
public class StaffDetailInfoController extends BasePresenterController implements StaffDetailInfoQueryContract.View, StaffDetailInfoQueryAPI {

    private StaffDetailInfoEntity mStaffDetailInfoEntity;

    @Override
    public void queryStaffDetailInfoSuccess(StaffDetailInfoEntity entity) {
        mStaffDetailInfoEntity = entity;
    }

    @Override
    public void queryStaffDetailInfoFailed(String errorMsg) {
        LogUtil.e("StaffDetailInfoController:"+errorMsg);
    }

    @Override
    public void queryStaffDetailInfo(String staffCode, long companyId) {
        presenterRouter.create(StaffDetailInfoQueryAPI.class).queryStaffDetailInfo(staffCode, companyId);
    }

    public StaffDetailInfoEntity getStaffDetailInfoEntity() {
        return mStaffDetailInfoEntity == null?new StaffDetailInfoEntity():mStaffDetailInfoEntity;
    }
}
