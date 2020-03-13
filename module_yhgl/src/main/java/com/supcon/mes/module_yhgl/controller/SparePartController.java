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
import com.supcon.mes.middleware.model.bean.SparePartEntity;
import com.supcon.mes.middleware.model.bean.WXGDEam;
import com.supcon.mes.middleware.model.bean.YHEntity;
import com.supcon.mes.middleware.model.event.BaseEvent;
import com.supcon.mes.module_yhgl.IntentRouter;
import com.supcon.mes.module_yhgl.model.api.SparePartAPI;
import com.supcon.mes.module_yhgl.model.bean.SparePartListEntity;
import com.supcon.mes.module_yhgl.model.contract.SparePartContract;
import com.supcon.mes.module_yhgl.model.event.ListEvent;
import com.supcon.mes.module_yhgl.presenter.SparePartPresenter;
import com.supcon.mes.module_yhgl.ui.adapter.SparePartAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangshizhan on 2018/8/28
 * Email:wangshizhan@supcom.com
 */
@Presenter(SparePartPresenter.class)
public class SparePartController extends BaseViewController implements SparePartContract.View {

    private List<SparePartEntity> mSparePartEntities = new ArrayList<>();
    private boolean editable;
    private YHEntity mYHEntity;

    @BindByTag("sparePartListWidget")
    CustomListWidget<SparePartEntity> sparePartListWidget;

    public SparePartController(View rootView) {
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
        sparePartListWidget.setAdapter(new SparePartAdapter(context, false));
    }

    @Override
    public void initListener() {
        super.initListener();
        sparePartListWidget.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                Bundle bundle = new Bundle();
                switch (action) {
                    case CustomListWidget.ACTION_VIEW_ALL:
                    case 0:
                        bundle.putString(Constant.IntentKey.SPARE_PART_ENTITIES, mSparePartEntities.toString());
                        bundle.putBoolean(Constant.IntentKey.IS_EDITABLE, editable);
                        bundle.putBoolean(Constant.IntentKey.IS_ADD, false);
                        if (mYHEntity.pending != null) {
                            bundle.putString(Constant.IntentKey.TABLE_STATUS, mYHEntity.pending.taskDescription);
                            bundle.putString(Constant.IntentKey.TABLE_ACTION, mYHEntity.pending.openUrl);
                        }
                        bundle.putLong(Constant.IntentKey.LIST_ID, mYHEntity.id == null ? -1L : mYHEntity.id);
                        if (mYHEntity.getEamID().id != null) {
                            bundle.putLong(Constant.IntentKey.EAM_ID, mYHEntity.getEamID().id);
                        }
                        IntentRouter.go(context, Constant.Router.YHGL_SPARE_PART_LIST, bundle);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void listSparePartListSuccess(SparePartListEntity entity) {
        mSparePartEntities = entity.result;
        for (SparePartEntity sparePartEntity : mSparePartEntities) {
            if (sparePartEntity.remark == null) {
                sparePartEntity.remark = "";
            }
            if (sparePartEntity.sum != null) {
                sparePartEntity.sum = sparePartEntity.sum.setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            if (sparePartEntity.actualQuantity != null) {
                sparePartEntity.actualQuantity = sparePartEntity.actualQuantity.setScale(2, BigDecimal.ROUND_HALF_UP);
            }
        }
        if (sparePartListWidget != null) {
//            sparePartListWidget.setData(entity.result);
//            sparePartListWidget.setTotal(entity.result.size());
            sparePartListWidget.setData(entity.result);
            if (editable) {
                sparePartListWidget.setShowText("编辑 (" + entity.result.size() + ")");
            } else {
                sparePartListWidget.setShowText("查看 (" + entity.result.size() + ")");
            }
        }
        EventBus.getDefault().post(new ListEvent("sparePart", mSparePartEntities));
    }

    @Override
    public void listSparePartListFailed(String errorMsg) {
        LogUtil.e("SparePartController listSparePartListFailed:" + errorMsg);
    }

    public void setCustomListWidget(CustomListWidget<SparePartEntity> customListWidget) {
        this.sparePartListWidget = customListWidget;
    }

    @Override
    public void initData() {
        super.initData();
        if (mYHEntity.id != null) {
            presenterRouter.create(SparePartAPI.class).listSparePartList(mYHEntity.id);
        }
    }

    public void setYHEntity(YHEntity mYHEntity) {
        this.mYHEntity = mYHEntity;
        presenterRouter.create(SparePartAPI.class).listSparePartList(mYHEntity.id);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(BaseEvent baseEvent) {
    }

    //获取备件数据
    public List<SparePartEntity> getSparePartEntities() {
        if (mSparePartEntities == null) {
            return new ArrayList<>();
        }
        return mSparePartEntities;
    }

    //更新备件数据
    public void updateSparePartEntities(List<SparePartEntity> mSparePartEntities) {
        if (mSparePartEntities == null)
            return;
        this.mSparePartEntities = mSparePartEntities;
        if (sparePartListWidget != null) {
//            sparePartListWidget.setData(mSparePartEntities);
//            sparePartListWidget.setTotal(mSparePartEntities.size());
            sparePartListWidget.setData(mSparePartEntities);
            if (editable) {
                sparePartListWidget.setShowText("编辑 (" + mSparePartEntities.size() + ")");
            } else {
                sparePartListWidget.setShowText("查看 (" + mSparePartEntities.size() + ")");
            }
        }

    }

    public void setEditable(boolean isEditable) {
        this.editable = isEditable;
    }

    public void clear() {
        sparePartListWidget.clear();
    }

    public void upEam(WXGDEam wxgdEam) {
        mYHEntity.eamID = wxgdEam;
    }
}
