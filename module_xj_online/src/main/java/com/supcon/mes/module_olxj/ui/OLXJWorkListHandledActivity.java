package com.supcon.mes.module_olxj.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.com_http.util.RxSchedulers;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.beans.FilterBean;
import com.supcon.mes.mbap.beans.GalleryBean;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomFilterView;
import com.supcon.mes.mbap.view.CustomGalleryView;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.FaultPicHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_olxj.IntentRouter;
import com.supcon.mes.module_olxj.R;
import com.supcon.mes.module_olxj.constant.OLXJConstant;
import com.supcon.mes.module_olxj.controller.OLXJCameraController;
import com.supcon.mes.module_olxj.model.bean.OLXJAreaEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJWorkItemEntity;
import com.supcon.mes.module_olxj.ui.adapter.OLXJWorkListFinishAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wangshizhan on 2018/3/27.
 * Email:wangshizhan@supcon.com
 */
@Router(Constant.Router.OLXJ_WORK_LIST_HANDLED)
@Controller(value = OLXJCameraController.class)
public class OLXJWorkListHandledActivity extends BaseRefreshRecyclerActivity<OLXJWorkItemEntity>{

    @BindByTag("leftBtn")
    CustomImageButton leftBtn;

    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("titleLayout")
    RelativeLayout titleLayout;

    @BindByTag("handledFilterLayout")
    RelativeLayout handledFilterLayout;

    @BindByTag("listDeviceFilter")
    CustomFilterView<FilterBean> listDeviceFilter;

    @BindByTag("oneKeySubmitBtn")
    TextView oneKeySubmitBtn;


    private OLXJAreaEntity mXJAreaEntity;
    private OLXJWorkListFinishAdapter mOLXJWorkListAdapter;
    private List<OLXJWorkItemEntity> xjWorkItemEntities = new ArrayList<>();

    String mFilterDeviceName = null;
    String deviceName = null;

    @BindByTag("contentView")
    RecyclerView contentView;

    private boolean isXJFinished = false;

    @Override
    protected IListAdapter createAdapter() {
        mOLXJWorkListAdapter = new OLXJWorkListFinishAdapter(context, getIntent().getBooleanExtra(Constant.IntentKey.IS_XJ_FINISHED, false));
        return mOLXJWorkListAdapter;
    }


    @Override
    protected int getLayoutID() {
        return R.layout.ac_olxj_work_item_handled;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(false);
        mXJAreaEntity = (OLXJAreaEntity) getIntent().getSerializableExtra(Constant.IntentKey.XJ_AREA_ENTITY);
        isXJFinished = getIntent().getBooleanExtra(Constant.IntentKey.IS_XJ_FINISHED, false);
    }

    @Override
    protected void initView() {
        super.initView();
        titleText.setText("已完成巡检项");
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);

        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(5, context)));

        initEmptyView();

//        initFilterView();
        if(isXJFinished){
            oneKeySubmitBtn.setVisibility(View.GONE);
        }
    }

    @SuppressLint("CheckResult")
    private void initFilterView() {
        List<FilterBean> filterBeans = new ArrayList<>();
        FilterBean filterBean;

        listDeviceFilter.setData(filterBeans);
        if (filterBeans.size() > 0) {
            listDeviceFilter.setCurrentItem(filterBeans.get(0));//默认第一项
            deviceName = filterBeans.get(0).name;
        }


        Flowable.fromIterable(xjWorkItemEntities)
                .subscribe(new Consumer<OLXJWorkItemEntity>() {
                    @Override
                    public void accept(OLXJWorkItemEntity workItemEntity) throws Exception {
                        if (mFilterDeviceName == null &&workItemEntity.eamID!=null && !TextUtils.isEmpty(workItemEntity.eamID.name) || mFilterDeviceName != null
                                && !TextUtils.isEmpty(workItemEntity.eamID.name) && !mFilterDeviceName.equals(workItemEntity.eamID.name)) {
                            mFilterDeviceName = workItemEntity.eamID.name;
                            FilterBean filterBean = new FilterBean();
                            filterBean.name = workItemEntity.eamID.name;
                            filterBeans.add(filterBean);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        listDeviceFilter.setData(filterBeans);
                    }
                });
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> onBackPressed());

        listDeviceFilter.setFilterSelectChangedListener((CustomFilterView.FilterSelectChangedListener) filterBean -> {
            deviceName = filterBean.name;
            if (filterBean.type == CustomFilterView.VIEW_TYPE_ALL) {
                deviceName = null;
            }
            refreshListController.refreshBegin();
        });

        refreshListController.setOnRefreshListener(() -> {
            doRefresh();
        });

        mOLXJWorkListAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            OLXJWorkItemEntity xjWorkItemEntity;
            int imgIndex;
            if (obj instanceof Integer) {
                imgIndex = (int) obj;
                xjWorkItemEntity = null;
            } else {
                xjWorkItemEntity = (OLXJWorkItemEntity) obj;
                imgIndex = 0;

            }

            String tag = (String) childView.getTag();
            switch (tag) {

                case "fReRecordBtn":

                    if (!xjWorkItemEntity.control || OLXJConstant.MobileWiLinkState.EXEMPTION_STATE.equals(xjWorkItemEntity.linkState)) {  //禁修改(结论不可修改或免检)

                        SnackbarHelper.showMessage(rootView, "该巡检项不允许重录");

                    } else {
                        new CustomDialog(context)
                                .twoButtonAlertDialog("是否重录数据？")
                                .bindView(R.id.redBtn, "是")
                                .bindView(R.id.grayBtn, "否")
                                .bindClickListener(R.id.grayBtn, v -> {
                                    //TODO
                                }, true)
                                .bindClickListener(R.id.redBtn, v -> {

                                    try {
                                        doRerecord(xjWorkItemEntity);

                                        EventBus.getDefault().post(new RefreshEvent(Constant.RefreshAction.XJ_WORK_REINPUT, position));

                                        ToastUtils.show(context, "处理成功!");
                                    } catch (Exception e) {
                                        ToastUtils.show(context, "处理失败：" + e.getMessage());
                                        e.printStackTrace();
                                    }

                                }, true)
                                .show();
                    }

                    break;

                case "fItemPics":

                    if (action == CustomGalleryView.ACTION_VIEW) {
                        CustomGalleryView customGalleryView = (CustomGalleryView) childView;
                        List<GalleryBean> galleryBeanList = customGalleryView.getGalleryAdapter().getList();

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("images", (Serializable) FaultPicHelper.getImagePathList(galleryBeanList));
                        bundle.putInt("position", imgIndex);  //点击位置索引

                        int[] location = new int[2];
                        childView.getLocationOnScreen(location);  //点击图片的位置
                        bundle.putInt("locationX", location[0]);
                        bundle.putInt("locationY", location[1]);

                        bundle.putInt("width", DisplayUtil.dip2px(100, context));//必须
                        bundle.putInt("height", DisplayUtil.dip2px(100, context));//必须
                        getWindow().setWindowAnimations(R.style.fadeStyle);
                        IntentRouter.go(context, Constant.Router.IMAGE_VIEW, bundle);
                    }

                    break;
            }

        });

        RxView.clicks(oneKeySubmitBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {

                        if (mOLXJWorkListAdapter.getList().size() <= 0) {
                            ToastUtils.show(context, "当前设备条件下暂无数据重录！");
                            return;
                        }

                        List<OLXJWorkItemEntity> xjWorkItemEntityList = new ArrayList<>();// 允许重录的数据
                        Flowable.fromIterable(mOLXJWorkListAdapter.getList())
                                .compose(RxSchedulers.io_main())
                                .filter(xjWorkItemEntity -> xjWorkItemEntity.control)
                                .subscribe(xjWorkItemEntity -> xjWorkItemEntityList.add(xjWorkItemEntity), throwable -> {

                                }, new Action() {
                                    @Override
                                    public void run() throws Exception {
                                        if (xjWorkItemEntityList.size() <= 0) {
                                            ToastUtils.show(context, "当前设备条件下系统巡检项设置不允许重录！");
                                            return;
                                        }
                                        new CustomDialog(context)
                                                .twoButtonAlertDialog("是否一键重录巡检项(仅可重录系统允许重录的巡检项)？")
                                                .bindView(R.id.grayBtn, "否")
                                                .bindView(R.id.redBtn, "是")
                                                .bindClickListener(R.id.grayBtn, null, true)
                                                .bindClickListener(R.id.redBtn, v -> {
                                                    try {
                                                        for (OLXJWorkItemEntity xjWorkItemEntity : xjWorkItemEntityList) {
                                                            doRerecord(xjWorkItemEntity);
                                                        }
                                                        EventBus.getDefault().post(mXJAreaEntity);
                                                        ToastUtils.show(context, "处理成功！");
                                                        finish();
                                                    } catch (Exception e) {
                                                        ToastUtils.show(context, "处理失败： " + e.getMessage());
                                                        e.printStackTrace();
                                                    } finally {
                                                    }
                                                }, true)
                                                .show();
                                    }
                                });
                    }
                });


    }

    @SuppressLint("CheckResult")
    private void doRefresh() {
        List<OLXJWorkItemEntity> workItems = new ArrayList<>();
        Flowable.fromIterable(mXJAreaEntity.workItemEntities)
                .subscribeOn(Schedulers.newThread())
                .filter(new Predicate<OLXJWorkItemEntity>() {
                    @Override
                    public boolean test(OLXJWorkItemEntity olxjWorkItemEntity) throws Exception {
                        if(!TextUtils.isEmpty(deviceName) && olxjWorkItemEntity.eamID!=null){
                            return olxjWorkItemEntity.isFinished && deviceName.equals(olxjWorkItemEntity.eamID.name);
                        }
                        return olxjWorkItemEntity.isFinished ;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<OLXJWorkItemEntity>() {
                    @Override
                    public void accept(OLXJWorkItemEntity olxjWorkItemEntity) throws Exception {
                        workItems.add(olxjWorkItemEntity);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        refreshListController.refreshComplete(workItems);
                        initFilterView(mXJAreaEntity.workItemEntities);
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void initFilterView(List<OLXJWorkItemEntity> itemEntities) {
        List<FilterBean> filterBeans = new ArrayList<>();
        Flowable.fromIterable(itemEntities)
                .subscribe(new Consumer<OLXJWorkItemEntity>() {
                    @Override
                    public void accept(OLXJWorkItemEntity workItemEntity) throws Exception {
                        if(workItemEntity.eamID!=null && !TextUtils.isEmpty(workItemEntity.eamID.name))
                        if (mFilterDeviceName == null ||  !mFilterDeviceName.equals(workItemEntity.eamID.name)) {
                            mFilterDeviceName = workItemEntity.eamID.name;
                            FilterBean filterBean = new FilterBean();
                            filterBean.name = workItemEntity.eamID.name;
                            filterBeans.add(filterBean);
                        }
                    }
                }, throwable -> {
                }, () -> listDeviceFilter.setData(filterBeans));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(RefreshEvent refreshEvent) {
        refreshListController.refreshBegin();
    }


    public void removeItem(int pos) {
        mOLXJWorkListAdapter.notifyItemRemoved(pos);
    }

    /**
     * @param
     * @return
     * @description 一键重录巡检项：重录允许重录的巡检项，但是若被免检的巡检项不允许重录时，依然会被重录
     * @author zhangwenshuai1 2018/12/29
     */
    private void doRerecord(OLXJWorkItemEntity xjWorkItemEntity) {
        xjWorkItemEntity.isFinished = false;
        xjWorkItemEntity.endTime = null;
        xjWorkItemEntity.linkState = OLXJConstant.MobileWiLinkState.WAIT_STATE;
        xjWorkItemEntity.realispass = false;
        xjWorkItemEntity.skipReasonID = null;
        xjWorkItemEntity.skipReasonName = null;

    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(mXJAreaEntity);
        finish();
    }

    /**
     * @author zhangwenshuai1
     * @date 2018/4/9
     * @description 初始化无数据
     */
    private void initEmptyView() {
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, ""));

    }
}