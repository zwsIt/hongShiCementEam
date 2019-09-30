package com.supcon.mes.module_wxgd.controller;

import android.view.View;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.mes.mbap.view.CustomListWidget;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.SparePartReceiveEntity;
import com.supcon.mes.middleware.model.bean.SparePartReceiveListEntity;
import com.supcon.mes.middleware.model.event.BaseEvent;
import com.supcon.mes.module_wxgd.model.api.SparePartApplyDetailAPI;
import com.supcon.mes.module_wxgd.model.contract.SparePartApplyDetailContract;
import com.supcon.mes.module_wxgd.presenter.SparePartApplyDetailPresenter;
import com.supcon.mes.module_wxgd.ui.SparePartApplyEditActivity;
import com.supcon.mes.module_wxgd.ui.adapter.SparePartReceiveAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @description SparePartApplyDetailController 备件领用申请明细
 * @author  2019/9/27
 */
@Presenter(SparePartApplyDetailPresenter.class)
public class SparePartApplyDetailController extends BaseViewController implements SparePartApplyDetailContract.View {

    private boolean editable;
    public Long tableId; // 单据ID

    @BindByTag("sparePartListWidget")
    CustomListWidget<SparePartReceiveEntity> sparePartListWidget;

    public SparePartApplyDetailController(View rootView) {
        super(rootView);
    }

    @Override
    public void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        tableId = getIntent().getLongExtra(Constant.IntentKey.TABLE_ID,0);

    }

    @Override
    public void initView() {
        super.initView();
        sparePartListWidget.setAdapter(new SparePartReceiveAdapter(context,editable));
    }

    @Override
    public void initData() {
        super.initData();
        presenterRouter.create(SparePartApplyDetailAPI.class).listSparePartApplyDetail(tableId);
    }

    @Override
    public void initListener() {
        super.initListener();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(BaseEvent baseEvent){

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void listSparePartApplyDetailSuccess(SparePartReceiveListEntity entity) {
        if (sparePartListWidget != null) {
            sparePartListWidget.setData(entity.result);
            if (editable) {
                sparePartListWidget.setShowText("编辑 (" + entity.result.size() + ")");
            } else {
                sparePartListWidget.setShowText("查看 (" + entity.result.size() + ")");
            }
        }
    }

    @Override
    public void listSparePartApplyDetailFailed(String errorMsg) {

    }


    public void setEditable(boolean isEditable) {
        this.editable = isEditable;
    }
    
}
