package com.supcon.mes.module_olxj.ui;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
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
import com.supcon.mes.module_olxj.R;
import com.supcon.mes.module_olxj.model.api.OLXJTaskRecordsAPI;
import com.supcon.mes.module_olxj.model.api.OLXJTodayTaskItemListAPI;
import com.supcon.mes.module_olxj.model.bean.OLXJTaskEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJWorkItemEntity;
import com.supcon.mes.module_olxj.model.contract.OLXJTaskRecordsContract;
import com.supcon.mes.module_olxj.model.contract.OLXJTodayTaskItemListContract;
import com.supcon.mes.module_olxj.presenter.OLXJTaskItemListPresenter;
import com.supcon.mes.module_olxj.presenter.OLXJTaskRecordsPresenter;
import com.supcon.mes.module_olxj.ui.adapter.OLXJTaskItemAdapter;
import com.supcon.mes.module_olxj.ui.adapter.OLXJTaskRecordsListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Flowable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @Description: 今日巡检任务明细list
 * @Author: zhangwenshuai
 * @CreateDate: 2020/6/11 17:28
 */
@Router(Constant.Router.JHXJ_TODAY_ITEM_LIST)
@Presenter(value = {OLXJTaskItemListPresenter.class})
public class OLXJTodayTaskItemsListActivity extends BaseRefreshRecyclerActivity<OLXJWorkItemEntity> implements OLXJTodayTaskItemListContract.View{

    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    ImageButton rightBtn;
    @BindByTag("contentView")
    RecyclerView contentView;

    OLXJTaskItemAdapter mOLXJTaskItemAdapter;

    @Override
    protected IListAdapter<OLXJWorkItemEntity> createAdapter() {
        mOLXJTaskItemAdapter = new OLXJTaskItemAdapter(this);
        return mOLXJTaskItemAdapter;
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
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(3,context)));
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context,null));

    }

    @Override
    protected void initView() {
        super.initView();
        titleText.setText("巡检项记录");
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
            presenterRouter.create(OLXJTodayTaskItemListAPI.class).getWorkItemList(getIntent().getLongExtra(Constant.IntentKey.TASKID,-1),getIntent().getLongExtra(Constant.IntentKey.WORK_ID,-1),pageIndex);
        });

        mOLXJTaskItemAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {});

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

    @SuppressLint("CheckResult")
    @Override
    public void getWorkItemListSuccess(CommonBAPListEntity entity) {
        List<OLXJWorkItemEntity> workItems = new ArrayList<>();
        Set<String> eamPartSet = new HashSet<>();
        Flowable.fromIterable(entity.result)
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        OLXJWorkItemEntity olxjWorkItemEntity = (OLXJWorkItemEntity) o;
                        String eamPart = olxjWorkItemEntity.part + (olxjWorkItemEntity.eamID == null ? "" : olxjWorkItemEntity.eamID.code);
                        if (!eamPartSet.contains(eamPart)){
                            OLXJWorkItemEntity titleItem = new OLXJWorkItemEntity();
                            titleItem.viewType = 1;
                            titleItem.eamID = olxjWorkItemEntity.eamID;
                            titleItem.part = olxjWorkItemEntity.part;
                            eamPartSet.add(eamPart);
                            workItems.add(titleItem);
                            workItems.add(olxjWorkItemEntity);
                        }else {
                            workItems.add(olxjWorkItemEntity);
                        }

                    }
                }, throwable -> {

                }, () -> refreshListController.refreshComplete(workItems));
    }

    @Override
    public void getWorkItemListFailed(String errorMsg) {
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
    }
}
