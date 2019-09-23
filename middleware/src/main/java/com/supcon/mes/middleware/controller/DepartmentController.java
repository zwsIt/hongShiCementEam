package com.supcon.mes.middleware.controller;

import android.annotation.SuppressLint;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.api.DepartmentQueryAPI;
import com.supcon.mes.middleware.model.bean.DepartmentInfo;
import com.supcon.mes.middleware.model.bean.DepartmentInfoDao;
import com.supcon.mes.middleware.model.bean.DepartmentInfoListEntity;
import com.supcon.mes.middleware.model.contract.DepartmentQueryContract;
import com.supcon.mes.middleware.presenter.DepartmentPresenter;
import com.supcon.mes.middleware.util.PinYinUtils;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wangshizhan on 2018/8/20
 * Email:wangshizhan@supcom.com
 */
@Presenter(DepartmentPresenter.class)
public class DepartmentController extends BasePresenterController implements DepartmentQueryContract.View, DepartmentQueryAPI {

    @Override
    public void onInit() {
        super.onInit();
        listDepartment();
    }

    @Override
    public void listDepartment() {
        presenterRouter.create(DepartmentQueryAPI.class).listDepartment();
    }
    @SuppressLint("CheckResult")
    @Override
    public void listDepartmentSuccess(DepartmentInfoListEntity entity) {
        DepartmentInfoDao departmentInfoDao = EamApplication.dao().getDepartmentInfoDao();
        departmentInfoDao.deleteAll();
        departmentInfoDao.insertOrReplaceInTx(entity.result);
        String departmentName = EamApplication.getAccountInfo().departmentName;
        if(departmentName!=null)
            Flowable.fromIterable(entity.result)
                    .subscribeOn(Schedulers.newThread())
                    .filter(new Predicate<DepartmentInfo>() {
                        @Override
                        public boolean test(DepartmentInfo departmentInfo) throws Exception {
                            return EamApplication.getAccountInfo().departmentName.equals(departmentInfo.name);
                        }
                    })
                    .subscribe(new Consumer<DepartmentInfo>() {
                        @Override
                        public void accept(DepartmentInfo departmentInfo) throws Exception {
                            EamApplication.getAccountInfo().departmentId = departmentInfo.id;
                            EamApplication.dao().getAccountInfoDao().update(EamApplication.getAccountInfo());

                            Flowable.fromIterable(entity.result)
                                    .subscribeOn(Schedulers.newThread())
                                    .subscribe(new Consumer<DepartmentInfo>() {
                                        @Override
                                        public void accept(DepartmentInfo departmentInfo) throws Exception {
                                            departmentInfo.searchPinyin = PinYinUtils.getPinyin(departmentInfo.name);
                                            long departId = EamApplication.getAccountInfo().departmentId;
                                            if(departmentInfo.layRec.contains(String.valueOf(departId))&& EamApplication.getAccountInfo().firstDepartmentId==0 && departmentInfo.layRec.contains("-")) {
                                                long cid = Long.parseLong(departmentInfo.layRec.split("-")[0]);
                                                EamApplication.getAccountInfo().firstDepartmentId = cid;
                                                if(departmentInfo.fullPathName!=null) {
                                                    EamApplication.getAccountInfo().firstDepartmentName = departmentInfo.fullPathName.split("/")[0];
                                                }
                                            }
                                            EamApplication.dao().getDepartmentInfoDao().update(departmentInfo);
                                        }
                                    });
                        }
                    });
    }

    @Override
    public void listDepartmentFailed(String errorMsg) {
        LogUtil.e("DepartmentController:" + errorMsg);
    }
}
