package com.supcon.mes.module_olxj.presenter;

import android.text.TextUtils;

import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.ResultEntity;
import com.supcon.mes.module_olxj.model.contract.OLXJTaskCreateContract;
import com.supcon.mes.module_olxj.model.contract.OLXJTaskStatusContract;
import com.supcon.mes.module_olxj.model.network.OLXJClient;

import java.util.Map;
import java.util.Objects;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangshizhan on 2019/4/1
 * Email:wangshizhan@supcom.com
 */
public class OLXJTaskStatusPresenter extends OLXJTaskStatusContract.Presenter {

    @Override
    public void updateStatus(long staffId, String taskId) {
        mCompositeSubscription.add(
                OLXJClient.updateStatus(EamApplication.getAccountInfo().staffId, taskId)
                        .onErrorReturn(throwable -> {
                            ResultEntity resultEntity = new ResultEntity();
                            resultEntity.success = false;
                            resultEntity.errMsg = throwable.toString();
                            return resultEntity;
                        })
                        .subscribe(resultEntity -> {
                            if (resultEntity.success) {
                                Objects.requireNonNull(getView()).updateStatusSuccess();
                            } else {
                                Objects.requireNonNull(getView()).updateStatusFailed(resultEntity.errMsg);
                            }
                        }, throwable -> {
                            Objects.requireNonNull(getView()).updateStatusFailed(throwable.toString());
                        })
        );

    }

    @Override
    public void cancelTasks(String taskIDs, String changeState) {
        mCompositeSubscription.add(
                OLXJClient.cancelTask(taskIDs, changeState)
                        .onErrorReturn(throwable -> {
                            ResultEntity resultEntity = new ResultEntity();
                            resultEntity.success = false;
                            resultEntity.errMsg = throwable.toString();
                            return resultEntity;
                        })
                        .subscribe(resultEntity -> {
                            if (!TextUtils.isEmpty(resultEntity.errMsg)) {
                                Objects.requireNonNull(getView()).cancelTasksFailed(resultEntity.errMsg);
                            } else {
                                Objects.requireNonNull(getView()).cancelTasksSuccess();
                            }
                        }, throwable -> {
                            Objects.requireNonNull(getView()).cancelTasksFailed(throwable.toString());
                        })
        );
    }

    @Override
    public void endTasks(String taskIDs, String endReason, boolean isFinish) {
        mCompositeSubscription.add(
                OLXJClient.endTask(taskIDs, endReason, isFinish)
                        .onErrorReturn(throwable -> {
                            ResultEntity resultEntity = new ResultEntity();
                            resultEntity.success = false;
                            resultEntity.errMsg = throwable.toString();
                            return resultEntity;
                        })
                        .subscribe(resultEntity -> {
                            if (resultEntity.success) {
                                Objects.requireNonNull(getView()).endTasksSuccess();
                            } else {
                                Objects.requireNonNull(getView()).endTasksFailed(resultEntity.errMsg);
                            }
                        }, throwable -> {
                            Objects.requireNonNull(getView()).endTasksFailed(throwable.toString());
                        })
        );
    }

}
