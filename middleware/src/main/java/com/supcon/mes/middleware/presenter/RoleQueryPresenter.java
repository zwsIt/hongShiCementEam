package com.supcon.mes.middleware.presenter;

import com.supcon.mes.middleware.model.bean.RoleListEntity;
import com.supcon.mes.middleware.model.contract.RoleQueryContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangshizhan on 2018/7/30
 * Email:wangshizhan@supcom.com
 */
public class RoleQueryPresenter extends RoleQueryContract.Presenter {

    @Override
    public void queryRoleListEntity(String userName) {
        mCompositeSubscription.add(MiddlewareHttpClient.queryRoleUserList(userName).onErrorReturn(new Function<Throwable, RoleListEntity>() {
            @Override
            public RoleListEntity apply(Throwable throwable) throws Exception {
                RoleListEntity roleListEntity = new RoleListEntity();
                roleListEntity.success = false;
                roleListEntity.errMsg = throwable.toString();
                return roleListEntity;
            }
        }).subscribe(new Consumer<RoleListEntity>() {
            @Override
            public void accept(RoleListEntity roleListEntity) throws Exception {
                if(roleListEntity.result!=null && roleListEntity.result.size() != 0){
                    getView().queryRoleListEntitySuccess(roleListEntity);
                }
                else{
                    getView().queryRoleListEntityFailed(roleListEntity.errMsg);
                }
            }
        }));
    }

}
