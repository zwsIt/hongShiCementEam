package com.supcon.mes.module_main.ui.adaper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_main.IntentRouter;
import com.supcon.mes.module_main.R;
import com.supcon.mes.module_main.model.bean.WarnDailyWorkEntity;

import java.util.concurrent.TimeUnit;

/**
 * Created by zhangwenshuai on 2020/6/2
 * Email:zhangwenshuai1@supcom.com
 * 预警提醒工作Adapter
 */
public class WarnWorkAdapter extends BaseListDataRecyclerViewAdapter<WarnDailyWorkEntity> {
    public WarnWorkAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<WarnDailyWorkEntity> getViewHolder(int viewType) {
        return new ContentViewHolder(context);
    }

    class ContentViewHolder extends BaseRecyclerViewHolder<WarnDailyWorkEntity> {

        @BindByTag("eam")
        CustomTextView eam;
        @BindByTag("content")
        CustomTextView content;
        @BindByTag("moreTvLl")
        LinearLayout moreTvLl;

        public ContentViewHolder(Context context) {
            super(context);
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(itemView)
                    .subscribe(o -> {
                        WarnDailyWorkEntity warnDailyWorkEntity = getItem(getAdapterPosition());
                        if (warnDailyWorkEntity.dataId == null){// 日常润滑预警
                            IntentRouter.go(context, Constant.Router.WARN_PLAN_LUBRICATION_NEW);
                        }else { // 预警提醒
                            if (warnDailyWorkEntity.peroidType == null) {
                                ToastUtils.show(context, "未查询到当前单据周期类型!");
                                return;
                            }
                            Bundle bundle = new Bundle();
                            bundle.putLong(Constant.IntentKey.WARN_ID, warnDailyWorkEntity.dataId);
                            bundle.putString(Constant.IntentKey.PROPERTY, warnDailyWorkEntity.peroidType.id);
                            if (Constant.WarnType.LUBRICATION_WARN.equals(warnDailyWorkEntity.sourceType)) {
                                IntentRouter.go(context, Constant.Router.LUBRICATION_EARLY_WARN, bundle);
                            } else if (Constant.WarnType.SPARE_PART_WARN.equals(warnDailyWorkEntity.sourceType)) {
                                IntentRouter.go(context, Constant.Router.SPARE_EARLY_WARN, bundle);
                            } else if (Constant.WarnType.MAINTENANCE_WARN.equals(warnDailyWorkEntity.sourceType)) {
                                IntentRouter.go(context, Constant.Router.MAINTENANCE_EARLY_WARN, bundle);
                            }
                        }
//                        onItemChildViewClick(itemView,getAdapterPosition(),warnDailyWorkEntity);
                    });
            RxView.clicks(moreTvLl)
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe(o -> IntentRouter.go(context, Constant.Router.WARN_PENDING_LIST));
        }

        @Override
        protected int layoutId() {
            return R.layout.main_item_warn;
        }

        @Override
        protected void initView() {
            super.initView();
        }

        @Override
        protected void update(WarnDailyWorkEntity data) {
            if (data.dataId == null){
                eam.setVisibility(View.GONE);
            }else {
                eam.setVisibility(View.VISIBLE);
            }
            eam.setContent(data.eamName +"("+data.eamCode+")");
            content.setContent(data.sourceType + "：" + data.content);
            if ("MainActivity".equals(context.getClass().getSimpleName()) && (getAdapterPosition() == getItemCount() - 1)) {
                moreTvLl.setVisibility(View.VISIBLE);
            }
        }
    }
}
