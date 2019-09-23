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
import com.supcon.mes.module_sbda_online.model.api.RepairAPI;
import com.supcon.mes.module_sbda_online.model.bean.RepairListEntity;
import com.supcon.mes.module_sbda_online.model.contract.RepairContract;
import com.supcon.mes.module_sbda_online.presenter.RepairPresenter;
import com.supcon.mes.module_sbda_online.ui.adapter.RepairAdapter;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/3/29
 * ------------- Description -------------
 * 维修
 */
@Presenter(value = RepairPresenter.class)
public class RepairFragment extends BaseRefreshRecyclerFragment implements RepairContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;

    private static Long eamId;

    public static RepairFragment newInstance(Long id) {
        eamId = id;
        RepairFragment fragment = new RepairFragment();
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
        refreshListController.setOnRefreshPageListener((page) -> {

            presenterRouter.create(RepairAPI.class).workRecord(eamId, page);
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_archives_list;
    }

    @Override
    protected IListAdapter createAdapter() {
        RepairAdapter repairAdapter = new RepairAdapter(getActivity());
        return repairAdapter;
    }

    @Override
    public void workRecordSuccess(RepairListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void workRecordFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, ErrorMsgHelper.msgParse(errorMsg));
        refreshListController.refreshComplete(null);
    }
}
