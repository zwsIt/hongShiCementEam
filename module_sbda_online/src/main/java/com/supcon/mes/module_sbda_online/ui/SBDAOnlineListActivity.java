package com.supcon.mes.module_sbda_online.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomFilterView;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.ScreenEntity;
import com.supcon.mes.middleware.model.event.NFCEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.KeyExpandHelper;
import com.supcon.mes.middleware.util.NFCHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_sbda_online.IntentRouter;
import com.supcon.mes.module_sbda_online.R;
import com.supcon.mes.module_sbda_online.model.api.SBDAOnlineListAPI;
import com.supcon.mes.module_sbda_online.model.api.ScreenAreaAPI;
import com.supcon.mes.module_sbda_online.model.api.ScreenTypeAPI;
import com.supcon.mes.module_sbda_online.model.bean.SBDAOnlineEntity;
import com.supcon.mes.module_sbda_online.model.bean.SBDAOnlineListEntity;
import com.supcon.mes.module_sbda_online.model.contract.SBDAOnlineListContract;
import com.supcon.mes.module_sbda_online.presenter.SBDAOnlineListPresenter;
import com.supcon.mes.module_sbda_online.presenter.ScreenAreaPresenter;
import com.supcon.mes.module_sbda_online.presenter.ScreenTypePresenter;
import com.supcon.mes.module_sbda_online.screen.FilterHelper;
import com.supcon.mes.module_sbda_online.ui.adapter.SBDAOnlineListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;


/**
 * Environment: hongruijun
 * Created by Xushiyun on 2018/3/30.
 */
@Router(Constant.Router.SBDA_ONLINE_LIST)
@Presenter(value = {SBDAOnlineListPresenter.class, ScreenAreaPresenter.class, ScreenTypePresenter.class})
public class SBDAOnlineListActivity extends BaseRefreshRecyclerActivity<SBDAOnlineEntity> implements SBDAOnlineListContract.View {
    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("leftBtn")
    AppCompatImageButton leftBtn;

    @BindByTag("customSearchView")
    CustomSearchView titleSearchView;

    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;

    @BindByTag("listTypeFilter")
    CustomFilterView listTypeFilter;

    @BindByTag("listAreaFilter")
    CustomFilterView listAreaFilter;

    @BindByTag("listStatusFilter")
    CustomFilterView listStatusFilter;

    private final Map<String, Object> queryParam = new HashMap<>();

    private SBDAOnlineListAdapter mSBDAListAdapter;
    private String selecStr;

    private NFCHelper nfcHelper;

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        nfcHelper = NFCHelper.getInstance();
        if (nfcHelper != null) {
            nfcHelper.setup(this);
            nfcHelper.setOnNFCListener(new NFCHelper.OnNFCListener() {
                @Override
                public void onNFCReceived(String nfc) {
                    LogUtil.d("NFC Received : " + nfc);
                    EventBus.getDefault().post(new NFCEvent(nfc));
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (nfcHelper != null)
            nfcHelper.onResumeNFC(this);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);

        //设置搜索框默认提示语
        titleSearchView.setHint("请输入设备编码");
        searchTitleBar.setBackgroundResource(R.color.gradient_start);
        searchTitleBar.disableRightBtn();
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));

        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(5));

        initFilterView();
    }

    private void initFilterView() {
        presenterRouter.create(ScreenAreaAPI.class).screenPart(listAreaFilter);
        presenterRouter.create(ScreenTypeAPI.class).screenPart(listTypeFilter);
        listStatusFilter.setData(FilterHelper.createStateFilter());

    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_sbda_online_list;
    }

    @Override
    public void getSearchSBDASuccess(SBDAOnlineListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void getSearchSBDAFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, ErrorMsgHelper.msgParse(errorMsg));
        refreshListController.refreshComplete(null);
    }

    @Override
    protected IListAdapter<SBDAOnlineEntity> createAdapter() {
        mSBDAListAdapter = new SBDAOnlineListAdapter(this);
        return mSBDAListAdapter;
    }


    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        refreshListController.setOnRefreshPageListener((page) -> {

            if (queryParam.containsKey(Constant.BAPQuery.EAM_CODE)) {
                queryParam.remove(Constant.BAPQuery.EAM_CODE);
            }
            if (!TextUtils.isEmpty(selecStr)) {
                queryParam.put(Constant.BAPQuery.EAM_CODE, selecStr);
            }
            queryParam.put(Constant.BAPQuery.IS_MAIN_EQUIP, "1");
            presenterRouter.create(SBDAOnlineListAPI.class).getSearchSBDA(queryParam, page);
        });
        //点击触发事件跳转界面
        mSBDAListAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            SBDAOnlineEntity sbdaOnlineEntity = (SBDAOnlineEntity) obj;
            Bundle bundle = new Bundle();
            bundle.putLong(Constant.IntentKey.SBDA_ONLINE_EAMID, sbdaOnlineEntity.id);
            bundle.putString(Constant.IntentKey.SBDA_ONLINE_EAMCODE, sbdaOnlineEntity.code);
            IntentRouter.go(context, Constant.Router.SBDA_ONLINE_VIEW, bundle);
        });
        leftBtn.setOnClickListener(v -> onBackPressed());

        RxTextView.textChanges(titleSearchView.editText())
                .skipInitialValue()
                .subscribe(charSequence -> {
                    if (TextUtils.isEmpty(charSequence)) {
                        doSearchTableNo(charSequence.toString());
                    }
                });
        KeyExpandHelper.doActionSearch(titleSearchView.editText(), true, () ->
                doSearchTableNo(titleSearchView.getInput()));

        listTypeFilter.setFilterSelectChangedListener(filterBean -> {
            queryParam.put(Constant.BAPQuery.EAM_TYPE, ((ScreenEntity) filterBean).code != null ? ((ScreenEntity) filterBean).code : "");
            doRefresh();
        });
        listStatusFilter.setFilterSelectChangedListener(filterBean -> {
            queryParam.put(Constant.BAPQuery.EAM_STATE, ((ScreenEntity) filterBean).layRec != null ? ((ScreenEntity) filterBean).layRec : "");
            doRefresh();
        });
        listAreaFilter.setFilterSelectChangedListener(filterBean -> {
            queryParam.put(Constant.BAPQuery.EAM_AREA, ((ScreenEntity) filterBean).id != null ? ((ScreenEntity) filterBean).id : "");
            doRefresh();
        });

    }

    public void doSearchTableNo(String search) {
        selecStr = search;
        refreshListController.refreshBegin();
    }

    /**
     * 进行过滤查询
     */
    private void doRefresh() {
        refreshListController.refreshBegin();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(LoginEvent loginEvent) {

        refreshListController.refreshBegin();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(RefreshEvent event) {

        refreshListController.refreshBegin();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getNFC(NFCEvent nfcEvent) {
        LogUtil.d("NFC_TAG", nfcEvent.getNfc());
        Map<String, Object> nfcJson = Util.gsonToMaps(nfcEvent.getNfc());
        if (nfcJson.get("textRecord") == null) {
            ToastUtils.show(context, "标签内容空！");
            return;
        }
        titleSearchView.setInput((String) nfcJson.get("textRecord"));
        doSearchTableNo((String) nfcJson.get("textRecord"));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //获取到Tag对象
        if (nfcHelper != null)
            nfcHelper.dealNFCTag(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (nfcHelper != null)
            nfcHelper.onPauseNFC(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (nfcHelper != null) {
            nfcHelper.release();
        }
    }

}
