package com.supcon.mes.module_warn.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.constant.ListType;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.UserPowerCheckController;
import com.supcon.mes.middleware.model.event.NFCEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.NFCHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_warn.IntentRouter;
import com.supcon.mes.module_warn.R;
import com.supcon.mes.module_warn.constant.WarnConstant;
import com.supcon.mes.module_warn.model.api.CompleteAPI;
import com.supcon.mes.module_warn.model.api.DailyLubricationWarnAPI;
import com.supcon.mes.module_warn.model.bean.DailyLubricateRecordEntity;
import com.supcon.mes.module_warn.model.bean.DailyLubricateTaskEntity;
import com.supcon.mes.module_warn.model.bean.DailyLubricateTaskListEntity;
import com.supcon.mes.module_warn.model.bean.DelayEntity;
import com.supcon.mes.module_warn.model.contract.CompleteContract;
import com.supcon.mes.module_warn.model.contract.DailyLubricationWarnContract;
import com.supcon.mes.module_warn.presenter.CompletePresenter;
import com.supcon.mes.module_warn.presenter.DailyLubricationWarnPresenter;
import com.supcon.mes.module_warn.presenter.DailyReceivePresenter;
import com.supcon.mes.module_warn.ui.adapter.DailyLubricateTaskAdapter;
import com.supcon.mes.module_warn.ui.adapter.PlanLubricationListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import common.Assert;
import io.reactivex.Flowable;
import io.reactivex.functions.Predicate;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/5/20
 * Email zhangwenshuai1@supcon.com
 * Desc 计划润滑(日常润滑预警)
 */
@Router(Constant.Router.WARN_PLAN_LUBRICATION_NEW)
@Presenter(value = {DailyLubricationWarnPresenter.class, CompletePresenter.class})
@Controller(value = {UserPowerCheckController.class})
public class PlanLubricationWarnNewActivity extends BaseRefreshRecyclerActivity<DailyLubricateTaskEntity> implements DailyLubricationWarnContract.View, CompleteContract.View {
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("lubricationStaff")
    CustomTextView lubricationStaff;
    @BindByTag("lubricationNum")
    CustomTextView lubricationNum;
    @BindByTag("contentView")
    RecyclerView contentView;

    private NFCHelper nfcHelper;
    private PlanLubricationListAdapter planLubricationListAdapter;
    private final Map<String, Object> queryParam = new HashMap<>();
    private DailyLubricateTaskEntity lubricationWarnTitle;
    private Long nextTime = 0L;

    @Override
    protected IListAdapter<DailyLubricateTaskEntity> createAdapter() {
        planLubricationListAdapter = new PlanLubricationListAdapter(this, false);
        return planLubricationListAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.warn_ac_plan_lubrication_list;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(3,context)));
        nfcHelper = NFCHelper.getInstance();
        if (nfcHelper != null) {
            nfcHelper.setup(context);
            nfcHelper.setOnNFCListener(new NFCHelper.OnNFCListener() {
                @Override
                public void onNFCReceived(String nfc) {
                    LogUtil.d("NFC Received : " + nfc);
                    EventBus.getDefault().post(new NFCEvent(nfc));
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (nfcHelper != null)
            nfcHelper.onResumeNFC(this);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(context.getResources().getString(R.string.warn_plan_lubrication));
        lubricationStaff.setContent(EamApplication.getAccountInfo().staffName);
        initOperateCodePermission();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        RxView.clicks(leftBtn)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(o -> onBackPressed());
        refreshListController.setOnRefreshPageListener((page) -> {
//            queryParam.put(Constant.BAPQuery.NAME, EamApplication.getAccountInfo().staffName);
            Map<String, Object> pageQueryParams = new HashMap<>();
            pageQueryParams.put("page.pageNo", page);
            presenterRouter.create(DailyLubricationWarnAPI.class).getLubrications(queryParam, pageQueryParams);
        });
        planLubricationListAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            DailyLubricateTaskEntity item = (DailyLubricateTaskEntity) obj;
            String tag = childView.getTag().toString();
            switch (tag){
                case "titleRl":
                    List<DailyLubricateTaskEntity> expandList = item.getExpendList();
                    if (item.isExpand) {
                        item.isExpand = false;
                        planLubricationListAdapter.getList().removeAll(expandList);
                        planLubricationListAdapter.notifyItemRangeRemoved(position + 1, expandList.size());
                        planLubricationListAdapter.notifyItemRangeChanged(position, planLubricationListAdapter.getItemCount());
                    } else {
                        doExpand(item,position);
                    }
                    break;
                case "finish":
                    finishLubrication(item,null);
                    break;
                case "delay":
                    delayLubrication(item);
                    break;

            }
        });
    }

    @SuppressLint("CheckResult")
    /**
     * 润滑延期
     */
    private void delayLubrication(DailyLubricateTaskEntity item) {
        List<DailyLubricateTaskEntity> list = planLubricationListAdapter.getList();
        StringBuffer sourceIds = new StringBuffer();
        Bundle bundle = new Bundle();
        Flowable.fromIterable(list)
                .filter(new Predicate<DailyLubricateTaskEntity>() {
                    @Override
                    public boolean test(DailyLubricateTaskEntity lubricationWarnEntity) throws Exception {
                        return !TextUtils.isEmpty(item.getEamID().code) && item.getEamID().code.equals(lubricationWarnEntity.eamID.code)
                                && lubricationWarnEntity.isCheck;
                    }
                })
                .subscribe(lubricationWarnEntity -> {
                    bundle.putString(Constant.IntentKey.WARN_PEROID_TYPE, lubricationWarnEntity.periodType != null ? lubricationWarnEntity.periodType.id : "");
                    if (TextUtils.isEmpty(sourceIds)) {
                        sourceIds.append(lubricationWarnEntity.id);
                    } else {
                        sourceIds.append(",").append(lubricationWarnEntity.id);
                    }
                    if (!lubricationWarnEntity.isDuration() && nextTime < lubricationWarnEntity.nextTime) {
                        nextTime = lubricationWarnEntity.nextTime;
                    }
                }, throwable -> {
                }, () -> {
                    if (!TextUtils.isEmpty(sourceIds)) {
                        bundle.putString(Constant.IntentKey.WARN_SOURCE_TYPE, "BEAM062/01");
                        bundle.putString(Constant.IntentKey.WARN_SOURCE_IDS, sourceIds.toString());
                        bundle.putLong(Constant.IntentKey.WARN_NEXT_TIME, nextTime);
                        IntentRouter.go(this, Constant.Router.DELAYDIALOG, bundle);
                    } else {
                        ToastUtils.show(this, "请选择操作项!");
                    }
                });
    }

    @SuppressLint("CheckResult")
    /**
     * 润滑完成
     */
    private void finishLubrication(DailyLubricateTaskEntity item, String nfcCard) {
        String eamCode;
        if (TextUtils.isEmpty(nfcCard)){
            eamCode = item.getEamID().code;
        }else {
            eamCode = nfcCard;
        }
        List<DailyLubricateTaskEntity> list = planLubricationListAdapter.getList();
        StringBuffer sourceIds = new StringBuffer();
        Flowable.fromIterable(list)
                .filter(lubricationWarnEntity -> !TextUtils.isEmpty(eamCode) && eamCode.equals(lubricationWarnEntity.eamID.code)
                && lubricationWarnEntity.isCheck)
                .subscribe(lubricationWarnEntity -> {
                    if (TextUtils.isEmpty(sourceIds)) {
                        sourceIds.append(lubricationWarnEntity.id);
                    } else {
                        sourceIds.append(",").append(lubricationWarnEntity.id);
                    }
                }, throwable -> {
                }, () -> {
                    if (!TextUtils.isEmpty(sourceIds)) {
                        Map<String, Object> param = new HashMap<>();
                        param.put(Constant.BAPQuery.sourceIds, sourceIds);
                        param.put("taskType", "BEAM_067/01"); // 日常润滑
                        onLoading("处理中...");
                        presenterRouter.create(CompleteAPI.class).dailyComplete(param);
                    } else {
                        ToastUtils.show(this, "请选择操作项!");
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(RefreshEvent event) {
        refreshListController.refreshBegin();
    }

    /**
     * 操作按钮权限初始化
     */
    private void initOperateCodePermission(){
        String operateCodes = WarnConstant.OperateCode.FINISH +","+WarnConstant.OperateCode.PLAN_DELAY_SET;
        getController(UserPowerCheckController.class).checkModulePermission(EamApplication.getCid(), operateCodes, new OnSuccessListener<Map<String, Boolean>>() {
            @Override
            public void onSuccess(Map<String, Boolean> result) {
                //判断权限按钮显示
                planLubricationListAdapter.setPermissionBtn(result);
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
        LogUtil.d("NFC_TAG", nfcEvent.getNfc());
        Map<String, Object> nfcJson = Util.gsonToMaps(nfcEvent.getNfc());
        if (nfcJson.get("textRecord") == null) {
            ToastUtils.show(context, "标签内容空！");
            return;
        }
        doDeal((String) nfcJson.get("textRecord"));
    }

    @SuppressLint("CheckResult")
    private void doDeal(String code) {
        List<DailyLubricateTaskEntity> list = planLubricationListAdapter.getList();
        if (list == null || list.size() <= 0) {
            ToastUtils.show(context, context.getResources().getString(R.string.warn_no_data_operate));
            return;
        }
        int count = 0;
        for (DailyLubricateTaskEntity dailyLubricateTaskEntity : list) {
            if (dailyLubricateTaskEntity.getEamID().code.equals(code)) {
                if (!dailyLubricateTaskEntity.isExpand){
                    doExpand(dailyLubricateTaskEntity,list.indexOf(dailyLubricateTaskEntity));
                }else {
                    count++;
                    break;
                }
            }
        }
        if (count == 0){
            ToastUtils.show(context, context.getResources().getString(R.string.warn_no_match_eam_lubrication));
        }else {
            finishLubrication(null,code);
        }
    }

    private void doExpand(DailyLubricateTaskEntity dailyLubricateTaskEntity, int position) {
        List<DailyLubricateTaskEntity> expandList = dailyLubricateTaskEntity.getExpendList();

        dailyLubricateTaskEntity.isExpand = true;
        planLubricationListAdapter.getList().addAll(position + 1, expandList);
        planLubricationListAdapter.notifyItemRangeInserted(position + 1, expandList.size());
        planLubricationListAdapter.notifyItemRangeChanged(position, expandList.size());
        contentView.scrollToPosition(position + expandList.size());
    }

    @SuppressLint("CheckResult")
    @Override
    public void getLubricationsSuccess(DailyLubricateTaskListEntity entity) {
        lubricationNum.setContent(String.valueOf(entity.totalCount));
        LinkedList<DailyLubricateTaskEntity> dailyLubricateEamEntityList = new LinkedList<>(); // 列表润滑设备展示
        Map<String, DailyLubricateTaskEntity> lubricationPartMap = new HashMap<>(); // 临时存储展示用的设备润滑部位
        Flowable.fromIterable(entity.result)
                .subscribe(lubricationWarnEntity -> {
                    lubricationWarnEntity.viewType = ListType.CONTENT.value();
                    lubricationWarnEntity.isCheck = true;
                    if (TextUtils.isEmpty(lubricationWarnEntity.getEamID().name)){
                        lubricationWarnEntity.eamID.name = "无设备信息";
                    }
                    String key = lubricationWarnEntity.getEamID().name+lubricationWarnEntity.getEamID().code;
                    if (!lubricationPartMap.containsKey(key)) {
                         // tile 展示
                        lubricationWarnTitle = new DailyLubricateTaskEntity();
                        lubricationWarnTitle.viewType = ListType.TITLE.value();
                        lubricationWarnTitle.eamID = lubricationWarnEntity.getEamID();

                        lubricationPartMap.put(key, lubricationWarnTitle);

                        // button 操作
                        DailyLubricateTaskEntity lubricationWarnButton = new DailyLubricateTaskEntity();
                        lubricationWarnButton.viewType = ListType.HEADER.value();
                        lubricationWarnButton.eamID = lubricationWarnEntity.getEamID();
                        lubricationWarnTitle.getExpendList().add(lubricationWarnEntity);
                        lubricationWarnTitle.getExpendList().add(lubricationWarnButton);

                        dailyLubricateEamEntityList.add(lubricationWarnTitle);
                    }else {
                        // 添加润滑部位在最后一项操作按钮前
                        lubricationPartMap.get(key).getExpendList().add(lubricationPartMap.get(key).getExpendList().size()-1,lubricationWarnEntity);
                    }

                }, throwable -> {
                    LogUtil.e(throwable.toString());
                }, () -> {
                    if (dailyLubricateEamEntityList.size() > 0){ // 默认展开第一项
                        dailyLubricateEamEntityList.get(0).isExpand = true;
                        dailyLubricateEamEntityList.addAll(1,dailyLubricateEamEntityList.get(0).getExpendList());
                    }
                        refreshListController.refreshComplete(dailyLubricateEamEntityList);
                });
    }

    @Override
    public void getLubricationsFailed(String errorMsg) {
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
        refreshListController.refreshComplete();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //获取到Tag对象
        if (nfcHelper != null)
            nfcHelper.dealNFCTag(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (nfcHelper != null)
            nfcHelper.onPauseNFC(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (nfcHelper != null) {
            nfcHelper.release();
        }
    }


    @Override
    public void dailyCompleteSuccess(DelayEntity entity) {
        onLoadSuccessAndExit(contentView.getResources().getString(R.string.warn_task_finish), () -> {
            refreshListController.refreshBegin();
        });
    }

    @Override
    public void dailyCompleteFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }
}
