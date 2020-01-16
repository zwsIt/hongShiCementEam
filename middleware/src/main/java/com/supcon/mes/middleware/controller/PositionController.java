package com.supcon.mes.middleware.controller;

import android.annotation.SuppressLint;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.api.PositionQueryAPI;
import com.supcon.mes.middleware.model.bean.PositionEntity;
import com.supcon.mes.middleware.model.bean.PositionEntityDao;
import com.supcon.mes.middleware.model.bean.PositionEntityListEntity;
import com.supcon.mes.middleware.model.contract.PositionQueryContract;
import com.supcon.mes.middleware.presenter.PositionPresenter;
import com.supcon.mes.middleware.util.PinYinUtils;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wangshizhan on 2018/8/20
 * Email:wangshizhan@supcom.com
 */
@Presenter(PositionPresenter.class)
public class PositionController extends BasePresenterController implements PositionQueryContract.View, PositionQueryAPI {

    @Override
    public void onInit() {
        super.onInit();
        listPosition();
    }

    @Override
    public void listPosition() {
        presenterRouter.create(PositionQueryAPI.class).listPosition();
    }
    @SuppressLint("CheckResult")
    @Override
    public void listPositionSuccess(PositionEntityListEntity entity) {
        PositionEntityDao positionEntityDao = EamApplication.dao().getPositionEntityDao();
        positionEntityDao.deleteAll();
        positionEntityDao.insertOrReplaceInTx(entity.result);
        String positionName = EamApplication.getAccountInfo().positionName;
        if(positionName!=null)
            Flowable.fromIterable(entity.result)
                    .subscribeOn(Schedulers.newThread())
                    .filter(new Predicate<PositionEntity>() {
                        @Override
                        public boolean test(PositionEntity positionEntity) throws Exception {
                            return EamApplication.getAccountInfo().positionName.equals(positionEntity.name);
                        }
                    })
                    .subscribe(new Consumer<PositionEntity>() {
                        @Override
                        public void accept(PositionEntity positionEntity) throws Exception {
                            EamApplication.getAccountInfo().positionId = positionEntity.id;
                            EamApplication.dao().getAccountInfoDao().update(EamApplication.getAccountInfo());

                            Flowable.fromIterable(entity.result)
                                    .subscribeOn(Schedulers.newThread())
                                    .subscribe(new Consumer<PositionEntity>() {
                                        @Override
                                        public void accept(PositionEntity positionEntity) throws Exception {
                                            positionEntity.searchPinyin = PinYinUtils.getPinyin(positionEntity.name);
//                                            long positionId = EamApplication.getAccountInfo().positionId;
//                                            if(positionEntity.layRec.contains(String.valueOf(positionId))&& EamApplication.getAccountInfo().firstDepartmentId==0 && positionEntity.layRec.contains("-")) {
//                                                long cid = Long.parseLong(positionEntity.layRec.split("-")[0]);
//                                                EamApplication.getAccountInfo().firstDepartmentId = cid;
//                                                if(positionEntity.fullPathName!=null) {
//                                                    EamApplication.getAccountInfo().firstDepartmentName = positionEntity.fullPathName.split("/")[0];
//                                                }
//                                            }
                                            EamApplication.dao().getPositionEntityDao().update(positionEntity);
                                        }
                                    });
                        }
                    });
    }

    @Override
    public void listPositionFailed(String errorMsg) {
        LogUtil.e("DepartmentController:" + errorMsg);
    }

}
