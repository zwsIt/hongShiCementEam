package com.supcon.mes.middleware.presenter;

import android.text.TextUtils;

import com.supcon.mes.mbap.beans.LinkEntity;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.middleware.model.bean.WorkFlowListEntity;
import com.supcon.mes.middleware.model.contract.WorkFlowContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;

import org.json.JSONArray;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangshizhan on 2018/7/20
 * Email:wangshizhan@supcom.com
 */
public class WorkFlowPresenter extends WorkFlowContract.Presenter{

    @Override
    public void findWorkFlow(long pendingId) {
        mCompositeSubscription.add(
                MiddlewareHttpClient.findWorkFlow(pendingId)
                        .onErrorReturn(new Function<Throwable, WorkFlowListEntity>() {
                            @Override
                            public WorkFlowListEntity apply(Throwable throwable) throws Exception {
                                WorkFlowListEntity workFlowListEntity = new WorkFlowListEntity();
                                workFlowListEntity.success = false;
                                workFlowListEntity.errMsg = throwable.toString();

                                return workFlowListEntity;
                            }
                        })
                        .subscribe(new Consumer<WorkFlowListEntity>() {
                            @Override
                            public void accept(WorkFlowListEntity linkListEntity) throws Exception {

                                if(linkListEntity.success){
                                    if(linkListEntity.result != null  && !TextUtils.isEmpty(linkListEntity.result)){

                                        JSONArray jsonArray = new JSONArray(linkListEntity.result);
                                        linkListEntity.linkEntities.addAll(GsonUtil.jsonToList(jsonArray.toString(), LinkEntity.class));

                                        getView().findWorkFlowSuccess(linkListEntity);
                                    }
                                    else{
                                        getView().findWorkFlowFailed("获取列表为空");
                                    }
                                }

                                else{
                                    getView().findWorkFlowFailed(linkListEntity.errMsg);
                                }
                            }
                        })
        );
    }
}
