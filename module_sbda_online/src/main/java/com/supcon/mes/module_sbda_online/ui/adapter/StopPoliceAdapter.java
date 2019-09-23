package com.supcon.mes.module_sbda_online.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomFilterView;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_sbda_online.R;
import com.supcon.mes.module_sbda_online.model.bean.StopPoliceEntity;

import java.text.SimpleDateFormat;

import static com.supcon.mes.middleware.constant.Constant.BAPQuery.STOP_POLICE_STOP_EXPLAIN;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.STOP_POLICE_STOP_REASON;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.STOP_POLICE_STOP_TYPE;

public class StopPoliceAdapter extends BaseListDataRecyclerViewAdapter<StopPoliceEntity> {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    public StopPoliceAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<StopPoliceEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<StopPoliceEntity> {

        @BindByTag("itemStopPoliceName")
        CustomTextView itemStopPoliceName;
        @BindByTag("itemStopPoliceStartTime")
        CustomTextView itemStopPoliceStartTime;
        @BindByTag("itemStopPoliceCloseTime")
        CustomTextView itemStopPoliceCloseTime;
        @BindByTag("itemStopPoliceTotalTime")
        CustomTextView itemStopPoliceTotalTime;
        @BindByTag("itemStopPoliceStopType")
        CustomTextView itemStopPoliceStopType;
        @BindByTag("itemStopPoliceStopReason")
        CustomTextView itemStopPoliceStopReason;
        @BindByTag("itemStopPoliceStopExplain")
        CustomEditText itemStopPoliceStopExplain;
    
    
        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_stop_police;
        }

        @SuppressLint("StringFormatInvalid")
        @Override
        protected void update(StopPoliceEntity stopPoliceEntity) {
            itemStopPoliceName.setContent(Util.strFormat(stopPoliceEntity.getEamType().name));
            itemStopPoliceStartTime.setContent(stopPoliceEntity.openTime != null ? dateFormat.format(stopPoliceEntity.openTime) : "--");
            itemStopPoliceCloseTime.setContent(stopPoliceEntity.closedTime != null ? dateFormat.format(stopPoliceEntity.closedTime) : "--");
            itemStopPoliceTotalTime.setContent(Util.big2(stopPoliceEntity.totalHour));
            
            itemView.setOnClickListener(v -> onItemChildViewClickListener.onItemChildViewClick(itemView, getAdapterPosition(), 0, stopPoliceEntity));
            if (stopPoliceEntity.recordId != null && stopPoliceEntity.recordId.closedType != null) {
                itemStopPoliceStopType.setContent(stopPoliceEntity.recordId.closedType.value);
            } else {
                itemStopPoliceStopType.setContent(null);
            }
            if (stopPoliceEntity.recordId != null && stopPoliceEntity.recordId.closedReason != null) {
                itemStopPoliceStopReason.setContent(stopPoliceEntity.recordId.closedReason.value);
            } else {
                itemStopPoliceStopReason.setContent(null);
            }
            if (stopPoliceEntity.recordId != null) {
                itemStopPoliceStopExplain.setContent(stopPoliceEntity.recordId.reason);
            } else {
                itemStopPoliceStopExplain.setContent(null);
            }
        }
    
    }
}
