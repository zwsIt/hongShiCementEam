package com.supcon.mes.middleware.controller;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.model.api.TableInfoAPI;
import com.supcon.mes.middleware.model.contract.TableInfoContract;
import com.supcon.mes.middleware.model.listener.OnAPIResultListener;
import com.supcon.mes.middleware.presenter.TableInfoPresenter;

import org.greenrobot.eventbus.EventBus;

/**
 * @description TableInfoController 获取单据表头信息
 * @author  2019/9/27
 */
@Presenter(value = {TableInfoPresenter.class})
public class TableInfoController extends BasePresenterController implements TableInfoContract.View {

    private OnAPIResultListener onAPIResultListener;

    public void getTableInfo(String url, Long tableId, String includes, OnAPIResultListener listener) {
        onAPIResultListener = listener;
        presenterRouter.create(TableInfoAPI.class).getTableInfo(url,tableId,includes);
    }

    @Override
    public void getTableInfoSuccess(Object entity) {
        if (onAPIResultListener != null){
            onAPIResultListener.onSuccess(entity);
        }
//        EventBus.getDefault().post(entity);
    }

    @Override
    public void getTableInfoFailed(String errorMsg) {
        if (onAPIResultListener != null){
            onAPIResultListener.onFail(errorMsg);
        }
        LogUtil.e(errorMsg + "");
    }

}
