package com.supcon.mes.module_sbda.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.com_http.util.RxSchedulers;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.controller.RefreshRecyclerController;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.adapter.RecyclerEmptyAdapter;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.middleware.constant.Module;
import com.supcon.mes.middleware.model.bean.CommonDeviceEntity;
import com.supcon.mes.middleware.model.event.SynDeviceEevent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.EmptyViewHelper;
import com.supcon.mes.middleware.util.SearchTitleBarHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_sbda.R;
import com.supcon.mes.module_sbda.model.api.MyDeviceAPI;
import com.supcon.mes.module_sbda.model.bean.MyDeviceListEntity;
import com.supcon.mes.module_sbda.model.contract.MyDeviceContract;
import com.supcon.mes.module_sbda.presenter.MyDevicePresenter;
import com.supcon.mes.module_sbda.ui.AddDeviceActivity;
import com.supcon.mes.module_sbda.ui.adapter.AddDeviceAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;

/**
 * Created by wangshizhan on 2017/11/23.
 * Email:wangshizhan@supcon.com
 */
@Presenter(value = {MyDevicePresenter.class})
public class MyDeviceFragment extends BaseRefreshRecyclerFragment<CommonDeviceEntity> implements
        MyDeviceContract.View{


    @BindByTag("contentView")
    RecyclerView recyclerView;

    AddDeviceAdapter mAdapter;

    List<CommonDeviceEntity> mDeviceEntities = new ArrayList<>();

    String module;
    boolean isSingle;

    public MyDeviceFragment(){
    }

    @Override
    protected int getLayoutID() {
        return R.layout.frag_device_list;
    }

    @Override
    protected void onInit() {
        module = ((AddDeviceActivity)getActivity()).getModule();
        isSingle = Module.Fault.name().equals(module);
        super.onInit();
        EventBus.getDefault().register(this);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setLoadMoreEnable(true);
        refreshListController.setPullDownRefreshEnabled(true);
    }

    //从list中获取到对应对象的位置-position,通知adapter更新对应的界面信息
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
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(getActivity(), R.color.themeColor);


        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_gap);
        recyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        initEmptyView();
    }

    private void initEmptyView() {
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));
    }

    @Override
    protected void initListener() {
        super.initListener();

//        refreshListController.setOnRefreshListener(() -> presenterRouter.create(MyDeviceAPI.class).getMyDevice(module));

        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {
                presenterRouter.create(MyDeviceAPI.class).getMyDevice(module, pageIndex, 30);
            }
        });

        mAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            CommonDeviceEntity commonDeviceEntity = (CommonDeviceEntity) obj;
            SynDeviceEevent synDeviceEevent = new SynDeviceEevent(commonDeviceEntity);
            if(null == getActivity()) return;

            if(childView.getId() == R.id.itemAddDeviceCheckBox){
                if(action == 1) {
                    ((AddDeviceActivity) getActivity()).addDevice(commonDeviceEntity);
                }
                else{
                    ((AddDeviceActivity) getActivity()).deleteDevice(commonDeviceEntity);
                }
                synDeviceEevent.setChecked(action==1);
                EventBus.getDefault().post(synDeviceEevent);
            }
            else{
                if(isSingle){
                    ((AddDeviceActivity) getActivity()).sendDevice((commonDeviceEntity));
                }
                else{
                    if(action == 1) {
                        ((AddDeviceActivity) getActivity()).addDevice(commonDeviceEntity);
                    }
                    else{
                        ((AddDeviceActivity) getActivity()).deleteDevice(commonDeviceEntity);
                    }

                    synDeviceEevent.setChecked(action==1);
                    EventBus.getDefault().post(synDeviceEevent);
                }

            }

        });

    }

    @Override
    protected IListAdapter<CommonDeviceEntity> createAdapter() {
        mAdapter = new AddDeviceAdapter(context, isSingle);
        return mAdapter;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void onResume() {
        super.onResume();
//        presenterRouter.create(MyDeviceAPI.class).getMyDevice(module, 1, 30);
    }


    @Override
    public void getMyDeviceSuccess(MyDeviceListEntity entity) {
        mDeviceEntities.addAll(entity.devices);
        SearchTitleBarHelper.addDeviceList(entity.devices);
        refreshListController.refreshComplete(entity.devices);
    }


    @Override
    public void getMyDeviceFailed(String errorMsg) {
        refreshListController.refreshComplete();
        SnackbarHelper.showError(rootView, errorMsg);
    }
}
