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
import com.supcon.mes.middleware.model.bean.MaintainEntity;
import com.supcon.mes.middleware.model.bean.WXGDEam;
import com.supcon.mes.middleware.model.bean.YHEntity;
import com.supcon.mes.module_yhgl.IntentRouter;
import com.supcon.mes.module_yhgl.model.api.MaintenanceAPI;
import com.supcon.mes.module_yhgl.model.bean.MaintenanceListEntity;
import com.supcon.mes.module_yhgl.model.contract.MaintenanceContract;
import com.supcon.mes.module_yhgl.model.event.ListEvent;
import com.supcon.mes.module_yhgl.presenter.MaintenancePresenter;
import com.supcon.mes.module_yhgl.ui.adapter.MaintenanceAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangshizhan on 2018/8/28
 * Email:wangshizhan@supcom.com
 */
@Presenter(MaintenancePresenter.class)
public class MaintenanceController extends BaseViewController implements MaintenanceContract.View, MaintenanceAPI {
    @BindByTag("maintenanceListWidget")
    CustomListWidget<MaintainEntity> mCustomListWidget;

    private long id = -1;
    private List<MaintainEntity> maintenanceEntities = new ArrayList<>();
    private boolean isEditable;
    private YHEntity mYHEntity;

    public MaintenanceController(View rootView) {
        super(rootView);
    }

    @Override
    public void onInit() {
        super.onInit();
        mYHEntity = (YHEntity) ((Activity) context).getIntent().getSerializableExtra(Constant.IntentKey.YHGL_ENTITY);
        if (mYHEntity != null) {
            this.id = mYHEntity.id;
        }
    }

    @Override
    public void initView() {
        super.initView();
        mCustomListWidget.setAdapter(new MaintenanceAdapter(context, false));
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
                        bundle.putString(Constant.IntentKey.MAINTENANCE_ENTITIES, maintenanceEntities.toString());
                        bundle.putBoolean(Constant.IntentKey.IS_EDITABLE, isEditable);
                        bundle.putBoolean(Constant.IntentKey.IS_ADD, false);
                        if (mYHEntity.pending != null) {
                            bundle.putString(Constant.IntentKey.TABLE_STATUS, mYHEntity.pending.taskDescription);
                        }
                        if (mYHEntity.getEamID().id != null) {
                            bundle.putLong(Constant.IntentKey.EAM_ID, mYHEntity.getEamID().id);
                        }
                        IntentRouter.go(context, Constant.Router.YHGL_MAINTENANCE_STAFF_LIST, bundle);
                        break;
                    default:
                        break;
                }

            }
        });
    }

    @Override
    public void listMaintenance(long id) {
        presenterRouter.create(MaintenanceAPI.class).listMaintenance(id);
    }

    @Override
    public void listMaintenanceSuccess(MaintenanceListEntity entity) {
        maintenanceEntities = entity.result;
        if (mCustomListWidget != null) {
            mCustomListWidget.setData(entity.result);
            if (isEditable) {
                mCustomListWidget.setShowText("编辑 (" + entity.result.size() + ")");
            } else {
                mCustomListWidget.setShowText("查看 (" + entity.result.size() + ")");
            }
        }
        EventBus.getDefault().post(new ListEvent("maintenance", maintenanceEntities));
    }

    @Override
    public void listMaintenanceFailed(String errorMsg) {
        LogUtil.e("LubricateOilsController listLubricateOilsListFailed:" + errorMsg);
    }

    public void setCustomListWidget(CustomListWidget<MaintainEntity> customListWidget) {
        this.mCustomListWidget = customListWidget;
    }

    @Override
    public void initData() {
        super.initData();
        presenterRouter.create(MaintenanceAPI.class).listMaintenance(id);
    }

    public void setYHEntity(YHEntity mYHEntity) {
        this.mYHEntity = mYHEntity;
        this.id = mYHEntity.id;
        presenterRouter.create(MaintenanceAPI.class).listMaintenance(id);
    }

    /**
     * @param
     * @return
     * @description 获取维保列表
     * @author zhangwenshuai1 2018/9/6
     */
    public List<MaintainEntity> getMaintenanceEntities() {
        if (maintenanceEntities == null) {
            return new ArrayList<>();
        }
        return maintenanceEntities;
    }

    /**
     * @param
     * @return
     * @description 更新维保列表
     * @author zhangwenshuai1 2018/9/6
     */
    public void updateMaintenanceEntities(List<MaintainEntity> list) {
        if (list == null)
            return;
        this.maintenanceEntities = list;
        if (mCustomListWidget != null) {
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

    public void upEam(WXGDEam wxgdEam) {
        mYHEntity.eamID = wxgdEam;
    }
}
