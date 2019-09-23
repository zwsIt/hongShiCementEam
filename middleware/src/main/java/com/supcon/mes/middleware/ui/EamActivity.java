package com.supcon.mes.middleware.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.CustomSwipeLayout;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.api.EamAPI;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchEntity;
import com.supcon.mes.middleware.model.contract.EamContract;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.model.event.NFCEvent;
import com.supcon.mes.middleware.presenter.EamPresenter;
import com.supcon.mes.middleware.ui.adapter.BaseSearchAdapter;
import com.supcon.mes.middleware.ui.view.PinyinSearchBar;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.KeyExpandHelper;
import com.supcon.mes.middleware.util.NFCHelper;
import com.supcon.mes.middleware.util.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Router(value = Constant.Router.EAM)
@Presenter(value = EamPresenter.class)
public class EamActivity extends BaseRefreshRecyclerActivity<CommonSearchEntity> implements EamContract.View {
    @BindByTag("contentView")
    RecyclerView recyclerView;
    /**
     * 顶部模糊搜索栏
     */
    CustomSearchView titleSearchView;
    /**
     * 顶部搜索控件
     */
    @BindByTag("titleBar")
    CustomHorizontalSearchTitleBar titleBar;

    @BindByTag("rightBtn_1")
    TextView rightBtn_1;

    @BindByTag("leftBtn")
    ImageButton leftBtn;
    /**
     * 多选模式，现在还在开发中
     * Todo:暂时不支持多选模式
     */
    private boolean isMulti;

    /**
     * 侧栏首字母导航控件
     */
    @BindByTag("pinyinSearchBar")
    PinyinSearchBar pinyinSearchBar;

    private BaseSearchAdapter mBaseSearchAdapter;

    private final Map<String, Object> queryParam = new HashMap<>();
    private String eamCode, areaName;

    private NFCHelper nfcHelper;
    private boolean isMainEam;
    private String searchTag;

    private List<CommonSearchEntity> searchEntities = new ArrayList<>();

    @Override
    protected IListAdapter<CommonSearchEntity> createAdapter() {
        mBaseSearchAdapter = new BaseSearchAdapter(this, isMulti);
        return mBaseSearchAdapter;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, "没有信息哦~"));
        eamCode = getIntent().getStringExtra(Constant.IntentKey.EAM_CODE);
        areaName = getIntent().getStringExtra(Constant.IntentKey.AREA_NAME);
        isMainEam = getIntent().getBooleanExtra(Constant.IntentKey.IS_MAIN_EAM, false);
        searchTag = getIntent().getStringExtra(Constant.IntentKey.COMMON_SEARCH_TAG);
        isMulti = getIntent().getBooleanExtra(Constant.IntentKey.IS_MULTI, false);

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
    protected int getLayoutID() {
        return R.layout.ac_sbda_search_contact;
    }

    @Override
    protected void initView() {
        super.initView();
        //设置状态栏背景色
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);

        titleSearchView = titleBar.searchView();
        titleSearchView.setHint("请输入搜索信息");

        titleBar.setTitleText("设备搜索");
        titleBar.disableRightBtn();

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(1, context)));
        recyclerView.addOnItemTouchListener(new CustomSwipeLayout.OnSwipeItemTouchListener(this));
        recyclerView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        recyclerView.setScrollBarSize(2);
        titleSearchView.setInput(eamCode);
        rightBtn_1.setVisibility(isMulti ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void initData() {
        super.initData();
        mBaseSearchAdapter.setMulti(isMulti);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        mBaseSearchAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {

            //如果当前模式为多选模式,则不响应单选模式触发返回事件
            final CommonSearchEntity commonSearchEntity = (CommonSearchEntity) obj;
            if (isMulti) {
                if (!searchEntities.contains(commonSearchEntity)) {
                    searchEntities.add(commonSearchEntity);
                } else {
                    searchEntities.remove(commonSearchEntity);
                }
                return;
            }

            CommonSearchEvent commonSearchEvent = new CommonSearchEvent();
            commonSearchEvent.commonSearchEntity = commonSearchEntity;
            commonSearchEvent.flag = searchTag;
            finish();
            EventBus.getDefault().post(commonSearchEvent);
        });

        rightBtn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchEntities.size() == 0) {
                    ToastUtils.show(EamActivity.this, "请选择设备!");
                    return;
                }
                CommonSearchEvent commonSearchEvent = new CommonSearchEvent();
                commonSearchEvent.mCommonSearchEntityList = searchEntities;
                commonSearchEvent.IS_MULTI = true;
                EventBus.getDefault().post(commonSearchEvent);
                back();
            }
        });

        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {
                final String blurMes = titleSearchView.editText().getText().toString().trim();
                if (queryParam.containsKey(Constant.BAPQuery.EAM_CODE)) {
                    queryParam.remove(Constant.BAPQuery.EAM_CODE);
                }
                if (queryParam.containsKey(Constant.BAPQuery.EAM_NAME)) {
                    queryParam.remove(Constant.BAPQuery.EAM_NAME);
                }
                if (!TextUtils.isEmpty(blurMes)) {
                    if (Util.isContainChinese(blurMes)) {
                        queryParam.put(Constant.BAPQuery.EAM_NAME, blurMes);
                    } else {
                        queryParam.put(Constant.BAPQuery.EAM_CODE, blurMes);
                    }
                }
                if (!TextUtils.isEmpty(areaName)) {
                    queryParam.put(Constant.BAPQuery.EAM_AREANAME, areaName);
                }
                if (isMainEam) {
                    queryParam.put(Constant.BAPQuery.IS_MAIN_EQUIP, "1");
                }
                presenterRouter.create(EamAPI.class).getEam(queryParam, pageIndex);
            }
        });
        KeyExpandHelper.doActionSearch(titleSearchView.editText(), true, () ->
                refreshListController.refreshBegin());

        RxTextView.textChanges(titleSearchView.editText())
                .skipInitialValue()
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(charSequence -> {
                    if (TextUtils.isEmpty(charSequence)) {
                        refreshListController.refreshBegin();
                    }
                });

        leftBtn.setOnClickListener(v -> back());

        pinyinSearchBar.setOnWordsChangeListener(word -> {
            final int pos = mBaseSearchAdapter.getPos(word);
            if (pos != -1) {
                recyclerView.scrollToPosition(pos);
            }
        });

        titleBar.setOnExpandListener(isExpand -> {
            if (isExpand) {
                titleSearchView.setInputTextColor(R.color.hintColor);
            } else {
                titleSearchView.setInputTextColor(R.color.black);
            }
        });
    }

    @Override
    public void getEamSuccess(CommonListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void getEamFailed(String errorMsg) {
        refreshListController.refreshComplete(null);
        ToastUtils.show(context, errorMsg);
    }

    /**
     * @param
     * @description NFC事件
     * @author zhangwenshuai1
     * @date 2018/6/28
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getNFC(NFCEvent nfcEvent) {
        LogUtil.d("NFC_TAG", nfcEvent.getNfc());
        Map<String, Object> nfcJson = Util.gsonToMaps(nfcEvent.getNfc());
        if (nfcJson.get("textRecord") == null) {
            ToastUtils.show(context, "标签内容空！");
            return;
        }
        eamCode = (String) nfcJson.get("textRecord");
        titleSearchView.setInput(eamCode);
        refreshListController.refreshBegin();
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
