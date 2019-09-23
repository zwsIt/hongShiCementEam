package com.supcon.mes.module_sbda_online.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomFilterView;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.DatePickController;
import com.supcon.mes.middleware.model.bean.CommonSearchEntity;
import com.supcon.mes.middleware.model.bean.EamType;
import com.supcon.mes.middleware.model.bean.ResultEntity;
import com.supcon.mes.middleware.model.bean.ScreenEntity;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.AnimatorUtil;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_sbda_online.IntentRouter;
import com.supcon.mes.module_sbda_online.R;
import com.supcon.mes.module_sbda_online.model.api.StopPoliceAPI;
import com.supcon.mes.module_sbda_online.model.bean.StopPoliceEntity;
import com.supcon.mes.module_sbda_online.model.bean.StopPoliceListEntity;
import com.supcon.mes.module_sbda_online.model.contract.StopPoliceContract;
import com.supcon.mes.module_sbda_online.presenter.StopPolicePresenter;
import com.supcon.mes.module_sbda_online.screen.FilterHelper;
import com.supcon.mes.module_sbda_online.ui.adapter.StopPoliceAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.supcon.mes.middleware.constant.Constant.BAPQuery.STOP_POLICE_EAM_IDS;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.STOP_POLICE_ID;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.STOP_POLICE_STAFF_ID;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.STOP_POLICE_STOP_EXPLAIN;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.STOP_POLICE_STOP_REASON;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.STOP_POLICE_STOP_TYPE;

@Router(Constant.Router.STOP_POLICE)
@Presenter(value = StopPolicePresenter.class)
public class StopPoliceListActivity extends BaseRefreshRecyclerActivity<StopPoliceEntity> implements StopPoliceContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("listEamNameFilter")
    CustomFilterView listEamNameFilter;


    private View timeStart, timeEnd;
    private ImageView startExpend, endExpend;
    private TextView startDate, endDate;

    private Map<String, Object> queryParam = new HashMap<>();
    /**
     * 单一停机报警列表项刷新请求列表
     */
    private Map<String, String> singleUpdateQueryParam = new HashMap<>();
    private DatePickController datePickController;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Calendar calendar;
    private StopPoliceAdapter stopPoliceAdapter;

    @Override
    protected IListAdapter createAdapter() {
        stopPoliceAdapter = new StopPoliceAdapter(this);
        return stopPoliceAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_stop_police_list;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));
        titleText.setText("停机报警");
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(5));

        listEamNameFilter.setData(FilterHelper.createEamNameFilter());

        timeStart = findViewById(R.id.stopPoliceStartTime);
        timeEnd = findViewById(R.id.stopPoliceStopTime);
        startExpend = timeStart.findViewById(R.id.expend);
        startDate = timeStart.findViewById(R.id.dateTv);
        endExpend = timeEnd.findViewById(R.id.expend);
        endDate = timeEnd.findViewById(R.id.dateTv);

        datePickController = new DatePickController(this);
        datePickController.setCycleDisable(true);
        datePickController.setCanceledOnTouchOutside(true);
        datePickController.setSecondVisible(false);
        datePickController.textSize(18);

        calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        startDate.setText(DateUtil.dateFormat(getTimeOfDayStart(), "yyyy-MM-dd"));
        endDate.setText(DateUtil.dateFormat(getTimeOfDayEnd(), "yyyy-MM-dd"));
        queryParam.put(Constant.BAPQuery.OPEN_TIME_START, dateFormat.format(getTimeOfDayStart()));
        queryParam.put(Constant.BAPQuery.OPEN_TIME_STOP, dateFormat.format(getTimeOfDayEnd()));

        singlePickerController.textSize(18);
        singlePickerController.setCanceledOnTouchOutside(true);
    }

    private CustomDialog mCustomDialog;
    private CustomTextView itemStopPoliceStopType;
    private CustomTextView itemStopPoliceStopReason;
    private CustomEditText itemStopPoliceStopExplain;
    private CustomTextView itemStopPoliceEamIds;
    private SinglePickController singlePickerController = new SinglePickController<String>(this);
    private static final Map<String, String> TJ_TYPE = new HashMap<>();
    private static final Map<String, Map<String, String>> TJ_REASON = new HashMap<>();

    static {
        String[] STOP_POLICE_TYPES = new String[4];
        STOP_POLICE_TYPES[0] = "BEAM2009/";
        for (int i = 1; i < STOP_POLICE_TYPES.length; i++) {
            STOP_POLICE_TYPES[i] = STOP_POLICE_TYPES[0] + "0" + i;
        }
        TJ_TYPE.put("计划停机", STOP_POLICE_TYPES[1]);
        TJ_TYPE.put("故障停机", STOP_POLICE_TYPES[2]);
        TJ_TYPE.put("不可控因素停机", STOP_POLICE_TYPES[3]);
        String[] STOP_POLICE_REASONS = new String[9];
        STOP_POLICE_REASONS[0] = "BEAM2_2012/";
        for (int i = 1; i < STOP_POLICE_REASONS.length; i++) {
            STOP_POLICE_REASONS[i] = STOP_POLICE_REASONS[0] + "0" + i;
        }
        Map<String, String> m1 = new HashMap<>();
        m1.put("计划性停机", STOP_POLICE_REASONS[1]);
        m1.put("避峰电停机", STOP_POLICE_REASONS[2]);
        m1.put("限产性停机", STOP_POLICE_REASONS[3]);

        Map<String, String> m2 = new HashMap<>();
        m2.put("工艺设备故障", STOP_POLICE_REASONS[4]);
        m2.put("工艺物料堵料停机", STOP_POLICE_REASONS[5]);
        m2.put("电器设备故障停机", STOP_POLICE_REASONS[6]);
        m2.put("设备故障", STOP_POLICE_REASONS[7]);

        Map<String, String> m3 = new HashMap<>();
        m3.put("不可控停机", STOP_POLICE_REASONS[8]);
        TJ_REASON.put("计划停机", m1);
        TJ_REASON.put("故障停机", m2);
        TJ_REASON.put("不可控因素停机", m3);
    }

    private Map<String, String> paramMap = new HashMap<>();

    private void showPopUp(StopPoliceEntity stopPoliceEntity) {
        paramMap.put(STOP_POLICE_STAFF_ID, "" + EamApplication.getAccountInfo().staffId);
        paramMap.put(STOP_POLICE_ID, stopPoliceEntity.recordId.id);

        if (mCustomDialog == null) {
            mCustomDialog = new CustomDialog(context);
            View view = LayoutInflater.from(context).inflate(R.layout.ly_stop_police_popup, null, false);
            itemStopPoliceStopType = view.findViewById(R.id.itemStopPoliceStopType);
            itemStopPoliceStopReason = view.findViewById(R.id.itemStopPoliceStopReason);
            itemStopPoliceStopExplain = view.findViewById(R.id.itemStopPoliceStopExplain);
            itemStopPoliceEamIds = view.findViewById(R.id.itemStopPoliceEamIds);
            mCustomDialog.layout(view);
            mCustomDialog
                    .bindChildListener(R.id.itemStopPoliceStopType
                            , (childView, action, obj) -> {
                                if (action == -1) {
                                    paramMap.remove(STOP_POLICE_STOP_TYPE);
                                    paramMap.remove(STOP_POLICE_STOP_REASON);
                                    itemStopPoliceStopType.setContent(null);
                                    return;
                                }
                                singlePickerController.list(Arrays.asList(TJ_TYPE.keySet().toArray()))
                                        .listener((index, item) -> {
                                            paramMap.put(STOP_POLICE_STOP_TYPE, TJ_TYPE.get(item));
                                            itemStopPoliceStopType.setContent((String) item);
                                            itemStopPoliceStopReason.setContent(null);
                                        }).show();
                            })
                    .bindChildListener(R.id.itemStopPoliceStopReason
                            , (childView, action, obj) -> {
                                if (action == -1) {
                                    paramMap.remove(STOP_POLICE_STOP_REASON);
                                    itemStopPoliceStopReason.setContent(null);
                                    return;
                                }
                                if (TextUtils.isEmpty(itemStopPoliceStopType.getContent())) {
                                    ToastUtils.show(context, "请先选择停机类别。");
                                    return;
                                }
                                singlePickerController.list(Arrays.asList(TJ_REASON.get(itemStopPoliceStopType.getContent()).keySet().toArray()))
                                        .listener((index, item) -> {
                                            paramMap.put(STOP_POLICE_STOP_REASON, TJ_REASON.get(itemStopPoliceStopType.getContent()).get(item));
                                            itemStopPoliceStopReason.setContent((String) item);
                                        }).show();
                            })
                    .bindChildListener(R.id.itemStopPoliceStopExplain, (childView, action, obj) -> {
                        if (action == -1) {
                            paramMap.remove(STOP_POLICE_STOP_EXPLAIN);
                            itemStopPoliceStopExplain.setContent(null);
                            return;
                        }
                    })
                    .bindChildListener(R.id.itemStopPoliceEamIds, (childView, action, obj) -> {
                        if (action == -1) {
                            paramMap.remove(STOP_POLICE_EAM_IDS);
                            return;
                        }
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(Constant.IntentKey.IS_MULTI, true);
                        IntentRouter.go(context, Constant.Router.EAM, bundle);
                    })
                    .bindClickListener(R.id.btn_stop_police_save, v -> {
                        LogUtil.e("ciruy", paramMap.toString());
                        if (paramMap.containsKey(STOP_POLICE_STAFF_ID) && paramMap.containsKey(STOP_POLICE_ID) && paramMap.containsKey(STOP_POLICE_STOP_TYPE) && paramMap.containsKey(STOP_POLICE_STOP_EXPLAIN)) {
                            onLoading("数据上传中...");
                            presenterRouter.create(StopPoliceAPI.class).updateStopPoliceItem(paramMap);
                            mCustomDialog.dismiss();
                        } else {
                            ToastUtils.show(context, "请确保停机类别和停机说明已填！");
                        }
                    }, false)
                    .bindClickListener(R.id.btn_stop_police_cancel, v -> paramMap.clear(), true);
            mCustomDialog.getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            RxTextView.textChanges(itemStopPoliceStopExplain.editText())
                    .skipInitialValue()
                    .observeOn(Schedulers.io())
                    .debounce(200, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(charSequence -> {
                        if (TextUtils.isEmpty(charSequence))
                            paramMap.remove(STOP_POLICE_STOP_EXPLAIN);
                        else
                            paramMap.put(STOP_POLICE_STOP_EXPLAIN, charSequence.toString());
                    });

        }
        if (stopPoliceEntity.recordId != null && stopPoliceEntity.recordId.closedType != null) {
            paramMap.put(STOP_POLICE_STOP_TYPE, stopPoliceEntity.recordId.closedType.id);
            itemStopPoliceStopType.setContent(stopPoliceEntity.recordId.closedType.value);
        } else {
            itemStopPoliceStopType.setContent(null);
        }
        if (stopPoliceEntity.recordId != null && stopPoliceEntity.recordId.closedReason != null) {
            paramMap.put(STOP_POLICE_STOP_REASON, stopPoliceEntity.recordId.closedReason.id);
            itemStopPoliceStopReason.setContent(stopPoliceEntity.recordId.closedReason.value);
        } else {
            itemStopPoliceStopReason.setContent(null);
        }
        if (stopPoliceEntity.recordId != null) {
            paramMap.put(STOP_POLICE_STOP_EXPLAIN, stopPoliceEntity.recordId.reason);
            itemStopPoliceStopExplain.setContent(stopPoliceEntity.recordId.reason);
        } else {
            itemStopPoliceStopExplain.setContent(null);
        }
        if (stopPoliceEntity.auxiliary != null) {
            StringBuilder stringBuilderName = new StringBuilder();
            StringBuilder stringBuilderId = new StringBuilder();
            List<StopPoliceEntity.EamInfo> eamInfos = GsonUtil.jsonToList(stopPoliceEntity.auxiliary, StopPoliceEntity.EamInfo.class);

            for (int i = 0; i < eamInfos.size(); i++) {
                StopPoliceEntity.EamInfo eamType = eamInfos.get(i);
                if (i != 0) {
                    stringBuilderName.append(",");
                    stringBuilderId.append(",");
                }
                stringBuilderName.append(eamType.eamName);
                stringBuilderId.append(eamType.eamId);
            }
            paramMap.put(STOP_POLICE_EAM_IDS, stringBuilderId.toString());
            itemStopPoliceEamIds.setContent(stringBuilderName.toString());
        } else {
            itemStopPoliceEamIds.setContent(null);
        }
        mCustomDialog.show();
    }

    @Subscribe
    public void receiveMultiDevice(CommonSearchEvent commonSearchEvent) {
        if (commonSearchEvent.IS_MULTI) {
            List<CommonSearchEntity> commonSearchEntities = commonSearchEvent.mCommonSearchEntityList;
            StringBuilder stringBuilderId = new StringBuilder();
            StringBuilder stringBuilderName = new StringBuilder();
            for (int i = 0; i < commonSearchEntities.size(); i++) {
                EamType eamType = (EamType) commonSearchEntities.get(i);
                if (i != 0) {
                    stringBuilderName.append(",");
                    stringBuilderId.append(",");
                }
                stringBuilderName.append(eamType.getSearchName());
                stringBuilderId.append(eamType.id);
            }
            paramMap.put(STOP_POLICE_EAM_IDS, stringBuilderId.toString());
            itemStopPoliceEamIds.setContent(stringBuilderName.toString());
        }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        stopPoliceAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            StopPoliceEntity stopPoliceEntity = (StopPoliceEntity) obj;
            if (stopPoliceEntity.onOrOff == null || stopPoliceEntity.onOrOff.value.equals("开")) {
                ToastUtils.show(context, "该单据的开机状态为“开”");
                return;
            }
            showPopUp(stopPoliceEntity);
        });

        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> onBackPressed());

        refreshListController.setOnRefreshPageListener((page) -> {
            if (!queryParam.containsKey(Constant.BAPQuery.ON_OR_OFF)) {
                queryParam.put(Constant.BAPQuery.ON_OR_OFF, "BEAM020/02");
            }
            LogUtil.e("ciruy", queryParam.toString());
            presenterRouter.create(StopPoliceAPI.class).runningGatherList(queryParam, page);
        });
        listEamNameFilter.setFilterSelectChangedListener(filterBean -> {
            queryParam.put(Constant.BAPQuery.EAM_NAME, !((ScreenEntity) filterBean).name.equals("设备不限") ? ((ScreenEntity) filterBean).name : "");
            doRefresh();
        });
        timeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimatorUtil.rotationExpandIcon(startExpend, 0, 180);
                datePickController.listener((year, month, day, hour, minute, second) -> {
                    if (compareTime(year + "-" + month + "-" + day, endDate.getText().toString())) {
                        startDate.setText(year + "-" + month + "-" + day);
                        if (!queryParam.containsKey(Constant.BAPQuery.OPEN_TIME_START)) {
                            queryParam.remove(Constant.BAPQuery.OPEN_TIME_START);
                        }
                        queryParam.put(Constant.BAPQuery.OPEN_TIME_START, year + "-" + month + "-" + day + " " + "00" + ":" + "00" + ":" + "00");
                        queryParam.put(Constant.BAPQuery.EAM_NAME, "");
                        listEamNameFilter.setCurrentItem("设备不限");
                        doRefresh();
                    }

                }).show(DateUtil.dateFormat(startDate.getText().toString()), startExpend);
            }
        });
        timeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimatorUtil.rotationExpandIcon(endExpend, 0, 180);
                datePickController.listener((year, month, day, hour, minute, second) -> {
                    if (compareTime(startDate.getText().toString(), year + "-" + month + "-" + day)) {
                        endDate.setText(year + "-" + month + "-" + day);
                        if (!queryParam.containsKey(Constant.BAPQuery.OPEN_TIME_STOP)) {
                            queryParam.remove(Constant.BAPQuery.OPEN_TIME_STOP);
                        }
                        queryParam.put(Constant.BAPQuery.OPEN_TIME_STOP, year + "-" + month + "-" + day + " " + "23" + ":" + "59" + ":" + "59");
                        listEamNameFilter.setCurrentItem("设备不限");
                        queryParam.put(Constant.BAPQuery.EAM_NAME, "");
                        doRefresh();
                    }
                }).show(DateUtil.dateFormat(endDate.getText().toString()), endExpend);
            }
        });


    }

    private void doRefresh() {
        refreshListController.refreshBegin();
    }

    @SuppressLint("CheckResult")
    @Override
    public void runningGatherListSuccess(StopPoliceListEntity entity) {
//        if (entity.pageNo != 1) {
//            refreshListController.refreshComplete(entity.result);
//            return;
//        }
        refreshListController.refreshComplete(entity.result);
//        Flowable.just(entity)
//                .map(stopPoliceListEntity -> {
//                    List<ScreenEntity> list = FilterHelper.createEamNameFilter();
//                    for (StopPoliceEntity stopPoliceEntity : stopPoliceListEntity.result) {
//                        ScreenEntity screenEntity = new ScreenEntity();
//                        screenEntity.name = stopPoliceEntity.getEamType().name;
//                        if (!list.contains(screenEntity))
//                            list.add(screenEntity);
//                    }
//                    return Pair.create(stopPoliceListEntity, list);
//                })
////                .observeOn(AndroidSchedulers.mainThread())
//                .compose(RxSchedulers.io_main())
//                .map(stopPoliceListEntityListPair -> {
//                    listEamNameFilter.setData(stopPoliceListEntityListPair.second);
//                    return stopPoliceListEntityListPair.first;
//                })
//                .subscribe(stopPoliceListEntity -> refreshListController.refreshComplete(entity.result));
    }

    @Override
    public void runningGatherListFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, ErrorMsgHelper.msgParse(errorMsg));
        refreshListController.refreshComplete(null);
    }

    @Override
    public void updateStopPoliceItemSuccess(ResultEntity entity) {
        onLoadSuccess("数据修改成功！");
        mCustomDialog.dismiss();
        EventBus.getDefault().post(new RefreshEvent());
    }

    @Override
    public void updateStopPoliceItemFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(LoginEvent loginEvent) {

        refreshListController.refreshBegin();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(RefreshEvent event) {

        refreshListController.refreshBegin();
    }

    private boolean compareTime(String start, String stop) {
        if (TextUtils.isEmpty(start) || TextUtils.isEmpty(stop)) {
            return false;
        }
        long startTime = DateUtil.dateFormat(start, "yyyy-MM-dd");
        long stopTime = DateUtil.dateFormat(stop, "yyyy-MM-dd");
        if (stopTime >= startTime) {
            return true;
        }
        ToastUtils.show(this, "开始时间不能大于结束时间!");
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public static long getTimeOfMonthStart() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.clear(Calendar.MINUTE);
        ca.clear(Calendar.SECOND);
        ca.clear(Calendar.MILLISECOND);
        ca.set(Calendar.DAY_OF_MONTH, 1);
        return ca.getTimeInMillis();
    }

    public static long getTimeOfDayStart() {
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DAY_OF_MONTH, -1);
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.clear(Calendar.MINUTE);
        ca.clear(Calendar.SECOND);
        ca.clear(Calendar.MILLISECOND);
        return ca.getTimeInMillis();
    }

    public static long getTimeOfDayEnd() {
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DAY_OF_MONTH, -1);
        ca.set(Calendar.HOUR_OF_DAY, 23);
        ca.set(Calendar.MINUTE, 59);
        ca.set(Calendar.SECOND, 59);
        return ca.getTimeInMillis();
    }
}
