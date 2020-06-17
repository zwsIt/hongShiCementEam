package com.supcon.mes.module_olxj.ui;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.picker.DateTimePicker;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.DatePickController;
import com.supcon.mes.middleware.model.bean.AreaMultiStageEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.ui.view.HsCircleProgress;
import com.supcon.mes.middleware.util.AnimatorUtil;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_olxj.R;
import com.supcon.mes.module_olxj.model.api.OLXJStatisticsAPI;
import com.supcon.mes.module_olxj.model.api.XJMultiDepartSelectAPI;
import com.supcon.mes.module_olxj.model.bean.OLXJStatisticsEntity;
import com.supcon.mes.module_olxj.model.contract.OLXJStatisticsContract;
import com.supcon.mes.module_olxj.model.contract.XJMultiDepartSelectContract;
import com.supcon.mes.module_olxj.presenter.MultiDepartSelectPresenter;
import com.supcon.mes.module_olxj.presenter.OLXJStatisticsPresenter;
import com.supcon.mes.module_olxj.ui.adapter.OLXJStatisticsAdapter;
import com.supcon.mes.module_olxj.ui.view.MultiStagePopwindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/8/14
 * ------------- Description -------------
 * 巡检统计zws
 */
@Router(Constant.Router.XJ_STATISTICS)
@Presenter(value = {OLXJStatisticsPresenter.class, MultiDepartSelectPresenter.class})
public class OLXJStatisticsActivity extends BaseRefreshRecyclerActivity implements OLXJStatisticsContract.View, XJMultiDepartSelectContract.View {

    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("contentView")
    RecyclerView contentView;
    @BindByTag("statisticsCirPro1")
    HsCircleProgress statisticsCirPro1;
    @BindByTag("statisticsCirPro2")
    HsCircleProgress statisticsCirPro2;
    @BindByTag("statisticsCirPro3")
    HsCircleProgress statisticsCirPro3;
    @BindByTag("statisticsCirPro4")
    HsCircleProgress statisticsCirPro4;

    @BindByTag("depotTv")
    TextView depotTv;
    @BindByTag("endTimeTv")
    TextView endTimeTv;
    @BindByTag("startTimeTv")
    TextView startTimeTv;

    private DatePickController datePickController;
    Map<String, Object> queryParam = new HashMap<>();
    private OLXJStatisticsAdapter olxjStatisticsAdapter;
    private MultiStagePopwindow multiStagePopwindow;

    @Override
    protected IListAdapter createAdapter() {
        olxjStatisticsAdapter = new OLXJStatisticsAdapter(this);
        return olxjStatisticsAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_olxj_statistics;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(5, context)));
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, ""));

        datePickController = new DatePickController(this);
        datePickController.setCycleDisable(true);
        datePickController.setCanceledOnTouchOutside(true);
        datePickController.setSecondVisible(false);
        datePickController.textSize(18);

        multiStagePopwindow = new MultiStagePopwindow(this) {
            @Override
            public void onDismiss() {
                super.onDismiss();
//                AnimatorUtil.rotationExpandIcon(depotExpend, 0, 180);
            }
        };

        queryParam.put("startDate", getTimeOfMonthStart());
        queryParam.put("endDate", getTimeOfMonthEnd());
        queryParam.put("deptName", EamApplication.getAccountInfo().departmentName);
        queryParam.put("deptId", EamApplication.getAccountInfo().departmentId);
    }

    @SuppressLint("CutPasteId")
    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);

        titleText.setText(getResources().getString(R.string.inspection_statistics));
        depotTv.setText(EamApplication.getAccountInfo().departmentName);
        startTimeTv.setText(DateUtil.dateFormat(getTimeOfMonthStart(), Constant.TimeString.YEAR_MONTH_DAY));
        endTimeTv.setText(DateUtil.dateFormat(System.currentTimeMillis(), Constant.TimeString.YEAR_MONTH_DAY));
    }

    @Override
    protected void initData() {
        super.initData();
        presenterRouter.create(XJMultiDepartSelectAPI.class).getDepartmentInfoList("");
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> onBackPressed());
        refreshListController.setOnRefreshListener(() -> presenterRouter.create(OLXJStatisticsAPI.class).getInspectStaticsInfo(queryParam));
        startTimeTv.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
//                AnimatorUtil.rotationExpandIcon(expend, 0, 180);
                datePickController.listener((year, month, day, hour, minute, second) -> {
                    startTimeTv.setText(year + "-" + month + "-" + day);
                    queryParam.put("startDate", DateUtil.dateFormat(startTimeTv.getText().toString(), Constant.TimeString.YEAR_MONTH_DAY));
                    refreshListController.refreshBegin();
                }).show(DateUtil.dateFormat(startTimeTv.getText().toString(), Constant.TimeString.YEAR_MONTH_DAY));/*.showMonth(DateUtil.dateFormat(dateTv.getText().toString(), "yyyy-MM"), expend)*/;
            }
        });
        endTimeTv.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
//                AnimatorUtil.rotationExpandIcon(expend, 0, 180);
                datePickController.listener(new DateTimePicker.OnYearMonthDayTimePickListener() {
                    @Override
                    public void onDateTimePicked(String year, String month, String day, String hour, String minute, String second) {
                        endTimeTv.setText(year + "-" + month + "-" + day);
                        queryParam.put("endDate", DateUtil.dateFormat(endTimeTv.getText().toString() + Constant.TimeString.END_TIME, Constant.TimeString.YEAR_MONTH_DAY_HOUR_MIN_SEC));
                        refreshListController.refreshBegin();
                    }
                }).show(DateUtil.dateFormat(endTimeTv.getText().toString(), Constant.TimeString.YEAR_MONTH_DAY));/*.showMonth(DateUtil.dateFormat(dateTv.getText().toString(), "yyyy-MM"), expend)*/;
            }
        });

        depotTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!multiStagePopwindow.isShowing()) {
//                    AnimatorUtil.rotationExpandIcon(depotExpend, 0, 180);
                }
                multiStagePopwindow.showPopupWindow(depotTv);
            }
        });
        multiStagePopwindow.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                multiStagePopwindow.dismiss();
                AreaMultiStageEntity areaMultiStageEntity = (AreaMultiStageEntity) obj;
                depotTv.setText(areaMultiStageEntity.getCurrentEntity().getName());
                queryParam.put("deptName", areaMultiStageEntity.getCurrentEntity().getName());
                queryParam.put("deptId", areaMultiStageEntity.getCurrentEntity().getId());
                if (areaMultiStageEntity.getCurrentEntity().getName().equals(EamApplication.getAccountInfo().companyName)) {
                    queryParam.put("deptName", "");
                    queryParam.put("deptId", "");
                }
                refreshListController.refreshBegin();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(LoginEvent loginEvent) {
        refreshListController.refreshBegin();
    }


    @Override
    public void getInspectStaticsInfoSuccess(CommonListEntity entity) {
        if (entity.result.size() > 0) {
            OLXJStatisticsEntity statisticsEntity = (OLXJStatisticsEntity) entity.result.get(entity.result.size() - 1);
            statisticsCirPro1.setPrefixText(statisticsEntity.xjcsTotal);
            statisticsCirPro2.setPrefixText(statisticsEntity.xjysTotal);
            statisticsCirPro3.setPrefixText(statisticsEntity.qdlTotal);
            statisticsCirPro4.setPrefixText(statisticsEntity.yhQtyTotal);
            entity.result.remove(entity.result.size() - 1);
        }
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void getInspectStaticsInfoFailed(String errorMsg) {
        refreshListController.refreshComplete(null);
        statisticsCirPro1.setPrefixText("");
        statisticsCirPro2.setPrefixText("");
        statisticsCirPro3.setPrefixText("");
        statisticsCirPro4.setPrefixText("");
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
    }

    // 月初0点
    public static long getTimeOfMonthStart() {
        Calendar ca = Calendar.getInstance();
//        ca.set(Calendar.HOUR_OF_DAY, 0);
//        ca.clear(Calendar.MINUTE);
//        ca.clear(Calendar.SECOND);
//        ca.clear(Calendar.MILLISECOND);
        ca.set(Calendar.DAY_OF_MONTH, 1);
        ca.add(Calendar.MONTH,0);
        return ca.getTimeInMillis();
    }
    // 当天24点
    public static long getTimeOfMonthEnd() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    @Override
    public void getDepartmentInfoListSuccess(AreaMultiStageEntity entity) {
        multiStagePopwindow.setData(entity);
    }

    @Override
    public void getDepartmentInfoListFailed(String errorMsg) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
