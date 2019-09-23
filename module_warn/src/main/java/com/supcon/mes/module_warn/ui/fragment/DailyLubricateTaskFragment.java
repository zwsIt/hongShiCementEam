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
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_warn.IntentRouter;
import com.supcon.mes.module_warn.R;
import com.supcon.mes.module_warn.model.api.DailyLubricationWarnAPI;
import com.supcon.mes.module_warn.model.api.DailyReceiveAPI;
import com.supcon.mes.module_warn.model.bean.DailyLubricateTaskEntity;
import com.supcon.mes.module_warn.model.bean.DailyLubricateTaskListEntity;
import com.supcon.mes.module_warn.model.bean.DelayEntity;
import com.supcon.mes.module_warn.model.contract.DailyLubricationWarnContract;
import com.supcon.mes.module_warn.model.contract.DailyReceiveContract;
import com.supcon.mes.module_warn.model.event.TabEvent;
import com.supcon.mes.module_warn.presenter.DailyLubricationWarnPresenter;
import com.supcon.mes.module_warn.presenter.DailyReceivePresenter;
import com.supcon.mes.module_warn.ui.adapter.DailyLubricateTaskAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

@Presenter(value = {DailyLubricationWarnPresenter.class, DailyReceivePresenter.class})
public class DailyLubricateTaskFragment extends BaseRefreshRecyclerFragment<DailyLubricateTaskEntity> implements DailyLubricationWarnContract.View, DailyReceiveContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;
    private DailyLubricateTaskEntity lubricationWarnTitle;

    private final Map<String, Object> queryParam = new HashMap<>();
    private String staffName = "";//责任人
    private DailyLubricateTaskAdapter dailyLubricateTaskAdapter;
    private int positionTitle = 0;
    private List<DailyLubricateTaskEntity> dailyLubricateTaskEntities;

    public static DailyLubricateTaskFragment newInstance() {
        DailyLubricateTaskFragment fragment = new DailyLubricateTaskFragment();
        return fragment;
    }

    @Override
    protected IListAdapter createAdapter() {
        dailyLubricateTaskAdapter = new DailyLubricateTaskAdapter(getActivity(), true);
        return dailyLubricateTaskAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_lubricate_list;
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
            Map<String, Object> pageQueryParams = new HashMap<>();
            pageQueryParams.put("isReceive", "unreceive");
            pageQueryParams.put("page.pageNo", page);
            presenterRouter.create(DailyLubricationWarnAPI.class).getLubrications(queryParam, pageQueryParams);
        });
        dailyLubricateTaskAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                int id = childView.getId();
                DailyLubricateTaskEntity item = dailyLubricateTaskAdapter.getItem(position);
                if (id == R.id.eamName) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.IntentKey.EAM_CODE, item.getEamID().code);
                    bundle.putBoolean(Constant.IntentKey.isEdit, false);
                    IntentRouter.go(getActivity(), Constant.Router.DAILY_LUBRICATION_EARLY_PART_WARN, bundle);
                } else if (id == R.id.receive) {
                    StringBuffer sourceIds = new StringBuffer();
                    Flowable.fromIterable(item.lubricationPartMap.values())
                            .map(dailyLubricateTaskEntities -> Flowable.fromIterable(dailyLubricateTaskEntities))
                            .subscribe(dailyLubricateTaskEntityFlowable -> dailyLubricateTaskEntityFlowable.subscribe(dailyLubricateTaskEntity -> {
                                if (TextUtils.isEmpty(sourceIds)) {
                                    sourceIds.append(dailyLubricateTaskEntity.id);
                                } else {
                                    sourceIds.append(",").append(dailyLubricateTaskEntity.id);
                                }
                            }), throwable -> {
                            }, () -> {
                                if (!TextUtils.isEmpty(sourceIds.toString())) {
                                    Map<String, Object> param = new HashMap<>();
                                    param.put(Constant.BAPQuery.sourceIds, sourceIds);
                                    param.put("staffId", EamApplication.getAccountInfo().staffId);
                                    onLoading("领取任务中...");
                                    presenterRouter.create(DailyReceiveAPI.class).receiveTask(param);
                                } else {
                                    ToastUtils.show(getActivity(), "当前设备没有润滑部位!");
                                }
                            });
                }
            }
        });
        //滚动监听
        dailyLubricateTaskAdapter.setOnScrollListener(pos -> contentView.scrollToPosition(pos));
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
                    if (lubricationWarnTitle != null) {
                        lubricationWarnTitle.lubricationMap.put(lubricationWarnEntity.getEamID().code, lubricationWarnEntity);

                        if (lubricationWarnTitle.lubricationPartMap.containsKey(lubricationWarnEntity.getEamID().code)) {
                            dailyLubricateTaskEntities = lubricationWarnTitle.lubricationPartMap.get(lubricationWarnEntity.getEamID().code);
                        } else {
                            dailyLubricateTaskEntities = new ArrayList<>();
                        }
                        dailyLubricateTaskEntities.add(lubricationWarnEntity);
                        lubricationWarnTitle.lubricationPartMap.put(lubricationWarnEntity.getEamID().code, dailyLubricateTaskEntities);
                    }
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
    public void receiveTaskSuccess(DelayEntity entity) {
        onLoadSuccessAndExit("领取成功", () -> {
            refreshListController.refreshBegin();
            EventBus.getDefault().post(new TabEvent());
        });
    }

    @Override
    public void receiveTaskFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }
}
