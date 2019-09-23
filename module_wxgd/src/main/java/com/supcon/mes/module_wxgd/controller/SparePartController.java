package com.supcon.mes.module_wxgd.controller;

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
import com.supcon.mes.middleware.model.event.BaseEvent;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_wxgd.IntentRouter;
import com.supcon.mes.module_wxgd.model.api.SparePartAPI;
import com.supcon.mes.middleware.model.bean.SparePartEntity;
import com.supcon.mes.module_wxgd.model.bean.SparePartListEntity;
import com.supcon.mes.middleware.model.bean.WXGDEntity;
import com.supcon.mes.module_wxgd.model.contract.SparePartContract;
import com.supcon.mes.module_wxgd.model.event.ListEvent;
import com.supcon.mes.module_wxgd.presenter.SparePartPresenter;
import com.supcon.mes.module_wxgd.ui.adapter.SparePartAdapter;

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

    //    private CustomListWidget<SparePartEntity> mCustomListWidget;
    private long id = -1;
    private ArrayList<SparePartEntity> mSparePartOldEntities = new ArrayList<>();
    private List<SparePartEntity> mSparePartEntities = new ArrayList<>();
    private boolean editable;
    private WXGDEntity mWXGDEntity;

    @BindByTag("sparePartListWidget")
    CustomListWidget<SparePartEntity> mCustomListWidget;
    private boolean iswarn;

    public SparePartController(View rootView) {
        super(rootView);
    }

    @Override
    public void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        mWXGDEntity = (WXGDEntity) ((Activity) context).getIntent().getSerializableExtra(Constant.IntentKey.WXGD_ENTITY);
        iswarn = ((Activity) context).getIntent().getBooleanExtra(Constant.IntentKey.ISWARN, false);
        if (mWXGDEntity != null) {
            this.id = mWXGDEntity.id;
        }
    }

    @Override
    public void initView() {
        super.initView();
        mCustomListWidget.setAdapter(new SparePartAdapter(context, false));
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
                        bundle.putString(Constant.IntentKey.SPARE_PART_ENTITIES, mSparePartEntities.toString());
                        bundle.putBoolean(Constant.IntentKey.IS_EDITABLE, editable);
                        bundle.putBoolean(Constant.IntentKey.ISWARN, iswarn);
                        bundle.putBoolean(Constant.IntentKey.IS_ADD, false);
                        bundle.putLong(Constant.IntentKey.REPAIR_SUM, mWXGDEntity.repairSum != null ? mWXGDEntity.repairSum : 1);
                        bundle.putString(Constant.IntentKey.TABLE_STATUS, mWXGDEntity.getPending().taskDescription);
                        bundle.putString(Constant.IntentKey.TABLE_ACTION, mWXGDEntity.pending.openUrl);
                        bundle.putLong(Constant.IntentKey.LIST_ID, id);
                        bundle.putLong(Constant.IntentKey.EAM_ID, mWXGDEntity.eamID.id != null ? mWXGDEntity.eamID.id : -1);
                        bundle.putSerializable(Constant.IntentKey.WXGD_WARN_ENTITIES, mSparePartOldEntities);
                        IntentRouter.go(context, Constant.Router.WXGD_SPARE_PART_LIST, bundle);
                        break;
                    default:
                        break;
                }
//                    if (mSparePartEntities != null) {
//                        bundle.putString(Constant.IntentKey.SPARE_PART_ENTITIES, mSparePartEntities.toString());
//                        bundle.putBoolean(Constant.IntentKey.IS_EDITABLE, editable);
//                        IntentRouter.go(mCustomListWidget.getContext(), Constant.Router.WXGD_SPARE_PART_LIST, bundle);
//                    }

            }
        });
    }

    @Override
    public void listSparePartListSuccess(SparePartListEntity entity) {
        mSparePartEntities = entity.result;
        mSparePartEntities.addAll(mSparePartOldEntities);
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
        if (mCustomListWidget != null) {
            mCustomListWidget.setData(entity.result);
            if (editable) {
                mCustomListWidget.setShowText("编辑 (" + entity.result.size() + ")");
            } else {
                mCustomListWidget.setShowText("查看 (" + entity.result.size() + ")");
            }
        }
        EventBus.getDefault().post(new ListEvent("sparePart", mSparePartEntities));
    }

    @Override
    public void listSparePartListFailed(String errorMsg) {
        LogUtil.e("SparePartController listSparePartListFailed:" + errorMsg);
    }

    public void setCustomListWidget(CustomListWidget<SparePartEntity> customListWidget) {
        this.mCustomListWidget = customListWidget;
    }

    @Override
    public void initData() {
        super.initData();
        presenterRouter.create(SparePartAPI.class).listSparePartList(id);
    }

    public void setWxgdEntity(WXGDEntity mWxgdEntity) {
        this.mWXGDEntity = mWxgdEntity;
        this.id = mWxgdEntity.id;
        presenterRouter.create(SparePartAPI.class).listSparePartList(id);
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
        if (mCustomListWidget != null) {
//            mCustomListWidget.setData(mSparePartEntities);
//            mCustomListWidget.setTotal(mSparePartEntities.size());
            mCustomListWidget.setData(mSparePartEntities);
            if (editable) {
                mCustomListWidget.setShowText("编辑 (" + mSparePartEntities.size() + ")");
            } else {
                mCustomListWidget.setShowText("查看 (" + mSparePartEntities.size() + ")");
            }
        }

    }

    public void updateOldSparePart(ArrayList<SparePartEntity> mSparePartOldEntities) {
        if (mSparePartOldEntities == null)
            return;
        this.mSparePartOldEntities = mSparePartOldEntities;
    }

    public void setEditable(boolean isEditable) {
        this.editable = isEditable;
    }

    public void clear() {
        mCustomListWidget.clear();
    }

}
