package com.supcon.mes.middleware.ui;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.api.RefProductAPI;
import com.supcon.mes.middleware.model.bean.Good;
import com.supcon.mes.middleware.model.bean.RefProductListEntity;
import com.supcon.mes.middleware.model.bean.SparePartRefEntity;
import com.supcon.mes.middleware.model.bean.SparePartRefListEntity;
import com.supcon.mes.middleware.model.contract.RefProductContract;
import com.supcon.mes.middleware.model.event.SparePartAddEvent;
import com.supcon.mes.middleware.presenter.RefProductPresenter;
import com.supcon.mes.middleware.ui.adapter.RefProductAdapter;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.Util;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * @author yangfei.cao
 * @ClassName RefProductListActivity
 * @date 2018/9/5
 * ------------- Description -------------
 * 备件参照
 */
@Router(Constant.Router.SPARE_PART_REF)
@Presenter(RefProductPresenter.class)
public class RefProductListActivity extends BaseRefreshRecyclerActivity<SparePartRefEntity> implements RefProductContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("leftBtn")
    ImageButton leftBtn;

    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;
    @BindByTag("customSearchView")
    CustomSearchView customSearchView;

    private RefProductAdapter refProductAdapter;
    private Map<String, Object> queryParam = new HashMap<>();
    private boolean isSparePartRef;
    private Long eamID;
    private ArrayList<String> addedSPList; // 已添加数据
    private String selectType; // 搜索字段

    @Override
    protected IListAdapter createAdapter() {
        refProductAdapter = new RefProductAdapter(this);
        return refProductAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_recycle;
    }

    @Override
    protected void onInit() {
        super.onInit();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(3, context)));
        contentView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);

        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setLoadMoreEnable(true);

        isSparePartRef = getIntent().getBooleanExtra(Constant.IntentKey.IS_SPARE_PART_REF, false);
        eamID = getIntent().getLongExtra(Constant.IntentKey.EAM_ID, 0);
        addedSPList = getIntent().getStringArrayListExtra(Constant.IntentKey.ADD_DATA_LIST);
    }

    @Override
    protected void initView() {
        super.initView();
        searchTitleBar.setTitleText(isSparePartRef ? "常用备件参照" : "备件参照");
        searchTitleBar.disableRightBtn();
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, "列表为空"));
        customSearchView.setHint("搜索");

        searchTitleBar.setSelectedTypeEnabled(true);
        searchTitleBar.setDefaultType("名称");
        selectType = "备件名称";
        searchTitleBar.setContentList(Arrays.asList("名称", "规格", "型号"));
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> {
            back();
        });
        refreshListController.setOnRefreshPageListener(pageIndex -> {
            if (isSparePartRef) {
                presenterRouter.create(RefProductAPI.class).listRefSparePart(pageIndex, eamID, queryParam);
            } else {
                presenterRouter.create(RefProductAPI.class).listRefProduct(pageIndex, queryParam);
            }
        });
        refProductAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            SparePartRefEntity item = refProductAdapter.getItem(position);
            if (addedSPList.contains(item.getProductID().id.toString())) {
                ToastUtils.show(context, "请勿重复添加备件!");
                return;
            }
            EventBus.getDefault().post(new SparePartAddEvent(isSparePartRef, item));
            RefProductListActivity.this.finish();
        });
        searchTitleBar.setOnExpandListener(isExpand -> {
            if (isExpand) {
                customSearchView.setHint(selectType);
//                customSearchView.setInputTextColor(R.color.hintColor);
            } else {
                customSearchView.setHint("搜索");
                customSearchView.setInputTextColor(R.color.black);
            }
        });
        searchTitleBar.setOnTypeSelectListener(new CustomSearchView.OnTypeSelectListener() {
            @Override
            public void onTypeSelect(String type) {
                selectType = "备件" + type;
                customSearchView.setHint(selectType);
                doSearch(customSearchView.getInput().trim());
            }
        });
        RxTextView.textChanges(customSearchView.editText())
                .skipInitialValue()
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        doSearch(charSequence.toString().trim());
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
        queryParam.remove(Constant.BAPQuery.PRODUCT_NAME);
        queryParam.remove(Constant.BAPQuery.PRODUCT_SPECIF);
        queryParam.remove(Constant.BAPQuery.PRODUCT_MODEL);
        switch (selectType) {
            case "备件名称":
                queryParam.put(Constant.BAPQuery.PRODUCT_NAME, searchContent);
                break;
            case "备件规格":
                queryParam.put(Constant.BAPQuery.PRODUCT_SPECIF, searchContent);
                break;
            case "备件型号":
                queryParam.put(Constant.BAPQuery.PRODUCT_MODEL, searchContent);
                break;
            default:
                break;
        }
        refreshListController.refreshBegin();

    }

    @SuppressLint("CheckResult")
    @Override
    public void listRefProductSuccess(RefProductListEntity entity) {
        List<Good> result = entity.result;
        List<SparePartRefEntity> sparePartRefEntities = new LinkedList<>();
        Flowable.fromIterable(result)
                .map(good -> {
                    SparePartRefEntity sparePartRefEntity = new SparePartRefEntity();
                    sparePartRefEntity.setProductID(good);
                    sparePartRefEntity.setStandingCrop(good.standingCrop); // 通过自定义字段获取现存量
                    return sparePartRefEntity;
                })
                .subscribe(sparePartRefEntity -> {
                    sparePartRefEntities.add(sparePartRefEntity);
                }, throwable -> {
                }, () -> refreshListController.refreshComplete(sparePartRefEntities));

    }

    @Override
    public void listRefProductFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
        refreshListController.refreshComplete();
    }

    @Override
    public void listRefSparePartSuccess(SparePartRefListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void listRefSparePartFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
        refreshListController.refreshComplete();
    }
}
