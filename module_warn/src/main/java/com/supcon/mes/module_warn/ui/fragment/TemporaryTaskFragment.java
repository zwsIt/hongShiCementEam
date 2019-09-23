package com.supcon.mes.module_warn.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshFragment;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.EamType;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.model.event.NFCEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_warn.IntentRouter;
import com.supcon.mes.module_warn.R;
import com.supcon.mes.module_warn.model.api.CompleteAPI;
import com.supcon.mes.module_warn.model.api.TemporaryAPI;
import com.supcon.mes.module_warn.model.bean.DelayEntity;
import com.supcon.mes.module_warn.model.bean.TemLubricateTaskEntity;
import com.supcon.mes.module_warn.model.contract.CompleteContract;
import com.supcon.mes.module_warn.model.contract.TemporaryContract;
import com.supcon.mes.module_warn.presenter.CompletePresenter;
import com.supcon.mes.module_warn.presenter.TemporaryPresenter;
import com.supcon.mes.module_warn.ui.adapter.TemporaryAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;

@Presenter(value = {TemporaryPresenter.class, CompletePresenter.class})
public class TemporaryTaskFragment extends BaseRefreshFragment implements TemporaryContract.View, CompleteContract.View {

    @BindByTag("recyclerView")
    RecyclerView recyclerView;
    @BindByTag("eamCode")
    CustomVerticalTextView eamCode;
    @BindByTag("eamName")
    CustomVerticalTextView eamName;
    @BindByTag("ensure")
    Button ensure;


    private TemporaryAdapter temporaryAdapter;
    private final Map<String, Object> queryParam = new HashMap<>();

    private boolean isFront;

    public static TemporaryTaskFragment newInstance() {
        TemporaryTaskFragment fragment = new TemporaryTaskFragment();
        return fragment;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_temporary;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
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
        refreshController.setAutoPullDownRefresh(false);
        refreshController.setPullDownRefreshEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        temporaryAdapter = new TemporaryAdapter(context);
        recyclerView.setAdapter((BaseListDataRecyclerViewAdapter) EmptyAdapterHelper.getRecyclerEmptyAdapter(context, "请刷卡获取设备"));
    }


    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        refreshController.setOnRefreshListener(() -> {
            Map<String, Object> pageQueryParams = new HashMap<>();
            pageQueryParams.put("page.pageNo", 1);
            presenterRouter.create(TemporaryAPI.class).getTempLubrications(queryParam, pageQueryParams);
        });

        RxView.clicks(ensure)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    List<TemLubricateTaskEntity> list = temporaryAdapter.getList();
                    StringBuffer sourceIds = new StringBuffer();
                    Flowable.fromIterable(list)
                            .filter(temLubricateTaskEntity -> temLubricateTaskEntity.isCheck)
                            .subscribe(temLubricateTaskEntity -> {
                                temLubricateTaskEntity.isLubri = true;
                                if (TextUtils.isEmpty(sourceIds)) {
                                    sourceIds.append(temLubricateTaskEntity.id);
                                } else {
                                    sourceIds.append(",").append(temLubricateTaskEntity.id);
                                }
                            }, throwable -> {
                            }, () -> {
                                if (!TextUtils.isEmpty(sourceIds)) {
                                    Map<String, Object> param = new HashMap<>();
                                    param.put(Constant.BAPQuery.sourceIds, sourceIds);
                                    param.put("taskType", "BEAM_067/02");
                                    onLoading("处理中...");
                                    presenterRouter.create(CompleteAPI.class).dailyComplete(param);
                                } else {
                                    ToastUtils.show(context, "请选择操作项!");
                                }
                            });
                });
        eamCode.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constant.IntentKey.IS_MAIN_EAM, true);
                IntentRouter.go(getActivity(), Constant.Router.EAM, bundle);
            }
        });
        eamName.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constant.IntentKey.IS_MAIN_EAM, true);
                IntentRouter.go(getActivity(), Constant.Router.EAM, bundle);
            }
        });
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
            eamCode.setContent(String.valueOf(nfcJson.get("textRecord")));
            queryParam.put(Constant.IntentKey.EAM_CODE, nfcJson.get("textRecord"));
            refreshController.refreshBegin();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void search(CommonSearchEvent commonSearchEvent) {
        if (commonSearchEvent.commonSearchEntity != null) {
            if (commonSearchEvent.commonSearchEntity instanceof EamType) {
                EamType eamType = (EamType) commonSearchEvent.commonSearchEntity;
                eamCode.setContent(Util.strFormat(eamType.code));
                eamName.setContent(Util.strFormat(eamType.name));
                queryParam.put(Constant.IntentKey.EAM_CODE, Util.strFormat(eamType.code));
                refreshController.refreshBegin();
            }
        }
    }

    @Override
    public void getTempLubricationsSuccess(CommonBAPListEntity entity) {
        if (entity.result != null && entity.result.size() > 0) {
            ensure.setVisibility(View.VISIBLE);
            TemLubricateTaskEntity dailyLubricateTaskEntity = (TemLubricateTaskEntity) entity.result.get(0);
            eamCode.setContent(dailyLubricateTaskEntity.getEamID().code);
            eamName.setContent(dailyLubricateTaskEntity.getEamID().name);
            temporaryAdapter.setList(entity.result);
            recyclerView.setAdapter(temporaryAdapter);
        } else {
            ensure.setVisibility(View.GONE);
            recyclerView.setAdapter((BaseListDataRecyclerViewAdapter) EmptyAdapterHelper.getRecyclerEmptyAdapter(context, "当前设备没有待润滑任务"));
        }
        refreshController.refreshComplete();
    }

    @Override
    public void getTempLubricationsFailed(String errorMsg) {
        ensure.setVisibility(View.GONE);
        recyclerView.setAdapter((BaseListDataRecyclerViewAdapter) EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));
        SnackbarHelper.showError(rootView, errorMsg);
    }

    @Override
    public void dailyCompleteSuccess(DelayEntity entity) {
        onLoadSuccessAndExit("任务完成", () -> {
            getActivity().finish();
        });
    }

    @Override
    public void dailyCompleteFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
