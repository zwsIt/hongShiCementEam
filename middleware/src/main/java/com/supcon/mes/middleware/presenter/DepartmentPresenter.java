package com.supcon.mes.middleware.presenter;

import com.supcon.mes.middleware.model.bean.DepartmentInfoListEntity;
import com.supcon.mes.middleware.model.contract.DepartmentQueryContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangshizhan on 2018/8/20
 * Email:wangshizhan@supcom.com
 */
public class DepartmentPresenter extends DepartmentQueryContract.Presenter {

    @Override
    public void listDepartment() {
        mCompositeSubscription.add(
                MiddlewareHttpClient.listDepartment(1)//暂时500个部门够用了
                        .onErrorReturn(new Function<Throwable, DepartmentInfoListEntity>() {
                            @Override
                            public DepartmentInfoListEntity apply(Throwable throwable) throws Exception {
                                DepartmentInfoListEntity departmentInfoListEntity = new DepartmentInfoListEntity();
                                departmentInfoListEntity.success = false;
                                departmentInfoListEntity.errMsg = throwable.toString();
                                return departmentInfoListEntity;
                            }
                        })
                        .subscribe(new Consumer<DepartmentInfoListEntity>() {
                            @Override
                            public void accept(DepartmentInfoListEntity departmentInfoListEntity) throws Exception {
                                if(departmentInfoListEntity.result != null && departmentInfoListEntity.result.size()!=0){
                                    getView().listDepartmentSuccess(departmentInfoListEntity);
                                }
                                else{
                                    getView().listDepartmentFailed(departmentInfoListEntity.errMsg);
                                }
                            }
                        }));
    }
}
