package com.supcon.mes.module_wxgd.ui.fragment;

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
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.ptr.PtrFrameLayout;
import com.supcon.common.view.util.DisplayUtil;
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
import com.supcon.mes.middleware.model.bean.WXGDEntity;
import com.supcon.mes.middleware.model.bean.YHEntity;
import com.supcon.mes.middleware.model.event.LoginValidEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.listener.OnAPIResultListener;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.FilterHelper;
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
 * WorkPendingListFragment 维修工单待办列表
 * created by zhangwenshuai1 2020/5/29
 */

@Presenter(value = {WXGDListPresenter.class})
//@Controller(value = PcController.class)
public class WorkPendingListFragment extends BaseRefreshRecyclerFragment<WXGDEntity> implements WXGDListContract.View, WXGDSubmitController.OnSubmitResultListener {

    @BindByTag("workStateRadioGroup")
    RadioGroup workStateRadioGroup;
    @BindByTag("contentView")
    RecyclerView contentView;

    private WXGDListAdapter wxgdListAdapter;

    Map<String, Object> queryParam = new HashMap<>();
    private WXGDSubmitController wxgdSubmitController;
//    private RoleController roleController;

    private String __pc__;

    @Override
    protected IListAdapter createAdapter() {
        wxgdListAdapter = new WXGDListAdapter(context);
        return wxgdListAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.work_fragment_work_pending_list;
    }

    @Override
    protected void onInit() {
        super.onInit();
        contentView.setLayoutManager(new LinearLayoutManager(getActivity()));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(5,context)));
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));

        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void onRegisterController() {
        super.onRegisterController();
//        roleController = new RoleController();
        wxgdSubmitController = new WXGDSubmitController(this);
//        roleController.queryRoleList(EamApplication.getUserName());
//        registerController(Constant.Controller.ROLE, roleController);
        registerController(Constant.Controller.SUBMIT, wxgdSubmitController);
    }
    /**
     * @param
     * @return 获取单据提交pc
     * @description
     * @author user 2019/10/31
     */
//    public void getSubmitPc(String operateCode) {
//        getController(PcController.class).queryPc(operateCode, "work", new OnAPIResultListener<String>() {
//            @Override
//            public void onFail(String errorMsg) {
//                ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
//            }
//
//            @Override
//            public void onSuccess(String result) {
//                __pc__ = result;
//            }
//        });
//    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {
                presenterRouter.create(WXGDListAPI.class).listWxgds(pageIndex, queryParam,false);
            }
        });
        workStateRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            generateFilterWorkflow(checkedId);
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
        map.put("workRecord.createPositionId", EamApplication.getAccountInfo().positionId);
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
    public void doSearch(String eamName) {
        queryParam.put(Constant.BAPQuery.EAM_CODE, eamName);
        refreshListController.refreshBegin();
    }

    /**
     * @param
     * @return
     * @description 封装工作流状态过滤条件
     * @author zhangwenshuai1 2018/8/14
     */
    @SuppressLint("CheckResult")
    private void generateFilterWorkflow(int checkedId) {
        String workStateId = "";
        if (checkedId == R.id.dispatchRBtn){
            workStateId = Constant.WorkState_ENG.DISPATCH;
        }else if (checkedId == R.id.executingRBtn){
            workStateId = Constant.WorkState_ENG.EXECUTE;
        }else if (checkedId == R.id.acceptanceRBtn){
            workStateId = Constant.WorkState_ENG.ACCEPT;
        }
        queryParam.put(Constant.BAPQuery.WORK_STATE, workStateId);
        doFilter();
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
    }

    @Override
    public void listWxgdsFailed(String errorMsg) {
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
        refreshListController.refreshComplete();
    }

    @Override
    public void submitSuccess(BapResultEntity entity) {
        onLoadSuccessAndExit("接单成功", () -> refreshListController.refreshBegin());
    }

    @Override
    public void submitFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
