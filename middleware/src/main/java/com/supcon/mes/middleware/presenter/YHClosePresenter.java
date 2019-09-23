package com.supcon.mes.middleware.presenter;

import android.text.TextUtils;

import com.supcon.mes.mbap.beans.LinkEntity;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.middleware.model.bean.ResultEntity;
import com.supcon.mes.middleware.model.bean.WorkFlowListEntity;
import com.supcon.mes.middleware.model.contract.WorkFlowContract;
import com.supcon.mes.middleware.model.contract.YHCloseContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;

import org.json.JSONArray;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangshizhan on 2018/7/20
 * Email:wangshizhan@supcom.com
 */
public class YHClosePresenter extends YHCloseContract.Presenter {

    @Override
    public void closeWorkAndSaveReason(long id, String reason) {
        mCompositeSubscription.add(
                MiddlewareHttpClient.closeWorkAndSaveReason(id, reason)
                        .onErrorReturn(new Function<Throwable, ResultEntity>() {
                            @Override
                            public ResultEntity apply(Throwable throwable) throws Exception {
                                ResultEntity resultEntity = new ResultEntity();
                                resultEntity.success = false;
                                resultEntity.errMsg = throwable.toString();
                                return resultEntity;
                            }
                        })
                        .subscribe(new Consumer<ResultEntity>() {
                            @Override
                            public void accept(ResultEntity resultEntity) throws Exception {
                                if (resultEntity.success) {
                                    getView().closeWorkAndSaveReasonSuccess(resultEntity);
                                } else {
                                    getView().closeWorkAndSaveReasonFailed(resultEntity.errMsg);
                                }
                            }
                        }));

    }
}
