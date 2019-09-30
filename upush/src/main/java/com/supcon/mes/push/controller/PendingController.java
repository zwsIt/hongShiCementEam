package com.supcon.mes.push.controller;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.app.annotation.Presenter;
import com.supcon.common.BaseConstant;
import com.supcon.common.view.base.controller.BaseDataController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.IntentRouter;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.WXGDEntity;
import com.supcon.mes.middleware.model.bean.YHEntity;
import com.supcon.mes.push.event.PushOpenEvent;
import com.supcon.mes.push.model.api.PendingQueryAPI;
import com.supcon.mes.push.model.bean.PendingPushEntity;
import com.supcon.mes.push.model.bean.PushEntity;
import com.supcon.mes.push.model.contract.PendingQueryContract;
import com.supcon.mes.push.presenter.PendingQueryPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by wangshizhan on 2019/4/30
 * Email:wangshizhan@supcom.com
 */
@Presenter(PendingQueryPresenter.class)
public class PendingController extends BaseDataController implements PendingQueryContract.View {

    private PendingPushEntity mPendingEntity;

    public PendingController(Context context) {
        super(context);
    }

    @Override
    public void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPushOpenEvent(PushOpenEvent pushOpenEvent) {
        PushEntity pushEntity = GsonUtil.gsonToBean(pushOpenEvent.getContent(), PushEntity.class);
        LogUtil.d("pushEntity :" + pushEntity);

        if (pushEntity.extra != null) {
            queryEntieyAndGo(pushEntity.extra);
        }

    }

    public void queryEntieyAndGo(PendingPushEntity pendingEntity) {

        if (TextUtils.isEmpty(pendingEntity.deploymentName)) {
            return;
        }

        mPendingEntity = pendingEntity;
        if (pendingEntity.deploymentName.contains("隐患管理")) {
            presenterRouter.create(PendingQueryAPI.class).queryYH(pendingEntity.tableNo);
        } else if (pendingEntity.deploymentName.contains("工单")) {
            presenterRouter.create(PendingQueryAPI.class).queryWXGD(pendingEntity.tableNo);
        } else if (pendingEntity.deploymentName.contains("停电")) {

            String url = "http://" + EamApplication.getIp() + ":" + EamApplication.getPort()
                    + Constant.WebUrl.TD_LIST;

            Bundle bundle = new Bundle();
            bundle.putString(BaseConstant.WEB_AUTHORIZATION, EamApplication.getAuthorization());
            bundle.putString(BaseConstant.WEB_COOKIE, EamApplication.getCooki());
            bundle.putBoolean(BaseConstant.WEB_HAS_REFRESH, true);
            bundle.putBoolean(BaseConstant.WEB_IS_LIST, true);
            bundle.putString(BaseConstant.WEB_URL, url);
            IntentRouter.go(context, Constant.Router.TD, bundle);
        } else if (pendingEntity.deploymentName.contains("送电")) {

            String url = "http://" + EamApplication.getIp() + ":" + EamApplication.getPort()
                    + Constant.WebUrl.SD_LIST;

            Bundle bundle = new Bundle();
            bundle.putString(BaseConstant.WEB_AUTHORIZATION, EamApplication.getAuthorization());
            bundle.putString(BaseConstant.WEB_COOKIE, EamApplication.getCooki());
            bundle.putBoolean(BaseConstant.WEB_HAS_REFRESH, true);
            bundle.putBoolean(BaseConstant.WEB_IS_LIST, true);
            bundle.putString(BaseConstant.WEB_URL, url);
            IntentRouter.go(context, Constant.Router.SD, bundle);
        }

    }

    @Override
    public void queryYHSuccess(CommonBAPListEntity entity) {
        if (entity.result.size() != 0) {
            Bundle bundle = new Bundle();
            if (mPendingEntity.deploymentName.contains("隐患管理")) {
                bundle.putSerializable(Constant.IntentKey.YHGL_ENTITY, (YHEntity) entity.result.get(0));
                if (mPendingEntity.nowStatus.equals("编辑")) {
                    IntentRouter.go(context, Constant.Router.YH_EDIT, bundle);
                } else if (mPendingEntity.nowStatus.equals("审核")) {
                    IntentRouter.go(context, Constant.Router.YH_VIEW, bundle);
                }
            }
        }
    }

    @Override
    public void queryYHFailed(String errorMsg) {
        LogUtil.e("PendingController:" + errorMsg);
    }

    @Override
    public void queryWXGDSuccess(CommonBAPListEntity entity) {
        if (entity.result.size() != 0) {
            Bundle bundle = new Bundle();
            if (mPendingEntity.deploymentName.contains("工单")) {
                WXGDEntity wxgdEntity = (WXGDEntity) entity.result.get(0);
                bundle.putSerializable(Constant.IntentKey.WXGD_ENTITY, wxgdEntity);

                switch (mPendingEntity.nowStatus) {
                    case "接单":
                    case "接单(确认)":
                        IntentRouter.go(context, Constant.Router.WXGD_RECEIVE, bundle);
                        break;
                    case "派工":
                        IntentRouter.go(context, Constant.Router.WXGD_DISPATCHER, bundle);
                        break;
                    case "通知":
                        bundle.putBoolean(Constant.IntentKey.isEdit, false);
                    case "执行":
                        IntentRouter.go(context, Constant.Router.WXGD_EXECUTE, bundle);
                        break;
                    case "验收":
                        IntentRouter.go(context, Constant.Router.WXGD_ACCEPTANCE, bundle);
                        break;
                }
            }
        }
    }

    @Override
    public void queryWXGDFailed(String errorMsg) {
        LogUtil.e("PendingController:" + errorMsg);
    }
}
