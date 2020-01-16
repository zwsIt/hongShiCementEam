package com.supcon.mes.module_contact.ui.fragment;

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
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.ContactEntity;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.module_contact.IntentRouter;
import com.supcon.mes.module_contact.R;
import com.supcon.mes.module_contact.model.api.RecentContactAPI;
import com.supcon.mes.module_contact.model.contract.RecentContactContract;
import com.supcon.mes.module_contact.presenter.RecentContactPresenter;
import com.supcon.mes.module_contact.ui.ContactSelectActivity;
import com.supcon.mes.module_contact.ui.adapter.ContactAdapter;

import java.util.List;

/**
 * Created by wangshizhan on 2019/12/3
 * Email:wangshizhan@supcom.com
 * 通讯录首页
 */

@Presenter(RecentContactPresenter.class)
public class ContactSelectFragment extends BaseRefreshRecyclerFragment<ContactEntity> implements RecentContactContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;
    @BindByTag("searchView")
    CustomSearchView searchView;
    @BindByTag("departInfos")
    TextView departInfos;
    @BindByTag("sameDepart")
    TextView sameDepart;
    @BindByTag("allStaff")
    TextView allStaff;
    @BindByTag("position")
    TextView position;
    @BindByTag("ivSameDepart")
    ImageView ivSameDepart;
    @BindByTag("ivDepartInfos")
    ImageView ivDepartInfos;
    @BindByTag("ivAllStaff")
    ImageView ivAllStaff;
    @BindByTag("ivPosition")
    ImageView ivPosition;

    private ContactAdapter mContactAdapter;
    private boolean isSelect = false;
    private boolean isMulti = false;
    private String searchTag;

    @Override
    protected IListAdapter createAdapter() {
        String selectedStaff = getActivity().getIntent().getStringExtra(Constant.IntentKey.IS_MULTI);
        mContactAdapter = new ContactAdapter(context, isSelect, isMulti, "", searchTag);
        return mContactAdapter;
    }


    @Override
    protected int getLayoutID() {
        return R.layout.frag_contact_select;
    }

    @Override
    protected void onInit() {
        isSelect = getActivity().getIntent().getBooleanExtra(Constant.IntentKey.IS_SELECT_STAFF, false);
        isMulti = getActivity().getIntent().getBooleanExtra(Constant.IntentKey.IS_MULTI, false);
        searchTag = getActivity().getIntent().getStringExtra(Constant.IntentKey.COMMON_SEARCH_TAG);
        super.onInit();

        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, "无记录"));
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setOnRefreshPageListener(pageIndex -> doFilter(pageIndex));
        refreshListController.setAutoPullDownRefresh(false);
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
        final int spacingInPixels = 10;
        contentView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        departInfos.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constant.IntentKey.IS_SELECT_STAFF, isSelect);
            bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG, searchTag);
            IntentRouter.go(context, Constant.Router.CONTACT_DEPART_TREE_SELECT, bundle);
        });
        ivDepartInfos.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constant.IntentKey.IS_SELECT_STAFF, isSelect);
            bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG, searchTag);
            IntentRouter.go(context, Constant.Router.CONTACT_DEPART_TREE_SELECT, bundle);
        });
        ivPosition.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constant.IntentKey.IS_SELECT_STAFF, isSelect);
            bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG, searchTag);
            IntentRouter.go(context, Constant.Router.CONTACT_POSITION_TREE_SELECT, bundle);
        });
        position.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constant.IntentKey.IS_SELECT_STAFF, isSelect);
            bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG, searchTag);
            IntentRouter.go(context, Constant.Router.CONTACT_POSITION_TREE_SELECT, bundle);
        });

        ivSameDepart.setOnClickListener(v -> {
            if (isSelect) {
                ((ContactSelectActivity) getActivity()).showFragment(1, "同部门");
            }
        });
        sameDepart.setOnClickListener(v -> {
            if (isSelect) {
                ((ContactSelectActivity) getActivity()).showFragment(1, "同部门");
            }

        });
        ivAllStaff.setOnClickListener(v -> {
            if (isSelect) {
                ((ContactSelectActivity) getActivity()).showFragment(1, "所有人");
            }
        });
        allStaff.setOnClickListener(v -> {
            if (isSelect) {
                ((ContactSelectActivity) getActivity()).showFragment(1, "所有人");
            }
        });
        mContactAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                if (isSelect && !isMulti)
                    getActivity().finish();
            }
        });
    }


    private void doFilter(int pageNo) {
        presenterRouter.create(RecentContactAPI.class).getRecentContactList(pageNo, 50, "");
    }


    @Override
    public void getRecentContactListSuccess(List entity) {

        List<ContactEntity> contactEntities = entity;
        refreshListController.refreshComplete(contactEntities);
    }

    @Override
    public void getRecentContactListFailed(String errorMsg) {
        ToastUtils.show(context, errorMsg);
        refreshListController.refreshComplete(null);
    }

    public ContactAdapter getContactAdapter() {
        return mContactAdapter;
    }
}
