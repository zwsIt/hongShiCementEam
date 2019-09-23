package com.supcon.mes.middleware.controller;

import android.view.View;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.model.api.ModulePermissonCheckAPI;
import com.supcon.mes.middleware.model.bean.LongResultEntity;
import com.supcon.mes.middleware.model.contract.ModulePermissonCheckContract;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.presenter.ModulePermissonCheckPresenter;

/**
 * Created by wangshizhan on 2018/9/21
 * Email:wangshizhan@supcom.com
 */
@Presenter(ModulePermissonCheckPresenter.class)
public class ModulePermissonCheckController extends BasePresenterController implements ModulePermissonCheckContract.View, ModulePermissonCheckAPI{

    private View addBtn;
    private OnSuccessListener<Long> mOnSuccessListener;

    @Override
    public void checkModulePermission(String userName, String proccessKey) {
        presenterRouter.create(ModulePermissonCheckAPI.class).checkModulePermission(userName, proccessKey);
    }

    @Override
    public void checkModulePermissionSuccess(LongResultEntity entity) {
//        addBtn.setVisibility(View.VISIBLE);
        if(addBtn!=null){
            addBtn.setVisibility(View.VISIBLE);
        }

        if(mOnSuccessListener!=null){
            mOnSuccessListener.onSuccess(entity.result);
        }
    }

    @Override
    public void checkModulePermissionFailed(String errorMsg) {
        LogUtil.e("ModulePermissonCheckController:"+errorMsg);
        if(addBtn!=null){
            addBtn.setVisibility(View.INVISIBLE);
        }
    }

    public void checkModulePermission(String userName, String proccessKey, OnSuccessListener<Long> onSuccessListener, View addBtn){
        this.mOnSuccessListener = onSuccessListener;
        this.addBtn = addBtn;
        presenterRouter.create(ModulePermissonCheckAPI.class).checkModulePermission(userName, proccessKey);
    }

}
