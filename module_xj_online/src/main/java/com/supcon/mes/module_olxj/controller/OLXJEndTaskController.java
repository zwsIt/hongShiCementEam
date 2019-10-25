package com.supcon.mes.module_olxj.controller;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.mes.middleware.model.listener.OnAPIResultListener;
import com.supcon.mes.module_olxj.model.api.OLXJTaskStatusAPI;
import com.supcon.mes.module_olxj.model.contract.OLXJTaskStatusContract;
import com.supcon.mes.module_olxj.presenter.OLXJTaskStatusPresenter;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/10/21
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
@Presenter(value = {OLXJTaskStatusPresenter.class})
public class OLXJEndTaskController extends BasePresenterController implements OLXJTaskStatusContract.View {

    private OnAPIResultListener mOnAPIResultListener;

    public OLXJEndTaskController(OnAPIResultListener onAPIResultListener){
        mOnAPIResultListener = onAPIResultListener;
    }

    public OLXJEndTaskController(){}

    /**
     * 结束巡检任务
     * @param taskIDs
     * @param endReason
     * @param isFinish
     */
    public void endTask(String taskIDs, String endReason, boolean isFinish, OnAPIResultListener onAPIResultListener){
        mOnAPIResultListener = onAPIResultListener;
        presenterRouter.create(OLXJTaskStatusAPI.class).endTasks(taskIDs, endReason, isFinish);
    }

    @Override
    public void updateStatusSuccess() {

    }

    @Override
    public void updateStatusFailed(String errorMsg) {

    }

    @Override
    public void cancelTasksSuccess() {

    }

    @Override
    public void cancelTasksFailed(String errorMsg) {

    }

    @Override
    public void endTasksSuccess() {
        mOnAPIResultListener.onSuccess("");
    }

    @Override
    public void endTasksFailed(String errorMsg) {
        mOnAPIResultListener.onFail(errorMsg);
    }
}
