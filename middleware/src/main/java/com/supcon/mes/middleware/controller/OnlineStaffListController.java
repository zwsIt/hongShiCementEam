package com.supcon.mes.middleware.controller;

import android.content.Context;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BaseDataController;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.api.OnlineStaffListAPI;
import com.supcon.mes.middleware.model.bean.TxlEntity;
import com.supcon.mes.middleware.model.bean.TxlListEntity;
import com.supcon.mes.middleware.model.contract.OnlineStaffListContract;
import com.supcon.mes.middleware.presenter.OnlineStaffListPresenter;

import java.util.ArrayList;
import java.util.List;

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

    private List<TxlEntity> mTxlEntityList;
    private int pageNo = 1;

    public OnlineStaffListController(Context context) {
        super(context);
    }

    @Override
    public void onInit() {
        super.onInit();
        mTxlEntityList = new ArrayList<>();
        getAllStaffList();
    }

    public void getAllStaffList() {
        presenterRouter.create(OnlineStaffListAPI.class).getOnlineStaffList(pageNo);
    }

    @Override
    public void getOnlineStaffListSuccess(TxlListEntity entity) {

        if (entity.result != null && entity.result.size() > 0) {
//            EamApplication.dao().getTxlEntityDao().insertOrReplaceInTx(entity.result);
            mTxlEntityList.addAll(entity.result);
        }
        if (entity.hasNext) {
            pageNo++;
            getAllStaffList();
        }else {
            EamApplication.dao().getTxlEntityDao().deleteAll();
            EamApplication.dao().getTxlEntityDao().insertOrReplaceInTx(mTxlEntityList);
        }
    }

    @Override
    public void getOnlineStaffListFailed(String errorMsg) {

    }
}
