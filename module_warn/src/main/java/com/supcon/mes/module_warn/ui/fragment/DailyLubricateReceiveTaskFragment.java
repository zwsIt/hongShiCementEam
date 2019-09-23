package com.supcon.mes.module_warn.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.constant.ListType;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.event.NFCEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_warn.IntentRouter;
import com.supcon.mes.module_warn.R;
import com.supcon.mes.module_warn.model.api.DailyLubricationWarnAPI;
import com.supcon.mes.module_warn.model.bean.DailyLubricateTaskEntity;
import com.supcon.mes.module_warn.model.bean.DailyLubricateTaskListEntity;
import com.supcon.mes.module_warn.model.contract.DailyLubricationWarnContract;
import com.supcon.mes.module_warn.model.event.TabEvent;
import com.supcon.mes.module_warn.presenter.DailyLubricationWarnPresenter;
import com.supcon.mes.module_warn.ui.adapter.DailyLubricateTaskAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

@Presenter(value = {DailyLubricationWarnPresenter.class})
public class DailyLubricateReceiveTaskFragment extends BaseRefreshRecyclerFragment<DailyLubricateTaskEntity> implements DailyLubricationWarnContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;
    private DailyLubricateTaskEntity lubricationWarnTitle;

    private final Map<String, Object> queryParam = new HashMap<>();
    private String staffName = "";//责任人
    private DailyLubricateTaskAdapter dailyLubricateTaskAdapter;
    private int positionTitle = 0;
    private boolean isFront;

    private List<DailyLubricateTaskEntity> dailyLubricateTaskEntities;

    public static DailyLubricateReceiveTaskFragment newInstance() {
        DailyLubricateReceiveTaskFragment fragment = new DailyLubricateReceiveTaskFragment();
        return fragment;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
    }

    @Override
    protected IListAdapter createAdapter() {
        dailyLubricateTaskAdapter = new DailyLubricateTaskAdapter(getActivity(), false);
        return dailyLubricateTaskAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_lubricate_list;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isFront = true;
        } else {
            isFront = false;
        }
    }

    @Override
    protected void initView() {
        super.initView();
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));
        contentView.setLayoutManager(new LinearLayoutManager(context));

    }

    @Override
    protected void initListener() {
        super.initListener();
        refreshListController.setOnRefreshPageListener((page) -> {
            queryParam.put(Constant.BAPQuery.NAME, EamApplication.getAccountInfo().staffName);
            Map<String, Object> pageQueryParams = new HashMap<>();
            pageQueryParams.put("page.pageNo", page);
            presenterRouter.create(DailyLubricationWarnAPI.class).getLubrications(queryParam, pageQueryParams);
        });
        dailyLubricateTaskAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                DailyLubricateTaskEntity item = dailyLubricateTaskAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putString(Constant.IntentKey.EAM_CODE, item.getEamID().code);
                bundle.putBoolean(Constant.IntentKey.isEdit, true);
                IntentRouter.go(getActivity(), Constant.Router.DAILY_LUBRICATION_EARLY_PART_WARN, bundle);
            }
        });
        //滚动监听
        dailyLubricateTaskAdapter.setOnScrollListener(pos -> contentView.scrollToPosition(pos));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(RefreshEvent event) {
        refreshListController.refreshBegin();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void tabChange(TabEvent tabEvent) {
        refreshListController.refreshBegin();
    }

    /**
     * @param
     * @description NFC事件
     * @author zhangwenshuai1
     * @date 2018/6/28
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getNFC(NFCEvent nfcEvent) {
        if (isFront) {
            LogUtil.d("NFC_TAG", nfcEvent.getNfc());
            Map<String, Object> nfcJson = Util.gsonToMaps(nfcEvent.getNfc());
            if (nfcJson.get("textRecord") == null) {
                ToastUtils.show(context, "标签内容空！");
                return;
            }
            doDeal((String) nfcJson.get("textRecord"));
        }
    }

    @SuppressLint("CheckResult")
    private void doDeal(String code) {
        List<DailyLubricateTaskEntity> list = dailyLubricateTaskAdapter.getList();
        if (list == null)
            return;
        if (list.size() <= 0) {
            SnackbarHelper.showMessage(contentView, "没有润滑设备!");
            return;
        }
        for (DailyLubricateTaskEntity dailyLubricateTaskEntity : list) {
            if (dailyLubricateTaskEntity.getEamID().code.equals(code)) {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.IntentKey.EAM_CODE, code);
                IntentRouter.go(getActivity(), Constant.Router.DAILY_LUBRICATION_EARLY_PART_ENSURE_WARN, bundle);

//                List<DailyLubricateTaskEntity> dailyLubricateTaskEntities = dailyLubricateTaskEntity.lubricationPartMap.get(code);
//                StringBuffer sourceIds = new StringBuffer();
//                Flowable.fromIterable(dailyLubricateTaskEntities)
//                        .subscribe(lubricationWarnEntity -> {
//                            if (TextUtils.isEmpty(sourceIds)) {
//                                sourceIds.append(lubricationWarnEntity.id);
//                            } else {
//                                sourceIds.append(",").append(lubricationWarnEntity.id);
//                            }
//                        }, throwable -> {
//                        }, () -> {
//                            if (!TextUtils.isEmpty(sourceIds)) {
//                                Map<String, Object> param = new HashMap<>();
//                                param.put(Constant.BAPQuery.sourceIds, sourceIds);
//                                onLoading("处理中...");
//                                presenterRouter.create(CompleteAPI.class).dailyComplete(param);
//                            } else {
//                                ToastUtils.show(getActivity(), "当前设备没有润滑部位!");
//                            }
//                        });

                return;
            }
        }
        ToastUtils.show(getActivity(), "(" + code + ")设备无待执行润滑任务!");
    }

    @SuppressLint("CheckResult")
    @Override
    public void getLubricationsSuccess(DailyLubricateTaskListEntity entity) {
        if (entity.pageNo == 1) {
            positionTitle = 0;
        }
        LinkedList<DailyLubricateTaskEntity> lubricationWarnEntities = new LinkedList<>();
        Flowable.fromIterable(entity.result)
                .subscribe(lubricationWarnEntity -> {
                    if (!TextUtils.isEmpty(lubricationWarnEntity.getEamID().getInspectionStaff().getName()) &&
                            !lubricationWarnEntity.getEamID().getInspectionStaff().getName().equals(staffName)) {
                        positionTitle++;
                        staffName = lubricationWarnEntity.getEamID().getInspectionStaff().getName();
                        lubricationWarnTitle = new DailyLubricateTaskEntity();
                        lubricationWarnTitle.viewType = ListType.TITLE.value();
                        lubricationWarnTitle.eamID = lubricationWarnEntity.getEamID();
                        lubricationWarnTitle.position = positionTitle;
                        lubricationWarnEntities.add(lubricationWarnTitle);
                    }
                    lubricationWarnEntity.viewType = ListType.CONTENT.value();
                    lubricationWarnTitle.lubricationMap.put(lubricationWarnEntity.getEamID().code, lubricationWarnEntity);

                    if (lubricationWarnTitle.lubricationPartMap.containsKey(lubricationWarnEntity.getEamID().code)) {
                        dailyLubricateTaskEntities = lubricationWarnTitle.lubricationPartMap.get(lubricationWarnEntity.getEamID().code);
                    } else {
                        dailyLubricateTaskEntities = new ArrayList<>();
                    }
                    dailyLubricateTaskEntities.add(lubricationWarnEntity);
                    lubricationWarnTitle.lubricationPartMap.put(lubricationWarnEntity.getEamID().code, dailyLubricateTaskEntities);
                }, throwable -> {
                    LogUtil.e(throwable.toString());
                }, () -> {
                    staffName = "";
                    refreshListController.refreshComplete(lubricationWarnEntities);
                });
    }

    @Override
    public void getLubricationsFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, ErrorMsgHelper.msgParse(errorMsg));
        refreshListController.refreshComplete(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
