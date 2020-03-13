package com.supcon.mes.middleware.presenter;

import com.supcon.mes.middleware.model.bean.AreaListEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.Company;
import com.supcon.mes.middleware.model.contract.AreaQueryContract;
import com.supcon.mes.middleware.model.contract.CompanyQueryContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by zhangwenshuai on 2020/3/11
 *  多组织登陆获取公司list
 */
public class CompanyQueryPresenter extends CompanyQueryContract.Presenter {

    @Override
    public void listCompany() {
        mCompositeSubscription.add(
                MiddlewareHttpClient.getCompanyList()
                .onErrorReturn(throwable -> {
                    CommonListEntity<Company> commonListEntity = new CommonListEntity<>();
                    commonListEntity.errMsg = throwable.toString();
                    return commonListEntity;
                }).subscribe(companyCommonListEntity -> {
                    if (companyCommonListEntity.success){
                        getView().listCompanySuccess(companyCommonListEntity);
                    }else {
                        getView().listCompanyFailed(companyCommonListEntity.errMsg);
                    }
                })
        );
    }

}
