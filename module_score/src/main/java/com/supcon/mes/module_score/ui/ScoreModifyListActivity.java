package com.supcon.mes.module_score.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.DatePickController;
import com.supcon.mes.middleware.controller.UserPowerCheckController;
import com.supcon.mes.middleware.model.api.CommonListAPI;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.contract.CommonListContract;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.AnimatorUtil;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_score.IntentRouter;
import com.supcon.mes.module_score.R;
import com.supcon.mes.module_score.constant.ScoreConstant;
import com.supcon.mes.module_score.model.bean.ScoreStaffEntity;
import com.supcon.mes.module_score.presenter.ScoreModifyListPresenter;
import com.supcon.mes.module_score.presenter.ScoreStaffRankListPresenter;
import com.supcon.mes.module_score.ui.adapter.RankingAdapter;
import com.supcon.mes.module_score.ui.adapter.ScoreRecordModifyAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * ScoreModifyListActivity
 * created by zhangwenshuai1 2020/9/14
 * 个人评分修改记录
 */
@Router(value = Constant.Router.SCORE_MODIFY_LIST)
@Presenter(value = ScoreModifyListPresenter.class)
public class ScoreModifyListActivity extends BaseRefreshRecyclerActivity implements CommonListContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    ImageButton rightBtn;

    private ScoreRecordModifyAdapter mScoreRecordModifyAdapter;

    private final Map<String, Object> queryParam = new HashMap<>();
    private Long mScoreData; // 评分日期
    private Long mScoreStaffId; // 评分人员

    @Override
    protected IListAdapter createAdapter() {
        mScoreRecordModifyAdapter = new ScoreRecordModifyAdapter(this);
        return mScoreRecordModifyAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_recycle_no_search;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);

        mScoreData = getIntent().getLongExtra(Constant.BAPQuery.DATE,0);
        mScoreStaffId = getIntent().getLongExtra(Constant.BAPQuery.ID,0);

        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));
        contentView.setLayoutManager(new LinearLayoutManager(context));
//        contentView.addItemDecoration(new SpaceItemDecoration(15));
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);

        titleText.setText(context.getResources().getString(R.string.score_modify_records));

        queryParam.put(Constant.BAPQuery.SCORE_DATA_START, DateUtil.dateFormat(mScoreData, "yyyy-MM-dd 00:00:00"));
        queryParam.put(Constant.BAPQuery.SCORE_DATA_STOP, DateUtil.dateFormat(mScoreData, "yyyy-MM-dd 23:59:59"));
        queryParam.put(Constant.BAPQuery.ID,mScoreStaffId);


    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        RxView.clicks(leftBtn)
                .subscribe(o -> onBackPressed());

        refreshListController.setOnRefreshPageListener(pageIndex -> {
            presenterRouter.create(CommonListAPI.class).listCommonObj(pageIndex,queryParam,false);
        });
    }

    public long getYesterday() {
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        ca.add(Calendar.DAY_OF_MONTH, -1);
        return ca.getTimeInMillis();
    }
    public long getToday() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY,0);
        ca.set(Calendar.MINUTE,0);
        ca.set(Calendar.SECOND,0);
        ca.set(Calendar.MILLISECOND,0);
//        DateUtil.dateFormat(ca.getTimeInMillis(),Constant.TimeString.YEAR_MONTH_DAY + Constant.TimeString.START_TIME);
//        DateUtil.dateFormat(,Constant.TimeString.YEAR_MONTH_DAY_HOUR_MIN_SEC);
        return ca.getTimeInMillis();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshEvent refreshEvent){
        refreshListController.refreshBegin();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void listCommonObjSuccess(CommonBAPListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void listCommonObjFailed(String errorMsg) {
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
        refreshListController.refreshComplete();
    }
}
