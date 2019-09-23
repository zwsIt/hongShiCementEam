package com.supcon.mes.module_olxj.ui;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_olxj.R;
import com.supcon.mes.module_olxj.model.api.OLXJTaskAPI;
import com.supcon.mes.module_olxj.model.api.OLXJTaskStatusAPI;
import com.supcon.mes.module_olxj.model.bean.OLXJTaskEntity;
import com.supcon.mes.module_olxj.model.contract.OLXJTaskContract;
import com.supcon.mes.module_olxj.model.contract.OLXJTaskStatusContract;
import com.supcon.mes.module_olxj.presenter.OLXJTaskListPresenter;
import com.supcon.mes.module_olxj.presenter.OLXJTaskStatusPresenter;
import com.supcon.mes.module_olxj.ui.adapter.OLXJTaskGroupListAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * Created by wangshizhan on 2019/3/29
 * Email:wangshizhan@supcom.com
 */
@Router(Constant.Router.JHXJ_LX_LIST)
@Presenter(value = {OLXJTaskListPresenter.class, OLXJTaskStatusPresenter.class})
public class OLXJTaskGroupListActivity extends BaseRefreshRecyclerActivity<OLXJTaskEntity> implements
        OLXJTaskContract.View, OLXJTaskStatusContract.View{

    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("leftBtn")
    ImageButton leftBtn;

    private OLXJTaskGroupListAdapter mAdapter;

    private Map<Long, OLXJTaskEntity> mTaskEntityMap = new HashMap<>();

    private Map<String, Object> queryParam = new HashMap<>();

    @Override
    protected IListAdapter<OLXJTaskEntity> createAdapter() {
        mAdapter = new OLXJTaskGroupListAdapter(context);
        return mAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_olxj_task_group_list;
    }

    @Override
    protected void onInit() {
        super.onInit();
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);

    }

    @Override
    protected void initView() {
        super.initView();
        setStatusBarColor(R.color.themeColor);
        ((ViewGroup)titleText.getParent()).setBackgroundResource(R.color.themeColor);

        titleText.setText("领取巡检任务");
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(5, context)));
        initEmptyView();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        refreshListController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {

                presenterRouter.create(OLXJTaskAPI.class).getOJXJTaskList(queryParam);
            }
        });

        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        onBackPressed();
                    }
                });

        mAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {

                OLXJTaskEntity olxjTaskEntity = (OLXJTaskEntity) obj;
                onLoading("正在领取巡检任务...");
                presenterRouter.create(OLXJTaskStatusAPI.class).updateStatus(EamApplication.getAccountInfo().staffId, String.valueOf(olxjTaskEntity.id));
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();

    }

    /**
     *@author zhangwenshuai1
     *@date 2018/3/27
     *@description  初始化无数据
     *
     */
    private void initEmptyView(){
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, ""));
    }

    @Override
    public void updateStatusSuccess() {

        onLoadSuccessAndExit("领取成功！", new OnLoaderFinishListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onLoaderFinished() {
                onBackPressed();
                Flowable.timer(300, TimeUnit.MILLISECONDS)
                        .subscribe(v -> {
                            EventBus.getDefault().post(new RefreshEvent());
                        });
            }
        });
    }

    @Override
    public void updateStatusFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void cancelTasksSuccess() {

    }

    @Override
    public void cancelTasksFailed(String errorMsg) {

    }

    @Override
    public void endTasksSuccess() {

    }

    @Override
    public void endTasksFailed(String errorMsg) {

    }

    @Override
    public void getOJXJTaskListSuccess(List entity) {
        refreshListController.refreshComplete(entity);
    }

    @Override
    public void getOJXJTaskListFailed(String errorMsg) {
        LogUtil.e(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void getOJXJLastTaskListSuccess(List entity) {

    }

    @Override
    public void getOJXJLastTaskListFailed(String errorMsg) {

    }
}
