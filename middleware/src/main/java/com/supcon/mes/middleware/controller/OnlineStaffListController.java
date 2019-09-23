package com.supcon.mes.middleware.controller;

import android.content.Context;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BaseDataController;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.api.OnlineStaffListAPI;
import com.supcon.mes.middleware.model.bean.TxlListEntity;
import com.supcon.mes.middleware.model.contract.OnlineStaffListContract;
import com.supcon.mes.middleware.presenter.OnlineStaffListPresenter;

/**
 * @Author xushiyun
 * @Create-time 7/23/19
 * @Pageage com.supcon.mes.middleware.controller
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
@Presenter(OnlineStaffListPresenter.class)
public class OnlineStaffListController extends BaseDataController implements OnlineStaffListContract.View {
    public OnlineStaffListController(Context context) {
        super(context);
    }

    @Override
    public void onInit() {
        super.onInit();
        getAllStaffList();
    }

    public void getAllStaffList() {
        presenterRouter.create(OnlineStaffListAPI.class).getOnlineStaffList();
    }

    @Override
    public void getOnlineStaffListSuccess(TxlListEntity entity) {
//        EamApplication.dao().getTxlEntityDao().deleteAll();
        if (entity.result != null && entity.result.size() > 0) {
            EamApplication.dao().getTxlEntityDao().insertOrReplaceInTx(entity.result);
        }
    }

    @Override
    public void getOnlineStaffListFailed(String errorMsg) {

    }
}
