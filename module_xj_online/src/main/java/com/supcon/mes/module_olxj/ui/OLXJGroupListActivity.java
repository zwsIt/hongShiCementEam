package com.supcon.mes.module_olxj.ui;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.supcon.mes.mbap.beans.LinkEntity;
import com.supcon.mes.mbap.beans.WorkFlowEntity;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.LinkController;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.middleware.model.bean.IDEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_olxj.R;
import com.supcon.mes.module_olxj.controller.OLXJWorkItemListRefController;
import com.supcon.mes.module_olxj.model.api.OLXJGroupAPI;
import com.supcon.mes.module_olxj.model.api.OLXJTaskCreateAPI;
import com.supcon.mes.module_olxj.model.api.OLXJTaskStatusAPI;
import com.supcon.mes.module_olxj.model.bean.OLXJGroupEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJWorkItemDto;
import com.supcon.mes.module_olxj.model.bean.OLXJWorkItemEntity;
import com.supcon.mes.module_olxj.model.contract.OLXJGroupContract;
import com.supcon.mes.module_olxj.model.contract.OLXJTaskCreateContract;
import com.supcon.mes.module_olxj.model.contract.OLXJTaskStatusContract;
import com.supcon.mes.module_olxj.presenter.OLXJGroupPresenter;
import com.supcon.mes.module_olxj.presenter.OLXJTaskCreatePresenter;
import com.supcon.mes.module_olxj.presenter.OLXJTaskStatusPresenter;
import com.supcon.mes.module_olxj.ui.adapter.OLXJGroupListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wangshizhan on 2019/3/29
 * Email:wangshizhan@supcom.com
 */
@Router(Constant.Router.XJLX_LIST)
@Presenter(value = {OLXJGroupPresenter.class, OLXJTaskCreatePresenter.class, OLXJTaskStatusPresenter.class})
public class OLXJGroupListActivity extends BaseRefreshRecyclerActivity<OLXJGroupEntity> implements
        OLXJGroupContract.View, OLXJTaskCreateContract.View, OLXJTaskStatusContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("leftBtn")
    ImageButton leftBtn;

    private OLXJWorkItemListRefController mOLXJWorkItemListRefController;
    private LinkController mLinkController;
    private OLXJGroupListAdapter mAdapter;
    private Long deploymentId;
    private OLXJGroupEntity mGroupEntity;
    private boolean isTemp = false;

    @Override
    protected IListAdapter<OLXJGroupEntity> createAdapter() {
        mAdapter = new OLXJGroupListAdapter(context);
        return mAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_olxj_group_list;
    }

    @Override
    protected void onInit() {
        super.onInit();
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);

        isTemp = getIntent().getBooleanExtra(Constant.IntentKey.XJ_IS_TEMP, false);
        if (isTemp) {//临时巡检
            mOLXJWorkItemListRefController = new OLXJWorkItemListRefController(context);
            registerController(OLXJWorkItemListRefController.class.getSimpleName(), mOLXJWorkItemListRefController);
            deploymentId = getIntent().getLongExtra(Constant.IntentKey.DEPLOYMENT_ID, 0);

            mLinkController = new LinkController();
            mLinkController.initStartTransition(null, "tempWF");
        }
    }

    @Override
    protected void initView() {
        super.initView();
        setStatusBarColor(R.color.themeColor);
        ((ViewGroup) titleText.getParent()).setBackgroundResource(R.color.themeColor);
        if (isTemp) {
            titleText.setText("选择路线生成临时任务");
        } else
            titleText.setText("巡检路线");
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
                presenterRouter.create(OLXJGroupAPI.class).queryGroupList();
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

                OLXJGroupEntity olxjGroupEntity = (OLXJGroupEntity) obj;
//                if(mOLXJWorkItemListRefController!=null) {
//                    onLoading("正在加载该线路巡检项...");
//                    mGroupEntity = olxjGroupEntity;
//                    mOLXJWorkItemListRefController.getData(olxjGroupEntity.id);
//                }
                onLoading("正在生成巡检任务...");
                mGroupEntity = olxjGroupEntity;
                Map<String, Object> map = new HashMap<>();
                map.put("tempStartTime", DateUtil.dateFormat(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
                map.put("tempEndTime", DateUtil.dateFormat(System.currentTimeMillis() + 1000 * 60 * 60 * 24, "yyyy-MM-dd HH:mm:ss"));
                map.put("tempWorkGroupId", mGroupEntity.id);
                map.put("tempStaffId", EamApplication.getAccountInfo().staffId);
                presenterRouter.create(OLXJTaskCreateAPI.class).createTempTaskNew(map);
            }
        });

/*        mOLXJWorkItemListRefController.setOnSuccessListener(new OnSuccessListener<List<OLXJWorkItemEntity>>() {
            @Override
            public void onSuccess(List<OLXJWorkItemEntity> result) {

                if(result == null || result.size() == 0){
//                    ToastUtils.show(context, "该条线路不存在巡检项！");
                    onLoadFailed("该条线路不存在巡检项！");
                    return;
                }
                LogUtil.d("result:"+result.size());
                onLoading("正在生成巡检任务...");

               submit(result);
            }
        });*/
    }

    @Override
    protected void initData() {
        super.initData();

    }

    /**
     * @author zhangwenshuai1
     * @date 2018/3/27
     * @description 初始化无数据
     */
    private void initEmptyView() {
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, ""));
    }

    @Override
    public void queryGroupListSuccess(CommonBAPListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void queryGroupListFailed(String errorMsg) {
        refreshListController.refreshComplete();
        LogUtil.e(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void queryTaskGroupListSuccess(CommonBAPListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void queryTaskGroupListFailed(String errorMsg) {
        refreshListController.refreshComplete();
        LogUtil.e(ErrorMsgHelper.msgParse(errorMsg));
    }


    @SuppressLint("CheckResult")
    private void submit(List<OLXJWorkItemEntity> workItems) {
        Map<String, Object> params = new HashMap<>();
        params.put("bap_validate_user_id", EamApplication.getAccountInfo().userId);
        params.put("potrolTaskWF.createPositionId", EamApplication.getAccountInfo().getPositionId());
        params.put("potrolTaskWF.createDepartmentId", EamApplication.getAccountInfo().getDepartmentId());
        params.put("viewselect", "tempEdit");
        params.put("datagridKey", "mobileEAM_potrolTaskNew_potrolTaskWF_tempEdit_datagrids");
        params.put("viewCode", "mobileEAM_1.0.0_potrolTaskNew_tempEdit");
        params.put("modelName", "PotrolTaskWF");
        params.put("potrolTaskWF.version", 0);
        params.put("id", "");
        List<WorkFlowEntity> workFlowEntities = new ArrayList<>();
        WorkFlowEntity submitEntity = null;
        for (LinkEntity linkEntity : mLinkController.getLinkEntities()) {

            WorkFlowEntity workFlowEntity = new WorkFlowEntity();
            workFlowEntity.dec = linkEntity.description;
            workFlowEntity.outcome = linkEntity.name;
            workFlowEntity.type = "normal";

            workFlowEntities.add(workFlowEntity);

            if ("生效".equals(linkEntity.description)) {
                submitEntity = workFlowEntity;
            }
        }

        params.put("operateType", Constant.Transition.SUBMIT);
        params.put("workFlowVar.outcomeMapJson", workFlowEntities.toString());
        params.put("workFlowVar.outcome", submitEntity != null ? submitEntity.outcome : "");
        params.put("deploymentId", deploymentId);

        params.put("taskDescription", "mobileEAM_1.0.0.tempWF.task333");
        params.put("activityName", "task333");
        params.put("potrolTaskWF.positionLayRec", 1000);
        params.put("potrolTaskWF.workGroupID.id", mGroupEntity.id);
        params.put("potrolTaskWF.resstaffID.id", EamApplication.getAccountInfo().staffId);
        params.put("potrolTaskWF.starTime", DateUtil.dateFormat(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
        params.put("potrolTaskWF.endTime", DateUtil.dateFormat(System.currentTimeMillis() + 1000 * 60 * 60 * 24, "yyyy-MM-dd HH:mm:ss"));//默认加一天
        params.put("potrolTaskWF.valueType.id", mGroupEntity.valueType.id);
        params.put("potrolTaskWF.valueType.value", mGroupEntity.valueType.value);
        params.put("potrolTaskWF.isTemp", true);

        List<OLXJWorkItemDto> olxjWorkItemDtos = new ArrayList<>();
        Flowable.just(workItems)
                .subscribeOn(Schedulers.newThread())
                .flatMap(new Function<List<OLXJWorkItemEntity>, Publisher<OLXJWorkItemDto>>() {
                    @Override
                    public Publisher<OLXJWorkItemDto> apply(List<OLXJWorkItemEntity> olxjWorkItemEntities) throws Exception {
                        olxjWorkItemDtos.addAll(GsonUtil.jsonToList(workItems.toString(), OLXJWorkItemDto.class));
                        return Flowable.fromIterable(olxjWorkItemDtos);
                    }
                })
                .subscribe(new Consumer<OLXJWorkItemDto>() {

                    @Override
                    public void accept(OLXJWorkItemDto olxjWorkItemDto) throws Exception {
                        olxjWorkItemDto.workItemID = new IDEntity(olxjWorkItemDto.id);
                        olxjWorkItemDto.publicItemID = new IDEntity("");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        params.put("dgLists['dg1489026057217']", olxjWorkItemDtos.toString());
                        params.put("dg1489026057217ModelCode", "mobileEAM_1.0.0_potrolTaskNew_PotrolTPartWF");
                        params.put("dg1489026057217ListJson", olxjWorkItemDtos.toString());

                        LogUtil.d("" + GsonUtil.gsonString(params));
                        presenterRouter.create(OLXJTaskCreateAPI.class).createTempTask(params);
                    }
                });


    }

    @Override
    public void createTempTaskSuccess(BapResultEntity resultEntity) {
        presenterRouter.create(OLXJTaskStatusAPI.class).updateStatus(EamApplication.getAccountInfo().staffId, String.valueOf(resultEntity.id));
    }

    @Override
    public void createTempTaskFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void createTempTaskNewSuccess(CommonEntity entity) {
        String id = (String) entity.result;
        if (TextUtils.isEmpty(id)) {
            onLoadFailed("生成任务ID为空！");
            return;
        }
        presenterRouter.create(OLXJTaskStatusAPI.class).updateStatus(EamApplication.getAccountInfo().staffId, id);
    }

    @Override
    public void createTempTaskNewFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void updateStatusSuccess() {
        onLoadSuccessAndExit("生成成功！", new OnLoaderFinishListener() {
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
}
