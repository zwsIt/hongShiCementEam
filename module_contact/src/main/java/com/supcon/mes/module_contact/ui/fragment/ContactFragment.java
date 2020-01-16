package com.supcon.mes.module_contact.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.ptr.PtrFrameLayout;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.ContactEntity;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.module_contact.IntentRouter;
import com.supcon.mes.module_contact.R;
import com.supcon.mes.module_contact.controller.ContactDataController;
import com.supcon.mes.module_contact.model.api.RecentContactAPI;
import com.supcon.mes.module_contact.model.contract.RecentContactContract;
import com.supcon.mes.module_contact.presenter.RecentContactPresenter;
import com.supcon.mes.module_contact.ui.adapter.ContactAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * Created by wangshizhan on 2019/12/3
 * Email:wangshizhan@supcom.com
 * 通讯录首页
 */

@Presenter(RecentContactPresenter.class)
@Controller(ContactDataController.class)
public class ContactFragment extends BaseRefreshRecyclerFragment<ContactEntity> implements RecentContactContract.View {

    @BindByTag("leftBtn")
    CustomImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    CustomImageButton rightBtn;

    @BindByTag("contentView")
    RecyclerView contentView;
    @BindByTag("searchView")
    CustomSearchView searchView;
    @BindByTag("departInfos")
    TextView departInfos;
    @BindByTag("refreshFrameLayout")
    PtrFrameLayout refreshFrameLayout;
    @BindByTag("sameDepart")
    TextView sameDepart;
    @BindByTag("position")
    TextView position;
    @BindByTag("allStaff")
    TextView allStaff;
    @BindByTag("ivSameDepart")
    ImageView ivSameDepart;
    @BindByTag("ivDepartInfos")
    ImageView ivDepartInfos;
    @BindByTag("ivPosition")
    ImageView ivPosition;
    @BindByTag("ivAllStaff")
    ImageView ivAllStaff;

    private ContactAdapter mContactAdapter;


    @Override
    protected IListAdapter createAdapter() {
        mContactAdapter = new ContactAdapter(context);
        return mContactAdapter;
    }


    @Override
    protected int getLayoutID() {
        return R.layout.frag_contact;
    }

    @Override
    protected void initView() {
        super.initView();
        titleText.setTextSize(18);

        titleText.setText("通讯录");
        leftBtn.setVisibility(View.GONE);
//        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setImageResource(R.drawable.sl_top_search);

        contentView.setLayoutManager(new LinearLayoutManager(context));
        final int spacingInPixels = 10;
        contentView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        refreshListController.setAutoPullDownRefresh(false);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, "无记录"));
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        RxView.clicks(rightBtn)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
//                        getActivity().getWindow().setWindowAnimations(R.style.settingAnimation);
                        IntentRouter.go(context, Constant.Router.SEARCH);
                    }
                });
        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {
                doFilter(pageIndex);
            }
        });
        ivDepartInfos.setOnClickListener(v -> IntentRouter.go(context, Constant.Router.CONTACT_DEPART_TREE_SELECT));
        departInfos.setOnClickListener(v -> IntentRouter.go(context, Constant.Router.CONTACT_DEPART_TREE_SELECT));
        ivSameDepart.setOnClickListener(v -> ContactFragment.this.goAllContact("同部门"));
        sameDepart.setOnClickListener(v -> ContactFragment.this.goAllContact("同部门"));
        ivAllStaff.setOnClickListener(v -> ContactFragment.this.goAllContact("所有人"));
        allStaff.setOnClickListener(v -> ContactFragment.this.goAllContact("所有人"));
        ivPosition.setOnClickListener(v -> IntentRouter.go(context, Constant.Router.CONTACT_POSITION_TREE_SELECT));
        position.setOnClickListener(v -> IntentRouter.go(context, Constant.Router.CONTACT_POSITION_TREE_SELECT));

        RxTextView.textChanges(searchView.editText())
                .skipInitialValue()
                .debounce(500, TimeUnit.MILLISECONDS)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        LogUtil.w("searchView:" + charSequence);

                        if (!TextUtils.isEmpty(charSequence)) {
                            Bundle bundle = new Bundle();
                            bundle.putString(Constant.IntentKey.SEARCH_CONTENT, charSequence.toString());
                            IntentRouter.go(context, Constant.Router.CONTACT_SEARCH_WITH_HEADER, bundle);
                        }
                    }
                });
    }


    private void goAllContact(String title) {
//        getActivity().getWindow().setWindowAnimations(R.style.fadeStyle);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.IntentKey.TITLE_CONTENT, title);
        IntentRouter.go(context, Constant.Router.CONTACT_SEARCH_WITH_HEADER, bundle);
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

    @Override
    public void onResume() {
        super.onResume();
        refreshListController.refreshBegin();
    }
}
