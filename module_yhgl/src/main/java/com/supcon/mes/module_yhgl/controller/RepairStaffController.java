package com.supcon.mes.module_yhgl.controller;

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
import com.supcon.mes.middleware.model.bean.RepairStaffEntity;
import com.supcon.mes.middleware.model.bean.YHEntity;
import com.supcon.mes.middleware.model.event.BaseEvent;
import com.supcon.mes.module_yhgl.IntentRouter;
import com.supcon.mes.module_yhgl.model.api.RepairStaffAPI;
import com.supcon.mes.module_yhgl.model.bean.RepairStaffListEntity;
import com.supcon.mes.module_yhgl.model.contract.RepairStaffContract;
import com.supcon.mes.module_yhgl.model.event.ListEvent;
import com.supcon.mes.module_yhgl.presenter.RepairStaffPresenter;
import com.supcon.mes.module_yhgl.ui.adapter.RepairStaffAdapter;

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
public class RepairStaffController extends BaseViewController implements RepairStaffContract.View, RepairStaffAPI {
    //    private CustomListWidget<RepairStaffEntity> mCustomListWidget;
    private List<RepairStaffEntity> staffEntities = new ArrayList<>();

    private long id = -1;
    private boolean isEditable;
    private YHEntity mYHEntity;

    @BindByTag("repairStaffListWidget")
    CustomListWidget<RepairStaffEntity> mCustomListWidget;

    public RepairStaffController(View rootView) {
        super(rootView);
    }

    @Override
    public void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        mYHEntity = (YHEntity) ((Activity) context).getIntent().getSerializableExtra(Constant.IntentKey.YHGL_ENTITY);
        if (mYHEntity != null) {
            this.id = mYHEntity.id;
        }
    }

    @Override
    public void initView() {
        super.initView();
        mCustomListWidget.setAdapter(new RepairStaffAdapter(context, false));
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
        if (mCustomListWidget != null) {
//            mCustomListWidget.setData(entity.result);
//            mCustomListWidget.setTotal(entity.result.size());
            mCustomListWidget.setData(entity.result);
            if (isEditable) {
                mCustomListWidget.setShowText("编辑 (" + entity.result.size() + ")");
            } else {
                mCustomListWidget.setShowText("查看 (" + entity.result.size() + ")");
            }
        }
        EventBus.getDefault().post(new ListEvent("repairStaff", staffEntities));
    }

    @Override
    public void initListener() {
        super.initListener();
        mCustomListWidget.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                Bundle bundle = new Bundle();
                switch (action) {
                    case CustomListWidget.ACTION_VIEW_ALL:
                    case 0:
                        bundle.putString(Constant.IntentKey.REPAIR_STAFF_ENTITIES, staffEntities.toString());
                        bundle.putBoolean(Constant.IntentKey.IS_EDITABLE, isEditable);
                        bundle.putBoolean(Constant.IntentKey.IS_ADD, false);

                        if (mYHEntity.pending != null) {
                            bundle.putString(Constant.IntentKey.TABLE_STATUS, mYHEntity.pending.taskDescription);
                        }
                        IntentRouter.go(context, Constant.Router.YHGL_REPAIR_STAFF_LIST, bundle);
                        break;
//                    case CustomListWidget.ACTION_ITEM_DELETE:
//                        break;
//                    case CustomListWidget.ACTION_EDIT:
//                        bundle.putString(Constant.IntentKey.REPAIR_STAFF_ENTITIES, repairStaffListStr);
//                        bundle.putBoolean(Constant.IntentKey.IS_EDITABLE, true);
//                        bundle.putLong(Constant.IntentKey.REPAIR_SUM, mWXGDEntity.repairSum);
//                        bundle.putString(Constant.IntentKey.TABLE_STATUS, mWXGDEntity.pending.taskDescription);
//                        IntentRouter.go(context, Constant.Router.WXGD_REPAIR_STAFF_LIST, bundle);
//                        break;
//                    case CustomListWidget.ACTION_ADD:
//                        bundle.putString(Constant.IntentKey.REPAIR_STAFF_ENTITIES, repairStaffListStr);
//                        bundle.putBoolean(Constant.IntentKey.IS_ADD, true);
//                        bundle.putBoolean(Constant.IntentKey.IS_EDITABLE, true);
//                        bundle.putLong(Constant.IntentKey.REPAIR_SUM, mWXGDEntity.repairSum);
//                        bundle.putString(Constant.IntentKey.TABLE_STATUS, mWXGDEntity.pending.taskDescription);
//                        IntentRouter.go(context, Constant.Router.WXGD_REPAIR_STAFF_LIST, bundle);
//                        break;
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
        this.mCustomListWidget = customListWidget;
    }


    @Override
    public void listRepairStaffList(long id) {
        presenterRouter.create(RepairStaffAPI.class).listRepairStaffList(id);
    }

    @Override
    public void initData() {
        super.initData();
        presenterRouter.create(RepairStaffAPI.class).listRepairStaffList(id);
    }

    public void setYHEntity(YHEntity mYHEntity) {
        this.mYHEntity = mYHEntity;
        this.id = mYHEntity.id;
        presenterRouter.create(RepairStaffAPI.class).listRepairStaffList(id);
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
        if (mCustomListWidget != null) {
//            mCustomListWidget.setData(list);
//            mCustomListWidget.setTotal(list.size());
            mCustomListWidget.setData(list);
            if (isEditable) {
                mCustomListWidget.setShowText("编辑 (" + list.size() + ")");
            } else {
                mCustomListWidget.setShowText("查看 (" + list.size() + ")");
            }
        }
    }


    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }

    public void clear() {
        mCustomListWidget.clear();
    }
}

