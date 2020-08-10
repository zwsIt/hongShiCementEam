package com.supcon.mes.module_wxgd.controller;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.view.CustomListWidget;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.event.BaseEvent;
import com.supcon.mes.module_wxgd.IntentRouter;
import com.supcon.mes.module_wxgd.model.api.RepairStaffAPI;
import com.supcon.mes.middleware.model.bean.RepairStaffEntity;
import com.supcon.mes.module_wxgd.model.bean.RepairStaffListEntity;
import com.supcon.mes.middleware.model.bean.WXGDEntity;
import com.supcon.mes.module_wxgd.model.contract.RepairStaffContract;
import com.supcon.mes.middleware.model.event.ListEvent;
import com.supcon.mes.module_wxgd.presenter.RepairStaffPresenter;
import com.supcon.mes.module_wxgd.ui.adapter.RepairStaffAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by wangshizhan on 2018/8/28
 * Email:wangshizhan@supcom.com
 */
@Presenter(RepairStaffPresenter.class)
public class RepairStaffController extends BaseViewController implements RepairStaffContract.View {
    private List<RepairStaffEntity> staffEntities = new ArrayList<>();

    private boolean isEditable;
    private WXGDEntity mWXGDEntity;

    @BindByTag("repairStaffListWidget")
    CustomListWidget<RepairStaffEntity> repairStaffListWidget;

    public RepairStaffController(View rootView) {
        super(rootView);
    }

    @Override
    public void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        mWXGDEntity = (WXGDEntity) ((Activity) context).getIntent().getSerializableExtra(Constant.IntentKey.WXGD_ENTITY);
    }

    @Override
    public void initView() {
        super.initView();
        repairStaffListWidget.setAdapter(new RepairStaffAdapter(context, false));
    }

    @Override
    public void listRepairStaffListSuccess(RepairStaffListEntity entity) {
        staffEntities = entity.result;
        for (RepairStaffEntity repairStaffEntity : staffEntities) {
            if (repairStaffEntity.remark == null) {
                repairStaffEntity.remark = "";
            }
            if (repairStaffEntity.workHour != null) {
                repairStaffEntity.workHour = repairStaffEntity.workHour.setScale(2, BigDecimal.ROUND_HALF_UP);
            }
        }
        if (repairStaffListWidget != null) {
//            repairStaffListWidget.setData(entity.result);
//            repairStaffListWidget.setTotal(entity.result.size());
            repairStaffListWidget.setData(entity.result);
            if (isEditable) {
                repairStaffListWidget.setShowText("编辑 (" + entity.result.size() + ")");
            } else {
                repairStaffListWidget.setShowText("查看 (" + entity.result.size() + ")");
            }
        }
        EventBus.getDefault().post(new ListEvent("repairStaff", staffEntities));
    }

    @Override
    public void initListener() {
        super.initListener();
        repairStaffListWidget.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                Bundle bundle = new Bundle();
                switch (action) {
                    case CustomListWidget.ACTION_VIEW_ALL:
                    case 0:
                        bundle.putString(Constant.IntentKey.REPAIR_STAFF_ENTITIES, staffEntities.toString());
                        bundle.putBoolean(Constant.IntentKey.IS_EDITABLE, isEditable);
                        bundle.putBoolean(Constant.IntentKey.IS_ADD, false);
                        bundle.putLong(Constant.IntentKey.REPAIR_SUM,mWXGDEntity.repairSum != null ? mWXGDEntity.repairSum : 1);
                        bundle.putString(Constant.IntentKey.TABLE_STATUS, mWXGDEntity.getPending().taskDescription);
                        bundle.putSerializable(Constant.IntentKey.WXGD_ENTITY,mWXGDEntity);
                        IntentRouter.go(context, Constant.Router.WXGD_REPAIR_STAFF_LIST, bundle);
                        break;
                    default:
                        break;
                }

            }
        });
    }

    @Override
    public void listRepairStaffListFailed(String errorMsg) {
        LogUtil.e("RepairStaffController listRepairStaffListFailed:" + errorMsg);
    }

    public void setCustomListWidget(CustomListWidget<RepairStaffEntity> customListWidget) {
        this.repairStaffListWidget = customListWidget;
    }

    @Override
    public void initData() {
        super.initData();
    }

    public void setWxgdEntity(WXGDEntity mWxgdEntity) {
        this.mWXGDEntity = mWxgdEntity;
        presenterRouter.create(RepairStaffAPI.class).listRepairStaffList(mWXGDEntity.id);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(BaseEvent baseEvent) {
    }

    /**
     * @param
     * @return
     * @description 获取维修人员列表
     * @author zhangwenshuai1 2018/9/6
     */
    public List<RepairStaffEntity> getRepairStaffEntities() {
        if (staffEntities != null) {
            return staffEntities;
        } else {
            return new LinkedList<>();
        }
    }

    /**
     * @param
     * @return
     * @description 更新维修人员列表
     * @author zhangwenshuai1 2018/9/6
     */
    public void updateRepairStaffEntiies(List<RepairStaffEntity> list) {
        if (list == null)
            return;
        this.staffEntities = list;
        if (repairStaffListWidget != null) {
//            repairStaffListWidget.setData(list);
//            repairStaffListWidget.setTotal(list.size());
            repairStaffListWidget.setData(list);
            if (isEditable) {
                repairStaffListWidget.setShowText("编辑 (" + list.size() + ")");
            } else {
                repairStaffListWidget.setShowText("查看 (" + list.size() + ")");
            }
        }
    }


    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }

    public void clear() {
        repairStaffListWidget.clear();
    }
}

