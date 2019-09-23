package com.supcon.mes.module_xj.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.com_http.util.RxSchedulers;
import com.supcon.common.view.base.activity.BasePresenterActivity;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.mbap.view.CustomTab;
import com.supcon.mes.mbap.view.NoScrollViewPager;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.XJAreaEntity;
import com.supcon.mes.middleware.model.bean.XJExemptionEntity;
import com.supcon.mes.middleware.model.bean.XJExemptionEntityDao;
import com.supcon.mes.middleware.model.bean.XJPathEntity;
import com.supcon.mes.middleware.model.bean.XJPathEntityDao;
import com.supcon.mes.middleware.model.bean.XJWorkItemEntity;
import com.supcon.mes.middleware.model.bean.XJWorkItemEntityDao;
import com.supcon.mes.middleware.model.bean.YHEntityVo;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_xj.IntentRouter;
import com.supcon.mes.module_xj.R;
import com.supcon.mes.module_xj.constant.XJConstant;
import com.supcon.mes.module_xj.model.api.XJWorkItemAPI;
import com.supcon.mes.module_xj.model.bean.XJWorkItemListEntity;
import com.supcon.mes.module_xj.model.contract.XJWorkItemContract;
import com.supcon.mes.module_xj.presenter.XJWorkItemPresenter;
import com.supcon.mes.module_xj.ui.fragment.XJHandledWorkItemFragment;
import com.supcon.mes.module_xj.ui.fragment.XJUnhandledWorkItemFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * Created by wangshizhan on 2018/1/16.
 * Email:wangshizhan@supcon.com
 */

@Deprecated
@Router(Constant.Router.HSCEMENT_XJITEM_LIST)
@Presenter(value = {XJWorkItemPresenter.class})
public class HSXJWorkItemActivity extends BasePresenterActivity implements XJWorkItemContract.View {

    XJUnhandledWorkItemFragment mXJUnhandledItemFragment;
    XJHandledWorkItemFragment mXJHandledItemFragment;

    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("rightBtn")
    ImageButton rightBtn;
    @BindByTag("customSearchView")
    CustomSearchView customSearchView;

    @BindByTag("scrollView")
    ScrollView scrollView;

    @BindByTag("xjGuideImage")
    ImageView xjGuideImage;

    @BindByTag("xjItemVP")
    NoScrollViewPager xjItemVP;

    @BindByTag("xjItemTab")
    CustomTab xjItemTab;

    @BindByTag("buttonBar")
    LinearLayout buttonBar;
    @BindByTag("oneKeyDoBtn")
    Button oneKeyDoBtn;
    @BindByTag("oneKeyRerecordBtn")
    Button oneKeyRerecordBtn;

    private XJAreaEntity mXJAreaEntity;

//    String areaName; //区域名称

    Map deviceNames = new HashMap<String, String>();

    Boolean isFinished = false;

    private SinglePickController mSinglePickController;

    private String eamName = "";

    private boolean initFlag;


    @Override
    protected int getLayoutID() {
        return R.layout.ac_work_item;
    }

    @Override
    protected void onInit() {
        super.onInit();
        mXJAreaEntity = (XJAreaEntity) getIntent().getSerializableExtra(Constant.IntentKey.XJAREA_ENTITY);
//        areaName = mXJAreaEntity.areaName;
        isFinished = getIntent().getStringExtra(Constant.IntentKey.XJPATH_STATUS).equals(Constant.XJPathStateType.PAST_CHECK_STATE);
        EventBus.getDefault().register(this);
        mSinglePickController = new SinglePickController(this);
        mSinglePickController.textSize(18);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);

        titleText.setText(mXJAreaEntity.areaName);
        titleText.setMaxLines(2);
        titleText.setEllipsize(TextUtils.TruncateAt.END);

        customSearchView.setHint("搜索");
        searchTitleBar.enableRightBtn();
        rightBtn.setImageResource(R.drawable.ic_work_lxyh);

        initTabs();
        initViewPager();
        Flowable.timer(0, TimeUnit.MILLISECONDS)
                .compose(RxSchedulers.io_main())
                .subscribe(aLong -> initImageView());

    }

    private void initImageView() {
        try {
            File file = new File(Constant.XJ_GUIDE_IMGPATH + mXJAreaEntity.guideImageName);
            if (!file.exists()) {
                return;
            }
            FileInputStream fileInputStream = new FileInputStream(Constant.XJ_GUIDE_IMGPATH + mXJAreaEntity.guideImageName);
            Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
            xjGuideImage.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    private void initTabs() {
        xjItemTab.addTab("未完成");
        xjItemTab.addTab("已完成");
    }

    private void initViewPager() {

        mXJUnhandledItemFragment = new XJUnhandledWorkItemFragment();
        mXJHandledItemFragment = new XJHandledWorkItemFragment();

        xjItemVP.setAdapter(new MyFragmentAdapter(getSupportFragmentManager()));

        int height = Util.getStatusBarHeight(context);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) xjItemVP.getLayoutParams();
        lp.height = getResources().getDisplayMetrics().heightPixels - height - DisplayUtil.dip2px(50 + 35, this);
        xjItemVP.setLayoutParams(lp);
        xjItemVP.setOffscreenPageLimit(2);
        xjItemTab.setCurrentTab(isFinished ? 1 : 0);
        xjItemVP.setCurrentItem(isFinished ? 1 : 0);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"CheckResult", "ClickableViewAccessibility"})
    @Override
    protected void initListener() {
        super.initListener();

        leftBtn.setOnClickListener(v -> onBackPressed());

        titleText.setOnClickListener(null);
        searchTitleBar.setOnExpandListener(isExpand -> {
            if (isExpand) {
                customSearchView.setHint("搜索设备名称");
                customSearchView.setInputTextColor(R.color.hintColor);
            } else {
                customSearchView.setHint("");
                customSearchView.setInputTextColor(R.color.black);
            }
        });
        RxTextView.textChanges(customSearchView.editText())
                .skipInitialValue()
                .debounce(300, TimeUnit.MILLISECONDS)
                .subscribe(charSequence -> HSXJWorkItemActivity.this.doSearch(charSequence));

        xjGuideImage.setOnClickListener(v -> {
//                List<GalleryBean> galleryBeanList = customGalleryView.getGalleryAdapter().getList();
            File file = new File(Constant.XJ_GUIDE_IMGPATH + mXJAreaEntity.guideImageName);
            if (!file.exists()) {
                ToastUtils.show(context, "无图片显示！");
                return;
            }
            Bundle bundle = new Bundle();
            List<String> list = new ArrayList<>();
            list.add(Constant.XJ_GUIDE_IMGPATH + mXJAreaEntity.guideImageName);
            bundle.putSerializable("images", (Serializable) list);
            bundle.putInt("position", 0);  //点击位置索引

            int[] location = new int[2];
            v.getLocationOnScreen(location);  //点击图片的位置
            bundle.putInt("locationX", location[0]);
            bundle.putInt("locationY", location[1]);

            bundle.putInt("width", DisplayUtil.dip2px(100, context));//必须
            bundle.putInt("height", DisplayUtil.dip2px(100, context));//必须

            bundle.putBoolean("isEditable", false);  //删除图标
            getWindow().setWindowAnimations(R.style.fadeStyle);
            IntentRouter.go(context, Constant.Router.IMAGE_VIEW, bundle);
        });

        xjItemTab.setOnTabChangeListener(current -> {
            xjItemVP.setCurrentItem(current);

        });

        xjItemVP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                if (xjItemTab.getCurrentPosition() != position) {
                    xjItemTab.setCurrentTab(position);
                }

                if (xjItemTab.getCurrentPosition() == 0) {  //未完成
                    if (mXJUnhandledItemFragment.getXjUfWorkItemEntities().size() <= 0) {
                        buttonBar.setVisibility(View.GONE);
                        oneKeyDoBtn.setVisibility(View.GONE);
                    } else {
                        buttonBar.setVisibility(View.VISIBLE);
                        oneKeyDoBtn.setVisibility(View.VISIBLE);
                        oneKeyRerecordBtn.setVisibility(View.GONE);
                    }
                }

                if (xjItemTab.getCurrentPosition() == 1) { //已完成
                    if (mXJHandledItemFragment.getXjWorkItemEntities().size() <= 0) {
                        buttonBar.setVisibility(View.GONE);
                        oneKeyRerecordBtn.setVisibility(View.GONE);
                    } else {
                        buttonBar.setVisibility(View.VISIBLE);
                        oneKeyRerecordBtn.setVisibility(View.VISIBLE);
                        oneKeyDoBtn.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                scrollView.smoothScrollTo(0,0); //默认置顶显示
            }
        });

        RxView.clicks(rightBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
//                            Bundle bundle = new Bundle();
//                            bundle.putLong("taskId",mXJAreaEntity.taskId);
//                            bundle.putLong("workId",mXJAreaEntity.areaId);
//                            IntentRouter.go(context,Constant.Router.XJ_QXGL_LIST,bundle);
                            IntentRouter.go(context, Constant.Router.OFFLINE_YH_LIST);
                        }
                );

        RxView.clicks(oneKeyDoBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        XJWorkItemEntityDao xjWorkItemEntityDao = EamApplication.dao().getXJWorkItemEntityDao();
                        //一键完成
                        new CustomDialog(context)
                                .twoButtonAlertDialog("是否一键完成巡检项？")
                                .bindView(R.id.grayBtn, "否")
                                .bindView(R.id.redBtn, "是")
                                .bindClickListener(R.id.grayBtn, null, true)
                                .bindClickListener(R.id.redBtn, v -> {
//                                        Flowable.fromIterable(mXJUnhandledItemFragment.getXjUfWorkItemEntities())
////                                                .flatMap()
////                                                .subscribe(new Consumer<Object>() {
////                                                })
                                    try {
                                        for (XJWorkItemEntity xjWorkItemEntity : mXJUnhandledItemFragment.getXjUfWorkItemEntities()) {
                                            doFinish(xjWorkItemEntity, xjWorkItemEntityDao);
                                        }

//                                        List<XJWorkItemEntity> xjWorkItemEntityList = mXJUnhandledItemFragment.getXjUfWorkItemEntities();
//                                        Iterator<XJWorkItemEntity> iterator = xjWorkItemEntityList.iterator();
//                                        while (iterator.hasNext()){
//                                            doFinish(iterator,iterator.next(), xjWorkItemEntityDao);
//                                        }


                                        ToastUtils.show(context, "处理成功！");
                                        EventBus.getDefault().post(new RefreshEvent());
                                    } catch (Exception e) {
                                        ToastUtils.show(context, "处理失败： " + e.getMessage());
                                        e.printStackTrace();
                                    } finally {
                                    }
                                }, true)
                                .show();

                    }
                });

        RxView.clicks(oneKeyRerecordBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        XJWorkItemEntityDao xjWorkItemEntityDao = EamApplication.dao().getXJWorkItemEntityDao();
                        List<XJWorkItemEntity> xjWorkItemEntityList = xjWorkItemEntityDao.queryBuilder()
                                .where(XJWorkItemEntityDao.Properties.TaskId.eq(mXJAreaEntity.taskId), XJWorkItemEntityDao.Properties.AreaId.eq(mXJAreaEntity.areaId), XJWorkItemEntityDao.Properties.Control.eq(true),
                                        XJWorkItemEntityDao.Properties.IsFinished.eq(true), XJWorkItemEntityDao.Properties.Ip.eq(EamApplication.getIp()))
                                .orderAsc(XJWorkItemEntityDao.Properties.ItemOrder, XJWorkItemEntityDao.Properties.Part)
                                .list();

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
                                        XJPathEntity xjPathEntity = EamApplication.dao().getXJPathEntityDao().queryBuilder()
                                                .where(XJPathEntityDao.Properties.Id.eq(mXJAreaEntity.taskId), XJPathEntityDao.Properties.Ip.eq(EamApplication.getIp()))
                                                .list().get(0);
                                        if (Constant.XJPathStateType.PAST_CHECK_STATE.equals(xjPathEntity.state)) {
                                            xjPathEntity.state = Constant.XJPathStateType.WAIT_CHECK_STATE;
                                            EamApplication.dao().getXJPathEntityDao().update(xjPathEntity);
                                        }
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

    private void doSearch(CharSequence charSequence) {
        eamName = charSequence.toString().trim();
        refreshData();
    }

    /**
     * @param
     * @param xjWorkItemEntityDao
     * @return
     * @description 一键完成巡检项
     * @author zhangwenshuai1 2018/12/29
     */
    private void doFinish(XJWorkItemEntity xjWorkItemEntity, XJWorkItemEntityDao xjWorkItemEntityDao) {
        if (!XJConstant.MobileWiLinkState.EXEMPTION_STATE.equals(xjWorkItemEntity.linkState) && !xjWorkItemEntity.isFinished){ //免检项过滤掉，因为在后续的循环中被免检的项没有从当前列表中移除
            xjWorkItemEntity.endTime = DateUtil.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
            xjWorkItemEntity.isFinished = true;
            xjWorkItemEntity.linkState = XJConstant.MobileWiLinkState.FINISHED_STATE;
            xjWorkItemEntity.staffId = EamApplication.getAccountInfo().staffId;
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

            List<XJWorkItemEntity> xjWorkItemEntityList = mXJUnhandledItemFragment.getXjUfWorkItemEntities();
//            Iterator<XJWorkItemEntity> iterator = xjWorkItemEntityList.iterator();

            for (XJExemptionEntity xjExemptionEntity : xjExemptionEntities) {
                for (XJWorkItemEntity xjWorkItemEntity1 :xjWorkItemEntityList){
                    if (xjExemptionEntity.exemptionItemId.equals(xjWorkItemEntity1.itemId) && !xjWorkItemEntity1.isFinished) { //避免将自己免检
                        xjWorkItemEntity1.linkState = XJConstant.MobileWiLinkState.EXEMPTION_STATE;
                        xjWorkItemEntity1.isFinished = true;
                        xjWorkItemEntity1.endTime = DateUtil.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
                        xjWorkItemEntity1.staffId = EamApplication.getAccountInfo().staffId;
                        xjWorkItemEntity1.result = null;  // 存在默认值时，需要清空
                        xjWorkItemEntity1.conclusionName = null;
                        xjWorkItemEntity1.conclusionID = null;
                        xjWorkItemEntityDao.update(xjWorkItemEntity1);

//                        xjWorkItemEntityList.remove(xjWorkItemEntity1);
//                        iterator.remove();
                        break;
                    }
                }
            }
        }
    }

    /**
     * @param
     * @return
     * @description 自动生成隐患单
     * @author zhangwenshuai1 2019/1/2
     */
    public void doSaveEditYh(XJWorkItemEntity xjWorkItemEntity) {
        YHEntityVo yhEntityVo = new YHEntityVo();
        yhEntityVo.setFindStaffName(EamApplication.getAccountInfo().staffName);
        yhEntityVo.setFindStaffId(EamApplication.getAccountInfo().staffId);
        yhEntityVo.setFindDate(DateUtil.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        yhEntityVo.setSrcID(xjWorkItemEntity.taskId);
        yhEntityVo.setTaskId(xjWorkItemEntity.id);
        yhEntityVo.setAreaInstallId("".equals(xjWorkItemEntity.eamAreaId) ? null : Long.valueOf(xjWorkItemEntity.eamAreaId));
        yhEntityVo.setAreaInstallName(xjWorkItemEntity.eamAreaName);
        yhEntityVo.setEamId(Long.valueOf(xjWorkItemEntity.equipmentId));
        yhEntityVo.setEamName(xjWorkItemEntity.equipmentName);
        yhEntityVo.setDescription("部位：" + xjWorkItemEntity.part + "\r\n" + "内容：" + xjWorkItemEntity.itemContent + "\r\n" + "正常值范围：" + xjWorkItemEntity.normalRange + "\r\n"
                + "结果：" + xjWorkItemEntity.result + "\r\n" + "结论：异常");

        yhEntityVo.setLocalPicPaths(xjWorkItemEntity.xjImgUrl);
        yhEntityVo.setStatus(true);

        EamApplication.dao().getYHEntityVoDao().insert(yhEntityVo);
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
                for (XJWorkItemEntity xjWorkItemEntity1 : mXJHandledItemFragment.getXjWorkItemEntities()) {
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

    /**
     * @param
     * @return
     * @description 删除保存的编辑隐患
     * @author zhangwenshuai1 2019/1/2
     */
    public void doDeleteEditYh(XJWorkItemEntity xjWorkItemEntity) {
        if (XJConstant.MobileConclusion.AB_NORMAL.equals(xjWorkItemEntity.conclusionID)) {
            EamApplication.dao().getDatabase().execSQL("delete from YHENTITY_VO where TASK_ID = ? and IP = ? ", new Object[]{xjWorkItemEntity.id, EamApplication.getIp()});
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        EventBus.getDefault().post(new RefreshEvent());
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initData() {
        super.initData();

        Flowable.fromIterable(EamApplication.dao().getXJWorkItemEntityDao().queryBuilder().where(XJWorkItemEntityDao.Properties.Ip.eq(EamApplication.getIp())).distinct().list())
                .compose(RxSchedulers.io_main())
                .subscribe(xjWorkItemEntity -> {
                    if (!deviceNames.keySet().contains(xjWorkItemEntity.equipmentId)) {
                        deviceNames.put(xjWorkItemEntity.equipmentId, xjWorkItemEntity.equipmentName);
                    }
                });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(RefreshEvent event) {
        this.mRefreshEvent = event;
        refreshData();
    }

    public void refreshData() {
        Log.i("mXJAreaEntity:", mXJAreaEntity.toString());
        presenterRouter.create(XJWorkItemAPI.class).getXJWorkItemList(mXJAreaEntity.areaId, mXJAreaEntity.taskId, eamName,null);
    }

    private RefreshEvent mRefreshEvent;

    @Override
    public void getXJWorkItemListSuccess(XJWorkItemListEntity entity) {
        initFlag = true;
        //暂时先不用这个方法来直接进行刷新
//        mXJUnhandledItemFragment.setUnhandledWorkItemList(entity.result);
//        if (null != mRefreshEvent && null != mRefreshEvent.action && mRefreshEvent.action.equals(Constant.RefreshAction.XJ_WORK_END)){
//            mXJUnhandledItemFragment.removeItem(mRefreshEvent.pos);
//        }else {
        mXJUnhandledItemFragment.setUnhandledWorkItemList(entity.result);
//        }


//        if (null != mRefreshEvent && null != mRefreshEvent.action && mRefreshEvent.action.equals(Constant.RefreshAction.XJ_WORK_REINPUT)){
//            mXJHandledItemFragment.removeItem(mRefreshEvent.pos);
//        }else {
        mXJHandledItemFragment.setHandledWorkItemList(entity.result);
//        }


        mRefreshEvent = null;
    }

    @Override
    public void getXJWorkItemListFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mXJUnhandledItemFragment.onActivityResult(requestCode, resultCode, data);
    }

    public void showButtonBar(boolean b, int tabPosition) {
        if (tabPosition == xjItemTab.getCurrentPosition()) {
            if (b && tabPosition == 0) { // 未完成页签
                buttonBar.setVisibility(View.VISIBLE);
                oneKeyDoBtn.setVisibility(View.VISIBLE);
            } else if (!b && tabPosition == 0) {
                buttonBar.setVisibility(View.GONE);
                oneKeyDoBtn.setVisibility(View.GONE);
            }
            if (b && tabPosition == 1) { //已完成页签
                buttonBar.setVisibility(View.VISIBLE);
                oneKeyRerecordBtn.setVisibility(View.VISIBLE);
                oneKeyDoBtn.setVisibility(View.GONE);
            } else if (!b && tabPosition == 1) {
                buttonBar.setVisibility(View.GONE);
                oneKeyRerecordBtn.setVisibility(View.GONE);
            }
        }
    }

    private class MyFragmentAdapter extends FragmentPagerAdapter {

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {

                case 0:
                    return mXJUnhandledItemFragment;
                case 1:
                    return mXJHandledItemFragment;

            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }


}
