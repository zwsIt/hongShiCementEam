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
import com.supcon.mes.module_sbda_online.model.api.ParamAPI;
import com.supcon.mes.module_sbda_online.model.bean.ParamEntity;
import com.supcon.mes.module_sbda_online.model.bean.ParamListEntity;
import com.supcon.mes.module_sbda_online.model.contract.ParamContract;
import com.supcon.mes.module_sbda_online.presenter.ParamPresenter;
import com.supcon.mes.module_sbda_online.ui.adapter.ParamAdapter;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/3/29
 * ------------- Description -------------
 * 技术参数
 */
@Presenter(value = ParamPresenter.class)
public class ParamFragment extends BaseRefreshRecyclerFragment<ParamEntity> implements ParamContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;
    private static Long eamId;

    public static ParamFragment newInstance(Long id) {
        eamId = id;
        ParamFragment fragment = new ParamFragment();
        return fragment;
    }

    @Override
    protected IListAdapter<ParamEntity> createAdapter() {
        ParamAdapter paramAdapter = new ParamAdapter(getActivity());
        return paramAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_archives_list;
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
            presenterRouter.create(ParamAPI.class).getEamParam(eamId);
        });
    }

    @Override
    public void getEamParamSuccess(ParamListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void getEamParamFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, ErrorMsgHelper.msgParse(errorMsg));
        refreshListController.refreshComplete(null);
    }
}
