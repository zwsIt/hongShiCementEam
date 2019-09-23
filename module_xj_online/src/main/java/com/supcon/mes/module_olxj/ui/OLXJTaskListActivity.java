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
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.CustomSwipeLayout;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.listener.OnTextListener;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.event.NFCEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.NFCHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.SystemCodeManager;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_olxj.IntentRouter;
import com.supcon.mes.module_olxj.R;
import com.supcon.mes.module_olxj.controller.MapController;
import com.supcon.mes.module_olxj.controller.OLXJTaskAreaController;
import com.supcon.mes.module_olxj.model.api.OLXJTaskAPI;
import com.supcon.mes.module_olxj.model.api.OLXJTaskStatusAPI;
import com.supcon.mes.module_olxj.model.bean.OLXJAreaEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJTaskEntity;
import com.supcon.mes.module_olxj.model.contract.OLXJTaskContract;
import com.supcon.mes.module_olxj.model.contract.OLXJTaskStatusContract;
import com.supcon.mes.module_olxj.presenter.OLXJTaskListPresenter;
import com.supcon.mes.module_olxj.presenter.OLXJTaskStatusPresenter;
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
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


/**
 * Created by zhangwenshuai1 on 2018/3/10.
 */

@Router(Constant.Router.JHXJ_LIST)
@Controller(MapController.class)
@Presenter(value = {OLXJTaskListPresenter.class, OLXJTaskStatusPresenter.class})
public class OLXJTaskListActivity extends BaseRefreshRecyclerActivity<OLXJTaskEntity> implements OLXJTaskContract.View, OLXJTaskStatusContract.View {

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

    private boolean isFront = false;
    private int enterPosition = -1;
    private NFCHelper nfcHelper;
    private CustomDialog customDialog;
    private boolean isFirstIn = true;

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
            nfcHelper.setOnNFCListener(new NFCHelper.OnNFCListener() {
                @Override
                public void onNFCReceived(String nfc) {
                    LogUtil.d("NFC Received : " + nfc);
                    EventBus.getDefault().post(new NFCEvent(nfc));
                }
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
        initEmptyView();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        //返回键
        leftBtn.setOnClickListener(v -> {
            finish();
        });
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

        getController(MapController.class).setOnMapAreaClickListener(new MapController.OnMapAreaClickListener() {
            @Override
            public void onAreaClick(OLXJAreaEntity areaEntity) {
                showSignReason(areaEntity);
            }
        });

        //手动刷新事件
        refreshListController.setOnRefreshListener(() -> {
            queryParam.put(Constant.BAPQuery.STAR_TIME, DateUtil.dateFormat(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
            queryParam.put(Constant.BAPQuery.END_TIME, DateUtil.dateFormat(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
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
                    LogUtil.d("-----", olxjTaskEntity.toString());

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
                .subscribe(new Consumer<Object>() {

                    @Override
                    public void accept(Object o) throws Exception {
                        if (mOLXJTaskListAdapter.getList() != null && mOLXJTaskListAdapter.getList().size() != 0)
                            showFinishDialog(mOLXJTaskListAdapter.getList().get(0));
                    }
                });
        RxView.clicks(olxjAbortBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {

                    @Override
                    public void accept(Object o) throws Exception {
                        if (mOLXJTaskListAdapter.getList() != null && mOLXJTaskListAdapter.getList().size() != 0)
                            showAbortDialog(mOLXJTaskListAdapter.getList().get(0));
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void doLoadArea(OLXJTaskEntity taskEntity) {
        onLoading("正在加载任务，请稍后...");
        Flowable.just(1)
                .subscribeOn(Schedulers.io())
                .map(new Function<Integer, List<OLXJAreaEntity>>() {
                    @Override
                    public List<OLXJAreaEntity> apply(Integer aLong) throws Exception {
                        String cache = SharedPreferencesUtils.getParam(context, Constant.SPKey.JHXJ_TASK_CONTENT, "");
                        if (!TextUtils.isEmpty(cache)) {
                            mAreaEntities = GsonUtil.jsonToList(cache, OLXJAreaEntity.class);
                        }
                        return mAreaEntities;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<OLXJAreaEntity>>() {
                               @Override
                               public void accept(List<OLXJAreaEntity> mAreaEntities) throws Exception {
                                   mOLXJTaskListAdapter.setAreaEntities(mAreaEntities);
                                   getController(MapController.class).setOLXJAreaEntities(mAreaEntities);
                               }
                           }
                        , throwable -> {
                        }
                        , new Action() {
                            @Override
                            public void run() throws Exception {
                                if (mAreaEntities != null && mAreaEntities.size() != 0) {
                                    onLoadSuccess();
                                    return;
                                }
                                if (mOLXJTaskAreaController == null) {
                                    mOLXJTaskAreaController = new OLXJTaskAreaController(context, 0);

                                }
                                mOLXJTaskAreaController.getData(taskEntity, new OnSuccessListener<Boolean>() {
                                    @Override
                                    public void onSuccess(Boolean result) {
                                        if (result) {
                                            mAreaEntities = mOLXJTaskAreaController.getAreaEntities();
                                            new Handler(getMainLooper()).post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    onLoadSuccess();
                                                    mOLXJTaskListAdapter.setAreaEntities(mAreaEntities);
                                                    saveAreaCache(mAreaEntities.toString());

                                                    getController(MapController.class).setOLXJAreaEntities(mAreaEntities);
                                                }
                                            });
                                        } else {
                                            onLoadFailed("加载失败");
                                        }
                                    }
                                });
                            }
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

    private void showFinishDialog(OLXJTaskEntity olxjTaskEntity) {
//        boolean isAllFinished = mOLXJTaskListAdapter.isAllFinished();
//        new CustomDialog(context)
//                .twoButtonAlertDialog(isAllFinished ? "确定提交任务？" : "还存在未完成的巡检项，确定是否提交任务？")
//                .bindClickListener(R.id.grayBtn, v -> {
//                }, true)
//                .bindClickListener(R.id.redBtn, v -> {
//
//                    onLoading("正在提交任务...");
//                    presenterRouter.create(OLXJTaskStatusAPI.class).endTasks(String.valueOf(olxjTaskEntity.id), "结束任务", true);
//                }, true)
//                .show();
        new CustomDialog(context)
                .twoButtonAlertDialog("当前巡检任务已完成,是否确定结束任务?")
                .bindClickListener(R.id.grayBtn, v -> {
                }, true)
                .bindClickListener(R.id.redBtn, v -> {

                    onLoading("正在提交任务...");
                    presenterRouter.create(OLXJTaskStatusAPI.class).endTasks(String.valueOf(olxjTaskEntity.id), "结束任务", true);
                }, true)
                .show();
    }

    String reason = null;

    private void showAbortDialog(OLXJTaskEntity olxjTaskEntity) {
        boolean isAllFinished = mOLXJTaskListAdapter.isAllFinished();
//        new CustomDialog(context)
//                .twoButtonAlertDialog(isAllFinished?"确定终止任务？":"还存在未完成的巡检项，确定终止任务？")
//                .bindClickListener(R.id.grayBtn, v -> {}, true)
//                .bindClickListener(R.id.redBtn, v -> {
//
//                    onLoading("正在终止任务...");
//                    presenterRouter.create(OLXJTaskStatusAPI.class).endTasks(String.valueOf(olxjTaskEntity.id), "终止任务", false);
//                }, true)
//                .show();

        reason = "";
        customDialog = new CustomDialog(context).layout(R.layout.item_dialog,
                DisplayUtil.getScreenWidth(context) - DisplayUtil.dip2px(40, context), WRAP_CONTENT)
                .bindView(R.id.blueBtn, "确定")
                .bindView(R.id.grayBtn, "取消")
                .bindTextChangeListener(R.id.reason, new OnTextListener() {
                    @Override
                    public void onText(String text) {
                        reason = text.trim();
                    }
                })
                .bindClickListener(R.id.blueBtn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v12) {
                        if (TextUtils.isEmpty(reason)) {
                            ToastUtils.show(context, "请输入终止原因!");
                            return;
                        }
                        onLoading("正在终止任务...");
                        presenterRouter.create(OLXJTaskStatusAPI.class).endTasks(String.valueOf(olxjTaskEntity.id), reason, false);
                        customDialog.dismiss();
                    }
                }, false)
                .bindClickListener(R.id.grayBtn, null, true);
        customDialog.show();
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
        LogUtil.i("onRestart");
//        Flowable.timer(100, TimeUnit.MILLISECONDS)
//                .subscribe(new Consumer<Long>() {
//                    @Override
//                    public void accept(Long aLong) throws Exception {
//                        refreshListController.refreshBegin();
//                    }
//                });
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
        LogUtil.e("onRefresh");
        Flowable.timer(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        refreshListController.refreshBegin();
                    }
                });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(LoginEvent event) {
        LogUtil.e("onLogin");
        Flowable.timer(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        refreshListController.refreshBegin();
                    }
                });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAreaUpdate(OLXJAreaEntity areaEntity) {
        if (enterPosition != -1) {
            mAreaEntities.set(enterPosition, areaEntity);
            mOLXJTaskListAdapter.setAreaEntities(mAreaEntities);
            saveAreaCache(mAreaEntities.toString());
            saveTask(mOLXJTaskEntity.toString());

            if (mOLXJTaskListAdapter.isAllFinished()) {
                showFinishDialog(mOLXJTaskListAdapter.getList().get(0));
            }
        }

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
//        Gson gson = new Gson();
//        nfcJson = gson.fromJson(nfcEvent.getNfc(),Map.class);

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
        if (list.size() <= 0) {
            SnackbarHelper.showMessage(contentView, "暂无任务数据列表");
            return;
        }


        if (mAreaEntities.size() <= 0) {
            SnackbarHelper.showMessage(contentView, "不存在对应的签到编码");
            return;
        }
        int index = 0;
        for (OLXJAreaEntity areaEntity : mAreaEntities) {
            if (code.equals(areaEntity.signCode)) {
                updateXJAreaEntity(areaEntity);//update数据
                LogUtil.i("BarcodeEvent1", code);
                enterPosition = index;
                updateTaskStatus();
                doGoArea(areaEntity);  //跳转
            }
            index++;
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
        mOLXJTaskListAdapter.resertExpandPosition();

        if (entities.size() != 0 && isCurrentTask((OLXJTaskEntity) entities.get(0))) {
            entities.set(0, mOLXJTaskEntity);
        }
        refreshListController.refreshComplete(entities);
        if (entities.size() > 0) {
            doLoadArea(mOLXJTaskListAdapter.getItem(0));
            mOLXJTaskListAdapter.expand();
        }
//        if (entities.size() == 0) {
//
//            ToastUtils.show(context, "没有巡检任务!");
//            initEmptyView();
//            if (isFirstIn) {
//                goXL();
//                isFirstIn = false;
//            }
//        }
        if (mOLXJTaskListAdapter.getItemCount() > 0 && xjTitleMap.isSelected())
            getController(MapController.class).show(mOLXJTaskListAdapter.getItem(0));
        else if (mOLXJTaskListAdapter.getItemCount() <= 0)
            getController(MapController.class).show(null);
        else
            getController(MapController.class).hide();
//        else {
//            getAreaCache();
//
//        }

    }

//    @SuppressLint("CheckResult")
//    private void getAreaCache() {
//        Flowable.timer(200, TimeUnit.MILLISECONDS)
//                .subscribeOn(Schedulers.io())
//                .map(new Function<Long, List<OLXJAreaEntity>>() {
//                    @Override
//                    public List<OLXJAreaEntity> apply(Long aLong) throws Exception {
//                        String cache = SharedPreferencesUtils.getParam(context, Constant.SPKey.JHXJ_TASK_CONTENT, "");
//                        if (!TextUtils.isEmpty(cache)) {
//                            mAreaEntities = GsonUtil.jsonToList(cache, OLXJAreaEntity.class);
//                        }
//                        return mAreaEntities;
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<List<OLXJAreaEntity>>() {
//                    @Override
//                    public void accept(List<OLXJAreaEntity> mAreaEntities) throws Exception {
//                        mOLXJTaskListAdapter.setAreaEntities(mAreaEntities);
//                        getController(MapController.class).setOLXJAreaEntities(mAreaEntities);
//                    }
//                });
//    }

    @Override
    public void getOJXJLastTaskListFailed(String errorMsg) {
        refreshListController.refreshComplete(null);
        LogUtil.e(ErrorMsgHelper.msgParse(errorMsg));
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
                return;
            }
            new SinglePickController<String>(this).list(cartReasonList).listener((index, item) -> {
                xjAreaEntity.signReason = cartReasonInfoList.get(index).id;
                xjAreaEntity.isSign = true;
                xjAreaEntity.signType = "cardType/02";
                xjAreaEntity.signedTime = DateUtil.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss");

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
    private void updateXJAreaEntity(OLXJAreaEntity xjAreaEntity) {
        xjAreaEntity.signType = "cardType/01";
        xjAreaEntity.isSign = true;
        xjAreaEntity.signedTime = DateUtil.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
        Log.i("XJArea:update", xjAreaEntity.toString());
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

    @Override
    public void updateStatusSuccess() {
        onLoadSuccess("任务操作成功！");
//        ToastUtils.show(context, "任务操作成功！");
        refreshListController.refreshBegin();
        saveAreaCache("");
        mAreaEntities.clear();
    }

    @Override
    public void updateStatusFailed(String errorMsg) {
//        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void cancelTasksSuccess() {
        onLoadSuccess("任务取消成功！");
//        ToastUtils.show(context, "任务取消成功！");
        refreshListController.refreshBegin();
        saveAreaCache("");
        mAreaEntities.clear();
    }

    @Override
    public void cancelTasksFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
//        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void endTasksSuccess() {
        onLoadSuccess("任务操作成功！");
//        ToastUtils.show(context, "任务取消成功！");
        refreshListController.refreshBegin();
        saveAreaCache("");
        mAreaEntities.clear();
    }

    @Override
    public void endTasksFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
//        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void saveAreaCache(String cache) {
        SharedPreferencesUtils.setParam(context, Constant.SPKey.JHXJ_TASK_CONTENT, cache);
    }

    private void saveTask(String task) {
        SharedPreferencesUtils.setParam(context, Constant.SPKey.JHXJ_TASK_ENTITY, task);
    }

    private boolean isCurrentTask(OLXJTaskEntity taskEntity) {

        if (taskEntity == null) {
            return false;
        }

        String taskCache = SharedPreferencesUtils.getParam(context, Constant.SPKey.JHXJ_TASK_ENTITY, "");

        OLXJTaskEntity cacheTask = GsonUtil.gsonToBean(taskCache, OLXJTaskEntity.class);

        if (cacheTask != null && taskEntity.id == cacheTask.id) {
            mOLXJTaskEntity = cacheTask;
            return true;
        }
        mOLXJTaskEntity = taskEntity;

        saveTask(taskEntity.toString());
        saveAreaCache("");
        mAreaEntities.clear();
        return false;
    }
}
