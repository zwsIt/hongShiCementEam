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
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.ContactEntity;
import com.supcon.mes.middleware.ui.view.PinyinSearchBar;
import com.supcon.mes.module_contact.R;
import com.supcon.mes.module_contact.controller.ContactDataController;
import com.supcon.mes.module_contact.model.api.ContactAPI;
import com.supcon.mes.module_contact.model.api.PinyinSearchBarAPI;
import com.supcon.mes.module_contact.model.contract.ContactContract;
import com.supcon.mes.module_contact.model.contract.PinyinSearchBarContract;
import com.supcon.mes.module_contact.presenter.ContactPresenter;
import com.supcon.mes.module_contact.presenter.PinyinSearchBarPresenter;
import com.supcon.mes.module_contact.ui.adapter.ContactAdapter;

import java.util.List;

/**
 * Created by wangshizhan on 2019/12/6
 * Email:wangshizhan@supcom.com
 */
@Controller(ContactDataController.class)
@Presenter({ContactPresenter.class, PinyinSearchBarPresenter.class})
public class ContactCommonFragment extends BaseRefreshRecyclerFragment<ContactEntity> implements ContactContract.View, PinyinSearchBarContract.View {


    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("pinyinSearchBar")
    PinyinSearchBar pinyinSearchBar;

    private ContactAdapter mContactAdapter;
    private String title;
    private String mSearch;

    private boolean isSelect = false;
    private boolean isMulti = false;

    @Override
    protected int getLayoutID() {
        return R.layout.frag_contact_common;
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

        if (TextUtils.isEmpty(title)) {
            title = getActivity().getIntent().getStringExtra(Constant.IntentKey.TITLE_CONTENT);
        }
        if (title == null) {
            title = "";
        }

        refreshListController.setAutoPullDownRefresh(false);
        refreshListController.setPullDownRefreshEnabled(false);
        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {
                presenterRouter.create(ContactAPI.class).getContactList(pageIndex, 20, getSearchType(),
                        (title.equals("同部门") || title.equals("我的下属")) ? EamApplication.getAccountInfo().departmentName : "");
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

        pinyinSearchBar.setOnWordsChangeListener(new PinyinSearchBar.OnWordsChangeListener() {
            @Override
            public void wordsChange(String words) {
                LogUtil.d("" + words);
                ToastUtils.show(context, "" + words);
                if (mContactAdapter != null && mContactAdapter.getList() != null) {
                    presenterRouter.create(PinyinSearchBarAPI.class).findContactPosition(mContactAdapter.getList(), words);
                }
            }
        });
    }


    @Override
    protected void initData() {
        super.initData();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (getController(ContactDataController.class).isInitFinish()) {
            refreshListController.refreshBegin();
        }
    }

    private String getSearchType() {
        if ("所有人".equals(title)) {
            mSearch = "";
        }
        return mSearch;
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

    public ContactAdapter getContactAdapter() {
        return mContactAdapter;
    }

    @Override
    public void findContactPositionSuccess(ContactEntity entity) {
        contentView.smoothScrollToPosition(mContactAdapter.getList().indexOf(entity));
    }

    @Override
    public void findContactPositionFailed(String errorMsg) {

    }
}
