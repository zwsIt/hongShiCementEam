package com.supcon.mes.middleware.controller;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.mes.middleware.model.api.YHCloseAPI;
import com.supcon.mes.middleware.model.bean.ResultEntity;
import com.supcon.mes.middleware.model.contract.YHCloseContract;
import com.supcon.mes.middleware.model.listener.OnFailListener;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.presenter.YHClosePresenter;

@Presenter(value = {YHClosePresenter.class})
public class YHCloseController extends BasePresenterController implements YHCloseAPI, YHCloseContract.View {

    private OnSuccessListener<ResultEntity> mOnSuccessListener;
    private OnFailListener onFailListener;

    @Override
    public void closeWorkAndSaveReason(long id, String reason) {
        presenterRouter.create(YHCloseAPI.class).closeWorkAndSaveReason(id, reason);
    }

    @Override
    public void closeWorkAndSaveReasonSuccess(ResultEntity entity) {
        if (mOnSuccessListener != null) {
            mOnSuccessListener.onSuccess(entity);
        }
    }

    @Override
    public void closeWorkAndSaveReasonFailed(String errorMsg) {
        if (onFailListener != null) {
            onFailListener.onFail(errorMsg);
        }
    }

    public void closeWorkAndSaveReason(long id, String reason, OnSuccessListener<ResultEntity> onSuccessListener, OnFailListener onFailListener) {
        this.mOnSuccessListener = onSuccessListener;
        this.onFailListener = onFailListener;
        presenterRouter.create(YHCloseAPI.class).closeWorkAndSaveReason(id, reason);
    }
}
