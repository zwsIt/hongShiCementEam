package com.supcon.mes.module_contact.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.fragment.BasePresenterFragment;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonSearchStaff;
import com.supcon.mes.middleware.model.bean.ContactEntity;
import com.supcon.mes.middleware.model.bean.DepartmentInfo;
import com.supcon.mes.middleware.model.bean.DepartmentTreeViewEntity;
import com.supcon.mes.middleware.model.bean.ICustomTreeView;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.module_contact.IntentRouter;
import com.supcon.mes.module_contact.R;
import com.supcon.mes.module_contact.model.api.ContactDepartSelectAPI;
import com.supcon.mes.module_contact.model.contract.ContactDepartSelectContract;
import com.supcon.mes.module_contact.presenter.ContactDepartSelectPresenter;
import com.supcon.mes.module_contact.ui.adapter.ContactDepartTreeSelectAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * @Author xushiyun
 * @Create-time 7/26/19
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
@Presenter(ContactDepartSelectPresenter.class)
public class ContactDepartTreeSelectFragment extends BasePresenterFragment implements ContactDepartSelectContract.View {

    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    ImageButton rightBtn;
    @BindByTag("titleBarLayout")
    RelativeLayout titleBarLayout;
    @BindByTag("customSearchView")
    CustomSearchView customSearchView;

    @BindByTag("contentView")
    private RecyclerView contentView;

    private ContactDepartTreeSelectAdapter mContactDepartTreeSelectAdapter;
    private ICustomTreeView<DepartmentInfo> rootEntity;
    private boolean isSelect;
    private String searchTag;

    @Override
    protected void onInit() {
        super.onInit();
        isSelect = getActivity().getIntent().getBooleanExtra(Constant.IntentKey.IS_SELECT_STAFF, false);
        searchTag = getActivity().getIntent().getStringExtra(Constant.IntentKey.COMMON_SEARCH_TAG);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> getActivity().onBackPressed());

        mContactDepartTreeSelectAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                DepartmentTreeViewEntity item = (DepartmentTreeViewEntity) mContactDepartTreeSelectAdapter.getItem(position);
                if (isSelect) {
                    ContactEntity contactEntity = item.getCurrentEntity().userInfo;
                    CommonSearchEvent commonSearchEvent = new CommonSearchEvent();
                    CommonSearchStaff commonSearchStaff = new CommonSearchStaff();
                    commonSearchStaff.id = contactEntity.getSTAFFID();
                    commonSearchStaff.code = contactEntity.getCODE();
                    commonSearchStaff.name = contactEntity.getNAME();
                    commonSearchStaff.pinyin = contactEntity.getSearchPinyin();
                    commonSearchStaff.department = contactEntity.getDEPARTMENTNAME();
                    commonSearchStaff.mainPosition = contactEntity.getPOSITIONNAME();
                    commonSearchEvent.commonSearchEntity = commonSearchStaff;
                    commonSearchEvent.flag = searchTag;
                    EventBus.getDefault().post(commonSearchEvent);
                    EamApplication.getAppContext().getPenultimateActivity(getActivity()).finish();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constant.IntentKey.CONTACT_ENTITY, item.getCurrentEntity().userInfo);
                    IntentRouter.go(context, Constant.Router.CONTACT_VIEW, bundle);
                }
                getActivity().finish();
            }
        });

        RxTextView.textChanges(customSearchView.editText())
                .skipInitialValue()
                .debounce(500, TimeUnit.MILLISECONDS)
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

    @Override
    protected void initData() {
        super.initData();

    }

    @Override
    protected void initView() {
        super.initView();
        titleText.setText("组织架构");

        contentView = rootView.findViewById(com.supcon.mes.middleware.R.id.contentView);
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(10));
        mContactDepartTreeSelectAdapter = new ContactDepartTreeSelectAdapter(context);
        contentView.setAdapter(mContactDepartTreeSelectAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenterRouter.create(ContactDepartSelectAPI.class).getDepartmentInfoList("");
    }

    @Override
    protected int getLayoutID() {
        return R.layout.frag_contact_depart_tree_select;
    }

    @Override
    public void getDepartmentInfoListSuccess(DepartmentTreeViewEntity entity) {
        mContactDepartTreeSelectAdapter.setRootEntity(entity);
    }

    @Override
    public void getDepartmentInfoListFailed(String errorMsg) {

    }

}
