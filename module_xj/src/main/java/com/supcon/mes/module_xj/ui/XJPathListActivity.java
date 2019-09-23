package com.supcon.mes.module_xj.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.supcon.common.view.App;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.CustomSwipeLayout;
import com.supcon.mes.mbap.beans.FilterBean;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomFilterView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.DaoSession;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.XJAreaEntity;
import com.supcon.mes.middleware.model.bean.XJAreaEntityDao;
import com.supcon.mes.middleware.model.bean.XJPathEntity;
import com.supcon.mes.middleware.model.bean.XJPathEntityDao;
import com.supcon.mes.middleware.model.bean.XJWorkItemEntity;
import com.supcon.mes.middleware.model.event.BarcodeEvent;
import com.supcon.mes.middleware.model.event.NFCEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.event.SB2AttachEvent;
import com.supcon.mes.middleware.model.event.UhfRfidEvent;
import com.supcon.mes.middleware.util.EM55UHFRFIDHelper;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.NFCHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.SystemCodeManager;
import com.supcon.mes.module_xj.IntentRouter;
import com.supcon.mes.module_xj.R;
import com.supcon.mes.module_xj.model.api.XJPathAPI;
import com.supcon.mes.module_xj.model.bean.XJPathListEntity;
import com.supcon.mes.module_xj.model.contract.XJPathContract;
import com.supcon.mes.module_xj.presenter.XJPathListPresenter;
import com.supcon.mes.module_xj.ui.adapter.XJPathListAdapter;
import com.supcon.mes.module_xj.util.FilterHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.query.QueryBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by zhangwenshuai1 on 2018/3/10.
 */

@Router(Constant.Router.XJGL_LIST)
@Presenter(value = {XJPathListPresenter.class})
public class XJPathListActivity extends BaseRefreshRecyclerActivity<XJPathEntity> implements XJPathContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("leftBtn")
    ImageButton leftBtn;  //返回键

    @BindByTag("titleText")
    TextView titleText;  //标题

    @BindByTag("listDateFilter")
    CustomFilterView<FilterBean> listDateFilter;  //时间筛选条件

    @BindByTag("listStateFilter")
    CustomFilterView<FilterBean> listStateFilter; //状态筛选条件

    /*@BindView(R.id.itemAreaProgress)
    TextView itemAreaProgress;    //巡检项进度数*/

    XJPathListAdapter mXJPathListAdapter;
//    private SinglePickController<String> mSingPickerCtrl;

    Map<String,Object> queryParam = new HashMap<>();  //参数

    List<XJPathEntity> startedList = new ArrayList<>();  //已开始数据列表
    List<XJAreaEntity> xjAreaEntityListQuery;  //根据barCode、taskId查询数据列表
    List<XJPathEntity> xjTaskSameList;  //多条已开始的同路线任务列表

    List<SystemCodeEntity> cartReasonInfoList = new ArrayList<>();  //签到原因对象list
    List<String> cartReasonList = new ArrayList<>();  //签到原因名称list

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String today = sdf.format(new Date());

    private boolean isFront = false;
    private int editPosition;
//    private EM55UHFRFIDHelper em55UHFRFIDHelper;  //超高频读取

    private NFCHelper nfcHelper;

    @Override
    protected IListAdapter<XJPathEntity> createAdapter() {
        mXJPathListAdapter = new XJPathListAdapter(this);
        return mXJPathListAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_xjpath_list;
    }

    @Override
    protected void onInit() {
        super.onInit();
        refreshListController.setAutoPullDownRefresh(true);  //将自动下拉刷新
        refreshListController.setPullDownRefreshEnabled(true);
        EventBus.getDefault().register(this);

        nfcHelper = NFCHelper.getInstance();
        if (nfcHelper != null){
            nfcHelper.setup(this);
            nfcHelper.setOnNFCListener(nfc -> EventBus.getDefault().post(new NFCEvent(nfc)));
        }

//        em55UHFRFIDHelper = EM55UHFRFIDHelper.getInstance();

        openDevice();
    }

    @Override
    protected void onRegisterController() {
        super.onRegisterController();
//        mSingPickerCtrl = new SinglePickController<String>(this);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this,R.color.themeColor); //设置手机导航栏背景色
        //设置页面布局
        contentView.setLayoutManager(new LinearLayoutManager(context));  //线性布局
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(1, context)));  //列表项之间间隔
        contentView.addOnItemTouchListener(new CustomSwipeLayout.OnSwipeItemTouchListener(this));
        titleText.setText("巡检作业");

        initEmptyView();
        initFilterView();
    }

    @Override
    protected void initListener() {
        super.initListener();

        //返回键
        leftBtn.setOnClickListener(v -> {
            finish();
        });

        //手动刷新事件
        refreshListController.setOnRefreshListener(() -> presenterRouter.create(XJPathAPI.class).getXJPathList(queryParam));

        mXJPathListAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {

            String tag = (String) childView.getTag();
            editPosition = position;
            switch (tag){

                case "taskStartBtn":

                    //弹框提示
                    if (action == 0 ){
                        XJPathEntity xjPathEntity = (XJPathEntity) obj;
                        xjPathEntity.startTimeActual = DateUtil.DateToString(new Date(),"yyyy-MM-dd HH:mm:ss");
                        xjPathEntity.isStart = true;
                        EamApplication.dao().getXJPathEntityDao().update(xjPathEntity);
                        ToastUtils.show(this, "请扫码签到进入巡检项列表！");
                    }else {
                        XJPathEntity xjPathEntity = (XJPathEntity) obj;
                        if ("待检".equals(xjPathEntity.state)){
                            new CustomDialog(context)
                                    .twoButtonAlertDialog("您是否结束该巡检任务？")
                                    .bindView(R.id.redBtn,"是")
                                    .bindView(R.id.grayBtn,"否")
                                    .bindClickListener(R.id.redBtn,v1 ->{

                                        xjPathEntity.endTimeActual = DateUtil.DateToString(new Date(),"yyyy-MM-dd HH:mm:ss");
                                        xjPathEntity.state = "已检";
                                        EamApplication.dao().getXJPathEntityDao().update(xjPathEntity);
                                        EventBus.getDefault().post(new RefreshEvent(Constant.RefreshAction.XJ_WORK_END, position));

                                    },true )
                                    .bindClickListener(R.id.grayBtn,v1 ->{},true)
                                    .show();
                        }

                    }

                    break;

                case "taskAreaListView":

                    //手动签到
                    XJAreaEntity xjAreaEntity = (XJAreaEntity)obj;
                    List<XJPathEntity> list = EamApplication.dao().getXJPathEntityDao().queryBuilder()
                            .where(XJPathEntityDao.Properties.Id.eq(xjAreaEntity.taskId), XJPathEntityDao.Properties.Ip.eq(EamApplication.getIp()))
                            .list();
                    if (list.size() > 0  &&  !list.get(0).isStart ){
                        SnackbarHelper.showMessage(contentView,"请先手动开始任务");
                        return;
                    }

                    showSignReason(xjAreaEntity);
                    break;

                case "taskExpandBtn":
                    if(action == 1){
                        if(position == mXJPathListAdapter.getItemCount()-1){
                            contentView.smoothScrollBy(0, DisplayUtil.dip2px(200, context), new AccelerateDecelerateInterpolator());
                        }
                        else if(position == 0){
                            contentView.smoothScrollBy(0, DisplayUtil.dip2px(-200, context), new AccelerateDecelerateInterpolator());
                        }
                        else{
                            contentView.smoothScrollToPosition(position);
                        }
                    }
                    else {
                        if(position == 0){
                            contentView.smoothScrollToPosition(position);
                        }
                        else{

                        }
                    }
                    break;

                case "itemViewDelBtn":
                    XJPathEntity xjPathEntity = (XJPathEntity) obj;
                    LogUtil.d("-----",xjPathEntity.toString());

                    new CustomDialog(context)
                            .twoButtonAlertDialog("是否删除该任务【开始时间："+xjPathEntity.startTime+"】?")
                            .bindView(R.id.redBtn,"")
                            .bindView(R.id.grayBtn,"")
                            .bindClickListener(R.id.grayBtn,v -> {},true)
                            .bindClickListener(R.id.redBtn, v -> {

                                //TODO...  删除任务、区域、巡检项
                                DaoSession daoSession = EamApplication.dao();
                                //delete 路线
                                daoSession.getXJPathEntityDao().delete(xjPathEntity);
                                //delete 区域
                                List<XJAreaEntity> xjAreaEntityList  = daoSession.getXJAreaEntityDao().queryBuilder()
                                        .where(XJAreaEntityDao.Properties.TaskId.eq(xjPathEntity.id),XJAreaEntityDao.Properties.Ip.eq(EamApplication.getIp()))
                                        .list();
                                daoSession.getXJAreaEntityDao().deleteInTx(xjAreaEntityList);
                                //delete 巡检项
                                List<XJWorkItemEntity> xjWorkItemEntityList = daoSession.getXJWorkItemEntityDao()
                                        .queryRawCreate("WHERE T.TASK_ID = ? and T.IP = ? ",xjPathEntity.id,EamApplication.getIp())
                                        .list();
                                daoSession.getXJWorkItemEntityDao().deleteInTx(xjWorkItemEntityList);
                                EventBus.getDefault().post(new RefreshEvent(Constant.RefreshAction.XJ_WORK_END, position));

                            }, true)
                            .show();

                    break;
            }

        });

        listDateFilter.setFilterSelectChangedListener(filterBean -> {
            Log.i("XJPath_DateFilter：",filterBean.toString());
            Calendar calendar = Calendar.getInstance();
            String startTime;
            String endTime;
            switch (filterBean.name){
                case Constant.Date.TODAY:
                    startTime = new StringBuilder(sdf.format(new Date())).append(Constant.TimeString.START_TIME).toString();
                    endTime = new StringBuilder(sdf.format(new Date())).append(Constant.TimeString.END_TIME).toString();
                    break;
                case Constant.Date.YESTERDAY:
                    calendar.add(Calendar.DAY_OF_WEEK,-1);
                    startTime = new StringBuilder(sdf.format(calendar.getTime())).append(Constant.TimeString.START_TIME).toString();
                    endTime = new StringBuilder(sdf.format(calendar.getTime())).append(Constant.TimeString.END_TIME).toString();
                    break;
                case Constant.Date.TOMORROW:
                    calendar.add(Calendar.DAY_OF_WEEK,+1);
                    startTime = new StringBuilder(sdf.format(calendar.getTime())).append(Constant.TimeString.START_TIME).toString();
                    endTime = new StringBuilder(sdf.format(calendar.getTime())).append(Constant.TimeString.END_TIME).toString();
                    break;
                case Constant.Date.THREEDAY:
                    calendar.add(Calendar.DAY_OF_WEEK,+1); //明天
                    startTime = new StringBuilder(sdf.format(calendar.getTime())).append(Constant.TimeString.START_TIME).toString();

                    calendar.add(Calendar.DAY_OF_WEEK,+2);  //后两天
                    endTime = new StringBuilder(sdf.format(calendar.getTime())).append(Constant.TimeString.END_TIME).toString();
                    break;
                case Constant.Date.THIS_WEEK:
                    /*calendar.setFirstDayOfWeek(Calendar.MONDAY);
                    calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);*/
                    int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                    if (day_of_week == 0){
                        day_of_week = 7;
                    }
                    calendar.add(Calendar.DATE,-day_of_week + 1);
                    startTime = new StringBuilder(sdf.format(calendar.getTime())).append(Constant.TimeString.START_TIME).toString();

                    Calendar calendar2 = Calendar.getInstance();
//                    calendar.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
                    day_of_week = calendar2.get(Calendar.DAY_OF_WEEK) - 1;
                    if (day_of_week == 0){
                        day_of_week = 7;
                    }
                    calendar2.add(Calendar.DATE,-day_of_week + 7 );
                    endTime = new StringBuilder(sdf.format(calendar2.getTime())).append(Constant.TimeString.END_TIME).toString();
                    break;
                case Constant.Date.THIS_MONTH:
                    calendar.set(Calendar.DAY_OF_MONTH,1);
                    startTime = new StringBuilder(sdf.format(calendar.getTime())).append(Constant.TimeString.START_TIME).toString();

                    calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                    endTime = new StringBuilder(sdf.format(calendar.getTime())).append(Constant.TimeString.END_TIME).toString();
                    break;
                default:
                    startTime = endTime = null;
                    break;
            }

            queryParam.put("startTime",startTime);
            queryParam.put("endTime",endTime);
            doFilter();
        });

        listStateFilter.setFilterSelectChangedListener(filterBean -> {
            Log.i("XJPath_StateFilter：",filterBean.toString());

            String state = Constant.XJPathStateType.WAIT_CHECK_STATE;  //默认状态
            if (Constant.XJPathStateType.WAIT_CHECK_STATE.equals(filterBean.name)){
                state = Constant.XJPathStateType.WAIT_CHECK_STATE;
            }else if (Constant.XJPathStateType.PAST_CHECK_STATE.equals(filterBean.name)){
                state = Constant.XJPathStateType.PAST_CHECK_STATE;
            }

            queryParam.put("state",state);
            doFilter();

        });

    }


    @Override
    protected void initData() {
        super.initData();

        //初始化签到原因列表
        cartReasonInfoList = SystemCodeManager.getInstance().getSystemCodeListByCode(Constant.SystemCode.CART_REASON);
        if (cartReasonInfoList.size() > 0){
            for (SystemCodeEntity cartReasonInfo : cartReasonInfoList){
                cartReasonList.add(cartReasonInfo.value);
            }
        }

        //默认今天
        queryParam.put("startTime",new StringBuilder(today).append(Constant.TimeString.START_TIME).toString());
        queryParam.put("endTime",new StringBuilder(today).append(Constant.TimeString.END_TIME).toString());

        //默认待检
        queryParam.put("state", Constant.XJPathStateType.WAIT_CHECK_STATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isFront = true;
//        refreshListController.refreshBegin();
//        EventBus.getDefault().post(new RefreshEvent());   //同样可以实现：已检中若重录，退出巡检项后，巡检任务页面刷新

        if (nfcHelper != null)
            nfcHelper.onResumeNFC(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.i("onRestart");
//        if (!em55UHFRFIDHelper.isStart()) {
//            em55UHFRFIDHelper.inventoryStart();
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeviceAttached(SB2AttachEvent sb2AttachEvent){

//        em55UHFRFIDHelper.inventoryStop();
//        em55UHFRFIDHelper.close();

        if(sb2AttachEvent.isAttached()){
            openDevice();
        }

    }

    @SuppressLint("CheckResult")
    private void openDevice() {
//        ToastUtils.show(context, "正在打开背夹...");
//        Flowable.just(true)
//                .subscribeOn(Schedulers.newThread())
//                .map(new Function<Boolean, Boolean>() {
//                    @Override
//                    public Boolean apply(Boolean aBoolean) throws Exception {
//                        return em55UHFRFIDHelper.open();
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Boolean>() {
//                    @Override
//                    public void accept(Boolean aBoolean) throws Exception {
//                        if (aBoolean) {
//                            if(!(App.getAppContext().store.lastElement() instanceof XJPathListActivity)){
//                                return;
//                            }
//                            em55UHFRFIDHelper.inventoryStart();
//                            ToastUtils.show(context, "打开成功！");
//                        } else {
//                            ToastUtils.show(context, "打开失败！");
//                        }
//                    }
//                });
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

//        if (em55UHFRFIDHelper.isStart())
//            em55UHFRFIDHelper.inventoryStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

//        em55UHFRFIDHelper.close();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //获取到Tag对象
        if (nfcHelper != null)
            nfcHelper.dealNFCTag(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(RefreshEvent event){
//        presenterRouter.create(XJPathAPI.class).getXJPathList(queryParam);
        refreshListController.refreshBegin();
        /*if(Constant.RefreshAction.XJ_WORK_END.equals(event.action)){
            mXJPathListAdapter.notifyItemRemoved(editPosition);
            mXJPathListAdapter.notifyItemRangeChanged(editPosition, mXJPathListAdapter.getList().size());
        }
        else
            mXJPathListAdapter.notifyItemChanged(editPosition);*/
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void scanBarCode(BarcodeEvent barcodeEvent){

        LogUtil.i("BarcodeEvent",barcodeEvent.getCode());

        dealSign(barcodeEvent.getCode());

        /*if (!isFront){
            return;
        }

        List<XJAreaEntity> xjAreaEntityList = EamApplication.dao().getXJAreaEntityDao().queryBuilder().where(XJAreaEntityDao.Properties.SignCode.eq(barcodeEvent.getCode())).list();
        if (xjAreaEntityList.size() <= 0){
            SnackbarHelper.showMessage(contentView,"不存在对应的签到编码");
            return;
        }

        //扫描
        boolean flag = false;//提示是否显示标识
        List<XJPathEntity> list = mXJPathListAdapter.getList();
        startedList.clear();

        for ( int i =0 ; i< list.size();i++ ){
            if (!list.get(i).isStart){
                if (i == list.size()-1 && !flag){
                    SnackbarHelper.showMessage(contentView,"请选择相应任务并点击开始");
                    return;
                }
            }else {
                startedList.add(list.get(i));
                flag = true;
            }
        }

        //通过barCode查询区域
        xjAreaEntityListQuery = new ArrayList();
        xjTaskSameList = new ArrayList();
        int count = 0;
        for (XJPathEntity xjPathEntity : startedList){
            xjAreaEntityListQuery = EamApplication.dao().getXJAreaEntityDao().queryBuilder().where(XJAreaEntityDao.Properties.SignCode.eq(barcodeEvent.getCode()),XJAreaEntityDao.Properties.TaskId.eq(xjPathEntity.id)).list();
            if (xjAreaEntityListQuery.size() > 0){
                //存在多个已开始的不同时间段的同路线任务，选择跳转。
                xjTaskSameList.add(xjPathEntity);
            }else {
                count++;
                if (count == startedList.size()){
                    SnackbarHelper.showMessage(contentView,"不存在对应的签到编码");
                    return;
                }
            }
        }

        //选择跳转
        if (xjTaskSameList.size() > 1){
            showOptionPath(barcodeEvent.getCode(),xjTaskSameList);
        }else {
            XJAreaEntity xjAreaEntity = EamApplication.dao().getXJAreaEntityDao().queryBuilder().where(XJAreaEntityDao.Properties.SignCode.eq(barcodeEvent.getCode())
                    ,XJAreaEntityDao.Properties.TaskId.eq(xjTaskSameList.get(0).id)).list().get(0);

            updateXJAreaEntity(xjAreaEntity);//update数据
            LogUtil.i("BarcodeEvent1",barcodeEvent.getCode());
            goArea(xjAreaEntity);  //跳转
        }*/


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUhfRfidEpcCode(UhfRfidEvent uhfRfidEvent) {
        Log.d("EPC", uhfRfidEvent.getEpcCode());
        dealSign(uhfRfidEvent.getEpcCode());
    }

    /**
     * @description NFC事件
     * @param
     * @author zhangwenshuai1
     * @date 2018/6/28
     *
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getNFC(NFCEvent nfcEvent){

        /*//判断是否开启NFC
        boolean nfc_enable  = SharedPreferencesUtils.getParam(context, Constant.SPKey.NFC_ENABLE,false);
        if (!nfc_enable){
            ToastUtils.show(context,"请到应用的【设置】中开启NFC",2000);
            return;
        }*/
        LogUtil.d("NFC_TAG",nfcEvent.getNfc());
        isFront = true;
        Map<String,Object> nfcJson = GsonUtil.gsonToMaps(nfcEvent.getNfc());
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

        List<XJPathEntity> list = mXJPathListAdapter.getList();//获取当前页面列表
        if (list == null)
            return;
        if (list.size() <= 0) {
            SnackbarHelper.showMessage(contentView, "暂无任务数据列表");
            return;
        }

        List<XJAreaEntity> xjAreaEntityList = EamApplication.dao().getXJAreaEntityDao().queryBuilder()
                .where(XJAreaEntityDao.Properties.SignCode.eq(code),XJAreaEntityDao.Properties.Ip.eq(EamApplication.getIp()))
                .list();
        if (xjAreaEntityList.size() <= 0) {
            SnackbarHelper.showMessage(contentView, "不存在对应的签到编码");
            return;
        }

        //扫描
        boolean flag = false;//提示是否显示标识

        startedList.clear();

        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).isStart) {
                if (i == list.size() - 1 && !flag) {
                    SnackbarHelper.showMessage(contentView, "请选择相应任务并点击开始");
                    return;
                }
            } else {
                startedList.add(list.get(i));
                flag = true;
            }
        }

        //通过barCode查询区域
        xjAreaEntityListQuery = new ArrayList();
        xjTaskSameList = new ArrayList();
        int count = 0;
        for (XJPathEntity xjPathEntity : startedList) {
            xjAreaEntityListQuery = EamApplication.dao().getXJAreaEntityDao().queryBuilder()
                    .where(XJAreaEntityDao.Properties.SignCode.eq(code), XJAreaEntityDao.Properties.TaskId.eq(xjPathEntity.id),XJAreaEntityDao.Properties.Ip.eq(EamApplication.getIp()))
                    .list();
            if (xjAreaEntityListQuery.size() > 0) {
                //存在多个已开始的不同时间段的同路线任务，选择跳转。
                xjTaskSameList.add(xjPathEntity);
            } else {
                count++;
                if (count == startedList.size()) {
                    SnackbarHelper.showMessage(contentView, "不存在对应的签到编码");
                    return;
                }
            }
        }

        //选择跳转
        if (xjTaskSameList.size() > 1) {
            showOptionPath(code, xjTaskSameList);
        } else {
            XJAreaEntity xjAreaEntity = EamApplication.dao().getXJAreaEntityDao().queryBuilder().where(XJAreaEntityDao.Properties.SignCode.eq(code)
                    , XJAreaEntityDao.Properties.TaskId.eq(xjTaskSameList.get(0).id),XJAreaEntityDao.Properties.Ip.eq(EamApplication.getIp())).list().get(0);

            updateXJAreaEntity(xjAreaEntity);//update数据
            LogUtil.i("BarcodeEvent1", code);
            goArea(xjAreaEntity);  //跳转
        }
    }

    @Override
    public void getXJPathListSuccess(XJPathListEntity entityList) {
        refreshListController.refreshComplete(entityList.result); //成功获取数据
        if (entityList.result.size() <= 0){
            initEmptyView();
        }
    }

    @Override
    public void getXJPathListFailed(String errorMsg) {
        refreshListController.refreshComplete();
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

    /**
     *@author zhangwenshuai1
     *@date 2018/3/27
     *@description 初始化筛选条件
     *
     */
    private void initFilterView(){
        listDateFilter.setData(FilterHelper.createDateFilter());
        listDateFilter.setCurrentItem("今天");

        listStateFilter.setData(FilterHelper.createXJPathStateFilter());
        listStateFilter.setCurrentItem("待检");
//        listStateFilter.setCurrentPosition(0);
    }

    private void doFilter(){
        mXJPathListAdapter.notifyDataSetChanged();
        presenterRouter.create(XJPathAPI.class).getXJPathList(queryParam);
    }

    /**
     *@author zhangwenshuai1
     *@date 2018/4/9
     *@description 选择相应时间段任务进入
     *
     */
    private void showOptionPath(String barcode,List<XJPathEntity> xjTaskSameList){
        List<String> xjNameList = new ArrayList<>();
        for (XJPathEntity xjPathEntity : xjTaskSameList ){
            xjNameList.add(xjPathEntity.getStartTime());
        }
        new SinglePickController<String>(this).list(xjNameList).listener((index, item) -> {
            /*XJAreaEntity xjAreaEntity = EamApplication.dao().getXJAreaEntityDao().queryBuilder().where(XJAreaEntityDao.Properties.SignCode.eq(barcode)
                    ,XJAreaEntityDao.Properties.TaskId.eq(xjTaskSameList.get(index).id)).list().get(0);*/
            QueryBuilder queryBuilder = EamApplication.dao().getXJAreaEntityDao().queryBuilder().where(XJAreaEntityDao.Properties.SignCode.eq(barcode)
                    ,XJAreaEntityDao.Properties.TaskId.eq(xjTaskSameList.get(index).id),XJAreaEntityDao.Properties.Ip.eq(EamApplication.getIp()));
            queryBuilder.LOG_SQL = true;
            queryBuilder.LOG_VALUES = true;
            XJAreaEntity xjAreaEntity = (XJAreaEntity) queryBuilder.list().get(0);

            updateXJAreaEntity(xjAreaEntity);

            goArea(xjAreaEntity);

        }).show();

    }

    /**
     *@author zhangwenshuai1
     *@date 2018/4/9
     *@description 手工签到原因上拉菜单
     *
     */
    private void showSignReason(XJAreaEntity xjAreaEntity){
        if (xjAreaEntity.isSign){
            goArea(xjAreaEntity);
        }else {
            if(cartReasonList.size() <= 0){
                return;
            }
            new SinglePickController<String>(this).list(cartReasonList).listener((index, item) -> {
                xjAreaEntity.signReason = cartReasonInfoList.get(index).id;
                xjAreaEntity.isSign = true;
                xjAreaEntity.signType = "cardType/02";
                xjAreaEntity.signedTime = DateUtil.DateToString(new Date(),"yyyy-MM-dd HH:mm:ss");
                EamApplication.dao().getXJAreaEntityDao().update(xjAreaEntity);

                goArea(xjAreaEntity);

            }).show();
        }

    }

    /**
     * @description update巡检区域数据
     * @param
     * @author zhangwenshuai1
     * @date 2018/6/15
     *
     */
    private void updateXJAreaEntity(XJAreaEntity xjAreaEntity){
        xjAreaEntity.signType = "cardType/01";
        xjAreaEntity.isSign = true;
        xjAreaEntity.signedTime = DateUtil.DateToString(new Date(),"yyyy-MM-dd HH:mm:ss");
        EamApplication.dao().getXJAreaEntityDao().update(xjAreaEntity);
        Log.i("XJArea:update", xjAreaEntity.toString());
    }


    /**
     * @description 跳转巡检项列表
     * @param
     * @author zhangwenshuai1
     * @date 2018/6/15
     *
     */
    private void goArea(XJAreaEntity xjAreaEntity) {
        Log.i("XJArea:", xjAreaEntity.toString());

        Bundle bundle = new Bundle();
        String status = listStateFilter.getCurrentItem();
        bundle.putSerializable(Constant.IntentKey.XJAREA_ENTITY, xjAreaEntity);
        bundle.putString(Constant.IntentKey.XJPATH_STATUS, status);
        IntentRouter.go(context, Constant.Router.XJITEM_LIST_UNHANDLED,bundle);
    }



}
