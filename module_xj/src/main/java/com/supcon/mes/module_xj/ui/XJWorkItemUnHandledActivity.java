package com.supcon.mes.module_xj.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.com_http.util.RxSchedulers;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.beans.FilterBean;
import com.supcon.mes.mbap.beans.SheetEntity;
import com.supcon.mes.mbap.constant.ListType;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomFilterView;
import com.supcon.mes.mbap.view.CustomGalleryView;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.mbap.view.CustomSheetDialog;
import com.supcon.mes.mbap.view.CustomSpinner;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.XJAreaEntity;
import com.supcon.mes.middleware.model.bean.XJExemptionEntity;
import com.supcon.mes.middleware.model.bean.XJHistoryEntity;
import com.supcon.mes.middleware.model.bean.XJHistoryEntityDao;
import com.supcon.mes.middleware.model.bean.XJWorkItemEntity;
import com.supcon.mes.middleware.model.bean.XJWorkItemEntityDao;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.event.ThermometerEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.SystemCodeManager;
import com.supcon.mes.module_xj.IntentRouter;
import com.supcon.mes.module_xj.R;
import com.supcon.mes.module_xj.constant.XJConstant;
import com.supcon.mes.module_xj.controller.TranslucentController;
import com.supcon.mes.module_xj.controller.XJCameraController;
import com.supcon.mes.module_xj.model.api.XJWorkItemAPI;
import com.supcon.mes.module_xj.model.bean.XJHistorySheetEntity;
import com.supcon.mes.module_xj.model.bean.XJWorkItemListEntity;
import com.supcon.mes.module_xj.model.contract.XJWorkItemContract;
import com.supcon.mes.module_xj.presenter.XJWorkItemPresenter;
import com.supcon.mes.module_xj.ui.adapter.XJHistorySheetAdapter;
import com.supcon.mes.module_xj.ui.adapter.XJWorkItemListNewAdapter;
import com.supcon.mes.viber_mogu.controller.MGViberController;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * Created by wangshizhan on 2019/1/16
 * Email:wangshizhan@supcom.com
 */
@Router(Constant.Router.XJITEM_LIST_UNHANDLED)
@Presenter(value = {XJWorkItemPresenter.class})
@Controller(value = {TranslucentController.class, XJCameraController.class})
public class XJWorkItemUnHandledActivity extends BaseRefreshRecyclerActivity<XJWorkItemEntity> implements XJWorkItemContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("leftBtn")
    CustomImageButton leftBtn;

    @BindByTag("rightBtn")
    CustomImageButton rightBtn;

    @BindByTag("listDeviceFilter")
    CustomFilterView<FilterBean> listDeviceFilter;

    @BindByTag("faultListBtn")
    TextView faultListBtn;

    @BindByTag("oneKeySubmitBtn")
    TextView oneKeySubmitBtn;

    @BindByTag("xjBtnLayout")
    LinearLayout xjBtnLayout;

    @BindByTag("finishedBottomBtn")
    TextView finishedBottomBtn;

    @BindByTag("faultListBottomBtn")
    TextView faultListBottomBtn;

    @BindByTag("oneKeySubmitBottomBtn")
    TextView oneKeySubmitBottomBtn;


    XJWorkItemListNewAdapter mXJWorkItemListNewAdapter;
    private SinglePickController<String> mSinglePickController;
    private XJAreaEntity mXJAreaEntity;
    private RefreshEvent mRefreshEvent;


    private List<XJWorkItemEntity> xjUfWorkItemEntities = new ArrayList<>();

    private List<String> resultList = new ArrayList<>();  //结果列表
    private List<SystemCodeEntity> conclusionList = new ArrayList<>(); //结论列表
    private List<String> conclusionStrList = new ArrayList<>(); //结论列表Str
    private List<SystemCodeEntity> passReasonList = new ArrayList<>(); //跳过原因
    private List<String> passReasonStrList = new ArrayList<>(); //跳过原因Str

    private String thermometervalue = ""; // 全局测温值

    String deviceName = null;

//    private CameraManager cameraManager;
    private XJCameraController mCameraController;

    private int mCameraPosition = -1;

    private CustomGalleryView customGalleryView;
    boolean isSmoothScroll = false;

    String mFilterDeviceName = null;
    private Map<String, Integer> devicePositions = new HashMap<>();

    RecyclerView.SmoothScroller smoothScroller;

    private MGViberController mViberController;
    private CustomDialog mViberDialog;

    @Override
    protected IListAdapter<XJWorkItemEntity> createAdapter() {
        mXJWorkItemListNewAdapter = new XJWorkItemListNewAdapter(context);
        return mXJWorkItemListNewAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_work_item_unhandled;
    }

    @Override
    protected void onInit() {
        super.onInit();
        mXJAreaEntity = (XJAreaEntity) getIntent().getSerializableExtra(Constant.IntentKey.XJAREA_ENTITY);
        EventBus.getDefault().register(this);
        mSinglePickController = new SinglePickController<>(this);
        mSinglePickController.textSize(18);
        mSinglePickController.setCanceledOnTouchOutside(true);

//        cameraManager = new CameraManager(this, Constant.PicType.XJ_PIC);

//        mCameraController = getController(XJCameraController.class);
//        mCameraController.init(Constant.IMAGE_SAVE_XJPATH,Constant.PicType.XJ_PIC);

        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(false);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        deviceName = null; //清空,已完成页面返回后，因为刷新会重新初始化设备过滤条件
        getWindow().setWindowAnimations(R.style.activityAnimation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initView() {
        super.initView();
        titleText.setText(mXJAreaEntity.areaName);
        rightBtn.setText("已完成");
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(1, context)));

        initEmptyView();

    }


    @SuppressLint("CheckResult")
    private void initFilterView() {
        List<FilterBean> filterBeans = new ArrayList<>();
//        FilterBean filterBean = new FilterBean();
//        filterBean.name = "不限";
//        filterBean.type = CustomFilterView.VIEW_TYPE_ALL;
//        filterBeans.add(filterBean);
        Flowable.fromIterable(mXJWorkItemListNewAdapter.getList())
                .subscribe(new Consumer<XJWorkItemEntity>() {
                    @Override
                    public void accept(XJWorkItemEntity workItemEntity) throws Exception {
                        if (mFilterDeviceName == null && !TextUtils.isEmpty(workItemEntity.title) || mFilterDeviceName != null && !TextUtils.isEmpty(workItemEntity.title) && !mFilterDeviceName.equals(workItemEntity.title)) {
                            mFilterDeviceName = workItemEntity.title;
                            FilterBean filterBean = new FilterBean();
                            filterBean.name = workItemEntity.title;
                            filterBeans.add(filterBean);
                            devicePositions.put(workItemEntity.title, mXJWorkItemListNewAdapter.getList().indexOf(workItemEntity));
                        }
                    }
                }, throwable -> {
                }, () -> listDeviceFilter.setData(filterBeans));
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        smoothScroller = new LinearSmoothScroller(this) {

            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;

            }

        };

        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> onBackPressed());

        RxView.clicks(rightBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> goFinishedXJActivity());

        RxView.clicks(finishedBottomBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> goFinishedXJActivity());

        RxView.clicks(faultListBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> IntentRouter.go(context, Constant.Router.OFFLINE_YH_LIST));

        RxView.clicks(faultListBottomBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> IntentRouter.go(context, Constant.Router.OFFLINE_YH_LIST));

        RxView.clicks(oneKeySubmitBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> showOneKeyDialog());

        RxView.clicks(oneKeySubmitBottomBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> showOneKeyDialog());

        listDeviceFilter.setFilterSelectChangedListener((CustomFilterView.FilterSelectChangedListener) filterBean -> {
            deviceName = filterBean.name;
            int position = devicePositions.get(deviceName);
//                contentView.smoothScrollToPosition(position);
            LinearLayoutManager mManager = (LinearLayoutManager) contentView.getLayoutManager();

            smoothScroller.setTargetPosition(position);

            mManager.startSmoothScroll(smoothScroller);

            isSmoothScroll = true;
        });

        contentView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            contentView.scrollBy(0, -DisplayUtil.dip2px(110, context));
//                        }
//                    }, 200);

                    if (isSmoothScroll) {
                        contentView.smoothScrollBy(0, -DisplayUtil.dip2px(110, context));
                        isSmoothScroll = false;
                    }
                }
            }
        });


        refreshListController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });


        mXJWorkItemListNewAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {

            XJWorkItemEntity xjWorkItemEntity;
//            int imgIndex;

            if (obj instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) obj;
//                imgIndex = (int) map.get("imgIndex");
                xjWorkItemEntity = (XJWorkItemEntity) map.get("obj");

            } else {
                xjWorkItemEntity = (XJWorkItemEntity) obj;
//                imgIndex = 0;
            }

            String tag = (String) childView.getTag();

            switch (tag) {
                case "vibrationBtn":
                    showViberDialog(position, xjWorkItemEntity);

                    break;
                case "ufItemSelectResult":
                    if (action == -1) {
                        xjWorkItemEntity.result = null;
                    } else {
                        if (XJConstant.MobileEditType.CHECKBOX.equals(xjWorkItemEntity.editType)) {//多选
                            dialogMoreChoice(xjWorkItemEntity, position);
                        } else {
                            showResultPicker(((CustomSpinner) childView).getSpinnerValue(), xjWorkItemEntity, position);
                        }
                    }

                    break;
                case "ufItemConclusion":
                    if (action == -1) {
                        xjWorkItemEntity.conclusionID = null;
                        xjWorkItemEntity.conclusionName = null;
                    } else {
                        showConclusionPicker(xjWorkItemEntity, position);
                    }

                    break;

                case "ufItemSkipBtn":
                    if (!xjWorkItemEntity.isPass) {
                        SnackbarHelper.showError(rootView, "该巡检项不允许跳过");
                    } else {
                        showSkipReasonPicker(xjWorkItemEntity);
                    }
                    break;

                case "ufItemEndBtn":
                    break;

                case "thermometerBtn":
                    xjWorkItemEntity.result = thermometervalue;
                    mXJWorkItemListNewAdapter.notifyItemChanged(position);
                    break;

                case "fHistoryBtn":

                    showHistories(xjWorkItemEntity);

                    break;
/*                case "ufItemPics":
                    switch (action) {
                        case CustomGalleryView.ACTION_TAKE_PICTURE_FROM_CAMERA:    //拍照保存
//                            cameraManager.startCamera(Constant.IMAGE_SAVE_XJPATH);
                            mCameraController.startCamera();
                            break;
                        case CustomGalleryView.ACTION_TAKE_PICTURE_FROM_GALLERY:
//                            cameraManager.startGallery(Constant.IMAGE_SAVE_XJPATH);
                            mCameraController.startGallery();
                            break;
                        case CustomGalleryView.ACTION_TAKE_VIDEO_FROM_CAMERA:
                            mCameraController.startVideo();
                            break;
                        case CustomGalleryView.ACTION_DELETE:   //删除
                            GalleryBean galleryBean = customGalleryView.getGalleryAdapter().getItem(imgIndex);
                            new CustomDialog(context)
                                    .twoButtonAlertDialog(galleryBean.fileType == FILE_TYPE_PICTURE?"是否删除图片？":"是否删除视频?")
                                    .bindView(R.id.redBtn, "")
                                    .bindView(R.id.grayBtn, "")
                                    .bindClickListener(R.id.redBtn, v -> {
                                        customGalleryView = (CustomGalleryView) childView;
                                        customGalleryView.deletePic(imgIndex);
                                        List<String> imgNamesList = Arrays.asList(xjWorkItemEntity.xjImgUrl.split(","));
                                        String xjImgUrl = xjWorkItemEntity.xjImgUrl;
                                        String imgName = imgNamesList.get(imgIndex);
//                                        cameraManager.deleteBitmap(Constant.IMAGE_SAVE_XJPATH + imgName);
//                                        cameraManager.deleteBitmap(imgName);
                                        mCameraController.deleteFile(imgName);

                                        if (xjImgUrl.startsWith(imgName)) {
                                            if (xjImgUrl.equals(imgName)) {
                                                xjWorkItemEntity.xjImgUrl = xjImgUrl.replace(imgName, "");
                                            } else {
                                                xjWorkItemEntity.xjImgUrl = xjImgUrl.replace(imgName + ",", "");
                                            }
                                        } else {
                                            xjWorkItemEntity.xjImgUrl = xjImgUrl.replace("," + imgName, "");
                                        }
                                        if (TextUtils.isEmpty(xjWorkItemEntity.xjImgUrl)) {  //实际拍照字段还原
                                            xjWorkItemEntity.realIsPhone = 0;
                                        }

                                        mXJWorkItemListNewAdapter.notifyItemChanged(position);

                                    }, true)
                                    .bindClickListener(R.id.grayBtn, v -> {

                                    }, true)
                                    .show();

                            break;

                        case CustomGalleryView.ACTION_VIEW:    //放大查看

                            customGalleryView = (CustomGalleryView) childView;

                            List<GalleryBean> galleryBeans = customGalleryView.getGalleryAdapter().getList();
                            GalleryBean galleryBean1 = galleryBeans.get(imgIndex);
                            if(galleryBean1.fileType == FILE_TYPE_PICTURE){
                                mCameraController.viewPic((CustomGalleryView) childView, galleryBeans, imgIndex);
                            }
                            else if(galleryBean1.fileType == FILE_TYPE_VIDEO){
                                mCameraController.viewVideo(galleryBean1);
                            }

                            break;
                        default:

                    }

                    mCameraPosition = position;
                    break;*/
                default:
            }

        });

//        mCameraController.setOnSuccessListener(new OnSuccessListener<File>() {
//            @Override
//            public void onSuccess(File result) {
//                XJWorkItemEntity xjWorkItemEntity = mXJWorkItemListNewAdapter.getItem(mCameraPosition);
//                if (TextUtils.isEmpty(xjWorkItemEntity.xjImgUrl)) {
//                    xjWorkItemEntity.xjImgUrl = result.getAbsolutePath();
//                } else {
//                    xjWorkItemEntity.xjImgUrl += ","+result.getAbsolutePath();
//                }
//                if (!TextUtils.isEmpty(xjWorkItemEntity.xjImgUrl)) {  //拍照时才可以修改(拍照但是又X掉，该字段为空)
//                    xjWorkItemEntity.realIsPhone = 1;
//                }
//                mXJWorkItemListNewAdapter.notifyItemChanged(mCameraPosition);
//            }
//        });
    }

    private void showViberDialog(int position, XJWorkItemEntity xjWorkItemEntity) {

        if(mViberDialog == null) {
            mViberDialog = new CustomDialog(context)
                    .layout(R.layout.ly_viber_dialog)
                    .bindClickListener(R.id.viberFinishBtn, null, true);
            mViberController = new MGViberController(mViberDialog.getDialog().getWindow().getDecorView());
            mViberController.onInit();
            mViberController.initView();
            mViberController.initListener();
            mViberController.initData();
        }
        else{
            mViberController.reset();
        }

        mViberDialog.bindClickListener(R.id.viberFinishBtn, v->{

            mViberController.stopTest();
            xjWorkItemEntity.result = mViberController.getData();
            mXJWorkItemListNewAdapter.notifyItemChanged(position);
        }, true).show();

    }

    private void goFinishedXJActivity() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.IntentKey.XJAREA_ENTITY, mXJAreaEntity);
        IntentRouter.go(context, Constant.Router.XJITEM_LIST_HANDLED, bundle);
    }

    @SuppressLint("CheckResult")
    private void showOneKeyDialog() {
        List<XJWorkItemEntity> xjWorkItemEntityList = mXJWorkItemListNewAdapter.getList(); // 通过列表获取可以保证默认值已经回填到结果上
        List<XJWorkItemEntity> xjWorkItemEntityFilterList = new ArrayList<>();
        Flowable.fromIterable(xjWorkItemEntityList)
                .filter(xjWorkItemEntity -> {
                    if (xjWorkItemEntity.viewType == 0) {
                        return true;
                    }
                    return false;
                })
                .subscribe(xjWorkItemEntity -> xjWorkItemEntityFilterList.add(xjWorkItemEntity), throwable -> {

                }, () -> {
                    if (xjWorkItemEntityFilterList.size() <= 0) {
                        ToastUtils.show(context, "列表无巡检数据完成！");
                    } else {
                        XJWorkItemEntityDao xjWorkItemEntityDao = EamApplication.dao().getXJWorkItemEntityDao();
                        //一键完成
                        new CustomDialog(context)
                                .twoButtonAlertDialog("是否一键完成巡检项？")
                                .bindView(R.id.grayBtn, "否")
                                .bindView(R.id.redBtn, "是")
                                .bindClickListener(R.id.grayBtn, null, true)
                                .bindClickListener(R.id.redBtn, v -> {
                                    try {
                                        onLoading("一键完成中...");
//                        List<XJWorkItemEntity> xjWorkItemEntityList = EamApplication.dao().getXJWorkItemEntityDao().queryBuilder()
//                                .where(XJWorkItemEntityDao.Properties.TaskId.eq(mXJAreaEntity.taskId), XJWorkItemEntityDao.Properties.AreaId.eq(mXJAreaEntity.areaId), XJWorkItemEntityDao.Properties.IsFinished.eq(false)
//                                        , XJWorkItemEntityDao.Properties.Ip.eq(EamApplication.getIp()))
//                                .orderAsc(XJWorkItemEntityDao.Properties.EquipmentName, XJWorkItemEntityDao.Properties.ItemOrder, XJWorkItemEntityDao.Properties.Part)
//                                .list();
//                        List<XJWorkItemEntity> xjWorkItemEntityList = mXJWorkItemListNewAdapter.getList(); // 通过列表获取可以保证默认值已经回填到结果上
//                        List<XJWorkItemEntity> xjWorkItemEntityFilterList = new ArrayList<>();
//                        Flowable.fromIterable(xjWorkItemEntityList)
//                                .filter(xjWorkItemEntity -> {
//                                    if (xjWorkItemEntity.viewType == 0){
//                                        return true;
//                                    }
//                                    return false;
//                                })
//                                .subscribe(xjWorkItemEntity -> xjWorkItemEntityFilterList.add(xjWorkItemEntity), throwable -> {
//
//                                }, () -> {
//                                    for (XJWorkItemEntity xjWorkItemEntity : xjWorkItemEntityFilterList) {
//                                        doFinish(xjWorkItemEntity, xjWorkItemEntityDao,xjWorkItemEntityFilterList);
//                                    }
////                        ToastUtils.show(context, "处理成功！");
//                                    onLoadSuccess("完成成功");
//                                    EventBus.getDefault().post(new RefreshEvent());
//                                });
                                        for (XJWorkItemEntity xjWorkItemEntity : xjWorkItemEntityFilterList) {
                                            doFinish(xjWorkItemEntity, xjWorkItemEntityDao, xjWorkItemEntityFilterList);
                                        }
//                        ToastUtils.show(context, "处理成功！");
                                        onLoadSuccess("完成成功");
                                        EventBus.getDefault().post(new RefreshEvent());

                                    } catch (Exception e) {
//                        ToastUtils.show(context, "处理失败： " + e.getMessage());
                                        onLoadFailed("完成操作失败！" + e.getMessage());
                                        e.printStackTrace();
                                    } finally {
                                    }
                                }, true)
                                .show();
                    }
                });

    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void deleteImage(ImageDeleteEvent imageDeleteEvent) {
//        List<String> picStrs = FaultPicHelper.getImagePathList(customGalleryView.getGalleryAdapter().getList());
//        int position = -1;
//        boolean isMatch = false;
//        for (int i = 0; !isMatch && picStrs.size() > 0; i++) {
//            String name = picStrs.get(i);
//            if (name.equals(imageDeleteEvent.getPicName())) {
//                position = picStrs.indexOf(name);
//                isMatch = true;
//            }
//        }
//
//        if (position != -1) {
//            customGalleryView.deletePic(position);
////            cameraManager.deleteBitmap(imageDeleteEvent.getPicName());
//            mCameraController.deleteFile(imageDeleteEvent.getPicName());
//
////            String imgName = imageDeleteEvent.getPicName().substring(imageDeleteEvent.getPicName().lastIndexOf(File.separator) + 1);
//            XJWorkItemEntity xjWorkItemEntity = mXJWorkItemListNewAdapter.getItem(mCameraPosition);
//            String xjImgUrl = xjWorkItemEntity.xjImgUrl;
//            if (xjImgUrl.startsWith(imageDeleteEvent.getPicName())) {
//                if (xjImgUrl.equals(imageDeleteEvent.getPicName())) {
//                    xjWorkItemEntity.xjImgUrl = xjImgUrl.replace(imageDeleteEvent.getPicName(), "");
//                } else {
//                    xjWorkItemEntity.xjImgUrl = xjImgUrl.replace(imageDeleteEvent.getPicName() + ",", "");
//                }
//            } else {
//                xjWorkItemEntity.xjImgUrl = xjImgUrl.replace("," + imageDeleteEvent.getPicName(), "");
//            }
//            if (TextUtils.isEmpty(xjWorkItemEntity.xjImgUrl)) {  //实际拍照字段还原
//                xjWorkItemEntity.realIsPhone = 0;
//            }
//
//            mXJWorkItemListNewAdapter.notifyItemChanged(mCameraPosition);
//
//        }
//    }

    @Override
    protected void initData() {
        super.initData();
        conclusionList = SystemCodeManager.getInstance().getSystemCodeListByCode(Constant.SystemCode.REAL_VALUE);
        passReasonList = SystemCodeManager.getInstance().getSystemCodeListByCode(Constant.SystemCode.PASS_REASON);
        //解析ObjList 到 StrList
        initStrList(conclusionList, passReasonList);
    }

    /**
     * @param
     * @param xjWorkItemEntityDao
     * @param xjWorkItemEntityList
     * @return
     * @description 一键完成巡检项
     * @author zhangwenshuai1 2018/12/29
     */
    private void doFinish(XJWorkItemEntity xjWorkItemEntity, XJWorkItemEntityDao xjWorkItemEntityDao, List<XJWorkItemEntity> xjWorkItemEntityList) {
        if (!XJConstant.MobileWiLinkState.EXEMPTION_STATE.equals(xjWorkItemEntity.linkState) && !xjWorkItemEntity.isFinished) { //免检项过滤掉，因为在后续的循环中被免检的项没有从当前列表中移除
            xjWorkItemEntity.endTime = DateUtil.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
            xjWorkItemEntity.isFinished = true;
            xjWorkItemEntity.linkState = XJConstant.MobileWiLinkState.FINISHED_STATE;
            xjWorkItemEntity.staffId = EamApplication.getAccountInfo().staffId;
            xjWorkItemEntity.result = TextUtils.isEmpty(xjWorkItemEntity.result) ? xjWorkItemEntity.defaultVal : xjWorkItemEntity.result; //若列表无滚动直接一键完成，默认值不会回填到结果
            //处理结论自动判定
            if (xjWorkItemEntity.isAutoJudge){
                xjWorkItemEntity.conclusionName = TextUtils.isEmpty(xjWorkItemEntity.conclusionName) ? "正常" : xjWorkItemEntity.conclusionName;
                xjWorkItemEntity.conclusionID = TextUtils.isEmpty(xjWorkItemEntity.conclusionID) ? "realValue/01" : xjWorkItemEntity.conclusionID;
            }

            xjWorkItemEntityDao.update(xjWorkItemEntity);

        }

        //自动生成隐患单
//        if (XJConstant.MobileConclusion.AB_NORMAL.equals(xjWorkItemEntity.conclusionID)) {
//            doSaveEditYh(xjWorkItemEntity);
//        }


        //TODO 免检逻辑:单选或是否存在免检规则
        /*传入任何SQL片段到WHERE字句*/
        if (XJConstant.MobileEditType.WHETHER.equals(xjWorkItemEntity.editType) || XJConstant.MobileEditType.RADIO.equals(xjWorkItemEntity.editType)) {
            Query query = EamApplication.dao().getXJExemptionEntityDao()
                    .queryRawCreate("WHERE T.ITEM_ID = ? AND T.RESULT = ? AND T.IP = ? ", xjWorkItemEntity.itemId, TextUtils.isEmpty(xjWorkItemEntity.result) ? "" : xjWorkItemEntity.result, EamApplication.getIp());
            List<XJExemptionEntity> xjExemptionEntities = query.list();

//            List<XJWorkItemEntity> xjWorkItemEntityList = mXJWorkItemListNewAdapter.getList();

            for (XJExemptionEntity xjExemptionEntity : xjExemptionEntities) {
                for (XJWorkItemEntity xjWorkItemEntity1 : xjWorkItemEntityList) {
                    if (xjExemptionEntity.exemptionItemId.equals(xjWorkItemEntity1.itemId) && !xjWorkItemEntity1.isFinished) { //避免将自己免检
                        xjWorkItemEntity1.linkState = XJConstant.MobileWiLinkState.EXEMPTION_STATE;
                        xjWorkItemEntity1.isFinished = true;
                        xjWorkItemEntity1.endTime = DateUtil.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
                        xjWorkItemEntity1.staffId = EamApplication.getAccountInfo().staffId;
                        xjWorkItemEntity1.result = null;  // 存在默认值时，需要清空
                        xjWorkItemEntity1.conclusionName = null;
                        xjWorkItemEntity1.conclusionID = null;
                        xjWorkItemEntityDao.update(xjWorkItemEntity1);

                        break;
                    }
                }
            }
        }
    }

    /**
     * @author zhangwenshuai1
     * @date 2018/3/31
     * @description 结果筛选框  单选/是否
     */
    private void showResultPicker(String selectedValue, XJWorkItemEntity xjWorkItemEntity, int position) {

        if (xjWorkItemEntity.candidateValue == null) {
            ToastUtils.show(context, "结果候选值为空！");
            return;
        }
        resultList = Arrays.asList(xjWorkItemEntity.candidateValue.split(","));
        int current = findPosition(selectedValue, resultList);
        mSinglePickController.list(resultList).listener((index, item) -> {
            xjWorkItemEntity.result = resultList.get(index);

            if (xjWorkItemEntity.isAutoJudge && xjWorkItemEntity.normalRange != null) {

                String[] normalRangeArr = xjWorkItemEntity.normalRange.split(",");
                if (Arrays.asList(normalRangeArr).contains(xjWorkItemEntity.result)) {
                    xjWorkItemEntity.conclusionID = "realValue/01";
                    xjWorkItemEntity.conclusionName = "正常";
                } else {
                    xjWorkItemEntity.conclusionID = "realValue/02";
                    xjWorkItemEntity.conclusionName = "异常";
                }

            }

            mXJWorkItemListNewAdapter.notifyItemChanged(position);

        }).show(current);

    }

    /**
     * @description 多选
     * @author zhangwenshuai1
     * @date 2018/5/2
     */
    private void dialogMoreChoice(XJWorkItemEntity xjWorkItemEntity, int xjPosition) {
        if (xjWorkItemEntity.candidateValue == null || xjWorkItemEntity.candidateValue.isEmpty()) {
            SnackbarHelper.showError(rootView, "无结果候选值");
            return;
        }


        String[] items = xjWorkItemEntity.candidateValue == null ? null : xjWorkItemEntity.candidateValue.split(",");  //候选值列表
        List<SheetEntity> list = new ArrayList<>();
        for (String item : items) {
            SheetEntity sheetEntity = new SheetEntity();
            sheetEntity.name = item;
            list.add(sheetEntity);
        }

        List<Boolean> checkedList = new ArrayList<>();
        for (String s : items) {
            if (xjWorkItemEntity.result != null && xjWorkItemEntity.result.contains(s)) {
                checkedList.add(true);
            } else {
                checkedList.add(false);
            }
        }

        new CustomSheetDialog(context)
                .multiSheet("多选列表", list, checkedList)
                .setOnItemChildViewClickListener((childView, position, action, obj) -> {

                    List<SheetEntity> sheetEntities = GsonUtil.jsonToList(obj.toString(), SheetEntity.class);

                    if (sheetEntities != null && sheetEntities.size() > 0) {

                        xjWorkItemEntity.result = "";
                        for (SheetEntity sheetEntity : sheetEntities) {
                            xjWorkItemEntity.result += sheetEntity.name + ",";
                        }

                        xjWorkItemEntity.result = xjWorkItemEntity.result.substring(0, xjWorkItemEntity.result.length() - 1);

                        mXJWorkItemListNewAdapter.notifyItemChanged(xjPosition);

                    }
                }).show();

    }

    /**
     * @author zhangwenshuai1
     * @date 2018/3/29
     * @description 结论筛选框
     */
    private void showConclusionPicker(XJWorkItemEntity xjWorkItemEntity, int position) {
        int current = findPosition(xjWorkItemEntity, conclusionList);
        if (conclusionStrList.size() <= 0) {
            return;
        }
        mSinglePickController.list(conclusionStrList).listener((index, item) -> {
            SystemCodeEntity realValueInfo = conclusionList.get(index);  //两个List的index位置一样
            xjWorkItemEntity.conclusionID = realValueInfo.id;
            xjWorkItemEntity.conclusionName = realValueInfo.value;
            mXJWorkItemListNewAdapter.notifyItemChanged(position);
        }).show(current);

    }


    /**
     * @author zhangwenshuai1
     * @date 2018/4/4
     * @description 跳过原因筛选框
     */
    private void showSkipReasonPicker(XJWorkItemEntity xjWorkItemEntity) {
        if (passReasonStrList.size() <= 0) {
            return;
        }
        mSinglePickController.list(passReasonStrList).listener((index, item) -> {
            SystemCodeEntity passReasonInfo = passReasonList.get(index);
            xjWorkItemEntity.skipReasonID = passReasonInfo.id;
            xjWorkItemEntity.skipReasonName = passReasonInfo.value;
            xjWorkItemEntity.realIsPass = 1;
            xjWorkItemEntity.isFinished = true;
            xjWorkItemEntity.linkState = XJConstant.MobileWiLinkState.SKIP_STATE;//跳检
            xjWorkItemEntity.endTime = DateUtil.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
            xjWorkItemEntity.staffId = EamApplication.getAccountInfo().staffId;
            xjWorkItemEntity.conclusionID = null;
            xjWorkItemEntity.conclusionName = null;
            xjWorkItemEntity.result = null;
            EamApplication.dao().getXJWorkItemEntityDao().update(xjWorkItemEntity);
            EventBus.getDefault().post(new RefreshEvent());
        }).show();
    }

    /**
     * @author zhangwenshuai1
     * @date 2018/4/2
     * @description 选择的位置index
     */
    private int findPosition(String value, List<String> list) {

        for (int i = 0; i < list.size(); i++) {
            if (value.equals(list.get(i))) {
                return i;
            }
        }
        return 0;
    }

    /**
     * @author zhangwenshuai1
     * @date 2018/4/4
     * @description 选择的位置index
     */
    private int findPosition(XJWorkItemEntity xjWorkItemEntity, List<SystemCodeEntity> list) {

        for (int i = 0; i < list.size(); i++) {
            SystemCodeEntity realValueInfo = list.get(i);
            if (xjWorkItemEntity.conclusionID == null) {
                return 0;
            }
            if (xjWorkItemEntity.conclusionID.equals(realValueInfo.id)) {
                return i;
            }
        }
        return 0;
    }

    /**
     * @author zhangwenshuai1
     * @date 2018/4/9
     * @description 解析上拉菜单数据
     */
    private void initStrList(List<SystemCodeEntity> conclusionList, List<SystemCodeEntity> passReasonList) {
        for (SystemCodeEntity realValueInfo : conclusionList) {
            conclusionStrList.add(realValueInfo.value);
        }
        for (SystemCodeEntity passReasonInfo : passReasonList) {
            passReasonStrList.add(passReasonInfo.value);
        }

    }

    /**
     * @description 历史弹框
     * @author zhangwenshuai1
     * @date 2018/5/8
     */
    @SuppressLint("CheckResult")
    private void showHistories(XJWorkItemEntity xjWorkItemEntity) {
        //查询最近三条（本地数据库会因为数据的下载导致历史增多）
        List<XJHistoryEntity> histories = EamApplication.dao().getXJHistoryEntityDao().queryBuilder()
                .where(XJHistoryEntityDao.Properties.WorkItemId.eq(xjWorkItemEntity.itemId), XJHistoryEntityDao.Properties.Ip.eq(EamApplication.getIp()))
                .orderDesc(XJHistoryEntityDao.Properties.DateTime).limit(3).list();

        if (histories.size() <= 0) {
            SnackbarHelper.showMessage(rootView, "无历史数据可查看");
            return;
        }
        XJHistorySheetAdapter adapter = new XJHistorySheetAdapter(context);
        List<XJHistorySheetEntity> historySheetEntities = new ArrayList<>();

        Flowable.fromIterable(histories)
                .compose(RxSchedulers.io_main())
                .map(xjHistoryEntity -> {

                    XJHistorySheetEntity entity = new XJHistorySheetEntity();
                    entity.id = xjHistoryEntity.id;
                    entity.content = xjHistoryEntity.content;
                    entity.result = xjHistoryEntity.result;
                    entity.conclusion = xjHistoryEntity.conclusion;
                    entity.dateTime = xjHistoryEntity.dateTime;
                    entity.eamName = xjHistoryEntity.eamName;

                    return entity;
                })
                .subscribe(sheetEntity -> historySheetEntities.add(sheetEntity),
                        throwable -> {
                        },
                        () -> new CustomSheetDialog(context)
                                .list("历史记录", historySheetEntities, adapter)
                                .show());


    }

    /**
     * @author zhangwenshuai1
     * @date 2018/4/9
     * @description 初始化无数据
     */
    private void initEmptyView() {
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, ""));

    }

    public void removeItem(int pos) {
        mXJWorkItemListNewAdapter.notifyItemRemoved(pos);
        mXJWorkItemListNewAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getThermometerVal(ThermometerEvent thermometerEvent) {
        LogUtil.i("ThermometerEvent", thermometerEvent.getThermometerVal());
        thermometervalue = thermometerEvent.getThermometerVal().replace("℃", "");

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(RefreshEvent event) {
        this.mRefreshEvent = event;
        getController(TranslucentController.class).initView();
        refreshListController.refreshBegin();
    }

    public void refreshData() {
        Log.i("mXJAreaEntity:", mXJAreaEntity.toString());
        presenterRouter.create(XJWorkItemAPI.class).getXJWorkItemList(mXJAreaEntity.areaId, mXJAreaEntity.taskId, "", false);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
        EventBus.getDefault().post(new RefreshEvent());
    }

    @SuppressLint("CheckResult")
    @Override
    public void getXJWorkItemListSuccess(XJWorkItemListEntity entity) {

        List<XJWorkItemEntity> xjWorkItemEntities = new ArrayList<>();
        XJWorkItemEntity headerEntity = new XJWorkItemEntity();
        headerEntity.headerPicPath = TextUtils.isEmpty(mXJAreaEntity.guideImageName) ? "" : Constant.XJ_GUIDE_IMGPATH + mXJAreaEntity.guideImageName;
        headerEntity.viewType = ListType.HEADER.value();
        xjWorkItemEntities.add(headerEntity);
        Flowable.fromIterable(entity.result)
                .filter(workItemEntity -> !workItemEntity.isFinished)
                .subscribe(workItemEntity -> {

                    if (TextUtils.isEmpty(workItemEntity.equipmentName)) {
                        XJWorkItemEntity titleEntity = new XJWorkItemEntity();
                        titleEntity.title = workItemEntity.equipmentName;
                        titleEntity.viewType = ListType.TITLE.value();
                        xjWorkItemEntities.add(titleEntity);
                    }

                    if (deviceName == null && !TextUtils.isEmpty(workItemEntity.equipmentName) || deviceName != null && !deviceName.equals(workItemEntity.equipmentName)) {
                        deviceName = workItemEntity.equipmentName;
                        XJWorkItemEntity titleEntity = new XJWorkItemEntity();
                        titleEntity.title = workItemEntity.equipmentName;
                        titleEntity.viewType = ListType.TITLE.value();
                        xjWorkItemEntities.add(titleEntity);
                    }

                    xjWorkItemEntities.add(workItemEntity);
                }, throwable -> {

                }, () -> {
                    deviceName = null;
                    refreshListController.refreshComplete(xjWorkItemEntities);
                    initFilterView();
                });


        mRefreshEvent = null;
    }

    @Override
    public void getXJWorkItemListFailed(String errorMsg) {
//        LogUtil.e(ErrorMsgHelper.msgParse(errorMsg));
        SnackbarHelper.showError(rootView, errorMsg);
    }

}
