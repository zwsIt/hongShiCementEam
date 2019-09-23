package com.supcon.mes.middleware.ui;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.api.RefMaintainAPI;
import com.supcon.mes.middleware.model.bean.RefMaintainEntity;
import com.supcon.mes.middleware.model.bean.RefMaintainListEntity;
import com.supcon.mes.middleware.model.contract.RefMaintainContract;
import com.supcon.mes.middleware.presenter.RefMaintainPresenter;
import com.supcon.mes.middleware.ui.adapter.RefMaintainAdapter;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.KeyExpandHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yangfei.cao
 * @ClassName LubricateActivity
 * @date 2018/9/5
 * ------------- Description -------------
 * 维保参照列表
 */
@Presenter(RefMaintainPresenter.class)
@Router(Constant.Router.MAINTAIN_REF)
public class RefMaintenanceActivity extends BaseRefreshRecyclerActivity<RefMaintainEntity> implements RefMaintainContract.View {
    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("leftBtn")
    ImageButton leftBtn;

    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;
    @BindByTag("customSearchView")
    CustomSearchView customSearchView;


    private RefMaintainAdapter maintainAdapter;
    private Map<String, Object> queryParam = new HashMap<>();
    private Long eamID;

    @Override
    protected IListAdapter createAdapter() {
        maintainAdapter = new RefMaintainAdapter(this);
        return maintainAdapter;
    }

    @Override
    protected void onInit() {
        super.onInit();
        eamID = getIntent().getLongExtra(Constant.IntentKey.EAM_ID, 0);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_recycle;
    }

    @Override
    protected void initView() {
        super.initView();
        searchTitleBar.setTitleText("维保业务参照");
        searchTitleBar.disableRightBtn();
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setLoadMoreEnable(true);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(3, context)));
        contentView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, "列表为空"));
        customSearchView.setHint("搜索");
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> {
            back();
        });
        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {
                presenterRouter.create(RefMaintainAPI.class).listRefMaintain(pageIndex, eamID, queryParam);
            }
        });
        maintainAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                RefMaintainEntity refMaintainEntity = maintainAdapter.getItem(position);
                EventBus.getDefault().post(refMaintainEntity);
                RefMaintenanceActivity.this.finish();
            }
        });
        searchTitleBar.setOnExpandListener(isExpand -> {
            if (isExpand) {
                customSearchView.setHint("内容");
                customSearchView.setInputTextColor(R.color.hintColor);
            } else {
                customSearchView.setHint("搜索");
                customSearchView.setInputTextColor(R.color.black);
            }
        });
        RxTextView.textChanges(customSearchView.editText())
                .skipInitialValue()
                .subscribe(charSequence -> {
                    if (TextUtils.isEmpty(charSequence)) {
                        doSearch(charSequence.toString().trim());
                    }
                });
        KeyExpandHelper.doActionSearch(customSearchView.editText(), true, () ->
                doSearch(customSearchView.getInput()));
    }

    /**
     * @param
     * @return
     * @description 搜索
     * @author zhangwenshuai1 2018/9/19
     */
    private void doSearch(String searchContent) {
        if (queryParam.containsKey(Constant.BAPQuery.CONTENT)) {
            queryParam.remove(Constant.BAPQuery.CONTENT);
        }
        queryParam.put(Constant.BAPQuery.CONTENT, searchContent);
        refreshListController.refreshBegin();
    }

    @Override
    public void listRefMaintainSuccess(RefMaintainListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void listRefMaintainFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
        refreshListController.refreshComplete();
    }
}
