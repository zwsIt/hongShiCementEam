package com.supcon.mes.module_olxj.ui;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.github.lzyzsd.circleprogress.CircleProgress;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.view.picker.DateTimePicker;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.DatePickController;
import com.supcon.mes.middleware.model.bean.AreaMultiStageEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.Department;
import com.supcon.mes.middleware.model.bean.DepartmentInfo;
import com.supcon.mes.middleware.model.contract.MultiDepartSelectContract;
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
 * 巡检统计
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

    @BindByTag("depotLayout")
    LinearLayout depotLayout;
    @BindByTag("depotTv")
    TextView depotTv;
    @BindByTag("depotExpend")
    ImageView depotExpend;

    @BindByTag("dateLayout")
    LinearLayout dateLayout;
    @BindByTag("expend")
    ImageView expend;
    @BindByTag("dateTv")
    TextView dateTv;

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
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
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
                AnimatorUtil.rotationExpandIcon(depotExpend, 180, 0);
            }
        };
    }

    @Override
    protected void initData() {
        super.initData();
        titleText.setText("巡检统计");
        queryParam.put("rangeTime", DateUtil.dateFormat(System.currentTimeMillis(), "yyyy-MM"));
        dateTv.setText(DateUtil.dateFormat(getTimeOfMonthStart(), "yyyy-MM"));

        presenterRouter.create(XJMultiDepartSelectAPI.class).getDepartmentInfoList("");
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> onBackPressed());
        refreshListController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenterRouter.create(OLXJStatisticsAPI.class).getInspectStaticsInfo(queryParam);
            }
        });
        dateLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                AnimatorUtil.rotationExpandIcon(expend, 0, 180);
                datePickController.listener(new DateTimePicker.OnYearMonthTimePickListener() {
                    @Override
                    public void onDateTimePicked(String year, String month, String hour, String minute, String second) {
                        dateTv.setText(year + "-" + month);
                        queryParam.put("rangeTime", year + "-" + month);
                        refreshListController.refreshBegin();
                    }
                }).showMonth(DateUtil.dateFormat(dateTv.getText().toString(), "yyyy-MM"), expend);
            }
        });

        depotLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!multiStagePopwindow.isShowing()) {
                    AnimatorUtil.rotationExpandIcon(depotExpend, 0, 180);
                }
                multiStagePopwindow.showPopupWindow(depotLayout);
            }
        });
        multiStagePopwindow.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                multiStagePopwindow.dismiss();
                AreaMultiStageEntity areaMultiStageEntity = (AreaMultiStageEntity) obj;
                depotTv.setText(areaMultiStageEntity.getCurrentEntity().getName());
                queryParam.put("deptName", areaMultiStageEntity.getCurrentEntity().getName());
                if (areaMultiStageEntity.getCurrentEntity().getName().equals("集团")) {
                    queryParam.put("deptName", "");
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
        SnackbarHelper.showError(rootView, ErrorMsgHelper.msgParse(errorMsg));
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
