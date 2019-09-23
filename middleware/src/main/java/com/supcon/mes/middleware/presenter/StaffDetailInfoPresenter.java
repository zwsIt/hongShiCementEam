package com.supcon.mes.middleware.presenter;

import com.supcon.mes.middleware.model.bean.StaffDetailInfoListEntity;
import com.supcon.mes.middleware.model.contract.StaffDetailInfoQueryContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangshizhan on 2018/8/1
 * Email:wangshizhan@supcom.com
 */
public class StaffDetailInfoPresenter extends StaffDetailInfoQueryContract.Presenter {
    @Override
    public void queryStaffDetailInfo(String staffCode, long companyId) {
        mCompositeSubscription.add(
                MiddlewareHttpClient.queryStaffDetailInfo(staffCode, companyId)
                        .onErrorReturn(new Function<Throwable, StaffDetailInfoListEntity>() {
                            @Override
                            public StaffDetailInfoListEntity apply(Throwable throwable) throws Exception {
                                StaffDetailInfoListEntity staffDetailInfoListEntity = new StaffDetailInfoListEntity();
                                staffDetailInfoListEntity.success = false;
                                staffDetailInfoListEntity.errMsg = throwable.toString();
                                return staffDetailInfoListEntity;
                            }
                        })
                        .subscribe(new Consumer<StaffDetailInfoListEntity>() {
                            @Override
                            public void accept(StaffDetailInfoListEntity staffDetailInfoListEntity) throws Exception {

                                if(staffDetailInfoListEntity.result!=null && staffDetailInfoListEntity.result.size() != 0){
                                    getView().queryStaffDetailInfoSuccess(staffDetailInfoListEntity.result.get(0));//一般只有一个信息
                                }
                                else{
                                    getView().queryStaffDetailInfoFailed(staffDetailInfoListEntity.errMsg);
                                }

                            }
                        })
        );
    }
}
