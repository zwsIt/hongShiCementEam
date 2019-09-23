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
import com.supcon.mes.module_sbda_online.model.api.LubriAPI;
import com.supcon.mes.module_sbda_online.model.bean.LubriEntity;
import com.supcon.mes.module_sbda_online.model.bean.LubriListEntity;
import com.supcon.mes.module_sbda_online.model.contract.LubriContract;
import com.supcon.mes.module_sbda_online.presenter.LubriPresenter;
import com.supcon.mes.module_sbda_online.ui.adapter.LubriAdapter;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/3/29
 * ------------- Description -------------
 * 润滑
 */
@Presenter(LubriPresenter.class)
public class LubricationFragment extends BaseRefreshRecyclerFragment<LubriEntity> implements LubriContract.View {


    @BindByTag("contentView")
    RecyclerView contentView;
    private static Long eamId;

    public static LubricationFragment newInstance(Long id) {
        eamId = id;
        LubricationFragment fragment = new LubricationFragment();
        return fragment;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_archives_list;
    }

    @Override
    protected IListAdapter createAdapter() {
        LubriAdapter lubriAdapter = new LubriAdapter(getActivity());
        return lubriAdapter;
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
            presenterRouter.create(LubriAPI.class).lubriRecord(eamId);
        });
    }

    @Override
    public void lubriRecordSuccess(LubriListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void lubriRecordFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, ErrorMsgHelper.msgParse(errorMsg));
        refreshListController.refreshComplete(null);
    }
}
