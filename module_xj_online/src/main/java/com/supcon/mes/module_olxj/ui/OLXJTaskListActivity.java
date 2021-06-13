package com.supcon.mes.module_olxj.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.CustomSwipeLayout;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.event.NFCEvent;
import com.supcon.mes.middleware.model.event.PositionEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.NFCHelper;
import com.supcon.mes.middleware.util.SystemCodeManager;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_olxj.IntentRouter;
import com.supcon.mes.module_olxj.R;
import com.supcon.mes.module_olxj.constant.OLXJConstant;
import com.supcon.mes.module_olxj.controller.MapController;
import com.supcon.mes.module_olxj.controller.OLXJTaskAreaController;
import com.supcon.mes.module_olxj.model.api.OLXJTaskAPI;
import com.supcon.mes.module_olxj.model.api.OLXJTaskStatusAPI;
import com.supcon.mes.module_olxj.model.bean.OLXJAreaEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJTaskEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJWorkItemEntity;
import com.supcon.mes.module_olxj.model.contract.OLXJTaskContract;
import com.supcon.mes.module_olxj.model.contract.OLXJTaskStatusContract;
import com.supcon.mes.module_olxj.presenter.OLXJTaskListPresenter;
import com.supcon.mes.module_olxj.presenter.OLXJTaskStatusPresenter;
import com.supcon.mes.module_olxj.ui.adapter.OLXJSelectPathAdapter;
import com.supcon.mes.module_olxj.ui.adapter.OLXJTaskListAdapter;
import com.supcon.mes.sb2.model.event.BarcodeEvent;
import com.supcon.mes.sb2.model.event.SB2AttachEvent;
import com.supcon.mes.sb2.model.event.UhfRfidEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


/**
 * Created by zhangwenshuai1 on 2018/3/10.
 */

@Router(Constant.Router.JHXJ_LIST)
@Controller(MapController.class)
@Presenter(value = {OLXJTaskListPresenter.class, OLXJTaskStatusPresenter.class})
public class OLXJTaskListActivity extends BaseRefreshRecyclerActivity<OLXJTaskEntity>
        implements OLXJTaskContract.View, OLXJTaskStatusContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("leftBtn")
    ImageButton leftBtn;  //返回键

    @BindByTag("titleText")
    TextView titleText;  //标题

    @BindByTag("xjTitleAdd")
    ImageView xjTitleAdd;

    @BindByTag("xjTitleMap")
    ImageView xjTitleMap;
    @BindByTag("todayXjRecordsTv")
    TextView todayXjRecordsTv;  //今日巡检

    @BindByTag("olxjFinishBtn")
    Button olxjFinishBtn;

    @BindByTag("olxjAbortBtn")
    Button olxjAbortBtn;

    @BindByTag("btnLayout")
    LinearLayout btnLayout;

    OLXJTaskListAdapter mOLXJTaskListAdapter;

    Map<String, Object> queryParam = new HashMap<>();  //参数
    OLXJTaskEntity mOLXJTaskEntity;
    List<OLXJAreaEntity> mAreaEntities = new ArrayList<>();  //根据barCode、taskId查询数据列
    OLXJTaskAreaController mOLXJTaskAreaController;

    List<SystemCodeEntity> cartReasonInfoList = new ArrayList<>();  //签到原因对象list
    List<String> cartReasonList = new ArrayList<>();  //签到原因名称list

    // 任务终止原因
    List<SystemCodeEntity> endReasonInfoList = new ArrayList<>();
    List<String> endReasonList = new ArrayList<>();
    String reason = null;
    private boolean isFront = false;
    private int enterPosition = -1;
    private NFCHelper nfcHelper;
    private CustomDialog customDialog;
    private CustomDialog mCustomDialog; // 结束任务dialog对象

    private List<OLXJTaskEntity> taskEntityList = new ArrayList<>();
    private List<OLXJTaskEntity> selectTaskEntityList = new ArrayList<>();
    private CustomDialog pathDialog, confirmDialog;
    private OLXJSelectPathAdapter olxjSelectPathAdapter;
    private LinearLayoutManager layoutManager;
    private int num = 0;
    List<OLXJAreaEntity> mAreaEntities01 = new ArrayList<>();  //01代表未检
    List<OLXJAreaEntity> mAreaEntities04 = new ArrayList<>();  //04代表已检
    @Override
    protected IListAdapter<OLXJTaskEntity> createAdapter() {
        mOLXJTaskListAdapter = new OLXJTaskListAdapter(this);
        return mOLXJTaskListAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_olxj_task_list;
    }

    @Override
    protected void onInit() {
        super.onInit();
        refreshListController.setAutoPullDownRefresh(true);  //将自动下拉刷新
        refreshListController.setPullDownRefreshEnabled(false);
        EventBus.getDefault().register(this);

        nfcHelper = NFCHelper.getInstance();
        if (nfcHelper != null) {
            nfcHelper.setup(this);
            nfcHelper.setOnNFCListener(nfc -> {
                LogUtil.d("NFC Received : " + nfc);
                EventBus.getDefault().post(new NFCEvent(nfc));
            });
        }
        openDevice();
    }

    @Override
    protected void onRegisterController() {
        super.onRegisterController();
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor); //设置手机导航栏背景色
        //设置页面布局
        contentView.setLayoutManager(new LinearLayoutManager(context));  //线性布局
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(1, context)));  //列表项之间间隔
        contentView.addOnItemTouchListener(new CustomSwipeLayout.OnSwipeItemTouchListener(this));
        titleText.setText("计划巡检");
        xjTitleAdd.setVisibility(View.GONE);
        xjTitleMap.setVisibility(View.GONE);
//        todayXjRecordsTv.setVisibility(View.GONE);

        //这个是初始化选择路线的弹框
        if (pathDialog == null) {
            pathDialog = new CustomDialog(context).layout(R.layout.v_dialog_path, DisplayUtil.dip2px(312, context), DisplayUtil.dip2px(282, context));
            Objects.requireNonNull(pathDialog.getDialog().getWindow()).setBackgroundDrawableResource(R.color.transparent);
        }
        pathDialog.getDialog().setCancelable(false);
        pathDialog.getDialog().setCanceledOnTouchOutside(false);
        RecyclerView rlv_method = pathDialog.getDialog().findViewById(R.id.rlv_method);
        if (layoutManager == null) {
            layoutManager = new LinearLayoutManager(context);
        }
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlv_method.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(1, context)));
        rlv_method.setLayoutManager(layoutManager);
        if (olxjSelectPathAdapter == null) {
            olxjSelectPathAdapter = new OLXJSelectPathAdapter(context);
        }
        rlv_method.setAdapter(olxjSelectPathAdapter);

        initEmptyView();

    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        if (olxjSelectPathAdapter != null) {
            olxjSelectPathAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
                @Override
                public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                    if (action == -1) {

                    } else {
                        OLXJTaskEntity olxjTaskEntity = (OLXJTaskEntity) obj;
                        for (int i = 0; i < olxjSelectPathAdapter.getList().size(); i++) {
                            olxjSelectPathAdapter.getList().get(i).isSelect = false;
                        }
                        if (olxjTaskEntity.isSelect) {
                            olxjTaskEntity.isSelect = false;
                        } else {
                            olxjTaskEntity.isSelect = true;
                        }
                        olxjSelectPathAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
        //返回键
        leftBtn.setOnClickListener(v -> onBackPressed());
        todayXjRecordsTv.setOnClickListener(v -> IntentRouter.go(context, Constant.Router.JHXJ_TODAY_RECORDS_LIST));
        xjTitleMap.setOnClickListener(v -> {
            if (mOLXJTaskListAdapter.getList() == null || mOLXJTaskListAdapter.getList().size() == 0) {
                ToastUtils.show(context, "未领取巡检任务,不能切换地图模式!");
                return;
            }

            if (xjTitleMap.isSelected()) {
                xjTitleMap.setSelected(false);
                mOLXJTaskListAdapter.setMap(false);
                btnLayout.setVisibility(View.VISIBLE);
                getController(MapController.class).hide();
            } else {
                xjTitleMap.setSelected(true);
                mOLXJTaskListAdapter.setMap(true);
                btnLayout.setVisibility(View.GONE);

                if (mAreaEntities == null || mAreaEntities.size() == 0) {
                    if (mOLXJTaskListAdapter != null && mOLXJTaskListAdapter.getList() != null) {
                        doLoadArea(mOLXJTaskListAdapter.getItem(0));
                    }
                }
                contentView.scrollBy(0, -DisplayUtil.dip2px(720, context));
                getController(MapController.class).show(mOLXJTaskListAdapter.getItem(0));
            }
//            mOLXJTaskListAdapter.notifyDataSetChanged();
        });

        getController(MapController.class).setOnMapAreaClickListener(this::showSignReason);

        refreshListController.setOnRefreshListener(() -> {
//            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            String today = sdf.format(new Date());
//            queryParam.put(Constant.BAPQuery.STAR_TIME1, new StringBuilder(today).append(Constant.TimeString.START_TIME).toString());
            queryParam.put(Constant.BAPQuery.STAR_TIME2, DateUtil.dateFormat(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
            queryParam.put(Constant.BAPQuery.END_TIME1, DateUtil.dateFormat(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
            queryParam.put(Constant.BAPQuery.TABLE_NO, getIntent().getStringExtra(Constant.IntentKey.TABLENO)); // 工作提醒传参

            presenterRouter.create(OLXJTaskAPI.class).getOJXJLastTaskList(queryParam);
        });

        mOLXJTaskListAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {

            String tag = (String) childView.getTag();
            switch (tag) {

                case "taskAreaListView":

                    //手动签到
                    OLXJAreaEntity areaEntity = (OLXJAreaEntity) obj;
                    showSignReason(areaEntity);
                    break;

                case "taskExpandBtn":
                    if (action == 1) {
                        if (mOLXJTaskListAdapter != null && mOLXJTaskListAdapter.getList() != null) {
                            doLoadArea(mOLXJTaskListAdapter.getItem(0));
                        }
                    }
                    break;

                case "itemViewDelBtn":
                    OLXJTaskEntity olxjTaskEntity = (OLXJTaskEntity) obj;

                    new CustomDialog(context)
                            .twoButtonAlertDialog("是否取消该任务【开始时间：" + DateUtil.dateTimeFormat(olxjTaskEntity.starTime) + "】?")
                            .bindView(R.id.redBtn, "")
                            .bindView(R.id.grayBtn, "")
                            .bindClickListener(R.id.grayBtn, v -> {
                            }, true)
                            .bindClickListener(R.id.redBtn, v -> {
                                onLoading("正在取消任务...");
                                presenterRouter.create(OLXJTaskStatusAPI.class).cancelTasks(String.valueOf(olxjTaskEntity.id), "LinkState/04");

                            }, true)
                            .show();

                    break;
            }

        });

        RxView.clicks(xjTitleAdd)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {

                    @Override
                    public void accept(Object o) throws Exception {

                        if (mOLXJTaskListAdapter.getList() != null && mOLXJTaskListAdapter.getList().size() != 0) {
                            showAlertDialog(mOLXJTaskListAdapter.getList().get(0));
                        } else {
                            goXL();
                        }

                    }
                });
        RxView.clicks(olxjFinishBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (mOLXJTaskListAdapter.getList() != null && mOLXJTaskListAdapter.getList().size() != 0)
                        showBtnFinishDialog(mOLXJTaskListAdapter.getList().get(0));
                });
        RxView.clicks(olxjAbortBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (mOLXJTaskListAdapter.getList() != null && mOLXJTaskListAdapter.getList().size() != 0)
                        showAbortDialog(mOLXJTaskListAdapter.getList().get(0));
                });
    }

    /**
     * 加载巡检区域
     *
     * @param taskEntity
     */
    @SuppressLint("CheckResult")
    private void doLoadArea(OLXJTaskEntity taskEntity) {
        onLoading("正在加载任务，请稍后...");
        Flowable.just(1)
                .subscribeOn(Schedulers.io())
                .map(aLong -> {
                    String cache = SharedPreferencesUtils.getParam(context, Constant.SPKey.JHXJ_TASK_CONTENT, "");
                    if (!TextUtils.isEmpty(cache)) {
                        mAreaEntities = GsonUtil.jsonToList(cache, OLXJAreaEntity.class);
                        upAreaData();
                    }
                    return mAreaEntities;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mAreaEntities -> {
                        }
                        , throwable -> {
                        }
                        , () -> {
                            if (mAreaEntities != null && mAreaEntities.size() != 0) {
                                if (mOLXJTaskAreaController != null){
                                    if (mOLXJTaskAreaController.getAreaEntities() != null){
                                        mAreaEntities = mOLXJTaskAreaController.getAreaEntities();
                                        upAreaData();
                                        mOLXJTaskListAdapter.setAreaEntities(mAreaEntities);
//                                getController(MapController.class).setOLXJAreaEntities(mAreaEntities);
                                        onLoadSuccess();
                                        if (mOLXJTaskListAdapter.isAllFinished()) {
                                            showFinishDialog(mOLXJTaskListAdapter.getList().get(0));
                                        }
                                        return;
                                    }
                                }
                            }
                            // 无缓存，在线获取巡检区域数据
                            if (mOLXJTaskAreaController == null) {
                                mOLXJTaskAreaController = new OLXJTaskAreaController(context, 0);

                            }
                            // 加载巡检区域及巡检项
                            mOLXJTaskAreaController.getData(taskEntity, result -> {
                                if (result) {
                                    mAreaEntities = mOLXJTaskAreaController.getAreaEntities();
                                    new Handler(getMainLooper()).post(() -> {
                                        upAreaData();
                                        mOLXJTaskListAdapter.setAreaEntities(mAreaEntities);
                                        onLoadSuccess();
                                        if (mOLXJTaskListAdapter.isAllFinished()) {
                                            showFinishDialog(mOLXJTaskListAdapter.getList().get(0));
                                        }

//                                        getController(MapController.class).setOLXJAreaEntities(mAreaEntities);
                                    });
                                } else {
                                    onLoadFailed("加载失败");
                                }
                            });
                        });

    }

    private void showAlertDialog(OLXJTaskEntity olxjTaskEntity) {
        new CustomDialog(context)
                .twoButtonAlertDialog("一次只能执行一条巡检任务，是否取消")
                .bindClickListener(R.id.grayBtn, v -> {
                }, true)
                .bindClickListener(R.id.redBtn, v -> {

                    onLoading("正在取消任务...");
                    presenterRouter.create(OLXJTaskStatusAPI.class).cancelTasks(String.valueOf(olxjTaskEntity.id), "LinkState/04");
                }, true)
                .show();
    }

    private void showBtnFinishDialog(OLXJTaskEntity olxjTaskEntity) {
        boolean isAllFinished = mOLXJTaskListAdapter.isAllFinished();
        new CustomDialog(context)
                .twoButtonAlertDialog(isAllFinished ? "确定提交任务？" : "还存在未完成的巡检项，确定是否提交任务？")
                .bindClickListener(R.id.grayBtn, v -> {
                }, true)
                .bindClickListener(R.id.redBtn, v -> {

                    onLoading("正在提交任务...");
                    presenterRouter.create(OLXJTaskStatusAPI.class).endTasks(String.valueOf(olxjTaskEntity.id), "结束任务", true);
                }, true)
                .show();
    }

    private void showFinishDialog(OLXJTaskEntity olxjTaskEntity) {
        mCustomDialog = new CustomDialog(context);
        mCustomDialog.getDialog().setCancelable(false);
        mCustomDialog.twoButtonAlertDialog("当前巡检任务已完成！")
//                .bindClickListener(R.id.grayBtn, v -> {
//                }, true)
                .bindClickListener(R.id.redBtn, v -> {

                    onLoading("正在提交任务...");
                    mOLXJTaskListAdapter.clear();
                    mAreaEntities.clear();
                    mOLXJTaskListAdapter.setAreaEntities(mAreaEntities);
                    presenterRouter.create(OLXJTaskStatusAPI.class).endTasks(String.valueOf(olxjTaskEntity.id), "结束任务", true);
                }, true);
        mCustomDialog.getDialog().findViewById(R.id.grayBtn).setVisibility(View.GONE);
        mCustomDialog.show();
    }

    private void showAbortDialog(OLXJTaskEntity olxjTaskEntity) {
        reason = "";
        customDialog = new CustomDialog(context).layout(R.layout.item_dialog,
                DisplayUtil.getScreenWidth(context) - DisplayUtil.dip2px(40, context), WRAP_CONTENT);
        ((ImageView) customDialog.getDialog().findViewById(R.id.customEditIcon)).setImageResource(R.drawable.ic_expand);
        customDialog.bindView(R.id.blueBtn, "确定")
                .bindView(R.id.grayBtn, "取消")
                .bindTextChangeListener(R.id.reason, text -> reason = text.trim())
                .bindClickListener(R.id.customEditIcon, v -> showEndTaskReason(customDialog.getDialog().findViewById(R.id.reason)), false)
                .bindClickListener(R.id.blueBtn, v12 -> {
                    if (TextUtils.isEmpty(reason)) {
                        ToastUtils.show(context, "请输入终止原因!");
                        return;
                    }
                    onLoading("正在终止任务...");
                    presenterRouter.create(OLXJTaskStatusAPI.class).endTasks(String.valueOf(olxjTaskEntity.id), reason, false);
                    customDialog.dismiss();
                }, false)
                .bindClickListener(R.id.grayBtn, null, true);
        customDialog.show();
    }

    /**
     * 终止任务原因选择
     *
     * @param reason
     */
    private void showEndTaskReason(CustomEditText reason) {
        if (endReasonInfoList.size() <= 0) {
            ToastUtils.show(context, "暂无原因数据，请退出页面重新加载");
            return;
        }
        new SinglePickController<String>(this)
                .list(endReasonList)
                .listener((index, item) -> reason.setContent(String.valueOf(item)))
                .show();
    }

    @Override
    protected void initData() {
        super.initData();

        //初始化签到原因列表
        cartReasonInfoList = SystemCodeManager.getInstance().getSystemCodeListByCode(Constant.SystemCode.CART_REASON);
        if (cartReasonInfoList.size() > 0) {
            for (SystemCodeEntity cartReasonInfo : cartReasonInfoList) {
                cartReasonList.add(cartReasonInfo.value);
            }
        }

        //初始化任务终止原因列表
        endReasonInfoList = SystemCodeManager.getInstance().getSystemCodeListByCode(Constant.SystemCode.XJ_END_TASK);
        if (endReasonInfoList.size() > 0) {
            for (SystemCodeEntity entity : endReasonInfoList) {
                endReasonList.add(entity.value);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        isFront = true;

        if (nfcHelper != null)
            nfcHelper.onResumeNFC(this);

    }

    @SuppressLint("CheckResult")
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeviceAttached(SB2AttachEvent sb2AttachEvent) {
        if (sb2AttachEvent.isAttached()) {
            openDevice();
        }
    }

    @SuppressLint("CheckResult")
    private void openDevice() {
    }

    @Override
    protected void onPause() {
        super.onPause();
        isFront = false;

        if (nfcHelper != null)
            nfcHelper.onPauseNFC(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (nfcHelper != null) {
            nfcHelper.release();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //获取到Tag对象
        if (nfcHelper != null)
            nfcHelper.dealNFCTag(intent);
    }

    @SuppressLint("CheckResult")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(RefreshEvent event) {
        if (!TextUtils.isEmpty(event.action)) {
            return;
        }
        Flowable.timer(500, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> refreshListController.refreshBegin());
    }


    @SuppressLint("CheckResult")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(LoginEvent event) {
        Flowable.timer(500, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> refreshListController.refreshBegin());
    }


    @SuppressLint("CheckResult")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAreaUpdate(PositionEvent positionEvent/*OLXJAreaEntity areaEntity*/) {
        if (enterPosition != -1) {
            OLXJAreaEntity areaEntity = (OLXJAreaEntity) positionEvent.getObj();
            mAreaEntities.set(enterPosition, areaEntity);
            upAreaData();
            mOLXJTaskListAdapter.setAreaEntities(mAreaEntities);
//            saveTask(mOLXJTaskEntity.toString());
            // 当前巡检任务下全部完成巡检区域，自动结束任务
            if (mOLXJTaskListAdapter.isAllFinished()) {
                showFinishDialog(mOLXJTaskListAdapter.getList().get(0));
            }
        }

        /*Flowable.timer(0, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (enterPosition != -1) {
                        mAreaEntities.set(enterPosition, areaEntity);
                        mOLXJTaskListAdapter.setAreaEntities(mAreaEntities);
                        saveAreaCache(mAreaEntities.toString());
                        saveTask(mOLXJTaskEntity.toString());
                    }
                }, throwable -> {

                }, () -> {
                    if (enterPosition != -1) {
                        // 当前巡检任务下全部完成巡检区域，自动结束任务
                        if (mOLXJTaskListAdapter.isAllFinished()) {
//                                Flowable.timer(300, TimeUnit.MILLISECONDS)
//                                        .observeOn(AndroidSchedulers.mainThread())
//                                        .subscribe(new Consumer<Long>() {
//                                            @Override
//                                            public void accept(Long aLong) throws Exception {
                            showFinishDialog(mOLXJTaskListAdapter.getList().get(0));
//                                            }
//                                        });
                        }

                    }
                });*/

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void scanBarCode(BarcodeEvent barcodeEvent) {
        LogUtil.i("BarcodeEvent", barcodeEvent.getScanCode());
        dealSign(barcodeEvent.getScanCode());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUhfRfidEpcCode(UhfRfidEvent uhfRfidEvent) {
        Log.d("EPC", uhfRfidEvent.getEpcCode());
        dealSign(uhfRfidEvent.getEpcCode());
    }

    /**
     * @param
     * @description NFC事件
     * @author zhangwenshuai1
     * @date 2018/6/28
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getNFC(NFCEvent nfcEvent) {

        /*//判断是否开启NFC
        boolean nfc_enable  = SharedPreferencesUtils.getParam(context, Constant.SPKey.NFC_ENABLE,false);
        if (!nfc_enable){
            ToastUtils.show(context,"请到应用的【设置】中开启NFC",2000);
            return;
        }*/
        LogUtil.d("NFC_TAG", nfcEvent.getNfc());
        isFront = true;
        Map<String, Object> nfcJson = Util.gsonToMaps(nfcEvent.getNfc());
        dealSign((String) nfcJson.get("id"));
    }

    /**
     * @param
     * @return
     * @description 红外或UHF RFID（超高频）通用签到处理
     * @author zhangwenshuai1 2018/8/2
     */
    private void dealSign(String code) {
        if (!isFront) {
            return;
        }

        List<OLXJTaskEntity> list = mOLXJTaskListAdapter.getList();//获取当前页面列表
        if (list == null)
            return;
        if (list.size() <= 0 || mAreaEntities.size() <= 0) {
            ToastUtils.show(context, "暂无任务数据列表");
            return;
        }
        int index = 0;
        for (OLXJAreaEntity areaEntity : mAreaEntities) {
            if (code.equals(areaEntity.signCode)) {
                updateXJAreaEntity(areaEntity, true);//update数据
                enterPosition = index;
                updateTaskStatus();
                doGoArea(areaEntity);  //跳转
                return;
            }
            index++;
        }
        if (index == mAreaEntities.size()) {
            ToastUtils.show(context, "不存在对应的签到编码");
        }
    }

    private void updateTaskStatus() {
        if (mOLXJTaskListAdapter.getList() != null && mOLXJTaskListAdapter.getList().size() != 0) {
            OLXJTaskEntity taskEntity = mOLXJTaskListAdapter.getItem(0);
            taskEntity.isStart = true;
            taskEntity.state = "已检";
            mOLXJTaskListAdapter.notifyItemChanged(0);
        }
    }

    @Override
    public void getOJXJTaskListSuccess(List entity) {

    }

    @Override
    public void getOJXJTaskListFailed(String errorMsg) {

    }

    @Override
    public void getOJXJLastTaskListSuccess(List entities) {
        mOLXJTaskListAdapter.resetExpandPosition();
        OLXJTaskEntity olxjTaskEntity = (OLXJTaskEntity) entities.get(0);

        taskEntityList.clear();
        taskEntityList.addAll(entities);
        if (entities.size() > 0) {
            olxjSelectPathAdapter.clear();
            olxjSelectPathAdapter.setList(entities);
            olxjSelectPathAdapter.notifyDataSetChanged();
            pathDialog.bindClickListener(R.id.tv_true, v -> {
                pathDialog.dismiss();
                for (int i = 0; i < olxjSelectPathAdapter.getList().size(); i++) {
                    OLXJTaskEntity olxjTaskEntity1 = olxjSelectPathAdapter.getList().get(i);
                    if (olxjTaskEntity1.isSelect) {
                        showConfirm(olxjTaskEntity1);
                    }
                }
            }, false)
                    .bindClickListener(R.id.tv_cancel, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    }, true);
                if (taskEntityList.size() == 1){
                    resultShow(olxjSelectPathAdapter.getList());
                } else {
                    pathDialog.show();
                }

//            String taskCache = SharedPreferencesUtils.getParam(context, Constant.SPKey.JHXJ_TASK_ENTITY, "");
//
//            if (olxjTaskEntity.completedPeopleActual == null){
//                pathDialog.show();
//            } else {
//                if (TextUtils.isEmpty(taskCache)){
//                    pathDialog.show();
//                }
//            }

        } else {
            refreshListController.refreshComplete();
        }

//        refreshListController.refreshComplete(entities);


    }

//    @Override
//    public void getOJXJLastTaskListSuccess(List entities) {
//        mOLXJTaskListAdapter.resetExpandPosition();
//
//        if (entities.size() != 0 && isCurrentTask((OLXJTaskEntity) entities.get(0))) {
//            entities.set(0, mOLXJTaskEntity);
//        }
//        refreshListController.refreshComplete(entities);
//        if (entities.size() > 0) {
//            doLoadArea(mOLXJTaskListAdapter.getItem(0));
//            mOLXJTaskListAdapter.expand();
//        }
//    }


    private void resultShow(List entities) {
        refreshListController.refreshComplete(entities);
        if (entities.size() > 0) {
            doLoadArea(mOLXJTaskListAdapter.getItem(0));
            mOLXJTaskListAdapter.expand();
        }
    }

    private void showConfirm(OLXJTaskEntity taskEntity) {
        confirmDialog = new CustomDialog(context)
                .twoButtonAlertDialog("您确定选择这条巡检路线嘛？")
                .bindView(R.id.grayBtn, "取消")
                .bindView(R.id.redBtn, "确定")
                .bindClickListener(R.id.grayBtn, v -> {
                    pathDialog.show();
                }, true)
                .bindClickListener(R.id.redBtn, v3 -> {
                    //关闭页面
                    confirmDialog.dismiss();
                    selectTaskEntityList.clear();
                    selectTaskEntityList.add(taskEntity);
                    saveTask(taskEntity.toString());
                    saveAreaCache("");
                    mAreaEntities.clear();
                    resultShow(selectTaskEntityList);
                }, true);
        confirmDialog.getDialog().setCancelable(false);
        confirmDialog.getDialog().setCanceledOnTouchOutside(false);
        confirmDialog.show();
    }

    @Override
    public void getOJXJLastTaskListFailed(String errorMsg) {
        refreshListController.refreshComplete(null);
//        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
    }

    private void goXL() {
        mAreaEntities.clear();
        saveAreaCache("");
        IntentRouter.go(context, Constant.Router.JHXJ_LX_LIST);
    }

    /**
     * @author zhangwenshuai1
     * @date 2018/3/27
     * @description 初始化无数据
     */
    private void initEmptyView() {
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, ""));
    }


    /**
     * @author zhangwenshuai1
     * @date 2018/4/9
     * @description 手工签到原因上拉菜单
     */
    private void showSignReason(OLXJAreaEntity xjAreaEntity) {
        if (xjAreaEntity.isSign && !TextUtils.isEmpty(xjAreaEntity.signedTime)) {
            goArea(xjAreaEntity);
        } else {
            if (cartReasonList.size() <= 0) {
                ToastUtils.show(context, "签到原因为空，请退出页面重新加载");
                return;
            }
            SinglePickController<String> singlePickController;
            singlePickController = new SinglePickController<>(this);
            singlePickController.setCanceledOnTouchOutside(true);
            singlePickController.list(cartReasonList).listener((index, item) -> {
                xjAreaEntity.signReason = cartReasonInfoList.get(index).id;
                updateXJAreaEntity(xjAreaEntity, false);
                goArea(xjAreaEntity);

            }).show();
        }
    }

    /**
     * @param
     * @description update巡检区域数据
     * @author zhangwenshuai1
     * @date 2018/6/15
     */
    private void updateXJAreaEntity(OLXJAreaEntity xjAreaEntity, boolean isCard) {
        if (isCard) {
            xjAreaEntity.signType = "cardType/01";
        } else {
            xjAreaEntity.signType = "cardType/02";
        }
        xjAreaEntity.isSign = true;
        xjAreaEntity.signedTime = DateUtil.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss");

        // 更新签到区域缓存信息
        saveAreaCache(mAreaEntities.toString());
    }

    private int getEnterPosition(OLXJAreaEntity areaEntity) {

        int index = 0;
        for (OLXJAreaEntity olxjAreaEntity : mAreaEntities) {
            if (olxjAreaEntity.id != null && areaEntity.id != null && olxjAreaEntity.id.equals(areaEntity.id)) {
                return index;
            }
            index++;
        }
        return index;
    }

    /**
     * @param
     * @description 跳转巡检项列表
     * @author zhangwenshuai1
     * @date 2018/6/15
     */
    private void goArea(OLXJAreaEntity xjAreaEntity) {
        Log.i("XJArea:", xjAreaEntity.toString());
        enterPosition = getEnterPosition(xjAreaEntity);
        updateTaskStatus();
        doGoArea(xjAreaEntity);
    }

    private void doGoArea(OLXJAreaEntity xjAreaEntity) {
        Bundle bundle = new Bundle();
        Collections.sort(xjAreaEntity.workItemEntities);
        bundle.putSerializable(Constant.IntentKey.XJ_AREA_ENTITY, xjAreaEntity);

        if ("1".equals(xjAreaEntity.finishType)) {
            bundle.putBoolean(Constant.IntentKey.IS_XJ_FINISHED, true);
            IntentRouter.go(context, Constant.Router.OLXJ_WORK_LIST_HANDLED, bundle);
        } else
            IntentRouter.go(context, Constant.Router.OLXJ_WORK_LIST_UNHANDLED, bundle);
    }

    @SuppressLint("CheckResult")
    @Override
    public void updateStatusSuccess() {
        onLoadSuccess("任务操作成功！");
        saveAreaCache("");
        mAreaEntities.clear();
        Flowable.timer(200, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> refreshListController.refreshBegin());
    }

    @Override
    public void updateStatusFailed(String errorMsg) {
//        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @SuppressLint("CheckResult")
    @Override
    public void cancelTasksSuccess() {
        onLoadSuccess("任务取消成功！");
        saveAreaCache("");
        mAreaEntities.clear();
        Flowable.timer(200, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> refreshListController.refreshBegin());
    }

    @Override
    public void cancelTasksFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @SuppressLint("CheckResult")
    @Override
    public void endTasksSuccess() {
        Flowable.timer(0, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    saveAreaCache("");
                    mAreaEntities.clear();
                    onLoadSuccess("任务操作成功！");
                }, throwable -> {

                }, new Action() {
                    @Override
                    public void run() throws Exception {
//                        refreshListController.refreshBegin();
                        presenterRouter.create(OLXJTaskAPI.class).getOJXJLastTaskList(queryParam);
//                        Flowable.timer(300, TimeUnit.MILLISECONDS)
//                                .subscribe(aLong -> refreshListController.refreshBegin());
                    }
                });

    }

    @Override
    public void endTasksFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
//        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void onBackPressed() {
        if (mCustomDialog != null && mCustomDialog.getDialog().isShowing()) {
            return;
        }
        super.onBackPressed();
    }

    private void saveAreaCache(String cache) {
        SharedPreferencesUtils.setParam(context, Constant.SPKey.JHXJ_TASK_CONTENT, cache);
    }

    private void saveTask(String task) {
        SharedPreferencesUtils.setParam(context, Constant.SPKey.JHXJ_TASK_ENTITY, task);
    }


    private void upAreaData(){
        if (mAreaEntities != null && mAreaEntities.size() > 0){
            for (int i = 0; i < mAreaEntities.size(); i++) {
                AtomicInteger finishedNum = new AtomicInteger();
                num = 0;
                for (int j = 0; j < mAreaEntities.get(i).workItemEntities.size(); j++) {
                    if (mAreaEntities.get(i).workItemEntities.get(j).getLinkState() != null){
                        String id = mAreaEntities.get(i).workItemEntities.get(j).getLinkState().id;
                        if (OLXJConstant.MobileWiLinkState.FINISHED_STATE.equals(
                                mAreaEntities.get(i).workItemEntities.get(j).getLinkState().id)){
                            num ++;
                        }
                    }
                }

                if (num == mAreaEntities.get(i).workItemEntities.size()) {
                    mAreaEntities04.add(mAreaEntities.get(i));
                } else {
                    mAreaEntities01.add(mAreaEntities.get(i));
                }
            }
        }
        mAreaEntities.clear();
        mAreaEntities.addAll(mAreaEntities01);
        mAreaEntities.addAll(mAreaEntities04);
        mAreaEntities01.clear();
        mAreaEntities04.clear();
        saveAreaCache(mAreaEntities.toString());
    }
}
