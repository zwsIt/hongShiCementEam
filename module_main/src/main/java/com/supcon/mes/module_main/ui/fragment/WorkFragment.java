package com.supcon.mes.module_main.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.BaseConstant;
import com.supcon.common.com_http.util.RxSchedulers;
import com.supcon.common.view.base.fragment.BaseControllerFragment;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.LogUtil;
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
import com.supcon.mes.middleware.model.bean.CommonSearchStaff;
import com.supcon.mes.middleware.model.bean.EamType;
import com.supcon.mes.middleware.model.contract.EamContract;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.model.event.NFCEvent;
import com.supcon.mes.middleware.presenter.EamPresenter;
import com.supcon.mes.middleware.ui.view.MarqueeTextView;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.HtmlParser;
import com.supcon.mes.middleware.util.HtmlTagHandler;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_login.model.bean.WorkInfo;
import com.supcon.mes.module_main.IntentRouter;
import com.supcon.mes.module_main.R;
import com.supcon.mes.module_main.model.api.EamAnomalyAPI;
import com.supcon.mes.module_main.model.api.ScoreStaffAPI;
import com.supcon.mes.module_main.model.api.WaitDealtAPI;
import com.supcon.mes.module_main.model.bean.ScoreEntity;
import com.supcon.mes.module_main.model.bean.WaitDealtEntity;
import com.supcon.mes.module_main.model.bean.WorkNumEntity;
import com.supcon.mes.module_main.model.contract.EamAnomalyContract;
import com.supcon.mes.module_main.model.contract.ScoreStaffContract;
import com.supcon.mes.module_main.model.contract.WaitDealtContract;
import com.supcon.mes.module_main.presenter.EamAnomalyPresenter;
import com.supcon.mes.module_main.presenter.ScoreStaffPresenter;
import com.supcon.mes.module_main.presenter.WaitDealtPresenter;
import com.supcon.mes.module_main.ui.MainActivity;
import com.supcon.mes.module_main.ui.adaper.WaitDealtAdapter;
import com.supcon.mes.module_main.ui.adaper.WorkAdapter;
import com.supcon.mes.module_main.ui.util.MenuHelper;
import com.supcon.mes.module_main.ui.view.MenuPopwindow;
import com.supcon.mes.module_main.ui.view.MenuPopwindowBean;
import com.supcon.mes.push.event.PushRefreshEvent;

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
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by wangshizhan on 2017/8/11.
 */
@Presenter(value = {WaitDealtPresenter.class, EamPresenter.class, ScoreStaffPresenter.class, EamAnomalyPresenter.class})
public class WorkFragment extends BaseControllerFragment implements WaitDealtContract.View, EamContract.View, ScoreStaffContract.View
        , MainActivity.WorkOnTouchListener, EamAnomalyContract.View {

    @BindByTag("workCustomAd")
    CustomAdView workCustomAd;

    @BindByTag("eamTv")
    CustomTextView eamTv;

    //待办
    @BindByTag("waitDealtLayout")
    LinearLayout waitDealtLayout;
    @BindByTag("waitDealtRecycler")
    RecyclerView waitDealtRecycler;
    @BindByTag("workRecycler")
    RecyclerView workRecycler;
    @BindByTag("scoreLayout")
    RelativeLayout scoreLayout;

    @BindByTag("rank")
    TextView rank;
    @BindByTag("score")
    TextView score;

    @BindByTag("workName")
    TextView workName;
    @BindByTag("workDepot")
    TextView workDepot;

    private boolean hidden;
    private WorkAdapter workAdapter;
    private MenuPopwindow menuPopwindow;
    private WaitDealtAdapter waitDealtAdapter;

    private List<MenuPopwindowBean> aewMenu;
    private List<MenuPopwindowBean> lubricateMenu;
    private List<MenuPopwindowBean> repairMenu;
    private List<MenuPopwindowBean> formMenu;
    private ArrayList<WorkInfo> workInfos;
    private ScoreEntity scoreEntity;
    private CommonSearchStaff proxyStaff;
    private CustomDialog customDialog;
    private String reason;
    private MarqueeTextView marqueeTextView;
    private boolean isRefreshing;
    private TextView waitMore;
    private AtomicBoolean atomicBoolean = new AtomicBoolean(false);
    private UserPowerCheckController userPowerCheckController;

    @Override
    protected int getLayoutID() {
        return R.layout.hs_frag_work;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (menuPopwindow != null && menuPopwindow.isShowing()) {
            menuPopwindow.dismiss();
            menuPopwindow.changeWindowAlfa(1f);
            if (oldPosition != -1)
                workRecycler.getChildAt(oldPosition).setSelected(false);
        }
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onResume() {
        super.onResume();
        presenterRouter.create(EamAnomalyAPI.class).getSloganInfo();
        presenterRouter.create(ScoreStaffAPI.class).getPersonScore(String.valueOf(EamApplication.getAccountInfo().getStaffId()));
        if (atomicBoolean.compareAndSet(true, true)) {
            getWorkData();
        }
        workName.setText(EamApplication.getAccountInfo().staffName);
        workDepot.setText(EamApplication.getAccountInfo().positionName);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initView() {
        super.initView();
        Flowable.timer(20, TimeUnit.MILLISECONDS)
                .compose(RxSchedulers.io_main())
                .subscribe(aLong -> initAd());
        View waitTitle = rootView.findViewById(R.id.hs_wait_title);
        ((TextView) waitTitle.findViewById(R.id.contentTitleLabel)).setText("工作提醒");
        waitMore = waitTitle.findViewById(R.id.contentTitleSettingIc);
        waitMore.setVisibility(View.VISIBLE);
        waitMore.setOnClickListener(v -> IntentRouter.go(getActivity(), Constant.Router.WAIT_DEALT));
        View workTitle = rootView.findViewById(R.id.hs_work_title);
        ((TextView) workTitle.findViewById(R.id.contentTitleLabel)).setText("我的工作");
        marqueeTextView = workTitle.findViewById(R.id.contentTitleTips);
        marqueeTextView.setVisibility(View.VISIBLE);

        waitDealtRecycler.setLayoutManager(new LinearLayoutManager(context));
        waitDealtAdapter = new WaitDealtAdapter(getActivity());
        waitDealtRecycler.setAdapter(waitDealtAdapter);

        GridLayoutManager layoutManager = new GridLayoutManager(context, 4);
        workRecycler.setLayoutManager(layoutManager);
        workAdapter = new WorkAdapter(getActivity());
        workRecycler.setAdapter(workAdapter);
        menuPopwindow = new MenuPopwindow(getActivity(), new LinkedList<>());
    }


    @SuppressLint("CheckResult")
    @Override
    protected void initData() {
        super.initData();
        workInfos = new ArrayList<>();
        WorkInfo workInfo1 = new WorkInfo();
        workInfo1.name = "计划巡检";
        workInfo1.iconResId = R.drawable.menu_aew_selector;
        workInfos.add(workInfo1);
        WorkInfo workInfo2 = new WorkInfo();
        workInfo2.name = "设备润滑";
        workInfo2.iconResId = R.drawable.menu_lubricate_selector;
        workInfos.add(workInfo2);
        WorkInfo workInfo3 = new WorkInfo();
        workInfo3.name = "维修执行";
        workInfo3.iconResId = R.drawable.menu_repair_selector;
        workInfos.add(workInfo3);
        WorkInfo workInfo4 = new WorkInfo();
        workInfo4.name = "工作报表";
        workInfo4.iconResId = R.drawable.menu_form_selector;
        workInfos.add(workInfo4);
        workAdapter.setList(workInfos);
        workAdapter.notifyDataSetChanged();

        aewMenu = MenuHelper.getAewMenu();
        lubricateMenu = MenuHelper.getLubricateMenu();
        repairMenu = MenuHelper.getRepairMenu();
        formMenu = MenuHelper.getFormMenu();

        userPowerCheckController = new UserPowerCheckController();
        powerCheck(userPowerCheckController, aewMenu, lubricateMenu, repairMenu, formMenu);
    }


    private void initAd() {
        List<GalleryBean> ads = new ArrayList<>();
        GalleryBean galleryBean = new GalleryBean();
        if (EamApplication.isHongshi()) {
            galleryBean.resId = R.drawable.banner_hssn;
        } else {
            galleryBean.resId = R.drawable.banner_hailuo;
        }
        ads.add(galleryBean);
        workCustomAd.setGalleryBeans(ads);
    }

    int oldPosition = -1;

    @SuppressLint({"CheckResult", "ClickableViewAccessibility"})
    @Override
    protected void initListener() {
        super.initListener();
        workAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
//                if (isRefreshing) {
//                    ToastUtils.show(getActivity(), "正在加载待办,请稍后点击!");
//                    return;
//                }
                if (oldPosition != -1)
                    workRecycler.getChildAt(oldPosition).setSelected(false);
                if (oldPosition == position) {
                    oldPosition = -1;
                    menuPopwindow.changeWindowAlfa(1f);
                    return;
                }
                switch (position) {
                    case 0:
//                        if (!menuPopwindow.refreshList(aewMenu)) return;
//                        menuPopwindow.showPopupWindow(childView, MenuPopwindow.right, 1);
//                        childView.setSelected(true);

                        Flowable.fromIterable(aewMenu)
                                .filter(menuPopwindowBean -> menuPopwindowBean.getType() == Constant.HSWorkType.JHXJ)
                                .subscribe(menuPopwindowBean -> {
                                    oldPosition = -1;
                                    menuPopwindow.changeWindowAlfa(1f);
                                    if (menuPopwindowBean.isPower()) {
                                        IntentRouter.go(getContext(), Constant.Router.JHXJ_LIST);
                                    } else {
                                        ToastUtils.show(context, "巡检模块未分配操作权限!");
                                    }
                                });
                        return;
                    case 1:
                        if (!menuPopwindow.refreshList(lubricateMenu)) return;
                        menuPopwindow.showPopupWindow(childView, MenuPopwindow.right, 0);
                        childView.setSelected(true);
                        break;
                    case 2:
                        if (!menuPopwindow.refreshList(repairMenu)) return;
                        menuPopwindow.showPopupWindow(childView, MenuPopwindow.left, 0);
                        childView.setSelected(true);
                        break;
                    case 3:
                        if (!menuPopwindow.refreshList(formMenu)) return;
                        menuPopwindow.showPopupWindow(childView, MenuPopwindow.left, 1);
                        childView.setSelected(true);
                        break;
                }
                oldPosition = position;
            }
        });

        menuPopwindow.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                menuPopwindow.dismiss();
                menuPopwindow.changeWindowAlfa(1f);
                oldPosition = -1;
                MenuPopwindowBean menuPopwindowBean = (MenuPopwindowBean) obj;
                if (!TextUtils.isEmpty(menuPopwindowBean.getRouter())) {

                    Bundle bundle = new Bundle();
                    switch (menuPopwindowBean.getType()) {
                        case Constant.HSWorkType.DAILY_WXGD:
                            bundle.putString(Constant.IntentKey.REPAIR_TYPE, "日常");
                            break;
                        case Constant.HSWorkType.REPAIR_WXGD:
                            bundle.putString(Constant.IntentKey.REPAIR_TYPE, "检修");
                            break;
                        case Constant.HSWorkType.OHAUL_WXGD:
                            bundle.putString(Constant.IntentKey.REPAIR_TYPE, "大修");
                            break;
                        case Constant.HSWorkType.TD:
                            String tdurl = "http://" + EamApplication.getIp() + ":" + EamApplication.getPort()
                                    + Constant.WebUrl.TD_LIST+"&date="+System.currentTimeMillis();
                            bundle.putString(BaseConstant.WEB_AUTHORIZATION, EamApplication.getAuthorization());
                            bundle.putString(BaseConstant.WEB_COOKIE, EamApplication.getCooki());
                            bundle.putString(BaseConstant.WEB_URL, tdurl);
                            bundle.putBoolean(BaseConstant.WEB_HAS_REFRESH, true);
                            bundle.putBoolean(BaseConstant.WEB_IS_LIST, true);
                            break;
                        case Constant.HSWorkType.SD:
                            String sdurl = "http://" + EamApplication.getIp() + ":" + EamApplication.getPort()
                                    + Constant.WebUrl.SD_LIST+"&date="+System.currentTimeMillis();
                            bundle.putString(BaseConstant.WEB_AUTHORIZATION, EamApplication.getAuthorization());
                            bundle.putString(BaseConstant.WEB_COOKIE, EamApplication.getCooki());
                            bundle.putBoolean(BaseConstant.WEB_HAS_REFRESH, true);
                            bundle.putBoolean(BaseConstant.WEB_IS_LIST, true);
                            bundle.putString(BaseConstant.WEB_URL, sdurl);
                            break;
                    }
                    IntentRouter.go(getContext(), menuPopwindowBean.getRouter(), bundle);
                } else {
                    ToastUtils.show(getActivity(), "正在开发中,敬请期待!");
                }
            }
        });


        menuPopwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (oldPosition != -1)
                    workRecycler.getChildAt(oldPosition).setSelected(false);
            }
        });
        waitDealtAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                WaitDealtEntity waitDealtEntity = (WaitDealtEntity) obj;
                if (childView.getId() == R.id.waitDealtEntrust) {
                    proxyDialog(waitDealtEntity);
                }
            }
        });

        eamTv.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action == -1) {
                    eamTv.setContent("");
                } else {
                    Bundle bundle = new Bundle();
//                    bundle.putBoolean(Constant.IntentKey.IS_MAIN_EAM, true);
                    bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG, "Main");
                    IntentRouter.go(getActivity(), Constant.Router.EAM, bundle);
                }
            }
        });

        scoreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scoreEntity != null && !TextUtils.isEmpty(scoreEntity.type)) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constant.IntentKey.RANKING, scoreEntity.ranking != null ? scoreEntity.ranking : -1);
                    bundle.putString(Constant.IntentKey.TYPE, scoreEntity.type);
                    IntentRouter.go(getActivity(), Constant.Router.RANKING, bundle);
                } else {
                    ToastUtils.show(getActivity(), "未获取到当前用户评分，不能查看排名！");
                }
            }
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
                            proxyStaff = null;
                        }
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG, "Main");
                        IntentRouter.go(context, Constant.Router.STAFF, bundle);
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
                        if (proxyStaff == null) {
                            ToastUtils.show(getActivity(), "请选择委托人");
                            return;
                        }
                        if (waitDealtEntity.pendingid == null) {
                            ToastUtils.show(getActivity(), "未获取当前代办信息");
                            return;
                        }
                        onLoading("正在委托...");
                        presenterRouter.create(WaitDealtAPI.class).proxyPending(waitDealtEntity.pendingid, proxyStaff.userId, reason);
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
                } else if (commonSearchEvent.commonSearchEntity instanceof EamType) {
                    EamType eamType = (EamType) commonSearchEvent.commonSearchEntity;
                    eamTv.setContent(Util.strFormat(eamType.name));
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constant.IntentKey.EAM, eamType);
                    IntentRouter.go(getActivity(), Constant.Router.EAM_DETAIL, bundle);
                }
            }
        }
    }

    @Override
    public boolean onTouch(MotionEvent ev) {
        boolean isClickWorkRecycler = inRangeOfView(workRecycler, ev);
        if (!isClickWorkRecycler) {
            menuPopwindow.changeWindowAlfa(1f);
            oldPosition = -1;
        }
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
                eamTv.setContent((String) nfcJson.get("textRecord"));
                Map<String, Object> params = new HashMap<>();
                params.put(Constant.IntentKey.EAM_CODE, nfcJson.get("textRecord"));
                presenterRouter.create(EamAPI.class).getEam(params, 1);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(LoginEvent loginEvent) {
        if (!atomicBoolean.get()) {
            powerCheck(userPowerCheckController, aewMenu, lubricateMenu, repairMenu, formMenu);
        } else {
            getWorkData();
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void refreshPush(RefreshEvent event) {
//        getWorkData();
//    }

    //有推送待办过来刷新界面
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshPush(PushRefreshEvent event) {
        getWorkData();
    }

    private void getWorkData() {
        presenterRouter.create(WaitDealtAPI.class).getWaitDealt(1, 3, new HashMap<>());
        presenterRouter.create(EamAnomalyAPI.class).getMainWorkCount(String.valueOf(EamApplication.getAccountInfo().getStaffId()));
        isRefreshing = true;
    }

    @Override
    public void getWaitDealtSuccess(CommonBAPListEntity entity) {
        isRefreshing = false;
        if (entity.result.size() > 0) {
            waitDealtLayout.setVisibility(View.GONE);
        } else {
            waitDealtLayout.setVisibility(View.VISIBLE);
        }
        waitDealtAdapter.setList(entity.result);
        waitDealtAdapter.notifyDataSetChanged();
        if (entity.totalCount > 0) {
            Spanned item = HtmlParser.buildSpannedText(String.format(context.getString(R.string.device_style15), "更多", entity.totalCount), new HtmlTagHandler());
            waitMore.setText(item);
        } else {
            waitMore.setText("更多");
        }

    }

    @Override
    public void getWaitDealtFailed(String errorMsg) {
        isRefreshing = false;
        LogUtil.e("获取待办失败:" + errorMsg);
        if (errorMsg.contains("401")) {
            SnackbarHelper.showError(rootView, ErrorMsgHelper.msgParse(errorMsg));
        }
        waitDealtLayout.setVisibility(View.VISIBLE);
        waitDealtAdapter.setList(new ArrayList<>());
        waitDealtAdapter.notifyDataSetChanged();
    }

    @Override
    public void proxyPendingSuccess(BapResultEntity entity) {
        onLoadSuccess("待办委托成功");
        presenterRouter.create(WaitDealtAPI.class).getWaitDealt(1, 3, new HashMap<>());
    }

    @Override
    public void proxyPendingFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }


    @Override
    public void getEamSuccess(CommonListEntity entity) {
        if (entity.result.size() > 0) {
            EamType eamType = (EamType) entity.result.get(0);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.IntentKey.EAM, eamType);
            IntentRouter.go(getActivity(), Constant.Router.EAM_DETAIL, bundle);
            return;
        }
        SnackbarHelper.showError(rootView, "未查询到设备");
    }

    @Override
    public void getEamFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, ErrorMsgHelper.msgParse(errorMsg));
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
        List result = entity.result;
        if (result.size() > 0) {
            updateNum(aewMenu, result, workInfos.get(0));
            updateNum(lubricateMenu, result, workInfos.get(1));
            updateNum(repairMenu, result, workInfos.get(2));
            updateNum(formMenu, result, workInfos.get(3));
            workAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getMainWorkCountFailed(String errorMsg) {
        LogUtil.e(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void getSloganInfoSuccess(CommonEntity entity) {
        String result = (String) entity.result;
        if (!TextUtils.isEmpty(result)) {
            marqueeTextView.setText(result);
        } else {
            marqueeTextView.setText("祝您工作愉快，生活幸福!");
        }
    }

    @Override
    public void getSloganInfoFailed(String errorMsg) {
        marqueeTextView.setText("祝您工作愉快，生活幸福!");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (menuPopwindow != null && menuPopwindow.isShowing()) {
            menuPopwindow.dismiss();
            menuPopwindow.changeWindowAlfa(1f);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @SuppressLint("CheckResult")
    private void updateNum(List<MenuPopwindowBean> menuPopwindowBeans, List<WorkNumEntity> workNumEntities, WorkInfo workInfo) {
        workInfo.num = 0;
        Flowable.fromIterable(workNumEntities).flatMap(new Function<WorkNumEntity, Publisher<?>>() {
            @Override
            public Publisher<?> apply(WorkNumEntity workNumEntity) throws Exception {
                Flowable<MenuPopwindowBean> filter = Flowable.fromIterable(menuPopwindowBeans)
                        .filter(new Predicate<MenuPopwindowBean>() {
                            @Override
                            public boolean test(MenuPopwindowBean menuPopwindowBean) throws Exception {
                                if (TextUtils.isEmpty(menuPopwindowBean.getTag()) || TextUtils.isEmpty(workNumEntity.tagName) || !menuPopwindowBean.isPower()) {
                                    return false;
                                }
                                if (menuPopwindowBean.getTag().equals(workNumEntity.tagName)) {
                                    menuPopwindowBean.setNum(workNumEntity.num);
                                    workInfo.num += workNumEntity.num;
                                }
                                return true;
                            }
                        });
                return filter;
            }
        }).subscribe();
    }

    @SuppressLint("CheckResult")
    private void powerCheck(UserPowerCheckController userPowerCheckController, List<MenuPopwindowBean>... menuPopwindowBeans) {
        List<MenuPopwindowBean> menuAllPopwindows = new ArrayList<>();
        for (List<MenuPopwindowBean> menuPopwindows : menuPopwindowBeans) {
            menuAllPopwindows.addAll(menuPopwindows);
        }
        StringBuilder menuOperateCodes = new StringBuilder();
        Flowable.fromIterable(menuAllPopwindows)
                .filter(menuPopwindowBean -> {
                    if (!TextUtils.isEmpty(menuPopwindowBean.getMenuOperateCodes())) {
                        return true;
                    }
                    return false;
                })
                .subscribe(menuPopwindowBean -> menuOperateCodes.append(menuPopwindowBean.getMenuOperateCodes()).append(","), throwable -> {
                }, () -> {
                    if (!TextUtils.isEmpty(menuOperateCodes)) {
                        menuOperateCodes.deleteCharAt(menuOperateCodes.length() - 1);
                        userPowerCheckController.checkModulePermission(EamApplication.getAccountInfo().cid, menuOperateCodes.toString()
                                , result -> {
                                    atomicBoolean.set(true);
                                    Flowable.fromIterable(Arrays.asList(menuPopwindowBeans))
                                            .flatMap(new Function<List<MenuPopwindowBean>, Flowable<MenuPopwindowBean>>() {
                                                @Override
                                                public Flowable<MenuPopwindowBean> apply(List<MenuPopwindowBean> menuPopwindowBeans1) throws Exception {
                                                    return Flowable.fromIterable(menuPopwindowBeans1);
                                                }
                                            })
                                            .filter(new Predicate<MenuPopwindowBean>() {
                                                @Override
                                                public boolean test(MenuPopwindowBean menuPopwindowBean) throws Exception {
                                                    if (result.containsKey(menuPopwindowBean.getMenuOperateCodes())) {
                                                        return true;
                                                    }
                                                    return false;
                                                }
                                            })
                                            .subscribe(new Consumer<MenuPopwindowBean>() {
                                                @Override
                                                public void accept(MenuPopwindowBean menuPopwindowBean) throws Exception {
                                                    menuPopwindowBean.setPower(result.get(menuPopwindowBean.getMenuOperateCodes()));
                                                }
                                            }, throwable -> {
                                            }, () -> getWorkData());

                                });
                    } else {
                        getWorkData();
                    }
                });

    }
}
