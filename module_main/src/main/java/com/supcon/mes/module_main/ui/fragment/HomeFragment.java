package com.supcon.mes.module_main.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.BaseConstant;
import com.supcon.common.com_http.util.RxSchedulers;
import com.supcon.common.view.base.fragment.BaseRefreshFragment;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.LogUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.beans.GalleryBean;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.listener.OnTextListener;
import com.supcon.mes.mbap.view.CustomAdView;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.UserPowerCheckController;
import com.supcon.mes.middleware.model.api.EamAPI;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchStaff;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.contract.EamContract;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.model.event.NFCEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.presenter.EamPresenter;
import com.supcon.mes.middleware.util.BadgeUtil;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.middleware.model.bean.WorkInfo;
import com.supcon.mes.middleware.util.WorkHelper;
import com.supcon.mes.module_login.controller.SilentLoginController;
import com.supcon.mes.module_main.model.api.MainMenuAPI;
import com.supcon.mes.module_main.model.contract.MainMenuContract;
import com.supcon.mes.module_main.presenter.MainMenuPresenter;
import com.supcon.mes.module_main.ui.adaper.WorkAdapter;
import com.supcon.mes.module_main.IntentRouter;
import com.supcon.mes.module_main.R;
import com.supcon.mes.module_main.model.api.MainPendingNumAPI;
import com.supcon.mes.module_main.model.api.ProcessedAPI;
import com.supcon.mes.module_main.model.api.ScoreStaffAPI;
import com.supcon.mes.module_main.model.api.WaitDealtAPI;
import com.supcon.mes.module_main.model.api.WarnPendingListAPI;
import com.supcon.mes.module_main.model.bean.ScoreEntity;
import com.supcon.mes.module_main.model.bean.WaitDealtEntity;
import com.supcon.mes.module_main.model.bean.WorkNumEntity;
import com.supcon.mes.module_main.model.contract.MainPendingNumContract;
import com.supcon.mes.module_main.model.contract.ProcessedContract;
import com.supcon.mes.module_main.model.contract.ScoreStaffContract;
import com.supcon.mes.module_main.model.contract.WaitDealtContract;
import com.supcon.mes.module_main.model.contract.WarnPendingListContract;
import com.supcon.mes.module_main.presenter.MainPendingNumPresenter;
import com.supcon.mes.module_main.presenter.ProcessedPresenter;
import com.supcon.mes.module_main.presenter.ScoreStaffPresenter;
import com.supcon.mes.module_main.presenter.WaitDealtPresenter;
import com.supcon.mes.module_main.presenter.WarnPendingPresenter;
import com.supcon.mes.module_main.ui.MainActivity;
import com.supcon.mes.module_main.ui.adaper.ProcessedAdapter;
import com.supcon.mes.module_main.ui.adaper.WaitDealtAdapter;
import com.supcon.mes.module_main.ui.adaper.WarnWorkAdapter;
import com.supcon.mes.module_main.ui.util.MenuHelper;
import com.supcon.mes.module_main.ui.view.MenuPopwindow;
import com.supcon.mes.module_main.ui.view.MenuPopwindowBean;
import com.supcon.mes.push.event.PushRefreshEvent;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.internal.crash.UMCrashManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Flowable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * @Description: 首页
 * @Author: zhangwenshuai
 * @CreateDate: 2020/6/4 14:56
 */
@Presenter(value = {WaitDealtPresenter.class, EamPresenter.class, ScoreStaffPresenter.class, MainPendingNumPresenter.class, WarnPendingPresenter.class, ProcessedPresenter.class, MainMenuPresenter.class})
public class HomeFragment extends BaseRefreshFragment implements WaitDealtContract.View, EamContract.View, ScoreStaffContract.View
        , MainActivity.WorkOnTouchListener, MainPendingNumContract.View, WarnPendingListContract.View , ProcessedContract.View, MainMenuContract.View {

    @BindByTag("workCustomAd")
    CustomAdView workCustomAd;

    @BindByTag("eamSearchIv")
    ImageView eamSearchIv;
    @BindByTag("contentView")
    ScrollView contentView;
    @BindByTag("workRecycler")
    RecyclerView workRecycler;
    @BindByTag("pendingRadioGroup")
    RadioGroup pendingRadioGroup;

    @BindByTag("warnRecycler")
    RecyclerView warnRecycler;
    @BindByTag("pendingRecycler")
    RecyclerView pendingRecycler;
    @BindByTag("processedRecycler")
    RecyclerView processedRecycler;
    @BindByTag("noDataLayout")
    LinearLayout noDataLayout;

    @BindByTag("scoreLayout")
    RelativeLayout scoreLayout;
    @BindByTag("rank")
    TextView rank;
    @BindByTag("score")
    TextView score;

    @BindByTag("workName")
    TextView workName;  // 登录人员
    @BindByTag("workDepot")
    TextView workDepot; // 部门

    @BindByTag("contentTitleLabel")
    TextView contentTitleLabel; // 工作提醒

    private boolean hidden;
    private WorkAdapter workAdapter;
    private WarnWorkAdapter mWarnWorkAdapter;
    private WaitDealtAdapter mWaitDealtAdapter;
    private ProcessedAdapter mProcessedAdapter;

    private ScoreEntity scoreEntity;
    private CommonSearchStaff proxyStaff;
    private List<CommonSearchEntity> mSelectStaffList;
    private CustomDialog customDialog;
    private String reason;
//    private AtomicBoolean atomicBoolean = new AtomicBoolean(false);
    private Map<String, Object> queryParam = new HashMap<>();
    private int index; // 工作提醒切换索引

    @Override
    protected int getLayoutID() {
        return R.layout.main_fragment_home;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        refreshController.setAutoPullDownRefresh(false);
        refreshController.setPullDownRefreshEnabled(true);
        // 业务菜单入口
        GridLayoutManager layoutManager = new GridLayoutManager(context, 4);
        workRecycler.setLayoutManager(layoutManager);
        workAdapter = new WorkAdapter(getActivity());
        workRecycler.setAdapter(workAdapter);

        initMyMenu();

    }

     /**
      * @method
      * @description 初始化我的菜单
      * @author: zhangwenshuai
      * @date: 2020/6/10 14:24
      * @param  * @param null
      * @return
      */
    private void initMyMenu() {
        presenterRouter.create(MainMenuAPI.class).listMyMenu(true);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onResume() {
        super.onResume();
//        if (atomicBoolean.compareAndSet(true, true)) {
            getWorkData();
//        }

//        MobclickAgent.onPageStart("HomeFragment");

    }

    @SuppressLint("CheckResult")
    @Override
    protected void initView() {
        super.initView();
        workName.setText(EamApplication.getAccountInfo().staffName);
        workDepot.setText(EamApplication.getAccountInfo().positionName);
        Flowable.timer(20, TimeUnit.MILLISECONDS)
                .compose(RxSchedulers.io_main())
                .subscribe(aLong -> initAd());

        pendingRecycler.setLayoutManager(new LinearLayoutManager(context));
        pendingRecycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(DisplayUtil.dip2px(5,context),DisplayUtil.dip2px(2,context),DisplayUtil.dip2px(5,context),
                        DisplayUtil.dip2px(1,context));
            }
        });
        mWaitDealtAdapter = new WaitDealtAdapter(getActivity());
        pendingRecycler.setAdapter(mWaitDealtAdapter);

        processedRecycler.setLayoutManager(new LinearLayoutManager(context));
        processedRecycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(DisplayUtil.dip2px(5,context),DisplayUtil.dip2px(2,context),DisplayUtil.dip2px(5,context),
                        DisplayUtil.dip2px(1,context));
            }
        });
        mProcessedAdapter = new ProcessedAdapter(getActivity());
        processedRecycler.setAdapter(mProcessedAdapter);


        warnRecycler.setLayoutManager(new LinearLayoutManager(context));
        warnRecycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(DisplayUtil.dip2px(5,context),DisplayUtil.dip2px(2,context),DisplayUtil.dip2px(5,context),
                        DisplayUtil.dip2px(1,context));
            }
        });
        mWarnWorkAdapter = new WarnWorkAdapter(getActivity());
        warnRecycler.setAdapter(mWarnWorkAdapter);
    }


    @SuppressLint("CheckResult")
    @Override
    protected void initData() {
        super.initData();
    }

    private void initAd() {
        List<GalleryBean> ads = new ArrayList<>();
        GalleryBean galleryBean = new GalleryBean();
        if (EamApplication.isHongshi()) {
            galleryBean.resId = R.drawable.banner_hssn;
            ads.add(galleryBean);
        } else if (EamApplication.isHailuo()) {
            galleryBean.resId = R.drawable.banner_hailuo;
            ads.add(galleryBean);
        } else if (EamApplication.isYNSW()) {
            galleryBean.resId = R.drawable.pic_ynsw_ban01;
            ads.add(galleryBean);
            galleryBean = new GalleryBean();
            galleryBean.resId = R.drawable.pic_ynsw_ban02;
            ads.add(galleryBean);
            galleryBean = new GalleryBean();
            galleryBean.resId = R.drawable.pic_ynsw_ban03;
            ads.add(galleryBean);
        } else {
            galleryBean.resId = R.drawable.pic_banner05;
            ads.add(galleryBean);
        }

        workCustomAd.setGalleryBeans(ads);
    }

    @SuppressLint({"CheckResult", "ClickableViewAccessibility"})
    @Override
    protected void initListener() {
        super.initListener();
//        workDepot.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                BadgeUtil.setNotificationBadge(11,context);
//            }
//        });
        refreshController.setOnRefreshListener(() -> getWorkData());
        pendingRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            refreshController.refreshBegin();
            Flowable.timer(50, TimeUnit.MILLISECONDS)
                    .compose(RxSchedulers.io_main())
                    .subscribe(aLong -> {
                        if (checkedId == R.id.processedRBtn){
                            index = 1;
                            presenterRouter.create(ProcessedAPI.class).workflowHandleList(queryParam,1, 2);
                        }else if (checkedId == R.id.subordinatePendingRBtn){
                            index = 2;
                            queryParam.put(Constant.BAPQuery.SUBORDINATE,EamApplication.getAccountInfo().departmentId);
                            presenterRouter.create(WaitDealtAPI.class).getWaitDealt(1, 2, queryParam);
                        }else {
                            index = 0;
                            queryParam.clear();
                            presenterRouter.create(WaitDealtAPI.class).getWaitDealt(1, 2, queryParam);
                        }
                    });
        });
        workAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {});

        mWaitDealtAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                WaitDealtEntity waitDealtEntity = (WaitDealtEntity) obj;
                if (childView.getId() == R.id.waitDealtEntrust) {
                    proxyDialog(waitDealtEntity);
                }
            }
        });

        RxView.clicks(eamSearchIv)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
//                        Bundle bundle = new Bundle();
//                    bundle.putBoolean(Constant.IntentKey.IS_MAIN_EAM, true);
//                        bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG, "Main");
//                        IntentRouter.go(getActivity(), Constant.Router.EAM, bundle);
                        Bundle bundle = new Bundle();
//                        bundle.putBoolean(Constant.IntentKey.IS_MAIN_EAM, true);
                        bundle.putBoolean(Constant.IntentKey.IS_MULTI, false);
                        bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG, "Main");
                        bundle.putBoolean(Constant.IntentKey.IS_SELECT,true);
                        com.supcon.mes.middleware.IntentRouter.go(context, Constant.Router.EAM_TREE_SELECT, bundle);

                    }
                });

        scoreLayout.setOnClickListener(v -> {
//            if (scoreEntity != null && !TextUtils.isEmpty(scoreEntity.type)) {
                Bundle bundle = new Bundle();
//                bundle.putInt(Constant.IntentKey.RANKING, scoreEntity.ranking != null ? scoreEntity.ranking : -1);
//                bundle.putString(Constant.IntentKey.TYPE, scoreEntity.type);
                IntentRouter.go(getActivity(), Constant.Router.SCORE_RANKING, bundle);
//            } else {
//                ToastUtils.show(getActivity(), "未获取到当前用户评分，不能查看排名！");
//            }
        });
    }

    /**
     * 委托代办
     *
     * @param waitDealtEntity
     */
    private void proxyDialog(WaitDealtEntity waitDealtEntity) {
        customDialog = new CustomDialog(context).layout(R.layout.proxy_dialog,
                DisplayUtil.getScreenWidth(context) * 2 / 3, WRAP_CONTENT)
                .bindView(R.id.blueBtn, "确定")
                .bindView(R.id.grayBtn, "取消")
                .bindChildListener(R.id.proxyPerson, new OnChildViewClickListener() {
                    @Override
                    public void onChildViewClick(View childView, int action, Object obj) {
                        if (action == -1) {
                            mSelectStaffList = null;
                        }
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(Constant.IntentKey.IS_MULTI, true);
                        bundle.putBoolean(Constant.IntentKey.IS_SELECT_STAFF, true);
                        bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG, "Main");
//                        IntentRouter.go(context, Constant.Router.CONTACT_SELECT, bundle);
                        IntentRouter.go(context,Constant.Router.STAFF,bundle);
                    }
                })
                .bindTextChangeListener(R.id.proxyReason, new OnTextListener() {
                    @Override
                    public void onText(String text) {
                        reason = text.trim();
                    }
                })
                .bindClickListener(R.id.blueBtn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v12) {
                        if (mSelectStaffList == null) {
                            ToastUtils.show(context, getResources().getString(R.string.main_select_assignor));
                            return;
                        }
                        if (waitDealtEntity.pendingId == null) {
                            ToastUtils.show(context, context.getResources().getString(R.string.no_get_pending));
                            return;
                        }
                        onLoading(getResources().getString(R.string.main_do_proxying));
                        StringBuilder sb = new StringBuilder();
                        for (CommonSearchEntity commonSearchEntity : mSelectStaffList){
                            sb.append(((CommonSearchStaff)commonSearchEntity).userId).append(",");
                        }
                        presenterRouter.create(WaitDealtAPI.class).proxyPending(waitDealtEntity.pendingId, sb.toString(), reason);
                        customDialog.dismiss();
                    }
                }, false)
                .bindClickListener(R.id.grayBtn, null, true);
        ((CustomEditText) customDialog.getDialog().findViewById(R.id.proxyReason)).editText().setScrollBarSize(0);
        customDialog.getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        customDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void search(CommonSearchEvent commonSearchEvent) {
        if (!TextUtils.isEmpty(commonSearchEvent.flag) && commonSearchEvent.flag.equals("Main")) {
            if (commonSearchEvent.commonSearchEntity != null) {
                if (commonSearchEvent.commonSearchEntity instanceof CommonSearchStaff) {
                    proxyStaff = (CommonSearchStaff) commonSearchEvent.commonSearchEntity;
                    CustomTextView person = customDialog.getDialog().findViewById(R.id.proxyPerson);
                    person.setContent(Util.strFormat(proxyStaff.name));
                } else if (commonSearchEvent.commonSearchEntity instanceof EamEntity) {
                    EamEntity eamEntity = (EamEntity) commonSearchEvent.commonSearchEntity;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constant.IntentKey.EAM, eamEntity);
                    IntentRouter.go(getActivity(), Constant.Router.EAM_DETAIL, bundle);
                }
            }else if (commonSearchEvent.mCommonSearchEntityList != null){
                mSelectStaffList = commonSearchEvent.mCommonSearchEntityList;
                StringBuilder searchStaffs = new StringBuilder();
                for (CommonSearchEntity commonSearchEntity : mSelectStaffList){
                    searchStaffs.append(((CommonSearchStaff)commonSearchEntity).getName()).append(",");
                }
                if (customDialog != null && customDialog.getDialog().isShowing()) {
                    CustomTextView person = customDialog.getDialog().findViewById(R.id.proxyPerson);
                    person.setContent(Util.strFormat(searchStaffs.substring(0,searchStaffs.length()-1)));
                }
            }
        }
    }

    @Override
    public boolean onTouch(MotionEvent ev) {
//        if (mChildView == null) return false;
//        boolean isClickWorkRecycler = inRangeOfView(mChildView, ev);
//        if (!isClickWorkRecycler) {
//            menuPopwindow.changeWindowAlfa(1f);
//            oldPosition = -1;
//        }
        return false;
    }

    /**
     * 判断是不是点击在控件上
     *
     * @param view
     * @param ev
     * @return
     */
    public boolean inRangeOfView(View view, MotionEvent ev) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if (ev.getX() < x || ev.getX() > (x + view.getWidth()) || ev.getY() < y || ev.getY() > (y + view.getHeight())) {
            return false;
        }
        return true;
    }

    /**
     * @param
     * @description NFC事件
     * @author zhangwenshuai1
     * @date 2018/6/28
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getNFC(NFCEvent nfcEvent) {
        if (!hidden) {
            if (!TextUtils.isEmpty(nfcEvent.getTag()) && nfcEvent.getTag().equals("Main")) {
                LogUtil.d("NFC_TAG", nfcEvent.getNfc());
                Map<String, Object> nfcJson = Util.gsonToMaps(nfcEvent.getNfc());
                if (nfcJson.get("textRecord") == null) {
                    ToastUtils.show(context, "标签内容空！");
                    return;
                }
                Map<String, Object> params = new HashMap<>();
                params.put(Constant.IntentKey.EAM_CODE, nfcJson.get("textRecord"));
                presenterRouter.create(EamAPI.class).getEam(params, true,1,20);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(LoginEvent loginEvent) {
//        if (!atomicBoolean.get()) {
//            powerCheck(userPowerCheckController, aewMenu, lubricateMenu, repairMenu, formMenu);
//        } else {
            getWorkData();
//        }
    }

    //有推送待办过来刷新界面
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshPush(PushRefreshEvent event) {
        getWorkData();
    }

    //有推送待办过来刷新界面
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshEvent event) {
        if (Constant.RefreshAction.HOME_APP_MENU.equals(event.action)) initMyMenu();
    }

    private void getWorkData() {
        if (index == 0){
            presenterRouter.create(WaitDealtAPI.class).getWaitDealt(1, 2, new HashMap<>());
        }
        presenterRouter.create(ScoreStaffAPI.class).getPersonScore(String.valueOf(EamApplication.getAccountInfo().getStaffId()));
        presenterRouter.create(WarnPendingListAPI.class).listWarnPending(new HashMap<>(),1,1);
        presenterRouter.create(MainPendingNumAPI.class).getMainWorkCount(String.valueOf(EamApplication.getAccountInfo().getStaffId()));
    }

    @Override
    public void getWaitDealtSuccess(CommonBAPListEntity entity) {
        processedRecycler.setVisibility(View.GONE);
        pendingRecycler.setVisibility(View.VISIBLE);
        mWaitDealtAdapter.setList(entity.result);
        mWaitDealtAdapter.notifyDataSetChanged();
        if (entity.result.size() > 0) {
            noDataLayout.setVisibility(View.GONE);
        } else {
            noDataLayout.setVisibility(View.VISIBLE);
        }
        refreshController.refreshComplete();
    }

    @Override
    public void getWaitDealtFailed(String errorMsg) {
        processedRecycler.setVisibility(View.GONE);
        refreshController.refreshComplete();
        noDataLayout.setVisibility(View.VISIBLE);
        ToastUtils.show(context,ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void proxyPendingSuccess(BapResultEntity entity) {
        onLoadSuccess("待办委托成功");
        presenterRouter.create(WaitDealtAPI.class).getWaitDealt(1, 2, new HashMap<>());
    }

    @Override
    public void proxyPendingFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void getEamSuccess(CommonListEntity entity) {
        if (entity.result.size() > 0) {
            EamEntity eamEntity = (EamEntity) entity.result.get(0);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.IntentKey.EAM, eamEntity);
            bundle.putBoolean(Constant.IntentKey.IS_NFC_SIGN, true);
            IntentRouter.go(getActivity(), Constant.Router.EAM_DETAIL, bundle);
            return;
        }
        ToastUtils.show(context, context.getResources().getString(R.string.no_query_eam));
    }

    @Override
    public void getEamFailed(String errorMsg) {
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void getPersonScoreSuccess(CommonEntity entity) {
        scoreEntity = (ScoreEntity) entity.result;
        rank.setText(Util.strFormat(scoreEntity.ranking));
        score.setText(Util.big2(scoreEntity.score));
    }

    @Override
    public void getPersonScoreFailed(String errorMsg) {
        LogUtil.e(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void getMainWorkCountSuccess(CommonBAPListEntity entity) {
        List<WorkNumEntity> result = entity.result;
        if (result.size() > 0) {
            updateNum(result);
        }
    }

    @Override
    public void getMainWorkCountFailed(String errorMsg) {
        LogUtil.e(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void onPause() {
        super.onPause();
//        if (menuPopwindow != null && menuPopwindow.isShowing()) {
//            menuPopwindow.dismiss();
//            menuPopwindow.changeWindowAlfa(1f);
//        }
//        MobclickAgent.onPageEnd("HomeFragment");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @SuppressLint("CheckResult")
    private void updateNum(List<WorkNumEntity> workNumEntities) {
        Flowable.fromIterable(workNumEntities)
                .subscribe(workNumEntity -> {
                    List<WorkInfo> workInfoList = workAdapter.getList();
                    for (WorkInfo workInfo : workInfoList) {
                        if (workNumEntity.tagName.equals(workInfo.tag)) {
                            workInfo.num = workNumEntity.num;
                            break;
                        }
                    }
                }, throwable -> {

                }, () -> workAdapter.notifyItemRangeChanged(0,workAdapter.getList().size()-1));
    }

//    @SuppressLint("CheckResult")
//    private void powerCheck(UserPowerCheckController userPowerCheckController, List<MenuPopwindowBean>... menuPopwindowBeans) {
//        List<MenuPopwindowBean> menuAllPopwindows = new ArrayList<>();
//        for (List<MenuPopwindowBean> menuPopwindows : menuPopwindowBeans) {
//            menuAllPopwindows.addAll(menuPopwindows);
//        }
//        StringBuilder menuOperateCodes = new StringBuilder();
//        Flowable.fromIterable(menuAllPopwindows)
//                .filter(menuPopwindowBean -> !TextUtils.isEmpty(menuPopwindowBean.getMenuOperateCodes()))
//                .subscribe(menuPopwindowBean -> menuOperateCodes.append(menuPopwindowBean.getMenuOperateCodes()).append(","), throwable -> {
//                }, () -> {
//                    if (!TextUtils.isEmpty(menuOperateCodes)) {
//                        menuOperateCodes.deleteCharAt(menuOperateCodes.length() - 1);
//                        userPowerCheckController.checkModulePermission(EamApplication.getAccountInfo().cid, menuOperateCodes.toString()
//                                , result -> {
//                                    atomicBoolean.set(true);
//                                    Flowable.fromIterable(Arrays.asList(menuPopwindowBeans))
//                                            .flatMap(new Function<List<MenuPopwindowBean>, Flowable<MenuPopwindowBean>>() {
//                                                @Override
//                                                public Flowable<MenuPopwindowBean> apply(List<MenuPopwindowBean> menuPopwindowBeans1) throws Exception {
//                                                    return Flowable.fromIterable(menuPopwindowBeans1);
//                                                }
//                                            })
//                                            .filter(new Predicate<MenuPopwindowBean>() {
//                                                @Override
//                                                public boolean test(MenuPopwindowBean menuPopwindowBean) throws Exception {
//                                                    if (result.containsKey(menuPopwindowBean.getMenuOperateCodes())) {
//                                                        return true;
//                                                    }
//                                                    return false;
//                                                }
//                                            })
//                                            .subscribe(new Consumer<MenuPopwindowBean>() {
//                                                @Override
//                                                public void accept(MenuPopwindowBean menuPopwindowBean) throws Exception {
//                                                    menuPopwindowBean.setPower(result.get(menuPopwindowBean.getMenuOperateCodes()));
//                                                }
//                                            }, throwable -> {
//                                            }, () -> HomeFragment.this.getWorkData());
//
//                                });
//                    } else {
//                        getWorkData();
//                    }
//                });
//
//    }

    @Override
    public void listWarnPendingSuccess(CommonBAPListEntity entity) {
        mWarnWorkAdapter.setList(entity.result);
        mWarnWorkAdapter.notifyDataSetChanged();
        refreshController.refreshComplete();
    }

    @Override
    public void listWarnPendingFailed(String errorMsg) {
        refreshController.refreshComplete();
        ToastUtils.show(context,ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void workflowHandleListSuccess(CommonBAPListEntity entity) {
        pendingRecycler.setVisibility(View.GONE);
        processedRecycler.setVisibility(View.VISIBLE);
        mProcessedAdapter.setList(entity.result);
        mProcessedAdapter.notifyDataSetChanged();
        if (entity.result.size() > 0) {
            noDataLayout.setVisibility(View.GONE);
        } else {
            noDataLayout.setVisibility(View.VISIBLE);
        }
        refreshController.refreshComplete();
    }

    @Override
    public void workflowHandleListFailed(String errorMsg) {
        pendingRecycler.setVisibility(View.GONE);
        refreshController.refreshComplete();
        ToastUtils.show(context,ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void listMyMenuSuccess(List entity) {
        workAdapter.setList(entity);
        workAdapter.notifyDataSetChanged();
    }

    @Override
    public void listMyMenuFailed(String errorMsg) {

    }

    @Override
    public void listAllMenuSuccess(List entity) {

    }

    @Override
    public void listAllMenuFailed(String errorMsg) {

    }

}
