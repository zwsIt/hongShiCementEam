package com.supcon.mes.module_wxgd.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.WXGDEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.KeyExpandHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_wxgd.IntentRouter;
import com.supcon.mes.module_wxgd.R;
import com.supcon.mes.module_wxgd.model.api.SparePartsConsumeLedgerAPI;
import com.supcon.mes.module_wxgd.model.api.WXGDStatisticsAPI;
import com.supcon.mes.module_wxgd.model.bean.SparePartsConsumeEntity;
import com.supcon.mes.module_wxgd.model.bean.WXGDListEntity;
import com.supcon.mes.module_wxgd.model.contract.SparePartsConsumeLedgerContract;
import com.supcon.mes.module_wxgd.model.contract.WXGDStatisticsContract;
import com.supcon.mes.module_wxgd.presenter.SparePartsConsumeLedgerPresenter;
import com.supcon.mes.module_wxgd.presenter.WXGDStatisticsPresenter;
import com.supcon.mes.module_wxgd.ui.adapter.SparePartConsumeLedgerAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/3
 * ------------- Description -------------
 * 备件消耗台账
 */
@Router(Constant.Router.SPARE_PART_CONSUME_LEDGER)
@Presenter(value = {SparePartsConsumeLedgerPresenter.class, WXGDStatisticsPresenter.class})
public class SparePartConsumeLedgerActivity extends BaseRefreshRecyclerActivity<SparePartsConsumeEntity> implements SparePartsConsumeLedgerContract.View
        , WXGDStatisticsContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("leftBtn")
    AppCompatImageButton leftBtn;

    @BindByTag("customSearchView")
    CustomSearchView titleSearchView;

    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;

    private String selecStr;
    private final Map<String, Object> queryParam = new HashMap<>();
    private SparePartConsumeLedgerAdapter sparePartLedgerAdapter;

    @Override
    protected IListAdapter createAdapter() {
        sparePartLedgerAdapter = new SparePartConsumeLedgerAdapter(this);
        return sparePartLedgerAdapter;
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
        //设置搜索框默认提示语
        titleSearchView.setHint("请输入零部件名称或编码");
        searchTitleBar.setBackgroundResource(R.color.gradient_start);
        searchTitleBar.disableRightBtn();
        searchTitleBar.setBackgroundResource(R.color.gradient_start);
        searchTitleBar.disableRightBtn();
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));

        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(5));
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_sparepart_consume_ledger;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        RxTextView.textChanges(titleSearchView.editText())
                .skipInitialValue()
                .debounce(1, TimeUnit.SECONDS)
                .subscribe(charSequence -> {
                    if (TextUtils.isEmpty(charSequence)) {
                        doSearchTableNo(charSequence.toString());
                    }
                });
        KeyExpandHelper.doActionSearch(titleSearchView.editText(), true, () ->
                doSearchTableNo(titleSearchView.getInput()));

        refreshListController.setOnRefreshPageListener((page) -> {

            queryParam.remove(Constant.BAPQuery.PRODUCT_CODE);
            queryParam.remove(Constant.BAPQuery.PRODUCT_NAME);
            if (!TextUtils.isEmpty(selecStr)) {
                if (Util.isContainChinese(selecStr)) {
                    queryParam.put(Constant.BAPQuery.PRODUCT_NAME, selecStr);
                } else {
                    queryParam.put(Constant.BAPQuery.PRODUCT_CODE, selecStr);
                }
            }
            presenterRouter.create(SparePartsConsumeLedgerAPI.class).productConsumeList(queryParam, page);
        });
        leftBtn.setOnClickListener(v -> onBackPressed());

        sparePartLedgerAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                SparePartsConsumeEntity item = sparePartLedgerAdapter.getItem(position);
                if (!TextUtils.isEmpty(item.getWorkList().tableNo)) {
                    Map<String, Object> queryParam = new HashMap<>();
                    queryParam.put(Constant.BAPQuery.TABLE_NO, item.getWorkList().tableNo);
                    onLoading("正在查询工单...");
                    presenterRouter.create(WXGDStatisticsAPI.class).listWxgds(1, queryParam);
                }
            }
        });
    }

    public void doSearchTableNo(String search) {
        selecStr = search;
        refreshListController.refreshBegin();
    }

    @Override
    public void productConsumeListSuccess(CommonBAPListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void productConsumeListFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, ErrorMsgHelper.msgParse(errorMsg));
        refreshListController.refreshComplete(null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(LoginEvent loginEvent) {

        refreshListController.refreshBegin();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(RefreshEvent event) {

        refreshListController.refreshBegin();
    }

    @Override
    public void listWxgdsSuccess(WXGDListEntity entity) {
        if (entity.result != null && entity.result.size() > 0) {
            onLoadSuccess();
            WXGDEntity wxgdEntity = entity.result.get(0);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.IntentKey.WXGD_ENTITY, wxgdEntity);
            if (wxgdEntity.pending != null && !TextUtils.isEmpty(wxgdEntity.pending.openUrl)) {
                switch (wxgdEntity.pending.openUrl) {
                    case Constant.WxgdView.RECEIVE_OPEN_URL:
                        IntentRouter.go(context, Constant.Router.WXGD_RECEIVE, bundle);
                        break;
                    case Constant.WxgdView.DISPATCH_OPEN_URL:
                        IntentRouter.go(context, Constant.Router.WXGD_DISPATCHER, bundle);
                        break;

                    case Constant.WxgdView.EXECUTE_OPEN_URL:
                        IntentRouter.go(context, Constant.Router.WXGD_EXECUTE, bundle);
                        break;
                    case Constant.WxgdView.ACCEPTANCE_OPEN_URL:
                        IntentRouter.go(context, Constant.Router.WXGD_ACCEPTANCE, bundle);
                        break;
                    default:
                        IntentRouter.go(context, Constant.Router.WXGD_EXECUTE, bundle);
                        break;
                }
            } else {
                IntentRouter.go(context, Constant.Router.WXGD_COMPLETE, bundle);
            }
        } else {
            onLoadSuccess("没有查询到工单！");
        }
    }

    @Override
    public void listWxgdsFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
