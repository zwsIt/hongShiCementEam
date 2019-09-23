package com.supcon.mes.module_wxgd.presenter;

import com.supcon.mes.module_wxgd.model.bean.SparePartListEntity;
import com.supcon.mes.module_wxgd.model.contract.SparePartContract;
import com.supcon.mes.module_wxgd.model.network.HttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangshizhan on 2018/8/28
 * Email:wangshizhan@supcom.com
 */
public class SparePartPresenter extends SparePartContract.Presenter {

    @Override
    public void listSparePartList(long id) {
        mCompositeSubscription.add(
                HttpClient.listSpareParts(id)
                        .onErrorReturn(new Function<Throwable, SparePartListEntity>() {
                            @Override
                            public SparePartListEntity apply(Throwable throwable) throws Exception {
                                SparePartListEntity sparePartListEntity = new SparePartListEntity();
                                sparePartListEntity.errMsg = throwable.toString();
                                return sparePartListEntity;
                            }
                        })
                        .subscribe(new Consumer<SparePartListEntity>() {
                            @Override
                            public void accept(SparePartListEntity sparePartListEntity) throws Exception {
                                if(sparePartListEntity.result != null){
                                    getView().listSparePartListSuccess(sparePartListEntity);
                                }
                                else{
                                    getView().listSparePartListFailed(sparePartListEntity.errMsg);
                                }
                            }
                        }));
    }
}
