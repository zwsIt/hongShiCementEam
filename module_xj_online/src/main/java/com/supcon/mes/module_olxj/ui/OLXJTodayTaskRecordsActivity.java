package com.supcon.mes.module_olxj.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.ptr.PtrFrameLayout;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.picker.DateTimePicker;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.DatePickController;
import com.supcon.mes.middleware.model.bean.AreaMultiStageEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_olxj.IntentRouter;
import com.supcon.mes.module_olxj.R;
import com.supcon.mes.module_olxj.model.api.OLXJTaskRecordsAPI;
import com.supcon.mes.module_olxj.model.api.XJMultiDepartSelectAPI;
import com.supcon.mes.module_olxj.model.bean.OLXJTaskEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJWorkItemEntity;
import com.supcon.mes.module_olxj.model.contract.OLXJTaskRecordsContract;
import com.supcon.mes.module_olxj.model.contract.XJMultiDepartSelectContract;
import com.supcon.mes.module_olxj.presenter.MultiDepartSelectPresenter;
import com.supcon.mes.module_olxj.presenter.OLXJTaskRecordsPresenter;
import com.supcon.mes.module_olxj.ui.adapter.OLXJTaskRecordsListAdapter;
import com.supcon.mes.module_olxj.ui.view.MultiStagePopwindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @Description: 今日巡检
 * @Author: zhangwenshuai
 * @CreateDate: 2020/6/11 17:28
 */
@Router(Constant.Router.JHXJ_TODAY_RECORDS_LIST)
@Presenter(value = {OLXJTaskRecordsPresenter.class, MultiDepartSelectPresenter.class})
public class OLXJTodayTaskRecordsActivity extends BaseRefreshRecyclerActivity<OLXJTaskEntity> implements OLXJTaskRecordsContract.View, XJMultiDepartSelectContract.View{

    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    ImageButton rightBtn;
    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("depotTv")
    TextView depotTv;
    @BindByTag("endTimeTv")
    TextView endTimeTv;
    @BindByTag("startTimeTv")
    TextView startTimeTv;

    OLXJTaskRecordsListAdapter mOLXJTaskRecordsListAdapter;
    private OLXJTaskEntity mOlxjTaskEntity;
    private DatePickController datePickController;
    Map<String, Object> queryParam = new HashMap<>();
    private MultiStagePopwindow multiStagePopwindow;

    @Override
    protected IListAdapter<OLXJTaskEntity> createAdapter() {
        mOLXJTaskRecordsListAdapter = new OLXJTaskRecordsListAdapter(this);
        return mOLXJTaskRecordsListAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_xj_task_situation_recycle;
    }

    @Override
    protected void onInit() {
        super.onInit();
        StatusBarUtils.setWindowStatusBarColor(this,R.color.themeColor);
        EventBus.getDefault().register(this);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        contentView.setLayoutManager(new LinearLayoutManager(this));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(5,context)));
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context,null));

        datePickController = new DatePickController(this);
        datePickController.setCycleDisable(true);
        datePickController.setCanceledOnTouchOutside(true);
        datePickController.setSecondVisible(false);
        datePickController.textSize(18);

//        queryParam.put(Constant.BAPQuery.ID,EamApplication.getAccountInfo().departmentId); // 默认当前登录人部门id
        queryParam.put(Constant.BAPQuery.STAR_TIME1,getTodayDate() + Constant.TimeString.START_TIME);
        queryParam.put(Constant.BAPQuery.STAR_TIME2,getTodayDate() + Constant.TimeString.END_TIME);

        multiStagePopwindow = new MultiStagePopwindow(this) {
            @Override
            public void onDismiss() {
                super.onDismiss();
//                AnimatorUtil.rotationExpandIcon(depotExpend, 0, 180);
            }
        };

    }

    @Override
    protected void initView() {
        super.initView();
        titleText.setText(context.getResources().getString(R.string.xj_situation));
        searchTitleBar.disableRightBtn();
        searchTitleBar.editText().setHint(context.getResources().getString(R.string.xj_please_input_charge_staff));

//        searchTitleBar.editText().setText(EamApplication.getAccountInfo().staffName);
        startTimeTv.setText(getTodayDate());
        endTimeTv.setText(getTodayDate());
        depotTv.setText(EamApplication.getAccountInfo().departmentName);

//        depotTv.setVisibility(View.GONE);
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
        leftBtn.setOnClickListener(v -> onBackPressed());

        refreshListController.setOnRefreshPageListener(pageIndex -> {
            queryParam.put(Constant.BAPQuery.NAME,searchTitleBar.editText().getText().toString().trim());
            presenterRouter.create(OLXJTaskRecordsAPI.class).getOJXJTaskList(pageIndex,queryParam);
        });
        searchTitleBar.setOnExpandListener(isExpand -> {
            if (isExpand) {
//                    searchTitleBar.searchView().setInputTextColor(R.color.black);
            } else {
                searchTitleBar.searchView().setInputTextColor(R.color.black);
            }
        });
        RxTextView.textChanges(searchTitleBar.editText()).skipInitialValue()
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence -> {
                    queryParam.put(Constant.BAPQuery.NAME,charSequence.toString().trim());
                    refreshListController.refreshBegin();
                });
        startTimeTv.setOnClickListener(v -> {
//                AnimatorUtil.rotationExpandIcon(expend, 0, 180);
            datePickController.listener((year, month, day, hour, minute, second) -> {
                startTimeTv.setText(year + "-" + month + "-" + day);
                queryParam.put(Constant.BAPQuery.STAR_TIME1, startTimeTv.getText().toString() + Constant.TimeString.START_TIME);
                refreshListController.refreshBegin();
            }).show(DateUtil.dateFormat(startTimeTv.getText().toString(), Constant.TimeString.YEAR_MONTH_DAY));/*.showMonth(DateUtil.dateFormat(dateTv.getText().toString(), "yyyy-MM"), expend)*/;
        });
        endTimeTv.setOnClickListener(v -> {
//                AnimatorUtil.rotationExpandIcon(expend, 0, 180);
            datePickController.listener(new DateTimePicker.OnYearMonthDayTimePickListener() {
                @Override
                public void onDateTimePicked(String year, String month, String day, String hour, String minute, String second) {
                    endTimeTv.setText(year + "-" + month + "-" + day);
                    queryParam.put(Constant.BAPQuery.STAR_TIME2, endTimeTv.getText().toString() + Constant.TimeString.END_TIME);
                    refreshListController.refreshBegin();
                }
            }).show(DateUtil.dateFormat(endTimeTv.getText().toString(), Constant.TimeString.YEAR_MONTH_DAY));/*.showMonth(DateUtil.dateFormat(dateTv.getText().toString(), "yyyy-MM"), expend)*/;
        });
        depotTv.setOnClickListener(view -> {
            if (!multiStagePopwindow.isShowing()) {
//                    AnimatorUtil.rotationExpandIcon(depotExpend, 0, 180);
            }
            multiStagePopwindow.showPopupWindow(depotTv);
        });
        multiStagePopwindow.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            multiStagePopwindow.dismiss();
            AreaMultiStageEntity areaMultiStageEntity = (AreaMultiStageEntity) obj;
            depotTv.setText(areaMultiStageEntity.getCurrentEntity().getName());
//            queryParam.put(Constant.BAPQuery.NAME, areaMultiStageEntity.getCurrentEntity().getName());
            queryParam.put(Constant.BAPQuery.ID, areaMultiStageEntity.getCurrentEntity().getId());
            if (areaMultiStageEntity.getCurrentEntity().getName().equals(EamApplication.getAccountInfo().companyName)) {
//                queryParam.put("deptName", "");
                queryParam.remove(Constant.BAPQuery.ID);
            }
            refreshListController.refreshBegin();
        });

        mOLXJTaskRecordsListAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            String tag = childView.getTag().toString();
            switch (tag){
                case "taskExpandBtn":
                    mOlxjTaskEntity = (OLXJTaskEntity) obj;
                    onLoading(context.getResources().getString(R.string.onLoading));
                    presenterRouter.create(OLXJTaskRecordsAPI.class).getOJXJAreaList(mOlxjTaskEntity.id);
                    break;
                case "taskAreaListView":
                    OLXJWorkItemEntity olxjWorkItemEntity = (OLXJWorkItemEntity) obj;
                    Bundle bundle = new Bundle();
                    bundle.putLong(Constant.IntentKey.TASKID,mOlxjTaskEntity.id);
                    bundle.putLong(Constant.IntentKey.WORK_ID,olxjWorkItemEntity.workID.id);
                    IntentRouter.go(context,Constant.Router.JHXJ_TODAY_ITEM_LIST,bundle);
                    break;
                    default:
            }
        });

    }

    @Override
    public void getOJXJTaskListSuccess(CommonBAPListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void getOJXJTaskListFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void getOJXJAreaListSuccess(CommonBAPListEntity entity) {
        mOLXJTaskRecordsListAdapter.setAreaEntities(entity.result);
        onLoadSuccess();
        if (entity.result.size() == 0){
            ToastUtils.show(context,context.getResources().getString(R.string.no_data));
        }
    }

    @Override
    public void getOJXJAreaListFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    private String getTodayDate(){
        Calendar calendar = Calendar.getInstance();
        return DateUtil.dateFormat(calendar.getTimeInMillis(),Constant.TimeString.YEAR_MONTH_DAY);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void rfresh(RefreshEvent event){
        refreshListController.refreshBegin();
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
