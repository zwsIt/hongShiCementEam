package com.supcon.mes.middleware.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.IntentRouter;
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.api.EamAreaTreeSelectAPI;
import com.supcon.mes.middleware.model.api.EamTypeTreeSelectAPI;
import com.supcon.mes.middleware.model.bean.EamAreaTreeViewEntity;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.bean.EamType;
import com.supcon.mes.middleware.model.bean.EamTypeTreeViewEntity;
import com.supcon.mes.middleware.model.bean.ICustomTreeView;
import com.supcon.mes.middleware.model.contract.EamAreaTreeSelectContract;
import com.supcon.mes.middleware.model.contract.EamTypeTreeSelectContract;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.presenter.EamAreaTreeSelectPresenter;
import com.supcon.mes.middleware.presenter.EamTypeTreeSelectPresenter;
import com.supcon.mes.middleware.ui.adapter.EamAreaTreeSelectAdapter;
import com.supcon.mes.middleware.ui.adapter.EamTypeTreeSelectAdapter;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zhangwenshuai on 2020/05/25
 * Email:zhangwenshuai1@supcom.com
 * 设备类型选择
 */
@Router(value = Constant.Router.EAM_TYPE_TREE_SELECT)
@Presenter(EamTypeTreeSelectPresenter.class)
public class EamTypeTreeSelectActivity extends BaseRefreshRecyclerActivity<ICustomTreeView<EamType>> implements EamTypeTreeSelectContract.View {
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    ImageButton rightBtn;
//    @BindByTag("customSearchView")
//    CustomSearchView customSearchView;

    @BindByTag("contentView")
    private RecyclerView contentView;

    private EamTypeTreeSelectAdapter mEamTypeTreeSelectAdapter;
    //    private ICustomTreeView<Area> rootEntity;
    private boolean isSelect;
    private String searchTag;

    @Override
    protected IListAdapter<ICustomTreeView<EamType>> createAdapter() {
        mEamTypeTreeSelectAdapter = new EamTypeTreeSelectAdapter(context);
        return mEamTypeTreeSelectAdapter;
    }
    @Override
    protected int getLayoutID() {
        return R.layout.frag_tree_select;
    }

    @Override
    protected void onInit() {
        super.onInit();
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(10));

        isSelect = getIntent().getBooleanExtra(Constant.IntentKey.IS_SELECT, false);
        searchTag = getIntent().getStringExtra(Constant.IntentKey.COMMON_SEARCH_TAG);
    }
    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this,R.color.themeColor);
        titleText.setText("设备类型架构");

//        contentView = rootView.findViewById(com.supcon.mes.middleware.R.id.contentView);
//        contentView.setLayoutManager(new LinearLayoutManager(context));
//        contentView.addItemDecoration(new SpaceItemDecoration(10));
//        contentView.setAdapter(mContactDepartTreeSelectAdapter);
    }
    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> onBackPressed());

        refreshListController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenterRouter.create(EamTypeTreeSelectAPI.class).getEamTypeList("");
            }
        });

        mEamTypeTreeSelectAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            EamTypeTreeViewEntity item = (EamTypeTreeViewEntity) mEamTypeTreeSelectAdapter.getItem(position);
            EamEntity eamEntity = item.getCurrentEntity().eamEntity;
            if (isSelect) {
                CommonSearchEvent commonSearchEvent = new CommonSearchEvent();
//                    CommonSearchStaff commonSearchStaff = new CommonSearchStaff();
//                    commonSearchStaff.id = eamEntity.getSTAFFID();
//                    commonSearchStaff.code = eamEntity.getCODE();
//                    commonSearchStaff.name = eamEntity.getNAME();
//                    commonSearchStaff.pinyin = eamEntity.getSearchPinyin();
//                    commonSearchStaff.department = eamEntity.getDEPARTMENTNAME();
//                    commonSearchStaff.mainPosition = eamEntity.getPOSITIONNAME();
                commonSearchEvent.commonSearchEntity = eamEntity;
                commonSearchEvent.flag = searchTag;
                EventBus.getDefault().post(commonSearchEvent);
                EamApplication.getAppContext().getPenultimateActivity(this).finish();
            } else {
                Bundle bundle = new Bundle();
//                    bundle.putSerializable(Constant.IntentKey.CONTACT_ENTITY, item.getCurrentEntity().eamEntity);
                bundle.putLong(Constant.IntentKey.SBDA_ONLINE_EAMID,eamEntity.id);
                bundle.putString(Constant.IntentKey.SBDA_ONLINE_EAMCODE,eamEntity.code);
                IntentRouter.go(context, Constant.Router.SBDA_ONLINE_VIEW, bundle);
            }
            finish();
        });

//        RxTextView.textChanges(customSearchView.editText())
//                .skipInitialValue()
//                .debounce(500, TimeUnit.MILLISECONDS)
//                .subscribe(new Consumer<CharSequence>() {
//                    @Override
//                    public void accept(CharSequence charSequence) throws Exception {
//                        LogUtil.w("searchView:" + charSequence);
//
//                        if (!TextUtils.isEmpty(charSequence)) {
//                            Bundle bundle = new Bundle();
//                            bundle.putString(Constant.IntentKey.SEARCH_CONTENT, charSequence.toString());
//                            IntentRouter.go(context, Constant.Router.CONTACT_SEARCH_WITH_HEADER, bundle);
//                        }
//                    }
//                });
    }

    @Override
    protected void initData() {
        super.initData();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void getEamTypeListSuccess(EamTypeTreeViewEntity entity) {
        mEamTypeTreeSelectAdapter.setRootEntity(entity);
        refreshListController.refreshComplete();
    }

    @Override
    public void getEamTypeListFailed(String errorMsg) {
        refreshListController.refreshComplete();
    }
}
