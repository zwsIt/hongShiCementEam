package com.supcon.mes.module_warn.ui;

import android.annotation.SuppressLint;
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
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.KeyExpandHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_warn.R;
import com.supcon.mes.module_warn.model.api.DelayRecordAPI;
import com.supcon.mes.module_warn.model.bean.DelayRecordEntity;
import com.supcon.mes.module_warn.model.bean.DelayRecordListEntity;
import com.supcon.mes.module_warn.model.contract.DelayRecordContract;
import com.supcon.mes.module_warn.presenter.DelayRecordPresenter;
import com.supcon.mes.module_warn.ui.adapter.DelayRecordAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/29
 * ------------- Description -------------
 * 延期列表
 */
@Router(Constant.Router.DELAY_RECORD)
@Presenter(value = DelayRecordPresenter.class)
public class DelayRecordActivity extends BaseRefreshRecyclerActivity<DelayRecordEntity> implements DelayRecordContract.View {

    @BindByTag("leftBtn")
    AppCompatImageButton leftBtn;

    @BindByTag("customSearchView")
    CustomSearchView titleSearchView;

    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;

    @BindByTag("contentView")
    RecyclerView contentView;

    private Map<String, Object> queryParam = new HashMap<>();
    private String ids, sourceType, url;

    @Override
    protected void onInit() {
        super.onInit();
        ids = getIntent().getStringExtra(Constant.IntentKey.WARN_SOURCE_IDS);
        sourceType = getIntent().getStringExtra(Constant.IntentKey.WARN_SOURCE_TYPE);
        url = getIntent().getStringExtra(Constant.IntentKey.WARN_SOURCE_URL);
    }

    @Override
    protected IListAdapter createAdapter() {
        DelayRecordAdapter delayRecordAdapter = new DelayRecordAdapter(this);
        return delayRecordAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_delay_record_list;
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(15));
        //设置搜索框默认提示语
        titleSearchView.setHint("请输入设备");
        searchTitleBar.setBackgroundResource(R.color.gradient_start);
        searchTitleBar.disableRightBtn();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> onBackPressed());

        searchTitleBar.setOnExpandListener(isExpand -> {
            if (isExpand) {
                titleSearchView.setHint("设备名称或编码");
                titleSearchView.setInputTextColor(com.supcon.mes.middleware.R.color.hintColor);
            } else {
                titleSearchView.setHint("搜索");
                titleSearchView.setInputTextColor(com.supcon.mes.middleware.R.color.black);
            }
        });
        RxTextView.textChanges(titleSearchView.editText())
                .skipInitialValue()
                .subscribe(charSequence -> {
                    if (TextUtils.isEmpty(charSequence)) {
                        doSearch(charSequence.toString());
                    }
                });
        KeyExpandHelper.doActionSearch(titleSearchView.editText(), true, () ->
                doSearch(titleSearchView.getInput()));

        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {
                queryParam.put("page.pageNo", pageIndex);
                queryParam.put("jwxItemId", ids);
                queryParam.put("workProperty", sourceType);
                presenterRouter.create(DelayRecordAPI.class).delayRecords(url, queryParam);
            }
        });
    }

    /**
     * @param
     * @return
     * @description 搜索
     * @author zhangwenshuai1 2018/9/19
     */
    private void doSearch(String searchContent) {
        if (queryParam.containsKey(Constant.BAPQuery.EAM_NAME)) {
            queryParam.remove(Constant.BAPQuery.EAM_NAME);
        }
        if (queryParam.containsKey(Constant.BAPQuery.EAM_CODE)) {
            queryParam.remove(Constant.BAPQuery.EAM_CODE);
        }
        if (Util.isContainChinese(searchContent)) {
            queryParam.put(Constant.BAPQuery.EAM_NAME, searchContent);
        } else {
            queryParam.put(Constant.BAPQuery.EAM_CODE, searchContent);
        }
        refreshListController.refreshBegin();
    }

    @Override
    public void delayRecordsSuccess(DelayRecordListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void delayRecordsFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, ErrorMsgHelper.msgParse(errorMsg));
        refreshListController.refreshComplete(null);
    }
}
