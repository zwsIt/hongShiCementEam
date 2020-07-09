package com.supcon.mes.module_wxgd.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.com_http.util.RxSchedulers;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.mes.mbap.beans.FilterBean;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.beans.WorkFlowEntity;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomFilterView;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.ModulePermissonCheckController;
import com.supcon.mes.middleware.controller.PcController;
import com.supcon.mes.middleware.controller.RoleController;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.middleware.model.bean.RoleEntity;
import com.supcon.mes.middleware.model.bean.ScreenEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.WXGDEam;
import com.supcon.mes.middleware.model.bean.WXGDEntity;
import com.supcon.mes.middleware.model.bean.YHEntity;
import com.supcon.mes.middleware.model.event.LoginValidEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.listener.OnAPIResultListener;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.FilterHelper;
import com.supcon.mes.middleware.util.ProcessKeyUtil;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.SystemCodeManager;
import com.supcon.mes.module_wxgd.IntentRouter;
import com.supcon.mes.module_wxgd.R;
import com.supcon.mes.module_wxgd.controller.WXGDSubmitController;
import com.supcon.mes.module_wxgd.model.api.WXGDListAPI;
import com.supcon.mes.module_wxgd.model.bean.WXGDListEntity;
import com.supcon.mes.module_wxgd.model.contract.WXGDListContract;
import com.supcon.mes.module_wxgd.presenter.WXGDListPresenter;
import com.supcon.mes.module_wxgd.ui.adapter.WXGDListAdapter;
import com.supcon.mes.module_wxgd.util.WXGDMapManager;
import com.supcon.mes.module_wxgd.util.WorkFilterHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * WXGDListActivity 维修工单列表
 * created by zhangwenshuai1 2018/8/9
 */

@Deprecated
@Router(value = Constant.Router.WXGD_LIST)
@Presenter(value = {WXGDListPresenter.class})
@Controller(value = PcController.class)
public class WXGDListActivity extends BaseRefreshRecyclerActivity<WXGDEntity> implements WXGDListContract.View, WXGDSubmitController.OnSubmitResultListener {

    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("leftBtn")
    AppCompatImageButton leftBtn;

    @BindByTag("rightBtn")
    AppCompatImageButton rightBtn;

    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("customSearchView")
    CustomSearchView customSearchView;

    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;

    @BindByTag("listWorkSourceFilter")
    CustomFilterView<FilterBean> listWorkSourceFilter;

    @BindByTag("listRepairTypeFilter")
    CustomFilterView<FilterBean> listRepairTypeFilter;

    @BindByTag("listPriorityFilter")
    CustomFilterView<FilterBean> listPriorityFilter;

    @BindByTag("listWorkflowFilter")
    CustomFilterView<ScreenEntity> listWorkflowFilter;

    @BindByTag("radioGroupFilter")
    RadioGroup radioGroupFilter;

    private WXGDListAdapter wxgdListAdapter;

    Map<String, Object> queryParam = new HashMap<>();
    private WXGDSubmitController wxgdSubmitController;
    private RoleController roleController;
    private String repairType;

    private Long deploymentId;
    private String __pc__;

    @Override
    protected IListAdapter createAdapter() {
        wxgdListAdapter = new WXGDListAdapter(context);
        return wxgdListAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_wxgd_list;
    }

    @Override
    protected void onInit() {
        super.onInit();
        repairType = getIntent().getStringExtra(Constant.IntentKey.REPAIR_TYPE);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setLoadMoreEnable(true);

        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView() {
        super.initView();

        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        contentView.setLayoutManager(new LinearLayoutManager(this));
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));
        contentView.addItemDecoration(new SpaceItemDecoration(10));

        customSearchView.setHint("搜索");
        searchTitleBar.disableRightBtn();
        initFilter();
    }

    @Override
    protected void initData() {
        super.initData();
        if (!TextUtils.isEmpty(repairType)) {
            listRepairTypeFilter.setCurrentItem(repairType);
            queryParam.put(Constant.BAPQuery.REPAIR_TYPE, SystemCodeManager.getInstance().getSystemCodeEntityId(Constant.SystemCode.YH_WX_TYPE, repairType));
        }
        ModulePermissonCheckController mModulePermissionCheckController = new ModulePermissonCheckController();
        mModulePermissionCheckController.checkModulePermission(EamApplication.getUserName(), ProcessKeyUtil.WORK, new OnSuccessListener<Long>() {
            @Override
            public void onSuccess(Long result) {
                deploymentId = result;
                if (deploymentId != null){
                    searchTitleBar.enableRightBtn();
                }
            }
        }, null);
    }

    @Override
    protected void onRegisterController() {
        super.onRegisterController();
        roleController = new RoleController();
        wxgdSubmitController = new WXGDSubmitController(this);
        roleController.queryRoleList(EamApplication.getUserName());
        registerController(Constant.Controller.ROLE, roleController);
        registerController(Constant.Controller.SUBMIT, wxgdSubmitController);
    }
    /**
     * @param
     * @return 获取单据提交pc
     * @description
     * @author user 2019/10/31
     */
    public void getSubmitPc(String operateCode) {
        getController(PcController.class).queryPc(operateCode, ProcessKeyUtil.WORK, new OnAPIResultListener<String>() {
            @Override
            public void onFail(String errorMsg) {
                ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
            }

            @Override
            public void onSuccess(String result) {
                __pc__ = result;
            }
        });
    }
    /**
     * @param
     * @return
     * @description 初始化过滤条件
     * @author zhangwenshuai1 2018/8/14
     */
    private void initFilter() {
        listWorkSourceFilter.setData(WorkFilterHelper.createWorkSourceList());
        listRepairTypeFilter.setData(WorkFilterHelper.createRepairTypeList());
        listPriorityFilter.setData(WorkFilterHelper.createPriorityList());
        FilterHelper.addView(this, radioGroupFilter, WorkFilterHelper.createWorkflowList());
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        leftBtn.setOnClickListener(v -> finish());

        RxView.clicks(rightBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (deploymentId != null) {
//                            createYH();
                            WXGDEntity wxgdEntity = new WXGDEntity();
                            wxgdEntity.id = -1L;
                            wxgdEntity.workSource = new SystemCodeEntity("BEAM2003/08", "手动添加", "BEAM2003", null, null, 0, EamApplication.getIp());
                            wxgdEntity.pending = new PendingEntity();
                            wxgdEntity.pending.deploymentId = deploymentId;
                            wxgdEntity.pending.taskDescription = "派工";
                            wxgdEntity.eamID = new EamEntity();

                            Bundle bundle = new Bundle();
                            bundle.putSerializable(Constant.IntentKey.WXGD_ENTITY,wxgdEntity);
                            IntentRouter.go(context,Constant.Router.WXGD_DISPATCHER,bundle);
                        } else {
                            ToastUtils.show(context, "当前用户并未拥有创建单据权限！");
                        }
                    }
                });

        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {
                presenterRouter.create(WXGDListAPI.class).listWxgds(pageIndex, queryParam,false);
                setRadioEnable(false);
            }
        });
        radioGroupFilter.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = findViewById(group.getCheckedRadioButtonId());
            generateFilterWorkflow(radioButton.getText().toString());
        });
        listWorkSourceFilter.setFilterSelectChangedListener(filterBean -> {
            LogUtil.d("filterBean", filterBean.toString());

            generateFilterCondition(filterBean.name);
        });

        listRepairTypeFilter.setFilterSelectChangedListener(new CustomFilterView.FilterSelectChangedListener<FilterBean>() {
            @Override
            public void onFilterSelected(FilterBean filterBean) {
                if (filterBean.type == CustomFilterView.VIEW_TYPE_ALL) {
                    queryParam.remove(Constant.BAPQuery.REPAIR_TYPE);
                } else {
                    queryParam.put(Constant.BAPQuery.REPAIR_TYPE, SystemCodeManager.getInstance().getSystemCodeEntityId(Constant.SystemCode.YH_WX_TYPE, filterBean.name));
                }
                doFilter();
            }
        });
        listPriorityFilter.setFilterSelectChangedListener(new CustomFilterView.FilterSelectChangedListener<FilterBean>() {
            @Override
            public void onFilterSelected(FilterBean filterBean) {
                if (filterBean.type == CustomFilterView.VIEW_TYPE_ALL) {
                    queryParam.remove(Constant.BAPQuery.WXGD_PRIORITY);
                } else {
                    queryParam.put(Constant.BAPQuery.WXGD_PRIORITY, SystemCodeManager.getInstance().getSystemCodeEntityId(Constant.SystemCode.YH_PRIORITY, filterBean.name));
                }
                doFilter();
            }
        });

        listWorkflowFilter.setFilterSelectChangedListener((CustomFilterView.FilterSelectChangedListener<ScreenEntity>) screenEntity -> {
            queryParam.put(Constant.BAPQuery.WORK_STATE, screenEntity.layRec);
            doFilter();
        });

        RxTextView.textChanges(customSearchView.editText())
                .skipInitialValue()
                .subscribe(charSequence -> {
                    doSearchTableNo(charSequence.toString().trim());
                });

        searchTitleBar.setOnExpandListener(isExpand -> {

            if (isExpand) {
                customSearchView.setHint("搜索设备名称");
                customSearchView.setInputTextColor(R.color.hintColor);
            } else {
                customSearchView.setHint("搜索");
                customSearchView.setInputTextColor(R.color.black);
            }

        });
        wxgdListAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                String tag = String.valueOf(childView.getTag());
                WXGDEntity wxgdEntity = (WXGDEntity) obj;
                switch (tag) {
                    case "receiveBtn":
                        new CustomDialog(context)
                                .twoButtonAlertDialog("你确认接单么？")
                                .bindView(R.id.grayBtn, "取消")
                                .bindView(R.id.redBtn, "确认")
                                .bindClickListener(R.id.grayBtn, null, true)
                                .bindClickListener(R.id.redBtn, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        doSubmit(wxgdEntity);
                                    }
                                }, true)
                                .show();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    //创建隐患单
    private void createYH() {
        YHEntity yhEntity = new YHEntity();
        yhEntity.findStaffID = EamApplication.me();
        yhEntity.findTime = System.currentTimeMillis();
        yhEntity.cid = EamApplication.getAccountInfo().cid;
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.IntentKey.YHGL_ENTITY, yhEntity);
        bundle.putSerializable(Constant.IntentKey.DEPLOYMENT_ID, deploymentId);
        IntentRouter.go(context, Constant.Router.YH_EDIT, bundle);
    }

    /**
     * @param
     * @return
     * @description 接单
     * @author zhangwenshuai1 2018/9/13
     */
    private void doSubmit(WXGDEntity wxgdEntity) {
        onLoading("接单中...");
        //封装公共参数
        Map<String, Object> map = WXGDMapManager.createMap(wxgdEntity);

        //封装工作流
        map = generateWorkFlow(map);

        generateParamsDtoAndSubmit(map);
    }

    private Map<String, Object> generateWorkFlow(Map<String, Object> map) {
        WorkFlowEntity workFlowEntity = new WorkFlowEntity();
        List<WorkFlowEntity> workFlowEntities = new ArrayList<>();
        workFlowEntity.dec = "接单";
        workFlowEntity.type = "normal";
        workFlowEntity.outcome = "Link2163";
        workFlowEntities.add(workFlowEntity);
        map.put("operateType", Constant.Transition.SUBMIT);
        map.put("workFlowVar.outcomeMapJson", workFlowEntities.toString());
        map.put("workFlowVar.outcome", workFlowEntity.outcome);
        return map;
    }

    private void generateParamsDtoAndSubmit(Map<String, Object> map) {
        RoleEntity roleEntity = roleController.getRoleEntity(0);
        map.put("workRecord.createPositionId", roleEntity.role != null ? roleEntity.role.id : "");
        map.put("viewselect", "workReceiptEdit");
        map.put("workRecord.receiptInfo", "jiedan");
        map.put("datagridKey", "BEAM2_workList_workRecord_workReceiptEdit_datagrids");
        map.put("viewCode", "BEAM2_1.0.0_workList_workReceiptEdit");
        map.put("modelName", "WorkRecord");
        map.put("taskDescription", "BEAM2_1.0.0.work.task457");


        map.put("dg1557994493235ModelCode", "BEAM2_1.0.0_workList_Maintenance");
        map.put("dg1557994493235ListJson", new LinkedList().toString());

        map.put("dg1531695879443ModelCode", "BEAM2_1.0.0_workList_LubricateOil");
        map.put("dg1531695879443ListJson", new LinkedList().toString());

        map.put("dg1531695879084ModelCode", "BEAM2_1.0.0_workList_SparePart");
        map.put("dg1531695879084ListJson", new LinkedList().toString());

        map.put("dg1531695879365ModelCode", "BEAM2_1.0.0_workList_RepairStaff");
        map.put("dg1531695879365ListJson", new LinkedList().toString());
        wxgdSubmitController.doReceiveSubmit(map,__pc__);
    }

    /**
     * @param
     * @return
     * @description 设备名称查询
     * @author zhangwenshuai1 2018/8/14
     */
    private void doSearchTableNo(String eamName) {
        queryParam.put(Constant.BAPQuery.EAM_NAME, eamName);
        refreshListController.refreshBegin();
    }

    /**
     * @param
     * @return
     * @description 封装工单来源过滤条件
     * @author zhangwenshuai1 2018/8/14
     */
    private void generateFilterCondition(String workSource) {
        String workSourceId = "";
        switch (workSource) {
            case Constant.WorkSource_CN.patrolcheck:
                workSourceId = Constant.WxgdWorkSource.patrolcheck;
                break;
            case Constant.WorkSource_CN.lubrication:
                workSourceId = Constant.WxgdWorkSource.lubrication;
                break;
            case Constant.WorkSource_CN.maintenance:
                workSourceId = Constant.WxgdWorkSource.maintenance;
                break;
            case Constant.WorkSource_CN.sparepart:
                workSourceId = Constant.WxgdWorkSource.sparepart;
                break;
            case Constant.WorkSource_CN.other:
                workSourceId = Constant.WxgdWorkSource.other;
                break;
            default:
                break;
        }
        queryParam.put(Constant.BAPQuery.WORK_SOURCE, workSourceId);
        doFilter();
    }

    /**
     * @param
     * @return
     * @description 封装工作流状态过滤条件
     * @author zhangwenshuai1 2018/8/14
     */
    @SuppressLint("CheckResult")
    private void generateFilterWorkflow(String workState) {
        String workStateId = "";
        switch (workState) {
            case Constant.WxgdStatus_CH.DISPATCH:
                workStateId = Constant.WorkState_ENG.DISPATCH;
                break;
            case Constant.WxgdStatus_CH.CONFIRM:
                workStateId = Constant.WorkState_ENG.CONFIRM;
                break;
            case Constant.WxgdStatus_CH.IMPLEMENT:
                workStateId = Constant.WorkState_ENG.EXECUTE;
                break;
            case Constant.WxgdStatus_CH.ACCEPTANCE:
                workStateId = Constant.WorkState_ENG.ACCEPT;
                break;
            case Constant.WxgdStatus_CH.COMPLETE:
                workStateId = Constant.WorkState_ENG.TAKE_EFFECT;
                break;
            default:
                break;
        }
        queryParam.put(Constant.BAPQuery.WORK_STATE, workStateId);
        Flowable.timer(500, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> doFilter());
    }


    /**
     * @param
     * @return
     * @description 过滤查询
     * @author zhangwenshuai1 2018/8/14
     */
    private void doFilter() {
        refreshListController.refreshBegin();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(LoginEvent loginEvent) {
        refreshListController.refreshBegin();
    }

    @SuppressLint("CheckResult")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(LoginValidEvent loginValidEvent) {
        Flowable.timer(500, TimeUnit.MILLISECONDS)
                .compose(RxSchedulers.io_main())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        refreshListController.refreshBegin();
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(RefreshEvent refreshEvent) {
        refreshListController.refreshBegin();
    }

    @Override
    public void listWxgdsSuccess(WXGDListEntity entity) {
        refreshListController.refreshComplete(entity.result);
        setRadioEnable(true);
    }

    @Override
    public void listWxgdsFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
        refreshListController.refreshComplete();
        setRadioEnable(true);
    }

    /**
     * @param
     * @return
     * @description 初始化无数据
     * @author zhangwenshuai1 2018/8/9
     */
//    private void initEmpty() {
//        mRecyclerEmptyAdapter = new RecyclerEmptyAdapter(context);
//        mRecyclerEmptyAdapter.addData(EmptyViewHelper.createEmptyEntity());
//        refreshListController.setEmpterAdapter(mRecyclerEmptyAdapter);
//
//    }

    @Override
    public void submitSuccess(BapResultEntity entity) {
        onLoadSuccessAndExit("接单成功", new OnLoaderFinishListener() {
            @Override
            public void onLoaderFinished() {
                refreshListController.refreshBegin();
            }
        });
    }

    public void setRadioEnable(boolean enable) {
        for (int i = 0; i < radioGroupFilter.getChildCount(); i++) {
            radioGroupFilter.getChildAt(i).setEnabled(enable);
        }
    }

    @Override
    public void submitFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }
}
