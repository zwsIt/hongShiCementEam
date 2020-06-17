package com.supcon.mes.module_main.ui.fragment;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_main.R;
import com.supcon.mes.module_main.model.api.ProcessedAPI;
import com.supcon.mes.middleware.model.bean.ProcessedEntity;
import com.supcon.mes.module_main.model.contract.ProcessedContract;
import com.supcon.mes.module_main.presenter.ProcessedPresenter;
import com.supcon.mes.module_main.ui.adaper.ProcessedAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/5/11
 * Email zhangwenshuai1@supcon.com
 * Desc 已处理ProcessedFragment
 */
@Presenter(value = ProcessedPresenter.class)
public class ProcessedFragment extends BaseRefreshRecyclerFragment<ProcessedEntity> implements ProcessedContract.View {
    @BindByTag("contentView")
    RecyclerView contentView;
    @BindByTag("waitState")
    RadioGroup waitState;

    private ProcessedAdapter processedAdapter;
    private Map<String, Object> queryParam = new HashMap<>();

    @Override
    protected IListAdapter<ProcessedEntity> createAdapter() {
        processedAdapter = new ProcessedAdapter(context);
        return processedAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.main_fragment_processed;
    }

    @Override
    protected void initView() {
        super.initView();
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(5,context)));
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {
                presenterRouter.create(ProcessedAPI.class).workflowHandleList(queryParam, pageIndex,20);
            }
        });

        waitState.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = getActivity().findViewById(checkedId);
                queryParam.put(Constant.BAPQuery.NEWSTATE, "全部".equals(radioButton.getText().toString())? "" : radioButton.getText().toString());
                refreshListController.refreshBegin();
            }
        });

        processedAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(LoginEvent loginEvent) {
        refreshListController.refreshBegin();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshEvent event) {
        refreshListController.refreshBegin();
    }

    @Override
    public void workflowHandleListSuccess(CommonBAPListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void workflowHandleListFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, ErrorMsgHelper.msgParse(errorMsg));
        refreshListController.refreshComplete(null);
    }


    public void doFilter(String processKey) {
        queryParam.put(Constant.BAPQuery.PROCESS_KEY, processKey);
        refreshListController.refreshBegin();
    }
}
