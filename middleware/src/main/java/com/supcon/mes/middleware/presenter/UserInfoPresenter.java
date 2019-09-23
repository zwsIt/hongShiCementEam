package com.supcon.mes.middleware.presenter;

import com.supcon.mes.middleware.model.bean.UserInfoListEntity;
import com.supcon.mes.middleware.model.contract.UserListQueryContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangshizhan on 2018/9/19
 * Email:wangshizhan@supcom.com
 */
public class UserInfoPresenter extends UserListQueryContract.Presenter {

    @Override
    public void queryUserInfoList(String staffName,int pageNo) {
        mCompositeSubscription.add(
                MiddlewareHttpClient.queryUserInfoList(staffName,pageNo)
                        .onErrorReturn(new Function<Throwable, UserInfoListEntity>() {
                            @Override
                            public UserInfoListEntity apply(Throwable throwable) throws Exception {
                                UserInfoListEntity userInfoListEntity = new UserInfoListEntity();
                                userInfoListEntity.errMsg = throwable.toString();
                                userInfoListEntity.success = false;
                                return userInfoListEntity;
                            }
                        })
                        .subscribe(new Consumer<UserInfoListEntity>() {
                            @Override
                            public void accept(UserInfoListEntity userInfoListEntity) throws Exception {
                                if(userInfoListEntity.result!=null && userInfoListEntity.result.size()!=0){
                                    getView().queryUserInfoListSuccess(userInfoListEntity);
                                }
                                else {
                                    getView().queryUserInfoListFailed(userInfoListEntity.errMsg);
                                }
                            }
                        }));
    }

}
