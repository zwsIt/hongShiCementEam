package com.supcon.mes.module_olxj.controller;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.model.api.DeviceDCSParamQueryAPI;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.contract.DeviceDCSParamQueryContract;
import com.supcon.mes.middleware.presenter.DeviceDCSParamQueryPresenter;
import com.supcon.mes.module_olxj.ui.adapter.DeviceDCSParamAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by wangshizhan on 2019/5/28
 * Email:wangshizhan@supcom.com
 */
@Presenter(DeviceDCSParamQueryPresenter.class)
public class DeviceDCSParamController extends BaseViewController implements DeviceDCSParamQueryContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;

    private DeviceDCSParamAdapter mDeviceDCSParamAdapter;

    private Long eamId;
    private boolean isSwitching = false;
    Disposable timer;

    public DeviceDCSParamController(View rootView, Long eamId) {
        this(rootView);
        this.eamId = eamId;
    }

    public DeviceDCSParamController(View rootView) {
        super(rootView);
    }

    @Override
    public void onInit() {
        super.onInit();
    }

    @Override
    public void initView() {
        super.initView();
        mDeviceDCSParamAdapter = new DeviceDCSParamAdapter(context);
        contentView.setLayoutManager(new LinearLayoutManager(context));  //线性布局
//        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(1, context)));  //列表项之间间隔
        contentView.setAdapter(mDeviceDCSParamAdapter);
    }


    @Override
    public void initData() {
        super.initData();

        if (eamId != null)
            presenterRouter.create(DeviceDCSParamQueryAPI.class).getDeviceDCSParams(eamId);
    }

    @SuppressLint("CheckResult")
    @Override
    public void getDeviceDCSParamsSuccess(CommonListEntity entity) {
        mDeviceDCSParamAdapter.setList(entity.result);
        mDeviceDCSParamAdapter.notifyDataSetChanged();
        Flowable.timer(200, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        EventBus.getDefault().post(entity);
                    }
                });
//        if(entity.result!=null) {
//            ViewGroup.LayoutParams lp = contentView.getLayoutParams();
//            lp.height = DisplayUtil.dip2px(20 * entity.result.size(), context);
//            contentView.setLayoutParams(lp);
//        }
        if (!isSwitching) {
            //去掉30秒刷新一次，现在只进入时候获取dcs数据
//            resetTimer();
        }
    }


    private void startTimer() {
        LogUtil.i("DeviceDCSParamController startTimer");
        timer = Flowable.timer(60, TimeUnit.SECONDS)
                .subscribe(aLong ->
                        presenterRouter.create(DeviceDCSParamQueryAPI.class).getDeviceDCSParams(eamId));
    }

    private void stopTimer() {
        LogUtil.i("DeviceDCSParamController stopTimer");
        if (timer != null) {
            timer.dispose();
            timer = null;
        }
    }

    private void resetTimer() {
        LogUtil.i("DeviceDCSParamController resetTimer");
        stopTimer();
        startTimer();
    }

    @Override
    public void getDeviceDCSParamsFailed(String errorMsg) {
        LogUtil.e("errorMsg:" + errorMsg);

    }

    public void clear() {
        mDeviceDCSParamAdapter.clear();
        mDeviceDCSParamAdapter.notifyDataSetChanged();
    }

    public void getDeviceParams(Long eamId) {
        if (eamId != null && !eamId.equals(this.eamId)) {
            isSwitching = true;
            clear();
            stopTimer();
            presenterRouter.create(DeviceDCSParamQueryAPI.class).getDeviceDCSParams(eamId);
            isSwitching = false;
        }

    }
}
