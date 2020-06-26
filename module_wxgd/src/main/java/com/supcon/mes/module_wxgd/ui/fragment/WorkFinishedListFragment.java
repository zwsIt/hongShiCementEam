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
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.com_http.util.RxSchedulers;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshPageListener;
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
 * WorkPendingListFragment 维修工单已结束列表
 * created by zhangwenshuai1 2020/5/29
 */

@Presenter(value = {WXGDListPresenter.class})
public class WorkFinishedListFragment extends BaseRefreshRecyclerFragment<WXGDEntity> implements WXGDListContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;

    Map<String, Object> queryParam = new HashMap<>();
    private WXGDSubmitController wxgdSubmitController;
    private RoleController roleController;

    @Override
    protected IListAdapter createAdapter() {
        WXGDListAdapter wxgdListAdapter = new WXGDListAdapter(context);
        return wxgdListAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.frag_list;
    }

    @Override
    protected void onInit() {
        super.onInit();
        contentView.setLayoutManager(new LinearLayoutManager(getActivity()));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(5,context)));
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        refreshListController.setOnRefreshPageListener(pageIndex -> {
            queryParam.put(Constant.BAPQuery.WORK_STATE,Constant.WorkState_ENG.TAKE_EFFECT);
            presenterRouter.create(WXGDListAPI.class).listWxgds(pageIndex, queryParam,true);
        });
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
        ToastUtils.show(context, errorMsg);
        refreshListController.refreshComplete();
    }
}
