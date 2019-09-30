package com.supcon.mes.module_wxgd.controller;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.module_wxgd.model.api.TableInfoAPI;
import com.supcon.mes.module_wxgd.model.bean.SparePartApplyHeaderInfoEntity;
import com.supcon.mes.module_wxgd.model.contract.TableInfoContract;
import com.supcon.mes.module_wxgd.presenter.TableInfoPresenter;

import org.greenrobot.eventbus.EventBus;

/**
 * @description TableInfoController 获取单据表头信息
 * @author  2019/9/27
 */
@Presenter(value = {TableInfoPresenter.class})
public class TableInfoController extends BasePresenterController implements TableInfoContract.View {

    public void getSparePartApplyTableInfo(Long tableId, String includes) {
        presenterRouter.create(TableInfoAPI.class).getSparePartApplyTableInfo(tableId,includes);
    }

    @Override
    public void getSparePartApplyTableInfoSuccess(SparePartApplyHeaderInfoEntity entity) {
        EventBus.getDefault().post(entity);
    }

    @Override
    public void getSparePartApplyTableInfoFailed(String errorMsg) {
        LogUtil.w(errorMsg);
    }

}
