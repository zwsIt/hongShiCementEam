package com.supcon.mes.module_yhgl.ui;

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
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.JWXItem;
import com.supcon.mes.middleware.model.bean.LubricateOilsEntity;
import com.supcon.mes.middleware.model.bean.RefLubricateEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntityDao;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.module_yhgl.IntentRouter;
import com.supcon.mes.module_yhgl.R;
import com.supcon.mes.module_yhgl.model.event.LubricateOilsEvent;
import com.supcon.mes.module_yhgl.ui.adapter.LubricateOilsAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * Created by wangshizhan on 2018/9/3
 * Email:wangshizhan@supcom.com
 */
@Router(Constant.Router.YHGL_LUBRICATE_OIL_LIST)
public class YHGLLubricateOilListActivity extends BaseRefreshRecyclerActivity<LubricateOilsEntity> {

    @BindByTag("contentView")
    protected RecyclerView contentView;

    @BindByTag("leftBtn")
    protected ImageButton leftBtn;

    @BindByTag("rightBtn")
    protected ImageButton rightBtn;

    @BindByTag("refBtn")
    protected ImageButton refBtn;

    @BindByTag("titleText")
    protected TextView titleText;

    private LubricateOilsAdapter mLubricateOilsAdapter;

    protected List<LubricateOilsEntity> mEntities = new ArrayList<>();
    protected boolean editable = false, isAdd = false;
    protected CustomDialog mCustomDialog;
    private String tableStatus;
    private SinglePickController mSinglePickController;
    private List<SystemCodeEntity> oilTypeList = new ArrayList<>();  //加换油
    private List<String> oilTypeListStr = new ArrayList<>();
    private List<Long> dgDeletedIds = new ArrayList<>(); //表体删除记录ids
    private Long eamID;

    @Override
    protected int getLayoutID() {
        return R.layout.ac_yhgl_repair_staff_list;
    }

    @Override
    protected void onInit() {
        super.onInit();

        EventBus.getDefault().register(context);
        editable = getIntent().getBooleanExtra(Constant.IntentKey.IS_EDITABLE, false);
        isAdd = getIntent().getBooleanExtra(Constant.IntentKey.IS_ADD, false);
        if (isAdd) {
            IntentRouter.go(context, Constant.Router.LUBRICATE_REF);
        }
        tableStatus = getIntent().getStringExtra(Constant.IntentKey.TABLE_STATUS);
        mLubricateOilsAdapter.setEditable(editable);
        mLubricateOilsAdapter.setTableStatus(tableStatus);
        String entityInfo = getIntent().getStringExtra(Constant.IntentKey.LUBRICATE_OIL_ENTITIES);
        eamID = getIntent().getLongExtra(Constant.IntentKey.EAM_ID, 0);
        if (TextUtils.isEmpty(entityInfo)) {
            finish();
        }
        mEntities.addAll(GsonUtil.jsonToList(entityInfo, LubricateOilsEntity.class));

        mSinglePickController = new SinglePickController(this);
        mSinglePickController.setCanceledOnTouchOutside(true);
        mSinglePickController.setDividerVisible(true);

        refreshListController.setAutoPullDownRefresh(false);
        refreshListController.setPullDownRefreshEnabled(false);

        oilTypeList = EamApplication.dao().getSystemCodeEntityDao().queryBuilder().where(SystemCodeEntityDao.Properties.EntityCode.eq(Constant.SystemCode.OIL_TYPE)).list();
        for (SystemCodeEntity systemCodeEntity : oilTypeList) {
            oilTypeListStr.add(systemCodeEntity.value);
        }
    }

    @Override
    protected IListAdapter<LubricateOilsEntity> createAdapter() {
        mLubricateOilsAdapter = new LubricateOilsAdapter(context, editable);
        return mLubricateOilsAdapter;
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText("润滑列表");
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(5, context)));
        contentView.addOnItemTouchListener(new CustomSwipeLayout.OnSwipeItemTouchListener(this));
        if (editable) {
            rightBtn.setVisibility(View.VISIBLE);
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
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        onBackPressed();
                    }
                });

        RxView.clicks(rightBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    IntentRouter.go(context, Constant.Router.LUBRICATE_REF);
                });

        RxView.clicks(refBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (eamID == 0) {
                        ToastUtils.show(context, "请选择设备！");
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Constant.IntentKey.IS_SPARE_PART_REF, true);
                    bundle.putLong(Constant.IntentKey.EAM_ID, eamID);
                    IntentRouter.go(context, Constant.Router.LUBRICATE_REF, bundle);
                });

        mLubricateOilsAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            String tag = (String) childView.getTag();
            LubricateOilsEntity lubricateOilsEntity = (LubricateOilsEntity) obj;
            switch (tag) {
                case "oilType":
                    if (action == -1) {
                        lubricateOilsEntity.oilType = null;
                        mLubricateOilsAdapter.notifyItemChanged(position);
                    } else {
                        mSinglePickController.list(oilTypeListStr)
                                .listener((index, item) -> {
                                    lubricateOilsEntity.oilType = oilTypeList.get(index);
                                    mLubricateOilsAdapter.notifyItemChanged(position);
                                }).show((lubricateOilsEntity.oilType == null || lubricateOilsEntity.oilType.id == null) ? "" : lubricateOilsEntity.oilType.value);
                    }
                    break;
                default:
                    break;
            }
        });

    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(new LubricateOilsEvent(mEntities, dgDeletedIds));
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
    public void addLuricateOil(RefLubricateEntity refLubricateEntity) {

        for (LubricateOilsEntity lubricateOilsEntity : mEntities) {
            // 润滑参照判断重复，非参照添加无判断
            if (lubricateOilsEntity.id != null && lubricateOilsEntity.id.equals(refLubricateEntity.id)) {
                ToastUtils.show(context, "请勿重复添加润滑油!");
                refreshListController.refreshComplete(mEntities);
                return;
            }
        }

        LubricateOilsEntity lubricateOilsEntity = new LubricateOilsEntity();
        lubricateOilsEntity.lubricate = refLubricateEntity.lubricateOil;
        lubricateOilsEntity.id = refLubricateEntity.id;
        lubricateOilsEntity.oilQuantity = refLubricateEntity.sum;
        lubricateOilsEntity.remark = refLubricateEntity.remark;
        lubricateOilsEntity.lubricatingPart = refLubricateEntity.lubricatePart;
        lubricateOilsEntity.oilType = refLubricateEntity.oilType;
        JWXItem jwxItem = new JWXItem();
        jwxItem.accessoryEamId = refLubricateEntity.accessoryEamId;
        jwxItem.claim = refLubricateEntity.claim;
        jwxItem.content = refLubricateEntity.content;
        jwxItem.id = refLubricateEntity.id;
        jwxItem.lastDuration = refLubricateEntity.lastDuration;
        jwxItem.nextDuration = refLubricateEntity.nextDuration;
        jwxItem.lastTime = refLubricateEntity.lastTime;
        jwxItem.nextTime = refLubricateEntity.nextTime;
        jwxItem.period = refLubricateEntity.period;
        jwxItem.periodType = refLubricateEntity.periodType;
        jwxItem.periodUnit = refLubricateEntity.periodUnit;
        jwxItem.sparePartId = refLubricateEntity.sparePartId;
        lubricateOilsEntity.jwxItemID = jwxItem;

        mEntities.add(lubricateOilsEntity);
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
