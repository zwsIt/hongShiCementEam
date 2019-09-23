package com.supcon.mes.module_yhgl.controller;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.view.CustomListWidget;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.AcceptanceCheckEntity;
import com.supcon.mes.middleware.model.bean.AcceptanceCheckListEntity;
import com.supcon.mes.middleware.model.bean.WXGDEntity;
import com.supcon.mes.module_yhgl.IntentRouter;
import com.supcon.mes.module_yhgl.model.api.AcceptanceCheckAPI;
import com.supcon.mes.module_yhgl.model.contract.AcceptanceCheckContract;
import com.supcon.mes.module_yhgl.model.event.ListEvent;
import com.supcon.mes.module_yhgl.presenter.AcceptanceCheckPresenter;
import com.supcon.mes.module_yhgl.ui.adapter.AcceptanceCheckAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangshizhan on 2018/8/28
 * Email:wangshizhan@supcom.com
 */
@Presenter(AcceptanceCheckPresenter.class)
public class AcceptanceCheckController extends BaseViewController implements AcceptanceCheckContract.View {

    @BindByTag("acceptanceCheckListWidget")
    CustomListWidget<AcceptanceCheckEntity> mCustomListWidget;

    private WXGDEntity mWxgdEntity;
    private long id = -1;
    private List<AcceptanceCheckEntity> mAcceptanceCheckEntities = new ArrayList<>();
    private boolean isEditable;

    public AcceptanceCheckController(View rootView) {
        super(rootView);
    }

    @Override
    public void onInit() {
        super.onInit();
        mWxgdEntity = (WXGDEntity) ((Activity) context).getIntent().getSerializableExtra(Constant.IntentKey.WXGD_ENTITY);
        if (mWxgdEntity != null) {
            this.id = mWxgdEntity.id;
        }
    }

    @Override
    public void initView() {
        super.initView();
        mCustomListWidget.setAdapter(new AcceptanceCheckAdapter(context, isEditable));
    }

    @Override
    public void initListener() {
        super.initListener();
        mCustomListWidget.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                Bundle bundle = new Bundle();
                switch (action) {
                    case CustomListWidget.ACTION_VIEW_ALL:
                    case 0:
                        bundle.putBoolean(Constant.IntentKey.IS_EDITABLE, false);
                        bundle.putBoolean(Constant.IntentKey.IS_ADD, false);
                        bundle.putString(Constant.IntentKey.ACCEPTANCE_ENTITIES, mAcceptanceCheckEntities.toString());
                        IntentRouter.go(context, Constant.Router.WXGD_ACCEPTANCE_LIST, bundle);
                        break;
                    default:
                        break;
                }

            }
        });
    }

    @Override
    public void listAcceptanceCheckListSuccess(AcceptanceCheckListEntity entity) {
        for (AcceptanceCheckEntity acceptanceCheckEntity : entity.result) {
            if (acceptanceCheckEntity.remark == null) {
                acceptanceCheckEntity.remark = "";
            }
        }
        mAcceptanceCheckEntities = entity.result;
        if (mCustomListWidget != null) {
//            mCustomListWidget.setData(entity.result);
            if (isEditable) {
                mCustomListWidget.setShowText("编辑 (" + mAcceptanceCheckEntities.size() + ")");
            } else {
                mCustomListWidget.setShowText("查看 (" + mAcceptanceCheckEntities.size() + ")");
            }
        }
        EventBus.getDefault().post(new ListEvent("acceptanceCheckEntity", mAcceptanceCheckEntities));
    }

    @Override
    public void listAcceptanceCheckListFailed(String errorMsg) {
        LogUtil.e("AcceptanceCheckController listAcceptanceCheckListFailed:" + errorMsg);
    }

    public void setCustomListWidget(CustomListWidget<AcceptanceCheckEntity> customListWidget) {
        this.mCustomListWidget = customListWidget;
    }

    @Override
    public void initData() {
        super.initData();
        presenterRouter.create(AcceptanceCheckAPI.class).listAcceptanceCheckList(id);
    }

    public void setWxgdEntity(WXGDEntity mWxgdEntity) {
        this.mWxgdEntity = mWxgdEntity;
        this.id = mWxgdEntity.id;
        presenterRouter.create(AcceptanceCheckAPI.class).listAcceptanceCheckList(id);
    }

    /**
     * @param
     * @return
     * @description 获取验收列表数据
     * @author zhangwenshuai1 2018/9/10
     */
    public List<AcceptanceCheckEntity> getAcceptanceCheckEntities() {
        if (mAcceptanceCheckEntities == null) {
            return new ArrayList<>();
        }
        return mAcceptanceCheckEntities;
    }

    /**
     * @param
     * @return
     * @description 更新列表数据
     * @author zhangwenshuai1 2018/9/10
     */
    public void updateAcceptanceCheckEntities(List<AcceptanceCheckEntity> list) {
        if (list == null) {
            return;
        }
        mAcceptanceCheckEntities = list;
        if (mCustomListWidget != null) {
//            mCustomListWidget.setData(mAcceptanceCheckEntities);
            if (isEditable) {
                mCustomListWidget.setShowText("编辑 (" + list.size() + ")");
            } else {
                mCustomListWidget.setShowText("查看 (" + list.size() + ")");
            }
        }
    }

    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }

    public void clear() {
        mCustomListWidget.clear();
    }
}
