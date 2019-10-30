package com.supcon.mes.module_sparepartapply_hl.controller;

import android.os.Bundle;
import android.view.View;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.view.CustomListWidget;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.SparePartReceiveEntity;
import com.supcon.mes.middleware.model.bean.SparePartReceiveListEntity;
import com.supcon.mes.middleware.model.event.BaseEvent;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_wxgd.IntentRouter;
import com.supcon.mes.module_wxgd.model.api.SparePartApplyDetailAPI;
import com.supcon.mes.module_wxgd.model.contract.SparePartApplyDetailContract;
import com.supcon.mes.module_wxgd.model.event.ListEvent;
import com.supcon.mes.module_wxgd.presenter.SparePartApplyDetailPresenter;
import com.supcon.mes.module_wxgd.ui.adapter.SparePartReceiveAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * @description SparePartApplyDetailController 备件领用申请明细
 * @author  2019/9/27
 */
@Presenter(SparePartApplyDetailPresenter.class)
public class SparePartApplyDetailController extends BaseViewController implements SparePartApplyDetailContract.View {

    private boolean editable;
    public Long tableId; // 单据ID

    private List<SparePartReceiveEntity> sparePartReceiveEntityList = new ArrayList<>();

    @BindByTag("sparePartListWidget")
    CustomListWidget<SparePartReceiveEntity> sparePartListWidget;

    public SparePartApplyDetailController(View rootView) {
        super(rootView);
    }

    public void setEditable(boolean isEditable) {
        this.editable = isEditable;
    }

    @Override
    public void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        tableId = getIntent().getLongExtra(Constant.IntentKey.TABLE_ID,-1);

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
        sparePartListWidget.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                Bundle bundle = new Bundle();
                switch (action) {
                    case CustomListWidget.ACTION_VIEW_ALL:
                    case 0:
//                        bundle.putString(Constant.IntentKey.SPARE_PART_ENTITIES, sparePartReceiveEntityList.toString());
                        bundle.putBoolean(Constant.IntentKey.IS_EDITABLE, editable);
//                        bundle.putBoolean(Constant.IntentKey.IS_ADD, false);
                        bundle.putLong(Constant.IntentKey.TABLE_ID,tableId);
                        IntentRouter.go(context, Constant.Router.SPARE_PART_APPLY_DETAIL_LIST, bundle);
                        break;
                    default:
                        break;
                }
            }
        });
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
        callBackData(entity);
        EventBus.getDefault().post(new ListEvent(Constant.EventFlag.SPAD, sparePartReceiveEntityList));
    }

    /**
     * 数据回填
     * @param entity
     */
    private void callBackData(SparePartReceiveListEntity entity) {
        sparePartReceiveEntityList = entity.result;
        if (sparePartListWidget != null) {
            sparePartListWidget.setData(entity.result);
            if (entity.result.size() == 0){
                sparePartListWidget.clear();
            }
            if (editable) {
                sparePartListWidget.setShowText("编辑 (" + entity.result.size() + ")");
            } else {
                sparePartListWidget.setShowText("查看 (" + entity.result.size() + ")");
            }
        }
    }

    @Override
    public void listSparePartApplyDetailFailed(String errorMsg) {
        LogUtil.e(errorMsg);
    }

    /**
     * 更新备件明细数据
     * @param mSparePartEntities
     */
    public void updateSparePartEntities(List<SparePartReceiveEntity> mSparePartEntities) {
        if (mSparePartEntities == null) return;

        SparePartReceiveListEntity sparePartReceiveListEntity = new SparePartReceiveListEntity();
        sparePartReceiveListEntity.result = mSparePartEntities;
        callBackData(sparePartReceiveListEntity);
    }

    /**
     * 获取备件领用申请明细PT
     * @return
     */
    public List<SparePartReceiveEntity> getSparePartApplyDetailList() {
        return sparePartReceiveEntityList;
    }
    
}
