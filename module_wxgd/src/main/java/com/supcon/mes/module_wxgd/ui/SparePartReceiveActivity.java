package com.supcon.mes.module_wxgd.ui;

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
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.CustomSwipeLayout;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.mes.mbap.beans.LinkEntity;
import com.supcon.mes.mbap.beans.WorkFlowEntity;
import com.supcon.mes.mbap.beans.WorkFlowVar;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.LinkController;
import com.supcon.mes.middleware.controller.ModulePermissonCheckController;
import com.supcon.mes.middleware.controller.ModulePowerController;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.Good;
import com.supcon.mes.middleware.model.bean.SparePartId;
import com.supcon.mes.middleware.model.bean.SparePartReceiveEntity;
import com.supcon.mes.middleware.model.bean.SparePartRefEntity;
import com.supcon.mes.middleware.model.event.SparePartAddEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_wxgd.IntentRouter;
import com.supcon.mes.module_wxgd.R;
import com.supcon.mes.module_wxgd.model.api.SparePartReceiveSubmitAPI;
import com.supcon.mes.module_wxgd.model.contract.SparePartReceiveSubmitContract;
import com.supcon.mes.module_wxgd.presenter.SparePartReceiveSubmitPresenter;
import com.supcon.mes.module_wxgd.ui.adapter.SparePartReceiveAdapter;
import com.supcon.mes.module_wxgd.util.SparePartReceiveMapManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * 备件领用申请：添加
 */
@Router(Constant.Router.SPARE_PART_RECEIVE)
@Presenter(value = {SparePartReceiveSubmitPresenter.class})
public class SparePartReceiveActivity extends BaseRefreshRecyclerActivity<SparePartReceiveEntity> implements SparePartReceiveSubmitContract.View {
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    ImageButton rightBtn;
    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("applicant")
    CustomVerticalTextView applicant;
    @BindByTag("applicationTime")
    CustomVerticalTextView applicationTime;

    @BindByTag("ensure")
    Button ensure;

    protected List<SparePartReceiveEntity> mSparePartEntityList = new ArrayList<>();
    private SparePartReceiveAdapter sparePartReceiveAdapter;
    private Long deploymentId;
    private String powerCode;
    private LinkController mLinkController;

    @Override
    protected int getLayoutID() {
        return R.layout.ac_sparepart_receive;
    }

    @Override
    protected IListAdapter createAdapter() {
        sparePartReceiveAdapter = new SparePartReceiveAdapter(this);
        return sparePartReceiveAdapter;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText("备件领用");
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(3, context)));
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, "请添加要申领备件"));
        contentView.addOnItemTouchListener(new CustomSwipeLayout.OnSwipeItemTouchListener(context));
        rightBtn.setVisibility(View.VISIBLE);
        refreshListController.setAutoPullDownRefresh(false);
        refreshListController.setPullDownRefreshEnabled(false);
    }

    @Override
    protected void initData() {
        super.initData();
        applicant.setContent(EamApplication.getAccountInfo().staffName);
        applicationTime.setContent(DateUtil.dateFormat(System.currentTimeMillis(), "yyyy-MM-dd"));
        refreshListController.refreshComplete(null);

        ModulePermissonCheckController mModulePermissonCheckController = new ModulePermissonCheckController();
        mModulePermissonCheckController.checkModulePermission(EamApplication.getUserName(), "sparePartApply",
                result -> {
                    deploymentId = result;
                    ModulePowerController modulePowerController = new ModulePowerController();
                    modulePowerController.checkModulePermission(deploymentId, result1 -> powerCode = result1.powerCode);
                }, null);
        mLinkController = new LinkController();
        mLinkController.initStartTransition(null, "sparePartApply");
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
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
        RxView.clicks(ensure)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> new CustomDialog(context)
                        .twoButtonAlertDialog("确定提交申请吗?")
                        .bindView(R.id.redBtn, "确定")
                        .bindView(R.id.grayBtn, "取消")
                        .bindClickListener(R.id.redBtn, v12 -> {
                            List<SparePartReceiveEntity> list = sparePartReceiveAdapter.getList();
                            for (SparePartReceiveEntity sparePartReceiveEntity : list) {
                                if (sparePartReceiveEntity.origDemandQuity == null || sparePartReceiveEntity.origDemandQuity <= 0) {
                                    ToastUtils.show(this, "申请数量必须大于0！");
                                    return;
                                }
                            }
                            List<LinkEntity> linkEntities = mLinkController.getLinkEntities();
                            if (linkEntities.size() > 0) {
                                doSubmit(createWorkFlowVar(linkEntities.get(0)));
                            } else {
                                ToastUtils.show(this, "当前用户并未拥有创建单据权限！");
                            }
                        }, true)
                        .bindClickListener(R.id.grayBtn, null, true)
                        .show());

        sparePartReceiveAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                if (childView.getId() == R.id.itemViewDelBtn) {
                    if (sparePartReceiveAdapter.getList().size() == 0) {
                        ensure.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addSparePart(SparePartAddEvent sparePartAddEvent) {
        SparePartRefEntity sparePartRefEntity = sparePartAddEvent.getSparePartRefEntity();
        Good good = sparePartRefEntity.getProductID();
        for (SparePartReceiveEntity sparePartEntity : mSparePartEntityList) {
            if (sparePartEntity.getSparePartId().productCode.equals(good.productCode)) {
                ToastUtils.show(context, "请勿重复添加备件!");
                return;
            }
        }
        SparePartReceiveEntity sparePartReceiveEntity = new SparePartReceiveEntity();
        SparePartId sparePartId = new SparePartId();
        sparePartId.id = good.id;
        sparePartId.productCode = good.productCode;
        sparePartId.productName = good.productName;
        sparePartId.productModel = good.productModel;
        sparePartId.productSpecif = good.productSpecif;
        sparePartId.productBaseUnit = good.productBaseUnit;
        sparePartReceiveEntity.sparePartId = sparePartId;
        mSparePartEntityList.add(sparePartReceiveEntity);
        refreshListController.refreshComplete(mSparePartEntityList);
        ensure.setVisibility(View.VISIBLE);
    }

    private WorkFlowVar createWorkFlowVar(LinkEntity linkEntity) {
        WorkFlowVar workFlowVar = new WorkFlowVar();
        List<WorkFlowEntity> workFlowEntities = new ArrayList<>();
        WorkFlowEntity workFlowEntity = new WorkFlowEntity();
        workFlowEntity.dec = linkEntity.description;
        workFlowEntity.outcome = linkEntity.name;
        workFlowEntity.type = "normal";
        workFlowEntities.add(workFlowEntity);
        workFlowVar.operateType = "submit";
        workFlowVar.dec = linkEntity.description;
        workFlowVar.outCome = linkEntity.name;
        workFlowVar.outcomeMapJson = workFlowEntities;
        return workFlowVar;
    }

    private void doSubmit(WorkFlowVar workFlowVar) {
        List<WorkFlowEntity> workFlowEntities = new ArrayList<>();
        WorkFlowEntity workFlowEntity = new WorkFlowEntity();
        workFlowEntity.dec = workFlowVar.dec;
        workFlowEntity.type = workFlowVar.outcomeMapJson.get(0).type;
        workFlowEntity.outcome = workFlowVar.outCome;
        workFlowEntities.add(workFlowEntity);


        Map map = SparePartReceiveMapManager.createMap();
        List list1 = SparePartReceiveMapManager.dataChange(sparePartReceiveAdapter.getList());
        map.put("dg1535943746846ModelCode", "BEAM2_1.0.0_sparePart_SparePartApply");
        map.put("dg1535943746846ListJson", list1.toString());
        map.put("dgLists['dg1535943746846']", list1.toString());

        map.put("operateType", Constant.Transition.SUBMIT);
        map.put("deploymentId", deploymentId != null ? deploymentId : "");

        map.put("workFlowVar.outcomeMapJson", workFlowEntities.toString());
        map.put("workFlowVar.dec", workFlowEntity.dec);
        map.put("workFlowVar.outcome", workFlowEntity.outcome);
        map.put("operateType", Constant.Transition.SUBMIT);

        if (TextUtils.isEmpty(powerCode)) {
            ToastUtils.show(this, "当前用户并未拥有创建单据权限！");
            return;
        }
        onLoading("正在处理中...");
        presenterRouter.create(SparePartReceiveSubmitAPI.class).doSubmitSparePart(map);
    }

    @Override
    public void doSubmitSparePartSuccess(BapResultEntity entity) {
        onLoadSuccessAndExit("领取成功", new OnLoaderFinishListener() {
            @Override
            public void onLoaderFinished() {
                finish();
            }
        });
    }

    @Override
    public void doSubmitSparePartFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
