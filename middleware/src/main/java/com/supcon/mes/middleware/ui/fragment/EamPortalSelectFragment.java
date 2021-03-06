package com.supcon.mes.middleware.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.ptr.PtrFrameLayout;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.IntentRouter;
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.api.RecentEamSelectAPI;
import com.supcon.mes.middleware.model.bean.ContactEntity;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.contract.RecentEamSelectContract;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.presenter.RecentEamSelectPresenter;
import com.supcon.mes.middleware.ui.EamTreeSelectActivity;
import com.supcon.mes.middleware.ui.adapter.BaseSearchAdapter;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by zhangwenshuai on 2020/05/25
 * Email:zhangwenshuai1@supcom.com
 * 设备选择门户入口
 */
@Presenter(RecentEamSelectPresenter.class)
public class EamPortalSelectFragment extends BaseRefreshRecyclerFragment<ContactEntity> implements RecentEamSelectContract.View {

    @BindByTag("areaIv")
    ImageView areaIv;
    @BindByTag("areaTv")
    TextView areaTv;
    @BindByTag("eamTypeIv")
    ImageView eamTypeIv;
    @BindByTag("eamTypeTv")
    TextView eamTypeTv;
    @BindByTag("allEamIv")
    ImageView allEamIv;
    @BindByTag("allEamTv")
    TextView allEamTv;
    @BindByTag("contentView")
    RecyclerView contentView;

    private BaseSearchAdapter mBaseSearchAdapter;
    private boolean isSelect = false;
    private boolean isMulti = false;
    private String searchTag;

    @Override
    protected IListAdapter createAdapter() {
        mBaseSearchAdapter = new BaseSearchAdapter(context,isMulti);
        return mBaseSearchAdapter;
    }


    @Override
    protected int getLayoutID() {
        return R.layout.frag_eam_select;
    }

    @Override
    protected void onInit() {
        super.onInit();
        isSelect = getActivity().getIntent().getBooleanExtra(Constant.IntentKey.IS_SELECT, false);
        isMulti = getActivity().getIntent().getBooleanExtra(Constant.IntentKey.IS_MULTI, false);
        searchTag = getActivity().getIntent().getStringExtra(Constant.IntentKey.COMMON_SEARCH_TAG);

        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, "无记录"));
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setAutoPullDownRefresh(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshListController.refreshBegin();
    }

    @Override
    protected void initView() {
        super.initView();

        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(5,context)));
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        refreshListController.setOnRefreshPageListener(pageIndex -> doFilter(pageIndex));
        areaIv.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constant.IntentKey.IS_SELECT, isSelect);
            bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG, searchTag);
            IntentRouter.go(context, Constant.Router.EAM_AREA_TREE_SELECT, bundle);
        });
        areaTv.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constant.IntentKey.IS_SELECT, isSelect);
            bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG, searchTag);
            IntentRouter.go(context, Constant.Router.EAM_AREA_TREE_SELECT, bundle);
        });
        eamTypeIv.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constant.IntentKey.IS_SELECT, isSelect);
            bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG, searchTag);
            IntentRouter.go(context, Constant.Router.EAM_TYPE_TREE_SELECT, bundle);
        });
        eamTypeTv.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constant.IntentKey.IS_SELECT, isSelect);
            bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG, searchTag);
            IntentRouter.go(context, Constant.Router.EAM_TYPE_TREE_SELECT, bundle);
        });

        allEamTv.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constant.IntentKey.IS_MAIN_EAM, true);
            bundle.putBoolean(Constant.IntentKey.IS_MULTI, false);
            bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG, searchTag);
//            bundle.putBoolean(Constant.IntentKey.IS_SELECT,true);
            IntentRouter.go(context, Constant.Router.EAM, bundle);

        });
        allEamIv.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constant.IntentKey.IS_MAIN_EAM, true);
            bundle.putBoolean(Constant.IntentKey.IS_MULTI, false);
            bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG, searchTag);
//            bundle.putBoolean(Constant.IntentKey.IS_SELECT,true);
            IntentRouter.go(context, Constant.Router.EAM, bundle);
        });
        mBaseSearchAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                EamEntity eamEntity = (EamEntity) obj;
                if (isSelect) {
                    CommonSearchEvent commonSearchEvent = new CommonSearchEvent();
                    commonSearchEvent.commonSearchEntity = eamEntity;
                    commonSearchEvent.flag = searchTag;
                    EventBus.getDefault().post(commonSearchEvent);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putLong(Constant.IntentKey.SBDA_ONLINE_EAMID,eamEntity.id);
                    bundle.putString(Constant.IntentKey.SBDA_ONLINE_EAMCODE,eamEntity.code);
                    IntentRouter.go(context, Constant.Router.SBDA_ONLINE_VIEW, bundle);
                }
                getActivity().finish();
            }
        });
    }


    private void doFilter(int pageNo) {
        presenterRouter.create(RecentEamSelectAPI.class).getRecentEamList(pageNo, 50, "");
    }


    @Override
    public void getRecentEamListSuccess(List entity) {
        refreshListController.refreshComplete(entity);
    }

    @Override
    public void getRecentEamListFailed(String errorMsg) {
        ToastUtils.show(context, errorMsg);
        refreshListController.refreshComplete();
    }

//    public ContactAdapter getContactAdapter() {
//        return mContactAdapter;
//    }
}
