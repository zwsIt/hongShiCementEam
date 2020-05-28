package com.supcon.mes.middleware.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.api.EamQueryLocalAPI;
import com.supcon.mes.middleware.model.bean.ContactEntity;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.contract.EamQueryLocalContract;
import com.supcon.mes.middleware.presenter.EamQueryLocalPresenter;
import com.supcon.mes.middleware.ui.adapter.BaseSearchAdapter;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;

import java.util.List;

/**
 * Created by zws on 2020/5/28
 * Email:zhangwenshuai1@supcom.com
 */
@Presenter(EamQueryLocalPresenter.class)
//@Controller(ContactDataController.class)
public class EamSearchSelectFragment extends BaseRefreshRecyclerFragment<EamEntity> implements EamQueryLocalContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;

    private BaseSearchAdapter mBaseSearchAdapter;
    private String mSearchContent;

    private boolean isSelect = false;
    private boolean isMulti = false;
    private String title;

    @Override
    protected int getLayoutID() {
        return R.layout.frag_list;
    }

    @Override
    protected IListAdapter createAdapter() {
//        boolean isSelect = getActivity().getIntent().getBooleanExtra(Constant.IntentKey.IS_SELECT_STAFF, false);
//        boolean isMulti = getActivity().getIntent().getBooleanExtra(Constant.IntentKey.IS_MULTI, false);
//        String searchTag = getActivity().getIntent().getStringExtra(Constant.IntentKey.COMMON_SEARCH_TAG);
        mBaseSearchAdapter = new BaseSearchAdapter(context, isMulti);
        return mBaseSearchAdapter;
    }

    @Override
    protected void onInit() {
        super.onInit();

        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(5,context)));

        isSelect = getActivity().getIntent().getBooleanExtra(Constant.IntentKey.IS_SELECT, false);
        isMulti = getActivity().getIntent().getBooleanExtra(Constant.IntentKey.IS_MULTI, false);
        mBaseSearchAdapter.setMulti(isMulti);

        mSearchContent = getActivity().getIntent().getStringExtra(Constant.IntentKey.SEARCH_CONTENT);
        if (TextUtils.isEmpty(title)) {
            title = getActivity().getIntent().getStringExtra(Constant.IntentKey.TITLE_CONTENT);
        }

        refreshListController.setAutoPullDownRefresh(false);
        refreshListController.setPullDownRefreshEnabled(false);
    }

    @Override
    protected void initView() {
        super.initView();
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context,context.getResources().getString(R.string.no_data)));
    }

    @Override
    protected void initListener() {
        super.initListener();
        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {
                presenterRouter.create(EamQueryLocalAPI.class).listEamLocal(pageIndex, 20, mSearchContent,"");
            }
        });
        mBaseSearchAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                if (isSelect && !isMulti)
                    getActivity().finish();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
//        if (getController(ContactDataController.class).isInitFinish() && !TextUtils.isEmpty(mSearchContent)) {
//            refreshListController.refreshBegin();
//        }
    }

    @Override
    public void listEamLocalSuccess(List entity) {
        refreshListController.refreshComplete(entity);
    }

    @Override
    public void listEamLocalFailed(String errorMsg) {
        refreshListController.refreshComplete(null);
        LogUtil.e("CommonContact error:" + errorMsg);
    }

    public void setTitle(String title) {
        this.title = title;

    }
    public void doSearch(String searchContent) {
        this.mSearchContent = searchContent;
        refreshListController.refreshBegin();
    }

//    public ContactAdapter getContactAdapter() {
//        return mContactAdapter;
//    }
}
