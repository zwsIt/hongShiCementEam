package com.supcon.mes.middleware.util;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.beans.LinkEntity;
import com.supcon.mes.middleware.model.api.WorkFlowAPI;
import com.supcon.mes.middleware.model.bean.WorkFlowListEntity;
import com.supcon.mes.middleware.model.contract.WorkFlowContract;
import com.supcon.mes.middleware.presenter.WorkFlowPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangshizhan on 2018/7/24
 * Email:wangshizhan@supcom.com
 */

@Presenter(value = WorkFlowPresenter.class)
public class WorkFlowController extends BasePresenterController implements WorkFlowContract.View{


    private List<LinkEntity> linkEntities;

    public void getWorkFlow(long pendingId){
        presenterRouter.create(WorkFlowAPI.class).findWorkFlow(pendingId);
    }

    @Override
    public void findWorkFlowSuccess(WorkFlowListEntity entity) {
        linkEntities = entity.linkEntities;
    }

    @Override
    public void findWorkFlowFailed(String errorMsg) {
        LogUtil.e(ErrorMsgHelper.msgParse(errorMsg));
    }

    public List<LinkEntity> getLinkEntities() {
        return linkEntities == null?new ArrayList<>():linkEntities;
    }
}
