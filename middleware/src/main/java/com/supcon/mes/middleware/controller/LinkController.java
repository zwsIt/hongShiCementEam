package com.supcon.mes.middleware.controller;

import android.annotation.SuppressLint;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.beans.LinkEntity;
import com.supcon.mes.mbap.beans.Transition;
import com.supcon.mes.mbap.view.CustomPopTransation;
import com.supcon.mes.mbap.view.CustomWorkFlowView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.api.LinkQueryAPI;
import com.supcon.mes.middleware.model.bean.LinkListEntity;
import com.supcon.mes.middleware.model.contract.LinkQueryContract;
import com.supcon.mes.middleware.presenter.LinkPresenter;
import com.supcon.mes.middleware.util.LinkHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * Created by wangshizhan on 2018/8/22
 * Email:wangshizhan@supcom.com
 */
@Presenter(LinkPresenter.class)
public class LinkController extends BasePresenterController implements LinkQueryContract.View{

    private CustomPopTransation mCustomPopTransation;
    private CustomWorkFlowView mCustomWorkFlowView;
    private List<LinkEntity> mLinkEntities;
    private List<Transition> mTransitions;
    private Long pendingId;
    private boolean cancelShow;

    @SuppressLint("CheckResult")
    @Override
    public void queryCurrentLinkSuccess(LinkListEntity entity) {
        if(entity.result!=null && entity.result.size()!=0){
            if (Constant.Transition.CANCEL_CN.equals(entity.result.get(0).description) || Constant.Transition.REJECT_CN.equals(entity.result.get(0).description)){
                Collections.reverse(entity.result);
            }
            mLinkEntities = entity.result;
        }
        if(mCustomPopTransation!=null)
            mCustomPopTransation.setTransitions(LinkHelper.convertToTransition(entity.result));

        if(mCustomWorkFlowView!=null){
            if (cancelShow){

                List<LinkEntity> linkEntities = new ArrayList<>();
                linkEntities.addAll(entity.result);
                for (LinkEntity linkEntity : linkEntities){
                    if (Constant.Transition.CANCEL_CN.equals(linkEntity.description)){
                        linkEntities.remove(linkEntity);
                        break;
                    }
                }
                mCustomWorkFlowView.setLinks(linkEntities.toString());

//                Flowable.fromIterable(entity.result)
//                        .filter(new Predicate<LinkEntity>() {
//                            @Override
//                            public boolean test(LinkEntity linkEntity) throws Exception {
//                                return Constant.Transition.CANCEL_CN.equals(linkEntity.description);
//                            }
//                        })
//                        .subscribe(new Consumer<LinkEntity>() {
//                            @Override
//                            public void accept(LinkEntity linkEntity) throws Exception {
//                                entity.result.remove(linkEntity);
//                            }
//                        }, new Consumer<Throwable>() {
//                            @Override
//                            public void accept(Throwable throwable) throws Exception {
//
//                            }
//                        }, new Action() {
//                            @Override
//                            public void run() throws Exception {
//                                mCustomWorkFlowView.setLinks(entity.result.toString());
//                            }
//                        });

            }else {
                mCustomWorkFlowView.setLinks(entity.result.toString());
            }
        }
    }

    @Override
    public void queryCurrentLinkFailed(String errorMsg) {
        LogUtil.e("LinkController queryCurrentLinkFailed:"+errorMsg);
    }

    @SuppressLint("CheckResult")
    @Override
    public void queryStartLinkSuccess(LinkListEntity entity) {

        mLinkEntities = new ArrayList<>();
        Flowable.fromIterable(entity.result)
                .filter(new Predicate<LinkEntity>() {
                    @Override
                    public boolean test(LinkEntity linkEntity) throws Exception {
                        return !(pendingId == null && linkEntity.description.equals("作废"));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LinkEntity>() {
                    @Override
                    public void accept(LinkEntity linkEntity) throws Exception {
                        mLinkEntities.add(linkEntity);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        if(mCustomPopTransation!=null){
                            mCustomPopTransation.setTransitions(LinkHelper.convertToTransition(mLinkEntities));
                        }

                        if(mCustomWorkFlowView!=null){
                            mCustomWorkFlowView.setLinks(mLinkEntities.toString());
                        }

                        mTransitions = LinkHelper.convertToTransition(mLinkEntities);
                    }
                });
    }

    @Override
    public void queryStartLinkFailed(String errorMsg) {
        LogUtil.e("LinkController queryStartLinkFailed:"+errorMsg);
    }


    public void initStartTransition(CustomWorkFlowView customWorkFlowView, String flowKey){
        this.mCustomWorkFlowView = customWorkFlowView;
        this.pendingId = null;
        presenterRouter.create(LinkQueryAPI.class).queryStartLink(flowKey);
    }

    public void initPendingTransition(CustomWorkFlowView customWorkFlowView, long pendingId){
        this.mCustomWorkFlowView = customWorkFlowView;
        this.pendingId = pendingId;
        presenterRouter.create(LinkQueryAPI.class).queryCurrentLink(pendingId);
    }

    public void initStartTransitionOld(CustomPopTransation transationView, String flowKey){
        this.mCustomPopTransation = transationView;
        this.pendingId = null;
        presenterRouter.create(LinkQueryAPI.class).queryStartLink(flowKey);
    }

    public void initPendingTransitionOld(CustomPopTransation transationView, long pendingId){
        this.mCustomPopTransation = transationView;
        this.pendingId = pendingId;
        presenterRouter.create(LinkQueryAPI.class).queryCurrentLink(pendingId);
    }

    public List<LinkEntity> getLinkEntities() {
        return mLinkEntities;
    }

    public List<Transition> getTransitions() {
        return mTransitions;
    }

    /**
     * @description 设置派单环节作废是否显示：工单来源于隐患单需要隐藏
     * @param show
     * @return
     * @author zhangwenshuai1 2019/2/15
     *
     */
    public void setCancelShow(boolean show) {
        this.cancelShow = show;
    }
}
