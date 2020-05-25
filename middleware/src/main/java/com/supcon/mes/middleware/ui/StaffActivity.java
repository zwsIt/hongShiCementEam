package com.supcon.mes.middleware.ui;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.CustomSwipeLayout;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.api.StaffAPI;
import com.supcon.mes.middleware.model.bean.CommonSearchEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchStaff;
import com.supcon.mes.middleware.model.bean.UserInfoListEntity;
import com.supcon.mes.middleware.model.contract.StaffContract;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.presenter.StaffPresenter;
import com.supcon.mes.middleware.ui.adapter.BaseSearchAdapter;
import com.supcon.mes.middleware.ui.view.PinyinSearchBar;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

@Router(value = Constant.Router.STAFF)
@Presenter(value = StaffPresenter.class)
public class StaffActivity extends BaseRefreshRecyclerActivity<CommonSearchEntity> implements StaffContract.View {
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

    /**
     * 侧栏首字母导航控件
     */
    @BindByTag("pinyinSearchBar")
    PinyinSearchBar pinyinSearchBar;

    @BindByTag("rightBtn_1")
    TextView rightBtn_1; // 确定

    private BaseSearchAdapter mBaseSearchAdapter;
    private String searchTag;
    private ArrayList<String> addedRSList; // 已添加数据
    private boolean isMulti;
    private List<CommonSearchEntity> selectStaffList = new ArrayList<>();

    @Override
    protected IListAdapter<CommonSearchEntity> createAdapter() {
        mBaseSearchAdapter = new BaseSearchAdapter(this);
        return mBaseSearchAdapter;
    }

    @Override
    protected void onInit() {
        super.onInit();
        searchTag = getIntent().getStringExtra(Constant.IntentKey.COMMON_SEARCH_TAG);
        addedRSList = getIntent().getStringArrayListExtra(Constant.IntentKey.ADD_DATA_LIST);
        isMulti = getIntent().getBooleanExtra(Constant.IntentKey.IS_MULTI,false);
        mBaseSearchAdapter.setMulti(isMulti);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context,context.getResources().getString(R.string.no_data)));
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
        titleSearchView.setHint("请输入人员姓名搜索");

        titleBar.setTitleText("人员搜索");
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
            CommonSearchEntity commonSearchEntity = (CommonSearchEntity) obj;
            if (addedRSList != null) {
                if (commonSearchEntity.getSearchId() != null && addedRSList.contains(commonSearchEntity.getSearchId())) {
                    ToastUtils.show(context, "请勿重复添加人员!");
                    return;
                }
            }
            if (isMulti){
                if (selectStaffList.contains(commonSearchEntity)) {
                    selectStaffList.remove(commonSearchEntity);
                } else {
                    selectStaffList.add(commonSearchEntity);
                }
            }else {
                CommonSearchEvent commonSearchEvent = new CommonSearchEvent();
                commonSearchEvent.commonSearchEntity = commonSearchEntity;
                commonSearchEvent.flag = searchTag;
                finish();
                EventBus.getDefault().post(commonSearchEvent);
            }
        });

        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {
                final String blurMes = titleSearchView.editText().getText().toString().trim();
                presenterRouter.create(StaffAPI.class).listCommonContractStaff(blurMes, pageIndex);
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
        RxView.clicks(rightBtn_1)
                .throttleFirst(300,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (selectStaffList.size() == 0) {
                            ToastUtils.show(context, getResources().getString(R.string.middleware_select_staff));
                            return;
                        }
                        CommonSearchEvent commonSearchEvent = new CommonSearchEvent();
                        commonSearchEvent.mCommonSearchEntityList = selectStaffList;
                        commonSearchEvent.flag = searchTag;
                        EventBus.getDefault().post(commonSearchEvent);
                        finish();
                    }
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void listCommonContractStaffSuccess(UserInfoListEntity entity) {
        if (entity.result != null) {
            List<CommonSearchEntity> commonSearchEntities = new ArrayList<>();
            Flowable.fromIterable(entity.result)
                    .subscribe(userInfo -> {
                        CommonSearchStaff commonSearchStaff = new CommonSearchStaff();
                        commonSearchStaff.id = userInfo.staff.id;
                        commonSearchStaff.code = userInfo.staff.code;
                        commonSearchStaff.name = userInfo.staff.name;
                        commonSearchStaff.userId = userInfo.id;
                        commonSearchStaff.mainPosition = userInfo.staff.getMainPosition().name;
                        commonSearchStaff.department = userInfo.staff.getMainPosition().getDepartment().name;
                        commonSearchEntities.add(commonSearchStaff);
                    }, throwable -> {
                    }, () -> refreshListController.refreshComplete(commonSearchEntities));


        } else {
            refreshListController.refreshComplete(null);
        }
    }

    @Override
    public void listCommonContractStaffFailed(String errorMsg) {
        refreshListController.refreshComplete(null);
        ToastUtils.show(context, errorMsg);
    }
}
