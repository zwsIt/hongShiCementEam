package com.supcon.mes.middleware.controller;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.model.api.CurrentDeploymentAPI;
import com.supcon.mes.middleware.model.api.ModulePowerAPI;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CurrentDeploymentEntity;
import com.supcon.mes.middleware.model.contract.CurrentDeploymentContract;
import com.supcon.mes.middleware.model.contract.ModulePowerContract;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.presenter.CurrentDeploymentPresenter;
import com.supcon.mes.middleware.presenter.ModulePowerPresenter;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/7/19
 * ------------- Description -------------
 */
@Presenter(value = {CurrentDeploymentPresenter.class})
public class CurrentDeploymentController extends BasePresenterController implements CurrentDeploymentContract.View {

    private OnSuccessListener<CurrentDeploymentEntity> mOnSuccessListener;

    public void getCurrentDeploymentId(String processKey, OnSuccessListener<CurrentDeploymentEntity> onSuccessListener) {
        this.mOnSuccessListener = onSuccessListener;
        presenterRouter.create(CurrentDeploymentAPI.class).getCurrentDeployment(processKey);
    }

    @Override
    public void getCurrentDeploymentSuccess(CurrentDeploymentEntity entity) {
        if (mOnSuccessListener != null) {
            mOnSuccessListener.onSuccess(entity);
        }
    }

    @Override
    public void getCurrentDeploymentFailed(String errorMsg) {
        LogUtil.e("CurrentDeploymentController:" + errorMsg);
    }
}
