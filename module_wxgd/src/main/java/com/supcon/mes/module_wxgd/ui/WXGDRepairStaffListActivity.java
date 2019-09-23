package com.supcon.mes.module_wxgd.ui;

import android.annotation.SuppressLint;
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
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.utils.controllers.DatePickController;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonSearchStaff;
import com.supcon.mes.middleware.model.bean.RepairStaffEntity;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.module_wxgd.IntentRouter;
import com.supcon.mes.module_wxgd.R;
import com.supcon.mes.module_wxgd.model.event.RepairStaffEvent;
import com.supcon.mes.module_wxgd.ui.adapter.RepairStaffAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * Created by wangshizhan on 2018/9/3
 * Email:wangshizhan@supcom.com
 * 维修人员列表
 */
@Router(Constant.Router.WXGD_REPAIR_STAFF_LIST)
public class WXGDRepairStaffListActivity extends BaseRefreshRecyclerActivity<RepairStaffEntity> {

    @BindByTag("contentView")
    protected RecyclerView contentView;

    @BindByTag("leftBtn")
    protected ImageButton leftBtn;

    @BindByTag("rightBtn")
    protected ImageButton rightBtn;

    @BindByTag("titleText")
    protected TextView titleText;

    private RepairStaffAdapter mRepairStaffAdapter;
    private DatePickController mDatePickController;

    protected List<RepairStaffEntity> mEntities = new ArrayList<>();
    protected boolean editable, isAdd;
    private long repairSum; // 工单执行次数
    private String tableStatus;
    private List<Long> dgDeletedIds = new ArrayList<>(); //表体删除记录ids

    @Override
    protected int getLayoutID() {
        return R.layout.ac_wxgd_repair_staff_list;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(context);
        editable = getIntent().getBooleanExtra(Constant.IntentKey.IS_EDITABLE, false);
        isAdd = getIntent().getBooleanExtra(Constant.IntentKey.IS_ADD, false);
        if (isAdd) {
        IntentRouter.go(context, Constant.Router.STAFF);
    }
        repairSum = getIntent().getLongExtra(Constant.IntentKey.REPAIR_SUM, 1);
        tableStatus = getIntent().getStringExtra(Constant.IntentKey.TABLE_STATUS);
        mRepairStaffAdapter.setEditable(editable);
        mRepairStaffAdapter.setRepairSum((int) repairSum);
        mRepairStaffAdapter.setTableStatus(tableStatus);
        String staffInfo = getIntent().getStringExtra(Constant.IntentKey.REPAIR_STAFF_ENTITIES);
        if (TextUtils.isEmpty(staffInfo)) {
            finish();
        }
        mEntities.addAll(GsonUtil.jsonToList(staffInfo, RepairStaffEntity.class));

        mDatePickController = new DatePickController(this);
        mDatePickController.setCanceledOnTouchOutside(true);
        mDatePickController.setDividerVisible(true);
        mDatePickController.setSecondVisible(true);

        refreshListController.setAutoPullDownRefresh(false);
        refreshListController.setPullDownRefreshEnabled(false);
    }

    @Override
    protected IListAdapter<RepairStaffEntity> createAdapter() {
        mRepairStaffAdapter = new RepairStaffAdapter(context, editable);
        return mRepairStaffAdapter;
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText("维修人员列表");
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(5, context)));
        contentView.addOnItemTouchListener(new CustomSwipeLayout.OnSwipeItemTouchListener(this));
        if (editable) {
            rightBtn.setVisibility(View.VISIBLE);
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
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        IntentRouter.go(context, Constant.Router.STAFF);
                    }
                });

        mRepairStaffAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            String tag = (String) childView.getTag();
            RepairStaffEntity repairStaffEntity = (RepairStaffEntity) obj;
            switch (tag) {
                case "actualStartTime":
                    if (action == -1) {
                        repairStaffEntity.startTime = null;
                        mRepairStaffAdapter.notifyItemChanged(position);
                    } else {
                        mDatePickController.listener((year, month, day, hour, minute, second) -> {
                            String dateTimeStr = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                            repairStaffEntity.startTime = DateUtil.dateFormat(dateTimeStr, "yyyy-MM-dd HH:mm:ss");

                            if (repairStaffEntity.endTime != null) {
                                //判断大小
                                if (repairStaffEntity.startTime > repairStaffEntity.endTime) {
                                    ToastUtils.show(context, "实际开始时间不能大于实际结束时间", 3000);
                                    return;
                                }
                                //计算工时
                                repairStaffEntity.workHour = new BigDecimal((repairStaffEntity.endTime - repairStaffEntity.startTime) / (60 * 60 * 1000.00)).setScale(2, BigDecimal.ROUND_HALF_UP);
                            }
                            mRepairStaffAdapter.notifyItemChanged(position);
                        }).show(repairStaffEntity.startTime == null ? new Date().getTime() : repairStaffEntity.startTime);
                    }

                    break;
                case "actualEndTime":
                    if (action == -1) {
                        repairStaffEntity.endTime = null;
                        mRepairStaffAdapter.notifyItemChanged(position);
                    } else {
                        mDatePickController.listener((year, month, day, hour, minute, second) -> {
                            String dateTimeStr = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                            repairStaffEntity.endTime = DateUtil.dateFormat(dateTimeStr, "yyyy-MM-dd HH:mm:ss");

                            if (repairStaffEntity.startTime != null) {
                                //判断大小
                                if (repairStaffEntity.endTime < repairStaffEntity.startTime) {
                                    ToastUtils.show(context, "实际结束时间不能小于实际开始时间", 3000);
                                    return;
                                }
                                //计算工时
                                repairStaffEntity.workHour = new BigDecimal((repairStaffEntity.endTime - repairStaffEntity.startTime) / (60 * 60 * 1000.00)).setScale(2, BigDecimal.ROUND_HALF_UP);
                            }
                            mRepairStaffAdapter.notifyItemChanged(position);
                        }).show(repairStaffEntity.endTime == null ? new Date().getTime() : repairStaffEntity.endTime);
                    }
                    break;
                default:
                    break;
            }
        });

    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(new RepairStaffEvent(mEntities, dgDeletedIds));
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
    public void addStaff(CommonSearchEvent commonSearchEvent) {
        if (!(commonSearchEvent.commonSearchEntity instanceof CommonSearchStaff)) {
            return;
        }
        CommonSearchStaff searchStaff = (CommonSearchStaff) commonSearchEvent.commonSearchEntity;

        for (RepairStaffEntity repairStaffEntity : mEntities) {
            if (repairStaffEntity.repairStaff != null && repairStaffEntity.timesNum != null && repairStaffEntity.timesNum >= repairSum) {
                if (repairStaffEntity.repairStaff.id.equals(searchStaff.id)) {
                    ToastUtils.show(context, "请勿重复添加人员!");
                    refreshListController.refreshComplete(mEntities);
                    return;
                }
            }
        }

        RepairStaffEntity repairStaffEntity = new RepairStaffEntity();
        repairStaffEntity.repairStaff = new Staff();
        repairStaffEntity.repairStaff.id = searchStaff.id;
        repairStaffEntity.repairStaff.code = searchStaff.code;
        repairStaffEntity.repairStaff.name = searchStaff.name;
        repairStaffEntity.timesNum = (int) repairSum;

        //实际开始/结束时间根据列表最后一项赋值
        if (mEntities != null && mEntities.size() > 0) {
            RepairStaffEntity entity = mEntities.get(mEntities.size() - 1);
            repairStaffEntity.startTime = entity.startTime;
            repairStaffEntity.endTime = entity.endTime;
            repairStaffEntity.workHour = entity.workHour;
        }
        mRepairStaffAdapter.setRepairSum((int) repairSum);
        mEntities.add(repairStaffEntity);
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
