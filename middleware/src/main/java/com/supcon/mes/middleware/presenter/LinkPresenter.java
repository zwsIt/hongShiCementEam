package com.supcon.mes.middleware.presenter;

import com.supcon.mes.middleware.model.bean.LinkListEntity;
import com.supcon.mes.middleware.model.contract.LinkQueryContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangshizhan on 2018/8/22
 * Email:wangshizhan@supcom.com
 */
public class LinkPresenter extends LinkQueryContract.Presenter {

    @Override
    public void queryCurrentLink(long pendingId) {
        mCompositeSubscription.add(
                MiddlewareHttpClient.getCurrentActTransition(pendingId)
                        .onErrorReturn(new Function<Throwable, LinkListEntity>() {
                            @Override
                            public LinkListEntity apply(Throwable throwable) throws Exception {
                                LinkListEntity linkListEntity = new LinkListEntity();
                                linkListEntity.success = false;
                                linkListEntity.errMsg = throwable.toString();
                                return linkListEntity;
                            }
                        })
                        .subscribe(new Consumer<LinkListEntity>() {
                            @Override
                            public void accept(LinkListEntity linkListEntity) throws Exception {
                                if(linkListEntity.success){
                                    getView().queryCurrentLinkSuccess(linkListEntity);
                                }
                                else{
                                    getView().queryCurrentLinkFailed(linkListEntity.errMsg);
                                }
                            }
                        })
        );
    }

    @Override
    public void queryStartLink(String flowKey) {
        mCompositeSubscription.add(
                MiddlewareHttpClient.getStartTransition(flowKey)
                        .onErrorReturn(new Function<Throwable, LinkListEntity>() {
                            @Override
                            public LinkListEntity apply(Throwable throwable) throws Exception {
                                LinkListEntity linkListEntity = new LinkListEntity();
                                linkListEntity.success = false;
                                linkListEntity.errMsg = throwable.toString();
                                return linkListEntity;
                            }
                        })
                        .subscribe(new Consumer<LinkListEntity>() {
                            @Override
                            public void accept(LinkListEntity linkListEntity) throws Exception {
                                if(linkListEntity.success){
                                    getView().queryStartLinkSuccess(linkListEntity);
                                }
                                else{
                                    getView().queryStartLinkFailed(linkListEntity.errMsg);
                                }
                            }
                        })
        );
    }

}
