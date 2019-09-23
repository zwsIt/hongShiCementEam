package com.supcon.mes.module_wxgd.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.CustomSwipeLayout;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.JWXItem;
import com.supcon.mes.middleware.model.bean.MaintainEntity;
import com.supcon.mes.middleware.model.bean.RefMaintainEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.module_wxgd.IntentRouter;
import com.supcon.mes.module_wxgd.R;
import com.supcon.mes.module_wxgd.model.event.MaintenanceEvent;
import com.supcon.mes.module_wxgd.ui.adapter.MaintenanceAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangshizhan on 2018/9/3
 * Email:wangshizhan@supcom.com
 * 维保人员列表
 */
@Router(Constant.Router.WXGD_MAINTENANCE_STAFF_LIST)
public class WXGDMaintenanceListActivity extends BaseRefreshRecyclerActivity<MaintainEntity> {

    @BindByTag("contentView")
    protected RecyclerView contentView;

    @BindByTag("leftBtn")
    protected ImageButton leftBtn;

    @BindByTag("refBtn")
    protected ImageButton refBtn;

    @BindByTag("titleText")
    protected TextView titleText;

    private MaintenanceAdapter maintenanceAdapter;

    protected List<MaintainEntity> mEntities = new ArrayList<>();
    protected boolean editable;
    private String tableStatus;
    private List<Long> dgDeletedIds = new ArrayList<>(); //表体删除记录ids
    private Long eamID;

    @Override
    protected int getLayoutID() {
        return R.layout.ac_wxgd_repair_staff_list;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(context);
        editable = getIntent().getBooleanExtra(Constant.IntentKey.IS_EDITABLE, false);
        eamID = getIntent().getLongExtra(Constant.IntentKey.EAM_ID, 0);
        tableStatus = getIntent().getStringExtra(Constant.IntentKey.TABLE_STATUS);
        maintenanceAdapter.setEditable(editable);
        maintenanceAdapter.setTableStatus(tableStatus);
        String maintenance = getIntent().getStringExtra(Constant.IntentKey.MAINTENANCE_ENTITIES);
        if (TextUtils.isEmpty(maintenance)) {
            finish();
        }
        mEntities.addAll(GsonUtil.jsonToList(maintenance, MaintainEntity.class));

        refreshListController.setAutoPullDownRefresh(false);
        refreshListController.setPullDownRefreshEnabled(false);
    }

    @Override
    protected IListAdapter<MaintainEntity> createAdapter() {
        maintenanceAdapter = new MaintenanceAdapter(context, editable);
        return maintenanceAdapter;
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText("维保列表");
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(5, context)));
        contentView.addOnItemTouchListener(new CustomSwipeLayout.OnSwipeItemTouchListener(this));
        if (editable) {
            refBtn.setVisibility(View.VISIBLE);
        }
        findViewById(R.id.includeSparePartLy).setVisibility(View.GONE);

        initEmptyView();

    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> onBackPressed());
        RxView.clicks(refBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    Bundle bundle = new Bundle();
                    bundle.putLong(Constant.IntentKey.EAM_ID, eamID);
                    IntentRouter.go(context, Constant.Router.MAINTAIN_REF, bundle);
                });

    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(new MaintenanceEvent(mEntities, dgDeletedIds));
        super.onBackPressed();
    }

    @Override
    protected void initData() {
        super.initData();
        refreshListController.refreshComplete(mEntities);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(context);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addMaintenance(RefMaintainEntity refMaintainEntity) {
        for (MaintainEntity maintainEntity : mEntities) {
            if (maintainEntity.getJwxItem().id != null && refMaintainEntity.id != null) {
                if (maintainEntity.getJwxItem().id.equals(refMaintainEntity.id)) {
                    ToastUtils.show(context, "当前维保业务已存在，请勿重复添加!");
                    refreshListController.refreshComplete(mEntities);
                    return;
                }
            }
        }
        MaintainEntity maintainEntity = new MaintainEntity();
        JWXItem jwxItem = new JWXItem();
        jwxItem.accessoryEamId = refMaintainEntity.accessoryEamId;
        jwxItem.claim = refMaintainEntity.claim;
        jwxItem.content = refMaintainEntity.content;
        jwxItem.id = refMaintainEntity.id;
        jwxItem.lastDuration = refMaintainEntity.lastDuration;
        jwxItem.nextDuration = refMaintainEntity.nextDuration;
        jwxItem.lastTime = refMaintainEntity.lastTime;
        jwxItem.nextTime = refMaintainEntity.nextTime;
        jwxItem.period = refMaintainEntity.period;
        jwxItem.periodType = refMaintainEntity.periodType;
        jwxItem.periodUnit = refMaintainEntity.periodUnit;
        jwxItem.sparePartId = refMaintainEntity.sparePartId;
        maintainEntity.jwxItemID = jwxItem;
        mEntities.add(maintainEntity);
        refreshListController.refreshComplete(mEntities);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshEvent refreshEvent) {
        if (refreshEvent.delId != null) {
            dgDeletedIds.add(refreshEvent.delId);
        }
        refreshListController.refreshComplete(mEntities);
    }

    private void initEmptyView() {
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, "暂无数据"));
    }

}
