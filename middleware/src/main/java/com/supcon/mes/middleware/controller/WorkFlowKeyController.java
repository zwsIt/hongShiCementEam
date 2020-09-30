package com.supcon.mes.middleware.controller;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.mes.middleware.model.api.PcQueryAPI;
import com.supcon.mes.middleware.model.api.WorkFlowKeyAPI;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.middleware.model.bean.LongResultEntity;
import com.supcon.mes.middleware.model.contract.PcQueryContract;
import com.supcon.mes.middleware.model.contract.WorkFlowKeyContract;
import com.supcon.mes.middleware.model.listener.OnAPIResultListener;
import com.supcon.mes.middleware.presenter.PcQueryPresenter;
import com.supcon.mes.middleware.presenter.WorkFlowKeyPresenter;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/6/24
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
@Presenter(value = {WorkFlowKeyPresenter.class})
public class WorkFlowKeyController extends BasePresenterController implements WorkFlowKeyContract.View {

    private OnAPIResultListener<Object> workFlowKeyOnlyListener;
    private OnAPIResultListener<Object> workFlowKeyPermissionListener;
    private OnAPIResultListener<Object> listener;

    /**
     * 获取模块实体的工作流key
     * @param entityCode 实体编码
     * @param type
     * @param onAPIResultListener
     */
    public void queryWorkFlowKeyOnly(String entityCode, String type,OnAPIResultListener<Object> onAPIResultListener){
        workFlowKeyOnlyListener = onAPIResultListener;
        presenterRouter.create(WorkFlowKeyAPI.class).queryWorkFlowKeyOnly(entityCode, type);
    }

    /**
     * 获取模块实体的工作流key，同时根据key 获取是否有单据制定权限
     * @param entityCode 实体编码
     * @param type
     * @param onAPIResultListener
     */
    public void queryWorkFlowKeyAndPermission(String entityCode, String type,OnAPIResultListener<Object> onAPIResultListener){
        workFlowKeyPermissionListener = onAPIResultListener;
        presenterRouter.create(WorkFlowKeyAPI.class).queryWorkFlowKeyAndPermission(entityCode, type);
    }

    /**
     * 获取模块实体的工作流key，同时根据key 获取单据__pc__
     * @param operateCode 操作编码
     * @param entityCode 实体编码
     * @param type
     * @param onAPIResultListener
     */
    public void queryWorkFlowKeyToPc(String operateCode, String entityCode, String type,OnAPIResultListener<Object> onAPIResultListener){
        listener = onAPIResultListener;
        presenterRouter.create(WorkFlowKeyAPI.class).queryWorkFlowKeyToPc(operateCode, entityCode, type);
    }


    @Override
    public void queryWorkFlowKeyOnlySuccess(CommonEntity entity) {
        if (workFlowKeyOnlyListener !=  null){
            workFlowKeyOnlyListener.onSuccess(entity.result == null ? "" : entity.result.toString());
        }
    }

    @Override
    public void queryWorkFlowKeyOnlyFailed(String errorMsg) {
        if (workFlowKeyOnlyListener !=  null){
            workFlowKeyOnlyListener.onFail(errorMsg);
        }
    }

    @Override
    public void queryWorkFlowKeyAndPermissionSuccess(LongResultEntity entity) {
        if (workFlowKeyPermissionListener !=  null){
            workFlowKeyPermissionListener.onSuccess(entity.result);
        }
    }

    @Override
    public void queryWorkFlowKeyAndPermissionFailed(String errorMsg) {
        if (workFlowKeyPermissionListener !=  null){
            workFlowKeyPermissionListener.onFail(errorMsg);
        }
    }

    @Override
    public void queryWorkFlowKeyToPcSuccess(CommonEntity entity) {
        if (listener !=  null){
            listener.onSuccess(entity.result);
        }
    }

    @Override
    public void queryWorkFlowKeyToPcFailed(String errorMsg) {
        if (listener !=  null){
            listener.onFail(errorMsg);
        }
    }
}
