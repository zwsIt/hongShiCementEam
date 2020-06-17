package com.supcon.mes.module_olxj.ui;

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
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.ptr.PtrFrameLayout;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_olxj.IntentRouter;
import com.supcon.mes.module_olxj.R;
import com.supcon.mes.module_olxj.model.api.OLXJTaskRecordsAPI;
import com.supcon.mes.module_olxj.model.bean.OLXJTaskEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJWorkItemEntity;
import com.supcon.mes.module_olxj.model.contract.OLXJTaskRecordsContract;
import com.supcon.mes.module_olxj.presenter.OLXJTaskRecordsPresenter;
import com.supcon.mes.module_olxj.ui.adapter.OLXJTaskRecordsListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 今日巡检
 * @Author: zhangwenshuai
 * @CreateDate: 2020/6/11 17:28
 */
@Router(Constant.Router.JHXJ_TODAY_RECORDS_LIST)
@Presenter(value = {OLXJTaskRecordsPresenter.class})
public class OLXJTodayTaskRecordsActivity extends BaseRefreshRecyclerActivity<OLXJTaskEntity> implements OLXJTaskRecordsContract.View{

    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    ImageButton rightBtn;
    @BindByTag("contentView")
    RecyclerView contentView;

    OLXJTaskRecordsListAdapter mOLXJTaskRecordsListAdapter;
    private OLXJTaskEntity mOlxjTaskEntity;

    @Override
    protected IListAdapter<OLXJTaskEntity> createAdapter() {
        mOLXJTaskRecordsListAdapter = new OLXJTaskRecordsListAdapter(this);
        return mOLXJTaskRecordsListAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_recycle_no_search;
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
    }

    @Override
    protected void initView() {
        super.initView();
        titleText.setText(context.getResources().getString(R.string.today_xj));
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> onBackPressed());
        refreshListController.setOnRefreshPageListener(pageIndex -> {
            Map<String, Object> queryParam = new HashMap<>();
            queryParam.put(Constant.BAPQuery.STAR_TIME1,getTodayDate()/*"2020-06-06"*/ + Constant.TimeString.START_TIME); // 测试数据改动
            queryParam.put(Constant.BAPQuery.STAR_TIME2,getTodayDate() + Constant.TimeString.END_TIME);
            presenterRouter.create(OLXJTaskRecordsAPI.class).getOJXJTaskList(pageIndex,queryParam);
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
