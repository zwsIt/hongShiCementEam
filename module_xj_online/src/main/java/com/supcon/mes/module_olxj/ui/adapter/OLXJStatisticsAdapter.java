package com.supcon.mes.module_olxj.ui.adapter;

import android.content.Context;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_olxj.R;
import com.supcon.mes.module_olxj.model.bean.OLXJStatisticsEntity;

public class OLXJStatisticsAdapter extends BaseListDataRecyclerViewAdapter<OLXJStatisticsEntity> {
    public OLXJStatisticsAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<OLXJStatisticsEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<OLXJStatisticsEntity> {

        @BindByTag("statisticsPosition")
        TextView statisticsPosition;
        @BindByTag("statisticsDepot")
        TextView statisticsDepot;
        @BindByTag("statisticsStaff")
        TextView statisticsStaff;
        @BindByTag("statisticsUndetected")
        TextView statisticsUndetected;
        @BindByTag("statisticsUnseasonal")
        TextView statisticsUnseasonal;
        @BindByTag("statisticsManualCheckIn")
        TextView statisticsManualCheckIn;
        @BindByTag("statisticsHiddenDanger")
        TextView statisticsHiddenDanger;
        @BindByTag("statisticsRunRate")
        TextView statisticsRunRate;
        @BindByTag("statisticsWorkOrder")
        TextView statisticsWorkOrder;

        public ViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_xj_statistics;
        }

        @Override
        protected void update(OLXJStatisticsEntity data) {
            statisticsPosition.setText(String.valueOf(getAdapterPosition() + 1));
            statisticsDepot.setText(Util.strFormat2(data.deptName));
            statisticsStaff.setText(Util.strFormat2(data.staffName));
            statisticsUndetected.setText(String.format(context.getResources().getString(R.string.device_style6), "巡检次数：", Util.strFormat2(data.xjcs)));
            statisticsUnseasonal.setText(String.format(context.getResources().getString(R.string.device_style6), "巡检用时：", Util.strFormat2(data.xjys)));
            statisticsManualCheckIn.setText(String.format(context.getResources().getString(R.string.device_style6), "手工签到率：", Util.strFormat2(data.qdl)));
            statisticsHiddenDanger.setText(String.format(context.getResources().getString(R.string.device_style6), "隐患数量：", Util.strFormat2(data.yhQty)));
            statisticsRunRate.setText(String.format(context.getResources().getString(R.string.device_style6), "班运转率：", Util.strFormat2(data.bzyzl)));
            statisticsWorkOrder.setText(String.format(context.getResources().getString(R.string.device_style6), "工单：", Util.strFormat2(data.gdInfo)));
        }
    }
}
