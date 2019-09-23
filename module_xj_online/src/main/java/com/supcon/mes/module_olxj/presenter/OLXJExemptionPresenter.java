package com.supcon.mes.module_olxj.presenter;

import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJExemptionEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJWorkItemEntity;
import com.supcon.mes.module_olxj.model.contract.OLXJExemptionContract;
import com.supcon.mes.module_olxj.model.network.OLXJClient;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * 免检规则
 */
public class OLXJExemptionPresenter extends OLXJExemptionContract.Presenter {
    @Override
    public void getExemptionEam(List<OLXJWorkItemEntity> olxjWorkItemEntities) {
        List<OLXJExemptionEntity> olxjExemptionEntitys = new ArrayList<>();
        mCompositeSubscription.add(Flowable.fromIterable(olxjWorkItemEntities)
                .flatMap(new Function<OLXJWorkItemEntity, Publisher<CommonBAPListEntity<OLXJExemptionEntity>>>() {
                    @Override
                    public Publisher<CommonBAPListEntity<OLXJExemptionEntity>> apply(OLXJWorkItemEntity olxjWorkItemEntity) throws Exception {
                        return OLXJClient.getExemptionEam(olxjWorkItemEntity.workItemID.id);
                    }
                })
                .onErrorReturn(new Function<Throwable, CommonBAPListEntity<OLXJExemptionEntity>>() {
                    @Override
                    public CommonBAPListEntity<OLXJExemptionEntity> apply(Throwable throwable) throws Exception {
                        CommonBAPListEntity<OLXJExemptionEntity> olxjExemptionEntity = new CommonBAPListEntity<>();
                        olxjExemptionEntity.errMsg = throwable.toString();
                        return olxjExemptionEntity;
                    }
                })
                .subscribe(new Consumer<CommonBAPListEntity<OLXJExemptionEntity>>() {
                    @Override
                    public void accept(CommonBAPListEntity<OLXJExemptionEntity> olxjExemptionEntity) throws Exception {
                        if (olxjExemptionEntity.result != null) {
                            olxjExemptionEntitys.addAll(olxjExemptionEntity.result);
                        }
                    }
                }, throwable -> {
                    getView().getExemptionEamFailed(throwable.toString());
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        getView().getExemptionEamSuccess(olxjExemptionEntitys);
                    }
                }));
    }
}
