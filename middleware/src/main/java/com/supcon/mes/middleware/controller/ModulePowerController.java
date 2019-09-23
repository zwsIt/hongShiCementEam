package com.supcon.mes.middleware.controller;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.model.api.ModulePowerAPI;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.contract.ModulePowerContract;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.presenter.ModulePowerPresenter;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/7/19
 * ------------- Description -------------
 */
@Presenter(value = {ModulePowerPresenter.class})
public class ModulePowerController extends BasePresenterController implements ModulePowerContract.View, ModulePowerAPI {

    private OnSuccessListener<BapResultEntity> mOnSuccessListener;

    @Override
    public void getStartActivePowerCode(long deploymentId) {
        presenterRouter.create(ModulePowerAPI.class).getStartActivePowerCode(deploymentId);
    }

    @Override
    public void getStartActivePowerCodeSuccess(BapResultEntity entity) {
        if (mOnSuccessListener != null) {
            mOnSuccessListener.onSuccess(entity);
        }
    }

    @Override
    public void getStartActivePowerCodeFailed(String errorMsg) {
        LogUtil.e("ModulePowerController:" + errorMsg);
    }

    public void checkModulePermission(long deploymentId, OnSuccessListener<BapResultEntity> onSuccessListener) {
        this.mOnSuccessListener = onSuccessListener;
        presenterRouter.create(ModulePowerAPI.class).getStartActivePowerCode(deploymentId);
    }
}
