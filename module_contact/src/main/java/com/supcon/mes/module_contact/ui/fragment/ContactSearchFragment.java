package com.supcon.mes.module_contact.ui.fragment;

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
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.ContactEntity;
import com.supcon.mes.module_contact.R;
import com.supcon.mes.module_contact.controller.ContactDataController;
import com.supcon.mes.module_contact.model.api.ContactAPI;
import com.supcon.mes.module_contact.model.contract.ContactContract;
import com.supcon.mes.module_contact.presenter.ContactPresenter;
import com.supcon.mes.module_contact.ui.adapter.ContactAdapter;

import java.util.List;

/**
 * Created by wangshizhan on 2019/12/6
 * Email:wangshizhan@supcom.com
 */
@Presenter(ContactPresenter.class)
@Controller(ContactDataController.class)
public class ContactSearchFragment extends BaseRefreshRecyclerFragment<ContactEntity> implements ContactContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;

    private ContactAdapter mContactAdapter;
    private String mSearchContent;

    private boolean isSelect = false;
    private boolean isMulti = false;
    private String title;

    @Override
    protected int getLayoutID() {
        return R.layout.frag_contact_search;
    }

    @Override
    protected IListAdapter createAdapter() {
        boolean isSelect = getActivity().getIntent().getBooleanExtra(Constant.IntentKey.IS_SELECT_STAFF, false);
        boolean isMulti = getActivity().getIntent().getBooleanExtra(Constant.IntentKey.IS_MULTI, false);
        String searchTag = getActivity().getIntent().getStringExtra(Constant.IntentKey.COMMON_SEARCH_TAG);
        mContactAdapter = new ContactAdapter(context, isSelect, isMulti, "", searchTag);
        return mContactAdapter;
    }

    @Override
    protected void onInit() {
        super.onInit();
        isSelect = getActivity().getIntent().getBooleanExtra(Constant.IntentKey.IS_SELECT_STAFF, false);
        isMulti = getActivity().getIntent().getBooleanExtra(Constant.IntentKey.IS_MULTI, false);

        mSearchContent = getActivity().getIntent().getStringExtra(Constant.IntentKey.SEARCH_CONTENT);
        if (TextUtils.isEmpty(title)) {
            title = getActivity().getIntent().getStringExtra(Constant.IntentKey.TITLE_CONTENT);
        }

        refreshListController.setAutoPullDownRefresh(false);
        refreshListController.setPullDownRefreshEnabled(false);
        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {
                presenterRouter.create(ContactAPI.class).getContactList(pageIndex, 20, mSearchContent,
                        (!TextUtils.isEmpty(title) && (title.equals("同部门") || title.equals("我的下属"))) ? EamApplication.getAccountInfo().departmentName : "");
            }
        });
    }

    @Override
    protected void initView() {
        super.initView();
        contentView.setLayoutManager(new LinearLayoutManager(context));
        final int spacingInPixels = 10;
        contentView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
    }

    @Override
    protected void initListener() {
        super.initListener();
        mContactAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
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
        if (getController(ContactDataController.class).isInitFinish() && !TextUtils.isEmpty(mSearchContent)) {
            refreshListController.refreshBegin();
        }
    }

    @Override
    public void getContactListSuccess(List entity) {
        refreshListController.refreshComplete(entity);
    }

    @Override
    public void getContactListFailed(String errorMsg) {
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

    public ContactAdapter getContactAdapter() {
        return mContactAdapter;
    }
}
