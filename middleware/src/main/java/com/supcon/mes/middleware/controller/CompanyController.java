package com.supcon.mes.middleware.controller;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.api.AreaQueryAPI;
import com.supcon.mes.middleware.model.api.CompanyQueryAPI;
import com.supcon.mes.middleware.model.bean.Area;
import com.supcon.mes.middleware.model.bean.AreaDao;
import com.supcon.mes.middleware.model.bean.AreaListEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.Company;
import com.supcon.mes.middleware.model.bean.CompanyDao;
import com.supcon.mes.middleware.model.contract.AreaQueryContract;
import com.supcon.mes.middleware.model.contract.CompanyQueryContract;
import com.supcon.mes.middleware.presenter.AreaPresenter;
import com.supcon.mes.middleware.presenter.CompanyQueryPresenter;
import com.supcon.mes.middleware.util.PinYinUtils;

import org.greenrobot.greendao.query.LazyList;

/**
 * Created by zws on 2020/05/25
 * Email:zhangwenshuai1@supcom.com
 */
@Presenter(CompanyQueryPresenter.class)
public class CompanyController extends BasePresenterController implements CompanyQueryContract.View, CompanyQueryAPI {

    @Override
    public void onInit() {
        super.onInit();
        listCompany();
    }

    @Override
    public void listCompanySuccess(CommonListEntity companyCommonListEntity) {
        CompanyDao companyDao = EamApplication.dao().getCompanyDao();
        LazyList<Company> lazyList =  companyDao.queryBuilder().build().listLazy();
        if (lazyList.size() > companyCommonListEntity.result.size()){
            companyDao.deleteAll();
        }
        companyDao.insertOrReplaceInTx(companyCommonListEntity.result);
    }

    @Override
    public void listCompanyFailed(String errorMsg) {
        LogUtil.e("CompanyController:"+errorMsg);
    }

    @Override
    public void listCompany() {
        presenterRouter.create(CompanyQueryAPI.class).listCompany();
    }


}
