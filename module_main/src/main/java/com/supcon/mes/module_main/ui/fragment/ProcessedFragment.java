package com.supcon.mes.module_main.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.beans.FilterBean;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.view.CustomFilterView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.DatePickController;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.CustomFilterBean;
import com.supcon.mes.middleware.model.bean.WXGDEntity;
import com.supcon.mes.middleware.model.bean.YHEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_main.IntentRouter;
import com.supcon.mes.module_main.R;
import com.supcon.mes.module_main.model.api.ProcessedAPI;
import com.supcon.mes.module_main.model.bean.ProcessedEntity;
import com.supcon.mes.module_main.model.contract.ProcessedContract;
import com.supcon.mes.module_main.presenter.ProcessedPresenter;
import com.supcon.mes.module_main.ui.adaper.ProcessedAdapter;
import com.supcon.mes.module_main.ui.util.FilterHelper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    @BindByTag("tableFilter")
    CustomFilterView<CustomFilterBean> tableFilter;
    @BindByTag("waitState")
    RadioGroup waitState;

    private ProcessedAdapter processedAdapter;
    private DatePickController datePickController;
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
        contentView.addItemDecoration(new SpaceItemDecoration(15));
        contentView.setLayoutManager(new LinearLayoutManager(context));

        datePickController = new DatePickController(getActivity());

        datePickController.setCycleDisable(true);
        datePickController.setCanceledOnTouchOutside(true);
        datePickController.setSecondVisible(false);
        datePickController.textSize(18);
    }

    @Override
    protected void initData() {
        super.initData();
        initFilter();
    }
    private void initFilter() {
        tableFilter.setData(FilterHelper.createTableTypeList());
        tableFilter.setCurrentPosition(0);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {
                setRadioEnable(false);
                presenterRouter.create(ProcessedAPI.class).workflowHandleList(queryParam, pageIndex);
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
        tableFilter.setFilterSelectChangedListener(new CustomFilterView.FilterSelectChangedListener<FilterBean>() {
            @Override
            public void onFilterSelected(FilterBean filterBean) {
                CustomFilterBean customFilterBean = (CustomFilterBean)filterBean;
                queryParam.put(Constant.BAPQuery.PROCESS_KEY, customFilterBean.id);
                refreshListController.refreshBegin();
            }
        });

        processedAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            ProcessedEntity processedEntity = (ProcessedEntity) obj;
            Bundle bundle = new Bundle();
            processedEntity.tableId = processedEntity.tableId == null ? -1L : processedEntity.tableId;
            switch (processedEntity.processKey){
                case Constant.ProcessKey.WORK_TICKET:
                    goWorkTicket(processedEntity, bundle);
                    break;
                case Constant.ProcessKey.ELE_ON:
                    bundle.putLong(Constant.IntentKey.TABLE_ID, processedEntity.tableId);
                    IntentRouter.go(context,Constant.Router.HS_ELE_ON_VIEW,bundle);
                    break;
                case Constant.ProcessKey.ELE_OFF:
                    bundle.putLong(Constant.IntentKey.TABLE_ID, processedEntity.tableId);
                    IntentRouter.go(context,Constant.Router.HS_ELE_OFF_VIEW,bundle);
                    break;
//                case Constant.ProcessKey.RUN_STATE_WF:
//                    break;
//                case Constant.ProcessKey.CHECK_APPLY_FW:
//                    break;
                case Constant.ProcessKey.FAULT_INFO:
                    YHEntity yhEntity = new YHEntity();
                    yhEntity.tableNo = processedEntity.workTableNo;
                    bundle.putSerializable(Constant.IntentKey.YHGL_ENTITY,yhEntity);
                    IntentRouter.go(context, Constant.Router.YH_LOOK, bundle);
                    break;
                case Constant.ProcessKey.WORK:
                    WXGDEntity wxgdEntity = new WXGDEntity();
                    wxgdEntity.tableNo = processedEntity.workTableNo;
                    bundle.putSerializable(Constant.IntentKey.WXGD_ENTITY,wxgdEntity);
                    IntentRouter.go(context, Constant.Router.WXGD_COMPLETE, bundle);
                    break;
                    default:
                        ToastUtils.show(context, context.getResources().getString(R.string.main_processed_table_no_view));
            }
        });

    }

     /**
      * @method
      * @description 跳转检修作业票
      * @author: zhangwenshuai
      * @date: 2020/5/30 15:13
      * @param  * @param null
      * @return
      */
    private void goWorkTicket(ProcessedEntity processedEntity, Bundle bundle) {
        if (!TextUtils.isEmpty(processedEntity.summary) && processedEntity.summary.contains("offApplyTableinfoid")) {
            try {
                String json = processedEntity.summary.substring(processedEntity.summary.indexOf("*") +1);
                if (GsonUtil.gsonToMaps(json).get("offApplyTableinfoid") != null) {
                    Double offApplyTableInfoId = (Double) GsonUtil.gsonToMaps(json).get("offApplyTableinfoid");
                    bundle.putLong(Constant.IntentKey.ElE_OFF_TABLE_INFO_ID, Objects.requireNonNull(offApplyTableInfoId).longValue()); // 停电作业票tableInfoId
                }
            } catch (Exception e) {
                e.printStackTrace();
                bundle.putLong(Constant.IntentKey.ElE_OFF_TABLE_INFO_ID,-1);
            }
        }
        bundle.putLong(Constant.IntentKey.TABLE_ID, processedEntity.tableId);
        IntentRouter.go(context, Constant.Router.OVERHAUL_WORKTICKET_VIEW, bundle);
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
        setRadioEnable(true);
    }

    @Override
    public void workflowHandleListFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, ErrorMsgHelper.msgParse(errorMsg));
        refreshListController.refreshComplete(null);
        setRadioEnable(true);
    }

    public void setRadioEnable(boolean enable) {
        for (int i = 0; i < waitState.getChildCount(); i++) {
            waitState.getChildAt(i).setEnabled(enable);
        }
    }

}
