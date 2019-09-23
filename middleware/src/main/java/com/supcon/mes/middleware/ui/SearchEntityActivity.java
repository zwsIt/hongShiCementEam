package com.supcon.mes.middleware.ui;

import android.annotation.SuppressLint;
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
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.CustomSwipeLayout;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.api.BaseSearchAPI;
import com.supcon.mes.middleware.model.bean.BaseSearchListEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchEntity;
import com.supcon.mes.middleware.model.contract.BaseSearchContract;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.presenter.BaseSearchPresenter;
import com.supcon.mes.middleware.ui.adapter.BaseSearchAdapter;
import com.supcon.mes.middleware.ui.view.PinyinSearchBar;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Xushiyun
 * @date 2018/5/22
 * Email:ciruy.victory@gmail.com
 * Todo:To design a common search component to show you with items that you could choose from
 */
@Router(Constant.Router.COMMON_SEARCH)
@Presenter(value = {BaseSearchPresenter.class})
public class SearchEntityActivity extends BaseRefreshRecyclerActivity<CommonSearchEntity> implements BaseSearchContract.View {
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
    
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    
    @BindByTag("rightBtn_1")
    TextView rightBtn_1;
    
    @BindByTag("titleText")
    TextView titleText;
    
    /**
     * 侧栏首字母导航控件
     */
    @BindByTag("pinyinSearchBar")
    PinyinSearchBar pinyinSearchBar;
    
    Map<String, String> queryParam = new HashMap<>();
    
    private BaseSearchAdapter mBaseSearchAdapter;
    
    /**
     * 系统编码模式是否启动
     */
    private boolean isSystemCode;
    /**
     * 区域模式是否启动
     */
    private boolean isArea;
    /**
     * 人员模式是否启动
     */
    private boolean isStaff;
    /**
     * 系统编码模式下的指定系统编码组
     */
    private String entityCode;
    
    /**
     * 多选模式，现在还在开发中
     * Todo:暂时不支持多选模式
     */
    private boolean isMulti;
    /**
     * 搜索模式
     */
    private String questCode;
    /**
     * 请求来自哪个控件，一般用于储存组件的tag用于区分组件
     */
    private String searchCode;
    private boolean isEam;
    /**
     * 设备权限
     */
    private String permission;
    
    private String defaultSearchValue;
    
    private List<CommonSearchEntity> selectedSearchIds = new ArrayList<>();
    
    @Override
    protected IListAdapter<CommonSearchEntity> createAdapter() {
        mBaseSearchAdapter = new BaseSearchAdapter(this, isMulti);
        return mBaseSearchAdapter;
    }
    
    @Override
    protected void onInit() {
        super.onInit();
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, "没有信息哦~"));
        
        //获取搜索模式，如果用户通过COMMON_SEARCH_MODE传入了非空的搜索模式时，则通过传入的搜索模式来设置搜索方式
        //如果用户并没有传入COMMON_SEARCH_MODE时，则检测用户是否使用了IS_AREA类型的方式指定搜索模式
        //如果也没有，那就设置默认搜索模式为人员搜索
        /*
      搜索模式，参数的具体值在Constant.CommonSearchMode中选择
     */
        String searchMode = getIntent().getStringExtra(Constant.IntentKey.COMMON_SAERCH_MODE);
        defaultSearchValue = getIntent().getStringExtra(Constant.IntentKey.COMMON_SEARCH_DEFAULT_SEARCH_VALUE);
        //如果searchMode不为空，则获取三个参数，便于后面判断
        if (TextUtils.isEmpty(searchMode)) {
            isSystemCode = getIntent().getBooleanExtra(Constant.IntentKey.IS_ENTITY_CODE, false);
            isArea = getIntent().getBooleanExtra(Constant.IntentKey.IS_AREA, false);
            isStaff = getIntent().getBooleanExtra(Constant.IntentKey.IS_STAFF, false);
            isEam = getIntent().getBooleanExtra(Constant.IntentKey.IS_EAM, false);
        } else {
            switch (searchMode) {
                //区域搜索模式
                case Constant.CommonSearchMode.AREA:
                    isArea = true;
                    break;
                //系统编码模式
                case Constant.CommonSearchMode.SYSTEM_CODE:
                    isSystemCode = true;
                    break;
                //人员搜索模式，这里依然采用默认设置人员搜索模式，但是为了统一性，所以也为人员也设置了模式参数
                case Constant.CommonSearchMode.STAFF:
                    isStaff = true;
                    break;
                case Constant.CommonSearchMode.EAM:
                    isEam = true;
                    break;
                default:
            }
        }
        
        isMulti = getIntent().getBooleanExtra(Constant.IntentKey.IS_MULTI, false);
        mBaseSearchAdapter.setMulti(isMulti);
        if (isSystemCode || isEam) {
            entityCode = getIntent().getStringExtra(Constant.IntentKey.ENTITY_CODE);
        }
        //设置优先级，如果用户比较闲，同时传入了多个模式参数，优先级为SystemCode>Area>Staff，如果用户没有传入参数，则默认使用用户
        questCode =
                isSystemCode ? Constant.IntentKey.IS_ENTITY_CODE :
                        isArea ? Constant.IntentKey.IS_AREA :
                                isEam ? Constant.IntentKey.IS_EAM :
                                        isStaff ? Constant.IntentKey.IS_STAFF : null;
        searchCode = getIntent().getStringExtra(Constant.IntentKey.COMMON_SEARCH_TAG);
        
        permission = getIntent().getStringExtra(Constant.IntentKey.MODULE);
    }
    
    @Override
    protected void initData() {
        super.initData();
        if (!TextUtils.isEmpty(defaultSearchValue)) {
            titleSearchView.setInput(defaultSearchValue);
        }
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
        if (isStaff) {
            titleSearchView.setHint("请输入姓名（拼音）");
        } else {
            titleSearchView.setHint("请输入搜索信息");
        }
        
        titleBar.setTitleText(isArea ? "区域搜索" : isEam ? "设备搜索" : "人员搜索");
        titleBar.disableRightBtn();
        
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(1, context)));
        recyclerView.addOnItemTouchListener(new CustomSwipeLayout.OnSwipeItemTouchListener(this));
        recyclerView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        recyclerView.setScrollBarSize(2);
        rightBtn_1.setVisibility(isMulti ? View.VISIBLE : View.GONE);
    }
    
    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        
        mBaseSearchAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            //如果当前模式为多选模式,则不响应单选模式触发返回事件
            final CommonSearchEntity commonSearchEntity = (CommonSearchEntity) obj;
            if(isMulti) {
                if(!selectedSearchIds.contains(commonSearchEntity)) {
                    selectedSearchIds.add(commonSearchEntity);
                }else {
                    selectedSearchIds.remove(commonSearchEntity);
                }
                return;
            }
            final CommonSearchEvent commonSearchEvent = new CommonSearchEvent();
            commonSearchEvent.flag = searchCode;
            commonSearchEvent.commonSearchEntity = commonSearchEntity;
            
            EventBus.getDefault().post(commonSearchEvent);
            SearchEntityActivity.this.finish();
        });
        
        refreshListController.setOnRefreshPageListener(pageIndex -> {
            final String blurMes = titleSearchView.editText().getText().toString().trim();
            if (TextUtils.isEmpty(permission)) {
                presenterRouter.create(BaseSearchAPI.class).baseSearch(pageIndex, entityCode, blurMes, questCode);
            } else {
                presenterRouter.create(BaseSearchAPI.class).baseEamSearch(pageIndex, entityCode, blurMes, permission);
            }
        });
        RxTextView.textChanges(titleSearchView.editText())
                .skipInitialValue()
                .debounce(1, TimeUnit.SECONDS)
                .subscribe(charSequence -> {
                    mBaseSearchAdapter.clear();
                    refreshListController.refreshBegin();
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
        
        titleText.setOnClickListener(null);
        
        rightBtn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchEntityListEvent searchEntityListEvent = new SearchEntityListEvent();
                searchEntityListEvent.mCommonSearchEntities = selectedSearchIds;
                searchEntityListEvent.flag = searchCode;
                EventBus.getDefault().post(searchEntityListEvent);
                back();
            }
        });
    }
    
    public class SearchEntityListEvent{
        public String flag;
        public List<CommonSearchEntity> mCommonSearchEntities;
    }
    
    @Override
    public void baseSearchSuccess(BaseSearchListEntity entity) {
        refreshListController.refreshComplete(entity.commonSearchEntities);
    }
    
    @Override
    public void baseSearchFailed(String errorMsg) {
        refreshListController.refreshComplete(null);
        ToastUtils.show(context, errorMsg);
    }
    
    @Override
    public void baseEamSearchSuccess(BaseSearchListEntity entity) {
        refreshListController.refreshComplete(entity.commonSearchEntities);
    }
    
    @Override
    public void baseEamSearchFailed(String errorMsg) {
        refreshListController.refreshComplete(null);
        ToastUtils.show(context, errorMsg);
    }
    
}