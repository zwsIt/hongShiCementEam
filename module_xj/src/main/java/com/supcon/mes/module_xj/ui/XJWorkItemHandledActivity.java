package com.supcon.mes.module_xj.ui;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
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
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.XJAreaEntity;
import com.supcon.mes.middleware.model.bean.XJExemptionEntity;
import com.supcon.mes.middleware.model.bean.XJExemptionEntityDao;
import com.supcon.mes.middleware.model.bean.XJPathEntity;
import com.supcon.mes.middleware.model.bean.XJPathEntityDao;
import com.supcon.mes.middleware.model.bean.XJWorkItemEntity;
import com.supcon.mes.middleware.model.bean.XJWorkItemEntityDao;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.FaultPicHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_xj.IntentRouter;
import com.supcon.mes.module_xj.R;
import com.supcon.mes.module_xj.constant.XJConstant;
import com.supcon.mes.module_xj.controller.XJCameraController;
import com.supcon.mes.module_xj.model.api.XJWorkItemAPI;
import com.supcon.mes.module_xj.model.bean.XJWorkItemListEntity;
import com.supcon.mes.module_xj.model.contract.XJWorkItemContract;
import com.supcon.mes.module_xj.presenter.XJWorkItemPresenter;
import com.supcon.mes.module_xj.ui.adapter.XJWorkItemListNewAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.query.QueryBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by wangshizhan on 2018/3/27.
 * Email:wangshizhan@supcon.com
 */
@Router(Constant.Router.XJITEM_LIST_HANDLED)
@Presenter(value = {XJWorkItemPresenter.class})
@Controller(value = XJCameraController.class)
public class XJWorkItemHandledActivity extends BaseRefreshRecyclerActivity<XJWorkItemEntity> implements XJWorkItemContract.View {

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


    private XJAreaEntity mXJAreaEntity;
    private XJWorkItemListNewAdapter mXJWorkItemListNewAdapter;
    private List<XJWorkItemEntity> xjWorkItemEntities = new ArrayList<>();

    String mFilterDeviceName = null;
    String deviceName = null;

    @BindByTag("contentView")
    RecyclerView contentView;

    @Override
    protected IListAdapter createAdapter() {
        mXJWorkItemListNewAdapter = new XJWorkItemListNewAdapter(context);
        return mXJWorkItemListNewAdapter;
    }


    @Override
    protected int getLayoutID() {
        return R.layout.ac_work_item_handled;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(false);
        mXJAreaEntity = (XJAreaEntity) getIntent().getSerializableExtra(Constant.IntentKey.XJAREA_ENTITY);
    }

    @Override
    protected void initView() {
        super.initView();
        titleText.setText("已完成巡检项");
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);

        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(5, context)));

        initEmptyView();

        initFilterView();

    }

    @SuppressLint("CheckResult")
    private void initFilterView() {
        List<FilterBean> filterBeans = new ArrayList<>();
        FilterBean filterBean;
//        filterBean.name = "不限";
//        filterBean.type = CustomFilterView.VIEW_TYPE_ALL;
//        filterBeans.add(filterBean);

        Cursor cursor = EamApplication.dao().getDatabase().rawQuery("select EQUIPMENT_NAME from XJWORK_ITEM_ENTITY where TASK_ID = ? and AREA_ID = ? and IP = ? group by EQUIPMENT_NAME order by EQUIPMENT_NAME ",
                new String[]{mXJAreaEntity.taskId.toString(), mXJAreaEntity.areaId.toString(), EamApplication.getIp()});

        try {
            if (cursor.moveToFirst()) {
                do {
                    filterBean = new FilterBean();
                    filterBean.name = cursor.getString(0);
                    filterBeans.add(filterBean);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        listDeviceFilter.setData(filterBeans);
        if (filterBeans.size() > 0) {
            listDeviceFilter.setCurrentItem(filterBeans.get(0));//默认第一项
            deviceName = filterBeans.get(0).name;
        }


//        Flowable.fromIterable(xjWorkItemEntities)
//                .subscribe(new Consumer<XJWorkItemEntity>() {
//                    @Override
//                    public void accept(XJWorkItemEntity workItemEntity) throws Exception {
//                        if (mFilterDeviceName == null && !TextUtils.isEmpty(workItemEntity.equipmentName) || mFilterDeviceName != null && !TextUtils.isEmpty(workItemEntity.equipmentName) && !mFilterDeviceName.equals(workItemEntity.equipmentName)) {
//                            mFilterDeviceName = workItemEntity.equipmentName;
//                            FilterBean filterBean = new FilterBean();
//                            filterBean.name = workItemEntity.equipmentName;
//                            filterBeans.add(filterBean);
//                        }
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//
//                    }
//                }, new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        listDeviceFilter.setData(filterBeans);
//                    }
//                });
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
            //TODO...
            presenterRouter.create(XJWorkItemAPI.class).getXJWorkItemList(mXJAreaEntity.areaId, mXJAreaEntity.taskId, TextUtils.isEmpty(deviceName) ? "" : deviceName, true);
        });

        mXJWorkItemListNewAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            XJWorkItemEntity xjWorkItemEntity;
            int imgIndex;
            if (obj instanceof Integer) {
                imgIndex = (int) obj;
                xjWorkItemEntity = null;
            } else {
                xjWorkItemEntity = (XJWorkItemEntity) obj;
                imgIndex = 0;

            }

            String tag = (String) childView.getTag();
            switch (tag) {

                case "fReRecordBtn":

                    if (!xjWorkItemEntity.control || XJConstant.MobileWiLinkState.EXEMPTION_STATE.equals(xjWorkItemEntity.linkState)) {  //禁修改(结论不可修改或免检)

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
                                        doRerecord(xjWorkItemEntity, EamApplication.dao().getXJWorkItemEntityDao());

                                        EventBus.getDefault().post(new RefreshEvent(Constant.RefreshAction.XJ_WORK_REINPUT, position));

                                        //TODO...判断任务状态是否修改为待检
                                        modifyTaskStatus(xjWorkItemEntity.taskId);

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

                        if (mXJWorkItemListNewAdapter.getList().size() <= 0) {
                            ToastUtils.show(context, "当前设备条件下暂无数据重录！");
                            return;
                        }

                        XJWorkItemEntityDao xjWorkItemEntityDao = EamApplication.dao().getXJWorkItemEntityDao();
//                        List<XJWorkItemEntity> xjWorkItemEntityList = xjWorkItemEntityDao.queryBuilder()
//                                .where(XJWorkItemEntityDao.Properties.TaskId.eq(mXJAreaEntity.taskId), XJWorkItemEntityDao.Properties.AreaId.eq(mXJAreaEntity.areaId), XJWorkItemEntityDao.Properties.Control.eq(true),
//                                        XJWorkItemEntityDao.Properties.IsFinished.eq(true), XJWorkItemEntityDao.Properties.Ip.eq(EamApplication.getIp()))
//                                .where(TextUtils.isEmpty(deviceName) ? :XJWorkItemEntityDao.Properties.EquipmentName.eq(deviceName))
//                                .orderAsc(XJWorkItemEntityDao.Properties.ItemOrder, XJWorkItemEntityDao.Properties.Part)
//                                .list();

                        List<XJWorkItemEntity> xjWorkItemEntityList = new ArrayList<>();// 允许重录的数据
                        Flowable.fromIterable(mXJWorkItemListNewAdapter.getList())
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
                                                        for (XJWorkItemEntity xjWorkItemEntity : xjWorkItemEntityList) {
                                                            doRerecord(xjWorkItemEntity, xjWorkItemEntityDao);
                                                        }
                                                        EventBus.getDefault().post(new RefreshEvent());
                                                        //TODO...判断任务状态是否修改为待检
                                                        modifyTaskStatus(mXJAreaEntity.taskId);
                                                        ToastUtils.show(context, "处理成功！");
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

    /**
     * @param
     * @return
     * @description 判断任务状态是否修改为待检
     * @author zhangwenshuai1 2019/1/18
     */
    private void modifyTaskStatus(Long taskId) {
        XJPathEntity xjPathEntity = EamApplication.dao().getXJPathEntityDao().queryBuilder()
                .where(XJPathEntityDao.Properties.Id.eq(taskId), XJPathEntityDao.Properties.Ip.eq(EamApplication.getIp()))
                .list().get(0);
        if (Constant.XJPathStateType.PAST_CHECK_STATE.equals(xjPathEntity.state)) {
            xjPathEntity.state = Constant.XJPathStateType.WAIT_CHECK_STATE;
            EamApplication.dao().getXJPathEntityDao().update(xjPathEntity);
        }
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
        mXJWorkItemListNewAdapter.notifyItemRemoved(pos);
    }

    /**
     * @param
     * @param xjWorkItemEntityDao
     * @return
     * @description 一键重录巡检项：重录允许重录的巡检项，但是若被免检的巡检项不允许重录时，依然会被重录
     * @author zhangwenshuai1 2018/12/29
     */
    private void doRerecord(XJWorkItemEntity xjWorkItemEntity, XJWorkItemEntityDao xjWorkItemEntityDao) {
        xjWorkItemEntity.isFinished = false;
        xjWorkItemEntity.endTime = null;
        xjWorkItemEntity.linkState = XJConstant.MobileWiLinkState.WAIT_STATE;
        xjWorkItemEntity.realIsPass = 0;
        xjWorkItemEntity.skipReasonID = null;
        xjWorkItemEntity.skipReasonName = null;

        xjWorkItemEntityDao.update(xjWorkItemEntity);

//        doDeleteEditYh(xjWorkItemEntity);

        //TODO...免检项还原
        if (XJConstant.MobileEditType.WHETHER.equals(xjWorkItemEntity.editType) || XJConstant.MobileEditType.RADIO.equals(xjWorkItemEntity.editType)) {
            QueryBuilder queryBuilder = EamApplication.dao().getXJExemptionEntityDao().queryBuilder();
            queryBuilder.where(XJExemptionEntityDao.Properties.ItemId.eq(xjWorkItemEntity.itemId), XJExemptionEntityDao.Properties.Result.eq(TextUtils.isEmpty(xjWorkItemEntity.result) ? "" : xjWorkItemEntity.result),
                    XJExemptionEntityDao.Properties.Ip.eq(EamApplication.getIp()));
            List<XJExemptionEntity> xjExemptionEntities = queryBuilder.list();
            queryBuilder.LOG_SQL = true;
            queryBuilder.LOG_VALUES = true;

            for (XJExemptionEntity xjExemptionEntity : xjExemptionEntities) {
                for (XJWorkItemEntity xjWorkItemEntity1 : mXJWorkItemListNewAdapter.getList()) {
                    if (xjExemptionEntity.exemptionItemId.equals(xjWorkItemEntity1.itemId) && xjWorkItemEntity1.isFinished) {
                        xjWorkItemEntity1.linkState = XJConstant.MobileWiLinkState.WAIT_STATE;
                        xjWorkItemEntity1.isFinished = false;
                        xjWorkItemEntity1.endTime = null;
                        xjWorkItemEntityDao.update(xjWorkItemEntity1);

//                        mXJHandledItemFragment.getXjWorkItemEntities().remove(xjWorkItemEntity1);
                        break;
                    }
                }
            }
        }

    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(new RefreshEvent());
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


    @SuppressLint("CheckResult")
    @Override
    public void getXJWorkItemListSuccess(XJWorkItemListEntity entity) {

        xjWorkItemEntities = entity.result;
        refreshListController.refreshComplete(xjWorkItemEntities);

//        Flowable.fromIterable(entity.result)
//                .compose(RxSchedulers.io_main())
//                .filter(new Predicate<XJWorkItemEntity>() {
//                    @Override
//                    public boolean test(XJWorkItemEntity xjWorkItemEntity) throws Exception {
//                        return xjWorkItemEntity.isFinished;
//                    }
//                })
//                .subscribe(xjWorkItemEntity -> xjWorkItemEntities.add(xjWorkItemEntity), throwable -> {
//                }, () -> {
////                    refreshListController.refreshComplete(xjWorkItemEntities);
//                    initFilterView();
//                });

//        Set<String> set = new HashSet<>();
//
//        Flowable.fromIterable(xjWorkItemEntities)
//                .compose(RxSchedulers.io_main())
//                .filter(xjWorkItemEntity -> {
//                    if (!TextUtils.isEmpty(xjWorkItemEntity.equipmentName)){
//                        return true;
//                    }
//                    return false;
//                })
//                .subscribe(xjWorkItemEntity -> set.add(xjWorkItemEntity.equipmentName), throwable -> {
//
//                }, () -> {
//                    List<FilterBean> filterBeanList = new ArrayList<>();
//                    FilterBean filterBean;
//                    for (String name : set){
//                        filterBean = new FilterBean();
//                        filterBean.name = name;
//                        filterBeanList.add(filterBean);
//                    }
//                    listDeviceFilter.setData(filterBeanList);
//                });

    }


    @Override
    public void getXJWorkItemListFailed(String errorMsg) {
//        LogUtil.e(ErrorMsgHelper.msgParse(errorMsg));
        SnackbarHelper.showError(rootView, errorMsg);
    }
}