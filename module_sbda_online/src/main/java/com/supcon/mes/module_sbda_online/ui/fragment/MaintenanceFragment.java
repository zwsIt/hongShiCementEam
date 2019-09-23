package com.supcon.mes.module_sbda_online.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_sbda_online.R;
import com.supcon.mes.module_sbda_online.model.api.MaintenanceAPI;
import com.supcon.mes.module_sbda_online.model.bean.MaintenanceListEntity;
import com.supcon.mes.module_sbda_online.model.contract.MaintenanceContract;
import com.supcon.mes.module_sbda_online.presenter.MaintenancePresenter;
import com.supcon.mes.module_sbda_online.ui.adapter.MaintenanceAdapter;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/3/29
 * ------------- Description -------------
 * 维保
 */
@Presenter(MaintenancePresenter.class)
public class MaintenanceFragment extends BaseRefreshRecyclerFragment implements MaintenanceContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;

    private static Long eamId;

    public static MaintenanceFragment newInstance(Long id) {
        eamId = id;
        MaintenanceFragment fragment = new MaintenanceFragment();
        return fragment;
    }

    @Override
    protected void initView() {
        super.initView();
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(15));

    }

    @Override
    protected void initListener() {
        super.initListener();
        refreshListController.setOnRefreshListener(() -> {
            presenterRouter.create(MaintenanceAPI.class).maintenanceRecord(eamId);
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_archives_list;
    }

    @Override
    protected IListAdapter createAdapter() {
        MaintenanceAdapter maintenanceAdapter = new MaintenanceAdapter(getActivity());
        return maintenanceAdapter;
    }

    @Override
    public void maintenanceRecordSuccess(MaintenanceListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void maintenanceRecordFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, ErrorMsgHelper.msgParse(errorMsg));
        refreshListController.refreshComplete(null);
    }
}
