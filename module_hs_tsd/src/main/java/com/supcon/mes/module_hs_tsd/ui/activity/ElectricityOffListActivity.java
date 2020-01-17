package com.supcon.mes.module_hs_tsd.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.ptr.PtrFrameLayout;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.picker.SinglePicker;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.constant.QueryBtnType;
import com.supcon.mes.middleware.controller.ModulePermissonCheckController;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.middleware.model.event.NFCEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.FilterHelper;
import com.supcon.mes.middleware.util.NFCHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_hs_tsd.IntentRouter;
import com.supcon.mes.module_hs_tsd.R;
import com.supcon.mes.module_hs_tsd.model.api.ElectricityOffOnListAPI;
import com.supcon.mes.module_hs_tsd.model.bean.ElectricityOffOnEntity;
import com.supcon.mes.module_hs_tsd.model.bean.ElectricityOffOnListEntity;
import com.supcon.mes.module_hs_tsd.model.contract.ElectricityOffOnListContract;
import com.supcon.mes.module_hs_tsd.presenter.ElectricityOffListPresenter;
import com.supcon.mes.module_hs_tsd.ui.adapter.ElectricityOffOnListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/27
 * Email zhangwenshuai1@supcon.com
 * Desc 停电列表
 */
@Router(value = Constant.Router.HS_TD_LIST)
@Controller(value = {ModulePermissonCheckController.class})
@Presenter(value = {ElectricityOffListPresenter.class})
public class ElectricityOffListActivity extends BaseRefreshRecyclerActivity<ElectricityOffOnEntity> implements ElectricityOffOnListContract.View {

    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("rightBtn")
    ImageButton rightBtn;
    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;
    @BindByTag("radioGroupFilter")
    RadioGroup radioGroupFilter;
    @BindByTag("contentView")
    RecyclerView contentView;
    @BindByTag("refreshFrameLayout")
    PtrFrameLayout refreshFrameLayout;

    private Map<String, Object> queryParam = new HashMap<>();
    private boolean pendingQuery = true;
    private ElectricityOffOnListAdapter mElectricityOffListAdapter;
    private Long deploymentId;
    private NFCHelper nfcHelper;
    private boolean isFront;
    private EamEntity mEamEntity;

    @Override
    protected void onInit() {
        super.onInit();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        EventBus.getDefault().register(this);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(5, context)));

        mEamEntity = (EamEntity) getIntent().getSerializableExtra(Constant.IntentKey.EAM); // 设备主页传参
        if (mEamEntity != null) {
            queryParam.put(Constant.BAPQuery.EAM_NAME, mEamEntity.name);
            searchTitleBar.searchView().setInput(mEamEntity.name);
        }

        nfcHelper = NFCHelper.getInstance();
        nfcHelper.setup(this);
        nfcHelper.setOnNFCListener(nfc -> EventBus.getDefault().post(new NFCEvent(nfc)));
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_elec_off_list;
    }

    @Override
    protected IListAdapter<ElectricityOffOnEntity> createAdapter() {
        mElectricityOffListAdapter = new ElectricityOffOnListAdapter(context);
        return mElectricityOffListAdapter;
    }

    @Override
    protected void initView() {
        super.initView();
        searchTitleBar.setTitleText("停电票");
        searchTitleBar.searchView().setHint("请输入设备名称");
        searchTitleBar.searchView().setInputTextColor(R.color.black);
        FilterHelper.addView(this, radioGroupFilter, FilterHelper.queryBtn());
        getController(ModulePermissonCheckController.class).checkModulePermission(EamApplication.getUserName().toLowerCase(), "EleOnWorkFlow", result -> {
            if (result == null) {
                searchTitleBar.disableRightBtn();
            }
            deploymentId = result;
        }, null);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> onBackPressed());
        RxView.clicks(rightBtn)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        // 制定单据
                        Bundle bundle = new Bundle();
                        bundle.putLong(Constant.IntentKey.DEPLOYMENT_ID, deploymentId);
                        IntentRouter.go(context, Constant.Router.HS_ELE_OFF_EDIT, bundle);
                    }
                });
        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {
                presenterRouter.create(ElectricityOffOnListAPI.class).listElectricityOffOn(pageIndex, queryParam, pendingQuery, false);
            }
        });
        RxTextView.textChanges(searchTitleBar.editText())
                .skipInitialValue()
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        queryParam.put(Constant.BAPQuery.EAM_NAME, charSequence.toString().trim());
                        doFilter();
                    }
                });

        radioGroupFilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == QueryBtnType.ALL_QUERY.getType()) {
                    pendingQuery = false;
                } else {
                    pendingQuery = true;
                }
                doFilter();
            }
        });

        mElectricityOffListAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                ElectricityOffOnEntity entity = (ElectricityOffOnEntity) obj;
                goDetail(entity);
            }
        });
    }

    /**
     * @param
     * @return
     * @description 跳转单据
     * @author zhangwenshuai1 2020/1/10
     */
    private void goDetail(ElectricityOffOnEntity electricityOffOnEntity) {
        PendingEntity pendingEntity = electricityOffOnEntity.getPending();
        if (pendingEntity == null) {
            ToastUtils.show(context, "代办为空,请刷新");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putLong(Constant.IntentKey.TABLE_ID, electricityOffOnEntity.getId());
        if (electricityOffOnEntity.getPending().id == null || electricityOffOnEntity.getPending().openUrl == null) { // 无代办、生效
            IntentRouter.go(context, Constant.Router.HS_ELE_OFF_VIEW, bundle);
        } else {
            bundle.putLong(Constant.IntentKey.PENDING_ID, electricityOffOnEntity.getPending().id);
            switch (electricityOffOnEntity.getPending().openUrl) {
                case Constant.HSEleOffView.EDIT_URL:
                    IntentRouter.go(context, Constant.Router.HS_ELE_OFF_EDIT, bundle);
                    break;
                case Constant.HSEleOffView.PREVIEW_URL:
                default:
                    IntentRouter.go(context, Constant.Router.HS_ELE_OFF_VIEW, bundle);
            }
            /*switch (electricityOffOnEntity.getPending().taskDescription) {
                case Constant.TableStatus_CH.ELE_OFF:
                    IntentRouter.go(context,Constant.Router.HS_ELE_OFF_EDIT,bundle);
                    break;
                case Constant.TableStatus_CH.REVIEW:
                case Constant.TableStatus_CH.REVIEW1:
                    bundle.putBoolean(Constant.IntentKey.IS_EDITABLE,true);
                case Constant.TableStatus_CH.TAKE_EFFECT:
                default:
                    IntentRouter.go(context,Constant.Router.HS_ELE_OFF_VIEW,bundle);
                    break;
            }*/
        }
    }

    private void doFilter() {
        refreshListController.refreshBegin();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (nfcHelper != null)
            nfcHelper.onResumeNFC(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        isFront = false;

        if (nfcHelper != null)
            nfcHelper.onPauseNFC(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        isFront = true;
        //获取到Tag对象
        if (nfcHelper != null)
            nfcHelper.dealNFCTag(intent);
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
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshEvent event) {
        refreshListController.refreshBegin();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getNFC(NFCEvent nfcEvent) {
        Map<String, Object> nfcJson = Util.gsonToMaps(nfcEvent.getNfc());
        dealGo((String) nfcJson.get("textRecord"));
    }

    /**
     * @param
     * @return
     * @description 刷卡跳转详情页面
     * @author zhangwenshuai1 2020/1/10
     */
    private void dealGo(String code) {
        if (!isFront) {
            return;
        }
        if (mElectricityOffListAdapter != null && mElectricityOffListAdapter.getList() != null && mElectricityOffListAdapter.getList().size() > 0) {
            List<ElectricityOffOnEntity> tempEntityList = new ArrayList<>();
            List<String> tableNoList = new ArrayList<>();
            for (ElectricityOffOnEntity entity : mElectricityOffListAdapter.getList()) {
                if (code.equals(entity.getEamID().code)) {
                    tempEntityList.add(entity);
                    tableNoList.add(entity.getTableNo());
                }
            }

            if (tempEntityList.size() == 0) {
                ToastUtils.show(context, "未能匹配到当前RFID标签的设备，请检查确认");
            } else if (tempEntityList.size() == 1) {
                goDetail(tempEntityList.get(0));
            } else {

                SinglePickController singlePickController = new SinglePickController<>(this);
                singlePickController.setCanceledOnTouchOutside(true);
                singlePickController.setDividerVisible(true);
                singlePickController.setDividerVisible(true);
                singlePickController.list(tableNoList)
                        .listener(new SinglePicker.OnItemPickListener() {
                            @Override
                            public void onItemPicked(int index, Object item) {
                                goDetail(tempEntityList.get(index));
                            }
                        }).show();
            }

        } else {
            ToastUtils.show(context, "暂无数据");
        }

    }

    @Override
    public void listElectricityOffOnSuccess(ElectricityOffOnListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void listElectricityOffOnFailed(String errorMsg) {
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
        refreshListController.refreshComplete();
    }
}
