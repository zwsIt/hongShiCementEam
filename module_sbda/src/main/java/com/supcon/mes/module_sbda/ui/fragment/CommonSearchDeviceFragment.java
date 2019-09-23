package com.supcon.mes.module_sbda.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.middleware.constant.Module;
import com.supcon.mes.middleware.model.bean.CommonDeviceEntity;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_sbda.R;
import com.supcon.mes.module_sbda.model.api.CommonSearchDeviceAPI;
import com.supcon.mes.middleware.model.bean.CommonSearchDeviceListEntity;
import com.supcon.mes.module_sbda.model.bean.SearchDeviceEntity;
import com.supcon.mes.module_sbda.model.contract.CommonSearchDeviceContract;
import com.supcon.mes.module_sbda.presenter.CommonSearchDevicePresenter;
import com.supcon.mes.module_sbda.ui.AddDeviceActivity;
import com.supcon.mes.module_sbda.ui.adapter.CommonSearchDeviceAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Environment: hongruijun
 * Created by Xushiyun on 2018/5/17.
 */
@Presenter(value = {CommonSearchDevicePresenter.class})
public class CommonSearchDeviceFragment extends BaseRefreshRecyclerFragment implements CommonSearchDeviceContract.View {
    @BindByTag("contentView")
    RecyclerView searchRecyclerView;

    private CommonSearchDeviceAdapter mAdapter;
    private String blurSearch = null;

    private String module;

    private boolean isSingle;
    @Override
    protected int getLayoutID() {
        return R.layout.fragment_common_search_device;
    }

    @Override
    protected void onInit() {
        super.onInit();
//        EventBus.getDefault().register(this);
        module = ((AddDeviceActivity)getActivity()).getModule();
        isSingle = Module.Fault.name().equals(module);

        refreshListController.setAutoPullDownRefresh(false);
        refreshListController.setLoadMoreEnable(true);
        refreshListController.setPullDownRefreshEnabled(true);
    }

    @Override
    protected void initView() {
        super.initView();

        searchRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_gap);
        searchRecyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
    }

    public void doSearch(String blurSear) {
//        presenterRouter.create(CommonSearchDeviceAPI.class).getSearchDevice(blurSear, null);
        blurSearch = blurSear;
        refreshListController.refreshBegin();
    }

    @Override
    protected void initData() {
        super.initData();
//        presenterRouter.create(CommonSearchDeviceAPI.class).getSearchDevice("", null);
    }

    @Override
    protected void initListener() {

//        refreshListController.setOnRefreshListener(() -> presenterRouter.create(CommonSearchDeviceAPI.class).getSearchDevice("", null));
        refreshListController.setOnRefreshPageListener(pageIndex -> presenterRouter.create(CommonSearchDeviceAPI.class).getSearchDevice(module, blurSearch, null, pageIndex));
        super.initListener();
        mAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            if (isSingle){
                ((AddDeviceActivity) getActivity()).sendDevice((CommonDeviceEntity) obj);
                getActivity().finish();
                return;
            }

            final SearchDeviceEntity searchDeviceEntity = new SearchDeviceEntity();
            searchDeviceEntity.result = new ArrayList<>();
            searchDeviceEntity.result.add((CommonDeviceEntity) obj);
            ((AddDeviceActivity)getActivity()).displaySearchFragment(false);
            EventBus.getDefault().post(searchDeviceEntity);
        });

        searchRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                ((AddDeviceActivity)getActivity()).hideSearchKeyboard();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                ((AddDeviceActivity)getActivity()).hideSearchKeyboard();
            }
        });
    }

    @Override
    public void getSearchDeviceSuccess(CommonSearchDeviceListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void getSearchDeviceFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
        refreshListController.refreshComplete(null);
    }

    @Override
    protected IListAdapter createAdapter() {
        mAdapter = new CommonSearchDeviceAdapter(context);
        return mAdapter;
    }
}
