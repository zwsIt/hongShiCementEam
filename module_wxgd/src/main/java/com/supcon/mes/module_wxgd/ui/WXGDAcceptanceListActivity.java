package com.supcon.mes.module_wxgd.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.view.CustomSwipeLayout;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.utils.controllers.DatePickController;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.AcceptanceCheckEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchStaff;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntityDao;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_wxgd.IntentRouter;
import com.supcon.mes.module_wxgd.R;
import com.supcon.mes.module_wxgd.ui.adapter.AcceptanceCheckAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangshizhan on 2018/9/3
 * Email:wangshizhan@supcom.com
 * 验收列表
 */
@Router(Constant.Router.WXGD_ACCEPTANCE_LIST)
public class WXGDAcceptanceListActivity extends BaseRefreshRecyclerActivity<AcceptanceCheckEntity> {

    @BindByTag("contentView")
    protected RecyclerView contentView;

    @BindByTag("leftBtn")
    protected ImageButton leftBtn;

    @BindByTag("rightBtn")
    protected ImageButton rightBtn;

    @BindByTag("titleText")
    protected TextView titleText;

    private AcceptanceCheckAdapter mAcceptanceCheckAdapter;

    protected List<AcceptanceCheckEntity> mEntities = new ArrayList<>();
    protected boolean isEditable = false;
    private int currentPosition;
    private DatePickController mDatePickController;
    private SinglePickController mSinglePickController;

    private List<String> checkResultListStr = new ArrayList<>();
    private List<SystemCodeEntity> checkResultList = new ArrayList<>();
    private List<Long> dgDeletedIds = new ArrayList<>(); //表体删除记录ids

    @Override
    protected int getLayoutID() {
        return R.layout.ac_wxgd_repair_staff_list;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        String entityInfo = intent.getStringExtra(Constant.IntentKey.ACCEPTANCE_ENTITIES);
        if (TextUtils.isEmpty(entityInfo)) {
            finish();
        }
        mEntities.addAll(GsonUtil.jsonToList(entityInfo, AcceptanceCheckEntity.class));

        isEditable = intent.getBooleanExtra(Constant.IntentKey.IS_EDITABLE, false);
//        rightBtn.setVisibility(View.VISIBLE);
        mAcceptanceCheckAdapter.setEditable(isEditable);

        mDatePickController = new DatePickController(this);
        mDatePickController.setCanceledOnTouchOutside(true);
        mDatePickController.setDividerVisible(true);
        mDatePickController.setSecondVisible(true);
        mSinglePickController = new SinglePickController(this);
        mSinglePickController.setDividerVisible(true);
        mSinglePickController.setCanceledOnTouchOutside(true);

        refreshListController.setAutoPullDownRefresh(false);
        refreshListController.setPullDownRefreshEnabled(false);
    }

    @Override
    protected IListAdapter<AcceptanceCheckEntity> createAdapter() {
        mAcceptanceCheckAdapter = new AcceptanceCheckAdapter(context, isEditable);
        return mAcceptanceCheckAdapter;
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText("验收列表");
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(5, context)));
        contentView.addOnItemTouchListener(new CustomSwipeLayout.OnSwipeItemTouchListener(this));

        findViewById(R.id.includeSparePartLy).setVisibility(View.GONE);

        initEmptyView();
        initCheckResult();
    }

    private void initCheckResult() {
        checkResultList = EamApplication.dao().getSystemCodeEntityDao().queryBuilder().where(SystemCodeEntityDao.Properties.EntityCode.eq(Constant.SystemCode.CHECK_RESULT)).list();
        for (SystemCodeEntity entity : checkResultList) {
            checkResultListStr.add(entity.value);
        }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> onBackPressed());
        RxView.clicks(rightBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (mAcceptanceCheckAdapter != null && mAcceptanceCheckAdapter.getList().size() == 0) {
                        AcceptanceCheckEntity acceptanceCheckEntity = new AcceptanceCheckEntity();
                        mEntities.add(acceptanceCheckEntity);
                        refreshListController.refreshComplete(mEntities);
                    } else {
                        SnackbarHelper.showMessage(rootView, "本次工单维修已新增验收数据，无需再次新增！");
                    }
                });
        mAcceptanceCheckAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                AcceptanceCheckEntity acceptanceCheckEntity = (AcceptanceCheckEntity) obj;
                currentPosition = position;
                String tag = (String) childView.getTag();
                switch (tag) {
                    case "acceptanceStaffCode":
                    case "acceptanceStaffName":
                        IntentRouter.go(context, Constant.Router.STAFF);
                        break;
                    case "acceptanceTime":
                        mDatePickController.listener((year, month, day, hour, minute, second) -> {
                            String currentAcceptChkDateTime = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                            acceptanceCheckEntity.checkTime = DateUtil.dateFormat(currentAcceptChkDateTime, "yyyy-MM-dd HH:mm:ss");
                            mAcceptanceCheckAdapter.notifyItemChanged(position);
                        }).show("".equals(acceptanceCheckEntity.checkTime) ? new Date().getTime() : acceptanceCheckEntity.checkTime);
                        break;
                    case "acceptanceResult":
                        mSinglePickController.list(checkResultListStr)
                                .listener((index, item) -> {
                                    acceptanceCheckEntity.checkResult = checkResultList.get(checkResultListStr.indexOf(String.valueOf(item)));
                                    mAcceptanceCheckAdapter.notifyItemChanged(position);
                                }).show(acceptanceCheckEntity.checkResult != null ? acceptanceCheckEntity.checkResult.value : "");
                        break;
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getAcceptChkStaff(CommonSearchEvent event) {
        if (event.commonSearchEntity instanceof CommonSearchStaff) {
            CommonSearchStaff searchStaff = (CommonSearchStaff) event.commonSearchEntity;
            AcceptanceCheckEntity item = mAcceptanceCheckAdapter.getItem(currentPosition);
            Staff staff = new Staff();
            staff.name = searchStaff.name;
            staff.code = searchStaff.code;
            item.checkStaff = staff;
            mAcceptanceCheckAdapter.notifyItemChanged(currentPosition);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshEvent refreshEvent) {
        if (refreshEvent.delId != null) {
            dgDeletedIds.add(refreshEvent.delId);
        }
        refreshListController.refreshComplete(mEntities);
    }

    @Override
    protected void initData() {
        super.initData();
        refreshListController.refreshComplete(mEntities);
    }

    private void initEmptyView() {
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, "暂无数据"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
//        EventBus.getDefault().post(new AcceptanceEvent(mEntities, dgDeletedIds));
        super.onBackPressed();
    }

}
