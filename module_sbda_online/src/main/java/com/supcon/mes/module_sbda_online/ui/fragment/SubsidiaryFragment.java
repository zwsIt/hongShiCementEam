package com.supcon.mes.module_sbda_online.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_sbda_online.IntentRouter;
import com.supcon.mes.module_sbda_online.R;
import com.supcon.mes.module_sbda_online.model.api.SubsidiaryAPI;
import com.supcon.mes.module_sbda_online.model.bean.SubsidiaryEntity;
import com.supcon.mes.module_sbda_online.model.bean.SubsidiaryListEntity;
import com.supcon.mes.module_sbda_online.model.contract.SubsidiaryContract;
import com.supcon.mes.module_sbda_online.presenter.SubsidiaryPresenter;
import com.supcon.mes.module_sbda_online.ui.adapter.SubsidiaryAdapter;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/3/29
 * ------------- Description -------------
 * 附属
 */
@Presenter(SubsidiaryPresenter.class)
public class SubsidiaryFragment extends BaseRefreshRecyclerFragment<SubsidiaryEntity> implements SubsidiaryContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;

    private static Long eamId;
    private SubsidiaryAdapter subsidiaryAdapter;

    public static SubsidiaryFragment newInstance(Long id) {
        eamId = id;
        SubsidiaryFragment fragment = new SubsidiaryFragment();
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
        refreshListController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenterRouter.create(SubsidiaryAPI.class).attachPart(eamId);
            }
        });
        subsidiaryAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                SubsidiaryEntity item = subsidiaryAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putLong(Constant.IntentKey.SBDA_ONLINE_EAMID, item.getAttachEamId().id);
                bundle.putString(Constant.IntentKey.SBDA_ONLINE_EAMCODE, item.getAttachEamId().code);
                IntentRouter.go(context, Constant.Router.SBDA_ONLINE_VIEW, bundle);
                getActivity().finish();
            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_archives_list;
    }

    @Override
    protected IListAdapter createAdapter() {
        subsidiaryAdapter = new SubsidiaryAdapter(getActivity());
        return subsidiaryAdapter;
    }

    @Override
    public void attachPartSuccess(SubsidiaryListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void attachPartFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, ErrorMsgHelper.msgParse(errorMsg));
        refreshListController.refreshComplete(null);
    }
}
