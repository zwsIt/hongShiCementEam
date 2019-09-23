package com.supcon.mes.middleware.controller;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.api.RepairGroupQueryAPI;
import com.supcon.mes.middleware.model.bean.RepairGroupEntityDao;
import com.supcon.mes.middleware.model.bean.RepairGroupListEntity;
import com.supcon.mes.middleware.model.contract.RepairGroupQueryContract;
import com.supcon.mes.middleware.presenter.RepairGroupPresenter;

/**
 * Created by wangshizhan on 2018/8/22
 * Email:wangshizhan@supcom.com
 */
@Presenter(RepairGroupPresenter.class)
public class RepairGroupController extends BasePresenterController implements RepairGroupQueryContract.View, RepairGroupQueryAPI {

    @Override
    public void onInit() {
        super.onInit();
        queryRepairGroup();
    }

    @Override
    public void queryRepairGroupSuccess(RepairGroupListEntity entity) {
        RepairGroupEntityDao repairGroupEntityDao = EamApplication.dao().getRepairGroupEntityDao();
        repairGroupEntityDao.deleteAll();
        repairGroupEntityDao.insertOrReplaceInTx(entity.result);
    }

    @Override
    public void queryRepairGroupFailed(String errorMsg) {
        LogUtil.e("RepairGroupController queryRepairGroupFailed:"+errorMsg);
    }


    @Override
    public void queryRepairGroup() {
        presenterRouter.create(RepairGroupQueryAPI.class).queryRepairGroup();
    }
}
