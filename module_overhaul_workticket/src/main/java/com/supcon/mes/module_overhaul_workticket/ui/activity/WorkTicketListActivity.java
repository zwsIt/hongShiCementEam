package com.supcon.mes.module_overhaul_workticket.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.ptr.PtrFrameLayout;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.beans.FilterBean;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomFilterView;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.constant.QueryBtnType;
import com.supcon.mes.middleware.controller.ModulePermissonCheckController;
import com.supcon.mes.middleware.model.bean.CustomFilterBean;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.FilterHelper;
import com.supcon.mes.middleware.util.SystemCodeManager;
import com.supcon.mes.module_overhaul_workticket.IntentRouter;
import com.supcon.mes.module_overhaul_workticket.R;
import com.supcon.mes.module_overhaul_workticket.model.api.WorkTicketListAPI;
import com.supcon.mes.module_overhaul_workticket.model.bean.WorkTicketEntity;
import com.supcon.mes.module_overhaul_workticket.model.bean.WorkTicketList;
import com.supcon.mes.module_overhaul_workticket.model.contract.WorkTicketListContract;
import com.supcon.mes.module_overhaul_workticket.presenter.WorkTicketListPresenter;
import com.supcon.mes.module_overhaul_workticket.ui.adapter.WorkTicketAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/10
 * Email zhangwenshuai1@supcon.com
 * Desc 检修票列表
 */
@Router(value = Constant.Router.OVERHAUL_WORKTICKET_LIST)
@Controller(value = {ModulePermissonCheckController.class})
@Presenter(value = {WorkTicketListPresenter.class})
public class WorkTicketListActivity extends BaseRefreshRecyclerActivity<WorkTicketEntity> implements WorkTicketListContract.View {

    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("rightBtn")
    ImageButton rightBtn;
    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;
    @BindByTag("radioGroupFilter")
    RadioGroup radioGroupFilter;
    @BindByTag("contentView")
    RecyclerView contentView;
    @BindByTag("riskAssessmentFilter")
    CustomFilterView<FilterBean> riskAssessmentFilter;

    private Map<String,Object> queryParam = new HashMap<>();
    private boolean pendingQuery = true;
    private WorkTicketAdapter mWorkTicketAdapter;
    private Long deploymentId;

    @Override
    protected void onInit() {
        super.onInit();
        StatusBarUtils.setWindowStatusBarColor(this,R.color.themeColor);
        EventBus.getDefault().register(this);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(5,context)));

        queryParam.put(Constant.BAPQuery.RISK_ASSESSMENT,"");
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_work_ticket_list;
    }

    @Override
    protected IListAdapter<WorkTicketEntity> createAdapter() {
        mWorkTicketAdapter = new WorkTicketAdapter(context);
        return mWorkTicketAdapter;
    }

    @Override
    protected void initView() {
        super.initView();
        searchTitleBar.searchView().setHint("请输入设备编码");
        searchTitleBar.searchView().setHint(getString(R.string.middleware_input_eam_code));
        searchTitleBar.searchView().setInputTextColor(R.color.black);
        FilterHelper.addView(this,radioGroupFilter,FilterHelper.queryBtn());
        getController(ModulePermissonCheckController.class).checkModulePermission(EamApplication.getUserName().toLowerCase(), "workTicketFW", result -> {
            if (result == null){
                searchTitleBar.disableRightBtn();
            }
            deploymentId = result;
        },null);
    }

    @Override
    protected void initData() {
        super.initData();
//        mRiskAssessmentList = SystemCodeManager.getInstance().getSystemCodeListByCode(Constant.SystemCode.RISK_ASSESSMENT);
        riskAssessmentFilter.setData(FilterHelper.createFilterBySystemCode(Constant.SystemCode.RISK_ASSESSMENT));
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> onBackPressed());
        RxView.clicks(rightBtn)
                .throttleFirst(500,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        // 制定单据
                        Bundle bundle = new Bundle();
                        bundle.putLong(Constant.IntentKey.DEPLOYMENT_ID, deploymentId);
                        IntentRouter.go(context,Constant.Router.OVERHAUL_WORKTICKET_EDIT,bundle);
                    }
                });
        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {
                presenterRouter.create(WorkTicketListAPI.class).listWorkTickets(pageIndex,queryParam,pendingQuery);
            }
        });
        RxTextView.textChanges(searchTitleBar.editText())
                .skipInitialValue()
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        queryParam.put(Constant.BAPQuery.EAM_CODE,charSequence.toString().trim());
                        doFilter();
                    }
                });

        radioGroupFilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == QueryBtnType.ALL_QUERY.getType()){
                    pendingQuery = false;
                }else {
                    pendingQuery = true;
                }
                doFilter();
            }
        });
        riskAssessmentFilter.setFilterSelectChangedListener(new CustomFilterView.FilterSelectChangedListener<FilterBean>() {
            @Override
            public void onFilterSelected(FilterBean filterBean) {
                CustomFilterBean bean = (CustomFilterBean) filterBean;
                queryParam.put(Constant.BAPQuery.RISK_ASSESSMENT,bean.id);
                refreshListController.refreshBegin();
            }
        });
    }

    private void doFilter() {
        refreshListController.refreshBegin();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshEvent event){
        refreshListController.refreshBegin();
    }

    @Override
    public void listWorkTicketsSuccess(WorkTicketList entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void listWorkTicketsFailed(String errorMsg) {
        ToastUtils.show(context,ErrorMsgHelper.msgParse(errorMsg));
        refreshListController.refreshComplete();
    }
}
