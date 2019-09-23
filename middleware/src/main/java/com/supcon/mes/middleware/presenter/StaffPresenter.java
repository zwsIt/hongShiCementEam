package com.supcon.mes.middleware.presenter;

import android.text.TextUtils;

import com.supcon.mes.middleware.model.bean.UserInfoListEntity;
import com.supcon.mes.middleware.model.contract.StaffContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;

public class StaffPresenter extends StaffContract.Presenter {
    @Override
    public void listCommonContractStaff(String staffName, int pageNo) {
        mCompositeSubscription.add(MiddlewareHttpClient.queryUserInfoList(staffName, pageNo)
                .onErrorReturn(throwable -> {
                    UserInfoListEntity userInfoListEntity = new UserInfoListEntity();
                    userInfoListEntity.errMsg = throwable.toString();
                    return userInfoListEntity;
                })
                .subscribe(userInfoListEntity -> {
                    if (TextUtils.isEmpty(userInfoListEntity.errMsg)) {
                        getView().listCommonContractStaffSuccess(userInfoListEntity);
                    } else {
                        getView().listCommonContractStaffFailed(userInfoListEntity.errMsg);
                    }
                }));
    }
}
