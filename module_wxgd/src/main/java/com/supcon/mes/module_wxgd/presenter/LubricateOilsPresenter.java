package com.supcon.mes.module_wxgd.presenter;

import com.supcon.mes.module_wxgd.model.bean.LubricateOilsListEntity;
import com.supcon.mes.module_wxgd.model.bean.RepairStaffListEntity;
import com.supcon.mes.module_wxgd.model.contract.LubricateOilsContract;
import com.supcon.mes.module_wxgd.model.contract.RepairStaffContract;
import com.supcon.mes.module_wxgd.model.network.HttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangshizhan on 2018/8/28
 * Email:wangshizhan@supcom.com
 */
public class LubricateOilsPresenter extends LubricateOilsContract.Presenter {

    @Override
    public void listLubricateOilsList(long id) {
        mCompositeSubscription.add(
                HttpClient.listLubricateOils(id)
                        .onErrorReturn(new Function<Throwable, LubricateOilsListEntity>() {
                            @Override
                            public LubricateOilsListEntity apply(Throwable throwable) throws Exception {
                                LubricateOilsListEntity lubricateOilsListEntity = new LubricateOilsListEntity();
                                lubricateOilsListEntity.errMsg = throwable.toString();
                                return lubricateOilsListEntity;
                            }
                        })
                        .subscribe(new Consumer<LubricateOilsListEntity>() {
                            @Override
                            public void accept(LubricateOilsListEntity lubricateOilsListEntity) throws Exception {
                                if(lubricateOilsListEntity.result != null){
                                    getView().listLubricateOilsListSuccess(lubricateOilsListEntity);
                                }
                                else{
                                    getView().listLubricateOilsListFailed(lubricateOilsListEntity.errMsg);
                                }
                            }
                        }));
    }
}
