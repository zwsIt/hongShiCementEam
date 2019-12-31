package com.supcon.mes.module_sparepartapply_hl.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.Good;
import com.supcon.mes.middleware.model.bean.SparePartReceiveEntity;
import com.supcon.mes.middleware.model.bean.SparePartReceiveListEntity;
import com.supcon.mes.middleware.model.bean.SparePartRefEntity;
import com.supcon.mes.middleware.model.event.SparePartAddEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_sparepartapply_hl.R;
import com.supcon.mes.module_sparepartapply_hl.constant.SPAHLConstant;
import com.supcon.mes.module_sparepartapply_hl.model.contract.SparePartApplyDetailContract;
import com.supcon.mes.module_sparepartapply_hl.model.event.SparePartApplyDetailEvent;
import com.supcon.mes.module_sparepartapply_hl.presenter.SparePartApplyDetailPresenter;
import com.supcon.mes.module_sparepartapply_hl.ui.adapter.SparePartApplyDetailAdapter;
import com.supcon.mes.module_wxgd.IntentRouter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * @description SparePartApplyDetailList
 * @author zhangwenshuai 2019/10/29
 * 备件领用申请明细list
 */
@Router(value = Constant.Router.SPARE_PART_APPLY_DETAIL_LIST)
@Presenter(value = {SparePartApplyDetailPresenter.class})
public class SparePartApplyDetailList extends BaseRefreshRecyclerActivity<SparePartReceiveEntity> implements SparePartApplyDetailContract.View {

    @BindByTag("contentView")
    protected RecyclerView contentView;

    @BindByTag("leftBtn")
    protected ImageButton leftBtn;

    @BindByTag("rightBtn")
    protected ImageButton rightBtn;

    @BindByTag("titleText")
    protected TextView titleText;

    private SparePartApplyDetailAdapter sparePartApplyDetailAdapter;
    private Long tableId;
    protected List<SparePartReceiveEntity> sparePartReceiveEntityList = new ArrayList<>();
    private List<Long> dgDeletedIds = new ArrayList<>(); //表体删除记录ids
    private boolean editable;
    private String url; // 获取备件领用申请明细PT之url
    private boolean mIsWork; // 是否来源工单
    private int mPosition;
    private boolean modify; // 修改原备件

    @Override
    protected void onInit() {
        super.onInit();

        EventBus.getDefault().register(this);
        StatusBarUtils.setWindowStatusBarColor(this, com.supcon.mes.module_wxgd.R.color.themeColor);
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(5, context)));
        contentView.addOnItemTouchListener(new CustomSwipeLayout.OnSwipeItemTouchListener(context));

        tableId = getIntent().getLongExtra(Constant.IntentKey.TABLE_ID,0);
        editable = getIntent().getBooleanExtra(Constant.IntentKey.IS_EDITABLE, false);//放在onInit()中会存在迟于创建Adapter，故setEditable
        mIsWork = getIntent().getBooleanExtra(SPAHLConstant.IntentKey.IS_WORK,false);
        sparePartReceiveEntityList = GsonUtil.jsonToList(getIntent().getStringExtra(Constant.IntentKey.SPARE_PART_ENTITIES),SparePartReceiveEntity.class);
        sparePartApplyDetailAdapter.setEditable(mIsWork,editable,getIntent().getBooleanExtra(SPAHLConstant.IntentKey.IS_SEND_STATUS,false));

        url = getIntent().getStringExtra(Constant.IntentKey.URL);

        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(false);
    }

    @Override
    protected IListAdapter<SparePartReceiveEntity> createAdapter() {
        sparePartApplyDetailAdapter = new SparePartApplyDetailAdapter(this);
        return sparePartApplyDetailAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_sparepart_apply_detail_list;
    }

    @Override
    protected void initView() {
        super.initView();
        titleText.setText("备件申请明细列表");
        if (!mIsWork && editable){
            rightBtn.setVisibility(View.VISIBLE);
        }
        initEmptyView();
    }

    private void initEmptyView() {
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, ""));
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        RxView.clicks(leftBtn)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        onBackPressed();
                    }
                });
        RxView.clicks(rightBtn)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        modify = false;
                        selectSparePart();
                    }
                });
        refreshListController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
//                presenterRouter.create(SparePartApplyDetailAPI.class).listSparePartApplyDetail(url, tableId);
                refreshListController.refreshComplete(sparePartReceiveEntityList);
            }
        });

        sparePartApplyDetailAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                String tag = childView.getTag().toString();
                SparePartReceiveEntity sparePartReceiveEntity = (SparePartReceiveEntity) obj;

                switch (tag){
                    case "sparePart":
                        mPosition = position;
//                        if (action == -1){
//                            sparePartReceiveEntity.sparePartId = null;
//                        }else {
                        modify = true;
                        selectSparePart();
//                        }
                        break;
                    case "itemViewDelBtn":
                        dgDeletedIds.add(sparePartReceiveEntity.id);
                        refreshListController.refreshComplete(sparePartReceiveEntityList);
                        break;

                }
            }
        });


    }

    private void selectSparePart() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constant.IntentKey.IS_SPARE_PART_REF, false);
        // 带入已添加备件，检验重复添加
        bundle = genAddDataList(bundle);
        IntentRouter.go(context, Constant.Router.SPARE_PART_REF, bundle);
    }

    public Bundle genAddDataList(Bundle bundle) {
        ArrayList<String> addedSPList = new ArrayList<>();
        for (SparePartReceiveEntity sparePartReceiveEntity : sparePartReceiveEntityList){
            if (sparePartReceiveEntity.sparePartId != null) {
                addedSPList.add(sparePartReceiveEntity.sparePartId.id.toString());
            }
        }
        bundle.putStringArrayList(Constant.IntentKey.ADD_DATA_LIST, addedSPList);
        return bundle;
    }

    /**
     * 添加备件
     * @param sparePartAddEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addSparePart(SparePartAddEvent sparePartAddEvent) {
        SparePartRefEntity sparePartRefEntity = sparePartAddEvent.getSparePartRefEntity();
        Good good = sparePartRefEntity.getProductID();
        for (SparePartReceiveEntity sparePartReceiveEntity : sparePartReceiveEntityList) {
            if (sparePartReceiveEntity.sparePartId != null) {
                if (sparePartReceiveEntity.sparePartId.id.equals(good.id)) {
                    ToastUtils.show(context, "请勿重复添加备件!");
                    refreshListController.refreshComplete(sparePartReceiveEntityList);
                    return;
                }
            }
        }
        SparePartReceiveEntity sparePartReceiveEntity = new SparePartReceiveEntity();
//        sparePartReceiveEntity.currDemandQuity = ;
        sparePartReceiveEntity.sparePartId = good;
        sparePartReceiveEntity.origDemandQuity = new BigDecimal(1).setScale(2,BigDecimal.ROUND_HALF_UP);
        sparePartReceiveEntity.price = good.productCostPrice;
        sparePartReceiveEntity.total = good.productCostPrice != null ? good.productCostPrice.multiply(sparePartReceiveEntity.origDemandQuity) : new BigDecimal(0);

        if (modify){
            SparePartReceiveEntity entityOld = sparePartReceiveEntityList.set(mPosition,sparePartReceiveEntity);
            sparePartReceiveEntity.id = entityOld.id;
        }else {
            sparePartReceiveEntityList.add(sparePartReceiveEntity);
        }

        refreshListController.refreshComplete(sparePartReceiveEntityList);

    }

    @Override
    public void listSparePartApplyDetailSuccess(SparePartReceiveListEntity entity) {
        sparePartReceiveEntityList = entity.result;
        // 控制小数位数4
        for (SparePartReceiveEntity sparePartReceiveEntity : sparePartReceiveEntityList){
            if (sparePartReceiveEntity.origDemandQuity != null){
                sparePartReceiveEntity.origDemandQuity = Util.bigDecimalScale(sparePartReceiveEntity.origDemandQuity,2);
            }
            if (sparePartReceiveEntity.currDemandQuity != null){
                sparePartReceiveEntity.currDemandQuity = Util.bigDecimalScale(sparePartReceiveEntity.currDemandQuity,2);
            }
            if (sparePartReceiveEntity.price != null){
                sparePartReceiveEntity.price = Util.bigDecimalScale(sparePartReceiveEntity.price,2);
            }
            if (sparePartReceiveEntity.total != null){
                sparePartReceiveEntity.total = Util.bigDecimalScale(sparePartReceiveEntity.total,2);
            }

        }
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void listSparePartApplyDetailFailed(String errorMsg) {
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
        refreshListController.refreshComplete();
    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(new SparePartApplyDetailEvent(sparePartReceiveEntityList, dgDeletedIds));
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
