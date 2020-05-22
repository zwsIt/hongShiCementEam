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
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.bean.LubricateOilsEntity;
import com.supcon.mes.middleware.model.bean.WXGDEam;
import com.supcon.mes.middleware.model.bean.YHEntity;
import com.supcon.mes.middleware.model.event.BaseEvent;
import com.supcon.mes.module_wxgd.ui.adapter.LubricateOilsAdapter;
import com.supcon.mes.module_yhgl.IntentRouter;
import com.supcon.mes.module_yhgl.model.api.LubricateOilsAPI;
import com.supcon.mes.module_yhgl.model.bean.LubricateOilsListEntity;
import com.supcon.mes.module_yhgl.model.contract.LubricateOilsContract;
import com.supcon.mes.module_yhgl.model.event.ListEvent;
import com.supcon.mes.module_yhgl.presenter.LubricateOilsPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangshizhan on 2018/8/28
 * Email:wangshizhan@supcom.com
 */
@Presenter(LubricateOilsPresenter.class)
public class LubricateOilsController extends BaseViewController implements LubricateOilsContract.View, LubricateOilsAPI {
    //    private CustomListWidget<LubricateOilsEntity> lubricateOilsListWidget;
    @BindByTag("lubricateOilsListWidget")
    CustomListWidget<LubricateOilsEntity> lubricateOilsListWidget;

    private List<LubricateOilsEntity> mLubricateOilsEntities = new ArrayList<>();
    private boolean isEditable;
    private YHEntity mYHEntity;
    private LubricateOilsAdapter mLubricateOilsAdapter;

    public LubricateOilsAdapter getLubricateOilsAdapter() {
        return mLubricateOilsAdapter;
    }

    public LubricateOilsController(View rootView) {
        super(rootView);
    }

    @Override
    public void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        mYHEntity = (YHEntity) ((Activity) context).getIntent().getSerializableExtra(Constant.IntentKey.YHGL_ENTITY);
    }

    @Override
    public void initView() {
        super.initView();
        mLubricateOilsAdapter = new LubricateOilsAdapter(context, isEditable);
        lubricateOilsListWidget.setAdapter(mLubricateOilsAdapter);
    }

    @Override
    public void initListener() {
        super.initListener();
        lubricateOilsListWidget.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                Bundle bundle = new Bundle();
                switch (action) {
                    case CustomListWidget.ACTION_VIEW_ALL:
                    case 0:
                        bundle.putString(Constant.IntentKey.LUBRICATE_OIL_ENTITIES, mLubricateOilsEntities.toString());
                        bundle.putBoolean(Constant.IntentKey.IS_EDITABLE, isEditable);
                        bundle.putBoolean(Constant.IntentKey.IS_ADD, false);
                        if (mYHEntity.pending != null) {
                            bundle.putString(Constant.IntentKey.TABLE_STATUS, mYHEntity.pending.taskDescription);
                        }
                        if (mYHEntity.getEamID().id != null) {
                            bundle.putLong(Constant.IntentKey.EAM_ID, mYHEntity.getEamID().id);
                        }
                        IntentRouter.go(context, Constant.Router.YHGL_LUBRICATE_OIL_LIST, bundle);
                        break;
//                    case CustomListWidget.ACTION_ITEM_DELETE:
//                        break;
//                    case CustomListWidget.ACTION_EDIT:
//                        bundle.putString(Constant.IntentKey.LUBRICATE_OIL_ENTITIES, lubricateOilsListStr);
//                        bundle.putBoolean(Constant.IntentKey.IS_EDITABLE, true);
//                        bundle.putLong(Constant.IntentKey.REPAIR_SUM, mWXGDEntity.repairSum);
//                        bundle.putString(Constant.IntentKey.TABLE_STATUS, mWXGDEntity.pending.taskDescription);
//                        IntentRouter.go(context, Constant.Router.WXGD_LUBRICATE_OIL_LIST, bundle);
//                        break;
//                    case CustomListWidget.ACTION_ADD:
//                        bundle.putString(Constant.IntentKey.LUBRICATE_OIL_ENTITIES, lubricateOilsListStr);
//                        bundle.putBoolean(Constant.IntentKey.IS_ADD, true);
//                        bundle.putBoolean(Constant.IntentKey.IS_EDITABLE, true);
//                        bundle.putLong(Constant.IntentKey.REPAIR_SUM, mWXGDEntity.repairSum);
//                        bundle.putString(Constant.IntentKey.TABLE_STATUS, mWXGDEntity.pending.taskDescription);
//                        IntentRouter.go(context, Constant.Router.WXGD_LUBRICATE_OIL_LIST, bundle);
//                        break;
                    default:
                        break;
                }

            }
        });
    }

    @Override
    public void listLubricateOilsListSuccess(LubricateOilsListEntity entity) {
        mLubricateOilsEntities = entity.result;
        for (LubricateOilsEntity lubricateOilsEntity : mLubricateOilsEntities) {
            if (lubricateOilsEntity.remark == null) {
                lubricateOilsEntity.remark = "";
            }
        }
        if (lubricateOilsListWidget != null) {
            lubricateOilsListWidget.setData(entity.result);
            if (isEditable) {
                lubricateOilsListWidget.setShowText("编辑 (" + entity.result.size() + ")");
            } else {
                lubricateOilsListWidget.setShowText("查看 (" + entity.result.size() + ")");
            }
        }
        EventBus.getDefault().post(new ListEvent("lubricateOils", mLubricateOilsEntities));
    }

    @Override
    public void listLubricateOilsListFailed(String errorMsg) {
        LogUtil.e("LubricateOilsController listLubricateOilsListFailed:" + errorMsg);
    }

    public void setCustomListWidget(CustomListWidget<LubricateOilsEntity> customListWidget) {
        this.lubricateOilsListWidget = customListWidget;
    }


    @Override
    public void listLubricateOilsList(long id) {
        presenterRouter.create(LubricateOilsAPI.class).listLubricateOilsList(id);
    }

    @Override
    public void initData() {
        super.initData();
        if (mYHEntity.id != null){
            presenterRouter.create(LubricateOilsAPI.class).listLubricateOilsList(mYHEntity.id);
        }
    }

    public void setYHEntity(YHEntity mYHEntity) {
        this.mYHEntity = mYHEntity;
        presenterRouter.create(LubricateOilsAPI.class).listLubricateOilsList(mYHEntity.id);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(BaseEvent baseEvent) {}

    /**
     * @param
     * @return
     * @description 获取润滑油列表
     * @author zhangwenshuai1 2018/9/6
     */
    public List<LubricateOilsEntity> getLubricateOilsEntities() {
        if (mLubricateOilsEntities == null) {
            return new ArrayList<>();
        }
        return mLubricateOilsEntities;
    }

    /**
     * @param
     * @return
     * @description 更新润滑油列表
     * @author zhangwenshuai1 2018/9/6
     */
    public void updateLubricateOilsEntities(List<LubricateOilsEntity> list) {
        if (list == null)
            return;
        this.mLubricateOilsEntities = list;
        if (lubricateOilsListWidget != null) {
//            lubricateOilsListWidget.setData(list);
//            lubricateOilsListWidget.setTotal(list.size());
            lubricateOilsListWidget.setData(list);
            if (isEditable) {
                lubricateOilsListWidget.setShowText("编辑 (" + list.size() + ")");
            } else {
                lubricateOilsListWidget.setShowText("查看 (" + list.size() + ")");
            }
        }
    }

    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }

    public void clear() {
        lubricateOilsListWidget.clear();
    }

    public void upEam(EamEntity eamEntity) {
        mYHEntity.eamID = eamEntity;
    }
}
