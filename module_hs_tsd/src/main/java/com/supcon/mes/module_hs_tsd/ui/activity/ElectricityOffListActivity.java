package com.supcon.mes.module_hs_tsd.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.ptr.PtrFrameLayout;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.constant.QueryBtnType;
import com.supcon.mes.middleware.controller.ModulePermissonCheckController;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.FilterHelper;
import com.supcon.mes.module_hs_tsd.IntentRouter;
import com.supcon.mes.module_hs_tsd.R;
import com.supcon.mes.module_hs_tsd.model.api.ElectricityOffListAPI;
import com.supcon.mes.module_hs_tsd.model.bean.ElectricityOffOnEntity;
import com.supcon.mes.module_hs_tsd.model.bean.ElectricityOffOnListEntity;
import com.supcon.mes.module_hs_tsd.model.contract.ElectricityOffListContract;
import com.supcon.mes.module_hs_tsd.presenter.ElectricityOffListPresenter;
import com.supcon.mes.module_hs_tsd.ui.adapter.ElectricityOffListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/27
 * Email zhangwenshuai1@supcon.com
 * Desc 停电列表
 */
@Router(value = Constant.Router.HS_TD_LIST)
@Controller(value = {ModulePermissonCheckController.class})
@Presenter(value = {ElectricityOffListPresenter.class})
public class ElectricityOffListActivity extends BaseRefreshRecyclerActivity<ElectricityOffOnEntity> implements ElectricityOffListContract.View {


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
    @BindByTag("refreshFrameLayout")
    PtrFrameLayout refreshFrameLayout;

    private Map<String,Object> queryParam = new HashMap<>();
    private boolean pendingQuery = true;
    private ElectricityOffListAdapter mElectricityOffListAdapter;
    private Long deploymentId;

    @Override
    protected void onInit() {
        super.onInit();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        EventBus.getDefault().register(this);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(5,context)));
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_elec_off_list;
    }

    @Override
    protected IListAdapter<ElectricityOffOnEntity> createAdapter() {
        mElectricityOffListAdapter = new ElectricityOffListAdapter(context);
        return mElectricityOffListAdapter;
    }

    @Override
    protected void initView() {
        super.initView();
        searchTitleBar.searchView().setHint("请输入设备名称");
        searchTitleBar.searchView().setInputTextColor(R.color.black);
        FilterHelper.addView(this,radioGroupFilter,FilterHelper.queryBtn());
        getController(ModulePermissonCheckController.class).checkModulePermission(EamApplication.getUserName().toLowerCase(), "EleOnWorkFlow", result -> {
            if (result == null){
                searchTitleBar.disableRightBtn();
            }
            deploymentId = result;
        },null);
    }

    @Override
    protected void initData() {
        super.initData();
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
                        IntentRouter.go(context,Constant.Router.HS_ELE_OFF_EDIT,bundle);
                    }
                });
        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {
                presenterRouter.create(ElectricityOffListAPI.class).listElectricityOff(pageIndex,queryParam,pendingQuery);
            }
        });
        RxTextView.textChanges(searchTitleBar.editText())
                .skipInitialValue()
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        queryParam.put(Constant.BAPQuery.EAM_NAME,charSequence.toString().trim());
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
    public void listElectricityOffSuccess(ElectricityOffOnListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void listElectricityOffFailed(String errorMsg) {
        ToastUtils.show(context,ErrorMsgHelper.msgParse(errorMsg));
        refreshListController.refreshComplete();
    }
}
