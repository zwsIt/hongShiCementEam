package com.supcon.mes.module_main.ui;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.EamType;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_main.R;
import com.supcon.mes.module_main.model.api.AnomalyAPI;
import com.supcon.mes.module_main.model.bean.AnomalyEntity;
import com.supcon.mes.module_main.model.contract.AnomalyContract;
import com.supcon.mes.module_main.presenter.AnomalyPresenter;
import com.supcon.mes.module_main.ui.adaper.AnomalyAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/7/25
 * ------------- Description -------------
 */
@Presenter(value = AnomalyPresenter.class)
@Router(value = Constant.Router.ANOMALY)
public class AnomalyActivity extends BaseRefreshRecyclerActivity<AnomalyEntity> implements AnomalyContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;

    private AnomalyAdapter anomalyAdapter;
    private EamType eamType;

    @Override
    protected IListAdapter<AnomalyEntity> createAdapter() {
        anomalyAdapter = new AnomalyAdapter(this);
        return anomalyAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.hs_ac_anomaly;
    }

    @Override
    protected void onInit() {
        super.onInit();
        eamType = (EamType) getIntent().getSerializableExtra(Constant.IntentKey.EAM);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));
        contentView.setLayoutManager(new LinearLayoutManager(context));
        titleText.setText("异常记录");
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        back();
                    }
                });
        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {
                Map<String, Object> param = new HashMap<>();
                param.put(Constant.BAPQuery.EAMCODE, eamType.code);
                presenterRouter.create(AnomalyAPI.class).getAnomalyList(pageIndex, 20, param);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(LoginEvent loginEvent) {

        refreshListController.refreshBegin();
    }


    @Override
    public void getAnomalyListSuccess(CommonBAPListEntity entity) {
        if (entity.result.size() > 0) {
            refreshListController.refreshComplete(entity.result);
        } else {
            refreshListController.refreshComplete(null);
        }
    }

    @Override
    public void getAnomalyListFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, ErrorMsgHelper.msgParse(errorMsg));
        refreshListController.refreshComplete(null);
    }

}
