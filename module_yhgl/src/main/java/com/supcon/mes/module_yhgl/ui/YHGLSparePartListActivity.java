package com.supcon.mes.module_yhgl.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.CustomSwipeLayout;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.Good;
import com.supcon.mes.middleware.model.bean.ResultEntity;
import com.supcon.mes.middleware.model.bean.SparePartEntity;
import com.supcon.mes.middleware.model.bean.SparePartRefEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.event.SparePartAddEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.SystemCodeManager;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_yhgl.IntentRouter;
import com.supcon.mes.module_yhgl.R;
import com.supcon.mes.module_yhgl.model.api.SparePartAPI;
import com.supcon.mes.module_yhgl.model.api.SparePartListAPI;
import com.supcon.mes.module_yhgl.model.bean.SparePartJsonEntity;
import com.supcon.mes.module_yhgl.model.bean.SparePartListEntity;
import com.supcon.mes.module_yhgl.model.bean.StandingCropResultEntity;
import com.supcon.mes.module_yhgl.model.contract.SparePartContract;
import com.supcon.mes.module_yhgl.model.contract.SparePartListContract;
import com.supcon.mes.module_yhgl.model.dto.GoodDto;
import com.supcon.mes.module_yhgl.model.event.SparePartEvent;
import com.supcon.mes.module_yhgl.model.event.VersionRefreshEvent;
import com.supcon.mes.module_yhgl.presenter.SparePartListPresenter;
import com.supcon.mes.module_yhgl.presenter.SparePartPresenter;
import com.supcon.mes.module_yhgl.ui.adapter.SparePartAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * Created by wangshizhan on 2018/9/3
 * Email:wangshizhan@supcom.com
 * 备件列表
 */
@Router(Constant.Router.YHGL_SPARE_PART_LIST)
@Presenter(value = {SparePartListPresenter.class, SparePartPresenter.class})
public class YHGLSparePartListActivity extends BaseRefreshRecyclerActivity<SparePartEntity> implements SparePartListContract.View, SparePartContract.View {

    @BindByTag("contentView")
    protected RecyclerView contentView;

    @BindByTag("leftBtn")
    protected ImageButton leftBtn;

    @BindByTag("refBtn")
    protected ImageButton refBtn;

    @BindByTag("rightBtn")
    protected ImageButton rightBtn;

    @BindByTag("titleText")
    protected TextView titleText;

    @BindByTag("updateStandingCropBtn")
    Button updateStandingCropBtn;
    @BindByTag("deliveryOutStorageBtn")
    Button deliveryOutStorageBtn;

    private SparePartAdapter mSparePartAdapter;

    private List<SparePartEntity> mSparePartEntityList = new LinkedList();

    protected boolean editable = false, isAdd = false;
    private String tableStatus;
    private List<Long> dgDeletedIds = new ArrayList<>(); //表体删除记录ids
    private List<SparePartEntity> selectedEntityList = new ArrayList<>(); // 选中备件List
    private String tableAction; // 单据状态Url
    private boolean allUpdateFlag; // 全部备件更新现存量
    private Long workListId; // 工单id
    private Long eamID;

    @Override
    protected int getLayoutID() {
        return R.layout.ac_yhgl_repair_staff_list;
    }

    @Override
    protected void onInit() {
        super.onInit();

        EventBus.getDefault().register(context);
        editable = getIntent().getBooleanExtra(Constant.IntentKey.IS_EDITABLE, false);//放在onInit()中会存在迟于创建Adapter
        isAdd = getIntent().getBooleanExtra(Constant.IntentKey.IS_ADD, false);

        if (isAdd) {
            IntentRouter.go(context, Constant.Router.SPARE_PART_REF);
        }
        tableStatus = getIntent().getStringExtra(Constant.IntentKey.TABLE_STATUS);
        tableAction = getIntent().getStringExtra(Constant.IntentKey.TABLE_ACTION);
        workListId = getIntent().getLongExtra(Constant.IntentKey.LIST_ID, 0);
        eamID = getIntent().getLongExtra(Constant.IntentKey.EAM_ID, 0);
        mSparePartAdapter.setEditable(editable);
        mSparePartAdapter.setTableStatus(tableStatus);
        mSparePartAdapter.setTableAction(tableAction);
        String entityInfo = getIntent().getStringExtra(Constant.IntentKey.SPARE_PART_ENTITIES);
        mSparePartEntityList.addAll(GsonUtil.jsonToList(entityInfo, SparePartEntity.class));

        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
    }

    @Override
    protected IListAdapter<SparePartEntity> createAdapter() {

        mSparePartAdapter = new SparePartAdapter(context, editable);
        return mSparePartAdapter;
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText("备件列表");
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(5, context)));
        contentView.addOnItemTouchListener(new CustomSwipeLayout.OnSwipeItemTouchListener(context));
        if (editable) {
            rightBtn.setVisibility(View.VISIBLE);
            refBtn.setVisibility(View.VISIBLE);
        }
        //接单、验收环节隐藏更新现存量及生成领用出库单按钮行
        if (Constant.WxgdView.RECEIVE_OPEN_URL.equals(tableAction) || Constant.WxgdView.ACCEPTANCE_OPEN_URL.equals(tableAction)) {
            findViewById(R.id.includeSparePartLy).setVisibility(View.GONE);
        }

        deliveryOutStorageBtn.setVisibility(View.GONE);// PC未实现 暂隐藏

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
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(Constant.IntentKey.IS_SPARE_PART_REF, false);
                        IntentRouter.go(context, Constant.Router.SPARE_PART_REF, bundle);
                    }
                });
        RxView.clicks(refBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (eamID == 0) {
                            ToastUtils.show(context, "请选择设备！");
                            return;
                        }
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(Constant.IntentKey.IS_SPARE_PART_REF, true);
                        bundle.putLong(Constant.IntentKey.EAM_ID, eamID);
                        IntentRouter.go(context, Constant.Router.SPARE_PART_REF, bundle);
                    }
                });

        refreshListController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenterRouter.create(SparePartAPI.class).listSparePartList(workListId);
            }
        });

        mSparePartAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                String tag = (String) childView.getTag();
                SparePartEntity sparePartEntity = (SparePartEntity) obj;
                switch (tag) {
                    case "chkBox":
                        if (action == 1) { // chkBox true
                            sparePartEntity.isDeliveried = true;
                            selectedEntityList.add(sparePartEntity);
                        } else { // chkBox false
                            sparePartEntity.isDeliveried = false;
                            selectedEntityList.remove(sparePartEntity);
                        }
//                        mSparePartAdapter.notifyItemChanged(position);
                        break;
                    default:
                        break;
                }
            }
        });

        updateStandingCropBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSparePartEntityList.size() <= 0) {
                    ToastUtils.show(context, "无备件可更新", 2500);
                    return;
                }

                StringBuilder sb = new StringBuilder();
                for (SparePartEntity sparePartEntity : selectedEntityList) {
                    if (sparePartEntity.productID == null) {
                        ToastUtils.show(context, "请去除无备件名称的项", 2500);
                        return;
                    }
                    sb.append(sparePartEntity.productID.productCode).append(",");
                }
                if (TextUtils.isEmpty(sb.toString())) {
                    //表体全部更新
                    for (SparePartEntity sparePartEntity : mSparePartEntityList) {
                        if (sparePartEntity.productID != null) {
                            sb.append(sparePartEntity.productID.productCode).append(",");
                            allUpdateFlag = true;
                        }
                    }
                }
                onLoading("现存量更新中...");
                presenterRouter.create(SparePartListAPI.class).updateStandingCrop(sb.substring(0, sb.length() - 1));
            }
        });
        deliveryOutStorageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSparePartEntityList.size() <= 0) {
                    ToastUtils.show(context, "列表无备件", 2500);
                    return;
                }
                if (selectedEntityList.size() <= 0) {
                    ToastUtils.show(context, "请选择需要的备件生成领用出库单", 2500);
                    return;
                }
                for (SparePartEntity sparePartEntity : selectedEntityList) {
                    if (sparePartEntity.productID == null) {
                        ToastUtils.show(context, "请去除无备件名称的项", 2500);
                        return;
                    }
                    if (sparePartEntity.sum == null || new BigDecimal(0).compareTo(sparePartEntity.sum) >= 0) {
                        ToastUtils.show(context, sparePartEntity.productID.productName + "的计划领用量不允许为空或小于0！", 2000);
                        return;
                    }
                }
//                EventBus.getDefault().post(selectedEntityList);
//                EventBus.getDefault().post(new SparePartEvent(mSparePartEntityList, dgDeletedIds,true));
//                finish();

                onLoading("备件生成领用出库单中...");
                presenterRouter.create(SparePartListAPI.class).generateSparePartApply(generateSparePartApplyList(mSparePartEntityList));

            }
        });

    }

    private String generateSparePartApplyList(List<SparePartEntity> selectedEntityList) {
        List<SparePartJsonEntity> list = new ArrayList<>();
        SparePartJsonEntity sparePartJsonEntity;
        GoodDto goodDto;
        SystemCodeEntity useState;
        SparePartJsonEntity.WorkListBean workListBean;
        for (SparePartEntity sparePartEntity : selectedEntityList) {
            sparePartJsonEntity = new SparePartJsonEntity();
            sparePartJsonEntity.setId(sparePartEntity.id == null ? "" : String.valueOf(sparePartEntity.id));
            sparePartJsonEntity.setSum(sparePartEntity.sum == null ? "" : String.valueOf(sparePartEntity.sum));
            sparePartJsonEntity.setUseQuantity(sparePartEntity.useQuantity == null ? "" : String.valueOf(sparePartEntity.useQuantity));
            sparePartJsonEntity.setActualQuantity(sparePartEntity.actualQuantity == null ? "" : String.valueOf(sparePartEntity.actualQuantity));
            sparePartJsonEntity.setStandingCrop(sparePartEntity.standingCrop == null ? "" : String.valueOf(sparePartEntity.standingCrop));
            sparePartJsonEntity.setRemark(sparePartEntity.remark);
            sparePartJsonEntity.setSparePartId(sparePartEntity.sparePartId == null ? "" : String.valueOf(sparePartEntity.sparePartId));

            goodDto = new GoodDto();
            goodDto.id = String.valueOf(sparePartEntity.productID.id);
            sparePartJsonEntity.setProductID(goodDto);

            useState = new SystemCodeEntity();
            useState.id = sparePartEntity.isDeliveried ? Constant.SparePartUseStatus.PRE_USE : Util.strFormat2(sparePartEntity.useState.id);
            sparePartJsonEntity.setUseState(useState);

            workListBean = new SparePartJsonEntity.WorkListBean();
            workListBean.setId(String.valueOf(workListId));
            sparePartJsonEntity.setWorkList(workListBean);

            list.add(sparePartJsonEntity);
        }
        return list.toString();
    }

    @Override
    public void onBackPressed() {
        for (SparePartEntity sparePartEntity : mSparePartEntityList) {
            if (sparePartEntity.isDeliveried) {
                sparePartEntity.isDeliveried = false;
            }
        }
        EventBus.getDefault().post(new SparePartEvent(mSparePartEntityList, dgDeletedIds));
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(context);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addSparePart(SparePartAddEvent sparePartAddEvent) {
        SparePartRefEntity sparePartRefEntity = sparePartAddEvent.getSparePartRefEntity();
        Good good = sparePartRefEntity.getProductID();
        for (SparePartEntity sparePartEntity : mSparePartEntityList) {
            if (sparePartEntity.productID != null) {
                if (sparePartEntity.productID.id.equals(good.id)) {
                    ToastUtils.show(context, "请勿重复添加备件!");
                    refreshListController.refreshComplete(mSparePartEntityList);
                    return;
                }
            }
        }
        SparePartEntity sparePartEntity = new SparePartEntity();
        sparePartEntity.productID = good;
        sparePartEntity.sum = BigDecimal.valueOf(1);
        sparePartEntity.useState = SystemCodeManager.getInstance().getSystemCodeEntity("BEAM2011/01");
        sparePartEntity.isRef = sparePartAddEvent.isFlag();
        sparePartEntity.lastDuration = sparePartRefEntity.getLastDuration();
        sparePartEntity.lastTime = sparePartRefEntity.getLastTime();
        sparePartEntity.nextDuration = sparePartRefEntity.getNextDuration();
        sparePartEntity.nextTime = sparePartRefEntity.getNextTime();
        sparePartEntity.period = sparePartRefEntity.getPeriod();
        sparePartEntity.periodType = sparePartRefEntity.getPeriodType();
        sparePartEntity.periodUnit = sparePartRefEntity.getPeriodUnit();
        sparePartEntity.accessoryName = sparePartRefEntity.getAccessoryEamId().getAttachEamId().name;
        sparePartEntity.remark = sparePartRefEntity.getSpareMemo();
        sparePartEntity.standingCrop = sparePartRefEntity.getStandingCrop();

        mSparePartEntityList.add(sparePartEntity);
        refreshListController.refreshComplete(removeDuplicte());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshEvent refreshEvent) {
        if (refreshEvent.delId != null) {
            dgDeletedIds.add(refreshEvent.delId);
        }
        refreshListController.refreshComplete(removeDuplicte());
//        refreshListController.refreshBegin();
    }

    private void initEmptyView() {
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, "暂无数据"));
    }


    @Override
    public void updateStandingCropSuccess(CommonListEntity entity) {
        if (allUpdateFlag) {
            allUpdateFlag = false;
            loaderController.showMsgAndclose("列表全部备件库存更新成功!", true, 2000);
        } else {
            loaderController.showMsgAndclose("库存更新成功!", true, 1000);
        }
        //回填页面现存量值
        List<StandingCropResultEntity> list = entity.result;
        for (SparePartEntity sparePartEntity : mSparePartEntityList) {
            for (StandingCropResultEntity standingCropResultEntity : list) {
                if (sparePartEntity.productID != null && standingCropResultEntity.productCode.equals(sparePartEntity.productID.productCode)) {
                    sparePartEntity.standingCrop = new BigDecimal(standingCropResultEntity.useQuantity);
                }
            }
        }
        refreshListController.refreshComplete(removeDuplicte()); // 非网络刷新，否则未保存的数据丢失
//        refreshListController.refreshBegin();
    }

    @Override
    public void updateStandingCropFailed(String errorMsg) {
        allUpdateFlag = false;
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void generateSparePartApplySuccess(ResultEntity entity) {
        selectedEntityList.clear();// 清空备件领用list
        loaderController.showMsgAndclose("处理成功", true, 200, new OnLoaderFinishListener() {
            @Override
            public void onLoaderFinished() {
//                onLoading("备件重新加载中...");
//                presenterRouter.create(SparePartAPI.class).listSparePartList(workListId);
                refreshListController.refreshBegin();
                EventBus.getDefault().post(new VersionRefreshEvent()); //更新工单version，使得返回页面，提示保存时，不必再次手动下拉刷新
            }
        });
    }

    @Override
    public void generateSparePartApplyFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void listSparePartListSuccess(SparePartListEntity entity) {
//        onLoadSuccess("加载成功！");
//        if (entity.result.size() >= mSparePartEntityList.size()){ //以数量大者为主
//            mSparePartEntityList = entity.result;
//        }
        mSparePartEntityList.addAll(entity.result);

        for (SparePartEntity sparePartEntity : mSparePartEntityList) {

            if (sparePartEntity.remark == null) {
                sparePartEntity.remark = "";
            }
            if (sparePartEntity.sum != null) {
                sparePartEntity.sum = sparePartEntity.sum.setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            if (sparePartEntity.actualQuantity != null) {
                sparePartEntity.actualQuantity = sparePartEntity.actualQuantity.setScale(2, BigDecimal.ROUND_HALF_UP);
            }
        }
        refreshListController.refreshComplete(removeDuplicte());

    }

    @Override
    public void listSparePartListFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    //排重
    public List<SparePartEntity> removeDuplicte() {
        Set<SparePartEntity> s = new TreeSet<>((o1, o2) -> {
            if (o1.productID != null && o2.productID != null && o1.productID.id != null && o2.productID.id != null) {
                int i = o1.productID.id.compareTo(o2.productID.id);
                return i;
            }
            return -1;
        });
        s.addAll(mSparePartEntityList);
        mSparePartEntityList = new LinkedList(s);
        return mSparePartEntityList;
    }
}
