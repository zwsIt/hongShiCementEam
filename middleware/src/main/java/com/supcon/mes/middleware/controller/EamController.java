package com.supcon.mes.middleware.controller;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.LogUtils;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.api.AreaQueryAPI;
import com.supcon.mes.middleware.model.api.EamAPI;
import com.supcon.mes.middleware.model.bean.Area;
import com.supcon.mes.middleware.model.bean.AreaDao;
import com.supcon.mes.middleware.model.bean.AreaListEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.bean.EamEntityDao;
import com.supcon.mes.middleware.model.contract.AreaQueryContract;
import com.supcon.mes.middleware.model.contract.EamContract;
import com.supcon.mes.middleware.presenter.AreaPresenter;
import com.supcon.mes.middleware.presenter.EamPresenter;
import com.supcon.mes.middleware.util.PageParamUtil;
import com.supcon.mes.middleware.util.PinYinUtils;

import org.greenrobot.greendao.query.LazyList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangshizhan on 2018/8/20
 * Email:wangshizhan@supcom.com
 */
@Presenter(EamPresenter.class)
public class EamController extends BasePresenterController implements EamContract.View, EamAPI {

    @Override
    public void onInit() {
        super.onInit();
        Map<String, Object> params = new HashMap<>();
        getEam(params,false,1,99999);
    }

    @Override
    public void getEamSuccess(CommonListEntity entity) {
        EamEntityDao eamEntityDao = EamApplication.dao().getEamEntityDao();
        LazyList<EamEntity> eamEntityLazyList = eamEntityDao.queryBuilder().build().listLazy();
        if (eamEntityLazyList.size() > entity.result.size()){
            eamEntityDao.deleteAll();
        }
        EamEntity eamEntity;
        for (Object object : entity.result){
            eamEntity = (EamEntity) object;
            eamEntity.setAreaId(eamEntity.installPlace == null ? 0L : eamEntity.installPlace.id);
            eamEntity.setEamTypeId(eamEntity.eamType == null ? 0L : eamEntity.eamType.id);
        }
        eamEntityDao.insertOrReplaceInTx(entity.result);
    }

    @Override
    public void getEamFailed(String errorMsg) {
        LogUtil.e("EamController:" + errorMsg);
    }

    @Override
    public void getEam(Map<String, Object> params, boolean nfcCard, int page, int pageSize) {
        presenterRouter.create(EamAPI.class).getEam(params, nfcCard, page, pageSize);
    }
}
