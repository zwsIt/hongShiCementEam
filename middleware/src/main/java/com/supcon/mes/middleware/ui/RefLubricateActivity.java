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
import com.supcon.mes.mbap.listener.OnTitleSearchExpandListener;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.api.RefLubricateAPI;
import com.supcon.mes.middleware.model.bean.LubricateListEntity;
import com.supcon.mes.middleware.model.bean.LubricateOil;
import com.supcon.mes.middleware.model.bean.RefLubricateEntity;
import com.supcon.mes.middleware.model.bean.RefLubricateListEntity;
import com.supcon.mes.middleware.model.contract.RefLubricateContract;
import com.supcon.mes.middleware.presenter.RefLubricatePresenter;
import com.supcon.mes.middleware.ui.adapter.RefLubricateAdapter;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.KeyExpandHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * @author yangfei.cao
 * @ClassName RefLubricateActivity
 * @date 2018/9/5
 * ------------- Description -------------
 * 润滑油选择
 */
@Presenter(RefLubricatePresenter.class)
@Router(Constant.Router.LUBRICATE_REF)
public class RefLubricateActivity extends BaseRefreshRecyclerActivity<RefLubricateEntity> implements RefLubricateContract.View {
    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("leftBtn")
    ImageButton leftBtn;

    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;
    @BindByTag("customSearchView")
    CustomSearchView customSearchView;


    private RefLubricateAdapter lubricateAdapter;
    private Map<String, Object> queryParam = new HashMap<>();
    private Long eamID;
    private boolean isSparePartRef;

    @Override
    protected IListAdapter createAdapter() {
        lubricateAdapter = new RefLubricateAdapter(this);
        return lubricateAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_recycle;
    }

    @Override
    protected void onInit() {
        super.onInit();
        eamID = getIntent().getLongExtra(Constant.IntentKey.EAM_ID, 0);
        isSparePartRef = getIntent().getBooleanExtra(Constant.IntentKey.IS_SPARE_PART_REF, false);
    }

    @Override
    protected void initView() {
        super.initView();
        searchTitleBar.setTitleText(isSparePartRef?"润滑油业务参照":"润滑油参照");
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
                if (isSparePartRef) {
                    presenterRouter.create(RefLubricateAPI.class).listRefLubricate(pageIndex, eamID, queryParam);
                } else {
                    presenterRouter.create(RefLubricateAPI.class).listLubricate(pageIndex, queryParam);
                }
            }
        });
        lubricateAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                RefLubricateEntity refLubricateEntity = lubricateAdapter.getItem(position);
                EventBus.getDefault().post(refLubricateEntity);
                RefLubricateActivity.this.finish();
            }
        });
        searchTitleBar.setOnExpandListener(new OnTitleSearchExpandListener() {
            @Override
            public void onTitleSearchExpand(boolean isExpand) {
                if (isExpand) {
                    customSearchView.setHint("润滑油名称");
                    customSearchView.setInputTextColor(R.color.hintColor);
                } else {
                    customSearchView.setHint("搜索");
                    customSearchView.setInputTextColor(R.color.black);
                }
            }
        });
        RxTextView.textChanges(customSearchView.editText())
                .skipInitialValue()
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        if (TextUtils.isEmpty(charSequence)) {
                            doSearch(charSequence.toString().trim());
                        }
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
        if (queryParam.containsKey(Constant.BAPQuery.NAME)) {
            queryParam.remove(Constant.BAPQuery.NAME);
        }
        queryParam.put(Constant.BAPQuery.NAME, searchContent);
        refreshListController.refreshBegin();
    }

    @SuppressLint("CheckResult")
    @Override
    public void listLubricateSuccess(LubricateListEntity entity) {
        List<LubricateOil> result = entity.result;
        List<RefLubricateEntity> refLubricateEntities = new LinkedList<>();
        Flowable.fromIterable(result)
                .map(lubricateOil -> {
                    RefLubricateEntity refLubricateEntity = new RefLubricateEntity();
                    refLubricateEntity.setLubricateOil(lubricateOil);
                    return refLubricateEntity;
                })
                .subscribe(refLubricateEntity -> {
                    refLubricateEntities.add(refLubricateEntity);
                }, throwable -> {
                }, () -> refreshListController.refreshComplete(refLubricateEntities));
    }

    @Override
    public void listLubricateFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
        refreshListController.refreshComplete();
    }

    @Override
    public void listRefLubricateSuccess(RefLubricateListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void listRefLubricateFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
        refreshListController.refreshComplete();
    }

}
