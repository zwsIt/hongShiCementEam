package com.supcon.mes.middleware.controller;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.model.api.PcQueryAPI;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.middleware.model.contract.PcQueryContract;
import com.supcon.mes.middleware.model.listener.OnAPIResultListener;
import com.supcon.mes.middleware.presenter.PcQueryPresenter;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/6/24
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
@Presenter(value = {PcQueryPresenter.class})
public class PcController extends BasePresenterController implements PcQueryContract.View {

    private OnAPIResultListener<String> listener;

    public void queryPc(String operateCode, String flowKey,OnAPIResultListener<String> onAPIResultListener){
        listener = onAPIResultListener;
        presenterRouter.create(PcQueryAPI.class).queryPc(operateCode, flowKey);
    }

    @Override
    public void queryPcSuccess(CommonEntity entity) {
        if (listener !=  null){
            listener.onSuccess(entity.result == null ? "" : entity.result.toString());
        }
    }

    @Override
    public void queryPcFailed(String errorMsg) {
        if (listener !=  null){
            listener.onFail(errorMsg);
        }
    }
}
