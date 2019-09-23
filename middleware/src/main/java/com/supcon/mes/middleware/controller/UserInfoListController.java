package com.supcon.mes.middleware.controller;

import android.annotation.SuppressLint;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.api.UserListQueryAPI;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.bean.UserInfo;
import com.supcon.mes.middleware.model.bean.UserInfoDao;
import com.supcon.mes.middleware.model.bean.UserInfoListEntity;
import com.supcon.mes.middleware.model.contract.UserListQueryContract;
import com.supcon.mes.middleware.presenter.UserInfoPresenter;
import com.supcon.mes.middleware.util.PinYinUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wangshizhan on 2018/9/19
 * Email:wangshizhan@supcom.com
 */
@Presenter(UserInfoPresenter.class)
public class UserInfoListController extends BasePresenterController implements UserListQueryAPI, UserListQueryContract.View {

    @Override
    public void onInit() {
        super.onInit();
        EamApplication.dao().getStaffDao().deleteAll();
        EamApplication.dao().getUserInfoDao().deleteAll();
        queryUserInfoList("", 1);
    }

    @Override
    public void queryUserInfoList(String staffName, int pageNo) {
        presenterRouter.create(UserListQueryAPI.class).queryUserInfoList(staffName, pageNo);
    }

    @SuppressLint("CheckResult")
    @Override
    public void queryUserInfoListSuccess(UserInfoListEntity entity) {
//        if(entity.result!=null && entity.result.size()!=0){
//            for (UserInfo userInfo :entity.result){
////                EamApplication.dao().getStaffDao().insertOrReplace(userInfo.staff);
//                userInfo.namePinyin = PinYinUtils.getPinyin(userInfo.name);
//                userInfo.host = EamApplication.getIp();
//                userInfo.staffId = userInfo.staff.id;
//                userInfo.staffName= userInfo.staff.name;
//                userInfo.staffCode = userInfo.staff.code;
//
//                EamApplication.dao().getStaffDao().insertOrReplace(userInfo.staff);
//            }
//        }

        Flowable.fromIterable(entity.result)
                .subscribeOn(Schedulers.newThread())
                .filter(new Predicate<UserInfo>() {
                    @Override
                    public boolean test(UserInfo userInfo) throws Exception {
                        return userInfo.staff != null;
                    }
                })
                .map(new Function<UserInfo, Staff>() {
                    @Override
                    public Staff apply(UserInfo userInfo) throws Exception {

                        userInfo.namePinyin = PinYinUtils.getPinyin(userInfo.staff.name);
                        userInfo.host = EamApplication.getIp();
                        userInfo.staffId = userInfo.staff.id;
                        userInfo.staffName = userInfo.staff.name;
                        userInfo.staffCode = userInfo.staff.code;


                        return userInfo.staff;
                    }
                })
                .subscribe(new Consumer<Staff>() {
                    @Override
                    public void accept(Staff staff) throws Exception {
                        EamApplication.dao().getStaffDao().insertOrReplace(staff);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        EamApplication.dao().getUserInfoDao().insertOrReplaceInTx(entity.result);
                    }
                });


        if (entity.hasNext) {
            queryUserInfoList("", entity.pageNo + 1);
        }
    }

    @Override
    public void queryUserInfoListFailed(String errorMsg) {
        LogUtil.e("UserInfoListController:" + errorMsg);
    }
}
