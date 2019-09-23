package com.supcon.mes.module_sbda.presenter;

import com.supcon.mes.module_sbda.model.bean.SearchDeviceEntity;
import com.supcon.mes.module_sbda.model.contract.SearchDeviceContract;
import com.supcon.mes.module_sbda.model.network.SBDAHttpClient;

/**
 * Created by wangshizhan on 2017/12/1.
 * Email:wangshizhan@supcon.com
 */

public class SearchDevicePresenter extends SearchDeviceContract.Presenter {


    @Override
    public void searchDevice(String code) {
        mCompositeSubscription.add(

                SBDAHttpClient.searchDevice(code)
                        .onErrorReturn(throwable -> {
                            SearchDeviceEntity entity = new SearchDeviceEntity();
                            entity.success = false;
                            entity.errMsg = throwable.toString();
                            return entity;
                        })
                        .subscribe(searchDeviceEntity -> {
                            if(searchDeviceEntity.success){
                                getView().searchDeviceSuccess(searchDeviceEntity);
                            }
                            else{
                                getView().searchDeviceFailed(searchDeviceEntity.errMsg);
                            }
                        })
        );
    }

}
