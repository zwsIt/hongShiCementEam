package com.supcon.mes.module_yhgl.ui.adapter;

import android.content.Context;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.middleware.model.bean.YHEntity;
import com.supcon.mes.middleware.util.HtmlParser;
import com.supcon.mes.middleware.util.HtmlTagHandler;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_yhgl.R;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/8/13
 * ------------- Description -------------
 */
public class YHGLStatisticsAdapter extends BaseListDataRecyclerViewAdapter<YHEntity> {
    public YHGLStatisticsAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<YHEntity> getViewHolder(int viewType) {
        return new WorkViewHolder(context);
    }

    class WorkViewHolder extends BaseRecyclerViewHolder<YHEntity> {
        @BindByTag("statisticsPosition")
        TextView statisticsPosition;
        @BindByTag("statisticsEam")
        TextView statisticsEam;
        @BindByTag("statisticsContent")
        TextView statisticsContent;
        @BindByTag("statisticsStaff")
        TextView statisticsStaff;
        @BindByTag("statisticsTime")
        TextView statisticsTime;

        public WorkViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_yh_statistics;
        }

        @Override
        protected void update(YHEntity data) {
            statisticsPosition.setText(String.valueOf(getAdapterPosition() + 1));

            String eam = String.format(context.getString(R.string.device_style10), Util.strFormat(data.getEamID().name)
                    , Util.strFormat(data.getEamID().code));
            statisticsEam.setText(HtmlParser.buildSpannedText(eam, new HtmlTagHandler()));
            statisticsContent.setText(Util.strFormat2(data.describe));
            statisticsStaff.setText(data.findStaffID != null ? data.findStaffID.name : "");
            statisticsTime.setText(data.findTime != 0 ? DateUtil.dateFormat(data.findTime, "yyyy-MM-dd") : "");
        }

    }
}
