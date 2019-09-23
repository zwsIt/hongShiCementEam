package com.supcon.mes.module_sbda.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.middleware.constant.Module;
import com.supcon.mes.middleware.model.bean.CommonDeviceEntity;
import com.supcon.mes.middleware.model.event.SynDeviceEevent;
import com.supcon.mes.module_sbda.R;
import com.supcon.mes.module_sbda.model.api.RecentDeviceAPI;
import com.supcon.mes.module_sbda.model.bean.RecentDeviceListEntity;
import com.supcon.mes.module_sbda.model.contract.RecentDeviceContract;
import com.supcon.mes.module_sbda.presenter.RecentDevicePresenter;
import com.supcon.mes.module_sbda.ui.AddDeviceActivity;
import com.supcon.mes.module_sbda.ui.adapter.AddDeviceAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Flowable;

/**
 * Created by wangshizhan on 2017/11/23.
 * Email:wangshizhan@supcon.com
 */
@Presenter(value = {RecentDevicePresenter.class})
public class RecentDeviceFragment extends BaseRefreshRecyclerFragment<CommonDeviceEntity> implements
        RecentDeviceContract.View {


    @BindByTag("contentView")
    RecyclerView recyclerView;

    AddDeviceAdapter mAdapter, mSearchAdapter;

    List<CommonDeviceEntity> mDeviceEntities = new ArrayList<>();
    String module;
    boolean isSingle;

    public static final int DISPLAY_NUM = 10;

    public RecentDeviceFragment() {

    }

    @Override
    protected int getLayoutID() {
        return R.layout.frag_device_list;
    }

    @Override
    protected void onInit() {
        module = ((AddDeviceActivity) getActivity()).getModule();
        isSingle = Module.Fault.name().equals(module);
        super.onInit();
        EventBus.getDefault().register(this);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setLoadMoreEnable(true);
    }

    @Subscribe
    public void onUpdateItemStatus(SynDeviceEevent synDeviceEevent) {

        Flowable.fromIterable(mDeviceEntities)
                .filter(curEntity -> Objects.equals(curEntity.eamId, synDeviceEevent.getCommonDeviceEntity().eamId))
                .map(deviceEntity1 -> {
                    int position = mDeviceEntities.indexOf(deviceEntity1);
                    if(!isSingle)
                        mAdapter.setCheck(position, synDeviceEevent.isChecked());
                    return position;
                })
                .subscribe(integer -> mAdapter.notifyItemChanged(integer));
    }

    @Override
    protected void initPresenter() {
        super.initPresenter();
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(getActivity(), R.color.themeColor);


        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_gap);
        recyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));

    }


    @Override
    protected void initListener() {
        super.initListener();

//        refreshListController.setOnRefreshListener(() -> presenterRouter.create(RecentDeviceAPI.class).getRecentDevice(module));

        refreshListController.setOnRefreshPageListener(pageIndex -> presenterRouter.create(RecentDeviceAPI.class).getRecentDevice(module, pageIndex));

        mAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {

            CommonDeviceEntity commonDeviceEntity = (CommonDeviceEntity) obj;
            SynDeviceEevent synDeviceEevent = new SynDeviceEevent(commonDeviceEntity) ;
            if (null == getActivity()) return;
            if(childView.getId() == R.id.itemAddDeviceCheckBox){
                if (action == 1) {
                    ((AddDeviceActivity) getActivity()).addDevice(commonDeviceEntity);
                } else {
                    ((AddDeviceActivity) getActivity()).deleteDevice(commonDeviceEntity);
                }
                synDeviceEevent.setChecked(action==1);
                EventBus.getDefault().post( synDeviceEevent);
            }
            else{
                if (isSingle) {
                    ((AddDeviceActivity) getActivity()).sendDevice(commonDeviceEntity);
                    getActivity().finish();
                } else {
                    if (action == 1) {
                        ((AddDeviceActivity) getActivity()).addDevice(commonDeviceEntity);
                    } else {
                        ((AddDeviceActivity) getActivity()).deleteDevice(commonDeviceEntity);
                    }
                    synDeviceEevent.setChecked(action==1);
                    EventBus.getDefault().post( synDeviceEevent);
                }

            }
        });

        mSearchAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            CommonDeviceEntity commonDeviceEntity = (CommonDeviceEntity) obj;
            String tag = (String) childView.getTag();
            if (null == getActivity()) return;
            if(childView.getId() == R.id.itemAddDeviceCheckBox){
                if (action == 1) {
                    ((AddDeviceActivity) getActivity()).addDevice(commonDeviceEntity);
                } else {
                    ((AddDeviceActivity) getActivity()).deleteDevice(commonDeviceEntity);
                }
            }
            else{
                //show all
                if (isSingle) {
                    ((AddDeviceActivity) getActivity()).sendDevice(commonDeviceEntity);
                }
            }

        });

    }

    public void addDevices(List<CommonDeviceEntity> deviceEntities) {
        mSearchAdapter.clear();
        mSearchAdapter.addList(deviceEntities);
        recyclerView.setAdapter(mSearchAdapter);
        mSearchAdapter.notifyItemInserted(0);
    }

    public void resetRecentList() {
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyItemInserted(0);
    }

    @Override
    protected IListAdapter<CommonDeviceEntity> createAdapter() {
        mAdapter = new AddDeviceAdapter(context, isSingle);
        mSearchAdapter = new AddDeviceAdapter(context, isSingle);
        return mAdapter;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void onResume() {
        super.onResume();
//        presenterRouter.create(RecentDeviceAPI.class).getRecentDevice(module, 1);
    }

    @Override
    public void getRecentDeviceSuccess(RecentDeviceListEntity recentDeviceListEntity) {
        if (recentDeviceListEntity.devices.size() != 0) {
            mDeviceEntities.addAll(recentDeviceListEntity.devices);
        }
        refreshListController.refreshComplete(recentDeviceListEntity.devices);
    }

    @Override
    public void getRecentDeviceFailed(String errorMsg) {
        refreshListController.refreshComplete();
//        SnackbarHelper.showError(rootView, errorMsg);
    }
}
