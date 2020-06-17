package com.supcon.mes.module_main.ui;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseFragmentActivity;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomTab;
import com.supcon.mes.mbap.view.NoScrollViewPager;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_main.IntentRouter;
import com.supcon.mes.module_main.R;
import com.supcon.mes.module_main.model.api.WarnPendingListAPI;
import com.supcon.mes.module_main.model.bean.WarnDailyWorkEntity;
import com.supcon.mes.module_main.model.contract.WarnPendingListContract;
import com.supcon.mes.module_main.presenter.WarnPendingPresenter;
import com.supcon.mes.module_main.ui.adaper.WarnWorkAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;


/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/6/2
 * Email zhangwenshuai1@supcon.com
 * Desc 预警工作待办
 */
@Router(Constant.Router.WARN_PENDING_LIST)
@Presenter(value = WarnPendingPresenter.class)
public class WarnPendingActivity extends BaseRefreshRecyclerActivity<WarnDailyWorkEntity> implements WarnPendingListContract.View {
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightTv")
    TextView rightTv;
    @BindByTag("contentView")
    RecyclerView contentView;

    private WarnWorkAdapter mWarnWorkAdapter;
    private Map<String, Object> queryParam = new HashMap<>();

    @Override
    protected IListAdapter<WarnDailyWorkEntity> createAdapter() {
        mWarnWorkAdapter = new WarnWorkAdapter(context);
        return mWarnWorkAdapter;
    }
    @Override
    protected int getLayoutID() {
        return R.layout.main_ac_warn_pending_list;
    }
    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(5,context)));
        contentView.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(context.getResources().getString(R.string.main_warn_work));
        rightTv.setText(context.getResources().getString(R.string.main_pending));
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> finish());
        rightTv.setOnClickListener(v -> {
            IntentRouter.go(context, Constant.Router.PENDING_LIST);
            finish();
        });
        refreshListController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenterRouter.create(WarnPendingListAPI.class).listWarnPending(queryParam, 1,20);
            }
        });
//        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
//            @Override
//            public void onRefresh(int pageIndex) {
//                presenterRouter.create(WarnPendingListAPI.class).listWarnPending(queryParam, pageIndex);
//            }
//        });

        mWarnWorkAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {});
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(LoginEvent loginEvent) {
        refreshListController.refreshBegin();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshEvent event) {
        refreshListController.refreshBegin();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void listWarnPendingSuccess(CommonBAPListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void listWarnPendingFailed(String errorMsg) {
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
        refreshListController.refreshComplete();
    }

}
