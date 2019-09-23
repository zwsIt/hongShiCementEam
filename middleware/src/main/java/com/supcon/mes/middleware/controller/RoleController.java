package com.supcon.mes.middleware.controller;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.model.api.RoleQueryAPI;
import com.supcon.mes.middleware.model.bean.RoleEntity;
import com.supcon.mes.middleware.model.bean.RoleListEntity;
import com.supcon.mes.middleware.model.contract.RoleQueryContract;
import com.supcon.mes.middleware.presenter.RoleQueryPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangshizhan on 2018/7/30
 * Email:wangshizhan@supcom.com
 */
@Presenter(RoleQueryPresenter.class)
public class RoleController extends BasePresenterController implements RoleQueryContract.View{

    private List<RoleEntity> roleEntityList;

    public void queryRoleList(String userName){

        presenterRouter.create(RoleQueryAPI.class).queryRoleListEntity(userName);

    }

    @Override
    public void queryRoleListEntitySuccess(RoleListEntity entity) {
        roleEntityList = entity.result;
    }

    @Override
    public void queryRoleListEntityFailed(String errorMsg) {
        LogUtil.e("RoleController:"+errorMsg);
    }

    public List<RoleEntity> getRoleEntityList() {
        return roleEntityList == null?new ArrayList<>():roleEntityList;
    }

    public  RoleEntity getRoleEntity(int index){

        return roleEntityList!=null&&roleEntityList.size()!=0&&roleEntityList.size()>=index?roleEntityList.get(index):new RoleEntity();

    }
}
