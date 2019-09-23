package com.supcon.mes.module_yhgl.presenter;


import com.supcon.mes.module_yhgl.model.bean.SparePartListEntity;
import com.supcon.mes.module_yhgl.model.contract.SparePartContract;
import com.supcon.mes.module_yhgl.model.network.YHGLHttpClient;

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
                YHGLHttpClient.listSpareParts(id)
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
