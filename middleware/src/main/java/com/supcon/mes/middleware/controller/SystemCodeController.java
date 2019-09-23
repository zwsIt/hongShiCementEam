package com.supcon.mes.middleware.controller;

import android.annotation.SuppressLint;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.api.SystemCodeAPI;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeListEntity;
import com.supcon.mes.middleware.model.contract.SystemCodeContract;
import com.supcon.mes.middleware.presenter.SystemCodePresenter;
import com.supcon.mes.middleware.util.SystemCodeManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * Created by wangshizhan on 2018/8/1
 * Email:wangshizhan@supcom.com
 */
@Presenter(SystemCodePresenter.class)
public class SystemCodeController extends BasePresenterController implements SystemCodeContract.View {

    List<String> entityCodes;

    public SystemCodeController() {
        entityCodes = new ArrayList<>();
        entityCodes.add(Constant.SystemCode.CARD_TYPE);
        entityCodes.add(Constant.SystemCode.REAL_VALUE);
        entityCodes.add(Constant.SystemCode.CART_REASON);
        entityCodes.add(Constant.SystemCode.WILINK_STATE);
        entityCodes.add(Constant.SystemCode.PASS_REASON);
        entityCodes.add(Constant.SystemCode.MOBILE_EAM001);
        entityCodes.add(Constant.SystemCode.MOBILE_EAM054);
        entityCodes.add(Constant.SystemCode.MOBILE_EAM055);

        entityCodes.add(Constant.SystemCode.XJ_TYPE);//巡检类型：巡检？点检？

        entityCodes.add(Constant.SystemCode.QX_TYPE);
        entityCodes.add(Constant.SystemCode.YH_STATE);
        entityCodes.add(Constant.SystemCode.YH_WX_TYPE);
        entityCodes.add(Constant.SystemCode.YH_SOURCE);
        entityCodes.add(Constant.SystemCode.YH_PRIORITY);
        entityCodes.add(Constant.SystemCode.YH_DISPOSAL);
        entityCodes.add(Constant.SystemCode.OIL_TYPE);
        entityCodes.add(Constant.SystemCode.CHECK_RESULT);
        entityCodes.add(Constant.SystemCode.WXGD_SOURCE);
        //停机类型，停机原因对应的系统编码加载
        entityCodes.add(Constant.SystemCode.TJ_TYPE);
        entityCodes.add(Constant.SystemCode.TJ_REASON);
    }

    @Override
    public void onInit() {
        super.onInit();
        queryAll(entityCodes);
    }

    @Override
    public void initData() {
        super.initData();


    }


    @Override
    public void getSystemCodeListSuccess(List entity) {
//        systemCodeEntities = entity.result;
        LogUtil.d("insert SystemCode:" + entity.size());
        SystemCodeManager.getInstance().setSystemCodeList(entity);
    }

    @Override
    public void getSystemCodeListFailed(String errorMsg) {
        LogUtil.e("SystemCodeController:" + errorMsg);
    }


    @SuppressLint("CheckResult")
    public void queryAll(List<String> codes) {
        presenterRouter.create(SystemCodeAPI.class).getSystemCodeList(codes);
    }

    public void querySystemCode(String entityCode) {


    }

    public List<SystemCodeEntity> getSystemCodeEntities(String entityCode) {

        return SystemCodeManager.getInstance().getSystemCodeListByCode(entityCode);
    }

    public String getSystemCodeEntityId(String entityCode, String entityName) {
        return SystemCodeManager.getInstance().getSystemCodeEntityId(entityCode, entityName);
    }

    public List<SystemCodeEntity> getSystemCodeEntities(int pageIndex, String entityCode, String mes) {
        return SystemCodeManager.getInstance().getSystemCodeListByCode(pageIndex, entityCode, mes);
    }


    public SystemCodeEntity getSystemCodeEntity(String id) {

        return SystemCodeManager.getInstance().getSystemCodeEntity(id);

    }

    public SystemCodeEntity getSystemCodeEntity(String entityCode, String id) {

        return SystemCodeManager.getInstance().getSystemCodeEntity(entityCode, id);

    }
}
