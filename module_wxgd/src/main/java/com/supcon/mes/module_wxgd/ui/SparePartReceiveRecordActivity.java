package com.supcon.mes.module_wxgd.ui;

import android.annotation.SuppressLint;
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
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.KeyExpandHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_wxgd.R;
import com.supcon.mes.module_wxgd.model.api.SparePartReceiveRecordAPI;
import com.supcon.mes.module_wxgd.model.api.SparePartsConsumeLedgerAPI;
import com.supcon.mes.module_wxgd.model.api.WXGDStatisticsAPI;
import com.supcon.mes.module_wxgd.model.bean.SparePartsConsumeEntity;
import com.supcon.mes.module_wxgd.model.contract.SparePartReceiveRecordContract;
import com.supcon.mes.module_wxgd.presenter.SparePartReceiveRecordPresenter;
import com.supcon.mes.module_wxgd.ui.adapter.SparePartReceiveRecordAdapter;

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
 * 备件领用记录
 */
@Router(Constant.Router.SPARE_PART_RECEIVE_RECORD)
@Presenter(value = {SparePartReceiveRecordPresenter.class})
public class SparePartReceiveRecordActivity extends BaseRefreshRecyclerActivity<SparePartsConsumeEntity> implements SparePartReceiveRecordContract.View {

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
    private SparePartReceiveRecordAdapter sparePartReceiveRecordAdapter;

    @Override
    protected IListAdapter createAdapter() {
        sparePartReceiveRecordAdapter = new SparePartReceiveRecordAdapter(this);
        return sparePartReceiveRecordAdapter;
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
        return R.layout.ac_sparepart_receive_record;
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
            presenterRouter.create(SparePartReceiveRecordAPI.class).sparePartList(queryParam, page);
        });
        leftBtn.setOnClickListener(v -> onBackPressed());
    }

    public void doSearchTableNo(String search) {
        selecStr = search;
        refreshListController.refreshBegin();
    }

    @Override
    public void sparePartListSuccess(CommonBAPListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void sparePartListFailed(String errorMsg) {
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
