package com.supcon.mes.middleware.controller;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.api.ContractQueryAPI;
import com.supcon.mes.middleware.model.bean.ContractListEntity;
import com.supcon.mes.middleware.model.bean.ContractStaffEntity;
import com.supcon.mes.middleware.model.contract.ContractQueryContract;
import com.supcon.mes.middleware.presenter.ContractPresenter;
import com.supcon.mes.middleware.util.PinYinUtils;

/**
 * Created by wangshizhan on 2018/8/20
 * Email:wangshizhan@supcom.com
 */
@Presenter(ContractPresenter.class)
public class ContractController extends BasePresenterController implements ContractQueryContract.View, ContractQueryAPI {

    private String staffName;

    @Override
    public void listStaff(String staffName, int pageNo) {
        this.staffName = staffName;
        presenterRouter.create(ContractQueryAPI.class).listStaff(staffName, pageNo);
    }

    @Override
    public void listStaffSuccess(ContractListEntity entity) {
        if(entity.result!=null && entity.result.size()!=0){
            for (ContractStaffEntity contractStaffEntity :entity.result){
                contractStaffEntity.pinyin = PinYinUtils.getPinyin(contractStaffEntity.name);
                EamApplication.dao().getContractStaffEntityDao().insertOrReplace(contractStaffEntity);
            }
        }

        if(entity.hasNext){
            listStaff(staffName!=null?staffName:"", entity.pageNo+1);
        }
    }

    @Override
    public void listStaffFailed(String errorMsg) {
        LogUtil.e("ContractController:"+errorMsg);
    }
}
