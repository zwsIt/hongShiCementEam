package com.supcon.mes.module_warn.ui;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.ptr.PtrFrameLayout;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.constant.ListType;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.ViewUtil;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.constant.DateBtn;
import com.supcon.mes.middleware.model.api.CommonListAPI;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.ContractStaffEntity;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.contract.CommonListContract;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.EmptyViewHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.FilterHelper;
import com.supcon.mes.module_warn.R;
import com.supcon.mes.module_warn.model.bean.DailyLubricateRecordEntity;
import com.supcon.mes.module_warn.presenter.DailyLubrRecordsFinishPresenter;
import com.supcon.mes.module_warn.ui.adapter.LubricationRecordsFinishListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Flowable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/8
 * Email zhangwenshuai1@supcon.com
 * Desc 已完成日常润滑记录列表
 */
@Router(Constant.Router.LUBRICATION_RECORDS_FINISH_LIST)
@Presenter(value = {DailyLubrRecordsFinishPresenter.class})
public class LubricationRecordsFinishListActivity extends BaseRefreshRecyclerActivity<DailyLubricateRecordEntity> implements CommonListContract.View {
    @BindByTag("leftBtn")
    CustomImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    CustomImageButton rightBtn;
    @BindByTag("contentView")
    RecyclerView contentView;
    @BindByTag("radioGroupFilter")
    RadioGroup radioGroupFilter;

    SimpleDateFormat sdf = new SimpleDateFormat(Constant.TimeString.YEAR_MONTH_DAY_HOUR_MIN_SEC);
    Map<String, Object> queryParams = new HashMap<>();
    EamEntity mEamEntity;

    @Override
    protected void onInit() {
        super.onInit();

        mEamEntity = (EamEntity) getIntent().getSerializableExtra(Constant.IntentKey.EAM);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setAutoPullDownRefresh(true);
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(5,context)));

        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context,""));
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText("已完成日常润滑记录");
    }

    @Override
    protected void initData() {
        super.initData();
        FilterHelper.addView(this,radioGroupFilter,FilterHelper.createMonthDateFilter());
    }

    @Override
    protected IListAdapter<DailyLubricateRecordEntity> createAdapter() {
        return new LubricationRecordsFinishListAdapter(context);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_daily_lubr_records_finish_list;
    }

    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> finish());
        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {
                queryParams.put(Constant.BAPQuery.EAM_CODE, mEamEntity.code);
                queryParams.put(Constant.BAPQuery.EAM_ID,mEamEntity.id);
                presenterRouter.create(CommonListAPI.class).listCommonObj(pageIndex, queryParams, false);
            }
        });
        radioGroupFilter.setOnCheckedChangeListener((group, checkedId) -> {
            Calendar calendar = Calendar.getInstance();
            if (checkedId == DateBtn.ONE_MONTH.getType()){
                calendar.setTime(new Date());
                calendar.add(Calendar.MONTH,-1);
                queryParams.put(Constant.BAPQuery.DEAL_TIME_S,sdf.format(calendar.getTime()));
            }else if (checkedId == DateBtn.THREE_MONTH.getType()){
                calendar.setTime(new Date());
                calendar.add(Calendar.MONTH,-3);
                queryParams.put(Constant.BAPQuery.DEAL_TIME_S,sdf.format(calendar.getTime()));
            }else if (checkedId == DateBtn.SIX_MONTH.getType()){
                calendar.setTime(new Date());
                calendar.add(Calendar.MONTH,-6);
                queryParams.put(Constant.BAPQuery.DEAL_TIME_S,sdf.format(calendar.getTime()));
            }
            queryParams.put(Constant.BAPQuery.DEAL_TIME_E,sdf.format(new Date()));
            refreshListController.refreshBegin();
        });
    }

    List<DailyLubricateRecordEntity> dailyLubricateRecordEntityList = new ArrayList<>(); // 列表润滑部位展示
    Map<String, DailyLubricateRecordEntity> lubricationPartMap = new HashMap<>(); // 临时存储展示用的润滑部位
    @SuppressLint("CheckResult")
    @Override
    public void listCommonObjSuccess(CommonBAPListEntity entity) {
        if (entity.pageNo == 1){
            dailyLubricateRecordEntityList.clear();
            lubricationPartMap.clear();
        }
        if (entity.result.size() <= 0){
            refreshListController.refreshComplete(entity.result);
        }else {
            Flowable.fromIterable(entity.result)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            DailyLubricateRecordEntity dailyLubricateRecordEntity = (DailyLubricateRecordEntity) o;
                            dailyLubricateRecordEntity.setViewType(ListType.CONTENT.value());
                            if (TextUtils.isEmpty(dailyLubricateRecordEntity.getJwxItemId().lubricatePart)){
                                dailyLubricateRecordEntity.getJwxItemId().lubricatePart = "无部位信息";
                            }
                            if (!TextUtils.isEmpty(dailyLubricateRecordEntity.getJwxItemId().lubricatePart) && !lubricationPartMap.containsKey(dailyLubricateRecordEntity.getJwxItemId().lubricatePart)){
                                DailyLubricateRecordEntity dailyLubricateRecordEntityTitle = new DailyLubricateRecordEntity();
                                dailyLubricateRecordEntityTitle.setViewType(ListType.TITLE.value());
                                dailyLubricateRecordEntityTitle.setJwxItemId(dailyLubricateRecordEntity.getJwxItemId());
                                dailyLubricateRecordEntityTitle.getExpendList().add(dailyLubricateRecordEntity);
                                lubricationPartMap.put(dailyLubricateRecordEntity.getJwxItemId().lubricatePart,dailyLubricateRecordEntityTitle);
                                dailyLubricateRecordEntityList.add(dailyLubricateRecordEntityTitle);
                            }else {
                                lubricationPartMap.get(dailyLubricateRecordEntity.getJwxItemId().lubricatePart).getExpendList().add(dailyLubricateRecordEntity);
                            }
                        }
                    }, throwable -> {

                    }, new Action() {
                        @Override
                        public void run() throws Exception {
                            refreshListController.refreshComplete(dailyLubricateRecordEntityList);
                        }
                    });
        }

    }

    @Override
    public void listCommonObjFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
    }
}
