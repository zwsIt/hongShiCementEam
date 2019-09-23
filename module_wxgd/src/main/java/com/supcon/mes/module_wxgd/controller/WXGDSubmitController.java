package com.supcon.mes.module_wxgd.controller;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.module_wxgd.model.api.WXGDSubmitAPI;
import com.supcon.mes.module_wxgd.model.contract.WXGDSubmitContract;
import com.supcon.mes.module_wxgd.presenter.WXGDSubmitPresenter;

import java.util.Map;

/**
 * @author yangfei.cao
 * @ClassName eam
 * @date 2018/9/6
 * ------------- Description -------------
 */
@Presenter(value = {WXGDSubmitPresenter.class})
public class WXGDSubmitController extends BasePresenterController implements WXGDSubmitContract.View, WXGDSubmitAPI {

    private OnSubmitResultListener submitResult;

    public WXGDSubmitController(OnSubmitResultListener submitResult) {
        this.submitResult = submitResult;
    }

    //接单提交
    @Override
    public void doReceiveSubmit(Map<String, Object> map) {
        presenterRouter.create(WXGDSubmitAPI.class).doReceiveSubmit(map);
    }

    //执行提交
    @Override
    public void doExecuteSubmit(Map<String, Object> map) {
        presenterRouter.create(WXGDSubmitAPI.class).doExecuteSubmit(map);
    }

    @Override
    public void doAcceptChkSubmit(Map<String, Object> map, Map<String, Object> attachmentMap) {
        presenterRouter.create(WXGDSubmitAPI.class).doAcceptChkSubmit(map, attachmentMap);
    }

    @Override
    public void doDispatcherSubmit(Map<String, Object> map) {
        presenterRouter.create(WXGDSubmitAPI.class).doDispatcherSubmit(map);
    }

    @Override
    public void doDispatcherWarnSubmit(Map<String, Object> map) {
        presenterRouter.create(WXGDSubmitAPI.class).doDispatcherWarnSubmit(map);
    }


    @Override
    public void doReceiveSubmitSuccess(BapResultEntity entity) {
        submitResult.submitSuccess(entity);
    }

    @Override
    public void doReceiveSubmitFailed(String errorMsg) {
        submitResult.submitFailed(errorMsg);
    }

    @Override
    public void doExecuteSubmitSuccess(BapResultEntity entity) {
        submitResult.submitSuccess(entity);
    }

    @Override
    public void doExecuteSubmitFailed(String errorMsg) {
        submitResult.submitFailed(errorMsg);
    }

    @Override
    public void doDispatcherSubmitSuccess(BapResultEntity entity) {
        submitResult.submitSuccess(entity);
    }

    @Override
    public void doDispatcherSubmitFailed(String errorMsg) {
        submitResult.submitFailed(errorMsg);
    }

    @Override
    public void doDispatcherWarnSubmitSuccess(BapResultEntity entity) {
        submitResult.submitSuccess(entity);
    }

    @Override
    public void doDispatcherWarnSubmitFailed(String errorMsg) {
        submitResult.submitFailed(errorMsg);
    }

    @Override
    public void doAcceptChkSubmitSuccess(BapResultEntity entity) {
        submitResult.submitSuccess(entity);
    }

    @Override
    public void doAcceptChkSubmitFailed(String errorMsg) {
        submitResult.submitFailed(errorMsg);
    }

    //回掉提交结果
    public interface OnSubmitResultListener {

        void submitSuccess(BapResultEntity entity);

        void submitFailed(String errorMsg);
    }

}
